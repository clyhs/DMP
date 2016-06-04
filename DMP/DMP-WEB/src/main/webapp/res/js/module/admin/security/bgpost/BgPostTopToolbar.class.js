Ext.BgPostTopToolbar = function(oGrid)
{
	var me = this;
	Ext.BgPostTopToolbar.superclass.constructor.call(this, {
        enableOverflow: true,
        items: [
        	{
        		xtype: 'tbtext', 
        		text: '岗位名称：'
        	},
        	{
        		xtype: 'textfield'
        	},{xtype: 'tbspacer', width: 10},
        	{
        		xtype: 'tbtext', 
        		text: '所属部门：'
        	},
        	{
        		xtype: 'textfield'
        	},{xtype: 'tbspacer', width: 10},
        	{
	            text: '搜索',
	            id: 'query',
	            hidden: false,
	            iconCls: 'search',
	            handler: function(oButton, oEvent)
	            {
        			var aComponents = me.findByType('textfield');
        			var sName = aComponents[0].getValue();
        			var sDeptName = aComponents[1].getValue();
        			oGrid.sName = sName;
        			oGrid.sDeptName = sDeptName;
        			oGrid.getStore().load({params:{sName:sName, sDeptName:sDeptName}});
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
	        				oButton.Window = new Ext.ViewBgPostWindow();
	        			}
	        			oButton.Window.nBgPostId=oGrid.getSelectionModel().getSelected().data.bgPostId;
	        			if(!oButton.Window.isVisible()) oButton.Window.show(oButton.getEl());
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要查看的岗位资料！');
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
        				oButton.Window = new Ext.AddBgPostWindow();
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
	        				oButton.Window = new Ext.EditBgPostWindow();
	        			}
	        			oButton.Window.nBgPostId=oGrid.getSelectionModel().getSelected().data.bgPostId;
	        			if(!oButton.Window.isVisible()) oButton.Window.show(oButton.getEl());
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要修改的岗位资料！');
	    			}
				}
        	},{xtype: 'tbspacer', width: 10},
        	{
        		text: '删除',
        		id: 'delete',
  	            hidden: false,
        		iconCls: 'del',
	            handler: function(oButton, oEvent)
	            {
        			var aRecord = oGrid.getSelectionModel().getSelections();
	    			if(aRecord.length > 0)
		    		{
	    				Ext.MessageBox.confirm('温馨提示', '真的要删除选择的岗位资料吗？', function(buttonId, text, opt)
	    				{
	    					if(buttonId == 'yes')
	    					{
	    						var aBgPostId = [0];
	    						for(var nKey in aRecord)
	    						{
	    							if(aRecord[nKey].data != undefined)
	    							{
	    								if(aRecord[nKey].data.nBgPostId != 1) 
	    									aBgPostId.push(aRecord[nKey].data.bgPostId);
	    							}
	    						}

	    						Ext.Ajax.request({   
	    							   url: 'delBgPost.json',   
	    							   method: 'post',   
	    							   params: {sBgPostIdList: aBgPostId.join(',')},   
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
	    				Ext.MessageBox.alert('温馨提示', '请选择要删除的岗位资料！');
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

Ext.extend(Ext.BgPostTopToolbar, Ext.Toolbar);