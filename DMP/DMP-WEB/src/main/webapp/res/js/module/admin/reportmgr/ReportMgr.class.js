Ext.ReportMgr = function()
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/reportmgr/getreportlist.json'
	    }),
	    root: 'data',
	    idProperty: 'reportMgrId',
	    totalProperty: 'total',
	    fields: [ 
	          'reportId' ,
	    	  'reportName',
	    	  'sql',
	    	  'totalSql',
	    	  'chartSql',
	    	  'sumSql',
	    	  'updateStaff',
	    	  'reportTable',
	    	  { name:'updateTime', type:'date', dateFormat: 'time'}
	    ]
	});
	oStore.load({params:{reportName:'', reportId:''}});
	
	var oSM = new Ext.grid.CheckboxSelectionModel();
	oSM.handleMouseDown = Ext.emptyFn;
	
	Ext.ReportMgr.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        id: 'grid',
        loadMask : true,
        sm:oSM,
        columns: [
        	oSM
        	,{header:'序号', width: 60,align: 'center', fixed: true, menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		}
        	 }
            ,{header: '报表ID', dataIndex: 'reportId', width: 180}
            ,{header: '报表名称', dataIndex: 'reportName',width: 190}
            //,{header: '查询SQL', dataIndex: 'sql', width: 140}
            //,{header: '总数SQL', dataIndex: 'totalSql', width: 140}
            //,{header: '图表SQL', dataIndex: 'chartSql', width: 140}
            //,{header: '合计SQL', dataIndex: 'sumSql', width: 140}
            ,{header: '修改人员', dataIndex: 'updateStaff',width: 150}
            ,{header: '修改时间', dataIndex: 'updateTime' ,renderer: Ext.util.Format.dateRenderer('Y-m-d'),width: 180}
        ],
        stripeRows: true,
        border:false,
        tbar: new Ext.ReportMgrTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			oStore.setBaseParam('reporName', me.reportName || '');
        			oStore.setBaseParam('reportId', me.reportId || '');
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

Ext.extend(Ext.ReportMgr, Ext.grid.GridPanel);
