Ext.BgUserTopToolbar = function(oGrid)
{
	var me = this;	
	Ext.BgUserTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
        	{
        		xtype: 'tbtext', 
        		text: '用户帐号：'
        	},
        	{
        		xtype: 'textfield'
        	},{xtype: 'tbspacer', width: 10},
        	{
        		xtype: 'tbtext', 
        		text: '用户名称：'
        	},
        	{
        		xtype: 'textfield'
        	},
        	{xtype: 'tbspacer', width: 10},
        			{
	            text: '搜索',
	            id: 'query',
	            hidden: false,
	            iconCls: 'search',
	            handler: function(oButton, oEvent)
	            {
        			var aComponents = me.findByType('textfield');
        			var sAccount = aComponents[0].getValue();
        			var sName = aComponents[1].getValue();
//        			var oNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
//		 			var nBgRoleId = oNode ? oNode.id.replace(/[^0-9]*/,'') : 0;
        			oGrid.sName = sName;
        			oGrid.sAccount = sAccount;
        			oGrid.getStore().load({params:{nBgRoleId:null, sName:sName, sAccount:sAccount}});
        		}
        	},{xtype: 'tbspacer', width: 10},
        	{
	            text: '查看',
	            id: 'view',
	            hidden: false,
	            iconCls: 'look',
	            handler: function(oButton, oEvent)
	            {
        			var oRecord = oGrid.getSelectionModel().getSelected();
	    			if(oRecord != undefined)
	    			{
	        			if(!oButton.Window)
	        			{
	        				oButton.Window = new Ext.ViewBgUserWindow();
	        			}
	        			oButton.Window.nBgUserId=oRecord.data.bgUserId;
	        			if(!oButton.Window.isVisible()) oButton.Window.show(oButton.getEl());
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要查看的用户资料！');
	    			}
    			}
        	},{xtype: 'tbspacer', width: 10},
        	{
	            text: '增加',
	            id: 'add',
	            hidden: false,
	            iconCls: 'add',
	            handler: function(oButton, oEvent)
	            {
        			if(!oButton.Window)
        			{
        				oButton.Window = new Ext.AddBgUserWindow();
        			}
        			if(!oButton.Window.isVisible()) oButton.Window.show(oButton.getEl());
				}
        	},{xtype: 'tbspacer', width: 10},
        	{
	            text: '修改',
	            id: 'edit',
	            hidden: false,
	            iconCls: 'edit_icon',
	            handler: function(oButton, oEvent)
	            {
        			var oRecord = oGrid.getSelectionModel().getSelected();
	    			if(oRecord != undefined)
	    			{
	        			if(!oButton.Window)
	        			{
	        				oButton.Window = new Ext.EditBgUserWindow();
	        			}
	        			oButton.Window.nBgUserId=oRecord.data.bgUserId;
	        			if(!oButton.Window.isVisible()) oButton.Window.show(oButton.getEl());
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要修改的用户资料！');
	    			}
				}
        	},{xtype: 'tbspacer', width: 10},
        	{
        		text: '删除',
        		id: 'delete',
 	            hidden: false,
        		iconCls: 'del',
	            handler: function(oButton, oEvent){
        			var aRecord = oGrid.getSelectionModel().getSelections();
	    			if(aRecord.length > 0)
		    		{
	    				Ext.MessageBox.confirm('温馨提示', '真的要删除选择的用户资料吗？', function(buttonId, text, opt)
	    				{
	    					if(buttonId == 'yes')
	    					{
	    						var aBgUserId = [0];
	    						for(var nKey in aRecord)
	    						{
	    							if(aRecord[nKey].data != undefined)
	    							{
	    								if(aRecord[nKey].data.bgUserId != 1) 
	    									aBgUserId.push(aRecord[nKey].data.bgUserId);
	    							}
	    						}

	    						Ext.Ajax.request({   
	    							   url: 'delBgUser.json',   
	    							   method: 'post',   
	    							   params: {sBgUserIdList: aBgUserId.join(',')},   
	    							   success: function(resp, action)
	    							   {   
	    							       var obj = Ext.util.JSON.decode(resp.responseText);   
	    							       if(obj.success)   
	    							       {    
	    							    	   oGrid.getStore().reload();
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
		    		}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要删除的用户资料！');
	    			}
				}
        	},
        	'->',
        	{
        		text: '帮助',
        		iconCls: 'help'
        	}
        ]
	});
};

Ext.extend(Ext.BgUserTopToolbar, Ext.Toolbar);