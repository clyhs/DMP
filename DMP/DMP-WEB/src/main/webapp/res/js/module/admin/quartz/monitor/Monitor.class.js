Ext.Monitor = function()
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/quartz/getMonitorList.json'
	    }),
	    root: 'data',
	    idProperty: 'MonitorId',
	    totalProperty: 'total',
	    fields: [
	    	{name: 'JOBNAME', type: 'string'},
	    	{name: 'JOBFILE', type: 'string'},
	    	{name: 'REPEAT_TIMES', type: 'long'},
	    	{name: 'NEXT_FIRE_TIME', type: 'string'},
	    	{name: 'JOBSTATUS', type: 'long'},
	    	{name: 'PREV_CONTINUED_TIME', type: 'long'},
	    	{name: 'START_TIME', type: 'string'},
	    	{name: 'END_TIME', type: 'string'},
	    	{name: 'ERRMSG', type: 'string'}
	    ]
	});
	oStore.load();
	
	Ext.Monitor.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        id: 'monitor',
        loadMask : true,
        columns: [
        	{header:'序号',align: 'center', width: 60, fixed: true, menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		}
        	 }
            ,{header: '调度名称',  width: 220, 
                    renderer: function(value, metaData, record, rowIndex, colIndex, store)
                    {
                    	var jobName = store.reader.jsonData.data[rowIndex].JOBNAME;
                    	var str = "<a href = 'javascript:showTransform("+rowIndex+",\""+jobName+"\")' style='color: blue; padding: 0px;'>"+jobName+"</a>";
            		    return str;
                    }}
            ,{header: '状态', align: 'center', dataIndex: 'JOBSTATUS', width: 80}
            ,{header: '运行次数', align: 'center', dataIndex: 'REPEAT_TIMES', width: 60}
            ,{header: '执行用时(秒)', align: 'center', dataIndex: 'PREV_CONTINUED_TIME'}
            ,{header: '下次启动时间', align: 'center', dataIndex: 'NEXT_FIRE_TIME',width: 130}
            ,{header: '执行开始时间', align: 'center', dataIndex: 'START_TIME',width: 130 }
            ,{header: '执行结束时间', align: 'center', dataIndex: 'END_TIME',width: 130}
            ,{header: '记录日志', align: 'center', renderer: function(value, metaData, record, rowIndex, colIndex, store)
            	{
            	if(record.data.ERRMSG == ''){
            		return '无错误';
            	}
            	var str = "<a href = 'javascript:showErrorMsg("+rowIndex+")' style='color: red; padding: 0px;'>查看</a>";
            	return str;
            	}}
            ,{header: '调度文件', align: 'center', dataIndex: 'JOBFILE',width: 120}
        ],
        stripeRows: true,
        border:false,
        tbar: new Ext.MonitorTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true
        })
	});
};

Ext.extend(Ext.Monitor, Ext.grid.GridPanel);

//跳到转换记录的窗口
function showTransform(rowIndex,jobName)
{
	var oCenterNav = parent.oCenterNav;
	var monitorData = oCenterNav.getComponent('Frame_monitor_'+rowIndex);
	if(monitorData)
	{
		oCenterNav.setActiveTab(monitorData);
		return ;
	}
    var oIFrameComponent = new parent.Ext.IFrameComponent({
				id: 'Frame_monitor_'+rowIndex, 
				url: contextPath+'/module/admin/quartz/monitordata.html', 
				title: '转换文件的日志追踪',
				name:jobName
			});
	oCenterNav.setActiveTab(oCenterNav.add(oIFrameComponent));
	return ;
}

//查看日志的信息
function showErrorMsg(rowIndex)
{
	var store= Ext.getCmp('monitor').getStore();
	var items = store.data.items;
	var record = items[rowIndex].data;
	var errorWin=new parent.Ext.Window({
		layout : 'border',
		width : 400,
		height : 500,
		plain : true,
		title : '错误日志显示',
		resizable: true,
		maximizable: true,
		closeAction : 'close',
		modal : true,
		iconCls: 'form',
		items : [{
					xtype: 'textarea',
					region: 'center',
					autoScroll: true,
					width: 250,
					value: record.ERRMSG ==""?'无错误信息': record.ERRMSG,
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
