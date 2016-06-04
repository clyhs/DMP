package org.dmp.module.admin.security.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.module.admin.security.dao.BgRRTDao;
import org.dmp.module.admin.security.dao.BgRoleDao;
import org.dmp.module.admin.security.dao.BgRolePermissionDao;
import org.dmp.module.admin.security.dao.BgURRDao;
import org.dmp.module.admin.security.dao.BgUserDao;
import org.dmp.module.common.tree.Tree;
import org.dmp.pojo.admin.security.BgRRT;
import org.dmp.pojo.admin.security.BgRole;
import org.dmp.pojo.admin.security.BgURR;
import org.dmp.pojo.admin.security.BgUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


/**
 * 后台角色管理逻辑层
 * @Author 伍锐凡
 * @Date 2012-3-26
 * @Version 1.0
 * @Remark
 */
@Service("BgRoleService")
@Scope("prototype")
public class BgRoleService
{
	@Resource(name = "BgRoleDao")
	private BgRoleDao m_oBgRoleDao;
	
	@Resource(name = "BgRRTDao")
	private BgRRTDao m_oBgRRTDao;
	
	@Resource(name = "BgURRDao")
	private BgURRDao m_oBgURRDao;
	
	@Resource(name = "BgUserDao")
	private BgUserDao m_oBgUserDao;
	
	@Resource(name = "BgRolePermissionDao")
	private BgRolePermissionDao m_oBgRolePermissionDao;
	
	/**
	 * 获取一个后台角色资料
	 * @param nBgRoleId
	 * @return BgRole
	 */
	public BgRole getBgRole(int nBgRoleId)
	{
		return m_oBgRoleDao.getBgRole(nBgRoleId);
	}
	
	/**
	 * 获取角色与树结点关联数据
	 * @param nBgRoleId
	 * @return List<Integer>
	 */
	public List<Integer> getBgRRTList(int nBgRoleId)
	{
		List<Integer> aTreeId = new ArrayList<Integer>();
		List<BgRRT> aBgRRT = m_oBgRRTDao.getBgRRTList(nBgRoleId);
		for(BgRRT oBgRRT : aBgRRT)
		{
			aTreeId.add(oBgRRT.getBgTreeId());
		}
		return aTreeId;
	}
	
	/**
	 * 获取后台角色资料列表
	 * @param sName
	 * @return List<BgRole>
	 */
	public List<BgRole> getBgRoleList(String sName, int nStart, int nOffset)
	{
		return m_oBgRoleDao.getBgRoleList(sName, nStart, nOffset);
	}
	
	/**
	 * 获取后台角色列表数量
	 * @param sName
	 * @return Long
	 */
	public Long getBgRoleListTotal(String sName)
	{
		return m_oBgRoleDao.getBgRoleListTotal(sName);
	}
	
	/**
	 * 获取后台角色资料列表
	 * @return List<BgRole>
	 */
	public List<BgRole> getBgRoleList()
	{
		return m_oBgRoleDao.getBgRoleList();
	}
	
	/**
	 * 获取后台角色资料树
	 * @param nBgRoleId
	 * @return List<Tree>
	 */
	public List<Tree> getBgRoleTree(int nBgRoleId)
	{
		List<Tree> aTree = new ArrayList<Tree>();
		List<BgRole> aBgRole = getBgRoleList();
		
		if(aBgRole != null && aBgRole.size() > 0)
		{
			for(BgRole oBgRole : aBgRole)
			{
				Tree oTree = new Tree();
				oTree.setText(oBgRole.getName());
				oTree.setId("TreeNav_" + String.valueOf(oBgRole.getBgRoleId()));
				oTree.setCls("file");
				aTree.add(oTree);
			}
		}
		return aTree;
	}	
	
