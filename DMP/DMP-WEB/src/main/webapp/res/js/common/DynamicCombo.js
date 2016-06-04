//动态下拉框组件
Ext.DynamicCombo = Ext.extend(Ext.form.ComboBox,{
	constructor: function(config){
		Ext.DynamicCombo.superclass.constructor.call(this, config);
	},
	initComponent: function(){
		var config = {
				 typeAhead: true,
				 triggerAction: 'all',
				 forceSelection: true,
				 mode: 'local',
				 width:this.width,
				 emptyText:'请选择',
				 store: this.store,
				 valueField: this.valueField,
				 displayField: this.displayField,
				 itype: 'combobox',
				 id: this.id
				
		}
		Ext.apply(this, config);
		Ext.apply(this.initialConfig, config);
		Ext.DynamicCombo.superclass.initComponent.apply(this, arguments);
	},
	onRender: function(ct, position){
		Ext.DynamicCombo.superclass.onRender.call(this, ct, position);
	}
});
