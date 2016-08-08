package org.runbuddy.tomahawk_android.fragments;

import android.os.Bundle;
import android.view.View;

import org.runbuddy.libtomahawk.collection.Collection;
import org.runbuddy.libtomahawk.collection.CollectionManager;
import org.runbuddy.libtomahawk.collection.DbCollection;
import org.runbuddy.libtomahawk.collection.UserCollection;
import org.tomahawk.tomahawk_android.R;

/**
 * Created by Jonney Chou on 2016/7/26.
 */
public class MylocalPlayListFragment extends PagerFragment {


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().containsKey(TomahawkFragment.COLLECTION_ID)) {
            String collectionId = getArguments().getString(TomahawkFragment.COLLECTION_ID);
            Collection collection = CollectionManager.get().getCollection(collectionId);
            if (collection == null) {
                getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
            getActivity().setTitle("testing name");
            if (collection instanceof UserCollection) {
                showContentHeader(R.drawable.drawer_background);
            } else if (collection instanceof DbCollection) {
                showContentHeader(((DbCollection) collection).getIconBackgroundPath());
            }
        } else {
            throw new RuntimeException("No collection-id provided to CollectionPagerFragment");
        }


    }


}
