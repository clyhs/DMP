//为角色分配界面及数据权限的window
var m_nRoleWidth = 400;
var m_nRoleHeight = 300; 
Ext.ux.CenterWindowPlugin = function(){
    this.init = function(win,pan) {
         Ext.EventManager.onWindowResize(function(){
             win.center();      
             pan.hide();
             });       
         };   
};
//新增权限
Ext.AddPermissionWindow = function()
{   
	var me = this;
	Ext.AddPermissionWindow.superclass.constructor.call(this, {
		layout:'form',
        width:m_nRoleWidth,
        height:m_nRoleHeight,
        plain: true,
        title: '增加权限',
        closeAction: 'hide',
        draggable:false,
        modal: true,
        defaultType: 'textfield',
        defaults: {width: 250},
        iconCls: 'form',
        items: [{xtype: 'dataRightCombo2', autoSelect : false, fieldLabel: "省份/地市权限", emptyText: "请查看"}], 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function()
            {  
            	//获取需要进行提交的界面权限
            	var pageRight = "";
            	
            	var pageObj = me.items.items[1];
            	if(pageObj != undefined){    //如果存在页面权限则进行以下操作
            		var pageItems = pageObj.items;
                	for(var i = 0;i < pageItems.length;i++){
                		if(pageItems.itemAt(i).checked){
                			pageRight += pageItems.itemAt(i).name + ',';
                			
                		}
                	}
                	var lastPageIndex = pageRight.lastIndexOf(",");
                	pageRight = pageRight.substr(0,lastPageIndex);
            	}
            	
            	//获取需要进行提交的数据权限及界面权限
            	var pageRight1 = pageRight.length == 0 ? "pageRight": pageRight;
            	var dataRight1 = dataRight.length == 0 ? "dataRight": dataRight;
            	Ext.getCmp('addPanel').rightValue[me.nTreeId] = pageRight1 + ";" + dataRight1;
            	Ext.Msg.alert('温馨提示', '新增成功',function(){
            		me.hide();
            	});
        	}
        },{
            text: '关闭',
            iconCls:'exit',
            handler: function()
            {
            	me.hide();
            }
        }],
        listeners:{
        	//权限窗体隐藏时省份下拉框也隐藏
        	hide: function(){me.items.items[0].getTree().hide();}
        }
	});
};
Ext.extend(Ext.AddPermissionWindow, Ext.Window);
//修改权限
Ext.EditPermissionWindow = function()
{   
	var me = this;
	Ext.EditPermissionWindow.superclass.constructor.call(this, {
		layout:'form',
        width:m_nRoleWidth,
        height:m_nRoleHeight,
        plain: true,
        title: '修改权限',
        closeAction: 'hide',
        draggable:false,
        modal: true,
        defaultType: 'textfield',
        defaults: {width: 250},
        iconCls: 'form',
        items: [{xtype: 'dataRightCombo1', autoSelect : false, fieldLabel: "省份/地市权限", emptyText: "请查看"}], 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function()
            {  
            	//获取需要进行提交的界面权限
            	var pageRight = "";
            	
            	var pageObj = me.items.items[1];
            	if(pageObj != undefined){
            		var pageItems = pageObj.items;
                	for(var i = 0;i < pageItems.length;i++){
                		if(pageItems.itemAt(i).checked){
                			pageRight += pageItems.itemAt(i).name;
                			pageRight += ",";
                		}
                	}
                	var lastPageIndex = pageRight.lastIndexOf(",");
                	pageRight = pageRight.substr(0,lastPageIndex);		
                	
            	}
            	
            	//获取需要进行提交的数据权限
          //  	var dataRight1 = "";
            	
            	var pageRight1 = pageRight.length == 0 ? "pageRight": pageRight;
            	var dataRight1 = dataRight.length == 0 ? "dataRight":dataRight;
            	Ext.getCmp('editPanel').rightValue[me.nTreeId] = pageRight1 + ";" + dataRight1;
            	Ext.Msg.alert('温馨提示', '修改成功',function(){
            		me.hide();
            	});
        	}
        },{
            text: '关闭',
            iconCls:'exit',
            handler: function()
            {
            	me.hide();
            }
        }],
        listeners:{
        	hide: function(){me.items.items[0].getTree().hide();}
        }
	});
};
Ext.extend(Ext.EditPermissionWindow, Ext.Window);

