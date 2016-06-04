Ext.LeftNav = function(oCenterNav)
{
	Ext.LeftNav.superclass.constructor.call(this, {
		region: 'west',
		title: '系统导航',
		split: true,
		collapsible: true,
		width: 200,
		minSize: 150,
		maxSize: 400,
		collapseMode: 'mini',
		margins: '0 0 5 0',
		cmargins: '0 5 5 5',
		animate:true, 
        autoScroll:true,
        enableDD:true,
        containerScroll: true,
        line: true,
        loader: new Ext.tree.TreeLoader({
        	dataUrl:contextPath+'/module/admin/navtree/getMyBgNavTree.json?nodeName='+encodeURIComponent(0)+'&secondaryNodeName='+encodeURIComponent(0)
        }),
        tools: [{id:'refresh', handler: function(event, toolEl, panel, tc){
    			panel.getRootNode().reload();
    		}
        }],
        rootVisible: false,
        root: new Ext.tree.AsyncTreeNode({id:'TreeNav_0', expanded: true}),
        mainWin: oCenterNav,
        listeners: {click: onNodeClick}
	});
};
Ext.extend(Ext.LeftNav, Ext.tree.TreePanel);

function onNodeClick(oNode, oEvent)
{

	this.nodeId = oNode.id;
	var sUrl = oNode.attributes.url;
	if(sUrl != undefined && sUrl != null && !Ext.isEmpty(sUrl.trim())){
		var param = sUrl.split("?")[1];
        JSUtil.prototype.preParam(this, param);   //预处理url后面所带参数
	}
	if(sUrl != undefined && sUrl != null && !Ext.isEmpty(sUrl.trim()))
	{
		oEvent.stopEvent();
		var oPanel = oNode.getOwnerTree().mainWin;
		
		var oTab = oPanel.getComponent('Frame_' + oNode.id);
		if(oTab){
			oPanel.setActiveTab(oTab);
		}else{
			var oIFrameComponent = new Ext.IFrameComponent({
				id: 'Frame_'+oNode.id, 
				name: 'Frame_'+oNode.id, 
				url: sUrl, 
				title: oNode.text
			});
			oPanel.setActiveTab(oPanel.add(oIFrameComponent));
		}
	}	
    var treeId = oNode.id.substring(8);
    //判断节点是否为叶子节点
    if(oNode.leaf){
    	//sysLogin(treeId, sUrl);   //提交用户操作信息
    }
	
}


//获取操作报表信息
function sysLogin(moduleId, url, content){
	Ext.Ajax.request({
		url: '../syslog/addSysLog.json',   
		method: 'post',   
		params: {moduleId: moduleId, content: content, url: url},   
		success: function(resp, action){
		},   
		failure:function(resp, action){   
		    Ext.Msg.alert('温馨提示','系统错误');   
		}   
	});
}
