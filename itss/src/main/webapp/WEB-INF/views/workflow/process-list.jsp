<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>流程定义</title>
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

			   Ext.define('ProcessModel', {
		            extend: 'Ext.data.Model',
		            fields: [
		                {name: 'id',  type: 'string'},
		                {name: 'deploymentId',   type: 'string'},
		                {name: 'name', type: 'string'},
		                {name: 'key', type: 'string'},
		                {name: 'version', type: 'string'},
		                {name: 'resourceName'},
		                {name: 'diagramResourceName'},
		                {name: 'suspended'},
		               // {name: 'metaInfo.description' , mapping : 'metaInfo.description'},
		               // {name: 'metaInfo.status' , mapping : 'metaInfo.status'}
		                {name: 'deploymentTime' }
		            ]

		        });
	       
	       
        
       
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'ProcessModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>workflow/process/list/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                }
            }
        });
     
    	var delBtn= {
            	id: 'delBtn',
                text: '删除',
                iconCls: 'x-button-delete',
                handler: delProcess
            }, convertToModelBtn= {
            	id: 'convertToModelBtn',
                text: '转换为Model',
                iconCls: 'x-button-application_cascade',
                handler: convertToModel
            }
    	  var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [delBtn,'-',convertToModelBtn]
	        });
      
        
        var sm = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var grid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: store,
            dockedItems: [tbar1],
            selModel: sm,
            columnLines: false,
            viewConfig:{getRowClass:changeRowClass},
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: 'ProcessDefinitionId', dataIndex: 'id', align: 'center', flex:2},
					{ header: 'DeploymentId', dataIndex: 'deploymentId', align: 'center', flex:1 },
                    { header: '名称', dataIndex: 'name', align: 'center', flex:2},
                    { header: 'KEY', dataIndex: 'key', align: 'center', flex:1},
                    { header: '版本号', dataIndex: 'version', align: 'center', flex:2},
                    { header: 'XML', dataIndex: 'resourceName', align: 'center', flex:2,renderer:xmlRenderer},
                    { header: '图片', dataIndex: 'diagramResourceName', align: 'center', flex:2,renderer:picRenderer}
//                     { header: '部署时间', dataIndex: 'deploymentTime', align: 'center', flex:2}
//                     { header: '是否挂起', dataIndex: 'suspended', align: 'center', flex:2},
//                     { header: '操作',  align: 'center', flex:2}
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

	    
        function typeRenderer(value) {
        	
            if (value=='2')  //异常日志
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/action_stop.gif "  alt="异常日志 " />';
            else  
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/accept.png   "  alt="入口日志" />';
        }

        
        function picRenderer(value,meta,record) {

            var url =basepath+'/workflow/resource/process-definition?processDefinitionId='+record.get('id')+'&resourceType=image';
        	 return '<a target="_blank"  href="' + url + '">' + value+ '</a>';  
        }
        
        
        function xmlRenderer(value,meta,record) {

            var url =basepath+'/workflow/resource/process-definition?processDefinitionId='+record.get('id')+'&resourceType=xml';
        	 return '<a target="_blank"  href="' + url + '">' + value+ '</a>';  
        }
        
        
        function delProcess(){

        	   var deploymentId =  checkBox(sm,'deploymentId');
        	   if(!deploymentId){
	       			return ;
	       		}
        	var url = basepath+'/workflow/process/delete?deploymentId='+deploymentId;
            Ext.Msg.confirm('删除记录', '确认要删除该记录吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: url,
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg ,
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
            });

         }


        function convertToModel(){
			   var id =  checkBox(sm,'id');
			   if(!id){
	       			return ;
	       		}
			var url = basepath+'/workflow/process/convert-to-model/'+id;
	                    Ext.Msg.wait('正在处理，请稍等......');
	                    Ext.Ajax.request({
	                        url: url,
	                        success: function (response) {
	                            Ext.Msg.hide();
	                            var responseText = Ext.decode(response.responseText);
	                            Ext.Msg.show({
	                                title: '提示',
	                                msg: responseText.msg ,
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
        
        
        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
