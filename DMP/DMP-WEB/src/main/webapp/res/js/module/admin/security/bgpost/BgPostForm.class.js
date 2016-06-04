var m_nWidth = 500;
var m_nHeight = 280;

function getBgPostForm() 
{
	return new Ext.FormPanel( {
		labelAlign : 'top',
		frame : true,
		bodyStyle : 'padding:5px 5px 0',
		fileUpload : true,
		items : [ 
		{
			xtype : 'textfield',
			fieldLabel : '<font color="red">*</font>岗位名称',
			name : 'sName',
			allowBlank : false,
			validateOnBlur : true,
			invalidText : '请输入岗位名称',
			blankText : '请输入岗位名称',
			anchor : '100%'
		}, {
			xtype : 'htmleditor',
			fieldLabel : '备注',
			name : 'sRemark',
			enableFont: false,
			height : 120,
			anchor : '100%'
		} ]
	});
}

//增加岗位资料
Ext.AddBgPostWindow = function() 
{
	var me = this;
	Ext.AddBgPostWindow.superclass.constructor.call(this, {
		layout : 'fit',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '增加岗位资料',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : getBgPostForm(),
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
						url : 'addBgPost.html',
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
Ext.extend(Ext.AddBgPostWindow, Ext.Window);

//修改岗位资料
Ext.EditBgPostWindow = function() 
{
	var me = this;
	Ext.EditBgPostWindow.superclass.constructor.call(this,{
		layout : 'fit',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '修改岗位资料',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : getBgPostForm(),
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
						url : 'editBgPost.html',
						params : {
							nBgPostId : me.nBgPostId
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
				getBgPost(win);
			}
		}
	});
};

Ext.extend(Ext.EditBgPostWindow, Ext.Window);

//查看岗位资料
Ext.ViewBgPostWindow = function() 
{
	var me = this;
	Ext.ViewBgPostWindow.superclass.constructor.call(this, {
		layout : 'fit',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '查看岗位资料',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : getBgPostForm(),
		buttons : [ {
			text : '关闭',
			iconCls : 'exit',
			handler : function() 
			{
				me.hide();
			}
		} ],
		listeners : {
			show : function(win) {getBgPost(win);}
		}
	});
};
Ext.extend(Ext.ViewBgPostWindow, Ext.Window);

function getBgPost(win)
{
	Ext.Ajax.request( {
		url : 'getBgPost.json',
		method : 'post',
		params : {
			nBgPostId : win.nBgPostId
		},
		success : function(resp, action) 
		{
			var obj = Ext.util.JSON.decode(resp.responseText);
			win.items.get(0).getForm().setValues( {
				'sName':obj.name,
				'sRemark':obj.remark
			});
		},
		failure : function(resp, action) 
		{
			Ext.Msg.alert('温馨提示', '系统错误!');
		}
	});
}


