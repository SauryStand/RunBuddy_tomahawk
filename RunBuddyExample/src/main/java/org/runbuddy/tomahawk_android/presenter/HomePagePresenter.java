package org.runbuddy.tomahawk_android.presenter;

import android.util.Log;

import org.runbuddy.tomahawk_android.model.domain.VideoBean;
import org.runbuddy.tomahawk_android.model.http.OkHttpManager;
import org.runbuddy.tomahawk_android.model.http.callback.StringCallBack;
import org.runbuddy.tomahawk_android.utils.tai.URLProviderUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/6/7
 * YinYueTai
 */
public class HomePagePresenter implements HomePageFragmentContract.Presenter{
    private HomePageFragmentContract.View firstPageView;

    public HomePagePresenter(HomePageFragmentContract.View firstPageView) {
        this.firstPageView = firstPageView;
        firstPageView.setPresenter(this);
    }

    @Override
    public void getData(int offset, int size) {
        OkHttpManager.getOkHttpManager().asyncGet(URLProviderUtil.getMainPageUrl(offset, size), "", new StringCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                firstPageView.setError(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String response) {
                ArrayList<VideoBean> firstPageBeanList = new ArrayList<>();
                //创建一个JsonParser
                JsonParser parser = new JsonParser();
                //通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
                Log.e("response",response);
                JsonElement el;
                try {
                    el = parser.parse(response);
                    //把JsonElement对象转换成JsonArray
                    JsonArray jsonArray = null;
                    if (el.isJsonArray()) {
                        jsonArray = el.getAsJsonArray();
                    }
                    Iterator it = jsonArray.iterator();
                    if (it.hasNext()) {
                        while (it.hasNext()) {
                            JsonElement e = (JsonElement) it.next();
                            //JsonElement转换为JavaBean对象
                            VideoBean field = new Gson().fromJson(e, VideoBean.class);
                            firstPageBeanList.add(field);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    firstPageView.setError(e.getLocalizedMessage());
                }
                firstPageView.setData(firstPageBeanList);
            }
        });
    }
}
