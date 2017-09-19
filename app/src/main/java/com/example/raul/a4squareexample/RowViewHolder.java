package com.example.raul.a4squareexample;

import android.view.View;

/**
 * Created by Raul on 18/09/2017.
 */

public class RowViewHolder extends RecycleViewCustomAdapter.PerfilViewHolder implements View.OnClickListener {
    private RecyclerViewClickListener listener;

    public RowViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition());
    }
}