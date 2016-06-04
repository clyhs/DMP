Ext.TaskTopToolbar = function(oGrid) {
	Ext.TaskTopToolbar.superclass.constructor.call(this, {
		enableOverflow : true,
		items : [
        {
	        xtype : 'tbtext',
	        text: '任务名称'
        },{
			xtype: 'textfield',
			width: 140,
			id : 'name'
		},{
			xtype: 'tbspacer', 
			width: 10
	    },{
	    	text: '搜索',
	    	iconCls: 'search',
	    	id: 'query',
	    	//hidden: true,
	        handler: function(oButton, oEvent)
	        {
     			var name = Ext.getCmp('name').getRawValue();
     			oGrid.name = name;
     			oGrid.getStore().load({params:{name: name}});
     		 }
     	},{
			xtype : 'tbspacer',
			width : 10
		},{
            text: '查看',
            iconCls: 'look',
            id: 'view',
            //hidden: true,
            handler: function(oButton, oEvent)
            {
            	var aRecord = oGrid.getSelectionModel().getSelections();
            	if(aRecord.length > 1)
            	{
            		Ext.MessageBox.alert('温馨提示', '请选择要查看的一行数据!');
            		return ;
            	}
    			var oRecord = oGrid.getSelectionModel().getSelected();
    			if(oRecord != undefined)
    			{
    				var task = oRecord.data;
        			oButton.Window = new Ext.ViewTaskWindow(task);
        			oButton.Window.show();
    			}else
    			{
    				Ext.MessageBox.alert('温馨提示', '请选择要查看的数据!');
    			}
			 }
    	},{
    		xtype: 'tbspacer', width: 10
        },{
           text: '增加',
           iconCls: 'add',
           id: 'add',
          // hidden: true,
           handler: function(oButton, oEvent)
           {
   				oButton.Window = new Ext.AddTaskWindow();
   				oButton.Window.show();
			}
   	    },{
   	    	xtype: 'tbspacer', width: 10
   	    },{
           text: '修改',
           iconCls: 'edit_icon',
           id: 'edit',
          // hidden: true,
           handler: function(oButton, oEvent)
           {
         	 var aRecord = oGrid.getSelectionModel().getSelections();
         	 if(aRecord.length > 1)
         	 {
         		Ext.MessageBox.alert('温馨提示', '请选择要修改的一行数据!');
         		return ;
         	 }
 			 var oRecord = oGrid.getSelectionModel().getSelected();
 			 if(oRecord != undefined)
 			 {
     				var task = oRecord.data;
     				oButton.Window = new Ext.EditTaskWindow(task);
     				oButton.Window.show();
 			 }else
 			 {
 				Ext.MessageBox.alert('温馨提示', '请选择要修改的数据!');
 			 }
			}
 	    },{
 	    	xtype: 'tbspacer', width: 10
 	    },{
    		text: '删除',
    		id: 'delete',
	            //hidden: true,
    		iconCls: 'del',
            handler: function(oButton, oEvent){
            	var aRecord = oGrid.getSelectionModel().getSelections();
    			if(aRecord.length > 0)
	    		{
    				Ext.MessageBox.confirm('温馨提示', '真的要删除选择的数据吗？', function(buttonId, text, opt)
    				{
    					if(buttonId == 'yes')
    					{
    						var aTaskId = [];
    						for(var i = 0;i<aRecord.length;i++)
    						{
    							aTaskId.push("'"+aRecord[i].data.taskCode+"'");
    						}

    						Ext.Ajax.request({   
    							   url: 'delTask.json',   
    							   method: 'post',   
    							   params: {sTaskIdList: aTaskId.join(',')},   
    							   success: function(resp, action)
    							   {   
    							       var obj = Ext.util.JSON.decode(resp.responseText);   
    							       if(obj.success)   
    							       {    
    							    	   oGrid.getStore().reload();
    							        }   
    							        else  
    							        {   
    							            Ext.Msg.alert('温馨提示', obj.msg);   
    							        }     
    							    },   
    							    failure:function(resp, action)
    							    {   
    							        Ext.Msg.alert('温馨提示','系统错误');   
    							    }   
    						}); 
    					}
    				});		    			
	    		}else
    			{
    				Ext.MessageBox.alert('温馨提示', '请选择要删除的数据!');
    				
    			}
			}
    	},{
			xtype : 'tbspacer',
			width : 10
		},'->',{
			text : '帮助',
			iconCls : 'help'
		}]
	});
};

Ext.extend(Ext.TaskTopToolbar, Ext.Toolbar);
