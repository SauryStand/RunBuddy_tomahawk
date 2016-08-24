package ren.solid.library.http;

import android.widget.ImageView;

import ren.solid.library.http.request.ImageRequest;

/**
 * Created by _SOLID
 * Date:2016/5/14
 * Time:11:03
 */
public class HttpClientManager {

    public static void displayImage(ImageView iv, String url) {
        ImageRequest request = new ImageRequest.Builder().url(url).imgView(iv).create();
        ImageLoader.getProvider().loadImage(request);
    }
}
