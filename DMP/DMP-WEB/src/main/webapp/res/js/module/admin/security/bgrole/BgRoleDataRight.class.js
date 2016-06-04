var checkvalues;
var dataRight = "-1";   
var arr = new Array();
var arrId = new Array();
function contains(a, obj) {
	return (a.indexOf(obj) != -1);
}
var treeObj = null;
/*
 * ary.push("广东") for ( var i=0 ; i < arr.length ; ++i ){
 * 
 * if(arr[i]=="被点击的省份"){ arr[i].splice(i,1); } }
 */

Ext.override(Ext.form.RadioGroup, {
			setValue : function(v) {
				this.eachItem(function(item) {
							item.setValue(item.getRawValue() == v);
						});
			},
			getValue : function() {
				var value = 3;
				this.eachItem(function(item) {
							if (item.checked) {
								value = item.getRawValue();
								return false;
							}
						});

				return value;
			}
		});

Ext.ux.TreeCheckCombo = Ext.extend(Ext.form.TriggerField, {

	// triggerClass: 'x-form-tree-trigger',
	defaultAutoCreate : {
		tag : "input",
		type : "text",
		size : "24",
		autocomplete : "off"
	},
	listClass : '',

	selectedClass : 'x-combo-selected',

	listEmptyText : '',

	triggerClass : 'x-form-arrow-trigger',

	shadow : 'sides',

	listAlign : 'tl-bl?',

	maxHeight : 300,

	minHeight : 300,

	triggerAction : 'query',

	minChars : 4,

	autoSelect : false,

	typeAhead : false,

	queryDelay : 500,

	pageSize : 0,

	selectOnFocus : false,

	queryParam : 'query',

	loadingText : 'Loading...',

	resizable : false,

	handleHeight : 8,

	allQuery : '',

	mode : 'remote',

	minListWidth : 70,

	forceSelection : false,

	typeAheadDelay : 250,

	lazyInit : true,

	initComponent : function() {
		// this.readOnly = true;
		Ext.ux.TreeCheckCombo.superclass.initComponent.call(this);
		this.on('specialkey', function(f, e) {
					if (e.getKey() == e.ENTER) {
						this.onTriggerClick();
					}
				}, this);
		this.getTree();
	},

	onTriggerClick : function() {
		var tree = this.getTree();
		if(tree.hidden)
		{
			tree.show();
		    this.getTree().getEl().alignTo(this.wrap, 'tl-bl?');
		}else{
			tree.hide();
		}
	},

	getTree : function() {
		var me = this;
		if (!this.treePanel) {
			if (!this.treeWidth) {
				this.treeWidth = Math.max(130, this.width || 130);
			}
			if (!this.treeHeight) {
				this.treeHeight = 300;
			}
			this.treePanel = new Ext.tree.CheckBoxTreePanel({
				renderTo : Ext.getBody(),
				loader : this.loader || new Ext.tree.TreeLoader({
							preloadChildren : (typeof this.root == 'undefined'),
							url : this.dataUrl || this.url
						}),
				root : this.root || new Ext.tree.AsyncTreeNode({
							children : this.children
						}),
				rootVisible : (typeof this.rootVisible != 'undefined')
						? this.rootVisible
						: (this.root ? true : false),
				floating : true,
				autoScroll : true,
				minWidth : 130,
				minHeight : 300,
				width : this.treeWidth,
				height : 300,
				collapsible: false,
				cls: 'treeCombo',
				listeners : {
					/*
					 * hide : this.onTreeHide, show : this.onTreeShow, click :
					 * this.onTreeNodeClick, expandnode :
					 * this.onExpandOrCollapseNode, collapsenode :
					 * this.onExpandOrCollapseNode, resize : this.onTreeResize,
					 * scope : this,
					 */
					'checkchange' : function(node, checked) {
						if (checked) {
							// node.getUI().addClass('complete');
							arr.push(node.text);
							// arr.push(node.parentNode.text);
							arrId.push(node.id);
							// checkvalues=new Array[];
							// checkvalues.add(node.text);
							// checkvalues = node.text+"|";
							// Ext.MessageBox.alert("tt");
							// Ext.MessageBox.alert(node.text);
							/*
							 * for(var t=0;t<arr.length;t++){
							 * Ext.MessageBox.alert(arr[t]); }
							 */
						} else {
							// node.getUI().removeClass('complete');
							for (var i = 0; i < arr.length; ++i) {
								if (arr[i] == node.text) {
									arr.splice(i, 1);
								}
							}
							for (var j = 0; j < arrId.length; ++j) {
								if (arrId[j] == node.id) {
									arrId.splice(j, 1);
								}
							}
							/*
							 * for(var t=0;t<arr.length;t++){
							 * Ext.MessageBox.alert(arr[t]); }
							 */
							// Ext.MessageBox.alert("z="+node.text);
							// checkvalues.remove
							// checkvalues=checkvalues-node.text
						}
						if(node.childNodes.length == 0 && node.leaf == false)
						{
							this.loader.dataUrl =this.loader.dataUrl.split("?")[0] + "?check="+node.attributes['checked'];
						    node.reload();
						}
					},
					'beforeload' : function(node){
						
						this.loader.dataUrl =this.loader.dataUrl.split("?")[0] + "?check=false"; 
					},
					'beforeshow' : function(node){
						me.getTree().clearAll();
						me.getTree().getRootNode().reload();
					}
					
				},
				buttons : [{
					text : '确定',
					//iconCls : 'save',
					handler : function() {
						dataRight = "";
						var msg = '';
						var msgId = '';
						var provinceNames = [];
						var selNodes = me.treePanel.getChecked();
						Ext.each(selNodes, function(node) {
                                   //alert(me.root);
									if (msg.length > 0) {
										msg += ',';
									}
									var pName = '';
									//alert('me.root='+me.root.id+'|'+me.root.text+'|mode.id='+node.id)
									var rootname=me.root.text;
									if (rootname!=node.text) {
										pName = node.parentNode.text;
										if (null == pName || "" == pName
												|| rootname == pName) {
											msg += node.text;
											provinceNames.push(node.text);
										} else {
											if (contains(provinceNames, pName)) {
												msg = msg.replace(pName + ',',
														'');
												provinceNames.pop();
											}
											pName = pName + "/";
											msg += pName;
											msg += node.text;
										}
									}

								}

						);
						
						Ext.each(selNodes, function(node) {
							nodeId = node.id.substring(7);
							dataRight += nodeId + ",";
						});
						var lastIndex = dataRight.lastIndexOf(",");
		            	dataRight = dataRight.substr(0,lastIndex);
//						if(dataRight.length > 0 ){
//							var lastIndex = dataRight.lastIndexOf(",");
//			            	dataRight = dataRight.substr(0,lastIndex);
//						}else{
//							dataRight = "prosub";
//						}
						/*if ("全部"== arr[0]) {
							msg = "全部";
							msgId = "";

						}*/
						/*
						 * for(i=0;i<arr.length;i++){ if(msg.length > 0){
						 * msg+=","; } msg+=arr[i]; }
						 */

						// var msgId = '';
						/*
						 * var selNodes = getChecked(); Ext.each(selNodes,
						 * function(node) { if (msg.length > 0) { msg += ', '; }
						 * msg += node.text; });
						 */
						/*
						 * for(j=0;j<arrId.length;j++){ if(msgId.length > 0){
						 * msgId+=","; } msgId+=arrId[j]; }
						 */
						me.setRawValue(msg);
						me.value = msgId;
						if (me.hiddenField) {
							me.hiddenField.value = msg;
						}
						if (me.value) {
							me.removeClass(me.emptyClass);
						}
						/*
						 * Ext.Msg.show({ title : 'Completed name', msg :
						 * msg.length > 0 ? msg : 'None', icon : Ext.Msg.INFO,
						 * minWidth : 200, buttons : Ext.Msg.OK });
						 */

						/*
						 * Ext.Msg.show({ title : 'Completed id', msg :
						 * msgId.length > 0 ? msgId : 'None', icon :
						 * Ext.Msg.INFO, minWidth : 200, buttons : Ext.Msg.OK
						 * });
						 */
						me.collapse();
					}
				
				}]
			});

			this.treePanel.show();
			this.relayEvents(this.treePanel.loader, ['beforeload', 'load',
							'loadexception']);
			if (this.resizable) {
				this.resizer = new Ext.Resizable(this.treePanel.getEl(), {
							pinned : true,
							handles : 'se'
						});
				this.mon(this.resizer, 'resize', function(r, w, h) {
							this.treePanel.setSize(w, h);
						}, this);
			}
		}
		return this.treePanel;
	},
	onExpandOrCollapseNode : function() {
		if (!this.maxHeight || this.resizable)
			return; // -----------------------------> RETURN
		var treeEl = this.treePanel.getTreeEl();
		var heightPadding = treeEl.getHeight() - treeEl.dom.clientHeight;
		var ulEl = treeEl.child('ul'); // Get the underlying tree element
		var heightRequired = ulEl.getHeight() + heightPadding;
		if (heightRequired > this.maxHeight)
			heightRequired = this.maxHeight;
		this.treePanel.setHeight(heightRequired);
	},
	onTreeResize : function() {
		if (this.treePanel)
			this.treePanel.getEl().alignTo(this.wrap, 'tl-bl?');
	},

	onTreeShow : function() {
		Ext.getDoc().on('mousewheel', this.collapseIf, this);
		Ext.getDoc().on('mousedown', this.collapseIf, this);
	},

	onTreeHide : function() {
		Ext.getDoc().un('mousewheel', this.collapseIf, this);
		Ext.getDoc().un('mousedown', this.collapseIf, this);
	},

	collapseIf : function(e) {
		if (!e.within(this.wrap) && !e.within(this.getTree().getEl())) {
			this.collapse();
		}
	},

	collapse : function() {
		this.getTree().hide();
		if (this.resizer)
			this.resizer.resizeTo(this.treeWidth, this.treeHeight);
	},

	// private
	validateBlur : function() {
		return !this.treePanel || !this.treePanel.isVisible();
	},

	setValue : function(v) {
		this.startValue = this.value = v;
		if (this.treePanel) {
			var n = this.treePanel.getNodeById(v);// 位于一级以下节点要树全部展开时才可用
			if (n) {
				n.select();// 默认选中节点
				this.setRawValue(n.text);
				if (this.hiddenField)
					this.hiddenField.value = v;
			}
		}
	},

	getValue : function() {
		return this.value;
	},

	/*onTreeNodeClick : function(node, e) {
		var pName = "";
		alert('this.root'+this.root);
		if ("全部" != node.text) {
			pName = node.parentNode.text;
			if (null == pName || "" == pName || "全部" == pName) {
				pName = "";
			} else {
				pName = pName + "/";
			}
		}

		this.setRawValue(pName + node.text);
		this.value = node.id;
		if (this.hiddenField)
			this.hiddenField.value = node.id;
		this.fireEvent('select', this, node);
		this.collapse();
	},*/
	onRender : function(ct, position) {
		Ext.ux.TreeCheckCombo.superclass.onRender.call(this, ct, position);
		if (this.hiddenName) {
			this.hiddenField = this.el.insertSibling({
						tag : 'input',
						type : 'hidden',
						name : this.hiddenName,
						id : (this.hiddenId || this.hiddenName)
					}, 'before', true);

			// prevent input submission
			this.el.dom.removeAttribute('name');
		}
	}

});

