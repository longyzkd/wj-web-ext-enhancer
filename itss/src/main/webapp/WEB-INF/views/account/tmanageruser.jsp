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
	        
	        //用户类型store
	        var storeType = Ext.create('Ext.data.Store', {
	            autoLoad: false,
	            fields: [
	                    'Code',
	                    'Name'
	                ],
	            data: [
						{Code: '', Name: '全部'},
	                    {Code: '1', Name: '管理员'},
	                    {Code: '2', Name: '办事处'},
	                    {Code: '3', Name: '安装工'},
	                    {Code: '4', Name: '客户'},
	                    {Code: '5', Name: '市场销售'}
	                    ]
	        });
	        
	        var cboUserId =  new Ext.form.field.Text({
	            name: 'userId',
	            fieldLabel: '电子邮件',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	        var btnSearch = new Ext.button.Button({
	            text: '查询',
	            iconCls: 'x-button-read',
	            handler: function () {
	            	storeUser.load();
	            }
	        });
	        
	        var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [cboUserId, btnSearch]
	        });
	        
	        var tbar2 = Ext.create('Ext.toolbar.Toolbar', {
            items: [{
            	id: 'addBtn',
                text: '新增',
                iconCls: 'x-button-insert',
                handler: function () {
                    editUser('insert');
                }
            }, '-', {
                text: '修改',
                iconCls: 'x-button-update',
                handler: function () {
                    editUser('update');
                }
            },/* '-', {
                text: '修改密码',
                iconCls: 'x-button-update',
                handler: function () {
                    UpdatePWD();
                }
            },*/ '-', {
            	id: 'delBtn',
                text: '删除',
                iconCls: 'x-button-delete',
                handler: deleteUsers
            }, '-', {
            	id: 'activBtn',
                text: '激活',
                iconCls: 'x-button-application_cascade',
                handler: activUser
            },  '-', {
            	id: 'shieldBtn',
                text: '屏蔽',
                iconCls: 'x-button-application_delete',
                handler: shieldUser
            },  '-', {
            	id: 'resetBtn',
                text: '重置',
                iconCls: 'x-button-application',
                handler: resetUser
            } , '-', {
                text: '刷新',
                iconCls: 'x-button-refresh',
                handler: function () {
                    storeUser.load();
                }
            }]
        });
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.User", "UserModel", "","")%>
        //用户列表Store
        var storeUser = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model: 'UserModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>user/listData',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
                	var params = {'userId': cboUserId.getValue()};
                	if(cboUserId.getValue() == ""){
                		store.proxy.extraParams={};
                		Ext.apply(store.proxy.extraParams, {});
                	}else{
                		Ext.apply(store.proxy.extraParams, params);
                	}
            		
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
            dockedItems: [tbar1, tbar2],
            selModel: smUser,
            columnLines: false,
            viewConfig:{getRowClass:changeRowClass},
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '姓名', dataIndex: 'username', align: 'center',flex:1},
					{ header: '电子邮件', dataIndex: 'userId', align: 'center', flex:2 },
                    { header: '手机', dataIndex: 'phone', align: 'center', flex:1},
                    { header: '所在部门', dataIndex: 'deptName', align: 'center',flex:1}
//                    { header: '状态', dataIndex: 'isStoped', align: 'center', width: 100, renderer: IsStop}
				],
				bbar: pgCfg
        });
        
        
        
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: gridUser
        });
        
        
	       
	    
	    storeUser.on('load', function(){
	    	smUser.deselect(smUser.getSelection());
	    });
	    
