package org.dmp.module.admin.security.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.security.BgUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


/**
 * 后台用户管理持久层
 * 
 */
@Repository("BgUserDao")
@Scope("prototype")
public class BgUserDao extends HBaseDao<BgUser>
{
	/**
	 * 获取一个后台用户资料
	 * 
	 * @param nBgUserId
	 * @return BgUser
	 */
	public BgUser getBgUser(int nBgUserId)
	{
		return (BgUser) super.getById(new Integer(nBgUserId));
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
		List<BgUser> aBgUser = super.select(
				"FROM BgUser WHERE sAccount=? AND sPassword=?", new Field()
						.addStr(sAccount).addStr(sPassword));
		return (aBgUser != null && aBgUser.size() > 0) ? aBgUser.get(0) : null;
	}
	
	/**
	 * 通过帐号获取用户资料
	 * @param sAccount
	 * @return BgUser
	 */
	public BgUser getBgUser(String sAccount)
	{
		List<BgUser> aBgUser = super.select(
				"FROM BgUser WHERE sAccount=?", new Field()
						.addStr(sAccount));
		return (aBgUser != null && aBgUser.size() > 0) ? aBgUser.get(0) : null;
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
		Field oField = new Field();
		if(nBgRoleId != 0) oField.addInt(nBgRoleId);
		String sHQL = nBgRoleId == 0 ? 
				"FROM BgUser WHERE sName LIKE ? AND sAccount LIKE ? ORDER BY nBgUserId DESC" 
				: "SELECT A FROM BgUser AS A, BgURR AS B WHERE A.nBgUserId=B.nBgUserId AND B.nBgRoleId=? AND A.sName LIKE ? AND A.sAccount LIKE ? ORDER BY A.nBgUserId DESC";
		return super.select(
				sHQL,
				oField.addStr("%" + sName + "%").addStr(
						"%" + sAccount + "%"), nStart, nOffset);
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
		Field oField = new Field();
		if(nBgRoleId != 0) oField.addInt(nBgRoleId);
		String sHQL = nBgRoleId == 0 ? 
				"FROM BgUser WHERE sName LIKE ? AND sAccount LIKE ?" 
				: "FROM BgUser AS A, BgURR AS B WHERE A.nBgUserId=B.nBgUserId AND B.nBgRoleId=? AND A.sName LIKE ? AND A.sAccount LIKE ?";		
		return super.getTotal(
				sHQL,
				oField.addStr("%" + sName + "%").addStr(
						"%" + sAccount + "%"));
	}
	
	/**
	 * 增加一个后台用户资料
	 * 
	 * @param oBgUser
	 * @return int
	 */
	public int addBgUser(BgUser oBgUser)
	{
		return super.insert(oBgUser);
	}
	
	/**
	 * 修改一个后台用户资料
	 * 
	 * @param oBgUser
	 * @return int
	 */
	public int editBgUser(BgUser oBgUser)
	{
		return super.update(oBgUser);
	}
	
	/**
	 * 修改用户部门名称
	 * @param nBgDeptId
	 * @param sDeptName
	 * @return int
	 */
	public int editDeptName(Integer nBgDeptId, String sDeptName)
	{
		return super.update("UPDATE BgUser SET sDeptName=? WHERE nBgDeptId=?", new Field().addStr(sDeptName).addInt(nBgDeptId));
	}
	
	/**
	 * 删除用户部门
	 * @param nBgDeptId
	 * @return int
	 */
	public int delDeptName(Integer nBgDeptId)
	{
		return super.update("UPDATE BgUser SET nBgDeptId=0,sDeptName='' WHERE nBgDeptId=?", new Field().addInt(nBgDeptId));
	}
	
	/**
	 * 删除用户列表
	 * 
	 * @param sBgUserIdList，ID之间用逗号隔开
	 * @return int
	 */
	public int delBgUserList(String sBgUserIdList)
	{
		return super.delete("DELETE BgUser WHERE nBgUserId IN ("+sBgUserIdList+") AND nBgUserId!=1");
	}
}
