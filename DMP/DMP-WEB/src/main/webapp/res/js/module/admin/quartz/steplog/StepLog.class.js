Ext.StepLog = function()
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/quartz/getStepLogList.json'
	    }),
	    root: 'data',
	    totalProperty: 'total',
	    fields: [
	        {name: 'TRANSNAME', type: 'string'},
	        {name: 'STEPNAME', type: 'string'},
	    	{name: 'ID_BATCH', type: 'string'},
	    	{name: 'LINES_READ', type: 'long'},
	    	{name: 'LINES_WRITTEN', type: 'long'},
	    	{name: 'LINES_INPUT', type: 'long'},
	    	{name: 'LINES_OUTPUT', type: 'long'},
	    	{name: 'ERRORS', type: 'long'},
	    	{name: 'LOG_DATE', type: 'string'}
	    ]
	});
	
	Ext.StepLog.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        loadMask : true,
        columns: [
        	{header:'序号', width: 60,align: 'center', fixed: true, menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		}
        	 }
        	,{header: '转换名称', dataIndex: 'TRANSNAME', width: 170 }
        	,{header: '步骤名称', dataIndex: 'STEPNAME', width: 170 }
            ,{header: '读(条)', align: 'center',dataIndex: 'LINES_READ', width: 170 }
            ,{header: '写(条)', align: 'center', dataIndex: 'LINES_WRITTEN',width: 120}
            ,{header: '输入(条)', align: 'center', dataIndex: 'LINES_INPUT', width: 80}
            ,{header: '输出(条)', align: 'center', dataIndex: 'LINES_OUTPUT'}
            ,{header: '错误', align: 'center', dataIndex: 'ERRORS', width: 140}
            ,{header: '日志记录时间', align: 'center', dataIndex: 'LOG_DATE', width: 200 }
        ],
        stripeRows: true,
        border:false,
        tbar: new Ext.StepLogTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			oStore.setBaseParam('jobName', me.jobName || '');
        		}
            }
        })
	});
};

Ext.extend(Ext.StepLog, Ext.grid.GridPanel);