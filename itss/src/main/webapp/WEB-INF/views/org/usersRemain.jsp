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
            var orgId = '${orgId}'; //后台null，这里就是空串

         //------------------用户       
 
	        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.User", "UserModel", "","")%>
        //用户列表Store
        var storeUser = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model: 'UserModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>org/orgUsersRemain',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
                	var params = {'orgId': orgId};
                	
                		Ext.apply(store.proxy.extraParams, params);
                	
            		
                }
            }
        });
        
        var cboPage = Ext.create('Ext.form.ComboBox', {
            store: new Ext.data.ArrayStore({
                fields: ['text', 'value'],
                data: [['10', 10], ['15', 15], ['20', 20], ['30', 30], ['50', 50]]
            }),
            valueField: 'value',
            displayField: 'text',
            width: 50
        });

        cboPage.setValue(50);

        cboPage.on('select', function (comboBox) {
        	storeUser.pageSize = parseInt(comboBox.getValue());
        	storeUser.loadPage(1);
        });

        var pgCfg = new Ext.PagingToolbar({
            pageSize: 50,
            store: storeUser,
            displayInfo: true,
            displayMsg: '显示 {0} - {1} 条记录，总共 {2} 条记录',
            emptyMsg: "没有记录",
            items: ['-', '每页显示', cboPage, '条']
        });
        
        //用户列表Panel
        var smUser = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var gridUser = Ext.create('Ext.grid.Panel', {
            region: 'center',
            id:'gridUser',
            store: storeUser,
            selModel: smUser,
            columnLines: false,
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '姓名', dataIndex: 'username', align: 'center', flex:1 },
                    { header: '所在部门', dataIndex: 'deptName', align: 'center',flex:1}
				],
			bbar: pgCfg,
			buttonAlign:'center',
			 buttons: [{
	                    type: 'submit',
	                    text: '确 定',
	                    handler: handleProcess
	                }, {
	                    type: 'button',
	                    text: '关 闭',
	                    handler: function () { CloseWindow(); }
	                }],
               listeners: {
            	   afterrender:function (_this,eopts){
                	   //console.log(_this);
                	  }
               }
        });

        
        new Ext.container.Viewport({
            layout: 'border',
            items: [gridUser]
        });


//         gridUser.on('afterrender',function(_this,eopts){
//         		_this.buttons.dom.align = 'center';
//             });
     //函数区
     //----------------------------------------------------------------------------------------------------------------------

            //关闭窗口刷新
            function closeRefresh() {
            	CloseWindow();
            }

            function handleProcess() {
            	 var res = checkBoxUsers();
            	// console.log(res);
                //编辑--------------------------------------------------------------------------
                    var action_url= '<%=basePath%>org/saveUsers';
                    Ext.Ajax.request({
                        url: action_url,
                        params: { orgId:orgId,userIds: res  },
                        success: function (response) {
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: CloseWindow  // 弹出窗口里面 提交特用
                            });
                        },
                        failure: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                        }
                    });
                   

            }


            function checkBoxUsers() {
                var ids = [];
                var selectedCount = smUser.getCount();
                if (selectedCount == 0) {
                    Ext.Msg.show({ title: '提示', msg: '未选中任何一个用户，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                    return false;
                } else {
                    Ext.each(smUser.getSelection( ),function(cur){

                    	ids.push(cur.data.userId);
                    	
                      }) ;

                    
                    return ids;
                }
            }
//--------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
