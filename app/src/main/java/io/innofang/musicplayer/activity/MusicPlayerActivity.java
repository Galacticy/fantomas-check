package io.innofang.musicplayer.activity;

import android.support.v4.app.Fragment;

import io.innofang.musicplayer.R;
import io.innofang.musicplayer.base.FragmentContainerActivity;
import io.innofang.musicplayer.fragment.PlayLocalMusicFragment;

/**
 * Author: Inno Fang
 * Time: 2018/1/8 20:18
 * Description:
 */


public class MusicPlayerActivity extends FragmentContainerActivity {
    @Override
    protected Fragment createFragment() {
        return PlayLocalMusicFragment.newInstance