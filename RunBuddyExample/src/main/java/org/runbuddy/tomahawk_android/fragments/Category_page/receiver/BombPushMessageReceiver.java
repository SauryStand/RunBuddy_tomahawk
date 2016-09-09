package org.runbuddy.tomahawk_android.fragments.Category_page.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.bmob.push.PushConstants;
import ren.solid.library.utils.Logger;

/**
 * Created by _SOLID
 * Date:2016/6/2
 * Time:15:41
 */
public class BombPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Logger.i(this, "客户端收到推送内容：" + intent.getStringExtra("msg"));
        }
    }
}
