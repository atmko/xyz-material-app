/*
 * Copyright (C) 2019 Aayat Mimiko
 */

package com.example.xyzreader.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.fragments.ArticleListFragment;
import com.example.xyzreader.models.ArticleData;
import com.example.xyzreader.utils.network_utils.NetworkFunctions;

import java.util.ArrayList;
import java.util.List;

public final class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder> {
    private final ArticleListFragment mFragment;
    private final OnListItemClickListener mOnListItemClickListener;
    private final List<ArticleData> mAdapterData;

    public ListAdapter(OnListItemClickListener clickListener) {
        mFragment = (ArticleListFragment) clickListener;
        mOnListItemClickListener = clickListener;
        mAdapterData = new ArrayList<>();
    }

    public interface OnListItemClickListener {
        void onItemClick(int position);
    }

    public class ListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        final ImageView thumbnail;
        final TextView title;
        final TextView subtitle;

        private ListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.article_title);
            subtitle = itemView.findViewById(R.id.article_subtitle);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mFragment.loadDetailFragment(thumbnail, position);

            mOnListItemClickListener.onItemClick(position);
        }
    }

    @NonNull
    @Override
    public ListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        int resourceId = R.layout.list_item_article;

        View view = layoutInflater.inflate(resourceId, viewGroup, false);

        return new ListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterViewHolder adapterViewHolder, int position) {
        Context context = adapterViewHolder.thumbnail.getContext();

        //get current ArticleData
        ArticleData currentArticleData = mAdapterData.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            adapterViewHolder.thumbnail.
                    setTransitionName(mFragment.getString
                            (R.string.list_to_detail_transition)+position);
        }

        adapterViewHolder.title.setText(currentArticleData.getmTitle());
        adapterViewHolder.subtitle.setText(currentArticleData.getmAuthor());

        //load image with glide
        NetworkFunctions.loadImage(
                context,
                currentArticleData.getmThumbUrl(),
                adapterViewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (mAdapterData == null) {
            return 0;
        } else {
            return mAdapterData.size();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addAdapterData(List<ArticleData> articleDataList) {
        mAdapterData.addAll(articleDataList);
        notifyDataSetChanged();
    }

    public ArticleData getArticleData(int index) {
        return mAdapterData.get(index);
    }

    public List<ArticleData> getArticleDataList() {
        return mAdapterData;
    }

    //clears and updates adapterData
    public void clearData() {
        mAdapterData.clear();
        notifyDataSetChanged();
    }

}
