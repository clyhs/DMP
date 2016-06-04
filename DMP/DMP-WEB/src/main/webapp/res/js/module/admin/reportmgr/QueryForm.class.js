

// 显示查看编辑的sql语句
function getQueryForm(query, readOnly) {
	if (!query) {
		var query = {
			id : '',
			componentId : '',
			conditionName : '',
			componentType : '',
			format : '',
			layout : '',
			comboxField : ''
		};
	}
	if (!readOnly) {
		readOnly = false;
	}
	return new Ext.FormPanel({
		region : 'center',
		height : 150,
		width : 600,
		labelWidth : 85,
		labelAlign : 'left',
		frame : true,
		items : [{
			layout : 'column',
			border : false,
			labelSeparator : ':',
			defaults : {
				layout : 'form',
				border : false,
				columnWidth : .5
			},
			items : [{
						items : [{
									xtype : 'hidden',
									id : 'id',
									value : query.id,
									readOnly : readOnly
								}]
					}, {
						items : [{
							width : 150,
							xtype : 'textfield',
							fieldLabel : '<font color="red">*</font>控件ID',
							id : 'componentId',
							value : query.componentId,
							readOnly : readOnly,
							maxLength : 20,
							maxLengthText : '字符数不能超过20个',
							listeners : {
								// 检查componentId是否在数据库中存在
								blur : function(params) {
									if (params.readOnly) {
										return;
									}
									if (params.getValue() != "") {
										Ext.Ajax.request({
											url :contextPath+'/module/admin/reportmgr/checkreportconditionexit.json',
											method : 'POST',
											params : {
												componentId : params.getValue()
											},
											success : function(response, action) {
												var resp = Ext.util.JSON
														.decode(response.responseText);// 获取从后台传递回来的字符串
												if (resp.success == true) {
													Ext.MessageBox.confirm(
															'温馨提示', resp.msg,
															function(buttonId,
																	text, opt) {
																if (buttonId == "yes") {
																	params
																			.setValue() == '';
																}
															});
												}
											}
										})
									}
								}
							}
						}]
					}, {
						items : [{
									width : 150,
									xtype : 'textfield',
									fieldLabel : '<font color="red">*</font>控件名称',
									id : 'conditionName',
									value : query.conditionName,
									readOnly : readOnly,
									maxLength : 40,
									maxLengthText : '字符数不能超过40个',
									listeners : {
								// 检查conditionName是否在数据库中存在
								blur : function(params) {
									if (params.readOnly) {
										return;
									}
									if (params.getValue() != "") {
										Ext.Ajax.request({
											url :contextPath+'/module/admin/reportmgr/checkbynameexit.json',
											method : 'POST',
											params : {
												conditionName : params.getValue()
											},
											success : function(response, action) {
												var resp = Ext.util.JSON
														.decode(response.responseText);// 获取从后台传递回来的字符串
												if (resp.success == true) {
													Ext.MessageBox.confirm(
															'温馨提示', resp.msg,
															function(buttonId,
																	text, opt) {
																if (buttonId == "yes") {
																	params
																			.setValue() == '';
																}
															});
												}
											}
										})
									}
								}
							}
								}]
					}, {
						items : [{
							width : 150,
							fieldLabel : "  控件类型",
							id : 'componentType',
							name : "componentType",
							xtype : 'combo',
							store : [
							       ['', '请选择'],
							        ['datefield', '日期格式'],
									['combobox', '下拉列表框'],
									['textfield', '文本类型'],
									['datetimefield', '时分秒']
									],
							editable : false,
							readOnly : readOnly,
							hiddenName : 'xx',
							forceSelection : true,
							mode : 'local',
							triggerAction : 'all',
							value : query.componentType
						}]
					}, {
						items : [{
									width : 150,
									xtype : 'textfield',
									fieldLabel : '  控件格式',
									id : 'format',
									value : query.format,
									readOnly : readOnly,
									maxLength : 40,
									maxLengthText : '字符数不能超过40个'
								}]
					}, {
						items : [{
									width : 150,
									xtype : 'numberfield',
									fieldLabel : '  控件优先级',
									id : 'layout',
									value : query.layout,
									readOnly : readOnly,
									maxLength : 40,
									allowDecimals:false,
									maxLengthText : '字符数不能超过40个'
								}]
					}, {
						items : [{
									width : 150,
									xtype : 'textfield',
									fieldLabel : '  控件关联ID',
									id : 'comboxField',
									value : query.comboxField,
									readOnly : readOnly,
									maxLength : 40,
									maxLengthText : '字符数不能超过40个'
								}]
					}]
		}]
	});
}

