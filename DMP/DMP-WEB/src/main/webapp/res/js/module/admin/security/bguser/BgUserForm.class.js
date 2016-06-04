var m_nWidth = 650;
var m_nHeight = 460;

function getBgUserForm() 
{
	return new Ext.FormPanel( {
		//labelAlign : 'top',
		labelWidth: 70,
		labelAlign:'right',
		frame : true,
		bodyStyle : 'padding:5px 5px 0',
		fileUpload : true,
		items : [ {
			layout : 'column',
			items : [ {
				columnWidth : .5,
				layout : 'form',
				items : [ {
					xtype : 'textfield',
					fieldLabel : '<font color="red">*</font>用户帐号',
					name : 'sAccount',
					tabIndex:1,
					allowBlank : false,
					validateOnBlur : true,
					invalidText : '请输入用户帐号',
					blankText : '请输入用户帐号！',
					anchor : '95%'
				}, {
					xtype : 'textfield',
					fieldLabel : '<font color="red">*</font>用户姓名',
					name : 'sName',
					tabIndex:3,
					allowBlank : false,
					validateOnBlur : true,
					invalidText : '请输入用户姓名',
					blankText : '请输入用户姓名！',
					anchor : '95%'
				}, {
					xtype : 'textfield',
					fieldLabel : '用户QQ',
					name : 'sQQ',
					anchor : '95%'
				}, {
					xtype : 'textfield',
					fieldLabel : '用户手机',
					name : 'sMobile',
					anchor : '95%'
				}, {
					xtype : 'textfield',
					fieldLabel : '用户传真',
					name : 'sFax',
					anchor : '95%'
				}]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [ {
					xtype : 'textfield',
					fieldLabel : '<font color="red">*</font>用户密码',
					name : 'sPassword',
					tabIndex:2,
					inputType : 'password',
					allowBlank : false,
					validateOnBlur : true,
					invalidText : '请输入用户密码',
					blankText : '请输入用户密码！',
					anchor : '100%'
				}, {
					xtype : 'radiogroup',
					fieldLabel : '用户性别',
					name: 'nSex',
					height: 22,
					items: [
		                {boxLabel: '男', name: 'nSex', inputValue:1},
		                {boxLabel: '女', name: 'nSex', inputValue:2},
		                {boxLabel: '未知', name: 'nSex', inputValue:3, checked: true}
					],
					anchor : '80%'
				}, {
					xtype : 'textfield',
					fieldLabel : '用户电邮',
					name : 'sEmail',
					anchor : '100%'
				}, {
					xtype : 'textfield',
					fieldLabel : '用户电话',
					name : 'sTel',
					anchor : '100%'
				}, {
					xtype : 'textfield',
					fieldLabel : '用户邮编',
					name : 'sZip',
					anchor : '100%'
				} ]
			} ]
		}, {
			xtype : 'textfield',
			fieldLabel : '用户地址',
			name : 'sAddress',
			anchor : '100%'
		}, {
			xtype : 'bgdepttrigger',
			fieldLabel : '所属部门',
			name : 'sDeptName',
			anchor : '100%'
		}, {
			xtype : 'bgposttrigger',
			fieldLabel : '用户岗位',
			name : 'sBgPost',
			anchor : '100%'
		}, {
			xtype : 'bgroletrigger',
			fieldLabel : '<font color="red">*</font>拥有角色',
			name : 'sBgRole',
			allowBlank : false,
			validateOnBlur : true,
			invalidText : '请选择用户角色',
			blankText : '请选择用户角色！',
			anchor : '100%'
		}, {
			xtype : 'htmleditor',
			fieldLabel : '用户简介',
			name : 'sBrief',
			enableFont: false,
			height : 130,
			anchor : '100%'
		} ]
	});
}

