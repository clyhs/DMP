Ext.BLANK_IMAGE_URL = contextPath+'/res/images/common/default/s.gif';

//入口函数
var g_oViewPort = null;
var oCenterNav;
Ext.onReady(function(){
	Ext.useShims = true;
	Ext.QuickTips.init();
	
	oCenterNav = new Ext.CenterNav();

	setTimeout(function()
	{
		var oIFrameComponent = new Ext.IFrameComponent({tag: 'iframe', id:'WelcomePanel', frameBorder: 0, url:'', title:'我的首页', closable:false});
		var p = oCenterNav.add(oIFrameComponent);
		oCenterNav.setActiveTab(p);
	}, 50);
	
	g_oViewPort = new Ext.Viewport({
		layout: 'border',
		items: [new Ext.TopNav(),new Ext.LeftNav(oCenterNav),oCenterNav]
	});
	
	//加载一级工具条
	var toolbar = new Ext.Toolbar({
		style: 'padding-left:0px; background-image:url('+contextPath+'/res/images/common/icons/navBG.gif); position:relative; width:100%; overflow:hidden;border: 0;', 
		id: 'tool_bar'
	});
	//加载二级工具条
	var toolbar2 = new Ext.Toolbar({
		style: 'padding-left:0px; background-image:url('+contextPath+'/res/images/common/icons/toolbar.png); position:relative; width:100%; overflow:hidden;border: 0;height: 18px;', 
		id: 'tool_bar2'
	});
	
	getMenuToolbar(toolbar,toolbar2);
	
	
	
	 
	
	
});
Ext.Ajax.on('requestcomplete', checkUserSessionStatus, this);
function checkUserSessionStatus(conn, response, options){
	if(response.getResponseHeader != undefined && response.getResponseHeader("sessionstatus") == "timeout"){
		window.location.reload();
	}
}
function getMenuToolbar(tbr,tbr2){
	//加载一级菜单
	Ext.Ajax.request({   
		   url: contextPath+'/module/admin/main/getMainTab.json',   
		   method: 'post',   
		   success: function(resp, action){
		       var obj = Ext.util.JSON.decode(resp.responseText);
		       var beforeButton = null;   //一级菜单前一次点击的button对象
		       for(var i = 0;i < obj.length; i++){
		    	    var button = new Ext.Toolbar.Button({
		    	    	text: obj[i].name,
		    	    	cls: 'top-toolbar',
		    	    	handler: function(oButton, oEvent){
		    	    		beforeButton.removeClass('top-btn-click');
		    	    		beforeButton = oButton;
		    	    		oButton.addClass('top-btn-click');
		    	    		var nodeName = oButton.getText();
		    	    		//点击一级菜单后触发的二级菜单
		    	    		getSecondaryMenu(tbr2,nodeName);

		    	    	},
		    	    	listeners: {
		    	    		mouseover: function(oButton, oEvent){oButton.addClass('top-btn-mouse');},
		    	    		mouseout: function(oButton, oEvent){oButton.removeClass('top-btn-mouse');}
		    	    	}
		    	    });
		    	    tbr.add(button);
		       }
		       tbr.render(Ext.get('toolbar_id'));
		       tbr.doLayout();
		       var defaultValue = tbr.items.items[0].text;  //设置第一个按钮为默认值
		       tbr.items.items[0].addClass('top-btn-click');
		       beforeButton = tbr.items.items[0];
		       getSecondaryMenu(tbr2,defaultValue);
		   },
		   failure:function(resp, action){
			   Ext.Msg.alert('温馨提示','系统错误');   
		   }   
	}); 
}

