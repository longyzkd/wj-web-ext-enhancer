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

            var contractId;
           
            //-----------------------------------------------------------------------
            
          
	        
            var txtContractName = new Ext.form.field.Text({
                   name: 'contractName',
                   fieldLabel: '合同名称'+'<font color=red>*</font>',
                   maxLength: 20,
                   allowBlank: false,
                   vtype:'checkunique',
                   beanClazz:'Contract',
                   property:'contractName',
                   action:action
               });

            
            var contactStore = Ext.create('Ext.data.Store', {  
            	 autoLoad: true,
                fields: ['code', 'name'],  
                proxy: {  
                    type: 'ajax',  
                    url: '<%=basePath%>contract/clientContactData/'+clientId,
                    reader: { type: 'json', root: 'dataList' }
                }  
            });     
            var clientContactField = Ext.create('Ext.form.ComboBox', {
                fieldLabel: '客户方联系人',
                name:'clientContactId',
                store: contactStore,
                displayField: 'name',
                valueField: 'code',
                allowBlank: false,
                editable :false
            });
               
               
            var contractNoField = new Ext.form.field.Text({
                name: 'contractNo',
                fieldLabel: '合同编号'+'<font color=red>*</font>',
                maxLength: 20,
                allowBlank: false
            });

            var seviceLvStore = Ext.create('Ext.data.Store', {  
            	 autoLoad: true,
                fields: ['code', 'name'],  
                proxy: {  
                    type: 'ajax',  
                    url: '<%=basePath%>contract/combo/ServiceLv',
                    reader: { type: 'json' }
                }  
            });     	
            var seviceLvField = Ext.create('Ext.form.ComboBox', {
                fieldLabel: '服务水平',
                name:'seviceLv',
                store: seviceLvStore,
                displayField: 'name',
                valueField: 'code',
                allowBlank: false,
                editable :false
            });
               
               
            var contractTypeStore = Ext.create('Ext.data.Store', {
	            autoLoad: true,
	            fields: [
	                    'code',
	                    'name'
	                ],
	            data: [
	                    {code: '1', name: '销售合同'},
	                    {code: '2', name: '服务合同'},
	                    {code: '3', name: '销售服务合同'}
	                    ]
	        });
            var contractTypeField = Ext.create('Ext.form.ComboBox', {
                fieldLabel: '合同类型',
                name:'contractType',
                store: contractTypeStore,
                displayField: 'name',
                valueField: 'code',
                editable :false
            });

            var contractAmtField = new Ext.form.field.Number({
                name: 'contractAmt',
                fieldLabel: '合同金额',
                maxLength: 20,
                allowDecimals:true,
                // Remove spinner buttons, and arrow key and mouse wheel listeners
                hideTrigger: true,
                keyNavEnabled: false,
                mouseWheelEnabled: false,
                minValue :0
            });




            /* 限制from最大不能超过今天，to最小不能小于from  by  wj   */
	        var serviceBeginTimeField = Ext.widget('datefield',{ 
	    	        id:"fromDateField",
	    	      //  format :'Y-m-d H:i:s',
	    	       format :'Y-m-d',
	                anchor: '100%',
	                fieldLabel: '服务起始时间',
	                name: 'serviceBeginTime',
	                allowBlank: false,
	             //   value: new Date() , // defaults to today
	                maxValue: new Date(),  // limited to the current date or prior
	                listeners:{
							change:function(_this, newValue, oldValue, eOpts){
								serviceEndTimeField.setMinValue( newValue );
								}

		                }
	            });
	        var serviceEndTimeField =   Ext.widget('datefield',{ 
	                anchor: '100%',
	                fieldLabel: '结束时间',
	                format :'Y-m-d ',
	                name: 'serviceEndTime',
	                allowBlank: false,
	                value: new Date()  // defaults to today
	            });


	        var descField =  Ext.widget('textarea', {
	            name: 'desc',
	            fieldLabel: '描述',
	            labelAlign: 'right'
	        });

	        
            var formRight = new Ext.form.Panel({
                region: 'center',
                title: '合同',
                width: '100%',
                bodyPadding: 10,
                buttonAlign: 'center',
                fieldDefaults: { labelAlign: 'right', labelWidth: 120, width: 325
                    /* 必须注释，数字类型会覆盖combox,datefield这些类型，导致不显示正确类型
                    , minValue: 0, //prevents negative numbers

                    // Remove spinner buttons, and arrow key and mouse wheel listeners
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false,
                    allowDecimals:false  */},
                items: [{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [txtContractName,clientContactField ]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [contractNoField, seviceLvField]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [contractTypeField, contractAmtField]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [serviceBeginTimeField, serviceEndTimeField]
                },{
                    xtype: 'fieldcontainer',
                    layout: { type: 'hbox', align: 'middle' },
                    items: [descField]
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
            
             <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.Contract", "Model", "","")%>
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

                       // console.log(store.getAt(0));

                        if(action == 'update'){ //只有在修改的时候（看输入的是否和库里的冲突         ） 才需要原始值（新增、查看{都不需要编辑} 都不需要）
                       		Ext.apply(txtContractName, {myrawValue:store.getAt(0).get('contractName')}, {});//在loadRecord之前先赋值，否则myrawValue会为空
                        }
                        
                        formRight.getForm().loadRecord(store.getAt(0));

                        contractId  = store.getAt(0).get('id');
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
//---------------------------tab2

				var addBtn= {
	            	id: 'uploadBtn',
	                text: '上传',
	                iconCls: 'x-button-insert',
	                handler: uploadWin
	            },delBtn= {
	            	id: 'delBtn',
	                text: '删除',
	                iconCls: 'x-button-delete',
	                handler: deleteThem
	            };
	        var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [addBtn,'-',delBtn]
	        });
	       
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.ContractAttach", "AttachModel", "","")%>
        var storeAttach = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'AttachModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>contract/attach/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                }
            }
        });
        var sm = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
		  var attachGrid = Ext.create('Ext.grid.Panel', {
	            region: 'center',
	            title: '附件',
	            store: storeAttach,
	            dockedItems: [tbar1],
	            //dockedItems: [tbar2,tbar1],
	            selModel: sm,
	            columnLines: false,
	            columns: [
						new Ext.grid.PageRowNumberer(),
						{ header: '文件名', dataIndex: 'attachName', align: 'center', flex:1}
					],
					bbar: createPage(storeAttach)
	        });


	        
 //--------总的           
           var tab=  Ext.create('Ext.tab.Panel', {
        	   layout: 'fit',
               autoScroll: false,
               border: false,
               region: 'center',
                items: [ formRight, attachGrid]

            });

            
            new Ext.container.Viewport({
                layout: 'border',
                items: [tab]
            });

            //函数区
            //----------------------------------------------------------------------------------------------------------------------

            function uploadWin(){
                //alert(1);
        			var fileField = Ext.widget('filefield',{
    			        name: 'file',
    			        fieldLabel: '上传附件',
    			        msgTarget: 'side',
    			        allowBlank: false,
    			        anchor: '100%',
    			        buttonText: '浏览'

    				});
          
            	  var form = new Ext.form.Panel({
                      fieldDefaults: {
                          labelAlign: 'right',
                          msgTarget: 'side',
                          labelWidth: 70
                      },
                      bodyStyle: 'padding:15px',
                      items: [fileField]
                  });

                  
            	 var win = new Ext.window.Window({
                     title: '文件上传 ',
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
                                     url: '<%=path%>/contract/attach/upload',
                                     method: 'POST',
                                     params: {
                                    	 contractId:contractId
                                     },
                                     success: function (form, action) {
                                         Ext.Msg.show({
                                             title: '提示',
                                             msg: action.result.msg,
                                             buttons: Ext.Msg.CANCEL,
                                             icon: Ext.Msg.INFO,
                                             fn: function () {
                                                 win.close();
                                                 storeAttach.load();
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
                         handler: function () { win.close(); storeAttach.load(); }
                     }]
                 });

          	   win.show();

            }
            
            function deleteThem(){


             }

            
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
