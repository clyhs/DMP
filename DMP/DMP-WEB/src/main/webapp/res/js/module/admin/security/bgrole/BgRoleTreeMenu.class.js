/**
 * 角色树型结构菜单项
 */
Ext.BgRoleTreeMenu = function()
{
	Ext.BgRoleTreeMenu.superclass.constructor.call(this, {
		items: 
		[
			{
		 		text: '查看',
		 		id: 'query',
		 		iconCls: 'look',
		 		handler: function(oItem, oEvent)
		 		{  
		 			showActivePanel(this);
		 	}
			
		    },
		 	{
		 		text: '增加',
		 		id: 'add',
		 		iconCls: 'add',
		 		handler: function(oItem, oEvent)
		 		{   
		 			showActivePanel(this);
		 		}
		 	},
		 	{
		 		text: '修改',
		 		id: 'edit',
		 		iconCls: 'edit_icon',
		 		handler: function(oItem, oEvent)
		 		{
		 			showActivePanel(this);
	 			}
		 	},
		 	{
		 		text: '删除',
		 		iconCls: 'del',
		 		handler: function(oItem, oEvent)
		 		{
		 		
		 			//将editPanel及addPanel中临时保存的rightValue清空
		 			if(Ext.getCmp('editPanel') != undefined){
		 				Ext.getCmp('editPanel').rightValue = {};
		 		    }
		 		    if(Ext.getCmp('addPanel') != undefined){
		 				Ext.getCmp('addPanel').rightValue = {};
		 		    }
		 			var nBgRoleId = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode().id.replace(/[^0-9]*/,'');
		 			//判断当前角色是否正在使用，如正在使用，则不允许删除
		 			Ext.Ajax.request({
		 				url: 'checkRoleUse.json',
		 				method: 'post',
		 				params: {nBgRoleId: nBgRoleId},
		 				success: function(resp, action){
		 					var obj = Ext.util.JSON.decode(resp.responseText);   
		 					if(obj.success){
		 						Ext.MessageBox.confirm('温馨提示', '真的要删除此角色吗？', function(buttonId, text, opt){
		 							if(buttonId == 'yes')
		 	    					{
		 								Ext.Ajax.request({   
			    							   url: 'delBgRole.json',   
			    							   method: 'post',   
			    							   params: {nBgRoleId: nBgRoleId},
			    							   success: function(resp, action){   
			    							        var obj = Ext.util.JSON.decode(resp.responseText);   
			    							        if(obj.success){   
			    							        	var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
			    							        	oNode.removeAll(true);
			    							        	oNode.remove(true);
			    							        	var treePermissionPanel = g_oViewPort.items.get(1);
			    							        	treePermissionPanel.doLayout();
//			    							        	Ext.Msg.alert('温馨提示', '删除成功',function(){
//			    							        		treePermissionPanel.items.get(0).hide();
//			    						            	});
			    							        }else{   
			    							            Ext.Msg.alert('温馨提示', obj.msg);   
			    							        }     
			    							   },   
			    							   failure:function(resp, action)
			    							   {   
			    							        Ext.Msg.alert('温馨提示','系统错误');   
			    							   }   
			    	    				}); 
		 								
		 	    					}
		 						});
		 					}else{
		 						Ext.Msg.alert('温馨提示', obj.msg); 
		 					}
		 				}
		 			});

	 			}
		 	}
		]
	});
};
Ext.extend(Ext.BgRoleTreeMenu, Ext.menu.Menu);

//将editPanel及addPanel中临时保存的rightValue清空
function showActivePanel(me){
	
	var treePermissionPanel = g_oViewPort.items.get(1);   //获取角色权限树型panel
	var beforePanel = treePermissionPanel.layout.activeItem;   //获取当前活动的panel
	
	alert(treePermissionPanel.id);
	//当前活动的panel存在,则进行判断，否则直接显示
	if(beforePanel != null && beforePanel.isVisible()){
		//如果正处于修改角色权限状态而点击查看角色权限菜单项，则弹出提示信息
		if(beforePanel.id == 'editPanel'){
			Ext.MessageBox.confirm('温馨提示', '您正处于修改界面，确定离开吗？', function(buttonId, text, opt){
				if(buttonId == 'yes'){
				    setActiveItem(me, treePermissionPanel);
				}
			});
		}else if(beforePanel.id == 'addPanel'){   //如果正处于新增界面，则弹出提示信息
			Ext.MessageBox.confirm('温馨提示', '您正处于新增界面，确定离开吗？', function(buttonId, text, opt){
				if(buttonId == 'yes'){
				    setActiveItem(me, treePermissionPanel);
				}
			});
		}else{
			setActiveItem(me, treePermissionPanel);
		}
		
	}else{
		setActiveItem(me, treePermissionPanel);
	}
}

//将panel设为active类型
function setActiveItem(me, treePermissionPanel){
	var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
	var nBgRoleId = oNode.id.replace(/[^0-9]*/,'');   //获取角色id
	var clickName = me.id;                            //获取当前点击事件的id，用来标识需要展示的是哪个panel
	
	//将editPanel及addPanel中临时保存的rightValue清空
	if(Ext.getCmp('editPanel') != undefined){
		Ext.getCmp('editPanel').rightValue = {};
    }
    if(Ext.getCmp('addPanel') != undefined){
		Ext.getCmp('addPanel').rightValue = {};
    }
	//如果点击名称为query，则设置当前展开项为查看角色权限
	if(clickName == 'query'){
		if(nBgRoleId != 0){
			if(!me.viewBgRolePanel){
				me.viewBgRolePanel = new Ext.ViewBgRolePanel(nBgRoleId, clickName);
				treePermissionPanel.add(me.viewBgRolePanel);

		    }
	    me.viewBgRolePanel.clickName = clickName;
		me.viewBgRolePanel.nBgRoleId = nBgRoleId;
	    me.viewBgRolePanel.show();
		treePermissionPanel.doLayout();
			
		treePermissionPanel.getLayout().setActiveItem('viewPanel'); 	
		}
	}
	//如果点击名称为edit，则设置当前展开项为修改角色权限
	if(clickName == 'edit'){
		if(nBgRoleId != 0){
			if(!me.editBgRolePanel){
 				me.editBgRolePanel = new Ext.EditBgRolePanel(nBgRoleId, clickName);
 				treePermissionPanel.add(me.editBgRolePanel);
 			}
 		me.editBgRolePanel.clickName = clickName;
		me.editBgRolePanel.nBgRoleId = nBgRoleId;
        me.editBgRolePanel.show();
 	    treePermissionPanel.doLayout();
 	    treePermissionPanel.getLayout().setActiveItem('editPanel'); 	
		}	
	}
	//如果点击名称为ad，则设置当前展开项为新增角色权限
	if(clickName == 'add'){
		if(!me.addBgRolePanel){
			me.addBgRolePanel = new Ext.AddBgRolePanel(clickName);
			treePermissionPanel.add(me.addBgRolePanel);
		}
		me.addBgRolePanel.clickName = clickName;
        me.addBgRolePanel.show();
		treePermissionPanel.doLayout();
		treePermissionPanel.getLayout().setActiveItem('addPanel'); 
	}
}