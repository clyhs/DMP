
Ext.onReady(function(){
	var userType = "";
	var province = "";
	var provinceId = "";
	var endTime = "";
	var nowDate = new Date();
	if(parent.Ext.McpWeekId == undefined){
		endTime = nowDate.getFullYear() + "-" + (nowDate.getMonth()+1) + "-" + nowDate.getDate();
	}else{
		idName = 'Frame_' + parent.Ext.McpWeekId;
	    iframe  = parent.frames[idName];

	    userType = iframe.Ext.getCmp("userType").getValue();   //获取用户类型
		province = iframe.Ext.getCmp("province").getRawValue();  
		provinceId = iframe.Ext.getCmp("province").getValue();
		endTime = iframe.Ext.getCmp("endTime").getRawValue();   //获取截止时间
	}
	
	if(userType == 0){
		userType = "";
	}
	if(province == '全部'){
		province = "";
	}
	if(endTime.length == 0){
		Ext.MessageBox.alert('温馨提示', '请输入截止时间！');
		return;
	}

	Ext.define('McpWeek',{
		extend: 'Ext.data.Model',
        fields: [{
            name: 'CURRENTTIME',
            type: "string"
        },{
            name: '会员到达数',
            type: "float"
        }]
    });

    var store = Ext.create('Ext.data.Store',{
    	model:'McpWeek',
    	autoLoad: false, 
		proxy :{
			 type : 'ajax',
		     url : '../common/getReportChartForPro.json?reportId=MUSERSTATBILL',
		     getMethod: function(){ return 'POST';},
		     reader:{
		    	 type:'json',
		    	 root:'data'
		     } 
		}
    });

    
    var lineChart = Ext.create('Ext.LineChart',{
		 yField: ['会员到达数'],
		 xField: 'CURRENTTIME',
		 unit: ['人'],
		 xTitle: '',
		 yTitle: '',
		 lineStore: store
	 });

    var lineWidth = this.frames.innerWidth==undefined?document.documentElement.clientWidth:this.frames.innerWidth;
    var lineHeight = this.frames.innerHeight==undefined?document.documentElement.clientHeight:this.frames.innerHeight;
    lineChart.setWidth(lineWidth);
	lineChart.setHeight(lineHeight);
	
    store.on('beforeload',function(){
		lineChart.el.mask('数据加载中...');
	});
	store.on('load',function(){
		lineChart.el.unmask();
	});
    store.load({params:{
    	userType: userType,
		province: province,
		provinceId: provinceId,
		endTime: endTime
    }});
	
   Ext.EventManager.onWindowResize(function(width, height){
   	  lineChart.setWidth(width);
   	  lineChart.setHeight(height);
   }); 
});

