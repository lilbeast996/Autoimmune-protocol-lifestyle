package com.healthy_lifestyle.aip_lifestyle.video_list;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.player.PlayerActivity;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<MeditationModel> meditationVideoData;
    private int lastPosition = -1;
    private LayoutInflater inflater;
    private VideoListAdapter.ItemClickListener clickListener;
    private Context context;

    public VideoListAdapter(Context context, List<MeditationModel> meditationVideoData) {
        this.inflater = LayoutInflater.from(context);
        this.meditationVideoData = meditationVideoData;
        this.context = context;
    }

    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_meditation_item, parent, false);
        return new VideoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.ViewHolder holder, int position) {
        MeditationModel meditationModel = meditationVideoData.get(position);
        holder.tvTitle.setText(meditationModel.getTitle());
        holder.cvMeditation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("title", meditationVideoData.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("description", meditationVideoData.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("videoId", meditationVideoData.get(holder.getAdapterPosition()).getVideoId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        return meditationVideoData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        CardView cvMeditation;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_meditation_title);
            cvMeditation = itemView.findViewById(R.id.cd_meditation);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(VideoListAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
