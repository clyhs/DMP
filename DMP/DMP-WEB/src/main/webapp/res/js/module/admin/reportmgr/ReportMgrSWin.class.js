//本js文件为报表配置的查询条件配置的相关代码
Ext.ReportMgrSWin = function() 
{
	var me = this;
	Ext.ReportMgrSWin.superclass.constructor.call(this, 
	{
		layout : 'border',
		width : 950,
		height : 500,
		y: 10,
		plain : true,
		id: 'ReportMgrSWin',
		title : '报表查询界面配置',
		closeAction : 'hide',
		//maximizable: true,
		iconCls: 'form',
		constrain: true,
		modal : true,
		/*tbar: new Ext.Toolbar({
			items:[{
				text: '测试toolbar效果',
				handler: function(oButton, oEvent){
					alert('还没开始呢！');
				}
			}]
		}),*/
		items : [getToolbarConfigForm(me)],
		buttons : [ {
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var conditionGroup = Ext.getCmp('conditionGroup');
				var buttonGroup = Ext.getCmp('buttonGroup');
				var neetTotalField = Ext.getCmp('totalGroupId');
				var conditionValue = conditionGroup.getValue();
				var buttonValue = buttonGroup.getValue();
				var totalValue = neetTotalField.getValue();
				var conditionArray = [];
				for(var i=0 ;i< conditionValue.length; i++){
					var conditionObject = {};
					conditionObject.reportId = me.reportId;
					conditionObject.updateStaffId = me.updateStaff;
					conditionObject.conditionName = conditionValue[i].boxLabel;
					conditionObject.componentId = conditionValue[i].componentId;
					conditionObject.componentType = conditionValue[i].componentType;
					conditionObject.format = conditionValue[i].format;
					conditionObject.layout = String(conditionValue[i].layout);
					conditionObject.comboxField = conditionValue[i].comboxField;
					conditionObject.sId = conditionValue[i].sId?String(conditionValue[i].sId):'0';
					conditionValue[i].sId = '0';
					conditionArray.push(conditionObject);
				}
				if(totalValue == 1){
					var conditionObject = {};
					conditionObject.reportId = me.reportId;
					conditionObject.updateStaffId = me.updateStaff;
					conditionObject.conditionName = '合计';
					conditionObject.componentType = 'total';
					conditionObject.layout = '0';
					conditionObject.sId = '0';
					conditionArray.push(conditionObject);
				}
				var buttonArray = [];
				for(var i= 0 ;i< buttonValue.length; i++){
					var buttonObject = {};
					buttonObject.reportId = me.reportId;
					buttonObject.updateStaffId = me.updateStaff;
					buttonObject.sId = buttonValue[i].sId? String(buttonValue[i].sId): '0';
					buttonValue[i].sId = '0';
					buttonObject.eventName = buttonValue[i].boxLabel;
					buttonObject.event = buttonValue[i].event;
					if(buttonValue[i].boxLabel == '图表分析' && Ext.getCmp('chartUrl').getValue() == ''){
						Ext.Msg.alert('温馨提示', '请填写图表分析路径');
						return;
					}else if(buttonValue[i].boxLabel == '图表分析' && Ext.getCmp('chartUrl').getValue() != ''){
						buttonObject.chartUrl = Ext.getCmp('chartUrl').getValue();
					}
					buttonObject.layout = String(buttonValue[i].layout);
					buttonArray.push(buttonObject);
				}
				
				if(conditionArray.length ==0 ){
					 Ext.Msg.alert('温馨提示', '请勾选报表对应的查询条件控件');
					 return;
				}
				if(buttonArray == 0){
					 Ext.Msg.alert('温馨提示', '请勾选报表对应的查询按钮控件');
					 return;
				}
				//异步提交toolbar的各个数据
				var msgTip = new Ext.LoadMask(me.getEl(),{  
			        msg:'数据加载中...' 
			    });
				msgTip.show();
				Ext.Ajax.request({
					method: 'POST',
					url: contextPath+'/module/admin/reportmgr/addComboAndEvent.json',
					params: { 
					      reportCondition:Ext.encode(conditionArray),
					      reportButton: Ext.encode(buttonArray)
					      },
					success:function(response,action){
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
                         var resp=Ext.util.JSON.decode(response.responseText);//获取从后台传递回来的字符串
                         if(resp .success==true )
                         {
                         	Ext.MessageBox.alert('温馨提示',resp.msg, 
                         	function(){
				         		me.hide();
				         	});
                          }
                          else{
                        	  Ext.Msg.alert('温馨提示',resp.msg);
                          }
                    },
                failure:function(response,action){
                	msgTip.hide();
                }
				});
				
			}
		},{
			text: '关闭',
			iconCls: 'exit',
			handler: function(){
				me.hide();
			}
		} ]
	});
};