	/**
	 * 增加一个后台角色资料
	 * @param sName
	 * @param sBgTreeIdList
	 * @param sRemark
	 * @return int
	 */
	public int addBgRole(String sName, String sBgTreeIdList, Map map, String sRemark)
	{
		List<BgRole> bgRoleList = checkRepeatByName(sName);    //判断角色名称是否重复
		if(bgRoleList.size() > 0) return 0;
		BgRole oBgRole = new BgRole();
		oBgRole.setName(sName);
		oBgRole.setRemark(sRemark);
		int nTotal = m_oBgRoleDao.addBgRole(oBgRole);    //增加角色
		int nBgRoleId = 0;
		if(nTotal > 0)
		{
			nBgRoleId = oBgRole.getBgRoleId();
			addBgRRT(nBgRoleId, sBgTreeIdList, map);    //增加角色与报表权限的关联关系
		}
		return nBgRoleId;
	}
	
	/**
	 * 修改一个后台角色资料
	 * @param nBgRoleId
	 * @param sName
	 * @param sBgTreeIdList
	 * @param sRemark
	 * @return int
	 */
	public int editBgRole(
		Integer nBgRoleId
		,String sName
		,String sBgTreeIdList
		,Map rightMap
		,String sRemark
		
	)
	{
		List<BgRole> bgRoleList = checkRepeatByName(sName);    //判断名称是否重复
		//如果角色名称重复且角色id不为当前的角色id则返回0
		if(bgRoleList.size() > 0 && !bgRoleList.get(0).getBgRoleId().equals(nBgRoleId)) return 0;
		//修改角色基本信息
		BgRole oBgRole = getBgRole(nBgRoleId);
		oBgRole.setName(sName);
		oBgRole.setRemark(sRemark);
		int nTotal = m_oBgRoleDao.editBgRole(oBgRole);
		if(nTotal > 0)
		{
			m_oBgRRTDao.delBgRRT(nBgRoleId,sBgTreeIdList);
//			addBgRRT(nBgRoleId, sBgTreeIdList);
			editBgRRT(nBgRoleId, sBgTreeIdList, rightMap);
			
			List<BgURR> aBgURR = m_oBgURRDao.getBgURRList(nBgRoleId);
			Set<Integer> aBgUserId = new HashSet<Integer>();
			if(aBgURR != null && aBgURR.size() > 0)
			{
				for(BgURR oBgURR : aBgURR)
				{
					aBgUserId.add(oBgURR.getBgUserId());
				}
			}
			for(Integer nBgUserId : aBgUserId)
			{
				editBgUserRole(nBgUserId);
			}
		}
		return nTotal;
	}
	
	/**
	 * 删除角色列表
	 * 
	 * @param nBgRoleId
	 * @return int
	 */
	public int delBgRole(int nBgRoleId)
	{
		int nTotal = m_oBgRoleDao.delBgRole(getBgRole(nBgRoleId));   //删除角色表中的角色信息
		if(nTotal > 0)
		{
			m_oBgRRTDao.delBgRRT(nBgRoleId);     //删除权限表中与该角色关联的信息
			List<BgURR> aBgURR = m_oBgURRDao.getBgURRList(nBgRoleId);   
			Set<Integer> aBgUserId = new HashSet<Integer>();
			if(aBgURR != null && aBgURR.size() > 0)
			{
				for(BgURR oBgURR : aBgURR)
				{
					aBgUserId.add(oBgURR.getBgUserId());
				}
			}
			m_oBgURRDao.delBgURRByRoleId(nBgRoleId);
			for(Integer nBgUserId : aBgUserId)
			{
				editBgUserRole(nBgUserId);
			}
		}
		return nTotal;
	}
	
	/**
	 * 判断角色是否重复
	 * 
	 * @param nBgRoleId
	 * @return int
	 */
	public int delBgRoleBefore(int nBgRoleId)
	{
		List<BgURR> aBgURR = m_oBgURRDao.getBgURRList(nBgRoleId);
		if(aBgURR != null && aBgURR.size() > 0){
			return 0;
		}
		return 1;
	}
	
