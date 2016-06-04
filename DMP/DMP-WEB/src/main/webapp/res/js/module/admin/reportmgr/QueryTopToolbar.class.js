Ext.QueryTopToolbar = function(oGrid)
{
	var me = this;	
	Ext.QueryTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
        	{xtype: 'tbtext', text: '控件ID'}
           ,{xtype: 'textfield'}
           
           ,{xtype: 'tbspacer', width: 10}
           ,{xtype: 'tbtext',text: '控件名称'}
           ,{xtype: 'textfield'}
           
           ,{xtype: 'tbspacer', width: 10}
           ,{text: '搜索',iconCls: 'search',id: 'query',
	            handler: function(oButton, oEvent)
	            {
        			var aComponents = me.findByType('textfield');
        			var queryId = aComponents[0].getValue();
        			var queryName = aComponents[1].getValue();
        			
        			oGrid.queryName = queryName;
        			oGrid.queryId = queryId;
        			oGrid.getStore().load(
        				{params:
        					{
        						queryId: queryId,
        						queryName: queryName
        					}
        				});
        		}
        	 }
            ,{xtype: 'tbspacer', width: 10}
        	,{
	            text: '查看',
	            iconCls: 'look',
	            id: 'view',
	            handler: function(oButton, oEvent)
	            {
	            	var aRecord = oGrid.getSelectionModel().getSelections();
	            	if(aRecord.length > 1)
	            	{
	            		Ext.MessageBox.alert('温馨提示', '请选择要查看的一个查询控件资料！');
	            		return ;
	            	}
        			var oRecord = oGrid.getSelectionModel().getSelected();
	    			if(oRecord != undefined)
	    			{
	    				
	    				var report = oRecord.data;
	        			oButton.Window = new Ext.ViewQueryWindow(report);
	        			oButton.Window.show();
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要查看的查询控件资料！');
	    			}
    			 }
        	   }
        	 ,{xtype: 'tbspacer', width: 10}
        	 ,{
	            text: '增加',
	            iconCls: 'add',
	            id: 'add',
	            handler: function(oButton, oEvent)
	            {
        				oButton.Window = new Ext.AddEditQueryWindow();
        				oButton.Window.show();
				}
        	   },{xtype: 'tbspacer', width: 10},
        	{
	            text: '修改',
	            iconCls: 'edit_icon',
	            id: 'edit',
	            handler: function(oButton, oEvent)
	            {
	            	var aRecord = oGrid.getSelectionModel().getSelections();
	            	if(aRecord.length > 1)
	            	{
	            		Ext.MessageBox.alert('温馨提示', '请选择要修改的一个报表资料！');
	            		return ;
	            	}
        			var oRecord = oGrid.getSelectionModel().getSelected();
	    			if(oRecord != undefined)
	    			{
	        				var report = oRecord.data;
	        				oButton.Window = new Ext.AddEditQueryWindow(report);
	        				oButton.Window.show();
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要修改的报表资料！');
	    			}
				}
        	},{xtype: 'tbspacer', width: 10},
        	{
        		text: '删除',
        		iconCls: 'del',
        		id: 'delete',
	            handler: function(oButton, oEvent){
        			var aRecord = oGrid.getSelectionModel().getSelections();
	    			if(aRecord.length > 0)
		    		{
	    				Ext.MessageBox.confirm('温馨提示', '真的要删除选择的报表资料吗？', function(buttonId, text, opt)
	    				{
	    					if(buttonId == 'yes')
	    					{
	    						var reportIdArrays = [];
	    						for(var i = 0;i < aRecord.length; i++)
	    						{
	    							reportIdArrays.push(aRecord[i].data.id);
	    						}

	    						Ext.Ajax.request({   
	    							   url: contextPath+'/module/admin/reportmgr/deleteReportCondition.json',   
	    							method: 'post',   
	    							   params: {reportIdArrays: Ext.encode(reportIdArrays)},   
	    							   success: function(resp, action)
	    							   {   
	    							       var obj = Ext.util.JSON.decode(resp.responseText);   
	    							       if(obj.success)   
	    							       {
	    							       	   Ext.MessageBox.alert('温馨提示', obj.msg,
	    							       	           function(){oGrid.getStore().reload();});
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
	    				Ext.MessageBox.alert('温馨提示', '请选择要删除的报表资料！');
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

Ext.extend(Ext.QueryTopToolbar, Ext.Toolbar);

function setCheckBoxNull(){
	var toolbarComboItems = Ext.getCmp('conditionGroup').items.items;
	var toolbarButtonItems = Ext.getCmp('buttonGroup').items.items;
	var list = [];
    for(var j = 0;j < toolbarComboItems.length;j++){
    	toolbarComboItems[j].sId = '0';
    	list.push(false);
	}
	Ext.getCmp('conditionGroup').setValue(list);
	var list2 = [];
    for(var j = 0;j < toolbarButtonItems.length;j++){
    	toolbarButtonItems[j].sId = '0';
    	list2.push(false);
	}
	Ext.getCmp('buttonGroup').setValue(list2);
	Ext.getCmp('totalGroupId').setValue(2);
	Ext.getCmp('chartUrl').setValue('');
}

//获取reportid相应的数据库数据
function reportSData(win){
	var msgTip = new Ext.LoadMask(Ext.getBody(),{  
        msg:'数据加载中...' 
    });
	msgTip.show();
	Ext.Ajax.request({
       // url: '../sysmgr/getreportGroupData.json',
		url:'',
        method: 'post',
        params:{queryId: win.queryId},
        success: function(response, opts) {
        	msgTip.hide();
        	if(response.getResponseHeader != undefined && response.getResponseHeader("sessionstatus") == "timeout"){
        		Ext.MessageBox.confirm('温馨提示', 'session超时，请重新登录！', function(buttonId, text, opt)
	    				{
	    					if(buttonId == 'yes')
	    					{
	    						window.location.reload();
	    				    }
	    				});
        		return;
        	}
        	var reportCondList = Ext.util.JSON.decode(response.responseText).data;
        	var reportEventList = Ext.util.JSON.decode(response.responseText).param.event;
        	var comboGroup = Ext.getCmp('conditionGroup');
        	var eventGroup = Ext.getCmp('buttonGroup');
        	for(var i=0; i< reportCondList.length; i++){
        		if(reportCondList[i].conditionName == '合计'){
        			Ext.getCmp('totalGroupId').setValue(1);
        			
        		}else{
        			comboGroup.setValue(reportCondList[i].conditionName,true);
        			Ext.getCmp(reportCondList[i].conditionName).sId = reportCondList[i].id;
        		}
        	}
        	for(var i=0; i< reportEventList.length ; i++){
        		eventGroup.setValue(reportEventList[i].eventName, true);
        		Ext.getCmp(reportEventList[i].eventName).sId = reportEventList[i].id;
        		if(reportEventList[i].eventName == '图表分析'){
        			Ext.getCmp('chartUrl').setValue(reportEventList[i].chartUrl);
        		}
        	}
        },
        failure:function(response,action){
        	msgTip.hide();
        	Ext.MessageBox.alert('温馨提示', '系统错误！');
        }
	});
}