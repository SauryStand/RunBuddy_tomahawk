package org.runbuddy.tomahawk_android.demo_zhihu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.runbuddy.tomahawk_android.demo_zhihu.entity.Article;
import org.runbuddy.tomahawk_android.demo_zhihu.listener.OnItemClickListener;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class SecondHomeAdapter extends RecyclerView.Adapter<SecondHomeAdapter.MyViewHolder>{

    private OnItemClickListener mClickListener;
    private LayoutInflater mInflater;
    private List<Article> mItems = new ArrayList<>();

    public SecondHomeAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<Article> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_ganhuo, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (mClickListener != null) {
                    mClickListener.onItemClick(position, v, holder);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Article item = mItems.get(position);
        holder.tv_Desc.setText(item.getTitle());
       //holder.tvContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Article getItem(int position) {
        return mItems.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Desc, tvContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_Desc = (TextView) itemView.findViewById(R.id.tv_desc);
            //tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
