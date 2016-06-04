   Ext.define('Ext.chart.theme.CustomTheme', {
        extend: 'Ext.chart.theme.Base',
        constructor: function(config) {
            this.callParent([Ext.apply({
                background: false,
                axis: {
                    stroke: '#444',
                    'stroke-width': 3
                },
                axisLabelTop: {
                    fill: '#444',
                    font: '12px Arial, Helvetica, sans-serif',
                    spacing: 2,
                    padding: 5,
                    renderer: function(v) { return v; }
                },
                axisLabelRight: {
                    fill: '#444',
                    font: '12px Arial, Helvetica, sans-serif',
                    spacing: 2,
                    padding: 5,
                    renderer: function(v) { return v; }
                },
                axisLabelBottom: {
                    fill: '#444',
                    font: '12px Arial, Helvetica, sans-serif',
                    spacing: 2,
                    padding: 5,
                    renderer: function(v) { return v; }
                },
                axisLabelLeft: {
                    fill: '#444',
                    font: '12px Arial, Helvetica, sans-serif',
                    spacing: 2,
                    padding: 5,
                    renderer: function(v) { return v; }
                },
                axisTitleTop: {
                    font: 'bold 18px Arial',
                    fill: '#444'
                },
                axisTitleRight: {
                    font: 'bold 18px Arial',
                    fill: '#444',
                    rotate: {
                        x:0, y:0,
                        degrees: 270
                    }
                },
                axisTitleBottom: {
                    font: 'bold 18px Arial',
                    fill: '#444'
                },
                axisTitleLeft: {
                    font: 'bold 18px Arial',
                    fill: '#444',
                    rotate: {
                        x:0, y:0,
                        degrees: 270
                    }
                },
                series: {
                    'stroke-width': 0
                },
                seriesLabel: {
                    font: '12px Arial',
                    fill: '#333'
                },
                marker: {
                    stroke: '#555',
                    fill: '#000',
                    radius: 3,
                    size: 3
                },
                seriesThemes: [{
                    fill: "#92EC00"
                }, {
                    fill: "#0772A1"
                }, {
                    fill: "#FFD200"
                }, {
                    fill: "#D30068"
                }, {
                    fill: "#7EB12C"
                }, {
                    fill: "#225E79"
                }, {
                    fill: "#BFA630"
                }, {
                    fill: "#9E2862"
                }],
                markerThemes: [{
                    fill: "#115fa6",
                    type: 'plus'
                }, {
                    fill: "#94ae0a",
                    type: 'cross'
                }, {
                    fill: "#a61120",
                    type: 'plus'
                }]
            }, config)]);
        }
    }); 


Ext.define('Ext.PieChart',{
    extend: 'Ext.chart.Chart',
	initComponent: function() {
		 var me = this;
		 var config = {
			 region: 'center',
		     iconCls:'tabPieChartIcon',
			 title:me.title,
			 animate: true,
			 style: 'background:#F5FFFA',
			 theme: 'CustomTheme',
		     legend: {
		       position: 'right'
		     },
		     series: [{
		            type: 'pie',
		            angleField: me.yField,
		            showInLegend: true,
		            tips: {
		                trackMouse: true,
		            	width: 230,
		            	autoHeight: true,
		                renderer: function(storeItem, item) {
		                    var total = 0;
		                    me.store.each(function(rec) {
		                        total += Number(rec.get(me.yField));
		                    });
		                    var title = "";
		                    var titleTip = "";
		                    if(storeItem.get(me.xField).length > 10){
    	                    	titleTip = storeItem.get(me.xField).replace("\n","").substring(0,10) + '......';
    	                    }else{
    	                    	titleTip = storeItem.get(me.xField);
    	                    }
		                    if(me.unit != undefined){
		                        title = titleTip +" " + storeItem.get(me.yField) + "(" + me.unit + ")   " 
		                        + ((storeItem.get( me.yField)/total)*100).toFixed(2) + "%";
		                    }else{
		                    	 title = titleTip +" " + storeItem.get(me.yField) + " 百分比  " 
			                     + ((storeItem.get( me.yField)/total)*100).toFixed(2) + "%";
		                    }
		                    
		                    this.update(title);
		                }
		            },
		            highlight: {
		                segment: {
		                    margin: 20
		                }
		            },
		            label: {
		                field:  me.xField,
		                display: 'rotate',
		                contrast: true,
		                font: '12px Arial',
		                renderer:function(item){
		                    var total = 0;
		                    var itemTotal = 0;
		                    me.store.each(function(rec) {
		                        total += Number(rec.get(me.yField));
		                        if(rec.get(me.xField) == item){
		                           itemTotal += Number(rec.get(me.yField)); }
		                    });
		                    if(total == 0){  
		                        return '没有要显示的数据';   
		                    }
		                    if(itemTotal == 0){
		                  	  return '';  
		                    }
		                    if(me.unit == undefined){
		                    	 return item + '  '+itemTotal + ' 百分比   '+ ((itemTotal/total)*100).toFixed(2) + '%';
			                }
		                    return item + '  '+itemTotal + '(' + me.unit + ')  '+ ((itemTotal/total)*100).toFixed(2)+ '%';
		                }
		            }
		        }]
		 }
		 Ext.apply(this, config);
		 Ext.PieChart.superclass.initComponent.apply(this, arguments);
	 },
	 onRender: function() {
		 this.callParent(arguments);
	 }
   });