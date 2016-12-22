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
                    text: '修改',
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
	       
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.Client", "Model", "","")%>
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'Model',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>client/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                },
 				onload: function (store, options) {
 					//sm.select(0);//在这用无效
                }
            }
        });
     

        
        var sm = Ext.create('Ext.selection.CheckboxModel',{
            mode: 'SINGEL' ,
			listeners:{
				selectionchange: function (_this, selected, eOpts ){

					storeUser.load();
				}

			}
	    }
	 );
        var grid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: store,
            dockedItems: [tbar1],
            //dockedItems: [tbar2,tbar1],
            selModel: sm,
            columnLines: false,
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '单位名称', dataIndex: 'unitName', align: 'center', flex:2},
					{ header: '地址', dataIndex: 'addr', align: 'center', flex:1 },
					{ header: '邮编', dataIndex: 'postCode', align: 'center', flex:1 }
				],
				bbar: createPage(store)
        });
        
        store.on('load', function(){
        	sm.select(0);
	    });
        
  //----------------------客户联系人
  
  
    	var addBtn2= {
	                text: '新增',
	                iconCls: 'x-button-delete',
	                handler:function(){editUser('insert')} 
	            },updateBtn2= {
                    text: '修改',
                    iconCls: 'x-button-delete',
                    handler:function(){ editUser('update')} 
                },delBtn2= {
	                text: '删除',
	                iconCls: 'x-button-delete',
	                handler: deleteThemUser
	            };
	        var tbar2 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [addBtn2,'-',updateBtn2,'-',delBtn2]
	        });
	       
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.ClientContact", "UserModel", "","")%>
        var storeUser = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: false,  //让主store来推动他加载
            model: 'UserModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>client/contact/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
                	var params = {'clientId': sm.getLastSelected( ).get('id')};
                	
            		Ext.apply(store.proxy.extraParams, params);
                },
 				onload: function (store, options) {
 					smUser.deselect(smUser.getSelection());
                }
            }
        });
     

        
        var smUser = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var gridUser = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: storeUser,
            dockedItems: [tbar2],
            //dockedItems: [tbar2,tbar1],
            selModel: smUser,
            columnLines: false,
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '姓名', dataIndex: 'userName', align: 'center', flex:2},
					{ header: '电子邮件', dataIndex: 'email', align: 'center', flex:1 },
					{ header: '联系方式', dataIndex: 'phone', align: 'center', flex:1 }
				],
				bbar: createPage(storeUser)
        });
  
  



        var panel=	Ext.create('Ext.panel.Panel', {
		    width: 500,
		    height: 400,
		    layout: 'border',
		    items: [{
		        region:'west',
		        xtype: 'panel',
		        margins: '5 0 0 5',
		        flex: 2,
		        collapsible: true,   // make collapsible
		        split: true,
		        id: 'west-region-container',
		        layout: 'fit',
		        items: grid
		    },{
		        title: '客户联系人',
		        region: 'center',     // center region is required, no width/height specified
		        xtype: 'panel',
		        layout: 'fit',
		        margins: '5 5 0 0',
		        flex: 1,
		        items: gridUser
		    }],
		});     


		
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: panel
        });
        
        
	       
	    
	  
	    
//函数区        
//----------------------------------------------------------------------------------------------------------
        
        
        
        
	    //编辑客户
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
        	var title = '客户维护';
        	
        	var url = '<%=path%>/client/toEdit'+param;
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
                        url: '<%=basePath%>client/deleteThem',
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

        
	    //编辑联系人
        function editUser(action){
        	
        	var param = '?action=' + action+"&clientId="+sm.getLastSelected( ).get('id') ;
        	if(action!='insert'){//update or view 
        		var  id = 	checkBox(smUser,'id');
        		
        		if(!id){
        			return false;
        		}
        		 param += "&id=" + id;
        	}
        	else{
        		
        	}
        	var title = '客户联系人维护';
        	
        	var url = '<%=path%>/client/contact/toEdit'+param;
        	ShowWindow(storeUser, title, url, 600, 380);
        	
        }       
        
        //删除联系人
        function deleteThemUser() {
            var res = checkBoxs(smUser,'id');
            if (!res) { return; } 

            Ext.Msg.confirm('删除记录', '确认要删除该记录吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>client/contact/deleteThem',
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
                                	storeUser.load();
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
