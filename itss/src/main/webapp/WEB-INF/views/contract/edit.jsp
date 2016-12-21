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
                   name: 'name',
                   fieldLabel: '服务名称'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
            var txtType = Ext.widget('fieldcontainer',{
                fieldLabel : '服务水平类型'+'<font color=red>*</font>',
                defaultType: 'radiofield',
                defaults: {
                    flex: 1
                },
                layout: 'hbox',
                items: [
                    {
                        boxLabel  : '工作日',
                        name      : 'type',
                        inputValue: 'workday',
                        id        : 'workday',
                        checked:true
                    }, {
                        boxLabel  : '无休',
                        name      : 'type',
                        inputValue: 'no-rest',
                        id        : 'radio2'
                    }
                ]
            });
               
               
            var storeHour = Ext.create('Ext.data.Store', {
                fields: ['code'],
                data : [
                    {"code":"0"}, {"code":"1"}, {"code":"2"}, {"code":"3"}, {"code":"4"}, {"code":"5"}, {"code":"6"}, {"code":"7"},
                    {"code":"8"}, {"code":"9"}, {"code":"10"}, {"code":"11"}, {"code":"12"}, {"code":"13"}, {"code":"14"}, {"code":"15"},
                    {"code":"16"}, {"code":"17"}, {"code":"18"}, {"code":"19"}, {"code":"20"}, {"code":"21"}, {"code":"22"}, {"code":"23"}, {"code":"24"}
                    //...
                ]
            });
               
            var txtServHour = Ext.widget('fieldcontainer',{
                fieldLabel : '服务起始时间'+'<font color=red>*</font>',
                
                defaults: {
                    flex: 1
                },
                
                layout: 'hbox',
                items: [
                	Ext.create('Ext.form.ComboBox', {
                    	name:'servBeiginHour',
                	    fieldLabel: '开始',
                	    store: storeHour,
                	    labelWidth: 40, width: 100,editable :false,
                	    queryMode: 'local',
                	    displayField: 'code',
                	    valueField: 'code',
                	    value:0
                	   
                	}),Ext.create('Ext.form.ComboBox', {
                		name:'servEndHour',
                	    fieldLabel: '截止',
                	    labelWidth: 40, width: 100,editable :false,
                	    store: storeHour,
                	    queryMode: 'local',
                	    displayField: 'code',
                	    valueField: 'code',
                	    value:24
                	   
                	})
                ]
            });


               
               
            var txtHighPriRespHour = new Ext.form.field.Number({
                   name: 'highPriRespHour',
                   fieldLabel: '高优先级响应时间'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
               
            var txtHighPriSolveHour = new Ext.form.field.Number({
                   name: 'highPriSolveHour',
                   fieldLabel: '高优先级解决时间'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
               
            var txtMidPriRespHour = new Ext.form.field.Number({
                   name: 'midPriRespHour',
                   fieldLabel: '中优先级响应时间'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
            var txtMidPriSolveHour = new Ext.form.field.Number({
                   name: 'midPriSolveHour',
                   fieldLabel: '中优先级解决时间'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
            var txtLowPriRespHour = new Ext.form.field.Number({
                   name: 'lowPriRespHour',
                   fieldLabel: '低优先级响应时间'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
            var txtLowPriSolveHour = new Ext.form.field.Number({
                   name: 'lowPriSolveHour',
                   fieldLabel: '低优先级解决时间'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false
               });
               
            
          
          
           
            var formRight = new Ext.form.Panel({
                region: 'center',
                title: titleName(action),
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                fieldDefaults: { labelAlign: 'right', labelWidth: 120, width: 325, minValue: 0, //prevents negative numbers

                    // Remove spinner buttons, and arrow key and mouse wheel listeners
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false,
                    allowDecimals:false  },
                items: [{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtName ]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtType, txtServHour]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtHighPriRespHour, txtHighPriSolveHour]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtMidPriRespHour, txtMidPriSolveHour]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtLowPriRespHour, txtLowPriSolveHour]
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
                }
            });
        
         
          //用户Model
            
             <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.ServiceLv", "Model", "","")%>
            //用户列表Store
            var store = Ext.create('Ext.data.Store', {
                autoLoad: false,
                model: 'Model',
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>contract/one',
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
                    var action_url= '<%=basePath%>contract/save';
                    if(action == 'update'){
                    	action_url= '<%=basePath%>contract/update?id='+id;
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
