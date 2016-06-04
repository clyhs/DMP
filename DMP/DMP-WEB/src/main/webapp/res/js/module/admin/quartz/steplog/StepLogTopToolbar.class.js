var timer = null;
Ext.StepLogTopToolbar = function(oGrid)
{
	var me = this;	
	Ext.StepLogTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
            {
            	xtype: 'tbtext',
            	text: '转换名称'
            },
            {
            	xtype: 'textfield',
            	width: 200,
            	id: 'jobName'
            },{xtype: 'tbspacer', width: 10},
            {
		        text: '查询',
	            iconCls: 'search',
	            handler: function(oButton, oEvent)
	            {
	            	var jobName = Ext.getCmp('jobName').getValue();
	            	oGrid.jobName = jobName;
	            	var component = me.findByType('combo');
	            	component[0].setValue(-1);
	            	if(timer){
	            		window.clearTimeout(timer);
	            	}
	            	oGrid.getStore().load({params: {jobName: jobName}});
	            }
		     
            },{xtype: 'tbspacer', width: 10},
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
                			var jobName = Ext.getCmp('jobName').getValue();
                			timer = window.setInterval(function(){
                				oGrid.getStore().load({params: {jobName: jobName}});
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
	            	var jobName = Ext.getCmp('jobName').getValue();
	            	oGrid.getStore().load({params: {jobName: jobName}});
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

Ext.extend(Ext.StepLogTopToolbar, Ext.Toolbar);