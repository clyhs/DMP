function getScheduleForm()
{
    return new parent.Ext.FormPanel({
	    region: 'center',
		labelWidth: 70,
		labelAlign:'right',
		id: 'schedulerPanel',
		frame : true,
		autoScroll: true,
		bodyStyle : 'padding: 15px;',
		fileUpload : true,
		items : [
			{
				xtype: 'textfield',
				width: 250,
				name: 'jobName',
				id: 'jobName',
				allowBlank : false,
				fieldLabel: '任务名称',
				value: '根据选择文件自动生成',
				//readOnly: true,
				disabled: true,
				anchor : '95%',
				style: {
				  width: '95%',
                  marginBottom: '5px'
                }
			},{
				xtype: 'textarea',
				name: 'description',
				id: 'description',
				fieldLabel: '<font color="red">*</font>任务描述',
				allowBlank : false,
				anchor : '95%',
				width: 250,
				heigth: 50,
				style: {
				  width: '95%',
                  marginBottom: '5px'
                }
			},{
				xtype: 'repositoryCombo',
				fieldLabel: '<font color="red">*</font>资源库名',
				allowBlank : false,
				name: 'repName',
				id: 'repName',
				width: 250,
				anchor : '95%',
				style: {
				  width: '95%',
                  marginBottom: '5px'
                }
			},{
				xtype: 'textfield',
				fieldLabel: '库用户名',
				name: 'userName',
				id: 'userName',
				width: 250,
				anchor : '95%',
				style: {
				  width: '95%',
                  marginBottom: '5px'
                }
			},{
				xtype: 'textfield',
				fieldLabel: '库的密码',
				name: 'passWord',
				id: 'passWord',
				width: 250,
				anchor : '95%',
				style: {
				  width: '95%',
                  marginBottom: '5px'
                }
			},{
				xtype: 'scedulertriggerFile',
				fieldLabel: '<font color="red">*</font>任务文件',
				emptyText: '请先选择资源库',
				anchor : '95%',
				allowBlank : false,
				id: 'actionPath',
				name: 'actionPath',
				width: 250,
				style: {
				  width: '95%',
                  marginBottom: '5px'
                }
			},{
				xtype: 'datetimefield' ,
                format: 'Y-m-d H:i:s',
                id: 'startTime',
                name: 'startTime',
                allowBlank : false,
                anchor : '95%',
                fieldLabel: '<font color="red">*</font>开始时间',
                width: 250,
                value: new Date() ,
				style: {
				  width: '95%',
                  marginBottom: '5px'
                }
			},{
		    	xtype: 'combo',
		    	anchor : '95%',
		    	width: 250,
		    	mode: 'local',
		    	id: 'cycle',
		    	name: 'notuse',
                allowBlank: false,
                triggerAction : 'all',
                fieldLabel: '<font color="red">*</font>任务周期',
		    	store: new Ext.data.ArrayStore({
		    		fields:['executeTimesId','executeTimesName'],
		    		data: [[1,'执行一次'],[2,'秒'],[3,'分'],[4,'时'],[5,'天'],[6,'周'],[7,'月'],[8,'年']]
		    	}),
		    	value: '执行一次',
		    	valueField: 'executeTimesId',
		    	displayField: 'executeTimesName',
		    	style: {
				  width: '95%',
                  marginBottom: '5px'
                },
                listeners: {
                	select: function(combo, record, index  )
                	{
                		selectModel(combo, record, index );
                		var isFirst = parent.Ext.getCmp('schedulerwin').firstCome;
                		if(index == 7 && isFirst == 1){
                		parent.Ext.getCmp('schedulerwin').firstCome = -1;
                		new parent.Ext.form.DateField({
                            format: 'm-d',
                            id: 'yeardatepanel',
                            name: 'notuse',
                            renderTo: 'yeardate',
                            width: 90,
                            value: new Date() 
				       })}
                	}
                }
		    },{
		    	xtype:'fieldset',
                title: '任务周期',
                id: 'fieldSet',
                hidden: true,
                //disabled: true,
                //collapsible: true,
                //collapsed: true,
                width: 330,
                autoHeight:true,
                defaults: {
                   anchor: '100%' 
                },
                items :[{
/*秒的周期选择模式 */  	layout : 'column',
		            	id: 'model_2',
		            	hidden: true,
		            	items: [{
                             columnWidth:.2,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 2px',
                             items:[{
                                     xtype: 'tbtext',
                                     text: '<font color="red">*</font>周期模式:'                                  }]
		            	},{
                             columnWidth:.8,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 26px;',
                             items:[{
                             	html: '每&nbsp<input type="text" id="secendcyclenum" style="width: 30px;" maxlength="9">&nbsp秒'
                             }]
		            	}]
		            
                    },{
/*分的周期选择模式*/     	layout : 'column',
		            	id: 'model_3',
		            	hidden: true,
		            	items: [{
                             columnWidth:.2,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 2px',
                             items:[{
                            	 xtype: 'tbtext',
                                 text: '<font color="red">*</font>周期模式:' 
                                  }]
		            	},{
                             columnWidth:.8,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 26px',
                             items:[{
                             	html: '每&nbsp<input type="text" id="minitecyclenum" style="width: 30px;" maxlength="9">&nbsp分'
                             }]
		            	}]
                    },{
/*小时的周期选择模式 */   	layout : 'column',
		            	id: 'model_4',
		            	hidden: true,
		            	items: [{
                             columnWidth:.2,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 2px',
                             items:[{
                            	  xtype: 'tbtext',
                                  text: '<font color="red">*</font>周期模式:' 
                                  }]
		            	},{
                             columnWidth:.8,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 26px',
                             items:[{
                             	html: '每&nbsp<input type="text" id="hourcyclenum" style="width: 30px;" maxlength="9">&nbsp时'
                             }]
		            	}]
                    },{
/*天的周期选择模式*/		layout : 'column',
		            	id: 'model_5',
		            	hidden: true,
		            	items: [{
                             columnWidth:.2,
                             baseCls:'x-plain',
                             bodyStyle:'padding:20px 0 0 2px',
                             items:[{
                            	  xtype: 'tbtext',
                                  text: '<font color="red">*</font>周期模式:' 
                                  }]
		            	},{
                             columnWidth:.6,
                             baseCls:'x-plain',
                             bodyStyle:'padding:20px 0 0 25px',
                             items:[{
                                     html: '<input type="radio" id="daytyperadio1" checked ="true" name = "daytype" value="0" onchange="changeDayType(this.value);">每<input type="text" id="daycyclenum" style="width: 30px;" maxlength="4">天' + 
									   '&nbsp&nbsp<input type="radio" id="daytyperadio2" name = "daytype" value="1" onchange="changeDayType(this.value);">每个工作日'
                                  }]
		            	}]
                    },{
/*周的周期选择模式*/     	xtype: 'checkboxgroup',                            //start周的周期选择模式
                        fieldLabel: '<font color="red">*</font>周期模式',
                        labelStyle: 'padding:15px 0 0 5px; text-align: left',
                        id: 'model_6',
                        name: 'notuse',
                        hidden: true,
                        style: {
                            paddingLeft: '10px'
                        },
                        columns: 3,
                        items: [
                        	{boxLabel: '星期一', value: 2, id : 'week2'},
                            {boxLabel: '星期二', value: 3, id : 'week3'},
                            {boxLabel: '星期三', value: 4, id : 'week4'},
                            {boxLabel: '星期四', value: 5, id : 'week5'},
                            {boxLabel: '星期五', value: 6, id : 'week6'},
                            {boxLabel: '星期六', value: 7, id : 'week7'},
                            {boxLabel: '星期天', value: 1, id : 'week1'}
		                    ],
		                    listeners: {
		                    change: function( thischeckbox, checked )
		                    	{
		                    		var cyclenum = parent.Ext.getCmp('schedulerwin').cyclenum;
		                    		for(var i= 0; i< checked.length; i++)
		                    		{
		                    			if(i == 0){
  		                    			    cyclenum = checked[i].value;
		                    			}else{
		                    			   cyclenum = cyclenum +","+checked[i].value;
		                    			}
		                    		}
		                    		parent.Ext.getCmp('schedulerwin').cyclenum = cyclenum ;
		                    	}}                                                      //end
		            },{
/*月的周期选择模式*/     	layout : 'column',
		            	id: 'model_7',
		            	hidden: true,
		            	items: [{
                             columnWidth:.2,
                             baseCls:'x-plain',
                             bodyStyle:'padding:20px 0 0 2px',
                             items:[{
                            	  xtype: 'tbtext',
                                  text: '<font color="red">*</font>周期模式:' 
                                  }]
		            	},{
                             columnWidth:.8,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 25px',
                             items:[{
                             	html: '<div style= "padding: 1px"><input type="radio" id="monthtyperadio1" name ="monthtype" value="0" checked="checked" onchange="changeMonthType(this.value);">&nbsp每月的<select id="monthcyclenum" name = "monthcyclenum">' + 
									   '<option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option>' +
									   '<option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option>' +
									   '<option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option><option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option><option value="29">29</option><option value="30">30</option>' +
									   '<option value="31">31</option><option value="L">L</option></select>日(L表示最后一天)</div><br>' + 
									   '<input type="radio" id="monthtyperadio2" name ="monthtype" value="1" onchange="changeMonthType(this.value);">&nbsp每月的<select id="monthweeknum" name = "monthweeknum" disabled= "disabled" style="width: 60px; font-size: 12px;"><option value="1">第1个</option><option value="2">第2个</option><option value="3">第3个</option><option value="4">第4个</option><option value="L">最后一个</option></select>' +
									   '<select id="monthdaynum" name= "monthdaynum" disabled= "disabled" style="width: 60px; font-size: 12px;"><option value="2">星期一</option><option value="3">星期二</option><option value="4">星期三</option><option value="5">星期四</option><option value="6">星期五</option><option value="7">星期六</option><option value="1">星期日</option></select>'
                             }]
		            	}]
		            },{
/*年的周期选择模式 */   	layout : 'column',
		            	id: 'model_8',
		            	hidden: true,
		            	items: [{
                             columnWidth:.2,
                             baseCls:'x-plain',
                             bodyStyle:'padding:25px 0 0 2px',
                             items:[{
                            	  xtype: 'tbtext',
                                  text: '<font color="red">*</font>周期模式:' 
                                  }]
		            	},{
                             columnWidth:.8,
                             baseCls:'x-plain',
                             bodyStyle:'padding:20px 0 0 25px',
                             items:[{
                                     html: '<div style= "padding: 1px"><input type="radio" id="yeartyperadio1" name ="yeartype" value="0" checked="checked" onchange="changeYearType(this.value);">&nbsp每年<span style= "float: right;margin: -1px 80px 0 0;" id="yeardate" ></span></div><br>' + 
									   '<div><input type="radio" id="yeartyperadio2" name ="yeartype"  value="1" onchange="changeYearType(this.value);">&nbsp每年<select id="yearmonthnum" name ="yearmonthnum" style="width: 30px;" disabled= "disabled"><option value="1">1</option><option value="2">2</option><option value="3">3</option>' +
									   '<option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option><option value="11">11</option><option value="12">12</option>' +
									   '</select>月<select id="yearweeknum" name="yearweeknum" style="width: 65px;font-size: 12px;" disabled= "disabled"><option value="1">第1个</option><option value="2">第2个</option><option value="3">第3个</option><option value="4">第4个</option><option value="L">最后一个</option></select>' +
									   '<select id="yeardaynum" name="yeardaynum" style="width: 60px; font-size: 12px;" disabled= "disabled"><option value="2">星期一</option><option value="3">星期二</option><option value="4">星期三</option><option value="5">星期四</option><option value="6">星期五</option><option value="7">星期六</option><option value="1">星期日</option></select></div>'
                                  }]
		            	}]
		            },{
/*截止日期的界面*/    	layout : 'column',
		            	hidden: true,
		            	id: 'overTime',
		            	items: [{
                             columnWidth:.2,
                             baseCls:'x-plain',
                             bodyStyle:'padding:10px 0 0 5px',
                             items:[{
                                     xtype: 'tbtext',
                                     text: '&nbsp截止日期:'
                                  }]
		            	   },{
                             columnWidth:.3,
                             baseCls:'x-plain',
                             bodyStyle:'padding:5px 0 0 5px; margin: 0 10px 20px 20px',
                             items:[{
                                     xtype: 'radio',
                                     boxLabel: '永不结束',
                                     id: 'enddateRadio1',
                                     name: 'haveenddate',
                                     checked : true,
                                     inputValue: '0',
                                     labelAlign:'right',
                                     listeners: {
                                    	 check: changeEndDateRadio
                              		}
                                   }]
                          },{
                          	columnWidth:.1,
                             baseCls:'x-plain',
                             bodyStyle:'padding:5px 0 0 13px ;margin: 4px 0 0 0 ',
                             items: [
                             	{
                             		xtype: 'radio',
                             		labelAlign:'right',
                             		id: 'enddateRadio2',
                             		name: 'haveenddate',
                             		inputValue: '1',
                             		boxLabel: '',
                             		listeners: {
                             			check: changeEndDateRadio
                             		}
                             	}]
                          },{
                          	 columnWidth:.4,
                             baseCls:'x-plain',
                             bodyStyle:'padding:5px 0 0 0',
                             items: [
                             	{
                             		xtype : 'datefield',
									format : 'Y-m-d',
									width : 100,
									value : new Date(),
									//disabled: true,
									readOnly: true,
                             		id: 'enddate',
                             		name: 'enddate'
                             	}]
                          
                          }]
		            }]	
		    }
		]
	});
}

