package com.shachar_dev.maphandlerlibrary;

import android.graphics.Bitmap;

public interface MapSnapshotCallback {
    void onSnapshotReady(Bitmap bitmap);
}
