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
	                iconCls: 'x-button-insert',
	                handler:function(){edit('insert','${type}')} 
	            },updateBtn= {
                	id: 'updateBtn',
                    text: '编辑',
                    iconCls: 'x-button-update',
                    handler:function(){ edit('update','${type}')} 
                },delBtn= {
	            	id: 'delBtn',
	                text: '删除',
	                iconCls: 'x-button-delete',
	                handler: deleteThem
	            }, viewBtn= {
	            	id: 'exportBtn',
	                text: '查看',
	                iconCls: 'x-button-eye',
	                handler: function(){edit('view','${type}')} 
	            };
	        var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [addBtn,'-',updateBtn,'-',delBtn,'-',viewBtn]
	        });
	       
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.Supplier", "Model", "","")%>
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'Model',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>supplier/data/${type}',
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
            selModel: sm,
            columnLines: false,
            viewConfig:{getRowClass:changeRowClass},
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '供应商名称', dataIndex: 'name', align: 'center', flex:2},
					{ header: '地址', dataIndex: 'addr', align: 'center', flex:1 },
					{ header: '邮编', dataIndex: 'postCode', align: 'center', flex:1 },
					{ header: '营业执照号码', dataIndex: 'businessLicenseNo', align: 'center', flex:1 },
					{ header: '税务号', dataIndex: 'taxNo', align: 'center', flex:1 },
					{ header: '银行', dataIndex: 'bank', align: 'center', flex:1 },
					{ header: '银行账号', dataIndex: 'bankAccount', align: 'center', flex:1 }
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

	    
        
        
        
	    //编辑用户
        function edit(action,type){
        	
        	var param = '/'+type+'?action=' + action ;
        	if(action!='insert'){//update or view 
        		var  id = 	checkBox(sm,'id');
        		
        		if(!id){
        			return false;
        		}
        		 param += "&id=" + id;
        	}
        	else{
        		
        	}
        	var title = '供应商信息维护';
        	
        	var url = '<%=path%>/supplier/toEdit'+param;
        	ShowWindow(store, title, url, 600, 380);
        	
        }       
        
        //删除
        function deleteThem() {
            var res = checkBoxs(sm,'id');
            if (!res) { return; } 

            Ext.Msg.confirm('删除记录', '确认要删除该记录吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>supplier/deleteThem',
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
