<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<jsp:include page="../_LayoutCommonExtJS.jsp"/>
	<script type="text/javascript">
		Ext.onReady(function(){
			
			Ext.QuickTips.init();
//顺序最好为，先store->插件->grid->viewPort ,从小到大
	        
 
 	// var curNodeId = -1;
	  var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
            items: [{
                text: '新增',
                iconCls: 'x-button-insert',
                handler: function () {
                	edit('insert')
                }
            },{
                text: '修改',
                iconCls: 'x-button-update',
                handler: function () {
                	edit('update')
                }
            },{
                text: '删除',
                iconCls: 'x-button-delete',
                handler: function () {
                	deleteIt();
                }
            },{
                text: '刷新',
                iconCls: 'x-button-refresh',
                handler: function () {
                	storeTree.load();
                }
            }]
        });

	   <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.vo.TreeNode", "TreeModel", "","")%>
        //树Store
        var storeTree = Ext.create('Ext.data.TreeStore', {
                autoLoad: true,
                storeId:'storeTree',
                //这个千万不能要
//                 root: {
// 					expanded: true
// 				},
                model: 'TreeModel',
               // fields: ['id','text'],
                folderSort: false,
                proxy: {
                    type: 'ajax',
                    url: '<%=basePath%>storeRoom/treeData',
                    reader: {
    					type: 'json'
    				}
                },
               	listeners: {
                    beforeload: function (store, options) {
                    }
                }
            });
        
        var cmRights = [{
            xtype: 'treecolumn',
            text: '名称',
            sortable: false,
            dataIndex: 'text',
            flex:1,
            titleAlign: 'center'
        }, {
            text: '编号',
            dataIndex: 'id',
            flex:1,
            titleAlign: 'center'
        }];
        var sm = Ext.create('Ext.selection.RowModel');
        var treePanel = Ext.create('Ext.tree.Panel', {
            title: '库房',
            store: storeTree,
            rootVisible: true,
            frame: false,
            columnLines: true,
            singleExpand: false,
            columns: cmRights,
            selModel: sm,
            height: 390,
            tbar:tbar1,
            root: {
                text: "库房",
               // expanded: true,
                id:'-1'
            },
            listeners: {
                "checkchange": function (node, state) {
                   
                },
                itemclick: function(s,r) {
                	//curNodeId = r.data.id;

                },
                afterrender: function(node) {
                   // alert(1);
//                 	treePanel.setRootNode({
//                     		 text: 'Root11',                        //根节点显示文字  
//                 	    	expanded: true
//                 	});        
                //	treePanel.expandAll();//展开树     
                	
                }     

                    
            }
        });



        
  //-----整体
  

        
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: treePanel
        });
        
        
        storeTree.on('load', function(){
        //	curNodeId = null;
          //  var sm = treePanel.getSelectionModel( ) ;
	    	//sm.deselect(sm.getSelection());

	    	treePanel.getSelectionModel().select(treePanel.getRootNode( ));
	    	//treePanel.expandAll();//展开树     
	    	
	    });
	    
	    
//函数区        
//----------------------------------------------------------------------------------------------------------

        //编辑用户
        function edit(action){
        	
        	var param = '?action=' + action ;
        	if(action!='insert'){//update or view 
				
            	
        		var  id = 		checkBox(sm,'id'); 
        		if(id =='-1'){
        			Ext.Msg.show({ title: '提示', msg: '无法编辑根节点!', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
        			return false;
            	}
        		if(!id){
        			return false;
        		}
        		 param += "&id=" + id;
        	}
        	else{//insert
            	var curNodeId = 	checkBox(sm,'id','请先选择一个节点');
            	if(!curNodeId){
        			return false;
        		}
        		 param += "&pid=" + curNodeId; 
        	}
        	var title = '库房维护';
        	
        	var url = '<%=path%>/storeRoom/toEdit'+param;
        	ShowWindow(storeTree, title, url, 700, 380);
        	
        }       
        
        //删除
        function deleteIt() {
            var res = checkBox(sm,'id');
            if(res =='-1'){
    			Ext.Msg.show({ title: '提示', msg: '无法删除根节点！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
    			return ;
        	}
        	
            if (!res) { return; } 

            Ext.Msg.confirm('删除记录', '确认要删除该记录吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>storeRoom/deleteCascade',
                        params: { id: res  },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg ,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: function () {
                                	storeTree.load();
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
        
        
        
        
        
        
//-----------------------------------------------------------------------------------------------------------
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
