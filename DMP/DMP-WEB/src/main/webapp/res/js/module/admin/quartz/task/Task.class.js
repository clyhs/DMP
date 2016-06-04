Ext.Task = function(){
	var me = this;
	var store = new Ext.data.JsonStore({
		autoDestroy : true,
		proxy : new Ext.data.HttpProxy({
		        method: 'post',
		        url: 'getTaskList.json'
		}),
		root: 'data',
		idProperty: 'TaskId',
		totalProperty: 'total',
		fields: [
		   'taskCode'
		  ,'name'
		  ,{name :'beginTime',type:'date',dateFormat:'time'}
		  ,{name :'endTime',type:'date',dateFormat:'time'}
		]
	});
	store.load({params:{name: ''}});
	Ext.grid.RowNumberer = Ext.extend(Ext.grid.RowNumberer, {
		width: 35,
		header:'序号',
		renderer: function (value, cellmeta, record, rowIndex, columnIndex, store) {
			var start = parseInt(store.lastOptions.params.start || 0, 10);
			return start + rowIndex + 1;    
		}
	});
	var oSM = new Ext.grid.CheckboxSelectionModel();
	oSM.handleMouseDown = Ext.emptyFn;
	Ext.Task.superclass.constructor.call(this, {
		region:'center',
        store: store,
        id: 'grid',
        loadMask : true,
        sm:oSM,
        columns: [
        	oSM
        	,new Ext.grid.RowNumberer()
            ,{header: '任务编码', dataIndex: 'taskCode', width: 180}
            ,{header: '任务名称', dataIndex: 'name', width: 180, id:'taskName'}
            ,{header: '开始时间', dataIndex: 'beginTime',width: 180,renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s')}
            ,{header: '结束时间', dataIndex: 'endTime', width: 180,renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s')}
            
        ],
        stripeRows: true,
        border:false,
        autoExpandColumn:'taskName',
        tbar: new Ext.TaskTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: store,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			store.setBaseParam('name', me.name || '');
        		}
            }
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

Ext.extend(Ext.Task, Ext.grid.GridPanel);
