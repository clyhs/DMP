Ext.Scheduler = function()
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/quartz/getSchedulerlist.json'
	    }),
	    root: 'data',
	    idProperty: 'jobName',
	    totalProperty: 'total',
	    fields: [ 
	          'jobId' ,
	    	  'jobName',
	    	  'actionPath',
	    	  'actionRef',
	    	  'triggerState',
	    	  'description',
	    	  { name:'nextFireTime'}
	    ]
	});
	oStore.load();
	var oSM = new Ext.grid.CheckboxSelectionModel();
	oSM.handleMouseDown = Ext.emptyFn;
	
	Ext.Scheduler.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        id: 'scheduler',
        loadMask : true,
        sm:oSM,
        columns: [
        	oSM
        	,{header:'序号', width: 60, fixed: true,align: 'center', menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		}
        	 }
            ,{header: '任务名称', dataIndex: 'jobName', width: 220}
            ,{header: '任务状态', width: 100, renderer: function(value, metaData, record, rowIndex, colIndex, store){
            	var state = store.reader.jsonData.data[rowIndex].triggerState; 
            	switch(state) {
            	case 4: return '挂起' ;
            	case 2: return '完成';
            	case 3: return '出错';
            	case -1: return '无调度';
            	case 0: return '正常';
            	case 1: return '暂停';
            	}
            	return store.reader.jsonData.data.triggerState;
            }}
            //,{header: '调度文件', dataIndex: 'actionRef', width: 140}
            ,{header: '任务描述', dataIndex: 'description', width: 180}
            ,{header: '下次执行时间', dataIndex: 'nextFireTime', width: 160}
            ,{header: '任务路径', dataIndex: 'actionPath',width: 120}
        ],
        stripeRows: true,
        border:false,
        tbar: new Ext.SchedulerTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true
        })
	});
	
	me.on('cellclick', function(grid, rowIndex,columnIndex,event) {
		  if (columnIndex != 0) {
		  if (oSM.isSelected(rowIndex)){
			  oSM.deselectRow(rowIndex);
		  }else{
			  oSM.selectRow(rowIndex, true);
		  }
		 }
	});
};

Ext.extend(Ext.Scheduler, Ext.grid.GridPanel);
