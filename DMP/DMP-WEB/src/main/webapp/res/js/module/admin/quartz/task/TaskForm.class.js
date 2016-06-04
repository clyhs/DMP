var width = 350;
var height = 200;
var nowDate = new Date();
var starTate = new Date(nowDate.getFullYear(), nowDate.getMonth()-1);
//查看编辑面板
function getTaskForm(task) {
	if (!task) {
		var task = {
			taskCode : '',
			name : '',
			beginTime : '',
			endTime : ''
		};
	}
	if (!readOnly) {
		readOnly = false;
	}
	if(!hidden){
		hidden = false;
	}
	return new Ext.FormPanel( {
		region: 'center',
		width : width,
		height : height,
		labelWidth: 70,
		labelAlign:'left',
		frame : true,
		items : [{
			width: 250,
			xtype : 'textfield',
			fieldLabel : '<font color="red">*</font>任务编码',
			name : 'taskCode',
			value:task.taskCode,
			readOnly: readOnly,
			hidden:hidden,
			allowBlank : false,
			maxLength:40,
			maxLengthText:'字符数不能超过40个'
		}, {
			width: 250,
			xtype : 'textfield',
			fieldLabel : '<font color="red">*</font>任务名称',
			name : 'name',
			value:task.name,
			readOnly: readOnly,
			allowBlank : false,
			maxLength:40,
			maxLengthText:'字符数不能超过40个'
		}, {
			width: 250,
			xtype: 'datetimefield' ,
			fieldLabel : '<font color="red">*</font>开始时间',
			format: 'Y-m-d H:i:s',
            value:task.beginTime,
            name:'TaskBeginTime',
			readOnly: readOnly
		},{
			width: 250,
			xtype: 'datetimefield' ,
			fieldLabel : '<font color="red">*</font>结束时间',
			format: 'Y-m-d H:i:s',
            value:task.endTime,
            name:'TaskEndTime',
			readOnly: readOnly
		}]
	});
}


//添加面板
function getTaskAdd() {
	return new Ext.FormPanel( {
		region: 'center',
		width : width,
		height : height,
		labelWidth: 70,
		labelAlign:'left',
		frame : true,
		items : [{
			width: 250,
			xtype : 'textfield',
			fieldLabel : '<font color="red">*</font>任务编码',
			name : 'taskCode',
			allowBlank : false,
			validateOnBlur : true,
			maxLength:40,
			maxLengthText:'字符数不能超过40个'
		}, {
			width: 250,
			xtype : 'textfield',
			fieldLabel : '<font color="red">*</font>任务名称',
			name : 'name',
			allowBlank : false,
			validateOnBlur : true,
			maxLength:40,
			maxLengthText:'字符数不能超过40个'
		}, {
			width: 250,
			xtype: 'datetimefield' ,
			fieldLabel : '<font color="red">*</font>开始时间',
			format: 'Y-m-d H:i:s',
            name:'TaskBeginTime',
            validateOnBlur : true,
            value:starTate
		},{
			width: 250,
			xtype: 'datetimefield' ,
			fieldLabel : '<font color="red">*</font>结束时间',
			format: 'Y-m-d H:i:s',
            name:'TaskEndTime',
            validateOnBlur : true,
            value:nowDate	
		}]
	});
}


//查看
Ext.ViewTaskWindow = function(task) 
{
	var me = this;
	Ext.ViewTaskWindow.superclass.constructor.call(this, 
	{
		width : width,
		height : height,
		plain : true,
		title : '查看任务信息',
		closeAction : 'hide',
		constrain : true,
		modal : true,
		iconCls: 'form',
		items : [getTaskForm(task, readOnly = true,hidden = false)],
		buttons : [ {
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.close();
			}
		} ]
	 });
};

Ext.extend(Ext.ViewTaskWindow, Ext.Window);

//增加
Ext.AddTaskWindow = function() 
{
	var me = this;
	Ext.AddTaskWindow.superclass.constructor.call(this, 
    {
		width : width,
		height : height,
		plain : true,
		title : '增加任务信息',
		closeAction : 'hide',
		modal : true,
		iconCls: 'form',
		items : getTaskAdd(),
		buttons : [{
			text : '提交',
			iconCls : 'save',
			handler : function(oButton, oEvent) {
				var oForm = me.items.get(0).getForm();
				var startTime  = oForm.getValues().TaskBeginTime;
				var endTime  = oForm.getValues().TaskEndTime;
				if (startTime == undefined || startTime == '') {
					Ext.Msg.alert('温馨提示', '请输入开始时间');
					return;
				} else if (endTime == undefined || endTime == '') {
					Ext.Msg.alert('温馨提示', '请输入结束时间');
					return;
				} else if (startTime > endTime) {
					Ext.Msg.alert('温馨提示', '开始时间不能大于结束时间');
					return;
				}
				if (oForm.isValid()) {
					oForm.submit({
						method : 'post',
						url : 'addTask.html',
						params : {},
						waitTitle : '温馨提示',
						waitMsg : '数据正在提交中，请稍候...',
						success : function(form, action) {
							g_oViewPort.items.get(0).getStore().reload();
							oForm.reset();
							me.hide();
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('温馨提示', action.result.msg);
						}
					});
				}else 
				{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
				}
			}
		},{
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.close();
			}
		}]
	});
};

Ext.extend(Ext.AddTaskWindow, Ext.Window);


// 修改
Ext.EditTaskWindow = function(task) 
{
	var me = this;
	Ext.EditTaskWindow.superclass.constructor.call(this, 
    {
		width : width,
		height : height,
		plain : true,
		title : '修改任务信息',
		closeAction : 'hide',
		iconCls: 'form',
		modal : true,
		items : [ getTaskForm(task, readOnly = false,hidden = true) ],
		buttons : [{
			text : '提交',
			iconCls : 'save',
			handler : function(oButton, oEvent) {
				var oForm = me.items.get(0).getForm();
				var startTime  = oForm.getValues().TaskBeginTime;
				var endTime  = oForm.getValues().TaskEndTime;
				if (startTime == undefined || startTime == '') {
					Ext.Msg.alert('温馨提示', '请输入开始时间');
					return;
				} else if (endTime == undefined || endTime == '') {
					Ext.Msg.alert('温馨提示', '请输入结束时间');
					return;
				} else if (startTime > endTime) {
					Ext.Msg.alert('温馨提示', '开始时间不能大于结束时间');
					return;
				}
				if (oForm.isValid()) {
					oForm.submit({
						method : 'post',
						url : 'editTask.html',
						params : {},
						waitTitle : '温馨提示',
						waitMsg : '数据正在提交中，请稍候...',
						success : function(form, action) {
							g_oViewPort.items.get(0).getStore().reload();
							oForm.reset();
							me.hide();
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('温馨提示', action.result.msg);
						}
					});
				}else{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
				}
			}
		},{
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.close();
			}
		}]
	  });
};

Ext.extend(Ext.EditTaskWindow, Ext.Window);
