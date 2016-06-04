Ext.define('Ext.LineChart',{
    extend: 'Ext.chart.Chart',
	initComponent: function() {
		 var me = this;
		 var length = this.yField.length;
		 var obj = [];
		 var s = "";
		 var rCount = 0;
         for(var i = 0 ;i <length; i++){
        	var tip = Ext.create('Ext.tip.ToolTip', {
				trackMouse: true,
				width: 150,
		        autoHeight: true,
		        renderer: function(storeItem, item){
		           this.setTitle(storeItem.get(me.xField));
		           this.update(storeItem.get(item.series.yField) + item.series.unit);
		        }
        	}); 
            s = {
				type : 'line',
				fill: true,
				smooth:true,
				highlight: {
					size: 7,
					radius:7
				},
				style: {  
                    opacity: 0.5  
				}, 
				axis : 'left',
				unit: me.unit ? me.unit[i] : "",
				xField : me.xField,
				yField : me.yField[i],
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips:tip 
			};
            obj.push(s);
		  }

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
	    	 theme: 'Base:gradients',
	    	 renderTo:Ext.getBody(),
	    	 axes: [
	    	   {
	    		 type: 'Numeric',
	    		 position: 'left',
	    		 fields: me.yField, 
	    		 title: me.yTitle,
	    		 minimum: 0,
	    		 label: {
	    			 renderer: function(v){
	    				 if(me.reportId != 'MUSERSTAT'){
	    					 return v;
	    				 }
	    				//特殊处理会员到达用户数(平台)图表的Y轴
	    				 if(v == 0){
	    					 str= v; 
	    					 rCount = 0;
	    				 }else{
	    				     str = 500+((rCount-1)*50)+'万';
	    				 }
		    			 rCount++; 
	    				 return str;
	    			 }
	    		 },
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
	    		  fields: me.xField,
	    		  title: me.xTitle,
	    		  grid: true,
	    		  label: {
	    			  renderer: function(name){
	    				  var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;     
		    			  if(name == undefined){
		    				  name = "";  
		    			  }
		    			  if(name.match(reg)){
		    				  name = name.substring(8, 10);
		    			  }
		    			  return name;
		    		  }
	    		  } 
	    	   }
	    	 ],
	    	 series: obj
		 }
		 Ext.apply(this, config);
		 Ext.LineChart.superclass.initComponent.apply(this, arguments);
	 },
	//处理当Y轴数据全部为0的时候纵坐标没显示问题
	 renderY: function(redraw){
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
	    	 //当Y轴所有值为0就设置线图maximum为1000,Y轴才会显示
	    	 this.axes.items[0].maximum = 1000;
	    	 //store load之后才能redraw,否则报错
	    	 if(redraw){
	    		 this.redraw(true); 
	    	 }
	     }
	 },
	 onRender: function() {
		 this.renderY(false);
	     this.callParent(arguments);
	     //特殊处理this.store经过拼凑传进来的情况,如CP结算类的报表
	     this.store.on('load',function(){
	    	 this.renderY(true); 
	     },this);
	  }
   });