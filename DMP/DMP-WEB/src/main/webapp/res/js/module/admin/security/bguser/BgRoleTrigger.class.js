Ext.BgRoleTopToolbar = function(oGrid)
{
	var me = this;
	Ext.BgRoleTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
        	{
        		xtype: 'tbtext', 
        		text: '角色名称：'
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
        			oGrid.getStore().load({params:{sName:sName}});
        		}
        	},{xtype: 'tbspacer', width: 10},
        	{
	            text: '选择返回',
	            iconCls: 'look',
	            handler: function(oButton, oEvent)
	            {
	            	var aRecord = oGrid.getSelectionModel().getSelections();
	            	var aRoleId = [];
	            	var aRoleName = [];
					for(var nKey in aRecord)
					{
						if(aRecord[nKey].data != undefined)
						{
							aRoleId.push(aRecord[nKey].data.bgRoleId);
							aRoleName.push(aRecord[nKey].data.name);
						}
					}
	    			if(aRoleId.length > 0)
	    			{
	    				oGrid.trigger.setRealValue(aRoleId.join(','));
	    				oGrid.trigger.setValue(aRoleName.join(','));
	    				oGrid.win.hide();
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择用户角色！');
	    			}
    			}
        	}
        ]
	});
};
Ext.extend(Ext.BgRoleTopToolbar, Ext.Toolbar);

Ext.BgRoleGrid = function(config)
{
	Ext.apply(this, config);
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: 'getBgRoleList.json'
	    }),
	    root: 'data',
	    idProperty: 'name',
	    totalProperty: 'total',
	    fields: [ 
	          {name :'bgRoleId', type:'int'}
	    	, 'name'
	    ]
	});
	oStore.load({params:{sName:'', sDeptName:''}});
	var oSM = new Ext.grid.CheckboxSelectionModel();
	
	Ext.BgRoleGrid.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        loadMask : true,
        ddGroup: 'gridToTreeDDGroup',
        enableDragDrop: true,
        sm:oSM,
        columns: [
        	oSM
        	,{header:'序号', width: 60, fixed: true, menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		}
        	 }
            ,{header: '角色名称', dataIndex: 'name', id:'Role_name'}
        ],
        stripeRows: true,
        autoExpandColumn: 'Role_name',
        border:false,
        tbar: new Ext.BgRoleTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			oStore.setBaseParam('sName', me.sName || '');
        		}
            }
        })
	});
};
Ext.extend(Ext.BgRoleGrid, Ext.grid.GridPanel);

Ext.BgRoleGridWindow = function(trigger) 
{
	var me = this;
	Ext.BgRoleGridWindow.superclass.constructor.call(this, 
	{
		layout : 'border',
		width : m_nWidth,
		height : m_nHeight,
		plain : true,
		title : '查找用户角色',
		closeAction : 'hide',
		modal : true,
		iconCls : 'form',
		items : new Ext.BgRoleGrid({trigger: trigger, win: me}),
		buttons : [ {
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				me.hide();
			}
		} ]
	});
};
Ext.extend(Ext.BgRoleGridWindow, Ext.Window);		

//触发控件
Ext.form.BgRoleTrigger = Ext.extend(Ext.form.TriggerField, 
{
	triggerClass: 'x-form-search-trigger',
	editable: false,
	realValue : '',
	initComponent : function()
	{
		this.win = new Ext.BgRoleGridWindow(this),
        Ext.form.BgRoleTrigger.superclass.initComponent.call(this);
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
Ext.reg('bgroletrigger', Ext.form.BgRoleTrigger);