package org.dmp.module.admin.main.dao;


import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.navtree.BgTree;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("MainDao")
@Scope("prototype")
public class MainDao extends HBaseDao<BgTree> {

	/**
	 * 获取主界面一级工具栏
	 * 
	 * @return List<BgTree>
	 */
	public List<BgTree> getMainTab() {
		String hql = "FROM BgTree WHERE nPId=1 AND nIsValid=1 ORDER BY nPos";
		
		
		return super.select(hql);
	}

	/**
	 * 获取主界面二级菜单
	 * 
	 * @return List<BgTree>
	 */
	public List<BgTree> getSecondaryMenu(String nodeName) {
		Field oField = new Field();
		oField.addStr(nodeName);
		String hql = " SELECT A FROM BgTree AS A,BgTree AS B WHERE B.sName = ? AND A.nPId = B.nBgTreeId ORDER BY A.nPId,A.nBgTreeId";
		return super.select(hql, oField);
	}

}
