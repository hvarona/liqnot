package com.henja.liqnot;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    private OnNotifierListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotifierListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotifierListFragment newInstance(NotifierDirector notifierDirector) {
        NotifierListFragment fragment = new NotifierListFragment();
        Bundle args = new Bundle();
        args.putSerializable(NotifierListFragment.NOTIFIER_DIRECTOR_KEY, notifierDirector);
        fragment.setArguments(args);
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

        this.notifierDirector = (NotifierDirector) getArguments().getSerializable(NotifierListFragment.NOTIFIER_DIRECTOR_KEY);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            //if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //} else {
            //    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
           // }
            recyclerView.setAdapter(new NotifierListRecyclerViewAdapter(this.notifierDirector,this.notifierDirector.getNotifiers()));
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

    public void loadNotifiers(){
        /*ListView notifierListView = (ListView) findViewById(R.id.notifier_list_view);
        DAOFactorySQLite db = DAOFactory.getSQLiteFactory(getView().getContext());
        DAONotifier daoNotifier = db.getNotifierDAO();

        DAOEnumeration<DAO<Notifier>,Notifier> enumeration = daoNotifier.getNotifiers(0,10);
        TextView notifierTextView;
        final ArrayList<String> list = new ArrayList<String>();

        while (enumeration.hasNext()){
            Notifier nextNotifier = enumeration.next();
            list.add("id:"+nextNotifier.getId());
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        notifierListView.setAdapter(adapter);*/

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
