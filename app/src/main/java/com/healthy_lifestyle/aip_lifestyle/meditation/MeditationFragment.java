package com.healthy_lifestyle.aip_lifestyle.meditation;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.healthy_lifestyle.aip_lifestyle.Constants;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.video_list.VideoListActivity;

public class MeditationFragment extends Fragment {
    CardView cvBeginner, cvIntermediate, cvAdvanced;
    private String difficultyTag = "difficultyTag";

    public static MeditationFragment newInstance() {
        return new MeditationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meditation, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        cvBeginner = root.findViewById(R.id.cv_beginner);
        cvIntermediate = root.findViewById(R.id.cv_intermediate);
        cvAdvanced = root.findViewById(R.id.cv_advanced);

        cvBeginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), VideoListActivity.class);
                intent.putExtra(difficultyTag, Constants.LEVEL_BEGINNER);
                startActivity(intent);
            }
        });

        cvIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), VideoListActivity.class);
                intent.putExtra(difficultyTag, Constants.LEVEL_INTERMEDIATE);
                startActivity(intent);
            }
        });

        cvAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), VideoListActivity.class);
                intent.putExtra(difficultyTag, Constants.LEVEL_ADVANCED);
                startActivity(intent);
            }
        });

        return root;
    }
}