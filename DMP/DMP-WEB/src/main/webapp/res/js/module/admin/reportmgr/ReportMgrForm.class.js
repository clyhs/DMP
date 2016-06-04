//显示查看的toolbar
function getReportTopToolbar(report, readOnly) 
{
	if(!report){
	 var report = {
	        reportId:'',
	        reportName: '',
	        updateStaff: '',
	        updateTime: '',
	        reportTable:''};
	}
	if(!readOnly){
		readOnly = false;
		report.updateTime = new Date();
	}
	if(!readOnly && report.reportId =='')
	{
		var readOnlyId = false;
	}
	else
	{
		var readOnlyId = true;
	}
	var nowDate = new Date();
	return new Ext.Toolbar({
		enableOverflow: true,
		items:[
			{
				xtype: 'tbtext',
				text: '报表Id'
			},
			{
				xtype: 'textfield',
				id: 'reportId',
				value: report.reportId,
				readOnly: readOnlyId,
				listeners: {
					//检查reportid是否在数据库中存在
					blur: function(params){
						if(params.readOnly)
						{
							return ;
						}
						if(params.getValue() != "")
						{
							Ext.Ajax.request({
							url : contextPath+'/module/admin/reportmgr/checkreportexit.json',
							method : 'POST',
							params : {
								reportId : params.getValue()
							},
							success : function(response, action) {
							var resp = Ext.util.JSON.decode(response.responseText);//获取从后台传递回来的字符串
							if (resp.success == true) 
							{
								Ext.MessageBox.confirm('温馨提示', resp.msg, 
				                   function(buttonId, text, opt)
				                   	{
				                   	if (buttonId == "yes") 
				                   	{
				                   		params.setValue() == '';
				                   	}
				                    });
			                }
		                    }
	                        })
						}
					}
				}
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: '报表名称'
			},
			{
				xtype: 'textfield',
				id: 'reportName',
				value: report.reportName,
				readOnly: readOnly
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: '操作人员'
			},
			{
				xtype: 'textfield',
				id: 'updateStaff',
				value: report.updateStaff,
				readOnly: readOnly
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: '操作时间'
			},
			{
				xtype: 'datefield' ,
                format: 'Y-m-d',
                maxValue: nowDate,
                value: report.updateTime ,
				id: 'updateTime',
				readOnly: true,
				disabled : true
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: '关联库表'				
			},{
				xtype: 'textfield',
				id: 'reportTable',
				value: report.reportTable,
				readOnly: readOnly
			}
		]
	})
}

//显示查看编辑的sql语句
function getAllSqls(report, readOnly)
{
	if(!report){
	 var report = {
	        sql:'',
	        totalSql: '',
	        chartSql: '',
	        sumSql: ''
	         };
	}
	if(!readOnly){
		readOnly = false;
	}
	
	return new Ext.TabPanel({
		region: 'center',
		height: 200,
		width: 850,
		activeTab: 0,
		items: [
			{
				title: 'sql语句编辑',
				items: [
					{
						xtype: 'textarea',
						height: 180,
						width: 840,
						autoScroll: true,
						id: 'sql',
						value: report.sql,
						readOnly: readOnly
					}
				]
			},
			{
				title: 'totalSql语句编辑',
				items: [
					{
						xtype: 'textarea',
						height: 180,
						width: 840,
						autoScroll: true,
						id: 'totalSql',
						value: report.totalSql,
						readOnly: readOnly
					}
				]

			},
			{
				title: 'chartSql语句编辑',
				items: [
					{
						xtype: 'textarea',
						height: 180,
						width: 840,
						autoScroll: true,
						id: 'chartSql',
						value: report.chartSql,
						readOnly: readOnly
					}
				]
			},{
				title: 'sumSql语句编辑',
				items: [
					{
						xtype: 'textarea',
						height: 180,
						width: 840,
						autoScroll: true,
						id: 'sumSql',
						value: report.sumSql,
						readOnly: readOnly
					}
				]
			}
		]
	})
}

//显示reportattr的grid信息
function getReportAttrGrid(pStore)
{
	return new Ext.grid.GridPanel({
		height: 200,
		width: 800,
		region: 'south',
		store: pStore,
		columns: [
			{header: 'dataIndex' , dataIndex: 'dataIndex'},
			{header: 'sortAble' , dataIndex: 'sortable'},
			{header: 'name' , dataIndex: 'name'},
			{header: 'width' , dataIndex: 'width'},
			{header: 'order' , dataIndex: 'order'},
			{header: 'type' , dataIndex: 'type'}
		]
	});
}

