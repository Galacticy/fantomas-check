
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

    public void setSongList(List<Song> songList) {
        mSongList.clear();
        mSongList.addAll(songList);
        notifyDataSetChanged();
    }

    public void addSongs(List<Song> songs) {
        mSongList.addAll(songs);
        notifyDataSetChanged();
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        holder.bindHolder(mSongList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {

        private TextView mItemTextView;
        private TextView mMusicNameTextView;
        private TextView mMusicInfoTextView;
        private ImageView mMoreInfoImageView;

        public MusicViewHolder(View itemView) {
            super(itemView);
            mItemTextView = itemView.findViewById(R.id.item_text_view);