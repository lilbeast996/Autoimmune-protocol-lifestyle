package com.healthy_lifestyle.aip_lifestyle.foodList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.healthy_lifestyle.aip_lifestyle.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private List<FoodListModel> foodList;
    private LayoutInflater inflater;
    private Context context;

    public FoodListAdapter(Context context, List<FoodListModel> mRecipes) {
        this.inflater = LayoutInflater.from(context);
        this.foodList = mRecipes;
        this.context = context;
    }

    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_foodlist_item, parent, false);
        return new FoodListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodListAdapter.ViewHolder holder, int position) {
        FoodListModel recipe = foodList.get(position);
        Glide.with(context).load(foodList.get(position).getImage()).into(holder.ivFoodImage);
        holder.tvFoodType.setText(recipe.getType());
        holder.tvFoodRepresentatives.setText(recipe.getRepresentatives());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage;
        TextView tvFoodType;
        TextView tvFoodRepresentatives;

        ViewHolder(View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.iv_food_item);
            tvFoodType = itemView.findViewById(R.id.tv_food_type);
            tvFoodRepresentatives = itemView.findViewById(R.id.tv_food_representatives);
        }
    }
}