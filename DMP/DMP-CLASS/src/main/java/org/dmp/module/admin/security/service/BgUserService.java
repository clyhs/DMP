package org.dmp.module.admin.security.service;

import java.util.List;

import javax.annotation.Resource;

import org.dmp.core.util.AlgorithmUitl;
import org.dmp.module.admin.security.dao.BgURPDao;
import org.dmp.module.admin.security.dao.BgURRDao;
import org.dmp.module.admin.security.dao.BgUserDao;
import org.dmp.pojo.admin.security.BgURP;
import org.dmp.pojo.admin.security.BgURR;
import org.dmp.pojo.admin.security.BgUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


/**
 * 后台用户管理逻辑层
 * @Author 伍锐凡
 * @Date 2012-3-26
 * @Version 1.0
 * @Remark
 */
@Service("BgUserService")
@Scope("prototype")
public class BgUserService
{
	@Resource(name = "BgUserDao")
	private BgUserDao m_oBgUserDao;
	
	@Resource(name = "BgURRDao")
	private BgURRDao m_oBgURRDao;
	
	@Resource(name = "BgURPDao")
	private BgURPDao m_oBgURPDao;
	
	/**
	 * 获取一个后台用户资料
	 * 
	 * @param nBgUserId
	 * @return BgUser
	 */
	public BgUser getBgUser(int nBgUserId)
	{
		BgUser oBgUser = m_oBgUserDao.getBgUser(nBgUserId);
		if(nBgUserId == 1)
		{
			oBgUser.setBgRoleId("0");
			oBgUser.setBgRole("全部");
		}
		return oBgUser;
	}
	
	/**
	 * 通过帐号和密码获取后台用户资料
	 * 
	 * @param sAccount
	 * @param sPassword
	 * @return BgUser
	 */
	public BgUser getBgUser(String sAccount, String sPassword)
	{
		return m_oBgUserDao.getBgUser(sAccount, AlgorithmUitl.md5(sPassword));
	}
	
	/**
	 * 通过帐号获取用户资料
	 * @param sAccount
	 * @return BgUser
	 */
	public BgUser getBgUser(String sAccount)
	{
		return m_oBgUserDao.getBgUser(sAccount);
	}
	
	/**
	 * 获取后台用户资料列表
	 * @param nBgRoleId
	 * @param sName
	 * @param sAccount
	 * @param nStart
	 * @param nOffset
	 * @return List<BgUser>
	 */
	public List<BgUser> getBgUserList(Integer nBgRoleId, String sName, String sAccount,
			int nStart, int nOffset)
	{
		return m_oBgUserDao.getBgUserList(nBgRoleId, sName, sAccount, nStart, nOffset);
	}
	
	/**
	 * 获取后台用户列表总数
	 * @param nBgRoleId
	 * @param sName
	 * @param sAccount
	 * @return Long
	 */
	public Long getBgUserListTotal(Integer nBgRoleId, String sName, String sAccount)
	{
		return m_oBgUserDao.getBgUserListTotal(nBgRoleId, sName, sAccount);
	}
	
	/**
	 * 增加一个后台用户资料
	 * @param sAccount
	 * @param sPassword
	 * @param sName
	 * @param nBgDeptId
	 * @param sDeptName
	 * @param sBgRoleId
	 * @param sBgRole
	 * @param sBgPostId
	 * @param sBgPost
	 * @param nSex
	 * @param sQQ
	 * @param sEmail
	 * @param sMobile
	 * @param sTel
	 * @param sFax
	 * @param sZip
	 * @param sAddress
	 * @param sBrief
	 * @return int
	 */
	public int addBgUser(
		 String sAccount
		,String sPassword
		,String sName
		,Integer nBgDeptId
		,String sDeptName
		,String sBgRoleId
		,String sBgRole
		,String sBgPostId
		,String sBgPost
		,Integer nSex
		,String sQQ
		,String sEmail
		,String sMobile
		,String sTel
		,String sFax
		,String sZip
		,String sAddress
		,String sBrief			
	)
	{
		BgUser oBgUser = new BgUser();
		oBgUser.setAccount(sAccount);
		oBgUser.setPassword(AlgorithmUitl.md5(sPassword));
		oBgUser.setName(sName);
		oBgUser.setBgDeptId(nBgDeptId);
		oBgUser.setDeptName(sDeptName);
		oBgUser.setBgRoleId(sBgRoleId);
		oBgUser.setBgRole(sBgRole);
		oBgUser.setBgPostId(sBgPostId);
		oBgUser.setBgPost(sBgPost);
		oBgUser.setSex(nSex);
		oBgUser.setQQ(sQQ);
		oBgUser.setEmail(sEmail);
		oBgUser.setMobile(sMobile);
		oBgUser.setTel(sTel);
		oBgUser.setFax(sFax);
		oBgUser.setZip(sZip);
		oBgUser.setAddress(sAddress);
		oBgUser.setBrief(sBrief);
		int nTotal = m_oBgUserDao.addBgUser(oBgUser);
		if(nTotal > 0)
		{
			addPost(sBgPostId, oBgUser.getBgUserId());
			addRole(sBgRoleId, oBgUser.getBgUserId());
		}
		return nTotal;
	}
	
