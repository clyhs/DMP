Ext.BLANK_IMAGE_URL = contextPath+'/res/images/common/default/s.gif';

var g_oViewPort = null;
Ext.onReady(function(){
	
	Ext.QuickTips.init();
	var jobName = this.name;
	g_oViewPort = new Ext.Viewport({
		layout: 'border',
		items: [new Ext.MonitorData(jobName)]
	});
});
