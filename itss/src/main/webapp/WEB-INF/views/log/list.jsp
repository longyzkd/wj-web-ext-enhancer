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

			//--条件参数
			  var from ;	
              var to ;
              var username ;

              
			
	        /* 限制from最大不能超过今天，to最小不能小于from  by  wj   */
	        var fromDateField = Ext.widget('datefield',{ 
	    	        id:"fromDateField",
	    	      //  format :'Y-m-d H:i:s',
	    	       format :'Y-m-d',
	                anchor: '100%',
	                fieldLabel: '开始时间',
	                name: 'fromDate',
	             //   value: new Date() , // defaults to today
	                maxValue: new Date(),  // limited to the current date or prior
	                listeners:{
							change:function(_this, newValue, oldValue, eOpts){
									toDateField.setMinValue( newValue );
								}

		                }
	            });
	        var toDateField =   Ext.widget('datefield',{ 
	                anchor: '100%',
	                fieldLabel: '结束时间',
	                format :'Y-m-d ',
	                name: 'toDate',
	                value: new Date()  // defaults to today
	            });
	        var userNameField =  new Ext.form.field.Text({
	            name: 'userAgent',
	            fieldLabel: '用户名',
	            emptyText: '请输入',
	            labelAlign: 'right',
	            labelWidth: 55,
	            width: 200
	        });
	        
	        var btnSearch = new Ext.button.Button({
	            text: '查询',
	            iconCls: 'x-button-read',
	            handler: function () {
	            	store.load();
	            }
	        });


	      	var delBtn= {
            	id: 'delBtn',
                text: '删除',
                iconCls: 'x-button-delete',
                handler: deleteThem
            }, exportBtn= {
            	id: 'exportBtn',
                text: '导出',
                iconCls: 'x-button-application_cascade',
                handler: exportThem
            };
	        var tbar1 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [delBtn,'-',exportBtn,'->',fromDateField,'-',toDateField,'-',userNameField,'-', btnSearch]
	        });
	        /*
	        var tbar2 = Ext.create('Ext.toolbar.Toolbar', {
	            items: [ {
	            	id: 'delBtn',
	                text: '删除',
	                iconCls: 'x-button-delete',
	                handler: deleteThem
	            }, '-', {
	            	id: 'exportBtn',
	                text: '导出',
	                iconCls: 'x-button-application_cascade',
	                handler: exportThem
	            }]
        	});
        	*/
        
        <%=com.kingen.util.JsonHelperJS.FormExtJsModel("com.kingen.bean.Log", "LogModel", "","")%>
        var store = Ext.create('Ext.data.Store', {//pageSize 默认25
            autoLoad: true,
            model: 'LogModel',
            proxy: {
                type: 'ajax',
                url: '<%=basePath%>log/listData',
                reader: { type: 'json', root: 'dataList' }
            },
            listeners: {
                beforeload: function (store, options) {
                  //  var from = 	fromDateField.getValue();
                     from = 	fromDateField.getRawValue();//不要格式化的数据
                     to = toDateField.getRawValue();
                     username = userNameField.getValue();
                	var params = {'fromDate': from,'toDate':to,'userAgent':username};
               		Ext.apply(store.proxy.extraParams, params);
            		
                }
            }
        });
     


        cboPage.on('select', function (comboBox) {
        	store.pageSize = parseInt(comboBox.getValue());
        	store.loadPage(1);
        });

        var pgCfg = new Ext.PagingToolbar({
           // pageSize: 50,
            store: store,
            displayInfo: true,
            displayMsg: '显示 {0} - {1} 条记录，总共 {2} 条记录',
            emptyMsg: "没有记录",
            items: ['-', '每页显示', cboPage, '条']
        });
        
        var smLog = Ext.create('Ext.selection.CheckboxModel', { mode: 'MULTI' });
        var gridLog = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: store,
            dockedItems: [tbar1],
            //dockedItems: [tbar2,tbar1],
            selModel: smLog,
            columnLines: false,
            viewConfig:{getRowClass:changeRowClass},
            columns: [
					new Ext.grid.PageRowNumberer(),
					{ header: '单位名称', dataIndex: 'dwmc', align: 'center', flex:2},
					{ header: '用户名', dataIndex: 'userAgent', align: 'center', flex:1 },
                    { header: '内容', dataIndex: 'title', align: 'center', flex:2},
                    { header: '类型', dataIndex: 'type', align: 'center', flex:1,renderer:typeRenderer},
                    { header: '时间', dataIndex: 'createDate', align: 'center', flex:2}
				],
				bbar: pgCfg
        });
        
        
        
        //整个页面的容器
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            border: false,
            items: gridLog
        });
        
        
	       
	    
	    store.on('load', function(){
	    	smLog.deselect(smLog.getSelection());
	    });
	    
//函数区        
//----------------------------------------------------------------------------------------------------------
        
	    function changeRowClass(record, rowIndex, rowParams, store){
	        if (record.get("type") == "2") {        
	            return 'x-grid-record-red';
	        }
	    } 

	    
        function typeRenderer(value) {
        	
            if (value=='2')  //异常日志
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/action_stop.gif "  alt="异常日志 " />';
            else  
                return '<img src="<%=path%>/static/jslib/ExtJs/resources/themes/icons/accept.png   "  alt="入口日志" />';
        }
        
        
        function checkBoxs() {
            var ids = [];
            var selectedCount = smLog.getCount();
            if (selectedCount == 0) {
                Ext.Msg.show({ title: '提示', msg: '未选中任何一个记录，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
                return false;
            } else {
                Ext.each(smLog.getSelection( ),function(cur){
                	ids.push(cur.data.id);
                  }) ;
                
                return ids;
            }
        }
        
        
        function exportThem(){//文件不走XMLHttpRequest
            //当前页、每页数量
        	var page =pgCfg.getPageData().currentPage  ,limit=store.pageSize;
        	var params = {'fromDate': from,'toDate':to,'userAgent':username,page:page,limit:limit};
        	/*分页导出*/
        	// window.location.href = '<%=basePath%>log/exportFile?fromDate='+from+' &toDate= '+to+'&userAgent='+username+' &page='+page+' &limit='+limit;  
        	 window.location.href = '<%=basePath%>log/exportFile?fromDate='+from+' &toDate= '+to+'&userAgent='+username;  
             //或者构造一个form提交

        }
       
        
        //删除
        function deleteThem() {
            var res = checkBoxs();
            if (!res) { return; } 

            Ext.Msg.confirm('删除记日志', '确认要删除该记录吗?', function (btn) {
                if (btn == 'yes') {
                    Ext.Msg.wait('正在处理，请稍等......');
                    Ext.Ajax.request({
                        url: '<%=basePath%>log/deleteThem',
                        params: { ids: res  },
                        success: function (response) {
                            Ext.Msg.hide();
                            var responseText = Ext.decode(response.responseText);
                            Ext.Msg.show({
                                title: '提示',
                                msg: responseText.msg ,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: function () {
                                    store.load();
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
