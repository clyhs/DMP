package org.dmp.module.admin.security.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.security.BgRRT;
import org.dmp.pojo.admin.security.BgRole;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


/**
 * 后台角色管理持久层
 * 
 */
@Repository("BgRoleDao")
@Scope("prototype")
public class BgRoleDao extends HBaseDao<BgRole>
{
	/**
	 * 获取一个后台角色资料
	 * 
	 * @param nBgRoleId
	 * @return BgRole
	 */
	public BgRole getBgRole(int nBgRoleId)
	{
		return (BgRole) super.getById(new Integer(nBgRoleId));
	}
	
	/**
	 * 通过用户标识获取角色
	 * @param nBgUserId
	 * @return List<BgRole>
	 */
	public List<BgRole> getBgRoleList(Integer nBgUserId)
	{
		return super.select("SELECT A FROM BgRole AS A, BgURR AS B WHERE A.nBgRoleId=B.nBgRoleId AND B.nBgUserId=?", new Field().addInt(nBgUserId));
	}
	
	/**
	 * 获取后台角色资料列表
	 * @param sName
	 * @return List<BgRole>
	 */
	public List<BgRole> getBgRoleList(String sName, int nStart, int nOffset)
	{
		return super.select("FROM BgRole WHERE sName LIKE ? ORDER BY sName DESC", new Field().addStr("%"+sName+"%"), nStart, nOffset);
	}
	
	/**
	 * 获取后台角色列表数量
	 * @param sName
	 * @return Long
	 */
	public Long getBgRoleListTotal(String sName)
	{
		return super.getTotal("FROM BgRole WHERE sName LIKE ?", new Field().addStr("%"+sName+"%"));
	}
	
	/**
	 * 获取后台角色资料列表
	 * @return List<BgRole>
	 */
	public List<BgRole> getBgRoleList()
	{
		return super.select("FROM BgRole ORDER BY nBgRoleId DESC");
	}
	
	/**
	 * 增加一个后台角色资料
	 * 
	 * @param oBgRole
	 * @return int
	 */
	public int addBgRole(BgRole oBgRole)
	{
		return super.insert(oBgRole);
	}
	
	/**
	 * 修改一个后台角色资料
	 * 
	 * @param oBgRole
	 * @return int
	 */
	public int editBgRole(BgRole oBgRole)
	{
		return super.update(oBgRole);
	}
	
	/**
	 * 删除一个后台角色资料
	 * 
	 * @param oBgRole
	 * @return int
	 */
	public int delBgRole(BgRole oBgRole)
	{
		return super.delete(oBgRole);
	}
	/**
	 * 获取权限功能关联表中的信息
	 * @param treeId
	 * @return
	 */
	public List<BgRRT> getRolePermissionInfo(int treeId, int roleId){
		Field field = new Field();
		field.addInt(treeId);
		field.addInt(roleId);
		return super.select("FROM BgRRT WHERE nBgTreeId=? AND nBgRoleId=?", field);
	}
	
	/**
	 * 修改角色与权限表中的界面权限和数据权限信息
	 * @param treeId
	 * @param pageRight
	 * @param dataRight
	 * @return
	 */
	public int editPageAndDataRight(int treeId, int roleId, String pageRight, String dataRight){
		Field field = new Field();
		field.addStr(pageRight);
		field.addStr(dataRight);
		field.addInt(treeId);
		field.addInt(roleId);
		String hql = "UPDATE BgRRT SET sPageRight=? , sDataRight=? WHERE nBgTreeId=? AND nBgRoleId=? ";
		return super.update(hql, field); 
	}
	
	/**
	 * 检查角色名称是否重复
	 * @param sName
	 * @return
	 */
	public List<BgRole> checkRepeatByName(String sName){
		return select("FROM BgRole WHERE sName=?", new Field().addStr(sName));
	}
	

}