//新增调度的窗口
parent.Ext.AddEditSchedulerWindow = function(edit) 
{
	var me = this;
	me.cyclenum = '';
	me.firstCome =1;
	me.edit = edit;
	parent.Ext.AddEditSchedulerWindow.superclass.constructor.call(this, 
	{
		layout : 'border',
		width : 400,
		height : 500,
		id: 'schedulerwin',
		constrain: true,
		bodyStyle: 'padding:10px 50px 0;',
		style: {
            top: '5px'
        },
		plain : true,
		title : '新增调度',
		resizable: false,
		closeAction : 'close',
		iconCls: 'form',
		//buttonAlign:"center",
		modal : true,
		items : getScheduleForm(),
		buttons : [
		    {
		    	text: '提交',
				iconCls: 'save',
				handler: function(){
					var cycle = parent.Ext.getCmp('cycle').getValue();
					var params = passparams(cycle,me);
					if(!params){
						return;
					}
					var formPanel = me.items.get(0).getForm();
					var url;
					if(me.edit){
						 url = contextPath+'/module/admin/quartz/editScheduler.html';
					}else{
						 url = contextPath+'/module/admin/quartz/addScheduler.html';
					}
					if (!formPanel.isValid()) 
					{
						parent.Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
						return;
					}
					formPanel.submit( {
					 	method: 'post',
	    		        url: url,
	    		        params : params,
	    		        waitTitle: '温馨提示',
						waitMsg: '数据正在提交中，请稍候...',
						success : function(form, action) 
	    		        {
							Ext.getCmp('scheduler').getStore().load();
	    		        	me.close();
	    		        },
	    				 failure : function(form, action) 
	    				{
	    					parent.Ext.MessageBox.alert('温馨提示', '系统出错');
	    				}
					 });
				}
		    },
			{
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.close();
			}
		} ],
		listeners: {
			beforeclose: function(){
				parent.Ext.getCmp('transFileWin').close();
			}
		}
	});
};
parent.Ext.extend(parent.Ext.AddEditSchedulerWindow, parent.Ext.Window);

