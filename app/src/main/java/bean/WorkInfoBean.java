package bean;


import java.util.List;

public class WorkInfoBean {
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
        private String crid;
        private String userid;
        private String usertype;
        private String jcr;
        private String jcsj;
        private String jcfxwt;
        private String wtcljy;
        private String memo;
        private String username;
        private String paramname;
        private String sfxz;

        public String getCrid() {
            return crid;
        }

        public void setCrid(String crid) {
            this.crid = crid;
        }

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

        public String getJcr() {
            return jcr;
        }

        public void setJcr(String jcr) {
            this.jcr = jcr;
        }

        public String getJcsj() {
            return jcsj;
        }

        public void setJcsj(String jcsj) {
            this.jcsj = jcsj;
        }

        public String getJcfxwt() {
            return jcfxwt;
        }

        public void setJcfxwt(String jcfxwt) {
            this.jcfxwt = jcfxwt;
        }

        public String getWtcljy() {
            return wtcljy;
        }

        public void setWtcljy(String wtcljy) {
            this.wtcljy = wtcljy;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getParamname() {
            return paramname;
        }

        public void setParamname(String paramname) {
            this.paramname = paramname;
        }

        public String getSfxz() {
            return sfxz;
        }

        public void setSfxz(String sfxz) {
            this.sfxz = sfxz;
        }
    }
}
