//Tab分页管理
Ext.ux.TabCloseMenu = function(){

    var tabs = null, menu = null, ctxItem = null;
    this.init = function(tp)
	{
        tabs = tp;
        tabs.on('contextmenu', onContextMenu);
    };

    function onContextMenu(ts, item, e)
	{
        if(!menu)
		{
            menu = new Ext.menu.Menu({items:[{
                id: tabs.id + '-close',
                text: '关闭全部',
				iconCls:'close',
                handler : function()
                {
                	tabs.items.each(function(item)
                    {
                		if(item.closable)
                		{
                			if(item.el.dom.src) item.el.dom.src = 'about:blank';
                			tabs.remove(item);
                		}
                    });
                }
            },{
                id: tabs.id + '-close-others',
                text: '关闭其它',
				iconCls:'closeothers',
                handler : function()
                {
                    tabs.items.each(function(item)
                    {
                        if(item.closable && item != ctxItem)
                        {
                        	if(item.el.dom.src) item.el.dom.src = 'about:blank';
                            tabs.remove(item);
                        }
                    });
                }
            },{
                id: tabs.id + '-fresh',
                iconCls:'fresh',
                text: '刷新本页',
                handler : function()
                {
					ts.activeTab.el.dom.contentWindow.location=ts.activeTab.el.dom.src;
                }
            }]});
        }
        ctxItem = item;
        var items = menu.items;
        items.get(tabs.id + '-close').setDisabled(!item.closable);
        var disableOthers = true;
        tabs.items.each(function()
        {
            if(this != item && this.closable)
            {
                disableOthers = false;
                return false;
            }
        });
        items.get(tabs.id + '-close-others').setDisabled(disableOthers);
        menu.showAt(e.getPoint());
    }
};