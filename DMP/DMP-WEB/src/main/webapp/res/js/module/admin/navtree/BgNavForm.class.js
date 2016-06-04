var m_nWidth = 550;
var m_nHeight = 400;

//构造from
function getBgNavForm(name)
{
	//Ext.form.FormPanel
	return new Ext.FormPanel({
	    labelAlign: 'top',
	    frame:true,
	    bodyStyle:'padding:5px 5px 0',
	    fileUpload: true,
		items:[{
		        xtype:'textfield',
		        fieldLabel: '<font color="red">*</font>导航名称',
		        name: 'sName',
		        allowBlank:false,
		        validateOnBlur: true,
		        invalidText: '请输入导航名称',
		        blankText:'请输入导航名称！',
		        anchor:'95%'
	    	},{
		        xtype:'textfield',
		        fieldLabel: '超链接',
		        name: 'sUrl',
		        anchor:'95%'
	    	},{
			    xtype: 'htmleditor',
			    name: 'sRemark',
				enableFont: false,
			    fieldLabel: '备注',
			    height: 130,
			    anchor: '95%'
		    }]
	});
}

//查询可供选择的界面权限
function getCheckBoxItems(formObj, name){
	Ext.Ajax.request({   
		   url: contextPath+'/module/admin/sysmgr/getRoleCheckGroup.json',   
		   method: 'post',   
		   params: {treeId: 0},   
		   success: function(resp, action)
		   {   
		       var obj = Ext.util.JSON.decode(resp.responseText);
		       var checkBoxItems = [];
			   if(obj.length > 0){
					//获取界面权限可选项
					for(var i = 0;i < obj.length;i++){
						var boxLabel = obj[i].map.S_RIGHTNAME;
						var value = obj[i].map.S_RIGHTCODE;
						checkBoxItems.push({
							id: name + value,
							boxLabel: boxLabel,
							name : value
						});
					}
				}
			   var checkGroup = new Ext.form.CheckboxGroup({
				    	xtype: 'checkboxgroup',
				    	id: name,
				    	name: 'pageRight',
				    	fieldLabel: '界面权限',
				    	column: 6,
				    	allowBlank: true,
				    	items: checkBoxItems,
				    	anchor: '100%'
			   });
			   formObj.insert(2, checkGroup);
			   formObj.doLayout();
 		    },   
		    failure:function(resp, action)
		    {   
		        Ext.Msg.alert('温馨提示','系统错误!');   
		    }   
		}); 	
}

//增加导航
Ext.AddBgNavWindow = function()
{
	var me = this;
	me.windowName = 'addWin';
	Ext.AddBgNavWindow.superclass.constructor.call(this, {
		layout:'fit',
        width: m_nWidth,
        height: m_nHeight,
        plain: true,
        title: '增加导航结点',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items: getBgNavForm(me.windowName), 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function(){
        		var oForm = me.items.get(0).getForm();
        		
        		var rightCheck = "";  
				if (oForm.isValid()) 
				{
					var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
					var nBgTreeId = 1;
					
					if(oNode)
					{
						nBgTreeId = oNode.id.replace(/[^0-9]*/, '');
					}
					
					
					oForm.submit({
	    		        method: 'POST',
	    		        url: contextPath+'/module/admin/navtree/addBgNavTreeNode.html',
	    		        params: {nPId: nBgTreeId, pageRight: rightCheck},
	    		        waitTitle: '温馨提示',
						waitMsg: '数据正在提交中，请稍候...',
	    		        success : function(form, action) 
	    		        {
	    				 	var oParentNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
	    				 	oParentNode.appendChild(new Ext.tree.TreeNode({
	    				 		text: action.result.param.sName,
	    				 		id: 'NavTreeId_'+action.result.param.nBgTreeId,
	    				 		leaf: true
	    				 	}));
	    				 	oParentNode.expand();
	    				 	oForm.reset();
	    				 	me.hide(oParentNode.getUI().getEl());
	    				},
	    				failure : function(form, action) 
	    				{
	    					Ext.MessageBox.alert('温馨提示', action.result.msg);
	    				}
	    			});
				}else
				{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
				}
        	}
        },{
            text:'清空',
            iconCls:'reset',
            handler: function()
            {
    			me.items.get(0).getForm().reset();
        	}
        },{
            text: '关闭',
            iconCls:'exit',
            handler: function()
            {
    			me.items.get(0).getForm().reset();
    			me.hide();
            }
        }],
        listeners:{
			show : function(win)
			{
				me.items.get(0).getForm().reset();
			}
		}
	});
};
Ext.extend(Ext.AddBgNavWindow, Ext.Window);

