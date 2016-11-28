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
            
         

	        
            var txtServiceName =  new Ext.form.field.Text({
                name: 'name',
                fieldLabel: '服务项名称'+'<font color=red>*</font>',
                maxLength: 20,
                //readOnly: ((action == 'update') ? true : false),
                allowBlank: false
              
            });
            var txtServiceCode = new Ext.form.field.Text({
                name: 'key',
                fieldLabel: '服务项代码'+'<font color=red>*</font>',
                maxLength: 20,
                allowBlank: false
              
            });

            var storeType = Ext.create('Ext.data.Store', {
	            autoLoad: false,
	            fields: [
	                    'code',
	                    'name'
	                ],
	            data: [
	                    {code: '1', name: '服务台类（事件、变更、问题，投诉等）'},
	                    {code: '2', name: '报告类（巡检、咨询等）'}
	                  
	                    ]
	        });
            var txtServiceType =  Ext.create('Ext.form.ComboBox', {
	            name: 'serviceType',
	            fieldLabel: '服务项类型'+'<font color=red>*</font>',
	            emptyText: '请选择',
	            store: storeType,
	            displayField: 'name',
	            valueField: 'code',
	            allowBlank: false,
	            labelAlign: 'right'
	        });

            var storeFlowTemplate = Ext.create('Ext.data.Store', {
	            autoLoad: false,
	            fields: [
	                    'code',
	                    'name'
	                ],
	            data: [
	                    {code: '1', name: '服务台类（事件、变更、问题，投诉等）'},
	                    {code: '2', name: '报告类（巡检、咨询等）'}
	                  
	                    ]
	        });
            var txtFlowTemplate =  Ext.create('Ext.form.ComboBox', {
	            name: 'flowTemplate',
	            fieldLabel: '服务流程模板'+'<font color=red>*</font>',
	            emptyText: '请选择',
	            store: storeFlowTemplate,
	            displayField: 'name',
	            valueField: 'code',
	            allowBlank: false,
	            labelAlign: 'right'
	        });
          
            var txtDescription =  Ext.widget('textfield',{

                   name : 'description',
                   fieldLabel: '描述',
                   maxLength :100
                });
             
           
           
            var formRight = new Ext.form.Panel({
                region: 'center',
                title: '用户信息&nbsp;&nbsp;' + titleName(action),
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                fieldDefaults: { labelAlign: 'right', labelWidth: 65, width: 245 },
                items: [

                	{
                        xtype: 'fieldcontainer',
                        layout: { type: 'hbox', align: 'middle' },
                       
                        items: [txtServiceName,txtServiceCode]
                    },{
                        xtype: 'fieldcontainer',
                        layout: { type: 'hbox', align: 'middle' },
                        items: [txtServiceType,txtFlowTemplate]
                    } ,{
                        xtype: 'fieldcontainer',
                        layout: { type: 'hbox', align: 'middle' },
                        items: [txtDescription]
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
           if(action=='update'){
           	
           		formRight.remove(txtServiceType).remove(txtFlowTemplate);
           }
           //保证顺序之用
          // formRight.add(treeRights);
           
           /*
           return formRight;
           		
           }
           */
            
          //用户Model
            
            Ext.define('WorkflowModel', {
	            extend: 'Ext.data.Model',
	            fields: [
	                {name: 'id',  type: 'string'},
	                {name: 'key',   type: 'string'},
	                {name: 'name', type: 'string'},
	                {name: 'version', type: 'string'},
	                {name: 'createTime'},
	                {name: 'lastUpdateTime	'},
	                {name: 'metaInfo'},
	            ]

	        });
            var store = Ext.create('Ext.data.Store', {
                autoLoad: false,
                model: 'WorkflowModel',
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>workflow/model/one',
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
            	store.load({
                    params: {
                    	'modelId': id
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
            	 var action_url= '<%=basePath%>workflow/model';
                if (action == 'insert') {
                	action_url += '/create';
                }else{
                	action_url += '/update?modelId='+id;
                 }
                    
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            submitEmptyText: false,
                            clientValidation: true,
                            url: action_url,
                            method: 'POST',
                          //  params: { action: action },
                            success: function (form, action) {
                                console.log(action);
                                Ext.Msg.show({
                                    title: '提示',
                                    msg: action.result.msg,
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.INFO,
                                    fn: CloseWindow
                                });
								if(action.result.success){
									window.open(basepath+"/modeler.html?modelId=" + action.result.data);
								}
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
