var allChildNode = [];    //定义父节点下所有子节点
/**
 * 角色管理下角色报表权限树型结构
 * shu.y
 */
Ext.BgRolePermissionTree = function(){
	var me = this;
	Ext.BgRolePermissionTree.superclass.constructor.call(this, {
		layout:'card',
		region: 'east',
		title: '角色权限',
		id:'rpt-199',
		width: 700,
		margins: '0 0 5 0',
		cmargins: '0 5 5 5',
		split: true,  
		header:false,  
		useSplitTips:true,
		border: false,
		
		collapsible : true,
		collapsibleSplitTip:'可拖动，双击收起',   
		activeItem : 0,
		items:[]
	});
};
Ext.extend(Ext.BgRolePermissionTree, Ext.Panel);
//增加角色
Ext.AddBgRolePanel = function(clickName)
{
	var me = this;
	me.rightValue = {};        //初始化角色权限值
	me.isBatch = false;     //初始化是否为批量设置权限
	Ext.AddBgRolePanel.superclass.constructor.call(this, {
		id: 'addPanel',
		layout:'fit',
        title: '增加角色',
        closeAction: 'hide',
        collapsible:true, 
        border: false,
        tbar:[{
    		xtype: 'tbtext', 
    		text: '角色名称：'
    	},
    	{
    		xtype: 'textfield',
    		width: 300
    	},'->',{
            text:'提交',
            iconCls:'save',
            handler: function(oButton, oEvent)
            {
        		var oValue = me.items.get(0).getValue();    //获取角色报表权限的值
        		var value = oValue.sName.split(" ");   
        		//判断是否有空格
        		if(value.length != 1){
        			Ext.Msg.alert('温馨提示','角色名称不能含空格');  
        			return;
        		}
        		//判断是否为空
        		if(Ext.isEmpty(oValue.sName))
        		{
        			Ext.Msg.alert('温馨提示','请输入角色名称');  
        		}else
        		{
        			if(oValue.aNode.length == 0)
    				{
    					Ext.Msg.alert('温馨提示','请选择权限结点');  
    				}else
					{
    					var myMask = new Ext.LoadMask(Ext.getBody(),{
    						msg: '数据正在提交中，请稍后...',
    						removeMask: true
    					});
    					myMask.show();
    					//判断是否为批量修改，如是则重置rightvalue的值
    					if(me.isBatch){ me.rightValue = setChildrenRightValue(me); }
    					
						Ext.Ajax.request({   
						   url: 'addBgRole.html',   
						   method: 'post',   
						   params: {sName: oValue.sName, sBgTreeIdList: oValue.aNode.join(','), sRightMap: Ext.encode(me.rightValue)},   
						   success: function(resp, action)
						   {
						        var obj = Ext.util.JSON.decode(resp.responseText); 
						        if(obj.success)
						        {
						        	me.rightValue = {};    //权限值清空，方便下次设值
						        	allChildNode = [];     //全局变量所有子节点清空，方便下次设值
						        	me.isBatch = false;    //提交成功后将是否批量设为false
						        	
									var oParentNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
									//对左侧的角色树做添加节点操作
								 	oParentNode.appendChild(new Ext.tree.TreeNode({
								 		text: oValue.sName,
								 		id: 'RoleId_'+obj.param.nBgRoleId,
								 		iconCls: 'role-tree',
								 		leaf: true
								 	}));
								 	myMask.hide();
								 	oParentNode.expand();
								 	Ext.Msg.alert('温馨提示', '新增成功',function(){
								 		me.show();
					            	});
						        }else
						        {
						        	myMask.hide();
						        	Ext.Msg.alert('温馨提示', obj.msg); 
						        }
						    },
						    failure:function(resp, action)
						    {   
						        Ext.Msg.alert('温馨提示','系统错误!');   
						    }   
						}); 
					}
        		}
        	}
        },{
			xtype: 'tbspacer',
			width: 20
		},{
            text: '关闭',
            iconCls:'exit',
            handler: function(oButton, oEvent){
    			me.hide();
            }
        },{
			xtype: 'tbspacer',
			width: 20
		}],
        items: new Ext.NavTree(null, clickName), 
        listeners:{
			show : function()
			{
				var oTree = me.items.get(0);
				var aComponents = me.getTopToolbar().findByType('textfield');
				aComponents[0].setValue("");
				oTree.clearSelect(oTree.getRootNode());
				if(oTree.body != undefined){
					oTree.body.dom.scrollTop = 0;    //新增界面展现时，滚动条回到顶端
				}
			}
		}
	});
};
Ext.extend(Ext.AddBgRolePanel, Ext.Panel);