	/**
	 * 后台用户归角色
	 * @param sBgUserId
	 * @param nBgRoleId
	 * @return int
	 */
	public int addBgUserToRole(String sBgUserId, int nBgRoleId)
	{
		int nTotal = 0;
		m_oBgURRDao.delBgURRByUR(sBgUserId, nBgRoleId);
		String[] aBgUserId = sBgUserId.split(",");
		for(String sTempBgUserId : aBgUserId)
		{
			Integer nBgUserId = Integer.valueOf(sTempBgUserId);
			BgUser oBgUser = m_oBgUserDao.getBgUser(nBgUserId);
			if(oBgUser == null) continue;
			BgURR oBgURR = m_oBgURRDao.getBgURR(nBgRoleId, nBgUserId);
			if(oBgURR == null)
			{
				BgURR oTempBgURR = new BgURR();
				oTempBgURR.setBgUserId(nBgUserId);
				oTempBgURR.setBgRoleId(nBgRoleId);
				if(m_oBgURRDao.addBgURR(oTempBgURR) > 0) nTotal ++;
			}
			editBgUserRole(nBgUserId);
		}
		return nTotal == 0 ? -1 : nTotal;
	}
	
	/**
	 * 增加角色与树结点的关联记录
	 * @param nBgRoleId
	 * @param sBgTreeIdList
	 * @return int
	 */
	private int addBgRRT(int nBgRoleId, String sBgTreeIdList, Map map)
	{
		int nTotal = 0;
		
		String[] aBgTreeIdList = sBgTreeIdList.split(",");
		if(aBgTreeIdList != null && aBgTreeIdList.length > 0)
		{
			
			for(String sBgTreeId : aBgTreeIdList)
			{
				String pageRight = "";    //页面按钮权限
			    String dataRight = "";    //省份下拉框数据权限
				String rightValue = (String) map.get(sBgTreeId);
				if(rightValue != null){
	                	String[] rightValue1 = rightValue.split(";");
	                	pageRight = rightValue1[0];
	                	dataRight = rightValue1[1];
	            }
				BgRRT oBgRRT = new BgRRT();
				oBgRRT.setBgRoleId(nBgRoleId);
				oBgRRT.setBgTreeId(Integer.valueOf(sBgTreeId));
				if(!pageRight.equals("pageRight")){
					oBgRRT.setsPageRight(pageRight);
             	}else{
             		oBgRRT.setsPageRight("");
             	}
				
				if(!dataRight.equals("-1") && !dataRight.equals("dataRight")){
             		oBgRRT.setsDataRight(dataRight);
             	}else{
             		oBgRRT.setsDataRight("");
             	}
				nTotal += m_oBgRRTDao.addBgRRT(oBgRRT);
			}
		}
		return nTotal == 0 ? -1 : nTotal;
	}
	
	/**
	 * 修改用户角色
	 * @param nBgUserId 
	 * return void
	 */
	private void editBgUserRole(Integer nBgUserId)
	{
		String sBgRoleId = "";
		String sBgRole = "";
		BgUser oBgUser = m_oBgUserDao.getBgUser(nBgUserId);
		if(oBgUser == null) return;
		List<BgRole> aBgRole = m_oBgRoleDao.getBgRoleList(nBgUserId);
		if(aBgRole != null && aBgRole.size() > 0)
		{
			for(int i = 0; i < aBgRole.size(); i ++)
			{
				if(i > 0)
				{
					sBgRoleId += ",";
					sBgRole += ",";
				}
				sBgRoleId += aBgRole.get(i).getBgRoleId();
				sBgRole += aBgRole.get(i).getName();
			}
		}
		oBgUser.setBgRoleId(sBgRoleId);
		oBgUser.setBgRole(sBgRole);
		m_oBgUserDao.editBgUser(oBgUser);
	}
	
	/**
	 * 获取角色管理界面权限选择项
	 * @param 
	 * @return
	 */
	public List<DynaBean> getRoleCheckGroup(int treeId){
		return m_oBgRolePermissionDao.getRoleCheckGroup(treeId);
	}
	
