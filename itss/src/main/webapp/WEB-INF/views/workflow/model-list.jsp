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
	        
//---------服务目录树
	       
// 	        var tbar_left = Ext.create('Ext.toolbar.Toolbar', {
//             items: [{
//             	id: 'addBtn',
//                 text: '新增',
//                 iconCls: 'x-button-save',
//                 handler: function () {
//                 	editServiceCategory();
//                 }
//             },{
//             	id: 'addBtn',
//                 text: '修改',
//                 iconCls: 'x-button-save',
//                 handler: function () {
//                 	editServiceCategory('edit');
//                 }
//             },{
//             	id: 'addBtn',
//                 text: '删除',
//                 iconCls: 'x-button-save',
//                 handler: function () {
//                 	delServiceCategory();
//                 }
//             },]
//         });

	        
// 	  Ext.define('RightsModel', {
//           extend: 'Ext.data.Model',
//           idProperty: 'menuId',
//           fields: [
//   			{ name: 'menuId', type: 'string' },
//               { name: 'pmenuId', type: 'string' },
//               { name: 'menuName', type: 'string' },
//               { name: 'note', type: 'string' }
//   		]
//       });
//         //权限树Store
//         var storeRights = Ext.create('Ext.data.TreeStore', {
//                 autoLoad: false,
//                 model: 'RightsModel',
//                 folderSort: false,
//                 proxy: {
//                     type: 'ajax',
<%--                     url: '<%=basePath%>permission/checkedTree', --%>
//                     reader: { type: 'json' }
//                    // reader: { type: 'json' , root: 'obj' }
//                 },
//                	listeners: {
//                     beforeload: function (store, options) {
//                         //跟orgs.jsp一样的逻辑，这里必须这么处理，会请求两次，第一次空串，第二次orgId. 莫名其妙
//                         //根据请求的方法栈来看，树会setRootNode，这个时候会自己请求一次树store  by wj 2016-11-14
//                         var orgId = gridWorkflowModel.getSelectionModel( ).getLastSelected()?gridWorkflowModel.getSelectionModel( ).getLastSelected().get('id'):'';

//                         //console.log(orgId);
//                         var params = { 'orgId': orgId};
//                         Ext.apply(store.proxy.extraParams, params);
//                     }
//                 }
//             });
        
//         var cmRights = [{
//             xtype: 'treecolumn',
//             text: '名称',
//             sortable: false,
//             dataIndex: 'menuName',
//             width: 250,
//             titleAlign: 'center'
//         }, {
//             text: '编号',
//             dataIndex: 'menuId',
//             width: 100,
//             titleAlign: 'center'
//         }, {
//             text: '备注',
//             sortable: false,
//             flex: 1,
//             dataIndex: 'note',
//             titleAlign: 'center'
//         }];
        
//         var treeRights = Ext.create('Ext.tree.Panel', {
//             title: '权限',
//             store: storeRights,
//             rootVisible: false,
//             frame: false,
//             columnLines: true,
//             checkModel: "parentCascade",
//             multiSelect: false,
//             singleExpand: false,
//             columns: cmRights,
//             height: 390,
//             tbar:tbar_permission,
//             listeners: {
//                 "checkchange": function (node, state) {
//                     //-------------------------------------------------
//                     //级联勾选多个父级节点
//                     checkedParentNode(node, state)
//                     //-------------------------------------------------
//                     //级联勾选子级节点
//                     node.cascadeBy(function (child) {
//                         child.set("checked", state);
//                     });
//                     //-------------------------------------------------
//                 }
//             }
//         });