//修改角色
Ext.EditBgRolePanel = function(nBgRoleId, clickName)
{
	var me = this;
	me.rightValue = {};     //初始化角色权限值
	me.isBatch = false;     //初始化是否为批量设置权限
	Ext.EditBgRolePanel.superclass.constructor.call(this, {
		id: 'editPanel',
		layout:'fit',
        title: '修改角色',
        closeAction: 'hide',
        collapsible:true, 
        border: false,
        tbar:[{
    		xtype: 'tbtext', 
    		text: '角色名称：'
    	},
    	{
    		xtype: 'textfield',
    		width: 300
    	},'->',{
            text:'提交',
            iconCls:'save',
            handler: function(oButton, oEvent)
            {
				var oValue = me.items.get(0).getValue();
				var value = oValue.sName.split(" ");   //判断是否有空格
        		if(value.length != 1){
        			Ext.Msg.alert('温馨提示','角色名称不能含空格');  
        			return;
        		}
        		if(Ext.isEmpty(oValue.sName))
        		{
        			Ext.Msg.alert('温馨提示','请输入角色名称');  
        		}else
        		{
        			if(oValue.aNode.length == 0)
    				{
    					Ext.Msg.alert('温馨提示','请选择权限结点');  
    				}else
					{
    					//var oNode = me.items.get(0).getSelectionModel().getSelectedNode();
    					var myMask = new Ext.LoadMask(Ext.getBody(),{
    						msg: '数据正在提交中，请稍后...',
    						removeMask: true
    					});
    					myMask.show();
    					//判断是否为批量修改，如是则重置rightvalue的值
    					if(me.isBatch){ me.rightValue = setChildrenRightValue(me); }
    					
    					Ext.Ajax.request({   
						   url: 'editBgRole.html',   
						   method: 'post',   
						   params: {nBgRoleId: me.nBgRoleId, sName: oValue.sName, sBgTreeIdList : oValue.aNode.join(','), sRightMap: Ext.encode(me.rightValue)},   
						   success: function(resp, action)
						   {
						        var obj = Ext.util.JSON.decode(resp.responseText); 
						        if(obj.success)
						        {   
						        	me.rightValue = {};    //权限值清空，方便下次设值
						        	allChildNode = [];     //全局变量所有子节点清空，方便下次设值
						        	me.isBatch = false;    //提交成功后将是否批量设为false
						        	var aComponents = me.getTopToolbar().findByType('textfield');
									aComponents[0].setValue(oValue.sName);
									myMask.hide();
									g_oViewPort.items.get(0).getRootNode().reload();    //刷新左侧的角色结构树
									Ext.Msg.alert('温馨提示', '修改成功',function(){
					            		me.hide();
					            	});
						        }else
						        {
						        	myMask.hide();
						        	Ext.Msg.alert('温馨提示', obj.msg); 
						        }
						   },
						   failure:function(resp, action)
						   {   
						        Ext.Msg.alert('温馨提示','系统错误!');   
						   }   
						}); 
					}
        		}
        	}
        },{
			xtype: 'tbspacer',
			width: 20
		},{
            text: '关闭',
            iconCls:'exit',
            handler: function(oButton, oEvent)
            {
    			me.hide();
            }
        },{
			xtype: 'tbspacer',
			width: 20
		}],
        items: new Ext.NavTree(nBgRoleId, clickName), 
		listeners:{
			show: function(){
				getRole(me.nBgRoleId, me.items.get(0));
				//me.nBgRoleId = win.nBgRoleId;
			}
		}
	});
};
Ext.extend(Ext.EditBgRolePanel, Ext.Panel);

