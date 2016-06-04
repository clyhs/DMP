Ext.BgDept = function(config)
{
	Ext.apply(this, config);
	var me = this;
	Ext.BgDept.superclass.constructor.call(this, {
		region: 'center',
		title: '后台部门管理',
		dataUrl:'getBgDeptList.json',
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
        root: new Ext.tree.AsyncTreeNode({id:'BgDeptTreeId_1',expanded: true, text:'全部部门'}),
        listeners: {
			contextmenu: function(oNode, oEvent)
			{
				oEvent.stopEvent();
				oNode.select();
				if(!me.BgDeptMenu)
				{
					me.BgDeptMenu = new Ext.BgDeptMenu();
				}
				if(oNode.getOwnerTree().getRootNode() == oNode)
				{
					me.BgDeptMenu.items.get(2).hide();
					me.BgDeptMenu.items.get(3).hide();
				}else
				{
					if(!me.BgDeptMenu.items.get(2).isVisible()) me.BgDeptMenu.items.get(2).show();
					if(!me.BgDeptMenu.items.get(3).isVisible()) me.BgDeptMenu.items.get(3).show();
				}
				me.BgDeptMenu.showAt(oEvent.getXY());
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
				var aBgDeptTreeId = [];
				for(var i=0,iLen=aData.length;i<iLen;i++)
				{
					aBgDeptTreeId.push(aData[i].id);
				}
				
				var nDestBgDeptId = oEvent.target.id.replace(/[^0-9]*/,'');
				var nSrcBgDeptId = aBgDeptTreeId.length > 0 ? 0 : oEvent.dropNode.id.replace(/[^0-9]*/,'');
				
				Ext.Ajax.request({   
				   url: 'editMoveBgDept.json',   
				   method: 'post',   
				   params: {nDestBgDeptId: nDestBgDeptId
						, nSrcBgDeptId: nSrcBgDeptId},
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
Ext.extend(Ext.BgDept, Ext.tree.TreePanel);
