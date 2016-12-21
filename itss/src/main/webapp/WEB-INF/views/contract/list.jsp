<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<jsp:include page="../_LayoutCommonExtJS.jsp"/>
	<script type="text/javascript">
		Ext.onReady(function(){
			
			Ext.QuickTips.init();

			

	      	var addBtn= {
	            	id: 'addBtn',
	                text: '新增',
	                iconCls: 'x-button-delete',
	                handler:function(){edit('insert')} 
	            },updateBtn= {
                	id: 'updateBtn',
                    text: '编辑',
                    iconCls: 'x-button-delete',
                    handler:function(){ edit('update')} 
                },delBtn= {
	            	id: 'delBtn',
	                text: '删除',
	                iconCls: 'x-button-delete',
	                handler: deleteThem
	            };
	        var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [addBtn,'-',updateBtn,'-',delBtn]
	        });
	       
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.Contract", "Model", "","")%>
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'Model',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>contract/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                }
            }
        });
     

        
        var sm = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var grid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: store,
            dockedItems: [tbar1],
            //dockedItems: [tbar2,tbar1],
            selModel: sm,
            columnLines: false,
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '合同名称', dataIndex: 'contractName', align: 'center', flex:2},
					{ header: '合同编号', dataIndex: 'contractNo', align: 'center', flex:1 },
					{ header: '客户单位', dataIndex: 'unitName', align: 'center', flex:1 },
					{ header: '合同类型', dataIndex: 'contractType', align: 'center', flex:1 },
					{ header: '合同描述', dataIndex: 'desc', align: 'center', flex:1 }
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
        
        
        
        
	    //编辑用户
        function edit(action){
        	
        	var param = '?action=' + action ;
        	if(action!='insert'){//update or view 
        		var  id = 	checkBox(sm,'id');
        		
        		if(!id){
        			return false;
        		}
        		 param += "&id=" + id;
        	}
        	else{
        		
        	}
        	var title = '服务水平维护';
        	
        	var url = '<%=path%>/contract/toEdit'+param;
        	ShowWindow(store, title, url, 700, 380);
        	
        }       
        
        //删除
        function deleteThem() {
            var res = checkBoxs(sm,'id');
            if (!res) { return; } 

            Ext.Msg.confirm('删除记录', '确认要删除该记录吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>contract/deleteThem',
                        params: { ids: res  },
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
        
        
        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
