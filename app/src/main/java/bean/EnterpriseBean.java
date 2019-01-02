package bean;


import java.util.List;

public class EnterpriseBean {
    private String success;
    private String errormessage;
    private List<CellsBean> cells;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
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
        private String comname;
        private String qyid;
        private String chargedeptname;
        private String zcaddress;
        private String industryname;
        private String addStatus;
        private String tablename;

        public String getComname() {
            return comname;
        }

        public void setComname(String comname) {
            this.comname = comname;
        }

        public String getQyid() {
            return qyid;
        }

        public void setQyid(String qyid) {
            this.qyid = qyid;
        }

        public String getChargedeptname() {
            return chargedeptname;
        }

        public void setChargedeptname(String chargedeptname) {
            this.chargedeptname = chargedeptname;
        }

        public String getZcaddress() {
            return zcaddress;
        }

        public void setZcaddress(String zcaddress) {
            this.zcaddress = zcaddress;
        }

        public String getIndustryname() {
            return industryname;
        }

        public void setIndustryname(String industryname) {
            this.industryname = industryname;
        }

        public String getAddStatus() {
            return addStatus;
        }

        public void setAddStatus(String addStatus) {
            this.addStatus = addStatus;
        }

        public String getTablename() {
            return tablename;
        }

        public void setTablename(String tablename) {
            this.tablename = tablename;
        }
    }
}
