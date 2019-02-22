package com.mangacollection.jblg.app.app.models.manga.trending;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;


public class TrendingResponse implements Parcelable {
    public final static Parcelable.Creator<TrendingResponse> CREATOR=new Creator<TrendingResponse>() {

        @SuppressWarnings({
                "unchecked"
        }
        )

        @Override
        public TrendingResponse createFromParcel(Parcel source) {
            return new TrendingResponse(source);
        }

        @Override
        public TrendingResponse[] newArray(int size) {
            return new TrendingResponse[size];
        }
    };

    @SerializedName("request_hash")
    @Expose
    private String requestHash;
    @SerializedName("request_cache")
    @Expose
    private Boolean requestCache;
    @SerializedName("top")
    @Expose
    private List<Top> top=null;

    public TrendingResponse(Parcel source){
        this.requestHash=((String)source.readValue(String.class.getClassLoader()));
        this.requestCache=((Boolean) source.readValue(String.class.getClassLoader()));
        source.readList(this.top,(com.mangacollection.jblg.app.app.models.manga.trending.Top.class.getClassLoader()));
    }
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
    public void setTop(List<Top> top) {
        this.top = top;
    }
    public List<Top> getTop() {
        return top;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("requestHash",requestHash).append("requestCache",requestCache).append("top",top).toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(requestHash);
        dest.writeValue(requestCache);
        dest.writeList(top);
    }
}
