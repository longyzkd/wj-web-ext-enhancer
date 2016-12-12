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


		        
			   Ext.define('App.model.Model', {
		            extend: 'Ext.data.Model',
// 		            requires: [
// 		                'App.model.Task'
// 		            ],
		            fields: [
		                {name: 'id' },
		                {name: 'userName' },
		                {name: 'host' },
		                {name: 'lastAccessTime' },
		                {name: 'forced' }
		                
		            ]

		        });
	       
	       
        
       
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'App.model.Model',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>user/listSession/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                }
            }
        });

        

     

        var btnForceLogout = new Ext.button.Button({
            text: '强制退出',
            iconCls: 'x-button-read',
            handler: forceLogout
        });
        var tbar = Ext.create('Ext.toolbar.Toolbar', {
            items: [btnForceLogout]
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
					{ header: '会话ID', dataIndex: 'id', align: 'center', flex:2},
					{ header: '用户名', dataIndex: 'userName', align: 'center', flex:1 },
                    { header: '主机地址', dataIndex: 'host', align: 'center', flex:2},
                    { header: '最后访问时间', dataIndex: 'lastAccessTime', align: 'center', flex:2},
                    { header: '已强制退出', dataIndex: 'forced', align: 'center', flex:2,renderer:function(value,meta,record ){return value ? "是": "否" }}
				]
			//	bbar: createPage(store)
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

	    
        function forceLogout(){
        	var  id = checkBox(sm,'id');
       		if(!id){
       			return ;
       		}
       		
        	Ext.Ajax.request({
                url: basepath+'/user/'+id+'/forceLogout',
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
    

        
       

        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
