package bean;


public class PoliceStationBean {
    /** 派出所ID */
    private String tpid = "";
    /** 乡镇ID */
    private String townid = "";
    private String townname = "";

    /** 乡镇派出所名称 */
    private String policename = "";
    /** 主管领导 */
    private String postname = "";
    /** 职务 */
    private String stationname = "";
    /** 联系电话 */
    private String linktel = "";
    /** 派出所情况说明 */
    private String policecontent = "";
    /** 创建人 */
    private String createid = "";
    /** 创建日期 */
    private String createdate = "";
    /** 修改人 */
    private String modifyid = "";
    /** 修改日期 */
    private String modifydate = "";
    /** 备注 */
    private String memo = "";

    public String getTpid() {
        return tpid;
    }
    public void setTpid(String tpid) {
        this.tpid = tpid;
    }
    public String getTownname() {
        return townname;
    }
    public void setTownname(String townname) {
        this.townname = townname;
    }
    public String getTownid() {
        return townid;
    }
    public void setTownid(String townid) {
        this.townid = townid;
    }
    public String getPolicename() {
        return policename;
    }
    public void setPolicename(String policename) {
        this.policename = policename;
    }
    public String getPostname() {
        return postname;
    }
    public void setPostname(String postname) {
        this.postname = postname;
    }
    public String getStationname() {
        return stationname;
    }
    public void setStationname(String stationname) {
        this.stationname = stationname;
    }
    public String getLinktel() {
        return linktel;
    }
    public void setLinktel(String linktel) {
        this.linktel = linktel;
    }
    public String getPolicecontent() {
        return policecontent;
    }
    public void setPolicecontent(String policecontent) {
        this.policecontent = policecontent;
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
}
