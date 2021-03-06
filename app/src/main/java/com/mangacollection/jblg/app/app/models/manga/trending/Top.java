package com.mangacollection.jblg.app.app.models.manga.trending;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Top implements Parcelable {
    public final static Parcelable.Creator<Top> CREATOR= new Creator<Top>() {
        @Override
        public Top createFromParcel(Parcel source) {
            return new Top(source);
        }

        @Override
        public Top[] newArray(int size) {
            return new Top[size];
        }
    };
    @SerializedName("mal_id")
    @Expose
    private Integer malId;
    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("volumes")
    @Expose
    private Integer volumes;
    @SerializedName("members")
    @Expose
    private Integer members;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private Object endDate;

    private Top(Parcel source){
        this.malId=((Integer)source.readValue(Integer.class.getClassLoader()));
        this.rank=((Integer)source.readValue(Integer.class.getClassLoader()));
        this.url=((String)source.readValue(String.class.getClassLoader()));
        this.imageUrl=((String)source.readValue(String.class.getClassLoader()));
        this.title=((String)source.readValue(String.class.getClassLoader()));
        this.type=((String)source.readValue(String.class.getClassLoader()));
        this.score=((Double)source.readValue(Double.class.getClassLoader()));
        this.volumes=((Integer)source.readValue(Integer.class.getClassLoader()));
        this.members=((Integer)source.readValue(Integer.class.getClassLoader()));
        this.startDate=((String)source.readValue(String.class.getClassLoader()));
        this.endDate=((Object)source.readValue(Object.class.getClassLoader()));
    }

    public Top(){}

    public Integer getMalId() {
        return malId;
    }

    public void setMalId(Integer malId) {
        this.malId = malId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getVolumes() {
        return volumes;
    }

    public void setVolumes(Integer volumes) {
        this.volumes = volumes;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("malId",malId).append("rank",rank).append("url",url).append("imageUrl",imageUrl).append("title",title).append("type",type).append("score",score).append("volumes",volumes).append("members",members).append("startDate",startDate).append("endDate",endDate).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(malId);
        dest.writeValue(rank);
        dest.writeValue(url);
        dest.writeValue(imageUrl);
        dest.writeValue(title);
        dest.writeValue(type);
        dest.writeValue(score);
        dest.writeValue(members);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeValue(volumes);
    }

    public int describeContents() {
        return 0;
    }
}