//修改导航
Ext.EditBgNavWindow = function()
{
	var me = this;
	me.windowName = 'editWin';
	Ext.EditBgNavWindow.superclass.constructor.call(this, {
		layout:'fit',
        width: m_nWidth,
        height: m_nHeight,
        plain: true,
        title: '修改导航结点',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items: getBgNavForm(me.windowName), 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function(){
            	var oForm = me.items.get(0).getForm();
            	var rightCheck = "";  
            	/*
            	var checkPageRight = oForm.findField('pageRight').getValue();  //得到组合框
				for(var i=0; i<checkPageRight.length; i++){
					rightCheck += checkPageRight[i].name + "," + checkPageRight[i].boxLabel;  
					if(i != checkPageRight.length-1){
						rightCheck += ";";
					}
                }*/
				if (oForm.isValid()) 
				{
					oForm.submit({
	    		        method: 'post',
	    		        url: 'editBgNavTreeNode.html',
	    		        params: {nBgTreeId: me.nBgTreeId, pageRight: rightCheck},
	    		        waitTitle: '温馨提示',
						waitMsg: '数据正在提交中，请稍候...',
	    		        success : function(form, action) 
	    		        {
							g_oViewPort.items.get(0).getSelectionModel().getSelectedNode().setText(action.result.param.sName);
							me.hide();
	    				},
	    				failure : function(form, action) 
	    				{
	    					Ext.MessageBox.alert('温馨提示', action.result.msg);
	    				}
	    			});
				}else
				{
					Ext.MessageBox.alert('温馨提示', '表单验证有误，不准提交数据!');
				}
        	}
        },{
            text: '关闭',
            iconCls:'exit',
            handler: function()
            {
    			me.items.get(0).getForm().reset();
    			me.hide();
            }
        }],
		listeners:{
			show : function(win)
			{
				me.items.get(0).getForm().reset();
				getNavTreeNode(win);
				//getRight(win);
			}
		}
	});
};
Ext.extend(Ext.EditBgNavWindow, Ext.Window);

//查看导航
Ext.ViewBgNavWindow = function()
{
	var me = this;
	me.windowName = 'viewWin';
	Ext.ViewBgNavWindow.superclass.constructor.call(this, {
		layout:'fit',
        width: m_nWidth,
        height: m_nHeight,
        plain: true,
        title: '查看导航结点',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items:getBgNavForm(me.windowName), 
        buttons: [
        {
            text: '关闭',
            iconCls:'exit',
            handler: function(){
    			me.hide();
            }
        }],
		listeners:{
			show : function(win)
			{
				me.items.get(0).getForm().reset();
				getNavTreeNode(win);
				//getRight(win);
			}
		}
	});
};
Ext.extend(Ext.ViewBgNavWindow, Ext.Window);

function getNavTreeNode(win)
{
	Ext.Ajax.request({   
	   url: 'getBgNavTreeNode.json',   
	   method: 'post',   
	   params: {nBgTreeId: win.nBgTreeId},   
	   success: function(resp, action)
	   {   
	       var obj = Ext.util.JSON.decode(resp.responseText);    
			win.items.get(0).getForm().setValues({
				'sName' : obj.name,
				'sUrl' : obj.url,
				'sRemark' : obj.remark
			});
	    },   
	    failure:function(resp, action)
	    {   
	        Ext.Msg.alert('温馨提示','系统错误!');   
	    }   
	}); 	
}
//获取存在的权限信息
function getRight(win){
	Ext.Ajax.request({   
		   url: '../sysmgr/getRoleCheckGroup.json',   
		   method: 'post',   
		   params: {treeId: win.nBgTreeId},   
		   success: function(resp, action)
		   {   
		       var obj = Ext.util.JSON.decode(resp.responseText); 
		       if(obj.length > 0){
		    	   win.items.get(0).getForm().setValues({
						'isProvince' : obj[0].map.N_ISPAGECODE
					});
		    	   var map = {};
		    	   for(var i = 0;i < obj.length; i++){
		    		   map[obj[i].map.S_RIGHTCODE] = true;
		    		   Ext.getCmp(win.windowName).setValue(map);
		    		   
		    	   }
		       }
		      
				
		    },   
		    failure:function(resp, action)
		    {   
		        Ext.Msg.alert('温馨提示','系统错误!');   
		    }   
		}); 	
}


