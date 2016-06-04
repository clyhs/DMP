Ext.BLANK_IMAGE_URL = contextPath+'/res/images/common/default/s.gif';

var g_oViewPort = null;
Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
	g_oViewPort = new Ext.Viewport({
		layout: 'border',
		items: [new Ext.ReportMgr()]
	});
	 
});

Ext.override(Ext.form.RadioGroup, {
	setValue: function(v){
		this.eachItem(function(item){   
			item.setValue(item.getRawValue() == v);   
		});
	},
	getValue: function(){
		var value = 3;
		this.eachItem(function(item){
			if(item.checked){
				value = item.getRawValue();
				return false;
			}
		});

		return value;
	}
});