//资源库的控件
var RepositoryStore = new Ext.data.JsonStore({
    url : contextPath+'/module/admin/quartz/getRespositorylist.json',
    method: 'post',
    fields : [ 'repositoryId', 'repositoryName' ],
    root : 'arr'
});
RepositoryStore.load();

var RepositoryCombo =  parent.Ext.extend(parent.Ext.form.ComboBox,{
    typeAhead: true,
    triggerAction: 'all',
    forceSelection: true,
    mode: 'local',
    width:80,
    emptyText:'请选择',
    store: RepositoryStore,
    valueField: 'repositoryId',
    displayField: 'repositoryName',
    constructor : function(){
        RepositoryCombo.superclass.constructor.apply(this, arguments);
    },
    onDestroy : function() {
        RepositoryCombo.superclass.onDestroy.apply(this, arguments);
    }
});
parent.Ext.reg('repositoryCombo', RepositoryCombo);

//触发控件
var ScedulerTriggerButton = parent.Ext.extend(parent.Ext.form.TriggerField, {
	triggerClass : 'x-form-search-trigger',
	editable : false,
	realValue : '',
	initComponent : function() {
		this.win = new parent.Ext.transFileWindow(this),
		ScedulerTriggerButton.superclass.initComponent.call(this);
	},
	onTriggerClick : function() {
		var repName = parent.Ext.getCmp('repName').getRawValue();
		if(repName == "")
		{
			parent.Ext.Msg.alert('温馨提示','请选择对应资源库');
			return ;
		}
		var tree = parent.Ext.getCmp('transFileWin').items.items[1];
		tree.getLoader().dataUrl= contextPath+'/module/admin/quartz/getRepTreeJSON.json?repName='+repName,
		tree.getRootNode().reload();
		this.win.show(this.trigger);
	},
	setRealValue : function(v) {
		this.realValue = v;
	},
	getRealValue : function() {
		return this.realValue;
	},
    constructor : function(){
    	ScedulerTriggerButton.superclass.constructor.apply(this, arguments);
    },
    onDestroy : function() {
    	ScedulerTriggerButton.superclass.onDestroy.apply(this, arguments);
    }
});
parent.Ext.reg('scedulertriggerFile', ScedulerTriggerButton);

