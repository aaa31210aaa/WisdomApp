package bean;

import java.util.List;

public class DutySystemBean {

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
        private String gzzdid;
        private String qyid;
        private String cbname;
        private String cbtype;
        private String efftime;
        private String cbtypename;
        private String qyname;
        private String issea;

        public String getGzzdid() {
            return gzzdid;
        }

        public void setGzzdid(String gzzdid) {
            this.gzzdid = gzzdid;
        }

        public String getQyid() {
            return qyid;
        }

        public void setQyid(String qyid) {
            this.qyid = qyid;
        }

        public String getCbname() {
            return cbname;
        }

        public void setCbname(String cbname) {
            this.cbname = cbname;
        }

        public String getCbtype() {
            return cbtype;
        }

        public void setCbtype(String cbtype) {
            this.cbtype = cbtype;
        }

        public String getEfftime() {
            return efftime;
        }

        public void setEfftime(String efftime) {
            this.efftime = efftime;
        }

        public String getCbtypename() {
            return cbtypename;
        }

        public void setCbtypename(String cbtypename) {
            this.cbtypename = cbtypename;
        }

        public String getQyname() {
            return qyname;
        }

        public void setQyname(String qyname) {
            this.qyname = qyname;
        }

        public String getIssea() {
            return issea;
        }

        public void setIssea(String issea) {
            this.issea = issea;
        }
    }
}
