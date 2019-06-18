/*
 * Copyright (C) 2019 Aayat Mimiko
 */

package com.example.xyzreader.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.xyzreader.MainActivity;
import com.example.xyzreader.R;
import com.example.xyzreader.adapters.ListAdapter;
import com.example.xyzreader.models.ArticleData;
import com.example.xyzreader.utils.ArticleDataParser;
import com.example.xyzreader.utils.network_utils.NetworkFunctions;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

public class ArticleListFragment extends Fragment implements ListAdapter.OnListItemClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    public static final String LIST_FRAGMENT_TAG = "article_list_fragment";

    public static final String SELECTED_ARICLE_DATA_KEY = "selected_movie";
    public static final String ARTICLE_DATA_LIST_KEY = "article_data_list";

    private Context mContext;
    private View mRootView;

    //adapters
    private ListAdapter mListAdapter;
    private List<ArticleData> mArticleDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list, container, false);
        mContext = mRootView.getContext();

        defineViews();

        if (savedInstanceState == null) {
            //execute search
            fetchArticles();

        } else {
            restoreSavedValues(savedInstanceState);
        }

        return mRootView;
    }

    private void defineViews() {
        //---configure recyclerVIew
        RecyclerView mListPosterRecyclerView = mRootView.findViewById(R.id.articleListRecyclerView);
        mListPosterRecyclerView.setHasFixedSize(true);

        //configureLayoutManager returns a LayoutManager
        mListPosterRecyclerView.setLayoutManager(configureLayoutManager());

        //define search adapter and search preferences
        mListAdapter = new ListAdapter(this);

        //set adapter to RecyclerView
        mListPosterRecyclerView.setAdapter(mListAdapter);
    }

    private void restoreSavedValues(Bundle savedInstanceState) {
        //restore saved values
        mArticleDataList = Parcels.unwrap
                (savedInstanceState.getParcelable(ArticleListFragment.ARTICLE_DATA_LIST_KEY));

        mListAdapter.addAdapterData(mArticleDataList);
    }

    private GridLayoutManager configureLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,
                getResources().getInteger(R.integer.search_column_span));

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }

    public void fetchArticles() {
        //build AN request
        final ANRequest request = NetworkFunctions.buildListRequest();

        request.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String returnedJSONString) {
                    //parse and populate retrieved data
                    mArticleDataList = ArticleDataParser.parseData(returnedJSONString,
                            mContext);

                    mListAdapter.addAdapterData(mArticleDataList);
            }

            @Override
            public void onError(ANError anError) {
                //notify error
                Snackbar.make(mRootView.findViewById(R.id.topLayout),
                        anError.getErrorDetail(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    public void loadDetailFragment(View sharedView, int position) {
        DetailFragment detailFragment = new DetailFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));
            detailFragment.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));
        }

        detailFragment.setArticleData(mListAdapter.getArticleData(position));

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragment_container, detailFragment, DetailFragment.DETAIL_FRAGMENT_TAG)
                .addSharedElement(sharedView, getString(R.string.list_to_detail_transition)+position)
                .commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARTICLE_DATA_LIST_KEY, Parcels.wrap(mListAdapter.getArticleDataList()));
    }

    @Override
    public void onItemClick(int position) {

    }
}
