Ext.Download = Ext.extend(Ext.util.Observable, 
{
	iframeId : 'x_download_iframe_id',
	formId : 'x_download_form_id',
	baseForm : undefined,
	constructor : function(config)
	{
		config = config || {};
		Ext.apply(this, config);
		Ext.Download.superclass.constructor.call(this, config);
		this.init();
	},
	
	init : function()
	{
		this.iframe = Ext.get(this.iframeId) || undefined;
		this.baseForm = Ext.get(this.formId) || undefined;
		if(!this.iframe)
		{
			var iframeName = 'iframe_'+Ext.id();
			var body = Ext.getBody();
			this.iframe = body.createChild({
	            tag:'iframe',
	            cls:'x-hidden',
				id:this.iframeId,
	            name:iframeName
	        })
	        
	        this.baseForm = body.createChild({
	            tag:'form',
	            cls:'x-hidden',
				method:'post',
				id:this.formId,
	            target:iframeName
	        });
		}
	},
	
	submit : function(url, params)
	{
		if(!this.baseForm) return;

        var items = this.baseForm.select('input.x-hidden');
		items.each(function(f)
		{
			Ext.destroy(f);            
		});

		params = params || {};
		for(var key in params)
		{
			this.baseForm.createChild({
	            tag:'input',
	            type:'text',
	            cls:'x-hidden',
	            name: key,
	            value: params[key]
	        });
		}
		
		this.baseForm.dom.action = url;
		this.baseForm.dom.submit();
	}
	
});

