package com.mangacollection.jblg.app.app.models.manga.manga;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Serialization implements Parcelable {
    public static final Creator<Serialization> CREATOR=new Creator<Serialization>() {
        @Override
        public Serialization createFromParcel(Parcel source) {
            return new Serialization(source);
        }

        @Override
        public Serialization[] newArray(int size) {
            return new Serialization[size];
        }
    };
    @SerializedName("url")
    private String url;
    @SerializedName("name")
    private String name;

    private Serialization(Parcel source) {
        url=source.readString();
        name=source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("url",url).append("name",name).toString();
    }

    public static Creator<Serialization> getCREATOR() {
        return CREATOR;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
