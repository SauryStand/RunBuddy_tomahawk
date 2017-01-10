package org.runbuddy.tomahawk.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.runbuddy.tomahawk.R;
import org.runbuddy.tomahawk.entity.Article;
import org.runbuddy.tomahawk.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonney Chou on 2016/7/29.
 * unused
 */
public class TranningAdapter extends RecyclerView.Adapter<TranningAdapter.ViewHolder> {

    private List<org.runbuddy.tomahawk.entity.Article> mItems = new ArrayList<>();
    private LayoutInflater mInflater;

    private OnItemClickListener mCnMenuItemClickListener;

    public TranningAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_tranning_home, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (mCnMenuItemClickListener != null) {
                    mCnMenuItemClickListener.onItemClickListener(position, v, holder);//接口
                }
            }
        });
        return holder;
    }


    @Override
    public int getItemCount(){
        return mItems.size();
    }

    public Article getItem(int position) {
        return mItems.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mCnMenuItemClickListener = itemClickListener;
    }


    public void setDatas(List<Article> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article item = mItems.get(position);

        // 把每个图片视图设置不同的Transition名称, 防止在一个视图内有多个相同的名称, 在变换的时候造成混乱
        // Fragment支持多个View进行变换, 使用适配器时, 需要加以区分
        ViewCompat.setTransitionName(holder.img, String.valueOf(position) + "_image");

        holder.img.setImageResource(item.getImgRes());
        holder.tvTitle.setText(item.getTitle());
    }


    //这个内部类写法要研究下
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }


}
