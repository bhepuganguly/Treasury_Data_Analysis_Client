package com.example.tatha.project5fedcash;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


public class ResultDetailFragment extends Fragment {

    private TextView mQuoteView = null;
    private int mCurrIdx = -1;
    private int mQuoteArrayLen;

    public int getShownIndex() {
        return mCurrIdx;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_result_detail, vg,false);

    }

    public void showQuoteAtIndex(int newIndex) {
        if (newIndex < 0 || newIndex >= mQuoteArrayLen)
            return;
        mCurrIdx = newIndex;
        mQuoteView.setText(ResultsActivity.right.get(mCurrIdx));
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mQuoteView = (TextView) getActivity().findViewById(R.id.textView1);
        mQuoteArrayLen = ResultsActivity.right.size();
    }


    @Override
    public void onAttach(Context activity) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
    }


}