//函数区        
//----------------------------------------------------------------------------------------------------------
        
	    function changeRowClass(record, rowIndex, rowParams, store){
	        if (record.get("status") == "0") {        
	            return 'x-grid-record-red';
	        }
	    } 

	    
        function IsStop(value) {
        	isStop = value;
        	
            if (value)
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/action_stop.gif" />';
            else
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/accept.png" />';
        }
        
        function fun_userType(value) {
        	
            if (value==1)
                return '管理员';
            else if (value==2)
                return '办事处';
            else if (value==3)
                return '安装工';
            else if (value==4)
                return '客户';
            else if (value==5)
                return '销售人员';
            else 
                return '';
        }
        
        function checkBoxUser() {
            var id;
            var selectedCount = smUser.getCount();
            if (selectedCount == 0) {
                Ext.Msg.show({ title: '提示', msg: '未选中任何一个用户，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else if (selectedCount > 1) {
                Ext.Msg.show({ title: '提示', msg: '只能选择一个用户，不能同时选择多个！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else {
                id = smUser.getLastSelected().data.userId;
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
        	
        	var param = '?action=' + action ;
        	if(action!='insert'){
        		var  userId = checkBoxUser();
        		
        		if(!userId){
        			return false;
        		}
        		 param += "&userId=" + userId;
        	}
        	else{
        		
        	}
        	var title = '用户信息维护';
        	
        	var url = '<%=path%>/user/toEdit'+param;
        	ShowWindow(storeUser, title, url, 600, 380);
        	
        }
        
        //删除用户
        function deleteUsers() {
            var res = checkBoxUsers();
            if (!res) { return; } 

            Ext.Msg.confirm('删除用户', '确认要删除该用户吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>user/deleteThem',
                        params: { userIds: res },
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
        //激活用户
        function activUser() {
            var res = checkBoxUsers();
            if (!res) { return; } 
            Ext.Msg.confirm('激活用户', '确认要激活该用户吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>user/updateUsers',
                        params: {  userIds: res ,status: 1 },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                //msg: responseText.msg,
                                msg: '激活成功',
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
        //屏蔽用户
        function shieldUser() {
            var res = checkBoxUsers();
            if (!res) { return; }

            Ext.Msg.confirm('屏蔽用户', '确认要屏蔽该用户吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>user/updateUsers',
                        params: {  userIds: res ,status: 0 },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: '屏蔽成功',
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
        //重置用户
        function resetUser() {
            var res = checkBoxUsers();
            if (!res) { return; } 

            Ext.Msg.confirm('重置用户', '确认要重置该用户吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>user/restUsers',
                        params: { userIds: res  },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: '用户密码已经被设置为默认密码（123456）!',
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
        
        
         //修改密码
        function UpdatePWD() {
        
       		 var  userId = checkBoxUser();
        		
        		if(!userId){
        			return ;
        		}
            var winTitle = '修改密码'; 

            //编辑form
            if (form != null) {
                form.destroy(true);
                form = null;
            }
            if (!form) {
               

                var txtNewPwd = new Ext.form.field.Text({
                    name: 'NewPwd',
                    inputType: 'password',
                    fieldLabel: '<font style="color:red;">新密码</font>',
                    regex: /^[A-Za-z0-9_]+$/,
                    regexText: '请输入有效的密码，含数字，字母，下划线',
                    allowBlank: false,
                    labelWidth: 60
                });

                var txtNewPwdAg = new Ext.form.field.Text({
                    name: 'password',
                    inputType: 'password',
                    fieldLabel: '<font style="color:red;">再次输入</font>',
                    regex: /^[A-Za-z0-9_]+$/,
                    regexText: '请输入有效的密码，含数字，	字母，下划线',
                    allowBlank: false,
                    labelWidth: 60,
                    validator: function (value) {
                        var pwd = this.previousSibling().value;
                        if (value != pwd) {
                            return '两次输入的密码不一致！';
                        }
                        else {
                            return true;
                        }
                    }
                });

                var form = new Ext.form.Panel({
                    fieldDefaults: {
                        labelAlign: 'right',
                        msgTarget: 'side',
                        labelWidth: 70
                    },
                    bodyStyle: 'padding:15px',
                    items: [txtNewPwd, txtNewPwdAg]
                });
            }
        //信息编辑Window
            if (win != null) {
                win.destroy(true);
                win = null;
            }
            if (!win) {
                var win = new Ext.window.Window({
                    title: winTitle,
                    width: 300,
                    height: 200,
                    modal: true,
                    resizeable: false,
                    collapsible: true,
                    closeAction: 'close',
                    bodyStyle: 'padding:5px;',
                    layout: 'fit',
                    buttonAlign: 'center',
                    items: [form],
                    buttons: [{
                        type: 'submit',
                        text: '确 定',
                        visible: false,
                        handler: function () {
                            if (form.isValid()) {
                                form.submit({
                                    submitEmptyText: false,
                                    clientValidation: true,
                                    url: '<%=path%>/user/updatePwd',
                                    method: 'POST',
                                    params: {
                                    	userId:userId
                                    },
                                    success: function (form, action) {
                                        Ext.Msg.show({
                                            title: '提示',
                                            msg: action.result.msg,
                                            buttons: Ext.Msg.CANCEL,
                                            icon: Ext.Msg.INFO,
                                            fn: function () {
                                                win.close();
                                                storeUser.load();
                                                
                                            }
                                        });
                                    },
                                    failure: function (form, action) {
                                        if (action.failureType == Ext.form.action.Action.SERVER_INVALID) {
                                            Ext.Msg.show({
                                                title: '提示',
                                                msg: action.result.msg,
                                                buttons: Ext.Msg.CANCEL,
                                                icon: Ext.Msg.ERROR
                                            });
                                        }
                                        else {
                                            Ext.Msg.show({
                                                title: '提示',
                                                msg: '无法访问后台！',
                                                buttons: Ext.Msg.CANCEL,
                                                icon: Ext.Msg.ERROR
                                            });
                                        }
                                    }
                                });
                            }
                            else {
                                Ext.Msg.show({ title: '提示', msg: '请检查输入项是否正确！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                            }
                        }
                    }, {
                        type: 'button',
                        text: '关 闭',
                        handler: function () { win.close(); }
                    }]
                });
            }
            win.show();

            //
            function SavePwd() {
                
                }
        }
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
