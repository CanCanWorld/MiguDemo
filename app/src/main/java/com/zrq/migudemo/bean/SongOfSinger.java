package com.zrq.migudemo.bean;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class SongOfSinger {

    private int status;
    private String total;
    private int num;
    private List<SongListBean> songList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<SongListBean> getSongList() {
        return songList;
    }

    public void setSongList(List<SongListBean> songList) {
        this.songList = songList;
    }

    public static class SongListBean {
        private String albumId;
        private String id;
        private String copyrightId;
        private AlBean al;

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(String copyrightId) {
            this.copyrightId = copyrightId;
        }

        public AlBean getAl() {
            return al;
        }

        public void setAl(AlBean al) {
            this.al = al;
        }

        public static class AlBean {
            private String type;
            private String title;
            private String linkUrl;
            private String imgUrl;
            private String summary;
            private String singer;
            private String album;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLinkUrl() {
                return linkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getSinger() {
                return singer;
            }

            public void setSinger(String singer) {
                this.singer = singer;
            }

            public String getAlbum() {
                return album;
            }

            public void setAlbum(String album) {
                this.album = album;
            }
        }
    }
}