// 显示Queryt的grid信息
function getQuerytAttrGrid(pStore) {
	return new Ext.grid.GridPanel({
				height : 320,
				width : 600,
				region : 'south',
				store : pStore,
				columns : [{
							header : '控件值',
							width : 120,
							dataIndex : 'code'
						}, {
							header : '页面显示值',
							width : 120,
							dataIndex : 'meaning'
						}]
			});
}

// 增加和修改的reportAttr的grid信息
function addEditQueryAttrGrid(pStore) {
	return new Ext.grid.EditorGridPanel({
		height : 320,
		width : 600,
		id : 'editorGridPanel',
		region : 'south',
		clicksToEdit : 1,
		store : pStore,
		columns : [{
					header : '控件值',
					dataIndex : 'code',
					width : 120,
					editor : new Ext.form.TextField({
								allowBlank : false
							})
				}, {
					header : '页面显示值',
					dataIndex : 'meaning',
					width : 120,
					editor : new Ext.form.TextField({
								allowBlank : false
							})
				}, {
					header : '操作',
					dataIndex : 'operation',
					width : 120,
					align : 'center',
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var str = "<a href = 'javascript:operation("
								+ rowIndex
								+ ")' style='color: blue; padding: 0px;'>删除</a>";
						return str;
					}
				}],
		tbar : getEditAddTopToolbar()
	});
}

// 查看报表资料
Ext.ViewQueryWindow = function(query) {
	var me = this;
	var queryAttrStore = getQueryAttrList(query);

	Ext.ViewQueryWindow.superclass.constructor.call(this, {
				layout : 'border',
				width : 600,
				height : 500,
				y : 10,
				plain : true,
				title : '查看控件资料',
				closeAction : 'close',
				iconCls : 'form',
				constrain : true,
				modal : true,
				items : [getQueryForm(query, readOnly = true),
						getQuerytAttrGrid(queryAttrStore)],
				buttons : [{
							text : '关闭',
							iconCls : 'exit',
							handler : function() {
								me.close();
							}
						}]
			});
};

Ext.extend(Ext.ViewQueryWindow, Ext.Window);

// 增加修改报表弹出框
Ext.AddEditQueryWindow = function(query) {
	var me = this;
	var oldComboxField = '';
	if (query) {
		me.title = "修改";
		oldComboxField = query.comboxField;
	} else {
		me.title = "增加";
	}
	var queryAttrStore = {};
	if (query) {
		queryAttrStore = getQueryAttrList(query);
	}
	Ext.AddEditQueryWindow.superclass.constructor.call(this, {
				layout : 'border',
				width : 600,
				height : 500,
				y : 10,
				plain : true,
				title : me.title + "控件资料",
				iconCls : 'form',
				constrain : true,
				closeAction : 'close',
				modal : true,
				// tbar: getQueryTopToolbar(query,readOnly=false),
				items : [getQueryForm(query),
						addEditQueryAttrGrid(queryAttrStore)],
				buttons : [{
					text : '提交',
					iconCls : 'save',
					handler : function(oButton, oEvent) {
						var editorGridPanel = Ext.getCmp('editorGridPanel');
						var recordCount = editorGridPanel.getStore().getCount(); // 获取grid的行数
						// 添加reportattr列表
						var reportAttrArray = [];
						for (var i = 0; i < recordCount; i++) {
							var reportAttr = {};
							var rowRecord = editorGridPanel.getStore().getAt(i);
							reportAttr.code = rowRecord.data['code'];
							reportAttr.meaning = rowRecord.data['meaning'];
							reportAttr.name = Ext.getCmp('comboxField')
									.getValue();
							reportAttrArray[i] = reportAttr;
						}
						// 添加report列表
						var query = {
							id : Ext.getCmp('id').getValue(),
							componentId : Ext.getCmp('componentId').getValue(),
							conditionName : Ext.getCmp('conditionName')
									.getValue(),
							componentType : Ext.getCmp('componentType')
									.getValue(),
							format : Ext.getCmp('format').getValue(),
							layout : Ext.getCmp('layout').getValue(),
							comboxField : Ext.getCmp('comboxField').getValue()
						};

						// if(reportAttrArray.length <= 0){
						// Ext.Msg.alert('温馨提示', "信息填写完整后请先保存再提交");return;}
						if (query.componentId == ""
								|| query.conditionName == ""
								|| query.componentType == ""
								|| query.format == "" || query.layout == "") {
							Ext.Msg.alert('温馨提示', "请填写完整控件信息");
							return;
						}
						if (me.title == "增加控件资料") {
							var url = contextPath+'/module/admin/reportmgr/addReportCondition.json';

						} else {
							var url = contextPath+'/module/admin/reportmgr/editReportCondition.json';

						}
						var msgTip = new Ext.LoadMask(me.getEl(), {
									msg : '数据加载中...'
								});
						msgTip.show();
						Ext.Ajax.request({
									method : 'POST',
									url : url,
									params : {
										reportAttr : Ext
												.encode(reportAttrArray),
										report : Ext.encode(query),
										oldComboxField : oldComboxField
									},
									success : function(response, action) {
										msgTip.hide();
										var resp = Ext.util.JSON
												.decode(response.responseText);// 获取从后台传递回来的字符串
										if (resp.success == true) {
											Ext.MessageBox.alert('温馨提示',
													resp.msg, function() {
														var gridPanel = Ext
																.getCmp('grid');
														gridPanel.getStore()
																.reload();
														me.close();
													});
										} else {
											Ext.Msg.alert('温馨提示', resp.msg);
										}
									},
									failure : function(response, action) {
										msgTip.hide();
									}
								});
					}
				}, {
					text : '关闭',
					iconCls : 'exit',
					handler : function() {
						me.close();
					}
				}]
			});
};

