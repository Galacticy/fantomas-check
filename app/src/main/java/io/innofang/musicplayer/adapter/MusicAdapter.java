
package io.innofang.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.innofang.musicplayer.R;
import io.innofang.musicplayer.bean.Song;

/**
 * Author: Inno Fang
 * Time: 2018/1/8 21:04
 * Description:
 */


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private Context mContext;
    private List<Song> mSongList;

    public MusicAdapter(Context context, List<Song> songList) {
        mContext = context;
        mSongList = songList;
    }

    public List<Song> getSongList() {
        return mSongList;
    }
