package org.dmp.module.admin.security.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.security.BgRRT;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


/**
 * 后台角色与树结点关联管理持久层
 *
 * @Remark
 */
@Repository("BgRRTDao")
@Scope("prototype")
public class BgRRTDao extends HBaseDao<BgRRT>
{
	
	/**
	 * 获取关联资料列表
	 * @param nBgRoleId
	 * @return List<BgRRT>
	 */
	public List<BgRRT> getBgRRTList(int nBgRoleId)
	{
		return super.select("FROM BgRRT WHERE nBgRoleId=? ORDER BY nBgRRTId DESC", new Field().addInt(nBgRoleId));
	}
	
	/**
	 * 增加关联资料
	 * 
	 * @param oBgRRT
	 * @return int
	 */
	public int addBgRRT(BgRRT oBgRRT)
	{
		return super.insert(oBgRRT);
	}
	
	/**
	 * 删除关联
	 * @param nBgRoleId
	 * @return int
	 */
	public int delBgRRT(int nBgRoleId)
	{
		return super.delete("DELETE FROM BgRRT WHERE nBgRoleId=?", new Field().addInt(nBgRoleId));
	}
	
	
	
	/**
	 *修改关联资料
	 * 
	 * @param oBgRRT
	 * @return int
	 */
	public int editBgRRT(BgRRT oBgRRT)
	{
		return super.update(oBgRRT);
	}
	
	
	
	/**
	 * 删除已经存在但没有进行勾选的角色权限
	 * @return
	 */
	public int delBgRRT(int nBgRoleId,String sBgTreeIdList){
		return super.delete("DELETE FROM BgRRT WHERE nBgRoleId=? AND nBgTreeId not in ("+sBgTreeIdList+")", new Field().addInt(nBgRoleId));
	}
}