//批量新增或批量修改报表权限
Ext.BatchPermissionWindow = function()
{   
	var me = this;
	Ext.BatchPermissionWindow.superclass.constructor.call(this, {
	  layout:'form',
      width:m_nRoleWidth,
      height:m_nRoleHeight,
      plain: true,
      title: '批量设置权限',
      closeAction: 'hide',
      draggable:false,
      modal: true,
      defaultType: 'textfield',
      defaults: {width: 250},
      iconCls: 'form',
      items: [{xtype: 'dataRightCombo3', autoSelect : false, fieldLabel: "省份/地市权限", emptyText: "请查看"}], 
      buttons: [{
          text:'提交',
          iconCls:'save',
          handler: function()
          {  
          	//获取需要进行提交的界面权限
          	var pageRight = "";
          	
          	var pageObj = me.items.items[1];
          	if(pageObj != undefined){
          		var pageItems = pageObj.items;
              	for(var i = 0;i < pageItems.length;i++){
              		if(pageItems.itemAt(i).checked){
              			pageRight += pageItems.itemAt(i).name;
              			pageRight += ",";
              		}
              	}
              	var lastPageIndex = pageRight.lastIndexOf(",");
              	pageRight = pageRight.substr(0,lastPageIndex);
          	}
          	
          	//获取需要进行提交的数据权限
          	var pageRight1 = pageRight.length == 0 ? "pageRight": pageRight;
          	var dataRight1 = dataRight.length == 0 ? "dataRight":dataRight;
          	
//          	var nowPanelObj = g_oViewPort.items.get(1).items.items[0];
          	//var nowPanelObj = Ext.getCmp(nowPanelId);
          	//nowPanelObj.isBatch = true;
          	if(Ext.getCmp('editPanel') != undefined){
              	Ext.getCmp('editPanel').rightValue[me.nTreeId] = pageRight1 + ";" + dataRight1;
              	Ext.getCmp('editPanel').isBatch = true;
          	}
          	if(Ext.getCmp('addPanel') != undefined){
          		Ext.getCmp('addPanel').rightValue[me.nTreeId] = pageRight1 + ";" + dataRight1;
          		Ext.getCmp('addPanel').isBatch = true;
          	}
          	
          	Ext.Msg.alert('温馨提示', '批量设置成功',function(){
          		me.hide();
          	});
      	}
      },{
          text: '关闭',
          iconCls:'exit',
          handler: function()
          {
          	me.hide();
          }
      }],
      listeners:{
      	hide: function(){me.items.items[0].getTree().hide();}
      }
	});
};
Ext.extend(Ext.BatchPermissionWindow, Ext.Window);

//查看角色
Ext.ViewPermissionWindow = function()
{
	var me = this;
	Ext.ViewPermissionWindow.superclass.constructor.call(this, {
		layout:'form',
        width:m_nRoleWidth,
        height:m_nRoleHeight,
        plain: true,
        title: '查看权限',
        closeAction: 'hide',
        draggable:false,
        modal: true,
        defaultType: 'textfield',
        defaults: {width: 250},
        iconCls: 'form',
        items:[{xtype: 'dataRightCombo', autoSelect : false, fieldLabel: "省份/地市权限", emptyText: "请查看"}
        ], 
        buttons: [
        {
            text: '关闭',
            iconCls:'exit',
            handler: function()
            {
            	me.hide();
//            	me.destroy( );
            }
        }],
        listeners:{
        	hide: function(){me.items.items[0].getTree().hide();}
        }
	});
};

Ext.extend(Ext.ViewPermissionWindow, Ext.Window);

