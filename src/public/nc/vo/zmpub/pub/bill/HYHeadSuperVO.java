package nc.vo.zmpub.pub.bill;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
/**
 * 标准单据主表 vo
 * @author zhf
 *
 */
public abstract class HYHeadSuperVO extends SuperVO {
/**
 * 行业supervo   标准单据信息  10个自定义项   5个pk_defdoc  5个字符串预留项   3个数字预留项  2个布尔预留项
 */
	public String pk_corp; //公司
	public Integer vbillstatus;
	public String vbillno;
	public String pk_billtype;
	public UFDate dbilldate;
	public String pk_busitype;//业务类型
	
	public String vmemo;//备注
	
	public String vemployeeid;//业务员
	
	public UFBoolean fisclose;//  是否作废	
	public UFBoolean fisself;//  是否自制
	
	public String voperatorid;//制单人
	public UFDate dmakedate;
	public String vapproveid;//审批人
	public UFDate dapprovedate;
	public String vapprovenote;
	
	public UFDateTime ts;		
	
	public Integer dr;
//	----------------------------------
	public String vdef1;
	public String vdef2;
	public String vdef3;
	public String vdef4;
	public String vdef5;
	public String vdef6;
	public String vdef7;
	public String vdef8;
	public String vdef9;
	public String vdef10;
	public String vdef11;
	public String vdef12;
	public String vdef13;
	public String vdef14;
	public String vdef15;
	public String vdef16;
	public String vdef17;
	public String vdef18;
	public String vdef19;
	public String vdef20;
	
	public String pk_defdoc1;//资产类别 （成本核算（传固定资产））
	public String pk_defdoc2;
	public String pk_defdoc3;
	public String pk_defdoc4;
	public String pk_defdoc5;
	public String pk_defdoc6;
	public String pk_defdoc7;
	public String pk_defdoc8;
	public String pk_defdoc9;
	public String pk_defdoc10;
	public String pk_defdoc11;
	public String pk_defdoc12;
	public String pk_defdoc13;
	public String pk_defdoc14;
	public String pk_defdoc15;
	public String pk_defdoc16;
	public String pk_defdoc17;
	public String pk_defdoc18;
	public String pk_defdoc19;
	public String pk_defdoc20;

	
	
	public String vreserve1;//验收单  存放会计账簿的临时字段
	public String vreserve2;
	public String vreserve3;
	public String vreserve4;
	public String vreserve5;
	public String vreserve6;
	public String vreserve7;
	public String vreserve8;
	public String vreserve9;
	public String vreserve10;
	
	public UFDouble nreserve1;
	public UFDouble nreserve2;
	public UFDouble nreserve3;
	public UFDouble nreserve4;
	public UFDouble nreserve5;
	public UFDouble nreserve6;
	public UFDouble nreserve7;
	public UFDouble nreserve8;
	public UFDouble nreserve9;
	public UFDouble nreserve10;

	
	public UFBoolean ureserve1;
	public UFBoolean ureserve2;
	public UFBoolean ureserve3;
	public UFBoolean ureserve4;
	public UFBoolean ureserve5;	
	public UFBoolean ureserve6;
	public UFBoolean ureserve7;
	public UFBoolean ureserve8;	
	public UFBoolean ureserve9;
	public UFBoolean ureserve10;
	
	public Integer ireserve1;
	public Integer ireserve2;
	public Integer ireserve3;
	public Integer ireserve4;
	public Integer ireserve5;
	public Integer ireserve6;
	public Integer ireserve7;
	public Integer ireserve8;
	public Integer ireserve9;
	public Integer ireserve10;
	
	
	
