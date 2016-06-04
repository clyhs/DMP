Ext.BLANK_IMAGE_URL = contextPath+'/res/images/common/default/s.gif';

var g_oViewPort = null;
Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
	g_oViewPort = new Ext.Viewport({
		layout: 'border',
		items: [new Ext.BgRoleTree(), new Ext.BgRolePermissionTree()]
	});
});
Ext.override(Ext.form.RadioGroup, {
	setValue: function(v){
		this.eachItem(function(item){   
			item.setValue(item.getRawValue() == v);   
		});
	},
	getValue: function(){
		var value = 3;
		this.eachItem(function(item){
			if(item.checked){
				value = item.getRawValue();
				return false;
			}
		});

		return value;
	}

});

Ext.override(Ext.tree.TreeNodeUI, {  
    onDblClick: function(e) {  
        e.preventDefault();  
        if (this.disabled) {  
            return;  
        }  
        if (this.fireEvent("beforedblclick", this.node, e) !== false) {  
            if (!this.node.isLeaf()) {  
                if (this.node.isExpandable()) {  
                    if (this.node.isExpanded()) {  
                        this.node.collapse(false, 'anim');  
                    }  
                    else {  
                        this.node.expand(false, 'anim');  
                    }  
                }  
            } else {  
            }  
            this.fireEvent("dblclick", this.node, e);  
        }  
    }  
}); 