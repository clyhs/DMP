Ext.MonitorTopToolbar = function(oGrid)
{
	var me = this;	
	Ext.MonitorTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
        	{
		        text: '刷新',
	            id: 'refresh',
	            iconCls: 'refresh',
	            handler: function(oButton, oEvent)
	            {
	            	//oGrid.getSelectionModel().clearSelections();
	            	oGrid.getStore().load();
				}
        	},
        	'->',
        	{
        		text: '帮助',
        		iconCls: 'help'
        	}
        ]
	});
};

Ext.extend(Ext.MonitorTopToolbar, Ext.Toolbar);