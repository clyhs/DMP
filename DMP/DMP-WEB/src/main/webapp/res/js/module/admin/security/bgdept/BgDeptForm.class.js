var m_nWidth = 550;
var m_nHeight = 260;

function getBgDeptForm()
{
	return new Ext.FormPanel({
	    labelAlign: 'top',
	    frame:true,
	    bodyStyle:'padding:5px 5px 0',
	    fileUpload: true,
		items:[{
		        xtype:'textfield',
		        fieldLabel: '<font color="red">*</font>部门名称',
		        name: 'sName',
		        allowBlank:false,
		        validateOnBlur: true,
		        invalidText: '请输入部门名称',
		        blankText:'请输入部门名称！',
		        anchor:'100%'
	    	},{
			    xtype:'htmleditor',
			    name: 'sRemark',
				enableFont: false,
			    fieldLabel:'备注',
			    height:90,
			    anchor:'100%'
		}]
	});
}

//增加部门
Ext.AddBgDeptWindow = function()
{
	var me = this;
	Ext.AddBgDeptWindow.superclass.constructor.call(this, {
		layout:'fit',
        width:m_nWidth,
        height:m_nHeight,
        plain: true,
        title: '增加部门',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items: getBgDeptForm(), 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function(){
        		var oForm = me.items.get(0).getForm();
				if (oForm.isValid()) 
				{
					var oNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
					var nBgDeptId = 1;
					if(oNode)
					{
						nBgDeptId = oNode.id.replace(/[^0-9]*/,'');
					}
					
					
    				oForm.submit({
	    		        method: 'post',
	    		        url: 'addBgDept.html',
	    		        params: {nPId: nBgDeptId},
	    		        waitTitle: '温馨提示',
						waitMsg: '数据正在提交中，请稍候...',
	    		        success : function(form, action) 
	    		        {
	    				 	var oParentNode = g_oViewPort.items.get(0).getSelectionModel().getSelectedNode();
	    				 	oParentNode.appendChild(new Ext.tree.TreeNode({
	    				 		text: action.result.param.sName,
	    				 		id: 'BgDeptTreeId_'+action.result.param.nBgDeptId,
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
        }]
	});
};
Ext.extend(Ext.AddBgDeptWindow, Ext.Window);

//修改部门
Ext.EditBgDeptWindow = function()
{
	var me = this;
	Ext.EditBgDeptWindow.superclass.constructor.call(this, {
		layout:'fit',
        width:m_nWidth,
        height:m_nHeight,
        plain: true,
        title: '修改部门',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items: getBgDeptForm(), 
        buttons: [{
            text:'提交',
            iconCls:'save',
            handler: function()
            {
            	var oForm = me.items.get(0).getForm();
				if (oForm.isValid()) 
				{
					oForm.submit({
	    		        method: 'post',
	    		        url: 'editBgDept.html',
	    		        params: {nBgDeptId: me.nBgDeptId},
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
				getBgDeptNode(win);
			}
		}
	});
};
Ext.extend(Ext.EditBgDeptWindow, Ext.Window);

//查看部门
Ext.ViewBgDeptWindow = function()
{
	var me = this;
	Ext.ViewBgDeptWindow.superclass.constructor.call(this, {
		layout:'fit',
        width:m_nWidth,
        height:m_nHeight,
        plain: true,
        title: '查看部门',
        closeAction: 'hide',
        modal: true,
        iconCls: 'form',
        items:getBgDeptForm(), 
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
				getBgDeptNode(win);
			}
		}
	});
};
Ext.extend(Ext.ViewBgDeptWindow, Ext.Window);

function getBgDeptNode(win)
{
	Ext.Ajax.request({   
	   url: 'getBgDept.json',   
	   method: 'post',   
	   params: {nBgDeptId: win.nBgDeptId},   
	   success: function(resp, action)
	   {   
	       var obj = Ext.util.JSON.decode(resp.responseText);    
			win.items.get(0).getForm().setValues({
				'sName' : obj.name,
				'sRemark' : obj.remark
			});
	    },   
	    failure:function(resp, action)
	    {   
	        Ext.Msg.alert('温馨提示','系统错误!');   
	    }   
	}); 	
}


