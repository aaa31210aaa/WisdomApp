package bean;

public class XcJcBean {
    //图片
    private int image;
    //检查id
    private String crid;
    //被检查单位
    private String bjcdw;
    //开始时间
    private String starttime;
    //结束时间
    private String endtime;
    //检查情况
    private String remark;
    //类型图片
    private int typeImage;
    //类型名字
    private String typeName;
    //处理状态
    private String handlingstatusname;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCrid() {
        return crid;
    }

    public void setCrid(String crid) {
        this.crid = crid;
    }

    public String getBjcdw() {
        return bjcdw;
    }

    public void setBjcdw(String bjcdw) {
        this.bjcdw = bjcdw;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(int typeImage) {
        this.typeImage = typeImage;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getHandlingstatusname() {
        return handlingstatusname;
    }

    public void setHandlingstatusname(String handlingstatusname) {
        this.handlingstatusname = handlingstatusname;
    }
}
