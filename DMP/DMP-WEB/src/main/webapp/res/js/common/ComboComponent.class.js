if("undefined" == typeof tokenId || tokenId == null){
	ymRange = parent.g_oViewPort.items.get(1).ymRange;   //获取年月多选时月份范围
	portalGroup = parent.g_oViewPort.items.get(1).portalGroup;   //获取是否显示全部门户
}

/*门户类型combobox*/
var portalTypeStore = new Ext.data.JsonStore({
    url : contextPath+'/module/admin/common/getPortalTypeComboBox.json?portalGroup=' + portalGroup,
    fields : [ 'portalId', 'portalName' ],
    root : 'arr'
});
portalTypeStore.load();

var PortalTypeCombo =  Ext.extend(Ext.form.ComboBox,{
    typeAhead: true,
    triggerAction: 'all',
    forceSelection: true,
    mode: 'local',
    width:80,
    emptyText:'请选择',
    store: portalTypeStore,
    valueField: 'portalId',
    displayField: 'portalName',
    constructor : function(){
        PortalTypeCombo.superclass.constructor.apply(this, arguments);
    },
    onDestroy : function() {
        PortalTypeCombo.superclass.onDestroy.apply(this, arguments);
    }
});
Ext.reg('portalTypeCombo', PortalTypeCombo);


//支持单选及多选的年月控件
var ymStore = new Ext.data.JsonStore({
    url : contextPath+'/module/admin/common/getYMComboBox.json?ymRange='+ymRange,
    fields : [ 'ymId', 'ymName' ],
    root : 'arr'
});
ymStore.load();

var YMCheckCombo =  Ext.extend(Ext.form.ComboBox,{
    typeAhead: true,
    triggerAction: 'all',
//    forceSelection: true,
    mode: 'local',
    width:120,
    emptyText:'请选择',
    store: ymStore,
    valueField: 'ymId',
    displayField: 'ymName',
    tpl:'<tpl for="."><div class="x-combo-list-item"><span><input type="checkbox" {[values.check?"checked":""]}  value="{[values.ymName]}" /></span><span >&nbsp;&nbsp;{ymName}</span></div></tpl>', 
	selectOnFocus: true,
    constructor : function(){
    	YMCheckCombo.superclass.constructor.apply(this, arguments);
    },
    onDestroy : function() {
    	YMCheckCombo.superclass.onDestroy.apply(this, arguments);
    },
	onSelect: function(record, index){
	     if(this.fireEvent('beforeselect', this, record, index) !== false){
            record.set('check',!record.get('check'));  
            var str=[];//页面显示的值  
            var strvalue=[];//传入后台的值  
            this.store.each(function(rc){  
                if(rc.get('check')){  
                    str.push(rc.get('ymName'));  
                    strvalue.push(rc.get('ymId'));  
                }  
            });  
            this.setValue(str.join());  
            this.value=strvalue.join();  
            //this.collapse();  
            this.fireEvent('select', this, record, index);  
        }  
    },
    listeners:{
    	 afterrender: function(combo) {
    		 setTimeout(function(){
    			 var rc = ymStore.getAt(0);
    			 rc.set('check',true);
    			 var tmp = [];
    			 tmp.push(rc.get("ymId"));
    			 combo.setValue(tmp.join());
    			 combo.value = tmp.join();
             },200);
	      }  
    }
});
Ext.reg('ymCheckCombo', YMCheckCombo);

