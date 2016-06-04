Ext.BgDeptMenu = function()
{
	var me = this;
	Ext.BgDeptMenu.superclass.constructor.call(this, {
		items: 
		[
			{
		 		text: '查看',
		 		iconCls: 'look',
		 		handler: function(oItem, oEvent)
		 		{
					var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
		 			var nBgDeptId = oNode.id.replace(/[^0-9]*/,'');
		 			if(!me.ViewBgDeptWindow)
		 			{
		 				me.ViewBgDeptWindow = new Ext.ViewBgDeptWindow();
		 			}
		 			me.ViewBgDeptWindow.nBgDeptId=nBgDeptId;
		 			if(!me.ViewBgDeptWindow.isVisible()) me.ViewBgDeptWindow.show(oNode.getUI().getEl());
	 			}
		 	},
		 	{
		 		text: '增加',
		 		iconCls: 'add',
		 		handler: function(oItem, oEvent)
		 		{
		 			if(!me.AddBgDeptWindow)
		 			{
		 				me.AddBgDeptWindow = new Ext.AddBgDeptWindow();
		 			}
		 			var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
		 			if(!me.AddBgDeptWindow.isVisible()) me.AddBgDeptWindow.show(oNode.getUI().getEl());
		 		}
		 	},
		 	{
		 		text: '修改',
		 		iconCls: 'edit_icon',
		 		handler: function(oItem, oEvent)
		 		{
		 			var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
		 			var nBgDeptId = oNode.id.replace(/[^0-9]*/,'');
		 			if(!me.EditBgDeptWindow)
		 			{
		 				me.EditBgDeptWindow = new Ext.EditBgDeptWindow();
		 			}
		 			me.EditBgDeptWindow.nBgDeptId=nBgDeptId;
		 			if(!me.EditBgDeptWindow.isVisible()) me.EditBgDeptWindow.show(oNode.getUI().getEl());
	 			}
		 	},
		 	{
		 		text: '删除',
		 		iconCls: 'del',
		 		handler: function(oItem, oEvent)
		 		{
			 		Ext.MessageBox.confirm('温馨提示', '真的要删除此部门吗？', function(buttonId, text, opt)
			 		{
    					if(buttonId == 'yes')
    					{
    						var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
    						var nBgDeptId = oNode.id.replace(/[^0-9]*/,'');
    						Ext.Ajax.request({   
    							   url: 'delBgDept.json',   
    							   method: 'post',   
    							   params: {nBgDeptId: nBgDeptId},
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
Ext.extend(Ext.BgDeptMenu, Ext.menu.Menu);