Ext.extend(Ext.ReportMgrSWin, Ext.Window);

function getToolbarConfigForm(me)
{
	if(me.firstCome != undefined && !me.firstCome){
		return;
	}
	Ext.Ajax.request({
        url: contextPath+'/module/admin/reportmgr/getComboGroup.json',
        method: 'post',
        success: function(response, opts) {
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
        	var conditionGroup = Ext.util.JSON.decode(response.responseText).data;
        	var buttonGroup = Ext.util.JSON.decode(response.responseText).param.event;
        	//所有组件的添加显示
        	var items=[];
            var conditionGroupLen = conditionGroup.length;
            for(var i=0;i< conditionGroupLen;i++)
            {
            	var condition=conditionGroup[i];
                var checkBox = {
                		boxLabel: condition.conditionName,
                		name: condition.conditionName,
                		id: condition.conditionName,
                		componentId: condition.componentId, 
                		componentType:condition.componentType,
                		format: condition.format,
                		layout: condition.layout,
                		comboxField: condition.comboxField,
                		checked:false
                		};
                items.push(checkBox);
              };
              var comboGroupCheckBox = new Ext.form.CheckboxGroup({
                       xtype: 'checkboxgroup',  
                       id:'conditionGroup',
                       name :'conditionGroup', 
                       columns: 7,
                       anchor:"95%",
                       msgTarget:"side",
                       width:400
                      });
              comboGroupCheckBox.items = items;
              var toolbarCombo = Ext.getCmp('toolbarCombo');
              toolbarCombo.add(comboGroupCheckBox);
              toolbarCombo.doLayout();
              //事件按钮的添加显示
              var items2 = [];
              var buttonGroupLen = buttonGroup.length;
              for(var i = 0; i< buttonGroupLen; i++){
            	  var button = buttonGroup[i];
            	  var checkBox = {
            			  boxLabel: button.buttonName,
            			  name: button.buttonName,
            			  id: button.buttonName,
            			  event: button.button,
            			  layout: button.layout
            	  };
            	  items2.push(checkBox);
              }
              var eventGroupCheckBox = new Ext.form.CheckboxGroup({
                  xtype: 'checkboxgroup',  
                  id:'buttonGroup',
                  name :'buttonGroup',
                  style: {
                      paddingLeft: '75px'
                  },
                  columnWidth : .5,
                  msgTarget:"side",
                  listeners: {
                	  change: function( checkboxGroup, checked ){
                		 for(var i= 0; i< checked.length; i++){
                			 if(checked[i].boxLabel == '图表分析'){
                				 Ext.getCmp('chartUrl').setReadOnly(false);
                			 }else{
                				 Ext.getCmp('chartUrl').setReadOnly(true);
                			 }
                		 }
                	  }
                  }
                 });
              eventGroupCheckBox.items = items2;
              var toolbarButton = Ext.getCmp('toolbarButton');
              toolbarButton.add(eventGroupCheckBox);
              /*
              toolbarButton.add(new Ext.form.TextField({
            	  name: 'chartUrl',
            	  columnWidth : .3,
            	  id: 'chartUrl',
            	  emptyText: '图表分析路径',
            	  readOnly: true,
            	  fieldLabel: 'chartUrl' 
              })) ;*/
              toolbarButton.doLayout();
        },
        failure:function(response,action){
        	Ext.MessageBox.alert('温馨提示', '系统错误！');
        }
	});
	me.firstCome = true;
    return new Ext.FormPanel({
	    region: 'center',
		labelWidth: 70,
		id: 'toolbarConfigForm',
		labelAlign:'right',
		frame : true,
		autoScroll: true,
		items: [{
			xtype:'fieldset',
            title: '<font color="red">*</font>查询条件列表',
            id: 'toolbarCombo',
            width: 900,
            autoHeight:true,
            defaults: {
               anchor: '100%' 
            },
            items: []
		},{
			xtype:'fieldset',
            title: '<font color="red">*</font>查询按钮列表',
            id: 'toolbarButton',
            width: 900,
            autoHeight:true,
            layout : 'column',
            defaults: {
               anchor: '100%' 
            },
            items: []
		},{
			xtype:'fieldset',
            title: '报表是否需要合计',
            id: 'neetTotalField',
            width: 900,
            autoHeight:true,
            defaults: {
               anchor: '100%' 
            },
            items: [{
            	 xtype: 'radiogroup',
            	 id: 'totalGroupId',
            	 anchor: '40%',
                 items: [
                         {boxLabel: '需要', name: 'neetTotal', inputValue: 1},
                         {boxLabel: '不需要', name: 'neetTotal', inputValue: 2, checked: true}
                         ]
            }]
		}]
		});
    
}


