
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