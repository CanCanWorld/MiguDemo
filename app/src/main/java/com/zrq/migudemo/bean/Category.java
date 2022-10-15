package com.zrq.migudemo.bean;

import java.util.List;

public class Category {

    private String msg;
    private ResDTO res;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResDTO getRes() {
        return res;
    }

    public void setRes(ResDTO res) {
        this.res = res;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResDTO {
        private List<CategoryDTO> category;

        public List<CategoryDTO> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryDTO> category) {
            this.category = category;
        }

        public static class CategoryDTO {
            private int count;
            private String ename;
            private String rname;
            private String cover_temp;
            private String name;
            private String cover;
            private int rank;
            private List<?> filter;
            private int sn;
            private String icover;
            private double atime;
            private int type;
            private String id;
            private String picasso_cover;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getEname() {
                return ename;
            }

            public void setEname(String ename) {
                this.ename = ename;
            }

            public String getRname() {
                return rname;
            }

            public void setRname(String rname) {
                this.rname = rname;
            }

            public String getCover_temp() {
                return cover_temp;
            }

            public void setCover_temp(String cover_temp) {
                this.cover_temp = cover_temp;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public List<?> getFilter() {
                return filter;
            }

            public void setFilter(List<?> filter) {
                this.filter = filter;
            }

            public int getSn() {
                return sn;
            }

            public void setSn(int sn) {
                this.sn = sn;
            }

            public String getIcover() {
                return icover;
            }

            public void setIcover(String icover) {
                this.icover = icover;
            }

            public double getAtime() {
                return atime;
            }

            public void setAtime(double atime) {
                this.atime = atime;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPicasso_cover() {
                return picasso_cover;
            }

            public void setPicasso_cover(String picasso_cover) {
                this.picasso_cover = picasso_cover;
            }
        }
    }
}
