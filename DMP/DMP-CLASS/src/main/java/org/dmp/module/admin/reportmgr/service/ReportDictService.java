package org.dmp.module.admin.reportmgr.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dmp.core.db.util.Field;
import org.dmp.module.admin.reportmgr.dao.ReportDictDao;
import org.dmp.pojo.admin.reportmgr.ReportDict;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("ReportDictService")
@Scope("prototype")
public class ReportDictService {

	@Resource(name = "ReportDictDao")
	private ReportDictDao reportDictDao;

	public List<ReportDict> getReportDictList(String reportId) {
		if (reportId != null) {
			return reportDictDao.getReportDictList(reportId);
		}
		return null;
	}

	public int addReportDictList(List<ReportDict> reportDictList) {
		return reportDictDao.addReportDictList(reportDictList);
	}

	@SuppressWarnings("unchecked")
	public int editReportAttrList(List<ReportDict> originalAttr,
			List<ReportDict> reportDictList) {
		Iterator<ReportDict> it = reportDictList.iterator();
		int nTotal = 0;
		Set<String> originalIdSet = new HashSet<String>();
		for (int i = 0; i < originalAttr.size(); i++) {
			originalIdSet.add(originalAttr.get(i).getName() + "_"
					+ originalAttr.get(i).getCode());
		}

		while (it.hasNext()) {
			ReportDict reportAttr = it.next();
			// 判断标准是否存在这条数据，不存在做新增操作
			if (reportDictDao.isReportDict(reportAttr.getName(),
					reportAttr.getCode())) {
				reportDictDao.insert(reportAttr);
			}

			if (originalIdSet.contains(reportAttr.getName() + "_"
					+ reportAttr.getCode())) {
				reportDictDao
						.update("update ReportDict set meaning=?  where name=? and code=?",
								new Field().addStr(reportAttr.getMeaning())
										.addStr(reportAttr.getName())
										.addStr(reportAttr.getCode()));
				// ReportDictDao.update(reportAttr);
				originalIdSet.remove(reportAttr.getName() + "_"
						+ reportAttr.getCode());
			}
		}
		Iterator<String> idIterator = originalIdSet.iterator();
		while (idIterator.hasNext()) {
			String id = idIterator.next();
			String[] str = id.split("_");
			reportDictDao.delete("delete ReportDict where name=? and code=? ",
					new Field().addStr(str[0]).addStr(str[1]));
			// ReportDictDao.delete(ReportDictDao.getById(id));
		}
		nTotal++;
		return nTotal != 0 ? 1 : -1;

	}

	@SuppressWarnings("unchecked")
	public int delReportDictList(List<ReportDict> reportDictList) {
		return reportDictDao.delete(reportDictList);
	}

	public List<ReportDict> lMapToReportDictList(
			List<Map<String, String>> reportDictListMap) {
		Iterator<Map<String, String>> it = reportDictListMap.iterator();
		List<ReportDict> reportDictList = new ArrayList<ReportDict>();
		while (it.hasNext()) {
			Map<String, String> map = it.next();
			ReportDict reportDict = new ReportDict();
			reportDict.setName(map.get("name"));
			reportDict.setMeaning(map.get("meaning"));
			reportDict.setCode(map.get("code"));
			reportDictList.add(reportDict);
		}
		return reportDictList;
	}
}
