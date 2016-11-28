<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<jsp:include page="../_LayoutCommonExtJS.jsp"/>
      <script type="text/javascript" src="<%=path%>/static/jslib/ExtJs/UserControl/sys_Dic.js" ></script>
	<script type="text/javascript">
		Ext.onReady(function(){
			
			Ext.QuickTips.init();

			/*
			var action = getQueryString("action");
            var _UserID = getQueryString("userId");
            */
            var action = '${action}';
            var orgId = '${orgId}'; //后台null，这里就是空串

           
            //-----------------------------------------------------------------------
            
           
	     
            var txtOrgName = new Ext.form.field.Text({
                name: 'name',
                fieldLabel: '名称'+'<font color=red>*</font>',
                maxLength: 20,
                allowBlank: false
              
            });
          
       

           
            var formRight = new Ext.form.Panel({
                region: 'center',
                title: '组织信息&nbsp;&nbsp;' + titleName(action),
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                items: [{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    fieldDefaults: { labelAlign: 'right', labelWidth: 65, width: 245 },
                    items: [txtOrgName ]
                }],



                
                buttons: [{
                    type: 'submit',
                    text: '确 定',
                    handler: handleProcess
                }, {
                    type: 'button',
                    text: '关 闭',
                    handler: function () { CloseWindow(); }
                }]
            });
            
            <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.SysOrg", "SysOrgModel", "","")%>
            //用户列表Store
            var storeOrg = Ext.create('Ext.data.Store', {
                autoLoad: false,
                model: 'SysOrgModel',
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>org/one',
                    reader: { type: 'json'}
                    
                },
                listeners: {
                    load: function (store, options) {
                       
                        formRight.getForm().loadRecord(store.getAt(0));
                    }
                }
        	});
            
            

            //表单自动加载数据 -----------------------------------------------------------------------------------------------------
            if (action != 'insert') {
            	storeOrg.load({
                    params: {
                    	'id': orgId
                    }
                });
            }

            new Ext.container.Viewport({
                layout: 'border',
                items: [formRight]
            });

            //函数区
            //----------------------------------------------------------------------------------------------------------------------
            //标题名称
            function titleName(action) {
                var title = '[查看]';
                switch (action) {
                    case 'insert': title = '[新增]'; break;
                    case 'update': title = '[修改]'; break;
                }
                return title;
            }

            //关闭窗口刷新
            function closeRefresh() {
            	CloseWindow();
            }

            function handleProcess() {
                //编辑--------------------------------------------------------------------------
                if (action == 'insert' || action == 'update') {
                    var action_url= '<%=basePath%>org/saveOrg';
                    if(action == 'update'){
                    	action_url= '<%=basePath%>org/updateOrg?id='+orgId;
                    }
                    
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
//                            params: { action: action, funidvalue: RightsIdArray },
                            params: { action: action },
                            success: function (form, action) {
                                Ext.Msg.show({
                                    title: '提示',
                                    msg: action.result.msg,
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.INFO,
                                    fn: CloseWindow
                                });
                            },
                            failure: function (form, action) {
                                if (action.failureType == Ext.form.action.Action.SERVER_INVALID) {
                                    Ext.Msg.show({
                                        title: '提示',
                                        msg: action.result.msg,
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.ERROR
                                    });
                                }
                                else {
                                    Ext.Msg.show({
                                        title: '提示',
                                        msg: '无法访问后台！',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.ERROR
                                    });
                                }
                            }
                        });
                    }
                    else {
                        Ext.Msg.show({ title: '提示', msg: '信息验证不通过！', buttons: Ext.Msg.CANCEL, icon: Ext.Msg.WARNING });
                    }
                }

            }
//--------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
