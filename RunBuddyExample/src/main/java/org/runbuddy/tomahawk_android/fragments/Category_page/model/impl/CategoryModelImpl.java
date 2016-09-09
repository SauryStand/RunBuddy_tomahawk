package org.runbuddy.tomahawk_android.fragments.Category_page.model.impl;

import org.runbuddy.tomahawk_android.fragments.Category_page.constant.Apis;
import org.runbuddy.tomahawk_android.fragments.Category_page.model.ICategoryModel;

import java.util.List;



/**
 * Created by _SOLID
 * Date:2016/5/17
 * Time:10:31
 */
public class CategoryModelImpl implements ICategoryModel {
    @Override
    public List<String> loadCateGory() {
        return Apis.getGanHuoCateGory();
    }
}
