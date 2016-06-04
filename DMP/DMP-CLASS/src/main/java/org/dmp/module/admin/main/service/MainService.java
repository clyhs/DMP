package org.dmp.module.admin.main.service;

import java.util.List;
import javax.annotation.Resource;
import org.dmp.module.admin.main.dao.MainDao;
import org.dmp.pojo.admin.navtree.BgTree;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("MainService")
@Scope("prototype")
public class MainService {
	
	@Resource(name = "MainDao")
	private MainDao mainDao;

	/**
	 * 获取主菜单tab
	 * @return
	 */
	public List<BgTree> getMainTab(){
		return mainDao.getMainTab();
	}
	/**
	 * 获取工具条二级菜单
	 * @return
	 */
	public List<BgTree> getSecondaryMenu(String nodeName){
		return mainDao.getSecondaryMenu(nodeName);
	}
	
}