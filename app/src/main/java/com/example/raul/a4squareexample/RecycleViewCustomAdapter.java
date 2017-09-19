package com.example.raul.a4squareexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Raul on 17/09/2017.
 */

public class RecycleViewCustomAdapter extends RecyclerView.Adapter<RecycleViewCustomAdapter.PerfilViewHolder>{

    private Context mContext;
    private ArrayList<String> venues;
    private ArrayList<String> checkins;
    private ArrayList<String> photos;
    private com.example.raul.a4squareexample.RecyclerViewClickListener listener;

    public RecycleViewCustomAdapter(Context c, ArrayList<String> venues, ArrayList<String> checkins,ArrayList<String> urls, com.example.raul.a4squareexample.RecyclerViewClickListener listener){
        mContext = c;
        this.venues = venues;
        this.checkins = checkins;
        this.photos = urls;
        this.listener = listener;
    }

    @Override
    public PerfilViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        //PerfilViewHolder perfilViewHolder = new PerfilViewHolder(v);
        //return perfilViewHolder;
        return new RowViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(PerfilViewHolder holder, int position) {
       holder.tvNombre.setText(venues.get(position));
        holder.tvSmall.setText("Check Ins: "+checkins.get(position));
       // Picasso.with(mContext).load(photos.get(position)).
        Picasso.with(mContext).load(photos.get(position)).skipMemoryCache().into(holder.ivFoto);
        //holder.ivFoto.setImageResource(perfiles.get(position).getFotoId());
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }


    public static class PerfilViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivFoto;
        private TextView tvNombre;
        private TextView tvSmall;

        PerfilViewHolder(View vistaElemento){
            super(vistaElemento);
            ivFoto = (ImageView) vistaElemento.findViewById(R.id.person_photo);
            tvNombre = (TextView) vistaElemento.findViewById(R.id.person_name);
            tvSmall = (TextView) vistaElemento.findViewById(R.id.person_age);
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
