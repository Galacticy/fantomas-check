
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