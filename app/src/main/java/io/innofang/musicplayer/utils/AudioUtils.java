package io.innofang.musicplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import io.innofang.musicplayer.bean.Song;

/**
 * Author: Inno Fang
 * Time: 2018/1/8 20:04
 * Description:
 *
 * 获取所有音乐文件
 */

public class AudioUtils {

    /**
     * 获取sd卡所有的音乐文件
     *
     * @return
     * @throws Exception
     */
    public static List<Song> getAllSongs(Context context) {

        List<Song> songs = null;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Au