// Ext.reg('combotree', Ext.ux.TreeCombo);

var loader = new Ext.tree.TreeLoader({
			dataUrl : contextPath+'/module/admin/common/getAreaTree.json'
		});
var loaderCheck = new Ext.tree.TreeLoader({
			dataUrl : contextPath+'/module/admin/common/getAreaTree.json?check=true'
		});

var ProvinceAreaCombo = Ext.extend(Ext.ux.TreeCheckCombo, {
			//id : 'provinceArea',
			width : 130,
			Height : 300,
			// maxHeight:200,

			root : new Ext.tree.AsyncTreeNode({
						id : 'AreaId_1',
						expanded : true,
						checked : false,
						text : '所有'
					}),
			// dataUrl:'getBgNavTree.json',
			loader : loader,
			allowBlank : false,
			valueField : 'id',
			displayField : 'text',
			typeAhead : true,
			mode : 'local',
			forceSelection : true,
			triggerAction : 'all',
			// hiddenName : 'param.businessId',
			// rootVisible : false,
			// emptyText:'Select a state...',
			selectOnFocus : true,
			emptyText : '请选择',
			constructor : function() {
				ProvinceAreaCombo.superclass.constructor.apply(this, arguments);
			},
			onDestroy : function() {
				ProvinceAreaCombo.superclass.onDestroy.apply(this, arguments);
			}
		});
