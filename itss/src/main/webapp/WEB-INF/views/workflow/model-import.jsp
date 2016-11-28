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


           
            //-----------------------------------------------------------------------
			
			var fileField = Ext.widget('filefield',{
			        name: 'file',
			        fieldLabel: '上传附件',
			        msgTarget: 'side',
			        allowBlank: false,
			        anchor: '100%',
			        buttonText: '浏览'

				});
      

		    
            var txtDescription =  Ext.widget('textfield',{

                   name : 'description',
                   fieldLabel: '描述',
                   maxLength :100
                });
             
           
           
            var formRight = new Ext.form.Panel({
                region: 'center',
              //  title: '用户信息&nbsp;&nbsp;' + titleName(action),
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                fieldDefaults: { labelAlign: 'right', labelWidth: 65, width: 245 },
                items: [

                	{
                        xtype: 'fieldcontainer',
                        layout: { type: 'vbox', align: 'middle' },
                       
                        items: [txtDescription,fileField]
                    } 

                    ],

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
            


            new Ext.container.Viewport({
                layout: 'border',
                items: [formRight]
            });

            //函数区
            //----------------------------------------------------------------------------------------------------------------------
            //标题名称


            function handleProcess() {
            	 var action_url = basepath+'/workflow/process/deploy' ;;
                    
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
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

//--------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
