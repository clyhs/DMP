Ext.BgNavMenu = function()
{
	var me = this;
	Ext.BgNavMenu.superclass.constructor.call(this, {
		items: 
		[
			{
		 		text: '查看',
		 		iconCls: 'look',
		 		handler: function(oItem, oEvent)
		 		{
					var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
		 			var nBgTreeId = oNode.id.replace(/[^0-9]*/,'');
		 			if(!me.ViewBgNavWindow)
		 			{
		 				me.ViewBgNavWindow = new Ext.ViewBgNavWindow();
		 			}
		 			me.ViewBgNavWindow.nBgTreeId=nBgTreeId;
		 			if(!me.ViewBgNavWindow.isVisible()) me.ViewBgNavWindow.show(oNode.getUI().getEl());
	 			}
		 	},
		 	{
		 		text: '增加',
		 		iconCls: 'add',
		 		handler: function(oItem, oEvent)
		 		{
		 			if(!me.AddBgNavWindow)
		 			{
		 				me.AddBgNavWindow = new Ext.AddBgNavWindow();
		 			}
		 			var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
		 			if(!me.AddBgNavWindow.isVisible()) me.AddBgNavWindow.show(oNode.getUI().getEl());
		 		}
		 	},
		 	{
		 		text: '修改',
		 		iconCls: 'edit_icon',
		 		handler: function(oItem, oEvent)
		 		{
		 			var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
		 			var nBgTreeId = oNode.id.replace(/[^0-9]*/,'');
		 			if(!me.EditBgNavWindow)
		 			{
		 				me.EditBgNavWindow = new Ext.EditBgNavWindow();
		 			}
		 			me.EditBgNavWindow.nBgTreeId=nBgTreeId;
		 			if(!me.EditBgNavWindow.isVisible()) me.EditBgNavWindow.show(oNode.getUI().getEl());
	 			}
		 	},
		 	{
		 		text: '删除',
		 		iconCls: 'del',
		 		handler: function(oItem, oEvent)
		 		{
			 		Ext.MessageBox.confirm('温馨提示', '真的要删除此导航结点吗？', function(buttonId, text, opt)
			 		{
    					if(buttonId == 'yes')
    					{
    						var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
    						var nBgTreeId = oNode.id.replace(/[^0-9]*/,'');
    						Ext.Ajax.request({   
    							   url: 'delBgNavTreeNode.json',   
    							   method: 'post',   
    							   params: {nBgTreeId: nBgTreeId},
    							   success: function(resp, action)
    							   {   
    							        var obj = Ext.util.JSON.decode(resp.responseText);   
    							        if(obj.success)   
    							        {   
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
Ext.extend(Ext.BgNavMenu, Ext.menu.Menu);