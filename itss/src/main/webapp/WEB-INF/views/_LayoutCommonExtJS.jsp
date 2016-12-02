<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<link href="<%=path%>/static/style/style.css" rel="stylesheet" type="text/css" />
<%-- <link href="<%=path%>/static/jslib/ExtJs/resources/css/ext-all.css" rel="stylesheet" type="text/css" /> --%>
<%-- <link href="<%=path%>/static/jslib/ExtJs/resources/css/ext-all-rtl.css" rel="stylesheet" type="text/css" /> --%>
<%-- <link href="<%=path%>/static/jslib/ExtJs/resources/css/ext-patch.css" rel="stylesheet" type="text/css" /> --%>
<link href="<%=path%>/static/jslib/ExtJs/resources/css/ext-all-neptune.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/jslib/ExtJs/resources/css/ext-all-neptune-rtl.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/jslib/ExtJs/resources/css/ext-patch.css" rel="stylesheet" type="text/css" />

<script src="<%=path%>/static/jslib/ExtJs/bootstrap.js" type="text/javascript"></script>
<script src="<%=path%>/static/jslib/ExtJs/locale/ext-lang-zh_CN.js" type="text/javascript"></script>    
<script src="<%=path%>/static/jslib/script/rootPath.js?v=1.1" type="text/javascript"></script>
<script src="<%=path%>/static/jslib/script/function.js" type="text/javascript"></script>
<script src="<%=path%>/static/jslib/script/extend.js" type="text/javascript"></script>



<script type="text/javascript">

		var basepath = '${ctx}';
		
    	curUser = '<shiro:principal property="userId"/>';
		
        Ext.grid.PageRowNumberer = Ext.extend(Ext.grid.RowNumberer, {
            width: 40,
            renderer: function (value, cellmeta, record, rowIndex, columnIndex, store) {
                if (store.lastOptions.params != null) {
                    var pageindex = store.lastOptions.params.start;
                    return pageindex + rowIndex + 1;
                } else {
                    return rowIndex + 1;
                }
            }
        });
        
        //标题名称
        function titleName(action) {
            var title = '[查看]';
            switch (action) {
                case 'insert': title = '[新增]'; break;
                case 'update': title = '[修改]'; break;
            }
            return title;
        }
        


        function closeRefresh() {
        	CloseWindow();
        }

        //各自页面分页都用cboPage，默认1页25条
        var cboPage = Ext.create('Ext.form.ComboBox', {
            store: new Ext.data.ArrayStore({
                fields: ['text', 'value'],
                data: [['10', 10], ['15', 15], ['25', 25], ['30', 30], ['50', 50]]
            }),
            valueField: 'value',
            displayField: 'text',
            width: 50,
            value:'25'
        });

		function createPage(store){

			 cboPage.on('select', function (comboBox) {
				 store.pageSize = parseInt(comboBox.getValue());
				 store.loadPage(1);
		        });
				//要先定义store，在其下面才行
		        var pgCfg = new Ext.PagingToolbar({
		            store: store,
		            displayInfo: true,
		            displayMsg: '显示 {0} - {1} 条记录，总共 {2} 条记录', //start, end and total 
		            emptyMsg: "没有记录",
		            items: ['-', '每页显示', cboPage, '条']
		        });
			return pgCfg;
		}

		  function checkBox(sm,idName) {
	            var id;
	            var selectedCount = sm.getCount();
	            if (selectedCount == 0) {
	                Ext.Msg.show({ title: '提示', msg: '未选中任何一个记录，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
	                return false;
	            } else if (selectedCount > 1) {
	                Ext.Msg.show({ title: '提示', msg: '只能选择一个记录，不能同时选择多个！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
	                return false;
	            } else {
	                id = sm.getLastSelected().data[idName];
	                return id;
	            }
	        }
	        function checkBoxs(sm,idName) {
	            var ids = [];
	            var selectedCount = sm.getCount();
	            if (selectedCount == 0) {
	                Ext.Msg.show({ title: '提示', msg: '未选中任何一个用户，请先选择！', buttons: Ext.Msg.OK, icon: Ext.Msg.WARNING });
	                return false;
	            } else {
	                Ext.each(sm.getSelection( ),function(cur){

	                	ids.push(cur.data[idName]);
	                	
	                  }) ;

	                
	                return ids;
	            }
	        }

	        //转为友好格式的yyyy-MM-dd类型
	        function simpleDate(num) { //Sat Mar 06 00:00:00 CST 2010
	            num = num + "";
	            var date = "";
	            var month = new Array();
	            month["Jan"] = 1; month["Feb"] = 2; month["Mar"] = 3; month["Apr"] = 4; month["May"] = 5; month["Jan"] = 6;
	            month["Jul"] = 7; month["Aug"] = 8; month["Sep"] = 9; month["Oct"] = 10; month["Nov"] = 11; month["Dec"] = 12;
	            var week = new Array();
	            week["Mon"] = "一"; week["Tue"] = "二"; week["Wed"] = "三"; week["Thu"] = "四"; week["Fri"] = "五"; week["Sat"] = "六"; week["Sun"] = "日";
	            str = num.split(" ");
	            date = str[5] + "-";
	            date = date + month[str[1]] + "-" + str[2];
	            return date;
	        }

	        function businessTypeRenderer(value){
		        
				switch(value){
					case 'vacation': return '请假申请'; 
					default: return '';
			
				}
        
			}
    </script>
    <script src="<%=path%>/static/jslib/ExtJs/vTypes.js" type="text/javascript"></script>