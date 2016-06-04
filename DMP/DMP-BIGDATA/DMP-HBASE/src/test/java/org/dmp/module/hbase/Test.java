package org.dmp.module.hbase;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.module.hbase.service.TestService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		AbstractApplicationContext factory = new FileSystemXmlApplicationContext(
				System.getProperty("user.dir") + "/conf/spring3_mvc.xml");
		System.out.println("hbase-----------------------------------");
		TestService oTestService = (TestService) factory.getBean("TestService");
		List<DynaBean> aTestList = oTestService.getTestList("", "", 0, 100);
		if (aTestList != null && aTestList.size() > 0)
		{
			for (DynaBean oBean : aTestList)
			{
				System.out.println(oBean.get("sName"));
			}
		}

		/*
		 * TestService oTestService = (TestService)
		 * factory.getBean("TestService"); Map<String, Field> aField = new
		 * HashMap<String, Field>(1000); System.out.println(Tools.getNow());
		 * String sContent =
		 * "事实上，在美国国内早就有评论指出，网络战实力最强和黑客高手最多的美国渲染外界网络威胁，只不过是为自身强化网络攻击能力和向政府要钱找借口，克拉珀12日在参议院解释这份报告时的发言似乎印证了这一点。他先是以“威胁”的口吻说，如果国会不设法降低预算自动削减给情报部门造成的压力，美国情报部门的能力“将显著受损”，继而又“哭穷”道，由于资金减少，数千名美国联邦调查局雇员面临休假，5000个情报承包商无法续签合同，甚至连在间谍卫星这种历史悠久的情报搜索系统的资金投入也要遭遇缩减，这无疑会影响情报部门“提升网络安全的努力”。"
		 * ; for (int i = 1; i < 1000001; i++) { aField.put(String.valueOf(i),
		 * new Field().addStr("sName", String.valueOf(i) + sContent)
		 * .addStr("sContent", String.valueOf(i) + sContent)); if (aField.size()
		 * > 1000) { oTestService.addTest(aField); aField.clear(); } } if
		 * (!aField.isEmpty()) { oTestService.addTest(aField); }
		 * System.out.println(Tools.getNow());
		 */

		factory.close();
		factory = null;
	}

}
