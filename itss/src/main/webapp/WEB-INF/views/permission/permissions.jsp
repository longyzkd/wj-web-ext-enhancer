<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<jsp:include page="../_LayoutCommonExtJS.jsp"/>
  	<style type="text/css">
  		tr.x-grid-record-red .x-grid-td {
			   background: #E6D1E3;
			}
			tr.x-grid-record-yellow .x-grid-td{
			   background: #F3FEC2;
			}
		tr.x-grid-record-green .x-grid-td{
		   background: #92FCCC;
		}
  	</style>
	<script type="text/javascript">
		Ext.onReady(function(){
			
			Ext.QuickTips.init();
			//顺序最好为，先store->插件->grid->viewPort ,从小到大
	        
//---------组
	       
	        
// 	        //组织列表Panel
// 	        var permission_smOrg = Ext.create('Ext.selection.CheckboxModel',{
// 	    	            mode: 'SINGEL' ,
// 						listeners:{
// 							selectionchange: function (_this, selected, eOpts ){

// 								storeRights.load();
// 							}

// 						}
// 		    	    }
// 	       	 );

	        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.SysOrg", "SysOrgModel", "","")%>
	        var storeOrg = Ext.create('Ext.data.Store', {
	            autoLoad: true,
	            model: 'SysOrgModel',
	            proxy: {
	                type: 'ajax',
	                url: '<%=basePath%>org/listOrgs',
	                reader: { type: 'json', root: 'dataList' }
	            },
	            listeners: {
	                beforeload: function (store, options) {
	                
	                },
	                load:function(){
	                	//permission_smOrg.select(0);
	                	gridOrg.getSelectionModel().select(0);
		              }
	            }
	        });


	        var cboOrgPage = Ext.create('Ext.form.ComboBox', {
	            store: new Ext.data.ArrayStore({
	                fields: ['text', 'value'],
	                data: [['10', 10], ['15', 15], ['20', 20], ['30', 30], ['50', 50]]
	            }),
	            valueField: 'value',
	            displayField: 'text',
	            width: 50
	        });

	        cboOrgPage.setValue(50);

	        cboOrgPage.on('select', function (comboBox) {
	        	storeOrg.pageSize = parseInt(comboBox.getValue());
	        	storeOrg.loadPage(1);
	        });
			//要先定义store，在其下面才行
	        var pgOrgCfg = new Ext.PagingToolbar({
	            pageSize: 50,
	            store: storeOrg,
	            displayInfo: true,
	            displayMsg: '显示 {0} - {1} 条记录，总共 {2} 条记录',
	            emptyMsg: "没有记录",
	            items: ['-', '每页显示', cboOrgPage, '条']
	        });
	        
	        var gridOrg = Ext.create('Ext.grid.Panel', {
	            region: 'center',
	            store: storeOrg,
	            //selModel: permission_smOrg,
	            columnLines: false,
	            columns: [
						new Ext.grid.PageRowNumberer(),
						{ header: '组名称', dataIndex: 'name', align: 'center', flex:1}
					],
				bbar: pgOrgCfg,
				listeners:{
					selectionchange: function (_this, selected, eOpts ){
						storeRights.load();
					}

				}
	        });



	        
 //------------------权限       
 
  
	  var tbar_permission = Ext.create('Ext.toolbar.Toolbar', {
            items: [{
            	id: 'addBtn',
                text: '保存',
                iconCls: 'x-button-save',
                handler: function () {
                	editPermission();
                }
            }]
        });

	        
	  Ext.define('RightsModel', {
          extend: 'Ext.data.Model',
          idProperty: 'menuId',
          fields: [
  			{ name: 'menuId', type: 'string' },
              { name: 'pmenuId', type: 'string' },
              { name: 'menuName', type: 'string' },
              { name: 'note', type: 'string' }
  		]
      });
        //权限树Store
        var storeRights = Ext.create('Ext.data.TreeStore', {
                autoLoad: false,
                model: 'RightsModel',
                folderSort: false,
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>permission/checkedTree',
                    reader: { type: 'json' }
                   // reader: { type: 'json' , root: 'obj' }
                },
               	listeners: {
                    beforeload: function (store, options) {
                        //跟orgs.jsp一样的逻辑，这里必须这么处理，会请求两次，第一次空串，第二次orgId. 莫名其妙
                        //根据请求的方法栈来看，树会setRootNode，这个时候会自己请求一次树store  by wj 2016-11-14
                        var orgId = gridOrg.getSelectionModel( ).getLastSelected()?gridOrg.getSelectionModel( ).getLastSelected().get('id'):'';

                        //console.log(orgId);
                        var params = { 'orgId': orgId};
                        Ext.apply(store.proxy.extraParams, params);
                    }
                }
            });
        
        var cmRights = [{
            xtype: 'treecolumn',
            text: '名称',
            sortable: false,
            dataIndex: 'menuName',
            width: 250,
            titleAlign: 'center'
        }, {
            text: '编号',
            dataIndex: 'menuId',
            width: 100,
            titleAlign: 'center'
        }, {
            text: '备注',
            sortable: false,
            flex: 1,
            dataIndex: 'note',
            titleAlign: 'center'
        }];
        
        var treeRights = Ext.create('Ext.tree.Panel', {
            title: '权限',
            store: storeRights,
            rootVisible: false,
            frame: false,
            columnLines: true,
            checkModel: "parentCascade",
            multiSelect: false,
            singleExpand: false,
            columns: cmRights,
            height: 390,
            tbar:tbar_permission,
            listeners: {
                "checkchange": function (node, state) {
                    //-------------------------------------------------
                    //级联勾选多个父级节点
                    checkedParentNode(node, state)
                    //-------------------------------------------------
                    //级联勾选子级节点
                    node.cascadeBy(function (child) {
                        child.set("checked", state);
                    });
                    //-------------------------------------------------
                }
            }
        });


        function checkedParentNode(node, state) {
            if (node.parentNode != null) {
                if (state) {
                    node.parentNode.set('checked', state);
                }
                checkedParentNode(node.parentNode, state)
            }
        }

        
  //-----整体
  
 var panel=	Ext.create('Ext.panel.Panel', {
		    width: 500,
		    height: 400,
		    layout: 'border',
		    items: [{
		    	title: '组列表',
		        region:'west',
		        xtype: 'panel',
		        margins: '5 0 0 5',
		        width: 200,
		        collapsible: true,   // make collapsible
		        split: true,
		        id: 'west-region-container',
		        layout: 'fit',
		        items:gridOrg
		    },{
		        title: '功能列表',
		        region: 'center',     // center region is required, no width/height specified
		        xtype: 'panel',
		        layout: 'fit',
		        margins: '5 5 0 0',
		        items:treeRights
		    }],
		});     
        
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: panel
        });
        
        
	       
	    
//函数区        
//----------------------------------------------------------------------------------------------------------

        
        
        //编辑权限
        function editPermission(){
        	
        	 var records = treeRights.getChecked();
             if ( records.length < 1) {
                 Ext.Msg.alert('提示', '请选择权限！');
                 return false;
             }
             var RightsIdArray = [];
             Ext.each(records, function (record) {
                 if(record.data['menuId']!=='root'){
	                 RightsIdArray.push(record.data['menuId']);
                   }

             });
             var action_url= '${ctx}/permission/checkTree';

             Ext.Ajax.request({
                 url: action_url,
                 params: { orgId:gridOrg.getSelectionModel().getLastSelected( ).get('id') ,permissionIds: RightsIdArray  },
                 success: function (response) {
                     var responseText = Ext.decode(response.responseText);
                     Ext.Msg.show({
                         title: '提示',
                         msg: responseText.msg,
                         buttons: Ext.Msg.OK,
                         icon: Ext.Msg.INFO,
                         fn: function(){storeRights.reload();}
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
        
        
        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
