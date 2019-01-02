package bean;


import java.util.List;

public class InstitutionalBean {
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
        private String personid;
        private String dwid;
        private String personname;
        private String personsex;
        private String persontel;
        private String personzw;
        private String personbytel;
        private String personzc;
        private String personzhiwei;
        private String personcardnum;
        private String personmail;

        public String getPersonid() {
            return personid;
        }

        public void setPersonid(String personid) {
            this.personid = personid;
        }

        public String getDwid() {
            return dwid;
        }

        public void setDwid(String dwid) {
            this.dwid = dwid;
        }

        public String getPersonname() {
            return personname;
        }

        public void setPersonname(String personname) {
            this.personname = personname;
        }

        public String getPersonsex() {
            return personsex;
        }

        public void setPersonsex(String personsex) {
            this.personsex = personsex;
        }

        public String getPersontel() {
            return persontel;
        }

        public void setPersontel(String persontel) {
            this.persontel = persontel;
        }

        public String getPersonzw() {
            return personzw;
        }

        public void setPersonzw(String personzw) {
            this.personzw = personzw;
        }

        public String getPersonbytel() {
            return personbytel;
        }

        public void setPersonbytel(String personbytel) {
            this.personbytel = personbytel;
        }

        public String getPersonzc() {
            return personzc;
        }

        public void setPersonzc(String personzc) {
            this.personzc = personzc;
        }

        public String getPersonzhiwei() {
            return personzhiwei;
        }

        public void setPersonzhiwei(String personzhiwei) {
            this.personzhiwei = personzhiwei;
        }

        public String getPersoncardnum() {
            return personcardnum;
        }

        public void setPersoncardnum(String personcardnum) {
            this.personcardnum = personcardnum;
        }

        public String getPersonmail() {
            return personmail;
        }

        public void setPersonmail(String personmail) {
            this.personmail = personmail;
        }
    }
}