Ext.reg('dataRightCombo', ProvinceAreaCombo);   //角色权限表中查看数据权限下拉框
var ProvinceAreaCombo = Ext.extend(Ext.ux.TreeCheckCombo, {
	//id : 'provinceArea',
	width : 130,
	Height : 300,
	// maxHeight:200,

	root : new Ext.tree.AsyncTreeNode({
				id : 'AreaId_1',
				expanded : true,
				checked : false,
				text : '所有'
			}),
	// dataUrl:'getBgNavTree.json',
	loader : loader,
	allowBlank : false,
	valueField : 'id',
	displayField : 'text',
	typeAhead : true,
	mode : 'local',
	forceSelection : true,
	triggerAction : 'all',
	// hiddenName : 'param.businessId',
	// rootVisible : false,
	// emptyText:'Select a state...',
	selectOnFocus : true,
	emptyText : '请选择',
	constructor : function() {
		ProvinceAreaCombo.superclass.constructor.apply(this, arguments);
	},
	onDestroy : function() {
		ProvinceAreaCombo.superclass.onDestroy.apply(this, arguments);
	}
});
Ext.reg('dataRightCombo1', ProvinceAreaCombo);  //角色权限表中修改数据权限的下拉框
var ProvinceAreaCombo = Ext.extend(Ext.ux.TreeCheckCombo, {
	//id : 'provinceArea',
	width : 130,
	Height : 300,
	// maxHeight:200,

	root : new Ext.tree.AsyncTreeNode({
				id : 'AreaId_1',
				expanded : true,
				checked : false,
				text : '所有'
			}),
	// dataUrl:'getBgNavTree.json',
	loader : loader,
	allowBlank : false,
	valueField : 'id',
	displayField : 'text',
	typeAhead : true,
	mode : 'local',
	forceSelection : true,
	triggerAction : 'all',
	// hiddenName : 'param.businessId',
	// rootVisible : false,
	// emptyText:'Select a state...',
	selectOnFocus : true,
	emptyText : '请选择',
	constructor : function() {
		ProvinceAreaCombo.superclass.constructor.apply(this, arguments);
	},
	onDestroy : function() {
		ProvinceAreaCombo.superclass.onDestroy.apply(this, arguments);
	}
});
Ext.reg('dataRightCombo2', ProvinceAreaCombo);  //角色权限表中新增数据权限的下拉框
var ProvinceAreaCombo = Ext.extend(Ext.ux.TreeCheckCombo, {
	//id : 'provinceArea',
	width : 130,
	Height : 300,
	// maxHeight:200,

	root : new Ext.tree.AsyncTreeNode({
				id : 'AreaId_1',
				expanded : true,
				checked : false,
				text : '所有'
			}),
	// dataUrl:'getBgNavTree.json',
	loader : loader,
	allowBlank : false,
	valueField : 'id',
	displayField : 'text',
	typeAhead : true,
	mode : 'local',
	forceSelection : true,
	triggerAction : 'all',
	// hiddenName : 'param.businessId',
	// rootVisible : false,
	// emptyText:'Select a state...',
	selectOnFocus : true,
	emptyText : '请选择',
	constructor : function() {
		ProvinceAreaCombo.superclass.constructor.apply(this, arguments);
	},
	onDestroy : function() {
		ProvinceAreaCombo.superclass.onDestroy.apply(this, arguments);
	}
});
Ext.reg('dataRightCombo3', ProvinceAreaCombo);  //角色权限中批量修改及新增数据权限的下拉框
