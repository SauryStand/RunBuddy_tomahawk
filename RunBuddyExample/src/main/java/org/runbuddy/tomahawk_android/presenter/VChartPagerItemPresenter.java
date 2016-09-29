package org.runbuddy.tomahawk_android.presenter;

import org.runbuddy.tomahawk_android.model.domain.VChartBean;
import org.runbuddy.tomahawk_android.model.domain.VChartPeriod;
import org.runbuddy.tomahawk_android.model.http.OkHttpManager;
import org.runbuddy.tomahawk_android.model.http.callback.StringCallBack;
import org.runbuddy.tomahawk_android.utils.tai.URLProviderUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import okhttp3.Call;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/6/7
 * YinYueTai
 */
public class VChartPagerItemPresenter implements VChartPagerItemContract.Presenter {

    private VChartPagerItemContract.View itemView;

    public VChartPagerItemPresenter(VChartPagerItemContract.View itemView) {
        this.itemView = itemView;
        itemView.setPresenter(this);
    }

    @Override
    public void getData(int offset, int size) {

    }

    @Override
    public void getPeriod(String areaCode) {
        OkHttpManager.getOkHttpManager().asyncGet(URLProviderUtil.getVChartPeriodUrl(areaCode), itemView, new StringCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                itemView.setError(e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                try {
                    VChartPeriod vChartPeriod = new Gson().fromJson(response, VChartPeriod.class);
                    itemView.setAreaData(vChartPeriod);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    itemView.setError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void getDataByPeriod(String area, int dateCode) {
        OkHttpManager.getOkHttpManager().asyncGet(URLProviderUtil.getVChartListUrl(area, dateCode), this, new StringCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                itemView.setError(e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                try {
                    VChartBean vChartBean = new Gson().fromJson(response, VChartBean.class);
                    itemView.setVideoData(vChartBean.getVideos());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    itemView.setError(e.getMessage());
                }
            }
        });
    }
}
