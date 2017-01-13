package com.henja.liqnot;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bo.NotifierCurrency;
import bo.NotifierCurrencyData;

/**
 *
 * Created by javier on 06/01/2017.
 */

public class CurrencySelectionRecyclerViewAdapter extends RecyclerView.Adapter<CurrencySelectionRecyclerViewAdapter.ViewHolder>{

    private int selectedPos = 0;
    private NotifierCurrencyData[] itemsData;
    private CurrencyListener currencyListener;

    public interface CurrencyListener {
        void OnCurrencyClick(NotifierCurrency currency);
    }

    public CurrencySelectionRecyclerViewAdapter(NotifierCurrencyData[] itemsData) {
        this.itemsData = itemsData;
    }

    public void setOnCurrencyListener(CurrencyListener currencyListener){
       this.currencyListener = currencyListener;
    }

    @Override
    public CurrencySelectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_item_layout, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(CurrencySelectionRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.currencyName.setText(itemsData[position].getCurrency().getName());
        holder.currencyIcon.setImageResource(itemsData[position].getCurrency().getIcon());

        if (selectedPos == position){
            this.currencyListener.OnCurrencyClick(this.itemsData[position].getCurrency());
        }

        holder.itemView.setSelected(selectedPos == position);
        Log.e("Item Changed","Item selected");
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView currencyName;
        ImageView currencyIcon;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            currencyName = (TextView) itemLayoutView.findViewById(R.id.currency_name);
            currencyIcon = (ImageView) itemLayoutView.findViewById(R.id.currency_icon);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedPos);
                    selectedPos = getLayoutPosition();
                    notifyItemChanged(selectedPos);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}

