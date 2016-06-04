Ext.BLANK_IMAGE_URL = '../../../res/images/common/default/s.gif';

var g_oViewPort = null;
Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
	g_oViewPort = new Ext.Viewport({
		layout: 'border',
		items: [new Ext.ReportImport()]
	});
	//判断界面权限哪些进行显示
	nodeId = parent.g_oViewPort.items.get(1).nodeId;
	var treeId = nodeId.substring(8);
	Ext.Ajax.request({
		url: '../common/getPageRightInfo.json', 
		method: 'post',  
		params: {treeId: treeId},
		success: function(resp, action){
			var result = resp.responseText;
			var strs = new Array();
			strs = result.split(",");
			for(var i = 0; i < strs.length;i++){
				var id = strs[i]+"";
				Ext.getCmp(id).show();
			}
		},   
		failure:function(resp, action){   
		    Ext.Msg.alert('温馨提示','系统错误');   
		}   
	}); 
});
