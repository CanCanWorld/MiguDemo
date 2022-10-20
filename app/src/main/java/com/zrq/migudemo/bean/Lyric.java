package com.zrq.migudemo.bean;

import androidx.annotation.Keep;

@Keep
public class Lyric {

    private String returnCode;
    private String msg;
    private String lyric;
    private Object sbslyric;
    private Object translatedLyric;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Object getSbslyric() {
        return sbslyric;
    }

    public void setSbslyric(Object sbslyric) {
        this.sbslyric = sbslyric;
    }

    public Object getTranslatedLyric() {
        return translatedLyric;
    }

    public void setTranslatedLyric(Object translatedLyric) {
        this.translatedLyric = translatedLyric;
    }
}
