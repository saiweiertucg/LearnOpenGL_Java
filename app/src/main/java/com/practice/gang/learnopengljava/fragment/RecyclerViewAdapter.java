package com.practice.gang.learnopengljava.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.practice.gang.learnopengljava.R;

import java.util.List;

/**
 * Created by gang on 2018/12/15.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<ItemFactory.FragmentItem> mValues;
    private final BaseFragment.OnListFragmentInteractionListener mListener;

    public RecyclerViewAdapter(List<ItemFactory.FragmentItem> items, BaseFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String title = mValues.get(position).title;
        holder.mTitle.setText(title.substring(0, title.indexOf("Activity")));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTitle;
        public ItemFactory.FragmentItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.title);
        }
    }
}