//加载二级菜单
function getSecondaryMenu(tbr2, nodeName){
	Ext.Ajax.request({
		url: contextPath+'/module/admin/main/getSecondaryMenu.json?nodeName=' + encodeURIComponent(nodeName),
		method: 'post',
		success: function(resp, responseText){
			if(resp.getResponseHeader("sessionstatus") == "timeout"){
				window.location.reload();
				return;
			}
			var beforeButton2 = null; //上一次点击的二级button对象
			tbr2.removeAll();  //删除已存在的所有二级菜单
    	    
			var secondaryObj = Ext.util.JSON.decode(resp.responseText);
			for(var j = 0;j < secondaryObj.length; j++){
				var secondaryButton = new Ext.Toolbar.Button({
					text: secondaryObj[j].name,
					cls: 'top-toolbar2',
					handler: function(oButton, oEvent){
						beforeButton2.removeClass('top-btn-click2');
	    	    		beforeButton2 = oButton;
	    	    		oButton.addClass('top-btn-click2');
						var secondaryNodeName = oButton.getText();
						refreshTreePanel(nodeName, secondaryNodeName);
					}
				
				});
				tbr2.add(secondaryButton);

			}
			tbr2.render(Ext.get('toolbar_id2'));
			tbr2.doLayout();
			var defaultTreeValue = tbr2.items.items[0].text;  //设置二级菜单中第一个按钮为默认值
			tbr2.items.items[0].addClass('top-btn-click2');
		    beforeButton2 = tbr2.items.items[0];
	        refreshTreePanel(nodeName, defaultTreeValue);
		}
	});
}

//刷新导航栏中树形面板
function  refreshTreePanel(nodeName, secondaryNodeName){ 

	var tree = g_oViewPort.items.items[1]; 
	showLoading(tree);
	Ext.Ajax.request({
		url: contextPath+'/module/admin/navtree/getMyBgNavTree.json?nodeName='+encodeURIComponent(nodeName)+'&secondaryNodeName='+encodeURIComponent(secondaryNodeName),
		method: 'post',
		success: function(request){
			var data = Ext.util.JSON.decode(request.responseText);
			removeChildNodes(tree.getRootNode());
			tree.getRootNode().appendChild(data);
			tree.getRootNode().expandChildNodes(true);
			hideLoading();
		},
		failure: function(){
			hideLoading();
			Ext.MessageBox.show({
				title: '系统导航',
				msg: '对不起，加载数据异常，请重试！',
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.ERROR
			});
		}
	});
}

var loading = null;
/**
 * 展示loading
 * @param treePanel
 */
function showLoading(treePanel){
	loading = new Ext.LoadMask(Ext.get(treePanel.getEl()),{msg: '请等待...'});
	loading.show();
}
/**
 * 去除loading
 */
function hideLoading(){
	loading.hide();
}
/**
 * 刷新树型菜单时删除之前节点，确保不会节点重复
 * @param node
 */
function removeChildNodes(node){
	while(node.firstChild){
		removeChildNodes(node.firstChild);
	}
	if(node.getDepth() != "0"){
		node.remove();
	}
}

/**
 * 对js中的当天时间进行处理
 * @param month
 */
function initCurrentTime(currentTime, num){
	var reg=new RegExp("-","g"); //创建正则RegExp对象   
	currentTime = currentTime.replace(reg,"/");  //360浏览器下yyyy/mm/dd格式的字符串才能转换为日期格式
	var selDate = new Date(currentTime);
	var dateRange = "";
	//num为1时表示图表分析按月统计(往前推12月)
	if(num == 1){
		var month12 = new Date(selDate.getFullYear(), (selDate.getMonth()-11),selDate.getDate());
		var month1 = selDate.getMonth()+1; 
		var month2 = month12.getMonth()+1; 
		var beforeMonth = month1 > 9 ? month1 : "0" + month1;    //月份为一位数则加0
		var startMonth = month2 > 9 ? month2 : "0" + month2;
		dateRange = month12.getFullYear()+""+startMonth+"-"+selDate.getFullYear()+""+beforeMonth;
	}else if(num == 2){ //num为2时表示当前月30天
		var month = selDate.getMonth()+1 > 9 ? selDate.getMonth()+1 : "0" + selDate.getMonth()+1;    //月份为一位数则加0
		dateRange = selDate.getFullYear()+""+month;
	}else{
		if(num == undefined ){num = 30;}
		var Date30 = new Date(selDate.getFullYear(),selDate.getMonth(),selDate.getDate()-num); 
		var month1 = Date30.getMonth()+1; 
		var month2 = selDate.getMonth()+1;
		var beforeMonth = month1 > 9 ? month1 : "0" + month1;    //月份为一位数则加0
		var startMonth = month2 > 9 ? month2 : "0" + month2;
		var beforeYM = Date30.getFullYear()+""+beforeMonth;
		var startYM = selDate.getFullYear()+""+startMonth;

		if(beforeYM == startYM){
			dateRange = beforeYM;
		}else{
			dateRange = beforeYM+"-"+startYM;
		}
		
	}
	return dateRange;

}

