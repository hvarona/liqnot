package com.henja.liqnot;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.henja.liqnot.app.LiqNotApp;
import com.henja.liqnot.dummy.DummyContent.DummyItem;

import bo.Notifier;
import bo.NotifierDirector;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNotifierListFragmentInteractionListener}
 * interface.
 */
public class NotifierListFragment extends Fragment{

    private NotifierDirector notifierDirector;
    private Toolbar toolbar;
    private Notifier selectedNotifier;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotifierListFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            if (selectedNotifier != null) {
                notifierDirector.removeNotifier(selectedNotifier);
                item.setVisible(false);
                return true;
            }
        } else if (item.getItemId() == R.id.modify_item) {
            if (selectedNotifier != null) {
                ((LiqNotMainActivity)getActivity()).onModifyNotifierAction(selectedNotifier);
                return true;
            }
        }
        return false;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotifierListFragment newInstance() {
        return new NotifierListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifier_list, container, false);
        setHasOptionsMenu(true);

        FloatingActionButton addNotifierButton = (FloatingActionButton) view.findViewById(R.id.add_notifier_floating_button);
        addNotifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LiqNotMainActivity)getActivity()).onNewNotifierAction();
            }
        });

        toolbar = ((LiqNotMainActivity)getActivity()).getToolbar();
        this.notifierDirector = ((LiqNotApp)getActivity().getApplication()).getNotifierDirector();


        RecyclerView listView = (RecyclerView) view.findViewById(R.id.notifier_list_view);
        // Set the adapter
        if (listView != null) {
            Context context = listView.getContext();
            listView.setLayoutManager(new LinearLayoutManager(context));
            NotifierListRecyclerViewAdapter adapter = new NotifierListRecyclerViewAdapter(this.notifierDirector,this.notifierDirector.getNotifiers());
            adapter.addListener(new NotifierListRecyclerViewAdapter.NotifierListListener() {
                @Override
                public void OnSelectedItem(Notifier notifier) {
                    MenuItemImpl deleteButton = (MenuItemImpl) toolbar.getMenu().findItem(R.id.delete_item);
                    MenuItemImpl modifiyButton = (MenuItemImpl) toolbar.getMenu().findItem(R.id.modify_item);

                    if (notifier != null) {
                        selectedNotifier = notifier;
                        deleteButton.setVisible(true);
                        modifiyButton.setVisible(true);
                    } else {
                        deleteButton.setVisible(false);
                        modifiyButton.setVisible(false);
                    }
                }
            });
            listView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnNotifierListFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        void OnNotifierListFragmentInteraction(DummyItem item);
    }
}
