Ext.BgUser = function()
{
	var me = this;
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: 'getBgUserList.json'
	    }),
	    root: 'data',
	    idProperty: 'bgUserId',
	    totalProperty: 'total',
	    fields: [ 
	          {name :'bgUserId', type:'int'}
	    	, 'account'
	    	, 'name'
	    	, 'mobile'
	    	, {name:'sex',type:'int'}
	    	, {name:'isValid', type:'int'}
	    	, {name :'createDate', type:'date', dateFormat: 'time'}
	    ]
	});
	oStore.load({params:{sName:'', sAccount:'', nBgRoleId:0}});
	var oSM = new Ext.grid.CheckboxSelectionModel();
	
	Ext.BgUser.superclass.constructor.call(this, {
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
            ,{header: '用户帐号', dataIndex: 'account', width: 140}
            ,{header: '用户姓名', dataIndex: 'name', id:'name'}
            ,{header: '用户性别', dataIndex: 'sex', width: 80
            	, renderer : function(value){
            		switch(value)
            		{
            			case 1 :
            				return '男';
            			break;
            			case 2 :
            				return '女';
            			break;
            			default:
            				return '未知';
            			break;
            		}
            	}
            },
            {header: '是否有效', dataIndex: 'isValid', width: 80
            	, renderer : function(value){
            		switch(value)
            		{
            			case 1 :
            				return '是';
            			break;
            			case 2 :
            				return '否';
            			break;
            		}
            	}
            }
            ,{header: '手机号码', dataIndex: 'mobile', width: 140}
            ,{header: '录入时间', dataIndex: 'createDate', width: 140, sortable: true, renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s')}
        ],
        stripeRows: true,
        autoExpandColumn: 'name',
        border:false,
        tbar: new Ext.BgUserTopToolbar(this),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: oStore,
            displayInfo: true,
            listeners:{
        		beforechange : function()
        		{
        			var oNode = g_oViewPort.items.get(1).getSelectionModel().getSelectedNode();
		 			var nBgRoleId = oNode ? oNode.id.replace(/[^0-9]*/,'') : 0;
        			oStore.setBaseParam('sName', me.sName || '');
        			oStore.setBaseParam('sAccount', me.sAccount || '');
        			oStore.setBaseParam('nBgRoleId', nBgRoleId);
        		}
            }
        })
	});
};

Ext.extend(Ext.BgUser, Ext.grid.GridPanel);
