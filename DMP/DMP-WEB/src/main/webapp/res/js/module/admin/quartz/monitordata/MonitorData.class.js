Ext.MonitorData = function(jobName)
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'module/admin/quartz/getMonitorDataList.json'
	    }),
	    root: 'data',
	    idProperty: 'MonitorId',
	    totalProperty: 'total',
	    fields: [
	    	{name: 'id_batch', type: 'string'},
	    	{name: 'lines_read', type: 'long'},
	    	{name: 'lines_written', type: 'long'},
	    	{name: 'lines_input', type: 'long'},
	    	{name: 'lines_output', type: 'long'},
	    	{name: 'errors', type: 'long'},
	    	{name: 'startDate', type: 'date',dateFormat: 'time'},
	    	{name: 'endDate', type: 'date',dateFormat: 'time'}
	    ]
	});
	oStore.load({params: {currentJobName: jobName}});
	
	me.currentJobName = jobName;
	Ext.MonitorData.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        id: jobName,
        loadMask : true,
        columns: [
        	{header:'序号', width: 60,align: 'center', fixed: true, menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		 }}
            ,{header: '读(条)', align: 'center',dataIndex: 'lines_read', width: 170 }
            ,{header: '写(条)', align: 'center', dataIndex: 'lines_written',width: 120}
            ,{header: '输入(条)', align: 'center', dataIndex: 'lines_input', width: 80}
            ,{header: '输出(条)', align: 'center', dataIndex: 'lines_output'}
            ,{header: '错误', align: 'center', dataIndex: 'errors', width: 140}
            ,{header: '开始时间', align: 'center', dataIndex: 'startDate', width: 200, renderer: Ext.util.Format.dateRenderer('Y-m-d H:m:s')}
            ,{header: '结束时间', align: 'center', dataIndex: 'endDate', width: 200 ,renderer: Ext.util.Format.dateRenderer('Y-m-d H:m:s')}
        ],
        stripeRows: true,
        border:false,
        tbar: new Ext.MonitorDataTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        		}
            }
        })
	});
};

Ext.extend(Ext.MonitorData, Ext.grid.GridPanel);