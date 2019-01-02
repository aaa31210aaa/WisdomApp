package bean;


import java.util.List;

public class QyYhInformationBean {
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
        private String crname;
        private String yhid;
        private String crtypename;
        private String pcdate;
        private String crstate;
        private String crstatename;
        private String deptname;

        public String getCrname() {
            return crname;
        }

        public void setCrname(String crname) {
            this.crname = crname;
        }

        public String getYhid() {
            return yhid;
        }

        public void setYhid(String yhid) {
            this.yhid = yhid;
        }

        public String getCrtypename() {
            return crtypename;
        }

        public void setCrtypename(String crtypename) {
            this.crtypename = crtypename;
        }

        public String getPcdate() {
            return pcdate;
        }

        public void setPcdate(String pcdate) {
            this.pcdate = pcdate;
        }

        public String getCrstate() {
            return crstate;
        }

        public void setCrstate(String crstate) {
            this.crstate = crstate;
        }

        public String getCrstatename() {
            return crstatename;
        }

        public void setCrstatename(String crstatename) {
            this.crstatename = crstatename;
        }

        public String getDeptname() {
            return deptname;
        }

        public void setDeptname(String deptname) {
            this.deptname = deptname;
        }
    }
}
