var timer = null;
Ext.MonitorDataTopToolbar = function(oGrid)
{
	var me = this;	
	Ext.MonitorDataTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
            {
            	xtype: 'tbtext',
            	text: '刷新间隔(秒)'
            },
		    {
		    	xtype: 'combo',
		    	width: 100,
		    	mode: 'local',
                allowBlank: false,
                triggerAction : 'all',
		    	store: new Ext.data.ArrayStore({
		    		fields:['freshTimeId','freshTimeName'],
		    		data: [[-1,'不刷新'],[5,'5秒刷新'],[10,'10秒刷新']]
		    	}),
		    	value: '不刷新',
		    	valueField: 'freshTimeId',
		    	displayField: 'freshTimeName',
		    	listeners: {
                	select: function(combo, record, index  )
                	{
                		if(index != 0){
                			timer = window.setInterval(function(){
                				oGrid.getStore().load({params: 
                					{currentJobName: oGrid.currentJobName}});
                            },index * 5 * 1000);
                            return ;
                		}
                		 window.clearTimeout(timer); 
                	}
                }
		    }
			,{xtype: 'tbspacer', width: 10},
        	{
		        text: '刷新',
	            iconCls: 'refresh',
	            handler: function(oButton, oEvent)
	            {
	            	oGrid.getStore().load({params: {currentJobName: oGrid.currentJobName}});
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

Ext.extend(Ext.MonitorDataTopToolbar, Ext.Toolbar);