Ext.BgNav = function()
{
	var me = this;
	Ext.BgNav.superclass.constructor.call(this, {
		region: 'center',
		title: '后台导航树管理',
		dataUrl:'getBgNavTree.json',
		border: false,
		collapsible: true,
		collapseMode: 'mini',
		margins: '0 0 5 0',
		cmargins: '0 5 5 5',
		animate:true, 
        autoScroll:true,
        enableDD:true,
        containerScroll: true,
        enableDD:true,
        line: true,
        tools: [{id:'refresh', handler: function(event,toolEl,panel,tc){
    			panel.getRootNode().reload();
    		}
        }],
        rootVisible: true,
        root: new Ext.tree.AsyncTreeNode({id:'NavTreeId_1',expanded: true, text:'全部导航结点'}),
        listeners: {
			contextmenu: function(oNode, oEvent)
			{
				oEvent.stopEvent();
				oNode.select();
				if(!me.NavMenu)
				{
					me.NavMenu = new Ext.BgNavMenu();
				}
				if(oNode.getOwnerTree().getRootNode() == oNode)
				{
					me.NavMenu.items.get(2).hide();
					me.NavMenu.items.get(3).hide();
				}else
				{
					if(!me.NavMenu.items.get(2).isVisible()) me.NavMenu.items.get(2).show();
					if(!me.NavMenu.items.get(3).isVisible()) me.NavMenu.items.get(3).show();
				}
				me.NavMenu.showAt(oEvent.getXY());
			},
			nodedragover: function(oEvent)
			{
				var oNode = oEvent.target;
				oNode.leaf = false;
				return true;
			},
			beforeNodeDrop: function(oEvent)
			{
				var aData = oEvent.data.selections||[];
				var aNavTreeId = [];
				for(var i=0,iLen=aData.length;i<iLen;i++)
				{
					aNavTreeId.push(aData[i].id);
				}
				
				var nDestBgTreeId = oEvent.target.id.replace(/[^0-9]*/,'');
				var nSrcBgTreeId = aNavTreeId.length > 0 ? 0 : oEvent.dropNode.id.replace(/[^0-9]*/,'');
				
				Ext.Ajax.request({   
					   url: 'editMoveBgNavTree.json',   
					   method: 'post',   
					   params: {nDestBgTreeId: nDestBgTreeId
							, nSrcBgTreeId: nSrcBgTreeId},
					   success: function(resp, action)
					   {   
					       var obj = Ext.util.JSON.decode(resp.responseText);   
					       if(!obj.success)   
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
Ext.extend(Ext.BgNav, Ext.tree.TreePanel);
