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

			
//------left
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.User", "Model", "","")%>
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'Model',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>user/listData',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
            		
                },
 				onload: function (store, options) {
 					//sm.select(0);//在这用无效
                }
            }
        });
     

        
        var sm = Ext.create('Ext.selection.CheckboxModel',{
            mode: 'SINGEL' ,
			listeners:{
				selectionchange: function (_this, selected, eOpts ){

					storeScore.load();
				}

			}
	    }
	 );
        var grid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: store,
            selModel: sm,
            columnLines: false,
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '用户姓名', dataIndex: 'username', align: 'center', flex:1},
					{ header: '所在部门', dataIndex: 'deptName', align: 'center', flex:1 },
				],
				bbar: createPage(store)
        });
        
        store.on('load', function(){
        	sm.select(0);
	    });
        
  //----------------------right
  
  
    	var addBtn2= {
	                text: '保存',
	                iconCls: 'x-button-insert',
	                handler: saveBatch 
	            }
	        var tbar2 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [addBtn2]
	        });
	       
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.vo.SkillAndScoreVo", "scoreModel", "","")%>
        var storeScore = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: false,  //让主store来推动他加载
            model: 'scoreModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>skill/score/data',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
                	var params = {'userId': sm.getLastSelected( ).get('userId')};
                	
            		Ext.apply(store.proxy.extraParams, params);
                },
 				onload: function (store, options) {
 					//smUser.deselect(smUser.getSelection());
                }
            }
        });
     

        
     //   var smUser = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var gridScore = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: storeScore,
            dockedItems: [tbar2],
            //dockedItems: [tbar2,tbar1],
        //    selModel: smUser,
            columnLines: false,
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '技能分类', dataIndex: 'name', align: 'center', flex:2},
					{ header: '分数', dataIndex: 'score', align: 'center', flex:1 ,
						editor: {
			                xtype: 'numberfield',
			                allowBlank: false,
			                allowDecimals:false,
			                // Remove spinner buttons, and arrow key and mouse wheel listeners
			                hideTrigger: true,
			                keyNavEnabled: false,
			                mouseWheelEnabled: false,
			                minValue :0,
			                maxValue:100
			            }
		            }
				],
			bbar: createPage(storeScore),
			plugins: [
		        Ext.create('Ext.grid.plugin.CellEditing', {
		            clicksToEdit: 1
		        })
		    ],
        });
  
  



        var panel=	Ext.create('Ext.panel.Panel', {
		    width: 500,
		    height: 400,
		    layout: 'border',
		    items: [{
		        region:'west',
		        title: '用户列表',
		        xtype: 'panel',
		        margins: '5 0 0 5',
		        flex: 1,
		        collapsible: true,   // make collapsible
		        split: true,
		        id: 'west-region-container',
		        layout: 'fit',
		        items: grid
		    },{
		        title: '技能列表',
		        region: 'center',     // center region is required, no width/height specified
		        xtype: 'panel',
		        layout: 'fit',
		        margins: '5 5 0 0',
		        flex: 1,
		        items: gridScore
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
        function saveBatch(){


        	  var m = storeScore.getUpdatedRecords().slice(0);  
              var jsonArray = [];  
              Ext.each(m,function(item){  
                //  console.log(item);
                //  console.log(item.data.id);
                  jsonArray.push({id:item.data.id,skillId:item.data.skillId,score:item.data.score});  
              });  
              //id有值则json里有id属性，否则没有，就连model里面都没有。奇怪！
              var params = {userId:sm.getLastSelected( ).get('userId'),scores :Ext.encode(jsonArray)}  ;

              Ext.Ajax.request({
            	  url:'<%=basePath%>skill/score/saveBatch',  
                  method: 'post', //filter handle post request
                  params: params,
                  success: function (response) {
                      var result = Ext.decode(response.responseText);
                      Ext.Msg.show({
                          title: '提示',
                          msg: result.msg ,
                          buttons: Ext.Msg.OK,
                          icon: Ext.Msg.INFO,
                          fn: function () {
                        	  storeScore.load();
                          }
                      });
                      
                  },
                  failure: function (response) {
                	  var result = Ext.decode(response.responseText);
                      Ext.Msg.show({
                          title: '错误',
                          msg: result.msg,
                          buttons: Ext.Msg.CANCEL,
                          icon: Ext.Msg.ERROR
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