	public String getPk_defdoc20() {
		return pk_defdoc20;
	}
	public void setPk_defdoc20(String pk_defdoc20) {
		this.pk_defdoc20 = pk_defdoc20;
	}
	public String getVdef11() {
		return vdef11;
	}
	public void setVdef11(String vdef11) {
		this.vdef11 = vdef11;
	}
	public String getVdef12() {
		return vdef12;
	}
	public void setVdef12(String vdef12) {
		this.vdef12 = vdef12;
	}
	public String getVdef13() {
		return vdef13;
	}
	public void setVdef13(String vdef13) {
		this.vdef13 = vdef13;
	}
	public String getVdef14() {
		return vdef14;
	}
	public void setVdef14(String vdef14) {
		this.vdef14 = vdef14;
	}
	public String getVdef15() {
		return vdef15;
	}
	public void setVdef15(String vdef15) {
		this.vdef15 = vdef15;
	}
	public String getVdef16() {
		return vdef16;
	}
	public void setVdef16(String vdef16) {
		this.vdef16 = vdef16;
	}
	public String getVdef17() {
		return vdef17;
	}
	public void setVdef17(String vdef17) {
		this.vdef17 = vdef17;
	}
	public String getVdef18() {
		return vdef18;
	}
	public void setVdef18(String vdef18) {
		this.vdef18 = vdef18;
	}
	public String getVdef19() {
		return vdef19;
	}
	public void setVdef19(String vdef19) {
		this.vdef19 = vdef19;
	}
	public String getVdef20() {
		return vdef20;
	}
	public void setVdef20(String vdef20) {
		this.vdef20 = vdef20;
	}
	public String getPk_defdoc6() {
		return pk_defdoc6;
	}
	public void setPk_defdoc6(String pk_defdoc6) {
		this.pk_defdoc6 = pk_defdoc6;
	}
	public String getPk_defdoc7() {
		return pk_defdoc7;
	}
	public void setPk_defdoc7(String pk_defdoc7) {
		this.pk_defdoc7 = pk_defdoc7;
	}
	public String getPk_defdoc8() {
		return pk_defdoc8;
	}
	public void setPk_defdoc8(String pk_defdoc8) {
		this.pk_defdoc8 = pk_defdoc8;
	}
	public String getPk_defdoc9() {
		return pk_defdoc9;
	}
	public void setPk_defdoc9(String pk_defdoc9) {
		this.pk_defdoc9 = pk_defdoc9;
	}
	public String getPk_defdoc10() {
		return pk_defdoc10;
	}
	public void setPk_defdoc10(String pk_defdoc10) {
		this.pk_defdoc10 = pk_defdoc10;
	}
	public String getPk_defdoc11() {
		return pk_defdoc11;
	}
	public void setPk_defdoc11(String pk_defdoc11) {
		this.pk_defdoc11 = pk_defdoc11;
	}
	public String getPk_defdoc12() {
		return pk_defdoc12;
	}
	public void setPk_defdoc12(String pk_defdoc12) {
		this.pk_defdoc12 = pk_defdoc12;
	}
	public String getPk_defdoc13() {
		return pk_defdoc13;
	}
	public void setPk_defdoc13(String pk_defdoc13) {
		this.pk_defdoc13 = pk_defdoc13;
	}
	public String getPk_defdoc14() {
		return pk_defdoc14;
	}
	public void setPk_defdoc14(String pk_defdoc14) {
		this.pk_defdoc14 = pk_defdoc14;
	}
	public String getPk_defdoc15() {
		return pk_defdoc15;
	}
	public void setPk_defdoc15(String pk_defdoc15) {
		this.pk_defdoc15 = pk_defdoc15;
	}
	public String getPk_defdoc16() {
		return pk_defdoc16;
	}
	public void setPk_defdoc16(String pk_defdoc16) {
		this.pk_defdoc16 = pk_defdoc16;
	}
	public String getPk_defdoc17() {
		return pk_defdoc17;
	}
	public void setPk_defdoc17(String pk_defdoc17) {
		this.pk_defdoc17 = pk_defdoc17;
	}
	public String getPk_defdoc18() {
		return pk_defdoc18;
	}
	public void setPk_defdoc18(String pk_defdoc18) {
		this.pk_defdoc18 = pk_defdoc18;
	}
	public String getPk_defdoc19() {
		return pk_defdoc19;
	}
	public void setPk_defdoc19(String pk_defdoc19) {
		this.pk_defdoc19 = pk_defdoc19;
	}
	public String getVreserve6() {
		return vreserve6;
	}
	public void setVreserve6(String vreserve6) {
		this.vreserve6 = vreserve6;
	}
	public String getVreserve7() {
		return vreserve7;
	}
	public void setVreserve7(String vreserve7) {
		this.vreserve7 = vreserve7;
	}
	public String getVreserve8() {
		return vreserve8;
	}
	public void setVreserve8(String vreserve8) {
		this.vreserve8 = vreserve8;
	}
	public String getVreserve9() {
		return vreserve9;
	}
	public void setVreserve9(String vreserve9) {
		this.vreserve9 = vreserve9;
	}
	public String getVreserve10() {
		return vreserve10;
	}
	public void setVreserve10(String vreserve10) {
		this.vreserve10 = vreserve10;
	}
	public UFDouble getNreserve4() {
		return nreserve4;
	}
	public void setNreserve4(UFDouble nreserve4) {
		this.nreserve4 = nreserve4;
	}
	public UFDouble getNreserve5() {
		return nreserve5;
	}
	public void setNreserve5(UFDouble nreserve5) {
		this.nreserve5 = nreserve5;
	}
	public UFDouble getNreserve6() {
		return nreserve6;
	}
	public void setNreserve6(UFDouble nreserve6) {
		this.nreserve6 = nreserve6;
	}
	public UFDouble getNreserve7() {
		return nreserve7;
	}
	public void setNreserve7(UFDouble nreserve7) {
		this.nreserve7 = nreserve7;
	}
	public UFDouble getNreserve8() {
		return nreserve8;
	}
	public void setNreserve8(UFDouble nreserve8) {
		this.nreserve8 = nreserve8;
	}
	public UFDouble getNreserve9() {
		return nreserve9;
	}
	public void setNreserve9(UFDouble nreserve9) {
		this.nreserve9 = nreserve9;
	}
	public UFDouble getNreserve10() {
		return nreserve10;
	}
	public void setNreserve10(UFDouble nreserve10) {
		this.nreserve10 = nreserve10;
	}
	public UFBoolean getUreserve4() {
		return ureserve4;
	}
	public void setUreserve4(UFBoolean ureserve4) {
		this.ureserve4 = ureserve4;
	}
	public UFBoolean getUreserve5() {
		return ureserve5;
	}
	public void setUreserve5(UFBoolean ureserve5) {
		this.ureserve5 = ureserve5;
	}
	public UFBoolean getUreserve6() {
		return ureserve6;
	}
	public void setUreserve6(UFBoolean ureserve6) {
		this.ureserve6 = ureserve6;
	}
	public UFBoolean getUreserve7() {
		return ureserve7;
	}
	public void setUreserve7(UFBoolean ureserve7) {
		this.ureserve7 = ureserve7;
	}
	public UFBoolean getUreserve8() {
		return ureserve8;
	}
	public void setUreserve8(UFBoolean ureserve8) {
		this.ureserve8 = ureserve8;
	}
	public UFBoolean getUreserve9() {
		return ureserve9;
	}
	public void setUreserve9(UFBoolean ureserve9) {
		this.ureserve9 = ureserve9;
	}
	public UFBoolean getUreserve10() {
		return ureserve10;
	}
	public void setUreserve10(UFBoolean ureserve10) {
		this.ureserve10 = ureserve10;
	}
	public Integer getIreserve1() {
		return ireserve1;
	}
	public void setIreserve1(Integer ireserve1) {
		this.ireserve1 = ireserve1;
	}
	public Integer getIreserve2() {
		return ireserve2;
	}
	public void setIreserve2(Integer ireserve2) {
		this.ireserve2 = ireserve2;
	}
	public Integer getIreserve3() {
		return ireserve3;
	}
	public void setIreserve3(Integer ireserve3) {
		this.ireserve3 = ireserve3;
	}
	public Integer getIreserve4() {
		return ireserve4;
	}
	public void setIreserve4(Integer ireserve4) {
		this.ireserve4 = ireserve4;
	}
	public Integer getIreserve5() {
		return ireserve5;
	}
	public void setIreserve5(Integer ireserve5) {
		this.ireserve5 = ireserve5;
	}
	public Integer getIreserve6() {
		return ireserve6;
	}
	public void setIreserve6(Integer ireserve6) {
		this.ireserve6 = ireserve6;
	}
	public Integer getIreserve7() {
		return ireserve7;
	}
	public void setIreserve7(Integer ireserve7) {
		this.ireserve7 = ireserve7;
	}
	public Integer getIreserve8() {
		return ireserve8;
	}
	public void setIreserve8(Integer ireserve8) {
		this.ireserve8 = ireserve8;
	}
	public Integer getIreserve9() {
		return ireserve9;
	}
	public void setIreserve9(Integer ireserve9) {
		this.ireserve9 = ireserve9;
	}
	public Integer getIreserve10() {
		return ireserve10;
	}
	public void setIreserve10(Integer ireserve10) {
		this.ireserve10 = ireserve10;
	}
	public String getVmemo() {
		return vmemo;
	}
	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	public String getVemployeeid() {
		return vemployeeid;
	}
	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	public Integer getVbillstatus() {
		return vbillstatus;
	}
	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}
	public String getVbillno() {
		return vbillno;
	}
	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}
	public String getPk_billtype() {
		return pk_billtype;
	}
	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}
	public UFDate getDbilldate() {
		return dbilldate;
	}
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}
	public String getPk_busitype() {
		return pk_busitype;
	}
	public void setPk_busitype(String pk_busitype) {
		this.pk_busitype = pk_busitype;
	}
	public UFBoolean getFisclose() {
		return fisclose;
	}
	public void setFisclose(UFBoolean fisclose) {
		this.fisclose = fisclose;
	}
	public UFBoolean getFisself() {
		return fisself;
	}
	public void setFisself(UFBoolean fisself) {
		this.fisself = fisself;
	}
	public String getVoperatorid() {
		return voperatorid;
	}
	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}
	public UFDate getDmakedate() {
		return dmakedate;
	}
	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}
	public String getVapproveid() {
		return vapproveid;
	}
	public void setVapproveid(String vapproveid) {
		this.vapproveid = vapproveid;
	}
	public UFDate getDapprovedate() {
		return dapprovedate;
	}
	public void setDapprovedate(UFDate dapprovedate) {
		this.dapprovedate = dapprovedate;
	}
	public String getVapprovenote() {
		return vapprovenote;
	}
	public void setVapprovenote(String vapprovenote) {
		this.vapprovenote = vapprovenote;
	}
	public String getVdef1() {
		return vdef1;
	}
	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	public String getVdef2() {
		return vdef2;
	}
	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	public String getVdef3() {
		return vdef3;
	}
	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}
	public String getVdef4() {
		return vdef4;
	}
	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}
	public String getVdef5() {
		return vdef5;
	}
	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}
	public String getVdef6() {
		return vdef6;
	}
	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}
	public String getVdef7() {
		return vdef7;
	}
	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	public String getVdef8() {
		return vdef8;
	}
	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	public String getVdef9() {
		return vdef9;
	}
	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	public String getVdef10() {
		return vdef10;
	}
	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	public String getPk_defdoc1() {
		return pk_defdoc1;
	}
	public void setPk_defdoc1(String pk_defdoc1) {
		this.pk_defdoc1 = pk_defdoc1;
	}
	public String getPk_defdoc2() {
		return pk_defdoc2;
	}
	public void setPk_defdoc2(String pk_defdoc2) {
		this.pk_defdoc2 = pk_defdoc2;
	}
	public String getPk_defdoc3() {
		return pk_defdoc3;
	}
	public void setPk_defdoc3(String pk_defdoc3) {
		this.pk_defdoc3 = pk_defdoc3;
	}
	public String getPk_defdoc4() {
		return pk_defdoc4;
	}
	public void setPk_defdoc4(String pk_defdoc4) {
		this.pk_defdoc4 = pk_defdoc4;
	}
	public String getPk_defdoc5() {
		return pk_defdoc5;
	}
	public void setPk_defdoc5(String pk_defdoc5) {
		this.pk_defdoc5 = pk_defdoc5;
	}
	public String getVreserve1() {
		return vreserve1;
	}
	public void setVreserve1(String vreserve1) {
		this.vreserve1 = vreserve1;
	}
	public String getVreserve2() {
		return vreserve2;
	}
	public void setVreserve2(String vreserve2) {
		this.vreserve2 = vreserve2;
	}
	public String getVreserve3() {
		return vreserve3;
	}
	public void setVreserve3(String vreserve3) {
		this.vreserve3 = vreserve3;
	}
	public String getVreserve4() {
		return vreserve4;
	}
	public void setVreserve4(String vreserve4) {
		this.vreserve4 = vreserve4;
	}
	public String getVreserve5() {
		return vreserve5;
	}
	public void setVreserve5(String vreserve5) {
		this.vreserve5 = vreserve5;
	}
	public UFDouble getNreserve1() {
		return nreserve1;
	}
	public void setNreserve1(UFDouble nreserve1) {
		this.nreserve1 = nreserve1;
	}
	public UFDouble getNreserve2() {
		return nreserve2;
	}
	public void setNreserve2(UFDouble nreserve2) {
		this.nreserve2 = nreserve2;
	}
	public UFDouble getNreserve3() {
		return nreserve3;
	}
	public void setNreserve3(UFDouble nreserve3) {
		this.nreserve3 = nreserve3;
	}
	public UFBoolean getUreserve1() {
		return ureserve1;
	}
	public void setUreserve1(UFBoolean ureserve1) {
		this.ureserve1 = ureserve1;
	}
	public UFBoolean getUreserve2() {
		return ureserve2;
	}
	public void setUreserve2(UFBoolean ureserve2) {
		this.ureserve2 = ureserve2;
	}
	public UFBoolean getUreserve3() {
		return ureserve3;
	}
	public void setUreserve3(UFBoolean ureserve3) {
		this.ureserve3 = ureserve3;
	}

}
