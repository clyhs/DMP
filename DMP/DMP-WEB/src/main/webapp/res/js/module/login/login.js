Ext.BLANK_IMAGE_URL = '${request.getContextPath()}/res/images/common/default/s.gif';
var container = this;
//入口函数
Ext.onReady(function(){

	if(container.frameElement != null && container.name != ''){
		container.parent.location.reload();
		return;
	}
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider({expires: new Date(new Date().getTime()+(1000*60*60*24*30))}));
	
	var oWin = new Ext.Window({
		layout:'fit',
		width:400,
		height:220,
		closable:false,
		collapsible:true,
		iconCls:'lock',
		title:'数据分析管理平台',
		plain: true,
		items:[new Ext.FormPanel({
			id:'LoginForm',
	        labelWidth: 75,
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        items: [{
	        	title: '请输入用户帐号和密码',
	            xtype:'fieldset',
	            collapsible: true,
	            autoHeight:true,
	            defaults: {width: 250},
	            defaultType: 'textfield',
	            items :[{
	                    fieldLabel: '登录帐号',
	                    id:'sAccount',
	                    name: 'sAccount',
	                    enableKeyEvents: true,
	                    listeners:{
	            			keyup : function(f, e)
	            			{
	            				focusSubmit(e);
	            			}
	                    }
	                },{
	                    fieldLabel: '登录密码',
	                    inputType:'password',
	                    id: 'sPassword',
	                    name: 'sPassword',
	                    enableKeyEvents: true,
	                    listeners:{
	            			keyup : function(f, e)
	            			{
	            				focusSubmit(e);
	            			}
	                    }
	                },{
	                    fieldLabel: '记住我',
	                    xtype:'checkbox',
	                    checked: true,
	                    id: 'bRemMe',
	                    name: 'bRemMe'
	                }
	            ]
	        }]
  	})],
		buttons: [{
			iconCls:'lock',
		    text:'登录',
		    handler: function(){
				var sAccount = Ext.getCmp('sAccount').getValue();
				var sPassword = Ext.getCmp('sPassword').getValue();
				
				if(Ext.isEmpty(sAccount))
				{
					Ext.MessageBox.alert('温馨提示', '请输入用户名！', function(){Ext.getCmp('sAccount').focus(100);});
					return;
				}
				if(Ext.isEmpty(sPassword))
				{
					Ext.MessageBox.alert('温馨提示', '请输入用户密码！', function(){Ext.getCmp('sPassword').focus(100);});
					return;
				}
				submit(sAccount, sPassword);
		    }
		},{
		    text: '重置',
		    iconCls:'reset',
		    handler: function(){
				Ext.getCmp('LoginForm').getForm().reset();
				if(Ext.state.Manager.get('cookie_sAccount') != undefined) 
				{
					Ext.getCmp('sAccount').setValue(Ext.state.Manager.get('cookie_sAccount'));
				}
				Ext.getCmp('sAccount').focus(100);
		    }
		}],
		listeners:{
			show : function()
			{
				if(Ext.state.Manager.get('cookie_sAccount') != undefined) 
				{
					Ext.getCmp('sAccount').setValue(Ext.state.Manager.get('cookie_sAccount'));
				}
			}
		}
	});
	
	oWin.show();
});

function focusSubmit(e)
{
	var sAccount = Ext.getCmp('sAccount').getValue();
	var sPassword = Ext.getCmp('sPassword').getValue();
	
	if(e.getKey() == 13)
	{
		if(Ext.isEmpty(sAccount)) 
		{
			Ext.getCmp('sAccount').focus(100);
			return;
		}
		if(Ext.isEmpty(sPassword)) 
		{
			Ext.getCmp('sPassword').focus(100);
			return;
		}
		submit(sAccount, sPassword);
	}
}

function submit(sAccount, sPassword)
{
	Ext.Ajax.request({   
		   url: 'islogin.json',   
		   method: 'post',   
		   params: {sAccount: sAccount, sPassword: sPassword},   
		   success: function(resp, action){   
				var obj = Ext.util.JSON.decode(resp.responseText); 
				switch(obj.success)
				{
					case true:
						if(Ext.getCmp('bRemMe').getValue())
						{
							Ext.state.Manager.set('cookie_sAccount', sAccount);
							Ext.state.Manager.set('cookie_sPassword', sPassword);
						}
						var url = contextPath+'/module/admin/main/index.html';
						
						window.location.href = contextPath+'/module/admin/main/index.html';
					break;
					default:
						Ext.getCmp('sPassword').setValue('');
						Ext.Msg.alert('温馨提示', obj.msg, function(){Ext.getCmp('sPassword').focus(100);});						
					break;
				}    
		   },   
		   failure:function(resp, action){   
		        Ext.Msg.alert('温馨提示','系统错误');   
		   }   
	}); 	
}