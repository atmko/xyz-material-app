package com.example.xyzreader;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.xyzreader.fragments.ArticleListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadListFragment();

        } else {
            restoreSavedValues(savedInstanceState);
        }
    }

    private void restoreSavedValues(Bundle savedInstanceState) {
    }

    private void loadListFragment() {
        ArticleListFragment articleListFragment = new ArticleListFragment();
        addFragment(R.id.fragment_container, articleListFragment, ArticleListFragment.LIST_FRAGMENT_TAG);
    }

    private void addFragment(int containerId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment, fragmentTag)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
