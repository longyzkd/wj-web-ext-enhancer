<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ include file="/WEB-INF/views/taglibsForActiviti.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>服务台-运行中的流程</title>
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



	        var btnCreateFlow = new Ext.button.Button({
	            text: '服务请求（请假）',
	            iconCls: 'x-button-read',
	            handler: createFlow
	        });

	        var btnwj = new Ext.button.Button({
	            text: 'wj-process（自定义一个流程）',
	            iconCls: 'x-button-read',
	            handler: createWjFlow
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
	        var btnTraceDiagram = new Ext.button.Button({
	            text: 'diagram-viewer',
	            iconCls: 'x-button-read',
	            handler: function () {
		            var pid =  checkBox(sm,'processInstanceId');
			        var pdid =   checkBox(sm,'processDefinitionId');
	            	graphTraceDiagram({pid:pid,pdid:pdid});
	            }
	        });


	        var btnSuspend = new Ext.button.Button({
	            text: '挂起',
	            iconCls: 'x-button-read',
	            handler: suspend
	        });

	        var btnRecover = new Ext.button.Button({
	            text: '恢复',
	            iconCls: 'x-button-read',
	            handler: recover
	        });
	        var tbar = Ext.create('Ext.toolbar.Toolbar', {
	            items: [btnCreateFlow,btnwj,'-',btnTrace,btnTraceDiagram,'->',btnSuspend,btnRecover]
	        });
	       
        
        Ext.define('ProcessModel', {
            extend: 'Ext.data.Model',
            fields: [
                {name: 'id',  type: 'string'},
                {name: 'processInstanceId', type: 'string'},
                {name: 'processDefinitionId',   type: 'string'},
                {name: 'suspended'},
                {name: 'name', type: 'string'},
                {name: 'description', type: 'string'},
                {name: 'applyDateStr'},
                {name: 'version'},
                
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
                url: '<%=basePath%>workflow/process/running/data',
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
            dockedItems: [tbar],
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
                    { header: '流程实例ID', dataIndex: 'processInstanceId', align: 'center', flex:2},
                    { header: '当前节点', dataIndex: 'activityName', align: 'center', flex:2},
                    { header: '创建时间', dataIndex: 'applyDateStr', align: 'center', flex:2},
                    { header: '状态', dataIndex: 'suspended', align: 'center', flex:2,renderer:function(value,meta,record ){return value ? "已挂起 V:"+record.get('version') : "正常 V:"+record.get('version') }}
				],
				bbar: createPage(store)
        });
        
        


        var panel=	Ext.create('Ext.panel.Panel', {
		    width: 500,
		    height: 400,
		    layout: 'border',
		    items: [{
		    	 title: '服务台',
		        region:'north',
		        xtype: 'panel',
		        margins: '5 0 0 5',
		        collapsible: false,   // make collapsible
		        split: false,
		        layout: 'fit',
		        dockedItems: [tbar1, tbar2,tbar3]
		    },{
		        title: '服务处理历史',
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
        
        
        function suspend(){
        	var  processInstanceId = checkBox(sm,'processInstanceId');
       		if(!processInstanceId){
       			return ;
       		}
       		Ext.Ajax.request({
                url: basepath+'/workflow/process/updateProcessStatusByProInstanceId/suspend/'+processInstanceId,
                success: function (response) {
                    var responseText = Ext.decode(response.responseText);
                    Ext.Msg.show({
                        title: '提示',
                        msg: responseText.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.INFO,
                        fn: function () {
                            store.load();
                        }
                    });
                },
                failure: function (response) {
                    var responseText = Ext.decode(response.responseText);
                    Ext.Msg.show({
                        title: '提示',
                        msg: responseText.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    });
                }
            });
        }
        function recover(){
        	var  processInstanceId = checkBox(sm,'processInstanceId');
       		if(!processInstanceId){
       			return ;
       		}
       		Ext.Ajax.request({
                url: basepath+'/workflow/process/updateProcessStatusByProInstanceId/active/'+processInstanceId,
                success: function (response) {
                    var responseText = Ext.decode(response.responseText);
                    Ext.Msg.show({
                        title: '提示',
                        msg: responseText.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.INFO,
                        fn: function () {
                            store.load();
                        }
                    });
                },
                failure: function (response) {
                    var responseText = Ext.decode(response.responseText);
                    Ext.Msg.show({
                        title: '提示',
                        msg: responseText.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    });
                }
            });
        }


        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
