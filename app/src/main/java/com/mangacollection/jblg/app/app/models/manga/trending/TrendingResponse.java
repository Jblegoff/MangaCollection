package com.mangacollection.jblg.app.app.models.manga.trending;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class TrendingResponse implements Parcelable {
    public final static Parcelable.Creator<TrendingResponse> CREATOR=new Creator<TrendingResponse>() {
        @Override
        public TrendingResponse createFromParcel(Parcel source) {
            return new TrendingResponse(source);
        }

        @Override
        public TrendingResponse[] newArray(int size) {
            return new TrendingResponse[size];
        }
    };
    @SerializedName("requesthash")
    @Expose
    private String requestHash;
    @SerializedName("requestcache")
    @Expose
    private Boolean requestCache;
    @SerializedName("top")
    @Expose
    private List<Top> top=null;

    private TrendingResponse(Parcel source) {
        this.requestHash=((String)source.readValue(String.class.getClassLoader()));
        this.requestCache=((Boolean) source.readValue(Boolean.class.getClassLoader()));
        source.readList(this.top,(Top.class.getClassLoader()));
    }
    public TrendingResponse(){}
    public String getRequestHash(){
        return requestHash;
    }
    public void setRequestHash(){
        this.requestHash=requestHash;
    }

    public Boolean getRequestCache() {
        return requestCache;
    }

    public void setRequestCache(Boolean requestCache) {
        this.requestCache = requestCache;
    }

    public List<Top> getTop() {
        return top;
    }

    public void setTop(List<Top> top) {
        this.top = top;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("requestHash",requestHash).append("requestCache",requestCache).append("top",top).toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(requestHash);
        dest.writeValue(requestCache);
        dest.writeList(top);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
