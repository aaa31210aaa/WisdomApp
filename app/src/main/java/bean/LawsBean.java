package bean;


import java.util.List;

public class LawsBean {
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
        private String lrtitle;
        private String flfgid;
        private String bbdate;
        private String industryname;
        private String lrtypename;

        public String getLrtitle() {
            return lrtitle;
        }

        public void setLrtitle(String lrtitle) {
            this.lrtitle = lrtitle;
        }

        public String getFlfgid() {
            return flfgid;
        }

        public void setFlfgid(String flfgid) {
            this.flfgid = flfgid;
        }

        public String getBbdate() {
            return bbdate;
        }

        public void setBbdate(String bbdate) {
            this.bbdate = bbdate;
        }

        public String getIndustryname() {
            return industryname;
        }

        public void setIndustryname(String industryname) {
            this.industryname = industryname;
        }

        public String getLrtypename() {
            return lrtypename;
        }

        public void setLrtypename(String lrtypename) {
            this.lrtypename = lrtypename;
        }
    }
}
