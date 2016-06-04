Ext.BLANK_IMAGE_URL = contextPath+'/res/images/common/default/s.gif';

var g_oViewPort = null;
Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
	g_oViewPort = new Ext.Viewport({
		layout: 'border',
		items: [new Ext.BgNav()]
	});
});