	/**
	 * 修改一个后台用户资料
	 * @param nBgUserId
	 * @param sPassword
	 * @param sName
	 * @param nBgDeptId
	 * @param sDeptName
	 * @param sBgRoleId
	 * @param sBgRole
	 * @param sBgPostId
	 * @param sBgPost
	 * @param nSex
	 * @param sQQ
	 * @param sEmail
	 * @param sMobile
	 * @param sTel
	 * @param sFax
	 * @param sZip
	 * @param sAddress
	 * @param sBrief
	 * @return int
	 */
	public int editBgUser(
		Integer nBgUserId
		,String sPassword
		,String sName
		,Integer nBgDeptId
		,String sDeptName
		,String sBgRoleId
		,String sBgRole
		,String sBgPostId
		,String sBgPost		
		,Integer nSex
		,String sQQ
		,String sEmail
		,String sMobile
		,String sTel
		,String sFax
		,String sZip
		,String sAddress
		,String sBrief
	)
	{
		Boolean bIsRoleEdit = Boolean.FALSE;
		Boolean bIsPostEdit = Boolean.FALSE;
		BgUser oBgUser = getBgUser(nBgUserId);
		if(!sPassword.equals("**********"))
		{
			oBgUser.setPassword(AlgorithmUitl.md5(sPassword));
		}
		oBgUser.setName(sName);
		if(nBgDeptId != 0 && nBgDeptId != oBgUser.getBgDeptId())
		{
			oBgUser.setBgDeptId(nBgDeptId);
			oBgUser.setDeptName(sDeptName);
		}
		if(!sBgRoleId.isEmpty() && !sBgRoleId.equals(oBgUser.getBgRoleId()) && nBgUserId != 1)
		{
			bIsRoleEdit = Boolean.TRUE;
			oBgUser.setBgRoleId(sBgRoleId);
			oBgUser.setBgRole(sBgRole);
		}
		if(!sBgPostId.isEmpty() && !sBgPostId.equals(oBgUser.getBgPostId()))
		{
			bIsPostEdit = Boolean.TRUE;
			oBgUser.setBgPostId(sBgPostId);
			oBgUser.setBgPost(sBgPost);
		}
		oBgUser.setSex(nSex);
		oBgUser.setQQ(sQQ);
		oBgUser.setEmail(sEmail);
		oBgUser.setMobile(sMobile);
		oBgUser.setTel(sTel);
		oBgUser.setFax(sFax);
		oBgUser.setZip(sZip);
		oBgUser.setAddress(sAddress);
		oBgUser.setBrief(sBrief);
		int nTotal = m_oBgUserDao.editBgUser(oBgUser);
		if(nTotal > 0)
		{
			if(bIsRoleEdit)
			{
				addRole(sBgRoleId, oBgUser.getBgUserId());
			}
			if(bIsPostEdit)
			{
				addPost(sBgPostId, oBgUser.getBgUserId());
			}
		}
		return nTotal;
	}
	
	/**
	 * 修改登录用户密码
	 * @param nBgUserId
	 * @param sPassword
	 * @return int
	 */
	public int editMyBgUserPW(Integer nBgUserId, String sPassword)
	{
		BgUser oBgUser = getBgUser(nBgUserId);
		oBgUser.setPassword(AlgorithmUitl.md5(sPassword));
		return m_oBgUserDao.editBgUser(oBgUser);
	}
	
	/**
	 * 删除用户列表
	 * 
	 * @param sBgUserIdList，ID之间用逗号隔开
	 * @return int
	 */
	public int delBgUserList(String sBgUserIdList)
	{
		int nTotal = m_oBgUserDao.delBgUserList(sBgUserIdList);
		if(nTotal > 0)
		{
			m_oBgURRDao.delBgURRByUser(sBgUserIdList);
		}
		return nTotal;
	}
	
	/**
	 * 增加角色
	 * @param sBgRoleId
	 * @param nBgUserId 
	 * return void
	 */
	private void addRole(String sBgRoleId, Integer nBgUserId)
	{
		if(!sBgRoleId.isEmpty() && nBgUserId != 1)
		{
			m_oBgURRDao.delBgURRByUser(String.valueOf(nBgUserId));
			String[] aBgRoleId = sBgRoleId.split(",");
			for(String sTempBgRoleId : aBgRoleId)
			{
				Integer nBgRoleId = Integer.valueOf(sTempBgRoleId);
				BgURR oBgURR = new BgURR();
				oBgURR.setBgRoleId(nBgRoleId);
				oBgURR.setBgUserId(nBgUserId);
				m_oBgURRDao.addBgURR(oBgURR);
			}
		}
	}
	
	/**
	 * 增加岗位
	 * @param sBgPostId
	 * @param nBgUserId 
	 * return void
	 */
	private void addPost(String sBgPostId, Integer nBgUserId)
	{
		if(!sBgPostId.isEmpty())
		{
			String[] aBgPostId = sBgPostId.split(",");
			m_oBgURPDao.delBgURPByUserId(String.valueOf(nBgUserId));
			for(String sTempBgPostId : aBgPostId)
			{
				Integer nBgPostId = Integer.valueOf(sTempBgPostId);
				BgURP oBgURP = new BgURP();
				oBgURP.setBgPostId(nBgPostId);
				oBgURP.setBgUserId(nBgUserId);
				m_oBgURPDao.addBgURP(oBgURP);
			}
		}			
	}
}