//转换文件的获取窗口
parent.Ext.transFileWindow = function(trigger) 
{
	var me = this;
	parent.Ext.transFileWindow.superclass.constructor.call(this, 
	{
		layout : 'border',
		id: 'transFileWin',
		width : 400,
		height : 500,
		constrain: true,
		resizable: true,
		plain : true,
		title : '选择转换文件',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : [new parent.Ext.uploadForm(this) ,new parent.Ext.tranTree({trigger: trigger, win: me})],
		buttons : [{
			text: '上传',
			iconCls: 'fileUp',
			handler: function(){
				var fileForm = me.items.items[0].getForm();
        		var file = parent.Ext.getCmp('file').getValue();
        		var index1 = file.lastIndexOf(".");
        		var index2 = file.length;
        		var fileType=file.substring(index1+1,index2);//获取文件后缀名
        		if(fileType != 'ktr' && fileType != 'kjb'){
        			parent.Ext.Msg.alert('温馨提示','请选择上传正确的格式文件');
        			return ; 
        		}
        		if(fileForm.isValid()){
        			fileForm.submit({
        				url: contextPath+'/module/admin/quartz/uploadfile.html',
        	            waitMsg: '文件上传中，请稍等',
        	            params: {filePath: fileForm.findField('filePath').getValue() },
        	            success: function(form, action){
        	            	var backData = Ext.util.JSON.decode(action.response.responseText);
        	            	parent.Ext.Msg.alert('温馨提示',backData.msg, function(){
        	            		parent.Ext.getCmp('tranTree').getRootNode().reload();
        	            	});
        	            },
                        failure: function(form,action) {
                           	parent.Ext.Msg.alert('温馨提示','系统出错');
                        }
        	        });
        	    }else{
        	    	parent.Ext.Msg.alert('温馨提示','表单验证有误，请认真核实数据');
        	    }
        	}
		},{
			text: '清空',
			iconCls: 'reset',
			handler: function(){
				me.items.items[0].getForm().reset();
			}
		},{
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.hide();
			}
		} ]
	});
};
parent.Ext.extend(parent.Ext.transFileWindow, parent.Ext.Window);

