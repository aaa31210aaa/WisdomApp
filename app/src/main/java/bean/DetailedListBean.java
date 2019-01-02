package bean;


import java.util.List;

public class DetailedListBean {
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
        private String scdid;
        private String cpid;
        private String zcbzid;
        private String bzflid;
        private String rscdesc;
        private String rsckyj;
        private String pczq;
        private String pcdeptid;
        private String wgpcd;
        private String sfbc;
        private String bzorder;
        private String isok;
        private String jlly;
        private String wgpcdname;
        private String sfbcname;
        private String isokname;
        private String jllyname;

        public String getScdid() {
            return scdid;
        }

        public void setScdid(String scdid) {
            this.scdid = scdid;
        }

        public String getCpid() {
            return cpid;
        }

        public void setCpid(String cpid) {
            this.cpid = cpid;
        }

        public String getZcbzid() {
            return zcbzid;
        }

        public void setZcbzid(String zcbzid) {
            this.zcbzid = zcbzid;
        }

        public String getBzflid() {
            return bzflid;
        }

        public void setBzflid(String bzflid) {
            this.bzflid = bzflid;
        }

        public String getRscdesc() {
            return rscdesc;
        }

        public void setRscdesc(String rscdesc) {
            this.rscdesc = rscdesc;
        }

        public String getRsckyj() {
            return rsckyj;
        }

        public void setRsckyj(String rsckyj) {
            this.rsckyj = rsckyj;
        }

        public String getPczq() {
            return pczq;
        }

        public void setPczq(String pczq) {
            this.pczq = pczq;
        }

        public String getPcdeptid() {
            return pcdeptid;
        }

        public void setPcdeptid(String pcdeptid) {
            this.pcdeptid = pcdeptid;
        }

        public String getWgpcd() {
            return wgpcd;
        }

        public void setWgpcd(String wgpcd) {
            this.wgpcd = wgpcd;
        }

        public String getSfbc() {
            return sfbc;
        }

        public void setSfbc(String sfbc) {
            this.sfbc = sfbc;
        }

        public String getBzorder() {
            return bzorder;
        }

        public void setBzorder(String bzorder) {
            this.bzorder = bzorder;
        }

        public String getIsok() {
            return isok;
        }

        public void setIsok(String isok) {
            this.isok = isok;
        }

        public String getJlly() {
            return jlly;
        }

        public void setJlly(String jlly) {
            this.jlly = jlly;
        }

        public String getWgpcdname() {
            return wgpcdname;
        }

        public void setWgpcdname(String wgpcdname) {
            this.wgpcdname = wgpcdname;
        }

        public String getSfbcname() {
            return sfbcname;
        }

        public void setSfbcname(String sfbcname) {
            this.sfbcname = sfbcname;
        }

        public String getIsokname() {
            return isokname;
        }

        public void setIsokname(String isokname) {
            this.isokname = isokname;
        }

        public String getJllyname() {
            return jllyname;
        }

        public void setJllyname(String jllyname) {
            this.jllyname = jllyname;
        }
    }
}
