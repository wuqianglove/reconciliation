/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 *//*

package com.mainsoft.mlp.reconciliation.modules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.mainsoft.mlp.common.utils.IdGen;
import com.mainsoft.mlp.reconciliation.common.enums.LclRuleCfgDistrictEnum;
import com.mainsoft.mlp.reconciliation.common.service.TreeService;
import com.mainsoft.mlp.reconciliation.common.utils.CacheUtils;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.bas.dto.CfgDistrictInfo;
import com.mainsoft.mlp.reconciliation.modules.bas.entity.CfgDistrict;
import com.mainsoft.mlp.reconciliation.modules.bas.web.CfgDistrictService;
import com.mainsoft.mlp.reconciliation.modules.payment.entity.PayCnpConfigure;
import com.mainsoft.mlp.reconciliation.modules.payment.entity.PayEcsConfigure;
import com.mainsoft.mlp.reconciliation.modules.payment.entity.PayUnpConfigure;
import com.mainsoft.mlp.reconciliation.modules.payment.enums.PayPaymentBillBusinessTypeEnum;
import com.mainsoft.mlp.reconciliation.payment.service.PayCnpConfigureService;
import com.mainsoft.mlp.reconciliation.modules.payment.service.PayEcsConfigureService;
import com.mainsoft.mlp.reconciliation.modules.payment.service.PayUnpConfigureService;
import com.mainsoft.mlp.reconciliation.modules.sys.dao.AreaDao;
import com.mainsoft.mlp.reconciliation.modules.sys.dto.AreaInfo;
import com.mainsoft.mlp.reconciliation.modules.sys.entity.Area;
import com.mainsoft.mlp.reconciliation.modules.sys.entity.Merchant;
import com.mainsoft.mlp.reconciliation.modules.sys.entity.Office;
import com.mainsoft.mlp.reconciliation.modules.sys.entity.User;
import com.mainsoft.mlp.reconciliation.modules.sys.utils.UserUtils;

*/
/**
 * 辖区Service
 * @author ThinkGem
 * @version 2014-05-16
 *//*

@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {
	
	//map里key是id(编码)，value是AREA
	public final String CACHE_AREA_MAP = "areaMap";
	//同步时默认的用户名和id
	private final String SYSTEM = "system";

	@Autowired
	private CfgDistrictService cfgDistrictService;
	
	@Autowired
	private AreaDao areaDao;
	
	@Autowired
	private PayUnpConfigureService payUnpConfigureService;
	
	@Autowired
	private PayCnpConfigureService payCnpConfigureService;
	
	@Autowired
	private PayEcsConfigureService payEcsConfigureService;
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
	    super.save(area);
	    CacheUtils.remove(this.CACHE_AREA_MAP);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);	
	}
	
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		CacheUtils.remove(this.CACHE_AREA_MAP);
	}
	
	*/
