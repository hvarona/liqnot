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
import bo.NotifierDirector;

/**
 * Created by javier on 06/01/2017.
 */

public class NotifierListRecyclerViewAdapter extends RecyclerView.Adapter<NotifierListRecyclerViewAdapter.ViewHolder> implements NotifierDirector.NotifierDirectorListener{

    private int selectedPos = -1;
    private int lastSelected = -1;
    private NotifierDirector notifierDirector;
    private ArrayList<Notifier> itemsData;
    private ArrayList<NotifierListListener> listeners;

    @Override
    public void OnNewNotifier(Notifier notifier) {
        this.itemsData.add(notifier);
        this.notifyItemInserted(itemsData.size()-1);
    }

    @Override
    public void OnNotifierRemoved(Notifier notifier) {
        int position = this.itemsData.indexOf(notifier);
        this.itemsData.remove(notifier);
        this.notifyItemRemoved(position);
    }

    @Override
    public void OnAssetsLoaded() {
        //
    }

    public interface NotifierListListener {
        public void OnSelectedItem(Notifier notifier);
    }

    public void addListener(NotifierListListener listener){
        if (this.listeners.indexOf(listener) == -1) {
            this.listeners.add(listener);
        }
    }

    public void notifyItemSelected(Notifier notifier){
        for(NotifierListListener listener : this.listeners){
            listener.OnSelectedItem(notifier);
        }
    }

    public NotifierListRecyclerViewAdapter(NotifierDirector notifierDirector, ArrayList<Notifier> items) {
        this.listeners = new ArrayList<NotifierListListener>();
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
        //holder.idText.setText(itemsData.get(position).getId());
        if (itemsData.get(position).getRule() != null) {
            holder.contentText.setText(itemsData.get(position).getRule().toHumanReadableString());
        } else {
            holder.contentText.setText("ERROR - EMPTY RULE");
        }

        if (!holder.itemView.isSelected()) {
            if (selectedPos == position) {
                notifyItemSelected(itemsData.get(position));
            }
        }

        holder.itemView.setSelected(selectedPos == position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //public TextView idText;
        public TextView contentText;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            //idText = (TextView) itemLayoutView.findViewById(R.id.id_text);
            contentText = (TextView) itemLayoutView.findViewById(R.id.content_text);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedPos);
                    lastSelected = selectedPos;
                    selectedPos = getLayoutPosition();
                    notifyItemChanged(selectedPos);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}

