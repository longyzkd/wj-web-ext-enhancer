<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE>
<html>
<head>
	<title>用户登录</title>
	<style type="text/css">
	
	    #CheckCode{ float:left;}  
   	   .x-form-code{width:75px;height:25px;vertical-align:middle;cursor:pointer; float:left; margin-left:7px;}  
	
	</style>
	<jsp:include page="../_LayoutCommonExtJS.jsp"/>
	
	<script type="text/javascript">
	//判断当前窗口是否有顶级窗口，如果有就让当前的窗口的地址栏发生变化， //这样就可以让登陆窗口显示在整个窗口了 
	function loadTopWindow(){
		 if (window.top!=null && window.top.document.URL!=document.URL){ 
			 	window.top.location= document.URL; 
		}
	} 
	</script>
	
	<script type="text/javascript">
	
	//定义验证码控件  
	Ext.define('fms.ux.VerifyCode', {

	extend : 'Ext.form.field.Trigger',
	alias : ['widget.verifycodefield', 'widget.verifycode'],
	// 图片的URL地址
	codeImgUrl : Ext.BLANK_IMAGE_URL,
	// 图片和输入框之间的距离
	imgMargin : 5,
	// 图片的宽度
	imgWidth : 75,
	// 图片的高度
	imgHeight : 23,
	// 点击图片的时候是否清空输入框
	clearOnClick : true,
	// 临时的FieldBody样式
	extraFieldBodyCls : Ext.baseCSSPrefix + 'form-file-wrap',
	componentLayout : 'triggerfield',
	childEls : ['imageWrap'],

	onRender : function() {
		var me = this, id = me.id, inputEl;

		me.callParent(arguments);

		inputEl = me.inputEl;

		// name goes on the fileInput, not the text input
		inputEl.dom.name = '';

		// 将imgConfig对象拷贝给前一个参数，并覆盖
		me.image = new Ext.Img(Ext.apply({
			renderTo : id + '-imageWrap',
			ownerCt : me,
			ownerLayout : me.componentLayout,
			id : id + '-img',
			ui : me.ui,
			src : me.codeImgUrl,
			disabled : me.disabled,
			width : me.imgWidth,
			height : me.imgHeight,
			style : me.getImgMarginProp() + me.imgMargin + 'px;cursor:pointer;',
			inputName : me.getName(),
			listeners : {
				scope : me,
				click : {
					element : 'el',
					fn : me.onImgClick
				}
			}
		}, me.imgConfig));

		// me.browseButtonWrap.dom.style.width =
		// (me.browseButtonWrap.dom.lastChild.offsetWidth +
		// me.button.getEl().getMargin('lr')) + 'px';

		me.imageWrap.dom.style.width = (me.imgWidth + me.image.getEl()
				.getMargin('lr'))
				+ 'px';
		if (Ext.isIE) {
			me.image.getEl().repaint();
		}
	},

	/**
	 * Gets the markup to be inserted into the subTplMarkup.
	 */
	getTriggerMarkup : function() {
		return '<td id="' + this.id + '-imageWrap"></td>';
	},

	onImgClick : function() {
		// 重新定义图片地址
		this.image.setSrc(this.codeImgUrl + '?time=' + new Date().getTime());
		this.reset();
	},
	getImgMarginProp : function() {
		return 'margin-left:';
	},

	setValue : Ext.emptyFn,

	reset : function() {
		var me = this, clear = me.clearOnClick;
		if (me.rendered) {
			if (clear) {
				me.inputEl.dom.value = '';
			}
		}
	}

}); 

	
	
    Ext.require([
        'Ext.tab.*',
        'Ext.window.*',
        'Ext.tip.*',
        'Ext.layout.container.Border'
    ]);   

    Ext.onReady(function () {
    	loadTopWindow();

    	var checkcode = 
/*
        	Ext.create('CheckCode',{  
            cls : 'key',  
            fieldLabel : '验证码',  
            name : 'jcaptchaCode',  
            id : 'CheckCode',  
            allowBlank : false,  
            isLoader:true,  
            blankText : '验证码不能为空',  
            codeUrl: basepath+'/jcaptcha.jpg',  
            width: 150,
            labelWidth: 50,
            x: 515,
            y: 340
        });  
            */
    	Ext.create('fms.ux.VerifyCode', {
    		fieldLabel : '验证码',
    		name : 'jcaptchaCode',
    		id : 'verifycode',
    		blankText : '验证码不能为空',
    		codeImgUrl : basepath+'/jcaptcha.jpg',
    		tabIndex : 3,
    		x :515,
    		y : 340,
    		  width: 250,
              labelWidth: 50
    	});



    	
    	/*
    	 Ext.Msg.show({
             title: '失败',
             msg: '${msg}',
             buttons: Ext.Msg.CANCEL,
             icon: Ext.Msg.WARN
         });
*/
        
        var txtUserId = Ext.create('Ext.form.field.Text', {
            id: 'txtUserId',
            fieldLabel: '帐号',
            width: 220,
            labelWidth: 50,
            maxLength: 20,
            allowBlank: false,
            x: 515,
            y: 260
        });

        var txtPassword = Ext.create('Ext.form.field.Text', {
            fieldLabel: '密码',
            width: 220,
            labelWidth: 50,
            allowBlank: false,
            maxLength: 20,
            inputType: 'password',
            x: 515,
            y: 300
        });
        var txtValidateCode = Ext.create('Ext.form.field.Text', {
            fieldLabel: '验证码',
            width: 220,
            labelWidth: 50,
            allowBlank: false,
            maxLength: 20,
            x: 515,
            y: 340
        });


        
        
            
        var usertype="1";
        if(getQueryString("type")!=null)
        	usertype = getQueryString("type");
        
        

        var myMask = new Ext.LoadMask(Ext.getBody(), { msg: "登录验证中，请稍后..." });

        var btnLogin = Ext.create('Ext.button.Button', {
            text: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;登&nbsp;&nbsp;录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
            x: 540,
            y: 390,
            listeners: {
                click: login
            }
        });

        //页面回车响应事件
        new Ext.util.KeyMap(document, {
            key: 13, // or Ext.EventObject.ENTER
            fn: function () {
                login();
            },
            scope: this
        });

        var btnReset = Ext.create('Ext.button.Button', {
            text: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;重&nbsp;&nbsp;置&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
            x: 630,
            y: 390,
            listeners: {
                click: {
                    fn: function () {
                        window.location = window.location;
                    }
                }
            }
        });

        var tbarUser = Ext.create('Ext.toolbar.Toolbar', {
            dock: 'bottom',
            items: [btnLogin, btnReset]
        });

        win = Ext.create('widget.window', {
            title: '系统登录',
            header: {
                titlePosition: 2,
                titleAlign: 'left'
            },
            layout: 'absolute',
            closable: false,
            html: '<img src="<%=path%>/static/style/image/Login.jpg">',
            closeAction: 'hide',
            width: 1011,
            minWidth: 800,
            height: 620,
            tools: [{ type: 'pin' }],
            items: [txtUserId, txtPassword,
            	checkcode,

              btnLogin,btnReset]
        });
        win.show();

        Ext.getCmp('txtUserId').focus(false, 300);

        function login() {
            if (!(txtUserId.validate() && txtPassword.validate()))
                return false;

            myMask.show();
            Ext.Ajax.request({
                url: '<%=basePath%>login',
                method: 'post', //filter handle post request
                params: {
                	username: txtUserId.getValue(),
                	password: txtPassword.getValue(),
                	jcaptchaCode: checkcode.getValue()
                },
                dataType:'html',
                loadMask: true,
                success: function (response) {
                	
                    myMask.hide();
                    var result = Ext.decode(response.responseText);
                    //console.log(result);
                    //alert(result.success);
                   if(result.success){
                   
                    self.location.href="${ctx}/main/home";
                   }else{
                   
                   	 Ext.Msg.show({
                        title: '失败',
                        msg: result.msg,
                        buttons: Ext.Msg.CANCEL,
                        icon: Ext.Msg.WARN
                    });
                   }
                    
                  
                   
                    
                },
                failure: function (response) {
                    myMask.hide();
                    Ext.Msg.show({
                        title: '错误',
                        msg: response.responseText,
                        buttons: Ext.Msg.CANCEL,
                        icon: Ext.Msg.ERROR
                    });
                }
            });
        }        
    });
</script>
</head>
<body style="background: #d7f1fe url('<%=path%>/static/style/image/LoginBg.jpg') repeat-x">
       <span style="color:red; height: 30px;">${msg}</span>
</body>
</html>