<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ include file="/WEB-INF/views/taglibsForActiviti.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>服务台-已结束的流程</title>
  	<jsp:include page="../_LayoutCommonExtJS.jsp"/>
  	
  	
  	
  	<style type="text/css">
  		tr.x-grid-record-red .x-grid-td {
			   background: #E6D1E3;
			}
			tr.x-grid-record-yellow .x-grid-td{
			   background: #F3FEC2;
			}
		tr.x-grid-record-green .x-grid-td{
		   background: #92FCCC;
		}
  	</style>
	<script type="text/javascript">

		var ctx = "${ctx}";
		
		Ext.onReady(function(){
			
			Ext.QuickTips.init();

			//--条件参数
			  var from ;	
              var to ;
              var username ;

              
			
	      
	        var khdwField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '客户单位',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var htField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '合同',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var zclxField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '资产类型',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var fwField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '服务',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var fwspField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '服务水平',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var yxjField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '优先级',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var zcbsField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '资产标识',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var gjzField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '关键字',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	      
	        var sjflField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '事件分类',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        var gzjbField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '故障级别',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	        var btnSearch = new Ext.button.Button({
	            text: '查询',
	            iconCls: 'x-button-read',
	            handler: function () {
	            	store.load();
	            }
	        });


	     
	        var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [khdwField,'-',htField,'-',zclxField,fwField]
	        });
	     
	        var tbar2 = Ext.create('Ext.toolbar.Toolbar', {
	        	 items: [fwspField,'-',fwspField,'-',zcbsField,gjzField]
	        });
	     
	        var tbar3 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [sjflField,'-',gzjbField,'->', btnSearch]
	        });



	      

	        var btnTrace = new Ext.button.Button({
	            text: '跟踪',
	            iconCls: 'x-button-read',
	            handler: function () {
		            var pid =  checkBox(sm,'processInstanceId');
			        var pdid =   checkBox(sm,'processDefinitionId');
	            	graphTrace({pid:pid,pdid:pdid});
	            }
	        });
	       
	        var tbar = Ext.create('Ext.toolbar.Toolbar', {
	            items: [btnTrace]
	        });
	       
        
        Ext.define('ProcessModel', {
            extend: 'Ext.data.Model',
            fields: [
                {name: 'historicProcessInstance'},
                {name: 'historicProcessInstance.id',  mapping:'historicProcessInstance.id'},
                {name: 'historicProcessInstance.startTime',  mapping:'historicProcessInstance.startTime'},
                {name: 'historicProcessInstance.endTime',  mapping:'historicProcessInstance.endTime'},
                {name: 'historicProcessInstance.businessKey',  mapping:'historicProcessInstance.businessKey'},
                {name: 'historicProcessInstance.deleteReason',  mapping:'historicProcessInstance.deleteReason'},
                
                {name: 'processDefinition'},
                {name: 'processDefinition.version',  mapping:'processDefinition.version'},
              
                
                {name: 'clientUint'},
                {name: 'contract'},
                {name: 'priority'},
                {name: 'title'},
                {name: 'businessType'},
                {name: 'businessName'},
                {name: 'activityName'}
            ]

        });
	        
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'ProcessModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>workflow/process/finished/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
                	var params = {};
               		Ext.apply(store.proxy.extraParams, params);
            		
                }
            }
        });
     


      
        
        var sm = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var grid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: store,
        //    dockedItems: [tbar],
            selModel: sm,
            columnLines: false,
            viewConfig:{getRowClass:changeRowClass},
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '优先级', dataIndex: 'priority', align: 'center', flex:2},
					{ header: '客户单位', dataIndex: 'clientUint', align: 'center', flex:1 },
                    { header: '合同', dataIndex: 'contract', align: 'center', flex:2},
                    { header: '标题', dataIndex: 'title', align: 'center', flex:1},
                    { header: '服务项', dataIndex: 'businessType', align: 'center', flex:2},
                    { header: '开始时间', dataIndex: 'historicProcessInstance.startTime', align: 'center', flex:2},
                    { header: '结束时间', dataIndex: 'historicProcessInstance.endTime', align: 'center', flex:2}
				],
				bbar: createPage(store)
        });
        
        


        var panel=	Ext.create('Ext.panel.Panel', {
		    width: 500,
		    height: 400,
		    layout: 'border',
		    items: [{
		    	 title: '历史查询',
		        region:'north',
		        xtype: 'panel',
		        margins: '5 0 0 5',
		        collapsible: false,   // make collapsible
		        split: false,
		        layout: 'fit',
		        dockedItems: [tbar1, tbar2,tbar3]
		    },{
		        title: '已结束服务处理历史',
		        region: 'center',     // center region is required, no width/height specified
		        xtype: 'panel',
		        layout: 'fit',
		        margins: '5 5 0 0',
		        items:grid
		    }],
		});  
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: panel
        });
        
        
	       
	    
	    store.on('load', function(){
	    	sm.deselect(sm.getSelection());
	    });
	    
//函数区        
//----------------------------------------------------------------------------------------------------------
        
	    function changeRowClass(record, rowIndex, rowParams, store){
	        if (record.get("type") == "2") {        
	            return 'x-grid-record-red';
	        }
	    } 

	    
        function typeRenderer(value) {
        	
            if (value=='2')  //异常日志
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/action_stop.gif "  alt="异常日志 " />';
            else  
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/accept.png   "  alt="入口日志" />';
        }
        
        
      
        
        
		//启动请假流程
        function createFlow(){
        	var title = '请选择服务';
        	var url = '<%=path%>/vacation/toAdd';
        	ShowWindow(store, title, url, 600, 380);

         }
		//启动请假流程
        function createWjFlow(){
        	var title = '请选择服务';
        	var url = '<%=path%>/otherFlow/toAdd?processKey=wj-process';
        	ShowWindow(store, title, url, 600, 380);

         }
        
        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
