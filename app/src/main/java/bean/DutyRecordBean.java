package bean;


import java.util.List;

public class DutyRecordBean {
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
        private String dutyreid;
        private String sumstartinfoid;
        private String dutyplanid;
        private String noticetype;
        private String recordcontent;
        private String recordman;
        private String recorddate;
        private String recordbeonduty;
        private String dutycontent;
        private String handlecase;
        private String handlestate;
        private String summaryname;
        private String dutyPlanName;
        private String sumStates;

        public String getDutyreid() {
            return dutyreid;
        }

        public void setDutyreid(String dutyreid) {
            this.dutyreid = dutyreid;
        }

        public String getSumstartinfoid() {
            return sumstartinfoid;
        }

        public void setSumstartinfoid(String sumstartinfoid) {
            this.sumstartinfoid = sumstartinfoid;
        }

        public String getDutyplanid() {
            return dutyplanid;
        }

        public void setDutyplanid(String dutyplanid) {
            this.dutyplanid = dutyplanid;
        }

        public String getNoticetype() {
            return noticetype;
        }

        public void setNoticetype(String noticetype) {
            this.noticetype = noticetype;
        }

        public String getRecordcontent() {
            return recordcontent;
        }

        public void setRecordcontent(String recordcontent) {
            this.recordcontent = recordcontent;
        }

        public String getRecordman() {
            return recordman;
        }

        public void setRecordman(String recordman) {
            this.recordman = recordman;
        }

        public String getRecorddate() {
            return recorddate;
        }

        public void setRecorddate(String recorddate) {
            this.recorddate = recorddate;
        }

        public String getRecordbeonduty() {
            return recordbeonduty;
        }

        public void setRecordbeonduty(String recordbeonduty) {
            this.recordbeonduty = recordbeonduty;
        }

        public String getDutycontent() {
            return dutycontent;
        }

        public void setDutycontent(String dutycontent) {
            this.dutycontent = dutycontent;
        }

        public String getHandlecase() {
            return handlecase;
        }

        public void setHandlecase(String handlecase) {
            this.handlecase = handlecase;
        }

        public String getHandlestate() {
            return handlestate;
        }

        public void setHandlestate(String handlestate) {
            this.handlestate = handlestate;
        }

        public String getSummaryname() {
            return summaryname;
        }

        public void setSummaryname(String summaryname) {
            this.summaryname = summaryname;
        }

        public String getDutyPlanName() {
            return dutyPlanName;
        }

        public void setDutyPlanName(String dutyPlanName) {
            this.dutyPlanName = dutyPlanName;
        }

        public String getSumStates() {
            return sumStates;
        }

        public void setSumStates(String sumStates) {
            this.sumStates = sumStates;
        }
    }
}
