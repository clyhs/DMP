package org.dmp.module.admin.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dmp.module.admin.security.dao.BgDeptDao;
import org.dmp.module.admin.security.dao.BgPostDao;
import org.dmp.module.admin.security.dao.BgURPDao;
import org.dmp.module.admin.security.dao.BgUserDao;
import org.dmp.pojo.admin.security.BgDept;
import org.dmp.pojo.admin.security.BgPost;
import org.dmp.pojo.admin.security.BgURP;
import org.dmp.pojo.admin.security.BgUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 后台岗位管理逻辑层
 * @Author 伍锐凡
 * @Date 2012-3-26
 * @Version 1.0
 * @Remark
 */
@Service("BgPostService")
@Scope("prototype")
public class BgPostService
{
	@Resource(name = "BgPostDao")
	private BgPostDao m_oBgPostDao;
	
	@Resource(name = "BgDeptDao")
	private BgDeptDao m_oBgDeptDao;
	
	@Resource(name = "BgURPDao")
	private BgURPDao m_oBgURPDao;
	
	@Resource(name = "BgUserDao")
	private BgUserDao m_oBgUserDao;
	
	/**
	 * 获取一个职位资料
	 * 
	 * @param nBgPostId
	 * @return BgPost
	 */
	public BgPost getBgPost(int nBgPostId)
	{
		return m_oBgPostDao.getBgPost(nBgPostId);
	}
	
	/**
	 * 获取后台岗位资料列表
	 * 
	 * @param sName
	 * @param sDeptName
	 * @param nStart
	 * @param nOffset
	 * @return List<BgPost>
	 */
	public List<BgPost> getBgPostList(String sName, String sDeptName,
			int nStart, int nOffset)
	{
		return m_oBgPostDao.getBgPostList(sName, sDeptName, nStart, nOffset);
	}
	
	/**
	 * 获取后台岗位列表总数
	 * 
	 * @param sName
	 * @param sDeptName
	 * @return Long
	 */
	public Long getBgPostListTotal(String sName, String sDeptName)
	{
		return m_oBgPostDao.getBgPostListTotal(sName, sDeptName);
	}
	
	/**
	 * 增加一个后台岗位资料
	 * @param sName
	 * @param nBgDeptId
	 * @param sRemark
	 * @return int
	 */
	public int addBgPost(String sName, Integer nBgDeptId, String sRemark)
	{
		BgPost oBgPost = new BgPost();
		oBgPost.setName(sName);
		oBgPost.setBgDeptId(nBgDeptId);
		BgDept oBgDept = m_oBgDeptDao.getBgDept(nBgDeptId);
		oBgPost.setDeptName(oBgDept == null || oBgDept.getName() == null ? "" : oBgDept.getName());
		oBgPost.setRemark(sRemark);
		return m_oBgPostDao.addBgPost(oBgPost);
	}
	
	/**
	 * 修改一个后台岗位资料
	 * @param nBgPostId
	 * @param sName
	 * @param nBgDeptId
	 * @param sRemark
	 * @return int
	 */
	public int editBgPost(Integer nBgPostId, String sName, Integer nBgDeptId, String sRemark)
	{
		BgPost oBgPost = new BgPost();
		oBgPost.setBgPostId(nBgPostId);
		oBgPost.setName(sName);
		oBgPost.setBgDeptId(nBgDeptId);
		BgDept oBgDept = m_oBgDeptDao.getBgDept(nBgDeptId);
		oBgPost.setDeptName(oBgDept == null || oBgDept.getName() == null ? "" : oBgDept.getName());
		oBgPost.setRemark(sRemark);
		int nTotal = m_oBgPostDao.editBgPost(oBgPost);
		if(nTotal > 0)
		{
			List<BgURP> aBgURP = m_oBgURPDao.getBgURPList(String.valueOf(nBgPostId));
			Set<Integer> aBgUserId = new HashSet<Integer>();
			if(aBgURP != null && aBgURP.size() > 0)
			{
				for(BgURP oBgURP : aBgURP)
				{
					aBgUserId.add(oBgURP.getBgUserId());
				}
			}
			for(Integer nBgUserId : aBgUserId)
			{
				editBgUserPost(nBgUserId);
			}
		}
		
		return nTotal;
	}
	
	/**
	 * 删除岗位列表
	 * 
	 * @param sBgPostIdList，ID之间用逗号隔开
	 * @return int
	 */
	public int delBgPostList(String sBgPostIdList)
	{
		int nTotal = m_oBgPostDao.delBgPostList(sBgPostIdList);
		if(nTotal > 0)
		{
			List<BgURP> aBgURP = m_oBgURPDao.getBgURPList(sBgPostIdList);
			Set<Integer> aBgUserId = new HashSet<Integer>();
			if(aBgURP != null && aBgURP.size() > 0)
			{
				for(BgURP oBgURP : aBgURP)
				{
					aBgUserId.add(oBgURP.getBgUserId());
				}
			}
			m_oBgURPDao.delBgURPByPostId(sBgPostIdList);
			for(Integer nBgUserId : aBgUserId)
			{
				editBgUserPost(nBgUserId);
			}
			
		}
		return nTotal;
	}
	
	/**
	 * 修改用户岗位
	 * @param nBgUserId 
	 * return void
	 */
	private void editBgUserPost(Integer nBgUserId)
	{
		String sBgPostId = "";
		String sBgPost = "";
		BgUser oBgUser = m_oBgUserDao.getBgUser(nBgUserId);
		if(oBgUser == null) return;
		List<BgPost> aBgPost = m_oBgPostDao.getBgPostList(nBgUserId);
		if(aBgPost != null && aBgPost.size() > 0)
		{
			for(int i = 0; i < aBgPost.size(); i ++)
			{
				if(i > 0)
				{
					sBgPostId += ",";
					sBgPost += ",";
				}
				sBgPostId += aBgPost.get(i).getBgPostId();
				sBgPost += aBgPost.get(i).getName();
			}
		}
		oBgUser.setBgPostId(sBgPostId);
		oBgUser.setBgPost(sBgPost);
		m_oBgUserDao.editBgUser(oBgUser);
	}	
}
