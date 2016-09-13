package org.runbuddy.tomahawk_android.ui.demo_zhihu.basic;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fragmentation.SupportFragment;

import org.tomahawk.tomahawk_android.R;

/**
 * Created by Administrator on 2016/8/8.
 */
public class BaseFragment extends SupportFragment {
    private static final String TAG = "Fragmentation";

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                        break;
                }
                return true;
            }
        });
    }
}
