package bean;


import java.util.List;

public class SubmissionBean {
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
        private String scid;
        private String qyid;
        private String checkadd;
        private String checkname;
        private String checkman;
        private String scdate;
        private String problems;
        private String iscommit;
        private String handlemethod;
        private String iscommitname;
        private String qyname;
        private String hylyname;
        private String pczq;
        private String yhNum;

        public String getScid() {
            return scid;
        }

        public void setScid(String scid) {
            this.scid = scid;
        }

        public String getQyid() {
            return qyid;
        }

        public void setQyid(String qyid) {
            this.qyid = qyid;
        }

        public String getCheckadd() {
            return checkadd;
        }

        public void setCheckadd(String checkadd) {
            this.checkadd = checkadd;
        }

        public String getCheckname() {
            return checkname;
        }

        public void setCheckname(String checkname) {
            this.checkname = checkname;
        }

        public String getCheckman() {
            return checkman;
        }

        public void setCheckman(String checkman) {
            this.checkman = checkman;
        }

        public String getScdate() {
            return scdate;
        }

        public void setScdate(String scdate) {
            this.scdate = scdate;
        }

        public String getProblems() {
            return problems;
        }

        public void setProblems(String problems) {
            this.problems = problems;
        }

        public String getIscommit() {
            return iscommit;
        }

        public void setIscommit(String iscommit) {
            this.iscommit = iscommit;
        }

        public String getHandlemethod() {
            return handlemethod;
        }

        public void setHandlemethod(String handlemethod) {
            this.handlemethod = handlemethod;
        }

        public String getIscommitname() {
            return iscommitname;
        }

        public void setIscommitname(String iscommitname) {
            this.iscommitname = iscommitname;
        }

        public String getQyname() {
            return qyname;
        }

        public void setQyname(String qyname) {
            this.qyname = qyname;
        }

        public String getHylyname() {
            return hylyname;
        }

        public void setHylyname(String hylyname) {
            this.hylyname = hylyname;
        }

        public String getPczq() {
            return pczq;
        }

        public void setPczq(String pczq) {
            this.pczq = pczq;
        }

        public String getYhNum() {
            return yhNum;
        }

        public void setYhNum(String yhNum) {
            this.yhNum = yhNum;
        }
    }
}
