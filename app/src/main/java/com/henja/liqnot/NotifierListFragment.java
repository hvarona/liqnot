package com.henja.liqnot;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.henja.liqnot.app.LiqNotApp;
import com.henja.liqnot.dummy.DummyContent;
import com.henja.liqnot.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

import bo.Notifier;
import bo.NotifierDirector;
import dao.DAO;
import dao.DAOEnumeration;
import dao.DAOFactory;
import dao.DAONotifier;
import dao.sqlite.DAOFactorySQLite;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNotifierListFragmentInteractionListener}
 * interface.
 */
public class NotifierListFragment extends Fragment{

    private static final String NOTIFIER_DIRECTOR_KEY = "notifier_director_key";
    private NotifierDirector notifierDirector;
    private Toolbar toolbar;
    private OnNotifierListFragmentInteractionListener mListener;
    private Notifier selectedNotifier;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotifierListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotifierListFragment newInstance() {
        NotifierListFragment fragment = new NotifierListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifier_list, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.notifier_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        this.notifierDirector = ((LiqNotApp)getActivity().getApplication()).getNotifierDirector();


        RecyclerView listView = (RecyclerView) view.findViewById(R.id.notifier_list_view);
        // Set the adapter
        if (listView instanceof RecyclerView) {
            Context context = listView.getContext();
            listView.setLayoutManager(new LinearLayoutManager(context));
            NotifierListRecyclerViewAdapter adapter = new NotifierListRecyclerViewAdapter(this.notifierDirector,this.notifierDirector.getNotifiers());
            adapter.addListener(new NotifierListRecyclerViewAdapter.NotifierListListener() {
                @Override
                public void OnSelectedItem(Notifier notifier) {
                    selectedNotifier = notifier;
                    MenuItemImpl deleteButton = (MenuItemImpl) toolbar.getMenu().findItem(R.id.delete_item);
                    deleteButton.setVisible(true);
                }
            });
            listView.setAdapter(adapter);

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.delete_item) {
                        if (selectedNotifier != null) {
                            notifierDirector.removeNotifier(selectedNotifier);
                            item.setVisible(false);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotifierListFragmentInteractionListener) {
            mListener = (OnNotifierListFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNotifierListFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnNotifierListFragmentInteractionListener(DummyItem item);
    }
}
