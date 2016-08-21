package org.runbuddy.tomahawk_android.fragments.star_page;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.runbuddy.tomahawk_android.fragments.star_page.widgets.DetailActivity;
import org.runbuddy.tomahawk_android.fragments.star_page.widgets.RecyclerViewAdapter;
import org.runbuddy.tomahawk_android.fragments.star_page.widgets.ViewModel;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonney Chou on 2016/8/21.
 */
public class StarPageFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener,RecyclerViewAdapter.OnItemClickListener {


    public static final String AVATAR_URL = "http://lorempixel.com/200/200/people/1/";

    private static List<ViewModel> items = new ArrayList<>();

    static {
        for (int i = 1; i <= 20; i++) {
            items.add(new ViewModel("User:" + i, "http://www.voyager2511.top/temp_file/images/" + i + ".jpg"));//http://lorempixel.com/500/500/animals/
        }
    }

    private DrawerLayout drawerLayout;
    private View content;
    private RecyclerView recyclerView;
    private NavigationView navigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_localList);
        View view = inflater.inflate(R.layout.star_page_main, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //initRecyclerView(view);
        setupDrawerLayout(view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setRecyclerAdapter(recyclerView);
        }
        return view;
    }



    public void onEnterAnimationComplete() {
        //super.onEnterAnimationComplete();
        setRecyclerAdapter(recyclerView);
        recyclerView.scheduleLayoutAnimation();
    }


    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    }
    private void setRecyclerAdapter(RecyclerView recyclerView) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupDrawerLayout(View view) {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.star_drawer_layout);
        onEnterAnimationComplete();

    }

    @Override
    public void onItemClick(View view, ViewModel viewModel) {
        DetailActivity.navigate((AppCompatActivity) getActivity(), view.findViewById(R.id.image), viewModel);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
