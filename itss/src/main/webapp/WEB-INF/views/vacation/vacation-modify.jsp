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
	        var fromDateField = Ext.widget('datefield',{ 
	    	        id:"fromDateField",
	    	      //  format :'Y-m-d H:i:s',
	    	       format :'Y-m-d',
	                anchor: '100%',
	                fieldLabel: '开始时间',
	                name: 'beginDate',
	                value: simpleDate('${vacation.beginDate}') , // defaults to today
	                maxValue: new Date(),  // limited to the current date or prior
	                listeners:{
							change:function(_this, newValue, oldValue, eOpts){
									toDateField.setMinValue( newValue );
								}

		                }
		               
	            });

	       // alert('${vacation.beginDate}');
	        var toDateField =   Ext.widget('datefield',{ 
	                anchor: '100%',
	                fieldLabel: '结束时间',
	                format :'Y-m-d ',
	                name: 'endDate',
	                value:simpleDate('${vacation.endDate}')
	            });
            var txtDays =  Ext.widget('textfield',{

                name : 'days',
                fieldLabel: '请假天数',
                maxLength :100,
                value:'${vacation.days }'
             });	

            var storeFlowTemplate = Ext.create('Ext.data.Store', {
	            autoLoad: false,
	            fields: [
	                    'code',
	                    'name'
	                ],
	            data: [
	                    {code: '1', name: '年假'},
	                    {code: '2', name: '事假'}
	                  
	                    ]
	        });
            var txtVacationType =  Ext.create('Ext.form.ComboBox', {
	            name: 'vacationType',
	            fieldLabel: '休假类型'+'<font color=red>*</font>',
	            emptyText: '请选择',
	            store: storeFlowTemplate,
	            displayField: 'name',
	            valueField: 'code',
	            allowBlank: false,
	            labelAlign: 'right',
	            value:'${vacation.vacationType}'
	        });
            var txtReason =  Ext.widget('textarea', {
	            name: 'reason',
	            fieldLabel: '原因',
	            displayField: 'name',
	            valueField: 'code',
	            labelAlign: 'right',
	            value:'${vacation.reason }'
	        });
          
            var txtComment =  Ext.widget('displayfield', {
                name:'content',	
	            fieldLabel: '意见',
	            labelAlign: 'right',
	            value:'${commentList.get(0).getContent() }'
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
                        items: [txtReason]
                    } ,{
                        xtype: 'fieldcontainer',
                        layout: { type: 'hbox', align: 'middle' },
                        items: [txtComment]
                    } 

                    ],

                buttons: [{
                    type: 'submit',
                    text: '提交',
                    handler: function (){complete(true)}
                }, {
                    type: 'button',
                    text: '取消申请',
                    handler: function () { complete(false) }
                }]
            });
            
       
            
            


            new Ext.container.Viewport({
                layout: 'border',
                items: [formRight]
            });

            //函数区
            //----------------------------------------------------------------------------------------------------------------------


            function complete(flag) {
            	 var action_url= basepath+'/vacation/modifyVacation/${vacation.task.id }';
                    
                    var form = formRight.getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
                            params: { id:'${vacation.id }',processInstanceId:'${vacation.processInstanceId }',reApply:flag },
                            success: function (form, action) {
                                console.log(action);
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
