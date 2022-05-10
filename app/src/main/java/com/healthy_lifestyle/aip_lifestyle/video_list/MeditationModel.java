package com.healthy_lifestyle.aip_lifestyle.video_list;

public class MeditationModel {
    private String title, description, videoId, tag;

    public MeditationModel() {}

    public MeditationModel(String tittle, String description, String videoId, String tag) {
        this.title = tittle;
        this.description = description;
        this.videoId = videoId;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