Ext.extend(Ext.AddEditQueryWindow, Ext.Window);

// 获取reportid对应的reportAttr列表
function getQueryAttrList(query) {
	var queryAttrStore = new Ext.data.JsonStore({
				proxy : new Ext.data.HttpProxy({
							method : 'post',
							url : contextPath+'/module/admin/reportmgr/getdictionarylist.json'
						}),
				root : 'data',
				fields : [{
							name : 'code',
							type : 'String'
						}, {
							name : 'meaning',
							type : 'String'
						}]
			});
	queryAttrStore.load({
				params : {
					id : query.id
				}
			});
	return queryAttrStore;
}

// 修改或添加reportattr的toptoolbar
function getEditAddTopToolbar() {
	return new Ext.Toolbar({
				enableOverflow : true,
				items : [{
							xtype : 'tbspacer',
							width : 10
						}, {
							xtype : 'tbtext',
							text : '控件值'
						}, {
							xtype : 'textfield',
							width : 150,
							id : 'code'
						}, {
							xtype : 'tbspacer',
							width : 10
						}, {
							xtype : 'tbtext',
							text : '页面显示值'
						}, {
							xtype : 'textfield',
							width : 150,
							id : 'meaning'
						}, {
							xtype : 'tbspacer',
							width : 10
						}, {
							text : '保存',
							iconCls : 'save',
							handler : function(oButton, oEvent) {
								var editorGridPanel = Ext
										.getCmp('editorGridPanel');
								// 参数值的获取
								var code = Ext.getCmp('code').getValue();
								var meaning = Ext.getCmp('meaning').getValue();
								var comboxField=Ext.getCmp('comboxField').getValue();
								if(comboxField==""){
									Ext.Msg.alert('温馨提示', "请填写控件关联ID");
									return;
								}

								if ((code && meaning) == "") {
									Ext.Msg.alert('温馨提示', "请填写完整信息");
									return;
								}

								// 保存输入的值到gridpanel中
								var reportAttrRecord = Ext.data.Record.create([
										{
											name : 'code',
											type : 'string'
										}, {
											name : 'meaning',
											type : 'string'
										}]);
								var reportAttr = new reportAttrRecord({
											code : code,
											meaning : meaning
										})
								editorGridPanel.getStore()
										.addSorted(reportAttr);
								editorGridPanel.getView().refresh();

							}
						}]
			})
}

function operation(index) {
	Ext.MessageBox.confirm('温馨提示', '确定要删除这条数据吗？',
			function(buttonId, text, opt) {
				if (buttonId == 'yes') {
					var editorGridPanel = Ext.getCmp('editorGridPanel');
					editorGridPanel.getStore().removeAt(index);
					editorGridPanel.getView().refresh();
				}
			});
}