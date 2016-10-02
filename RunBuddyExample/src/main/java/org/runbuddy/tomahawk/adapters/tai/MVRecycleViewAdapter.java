package org.runbuddy.tomahawk.adapters.tai;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.runbuddy.R;
import org.runbuddy.tomahawk.activities.tai.MVDetailActivity;
import org.runbuddy.tomahawk.model.domain.VideoBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/5/11
 * YinYueTai
 */
public class MVRecycleViewAdapter extends RecyclerView.Adapter<MVRecycleViewAdapter.ViewHolder> {

    private ArrayList<VideoBean> videoList = new ArrayList<>();
    private Activity activity;
    private RelativeLayout.LayoutParams layoutParams;

    public MVRecycleViewAdapter(ArrayList<VideoBean> videoList, Activity activity, int mWidth, int mHeight) {
        this.videoList = videoList;
        this.activity = activity;
        layoutParams = new RelativeLayout.LayoutParams(mWidth, mHeight);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mv_recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoBean videoBean = videoList.get(position);
        holder.posterImg.setLayoutParams(layoutParams);
        holder.itemTransbg.setLayoutParams(layoutParams);
        holder.name.setText(videoBean.getTitle());
        holder.author.setText(videoBean.getDescription());
        if (videoBean.isAd()){
            Glide.with(activity).load(videoBean.getThumbnailPic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.posterImg);
            holder.playCount.setText("");
        }else {
            Glide.with(activity).load(videoBean.getAlbumImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.posterImg);
            holder.playCount.setText("播放次数：" + videoBean.getTotalViews());
            holder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(activity, MVDetailActivity.class);
                    intent.putExtra("id", videoBean.getId());
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.empty_logo)
        ImageView emptyLogo;
        @Bind(R.id.poster_img)
        ImageView posterImg;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.author)
        TextView author;
        @Bind(R.id.play_count)
        TextView playCount;
        @Bind(R.id.item_transbg)
        ImageView itemTransbg;
        @Bind(R.id.item_root)
        RelativeLayout itemRoot;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