//         function checkedParentNode(node, state) {
//             if (node.parentNode != null) {
//                 if (state) {
//                     node.parentNode.set('checked', state);
//                 }
//                 checkedParentNode(node.parentNode, state)
//             }
//         }

	       



	        
 //------------------workflow-model 
 
       
 
     Ext.define('WorkflowModel', {
	            extend: 'Ext.data.Model',
	            fields: [
	                {name: 'id',  type: 'string'},
	                {name: 'key',   type: 'string'},
	                {name: 'name', type: 'string'},
	                {name: 'version', type: 'string'},
	                {name: 'createTime'},
	                {name: 'lastUpdateTime'},
	               // {name: 'metaInfo.description' , mapping : 'metaInfo.description'},
	               // {name: 'metaInfo.status' , mapping : 'metaInfo.status'}
	                {name: 'metaInfo' }
	            ]

	        });

  
	        var storeWorkflowModel = Ext.create('Ext.data.Store', {
	            autoLoad: true,
	            model: 'WorkflowModel',
	            proxy: {
	                type: 'ajax',
	                url: '<%=basePath%>workflow/model/listData',
	                reader: { type: 'json' }
	            },
	            listeners: {
	                beforeload: function (store, options) {
	                
	                },
	                load:function(store){
	                	console.log(storeWorkflowModel.getAt(0));
		                
		              }
	            }
	        });

	     	var addBtn= {
	            	id: 'addBtn',
	                text: '新增',
	                iconCls: 'x-button-delete',
	                handler: function () {
	                	editModel("insert")
	                }
	            }, editBtn= {
	            	id: 'editBtn',
	                text: '修改',
	                iconCls: 'x-button-application_cascade',
	                handler: function () {
	                	editModel("update")
	                }
	            },delBtn= {
	            	id: 'delBtn',
	                text: '删除',
	                iconCls: 'x-button-delete',
	                handler: deleteThem
	            }, editFlowBtn= {
	            	id: 'editFlowBtn',
	                text: '编辑流程',
	                iconCls: 'x-button-application_cascade',
	                handler: editFlow
	            },deployBtn= {
	            	id: 'deployBtn',
	                text: '发布',
	                iconCls: 'x-button-delete',
	                handler: deploy
	            }, exportBtn= {
	            	id: 'exportBtn',
	                text: '导出',
	                iconCls: 'x-button-application_cascade',
	                handler: exportFlow
	            }, importBtn= {
		            	id: 'importBtn',
		                text: '导入并发布',
		                iconCls: 'x-button-application_cascade',
		                handler: importFlow
		            };

            
	        var tbarRight = Ext.create('Ext.toolbar.Toolbar', {
	            items: [addBtn,'-',editBtn,'-',delBtn,'-',editFlowBtn,'-',deployBtn,'-', exportBtn,'-',importBtn]
	        });
	        var smModel = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
	        var gridWorkflowModel = Ext.create('Ext.grid.Panel', {
	            region: 'center',
	            store: storeWorkflowModel,
	            selModel: smModel,
	            columnLines: false,
	            columns: [
						new Ext.grid.PageRowNumberer(),
						{ header: '名称', dataIndex: 'name', align: 'center', flex:1},
						{ header: '版本', dataIndex: 'version', align: 'center', flex:1},
						{ header: '描述', dataIndex: 'metaInfo', align: 'center', flex:1,renderer:renderMeteInfoDescription},
						{ header: '状态', dataIndex: 'metaInfo', align: 'center', flex:1,renderer:renderMeteInfoStatus}
					],
				tbar:tbarRight,
				bbar: createPage(storeWorkflowModel),
				listeners:{
					selectionchange: function (_this, selected, eOpts ){
					}

				}
	        });
 
 
 ///-----
 
  
	 

        
  //-----整体
  
 var panel=	Ext.create('Ext.panel.Panel', {
		    width: 500,
		    height: 400,
		    layout: 'border',
		    items: [{
		    	title: '服务目录',
		        region:'west',
		        xtype: 'panel',
		        margins: '5 0 0 5',
		        width: 200,
		        collapsible: true,   // make collapsible
		        split: true,
		        id: 'west-region-container',
		        layout: 'fit',
		        items:[]
		    },{
		        title: '服务项',
		        region: 'center',     // center region is required, no width/height specified
		        xtype: 'panel',
		        layout: 'fit',
		        margins: '5 5 0 0',
		        items:gridWorkflowModel
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

		function renderMeteInfoDescription(value){

			value = Ext.decode(value);
			return value.description;

		}
		function renderMeteInfoStatus(value){

			value = Ext.decode(value);
			return value.status?(value.status=='work'?'启用':'未启用'):'';

		}
        
        
        //编辑
        function editModel(action){
        	
        	var param = '?action=' + action ;
        	if(action!='insert'){
        		var  id = checkBox();
        		
        		if(!id){
        			return false;
        		}
        		 param += "&id=" + id;
        	}
        	else{
        		
        	}
        	var title = '服务项定义';
        	
        	var url = '<%=path%>/workflow/model/toEdit'+param;
        	ShowWindow(storeWorkflowModel, title, url, 600, 380);
        	
        }
        
        
        function checkBox() {
            var id;
            var selectedCount = smModel.getCount();
            if (selectedCount == 0) {
                Ext.Msg.show({ title: '提示', msg: '未选中任何一个记录，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else if (selectedCount > 1) {
                Ext.Msg.show({ title: '提示', msg: '只能选择一个记录，不能同时选择多个！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else {
                console.log(smModel.getLastSelected());
                id = smModel.getLastSelected().data.id;
                return id;
            }
        }
        function checkBoxs() {
            var ids = [];
            var selectedCount = smModel.getCount();
            if (selectedCount == 0) {
                Ext.Msg.show({ title: '提示', msg: '未选中任何一个记录，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else {
                Ext.each(smModel.getSelection( ),function(cur){

                	ids.push(cur.data.id);
                	
                  }) ;

                
                return ids;
            }
        }

      //删除
        function deleteThem() {
            var res = checkBoxs();
            if (!res) { return; } 

            Ext.Msg.confirm('删除记录', '确认要删除该记录吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>workflow/model/deleteThem',
                        params: { modelIds: res  },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg ,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: function () {
                                	storeWorkflowModel.load();
                                }
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
            });
        }
      //部署
        function deploy() {
            var res = checkBox();
            if (!res) { return; } 

            Ext.Msg.confirm('部署流程', '确认要部署该流程吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>workflow/model/deploy/'+res,
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg ,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: function () {
                                	storeWorkflowModel.load();
                                }
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
            });
        }
      //编辑流程
        function editFlow() {
            var res = checkBox();
            if (!res) { return; } 

           var url = basepath+"/modeler.html?modelId=" + res;
           window.open(url);
        }

        function exportFlow(){
        	  var res = checkBox();
              if (!res) { return; } 
			location.href=basepath+'/workflow/model/export/'+ res ;
          }
        function importFlow(){
			var title = '导入服务项定义';
        	
        	var url = basepath+'/workflow/model/toImport' ;
        	ShowWindow(storeWorkflowModel, title, url, 600, 380);
          }
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
