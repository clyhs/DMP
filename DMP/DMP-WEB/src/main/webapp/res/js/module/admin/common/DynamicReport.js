Ext.BLANK_IMAGE_URL = contextPath+'/res/images/common/default/s.gif';

var g_oViewPort = null;
Ext.onReady(function(){
	Ext.QuickTips.init();
//	var url = parent.g_oViewPort.items.get(1).selModel.selNode.attributes.url;
	var leftTree = parent.g_oViewPort.items.get(1);   //导航栏点击节点
	//如果selNode为空，则url为我的首页中点击的相关链接否则为导航树节点的url
	var url = leftTree.selModel.selNode==null ? leftTree.hrefUrl:leftTree.selModel.selNode.attributes.url;
	var param ,request,urlSuff="";
	if(url && url.length>0){
		//当url中没有问号的时候此处不会报错
		param = url.split("?")[1];
	}else{
		 Ext.Msg.alert('温馨提示','reportId不能为空!');
		 return;
	}
	if(param){
		request = JSUtil.getRequest(param);
	}else{
		 Ext.Msg.alert('温馨提示','reportId不能为空!');
		 return;
	}
	parent.JSUtil.prototype.preParam(leftTree, param);   //导航栏中url后面所带参数处理 
	
	//报表页面的标题用于命名excel文件
	var reportTitle = parent.g_oViewPort.items.get(2).activeTab.title;
	//当省份控件有全国时allPro为true,此时获取数据的url都改变
	if(request.allPro == "true"){
		urlSuff = "ForPro";
	}
	request.storeUrl = contextPath+'/module/admin/common/getStatReportData' + urlSuff + '.json?reportId=' + request.reportId;
	request.totalUrl = contextPath+'/module/admin/common/getReportDataTotal' + urlSuff + '.json?reportId=' + request.reportId;
	request.exportUrl = contextPath+'/module/admin/common/doDataExport' + urlSuff + '.xls?reportId='+ request.reportId + '&name=' + reportTitle;
	request.chartUrl = contextPath+'/module/admin/common/getReportChart' + urlSuff + '.json?reportId=' + request.reportId;
	g_oViewPort = new Ext.Viewport({
		layout: 'border',
		items: [new Ext.DynGridPanel(request)]
	});
});

//动态表格
Ext.DynGridPanel = function(config)
{
	Ext.apply(this, config);
	var me = this;
	Ext.DynGridPanel.superclass.constructor.call(this, {
		region:'center',
		renderTo: 'dynamic-grid',
		storeUrl: me.storeUrl,
		rowNumberer: true,
		tbar : new Ext.DynTopToolbar(this),
		bbar : new Ext.PagingToolbar({
		    store: me.getStore(),
			pageSize: 100,
			hideLabel: true,
			displayInfo: true,
			listeners:{
        		beforechange: function()
        		{   
        			//params是toolbar里的参数
        			for(var key in me.params){
        				me.getStore().setBaseParam(key, me.params[key]);	
        			}
        		}
            }
		})
	});
};
Ext.extend(Ext.DynGridPanel, Ext.grid.DynamicGrid);

//动态toolbar oGrid为gridpanel对象
Ext.DynTopToolbar = function(oGrid) {
	Ext.DynTopToolbar.superclass.constructor.call(this, {
		id:'tbar',
		oGrid: oGrid,
		storeUrl: contextPath+'/module/admin/common/getToolbarData.json?reportId=' + oGrid.reportId
	});
};
Ext.extend(Ext.DynTopToolbar, Ext.DynamicToolbar);
