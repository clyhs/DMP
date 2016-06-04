Ext.BgRoleMenu = function()
{
	var me = this;
	Ext.BgRoleMenu.superclass.constructor.call(this, {
		items: 
		[
			{
		 		text: '查看',
		 		iconCls: 'look',
		 		handler: function(oItem, oEvent)
		 		{
					var oNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
		 			var nBgRoleId = oNode.id.replace(/[^0-9]*/,'');
		 			if(!me.ViewBgRoleWindow)
		 			{
		 				me.ViewBgRoleWindow = new Ext.ViewBgRoleWindow();
		 			}
		 			me.ViewBgRoleWindow.nBgRoleId=nBgRoleId;
		 			if(!me.ViewBgRoleWindow.isVisible()) me.ViewBgRoleWindow.show(oNode.getUI().getEl());
	 			}
		 	},
		 	{
		 		text: '增加',
		 		iconCls: 'add',
		 		handler: function(oItem, oEvent)
		 		{
		 			if(!me.AddBgRoleWindow)
		 			{
		 				me.AddBgRoleWindow = new Ext.AddBgRoleWindow();
		 			}
		 			var oNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
		 			if(!me.AddBgRoleWindow.isVisible()) me.AddBgRoleWindow.show(oNode.getUI().getEl());
		 		}
		 	},
		 	{
		 		text: '修改',
		 		iconCls: 'edit_icon',
		 		handler: function(oItem, oEvent)
		 		{
		 			var oNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
		 			var nBgRoleId = oNode.id.replace(/[^0-9]*/,'');
		 			if(!me.EditBgRoleWindow)
		 			{
		 				me.EditBgRoleWindow = new Ext.EditBgRoleWindow();
		 			}
		 			me.EditBgRoleWindow.nBgRoleId=nBgRoleId;
		 			if(!me.EditBgRoleWindow.isVisible()) me.EditBgRoleWindow.show(oNode.getUI().getEl());
	 			}
		 	},
		 	{
		 		text: '删除',
		 		iconCls: 'del',
		 		handler: function(oItem, oEvent)
		 		{
			 		Ext.MessageBox.confirm('温馨提示', '真的要删除此角色吗？', function(buttonId, text, opt)
			 		{
    					if(buttonId == 'yes')
    					{
    						var nBgRoleId = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode().id.replace(/[^0-9]*/,'');
    						Ext.Ajax.request({   
    							   url: 'delBgRole.json',   
    							   method: 'post',   
    							   params: {nBgRoleId: nBgRoleId},
    							   success: function(resp, action)
    							   {   
    							        var obj = Ext.util.JSON.decode(resp.responseText);   
    							        if(obj.success)   
    							        {   
    							        	var oNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
    							        	oNode.removeAll(true);
    							        	oNode.remove(true);
    							        }   
    							        else  
    							        {   
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
	 			}
		 	}
		]
	});
};
Ext.extend(Ext.BgRoleMenu, Ext.menu.Menu);