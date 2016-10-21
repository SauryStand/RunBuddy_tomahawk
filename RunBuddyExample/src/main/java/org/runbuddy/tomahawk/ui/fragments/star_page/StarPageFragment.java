package org.runbuddy.tomahawk.ui.fragments.star_page;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.runbuddy.tomahawk.R;
import org.runbuddy.tomahawk.ui.fragments.star_page.widgets.DetailActivity;
import org.runbuddy.tomahawk.ui.fragments.star_page.widgets.RecyclerViewAdapter;
import org.runbuddy.tomahawk.ui.fragments.star_page.widgets.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny Chow on 2016/8/21.
 */
public class StarPageFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewAdapter.OnItemClickListener {

    private static final int PAGE_SIZE = 15;
    public static final String AVATAR_URL = "http://lorempixel.com/200/200/people/1/";
    protected Thread mThread;
    private static List<ViewModel> items = new ArrayList<>();

    static {
        for (int i = 0; i <= 15; i++) {
            //items.add(new ViewModel("User:" + i, "http://www.voyager2511.top/temp_file/images/" + i + ".jpg"));
            items.add(new ViewModel("test:" + i, "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/" + PAGE_SIZE + "/" + i));
            //这里需要换个图片才行
        }
    }

    private DrawerLayout drawerLayout;
    private View content;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_running_mv_page);
        View view = inflater.inflate(R.layout.star_page_main, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //initRecyclerView(view);
        setupDrawerLayout(view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setRecyclerAdapter(recyclerView);
        }
        mThread = new Thread(runnable);
        mThread.start();
        return view;
    }

    private void catchGankImage() {
        try {

            JSONArray jsonArray = new JSONArray("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/15/2");
            int temp = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                temp++;
            }
            //JSONObject jsonObject = new JSONObject("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/15/2");
            //String temp = jsonObject.get("who").toString();
            Toast.makeText(getActivity(), "----->>" + temp + "", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void testingImage() {

    }
    /**************************************/
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            URLServer server = new URLServer(mHandler);
            server.getImage();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            switch(msg.what) {
                case 0:
                    execute(msg);
                    break;
                case 1:
                    break;
            }
        }

        private void execute(Message msg) {
            try {
                JSONObject resultJson = new JSONObject(msg.obj.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    /***************************************/


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
