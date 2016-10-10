/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2012, Christopher Reichert <creichert07@gmail.com>
 *   Copyright 2013, Enno Gottschalk <mrmaffen@googlemail.com>
 *
 *   Tomahawk is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Tomahawk is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Tomahawk. If not, see <http://www.gnu.org/licenses/>.
 */
package org.runbuddy.tomahawk.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.Trace;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
import org.runbuddy.tomahawk.R;
import org.runbuddy.tomahawk.services.PlaybackService;
import org.runbuddy.tomahawk.utils.TomahawkHttpSender;

import java.lang.ref.WeakReference;


/**
 * This class represents the Application core.
 */
@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://crash-stats.tomahawk-player.org:5984/acra-tomahawkandroid/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "reporter",
        formUriBasicAuthPassword = "unknackbar",
        excludeMatchingSharedPreferencesKeys = {".*_config$"},
        mode = ReportingInteractionMode.DIALOG,
        logcatArguments = {"-t", "2000", "-v", "time"},
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
        resDialogOkToast = R.string.crash_dialog_ok_toast)
public class TomahawkApp extends Application {

    private static final String TAG = TomahawkApp.class.getSimpleName();
    public final static String PLUGINNAME_HATCHET = "hatchet";
    public final static String PLUGINNAME_USERCOLLECTION = "usercollection";
    public final static String PLUGINNAME_SPOTIFY = "spotify";
    public final static String PLUGINNAME_DEEZER = "deezer";
    public final static String PLUGINNAME_BEATSMUSIC = "beatsmusic";
    public final static String PLUGINNAME_JAMENDO = "jamendo";
    public final static String PLUGINNAME_OFFICIALFM = "officialfm";
    public final static String PLUGINNAME_SOUNDCLOUD = "soundcloud";
    public final static String PLUGINNAME_GMUSIC = "gmusic";
    public final static String PLUGINNAME_AMZN = "amazon";
    private static Context sApplicationContext;

    /*************************************/
    private Context mContext = null;
    /**
     * 轨迹服务
     */
    private Trace trace = null;
    /**
     * 轨迹服务客户端
     */
    private LBSTraceClient client = null;
    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    private int serviceId = 126620;
    /**
     * entity标识
     */
    private String entityName = "myTrace";
    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;
    private MapView bmapView = null;
    private BaiduMap mBaiduMap = null;
    private TrackHandler mHandler = null;
    /**********************************************/


    @Override
    public void onCreate() {
        ACRA.init(this);
        ACRA.getErrorReporter().setReportSender(
                new TomahawkHttpSender(ACRA.getConfig().httpMethod(), ACRA.getConfig().reportType(),
                        null));

        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder().detectCustomSlowCalls().detectDiskReads()
                        .detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .setClassInstanceLimit(Class.forName(PlaybackService.class.getName()), 1)
                    .penaltyLog().build());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.toString());
        }

        super.onCreate();
        sApplicationContext = getApplicationContext();
        /*******************************************/
        mContext = getApplicationContext();
        SDKInitializer.initialize(this);
        // 初始化轨迹服务客户端
        client = new LBSTraceClient(mContext);
        // 初始化轨迹服务
        trace = new Trace(mContext, serviceId, entityName, traceType);
        // 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);
        mHandler = new TrackHandler(this);
        /****************************************/

    }

    public void initBmap(MapView bmapView) {
        this.bmapView = bmapView;
        this.mBaiduMap = bmapView.getMap();
        this.bmapView.showZoomControls(false);
    }

    static class TrackHandler extends Handler {
        WeakReference<TomahawkApp> trackApp;

        TrackHandler(TomahawkApp tomahawkApp) {
            trackApp = new WeakReference<TomahawkApp>(tomahawkApp);
        }

        @Override
        public void handleMessage(Message msg) {
            //Toast.makeText(trackApp.get().mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    }

    public Context getmContext() {
        return mContext;
    }

    public Trace getTrace() {
        return trace;
    }

    public LBSTraceClient getClient() {
        return client;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getEntityName() {
        return entityName;
    }

    public Handler getmHandler() {
        return mHandler;
    }

    public MapView getBmapView() {
        return bmapView;
    }

    public BaiduMap getmBaiduMap() {
        return mBaiduMap;
    }

    public static Context getContext() {
        return sApplicationContext;
    }

}
