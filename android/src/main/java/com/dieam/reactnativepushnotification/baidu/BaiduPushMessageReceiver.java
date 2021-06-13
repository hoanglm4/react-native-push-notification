package com.dieam.reactnativepushnotification.baidu;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.dieam.reactnativepushnotification.modules.RNPushNotification;
import com.dieam.reactnativepushnotification.modules.RNPushNotificationHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 error_code	description
0	Bind successfully
10001	The current network is unavailable, please check the network
10002	Service is unavailable, connection to server failed
10003	Service unavailable, 503 error
10101	The application integration method is wrong, please check the declarations and permissions
20001	unknown mistake
30600	Service internal error
30601	Illegal function request, please check the content of your request
30602	The request parameter is wrong, please check your parameter
30603	Illegal construction of the request, server verification failed
30605	The requested data does not exist on the server
30608	The binding relationship does not exist or was not found
30609	The number of devices bound to a Baidu account exceeds the limit (multiple devices log in to the same Baidu account)
30612	Baidu account is banned when the application is bound, and whitelist authorization is required
 */

public class BaiduPushMessageReceiver extends PushMessageReceiver {
    public static final String TAG = BaiduPushMessageReceiver.class
            .getSimpleName();

    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        final Bundle bundle = new Bundle();
        bundle.putInt("errorCode", errorCode);
        bundle.putString("appId", appid);
        bundle.putString("userId", userId);
        bundle.putString("channelId", channelId);
        bundle.putString("requestId", requestId);

        sendEvent(context, bundle, RNPushNotification.ACTION_BAIDU_BIND);
    }

    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "onMessage=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);
        updateContent(context, messageString);

        final Bundle bundle = new Bundle();
        Bundle dataBundle = new Bundle();

        if (!TextUtils.isEmpty(customContentString)) {
            try {
                JSONObject customJson = new JSONObject(customContentString);
                JSONArray keys = customJson.names();
                for (int i = 0; i < keys.length(); i++) {
                    String key = keys.getString(i);
                    String value = customJson.getString(key);
                    dataBundle.putString(key, value);
                }
                bundle.putParcelable("data", dataBundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        handleRemotePushNotification(context, bundle, false);
    }

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);

        updateContent(context, notifyString);

        final Bundle bundle = new Bundle();
        Bundle dataBundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", description);

        if (!TextUtils.isEmpty(customContentString)) {
            try {
                JSONObject customJson = new JSONObject(customContentString);
                JSONArray keys = customJson.names();
                for (int i = 0; i < keys.length(); i++) {
                    String key = keys.getString(i);
                    String value = customJson.getString(key);
                    dataBundle.putString(key, value);
                }
                bundle.putParcelable("data", dataBundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        handleRemotePushNotification(context, bundle, false);
    }

    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "onNotificationClicked title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        updateContent(context, notifyString);

        final Bundle bundle = new Bundle();
        Bundle dataBundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", description);

        if (!TextUtils.isEmpty(customContentString)) {
            try {
                JSONObject customJson = new JSONObject(customContentString);
                JSONArray keys = customJson.names();
                for (int i = 0; i < keys.length(); i++) {
                    String key = keys.getString(i);
                    String value = customJson.getString(key);
                    dataBundle.putString(key, value);
                }
                bundle.putParcelable("data", dataBundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.invokeApp(context, bundle);
    }

    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> successTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " successTags=" + successTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        updateContent(context, responseString);
    }

    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> successTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " successTags=" + successTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        updateContent(context, responseString);
    }

    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            Log.d(TAG, "解绑成功");
        }
        updateContent(context, responseString);
    }

    private void updateContent(Context context, String content) {
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;
        Log.d(TAG, "updateContent: " + logText);
    }

    private void handleRemotePushNotification(Context context, Bundle bundle, boolean userInteraction) {
        // If notification ID is not provided by the user for push notification, generate one at random
        if (bundle.getString("id") == null) {
            SecureRandom randomNumberGenerator = new SecureRandom();
            bundle.putString("id", String.valueOf(randomNumberGenerator.nextInt()));
        }

        Application applicationContext = (Application) context.getApplicationContext();

        RNPushNotificationHelper pushNotificationHelper = new RNPushNotificationHelper(applicationContext);

        boolean isForeground = pushNotificationHelper.isApplicationInForeground();

        bundle.putBoolean("foreground", isForeground);
        bundle.putBoolean("userInteraction", userInteraction);
        bundle.putBoolean("baidu", true);
        this.sendEvent(context, bundle, RNPushNotification.ACTION_BAIDU_PUSH_NOTIFICATION);
    }

    private void invokeApp(Context context, Bundle bundle) {
        bundle.putBoolean("foreground", true);
        bundle.putBoolean("userInteraction", true);
        bundle.putBoolean("baidu", true);
        RNPushNotificationHelper helper = new RNPushNotificationHelper((Application) context.getApplicationContext());
        helper.invokeApp(bundle);
    }

    private void sendEvent(Context context, Bundle bundle, String actionName) {
        Intent intent = new Intent();
        intent.setAction(actionName);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }
}
