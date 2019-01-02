package bean;


import java.util.List;

public class DutyPlanListBean {
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
        private String dutyplanid;
        private String sumstartinfoid;
        private String dutyman;
        private String dutychargeman;
        private String shiftmun;
        private String recordman;
        private String starttime;
        private String endtime;
        private String shiftmunname;
        private String dutyPlanName;
        private String memo;

        public String getDutyplanid() {
            return dutyplanid;
        }

        public void setDutyplanid(String dutyplanid) {
            this.dutyplanid = dutyplanid;
        }

        public String getSumstartinfoid() {
            return sumstartinfoid;
        }

        public void setSumstartinfoid(String sumstartinfoid) {
            this.sumstartinfoid = sumstartinfoid;
        }

        public String getDutyman() {
            return dutyman;
        }

        public void setDutyman(String dutyman) {
            this.dutyman = dutyman;
        }

        public String getDutychargeman() {
            return dutychargeman;
        }

        public void setDutychargeman(String dutychargeman) {
            this.dutychargeman = dutychargeman;
        }

        public String getShiftmun() {
            return shiftmun;
        }

        public void setShiftmun(String shiftmun) {
            this.shiftmun = shiftmun;
        }

        public String getRecordman() {
            return recordman;
        }

        public void setRecordman(String recordman) {
            this.recordman = recordman;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getShiftmunname() {
            return shiftmunname;
        }

        public void setShiftmunname(String shiftmunname) {
            this.shiftmunname = shiftmunname;
        }

        public String getDutyPlanName() {
            return dutyPlanName;
        }

        public void setDutyPlanName(String dutyPlanName) {
            this.dutyPlanName = dutyPlanName;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }
    }
}
