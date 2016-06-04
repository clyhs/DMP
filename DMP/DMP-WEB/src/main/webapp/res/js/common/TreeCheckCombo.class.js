var checkvalues;

var arr = new Array();
var arrId = new Array();

if("undefined" == typeof tokenId || tokenId == null){
	//判断数据权限哪些进行显示
	var nodeId = parent.g_oViewPort.items.get(1).nodeId;
	treeId = nodeId.substring(8);
	proRight = parent.g_oViewPort.items.get(1).proRight;
}


function contains(a, obj) {
	return (a.indexOf(obj) != -1);
}
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

	autoSelect : true,

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
				id: 'treePanel',
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
				cls: 'treeCombo',
				listeners : {
					/*
					 * hide : this.onTreeHide, show : this.onTreeShow, click :
					 * this.onTreeNodeClick, expandnode :
					 * this.onExpandOrCollapseNode, collapsenode :
					 * this.onExpandOrCollapseNode, resize : this.onTreeResize,
					 * scope : this,
					 */
					'expandnode': function(expendNode){
						var childNode = expendNode.childNodes;
						if(me.treePanel.getNodeById("AreaId_2").attributes.checked == true || me.treePanel.getNodeById("AreaId_3").attributes.checked == true){
							for(var j = 0;j < childNode.length;j++){
								childNode[j].disable();
							}
						}else{
							for(var j = 0;j < childNode.length;j++){
								childNode[j].enable();
							}
						}
					},
					
					'checkchange' : function(node, checked) {
						var allTreeNode = getAllChildrenNodes(this.root);   //获取所有子节点
					    var treeNode = this.root.childNodes;    //获取根目录下的子节点
					    var isRootNodeCheck = false;
					    for(var k = 0;k < treeNode.length;k++){
					    	if(treeNode[k].attributes.checked == true){
					    		isRootNodeCheck = true;
					    	}
					    }
					   
					    //如果全国全部某个选中，则其他所有节点不可选
						if((node.id == "AreaId_2" || node.id == "AreaId_3") && checked){
							for(var i = 0;i < allTreeNode.length;i++){
								if(allTreeNode[i].id != node.id){
									allTreeNode[i].disable();
								}
							}
						
						}
						//如果全国全部没选中，但其他节点有选中，则全国全部不可选
						else if(node.id != "AreaId_2" && node.id != "AreaId_3"){
//							if((isRootNodeCheck && !checked) || (!isRootNodeCheck && checked) || (isRootNodeCheck && checked)){
//								me.treePanel.getNodeById("AreaId_2").disable();
//								me.treePanel.getNodeById("AreaId_3").disable();
//							}else {
//								me.treePanel.getNodeById("AreaId_2").enable();
//								me.treePanel.getNodeById("AreaId_3").enable();
//							}
							if(!isRootNodeCheck && !checked){
								me.treePanel.getNodeById("AreaId_2").enable();
								me.treePanel.getNodeById("AreaId_3").enable();
							}else{
								me.treePanel.getNodeById("AreaId_2").disable();
								me.treePanel.getNodeById("AreaId_3").disable();
							}
						
							
						}
						else{
							for(var i = 0;i < allTreeNode.length;i++){
								allTreeNode[i].enable();
						}
						}
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
							this.loader.dataUrl = this.loader.dataUrl.split("?")[0] + "?check="+node.attributes['checked'] + "&treeId=" + treeId;
						    node.reload();
						}
					},
					'beforeload' : function(node){
						
						this.loader.dataUrl = this.loader.dataUrl.split("?")[0] + "?check="+node.attributes['checked'] + "&treeId=" + treeId; 
					}
				},
				buttons : [{
					text : '确定',
					//iconCls : 'save',
					handler : function() {
						var msg = '';
						var msgId = '';
						var provinceNames = [];
						var provinceIds = [];
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
									if (msgId.length > 0) {
										msgId += ',';
									}
										var pId = '';
									//alert('me.root='+me.root.id+'|'+me.root.text+'|mode.id='+node.id)
									var rootid=me.root.id;
									if (rootid!=node.id) {
										pId = node.parentNode.id;
										if (null == pId || "" == pId
												|| rootid == pId) {
											msgId += node.id;
											provinceIds.push(node.id);
										} else {
											if (contains(provinceIds, pId)) {
												msgId = msgId.replace(pId + ',',
														'');
												provinceIds.pop();
											}
											pId = pId + "/";
											msgId += pId;
											msgId += node.id;
										}
									}
								});
                       
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
			this.treePanel.hide();
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
			rootVisible : true,
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
Ext.reg('provinceAreaCombo', ProvinceAreaCombo);

/* Single province combo box */

var provinceStore = new Ext.data.JsonStore({
			url : contextPath+'/module/admin/common/getProvinceList.json?treeId='+treeId+ '&proRight='+proRight,
			fields : ['areaId', 'name'],
			root : 'arr'
		});

provinceStore.load();

var SingleProvinceCombo = Ext.extend(Ext.form.ComboBox, {
			//id : 'province',
			typeAhead : true,
			triggerAction : 'all',
			forceSelection : true,
			mode : 'local',
			width : 90,
			emptyText : '请选择',
			store : provinceStore,
			valueField : 'areaId',
			displayField : 'name',
			constructor : function() {
				SingleProvinceCombo.superclass.constructor.apply(this,
						arguments);
			},
			onDestroy : function() {
				SingleProvinceCombo.superclass.onDestroy.apply(this, arguments);
			},
			listeners: {  
		        afterRender: function(combo) {
		        	provinceStore.on("load", function() {
		        		tmp = provinceStore.getAt(0).get("areaId"); 
		        	    combo.setValue(tmp);
		        		});  
		          }    
		      }  
		});
Ext.reg('singleProvinceCombo', SingleProvinceCombo);


/**
 * 取得一个节点的所有子节点，不包括节点本身
 * @param node
 * @returns {Array}
 */
function getAllChildrenNodes(node){
	for(var i = 0;i < node.childNodes.length;i++){
		
	}
	var children = [];
	children.push(node);
	if(!node.isLeaf()){
	for(var i=0;i<node.childNodes.length;i++){
	children = children.concat(getAllChildrenNodes(node.childNodes[i]));
	}
	}
	return children;
	}; 
