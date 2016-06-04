package org.dmp.module.admin.security.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.security.BgURR;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


/**
 * 后台角色与用户关联管理持久层
 * 
 * @Author 伍锐凡
 * @Date 2012-3-26
 * @Version 1.0
 * @Remark
 */
@Repository("BgURRDao")
@Scope("prototype")
public class BgURRDao extends HBaseDao<BgURR>
{
	
	/**
	 * 获取关联资料列表
	 * @param nBgRoleId
	 * @return List<BgURR>
	 */
	public List<BgURR> getBgURRList(int nBgRoleId)
	{
		return super.select("FROM BgURR WHERE nBgRoleId=? ORDER BY nBgURRId DESC", new Field().addInt(nBgRoleId));
	}
	
	/**
	 * 获取关联
	 * @param nBgRoleId
	 * @param nBgUserId
	 * @return BgURR
	 */
	public BgURR getBgURR(Integer nBgRoleId, Integer nBgUserId)
	{
		List<BgURR> aBgURR = super.select("FROM BgURR WHERE nBgRoleId=? AND nBgUserId=?", new Field().addInt(nBgRoleId).addInt(nBgUserId));
		return aBgURR != null && aBgURR.size() > 0 ? aBgURR.get(0) : null;
	}
	
	/**
	 * 增加关联资料
	 * 
	 * @param oBgURR
	 * @return int
	 */
	public int addBgURR(BgURR oBgURR)
	{
		return super.insert(oBgURR);
	}
	
	/**
	 * 删除关联
	 * @param nBgRoleId
	 * @return int
	 */
	public int delBgURRByRoleId(int nBgRoleId)
	{
		return super.delete("DELETE FROM BgURR WHERE nBgRoleId=?", new Field().addInt(nBgRoleId));
	}
	public int delBgURRByUR(String sUserId, int nBgRoleId)
	{
		return super.delete("DELETE FROM BgURR WHERE nBgUserId IN ("+sUserId+") AND nBgRoleId=?", new Field().addInt(nBgRoleId));
	}
	public int delBgURRByUser(String sUserId)
	{
		return super.delete("DELETE FROM BgURR WHERE nBgUserId IN ("+sUserId+")");
	}
}
