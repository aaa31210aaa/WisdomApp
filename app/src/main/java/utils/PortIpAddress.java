package utils;


import android.content.Context;

public class PortIpAddress {
    public static String SUCCESS_CODE = "true";
    public static String ERR_CODE = "false";
    public static String CODE = "success";
    public static String MESSAGE = "errormessage";
    public static String JsonArrName = "cells";
    public static String TOKEN_KEY = "access_token";
    public static String USERID_KEY = "userid";


//    public static String myUrl = "http://192.168.5.68:8081/DMHDCS/";
//    public static String host = "http://192.168.5.68:8081/DMHDCS/mobile/";

    //黄依
//    public static String myUrl = "http://192.168.5.87:8080/DMHDCS_GYAJ/";
//    public static String host = "http://192.168.5.87:8080/DMHDCS_GYAJ/mobile/";


    //228
//    public static String myUrl = "http://192.168.5.228:8060/GYAJ/";
//    public static String host = "http://192.168.5.228:8060/GYAJ/mobile/";

    //外网
//    public static String myUrl = "http://218.76.35.118:8888/gyaj/";
//    public static String host = "http://218.76.35.118:8888/gyaj/mobile/";

    //现场
    public static String myUrl = "http://220.170.143.52:8882/";
    public static String host = "http://220.170.143.52:8882/mobile/";

    //军
//    public static String myUrl ="http://192.168.5.79:8080/GYAJ/";
//    public static String host ="http://192.168.5.79:8080/GYAJ/mobile/";