//获取查看窗口items
function getViewWindowItems(nTreeId, nBgRoleId){
	var me = this;
	Ext.Ajax.request({
		url: contextPath+'/module/admin/security/getRoleCheckGroup.json',   
		method: 'post',   
		params: {treeId: nTreeId},
		success: function(resp, action){
			//查看窗口不存在则新建
			if(!me.viewPermissionWindow){
				me.viewPermissionWindow = new Ext.ViewPermissionWindow();
				me.viewPermissionWindow.items.items[0].getTree().hide();
			}
			//由于每个报表界面权限不同，所以在显示前需先移除之前的再重新加载
			if(me.viewPermissionWindow.items.get(1) != undefined){
				me.viewPermissionWindow.remove(me.viewPermissionWindow.items.get(1));
			}
			//让省份树结构随着窗口大小而始终显示在中间
			var centerWin = new Ext.ux.CenterWindowPlugin({});
			centerWin.init(me.viewPermissionWindow, me.viewPermissionWindow.items.items[0].getTree());  //   将window随着浏览器大小变化
		    
			me.viewPermissionWindow.nTreeId = nTreeId;
			me.viewPermissionWindow.nBgRoleId = nBgRoleId;

			var obj = Ext.util.JSON.decode(resp.responseText);
			var checkBoxGroup;
			//判断省份/地市权限是否显示
			if(obj[0].map.N_ISPAGECODE == 0){
				me.viewPermissionWindow.items.get(0).hide();
	        }else{
	        	me.viewPermissionWindow.items.get(0).show();
	        	getDataValue(me.viewPermissionWindow);    //获取省份数据权限
	        }
			//判断界面权限是否显示
			if(obj.length > 0){
				var checkBoxName = "界面权限";
				var checkBoxId = "viewId";
				var checkBoxItems = [];
				//获取界面权限可选项
				for(var i = 0;i < obj.length;i++){
					var boxLabel = obj[i].map.S_RIGHTNAME;
					var value = obj[i].map.S_RIGHTCODE;
					checkBoxItems.push({
						id: value + checkBoxId,
						boxLabel: boxLabel,
						name : value
					});
				}
				checkBoxGroup = getCheckBoxGroup(checkBoxItems,checkBoxName,checkBoxId);
				me.viewPermissionWindow.add(checkBoxGroup);
				me.viewPermissionWindow.checkBoxId = checkBoxId;
				getPageValue(me.viewPermissionWindow);
			}
			me.viewPermissionWindow.show();
 
		},   
		failure:function(resp, action){   
		    Ext.Msg.alert('温馨提示','系统错误');   
		}   
	}); 
}
//获取修改窗口items
function getEditWindowItems(nTreeId, nBgRoleId){
	var me = this;
	Ext.Ajax.request({
		url: contextPath+'/module/admin/security/getRoleCheckGroup.json',   
		method: 'post',   
		params: {treeId: nTreeId},
		success: function(resp, action){
			if(!me.editPermissionWindow){
				me.editPermissionWindow = new Ext.EditPermissionWindow();
				me.editPermissionWindow.items.items[0].getTree().hide();
			}
			if(me.editPermissionWindow.items.get(1) != undefined){
				me.editPermissionWindow.remove(me.editPermissionWindow.items.get(1));
			}
			var centerWin = new Ext.ux.CenterWindowPlugin({});
			centerWin.init(me.editPermissionWindow, me.editPermissionWindow.items.items[0].getTree());  //   将window随着浏览器大小变化
			dataRight = "-1";
		    me.editPermissionWindow.nTreeId = nTreeId;
			me.editPermissionWindow.nBgRoleId = nBgRoleId;
			
			
			var obj = Ext.util.JSON.decode(resp.responseText);  
			//判断省份/地市权限是否显示
			if(obj[0].map.N_ISPAGECODE == 0){
				me.editPermissionWindow.items.get(0).hide();
	        }else{
	        	me.editPermissionWindow.items.get(0).show();
	        	getDataValue(me.editPermissionWindow); 
	        }
			var checkBoxGroup;
			if(obj.length > 0){
				var checkBoxName = "界面权限";
				var checkBoxId = "editId";
				var checkBoxItems = [];
				//获取界面权限可选项
				for(var i = 0;i < obj.length;i++){
					var boxLabel = obj[i].map.S_RIGHTNAME;
					var value = obj[i].map.S_RIGHTCODE;
					checkBoxItems.push({
						id: value + checkBoxId,
						boxLabel: boxLabel,
						name : value
					});
				}
			    checkBoxGroup = getCheckBoxGroup(checkBoxItems,checkBoxName,checkBoxId);
			    me.editPermissionWindow.add(checkBoxGroup);
				me.editPermissionWindow.checkBoxId = checkBoxId;
			    getPageValue(me.editPermissionWindow);
			}
			me.editPermissionWindow.show();
		},   
		failure:function(resp, action){   
		    Ext.Msg.alert('温馨提示','系统错误');   
		}   
	}); 
}
//获取新增窗口items
function getAddWindowItems(nTreeId){
	var me = this;
	Ext.Ajax.request({
		url: contextPath+'/module/admin/security/getRoleCheckGroup.json',   
		method: 'post',   
		params: {treeId: nTreeId},
		success: function(resp, action){
			if(!me.addPermissionWindow){
				me.addPermissionWindow = new Ext.AddPermissionWindow();
				me.addPermissionWindow.items.items[0].getTree().hide();
			}
			if(me.addPermissionWindow.items.get(1) != undefined){
				me.addPermissionWindow.remove(me.addPermissionWindow.items.get(1));
			}
			me.addPermissionWindow.nTreeId = nTreeId;
			dataRight = "-1";
			var centerWin = new Ext.ux.CenterWindowPlugin({});
			centerWin.init(me.addPermissionWindow, me.addPermissionWindow.items.items[0].getTree());  //   将window随着浏览器大小变化
			
			var obj = Ext.util.JSON.decode(resp.responseText); 
			//判断省份/地市权限是否显示
			if(obj[0].map.N_ISPAGECODE == 0){
				me.addPermissionWindow.items.get(0).hide();
	        }else{
	        	me.addPermissionWindow.items.get(0).show();
	        	getDataValue(me.addPermissionWindow); 
	        }
			var checkBoxGroup;
			if(obj.length > 0){
				var checkBoxName = "界面权限";
				var checkBoxId = "addId";
				var checkBoxItems = [];
				//获取界面权限可选项
				for(var i = 0;i < obj.length;i++){
					var boxLabel = obj[i].map.S_RIGHTNAME;
					var value = obj[i].map.S_RIGHTCODE;
					checkBoxItems.push({
						id: value + checkBoxId,
						boxLabel: boxLabel,
						name : value
					});
				}
			    checkBoxGroup = getCheckBoxGroup(checkBoxItems,checkBoxName,checkBoxId);
			    me.addPermissionWindow.add(checkBoxGroup);
				me.addPermissionWindow.checkBoxId = checkBoxId;
				getPageValue(me.addPermissionWindow);
			}
			me.addPermissionWindow.show();
		},   
		failure:function(resp, action){   
		    Ext.Msg.alert('温馨提示','系统错误');   
		}   
	}); 
}
//批量新增或修改报表中心界面权限信息
function getBatchWindowItems(nTreeId, nBgRoleId){
	var me = this;
	Ext.Ajax.request({
		url: contextPath+'/module/admin/security/getRoleCheckGroup.json',   
		method: 'post',   
		params: {treeId: nTreeId},
		success: function(resp, action){
			if(!me.batchPermissionWindow){
				me.batchPermissionWindow = new Ext.BatchPermissionWindow();
				me.batchPermissionWindow.items.items[0].getTree().hide();
			}
			if(me.batchPermissionWindow.items.get(1) != undefined){
				me.batchPermissionWindow.remove(me.batchPermissionWindow.items.get(1));
			}
			me.batchPermissionWindow.nTreeId = nTreeId;
			dataRight = "-1";
			var centerWin = new Ext.ux.CenterWindowPlugin({});
			centerWin.init(me.batchPermissionWindow, me.batchPermissionWindow.items.items[0].getTree());  //   将window随着浏览器大小变化
			//清空数据权限信息
			var treePanel = me.batchPermissionWindow.items.items[0];
			treePanel.setRawValue("");
			
			var obj = Ext.util.JSON.decode(resp.responseText); 
			//判断省份/地市权限是否显示
			if(obj[0].map.N_ISPAGECODE == 0){
				me.batchPermissionWindow.items.get(0).hide();
	        }else{
	        	me.batchPermissionWindow.items.get(0).show();
	        	getDataValue(me.batchPermissionWindow); 
	        }
			var checkBoxGroup;
			if(obj.length > 0){
				var checkBoxName = "界面权限";
				var checkBoxId = "batchId";
				var checkBoxItems = [];

				//获取界面权限可选项
				for(var i = 0;i < obj.length;i++){
					var boxLabel = obj[i].map.S_RIGHTNAME;
					var value = obj[i].map.S_RIGHTCODE;
					checkBoxItems.push({
						id: value + checkBoxId,
						boxLabel: boxLabel,
						name : value
					});
				}
			    checkBoxGroup = getCheckBoxGroup(checkBoxItems,checkBoxName,checkBoxId);
			    me.batchPermissionWindow.add(checkBoxGroup);
				me.batchPermissionWindow.checkBoxId = checkBoxId;
				getPageValue(me.batchPermissionWindow);
				me.batchPermissionWindow.show();
			}

		},   
		failure:function(resp, action){   
		    Ext.Msg.alert('温馨提示','系统错误');   
		}   
	}); 
}

