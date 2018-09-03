package com.codeswingstudios.rabatt;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHandler {

    private Activity activity;
    private int requestCode;
    private PermissionListener permissionListener;

    public void requestPermissions(Activity activity, @NonNull String[] Permissions, int requestCode, PermissionListener permissionListener ){
        this.activity = activity;
        this.requestCode = requestCode;
        this.permissionListener = permissionListener;
        if(!needRuntimePermissions()){
            permissionListener.OnSuccess();
            return;
        }
        requestUngrantedPermissions(Permissions, requestCode);
    }

    public boolean needRuntimePermissions(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private void requestUngrantedPermissions(String[] permissions, int requestCode) {
        String[] ungrantedPermissions = findUnGrantedPermissions(permissions);
        if (ungrantedPermissions.length == 0) {
            permissionListener.OnSuccess();
            return;
        }
        ActivityCompat.requestPermissions(activity, ungrantedPermissions, requestCode);
    }

    private String[] findUnGrantedPermissions(String[] Permissions) {
        List<String> unGrantedPermission = new ArrayList<>();
        for (String permission : Permissions) {
            if (!isPermissionGranted(permission)) {
                unGrantedPermission.add(permission);
            }
        }
        return unGrantedPermission.toArray(new String[0]);
    }

    private boolean isPermissionGranted(String permission) {
        return (ActivityCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == requestCode) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permissionListener.OnFailed();
                        return;
                    }
                }
                permissionListener.OnSuccess();
            } else {
                permissionListener.OnFailed();
            }
        }
    }



    public interface PermissionListener{
        void OnSuccess();

        void OnFailed();
    }




}
