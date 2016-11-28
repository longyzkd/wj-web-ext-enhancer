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
			
			var allowInsert = true;
	        var allowUpdate = true;
	        var allowRead = true;
	        var allowDelete = true;
	        
	        var userType = ''; //当前登陆用户的类型
	        
	        var isStop;
	        
//---------组
	        var cboUserId =  new Ext.form.field.Text({
	            name: 'name',
	            fieldLabel: '组名称',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	        
	        var tbar_org = Ext.create('Ext.toolbar.Toolbar', {
	            items: [{
	            	id: 'addOrgBtn',
	                text: '新增',
	                iconCls: 'x-button-insert',
	                handler: function () {
	                    editOrg('insert');
	                }
	            }, '-', {
	                text: '修改',
	                iconCls: 'x-button-update',
	                handler: function () {
	                	editOrg('update');
	                }
	            },'-', {
	            	id: 'delOrgBtn',
	                text: '删除',
	                iconCls: 'x-button-delete',
	                handler: deleteOrg
	            }]
	        });


	        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.SysOrg", "SysOrgModel", "","")%>
	        var storeOrg = Ext.create('Ext.data.Store', {
	            autoLoad: true,
	            model: 'SysOrgModel',
	            proxy: {
	                type: 'ajax',
	                url: '<%=basePath%>org/listOrgs',
	                reader: { type: 'json', root: 'dataList' }
	            },
	            listeners: {
	                beforeload: function (store, options) {
	                
	                }
	            }
	        });
	        
	        var cboOrgPage = Ext.create('Ext.form.ComboBox', {
	            store: new Ext.data.ArrayStore({
	                fields: ['text', 'value'],
	                data: [['10', 10], ['15', 15], ['20', 20], ['30', 30], ['50', 50]]
	            }),
	            valueField: 'value',
	            displayField: 'text',
	            width: 50
	        });

	        cboOrgPage.setValue(50);

	        cboOrgPage.on('select', function (comboBox) {
	        	storeOrg.pageSize = parseInt(comboBox.getValue());
	        	storeOrg.loadPage(1);
	        });

	        var pgOrgCfg = new Ext.PagingToolbar({
	            pageSize: 50,
	            store: storeOrg,
	            displayInfo: true,
	            displayMsg: '显示 {0} - {1} 条记录，总共 {2} 条记录',
	            emptyMsg: "没有记录",
	            items: ['-', '每页显示', cboOrgPage, '条']
	        });
	        
	        //组织列表Panel
	        var smOrg = Ext.create('Ext.selection.CheckboxModel',{
	    	            mode: 'SINGEL' ,
						listeners:{
							selectionchange: function (_this, selected, eOpts ){

								storeUser.load();
							}

						}
		    	    }
	       	 );
	        var gridOrg = Ext.create('Ext.grid.Panel', {
	            region: 'center',
	            store: storeOrg,
	            dockedItems: [tbar_org],
	            selModel: smOrg,
	            columnLines: false,
	            columns: [
						new Ext.grid.PageRowNumberer(),
						{ header: '组名称', dataIndex: 'name', align: 'center', flex:1 },
					],
				bbar: pgOrgCfg
	        });



	        
 //------------------用户       
 
  
	  var tbar_user = Ext.create('Ext.toolbar.Toolbar', {
            items: [{
            	id: 'addBtn',
                text: '新增',
                iconCls: 'x-button-insert',
                handler: function () {
                    editUser('insert');
                }
            },'-', {
            	id: 'delBtn',
                text: '删除',
                iconCls: 'x-button-delete',
                handler: deleteUser
            }]
        });

	        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.User", "UserModel", "","")%>
        //用户列表Store
        var storeUser = Ext.create('Ext.data.Store', {
            autoLoad: false,
            model: 'UserModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>org/orgUsers',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
                	var params = {'orgId': smOrg.getLastSelected( ).get('id')};
                	
                		Ext.apply(store.proxy.extraParams, params);
                	
            		
                }
            }
        });
        
        var cboPage = Ext.create('Ext.form.ComboBox', {
            store: new Ext.data.ArrayStore({
                fields: ['text', 'value'],
                data: [['10', 10], ['15', 15], ['20', 20], ['30', 30], ['50', 50]]
            }),
            valueField: 'value',
            displayField: 'text',
            width: 50
        });

        cboPage.setValue(50);

        cboPage.on('select', function (comboBox) {
        	storeUser.pageSize = parseInt(comboBox.getValue());
        	storeUser.loadPage(1);
        });

        var pgCfg = new Ext.PagingToolbar({
            pageSize: 50,
            store: storeUser,
            displayInfo: true,
            displayMsg: '显示 {0} - {1} 条记录，总共 {2} 条记录',
            emptyMsg: "没有记录",
            items: ['-', '每页显示', cboPage, '条']
        });
        
        //用户列表Panel
        var smUser = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var gridUser = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: storeUser,
            dockedItems: [tbar_user],
            selModel: smUser,
            columnLines: false,
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '姓名', dataIndex: 'username', align: 'center', flex:1 },
					{ header: '电子邮件', dataIndex: 'userId', align: 'center',flex:2 },
                    { header: '所在部门', dataIndex: 'deptName', align: 'center', flex:1}
				],
				bbar: pgCfg
        });
        
  //-----整体
  
 var panel=	Ext.create('Ext.panel.Panel', {
		    width: 500,
		    height: 400,
		    layout: 'border',
		    items: [{
		    	title: '组织',
		        region:'west',
		        xtype: 'panel',
		        margins: '5 0 0 5',
		        width: 200,
		        collapsible: true,   // make collapsible
		        split: true,
		        id: 'west-region-container',
		        layout: 'fit',
		        items:gridOrg
		    },{
		        title: '组用户',
		        region: 'center',     // center region is required, no width/height specified
		        xtype: 'panel',
		        layout: 'fit',
		        margins: '5 5 0 0',
		        items:gridUser
		    }],
		});     
        
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: panel
        });
        
        
	       
	    
	    storeOrg.on('load', function(){
	    	smOrg.select(0);
	    });
	    storeUser.on('load', function(){
	    	smUser.deselect(smUser.getSelection());
	    });
	    
