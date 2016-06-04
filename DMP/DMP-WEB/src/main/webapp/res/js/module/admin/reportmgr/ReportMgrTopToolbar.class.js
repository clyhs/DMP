Ext.ReportMgrTopToolbar = function(oGrid)
{
	var me = this;	
	Ext.ReportMgrTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
        	{xtype: 'tbtext', text: '报表ID'}
           ,{xtype: 'textfield'}
           
           ,{xtype: 'tbspacer', width: 10}
           ,{xtype: 'tbtext',text: '报表名称'}
           ,{xtype: 'textfield'}
           
           ,{xtype: 'tbspacer', width: 10}
           ,{text: '搜索',iconCls: 'search',id: 'query', hidden: true,
	            handler: function(oButton, oEvent)
	            {
        			var aComponents = me.findByType('textfield');
        			var reportId = aComponents[0].getValue();
        			var reportName = aComponents[1].getValue();
        			
        			oGrid.reportName = reportName;
        			oGrid.reportId = reportId;
        			oGrid.getStore().load(
        				{params:
        					{
        						reportId: reportId,
        						reportName: reportName
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
	            		Ext.MessageBox.alert('温馨提示', '请选择要查看的一个报表资料！');
	            		return ;
	            	}
        			var oRecord = oGrid.getSelectionModel().getSelected();
	    			if(oRecord != undefined)
	    			{
	    				
	    				var report = oRecord.data;
	        			oButton.Window = new Ext.ViewReportMgrWindow(report);
	        			oButton.Window.show();
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要查看的报表资料！');
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
        				oButton.Window = new Ext.AddEditReportMgrWindow();
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
	        				oButton.Window = new Ext.AddEditReportMgrWindow(report);
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
	    							reportIdArrays.push(aRecord[i].data.reportId);
	    						}

	    						Ext.Ajax.request({   
	    							   url: contextPath+'/module/admin/reportmgr/deleteReport.json',   
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
        	},{xtype: 'tbspacer', width: 10},{
        		text: '界面配置',
        		iconCls: 'config',
        		id: 'selectEdit',
        		handler: function(oButton, oEvent){
	            	var aRecord = oGrid.getSelectionModel().getSelections();
	            	if(aRecord.length > 1)
	            	{
	            		Ext.MessageBox.alert('温馨提示', '请选择要查询配置的一个报表资料！');
	            		return ;
	            	}
        			var oRecord = oGrid.getSelectionModel().getSelected();
        			if(oRecord != undefined)
	    			{
	        				var reportId = oRecord.data.reportId;
	        				var updateStaff = oRecord.data.updateStaff;
	        				if(me.Window){
	        					me.Window.reportId = reportId;
	        					me.Window.updateStaff = updateStaff;
	        					setCheckBoxNull();
	        					me.Window.show();
	        					reportSData(me.Window);
	        					return;
	        				}
	        				me.Window = new Ext.ReportMgrSWin();
	        				me.Window.reportId = reportId;
        					me.Window.updateStaff = updateStaff;
	        				me.Window.show(reportSData(me.Window));
	        				return;
	    			}else{
	    				Ext.MessageBox.alert('温馨提示', '请选择要修改查询配置的报表！');
	    			}
        			return;
	            }
        	},
        	{text: '指标配置',iconCls: 'quota',id: 'quota',
	            handler: function(oButton, oEvent)
	            {
	            	var aRecord = oGrid.getSelectionModel().getSelections();
	            	if(aRecord.length > 1)
	            	{
	            		Ext.MessageBox.alert('温馨提示', '请选择要指标配置的一个报表资料！');
	            		return ;
	            	}
        			var oRecord = oGrid.getSelectionModel().getSelected();
        			if(oRecord != undefined)
	    			{
	        			var reportTable = oRecord.data.reportTable;  //获取到报表的关联table，这里是打开指标配置界面
	        			var reportName = oRecord.data.reportName;
	        			if(reportTable == "" || reportTable == null){
	        				Ext.MessageBox.alert('温馨提示', '请先到报表资料中填写报表关联table！');
	        				return ;
	        			}
	        			if(me.QuotaWindow){
	        				var reportQuotaGridPanel = Ext.getCmp('reportQuotaGridPanel');
	        				reportQuotaGridPanel.getStore().load({params: {
	        					reportTable: reportTable
	                        }});
	        				me.QuotaWindow.show();
	        				return;
	        			}
	        			me.QuotaWindow = new Ext.ReportQuotaWin(reportName,reportTable);
	        			me.QuotaWindow.show();
	        			return;
	    			}else{
	    				Ext.MessageBox.alert('温馨提示', '请选择要修改查询配置的报表！');
	    			}
        			return;
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

Ext.extend(Ext.ReportMgrTopToolbar, Ext.Toolbar);

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
	//Ext.getCmp('chartUrl').setValue('');
}

//获取reportid相应的数据库数据
function reportSData(win){
	var msgTip = new Ext.LoadMask(Ext.getBody(),{  
        msg:'数据加载中...' 
    });
	msgTip.show();
	Ext.Ajax.request({
        url: contextPath+'/module/admin/reportmgr/getreportGroupData.json',
        method: 'post',
        params:{reportId: win.reportId},
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