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

           
            //-----------------------------------------------------------------------
            
          
	        
            var txtName = new Ext.form.field.Text({
                   name: 'unitName',
                   fieldLabel: '单位名称'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
               
            var txtAddr = new Ext.form.field.Text({
                   name: 'addr',
                   fieldLabel: '地址',
                   maxLength: 20
               });
               
               
            var txtPostCode = new Ext.form.field.Text({
                   name: 'postCode',
                   fieldLabel: '邮编',
                   maxLength: 20,
                   regex:/^[1-9][0-9]{5}$/,
                   regexText:'格式不对'
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
                    items: [txtName ]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtAddr,txtPostCode]
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
            
             <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.Client", "Model", "","")%>
            //用户列表Store
            var store = Ext.create('Ext.data.Store', {
                autoLoad: false,
                model: 'Model',
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>client/one',
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
                    var action_url= '<%=basePath%>client/save';
                    if(action == 'update'){
                    	action_url= '<%=basePath%>client/update?id='+id;
                    }
                    
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
//                            params: { action: action, funidvalue: RightsIdArray },
                            params: { action: action }, //no use
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
