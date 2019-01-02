package bean;


import java.util.List;

public class NrDetailListBean {
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
        private String zcbzdid;
        private String zcbzid;
        private String bzflid;
        private String bzflname;
        private String hybz1;
        private String hybz2;
        private String hybz3;
        private String hybz4;
        private String hybz5;
        private String bzorder;
        private String rscdesc;
        private String rsckyj;
        private String pczq;
        private String pczqval;
        private String sfbc;
        private String pcdeptname;
        private String wgpcd;

        public String getZcbzdid() {
            return zcbzdid;
        }

        public void setZcbzdid(String zcbzdid) {
            this.zcbzdid = zcbzdid;
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

        public String getBzflname() {
            return bzflname;
        }

        public void setBzflname(String bzflname) {
            this.bzflname = bzflname;
        }

        public String getHybz1() {
            return hybz1;
        }

        public void setHybz1(String hybz1) {
            this.hybz1 = hybz1;
        }

        public String getHybz2() {
            return hybz2;
        }

        public void setHybz2(String hybz2) {
            this.hybz2 = hybz2;
        }

        public String getHybz3() {
            return hybz3;
        }

        public void setHybz3(String hybz3) {
            this.hybz3 = hybz3;
        }

        public String getHybz4() {
            return hybz4;
        }

        public void setHybz4(String hybz4) {
            this.hybz4 = hybz4;
        }

        public String getHybz5() {
            return hybz5;
        }

        public void setHybz5(String hybz5) {
            this.hybz5 = hybz5;
        }

        public String getBzorder() {
            return bzorder;
        }

        public void setBzorder(String bzorder) {
            this.bzorder = bzorder;
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

        public String getPczqval() {
            return pczqval;
        }

        public void setPczqval(String pczqval) {
            this.pczqval = pczqval;
        }

        public String getSfbc() {
            return sfbc;
        }

        public void setSfbc(String sfbc) {
            this.sfbc = sfbc;
        }

        public String getPcdeptname() {
            return pcdeptname;
        }

        public void setPcdeptname(String pcdeptname) {
            this.pcdeptname = pcdeptname;
        }

        public String getWgpcd() {
            return wgpcd;
        }

        public void setWgpcd(String wgpcd) {
            this.wgpcd = wgpcd;
        }
    }
}
