Ext.Query = function()
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: contextPath+'/module/admin/reportmgr/getquerylist.json'
	    }),
	    root: 'data',
	    idProperty: 'queryId',
	    totalProperty: 'total',
	    fields: [ 
	          'id',
	          'componentId' ,
	    	  'conditionName',
	    	  'componentType',
	    	  'format',
	    	  'layout',
	    	  'comboxField'
	    ]
	});
	oStore.load({params:{queryName:'', queryId:''}});
	
	var oSM = new Ext.grid.CheckboxSelectionModel();
	oSM.handleMouseDown = Ext.emptyFn;
	
	Ext.Query.superclass.constructor.call(this, {
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
        	 ,{header:'id',dataIndex: 'id', width: 180,hidden :true}
            ,{header: '控件ID', dataIndex: 'componentId', width: 180}
            ,{header: '控件名称', dataIndex: 'conditionName',width: 190}
             ,{header: '控件类型', dataIndex: 'componentType',width: 190}
            ,{header: '控件格式', dataIndex: 'format',width: 150}
            ,{header: '控件优先级', dataIndex: 'layout' ,width: 180}
             ,{header: '控件关联ID', dataIndex: 'comboxField' ,width: 180}
        ],
        stripeRows: true,
        border:false,
        tbar: new Ext.QueryTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			oStore.setBaseParam('conditionName', me.queryName || '');
        			oStore.setBaseParam('componentId', me.queryId || '');
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

Ext.extend(Ext.Query, Ext.grid.GridPanel);
