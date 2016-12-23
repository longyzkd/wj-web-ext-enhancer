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
            var type = '${type}'; //后台null，这里就是空串

           
            //-----------------------------------------------------------------------
            
          
	        
            var nameField = action == 'view' ? Ext.widget('displayfield',{
            	  name: 'name',
                  fieldLabel: '名称'+'<font color=red>*</font>'
               }): new Ext.form.field.Text({
                   name: 'name',
                   fieldLabel: '名称'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false,
//                   beanClazz:'me.entity.User',
                   vtype:'checkunique',
                   beanClazz:'Supplier',
                   property:'name',
                   action:action
                  // myrawValue:nameField.getValue()
               });
               
            
            var taxNoField = action == 'view' ? Ext.widget('displayfield',{
          	  name: 'taxNo',
              fieldLabel: '税务号'
           }): new Ext.form.field.Text({
               name: 'taxNo',
               fieldLabel: '税务号',
               maxLength: 20
             
           });
          
            var addrField = action == 'view' ? Ext.widget('displayfield',{
          	  name: 'addr',
              fieldLabel: '地址'
           }): new Ext.form.field.Text({
               name: 'addr',
               fieldLabel: '地址',
               maxLength: 20
             
           });
          
            var bankField = action == 'view' ? Ext.widget('displayfield',{
          	  name: 'bank',
              fieldLabel: '银行'
           }): new Ext.form.field.Text({
               name: 'bank',
               fieldLabel: '银行',
               maxLength: 20
             
           });
          
            var postCodeField = action == 'view' ? Ext.widget('displayfield',{
          	  name: 'postCode',
              fieldLabel: '邮编'
           }): new Ext.form.field.Text({
               name: 'postCode',
               fieldLabel: '邮编',
               maxLength: 20
             
           });
          
            var bankAccountField = action == 'view' ? Ext.widget('displayfield',{
          	  name: 'bankAccount',
              fieldLabel: '银行账号'
           }): new Ext.form.field.Text({
               name: 'bankAccount',
               fieldLabel: '银行账号',
               maxLength: 20
             
           });
          
            var businessLicenseNoField = action == 'view' ? Ext.widget('displayfield',{
          	  name: 'businessLicenseNo',
              fieldLabel: '营业执照号码'
           }): new Ext.form.field.Text({
               name: 'businessLicenseNo',
               fieldLabel: '营业执照号码',
               maxLength: 20
             
           });
          
          
           
            var formRight = new Ext.form.Panel({
                region: 'center',
                id:'formRight',
                title: titleName(action),
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                fieldDefaults: { 
                	//anchor: '100%',
                    
                    labelAlign: 'right', 
                    labelWidth: 95, 
                    width: 245
                   // combineErrors: true,
                   // msgTarget: 'side'
                },
                items: [{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [nameField, taxNoField]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [addrField, bankField]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [postCodeField, bankAccountField]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [businessLicenseNoField]
                }],

                buttons: [{
                    type: 'submit',
                    text: '确 定',
                    handler: handleProcess,
                    id:'submit',
                    hidden:action==='view'
                }, {
                    type: 'button',
                    text: '关 闭',
                    handler: function () { CloseWindow(); }
                }],
                
                listeners: {
                	afterlayout: function (_this, options) {
                    	if(action==='view'){
                		//	_this.remove(Ext.getCmp('submit'));
                        }
                    }
                }
            });
        
         
          //用户Model
            
             <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.Supplier", "Model", "","")%>
            //用户列表Store
            var store = Ext.create('Ext.data.Store', {
                autoLoad: false,
                model: 'Model',
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>supplier/one',
                    reader: { type: 'json'}
                    
                },
                listeners: {
                    load: function (store, options) {

                    	if(action == 'update'){ //只有在修改的时候（看输入的是否和库里的冲突         ） 才需要原始值（新增、查看{都不需要编辑} 都不需要）
                       		//Ext.apply(nameField, {myrawValue: Ext.getCmp('formRight').getForm().getRecord().get('name')}, {});
                       		Ext.apply(nameField, {myrawValue:store.getAt(0).get('name')}, {});//在loadRecord之前先赋值，否则myrawValue会为空
                        }
                          
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
                    var action_url= '<%=basePath%>supplier/save';
                    if(action == 'update'){
                    	action_url= '<%=basePath%>supplier/update?id='+id;
                    }
                    
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
//                            params: { action: action, funidvalue: RightsIdArray },
                            params: { action: action,type:type },
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