//获取后台资源库中的目录的文件列表
parent.Ext.tranTree = function(config)
{
	Ext.apply(this, config);
	var me = this;
	parent.Ext.tranTree.superclass.constructor.call(this, {
		id: 'tranTree',
	    useArrows: true,
	    region: 'center',
        autoScroll: true,
        animate: true,
        enableDD: true,
        containerScroll: true,
        rootVisible: false,
        border: false,
        dataUrl: contextPath+'/module/admin/quartz/getRepTreeJSON.json',
        root: {
           id: 'root',
           expanded: true
        },
        listeners: {
        	dblclick: getThetransformFile,
        	contextmenu: function(oNode, oEvent) {
        		oEvent.stopEvent();
				oNode.select();
			    
				me.tranTreeMenu = new parent.Ext.tranTreeMenu(oNode, oEvent);
        		me.tranTreeMenu.showAt(oEvent.getXY());
        	}
        }
        });
};
parent.Ext.extend(parent.Ext.tranTree, parent.Ext.tree.TreePanel);

//菜单栏的显示
parent.Ext.tranTreeMenu = function(oNode, oEvent)
{
	var me = this;
	parent.Ext.tranTreeMenu.superclass.constructor.call(this, {
		items: 
		[{
			text: '添加文件',
			iconCls: 'upload-icon',
			hidden: oNode.leaf? true: false,
			handler: function(oItem, oEvent){
				var path = [];
				while(oNode.text != "/")
				{
					path.push(oNode.text);
					oNode = oNode.parentNode;
				}
				parent.Ext.getCmp('filePath').setValue(path.reverse().join("/"));
			}
		},{
			text: '删除文件',
	 		iconCls: 'del',
            handler: function(oItem, oEvent){
            	var path = [];
            	var isFile = false;
            	var fileType ="";
            	if(oNode.leaf){
            		isFile = true;
            		fileType = oNode.text.split("[")[1].split("]")[0];
            	}
				while(oNode.text != "/")
				{
					path.push(oNode.text);
					oNode = oNode.parentNode;
				}
				var filePath = path.reverse().join("/");
				Ext.Ajax.request({
					url : contextPath+'/module/admin/quartz/deletefile.json',
					method : 'POST',
					params : {
						filePath : isFile? filePath.split("[k")[0]+ "."+ fileType: filePath,
						isFile: isFile
					},
					success : function(response, action) {
					    var resp = Ext.util.JSON.decode(response.responseText);//获取从后台传递回来的字符串
					    if (resp.success == true) 
					    {
					    	parent.Ext.Msg.alert('温馨提示',resp.msg, function(){
					    		parent.Ext.getCmp('tranTree').getRootNode().reload();
					    	});
					    }
			        },
			        failure: function(form,action) {
			           	parent.Ext.Msg.alert('温馨提示','系统出错');
			        }
			    });
			}
		}
		]
	});
};
parent.Ext.extend(parent.Ext.tranTreeMenu, parent.Ext.menu.Menu);

