package org.runbuddy.tomahawk_android.presenter;

import org.runbuddy.tomahawk_android.model.domain.VideoBean;

import java.util.ArrayList;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/6/7
 * YinYueTai
 */
public interface HomePageFragmentContract {
    interface Presenter extends BasePresenter {

    }
    interface View extends BaseView<Presenter> {
        void setData(ArrayList<VideoBean> dataList);
        void setError(String msg);
    }
}
