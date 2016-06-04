package org.dmp.module.admin.security.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.JBaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * 角色管理
 */
@Repository("BgRolePermissionDao")
@Scope("prototype")
public class BgRolePermissionDao extends JBaseDao
{
	/**
	 * 获取角色管理界面权限选择项
	 * @param 
	 * @return
	 */
	public List<DynaBean> getRoleCheckGroup(int treeId){

        String sql = "SELECT N_TREEID, S_RIGHTCODE, S_RIGHTNAME, N_ISPAGECODE FROM TD_S_RIGHTCODE WHERE N_TREEID="+ treeId;
		List<DynaBean> list = m_oDB.select(sql);
		return list;
	}
	/**
	 * 获取新增的树节点的treeId
	 * @return
	 */
	public int getCurrentTreeId(){
		String sql = "SELECT N_TREEID FROM TD_S_RIGHTCODE WHERE S_RIGHTCODE IS NULL AND S_RIGHTNAME IS NULL ORDER BY N_TREEID DESC";
		List<DynaBean> list = m_oDB.select(sql);
		int currentTreeId = Integer.parseInt((String) list.get(0).get("N_TREEID"));
        return currentTreeId;		
	}
	
	/**
	 * 增加角色权限设置信息
	 * @param treeId
	 * @param isProvince
	 * @param rightCode
	 * @param rightName
	 * @return
	 */
	public int addRightCode(int treeId, int isProvince, String rightCode, String rightName){
		Field field = new Field();
		field.addInt(treeId);
		field.addStr(rightCode);
		field.addStr(rightName);
		field.addInt(isProvince);
		String sql = "INSERT INTO TD_S_RIGHTCODE VALUES(?,?,?,?)";
		return m_oDB.insert(sql, field );
	}
	
	
	/**
	 * 获取新增的treeId后删除触发器生成的权限信息
	 * @param treeId
	 */
    public int deleteRightCode(int treeId){
    	String sql = "DELETE FROM TD_S_RIGHTCODE WHERE N_TREEID=?";
    	return m_oDB.delete(sql, new Field().addInt(treeId));
    }
	
}
