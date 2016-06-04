Ext.DynamicToolbar = Ext.extend(Ext.Toolbar,{
	initComponent: function(){
		var config = {
			enableOverflow : true
		}
		Ext.apply(this, config);
		Ext.apply(this.initialConfig, config);
		Ext.DynamicToolbar.superclass.initComponent.apply(this, arguments);
	},
	//此方法用于判断输入的数据是否合法和拼接要传到后台的参数params
	createParams: function(items){
		var s = "";
		var itemValue = "";
		var itemRawValue = "";
		var y = true;
		//判断控件类型,itype是自己定义的,用来区分是否是下拉框
        Ext.each(items, function(item){
			if(item.itype == "combobox" || item.xtype == "textfield" || 
			   item.xtype == "datetimefield" || item.xtype == "datefield"){
				itemRawValue = item.getRawValue();
				itemValue = item.getValue();
				if(item.itype == "combobox"){					
					//top控件要特殊处理
					if(item.id == 'top'){
						if(itemValue == ""){
						   itemValue = 50;
						}
						if(itemRawValue == "全部"){
						   itemRawValue = "";
						}
					}else if(item.id == 'cpIncome'){
						if(itemValue == "" || itemValue == 0){
							   itemValue = 0;
							   itemRawValue = 0;
						}
					}else{
						if(itemRawValue == "全部"){
							itemRawValue = "";
							itemValue = "";	
						}
					}
				}
				//包含省份/地市控件时，分别获取省份及地市信息
				if(item.xtype == 'provinceAreaCombo'){
					s = s + "areaMultiSelect: 'proArea',";
					var provinceArea = getProvinceArea(itemRawValue);
					if(containsStr(provinceArea.proStr, "全国")){
						itemValue = '3';
						itemRawValue = '%';
						s = s + "area: '%',";
					}else if(provinceArea.proStr == ""){
						itemValue = '2';
						itemRawValue = '%';
						s = s + "area: '%',";
					}else{
						itemRawValue = provinceArea.proStr == null ? null : provinceArea.proStr;
						var areaStr = provinceArea.areaStr == null ? null : provinceArea.areaStr;;
						s = s + "area: '" + areaStr +"',";
						
					}
				}
				//对结算账期多选进行特殊标识
				if(item.xtype == 'ymCheckCombo'){
					s = s + "ymMultiSelect: 'ymMulti',";
				}
				if(item.xtype == 'cpCheckCombo'){
					s = s + "cpMultiSelect: 'cpMulti',";
				}
				if(item.xtype == 'longNumberCheckCombo'){
					s = s + "longNumMultiSelect: 'longNumMulti',";
				}
				if(item.xtype == "datefield" && itemRawValue == ""){
					Ext.Msg.alert('温馨提示', '请输入时间');
					y = false;
				}
				if(item.xtype == "datetimefield" && itemRawValue == ""){
					Ext.Msg.alert('温馨提示', '请输入时间');
					y = false;
				}
				if(item.xtype == "ymCheckCombo" && itemRawValue == ""){
					Ext.Msg.alert('温馨提示', '请选择结算账期');
					y = false;
				}
				//拼接参数,传getRowValue和getValue到后台如(province:'广东',provinceId:'20')
				s = s + item.id + ":'" + itemRawValue + "'," + item.id + "Id:'" + itemValue + "',";
			}
		});
		var params =Ext.decode("{" + s + "flag:'ttt'}");
		//如果有时间控件就判断时间是否正确
		if(params.startTime != undefined && params.endTime != undefined){
			 if(params.startTime > params.endTime)
             {
                Ext.MessageBox.alert('温馨提示', '结束时间必须大于开始时间！');
                y = false;
              }
		}
		this.params = params;
		return y;
	},
	tbarItems: [],
	summary: null,
	//插入控件到toolbar, json为要插入控件的json格式, tbar为要放置控件的tbar
	insertComponent: function(json, tbar){ 
		var me = this;
		var nowDate = new Date();
		var startDate = new Date(nowDate.getFullYear(),nowDate.getMonth(),nowDate.getDate()-1);
		//循环显示Toobar上的按钮
		Ext.each(json,function(v){
			//图表按钮
			if(v.event == 'chart'){
				tbar.insert(0, {
					xtype : 'tbspacer',
					width : 10
				});
				tbar.insert(0, {
					text: v.eventName,
					id: v.event,
					iconCls:'chart',
					frame:false,
					handler : function(oButton,oEvent){
						var items = me.tbarItems;
						//判断查询条件是否合法,合法再显示图表
					    if(me.createParams(items)){
					    	var oIFrameComponent = new parent.Ext.IFrameComponent({
					    		autoScroll:false,
							    name:'chartIframe',
							    url: v.chartUrl
						    });
					    	var reportTitle = parent.g_oViewPort.items.get(2).activeTab.title;
					    	//图表标题
					    	var sTitle = "<font size ='3' >" + reportTitle + "</font>";
					    	//特殊处理cp结算按天统计图表
					    	if(v.reportId =='CPSETTLEBYDAY'){
					    		var comValue = Ext.getCmp('settleTime').getRawValue();
					    		sTitle = sTitle +"&nbsp;&nbsp;&nbsp;时间："+comValue;
					    	}
		                    var win = new parent.Ext.Window({
		                	    layout: 'fit',
		                	    maximizable: true,
		                        plain: true,
		                        width:900,
		                        height:490,
		                        border:false,
		                        title:sTitle,
		                        modal: false
		                  });
		                  win.add(oIFrameComponent);
		                  win.show();
					  }
					}
			    });	
			 }
			//导出Excel按钮
			if(v.event == 'export'){
				tbar.insert(0, {
					xtype : 'tbspacer',
					width : 10
				});
				tbar.insert(0, {
					text : v.eventName,
					id: v.event,
					iconCls : 'excel',
					handler : function(oButton, oEvent) {
						var items = me.tbarItems;
						if(me.createParams(items)){
						   var download = new Ext.Download();
						   download.submit(me.oGrid.exportUrl, me.params);
						}
				     }
				  });
			 }
			//查询按钮
			if(v.event == 'query'){
				tbar.insert(0, {
					xtype : 'tbspacer',
					width : 10
				});
				tbar.insert(0, {
					text : v.eventName,
					id: v.event,
					iconCls : 'search',
					handler : function(oButton, oEvent) {
						var items = me.tbarItems;
						if(me.createParams(items)){
							//点查询的时候保存查询参数到gridpanel的params,供翻页控件使用
							me.oGrid.params = me.params;
							me.oGrid.getStore().load({
								params : me.params
							});
							//如果报表里有合计就加上合计
							if(me.summary != null){
								Ext.Ajax.request({
									url: me.oGrid.totalUrl,
									method:'post',
									params: me.params,
									success:function(resp, action){
										var obj = Ext.util.JSON.decode(resp.responseText).msg;
										me.summary.setSumValue(Ext.decode(obj));
									},
									failure:function(resp, action){
										Ext.MessageBox.alert('温馨提示', '请求失败！');
									}
								});
							}
						};
					 }
				 });
			  }
		});
		 //控件文字的长度
		var fontWidth = "";
		//控件的长度
		var componWidth = "";
		//控件名称上的间距
		var spacerWidth = 10;
		//循环显示Toobar上的控件和Grid下面的合计
		Ext.each(json,function(v){
			//合计组件,如果有合计的报表,这里先初始化合计组件
			if(v.componentType == "total"){
				me.summary = new Ext.ux.grid.GridSummary();
				tbar.oGrid.plugins = tbar.oGrid.initPlugin(me.summary);  
				me.summary.refreshSummary();
			}
			//文本框控件
			if(v.componentType == "textfield"){
				fontWidth = v.conditionName.length*12.5;
				//特殊处理 任务ID和渠道ID 长度
				if(v.conditionName == '渠道ID' || v.conditionName == '任务ID'){
					fontWidth = 35;
				}
				//特殊处理 CPID 长度
				if(v.conditionName == 'CPID'){
					fontWidth = 33;
				}
				componWidth = 180-fontWidth;
				tbar.insert(0, {
					xtype : 'tbspacer',
					width : 10
				});
				//根据componentType创建不同的控件
				tbar.insert(0, {
					xtype : v.componentType,
					id: v.componentId,
					width: componWidth
				});
				//根据conditionName创建不同的控件名称
				tbar.insert(0, {
					 xtype: "tbtext",
					 text:  '&nbsp&nbsp'+ v.conditionName,
					 width: fontWidth + spacerWidth
				});
			}
			//下拉框控件
			if(v.componentType == "combobox"){
				 //控件文字的长度
				 fontWidth = v.conditionName.length*12.5;
				 //特殊处理 TOP 控件的标签的长度
				 if(v.conditionName == 'TOP'){
					 fontWidth = 30; 
				 }
				 //特殊处理 省份/地市 控件和标签的长度
				 if(v.conditionName == '省份/地市'){
					 fontWidth = 50;
				 }
				 //特殊处理 CP收入 控件的标签的长度
				 if(v.componentId == "cpIncome"){
					 fontWidth = 51; 
				 }
				 //特殊处理 CP名称 控件的标签的长度
				 if(v.componentId == "cpName"){
					 fontWidth = 44; 
				 }
				 //控件的长度
				 componWidth = 180-fontWidth;
				 //sComboxField获取表名和下拉框控件valueField和displayField值
				 var sComboxField = v.comboxField;
				 if(sComboxField != null && sComboxField !=''){
//					 var valueField = v.comboxField.split('.')[1];
//					 var displayField = v.comboxField.split('.')[2];
					 var valueField = '';
					 var displayField = '';
//					 if(v.comboxField.split('.')[0].toLocaleUpperCase() == 'TD_S_REPORT_DICT'){
					 if(v.comboxField.split('.').length==1){
						 valueField = "S_ID";
						 displayField = "S_VALUE";
					 }else{
						 valueField = v.comboxField.split('.')[1];
						 displayField = v.comboxField.split('.')[2];
					 }
					 //cStore为下拉框控件数值的内容
					 var cStore = new Ext.data.JsonStore({
						 url : contextPath+'/module/admin/common/getComboBox.json?reportId=' + v.reportId +"&componentId=" + v.componentId,
						 fields : [valueField, displayField],
						 root : 'data'
				     });
					 cStore.load();
					 //comboBoxObj为下拉框属性
					 var comboBoxObj = {};
					 comboBoxObj.store = cStore;
					 comboBoxObj.valueField = valueField;
					 comboBoxObj.displayField = displayField;
					 comboBoxObj.width = componWidth;
					 comboBoxObj.id = v.componentId;
					 tbar.insert(0, {
						 xtype : 'tbspacer',
						 width : 10
					 });
					 //动态下拉框组件
					 tbar.insert(0,new Ext.DynamicCombo(comboBoxObj));
					 tbar.insert(0, {
						 xtype: "tbtext",
						 text: '&nbsp&nbsp'+ v.conditionName,
						 width: fontWidth + spacerWidth
					  });
				  }
				  //   省份/地市,结算帐期,门户,cp名称控件不用动态下拉框组件
				  if(v.componentId == 'province' || v.componentId == 'settleTime' || 
				     v.componentId == 'portalType' || v.componentId == 'cpName' ||
				     v.componentId =='year' || v.componentId == 'longNumber'){
					  tbar.insert(0, {
						  xtype : 'tbspacer',
						  width : 10
					  });
					  tbar.insert(0, {
						  xtype : v.format,
						  id : v.componentId,
						  itype: 'combobox',
						  width: componWidth
					   });
					   tbar.insert(0, {
						   xtype: "tbtext",
						   text: '&nbsp&nbsp'+ v.conditionName,
						   width: fontWidth + spacerWidth
					   });
				   }		
			}
			//时间 控件,格式：年月,年月日
			if(v.componentType == "datefield"){
				fontWidth = v.conditionName.length*12.5;
				componWidth = 180-fontWidth;
				if(v.componentId == 'startTime'){
			 //format为日期格式
					if(v.format == 'YM'){
						tbar.insert(0, {
							xtype : 'tbspacer',
							width : 10
						  });
						tbar.insert(0, {
							xtype: 'datefield',
							format:'Y-m',
						    maxValue: nowDate,
						    allowBlank: false,
							value: startDate,
							plugins:'monthPickerPlugin',
							id:v.componentId,
							width: componWidth
						  });
						tbar.insert(0, {
							xtype: "tbtext",
							text: '<span style="color: red;"> *</span>' + v.conditionName,
							width: fontWidth + spacerWidth
						  });
					}	
				 }
				 if(v.componentId == 'endTime'){
				   if(v.format == 'YM'){
					   tbar.insert(0, {
						xtype : 'tbspacer',
						width : 10
					  });
					   tbar.insert(0, {
						xtype: 'datefield',
						format:'Y-m',
						maxValue: nowDate,
						allowBlank: false,
						value: nowDate,
						plugins:'monthPickerPlugin',
						id:v.componentId,
						width: componWidth
					  });
					   tbar.insert(0, {
						xtype: "tbtext",
						text: '<span style="color: red;"> *</span>' + v.conditionName,
						width: fontWidth + spacerWidth
					  });
				   }
				 }
			    if(v.componentId == 'startTime'){
				   if(v.format == 'YMD'){
					   tbar.insert(0, {
					      xtype : 'tbspacer',
						  width : 10
					    });
					   tbar.insert(0, {
					      xtype: 'datefield' ,
					      format: 'Y-m-d', 
					      maxValue: nowDate,
					      allowBlank: false,
					      value:startDate,
					      id: v.componentId,
					      width: componWidth
				        });
					   
					   tbar.insert(0, {
						  xtype: "tbtext",
						  text: '<span style="color: red;"> *</span>' + v.conditionName,
						  width: fontWidth + spacerWidth
					    });
				   }
			    }
			    if(v.componentId == 'endTime'){
				    if(v.format == 'YMD'){
				    	tbar.insert(0, {
					      xtype : 'tbspacer',
						  width : 10
					   });
				    	tbar.insert(0, {
					      xtype: 'datefield' ,
					      format: 'Y-m-d', 
					      maxValue: nowDate,
					      allowBlank: false,
					      value:nowDate,
					      id: v.componentId,
					      width: componWidth
				      });
				    	tbar.insert(0, {
						xtype: "tbtext",
						text: '<span style="color: red;"> *</span>' + v.conditionName,
						width: fontWidth + spacerWidth
					  });
				  }
			   }
			}
			//时间控件  格式:时分秒
			if(v.componentType == 'datetimefield'){
				fontWidth = 91;
				tbar.insert(0, {
					xtype : 'tbspacer',
					width : 10
			   });
			  if(v.componentId == 'startTime'){
				  tbar.insert(0, {
						xtype : 'datetimefield',
						format : 'Y-m-d H:i:s',
						maxValue : nowDate,
						allowBlank: false,
						value : startDate,
						id : v.componentId,
						width: 220-fontWidth
				  });
				  tbar.insert(0, {
						xtype: "tbtext",
						text: '<span style="color: red;"> *</span>' + v.conditionName,
						width:fontWidth + spacerWidth 
				  });  
			  }
			  if(v.componentId == 'endTime'){
				  tbar.insert(0, {
						xtype : 'datetimefield',
						format : 'Y-m-d H:i:s',
						maxValue : nowDate,
						allowBlank: false,
						value : nowDate,
						id : v.componentId,
						width: 220-fontWidth
				  });
				  tbar.insert(0, {
						xtype: "tbtext",
						text: '<span style="color: red;"> *</span>' + v.conditionName,
						width: fontWidth + spacerWidth
				  });  
			  }
			}
		});
		Ext.each(tbar.items.items,function(item){
			me.tbarItems.push(item);
	    });
	},
	onRender: function(ct, position){
		Ext.DynamicToolbar.superclass.onRender.call(this, ct, position);
		var me = this;
		//节点id
		var nodeId = parent.g_oViewPort.items.get(1).nodeId;
		//树Id用于权利判断
	    var treeId = nodeId.substring(8);
	    //图表获取数据url
		me.chartUrl = me.oGrid.chartUrl;
		Ext.Ajax.request({
			url: this.storeUrl,
			method: 'post',  
			params:{treeId:treeId},
			success: function(resp, action){
				var allJson = Ext.decode(resp.responseText);
				var tbar1Json = [];
				var tbar2Json = [];
				//查询条件的个数
				var componentSize = 0;
				//循环获取查询条件的个数,不包括按钮和合计组件
				Ext.each(allJson,function(item){
					if(item.componentType != undefined && item.componentType != 'total'){
						componentSize++;
					}
				});	
				//如果查询条件超过4个就用两个toolbar来放置
				if(componentSize > 4){
					var componCount = 0;
					for(var i = allJson.length-1;i >= 0; i--){
						var item = allJson[i];
						//toolbar1放四个控件和按钮
						if(item.componentType != undefined && item.componentType != 'total' && componCount < 4){
							tbar1Json.push(item);
							componCount++;
						}else if(item.componentType != undefined && item.componentType != 'total'){
							tbar2Json.push(item);
						}else{
							tbar1Json.push(item);
						}
					}
					var width = me.oGrid.getWidth();
					var height = me.oGrid.getHeight();
					//插入第一个toolbar
					me.insertComponent(tbar1Json.reverse(), me);
					//插入 拉下和拉上的按钮
					me.add('->',{
						 iconCls : 'icon1',
						 handler : function(oButton, oEvent) {
							 if(tbar2.hidden){
								 tbar2.hidden = false;
								 //设置图标
								 this.setIconClass('icon2');
								 //设置gridPanel的高和宽
								 me.oGrid.setSize(width, height-25);
								 //设置toolbar的显示和隐藏
								 tbar2.setVisible(true);
								 //强制重新计算gridpanel的大小布局
								 me.oGrid.syncSize();
							 }else{
								 tbar2.hidden = true;
								 this.setIconClass('icon1');
								 me.oGrid.setSize(width, height+25);
								 tbar2.setVisible(false);
								 me.oGrid.syncSize();
							 }
						 }
					  });
					//插入第二个toolbar
					var tbar2 = new Ext.Toolbar({
						enableOverflow : true,
						hidden:true,
						//height:24,
						renderTo: me.oGrid.tbar
					});
					me.insertComponent(tbar2Json.reverse(), tbar2);
					//重新计算第二个toolbar的布局
					tbar2.doLayout();
				}else{
					me.insertComponent(allJson, me);
				}
				me.doLayout();
			},
			failure: function(resp, action){
				Ext.MessageBox.alert('温馨提示', '请求失败!');
			}
		});
	}
});

//将省份地市拆分开
function getProvinceArea(provinceString){
	var provinceArea = {};
	if(provinceString == "")
	{
		provinceArray = {
			proStr: "",
			areaStr: ""
		}
		return provinceArray;
	}
	var provinceArrays = provinceString.split(",");
	var proStr = null;
	var areaStr = null;
	for (var i = 0; i < provinceArrays.length; i++) {
		var paramArea = provinceArrays[i].split("/");
		if (paramArea.length == 2) {
			areaStr = areaStr == null ? paramArea[1] + ",": areaStr + paramArea[1] + ",";
		}else{
			if(!containsStr(proStr, paramArea[0]))
			{
				proStr = proStr == null ? paramArea[0] + ",": proStr + paramArea[0] + ",";
			}
		}
		
	}
	provinceArea = {
		proStr:proStr,
		areaStr: areaStr
	}
	return provinceArea;
}

//判断字符串中是否包含某字符串
function  containsStr(str, searchChars){
	var containsStr = false;
	if(str != null){
		var str = str.split(",");
		
		for(var i = 0;i < str.length;i++){
			if(str[i] == searchChars){
				containsStr = true;
			}
		}
	}
	
	return containsStr;
}
