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
		                {name: 'task' ,type:'auto'},
		                {name: 'task.createTime' ,mapping:'task.createTime'},
		                {name: 'task.id' ,mapping:'task.id'},
		                {name: 'task.assignee' ,mapping:'task.assignee'},
		                {name: 'task.taskDefinitionKey' ,mapping:'task.taskDefinitionKey'},
		                {name: 'task.owner' ,mapping:'task.owner'},
		                {name: 'task.name' ,mapping:'task.name'},
		                
		                {name: 'processInstance'},
		                {name: 'pi.suspended',mapping:'processInstance.suspended'},
		                
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
                url: '<%=basePath%>workflow/todoTask/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                }
            }
        });

        

        var btnClaim = new Ext.button.Button({
            text: '接收',
            iconCls: 'x-button-read',
            handler: claim
        });

        var btnHandle = new Ext.button.Button({
            text: '处理',
            iconCls: 'x-button-read',
            handler: handle
        });
        var btnSuspend = new Ext.button.Button({
            text: '挂起',
            iconCls: 'x-button-read',
            handler: function () {
            	
            }
        });

        var btnRecover = new Ext.button.Button({
            text: '恢复',
            iconCls: 'x-button-read',
            handler: function () {
            	
            }
        });
        var tbar = Ext.create('Ext.toolbar.Toolbar', {
            items: [btnClaim,'-',btnHandle]
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
					{ header: '服务名称', dataIndex: 'businessType', align: 'center', flex:2,renderer:businessTypeRenderer},
					{ header: '任务名称', dataIndex: 'title', align: 'center', flex:1 },
                    { header: '处理事宜', dataIndex: 'task.name', align: 'center', flex:2},
                    { header: '申请人', dataIndex: 'user_name', align: 'center', flex:2},
                    { header: '状态', dataIndex: 'pi.suspended', align: 'center', flex:2,renderer:function(value,meta,record ){return value ? "已挂起 V:"+record.get('pd.version') : "正常 V:"+record.get('pd.version') }},
                    { header: '创建时间', dataIndex: 'task.createTime', align: 'center', flex:1}
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

	    
        function claim(){
        	var  taskId = checkBox(sm,'task.id');
       		if(!taskId){
       			return ;
       		}
       		
        	Ext.Ajax.request({
                url: '<%=basePath%>workflow/claim/'+taskId,
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
                    Ext.Msg.hide();
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
        //根据不同的业务类型bizzType，办理页面也不一样
        function handle(){
        	var  taskId = checkBox(sm,'task.id');
        	var  businessType = checkBox(sm,'businessType');
        	var  taskDefinitionKey = checkBox(sm,'task.taskDefinitionKey');
        	alert(taskDefinitionKey);
       		if(!taskId){
       			return ;
       		}
       		var title ;
        	var url
           	if(taskDefinitionKey=='modifyApply'){
           		 title = '修改申请';
             }else{
               		title = '流程审批';

              }
	        	
	        url = '<%=path%>/vacation/toApproval/'+taskId;   //在后台根据taskKey来判断做的事情
        	ShowWindow(store, title, url, 600, 380);
        
        }


        
       

        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