	/**
	 * 获取权限功能关联表中的信息
	 * @param treeId
	 * @return
	 */
	public List<BgRRT> getRolePermissionInfo(int treeId, int roleId){
		return m_oBgRoleDao.getRolePermissionInfo(treeId, roleId);
	}
	
	/**
	 * 修改角色与树结点的关联记录
	 * @param nBgRoleId
	 * @param sBgTreeIdList
	 * @return int
	 */
	private void editBgRRT(int nBgRoleId, String sBgTreeIdList, Map map)
	{

		String[] aBgTreeIdList = sBgTreeIdList.split(",");
		if(aBgTreeIdList != null && aBgTreeIdList.length > 0)
		{
            for(int i = 0;i < aBgTreeIdList.length;i++){

            	List<BgRRT> rrtList = getRolePermissionInfo(Integer.parseInt(aBgTreeIdList[i]), nBgRoleId);
            	//获取树节点的界面及数据权限
                String rightValue = (String) map.get(aBgTreeIdList[i]);
                //如果报表权限之前已存在，则进行修改，否则新建记录
            	if(rrtList.size() > 0){
            		BgRRT oBgRRT = rrtList.get(0);
            		String pageRight = oBgRRT.getsPageRight() == null ? "" : oBgRRT.getsPageRight();
                	String dataRight = oBgRRT.getsDataRight() == null ? "" : oBgRRT.getsDataRight();
//            		oBgRRT.setBgRoleId(nBgRoleId);
//            		oBgRRT.setBgTreeId(Integer.parseInt(aBgTreeIdList[i]));
            		if(rightValue != null){
                     	String[] rightValue1 = rightValue.split(";");
                     	pageRight = rightValue1[0];
                     	dataRight = rightValue1[1];
            		}
            		if(!pageRight.equals("pageRight")){
    					oBgRRT.setsPageRight(pageRight);
                 	}else{
	              		oBgRRT.setsPageRight("");
	              	}
    				
            		//选择了省份权限并点击了确定
            		if(!dataRight.equals("-1") && !dataRight.equals("dataRight")){
            			oBgRRT.setsDataRight(dataRight);
            		}else if(dataRight.equals("dataRight")){
            			oBgRRT.setsDataRight("");
                 	}
                    
            		m_oBgRRTDao.editBgRRT(oBgRRT);
            	}else{
            		BgRRT oBgRRT = new BgRRT();
                	String pageRight = "";
                	String dataRight = "";
            		oBgRRT.setBgRoleId(nBgRoleId);
            		oBgRRT.setBgTreeId(Integer.parseInt(aBgTreeIdList[i]));
           		    if(rightValue != null){
	                  	String[] rightValue1 = rightValue.split(";");
	                  	pageRight = rightValue1[0];
	                  	dataRight = rightValue1[1];
           		    }
	           		if(!pageRight.equals("pageRight")){
	 					oBgRRT.setsPageRight(pageRight);
	              	}else{
	              		oBgRRT.setsPageRight("");
	              	}
	 				
	           	//选择了省份权限并点击了确定
            		if(!dataRight.equals("-1") && !dataRight.equals("dataRight")){
            			oBgRRT.setsDataRight(dataRight);
            		}else{
            			oBgRRT.setsDataRight("");
                 	}
                  
            		m_oBgRRTDao.addBgRRT(oBgRRT);
            	}
            }
		}
	}
	
	/**
	 * 是否打开角色权限界面
	 * @param treeId
	 * @param pageRight
	 * @param dataRight
	 * @return
	 */
	public int getRightCode(int treeId){
		List<DynaBean> rightCodeList = getRoleCheckGroup(treeId);
		if(rightCodeList.size() > 0){
			return 1;
		}
		return 0;
	}
	
	/**
	 * 检查角色名称是否重复
	 * @param sName
	 * @return
	 */
	public List<BgRole> checkRepeatByName(String sName){
		return m_oBgRoleDao.checkRepeatByName(sName);
	}
}
