package com.mangacollection.jblg.app.app.models.manga;


import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;

public enum TempEnumForManga {
    INSTANCE;
    MangaResponse manga;

    public MangaResponse getManga(){
        return manga;
    }
    public void setManga(MangaResponse manga){
        this.manga=manga;
    }
}
