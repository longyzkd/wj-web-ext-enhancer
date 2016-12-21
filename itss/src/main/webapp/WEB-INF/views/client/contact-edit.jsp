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

            var action = '${action}';
            var id = '${id}'; //后台null，这里就是空串
            var clientId = '${clientId}'; //后台null，这里就是空串

           
            //-----------------------------------------------------------------------
            
          
	        
            var txtUserID = action == 'insert' ? new Ext.form.field.Text({
                name: 'email',
                fieldLabel: '电子邮件'+'<font color=red>*</font>',
                maxLength: 20,
                //readOnly: ((action == 'update') ? true : false),
                allowBlank: false,
                vtype:'email'
              
            }): Ext.widget('displayfield',{
            	  name: 'userId',
                  fieldLabel: '电子邮件'+'<font color=red>*</font>'
               });
            var txtUserName = new Ext.form.field.Text({
                name: 'userName',
                fieldLabel: '用户姓名'+'<font color=red>*</font>',
                maxLength: 20,
                allowBlank: false
              
            });
          
            var txtPwd =  {
                xtype: 'textfield',
                inputType: 'password',
                name : 'plainPassword',
                fieldLabel: '密码'+'<font color=red>*</font>',
                maxLength :10,
                id:'plainPassword',
                hidden:action=='update'?true:false,
               	allowBlank: false
            };
            var txtPwdConfirm =   {
                xtype: 'textfield',
                inputType: 'password',
                fieldLabel: '确认密码'+'<font color=red>*</font>',
                name : 'pwd',
                maxLength :10,
                allowBlank: false,
                validator: function (value) {
                        var pwd = this.previousSibling().value;
                        if (value != pwd) {
                            return '两次输入的密码不一致！';
                        }
                        else {
                            return true;
                        }
                    },
                     hidden:action=='update'?true:false
            };    

            var txtPhone = new Ext.form.field.Text({
                name: 'phone',
                fieldLabel: '手机'+'<font color=red>*</font>',
                allowBlank: false,
                maxLength: 20
                //regex: /^((\d{3,4}-)*\d{7,8}(-\d{3,4})*|13\d{9})$/  	
              
            });
          
            
          
          
           
            var formRight = new Ext.form.Panel({
                region: 'center',
                title: titleName(action),
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                fieldDefaults: { labelAlign: 'right', labelWidth: 65, width: 245},
                items: [{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtUserID, txtUserName ]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtPwd, txtPwdConfirm]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtPhone]
                }],

                buttons: [{
                    type: 'submit',
                    text: '确 定',
                    handler: handleProcess,
                    id:'submit'
                   
                }, {
                    type: 'button',
                    text: '关 闭',
                    handler: function () { CloseWindow(); }
                }],
                
                listeners: {
                }
            });
        
         
          //用户Model
            
             <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.ClientContact", "Model", "","")%>
            //用户列表Store
            var store = Ext.create('Ext.data.Store', {
                autoLoad: false,
                model: 'Model',
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>client/contact/one',
                    reader: { type: 'json'}
                    
                },
                listeners: {
                    load: function (store, options) {
                       
                        formRight.getForm().loadRecord(store.getAt(0));
                    }
                }
        	});
            
            

            //表单自动加载数据 -----------------------------------------------------------------------------------------------------
            if (action != 'insert') {//update or view 
            	store.load({
                    params: {
                    	'id': id
                    }
                });
            }

            new Ext.container.Viewport({
                layout: 'border',
                items: [formRight]
            });

            //函数区
            //----------------------------------------------------------------------------------------------------------------------


            function handleProcess() {
                //编辑--------------------------------------------------------------------------
                if (action == 'insert' || action == 'update') {
                    var action_url= '<%=basePath%>client/contact/save';
                    if(action == 'update'){
                    	action_url= '<%=basePath%>client/contact/update?id='+id;
                    }
                    
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
//                            params: { action: action, funidvalue: RightsIdArray },
                            params: { action: action ,clientId:clientId},
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
