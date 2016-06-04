Ext.BgPostTopToolbar = function(oGrid)
{
	var me = this;
	Ext.BgPostTopToolbar.superclass.constructor.call(this, {
        enableOverflow: true,
        items: [
        	{
        		xtype: 'tbtext', 
        		text: '岗位名称：'
        	},
        	{
        		xtype: 'textfield'
        	},{xtype: 'tbspacer', width: 10},
        	{
        		xtype: 'tbtext', 
        		text: '所属部门：'
        	},
        	{
        		xtype: 'textfield'
        	},{xtype: 'tbspacer', width: 10},
        	{
	            text: '搜索',
	            iconCls: 'search',
	            handler: function(oButton, oEvent)
	            {
        			var aComponents = me.findByType('textfield');
        			var sName = aComponents[0].getValue();
        			var sDeptName = aComponents[1].getValue();
        			oGrid.getStore().load({params:{sName:sName, sDeptName:sDeptName}});
        		}
        	},{xtype: 'tbspacer', width: 10},
        	{
	            text: '选择返回',
	            iconCls: 'look',
	            handler: function(oButton, oEvent)
	            {
	            	var aRecord = oGrid.getSelectionModel().getSelections();
	            	var aPostId = [];
	            	var aPostName = [];
					for(var nKey in aRecord)
					{
						if(aRecord[nKey].data != undefined)
						{
							aPostId.push(aRecord[nKey].data.bgPostId);
							aPostName.push(aRecord[nKey].data.name);
						}
					}
	    			if(aPostId.length > 0)
	    			{
	    				oGrid.trigger.setRealValue(aPostId.join(','));
	    				oGrid.trigger.setValue(aPostName.join(','));
	    				oGrid.win.hide();
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择用户岗位！');
	    			}
    			}
        	}
        ]
	});
};
Ext.extend(Ext.BgPostTopToolbar, Ext.Toolbar);

Ext.BgPostGridWindow = function(trigger) 
{
	var me = this;
	Ext.BgPostGridWindow.superclass.constructor.call(this, {
		layout : 'border',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '查找用户岗位',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : new Ext.BgPost({trigger: trigger, win: me}),
		buttons : [ {
			text : '关闭',
			iconCls : 'exit',
			handler : function() 
			{
				me.hide();
			}
		} ]
	});
};
Ext.extend(Ext.BgPostGridWindow, Ext.Window);		

//触发控件
Ext.form.BgPostTrigger = Ext.extend(Ext.form.TriggerField, 
{
	triggerClass: 'x-form-search-trigger',
	editable: false,
	realValue : '',
	initComponent : function()
	{
		this.win = new Ext.BgPostGridWindow(this),
        Ext.form.BgPostTrigger.superclass.initComponent.call(this);
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
Ext.reg('bgposttrigger', Ext.form.BgPostTrigger);