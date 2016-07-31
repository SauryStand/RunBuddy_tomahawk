package org.runbuddy.tomahawk_android.fragments;

import android.os.Bundle;
import android.view.View;

import org.tomahawk.tomahawk_android.R;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class TranningListFragment extends PagerFragment {

    public static final int MODE_HEADER_STATIC_SMALL = 8;



    /*
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.mytraining_fragment,container,false);
        return view;
    }*/

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showContentHeader(R.drawable.collection_header);

        int initialPage = -1;
        if (getArguments() != null) {
            if (getArguments().containsKey(TomahawkFragment.CONTAINER_FRAGMENT_PAGE)) {
                initialPage = getArguments().getInt(TomahawkFragment.CONTAINER_FRAGMENT_PAGE);
            }
        }
    }






}
