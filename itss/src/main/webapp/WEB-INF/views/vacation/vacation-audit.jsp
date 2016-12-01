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
         

	        
           /* 限制from最大不能超过今天，to最小不能小于from  by  wj   */
	        var fromDateField = Ext.widget('displayfield',{ 
	    	        id:"fromDateField",
	                anchor: '100%',
	                fieldLabel: '开始时间',
	                value:'${vacation.beginDate}'
	                
	            });
	        var toDateField =   Ext.widget('displayfield',{ 
	                anchor: '100%',
	                fieldLabel: '结束时间',
	                value: '${vacation.endDate}' // defaults to today
	            });

            var txtDays =  Ext.widget('displayfield',{

                fieldLabel: '请假天数',
                maxLength :100,
                value: '${vacation.days }'
             });	

            var txtVacationType =  Ext.widget('displayfield',{
	            fieldLabel: '休假类型'+'<font color=red>*</font>',
	            labelAlign: 'right',
	            value:'${vacation.vacationType}'
	        });
            var txtReason =  Ext.widget('textarea', {
	            fieldLabel: '原因',
	            labelAlign: 'right',
	            value:'${vacation.reason }'
	        });
          
             
	            var txtMyComment =  Ext.widget('textarea', {
	                name:'content',	
		            fieldLabel: '我的意见',
		            labelAlign: 'right'
		        });
          
           
            var formRight = new Ext.form.Panel({
                region: 'center',
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                fieldDefaults: { labelAlign: 'right', labelWidth: 65, width: 245 },
                items: [

                	{
                        xtype: 'fieldcontainer',
                        layout: { type: 'hbox', align: 'middle' },
                       
                        items: [fromDateField,toDateField]
                    },{
                        xtype: 'fieldcontainer',
                        layout: { type: 'hbox', align: 'middle' },
                        items: [txtDays,txtVacationType]
                    } ,{
                        xtype: 'fieldcontainer',
                        layout: { type: 'hbox', align: 'middle' },
                        items: [txtReason,txtMyComment]
                    } 

                    ],

                buttons: [{
                    type: 'submit',
                    text: '同 意',
                    handler: function (){complete(true)}
                }, {
                    type: 'button',
                    text: '不同意',
                    handler:  function (){complete(false)}
                }]
            });
            
       
            
            


            new Ext.container.Viewport({
                layout: 'border',
                items: [formRight]
            });

            //函数区
            //----------------------------------------------------------------------------------------------------------------------


            function complete(completeFlag) {
            	 var action_url= basepath+'/vacation/complete/${vacation.task.id }';
                    console.log(this);
                    var form = formRight.getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
                            params: { completeFlag: completeFlag,vacationId: '${vacation.id }' },
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