//查看角色
Ext.ViewBgRolePanel = function(nBgRoleId, clickName)
{
	var me = this;
    Ext.ViewBgRolePanel.superclass.constructor.call(this, {
    	id: 'viewPanel',
    	layout:'fit',
        title: '查看角色',
        closeAction: 'hide',
        collapsible:true,
        border: false,
        tbar:[{
    		xtype: 'tbtext', 
    		text: '角色名称：'
    	},
    	{
    		xtype: 'textfield',
    		width: 300
    	},{
			xtype: 'tbspacer',
			width: 60
		},'->',{
		    text: '关闭',
		    iconCls:'exit',
		    handler: function(oButton, oEvent){
		    	me.hide();
		    }
		},{
			xtype: 'tbspacer',
			width: 20
		}],
        items: new Ext.NavTree(nBgRoleId, clickName),  
		listeners:{
			show: function(){
				getRole(me.nBgRoleId, me.items.get(0));
				//me.nBgRoleId = win.nBgRoleId;
			}
		}
	});
};

Ext.extend(Ext.ViewBgRolePanel, Ext.Panel);

function getRole(nBgRoleId, oTree)
{  
    var root = oTree.getRootNode();
    oTree.clearAll();
	Ext.Ajax.request({   
	   url: 'getBgRole.json',   
	   method: 'post',   
	   params: {nBgRoleId: nBgRoleId||0},   
	   success: function(resp, action)
	   {   
	        var obj = Ext.util.JSON.decode(resp.responseText);    
			var aComponents = oTree.ownerCt.getTopToolbar().findByType('textfield');
			aComponents[0].setValue(obj.sName);
			oTree.setSelect(root, obj.aNavTreeId);	
	    },   
	    failure:function(resp, action)
	    {   
	        Ext.Msg.alert('温馨提示','系统错误!');   
	    }   
	}); 
}

