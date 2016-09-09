package org.runbuddy.tomahawk_android.fragments.Category_page.presenter.impl;

import org.runbuddy.tomahawk_android.fragments.Category_page.model.ICategoryModel;
import org.runbuddy.tomahawk_android.fragments.Category_page.model.impl.CategoryModelImpl;
import org.runbuddy.tomahawk_android.fragments.Category_page.presenter.ICategoryPresenter;
import org.runbuddy.tomahawk_android.fragments.Category_page.ui.view.ICategoryView;




/**
 * Created by _SOLID
 * Date:2016/5/17
 * Time:10:33
 */
public class CategoryPresenterImpl implements ICategoryPresenter {

    ICategoryView mIHomeView;
    ICategoryModel mIHomeModel;

    public CategoryPresenterImpl(ICategoryView homeView) {
        mIHomeView = homeView;
        mIHomeModel = new CategoryModelImpl();
    }



    @Override
    public void getAdapterData() {
        mIHomeView.showLoading();
        mIHomeView.setPagerAdapterData(mIHomeModel.loadCateGory());
        mIHomeView.hideLoading();
    }
}
