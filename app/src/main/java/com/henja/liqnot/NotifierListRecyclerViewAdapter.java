package com.henja.liqnot;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bo.Notifier;
import bo.NotifierDirector;

/**
 *
 * Created by javier on 06/01/2017.
 */

class NotifierListRecyclerViewAdapter extends RecyclerView.Adapter<NotifierListRecyclerViewAdapter.ViewHolder> implements NotifierDirector.NotifierDirectorListener{

    private int selectedPos = -1;
    private ArrayList<Notifier> itemsData;
    private ArrayList<NotifierListListener> listeners;

    @Override
    public void OnNewNotifier(Notifier notifier) {
        this.itemsData.add(notifier);
        this.notifyItemInserted(itemsData.size()-1);
    }

    @Override
    public void OnNotifierModified(Notifier notifier) {
        int position = this.itemsData.indexOf(notifier);
        this.notifyItemChanged(position);
    }

    @Override
    public void OnNotifierRemoved(Notifier notifier) {
        int position = this.itemsData.indexOf(notifier);
        this.itemsData.remove(notifier);
        this.notifyItemRemoved(position);
        this.selectedPos = -1;
    }

    @Override
    public void OnAssetsLoaded() {
        //
    }

    interface NotifierListListener {
        void OnSelectedItem(Notifier notifier);
    }

    void addListener(NotifierListListener listener){
        if (this.listeners.indexOf(listener) == -1) {
            this.listeners.add(listener);
        }
    }

    private void notifyItemSelected(Notifier notifier){
        for(NotifierListListener listener : this.listeners){
            listener.OnSelectedItem(notifier);
        }
    }

    NotifierListRecyclerViewAdapter(NotifierDirector notifierDirector, ArrayList<Notifier> items) {
        this.listeners = new ArrayList<>();
        notifierDirector.addNotifierDirectorListener(this);

        this.itemsData = new ArrayList<>();
        for (Notifier item:items) {
            this.itemsData.add(item);
        }
    }

    @Override
    public NotifierListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notifier, null);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(NotifierListRecyclerViewAdapter.ViewHolder holder, int position) {
        try {
            if (itemsData.get(position).getRule() != null) {
                holder.contentText.setText(itemsData.get(position).getRule().toHumanReadableString());
            } else {
                holder.contentText.setText("ERROR - EMPTY RULE");
            }

        /*if (!holder.itemView.isSelected()) {
            if (selectedPos == position) {
                notifyItemSelected(itemsData.get(position));
            }
        }*/

            holder.itemView.setSelected(selectedPos == position);
            if (selectedPos == position) {

                holder.contentText.setBackgroundColor(0x99009999);
            } else {
                holder.contentText.setBackgroundColor(Color.LTGRAY);
            }
        }catch(Exception e)
        {e.printStackTrace();}
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentText;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            contentText = (TextView) itemLayoutView.findViewById(R.id.content_text);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPos);

                    if (selectedPos == getLayoutPosition()){
                        selectedPos = -1;
                        notifyItemSelected(null);
                    } else {
                        selectedPos = getLayoutPosition();
                        notifyItemSelected(itemsData.get(selectedPos));
                    }
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

