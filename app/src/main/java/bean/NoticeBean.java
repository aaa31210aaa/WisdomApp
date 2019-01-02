package bean;


import java.util.List;

public class NoticeBean {
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
        private String messageid;
        private String messagetitle;
        private String mestime;

        public String getMessageid() {
            return messageid;
        }

        public void setMessageid(String messageid) {
            this.messageid = messageid;
        }

        public String getMessagetitle() {
            return messagetitle;
        }

        public void setMessagetitle(String messagetitle) {
            this.messagetitle = messagetitle;
        }

        public String getMestime() {
            return mestime;
        }

        public void setMestime(String mestime) {
            this.mestime = mestime;
        }
    }
}
