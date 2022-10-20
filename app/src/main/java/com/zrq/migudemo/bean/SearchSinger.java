package com.zrq.migudemo.bean;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class SearchSinger {

    private List<ArtistsDTO> artists;
    private int pgt;
    private String keyword;
    private String pageNo;
    private boolean success;

    public List<ArtistsDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistsDTO> artists) {
        this.artists = artists;
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

    public static class ArtistsDTO {
        private String artistPicL;
        private int fullSongTotal;
        private String artistPicM;
        private int songNum;
        private int albumNum;
        private String artistPicS;
        private String id;
        private String title;
        private List<String> highlightStr;

        public String getArtistPicL() {
            return artistPicL;
        }

        public void setArtistPicL(String artistPicL) {
            this.artistPicL = artistPicL;
        }

        public int getFullSongTotal() {
            return fullSongTotal;
        }

        public void setFullSongTotal(int fullSongTotal) {
            this.fullSongTotal = fullSongTotal;
        }

        public String getArtistPicM() {
            return artistPicM;
        }

        public void setArtistPicM(String artistPicM) {
            this.artistPicM = artistPicM;
        }

        public int getSongNum() {
            return songNum;
        }

        public void setSongNum(int songNum) {
            this.songNum = songNum;
        }

        public int getAlbumNum() {
            return albumNum;
        }

        public void setAlbumNum(int albumNum) {
            this.albumNum = albumNum;
        }

        public String getArtistPicS() {
            return artistPicS;
        }

        public void setArtistPicS(String artistPicS) {
            this.artistPicS = artistPicS;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getHighlightStr() {
            return highlightStr;
        }

        public void setHighlightStr(List<String> highlightStr) {
            this.highlightStr = highlightStr;
        }
    }
}