Ext.NavTree = function(nBgRoleId, clickName)
{   
	var me = this;
	Ext.NavTree.superclass.constructor.call(this, {
		dataUrl:'getBgNavTree.json',
		cls: 'role-panle',
		collapsible: false, 
        root: new Ext.tree.AsyncTreeNode({id:'TreeNav_1',expanded: true, checked:false, text:'全部权限'}),
        listeners: {
        	afterlayout: function(me){
        		//滚动条回到顶端
        		me.body.dom.scrollTop = 0;
        	},
        	click: function(oNode, oEvent){
        		var nTreeId = oNode.id.replace(/[^0-9]*/,'');
        		//判断是否为报表中心下模块，如是，则可进行批量权限分配
        		var isReportStat = false;
        		var haveChildNode = false;
        		var allParentNodes = getAllParentNodes(oNode);
        		for(var i = 0;i < allParentNodes.length;i++){
        			if(allParentNodes[i] == 14){
        				isReportStat = true;
        			}
        		}
        		alert('he');
        		//如果有子节点则表示不是最后一级则可以批量修改
        		if(oNode.childNodes.length > 0){
        			haveChildNode = true;
        		}
        	    //属于报表中心下模块且节点不是最后一级节点并且状态为选中则允许打开批量修改权限
        		if(oNode.attributes.checked == true && haveChildNode && isReportStat){
        			if(clickName == 'edit'){
        				getBatchWindowItems(nTreeId, Ext.getCmp("editPanel").nBgRoleId);
        			}
        			if(clickName == 'add'){
        				getBatchWindowItems(nTreeId);
        			}
        		}
        		//如果节点属性为已选择且为叶子节点则允许打开权限配置窗口
        		if(oNode.attributes.checked == true && oNode.leaf){
            		Ext.Ajax.request({
            			url: 'isOpenWindow.json',   
            			method: 'post',   
            			params: {treeId: nTreeId},
            			success: function(resp, action){
            				var obj = Ext.util.JSON.decode(resp.responseText);
            				if(obj.success){ 
            					if(clickName == 'query'){
            	        			getViewWindowItems(nTreeId, Ext.getCmp("viewPanel").nBgRoleId);
            	        		}
            	        		if(clickName == 'edit'){
            	        			getEditWindowItems(nTreeId, Ext.getCmp("editPanel").nBgRoleId);
            	        		}
            	        		if(clickName == 'add'){
            	        			getAddWindowItems(nTreeId);
            	        		}
            					
            				}else{
            					Ext.Msg.alert('温馨提示', obj.msg);
            				}
            			},   
            			failure:function(resp, action){   
            				alert('1');
            				Ext.Msg.alert('温馨提示','系统错误');   
            			}  
            		});
        		}
        	}
        }
        
	});
	//初始化时获取角色的报表权限值
	if(nBgRoleId != null){
		this.on("load",function(node,response){
			getRole(nBgRoleId, me);
		});
	}
	
	this.getRootNode().expand(true);
};
Ext.extend(Ext.NavTree, Ext.tree.CheckBoxTreePanel, 
{
	getValue : function()
	{
		var aComponents = this.ownerCt.getTopToolbar().findByType('textfield');
        var sName = aComponents[0].getValue();
        var aNode = this.getChecked();
        var aId = [];
        for(var i = 0, nLen = aNode.length; i < nLen; i++)
        {
        	aId.push(aNode[i].id.replace(/[^0-9]*/,''));
        }
		return {
			sName : sName,
			aNode : aId
		};
	}
});

/**
 * 获取该节点的所有父节点，包括节点本身
 * @param node
 * @returns {Array}
 */
function getAllParentNodes(node){
	var parentNodes=[];
	
	parentNodes.push(node.id.replace(/[^0-9]*/,''));
	if(node.parentNode){
	parentNodes = parentNodes.concat(getAllParentNodes(node.parentNode));
	}
	return parentNodes;
};

//获取该节点下的所有子节点
function findChildNode(node){
	var childnodes = node.childNodes;     //获取当前节点的子节点
	var nd;   //定义节点
	for(var i = 0;i < childnodes.length; i++){ //从节点中取出子节点依次遍历
		nd = childnodes[i];
		//如果节点不存在子节点，则表明为叶子节点，则可以将值放入数组中
		if(!nd.hasChildNodes() && nd.attributes.checked){
			allChildNode.push(nd.id.replace(/[^0-9]*/,''));
		}
		//如果节点存在子节点，则递归调用
		if(nd.hasChildNodes()){ //判断子节点下是否存在子节点
			findChildNode(nd); //如果存在子节点 递归
		}
	}
	return allChildNode;
}

//批量设置节点下选中的子节点的值(参数为角色报表权限树所在的panel)
function setChildrenRightValue(panel){
	var rightValue = panel.rightValue;  //获取当前角色批量修改的权限信息
	alert(Ext.encode(panel.rightValue));
	var treePanel = panel.items.items[0];    //获取报表权限树
	//var rightMap = {};       //定义所有子节点的报表权限值
	var node;
	var parentRight = "";
	//var allChildNode = [];
	for(var i in rightValue){
		var nodeId = "TreeNav_" + i;
		node = treePanel.getNodeById(nodeId);  //根据nodeId获取node对象
		parentRight = rightValue[i];   //父节点批量设置的报表权限值
		if(node.hasChildNodes()){
			allChildNode = findChildNode(node);
			for(var j = 0;j < allChildNode.length;j++){
				rightValue[allChildNode[j]] = parentRight;
			}
		}
	}
	return rightValue;
}