//增加后台用户资料
Ext.AddBgUserWindow = function() 
{
	var me = this;
	Ext.AddBgUserWindow.superclass.constructor.call(this, 
	{
		layout : 'fit',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '增加后台用户资料',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : getBgUserForm(),
		buttons : [ {
			text : '提交',
			iconCls : 'save',
			handler : function() 
			{
				var oForm = me.items.get(0).getForm();
				if (oForm.isValid()) 
				{
					oForm.submit( {
						method : 'post',
						url : 'addBgUser.html',
						params : {
							nBgDeptId : oForm.findField('sDeptName').getRealValue()
							,sBgRoleId: oForm.findField('sBgRole').getRealValue()
							,sBgPostId: oForm.findField('sBgPost').getRealValue()
						},
						waitTitle: '温馨提示',
						waitMsg: '数据正在提交中，请稍候...',
						success : function(form, action) 
						{
							g_oViewPort.items.get(0).getStore().reload();
							oForm.reset();
							me.hide();
						},
						failure : function(form, action) 
						{
							Ext.MessageBox.alert('温馨提示', action.result.msg);
						}
					});
				} else 
				{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
				}
			}
		}, {
			text : '清空',
			iconCls : 'reset',
			handler : function() 
			{
				me.items.get(0).getForm().reset();
			}
		}, {
			text : '关闭',
			iconCls : 'exit',
			handler : function() 
			{
				me.items.get(0).getForm().reset();
				me.hide();
			}
		} ]
	});
};
Ext.extend(Ext.AddBgUserWindow, Ext.Window);

//修改后台用户资料
Ext.EditBgUserWindow = function() 
{
	var me = this;
	Ext.EditBgUserWindow.superclass.constructor.call(this,
	{
		layout : 'fit',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '修改后台用户资料',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : getBgUserForm(),
		buttons : [{
			text : '提交',
			iconCls : 'save',
			handler : function() 
			{
				var oForm = me.items.get(0).getForm();
				if (oForm.isValid()) 
				{
					oForm.submit({
						method : 'post',
						url : 'editBgUser.html',
						params : {
							nBgUserId : me.nBgUserId
							,nBgDeptId: oForm.findField('sDeptName').getRealValue()
							,sBgRoleId: oForm.findField('sBgRole').getRealValue()
							,sBgPostId: oForm.findField('sBgPost').getRealValue()
						},
						waitTitle: '温馨提示',
						waitMsg: '数据正在提交中，请稍候...',
						success : function(form,action) 
						{
							g_oViewPort.items.get(0).getStore().reload();
							me.hide();
						},
						failure : function(form,action) 
						{
							Ext.MessageBox.alert('温馨提示', action.result.msg);
						}
					});
				} else 
				{
					Ext.MessageBox.alert('温馨提示',	'表单验证有误，不准提交数据!');
				}
			}
			}, {
				text : '关闭',
				iconCls : 'exit',
				handler : function() 
				{
					me.items.get(0).getForm().reset();
					me.hide();
				}
			} 
		],
		listeners : {
			show : function(win) 
			{
				var oForm = win.items.get(0).getForm();
				oForm.findField('sAccount').disable();
				getUser(win);
			}
		}
	});
};

Ext.extend(Ext.EditBgUserWindow, Ext.Window);

//查看后台用户资料
Ext.ViewBgUserWindow = function() 
{
	var me = this;
	Ext.ViewBgUserWindow.superclass.constructor.call(this, 
	{
		layout : 'fit',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '查看后台用户资料',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : getBgUserForm(),
		buttons : [ {
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.hide();
			}
		} ],
		listeners : {
			show : function(win) {getUser(win);}
		}
	});
};

function getUser(win)
{
	Ext.Ajax.request( {
		url : 'getBgUser.json',
		method : 'post',
		params : {
			nBgUserId : win.nBgUserId
		},
		success : function(resp, action) 
		{
			var obj = Ext.util.JSON.decode(resp.responseText);
			var oForm = win.items.get(0).getForm();
			oForm.setValues( {
				'sAccount':obj.account,
				'sPassword':'**********',
				'sName':obj.name,
				'nSex':obj.sex,
				'nType':obj.type,
				'sQQ':obj.qq,
				'sEmail':obj.email,
				'sMobile':obj.mobile,
				'sTel':obj.tel,
				'sFax':obj.fax,
				'sZip':obj.zip,
				'sAddress':obj.address,
				'sDeptName':obj.deptName,
				'sBgRole':obj.bgRole,
				'sBgPost':obj.bgPost,
				'sBrief':obj.brief
			});
			
			oForm.findField('sDeptName').setRealValue(obj.bgDeptId);
			oForm.findField('sBgRole').setRealValue(obj.bgRoleId);
			oForm.findField('sBgPost').setRealValue(obj.bgPostId);	
		},
		failure : function(resp, action) 
		{
			Ext.Msg.alert('温馨提示', '系统错误!');
		}
	});
}

Ext.extend(Ext.ViewBgUserWindow, Ext.Window);