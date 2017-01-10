package com.henja.liqnot;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bo.Notifier;
import bo.NotifierCurrency;
import bo.NotifierCurrencyData;
import bo.NotifierDirector;

/**
 * Created by javier on 06/01/2017.
 */

public class NotifierListRecyclerViewAdapter extends RecyclerView.Adapter<NotifierListRecyclerViewAdapter.ViewHolder> implements NotifierDirector.NotifierDirectorListener{

    private int selectedPos = 0;
    private NotifierDirector notifierDirector;
    private ArrayList<Notifier> itemsData;

    @Override
    public void OnNewNotifier(Notifier notifier) {
        this.itemsData.add(notifier);
        this.notifyItemInserted(itemsData.size()-1);
    }

    public interface NotifierListListener {
        public void OnItemClick(Notifier notifier);
    }

    public NotifierListRecyclerViewAdapter(NotifierDirector notifierDirector, ArrayList<Notifier> items) {
        this.notifierDirector = notifierDirector;
        this.notifierDirector.addNotifierDirectorListener(this);

        this.itemsData = new ArrayList<Notifier>();
        for (Notifier item:items) {
            this.itemsData.add(item);
        }
    }

    @Override
    public NotifierListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notifier, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotifierListRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.idText.setText(itemsData.get(position).getId());
        if (itemsData.get(position).getRule() != null) {
            holder.contentText.setText(itemsData.get(position).getRule().toHumanReadableString());
        } else {
            holder.contentText.setText("ERROR - EMPTY RULE");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView idText;
        public TextView contentText;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            idText = (TextView) itemLayoutView.findViewById(R.id.id_text);
            contentText = (TextView) itemLayoutView.findViewById(R.id.content_text);
        }


    }


    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}

