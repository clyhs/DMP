/**
 * 角色管理中的角色树
 */
Ext.BgRoleTree = function()
{
	var me = this;
	Ext.BgRoleTree.superclass.constructor.call(this, {
		region: 'center',
		title: '角色',
		dataUrl:'getBgRoleTree.json',
		split: true,
		collapsible: true,
		collapseMode: 'mini',
		margins: '0 0 5 0',
		cmargins: '0 5 5 5',
		animate:true, 
        autoScroll:true,
        enableDD:true,
        containerScroll: true,
        border: false,
        ddGroup: 'gridToTreeDDGroup',
        line: true,
        tools: [{id:'refresh', handler: function(event,toolEl,panel,tc){
    			panel.getRootNode().reload();
    		}
        }],
        rootVisible: true,
        root: new Ext.tree.AsyncTreeNode({id:'RoleId_0',expanded: true, text:'全部角色'}),
        listeners: {
        	//监听鼠标右键点击的菜单项
			contextmenu: function(oNode, oEvent)
			{
				oEvent.stopEvent();
				oNode.select();
				//如菜单项不存在则初始化
				if(!me.RoleMenu)
				{
					me.RoleMenu = new Ext.BgRoleTreeMenu(me);
				}
				var id = oNode.id.replace(/[^0-9]*/,'');
				if(id=='0')       //id为0时则表示全部角色，能够进行新增角色的操作，其余选项隐藏
				{
					me.RoleMenu.items.get(0).hide();
					if(!me.RoleMenu.items.get(1).isVisible()) me.RoleMenu.items.get(1).show();
					me.RoleMenu.items.get(2).hide();
					me.RoleMenu.items.get(3).hide();
				}else            //除了新增角色外，其余选项可选
				{
					if(!me.RoleMenu.items.get(0).isVisible()) me.RoleMenu.items.get(0).show();
					me.RoleMenu.items.get(1).hide();
					if(!me.RoleMenu.items.get(2).isVisible()) me.RoleMenu.items.get(2).show();
					if(!me.RoleMenu.items.get(3).isVisible()) me.RoleMenu.items.get(3).show();
				}
				me.RoleMenu.showAt(oEvent.getXY());
			},
			expandnode : function(node){  //更改角色前面的图标
				
				node.setIconCls("role-tree");
			    //更改当前节点下的所有子节点的图标  
			    for (var i = 0, len = node.childNodes.length; i < len; i++) {
			    	var curChild = node.childNodes[i]; 
			        curChild.setIconCls("role-tree");
			    }  
			}
		}
	});
};
Ext.extend(Ext.BgRoleTree, Ext.tree.TreePanel);