//定义myCheckBoxGroup
function getCheckBoxGroup(checkBoxItems, checkBoxName,checkBoxId){
	return new Ext.form.CheckboxGroup({  
		id: checkBoxId,
		fieldLabel: checkBoxName,
	    xtype : 'checkboxgroup',      
	    columns : 3,    
        allowBlank: true,  
	    items : checkBoxItems    
	}); 
}
//获取已存在的界面权限信息
function getPageValue(win){
    var list = [];
    //将之前的勾选项清空
    for(var j = 0;j < win.items.items[1].items.length;j++){
    	list.push(false);
    }
    Ext.getCmp(win.checkBoxId).setValue(list);
	
    Ext.Ajax.request({
		url: contextPath+'/module/admin/security/getRolePermissionInfo.json',   
		method: 'post', 
		params: {treeId: win.nTreeId,roleId: win.nBgRoleId},
		success: function(resp, action){
			var obj = Ext.util.JSON.decode(resp.responseText);   
			//获取数据库存储的界面权限信息
			if(obj.length>0 && obj[0].sPageRight != null){
				var pageStr = obj[0].sPageRight;
				var strs = new Array();
				strs = pageStr.split(",");
				var map = {};
				for(var i = 0;i < strs.length;i++){
					map[strs[i] + win.checkBoxId ] = true;
					Ext.getCmp(win.checkBoxId).setValue(map);
				}
			}
			//新增时，临时保存修改的界面权限信息
			if(win.checkBoxId == "addId"){
		    	var rightMap = Ext.getCmp('addPanel').rightValue;
		    	setCheckBoxForPage(win, rightMap);
		    }  
			//修改时，临时保存修改的界面权限信息
			if(win.checkBoxId == "editId"){
		    	var rightMap = Ext.getCmp('editPanel').rightValue;
		    	setCheckBoxForPage(win, rightMap);
		    }  
			//批量修改时，临时保存修改的界面权限信息
			if(win.checkBoxId == "batchId"){
		    	var rightMap = Ext.getCmp('editPanel') == undefined ? Ext.getCmp('addPanel').rightValue : Ext.getCmp('editPanel').rightValue;
		    	setCheckBoxForPage(win, rightMap);
		    }
			
		},   
		failure:function(resp, action){   
		    Ext.Msg.alert('温馨提示','系统错误');   
		}   
	}); 
}
//获取省份地市数据权限
function getDataValue(win){
	var treePanel = win.items.items[0];
	var root = treePanel.getTree().getRootNode();
	treePanel.getTree().clearAll();
	treePanel.setRawValue('');
//    root.expand(true);
	treePanel.on("load",function(node,response){
		Ext.Ajax.request({   
			   url: contextPath+'/module/admin/security/getRolePermissionInfo1.json',   
			   method: 'post',   
			   params: {treeId: win.nTreeId,roleId: win.nBgRoleId},
			   success: function(resp, action)
			   {    
			        var obj = Ext.util.JSON.decode(resp.responseText);
			        if(obj.dataRight != undefined){
						root.getOwnerTree().setSelect(root, obj.dataRight);	
			        }
			        //新增时，临时保存数据权限信息
			        if(win.checkBoxId == "addId"){
				    	var rightMap = Ext.getCmp('addPanel').rightValue;
				    	setCheckTreeForPro(root, win.nTreeId, rightMap);
				    }  
			        //修改时，临时保存数据权限信息
					if(win.checkBoxId == "editId"){
				    	var rightMap = Ext.getCmp('editPanel').rightValue;
				    	setCheckTreeForPro(root, win.nTreeId, rightMap);
				    }  
					//批量修改时，临时保存修改的界面权限信息
					if(win.checkBoxId == "batchId"){
				    	var rightMap = Ext.getCmp('editPanel') == undefined ? Ext.getCmp('addPanel').rightValue : Ext.getCmp('editPanel').rightValue;
				    	setCheckTreeForPro(root, win.nTreeId, rightMap);
				    } 
			    },   
			    failure:function(resp, action)
			    {   
			        Ext.Msg.alert('温馨提示','系统错误!');   
			    }   
			}); 
	});
}

//设置临时存在的省份数据权限信息
function setCheckTreeForPro(root, treeId, rightMap){
	for(var i in rightMap){
		var str = rightMap[treeId];
		if( str != undefined){
			var strs = str.split(";")[1].split(",");
			var list = [];
			for(var i = 0;i < strs.length;i++){
				list.push(parseInt(strs[i]));
			}
			root.getOwnerTree().setSelect(root, list);	
		}
	}
}
//设置临时存在的界面权限信息
function setCheckBoxForPage(win, rightMap){
	for(var i in rightMap){
		var str = rightMap[win.nTreeId];
		if( str != undefined){
			var strs = str.split(";")[0].split(",");
			var map = {};
			for(var i = 0;i < strs.length;i++){
				map[strs[i] + win.checkBoxId ] = true;
				Ext.getCmp(win.checkBoxId).setValue(map);
			}
		}
	}
}