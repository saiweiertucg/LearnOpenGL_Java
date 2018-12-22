package com.practice.gang.learnopengljava.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practice.gang.learnopengljava.R;

import java.util.List;

/**
 * Created by gang on 2018/12/15.
 */

public class BaseFragment extends Fragment {

    private static final String PKG = "pkg";
    private String pkg;
    private OnListFragmentInteractionListener mListener;

    public BaseFragment() {
    }

    public static Fragment newInstance(String pkg) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putString(PKG, pkg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            pkg = getArguments().getString(PKG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_item_list, container, false);

        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(new RecyclerViewAdapter(ItemFactory.createFragmentItems(mListener.getActivityClasses(pkg)), mListener));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ItemFactory.FragmentItem item);

        List<Class> getActivityClasses(String pkg);
    }

}