//下面的是指标界面报表指标配置grid
function addEditReportquotaGrid(pStore)
{
	var SM = new Ext.grid.CheckboxSelectionModel();
	return new Ext.grid.EditorGridPanel({
		//height: 200,
		width: 800,
		id: 'reportQuotaGridPanel',
		region: 'center',
		clicksToEdit: 1,
		store: pStore,
		sm:SM,
		columns: [
           SM,	          
		   {
			   header: 'sId',
 			   dataIndex: 'sId',
			   width: 60//,
			   //css: 'background-color: #DFDFDF; color: #aFaFaF;'
		    },
			{
				header: 'reportTable',
				dataIndex: 'reportTable',
				width: 150
			},			
			{
				header: 'columnName',
				dataIndex: 'columnName',
				width: 130
			},
			{
				header: 'minValue',
				dataIndex: 'minValue',
				editor: new Ext.form.TextField({
					allowBlank: false
				}),
				renderer: function(value, metaData, record, rowIndex, colIndex, store){
					//根据不同的指标模式值来修改样式
					var configType = store.reader.jsonData.data[rowIndex].configType;
					if(configType == '<=' || configType == 'none')
					{
						metaData.style = 'background-color: #DFDFDF; color: #aFaFaF;';
					}else{
						metaData.style = '';
					}
                    return value;
                }
			},
			//reporttable字段是否配置关键指标
			{
				header: 'configType',
				dataIndex: 'configType',
				//id: 'configType',
				editor: new Ext.form.ComboBox({
					mode: 'local',
					allowBlank: false,
					editable: true,
					triggerAction : 'all',
					store: new Ext.data.ArrayStore({
						fields :['configType', 'configType'],
						data: [['<=', '<='],['between', 'between'] ,['>=', '>='],['none','none']]
					}),
					value: 'between',
					valueField: 'configType',
					displayField: 'configType',
					listeners: {
		                	select: function(combo, record, index)
		                	{
		                		var reportQuotaGridPanel = Ext.getCmp('reportQuotaGridPanel');
		                		var selections  = reportQuotaGridPanel.getSelectionModel().getSelections();
		                		var store = reportQuotaGridPanel.getStore();
		                		var selectData = selections[0];
		                		var rowIndex = store.indexOf(selectData);
		                		if(index == 0){
		                			store.getAt(rowIndex).set("configType",'<=');
		                			store.data.items[rowIndex].json.configType = '<=';
		                			store.getAt(rowIndex).set("minValue",'0');
		                			store.data.items[rowIndex].json.minValue = '0';
		                		}else if(index == 1){
		                			store.getAt(rowIndex).set("configType",'between');
		                			store.data.items[rowIndex].json.configType = 'between';
		                		}else if(index == 2){
		                			store.getAt(rowIndex).set("configType",'>=');
		                			store.data.items[rowIndex].json.configType = '>=';
		                			store.getAt(rowIndex).set("maxValue",'0');
		                			store.data.items[rowIndex].json.maxValue = '0';
		                		}else{
		                			store.getAt(rowIndex).set("configType",'none');
		                			store.data.items[rowIndex].json.configType = 'none';
		                			store.getAt(rowIndex).set("minValue",'0');
		                			store.data.items[rowIndex].json.minValue = '0';
		                			store.getAt(rowIndex).set("maxValue",'0');
		                			store.data.items[rowIndex].json.maxValue = '0';
		                		}
		                		reportQuotaGridPanel.reconfigure( store, reportQuotaGridPanel.colModel );
		                		return ;
		                	}
		                }
				})
			},
			{
				header: 'maxValue' ,
				dataIndex: 'maxValue',
				editor: new Ext.form.TextField({
					allowBlank: false
				}),
				renderer: function(value, metaData, record, rowIndex, colIndex, store){
					//根据不同的指标模式值来修改样式
					var configType = store.reader.jsonData.data[rowIndex].configType;
					if(configType == '>=' || configType == 'none')
					{
						metaData.style = 'background-color: #DFDFDF; color: #aFaFaF;';
					}else{
						metaData.style = '';
					}
                    return value;
                }				
			}
		]
	});
}
//指标配置界面的window
Ext.ReportQuotaWin = function(reportName, reportTable) 
{
	var me = this;
	
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/reportmgr/getreportQuotalist.json'
	    }),
	    root: 'data',
	    fields: [ 
	          {name: 'columnName', type: 'String'},
	          {name: 'reportTable', type: 'String'},
	          {name: 'sId', type: 'String'},
	          {name: 'configType', type: 'String'},
	          {name: 'maxValue', type: 'String'},
	          {name: 'minValue' , type: 'String'}
	    ]
	});
	oStore.load({
		        params:{reportTable: reportTable}
	});
	
	Ext.ReportQuotaWin.superclass.constructor.call(this, 
	{
		layout : 'border',
		width : 950,
		height : 400,
		y: 100,
		plain : true,
		id: 'ReportQuotaWin',
		title : '报表指标配置界面',
		closeAction : 'hide',
		iconCls: 'form',
		constrain: true,
		modal : true,
		items : [addEditReportquotaGrid(oStore)],
		buttons : [ {
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var reportQuotaGridPanel = Ext.getCmp('reportQuotaGridPanel');
				var recordCount = reportQuotaGridPanel.getStore().getCount();  //获取grid的行数
				var reportQuotaArray = [];
				for(var i=0; i< recordCount; i++)
				{
					var reportQuota = {};
					var rowRecord = reportQuotaGridPanel.getStore().getAt(i);
					reportQuota.sId = rowRecord.data['sId'];
					reportQuota.reportTable = rowRecord.data['reportTable'];
					reportQuota.columnName = rowRecord.data['columnName'];
					reportQuota.minValue = rowRecord.data['minValue'];
					reportQuota.maxValue = rowRecord.data['maxValue'];
					reportQuota.configType = rowRecord.data['configType'];
					if(reportQuota.configType == 'between' && 
							parseFloat(reportQuota.maxValue) <= parseFloat(reportQuota.minValue) 
							&& reportQuota.maxValue != reportQuota.minValue != 0 ){
						Ext.MessageBox.alert('温馨提示', '指标配置范围值错误！');
						return ;
					}
					reportQuotaArray[i] = reportQuota;
				}
				//提交指标配置后台处理
				var msgTip = new Ext.LoadMask(me.getEl(),{  
			        msg:'数据加载中...' 
			    });
				msgTip.show();
				Ext.Ajax.request({
					method: 'POST',
					url: contextPath+'/module/admin/reportmgr/addOrUpdateReportQuota.json',
					params: { 
						reportQuotaArray:Ext.encode(reportQuotaArray),
					      },
					success:function(response,action){
						msgTip.hide();
                        var resp=Ext.util.JSON.decode(response.responseText);//获取从后台传递回来的字符串
                        if(resp .success==true )
                        {
                        	Ext.MessageBox.alert('温馨提示',resp.msg, 
                         	function(){
                         	    var gridPanel = Ext.getCmp('grid');
    				            gridPanel.getStore().reload();
				         		me.hide();
				         	});
                         }
                         else{
                        	 Ext.Msg.alert('温馨提示',resp.msg);
                         }
                    },
                failure:function(response,action){
                	msgTip.hide();
                }
				});
			}
		},{
			text: '关闭',
			iconCls: 'exit',
			handler: function(){
				me.hide();
			}
		} ]
	});
	
};

Ext.extend(Ext.ReportQuotaWin, Ext.Window);