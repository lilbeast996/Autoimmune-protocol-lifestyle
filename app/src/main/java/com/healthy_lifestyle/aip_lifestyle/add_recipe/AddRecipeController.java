package com.healthy_lifestyle.aip_lifestyle.add_recipe;

import android.net.Uri;

public interface AddRecipeController {
     void OnUpload(String title, String description, Uri uri, String prep, String ingredients, String recipe);
}
