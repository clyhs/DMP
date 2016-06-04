var m_nRoleWidth = 400;
var m_nRoleHeight = 500;

//增加角色
Ext.AddBgRoleWindow = function()
{
	var me = this;
	Ext.AddBgRoleWindow.superclass.constructor.call(this, {
		layout:'fit',
        width:m_nRoleWidth,
        height:m_nRoleHeight,
        plain: true,
        title: '增加角色',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items: new Ext.NavTree(), 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function()
            {
        		var oValue = me.items.get(0).getValue();
        		if(Ext.isEmpty(oValue.sName))
        		{
        			Ext.Msg.alert('温馨提示','请输入角色名称');  
        		}else
        		{
        			if(oValue.aNode.length == 0)
    				{
    					Ext.Msg.alert('温馨提示','请选择权限结点');  
    				}else
					{
						Ext.Ajax.request({   
						   url: 'addBgRole.html',   
						   method: 'post',   
						   params: {sName: oValue.sName, sBgTreeIdList: oValue.aNode.join(',')},   
						   success: function(resp, action)
						   {
						        var obj = Ext.util.JSON.decode(resp.responseText); 
						        if(obj.success)
						        {
									var oParentNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
								 	oParentNode.appendChild(new Ext.tree.TreeNode({
								 		text: oValue.sName,
								 		id: 'RoleId_'+obj.param.nBgRoleId,
								 		leaf: true
								 	}));
								 	oParentNode.expand();
								 	me.hide(oParentNode.getUI().getEl());
						        }else
						        {
						        	Ext.Msg.alert('温馨提示', obj.msg); 
						        }
						    },
						    failure:function(resp, action)
						    {   
						        Ext.Msg.alert('温馨提示','系统错误!');   
						    }   
						}); 
					}
        		}
        	}
        },{
            text: '关闭',
            iconCls:'exit',
            handler: function(){
    			me.hide();
            }
        }],
        listeners:{
			show : function(win)
			{
				var oTree = me.items.get(0);
				oTree.clearSelect(oTree.getRootNode());
			}
		}
	});
};
Ext.extend(Ext.AddBgRoleWindow, Ext.Window);

//修改角色
Ext.EditBgRoleWindow = function()
{
	var me = this;
	Ext.EditBgRoleWindow.superclass.constructor.call(this, {
		layout:'fit',
        width:m_nRoleWidth,
        height:m_nRoleHeight,
        plain: true,
        title: '修改角色',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items: new Ext.NavTree(), 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function()
            {
				var oValue = me.items.get(0).getValue();
        		if(Ext.isEmpty(oValue.sName))
        		{
        			Ext.Msg.alert('温馨提示','请输入角色名称');  
        		}else
        		{
        			if(oValue.aNode.length == 0)
    				{
    					Ext.Msg.alert('温馨提示','请选择权限结点');  
    				}else
					{
    					var oNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
						Ext.Ajax.request({   
						   url: 'editBgRole.html',   
						   method: 'post',   
						   params: {nBgRoleId: me.nBgRoleId, sName: oValue.sName, sBgTreeIdList : oValue.aNode.join(',')},   
						   success: function(resp, action)
						   {
						        var obj = Ext.util.JSON.decode(resp.responseText); 
						        if(obj.success)
						        {
						        	oNode.setText(oValue.sName);
								 	me.hide(oNode.getUI().getEl());
						        }else
						        {
						        	Ext.Msg.alert('温馨提示', obj.msg); 
						        }
						   },
						   failure:function(resp, action)
						   {   
						        Ext.Msg.alert('温馨提示','系统错误!');   
						   }   
						}); 
					}
        		}
        	}
        },{
            text: '关闭',
            iconCls:'exit',
            handler: function()
            {
    			me.hide();
            }
        }],
		listeners:{
			show : function(win){getRole(win, me.items.get(0));}
		}
	});
};
Ext.extend(Ext.EditBgRoleWindow, Ext.Window);

//查看角色
Ext.ViewBgRoleWindow = function()
{
	var me = this;
	Ext.ViewBgRoleWindow.superclass.constructor.call(this, {
		layout:'fit',
        width:m_nRoleWidth,
        height:m_nRoleHeight,
        plain: true,
        title: '查看角色',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items:new Ext.NavTree(), 
        buttons: [
        {
            text: '关闭',
            iconCls:'exit',
            handler: function()
            {
    			me.hide();
            }
        }],
		listeners:{
			show : function(win){getRole(win, me.items.get(0));}
		}
	});
};

Ext.extend(Ext.ViewBgRoleWindow, Ext.Window);

function getRole(win, oTree)
{
    var root = oTree.getRootNode();
    oTree.clearAll();
	Ext.Ajax.request({   
	   url: 'getBgRole.json',   
	   method: 'post',   
	   params: {nBgRoleId: win.nBgRoleId||0},   
	   success: function(resp, action)
	   {   
	        var obj = Ext.util.JSON.decode(resp.responseText);    
			var aComponents = oTree.getTopToolbar().findByType('textfield');
			aComponents[0].setValue(obj.sName);
			oTree.setSelect(root, obj.aNavTreeId);
	    },   
	    failure:function(resp, action)
	    {   
	        Ext.Msg.alert('温馨提示','系统错误!');   
	    }   
	}); 
}

Ext.NavTree = function(oCenterNav)
{
	Ext.NavTree.superclass.constructor.call(this, {
		dataUrl:'getBgNavTree.json',
        root: new Ext.tree.AsyncTreeNode({id:'TreeNav_1',expanded: true, checked:false, text:'全部权限'}),
        tbar:[{
        		xtype: 'tbtext', 
        		text: '角色名称：'
        	},
        	{
        		xtype: 'textfield',
        		width: 300
        	}
        ]
	});
	
	this.getRootNode().expand(true);
};
Ext.extend(Ext.NavTree, Ext.tree.CheckBoxTreePanel, 
{
	getValue : function()
	{
		var aComponents = this.getTopToolbar().findByType('textfield');
        var sName = aComponents[0].getValue();
        var aNode = this.getChecked();
        var aId = [];
        for(var i = 0, nLen = aNode.length; i < nLen; i++)
        {
        	aId.push(aNode[i].id.replace(/[^0-9]*/,''));
        }
		return {
			sName : sName,
			aNode : aId
		};
	}
});
