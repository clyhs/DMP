
var phoneTypeData=[['全部',''],['Android','Android']
    ,['iOS','iOS']
    ,['Windows Phone','Windows Phone']
    ,['Symbian', 'Symbian']
];

var phoneTypeStore = new Ext.data.SimpleStore({
    fields: ['phoneTypeId', 'phoneTypeName'],
    data : phoneTypeData
});

var phoneTypeCombo = new Ext.form.ComboBox({
    typeAhead: true,
    triggerAction: 'all',
    forceSelection: true,
    mode: 'local',
    width:125,
    emptyText:'请选择类型'   ,
    store: phoneTypeStore,
    valueField: 'phoneTypeName',
    displayField: 'phoneTypeId'
});

/*---------------year settings-----------------*/
var currentTime = new Date();
var now = currentTime.getFullYear();
var years = [];
var y = now;
while(y >= 1990){
    years.push([y]);
    y--;
}
var yearStore = new Ext.data.SimpleStore({
    fields: [ 'year'],
    data: years
});

var YearComboBox =  Ext.extend(Ext.form.ComboBox,{
    typeAhead: true,
    triggerAction: 'all',
    forceSelection: true,
    editable: false,
    lazyRender: true,
    mode: 'local',
    width:60,
    store: yearStore,
    valueField: 'year',
    displayField: 'year' ,
    value : now ,
    constructor : function(){
        YearComboBox.superclass.constructor.apply(this, arguments);
    },
    onDestroy : function() {
        YearComboBox.superclass.onDestroy.apply(this, arguments);
    }
} )
Ext.reg('yearcombobox', YearComboBox);


