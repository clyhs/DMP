Ext.TransLog = function()
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/quartz/getTransLogList.json'
	    }),
	    root: 'data',
	    fields: [
	 	        {name: 'TRANSNAME', type: 'string'},
	 	        {name: 'STATUS', type: 'string'},
		    	{name: 'ID_BATCH', type: 'string'},
		    	{name: 'LINES_READ', type: 'long'},
		    	{name: 'LINES_WRITTEN', type: 'long'},
		    	{name: 'LINES_INPUT', type: 'long'},
		    	{name: 'LINES_OUTPUT', type: 'long'},
		    	{name: 'ERRORS', type: 'long'},
		    	{name: 'STARTDATE', type: 'string'},
		    	{name: 'LOGDATE', type: 'string'},
		    	{name: 'ENDDATE', type: 'string'},
		    	{name: 'LOG_FIELD', type: 'string'}
		    ]
	});
	
	Ext.TransLog.superclass.constructor.call(this, {
		region:'center',
		id: 'transLog',
        store: oStore,
        loadMask : true,
        columns: [
              	{header:'序号', width: 60,align: 'center', fixed: true, menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		 }}
            	,{header: '转换名称', dataIndex: 'TRANSNAME', width: 170 }
            	,{header: '转换状态', align: 'center',dataIndex: 'STATUS', width: 100 }
                ,{header: '读(条)', align: 'center',dataIndex: 'LINES_READ', width: 170 }
                ,{header: '写(条)', align: 'center', dataIndex: 'LINES_WRITTEN',width: 120}
                ,{header: '输入(条)', align: 'center', dataIndex: 'LINES_INPUT', width: 80}
                ,{header: '输出(条)', align: 'center', dataIndex: 'LINES_OUTPUT'}
                ,{header: '错误', align: 'center', dataIndex: 'ERRORS', width: 140}
                ,{header: '记录日志', align: 'center', renderer: function(value, metaData, record, rowIndex, colIndex, store)
                	{
                	if(record.data.ERRORS != 0){
                		var str = "<a href = 'javascript:showLogMsg("+rowIndex+")' style='color: red; padding: 0px;'>查看</a>";
                    	return str;
                	}
                	var str = "无错误";
                	return str;
                	}}
                ,{header: '开始时间', align: 'center', dataIndex: 'ENDDATE', width: 200}
                ,{header: '结束时间', align: 'center', dataIndex: 'LOGDATE', width: 200 }
            ],
        stripeRows: true,
        border:false,
        tbar: new Ext.TransLogTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			oStore.setBaseParam('transLogName', me.transLogName || '');
        		}
            }
        })
	});
};

Ext.extend(Ext.TransLog, Ext.grid.GridPanel);

//查看日志的信息
function showLogMsg(rowIndex)
{
	var store= Ext.getCmp('transLog').getStore();
	var items = store.data.items;
	var record = items[rowIndex].data;
	var errorWin=new parent.Ext.Window({
		layout : 'border',
		width : 800,
		height : 600,
		plain : true,
		title : '错误日志显示',
		constrain: true,
		maximizable: true,
		resizable: true,
		closeAction : 'close',
		modal : true,
		iconCls: 'form',
		items : [{
					xtype: 'textarea',
					region: 'center',
					autoScroll: true,
					width: 250,
					value: record.LOG_FIELD,
					heigth: 50,
					readOnly: true
					}],
		buttons : [{
			text : '关闭',
			iconCls : 'exit',
			handler : function() {
				errorWin.close();
			}
		} ]
	});
	errorWin.show();
	return;
}