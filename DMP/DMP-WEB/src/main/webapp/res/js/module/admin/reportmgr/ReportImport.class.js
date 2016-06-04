Ext.ReportImport = function()
{
	
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: 'getExcelList.json'
	    }),
	    root: 'data',
	    totalProperty: 'total',
	    fields: [
	       'F0'
	      ,'F1'
	      ,'F2'
	      ,'F3'
	      ,'F4'
	      ,'F5'
	      ,'F6'
	      ,'F7'
	      ,'F8'
	      ,'F9'
	      ,'F10'
	      ,'F11'
	      ,'F12'
	      ,'F13'
	      ,'F14'
	      ,'F15'
	      ,'F16'
	      ,'F17'
	      ,'F18'
	      ,'F19'
	      ,'F20'
	    ]
	});
	
	oStore.load();
	var sm = new Ext.grid.CheckboxSelectionModel({singleSelect : false});
	sm.handleMouseDown = Ext.emptyFn;


    Ext.ReportImport.superclass.constructor.call(this, {
    	id:'editorGridPanel',
    	store: oStore,
        region:'center',
        loadMask : true,
        selModel:sm,
        clickstoEdit:1,
        columns: [],
        listeners : {
        	afteredit:function(e){
        		var conn = new Ext.data.Connection();
			    conn.request({
			    	 url:'editExcel.json',
			    	 params:{
			    		 id:e.record.data.F0,
			    		 field:e.field,
			    		 changeValue:e.value
			    	 },
			         success : function(form, action) 
					 {
			        	 oStore.reload();	
					 },
					 failure : function(form, action) 
					 {
						 Ext.Msg.alert('温馨提示','请求失败!');
					 }
			    });
        	}
        },
        stripeRows: true,
        border:false,
        tbar: new Ext.ReportImportTopToolbar(this),
	    bbar : new Ext.PagingToolbar({
		pageSize : 100,
		store : oStore,
		displayInfo : true,
		listeners : {
			beforechange : function() {
				oStore.setBaseParam('loadData','loadData');
				
			}
		}
	 })
  });  
    
	this.on('cellclick', function(grid, rowIndex,columnIndex,event) {
	  if (columnIndex != 0) {
	  if (sm.isSelected(rowIndex)){
		 sm.deselectRow(rowIndex);
	  }else{
	    sm.selectRow(rowIndex, true);
	  }
	 }
	});
    
};

Ext.extend(Ext.ReportImport, Ext.grid.EditorGridPanel);
