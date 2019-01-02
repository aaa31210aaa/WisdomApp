package bean;


import java.util.List;

public class PendingBean {
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
        private String cpid;
        private String cpname;
        private String checktype;
        private String checkman;
        private String checkdept;
        private String checkstatar;
        private String checkend;
        private String checkcontent;
        private String planfrom;
        private String planstatus;
        private String checktypename;
        private String planfromname;
        private String planstatusname;
        private String checkdeptname;

        public String getCpid() {
            return cpid;
        }

        public void setCpid(String cpid) {
            this.cpid = cpid;
        }

        public String getCpname() {
            return cpname;
        }

        public void setCpname(String cpname) {
            this.cpname = cpname;
        }

        public String getChecktype() {
            return checktype;
        }

        public void setChecktype(String checktype) {
            this.checktype = checktype;
        }

        public String getCheckman() {
            return checkman;
        }

        public void setCheckman(String checkman) {
            this.checkman = checkman;
        }

        public String getCheckdept() {
            return checkdept;
        }

        public void setCheckdept(String checkdept) {
            this.checkdept = checkdept;
        }

        public String getCheckstatar() {
            return checkstatar;
        }

        public void setCheckstatar(String checkstatar) {
            this.checkstatar = checkstatar;
        }

        public String getCheckend() {
            return checkend;
        }

        public void setCheckend(String checkend) {
            this.checkend = checkend;
        }

        public String getCheckcontent() {
            return checkcontent;
        }

        public void setCheckcontent(String checkcontent) {
            this.checkcontent = checkcontent;
        }

        public String getPlanfrom() {
            return planfrom;
        }

        public void setPlanfrom(String planfrom) {
            this.planfrom = planfrom;
        }

        public String getPlanstatus() {
            return planstatus;
        }

        public void setPlanstatus(String planstatus) {
            this.planstatus = planstatus;
        }

        public String getChecktypename() {
            return checktypename;
        }

        public void setChecktypename(String checktypename) {
            this.checktypename = checktypename;
        }

        public String getPlanfromname() {
            return planfromname;
        }

        public void setPlanfromname(String planfromname) {
            this.planfromname = planfromname;
        }

        public String getPlanstatusname() {
            return planstatusname;
        }

        public void setPlanstatusname(String planstatusname) {
            this.planstatusname = planstatusname;
        }

        public String getCheckdeptname() {
            return checkdeptname;
        }

        public void setCheckdeptname(String checkdeptname) {
            this.checkdeptname = checkdeptname;
        }
    }
}
