package com.example.proyecto_primer_parcial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder> {
    private List<Mensaje> mensajes;
    private OnMensajeClickListener listener;

    public MensajeAdapter(List<Mensaje> mensajes, OnMensajeClickListener listener) {
        this.mensajes = mensajes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MensajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        holder.bind(mensaje, listener);
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class MensajeViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MensajeViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(Mensaje mensaje, OnMensajeClickListener listener) {
            textView.setText(mensaje.getTexto());
            itemView.setOnClickListener(v -> listener.onMensajeClick(getAdapterPosition(), mensaje));
        }
    }

    public interface OnMensajeClickListener {
        void onMensajeClick(int position, Mensaje mensaje);
    }
}
