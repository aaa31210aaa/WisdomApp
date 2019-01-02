package bean;


import java.util.List;

public class BmGlBean {

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
        private String deptid;
        private String parentdeptname;
        private String deptname;
        private String deptcode;

        public String getDeptid() {
            return deptid;
        }

        public void setDeptid(String deptid) {
            this.deptid = deptid;
        }

        public String getParentdeptname() {
            return parentdeptname;
        }

        public void setParentdeptname(String parentdeptname) {
            this.parentdeptname = parentdeptname;
        }

        public String getDeptname() {
            return deptname;
        }

        public void setDeptname(String deptname) {
            this.deptname = deptname;
        }

        public String getDeptcode() {
            return deptcode;
        }

        public void setDeptcode(String deptcode) {
            this.deptcode = deptcode;
        }
    }
}