parent.Ext.uploadForm = function(tranWindow)
{
	var me = this;
	var tranWindow = tranWindow;
	parent.Ext.uploadForm.superclass.constructor.call(this, {
        region: 'south',
        fileUpload: true,
        frame: true,
        title: '手动上传转换文件',
        autoHeight: true,
        labelWidth: 70,
        buttonAlign: 'center',
        defaults: {
            anchor: '90%',
            allowBlank: false,
            msgTarget: 'side'
        },
        items: [{
            xtype: 'textfield',
            name: 'filePath',
            id: 'filePath',
            allowBlank: true,
            emptyText: '从树节点中获取路径',
            disabled: true,
            fieldLabel: '放置目录'
        },{
            xtype: 'fileuploadfield',
            id: 'file',
            name: 'file',
            emptyText: '选择 ktr、kjb 文件',
            fieldLabel: '浏览文件',
            buttonText: '',
            buttonCfg: {
                iconCls: 'upload-icon'
            }
        }
        ]
	});
};
parent.Ext.extend(parent.Ext.uploadForm, parent.Ext.FormPanel);

//获取转换文件的路径
function getThetransformFile(oNode, oEvent){
	if(!oNode.leaf){
		return;
	}
	var filePath = oNode.text.split("[")[0];
	var fileName = filePath;
	parent.Ext.getCmp('jobName').setValue(filePath);
	var fileType = oNode.text.split("[")[1].split("]")[0];
	while(oNode.parentNode && oNode.parentNode.text != '/' )
	{
		oNode = oNode.parentNode;
		filePath = oNode.text +"/" + filePath;
	}
	filePath =filePath +"."+fileType;
	var fileField = parent.Ext.getCmp('actionPath');
	fileField.setValue(filePath);
	var transFileWindow = this.win;
	transFileWindow.hide();
	Ext.Ajax.request({
		url : contextPath+'/module/admin/quartz/checkJobName.json',
		method : 'POST',
		params : {
			jobName : fileName
		},
		success : function(response, action) {
		    var resp = Ext.util.JSON.decode(response.responseText);//获取从后台传递回来的字符串
		    if (resp.success == true) 
		    {
		    	parent.Ext.Msg.alert('温馨提示',resp.msg);
		    }
        },
        failure: function(form,action) {
           	parent.Ext.Msg.alert('温馨提示','系统出错');
        }
    });
    return ;
}

