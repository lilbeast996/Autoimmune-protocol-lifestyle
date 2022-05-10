package com.healthy_lifestyle.aip_lifestyle.recipe;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;


import com.healthy_lifestyle.aip_lifestyle.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<String> ingredientsList;
    private LayoutInflater inflater;
    private IngredientsAdapter.ItemClickListener clickListener;

    IngredientsAdapter(Context context, List<String> ingredientsList) {
        this.inflater = LayoutInflater.from(context);
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_ingredients_item, parent, false);
        return new IngredientsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(IngredientsAdapter.ViewHolder holder, int position) {
        String animal = ingredientsList.get(position);
        holder.checkBox.setText(animal);
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return ingredientsList.get(id);
    }

    void setClickListener(IngredientsAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
