package org.runbuddy.tomahawk_android.adapters.tai;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import org.runbuddy.tomahawk_android.model.domain.MVDetailBean;
import org.runbuddy.tomahawk_android.activities.tai.MVDetailActivity;

import org.tomahawk.tomahawk_android.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/5/20
 * YinYueTai
 */
public class RelativeMvRecycleAdapter extends RecyclerView.Adapter<RelativeMvRecycleAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.artistName)
        TextView artistName;
        @Bind(R.id.play_count)
        TextView playCount;
        @Bind(R.id.poster_img)
        ImageView posterImg;
        @Bind(R.id.item_root)
        LinearLayout itemRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Activity activity;
    private List<MVDetailBean.RelatedVideosBean> relatedVideosBeen;

    public RelativeMvRecycleAdapter(Activity activity, List<MVDetailBean.RelatedVideosBean> relatedVideosBeen) {
        this.activity = activity;
        this.relatedVideosBeen = relatedVideosBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relative_mv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MVDetailBean.RelatedVideosBean videosBean = relatedVideosBeen.get(position);
        holder.artistName.setText(videosBean.getArtistName());
        holder.playCount.setText("播放次数：" + videosBean.getTotalViews());
        holder.title.setText(videosBean.getTitle());
        Glide.with(activity).load(videosBean.getPosterPic()).placeholder(R.drawable.empty_logo).centerCrop().into(holder.posterImg);
        holder.itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, MVDetailActivity.class);
                intent.putExtra("id", videosBean.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return relatedVideosBeen.size();
    }
}