/**
	 * 同步保存
	 * @param basDevior
	 *//*

	@Transactional(readOnly = false)
	public void syncSave(Area area) {
		Area selectArea = get(area.getId());
		User user = new User();
		user.setName(SYSTEM);
		user.setId(SYSTEM);
		area.setUpdateBy(user);
		area.setCreateBy(user);
		area.setParent(selectArea.getParent());
		area.setParentIds(selectArea.getParentIds());
		//查询如果存在则修改,不存在则添加
		if(selectArea != null && StringUtils.isNoneBlank(selectArea.getId())){
			save(area);
			// 根据areaId删除以前关联的记录
			deleteAgoRecordByAreaId(area.getId());
			// 保存保税功能
			saveCfgDistrict(area.getId(), area.getBndCfgDistrict(), user);
			// 保存自贸功能
			saveCfgDistrict(area.getId(), area.getFtaCfgDistrict(), user);
			// 保存中转功能
			saveCfgDistrict(area.getId(), area.getTsfCfgDistrict(), user);
			// 保存混合开票功能
			saveCfgDistrict(area.getId(), area.getMidCfgDistrict(), user);
			// 保存邮寄收费单功能
			saveCfgDistrict(area.getId(), area.getPosCfgDistrict(), user);
			// 保存启用APP功能
			saveCfgDistrict(area.getId(), area.getUseAppCfgDistrict(), user);
			// 保存拼箱规则
			saveLclRuleCfgDistrict(area.getId(), area.getLclRuleCfgDistrict(), user);
			// 保存代收配置
			saveAfgDistrict(area.getId(), area.getAfgCfgDistrict(), user);
			//提运单自动认定功能 
			saveCfgDistrict(area.getId(), area.getFreCfgDistrict(), user);
			//舱单自动认定功能 
			saveCfgDistrict(area.getId(), area.getManCfgDistrict(), user);
			//备案自动审核
			saveCfgDistrict(area.getId(), area.getAcpCfgDistrict(), user);
			//保存辖区支付方式配置信息
			saveCfgDistrict(area.getId(), area.getCnNotSplitCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getCnSplitCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getUnpB2BCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getUnpB2CCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getEcsPayCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getDisabledSecondSysDistrict(), user);
			//保存银行支付地址  从area实体中获得银行的相关地址
			saveCfgDistrictPayUrl(area.getId(), area.getUnpUrlCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getCnNotSplitUrlCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getCnSplitUrlCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getUnpFileCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getEcsPayUrlCfgDistrict(), user);
			
		}else{
			area.setIsNewRecord(true);
			save(area);
			// 保存辖区配置信息
			saveCfgDistrict(area.getId(), area.getBndCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getFtaCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getTsfCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getMidCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getPosCfgDistrict(), user);
			// 保存拼箱规则
			saveLclRuleCfgDistrict(area.getId(), area.getLclRuleCfgDistrict(), user);
			// 保存代收配置
			saveAfgDistrict(area.getId(), area.getAfgCfgDistrict(), user);
			//提运单自动认定功能 
			saveCfgDistrict(area.getId(), area.getFreCfgDistrict(), user);
			//舱单自动认定功能 
			saveCfgDistrict(area.getId(), area.getManCfgDistrict(), user);
			//备案自动审核
			saveCfgDistrict(area.getId(), area.getAcpCfgDistrict(), user);
			//保存辖区支付方式配置信息
			saveCfgDistrict(area.getId(), area.getCnNotSplitCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getCnSplitCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getUnpB2BCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getUnpB2CCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getEcsPayCfgDistrict(), user);
			saveCfgDistrict(area.getId(), area.getDisabledSecondSysDistrict(), user);
			//保存银行支付地址  从area实体中获得银行的相关地址
			saveCfgDistrictPayUrl(area.getId(), area.getUnpUrlCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getCnNotSplitUrlCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getCnSplitUrlCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getUnpFileCfgDistrict(), user);
			saveCfgDistrictPayUrl(area.getId(), area.getEcsPayUrlCfgDistrict(), user);
		}
	}
	*/
/**
	 * 根据areaId删除以前关联的记录
	 * @author cuiyl
	 * @version 2016-12-13
	 *//*

	public void deleteAgoRecordByAreaId(String areaId) {
		CfgDistrict cfgDistrict = new CfgDistrict();
		cfgDistrict.setDistrict(areaId);
		cfgDistrictService.deleteCfgsByAreaId(cfgDistrict);
	}





	
	*/
/**
	 * 从缓存中获取所有辖区Map
	 * @author gaosong
	 * 2017-03-07
	 * *//*

	@SuppressWarnings("unchecked")
	public Map<String, Area> getAreaMapByCache(){
		Map<String, Area> areaMap = (Map<String, Area>)CacheUtils.get(this.CACHE_AREA_MAP);
		if (areaMap == null){
			//创建cache
			createCache();
			areaMap = (Map<String, Area>)CacheUtils.get(this.CACHE_AREA_MAP);
		}
		return areaMap;
	}
	
	*/
/**
	 * 创建缓存，查询一次数据库同时构建好所有数据的map和按辖区组织的map
	 * @author gaosong
	 * 2017-03-07
	 * *//*

	private void createCache(){
		Map<String, Area> areaMap = Maps.newHashMap();
		for (Area area : dao.findAllList(new Area())){
			areaMap.put(area.getId(), area);
		}
		CacheUtils.put(this.CACHE_AREA_MAP, areaMap);
	}
	
	*/
