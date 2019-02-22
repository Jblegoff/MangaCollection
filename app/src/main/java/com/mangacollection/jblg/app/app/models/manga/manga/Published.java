package com.mangacollection.jblg.app.app.models.manga.manga;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Published implements Parcelable {
    public static final Creator<Published> CREATOR=new Creator<Published>() {
        @Override
        public Published createFromParcel(Parcel source) {
            return new Published(source);
        }

        @Override
        public Published[] newArray(int size) {
            return new Published[size];
        }
    };
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;
    private Published(Parcel source) {
        from=source.readString();
        to=source.readString();
    }

    public static Creator<Published> getCREATOR() {
        return CREATOR;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(to);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("from",from).append("to",to).toString();
    }
}