//函数区        
//----------------------------------------------------------------------------------------------------------


	    
        //编辑组织
        function editOrg(action){
        	
        	var param = '?action=' + action ;
        	if(action!='insert'){
        		var  orgId = checkBoxOrg();
        		
        		if(!orgId){
        			return false;
        		}
        		 param += "&orgId=" + orgId;
        	}
        	var title = '组织信息维护';
        	
        	var url = '<%=path%>/org/toEdit'+param;
        	ShowWindow(storeOrg, title, url, 400, 280);
        	
        }
	    
        
        function checkBoxOrg() {
            var id;
            var selectedCount = smOrg.getCount();
            if (selectedCount == 0) {
                Ext.Msg.show({ title: '提示', msg: '未选中任何一个记录，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else if (selectedCount > 1) {
                Ext.Msg.show({ title: '提示', msg: '只能选择一个记录，不能同时选择多个！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else {
                id = smOrg.getLastSelected().data.id;
                return id;
            }
        }
        function checkBoxUsers() {
            var ids = [];
            var selectedCount = smUser.getCount();
            if (selectedCount == 0) {
                Ext.Msg.show({ title: '提示', msg: '未选中任何一个用户，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else {
                Ext.each(smUser.getSelection( ),function(cur){

                	ids.push(cur.data.userId);
                	
                  }) ;

                
                return ids;
            }
        }
        
        //编辑用户
        function editUser(action){
        	
        	var param = '?orgId=' + smOrg.getLastSelected( ).get('id') ;
        	var title = '用户信息维护';
        	var url = '<%=path%>/org/toUsersAdd'+param;
        	ShowWindow(storeUser, title, url, 500, 380);//store 用来 提示关闭-窗口关闭-store.load的
        	
        }
        
        function deleteOrg() {
            var res = checkBoxOrg();
            if (!res) { return; } 

            Ext.Msg.confirm('删除用户', '确认要删除该组织吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>org/delOrg',
                        params: { orgId: res },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: function () {
                                    storeUser.load();
                                    storeOrg.load();
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
        //删除用户
        function deleteUser() {
            var res = checkBoxUsers();
            if (!res) { return; } else { _UserId = res; }

            Ext.Msg.confirm('删除用户', '确认要删除该用户吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>org/delUsers',
                        params: {orgId:smOrg.getLastSelected( ).get('id'), userIds: res },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg,
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
