package org.dmp.module.admin.security.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.security.BgDept;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * 后台部门管理持久层
 * 
 */
@Repository("BgDeptDao")
@Scope("prototype")
public class BgDeptDao extends HBaseDao<BgDept>
{
	/**
	 * 获取一个部门
	 * 
	 * @param nBgDeptId
	 * @return BgDept
	 */
	public BgDept getBgDept(Integer nBgDeptId)
	{
		return (BgDept) super.getById(nBgDeptId);
	}
	
	/**
	 * 获取部门列表
	 * 
	 * @param nBgDeptId
	 * @return List<BgDept>
	 */
	public List<BgDept> getBgDeptList(Integer nBgDeptId)
	{
		return super.select("FROM BgDept WHERE nPId=?", new Field().addInt(nBgDeptId));
	}
	
	/**
	 * 获取部门列表记录总数
	 * 
	 * @param nBgDeptId
	 * @return Long
	 */
	public Long getBgDeptListTotal(Integer nBgDeptId)
	{
		return super.getTotal("FROM BgDept WHERE nBgDeptId=?",
				new Field().addInt(nBgDeptId));
	}
	
	/**
	 * 增加一个部门
	 * 
	 * @param oBgDept
	 * @return int
	 */
	public int addBgDept(BgDept oBgDept)
	{
		int nPKId = 0;
		if (super.insert(oBgDept) > 0)
		{
			nPKId = oBgDept.getBgDeptId();
			//BgDept oParentBgDept = (BgDept) super.getById(oBgDept.getPId());
			BgDept oParentBgDept = (BgDept) super.getById(oBgDept.getPId());
			if(oParentBgDept==null){
				super.update("UPDATE BgDept SET sPath=? WHERE nBgDeptId=?", new Field().addStr("1/" + Integer.valueOf(nPKId) + "/").addInt(nPKId));

			}else{
				super.update("UPDATE BgDept SET sPath=? WHERE nBgDeptId=?", new Field().addStr(oParentBgDept.getPath() + Integer.valueOf(nPKId) + "/").addInt(nPKId));
			}
			
			
		}
		return nPKId;
	}
	
	/**
	 * 修改一个部门
	 * 
	 * @param oBgDept
	 * @return int
	 */
	public int editBgDept(BgDept oBgDept)
	{
		return super.update(oBgDept);
	}
	
	/**
	 * 删除一个部门
	 * 
	 * @param nBgDeptId
	 * @return int
	 */
	public int delBgDept(Integer nBgDeptId)
	{
		int nTotal = -1;
		BgDept oBgDept = getBgDept(nBgDeptId);
		if (oBgDept != null)
		{
			nTotal = super.delete("DELETE FROM BgDept WHERE sPath LIKE ?",
					new Field().addStr(oBgDept.getPath() + "%"));
		}
		return nTotal;
	}
	
	/**
	 * 修改部门路径
	 * @param sConPath
	 * @param sOldPath
	 * @param sNewPath
	 * @return int
	 */
	public int editBgDeptPath(String sConPath, String sOldPath, String sNewPath)
	{
		return super.update(
				"UPDATE BgDept SET sPath=REPLACE(sPath, ?, ?) WHERE sPath LIKE ?",
				new Field().addStr(sOldPath).addStr(sNewPath)
						.addStr(sConPath + "%"));
	}
}
