Ext.BgDeptTree = function(config)
{
	Ext.apply(this, config);
	Ext.BgDeptTree.superclass.constructor.call(this, {
		region: 'center',
		title: '部门管理',
		dataUrl:'getBgDeptList.do',
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
			click: function(oNode, oEvent)
			{
	
			}
		}
	});
};
Ext.extend(Ext.BgDeptTree, Ext.tree.TreePanel);

Ext.BgDeptTreeWindow = function(trigger) 
{
	var me = this;
	Ext.BgDeptTreeWindow.superclass.constructor.call(this, {
		layout : 'border',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '用户部门',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : new Ext.BgDeptTree({trigger: trigger, win: me}),
		buttons : [ {
			text : '选中返回',
			iconCls : 'look',
			handler : function() 
			{
				var oNode = me.items.get(0).getSelectionModel().getSelectedNode();
				if(oNode && parseInt(oNode.id.replace(/[^0-9]*/,''), 10) != 1)
				{
					trigger.setValue(oNode.text);
					trigger.setRealValue(oNode.id.replace(/[^0-9]*/,''));
					me.hide();
				}else
				{
					Ext.Msg.alert('温馨提示', '请选择用户部门'); 
				}
			}
		} ]
	});
};
Ext.extend(Ext.BgDeptTreeWindow, Ext.Window);		

//触发控件
Ext.form.BgDeptTrigger = Ext.extend(Ext.form.TriggerField, 
{
	triggerClass: 'x-form-search-trigger',
	editable: false,
	realValue : '',
	initComponent : function()
	{
		this.win = new Ext.BgDeptTreeWindow(this),
        Ext.form.BgDeptTrigger.superclass.initComponent.call(this);
	},
	onTriggerClick : function()
	{
		this.win.show(this.trigger);
	},
	setRealValue : function(v)
	{
		this.realValue = v;
	},
	getRealValue : function()
	{
		return this.realValue;
	}
});
Ext.reg('bgdepttrigger', Ext.form.BgDeptTrigger);