/**
	 * 根据辖区编码获得所有子辖区
	 * @author liteng	
	 * @version 2017-09-22
	 *//*

	public List<Area> getAreasById(String district){
		return areaDao.getAreasById(district);
	}
	*/
/**
	 * 插入迁移自一期外网库的数据
	 *//*

	public void insertMove(Area area){
		areaDao.insertMove(area);
	}


	
	@Transactional(readOnly = false)
	public void saveMerchant(Merchant merchant){
		//银联个人
		if(merchant.isUnpB2B()){
			//如果...则执行修改，否则执行新建（需要生成ID）
			if(merchant.getUnpB2BGjfCode() != null && !"".equals(merchant.getUnpB2BGjfCode())){
				if(StringUtils.isBlank(merchant.getUnpB2BGjfMerchantCode())){
					//执行删除
					payUnpConfigureService.delete(new PayUnpConfigure(merchant.getUnpB2BGjfCode()));
				}else{
					PayUnpConfigure payUnpConfigure = payUnpConfigureService.get(merchant.getUnpB2BGjfCode());
					payUnpConfigure.setMerchantCode(merchant.getUnpB2BGjfMerchantCode().trim());
					payUnpConfigure.setMerchantName(merchant.getUnpB2BGjfMerchantName().trim());
					//修改
					payUnpConfigureService.save(payUnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getUnpB2BGjfMerchantCode())){
				PayUnpConfigure payUnpConfigure = new PayUnpConfigure();
				payUnpConfigure.setId(IdGen.uuid());
				payUnpConfigure.setDistrict(merchant.getDistrict());
				payUnpConfigure.setAccountType("02");
				payUnpConfigure.setKeyPwd("gdzsmm");
				payUnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE.getCode());
				payUnpConfigure.setMerchantCode(merchant.getUnpB2BGjfMerchantCode().trim());
				payUnpConfigure.setMerchantName(merchant.getUnpB2BGjfMerchantName().trim());
				//分账类型默认为不分账
				payUnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payUnpConfigure.setPublicKey("/payment/unionpay/B2B/acp_prod_verify_sign.cer");
				//私钥设置为默认值
				payUnpConfigure.setPrivateKey("/payment/unionpay/B2B/898440193990097.pfx");
				payUnpConfigure.setDelFlag("0");
				payUnpConfigure.setIsNewRecord(true);
				payUnpConfigureService.save(payUnpConfigure);
			}
			
			if(merchant.getUnpB2BPenaltyCode() != null && !"".equals(merchant.getUnpB2BPenaltyCode())){
				if(StringUtils.isBlank(merchant.getUnpB2BPenaltyMerchantCode())){
					//执行删除
					payUnpConfigureService.delete(new PayUnpConfigure(merchant.getUnpB2BPenaltyCode()));
				}else{
					PayUnpConfigure payUnpConfigure = payUnpConfigureService.get(merchant.getUnpB2BPenaltyCode());
					payUnpConfigure.setMerchantCode(merchant.getUnpB2BPenaltyMerchantCode().trim());
					payUnpConfigure.setMerchantName(merchant.getUnpB2BPenaltyMerchantName().trim());
					//修改
					payUnpConfigureService.save(payUnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getUnpB2BPenaltyMerchantCode())){
				PayUnpConfigure payUnpConfigure = new PayUnpConfigure();
				payUnpConfigure.setId(IdGen.uuid());
				payUnpConfigure.setDistrict(merchant.getDistrict());
				payUnpConfigure.setAccountType("02");
				payUnpConfigure.setKeyPwd("gdzsmm");
				payUnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PENALTYANDCONFISCATORYINCOME.getCode());
				payUnpConfigure.setMerchantCode(merchant.getUnpB2BPenaltyMerchantCode().trim());
				payUnpConfigure.setMerchantName(merchant.getUnpB2BPenaltyMerchantName().trim());
				//分账类型默认为不分账
				payUnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payUnpConfigure.setPublicKey("/payment/unionpay/B2B/acp_prod_verify_sign.cer");
				//私钥设置为默认值
				payUnpConfigure.setPrivateKey("/payment/unionpay/B2B/898440193990097.pfx");
				payUnpConfigure.setDelFlag("0");
				payUnpConfigure.setIsNewRecord(true);
				payUnpConfigureService.save(payUnpConfigure);
			}
			
			if(merchant.getUnpB2BExamineCode() != null && !"".equals(merchant.getUnpB2BExamineCode())){
				if(StringUtils.isBlank(merchant.getUnpB2BExamineMerchantCode())){
					payUnpConfigureService.delete(new PayUnpConfigure(merchant.getUnpB2BExamineCode()));
				}else{
					PayUnpConfigure payUnpConfigure = payUnpConfigureService.get(merchant.getUnpB2BExamineCode());
					payUnpConfigure.setMerchantCode(merchant.getUnpB2BExamineMerchantCode().trim());
					payUnpConfigure.setMerchantName(merchant.getUnpB2BExamineMerchantName().trim());
					//修改
					payUnpConfigureService.save(payUnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getUnpB2BExamineMerchantCode())){
				PayUnpConfigure payUnpConfigure = new PayUnpConfigure();
				payUnpConfigure.setId(IdGen.uuid());
				payUnpConfigure.setDistrict(merchant.getDistrict());
				payUnpConfigure.setAccountType("02");
				payUnpConfigure.setKeyPwd("gdzsmm");
				payUnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.EXAMINATIONFORTHECREW.getCode());
				payUnpConfigure.setMerchantCode(merchant.getUnpB2BExamineMerchantCode().trim());
				payUnpConfigure.setMerchantName(merchant.getUnpB2BExamineMerchantName().trim());
				//分账类型默认为不分账
				payUnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payUnpConfigure.setPublicKey("/payment/unionpay/B2B/acp_prod_verify_sign.cer");
				//私钥设置为默认值
				payUnpConfigure.setPrivateKey("/payment/unionpay/B2B/898440193990097.pfx");
				payUnpConfigure.setDelFlag("0");
				payUnpConfigure.setIsNewRecord(true);
				payUnpConfigureService.save(payUnpConfigure);
			}
			
		}
		//银联企业
		if(merchant.isUnpB2C()){
			if(merchant.getUnpB2CGjfCode() != null && !"".equals(merchant.getUnpB2CGjfCode())){
				if(StringUtils.isBlank(merchant.getUnpB2CGjfMerchantCode())){
					payUnpConfigureService.delete(new PayUnpConfigure(merchant.getUnpB2CGjfCode()));
				}else{
					PayUnpConfigure payUnpConfigure = payUnpConfigureService.get(merchant.getUnpB2CGjfCode());
					payUnpConfigure.setMerchantCode(merchant.getUnpB2CGjfMerchantCode().trim());
					payUnpConfigure.setMerchantName(merchant.getUnpB2CGjfMerchantName().trim());
					//修改
					payUnpConfigureService.save(payUnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getUnpB2CGjfMerchantCode())){
				PayUnpConfigure payUnpConfigure = new PayUnpConfigure();
				payUnpConfigure.setId(IdGen.uuid());
				payUnpConfigure.setDistrict(merchant.getDistrict());
				payUnpConfigure.setAccountType("01");
				payUnpConfigure.setKeyPwd("gdzsmm");
				payUnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE.getCode());
				payUnpConfigure.setMerchantCode(merchant.getUnpB2CGjfMerchantCode().trim());
				payUnpConfigure.setMerchantName(merchant.getUnpB2CGjfMerchantName().trim());
				//分账类型默认为不分账
				payUnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payUnpConfigure.setPublicKey("/payment/unionpay/B2C/acp_prod_verify_sign.cer");
				//私钥设置为默认值
				payUnpConfigure.setPrivateKey("/payment/unionpay/B2C/898440193990097.pfx");
				payUnpConfigure.setDelFlag("0");
				payUnpConfigure.setIsNewRecord(true);
				payUnpConfigureService.save(payUnpConfigure);
			}
			
			if(merchant.getUnpB2CPenaltyCode() != null && !"".equals(merchant.getUnpB2CPenaltyCode())){
				if(StringUtils.isBlank(merchant.getUnpB2CPenaltyMerchantCode())){
					payUnpConfigureService.delete(new PayUnpConfigure(merchant.getUnpB2CPenaltyCode()));
				}else{
					PayUnpConfigure payUnpConfigure = payUnpConfigureService.get(merchant.getUnpB2CPenaltyCode());
					payUnpConfigure.setMerchantCode(merchant.getUnpB2CPenaltyMerchantCode().trim());
					payUnpConfigure.setMerchantName(merchant.getUnpB2CPenaltyMerchantName().trim());
					//修改
					payUnpConfigureService.save(payUnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getUnpB2CPenaltyMerchantCode())){
				PayUnpConfigure payUnpConfigure = new PayUnpConfigure();
				payUnpConfigure.setId(IdGen.uuid());
				payUnpConfigure.setDistrict(merchant.getDistrict());
				payUnpConfigure.setAccountType("01");
				payUnpConfigure.setKeyPwd("gdzsmm");
				payUnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PENALTYANDCONFISCATORYINCOME.getCode());
				payUnpConfigure.setMerchantCode(merchant.getUnpB2CPenaltyMerchantCode().trim());
				payUnpConfigure.setMerchantName(merchant.getUnpB2CPenaltyMerchantName().trim());
				//分账类型默认为不分账
				payUnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payUnpConfigure.setPublicKey("/payment/unionpay/B2C/acp_prod_verify_sign.cer");
				//私钥设置为默认值
				payUnpConfigure.setPrivateKey("/payment/unionpay/B2C/898440193990097.pfx");
				payUnpConfigure.setDelFlag("0");
				payUnpConfigure.setIsNewRecord(true);
				payUnpConfigureService.save(payUnpConfigure);
			}
			
			if(merchant.getUnpB2CExamineCode() != null && !"".equals(merchant.getUnpB2CExamineCode())){
				if(StringUtils.isBlank(merchant.getUnpB2CExamineMerchantCode())){
					payUnpConfigureService.delete(new PayUnpConfigure(merchant.getUnpB2CExamineCode()));
				}else{
					PayUnpConfigure payUnpConfigure = payUnpConfigureService.get(merchant.getUnpB2CExamineCode());
					payUnpConfigure.setMerchantCode(merchant.getUnpB2CExamineMerchantCode().trim());
					payUnpConfigure.setMerchantName(merchant.getUnpB2CExamineMerchantName().trim());
					//修改
					payUnpConfigureService.save(payUnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getUnpB2CExamineMerchantCode())){
				PayUnpConfigure payUnpConfigure = new PayUnpConfigure();
				payUnpConfigure.setId(IdGen.uuid());
				payUnpConfigure.setDistrict(merchant.getDistrict());
				payUnpConfigure.setAccountType("01");
				payUnpConfigure.setKeyPwd("gdzsmm");
				payUnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.EXAMINATIONFORTHECREW.getCode());
				payUnpConfigure.setMerchantCode(merchant.getUnpB2CExamineMerchantCode().trim());
				payUnpConfigure.setMerchantName(merchant.getUnpB2CExamineMerchantName().trim());
				//分账类型默认为不分账
				payUnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payUnpConfigure.setPublicKey("/payment/unionpay/B2C/acp_prod_verify_sign.cer");
				//私钥设置为默认值
				payUnpConfigure.setPrivateKey("/payment/unionpay/B2C/898440193990097.pfx");
				payUnpConfigure.setDelFlag("0");
				payUnpConfigure.setIsNewRecord(true);
				payUnpConfigureService.save(payUnpConfigure);
			}
		}
		if(merchant.isCnpCard()){
			if(merchant.getCnpCardGjfCode() != null && !"".equals(merchant.getCnpCardGjfCode())){
				if(StringUtils.isBlank(merchant.getCnpCardGjfMerchantCode())){
					payCnpConfigureService.delete(new PayCnpConfigure(merchant.getCnpCardGjfCode()));
				}else{
					PayCnpConfigure payCnpConfigure = payCnpConfigureService.get(merchant.getCnpCardGjfCode());
					payCnpConfigure.setMerchantCode(merchant.getCnpCardGjfMerchantCode().trim());
					payCnpConfigure.setMerchantName(merchant.getCnpCardGjfMerchantName().trim());
					payCnpConfigureService.save(payCnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getCnpCardGjfMerchantCode())){
				PayCnpConfigure payCnpConfigure = new PayCnpConfigure();
				payCnpConfigure.setId(IdGen.uuid());
				payCnpConfigure.setDistrict(merchant.getDistrict());
				payCnpConfigure.setAccountType("02");
				payCnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE.getCode());
				payCnpConfigure.setMerchantCode(merchant.getCnpCardGjfMerchantCode().trim());
				payCnpConfigure.setMerchantName(merchant.getCnpCardGjfMerchantName().trim());
				//分账类型默认为不分账
				payCnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payCnpConfigure.setPublicKey("/payment/chinapay/PgPubk.key");
				//私钥设置为默认值
				payCnpConfigure.setPrivateKey("/payment/chinapay/MerPrK_808080090993328_20130909141904.key");
				payCnpConfigure.setDelFlag("0");
				payCnpConfigure.setIsNewRecord(true);
				payCnpConfigureService.save(payCnpConfigure);
			}
			
			if(merchant.getCnpCardPenaltyCode() != null && !"".equals(merchant.getCnpCardPenaltyCode())){
				if(StringUtils.isBlank(merchant.getCnpCardPenaltyMerchantCode())){
					payCnpConfigureService.delete(new PayCnpConfigure(merchant.getCnpCardPenaltyCode()));
				}else{
					PayCnpConfigure payCnpConfigure = payCnpConfigureService.get(merchant.getCnpCardPenaltyCode());
					payCnpConfigure.setMerchantCode(merchant.getCnpCardPenaltyMerchantCode().trim());
					payCnpConfigure.setMerchantName(merchant.getCnpCardPenaltyMerchantName().trim());
					payCnpConfigureService.save(payCnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getCnpCardPenaltyMerchantCode())){
				PayCnpConfigure payCnpConfigure = new PayCnpConfigure();
				payCnpConfigure.setId(IdGen.uuid());
				payCnpConfigure.setDistrict(merchant.getDistrict());
				payCnpConfigure.setAccountType("02");
				payCnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PENALTYANDCONFISCATORYINCOME.getCode());
				payCnpConfigure.setMerchantCode(merchant.getCnpCardPenaltyMerchantCode().trim());
				payCnpConfigure.setMerchantName(merchant.getCnpCardPenaltyMerchantName().trim());
				//分账类型默认为不分账
				payCnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payCnpConfigure.setPublicKey("/payment/chinapay/PgPubk.key");
				//私钥设置为默认值
				payCnpConfigure.setPrivateKey("/payment/chinapay/MerPrK_808080090993328_20130909141904.key");
				payCnpConfigure.setDelFlag("0");
				payCnpConfigure.setIsNewRecord(true);
				payCnpConfigureService.save(payCnpConfigure);
			}
			
			if(merchant.getCnpCardExamineCode() != null && !"".equals(merchant.getCnpCardExamineCode())){
				if(StringUtils.isBlank(merchant.getCnpCardExamineMerchantCode())){
					payCnpConfigureService.delete(new PayCnpConfigure(merchant.getCnpCardExamineCode()));
				}else{
					PayCnpConfigure payCnpConfigure = payCnpConfigureService.get(merchant.getCnpCardExamineCode());
					payCnpConfigure.setMerchantCode(merchant.getCnpCardExamineMerchantCode().trim());
					payCnpConfigure.setMerchantName(merchant.getCnpCardExamineMerchantName().trim());
					payCnpConfigureService.save(payCnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getCnpCardExamineMerchantCode())){
				PayCnpConfigure payCnpConfigure = new PayCnpConfigure();
				payCnpConfigure.setId(IdGen.uuid());
				payCnpConfigure.setDistrict(merchant.getDistrict());
				payCnpConfigure.setAccountType("02");
				payCnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.EXAMINATIONFORTHECREW.getCode());
				payCnpConfigure.setMerchantCode(merchant.getCnpCardExamineMerchantCode().trim());
				payCnpConfigure.setMerchantName(merchant.getCnpCardExamineMerchantName().trim());
				//分账类型默认为不分账
				payCnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payCnpConfigure.setPublicKey("/payment/chinapay/PgPubk.key");
				//私钥设置为默认值
				payCnpConfigure.setPrivateKey("/payment/chinapay/MerPrK_808080090993328_20130909141904.key");
				payCnpConfigure.setDelFlag("0");
				payCnpConfigure.setIsNewRecord(true);
				payCnpConfigureService.save(payCnpConfigure);
			}
		}
		if(merchant.isCnpCompany()){
			if(merchant.getCnpCompanyGjfCode() != null && !"".equals(merchant.getCnpCompanyGjfCode())){
				if(StringUtils.isBlank(merchant.getCnpCompanyGjfMerchantCode())){
					payCnpConfigureService.delete(new PayCnpConfigure(merchant.getCnpCompanyGjfCode()));
				}else{
					PayCnpConfigure payCnpConfigure = payCnpConfigureService.get(merchant.getCnpCompanyGjfCode());
					payCnpConfigure.setMerchantCode(merchant.getCnpCompanyGjfMerchantCode().trim());
					payCnpConfigure.setMerchantName(merchant.getCnpCompanyGjfMerchantName().trim());
					payCnpConfigureService.save(payCnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getCnpCompanyGjfMerchantCode())){
				PayCnpConfigure payCnpConfigure = new PayCnpConfigure();
				payCnpConfigure.setId(IdGen.uuid());
				payCnpConfigure.setDistrict(merchant.getDistrict());
				payCnpConfigure.setAccountType("01");
				payCnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE.getCode());
				payCnpConfigure.setMerchantCode(merchant.getCnpCompanyGjfMerchantCode().trim());
				payCnpConfigure.setMerchantName(merchant.getCnpCompanyGjfMerchantName().trim());
				//分账类型默认为不分账
				payCnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payCnpConfigure.setPublicKey("/payment/chinapay/PgPubk.key");
				//私钥设置为默认值
				payCnpConfigure.setPrivateKey("/payment/chinapay/MerPrK_808080090993328_20130909141904.key");
				payCnpConfigure.setDelFlag("0");
				payCnpConfigure.setIsNewRecord(true);
				payCnpConfigureService.save(payCnpConfigure);
			}
			
			if(merchant.getCnpCompanyPenaltyCode() != null && !"".equals(merchant.getCnpCompanyPenaltyCode())){
				if(StringUtils.isBlank(merchant.getCnpCompanyPenaltyMerchantCode())){
					payCnpConfigureService.delete(new PayCnpConfigure(merchant.getCnpCompanyPenaltyCode()));
				}else{
					PayCnpConfigure payCnpConfigure = payCnpConfigureService.get(merchant.getCnpCompanyPenaltyCode());
					payCnpConfigure.setMerchantCode(merchant.getCnpCompanyPenaltyMerchantCode().trim());
					payCnpConfigure.setMerchantName(merchant.getCnpCompanyPenaltyMerchantName().trim());
					payCnpConfigureService.save(payCnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getCnpCompanyPenaltyMerchantCode())){
				PayCnpConfigure payCnpConfigure = new PayCnpConfigure();
				payCnpConfigure.setId(IdGen.uuid());
				payCnpConfigure.setDistrict(merchant.getDistrict());
				payCnpConfigure.setAccountType("01");
				payCnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PENALTYANDCONFISCATORYINCOME.getCode());
				payCnpConfigure.setMerchantCode(merchant.getCnpCompanyPenaltyMerchantCode().trim());
				payCnpConfigure.setMerchantName(merchant.getCnpCompanyPenaltyMerchantName().trim());
				//分账类型默认为不分账
				payCnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payCnpConfigure.setPublicKey("/payment/chinapay/PgPubk.key");
				//私钥设置为默认值
				payCnpConfigure.setPrivateKey("/payment/chinapay/MerPrK_808080090993328_20130909141904.key");
				payCnpConfigure.setDelFlag("0");
				payCnpConfigure.setIsNewRecord(true);
				payCnpConfigureService.save(payCnpConfigure);
			}
			
			if(merchant.getCnpCompanyExamineCode() != null && !"".equals(merchant.getCnpCompanyExamineCode())){
				if(StringUtils.isBlank(merchant.getCnpCompanyExamineMerchantCode())){
					payCnpConfigureService.delete(new PayCnpConfigure(merchant.getCnpCompanyExamineCode()));
				}else{
					PayCnpConfigure payCnpConfigure = payCnpConfigureService.get(merchant.getCnpCompanyExamineCode());
					payCnpConfigure.setMerchantCode(merchant.getCnpCompanyExamineMerchantCode().trim());
					payCnpConfigure.setMerchantName(merchant.getCnpCompanyExamineMerchantName().trim());
					payCnpConfigureService.save(payCnpConfigure);
				}
			}else if(StringUtils.isNoneBlank(merchant.getCnpCompanyExamineMerchantCode())){
				PayCnpConfigure payCnpConfigure = new PayCnpConfigure();
				payCnpConfigure.setId(IdGen.uuid());
				payCnpConfigure.setDistrict(merchant.getDistrict());
				payCnpConfigure.setAccountType("01");
				payCnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.EXAMINATIONFORTHECREW.getCode());
				payCnpConfigure.setMerchantCode(merchant.getCnpCompanyExamineMerchantCode().trim());
				payCnpConfigure.setMerchantName(merchant.getCnpCompanyExamineMerchantName().trim());
				//分账类型默认为不分账
				payCnpConfigure.setSplitType(0);
				//公钥设置为默认值
				payCnpConfigure.setPublicKey("/payment/chinapay/PgPubk.key");
				//私钥设置为默认值
				payCnpConfigure.setPrivateKey("/payment/chinapay/MerPrK_808080090993328_20130909141904.key");
				payCnpConfigure.setDelFlag("0");
				payCnpConfigure.setIsNewRecord(true);
				payCnpConfigureService.save(payCnpConfigure);
			}
		}
		if(merchant.isEcs()){
			if(merchant.getEcsCode() != null && !"".equals(merchant.getEcsCode())){
				if(StringUtils.isBlank(merchant.getEcsPayeeCode())
						|| StringUtils.isBlank(merchant.getEcsPayeeAccount())
						|| StringUtils.isBlank(merchant.getEcsAccountBank())
						|| StringUtils.isBlank(merchant.getEcsAccountName())){
					payEcsConfigureService.deletePhysical(new PayEcsConfigure(merchant.getEcsCode()));
				}else{
					PayEcsConfigure payEcsConfigure = payEcsConfigureService.get(merchant.getEcsCode());
					payEcsConfigure.setPayeeCode(merchant.getEcsPayeeCode().trim());
					payEcsConfigure.setPayeeAccount(merchant.getEcsPayeeAccount().trim());
					payEcsConfigure.setAccountBank(merchant.getEcsAccountBank().trim());
					payEcsConfigure.setAccountName(merchant.getEcsAccountName().trim());
					payEcsConfigureService.save(payEcsConfigure);
				}
			}else if(StringUtils.isNotBlank(merchant.getEcsPayeeCode())
						&& StringUtils.isNotBlank(merchant.getEcsPayeeAccount())
						&& StringUtils.isNotBlank(merchant.getEcsAccountBank())
						&& StringUtils.isNotBlank(merchant.getEcsAccountName())){
				PayEcsConfigure payEcsConfigure = new PayEcsConfigure();
				payEcsConfigure.setId(IdGen.uuid());
				payEcsConfigure.setDistrict(merchant.getDistrict());
				//业务类型编码设置为默认值
				payEcsConfigure.setBizTypeCode("PORJSF");
				//业务类型名称设置为默认值
				payEcsConfigure.setBizTypeName("港口建设费");
				payEcsConfigure.setPayeeCode(merchant.getEcsPayeeCode().trim());
				payEcsConfigure.setPayeeAccount(merchant.getEcsPayeeAccount().trim());
				payEcsConfigure.setAccountBank(merchant.getEcsAccountBank().trim());
				payEcsConfigure.setAccountName(merchant.getEcsAccountName().trim());
				//账号币种设置为默认值
				payEcsConfigure.setCurrency("CNY");
				payEcsConfigure.setDelFlag("0");
				payEcsConfigure.setIsNewRecord(true);
				payEcsConfigureService.save(payEcsConfigure);
			}
		}
	}
	

	
	*/
/**
	 * 查询所有辖区
	 *//*

	public List<Area> findAllArea(){
		return areaDao.findAllArea();
	}
}
*/
