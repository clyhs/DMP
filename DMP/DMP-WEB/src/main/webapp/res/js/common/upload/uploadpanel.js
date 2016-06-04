Ext.ns('Ext.ux');

Ext.ux.UploadPanel = function(cfg)
{
	this.aUploadId = [];
	Ext.apply(this, cfg);
	var sFlashUrl = 'swfupload.swf';
	var aScript = document.getElementsByTagName('script');
	for(var i = 0, nLen = aScript.length; i < nLen; i++)
	{
		if(/uploadpanel\.js$/.test(aScript[i].src))
		{
			sFlashUrl = aScript[i].src.replace(/(uploadpanel\.js)$/gi, '') + sFlashUrl;
			break;
		}
	}
	this.setting = {
		upload_url : this.uploadUrl||"../../attachments/addCMFile.do", 
		flash_url : sFlashUrl,
		button_image_url : Ext.BLANK_IMAGE_URL,
		file_size_limit : this.fileSize || (1024*50) ,//上传文件体积上限，单位MB
		file_post_name : this.filePostName||"attachment",
		file_types : this.fileTypes||"*.*",  //允许上传的文件类型 
        file_types_description : this.fileDesc||"All Files",  //文件类型描述
        file_upload_limit : "0",  //限定用户一次性最多上传多少个文件，在上传过程中，该数字会累加，如果设置为“0”，则表示没有限制 
        //file_queue_limit : "10",//上传队列数量限制，该项通常不需设置，会根据file_upload_limit自动赋值              
		post_params : this.postParams||{},
		use_query_string : true,
		debug : false,
		button_cursor : SWFUpload.CURSOR.HAND,
		button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
		custom_settings : {//自定义参数
			scope_handler : this
		},
		file_queued_handler : this.onFileQueued,
		swfupload_loaded_handler : Ext.emptyFn,// 当Flash控件成功加载后触发的事件处理函数
		file_dialog_start_handler : Ext.emptyFn,// 当文件选取对话框弹出前出发的事件处理函数
		file_dialog_complete_handler : this.onDiaogComplete,//当文件选取对话框关闭后触发的事件处理
		upload_start_handler : this.onUploadStart,// 开始上传文件前触发的事件处理函数
		upload_success_handler : this.onUploadSuccess,// 文件上传成功后触发的事件处理函数 
		upload_progress_handler : this.uploadProgress,
		upload_complete_handler : this.onUploadComplete,
		upload_error_handler : this.onUploadError,
		file_queue_error_handler : this.onFileError
	};
	
	var oStore = new Ext.data.JsonStore({
	    autoDestroy: true,
	    proxy : new Ext.data.HttpProxy({
	        method: 'post',
	        url: this.dataUrl || '../attachments/getCMFile.do'
	    }),
	    root: 'data',
	    idProperty: 'name',
	    totalProperty: 'total',
	    fields: [ 
	          {name :'id', type:'int'}
	        , {name :'fileId', type:'int'}
	    	, 'sname'
	    	, 'sort'
	    	, {name:'size',type:'int'}
	    	, {name:'percent', type:'int'}
	    	, {name :'fileStatus', type:'int'}
	    ]
	});
	var oSM = new Ext.grid.CheckboxSelectionModel();
	
	Ext.ux.UploadPanel.superclass.constructor.call(this, {
		region:'center',
        store: oStore,
        loadMask : true,
        ddGroup: 'gridToTreeDDGroup',
        enableDragDrop: true,
        height: 320,
        sm:oSM,
        columns: [
        	oSM,
	        {header: '文件名', sortable: true,dataIndex: 'sname', id:'name'},
	        {header: '类型', width: 70, sortable: true,dataIndex: 'sort'},
	        {header: '大小', width: 70, sortable: true,dataIndex: 'size',renderer:this.formatFileSize},
	        {header: '进度', width: 150, sortable: true,dataIndex: 'percent',renderer:this.formatProgressBar,scope:this},
	        {header: '状态', width: 70, sortable: true,dataIndex: 'fileStatus',renderer:this.formatFileState,scope:this}
        ],
        stripeRows: true,
        autoExpandColumn: 'name',
        border:false,
		tbar : [
			{text:'添加文件',iconCls:'add',ref:'../addBtn'},'-',
			{text:'上传文件',ref:'../uploadBtn',iconCls:'save',handler:this.startUpload,scope:this},'-',
			//{text:'停止上传',ref:'../stopBtn',iconCls:'close',handler:this.stopUpload,scope:this,disabled:true},'-',
			{text:'选择删除',ref:'../deleteBtn',iconCls:'del',handler:this.deleteChoice,scope:this},'-',
			{text:'刷新列表',ref:'../refreshBtn',iconCls:'refresh',handler:this.refreshUpload,scope:this},'-',
			{text:'下载文件',ref:'../downloadBtn',iconCls:'preview',handler:this.downloadFile,scope:this}
		]
	});
};
Ext.extend(Ext.ux.UploadPanel, Ext.grid.GridPanel, {
	initComponent : function()
	{
		Ext.ux.UploadPanel.superclass.initComponent.call(this);
		this.addEvents('upload', 'remove', 'download');
	},
	getValue : function()
	{
		return this.aUploadId;
	},
	load : function(params)
	{
		this.aUploadId = [];
		this.getStore().load(params);
	},
	destroy:function()
	{
		this.swfupload.destroy();
	},
 	onUploadStart : function(file) 
 	{  
	   var post_params = this.settings.post_params;  
	   Ext.apply(post_params,
	   {//处理中文参数问题
	   		//fileName : file.name,
	        fileName : encodeURIComponent(file.name)
	   });  
	   this.setPostParams(post_params);  
	},
	startUpload : function() 
	{
		if (this.swfupload) 
		{
			if (this.swfupload.getStats().files_queued > 0) 
			{
				this.swfupload.uploadStopped = false;
				this.swfupload.startUpload();
			}
		}
	},
	formatFileSize : function(_v, celmeta, record) 
	{
		return Ext.util.Format.fileSize(_v);
	},
	formatFileState : function(n)
	{//文件状态
		switch(n){
			case -1 : return '未上传';
			break;
			case -2 : return '正在上传';
			break;
			case -3 : return '<div style="color:red;">上传失败</div>';
			break;
			case -4 : return '上传成功';
			break;
			case -5 : return '取消上传';
			break;
			default: return n;
		}
	},
	formatProgressBar : function(v)
	{
		return this.getTplStr(v);
	},
	getTplStr : function(v)
	{
		var bgColor = "orange";
	    var borderColor = "#008000";
		return String.format(
			'<div>'+
				'<div style="border:1px solid {0};height:10px;width:{1}px;margin:4px 0px 1px 0px;float:left;">'+		
					'<div style="float:left;background:{2};width:{3}%;height:10px;"><div></div></div>'+
				'</div>'+
			'<div style="text-align:center;float:right;width:40px;margin:3px 0px 1px 0px;height:10px;font-size:12px;">{3}%</div>'+			
		'</div>', borderColor,(90),bgColor, v);
	},
	onUploadComplete : function(file) 
	{
		var me = this.customSettings.scope_handler;
		if(file.filestatus==-4)
		{
			var ds = me.store;
			var fire = true;
			for(var i=0;i<ds.getCount();i++)
			{
				var record =ds.getAt(i);
				if(record.get('id')==file.id)
				{
					record.set('percent', 100);
					if(record.get('fileStatus')!=-3)
					{
						record.set('fileStatus', file.filestatus);
					}
					record.commit();
				}
				if(fire && record.get('fileStatus')!=-4) fire = false;
			}
			if(fire) me.fireEvent('upload', me, me.aUploadId);
		}
		
		if (this.getStats().files_queued > 0 && this.uploadStopped == false) 
		{
			this.startUpload();
		}	
	},
	onFileQueued : function(file) 
	{
		var me = this.customSettings.scope_handler;
		var rec = new Ext.data.Record({
			id : file.id,
			fileId : 0,
			sname : file.name,
			size : file.size,
			sort : file.type,
			fileStatus : file.filestatus,
			percent : 0
		});
		me.getStore().add(rec);
	},
	onUploadSuccess : function(file, serverData) 
	{
		var oResponse = Ext.util.JSON.decode(serverData);
		var me = this.customSettings.scope_handler;
		var ds = me.store;
		for(var i=0;i<ds.getCount();i++)
		{
			var rec =ds.getAt(i);
			if(rec.get('id')==file.id)
			{
				if (oResponse.success)
				{
					rec.set('fileStatus', file.filestatus);
					me.aUploadId.push(oResponse.param);
				}else
				{
					rec.set('percent', 0);
					rec.set('fileStatus', -3);
				}
				rec.commit();
			}
		}		
	},
	uploadProgress : function(file, bytesComplete, totalBytes)
	{//处理进度条
		var me = this.customSettings.scope_handler;
		var percent = Math.ceil((bytesComplete / totalBytes) * 100);
		percent = percent == 100? 99 : percent;
       	var ds = me.store;
		for(var i=0;i<ds.getCount();i++)
		{
			var record =ds.getAt(i);
			if(record.get('id')==file.id)
			{
				record.set('percent', percent);
				record.set('fileStatus', file.filestatus);
				record.commit();
			}
		}
	},
	onUploadError : function(file, errorCode, message) 
	{
		var me = this.customSettings.scope_handler;
		var ds = me.store;
		for(var i=0;i<ds.getCount();i++)
		{
			var rec =ds.getAt(i);
			if(rec.get('id')==file.id)
			{
				rec.set('percent', 0);
				rec.set('fileStatus', file.filestatus);
				rec.commit();
			}
		}
	},
	onFileError : function(file,n)
	{
		switch(n)
		{
			case -100 : tip('待上传文件列表数量超限，不能选择！');
			break;
			case -110 : tip('文件太大，不能选择！');
			break;
			case -120 : tip('该文件大小为0，不能选择！');
			break;
			case -130 : tip('该文件类型不可以上传！');
			break;
		}
		function tip(msg)
		{
			Ext.Msg.show({
				title : '温馨提示',
				msg : msg,
				width : 280,
				icon : Ext.Msg.WARNING,
				buttons :Ext.Msg.OK
			});
		}
	},
	onDiaogComplete : function()
	{
	},
	stopUpload : function() 
	{
		if (this.swfupload) 
		{
			this.swfupload.uploadStopped = true;
			this.swfupload.stopUpload();
		}
	},
	refreshUpload : function()
	{
		this.getStore().reload();
	},
	deleteChoice : function()
	{
		var me = this;
		var aRecord = this.getSelectionModel().getSelections();
		if(aRecord.length == 0)
		{
			Ext.MessageBox.alert('温馨提示', '请选择要删除的文件!');
		}else
		{
			Ext.MessageBox.confirm('温馨提示', '真的要删除选择的文件吗？', function(buttonId, text, opt)
			{
				if(buttonId == 'yes')
				{
					me.delRecord(aRecord);
				}
			});
		}
	},
	downloadFile : function()
	{
		var oRecord = this.getSelectionModel().getSelected();
		if(oRecord != undefined)
		{
			this.fireEvent('download', this, oRecord.get('fileId'));
		}
	},
	deleteAll : function()
	{
		var oStore = this.getStore();
		var aRecord = oStore.getRange();
		this.delRecord(aRecord);
	},
	delRecord : function(aRecord)
	{
		aRecord = aRecord || [];
		if(aRecord.length > 0)
		{
			var aDelId = [];
			for(var i=0, nLen = aRecord.length; i<nLen; i++)
			{
				this.swfupload.cancelUpload(aRecord[i].get('id'), false);
				if(aRecord[i].get('fileId') != 0) aDelId.push(aRecord[i].get('fileId'));
			}
			this.store.remove(aRecord);
			this.swfupload.uploadStopped = false;
			if(aDelId.length > 0) this.fireEvent('remove', this, aDelId.join(','));
		}
	},
	afterRender : function()
	{
		Ext.ux.UploadPanel.superclass.afterRender.call(this);

		var em = this.getTopToolbar().get(0).el.child('em');
		var placeHolderId = Ext.id();
		em.setStyle({
			position : 'relative',
			display : 'block'
		});
		var child = em.createChild({tag:'div'});
		child.createChild({
			tag : 'div',
			id : placeHolderId
		});
		this.swfupload = new SWFUpload(Ext.apply(this.setting,{
			button_width : 65,
			button_height : 16,
			button_placeholder_id :placeHolderId
		}));
		this.swfupload.uploadStopped = false;
		Ext.get(this.swfupload.movieName).setStyle({
			position : 'absolute',
			top : 0,
			left : -2
		});				
	}
});
Ext.reg('uploadpanel', Ext.ux.UploadPanel);
