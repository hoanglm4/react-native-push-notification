package com.dieam.reactnativepushnotification.helpers;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GoogleAPIProvider {
    private boolean isGmsEnabled;
    private ReactApplicationContext context;

    public GoogleAPIProvider(ReactApplicationContext context) {
        this.context = context;
        this.initializeGMSStatus();
    }

    private void initializeGMSStatus() {
        try {
            ApplicationInfo gmsInfo = context.getPackageManager().getApplicationInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0);
            isGmsEnabled = gmsInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            isGmsEnabled = false;
        }
    }

    public GooglePlayServicesStatus getGooglePlayServicesStatus() {
        if (!isGmsEnabled) {
            return GooglePlayServicesStatus.GMS_DISABLED;
        }

        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            return GooglePlayServicesStatus.GMS_NEED_UPDATE;
        }
        return GooglePlayServicesStatus.AVAILABLE;
    }
}
