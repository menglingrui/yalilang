package nc.bs.pf.changedir;
import nc.bs.pf.change.VOConversion;
import nc.vo.pf.change.UserDefineFunction;
/**
 * �������ⵥת������ⵥ
 * @author zpm
 *
 */
public class CHG4ITO4A extends VOConversion {

	
	public CHG4ITO4A() {
		
		super();
	}
	/**
	* ��ú������ȫ¼������
	*/
	public String getAfterClassName() {
//		return "nc.bs.ic.sn.chg.AfterChg4IToIn";
		return null;
	}
	/**
	* �����һ���������ȫ¼������
	*/
	public String getOtherClassName() {
		return  null;
	}
	/**
	* ����ֶζ�Ӧ
	*/
	public String[] getField() {
		return new String[] {			
			"H_pk_defdoc20->H_pk_defdoc20",
			"H_pk_defdoc19->H_pk_defdoc19",
			"H_pk_defdoc18->H_pk_defdoc18",
			"H_pk_defdoc17->H_pk_defdoc17",
			"H_pk_defdoc16->H_pk_defdoc16",
			"H_pk_defdoc15->H_pk_defdoc15",
			"H_pk_defdoc14->H_pk_defdoc14",
			"H_pk_defdoc13->H_pk_defdoc13",
			"H_pk_defdoc12->H_pk_defdoc12",
			"H_pk_defdoc11->H_pk_defdoc11",
			"H_pk_defdoc10->H_pk_defdoc10",
			"H_pk_defdoc9->H_pk_defdoc9",
			"H_pk_defdoc8->H_pk_defdoc8",
			"H_pk_defdoc7->H_pk_defdoc7",
			"H_pk_defdoc6->H_pk_defdoc6",
			"H_pk_defdoc5->H_pk_defdoc5",
			"H_pk_defdoc4->H_pk_defdoc4",
			"H_pk_defdoc3->H_pk_defdoc3",
			"H_pk_defdoc2->H_pk_defdoc2",
			"H_pk_defdoc1->H_pk_defdoc1",	
			
			"H_vuserdef20->H_vuserdef20",
			"H_vuserdef19->H_vuserdef19",
			"H_vuserdef18->H_vuserdef18",
			"H_vuserdef17->H_vuserdef17",
			"H_vuserdef16->H_vuserdef16",
			"H_vuserdef15->H_vuserdef15",
			"H_vuserdef14->H_vuserdef14",
			"H_vuserdef13->H_vuserdef13",
			"H_vuserdef12->H_vuserdef12",
			"H_vuserdef11->H_vuserdef11",
			"H_vuserdef10->H_vuserdef10",
			"H_vuserdef9->H_vuserdef9",
			"H_vuserdef8->H_vuserdef8",
			"H_vuserdef7->H_vuserdef7",
			"H_vuserdef6->H_vuserdef6",
			"H_vuserdef5->H_vuserdef5",
			"H_vuserdef4->H_vuserdef4",
			"H_vuserdef3->H_vuserdef3",
			"H_vuserdef2->H_vuserdef2",
			"H_vuserdef1->H_vuserdef1",
			
			"H_coperatorid->SYSOPERATOR",//����Ա
			"H_cwhsmanagerid->H_cwhsmanagerid",//���ԱID
			"H_vnote->H_vnote",//��ע
			"H_pk_corp->H_pk_corp",//��˾ 
			"H_coperatoridnow->SYSOPERATOR",//����Ա,���ݼ���ʹ��
			


			"H_cwarehouseid->H_cinwarehouseid",//�ֿ�ID
			"H_pk_calbody->H_pk_calbody",//�����֯
			"H_dbilldate->SYSDATE",//��������
			"H_cbizid->H_cinbizid",//ҵ��ԱID
			"H_cdptid->H_cindptid",//����ID
			
			"B_vuserdef20->B_vuserdef20",
			"B_vuserdef19->B_vuserdef19",
			"B_vuserdef18->B_vuserdef18",
			"B_vuserdef17->B_vuserdef17",
			"B_vuserdef16->B_vuserdef16",
			"B_vuserdef15->B_vuserdef15",
			"B_vuserdef14->B_vuserdef14",
			"B_vuserdef13->B_vuserdef13",
			"B_vuserdef12->B_vuserdef12",
			"B_vuserdef11->B_vuserdef11",
			"B_vuserdef10->B_vuserdef10",
			"B_vuserdef9->B_vuserdef9",
			"B_vuserdef8->B_vuserdef8",
			"B_vuserdef7->B_vuserdef7",
			"B_vuserdef6->B_vuserdef6",
			"B_vuserdef5->B_vuserdef5",
			"B_vuserdef4->B_vuserdef4",
			"B_vuserdef3->B_vuserdef3",
			"B_vuserdef2->B_vuserdef2",
			"B_vuserdef1->B_vuserdef1",
			
			"B_vfree10->B_vfree10",
			"B_vfree9->B_vfree9",
			"B_vfree8->B_vfree8",
			"B_vfree7->B_vfree7",
			"B_vfree6->B_vfree6",
			"B_vfree5->B_vfree5",
			"B_vfree4->B_vfree4",
			"B_vfree3->B_vfree3",
			"B_vfree2->B_vfree2",
			"B_vfree1->B_vfree1",
			"B_vfree0->B_vfree0",
			

			"B_pk_defdoc20->B_pk_defdoc20",
			"B_pk_defdoc19->B_pk_defdoc19",
			"B_pk_defdoc18->B_pk_defdoc18",
			"B_pk_defdoc17->B_pk_defdoc17",
			"B_pk_defdoc16->B_pk_defdoc16",
			"B_pk_defdoc15->B_pk_defdoc15",
			"B_pk_defdoc14->B_pk_defdoc14",
			"B_pk_defdoc13->B_pk_defdoc13",
			"B_pk_defdoc12->B_pk_defdoc12",
			"B_pk_defdoc11->B_pk_defdoc11",
			"B_pk_defdoc10->B_pk_defdoc10",
			"B_pk_defdoc9->B_pk_defdoc9",
			"B_pk_defdoc8->B_pk_defdoc8",
			"B_pk_defdoc7->B_pk_defdoc7",
			"B_pk_defdoc6->B_pk_defdoc6",
			"B_pk_defdoc5->B_pk_defdoc5",
			"B_pk_defdoc4->B_pk_defdoc4",
			"B_pk_defdoc3->B_pk_defdoc3",
			"B_pk_defdoc2->B_pk_defdoc2",
			"B_pk_defdoc1->B_pk_defdoc1",
			

			"B_csourcebillhid->B_cgeneralhid",
			"B_csourcebillbid->B_cgeneralbid",
			"B_vsourcebillcode->H_vbillcode",
			"B_csourcetype->H_cbilltypecode",
			
			"B_cfirstbillhid->B_cfirstbillhid",//����Դͷ����
			"B_cfirstbillbid->B_cfirstbillbid",//����Դͷ����
			"B_vfirstbillcode->B_vfirstbillcode",//����Դͷ����
			"B_cfirsttype->B_cfirsttype",//����Դͷ����
			
			"B_castunitid->B_castunitid",
			"B_dbizdate->SYSDATE",
			"B_dvalidate->B_dvalidate",
			"B_scrq->B_scrq",
			"B_cinvbasid->B_cinvbasid",
			"B_vbatchcode->B_vbatchcode",
			"B_cprojectphaseid->B_cprojectphaseid",//��Ŀ�׶�ID
			"B_pk_measdoc->B_pk_measdoc",
			"B_cinventoryid->B_cinventoryid",
			"B_vnotebody->B_vnotebody",
			"B_cprojectid->B_cprojectid",//��ĿID
		
			"B_hsl->B_hsl",

			"B_vproductbatch->B_vproductbatch",
			
			"B_nprice->B_nprice",
			"B_nmny->B_nmny",
			
			"B_cvendorid->B_cvendorid",
			"B_vtransfercode->B_vtransfercode",
			"B_ntarenum->B_ntarenum",//Ƥ��������
//			"B_pk_bodycalbody->H_pk_defdoc1",//�����֯
//			"B_pk_corp->H_pk_defdoc2",//���幫˾
			//
			"B_nshouldinnum->B_nshouldoutnum", //Ӧ������ 
			"B_nneedinassistnum->B_nshouldoutassistnum",// Ӧ�븨����
			//
			"B_ninnum->B_noutnum",// ʵ������  modify by yf ����������ʱ����Ҫʵ������
//			"B_ninassistnum->B_noutassistnum",// ʵ�븨����
//			
//			"B_ncorrespondnum->B_noutnum",//�ۼ��������
			 //
		};
	}
	/**
	* ��ù�ʽ
	*/
	public String[] getFormulas() {
		return new String[] {
				"H_cbilltypecode->\"4A\"",
				
//				"B_noutnum->B_ninnum - iif(B_ncorrespondnum==null,0,B_ncorrespondnum)",//�ۼƳ�������
//				"B_noutassistnum->B_ninassistnum-iif(B_ncorrespondastnum==null,0,B_ncorrespondastnum)",//�ۼƳ��⸨����
//				"B_noutgrossnum->B_ningrossnum-iif(B_ncorrespondgrsnum==null,0,B_ncorrespondgrsnum)",//�ۼƳ���ë��
//				"B_nmny->B_nprice*(B_ninnum - iif(B_ncorrespondnum==null,0,B_ncorrespondnum))",//�ۼƳ�������
				
			};
	}
	/**
	* �����û��Զ��庯��
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}