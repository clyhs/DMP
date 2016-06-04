Ext.CenterNav = function()
{
	Ext.CenterNav.superclass.constructor.call(this, {
		id: 'center_id',
        margins:'0 0 5 0',
        minTabWidth: 100,
        tabWidth: 80,
        plugins: new Ext.ux.TabCloseMenu(),
        enableTabScroll: true,
        defaults: {autoScroll:true},
        activeTab: 0,
        region:'center'
	});
};
Ext.extend(Ext.CenterNav, Ext.TabPanel, {
	onStripMouseDown : function(e)
	{
        if(e.button !== 0)
        {
            return;
        }
        e.preventDefault();
        var t = this.findTargets(e);
        if(t.close)
        {
            if (t.item.fireEvent('beforeclose', t.item) !== false) 
            {
                t.item.fireEvent('close', t.item);
				t.item.el.dom.src = 'about:blank';
                this.remove(t.item);
            }
            return;
        }
        if(t.item && t.item != this.activeTab)
        {
            this.setActiveTab(t.item);
        }
    },
	initTab : function(item, index)
	{
        var before = this.strip.dom.childNodes[index],
            p = this.getTemplateArgs(item),
            el = before ?
                 this.itemTpl.insertBefore(before, p) :
                 this.itemTpl.append(this.strip, p),
            cls = 'x-tab-strip-over',
            tabEl = Ext.get(el);
        tabEl.hover(function()
        {
            if(!item.disabled)
            {
                tabEl.addClass(cls);
            }
        }, function()
        {
            tabEl.removeClass(cls);
        });
        if(item.tabTip)
        {
            tabEl.child('span.x-tab-strip-text', true).qtip = item.tabTip;
        }
        item.tabEl = el;
		tabEl.on('dblclick', function()
		{
			if(g_oViewPort.items.get(0).isVisible())
			{
				g_oViewPort.items.get(0).setVisible(false);
				if(g_oViewPort.items.get(1).isVisible()) g_oViewPort.items.get(1).collapse(true);
			}else
			{
				g_oViewPort.items.get(0).setVisible(true);
				if(!g_oViewPort.items.get(1).isVisible()) g_oViewPort.items.get(1).expand(true);
			}
			g_oViewPort.doLayout();
		});
        tabEl.select('a').on('click', function(e)
        {
            if(!e.getPageX())
            {
                this.onStripMouseDown(e);
            }
        }, this, {preventDefault: true});
        item.on({
            scope: this,
            disable: this.onItemDisabled,
            enable: this.onItemEnabled,
            titlechange: this.onItemTitleChanged,
            iconchange: this.onItemIconChanged,
            beforeshow: this.onBeforeShowItem
        });
    }
});

//窗口容器
Ext.IFrameComponent = Ext.extend(Ext.BoxComponent, {
	closable: true,
	iconCls:'TabIcons',
    autoScroll:true,
	onRender : function(ct, position)
	{
		this.el = ct.createChild({tag: 'iframe', id: 'FramePanel'+this.id,name: this.name, frameBorder: 0, src: this.url});
	}
});