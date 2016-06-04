Ext.grid.DynamicGrid = Ext.extend(Ext.grid.GridPanel, {
	initComponent: function() {
		/*var ds = new Ext.data.Store({
			method: 'post',
			url: this.storeUrl,
			reader: new Ext.data.JsonReader()
		});*/
		var ds = new Ext.data.JsonStore({
		    autoDestroy: true,
		    proxy : new Ext.data.HttpProxy({
		        method: 'post',
		        url: this.storeUrl
		    })
		});
		var config = {
			viewConfig: {
				forceFit: true
			},
			enableColLock: false,
			loadMask: true,
			border: false,
			stripeRows: true,
			ds: ds,
			columns: [],
			region:'center'
		};
		this.bbar.bindStore(ds, true);
		Ext.apply(this, config);
		Ext.apply(this.initialConfig, config);
		Ext.grid.DynamicGrid.superclass.initComponent.apply(this, arguments);
	},
	
	onRender: function(ct, position) {
		this.colModel.defaultSortable = true;
		Ext.grid.DynamicGrid.superclass.onRender.call(this, ct, position);

		this.store.on('load', function() {
			if (typeof(this.store.reader.jsonData.columns) === 'object') {
				var columns = [];
				if (this.rowNumberer) {
					columns.push(
						{header:'序号',align: 'center', width: 60, fixed: true, menuDisabled: false
				        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
				        			var start = parseInt(store.lastOptions.params.start || 0, 10);
				    				return start + rowIndex + 1;   
				        		}
				        	 }		
					);
				}
				if (this.checkboxSelModel) {
					columns.push(new Ext.grid.CheckboxSelectionModel());
				}
				
				Ext.each(this.store.reader.jsonData.columns, 
					function(column) {
					    var sAlign = column.align;
					    if(sAlign == "right"){
					    	column.renderer = function(value){
					    	   value = value.trim();
					    	   var p = /%$/; //处理百分号问题
					    	   var r = /^\d*\.\d*$/;
					    	   if(p.test(value)){
					    		 value = value.substring(0,value.length-1); 
					    		 if(!r.test(Number(value))){
					    			 return Ext.util.Format.number(value,'0,000') + '%';
					    		 }else{
					    			 return Ext.util.Format.number(value,'0,000.00') + '%'; 
					    		 }
					    	   }else{
						           if(!r.test(Number(value))){
						        	  return Ext.util.Format.number(value,'0,000');
						           }else{
						        	  return Ext.util.Format.number(value,'0,000.00');  
						           };  
					    	   }
					    	}
					    }
						columns.push(column);
					}
				);
				this.getColumnModel().setConfig(columns);
			}
				
		}, this);
		this.store.load();
	}
});