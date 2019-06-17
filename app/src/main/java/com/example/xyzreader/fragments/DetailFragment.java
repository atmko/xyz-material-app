package com.example.xyzreader.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.models.ArticleData;
import com.example.xyzreader.utils.network_utils.NetworkFunctions;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DetailFragment extends Fragment {

    private static final String TAG = "ArticleDetailFragment";
    public static final String DETAIL_FRAGMENT_TAG = "detail_fragment";

    private ArticleData mArticleData;

    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private ImageView photo;
    private TextView bodyView;
    private TextView bylineView;
    private FloatingActionButton actionButton;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        defineViews(rootView);

        //noinspection StatementWithEmptyBody
        if (savedInstanceState == null) {

        } else {
            restoreSavedValues(savedInstanceState);
        }

        setViewValues();

        return rootView;
    }

    public void setArticleData(ArticleData articleData) {
        this.mArticleData = articleData;
    }

    private Date parsePublishedDate() {
        try {
            String date = mArticleData.getmPublishedDate();
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    private void defineViews(View rootView) {
        collapsingToolbar = rootView.findViewById(R.id.collapsing_toolbar);photo = rootView.findViewById(R.id.photo);
        toolbar = rootView.findViewById(R.id.toolbar);
        bodyView = (TextView) rootView.findViewById(R.id.article_body);
        bylineView = (TextView) rootView.findViewById(R.id.article_byline);
        actionButton = rootView.findViewById(R.id.share_fab);

    }

    private void restoreSavedValues(Bundle savedInstanceState) {
        mArticleData = Parcels.unwrap
                (savedInstanceState.getParcelable(ArticleListFragment.SELECTED_ARICLE_DATA_KEY));

    }

    private void setViewValues() {
        //set title
        collapsingToolbar.setTitle(mArticleData.getmTitle());

        //configure toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_top));
                    ArticleListFragment articleListFragment = (ArticleListFragment) fragmentManager.findFragmentByTag(ArticleListFragment.LIST_FRAGMENT_TAG);
                    articleListFragment.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_bottom));
                }

                fragmentManager.popBackStack();
            }
        });

        //load photo
        NetworkFunctions.loadImage(getContext(), mArticleData.getmPhotoUrl(), photo);

        //set body text
        bodyView.setText((Html.fromHtml(mArticleData.getmBody().replaceAll("(\r\n|\n)", "<br />"))));
        bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

        //set subtitle text
        bylineView.setMovementMethod(new LinkMovementMethod());

        Date publishedDate = parsePublishedDate();
        if (!publishedDate.before(START_OF_EPOCH.getTime())) {
            bylineView.setText(Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            + " by <font color='#ff6e00'>"
                            + mArticleData.getmAuthor()
                            + "</font>"));

        } else {
            // If date is before 1902, just show the string
            bylineView.setText(Html.fromHtml(
                    outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                            + mArticleData.getmAuthor()
                            + "</font>"));

        }

        //configure floating action button
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //save ArticleData object
        outState.putParcelable(ArticleListFragment.SELECTED_ARICLE_DATA_KEY, Parcels.wrap(mArticleData));
    }
}
