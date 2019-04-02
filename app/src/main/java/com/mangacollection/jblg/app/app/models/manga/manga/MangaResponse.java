package com.mangacollection.jblg.app.app.models.manga.manga;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class MangaResponse implements Parcelable {
    public static final Creator<MangaResponse> CREATOR=new Creator<MangaResponse>() {
        @Override
        public MangaResponse createFromParcel(Parcel source) {

            return new MangaResponse(source);
        }

        @Override
        public MangaResponse[] newArray(int size) {
            return new MangaResponse[size];
        }
    };
    @SerializedName("request_hash")
    private String requestHash;
    @SerializedName("request_cached")
    private Boolean requestCached;
    @SerializedName("mal_id")
    private Integer malId;
    @SerializedName("link_canonical")
    private String linkCanonical;
    @SerializedName("title")
    private String title;
    @SerializedName("title_english")
    private String titleEnglish;
    @SerializedName("title_japanese")
    private String titleJapanese;
    @SerializedName("title_synonyms")
    private Object titleSynonyms;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("volumes")
    private Integer volumes;
    @SerializedName("status")
    private String status;
    @SerializedName("publishing")
    private Boolean publishing;
    @SerializedName("published_string")
    private String publishedString;
    @SerializedName("published")
    private Published published;
    @SerializedName("rating")
    private String rating;
    @SerializedName("score")
    private Double score;
    @SerializedName("scored_by")
    private Integer scoredBy;
    @SerializedName("rank")
    private Integer rank;
    @SerializedName("popularity")
    private Integer popularity;
    @SerializedName("members")
    private Integer members;
    @SerializedName("favorites")
    private Integer favorites;
    @SerializedName("synopsis")
    private String synopsis;
    @SerializedName("background")
    private String background;
    @SerializedName("authors")
    private List<Author> authors;
    @SerializedName("genre")
    private List<Genre> genres;
    @SerializedName("characters")
    private List<Characters> characters;
    @SerializedName("type")
    private String type;
    @SerializedName("Serialization")
    private List<Serialization> serializations;

    private MangaResponse(Parcel in){
        requestHash=in.readString();
        byte tmpRequestCached=in.readByte();
        requestCached=tmpRequestCached==0?null : tmpRequestCached==1;
        if(in.readByte()==0){
            malId=null;
        } else{
            malId=in.readInt();
        }
        linkCanonical = in.readString();
        title=in.readString();
        titleEnglish=in.readString();
        titleJapanese=in.readString();
        imageUrl=in.readString();
        type=in.readString();
        if(in.readByte()==0){
            volumes=null;
        }else{
            volumes=in.readInt();
        }
        status=in.readString();
        byte tmpPublished = in.readByte();
        publishing=tmpPublished==0?null :tmpPublished==1;
        publishedString=in.readString();
        published=in.readParcelable(Published.class.getClassLoader());
        rating=in.readString();
        if(in.readByte()==0){
            score=null;
        }else {
            score=in.readDouble();
        }
        if(in.readByte()==0){
            scoredBy=null;
        }else {
            scoredBy=in.readInt();
        }
        if (in.readByte()==0){
            rank=null;
        }else{
            rank=in.readInt();
        }
        if (in.readByte()==0){
            popularity=null;
        }else {
            popularity=in.readInt();
        }
        if(in.readByte()==0){
            members=null;
        }else {members=in.readInt();}
        if(in.readByte()==0){
            favorites=null;
        }else {
            favorites=in.readInt();
        }
        synopsis=in.readString();
        background=in.readString();
        titleJapanese=in.readString();
        title=in.readString();
        titleEnglish=in.readString();
        genres=in.createTypedArrayList(Genre.CREATOR);
        characters=in.createTypedArrayList(Characters.CREATOR);
        serializations=in.createTypedArrayList(Serialization.CREATOR);
        authors=in.createTypedArrayList(Author.CREATOR);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requestHash);
        dest.writeByte((byte)(requestCached==null ? 1:2));
        if (malId==null){
            dest.writeByte((byte)0);
        }else{
            dest.writeByte((byte)1);
            dest.writeInt(malId);
        }
        dest.writeString(linkCanonical);
        dest.writeString(title);
        dest.writeString(titleEnglish);
        dest.writeString(titleJapanese);
        dest.writeString(imageUrl);
        dest.writeString(type);
        if (volumes==null) {
            dest.writeInt(0);
        }else {
            dest.writeByte((byte)1);
            dest.writeInt(volumes);
        }
        dest.writeString(status);
        dest.writeString(synopsis);
        dest.writeString(title);
        dest.writeString(titleEnglish);
        dest.writeString(titleJapanese);
        if(score==null){
            dest.writeByte((byte)0);
        }else {
            dest.writeByte((byte) 1);
            dest.writeDouble(score);
        }

        if(scoredBy==null){
            dest.writeByte((byte) 0);
        }else {
            dest.writeByte((byte) 1);
            dest.writeInt(scoredBy);
        }
        if (rank == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rank);
        }
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(popularity);
        }
        if (members == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(members);
        }
        if (favorites == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(favorites);
        }
        dest.writeByte((byte)(publishing == null ? 0 : (publishing ? 1 : 2)));
        dest.writeString(publishedString);
        dest.writeParcelable(published,flags);
        dest.writeString(rating);
        dest.writeTypedList(genres);
        dest.writeTypedList(characters);
        dest.writeTypedList(authors);
        dest.writeTypedList(serializations);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Creator<MangaResponse> getCREATOR() {
        return CREATOR;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public Boolean getRequestCached() {
        return requestCached;
    }

    public void setRequestCached(Boolean requestCached) {
        this.requestCached = requestCached;
    }

    public Integer getMalId() {
        return malId;
    }

    public void setMalId(Integer malId) {
        this.malId = malId;
    }

    public String getLinkCanonical() {
        return linkCanonical;
    }

    public void setLinkCanonical(String linkCanonical) {
        this.linkCanonical = linkCanonical;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public String getTitleJapanese() {
        return titleJapanese;
    }

    public void setTitleJapanese(String titleJapanese) {
        this.titleJapanese = titleJapanese;
    }

    public Object getTitleSynonyms() {
        return titleSynonyms;
    }

    public void setTitleSynonyms(Object titleSynonyms) {
        this.titleSynonyms = titleSynonyms;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getVolumes() {
        return volumes;
    }

    public void setVolumes(Integer volumes) {
        this.volumes = volumes;
    }


    public String getStatus() {
        return status;
    }

    public void setStatu(String status) {
        this.status = status;
    }

    public boolean isPublishing() {
        return publishing;
    }

    public void setPublishing(boolean publishing) {
        this.publishing = publishing;
    }

    public String getPublishedString() {
        return publishedString;
    }

    public void setPublishedString(String publishedString) {
        this.publishedString = publishedString;
    }

    public Published getPublished() {
        return published;
    }

    public void setPublished(Published published) {
        this.published = published;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getScoredBy() {
        return scoredBy;
    }

    public void setScoredBy(Integer scoredBy) {
        this.scoredBy = scoredBy;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Boolean getPublishing() {
        return publishing;
    }

    public void setPublishing(Boolean publishing) {
        this.publishing = publishing;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Serialization> getSerializations() {
        return serializations;
    }

    public void setSerializations(List<Serialization> serializations) {
        this.serializations = serializations;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Characters> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Characters> characters) {
        this.characters = characters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("requestHash", requestHash)
                .append("requestCached", requestCached).append("malId", malId)
                .append("linkCanonical", linkCanonical).append("title", title)
                .append("titleEnglish", titleEnglish).append("titleJapanese", titleJapanese)
                .append("titleSynonyms", titleSynonyms).append("imageUrl", imageUrl)
                .append("type", type).append("volumes", volumes)
                .append("status", status).append("publishing", publishing)
                .append("publishingString", publishedString).append("published", published)
                .append("rating", rating).append("score", score).append("scoredBy", scoredBy)
                .append("rank", rank).append("popularity", popularity).append("members", members)
                .append("favorites", favorites).append("synopsis", synopsis).append("background", background)
                .append("genre", genres).append("charactersj", characters)
                .append("serialization",serializations).append("authors",authors).toString();
    }


}
