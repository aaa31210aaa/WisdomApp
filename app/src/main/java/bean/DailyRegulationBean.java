package bean;


public class DailyRegulationBean {
    /**
     * 记录ID
     */
    private String crid = "";
    /**
     * 企业信息ID
     */
    private String qyid = "";
    /**
     * 检查单位ID
     */
    private String deptid = "";
    /**
     * 检查时间
     */
    private String jcsj = "";
    /**
     * 检查类型
     */
    private String jclx = "";
    /**
     * 检查人
     */
    private String jcr = "";
    /**
     * 是否达标
     */
    private String sfdb = "";
    //是否达标
    private String sfdbname = "";
    /**
     * 治理截止时间
     */
    private String zljzsj = "";
    /**
     * 隐患状态
     */
    private String yhzt = "";
    /**
     * 来源
     */
    private String ly = "";
    /**
     * 创建人
     */
    private String createid = "";
    /**
     * 创建日期
     */
    private String createdate = "";
    /**
     * 修改人
     */
    private String modifyid = "";
    /**
     * 修改日期
     */
    private String modifydate = "";

    /**
     * 备注
     */
    private String memo = "";

    /**
     * 状态
     */
    private String handlingstatus = "";
    private String comname;
    private String paramname;
    private String yhztname;


    public String getYhztname() {
        return yhztname;
    }

    public void setYhztname(String yhztname) {
        this.yhztname = yhztname;
    }

    public String getSfdbname() {
        return sfdbname;
    }

    public void setSfdbname(String sfdbname) {
        this.sfdbname = sfdbname;
    }


    public String getCrid() {
        return crid;
    }

    public void setCrid(String crid) {
        this.crid = crid;
    }

    public String getQyid() {
        return qyid;
    }

    public void setQyid(String qyid) {
        this.qyid = qyid;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getJcsj() {
        return jcsj;
    }

    public void setJcsj(String jcsj) {
        this.jcsj = jcsj;
    }

    public String getJclx() {
        return jclx;
    }

    public void setJclx(String jclx) {
        this.jclx = jclx;
    }

    public String getJcr() {
        return jcr;
    }

    public void setJcr(String jcr) {
        this.jcr = jcr;
    }

    public String getSfdb() {
        return sfdb;
    }

    public void setSfdb(String sfdb) {
        this.sfdb = sfdb;
    }

    public String getZljzsj() {
        return zljzsj;
    }

    public void setZljzsj(String zljzsj) {
        this.zljzsj = zljzsj;
    }

    public String getYhzt() {
        return yhzt;
    }

    public void setYhzt(String yhzt) {
        this.yhzt = yhzt;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getModifyid() {
        return modifyid;
    }

    public void setModifyid(String modifyid) {
        this.modifyid = modifyid;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getHandlingstatus() {
        return handlingstatus;
    }

    public void setHandlingstatus(String handlingstatus) {
        this.handlingstatus = handlingstatus;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getParamname() {
        return paramname;
    }

    public void setParamname(String paramname) {
        this.paramname = paramname;
    }

}
