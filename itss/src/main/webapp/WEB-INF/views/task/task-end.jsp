<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
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
		Ext.onReady(function(){
			
			Ext.QuickTips.init();

			 Ext.define('App.model.Task', {
		            extend: 'Ext.data.Model',
		            fields: [
		                {name: 'id'},
		                {name: 'processInstanceId'},
		                {name: 'processDefinitionId', type: 'string'},
		                {name: 'name'},
		                {name: 'description'},
		                {name: 'assignee', type: 'string'},
		                {name: 'createTime'},
		                {name: 'formKey'}
		            ]

		        });

		        
			   Ext.define('App.model.Model', {
		            extend: 'Ext.data.Model',
// 		            requires: [
// 		                'App.model.Task'
// 		            ],
		            fields: [
		              
		                
		            	 {name: 'historicProcessInstance'},
			             {name: 'historicProcessInstance.startTime',mapping:'historicProcessInstance.startTime'},
			             {name: 'historicProcessInstance.claimTime',mapping:'historicProcessInstance.claimTime'},
			             {name: 'historicProcessInstance.endTime',mapping:'historicProcessInstance.endTime'},
			             {name: 'historicProcessInstance.deleteReason',mapping:'historicProcessInstance.deleteReason'},
		                
		                {name: 'processDefinition'},
		                {name: 'pd.version',mapping:'processDefinition.version'},

		                
		                {name: 'title', type: 'string'},
		                {name: 'businessType', type: 'string'},
		                {name: 'user_name', type: 'string'},
		                {name: 'user_id', type: 'string'}
		            ]

		        });
	       
	       
        
       
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'App.model.Model',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>workflow/finishedTask/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                }
            }
        });

        



        var btnRecover = new Ext.button.Button({
            text: '恢复',
            iconCls: 'x-button-read',
            handler: function () {
            	
            }
        });
        var tbar = Ext.create('Ext.toolbar.Toolbar', {
            items: [btnRecover]
        });
        
        var sm = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var grid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: store,
            //dockedItems: [tbar],
            selModel: sm,
            columnLines: false,
            viewConfig:{getRowClass:changeRowClass},
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '服务名称', dataIndex: 'businessType', align: 'center', flex:2,renderer:businessTypeRenderer},
					{ header: '任务名称', dataIndex: 'title', align: 'center', flex:1 },
                    { header: '申请人', dataIndex: 'user_name', align: 'center', flex:2},
                    { header: '任务开始时间', dataIndex: 'historicProcessInstance.startTime', align: 'center', flex:2},
                    { header: '任务结束时间', dataIndex: 'historicProcessInstance.endTime', align: 'center', flex:1}
				],
				bbar: createPage(store)
        });
        
        
        
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: grid
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

	    

		
        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
