package com.healthy_lifestyle.aip_lifestyle.add_recipe;

public interface AddRecipeView {
    void showActionBar();
    void onAddedSuccess(String message);
    void onAddedFailed(String message);
    void onLoadingStarted();
    void onLoadingFinished();
}
