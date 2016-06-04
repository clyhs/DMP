package org.dmp.module.admin.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.dmp.module.admin.security.dao.BgDeptDao;
import org.dmp.module.admin.security.dao.BgUserDao;
import org.dmp.module.common.tree.Tree;
import org.dmp.pojo.admin.security.BgDept;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


/**
 * 后台部门管理逻辑层
 */
@Service("BgDeptService")
@Scope("prototype")
public class BgDeptService
{
	@Resource(name = "BgDeptDao")
	private BgDeptDao m_oBgDeptDao;
	
	@Resource(name = "BgUserDao")
	private BgUserDao m_oBgUserDao;
	
	/**
	 * 获取后台部门
	 * @param nBgDeptId
	 * @return List<Tree>
	 */
	public List<Tree> getBgDept(Integer nBgDeptId, Boolean bIsCheckBoxTree)
	{
		List<Tree> aTree = new ArrayList<Tree>();
		List<BgDept> aBgDept = getBgDeptList(nBgDeptId);
		
		if(aBgDept != null && aBgDept.size() > 0)
		{
			for(BgDept oBgDept : aBgDept)
			{
				Tree oTree = new Tree();
				oTree.setText(oBgDept.getName());
				oTree.setId("TreeNav_" + String.valueOf(oBgDept.getBgDeptId()));
				if(bIsCheckBoxTree) oTree.setChecked(Boolean.FALSE);
				List<BgDept> aBgDeptChild = getBgDeptList(oBgDept.getBgDeptId());
				if(aBgDeptChild != null && aBgDeptChild.size() > 0)
				{
					oTree.setLeaf(Boolean.FALSE);
					oTree.setCls("folder");
					if(nBgDeptId == 1) 
					{
						oTree.setExpanded(Boolean.TRUE);
						oTree.setChildren(getBgDept(oBgDept.getBgDeptId(), bIsCheckBoxTree));
					}
				}
				
				aTree.add(oTree);
			}
		}
		return aTree;
	}
	
	/**
	 * 获取一个部门
	 * @param nBgDeptId
	 * @return BgDept
	 */
	public BgDept getBgDept(Integer nBgDeptId)
	{
		return m_oBgDeptDao.getBgDept(nBgDeptId);
	}
	
	/**
	 * 获取部门列表
	 * @param nBgDeptId
	 * @return List<BgDept>
	 */
	public List<BgDept> getBgDeptList(Integer nBgDeptId)
	{
		return m_oBgDeptDao.getBgDeptList(nBgDeptId);
	}
	
	/**
	 * 增加一个部门
	 * @param nPId
	 * @param sName
	 * @param sRemark
	 * @return int
	 */
	public int addBgDept(int nPId, String sName, String sRemark)
	{
		BgDept oBgDept = new BgDept();
		oBgDept.setPId(nPId);
		oBgDept.setName(sName);
		oBgDept.setRemark(sRemark);
		return m_oBgDeptDao.addBgDept(oBgDept);
	}
	
	/**
	 * 修改一个部门
	 * @param nBgDeptId
	 * @param sName
	 * @param sRemark
	 * @return int
	 */
	public int editBgDept(int nBgDeptId, String sName, String sRemark)
	{
		BgDept oBgDept = getBgDept(nBgDeptId);
		if(oBgDept == null) return -1;
		oBgDept.setName(sName);
		oBgDept.setRemark(sRemark);
		int nTotal = m_oBgDeptDao.editBgDept(oBgDept);
		if(nTotal > 0)
		{
			m_oBgUserDao.editDeptName(oBgDept.getBgDeptId(), oBgDept.getName());
		}
		return nTotal;
	}
	
	/**
	 * 删除一个部门
	 * @param nBgDeptId
	 * @return int
	 */
	public int delBgDept(Integer nBgDeptId)
	{
		int nTotal = m_oBgDeptDao.delBgDept(nBgDeptId);
		if(nTotal > 0)
		{
			m_oBgUserDao.delDeptName(nBgDeptId);
		}
		return nTotal;
	}
	
	/**
	 * 移动部门
	 * @param nDestBgDeptId
	 * @param nSrcBgDeptId
	 * @return int
	 */
	public int editMoveBgDept(int nDestBgDeptId, int nSrcBgDeptId)
	{
		int nTotal = -1;
		BgDept oSrcBgDept = getBgDept(nSrcBgDeptId);
		BgDept oDestBgDept = getBgDept(nDestBgDeptId);
		if(oSrcBgDept != null && oDestBgDept != null)
		{
			BgDept oSrcParentBgDept = getBgDept(oSrcBgDept.getPId());
			
			oSrcBgDept.setPId(oDestBgDept.getBgDeptId());
			nTotal = m_oBgDeptDao.editBgDept(oSrcBgDept);
			if(oSrcParentBgDept != null)
			{
				m_oBgDeptDao.editBgDeptPath(oSrcBgDept.getPath(), oSrcParentBgDept.getPath(), oDestBgDept.getPath());
			}
		}
		return nTotal;
	}
	
}