//增加和修改的reportAttr的grid信息
function addEditReportAttrGrid(pStore)
{
	return new Ext.grid.EditorGridPanel({
		height: 200,
		width: 800,
		id: 'editorGridPanel',
		region: 'south',
		clicksToEdit: 1,
		store: pStore,
		columns: [
		    {
				header: 'id',
				dataIndex: 'id',
				width: 50,
				css: 'background-color: #DFDFDF; color: #aFaFaF;'
		    },
			{
				header: 'dataIndex',
				dataIndex: 'dataIndex',
				editor: new Ext.form.TextField({
					allowBlank: false
				})
			},
			{
				header: 'sortAble',
				dataIndex: 'sortable',
				id: 'sortable',
				editor: new Ext.form.ComboBox({
					 mode: 'local',
                     allowBlank: false,
                     editable: false,
					 triggerAction : 'all',
   	                 store: new Ext.data.ArrayStore({
   	                           fields :['sortableName', 'sortableName'],
			                   data: [['false', 'false'], ['true', 'true']]
			                }),
			         valueField: 'sortableName',
		             displayField: 'sortableName'
			})
			},
			{
				header: 'name',
				dataIndex: 'name',
				editor: new Ext.form.TextField({
					allowBlank: false
				})
			},
			{
				header: 'width' ,
				dataIndex: 'width',
				editor: new Ext.form.TextField({
					allowBlank: false
				})
			},
			{
				header: 'order' ,
				dataIndex: 'order',
				editor: new Ext.form.TextField({
					allowBlank: false
				})
			},
			{
				header: 'type' ,
				dataIndex: 'type',
				editor: new Ext.form.TextField({
					allowBlank: false
			     })
			},
			{
				header: 'operation',
				dataIndex: 'operation',
				align: 'center',
				renderer: function(value, metaData, record, rowIndex, colIndex, store)
                {
                	var str = "<a href = 'javascript:operation("+rowIndex+")' style='color: blue; padding: 0px;'>删除</a>";
        		    return str;
                }
			}
		],
		tbar: getEditAddTopToolbar()
	});
}

//查看报表资料
Ext.ViewReportMgrWindow = function(report) 
{
	var me = this;
    var reportAttrStore = getRportAttrList(report);
	
	Ext.ViewReportMgrWindow.superclass.constructor.call(this, 
	{
		layout : 'border',
		width : 850,
		height : 500,
		y: 10,
		plain : true,
		title : '查看报表资料',
		closeAction : 'close',
		iconCls: 'form',
		constrain: true,
		modal : true,
		tbar: getReportTopToolbar(report,readOnly=true),
		items : [getAllSqls(report,readOnly=true), getReportAttrGrid(reportAttrStore)],
		buttons : [ {
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.close();
			}
		} ]
	});
};

Ext.extend(Ext.ViewReportMgrWindow, Ext.Window);

