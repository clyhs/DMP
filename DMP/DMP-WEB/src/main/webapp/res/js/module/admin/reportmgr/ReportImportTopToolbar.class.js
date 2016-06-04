Ext.ReportImportTopToolbar = function(oGrid) {
	var tableNameStore = new Ext.data.JsonStore({
	    url:'getTableNameCombo.json',
	    fields:['TABLENAME'],
	    root:'data'
	});
	tableNameStore.load();

	var tableNameCombo = new Ext.form.ComboBox({
		id : 'tableName',
		typeAhead : true,
		triggerAction : 'all',
		forceSelection : true,
		mode : 'local',
		width : 180,
		store : tableNameStore,
		valueField : 'TABLENAME',
		displayField : 'TABLENAME',
		allowBlank:false,
		value:'TR_P_VISITS',
		editable:false
	});
	

	var formPanel =  new Ext.FormPanel({
		frame : true,
		baseCls:'',
		bodyStyle : 'padding:5px 5px 0',
		fileUpload : true,
		labelWidth:50,
		items:[
		 {
		     xtype:'fileuploadfield',  
		     fieldLabel :'Excel文件',
			 name : 'filePath',
			 id : 'filePath',
			 allowBlank : true,
			 validateOnBlur : true,
			 invalidText : '选择excel文件',
			 blankText : '选择excel文件！',
			 width:300
		 }
		]
     });

	Ext.ReportImportTopToolbar.superclass.constructor.call(this,{
	  enableOverflow : false,
	  items:[ 
		{
		   xtype : 'tbspacer',
		   width : 10
		},{
			xtype:'tbtext',
			text:'模版名称'
		},{
			xtype: 'tbspacer', width: 10
		},tableNameCombo,
		{
		    xtype : 'tbspacer',
		    width : 10
		},{
			text:'模版下载',
			iconCls:'down',
			id: 'down',
	        hidden: true,
			handler:function(oButton, oEvent){
				var fileName = Ext.getCmp('tableName').getRawValue();
			    var oForm =formPanel.getForm();
				if (oForm.isValid()) 
				{
				    oForm.submit( {
					method : 'post',
					url : 'getExcelModel.html',
					params : {
						fileName:fileName
					},
					success : function(form, action) 
					{
						
					},
					failure : function(form, action) 
					{
						Ext.Msg.alert('温馨提示',Ext.decode(action.response.responseText).msg);
					}
				});
				} else 
				{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
				}
			}
		},{
			xtype: 'tbspacer', width: 40
		},formPanel
		 ,{
		    xtype : 'tbspacer',
		    width : 10
		},{
			text:'预览',
			iconCls:'search',
			id: 'preview',
	        hidden: true,
			handler : function(oButton, oEvent){
			  var filePath = Ext.getCmp('filePath').getRawValue();
			  if(filePath==''){
					Ext.Msg.alert('温馨提示', '请选择要预览的Excel文件!');
					return;
			  }
			 var pos = filePath.lastIndexOf(".");
			 var lastName = filePath.substring(pos,filePath.length);
			 if(lastName.toLowerCase()!=".xlsx"&&lastName.toLowerCase()!=".xls"){
				 Ext.Msg.alert('温馨提示','文件类型必须为xls,xlsx!');
				 return;
			 }
			 if(filePath==''){
				Ext.Msg.alert('温馨提示', '请选择要预览的Excel文件!');
			 }else{
				var oForm =formPanel.getForm();
				if (oForm.isValid()) 
				{
					oForm.submit( {
						method : 'POST',
						url : 'getExcelToList.html',
						params : {
						},
						success : function(form, action) 
						{
							var sColumn = Ext.decode(action.response.responseText).msg;
							oGrid.getStore().load({
								params:{loadData:'loadData'},
								callback:function(records, options, success){
									var columnModle = sColumn;
									var selModel = oGrid.selModel;
									var obj = Ext.decode(columnModle);
									if (typeof (obj) === 'object') {
										var columns = [];
										columns.push(selModel);
										Ext.each(obj, function(column) {
											columns.push(column);
										});
										oGrid.getColumnModel().setConfig(columns);
									
									}

								}
							});	
						},
						failure : function(form, action) 
						{
							 Ext.Msg.alert('温馨提示','请求失败!');
						}
					});
				}else{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
				}
			}
		   }
		},{
		    xtype : 'tbspacer',
		    width : 10
		},{
			text:'Excel导入',
			iconCls:'excel2',
			id: 'import',
	        hidden: true,
			handler : function(oButton, oEvent){
			  var tableName = Ext.getCmp('tableName').getRawValue();
			  var filePath = Ext.getCmp('filePath').getRawValue();
			  var reg = /[^\\\/]*[\\\/]+/g;
			  var  str = filePath.replace(reg,''); 
			  var path = str.substring(0,str.lastIndexOf("."));
			  
			  if(filePath==''){
					Ext.Msg.alert('温馨提示', '请选择要导入的Excel文件');
					return;
			  }
			  if(tableName!=path){
				  Ext.MessageBox.alert('温馨提示', '模版名称和Excel文件不一致');
				  return;  
			  }
			  if(tableName==''){
					Ext.Msg.alert('温馨提示', '请选择模版名称');
					return;
			  }
			  var storeRecord = oGrid.getStore().getCount();
				if(storeRecord == 0 ){
					Ext.MessageBox.alert('温馨提示', '预览界面上没有要导入的数据!');
					return;
			  }
			  var oForm =formPanel.getForm();
			  if (oForm.isValid()) 
			  {
				  oForm.submit({
						method : 'post',
						url : 'importExcel.html',
						params : {
							 tableName:tableName	
						},
						waitTitle: '温馨提示',
						waitMsg: '数据正在提交中，请稍候...',
						success : function(form, action) 
						{
							 oGrid.getColumnModel().setConfig([]);
							 oGrid.getStore().removeAll();
							 Ext.Msg.alert('温馨提示', action.result.msg); 
						},
						failure : function(form, action) 
						{
							 Ext.Msg.alert('温馨提示',action.result.msg);
						}
				 });
			  }else{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
			  }
		   }
		},{
		    xtype : 'tbspacer',
		    width : 10
		},{
			text:'删除',
			iconCls:'del',
			id: 'delete',
	        hidden: true,
			handler:function(oButton, oEvent){
				var aRecord =  oGrid.getSelectionModel().getSelections();
			    var storeRecord = oGrid.getStore().getCount();
			    if(storeRecord == 0 ){
			    	Ext.MessageBox.alert('温馨提示', '界面上没有数据不需要删除!');
			    	return;
			    }
				if(aRecord.length > 0)
	    		{
					Ext.MessageBox.confirm('温馨提示', '真的要删除选择的数据吗？', function(buttonId, text, opt){
						if(buttonId == 'yes')
    					{   
							var recordId = [];
    						for(var i = 0;i<aRecord.length;i++)
    						{
    							var id = aRecord[i].data.F0;
    							recordId.push(id);
    						}
							Ext.Ajax.request({
							  url: 'delExcel.json',   
  							   method: 'post',   
  							   params: {ids:recordId.join(',')},   
  							   success: function(resp, action)
  							   {   
  							       var obj = Ext.util.JSON.decode(resp.responseText);   
  							       if(obj.success){    
  							    	   oGrid.getStore().reload();
  							       }else{   
  							            Ext.Msg.alert('温馨提示', obj.msg);   
  							       }     
  							    },   
  							    failure:function(resp, action)
  							    {   
  							        Ext.Msg.alert('温馨提示','请求失败!');   
  							    } 
							});
    					}
					});
	    		}else{
	    			Ext.MessageBox.alert('温馨提示', '请选择要删除的数据!');
	    		}
			}
		},{
			xtype : 'tbspacer',
			width : 10
		},'->',{
			text : '帮助',
			iconCls : 'help'
		}]
	});
};

Ext.extend(Ext.ReportImportTopToolbar, Ext.Toolbar);
