package com.mangacollection.jblg.app.app.models.manga.manga;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Author implements Parcelable {
    public static final Creator<Author> CREATOR=new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
    @SerializedName("mal_id")
    private Integer malId;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;

    private Author(Parcel source) {
        if(source.readByte()==0){
            malId=null;
        }else{
            malId=source.readInt();
        }

        name=source.readString();
        url=source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (malId==null){
            dest.writeByte((byte)0);
        }else{
            dest.writeByte((byte)1);
            dest.writeInt(malId);
        }
        dest.writeString(url);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @NonNull
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mal_id",malId)
                .append("url",url).append("name",name).toString();
    }

    public static Creator<Author> getCREATOR() {
        return CREATOR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
