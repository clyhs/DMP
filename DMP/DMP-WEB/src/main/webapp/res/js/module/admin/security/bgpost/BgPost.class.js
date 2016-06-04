Ext.BgPost = function(config)
{
	Ext.apply(this, config);
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: 'getBgPostList.json'
	    }),
	    root: 'data',
	    idProperty: 'bgPostId',
	    totalProperty: 'total',
	    fields: [ 
	          {name :'bgPostId', type:'int'}
	    	, 'name'
	    	, 'deptName'
	    ]
	});
	oStore.load({params:{sName:'', sDeptName:''}});
	var oSM = new Ext.grid.CheckboxSelectionModel();
	
	Ext.BgPost.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        loadMask : true,
        ddGroup: 'gridToTreeDDGroup',
        enableDragDrop: true,
        sm:oSM,
        columns: [
        	oSM
        	,{header:'序号', width: 60, fixed: true, menuDisabled: true
        		, renderer: function(value, metaData, record, rowIndex, colIndex, store){
        			return store.reader.jsonData.start + rowIndex + 1;
        		}
        	 }
            ,{header: '岗位名称', dataIndex: 'name', id:'post_name'}
            ,{header: '所属部门', dataIndex: 'deptName', width: 200}
        ],
        stripeRows: true,
        autoExpandColumn: 'post_name',
        border:false,
        tbar: new Ext.BgPostTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			oStore.setBaseParam('sName', me.sName || '');
        			oStore.setBaseParam('sDeptName', me.sDeptName || '');
        		}
            }
        })
	});
};

Ext.extend(Ext.BgPost, Ext.grid.GridPanel);
