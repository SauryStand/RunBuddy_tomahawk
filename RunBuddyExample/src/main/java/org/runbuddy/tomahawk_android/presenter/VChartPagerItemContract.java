package org.runbuddy.tomahawk_android.presenter;

import org.runbuddy.tomahawk_android.model.domain.VChartPeriod;
import org.runbuddy.tomahawk_android.model.domain.VideoBean;

import java.util.List;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/6/7
 * YinYueTai
 */
public interface VChartPagerItemContract {
    interface Presenter extends BasePresenter {
        void getPeriod(String areaCode);
        void getDataByPeriod(String area, int dateCode);
    }
    interface View extends BaseView<Presenter>{
        void setAreaData(VChartPeriod vChartPeriod);
        void setVideoData(List<VideoBean> videosBeen);
        void setError(String msg);
    }
}
