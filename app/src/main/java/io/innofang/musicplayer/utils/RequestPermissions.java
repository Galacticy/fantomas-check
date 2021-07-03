package io.innofang.musicplayer.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Inno Fang
 * Time: 2018/1/21 14:44
 * Description:
 */


public class RequestPermissions {

    public static final int PERMISSIONS_REQUEST_CODE = 10;

    private static OnPermissionsRequestListener sListener;

    public static void requestPermissions(Activity activity, String[] permissions,