    //token值
    public static String GetToken(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "user_token", null);
    }

    /**
     * 获取企业名
     *
     * @return
     */
    public static String getUserName(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "username", "");
    }

    /**
     * 获取企业id
     *
     * @return
     */
    public static String getUserId(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "userid", "");
    }

    /**
     * 判断用户类型
     *
     * @return
     */
    public static boolean getUserType(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "usertype", "").equals("1") ? true : false;
    }


    //登陆
    public static String LoginAddress() {
        return host + "login.action";
    }

    //更新定位地址
    public static String LocationAdress(){
        return host +"companyreg/updateCoordinate.action";
    }

    //检查更新
    public static String UpdateApp(){
        return host+"update/updateapp.action";
    }

    //判断企业是否有企业信息
    public static String isHaveLatLng(){
        return host+"companyreg/isExistCoordinate.action";
    }

    //所有企业信息
    public static String Companyinfo() {
        return host + "mobilecompanybase/getCompanyinfo.action";
    }

    //已制定清单的企业
    public static String Ybzlist() {
        return host + "riskzcbzdetail/ybzlist.action";
    }

    //未指定清单的企业
    public static String Wbzlist() {
        return host + "riskzcbzdetail/wbzlist.action";
    }


    //有隐患企业信息
    public static String YhCompanyinfo() {
        return host + "mobilecompanybase/getCompanyinfobyrisk.action";
    }

    //企业详细信息
    public static String CompanyDetailInfo() {
        return host + "mobilecompanybase/getCompanydetailinfo.action";
    }

    //事故快报
    public static String Accidentinfo() {
        return host + "mobileaccidentinfo/getAccidentinfo.action";
    }

    //事故单位
    public static String Sgdw() {
        return host + "mobileaccidentinfo/sgdwByLoginName.action";
    }

    //事故地区
    public static String Sgdq() {
        return host + "mobileaccidentinfo/sgdqByUeserId.action";
    }

    //事故详情
    public static String Accidentdetailinfo() {
        return host + "mobileaccidentinfo/getAccidentdetailinfo.action";
    }

    //事故填报接口
    public static String AddAccidentdetailinfo() {
        return host + "mobileaccidentinfo/addAccidentdetailinfo.action";
    }

    //通知公告
    public static String MessageList() {
        return host + "message/messageList.action";
    }

    //公告详情
    public static String MessageDetail() {
        return host + "message/messageInfo.action";
    }

    //隐患登记
    public static String Hidden() {
        return host + "riskinfo/saveInfo.action";
    }

    //重大隐患复查列表
    public static String RiskList() {
        return host + "riskinfo/riskList.action";
    }

    //重大隐患复查详情
    public static String RiskDetail() {
        return host + "riskinfo/riskInfo.action";
    }

    //重大隐患复查详情提交
    public static String RiskSaveInfo() {
        return host + "zdriskaccept/saveInfo.action";
    }

    //执法检查
    public static String Riskzcbzdetail() {
        return host + "riskzcbzdetail/list.action";
    }

    //行政执法行业
    public static String XzzfHy() {
        return host + "mobilezfjcinfo/menuByLoginName.action";
    }

    //行政执法
    public static String Xzzf() {
        return host + "mobilezfjcinfo/list.action";
    }

    //执法企业查询
    public static String XzzfQy() {
        return host + "mobilezfjcinfo/bjcdwlistByLoginName.action";
    }

    //新增执法
    public static String AddXzzf() {
        return host + "mobilezfjcinfo/saveInfo.action";
    }


    //危险源
    public static String GetCompanyRisk() {
        return host + "mobilecompanybase/getCompanyRisk.action";
    }

    //危险源详情
    public static String GetCompanyRiskdetail() {
        return host + "mobilecompanybase/getCompanyRiskdetail.action";
    }

    public static String GetCompanyRiskQy() {
        return host + "mobilecompanyinfo/getCompanyrisk.action";
    }

    public static String GetCompanyRiskdetailQy() {
        return host + "mobilecompanyinfo/getCompanyriskdetail.action";
    }


    //企业隐患信息
    public static String GetRiskinfo() {
        return host + "mobilecompanybase/getRiskinfo.action";
    }

    //安全检查隐患
    public static String getHiddenInfo() {
        return host + "mobileRmSafecheck/Yhlist.action";
    }

    //企业隐患信息详情
    public static String GetRiskinfodetail() {
        return host + "mobilecompanybase/getRiskinfodetail.action";
    }

    public static String GetRiskinfoQy() {
        return host + "mobilecompanyinfo/getRiskinfo.action";
    }

    public static String GetRiskinfodetailQy() {
        return host + "mobilecompanyinfo/getRiskinfodetail.action";
    }


    //企业安全生产信息
    public static String GetSafechecklist() {
        return host + "mobilecompanybase/getSafechecklist.action";
    }

    //企业安全生产详情信息
    public static String GetSafechecklistdetail() {
        return host + "mobilecompanybase/getSafechecklistdetail.action";
    }


    //企业规章制度列表
    public static String GetCompanybylaw() {
        return host + "mobilecompanybase/getCompanybylaw.action";
    }

    //企业规章制度详情
    public static String GetCompanybylawdetail() {
        return host + "mobilecompanybase/getCompanybylawdetail.action";
    }

    public static String GetCompanybylawQy() {
        return host + "mobilecompanyinfo/getCompanybylaw.action";
    }

    public static String GetCompanybylawdetailQy() {
        return host + "mobilecompanyinfo/getCompanybylawdetail.action";
    }


    //法律法规列表
    public static String GetLawregulations() {
        return host + "mobilelawregulations/getLawregulations.action";
    }

    //法律法规详情
    public static String GetLawregulationsdetail() {
        return host + "mobilelawregulations/getLawregulationsdetail.action";
    }

    //分级查询风险监控
    public static String Companyreg() {
        return host + "companyreg/complist.action";
    }

    //职业危害
    public static String Contactlimit() {
        return host + "contactlimit/list.action";
    }

    public static String ContactlimitDetail() {
        return host + "contactlimit/info.action";
    }


    //高毒列表
    public static String Htoxicmatter() {
        return host + "htoxicmatter/list.action";
    }

    //高毒详情
    public static String HtoxicmatterDetail() {
        return host + "htoxicmatter/info.action";
    }


    //危化品接口
    public static String GetChemical() {
        return host + "mobilechemical/getChemical.action";
    }

    //危险性概述
    public static String GetChemicaldetailPartOne() {
        return host + "mobilechemical/getChemicaldetailPartOne.action";
    }

    //化学反应
    public static String GetChemicaldetailPartTwo() {
        return host + "mobilechemical/getChemicaldetailPartTwo.action";
    }

    //理化特性
    public static String GetChemicaldetailPartThree() {
        return host + "mobilechemical/getChemicaldetailPartThree.action";
    }

    //接触控制
    public static String GetChemicaldetailPartFour() {
        return host + "mobilechemical/getChemicaldetailPartFour.action";
    }

    //运输/废弃
    public static String GetChemicaldetailPartFive() {
        return host + "mobilechemical/getChemicaldetailPartFive.action";
    }

    //应急处理
    public static String GetChemicaldetailPartSix() {
        return host + "mobilechemical/getChemicaldetailPartSix.action";
    }

    //危险源临界值
    public static String GetHazardmatter() {
        return host + "mobilehazardmatter/getHazardmatter.action";
    }

    //危险源临界值详情
    public static String GetHazardmatterdetail() {
        return host + "mobilehazardmatter/getHazardmatterdetail.action";
    }

    //统计数据
    public static String GetTjData() {
        return host + "riskinfo/getTjData.action";
    }

    //职业卫生信息
    public static String GetOccuhealth() {
        return host + "mobilecompanybase/getOccuhealth.action";
    }

    //职业卫生详情
    public static String GetOccuhealthDetail() {
        return host + "mobilecompanybase/getOccuhealthdetailqy.action";
    }

    public static String GetOccuhealthQy() {
        return host + "mobilecompanyinfo/getOccuhealth.action";
    }

    public static String GetOccuhealthDetailQy() {
        return host + "mobilecompanyinfo/getOccuhealthdetail.action";
    }


    //应急预案信息
    public static String GetEmergencyreserve() {
        return host + "mobilecompanybase/getEmergencyreserve.action";
    }

    //应急预案详情
    public static String GetEmergencyreserveDetail() {
        return host + "mobilecompanybase/getEmergencyreservedetailqy.action";
    }

    public static String GetEmergencyreserveQy() {
        return host + "mobilecompanyinfo/getEmergencyreserve.action";
    }

    public static String GetEmergencyreserveDetailQy() {
        return host + "mobilecompanyinfo/getEmergencyreservedetail.action";
    }

    //企业安全培训
    public static String GetSafetytraining() {
        return host + "mobilecompanybase/getSafetytraining.action";
    }

    //企业安全培训详情
    public static String GetSafetytrainingDetail() {
        return host + "mobilecompanybase/getSafetytrainingdetailqy.action";
    }

    public static String GetSafetytrainingQy() {
        return host + "mobilecompanyinfo/getSafetytraining.action";
    }

    public static String GetSafetytrainingDetailQy() {
        return host + "mobilecompanyinfo/getSafetytrainingdetail.action";
    }


    //企业安全生产投入
    public static String GetSafeinvestment() {
        return host + "mobilecompanybase/getSafeinvestment.action";
    }


    //企业安全生产投入详情
    public static String GetSafeinvestmentDetail() {
        return host + "mobilecompanybase/getSafeinvestmentdetailqy.action";
    }

    public static String GetSafeinvestmentQy() {
        return host + "mobilecompanyinfo/getSafeinvestment.action";
    }

    public static String GetSafeinvestmentDetailQy() {
        return host + "mobilecompanyinfo/getSafeinvestmentdetail.action";
    }


    //行业列表
    public static String GetIndustry() {
        return host + "";
    }

    //安全机构部门管理
    public static String GetCompanydept() {
        return host + "mobilecompanyinfo/getCompanydept.action";
    }

    //安全机构部门管理详情接口
    public static String GetCompanydeptdetail() {
        return host + "mobilecompanyinfo/getCompanydeptdetail.action";
    }

    //安全人员管理
    public static String GetSafepeopleQy() {
        return host + "mobilecompanyinfo/getSafepeople.action";
    }

    //首页人员管理
    public static String GetSafepeople() {
        return host + "mobilecompanybase/getSafepeople.action";
    }


    //安全人员管理详情
    public static String GetSafepeopledetail() {
        return host + "mobilecompanyinfo/getSafepeopledetail.action";
    }

    //特种设备设施
    public static String GetSafefacilitiesQy() {
        return host + "mobilecompanyinfo/getSafefacilities.action";
    }

    //首页特种设备
    public static String GetSafefacilities() {
        return host + "mobilecompanybase/getSafefacilities.action";
    }


    //特种设备设施详情
    public static String GetSafefacilitiesdetail() {
        return host + "mobilecompanyinfo/getSafefacilitiesdetail.action";
    }

    //应急物资信息
    public static String GetEmergencymaterials() {
        return host + "mobilecompanyinfo/getEmergencymaterials.action";
    }

    //应急物资信息详情
    public static String GetEmergencymaterialsdetail() {
        return host + "mobilecompanyinfo/getEmergencymaterialsdetail.action";
    }

    //应急演练信息
    public static String GetEmergencydrill() {
        return host + "mobilecompanyinfo/getEmergencydrill.action";
    }

    //应急演练信息详情
    public static String GetEmergencydrilldetail() {
        return host + "mobilecompanyinfo/getEmergencydrilldetail.action";
    }

    //下载文件
    public static String DownLoadFiles() {
        return host + "riskinfo/downLoadFile.action";
    }


    //企业证照
    public static String GetCertificatelistQy() {
        return host + "mobilecompanyinfo/getCertificatelist.action";
    }

    public static String GetCertificatelist() {
        return host + "mobilecompanybase/getCertificatelist.action";
    }

    //企业证照详情
    public static String GetCertificateDetail() {
        return host + "mobilecompanyinfo/getCertificatelistdetail.action";
    }

    //搜索被检查单位
    public static String GetSelectQyname() {
        return host + "mobilecheck/getSelectQyname.action";
    }

    //应急救援领导机构
    public static String Leadership() {
        return host + "mobileEmerResTeam/resourcesldjglist.action";
    }

    //应急领导机构人员
    public static String Ldjgrylist() {
        return host + "mobileEmerResTeam/resourcesldjgrylist.action";
    }

    //应急值守制度
    public static String Recordlist() {
        return host + "mobileEmerResTeam/yjglzdlist.action";
    }

    //应急值守计划
    public static String EmePlanlist() {
        return host + "mobileEmerResTeam/emePlanlist.action";
    }

    //应急值守记录
    public static String Zsjllist() {
        return host + "mobileEmerResTeam/emeRecordlist.action";
    }

    //应急启动事件列表
    public static String EventList() {
        return host + "mobileEmerResTeam/selectEventList.action";
    }

    //值班计划名称列表
    public static String PlanByEvent() {
        return host + "mobileEmerResTeam/selectPlanByEvent.action";
    }

    //新增值班记录
    public static String AddRecordInfo() {
        return host + "mobileEmerResTeam/saveEmeRecordInfo.action";
    }

    //应急救援队伍管理
    public static String Rankslist() {
        return host + "mobileEmerResTeam/rankslist.action";
    }

    //队伍成员
    public static String Emepersonlist() {
        return host + "mobileEmerResTeam/emepersonlist.action";
    }


    //应急救援专家管理
    public static String Expertlist() {
        return host + "mobileEmerResTeam/expertlist.action";
    }

    //外协救援队伍管理
    public static String Externaldeptlist() {
        return host + "mobileEmerResTeam/externaldeptlist.action";
    }

    //应急物资管理
    public static String Vehiclelist() {
        return host + "mobileEmerResTeam/vehiclelist.action";
    }

    //乡镇联系信息
    public static String Townvillageslist() {
        return host + "mobileEmerResTeam/townvillageslist.action";
    }

    //乡镇派出所
    public static String Townpolicelist() {
        return host + "mobileEmerResTeam/townpolicelist.action";
    }

    //医疗救护机构
    public static String Resourceshospitallist() {
        return host + "mobileEmerResTeam/resourceshospitallist.action";
    }

    //事件总结评估
    public static String Summaryevaluationlist() {
        return host + "mobileEmerResTeam/summaryevaluationlist.action";
    }

    //安全检查计划
    public static String ScCheckplan() {
        return host + "mobileScCheckplan/list.action";
    }

    //计划清单
    public static String Detaillist() {
        return host + "mobileScCheckplan/detaillist.action";
    }

    //内容清单
    public static String NrDetailList() {
        return host + "riskzcbzdetail/detailListByZcbzid.action";
    }

    //安全检查记录
    public static String RmSafecheck() {
        return host + "mobileRmSafecheck/list.action";
    }

    //驻矿盯守人员管理
    public static String Zkdsrygl() {
        return host + "mobileStareMine/list.action";
    }

    //新增驻矿盯守人员
    public static String SavePeopleInfo() {
        return host + "mobileStareMine/savePeopleInfo.action";
    }

    //用户列表
    public static String UserTypeList() {
        return host + "mobileStareMine/userTypeList.action";
    }

    //乡镇列表
    public static String TownshipList() {
        return host + "mobileStareMine/townshipList.action";
    }

    //姓名列表
    public static String PeopleList() {
        return host + "mobileStareMine/getPeopleList.action";
    }

    //驻矿企业列表
    public static String ZkqyList() {
        return host + "mobileStareMine/zkqyList.action";
    }

    //工作信息列表
    public static String WorkInfolist() {
        return host + "mobileStareMine/recordlist.action";
    }

    //新增工作信息
    public static String SaveInfo() {
        return host + "mobileStareMine/saveInfo.action";
    }

    //日常监管
    public static String CheckRecordGovAction() {
        return host + "MobileScCheckRecordGovAction/list.action";
    }

    //日常监管企业查询
    public static String SelectQyList() {
        return host + "MobileScCheckRecordGovAction/selectQyList.action";
    }

    //日常监管隐患状态查询
    public static String SelectYhList() {
        return host + "MobileScCheckRecordGovAction/selectYhList.action";
    }

    //新增日常监管信息
    public static String DailySaveInfo() {
        return host + "MobileScCheckRecordGovAction/saveInfo.action";
    }

    //隐患新增所属企业
    public static String AddHiddenSelectQyList() {
        return host + "riskinfo/selectQyList.action";
    }

    //

}
