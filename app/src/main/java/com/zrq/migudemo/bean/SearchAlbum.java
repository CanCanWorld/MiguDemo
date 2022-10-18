package com.zrq.migudemo.bean;

import java.util.List;

public class SearchAlbum {

    private List<AlbumsDTO> albums;
    private int pgt;
    private String keyword;
    private String pageNo;
    private boolean success;

    public List<AlbumsDTO> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumsDTO> albums) {
        this.albums = albums;
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

    public static class AlbumsDTO {
        private int fullSongTotal;
        private List<SingerDTO> singer;
        private String albumPicS;
        private int songNum;
        private Object programName;
        private String publishDate;
        private String id;
        private String title;
        private List<?> movieName;
        private String albumPicL;
        private String albumPicM;
        private List<String> highlightStr;

        public int getFullSongTotal() {
            return fullSongTotal;
        }

        public void setFullSongTotal(int fullSongTotal) {
            this.fullSongTotal = fullSongTotal;
        }

        public List<SingerDTO> getSinger() {
            return singer;
        }

        public void setSinger(List<SingerDTO> singer) {
            this.singer = singer;
        }

        public String getAlbumPicS() {
            return albumPicS;
        }

        public void setAlbumPicS(String albumPicS) {
            this.albumPicS = albumPicS;
        }

        public int getSongNum() {
            return songNum;
        }

        public void setSongNum(int songNum) {
            this.songNum = songNum;
        }

        public Object getProgramName() {
            return programName;
        }

        public void setProgramName(Object programName) {
            this.programName = programName;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
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

        public List<?> getMovieName() {
            return movieName;
        }

        public void setMovieName(List<?> movieName) {
            this.movieName = movieName;
        }

        public String getAlbumPicL() {
            return albumPicL;
        }

        public void setAlbumPicL(String albumPicL) {
            this.albumPicL = albumPicL;
        }

        public String getAlbumPicM() {
            return albumPicM;
        }

        public void setAlbumPicM(String albumPicM) {
            this.albumPicM = albumPicM;
        }

        public List<String> getHighlightStr() {
            return highlightStr;
        }

        public void setHighlightStr(List<String> highlightStr) {
            this.highlightStr = highlightStr;
        }

        public static class SingerDTO {
            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
