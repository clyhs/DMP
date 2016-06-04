package org.dmp.module.admin.navtree.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.navtree.BgTree;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("NavTreeDao")
@Scope("prototype")
public class NavTreeDao extends HBaseDao<BgTree> {
	/**
	 * 获取一个树结点
	 * 
	 * @param nBgTreeId
	 * @return BgTree
	 */
	public BgTree getTreeNode(Integer nBgTreeId) {
		return (BgTree) super.getById(nBgTreeId);
	}

	/**
	 * 获取树结点列表
	 * 
	 * @param nBgTreeId
	 * @return List<BgTree>
	 */
	public List<BgTree> getTreeNodeList(Integer nBgTreeId) {
		return super.select("FROM BgTree WHERE nPId=? ORDER BY nBgTreeId,nPos",
				new Field().addInt(nBgTreeId));
	}

	/**
	 * 获取与用户相关的树结点列表
	 * 
	 * @param nBgUserId
	 * @param nBgTreeId
	 * @return List<BgTree>
	 */
	public List<BgTree> getMyTreeNodeList(Integer nBgUserId, Integer nBgTreeId,
			String nodeName) {
		Integer treeId = getBgPId(nodeName);
		if (nBgTreeId.equals(treeId)) {
			Field oField = new Field();
			oField.addInt(nBgTreeId);
			oField.addStr(nodeName + "%");

			String sHQL =  "FROM BgTree WHERE nPId=? AND nIsValid=1 AND sName LIKE ? ORDER BY nBgTreeId" ;
					
			return super.select(sHQL, oField);
		} else {
			Field oField = new Field();
			oField.addInt(nBgTreeId);
			

			String sHQL =  "FROM BgTree WHERE nPId=? AND nIsValid=1 ORDER BY nBgTreeId";
			return super.select(sHQL, oField);
		}

	}

	/**
	 * 获取树结点列表记录总数
	 * 
	 * @param nBgTreeId
	 * @return Long
	 */
	public Long getTreeNodeListTotal(Integer nBgTreeId) {
		return super.getTotal("FROM BgTree WHERE nBgTreeId=?",
				new Field().addInt(nBgTreeId));
	}

	/**
	 * 增加一个树结点
	 * 
	 * @param oBgTree
	 * @return int
	 */
	public int addTreeNode(BgTree oBgTree) {
		int nPKId = 0;
		if (super.insert(oBgTree) > 0) {
			nPKId = oBgTree.getBgTreeId();
			BgTree oParentBgTree = (BgTree) super.getById(oBgTree.getPId());
			if(oParentBgTree==null){
				super.update(
						"UPDATE BgTree SET sPath=? WHERE nBgTreeId=?",
						new Field().addStr(
								"1/" + Integer.valueOf(nPKId)
										+ "/").addInt(nPKId));
			
			}else{
			    super.update(
					"UPDATE BgTree SET sPath=? WHERE nBgTreeId=?",
					new Field().addStr(
							oParentBgTree.getPath() + Integer.valueOf(nPKId)
									+ "/").addInt(nPKId));
			}
		}
		return nPKId;
	}

	/**
	 * 修改一个树结点
	 * 
	 * @param oBgTree
	 * @return int
	 */
	public int editTreeNode(BgTree oBgTree) {
		return super.update(oBgTree);
	}

	/**
	 * 删除一个树结点
	 * 
	 * @param nBgTreeId
	 * @return int
	 */
	public int delTreeNode(Integer nBgTreeId) {
		int nTotal = -1;
		BgTree oBgTree = getTreeNode(nBgTreeId);
		if (oBgTree != null) {
			nTotal = super.delete("DELETE FROM BgTree WHERE sPath LIKE ?",
					new Field().addStr(oBgTree.getPath() + "%"));
		}
		return nTotal;
	}

	/**
	 * 修改树结点路径
	 * 
	 * @param sConsPath
	 * @param sOldPath
	 * @param sNewPath
	 * @return int
	 */
	public int editTreeNodePath(String sConsPath, String sOldPath,
			String sNewPath) {
		return super
				.update("UPDATE BgTree SET sPath=REPLACE(sPath, ?, ?) WHERE sPath LIKE ?",
						new Field().addStr(sOldPath).addStr(sNewPath)
								.addStr(sConsPath + "%"));
	}

	/**
	 * 根据节点名获取节点id
	 * 
	 * @param nodeName
	 * @return Integer
	 */
	public Integer getBgPId(String nodeName) {
		Field oField = new Field();
		oField.addStr(nodeName + "%");
		List<BgTree> bgTreeList = super.select(
				"FROM BgTree WHERE sName LIKE ?", oField);
		if (bgTreeList.size() > 0) {
			return bgTreeList.get(0).getPId();
		}
		return 0;
	}

	/**
	 * 根据节点名获取节点id
	 * 
	 * @param nodeName
	 * @return Integer
	 */
	public Integer getBgTreeId(String nodeName) {
		Field oField = new Field();
		oField.addStr(nodeName + '%');
		List<BgTree> bgTreeList = super.select(
				"FROM BgTree WHERE sName LIKE ?", oField);
		if (bgTreeList.size() > 0) {
			return bgTreeList.get(0).getBgTreeId();
		}
		return 0;
	}

	/**
	 * 根据reportId获取树信息
	 * 
	 * @param reportId
	 * @return
	 */
	public BgTree getTreeByName(String reportId) {
		Field oField = new Field();
		oField.addStr(reportId);
		String sHQL = "SELECT DISTINCT A FROM BgTree AS A,Report AS B WHERE 1=1 AND A.sName = B.reportName AND B.reportId = ?";
		List<BgTree> bg = super.select(sHQL, oField);
		return bg.get(0);
	}

}