//增加修改报表弹出框
Ext.AddEditReportMgrWindow = function(report) 
{
	var me = this;
	if(report)
	{
		me.title = "修改";
	}
	else
	{
		me.title = "增加";
	}
	var reportAttrStore = {};
	if(report){
		reportAttrStore = getRportAttrList(report);
	}
	Ext.AddEditReportMgrWindow.superclass.constructor.call(this, 
	{
		layout : 'border',
		width : 850,
		height : 500,
		y: 10,
		plain : true,
		title : me.title+"报表资料",
		iconCls: 'form',
		constrain: true,
		closeAction : 'close',
		modal : true,
		tbar: getReportTopToolbar(report),
		items : [getAllSqls(report), addEditReportAttrGrid(reportAttrStore)],
		buttons : [
			{
				text: '提交',
				iconCls: 'save',
				handler: function(oButton, oEvent){
					var editorGridPanel = Ext.getCmp('editorGridPanel');
					var recordCount = editorGridPanel.getStore().getCount();  //获取grid的行数
					//添加reportattr列表
					var reportAttrArray = [];
					for(var i=0; i< recordCount; i++)
					{
						var reportAttr = {};
						var rowRecord = editorGridPanel.getStore().getAt(i);
						reportAttr.dataIndex = rowRecord.data['dataIndex'];
						reportAttr.sortable = rowRecord.data['sortable'];
						reportAttr.name = rowRecord.data['name'];
						reportAttr.width = rowRecord.data['width'];
						reportAttr.order = rowRecord.data['order'];
						reportAttr.type = rowRecord.data['type'];
						reportAttr.reportId = Ext.getCmp('reportId').getValue();
						reportAttr.id = rowRecord.data['id']?rowRecord.data['id']:"0";
						reportAttrArray[i] = reportAttr;
					}
					//添加report列表
					var report = {
						reportId: Ext.getCmp('reportId').getValue(),
						reportName: Ext.getCmp('reportName').getValue(),
						updateStaff: Ext.getCmp('updateStaff').getValue(),
						updateTime: Ext.getCmp('updateTime').getRawValue(),
						reportTable: Ext.getCmp('reportTable').getRawValue(),
						sql: Ext.getCmp('sql').getValue(),
						totalSql: Ext.getCmp('totalSql').getValue(),
						chartSql: Ext.getCmp('chartSql').getValue(),
						sumSql: Ext.getCmp('sumSql').getValue()
					};
					
					if(reportAttrArray.length <= 0){ Ext.Msg.alert('温馨提示', "信息填写完整后请先保存再提交");return;}
					if(report.reportId == "" || report.reportName =="" 
					  ||report.updateStaff ==""||report.sql ==""||report.totalSql=="")
						{ Ext.Msg.alert('温馨提示', "请填写完整报表信息");return;}
					if(me.title == "增加报表资料")
					{
						var url = contextPath+'/module/admin/reportmgr/addReport.json';
					}
					else
					{
						var url = contextPath+'/module/admin/reportmgr/editReport.json';
					}
					var msgTip = new Ext.LoadMask(me.getEl(),{  
				        msg:'数据加载中...' 
				    });
					msgTip.show();
					Ext.Ajax.request({
						method: 'POST',
						url: url,
						params: { 
						      reportAttr:Ext.encode(reportAttrArray),
						      report: Ext.encode(report)
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
    				         		me.close();
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
			}, {
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.close();
			}
		} ]
	});
};

Ext.extend(Ext.AddEditReportMgrWindow, Ext.Window);

//获取reportid对应的reportAttr列表
function getRportAttrList(report){
	var reportAttrStore = new Ext.data.JsonStore({
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/reportmgr/getreportAttrlist.json'
	    }),
	    root: 'data',
	    fields: [
	    	{ name: 'name', type: 'String'},
	    	{ name: 'type', type: 'String'},
	    	{ name: 'width', type: 'String'},
	    	{ name: 'order', type: 'String'},
	    	{ name: 'dataIndex', type: 'String'},
	    	{ name: 'sortable', type: 'String'},
	    	{ name: 'reportId', type: 'String'},
	    	{ name: 'id', type: 'String'}
	    ]
	});
	reportAttrStore.load({params:{reportId: report.reportId}});
	return reportAttrStore ;
}

//修改或添加reportattr的toptoolbar
function getEditAddTopToolbar(){
	return new Ext.Toolbar({
		enableOverflow: true,
		items: [
			{
				xtype: 'tbtext',
				text: 'dataIndex'
			},
			{
				xtype: 'textfield',
				width: 50,
				id: 'dataIndex'
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: 'sortable'
			},
			    getSortableCombox()     
			,{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: 'name'
			},
			{
				xtype: 'textfield',
				id: 'name'
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: 'width'
			},
			{
				xtype: 'textfield',
				width: 40,
				id: 'width'
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: 'order'
			},
			{
				xtype: 'textfield',
				width: 50,
				id: 'order'
			},{xtype: 'tbspacer', width: 10},
			{
				xtype: 'tbtext',
				text: 'type'
			},
			{
				xtype: 'textfield',
				width: 80,
				id: 'type'
			},{xtype: 'tbspacer', width: 10},
			{
				text: '保存',
				iconCls: 'save',
				handler: function(oButton, oEvent){
					var editorGridPanel = Ext.getCmp('editorGridPanel');
					//参数值的获取
					var dataIndex = Ext.getCmp('dataIndex').getValue();
					var sortable = Ext.getCmp('sortableComboBox').getValue();
					var name = Ext.getCmp('name').getValue();
					var width = Ext.getCmp('width').getValue();
					var order = Ext.getCmp('order').getValue();
					var type = Ext.getCmp('type').getValue();
					
					if((dataIndex && sortable && name && width && order && type) =="")
					{
						Ext.Msg.alert('温馨提示', "请填写完整信息");
						return;
					}
					
					//保存输入的值到gridpanel中
					var reportAttrRecord = Ext.data.Record.create([
						{name: 'dataIndex', type: 'string'},
						{name: 'sortable', type: 'string'},
						{name: 'name', type: 'string'},
						{name: 'width', type: 'long'},
						{name: 'order', type: 'long'},
						{name: 'type', type: 'string'}
					]);
					var reportAttr = new reportAttrRecord(
						{
							dataIndex: dataIndex,
							sortable: sortable,
							name: name,
							width: width,
							order: order,
							type: type
						})
					editorGridPanel.getStore().addSorted(reportAttr);
					editorGridPanel.getView().refresh();
					
				}
			}
			]
	})
}

function getSortableCombox(){
	return new Ext.form.ComboBox({
        width: 70,
        id: 'sortableComboBox',
        mode: 'local',
        allowBlank: false,
        editable: false,
        triggerAction : 'all',
   	    store: new Ext.data.ArrayStore({
   	    	id: 'sortable',
		    fields :['sortableName', 'sortableName'],
			data: [['false', 'false'], ['true', 'true']]
			}),
		value:'false',
		valueField: 'sortableName',
		displayField: 'sortableName'
		})
}

function operation(index){
	Ext.MessageBox.confirm('温馨提示', '真的要删除选择的调度任务吗？', function(buttonId, text, opt){
		if(buttonId == 'yes'){
			var editorGridPanel = Ext.getCmp('editorGridPanel');
			editorGridPanel.getStore().removeAt(index);
			editorGridPanel.getView().refresh();
		}
		});
}