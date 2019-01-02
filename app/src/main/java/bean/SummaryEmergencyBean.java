package bean;


public class SummaryEmergencyBean {
    /** 评估ID */
    private String evaluaid = "";
    /** 应急事件ID */
    private String eventid = "";
    /** 总结评估单位 */
    private String evaluaunit = "";
    /** 评估人 */
    private String evaluaman = "";
    /** 评估时间 */
    private String evaluatime = "";
    /** 附件 */
    private String enclosure = "";
    /** 附件名称 */
    private String enclosurename = "";
    /** 组织机构ID */
    private String deptid = "";
    /** 父级组织机构ID */
    private String parentdeptid = "";
    /** 备注 */
    private String memo = "";
    /** 创建人 */
    private String createman = "";
    /** 创建时间 */
    private String createdate = "";
    /** 修改人 */
    private String modifyman = "";
    /** 修改时间 */
    private String modifydate = "";
    //评估内容
    private String valuacontent = "";
    //评估单位
    private String evaluaunitName = "";



    public String getEnclosurename() {
        return enclosurename;
    }
    public void setEnclosurename(String enclosurename) {
        this.enclosurename = enclosurename;
    }
    public String getEvaluaunitName() {
        return evaluaunitName;
    }
    public void setEvaluaunitName(String evaluaunitName) {
        this.evaluaunitName = evaluaunitName;
    }
    public String getValuacontent() {
        return valuacontent;
    }
    public void setValuacontent(String valuacontent) {
        this.valuacontent = valuacontent;
    }
    public String getEvaluaid() {
        return evaluaid;
    }
    public void setEvaluaid(String evaluaid) {
        this.evaluaid = evaluaid;
    }
    public String getEventid() {
        return eventid;
    }
    public void setEventid(String eventid) {
        this.eventid = eventid;
    }
    public String getEvaluaunit() {
        return evaluaunit;
    }
    public void setEvaluaunit(String evaluaunit) {
        this.evaluaunit = evaluaunit;
    }
    public String getEvaluaman() {
        return evaluaman;
    }
    public void setEvaluaman(String evaluaman) {
        this.evaluaman = evaluaman;
    }
    public String getEvaluatime() {
        return evaluatime;
    }
    public void setEvaluatime(String evaluatime) {
        this.evaluatime = evaluatime;
    }
    public String getEnclosure() {
        return enclosure;
    }
    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }
    public String getDeptid() {
        return deptid;
    }
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }
    public String getParentdeptid() {
        return parentdeptid;
    }
    public void setParentdeptid(String parentdeptid) {
        this.parentdeptid = parentdeptid;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getCreateman() {
        return createman;
    }
    public void setCreateman(String createman) {
        this.createman = createman;
    }
    public String getCreatedate() {
        return createdate;
    }
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
    public String getModifyman() {
        return modifyman;
    }
    public void setModifyman(String modifyman) {
        this.modifyman = modifyman;
    }
    public String getModifydate() {
        return modifydate;
    }
    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }
}
