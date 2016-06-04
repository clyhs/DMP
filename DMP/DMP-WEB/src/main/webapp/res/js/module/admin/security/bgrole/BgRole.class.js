Ext.BgRole = function()
{
	var me = this;
	Ext.BgRole.superclass.constructor.call(this, {
		region: 'east',
		title: '角色',
		dataUrl:'getBgRoleTree.json',
		split: true,
		collapsible: true,
		width: 200,
		minSize: 150,
		maxSize: 400,
		collapseMode: 'mini',
		margins: '0 0 5 0',
		cmargins: '0 5 5 5',
		animate:true, 
        autoScroll:true,
        enableDD:true,
        containerScroll: true,
        enableDD:true,
        ddGroup: 'gridToTreeDDGroup',
        line: true,
        tools: [{id:'refresh', handler: function(event,toolEl,panel,tc){
    			panel.getRootNode().reload();
    		}
        }],
        rootVisible: true,
        root: new Ext.tree.AsyncTreeNode({id:'RoleId_0',expanded: true, text:'全部角色'}),
        listeners: {
			contextmenu: function(oNode, oEvent)
			{
				oEvent.stopEvent();
				oNode.select();
				if(!me.RoleMenu)
				{
					me.RoleMenu = new Ext.BgRoleMenu();
				}
				var id = oNode.id.replace(/[^0-9]*/,'');
				if(id=='0')
				{
					me.RoleMenu.items.get(0).hide();
					if(!me.RoleMenu.items.get(1).isVisible()) me.RoleMenu.items.get(1).show();
					me.RoleMenu.items.get(2).hide();
					me.RoleMenu.items.get(3).hide();
				}else
				{
					if(!me.RoleMenu.items.get(0).isVisible()) me.RoleMenu.items.get(0).show();
					me.RoleMenu.items.get(1).hide();
					if(!me.RoleMenu.items.get(2).isVisible()) me.RoleMenu.items.get(2).show();
					if(!me.RoleMenu.items.get(3).isVisible()) me.RoleMenu.items.get(3).show();
				}
				me.RoleMenu.showAt(oEvent.getXY());
			},
			nodedragover: function(oEvent)
			{
				var oNode = oEvent.target;
				oNode.leaf = false;
				var aData = oEvent.data.selections;
				var id = oNode.id.replace(/[^0-9]*/,'');
				if(aData)
				{
					//从Grid拖拉到树
					return id == '0'?false:true;
				}
			},
			beforeNodeDrop: function(oEvent)
			{
				var aData = oEvent.data.selections||[];
				var aBgUserId = [0];
				for(var i=0,iLen=aData.length;i<iLen;i++)
				{
					if(aData[i].data.bgUserId == 1) continue;
					aBgUserId.push(aData[i].data.bgUserId);
				}
				
				var nBgRoleId = oEvent.target.id.replace(/[^0-9]*/,'');
				
				Ext.Ajax.request({   
					   url: 'moveToRole.json',   
					   method: 'post',   
					   params: {sBgUserId: aBgUserId.join(',')
							, nBgRoleId: nBgRoleId},
					   success: function(resp, action)
					   {   
					        var obj = Ext.util.JSON.decode(resp.responseText);   
					        if(obj.success)   
					        {    
					 			g_oViewPort.items.get(0).getStore().reload();
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
		}
	});
};
Ext.extend(Ext.BgRole, Ext.tree.TreePanel);
