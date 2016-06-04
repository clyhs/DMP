Ext.SchedulerTopToolbar = function(oGrid)
{
	var me = this;	
	Ext.SchedulerTopToolbar.superclass.constructor.call(this, 
	{
        enableOverflow: true,
        items: [
            {xtype: 'tbspacer', width: 10}
        	,{
	            text: '新增',
	            id: 'add',
	            iconCls: 'add',
	            handler: function(oButton, oEvent)
	            {
	            
	            	var win = new parent.Ext.AddEditSchedulerWindow();
        			win.show();
	            }
        	   }
        	 ,{xtype: 'tbspacer', width: 10}
        	 ,{
	            text: '修改',
	            id: 'edit',
	            iconCls: 'edit_icon',
	            handler: function(oButton, oEvent)
	            {
	            	var aRecord = oGrid.getSelectionModel().getSelections();
	            	if(aRecord.length > 1)
	            	{
	            		Ext.MessageBox.alert('温馨提示', '请选择要修改的一个调度任务！');
	            		return ;
	            	}
        			var oRecord = oGrid.getSelectionModel().getSelected();
	    			if(oRecord != undefined)
	    			{
	    				var win = new parent.Ext.AddEditSchedulerWindow(true);
        				var jobName = oRecord.data.jobName;
        				parent.Ext.getCmp('schedulerPanel').getForm().load({
        				    url: contextPath+'/module/admin/quartz/getScheduler.json', 
                            method: 'POST', 
                            params: {
                               	jobName: jobName
                            },
                            waitTitle: '温馨提示',
                            waitMsg: '数据正在加载中，请稍后。。。',
                            success: function(form,action) {
                               	formData(form,action);       //form的一些数值处理
                               	win.show();
                               	win.setTitle('修改调度');
                               	showFieldSet(form, action);
                               	oGrid.getSelectionModel().clearSelections();
                             },
                            failure: function(form,action) {
                               	Ext.Msg.alert('温馨提示','系统出错');
                             }
        				})
	    			}else
	    			{
	    				Ext.MessageBox.alert('温馨提示', '请选择要修改的调度任务！');
	    			}
				}
        	   },{xtype: 'tbspacer', width: 10},
        	{
	            text: '删除',
	            id: 'delete',
	            iconCls: 'del',
	            handler: function(oButton, oEvent)
	            {
		            var aRecord = oGrid.getSelectionModel().getSelections();
	            	if(aRecord.length > 0)
	            	{
	            		var jobNameArray = [];
	   				    for(var i = 0;i < aRecord.length; i++)
	   					{
	   						jobNameArray.push(aRecord[i].data.jobName);
	   					}
	            		Ext.Ajax.request({ 
	   						url: contextPath+'/module/admin/quartz/deleteSchedulerList.json',   
   						    method: 'post',   
	   						params: {jobNameArray: Ext.encode(jobNameArray)},   
	   						success: function(resp, action)
	   						{
	   						    var obj = Ext.util.JSON.decode(resp.responseText);
	   						    oGrid.getSelectionModel().clearSelections();
	   						    Ext.Msg.alert('温馨提示','任务已经删除');
	   					    },
	   						failure:function(resp, action)
  						    {   
   						        Ext.Msg.alert('温馨提示','系统错误');   
   						    }   
	   						}); 
	            	}else
	            	{
	            		Ext.Msg.alert('温馨提示','请选择要删除的任务');
	            	}
						
				}
        	},{xtype: 'tbspacer', width: 10},
        	{
        		text: '彻底删除',
        		id: 'sheerdel',
        		iconCls: 'close',
	            handler: function(oButton, oEvent)
	            {
		            var aRecord = oGrid.getSelectionModel().getSelections();
		            if(aRecord.length > 0)
	            	{
	            		Ext.MessageBox.confirm('温馨提示', '真的要删除选择的调度任务吗？', function(buttonId, text, opt)
	            		{
	            			if(buttonId == 'yes')
	    					{
	    						var jobNameArray = [];
    				            for(var i = 0;i < aRecord.length; i++)
    					        {
    					            jobNameArray.push(aRecord[i].data.jobName);
     					        }
	            		        Ext.Ajax.request({ 
	            		            url: contextPath+'/module/admin/quartz/completedeleteSchedulerList.json',   
							        method: 'post',   
	    					        params: {jobNameArray: Ext.encode(jobNameArray)},   
	    					        success: function(resp, action)
	    					        {
	    					             var obj = Ext.util.JSON.decode(resp.responseText);  
	    					             if (obj.success) 
	    					             {
	    					                Ext.MessageBox.alert('温馨提示', obj.msg,
										      function() {
	    					                	oGrid.getSelectionModel().clearSelections();
											    oGrid.getStore().reload();
										      });
								         } else 
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
		            	Ext.Msg.alert('温馨提示','请选择要删除的任务');
		            }
	            }
        	},{xtype: 'tbspacer', width: 10}
	       	 ,{
	            text: '暂停',
	            id: 'stop',
	            iconCls: 'pause',
	            handler: function(oButton, oEvent)
	            {
					var aRecord = oGrid.getSelectionModel().getSelections();
					if (aRecord.length > 0) {
						var jobNameArray = [];
						for (var i = 0; i < aRecord.length; i++) {
							jobNameArray.push(aRecord[i].data.jobName);
						}
						Ext.Ajax.request({
							url : contextPath+'/module/admin/quartz/pauseSchedulerList.json',
							method : 'post',
							params : { jobNameArray : Ext.encode(jobNameArray)},
						    success : function(resp, action) {
						    	var obj = Ext.util.JSON.decode(resp.responseText);
						    	oGrid.getSelectionModel().clearSelections();
						    	Ext.Msg.alert('温馨提示', '任务已经暂停');
							},
						failure : function(resp, action) {
							Ext.Msg.alert('温馨提示', '系统错误');
						}
						});
					} else {
						Ext.Msg.alert('温馨提示', '请选择要运行的任务');
					}
					}
	     	  }
	       	 ,{xtype: 'tbspacer', width: 10}
	       	 ,{
		            text: '恢复',
		            id: 'recover',
		            iconCls: 'resume',
		            handler: function(oButton, oEvent)
		            {
		            	var aRecord = oGrid.getSelectionModel().getSelections();
		            	if(aRecord.length > 0)
		            	{
		            		var jobNameArray = [];
	    				    for(var i = 0;i < aRecord.length; i++)
	    					{
	    						jobNameArray.push(aRecord[i].data.jobName);
	    					}
		            		Ext.Ajax.request({ 
	    						url: contextPath+'/module/admin/quartz/resumeSchedulerList.json',   
   							    method: 'post',   
	    						params: {jobNameArray: Ext.encode(jobNameArray)},   
	    						success: function(resp, action)
	    						{
	    						    var obj = Ext.util.JSON.decode(resp.responseText); 
	    						    oGrid.getSelectionModel().clearSelections();
	    						    Ext.Msg.alert('温馨提示','任务已经恢复'); 
	    					    },
	    						failure:function(resp, action)
  							    {   
   							        Ext.Msg.alert('温馨提示','系统错误');   
   							    }   
	    						}); 
		            	}else
		            	{
		            		Ext.Msg.alert('温馨提示','请选择要恢复的任务');
		            	}
					}
		     }
	       	 ,{xtype: 'tbspacer', width: 10}
	       	 ,{
		            text: '运行',
		            id: 'start',
		            iconCls: 'run',
		            handler: function(oButton, oEvent)
		            {
		            	var aRecord = oGrid.getSelectionModel().getSelections();
		            	if(aRecord.length > 0)
		            	{
		            		var jobNameArray = [];
	    				    for(var i = 0;i < aRecord.length; i++)
	    					{
	    						jobNameArray.push(aRecord[i].data.jobName);
	    					}
		            		Ext.Ajax.request({ 
	    						url: contextPath+'/module/admin/quartz/runSchedulerList.json',   
   							    method: 'post',   
	    						params: {jobNameArray: Ext.encode(jobNameArray)},   
	    						success: function(resp, action)
	    						{
	    						    var obj = Ext.util.JSON.decode(resp.responseText);
	    						    oGrid.getSelectionModel().clearSelections();
	    						    Ext.Msg.alert('温馨提示','任务已经开始运行');
	    					    },
	    						failure:function(resp, action)
  							    {   
   							        Ext.Msg.alert('温馨提示','系统错误');   
   							    }   
	    						}); 
		            	}else
		            	{
		            		Ext.Msg.alert('温馨提示','请选择要运行的任务');
		            	}
	     				
					}
		     }
	       	 ,{xtype: 'tbspacer', width: 10}
	       	 ,{
		            text: '刷新',
		            id: 'refresh',
		            iconCls: 'refresh',
		            handler: function(oButton, oEvent)
		            {
		            	oGrid.getSelectionModel().clearSelections();
		            	oGrid.getStore().load();
					}
		     },{xtype: 'tbspacer', width: 10},
		     {
	            text: 'Kettle文件上传',
	            id: 'fileUp',
	            iconCls: 'fileUp',
	            handler: function(oButton, oEvent)
	            {
		            new Ext.Window({
					width : 650,
					title : 'Kettle文件上传',
					height : 300,
					layout : 'fit',
					modal : true,
					items : [
						{
							xtype:'uploadPanel',
							border : false,
							fileSize : 1024*550,//限制文件大小
							uploadUrl : contextPath+'/module/admin/quartz/uploadfile.html',
							flashUrl : contextPath+'/res/js/common/upload/swfupload.swf',
							filePostName : 'file', //后台接收参数
							fileTypes : '*.kjb;*.ktr',//可上传文件类型
							postParams : {filePath:''} //上传文件存放目录
						}
					]
					}).show();
	            }
        	   },
        	'->',
        	{
        		text: '帮助',
        		iconCls: 'help'
        	}
        ]
	});
};

Ext.extend(Ext.SchedulerTopToolbar, Ext.Toolbar);

function formData(form,action)
{
  var backData = Ext.util.JSON.decode(action.response.responseText);
  //文件路径的重写
  actionPath = backData.data.actionPath;
  var filePath = "";
  if(actionPath.split("/").length > 1){
	  var folders = actionPath.split("/.kettle")[1].split("/");
	  for(var i=1;i<folders; i++)
	  {
		  filePath = folders[i] + "/" + filePath;
	  }
  }else{
	  var folders = actionPath.split("\\.kettle")[1].split("\\");
	  for(var i=1;i<folders.length;i++){
		  filePath = folders[i] + "/" + filePath;
	  }
  }
  parent.Ext.getCmp('actionPath').setValue(filePath + backData.data.actionRef+"." +backData.data.fileType);
  return;
}
function showFieldSet(form, action)
{
	var backData = Ext.util.JSON.decode(action.response.responseText).data;
	var cycle = backData.cycle;
	if(cycle == 1){ return; }
	//parent.Ext.getCmp('fieldSet').setDisabled(false);
	//parent.Ext.getCmp('fieldSet').expand(true);
	parent.Ext.getCmp('fieldSet').show();
	parent.Ext.getCmp('overTime').show();
	parent.Ext.getCmp("model_"+cycle).show();
	beforeIndex = cycle;
	if(backData.haveEndDate == 0){
		parent.Ext.getCmp('enddateRadio1').setValue(true);
	}else{
		parent.Ext.getCmp('enddateRadio2').setValue(true);
		parent.Ext.getCmp('enddate').setValue(new Date(backData.endDate).format('Y-m-d'));
	}
	if(cycle ==2){
		parent.document.getElementById('secendcyclenum').value = backData.cycleNum;
	    return ;
	}
	if(cycle == 3)
	{
		parent.document.getElementById('minitecyclenum').value = backData.cycleNum;	
		return ;
	}
	if(cycle == 4)
	{
		parent.document.getElementById('hourcyclenum').value = backData.cycleNum;
		return ;
	}
	if(cycle == 5)
	{
		if(backData.dayType == 0)
		{
			parent.document.getElementById('daytyperadio1').checked = true;
			parent.document.getElementById('daycyclenum').value = backData.cycleNum;
		}else{
			parent.changeDayType(1);
			parent.document.getElementById('daytyperadio2').checked = true;
		}
		return ;
	}
	if(cycle == 6)
	{
		var weekItems = parent.Ext.getCmp('model_6');
		var checkWeeks =  backData.cycleNum.split(",");
		for(var i=0; i< checkWeeks.length; i++)
		{
			weekItems.setValue("week"+checkWeeks[i],true);
		}
		return ;
	}
	if(cycle == 7)
	{
		if(backData.monthType ==0){ 
			parent.document.getElementById('monthcyclenum').value = backData.cycleNum;
		}
		else{ 
			parent.changeMonthType(1);
			parent.document.getElementById('monthtyperadio2').checked = true;
			parent.document.getElementById('monthdaynum').value = backData.dayNum;
			parent.document.getElementById('monthweeknum').value = backData.weekNum;
		}
		return;
	}
	if(cycle == 8)
	{
		var yeardatepanel = new parent.Ext.form.DateField({
            format: 'm-d',
            id: 'yeardatepanel',
            name: 'notuse',
            renderTo: 'yeardate',
            width: 90,
            value: new Date()
       });
		parent.Ext.getCmp('schedulerwin').firstCome = -1;
		if(backData.yearType == 0){
			var cycleNum = backData.cycleNum.split("-");
			parent.Ext.getCmp('yeardatepanel').setValue(new Date(13,parseInt(cycleNum[0],10)-1,parseInt(cycleNum[1],10)));
		}else{
			parent.changeYearType(1);
			parent.document.getElementById('yeartyperadio2').checked = true;
			parent.document.getElementById('yeardaynum').value = backData.dayNum;
			parent.document.getElementById('yearweeknum').value = backData.weekNum;
			parent.document.getElementById('yearmonthnum').value = backData.monthNum;
		}
		return; 
	}
}