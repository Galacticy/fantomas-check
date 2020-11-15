
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

