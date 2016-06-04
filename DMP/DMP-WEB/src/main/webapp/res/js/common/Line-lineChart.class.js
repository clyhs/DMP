/**
  调用Ext.chart.TipsChart类例子
  	var tipChart = Ext.create('Ext.chart.TipsChart',{
 		 yField: 'NUM',
		 xField: 'CURRENTTIME',
		 unit: '次',//如果数据没有单位不必定义unit
		 xTitle: '年月',
		 yTitle: '数量',
		 lineStore: lineStore,
		 tipLineStore: tipLineStore,
		 tipStoParams: {
			    userType: userType,
				province: province,
				provinceId: provinceId,
				endTime: endTime,
				memberType: memberType
		 }
	 });
 */

Ext.define('Ext.chart.TipsChart',{
		 extend: 'Ext.chart.Chart',
		 initComponent: function() {
			//重写鼠标经过线图上的圆点方法，使鼠标经过时折线图不出现。
			 Ext.override(Ext.chart.series.Series,{onItemMouseOver: function(item){
				 var me = this;
			     if(item.series == me){
			    	 if(me.highlight){
			    		 me.highlightItem(item);
					 }
					 if(item.series.id == 'seriesId'){
						 if (me.tooltip) {
							 me.showTip(item);
				         }
					 }
				 }
			 }});
			 
			//重写鼠标移出方法，去掉使饼图隐藏的源码。
			Ext.override(Ext.chart.series.Series,{onItemMouseOut: function(item){
				var me = this;
			    if (item.series === me) {
			    	me.unHighlightItem();
			    }
			}});
				
			var me = this;
			//折线图Tip
			var lineTip = Ext.create('Ext.tip.ToolTip', {
				trackMouse: true,
		        width: 150,
		        autoHeight: true, 
		        renderer: function(storeItem, item) {
		        	this.setTitle(storeItem.get(me.xField));
			        this.update(storeItem.get(me.tipyField) + item.series.unit);
		        }
			});
			//折线图
			var lineChart = Ext.create('Ext.chart.Chart',{ 
				 width: window.innerWidth ? window.innerWidth-100 : 700,
				 height: window.innerHeight ? window.innerHeight-50 : 420,
//				 legend: {
//				     position: 'right'
//				 },		
				 style: 'background:#F5FFFA',
				 animate: false,
				 store: this.tipLineStore,
				 shadow: true,
				 theme: 'Base:gradients',
				 axes: [
				    	   {
				    		 type: 'Numeric',
				    		 position: 'left',
				    		 fields: this.tipyField,
				    		 title: this.yTitle,
				    		 minimum: 0,
				    		 grid: {
				    			//奇数行
					  			   odd: {
									   opacity: 1,
									   fill: '#FFFF99',//表格内的填充色  
					                   stroke: '#FF3300',//表格线颜色  
					                   'stroke-width': 0.5//表格线宽度
								   },
								   //偶数行
								   even:{
									   opacity: 0,//透明  
					                   stroke: '#6600CC',//表格线颜色  
					                   'stroke-width': 0.5//表格线宽度 
								   }
						    }
				    	   },{
				    		  type: 'Category',
				    		  position: 'bottom',
				    		  fields: this.xField,
				    		  title: this.xTitle,
				    		  grid: true,
				    		  label: {
//				    			  rotate: {
//				    				  degrees: 270
//				    			  },
				    			  renderer: function(name){
					    			  if(name == undefined){
					    				  name = "";  
					    			  }
					    			  return name;
					    		  }
				    		  } 
				    	   }
				    	 ],
				 series: [{
					id:'seriesId',
					type: 'line',
					highlight: {
						size: 7,
						radius:7
					},
					style: {  
	                    opacity: 0.5  
					}, 
					axis : 'left',
					unit: me.unit ? me.unit : "",
				    xField: this.xField,
				    yField: this.tipyField,
				    fill: true,
					smooth:true,
				    markerConfig: {
				    	type : 'circle',
						size : 0,
						radius : 4,
						'stroke-width' : 0
				    },
				    tips: lineTip
				 }]
			});
		
			me.tipLineStore.on('beforeload',function(){
				me.el.mask('数据加载中...');
			});
			me.tipLineStore.on('load',function(){
				me.el.unmask();
			});
			this.lineChart = lineChart;
			//线图tip
			var tip = Ext.create('Ext.tip.ToolTip', { 
				showTip: true,//自定义了一个属性,兼容IE8 鼠标经过，饼图会隐藏bug
	    		autoHide: false,
	    		closable: true,
	    		maxWidth: 1000,
	    		layout: 'fit',
	    		items: {
	    			xtype: 'container',
	    			items: [lineChart]
	    		},
	    		renderer: function(klass, item){
	    		  var storeItem = item.storeItem;
	    		  var title = "";
	    		  if(me.unit != undefined){
	    			  title = "<font size ='1' >" + storeItem.get(me.xField) + "  " 
	    			  + storeItem.get(me.yField) + "(" + me.unit + ")" + "</font>";  
	    		  }else{
	    			  title = "<font size ='1' >" + storeItem.get(me.xField) + "  " 
	    			  + storeItem.get(me.yField) + "</font>";
	    		  }
	    		  this.setTitle(title);
	    		  if(storeItem.get(me.yField) == 0){
	    			  me.lineChart.axes.items[0].maximum = 1000;
	    		  }else{
	    			  me.lineChart.axes.items[0].maximum = storeItem.get(me.yField);
	    		  }
	    		  me.tipStoParams.xField = storeItem.get(me.xField);
	    		  me.tipLineStore.load({params:me.tipStoParams});
	    		}
			});
			
			
		//线图的配置
		  var config = {
			 width:this.width ? this.width : 890,
	     	 height:this.height ? this.height : 450,
	     	 legend: {
	     		 position: 'right',
	     		 labelFont: '11px Helvetica, sans-serif'
	     	 },
	     	 style: 'background:#F5FFFA',
	    	 animate: true,
	    	 shadow: true,
	    	 store: this.lineStore,
	    	 renderTo:Ext.getBody(),
	    	 theme: 'Base:gradients',
	    	 axes: [
	    	   {
	    		 type: 'Numeric',
	    		 minimum: 0,
	    		 position: 'left',
	    		 fields: this.yField,
	    		 title: me.yTitle,
	    		 grid: {
	    			//奇数行
	    			 odd: {
						   opacity: 1,
						   fill: '#FFFF99',//表格内的填充色  
		                   stroke: '#FF3300',//表格线颜色  
		                   'stroke-width': 0.5//表格线宽度
					   },  
					 //偶数行
					   even:{
						   opacity: 0,//透明  
		                   stroke: '#6600CC',//表格线颜色  
		                   'stroke-width': 0.5//表格线宽度 
					   } 
			    }
	    	   },{
	    		  type: 'Category',
	    		  position: 'bottom',
	    		  fields: this.xField,
	    		  title: me.xTitle,
	    		  grid: true,
	    		  label: {
	    			  font: '12px Arial',
//	    			  rotate: {
//	    				  degrees: 270
//	    			  },
	    			  renderer: function (name){
	    				  var v = name.substring(6,8);
	    				  return v;
	    			  }
	    		  }
	    	   }
	    	 ],
	    	 series: [
	    	   {
	    		 type: 'line',
	    		 axis: 'left',
	    		 fill: true,
	    		 smooth:true,
	    		 xField: this.xField,
	    		 yField: this.yField,
	    		 tips: tip,
	    		 highlight: {
    				 size: 7,
				     radius:7
			     },
			     style: {  
	                    opacity: 0.5  
				 }, 
	    		 markerConfig: {
	    			 type: 'circle',
	    			 size: 0,
	    			 radius: 4,
	    			 'stroke-width': 0
	    		 },
	    		 listeners: {//当鼠标弹起时显示饼图
	    			"itemmouseup": function(item){
	    			    var me = this;
	    		        if (item.series === me) {
	    		            if (me.highlight) {
	    		                me.highlightItem(item);
	    		            }
	    		            if (me.tooltip) {
	    		                me.showTip(item);
	    		            }
	    		        }
	    			} 
	    		 }
	    	   }
	    	 ]
		  }
		 Ext.apply(this, config);
		 Ext.chart.TipsChart.superclass.initComponent.apply(this, arguments);
	    },
	    onRender: function() {
	    	 //处理当Y轴数据全部为0的时候纵坐标没显示问题
			 var records = this.store.getRange();
			 var yField = this.yField;
			 var flag = false;
		     for(var i = 0;i<records.length;i++){
		    	 for(var j = 0;j<yField.length;j++){
		    		 var r = records[i];
		    		 var y = yField[j];
		    		 if(r.get(y)>0){
		    			 flag = true;
		    			 continue;
		    		 }
		    		 if(flag){
		    			 continue;
		    		 }
		    	 }
		     }
		     if(!flag && records.length>0 ){
		    	 this.axes.items[0].maximum = 1000;
		    	 this.lineChart.axes.items[0].maximum = 1000;
		     }
	         this.callParent(arguments);
	    }
	 });
