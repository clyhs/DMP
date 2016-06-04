Ext.tree.CheckBoxTreePanel = Ext.extend(Ext.tree.TreePanel, {
	region: 'center',
	border: false,
	collapsible: true,
	collapseMode: 'mini',
	margins: '0 0 5 0',
	cmargins: '0 5 5 5',
	animate: true, 
//    autoScroll: true,
//    enableDD: true,
    bodyStyle :'overflow-x:visible;overflow-y:scroll', 
    containerScroll: true,
    enableDD: true,
    line: true,
    rootVisible: true,	
	getValue : function()
	{
        var aNode = this.getChecked();
        var aId = [];
        for(var i = 0, nLen = aNode.length; i < nLen; i++)
        {
        	aId.push(aNode[i].id.replace(/[^0-9]*/, ''));
        }
		return aId;
	}
	,initComponent : function()
	{
		Ext.tree.CheckBoxTreePanel.superclass.initComponent.call(this);
		this.on('checkchange', function(oNode, checked)
		{
			var oTree = oNode.getOwnerTree();
    		if(oTree.getRootNode() == oNode)
    		{
    			oTree.allSelect(oNode, oNode.getUI().isChecked());
    		}else
    		{
    	        if(checked)
    	        {
    	        	//oTree.childsSelect(oNode);
    	        	oTree.allChildSelect(oNode, oNode.getUI().isChecked());
    				oTree.upSelect(oNode);
    	        }else
    	        {
    	        	oTree.downUnSelect(oNode);
    	        }
    	    }
    	});
	}
	,childsSelect : function(oNode)
	{
		var aChildNodes = oNode.childNodes;
		for(var i = 0, nLen = aChildNodes.length; i < nLen; i++)
		{
			var oUI = aChildNodes[i].getUI();
			if(!oUI.isChecked())
			{
				this.setCBChecked(oUI, true);
			}
		}
	}
	,allChildSelect : function(oNode)
	{
		var aChildNodes = oNode.childNodes;
		for(var i = 0, nLen = aChildNodes.length; i< nLen; i++)
		{
			var oUI = aChildNodes[i].getUI();
			if(!oUI.isChecked())
			{
				this.setCBChecked(oUI, true);
			}
			
			if(aChildNodes[i].hasChildNodes())
			{
				this.allChildSelect(aChildNodes[i]);
			}
		}       
	}
	,upSelect : function(oNode)
	{
		var oParentNode = oNode.parentNode;
		if(oParentNode)
		{
			var oUI = oParentNode.getUI();
			if(!oUI.isChecked())
			{
				this.setCBChecked(oUI, true);
			}
			this.upSelect(oParentNode);
		}
	}
	,downUnSelect : function(oNode)
	{
		var oUI = oNode.getUI();
		if(oUI.isChecked())
		{
			this.setCBChecked(oUI, false);
		}
		
		var aChildNodes = oNode.childNodes;
		for(var i = 0, nLen = aChildNodes.length; i < nLen; i++)
		{
			this.downUnSelect(aChildNodes[i]);
		}
	}
	,allSelect : function(oNode, bChecked)
	{
		var oUI = oNode.getUI();
		this.setCBChecked(oUI, bChecked);
		
		var aChildNodes = oNode.childNodes;
		for(var i = 0, nLen = aChildNodes.length; i < nLen; i++)
		{
			this.allSelect(aChildNodes[i], bChecked);
		}
	}
	,setSelect : function(oNode, aTreeId)
	{
		if(aTreeId.length == 0) return;
		var id = parseInt(oNode.id.replace(/[^0-9]*/, ''), 10);
		if(aTreeId.indexOf(id) > -1)
		{
			var oUI = oNode.getUI();
			if(!oUI.isChecked())
			{
				this.setCBChecked(oUI, true);
			}
			aTreeId.remove(id);
		}
		
		var aChildNodes = oNode.childNodes;
		for(var i = 0, nLen = aChildNodes.length; i < nLen; i++)
		{
			this.setSelect(aChildNodes[i], aTreeId);
		}
	}
	,clearSelect : function(oNode)
	{
		var oUI = oNode.getUI();
		if(oUI.isChecked())
		{
			this.setCBChecked(oUI, false);
		}
		
		var aChildNodes = oNode.childNodes;
		for(var i = 0, nLen = aChildNodes.length; i < nLen; i++)
		{
			this.clearSelect(aChildNodes[i]);
		}
	}
	,clearAll : function()
	{
		this.clearSelect(this.getRootNode());
	}
	,setCBChecked : function(oUI, bChecked)
	{
		oUI.checkbox.checked = bChecked;
		oUI.checkbox.defaultChecked = bChecked;
		oUI.node.attributes.checked = bChecked;
	}
});			
