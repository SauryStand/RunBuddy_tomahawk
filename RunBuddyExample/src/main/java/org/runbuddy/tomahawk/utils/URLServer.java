package org.runbuddy.tomahawk.utils;

import java.util.logging.Handler;

/**
 * Created by Jonhnny Chou on 2017/1/10.
 */

public class URLServer {

    public static final String SERVER_ADDRESS = "http://115.28.71.229:8073";

    private Handler mHandler;

    public URLServer(){
    }

    public URLServer(Handler handler) {
        mHandler = handler;
    }
    
    //// TODO: 2017/1/10  

}
