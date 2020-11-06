
package io.innofang.musicplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Author: Inno Fang
 * Time: 2018/1/8 20:15
 * Description:
 */


public abstract class FragmentContainerActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    protected abstract int getLayoutResId();

    protected abstract int getFragmentContainerId();

    @Override