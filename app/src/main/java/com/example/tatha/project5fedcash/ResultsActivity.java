package com.example.tatha.project5fedcash;

/**
 * Created by jishna on 12/6/17.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ResultsActivity extends Activity implements ResultListFragment.ListSelectionListener{
    static ArrayList<String> left =new ArrayList<String>();
    static ArrayList<String> right =new ArrayList<String>();
    private ResultDetailFragment mDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //getting intent from main activity
        Intent i = getIntent();
        Bundle b = i.getExtras();
        //updating the left and right arraylists
        left.add(b.getString("input"));
        right.add(b.getString("out"));
        //getting reference to details fragment
        mDetailsFragment = (ResultDetailFragment) getFragmentManager().findFragmentById(R.id.fragment2);

    }


    @Override
    public void onListSelection(int index) {
        if (mDetailsFragment.getShownIndex() != index) {

            // Tell the QuoteFragment to show the quote string at position index
            mDetailsFragment.showQuoteAtIndex(index);
        }
    }
}
