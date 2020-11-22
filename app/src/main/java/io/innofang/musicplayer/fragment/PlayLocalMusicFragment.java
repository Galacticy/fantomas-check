
package io.innofang.musicplayer.fragment;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.innofang.musicplayer.R;
import io.innofang.musicplayer.adapter.MusicAdapter;
import io.innofang.musicplayer.bean.Song;
import io.innofang.musicplayer.utils.AudioUtils;
import io.innofang.musicplayer.utils.RequestPermissions;

/**
 * Author: Inno Fang
 * Time: 2018/1/8 20:19
 * Description:
 */


public class PlayLocalMusicFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "PlayLocalMusicFragment";

    private ImageView mSkipPrevImageView;
    private ImageView mSkipNextImageView;
    private ImageView mPlayOrPauseImageView;
    private ImageView mPlayModeImageView;
    private RecyclerView mMusicRecyclerView;
    private SeekBar mSeekBar;

    private MusicAdapter mMusicAdapter;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    // 保存查到的所有歌曲
    private List<Song> mSongList = new ArrayList<>();

    // 歌曲在 mSongList 的下标
    private int songIndex = -1;

    // 播放模式选择，队列播放，单曲循环，随机播放
    private final int PLAY_QUEUE = R.drawable.ic_queue;
    private final int REPEAT_ONE = R.drawable.ic_repeat_one;
    private final int PLAY_RANDOM = R.drawable.ic_random;

    // 保存现在的播放模式
    @DrawableRes
    private int playMode = PLAY_RANDOM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置 Fragment 可以设置标题栏
        setHasOptionsMenu(true);

        // 对媒体适配器进行初始化
        mMusicAdapter = new MusicAdapter(getContext(), mSongList);
    }

    /**
     * 用于获取当前 Fragment 的实例
     *
     * @return
     */
    public static PlayLocalMusicFragment newInstance() {

        Bundle args = new Bundle();

        PlayLocalMusicFragment fragment = new PlayLocalMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_local_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSkipPrevImageView = view.findViewById(R.id.skip_previous_image_view);
        mSkipNextImageView = view.findViewById(R.id.skip_next_image_view);
        mPlayOrPauseImageView = view.findViewById(R.id.play_or_pause_image_view);
        mPlayModeImageView = view.findViewById(R.id.play_mode_image_view);
        mMusicRecyclerView = view.findViewById(R.id.music_recycler_view);
        mSeekBar = view.findViewById(R.id.seek_bar);


        // 权限申请
        RequestPermissions.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                new RequestPermissions.OnPermissionsRequestListener() {
                    @Override
                    public void onGranted() {
                        // 同意权限后加载音乐列表并初始化事件
                        loadMusicList();
                        initEvent();
                    }

                    @Override
                    public void onDenied(List<String> deniedList) {
                        Toast.makeText(getActivity(), "拒绝权限将无法获取歌曲目录", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * 异步加载本地歌曲，UI 线程不做耗时操作
     */
    private void loadMusicList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Song> songs = AudioUtils.getAllSongs(getContext());
                // 不能在子线程更新 UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMusicAdapter.addSongs(songs);
                    }
                });
            }
        }).start();
    }

    /**
     * 初始化设置，及设置点击事件
     */
    private void initEvent() {
        mMusicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMusicRecyclerView.setAdapter(mMusicAdapter);
        mMusicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onClick(Song song) {
                playMusic(song);
            }
        });

        changePlayMode();

        mSeekBar.setOnSeekBarChangeListener(this);
        mSkipNextImageView.setOnClickListener(this);
        mSkipPrevImageView.setOnClickListener(this);
        mPlayOrPauseImageView.setOnClickListener(this);
        mPlayModeImageView.setOnClickListener(this);
    }

    /**
     * 播放歌曲方法
     *
     * @param song
     */
    private void playMusic(Song song) {
        try {
            // 重置 MediaPlayer 对象为刚刚创建的状态
            mMediaPlayer.reset();

            // 设置资源并置 MediaPlayer 为准备状态
//            File file = new File(song.getFileUrl());
            mMediaPlayer.setDataSource(song.getFileUrl());
            mMediaPlayer.prepare();

            // 保存当前歌曲下标
            songIndex = mSongList.indexOf(song);

            // 标题栏显示歌名，子标题显示 专辑-歌手
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.getSupportActionBar().setTitle(song.getTitle());
            activity.getSupportActionBar().setSubtitle(song.getSinger() + " - " + song.getAlbum());

            // 播放音乐
            mMediaPlayer.start();
            // 一旦播放音乐就是显示停止图标
            mPlayOrPauseImageView.setImageResource(R.drawable.ic_pause);

            // 设置进度条持续时间
            mSeekBar.setMax(song.getDuration());

            // 更新进度条
            new Thread(mRunnable).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_previous_image_view:
                // 如果不是播放状态，那么无法点击
                if (!mMediaPlayer.isPlaying()) return;

                // 对播放的歌进行越界检查并更新
                if (songIndex == 0) songIndex = mSongList.size() - 1;
                else songIndex--;

                // 播放
                playMusic(mSongList.get(songIndex));
                break;
            case R.id.skip_next_image_view:
                // 播放下一首歌曲
                playNextSong();
                break;
            case R.id.play_or_pause_image_view:
                if (mMediaPlayer.isPlaying()) {
                    mPlayOrPauseImageView.setImageResource(R.drawable.ic_play);
                    mMediaPlayer.pause();
                } else {
                    // 如果当前处于初始状态，没有播放歌曲，则根据播放模式来播放第一首歌
                    if (songIndex == -1 && !mSongList.isEmpty()) {