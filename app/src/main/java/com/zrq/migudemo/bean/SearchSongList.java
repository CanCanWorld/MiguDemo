package com.zrq.migudemo.bean;

import java.util.List;

public class SearchSongList {

    private List<SongListsBean> songLists;
    private int pgt;
    private String keyword;
    private String pageNo;
    private boolean success;

    public List<SongListsBean> getSongLists() {
        return songLists;
    }

    public void setSongLists(List<SongListsBean> songLists) {
        this.songLists = songLists;
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

    public static class SongListsBean {
        private List<String> highlightStr;
        private String id;
        private String img;
        private Object intro;
        private int keepNum;
        private int musicNum;
        private String name;
        private int playNum;
        private int priority;
        private int shareNum;
        private int songlistType;
        private String userId;
        private Object userName;

        public List<String> getHighlightStr() {
            return highlightStr;
        }

        public void setHighlightStr(List<String> highlightStr) {
            this.highlightStr = highlightStr;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Object getIntro() {
            return intro;
        }

        public void setIntro(Object intro) {
            this.intro = intro;
        }

        public int getKeepNum() {
            return keepNum;
        }

        public void setKeepNum(int keepNum) {
            this.keepNum = keepNum;
        }

        public int getMusicNum() {
            return musicNum;
        }

        public void setMusicNum(int musicNum) {
            this.musicNum = musicNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPlayNum() {
            return playNum;
        }

        public void setPlayNum(int playNum) {
            this.playNum = playNum;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getShareNum() {
            return shareNum;
        }

        public void setShareNum(int shareNum) {
            this.shareNum = shareNum;
        }

        public int getSonglistType() {
            return songlistType;
        }

        public void setSonglistType(int songlistType) {
            this.songlistType = songlistType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }
    }
}
