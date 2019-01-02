package bean;


import java.util.List;

public class ZkdsryglBean {
    private boolean success;
    private String errormessage;
    private List<CellsBean> cells;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public List<CellsBean> getCells() {
        return cells;
    }

    public void setCells(List<CellsBean> cells) {
        this.cells = cells;
    }

    public static class CellsBean {
        private String userid;
        private String usertype;
        private String username;
        private String fusername;
        private String postname;
        private String stationname;
        private String linktel;
        private String supervisedept;
        private String inminedeptid;
        private String inminedeptname;
        private String inminetime;
        private String comname;
        private String memo;
        private String paramname;
        private String townshipname;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFusername() {
            return fusername;
        }

        public void setFusername(String fusername) {
            this.fusername = fusername;
        }

        public String getPostname() {
            return postname;
        }

        public void setPostname(String postname) {
            this.postname = postname;
        }

        public String getStationname() {
            return stationname;
        }

        public void setStationname(String stationname) {
            this.stationname = stationname;
        }

        public String getLinktel() {
            return linktel;
        }

        public void setLinktel(String linktel) {
            this.linktel = linktel;
        }

        public String getSupervisedept() {
            return supervisedept;
        }

        public void setSupervisedept(String supervisedept) {
            this.supervisedept = supervisedept;
        }

        public String getInminedeptid() {
            return inminedeptid;
        }

        public void setInminedeptid(String inminedeptid) {
            this.inminedeptid = inminedeptid;
        }

        public String getInminedeptname() {
            return inminedeptname;
        }

        public void setInminedeptname(String inminedeptname) {
            this.inminedeptname = inminedeptname;
        }

        public String getInminetime() {
            return inminetime;
        }

        public void setInminetime(String inminetime) {
            this.inminetime = inminetime;
        }

        public String getComname() {
            return comname;
        }

        public void setComname(String comname) {
            this.comname = comname;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getParamname() {
            return paramname;
        }

        public void setParamname(String paramname) {
            this.paramname = paramname;
        }

        public String getTownshipname() {
            return townshipname;
        }

        public void setTownshipname(String townshipname) {
            this.townshipname = townshipname;
        }
    }
}
