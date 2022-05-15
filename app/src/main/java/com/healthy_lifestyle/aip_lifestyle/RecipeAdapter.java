package com.healthy_lifestyle.aip_lifestyle;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.healthy_lifestyle.aip_lifestyle.recipe.RecipeActivity;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private List<RecipeModel> recipeList;
    private int lastPosition = -1;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    private Context context;
    private Boolean triggeredFromCatalog;
    private String fragment;

    public RecipeAdapter(Context context, List<RecipeModel> mRecipes, Boolean triggeredFromCatalog, String fragment) {
        this.inflater = LayoutInflater.from(context);
        this.recipeList = mRecipes;
        this.context = context;
        this.triggeredFromCatalog = triggeredFromCatalog;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeModel recipe = recipeList.get(position);
        Glide.with(context).load(recipeList.get(position).getImage()).into(holder.ivRecipe);
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvDescription.setText(recipe.getDescription());
        holder.tvPrep.setText(recipe.getPrep());
        holder.cvRecipe.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String ingredients = String.join(", ", recipeList.get(holder.getAdapterPosition()).getIngredients());
                final Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra("title", recipeList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("prep", recipeList.get(holder.getAdapterPosition()).getPrep());
                intent.putExtra("image", recipeList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("description", recipeList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("recipe", recipeList.get(holder.getAdapterPosition()).getRecipe());
                intent.putExtra("ingredients",ingredients);
                intent.putExtra("recipeKey", recipeList.get(holder.getAdapterPosition()).getRecipeKey());
                intent.putExtra("triggeredFrom", triggeredFromCatalog);
                intent.putExtra("fragment", fragment);
                context.startActivity(intent);
            }
        });
        setAnimation(holder.itemView,position);
    }

    private void setAnimation(View viewToAnimate, int position){
        if(position > lastPosition){
            ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivRecipe;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrep;
        CardView cvRecipe;

        ViewHolder(View itemView) {
            super(itemView);
            ivRecipe = itemView.findViewById(R.id.iv_recipe_item);
            tvTitle = itemView.findViewById(R.id.tv_recipe_title);
            tvDescription = itemView.findViewById(R.id.tv_recipe_description);
            tvPrep = itemView.findViewById(R.id.tv_recipe_prep);
            cvRecipe = itemView.findViewById(R.id.cv_recipe);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public void filteredList(ArrayList<RecipeModel> filterList) {
        recipeList = filterList;
        notifyDataSetChanged();
    }
}