//不同模式的获取
var beforeIndex = 0;
function selectModel(combo, record, index )
{   
    index++;
	if(index != 1)
	{
		
		if(beforeIndex != index && beforeIndex != 0){
			 parent.Ext.getCmp("model_"+ beforeIndex).hide();
		}
		beforeIndex = index;
		/*parent.Ext.getCmp("model_"+ index).show();
		parent.Ext.getCmp('fieldSet').setDisabled(false);
		parent.Ext.getCmp('fieldSet').expand(true);
		parent.Ext.getCmp('overTime').show();*/
		parent.Ext.getCmp('fieldSet').show();
		parent.Ext.getCmp("model_"+ index).show();
		parent.Ext.getCmp('fieldSet').expand(true);
		parent.Ext.getCmp('overTime').show();
	    return ;
	}else{
		/*parent.Ext.getCmp("model_"+ beforeIndex).hide();
		parent.Ext.getCmp('fieldSet').collapse(false);
		parent.Ext.getCmp('fieldSet').setDisabled(true);*/
		parent.Ext.getCmp("model_"+ beforeIndex).hide();
		parent.Ext.getCmp('fieldSet').collapse(false);
		parent.Ext.getCmp('fieldSet').hide();
	}
};

function changeEndDateRadio(thisRadio, checked)
{
	if(thisRadio.getId() == 'enddateRadio1' && checked)
	{
		parent.Ext.getCmp('enddate').setReadOnly(true);
	}else if(thisRadio.getId() == 'enddateRadio2' && checked)
	{
		parent.Ext.getCmp('enddate').setReadOnly(false);
	};
}

function passparams(cycle,schewin)
{
	var haveEndDate = parent.Ext.getCmp('enddateRadio1').getValue();
	var monthTypeRadio = parent.document.getElementById('monthtyperadio1');
	var yeartyperadio = parent.document.getElementById('yeartyperadio1');
	var jobName = parent.Ext.getCmp('jobName').getValue();
	cycle = cycle == '执行一次' ? 1:cycle;
	if(!haveEndDate)
	{
		var startTime = parent.Ext.getCmp('startTime').getRawValue();
		var endTime = parent.Ext.getCmp('enddate').getRawValue();
		if(endTime < startTime){
			parent.Ext.Msg.alert('温馨提示','开始时间不能在结束时间之间');
			return false;
		}
	}
	if(cycle == 1 || cycle == 8 && !yeartyperadio.checked){
		resultparams = {
				cycle: cycle,
				jobName: jobName
		};
		return resultparams;
	}
	if(cycle == 7 && !monthTypeRadio.checked){
		resultparams = {
				cycle: cycle,
				jobName: jobName
		};
		return resultparams;
	}else if(cycle == 7 && monthTypeRadio.checked){
		cyclenum = parent.document.getElementById('monthcyclenum').value;
	}
	if(cycle == 2){ cyclenum = parent.document.getElementById('secendcyclenum').value;};
	if(cycle == 3){ cyclenum = parent.document.getElementById('minitecyclenum').value;};
	if(cycle == 4){ cyclenum = parent.document.getElementById('hourcyclenum').value;};
	if(cycle == 5){ cyclenum = parent.document.getElementById('daycyclenum').value;};
	if(cycle == 6){ cyclenum = schewin.cyclenum;};
	if(cycle == 8 && yeartyperadio.checked){
		cyclenum = parent.Ext.getCmp('yeardatepanel').getRawValue();
	}
	resultparams = {
		cycle: cycle,
		cyclenum: cyclenum,
		jobName: jobName
	};
	return resultparams;
	
}