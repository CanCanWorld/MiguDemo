package com.zrq.migudemo.bean;

import java.util.List;

public class SearchSong {

    private List<MusicsDTO> musics;
    private int pgt;
    private String keyword;
    private String pageNo;
    private boolean success;

    public List<MusicsDTO> getMusics() {
        return musics;
    }

    public void setMusics(List<MusicsDTO> musics) {
        this.musics = musics;
    }

    public int getPgt() {
        return pgt;
    }

    public void setPgt(int pgt) {
        this.pgt = pgt;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class MusicsDTO {
        private String songName;
        private Object isHdCrbt;
        private String albumName;
        private String has24Bitqq;
        private String hasMv;
        private String artist;
        private String hasHQqq;
        private String albumId;
        private String title;
        private String singerName;
        private String cover;
        private Object mp3;
        private String hasSQqq;
        private Object has3Dqq;
        private String singerId;
        private String mvCopyrightId;
        private String copyrightId;
        private Object unuseFlag;
        private String auditionsFlag;
        private String auditionsLength;
        private String mvId;
        private String id;
        private String lyrics;

        public String getSongName() {
            return songName;
        }

        public void setSongName(String songName) {
            this.songName = songName;
        }

        public Object getIsHdCrbt() {
            return isHdCrbt;
        }

        public void setIsHdCrbt(Object isHdCrbt) {
            this.isHdCrbt = isHdCrbt;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getHas24Bitqq() {
            return has24Bitqq;
        }

        public void setHas24Bitqq(String has24Bitqq) {
            this.has24Bitqq = has24Bitqq;
        }

        public String getHasMv() {
            return hasMv;
        }

        public void setHasMv(String hasMv) {
            this.hasMv = hasMv;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getHasHQqq() {
            return hasHQqq;
        }

        public void setHasHQqq(String hasHQqq) {
            this.hasHQqq = hasHQqq;
        }

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSingerName() {
            return singerName;
        }

        public void setSingerName(String singerName) {
            this.singerName = singerName;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public Object getMp3() {
            return mp3;
        }

        public void setMp3(Object mp3) {
            this.mp3 = mp3;
        }

        public String getHasSQqq() {
            return hasSQqq;
        }

        public void setHasSQqq(String hasSQqq) {
            this.hasSQqq = hasSQqq;
        }

        public Object getHas3Dqq() {
            return has3Dqq;
        }

        public void setHas3Dqq(Object has3Dqq) {
            this.has3Dqq = has3Dqq;
        }

        public String getSingerId() {
            return singerId;
        }

        public void setSingerId(String singerId) {
            this.singerId = singerId;
        }

        public String getMvCopyrightId() {
            return mvCopyrightId;
        }

        public void setMvCopyrightId(String mvCopyrightId) {
            this.mvCopyrightId = mvCopyrightId;
        }

        public String getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(String copyrightId) {
            this.copyrightId = copyrightId;
        }

        public Object getUnuseFlag() {
            return unuseFlag;
        }

        public void setUnuseFlag(Object unuseFlag) {
            this.unuseFlag = unuseFlag;
        }

        public String getAuditionsFlag() {
            return auditionsFlag;
        }

        public void setAuditionsFlag(String auditionsFlag) {
            this.auditionsFlag = auditionsFlag;
        }

        public String getAuditionsLength() {
            return auditionsLength;
        }

        public void setAuditionsLength(String auditionsLength) {
            this.auditionsLength = auditionsLength;
        }

        public String getMvId() {
            return mvId;
        }

        public void setMvId(String mvId) {
            this.mvId = mvId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        @Override
        public String toString() {
            return "MusicsDTO{" +
                    "songName='" + songName + '\'' +
                    ", isHdCrbt=" + isHdCrbt +
                    ", albumName='" + albumName + '\'' +
                    ", has24Bitqq='" + has24Bitqq + '\'' +
                    ", hasMv='" + hasMv + '\'' +
                    ", artist='" + artist + '\'' +
                    ", hasHQqq='" + hasHQqq + '\'' +
                    ", albumId='" + albumId + '\'' +
                    ", title='" + title + '\'' +
                    ", singerName='" + singerName + '\'' +
                    ", cover='" + cover + '\'' +
                    ", mp3=" + mp3 +
                    ", hasSQqq='" + hasSQqq + '\'' +
                    ", has3Dqq=" + has3Dqq +
                    ", singerId='" + singerId + '\'' +
                    ", mvCopyrightId='" + mvCopyrightId + '\'' +
                    ", copyrightId='" + copyrightId + '\'' +
                    ", unuseFlag=" + unuseFlag +
                    ", auditionsFlag='" + auditionsFlag + '\'' +
                    ", auditionsLength='" + auditionsLength + '\'' +
                    ", mvId='" + mvId + '\'' +
                    ", id='" + id + '\'' +
                    ", lyrics='" + lyrics + '\'' +
                    '}';
        }
    }
}
