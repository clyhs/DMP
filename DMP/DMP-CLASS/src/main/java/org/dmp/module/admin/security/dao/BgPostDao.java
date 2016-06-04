package org.dmp.module.admin.security.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.security.BgPost;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * 后台岗位管理持久层
 * 
 * 
 */
@Repository("BgPostDao")
@Scope("prototype")
public class BgPostDao extends HBaseDao<BgPost>
{
	/**
	 * 获取一个岗位资料
	 * 
	 * @param nBgPostId
	 * @return BgPost
	 */
	public BgPost getBgPost(int nBgPostId)
	{
		return (BgPost) super.getById(new Integer(nBgPostId));
	}
	
	/**
	 * 获取岗位列表
	 * @param nBgUserId
	 * @return List<BgPost>
	 */
	public List<BgPost> getBgPostList(Integer nBgUserId)
	{
		return super.select("SELECT A FROM BgPost AS A, BgURP AS B WHERE A.nBgPostId=B.nBgPostId AND B.nBgUserId=?", new Field().addInt(nBgUserId));
	}
	
	/**
	 * 获取后台岗位资料列表
	 * 
	 * @param sName
	 * @param sDeptName
	 * @param nStart
	 * @param nOffset
	 * @return List<BgPost>
	 */
	public List<BgPost> getBgPostList(String sName, String sDeptName,
			int nStart, int nOffset)
	{
		return super.select(
				"FROM BgPost WHERE sName LIKE ? AND sDeptName LIKE ? ORDER BY nBgPostId DESC",
				new Field().addStr("%" + sName + "%").addStr(
						"%" + sDeptName + "%"), nStart, nOffset);
	}
	
	/**
	 * 获取后台岗位列表总数
	 * 
	 * @param sName
	 * @param sDeptName
	 * @return Long
	 */
	public Long getBgPostListTotal(String sName, String sDeptName)
	{
		return super.getTotal(
				"FROM BgPost WHERE sName LIKE ? AND sDeptName LIKE ?",
				new Field().addStr("%" + sName + "%").addStr(
						"%" + sDeptName + "%"));
	}
	
	/**
	 * 增加一个后台岗位资料
	 * 
	 * @param oBgPost
	 * @return int
	 */
	public int addBgPost(BgPost oBgPost)
	{
		return super.insert(oBgPost);
	}
	
	/**
	 * 修改一个后台岗位资料
	 * 
	 * @param oBgPost
	 * @return int
	 */
	public int editBgPost(BgPost oBgPost)
	{
		return super.update(oBgPost);
	}
	
	/**
	 * 删除岗位列表
	 * 
	 * @param sBgPostIdList，ID之间用逗号隔开
	 * @return int
	 */
	public int delBgPostList(String sBgPostIdList)
	{
		return super.delete("DELETE BgPost WHERE nBgPostId IN ("+sBgPostIdList+")");
	}
}
