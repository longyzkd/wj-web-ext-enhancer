package com.kingen.aop;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kingen.bean.Log;
import com.kingen.bean.User;
import com.kingen.service.log.LogService;
import com.kingen.util.Constants;
import com.kingen.util.DateUtils;
import com.kingen.util.SpringContextHolder;
import com.kingen.util.StringUtils;
import com.kingen.util.UserUtils;

/**
 * 日志AOP
 * 
 * @author wj
 * @date 2016-11-16
 *
 */
@Aspect
@Component
public class LogAOP {

	private static LogService logService = SpringContextHolder.getBean(LogService.class);

	// 本地异常日志记录对象
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 在所有标注@LogAnnotation的地方切入
	 * 
	 * @param joinPoint
	 */
	@Pointcut("@annotation(com.kingen.aop.ControllerLogAnnotation)")
	public void controllerlogAspect() {
	}
	@Pointcut("@annotation(com.kingen.aop.ServiceLogAnnotation)")
	public void servicelogAspect() {
	}

	// @Around(value = "aApplogic() && @annotation(annotation) &&args(object,..)
	// ", argNames = "annotation,object")
	// public Object around(ProceedingJoinPoint pj,
	// LogAnnotation annotation, Object object) throws Throwable {
	// System.out.println("moduleName:"+annotation.moduleName());
	// System.out.println("option:"+annotation.option());
	// pj.proceed();
	// return object;
	// }

	/**
	 * 后置通知 用于拦截Controller层记录用户的操作，不管成功失败
	 * 
	 * @param joinPoint
	 *            切点
	 * @throws Exception
	 */
//	@Around(value = "logAspect() && @annotation(annotation) &&args(object,..) ", argNames = "annotation,object")
//	public void doAround(ProceedingJoinPoint joinPoint, LogAnnotation annotation, Object object) {
	//用@Around 会导致controller不执行，不返回页面
	
//	 @After(value = "logAspect() && @annotation(annotation) &&args(object,..) ", argNames = "annotation,object")
//	 public void doAfter(JoinPoint joinPoint, LogAnnotation annotation, Object object) {
	
//	 @AfterReturning(value = "controllerlogAspect() && @annotation(annotation) &&args(object,..) ", argNames = "", returning = "retVal")
//	 public void doAfterReturning(JoinPoint joinPoint, ControllerLogAnnotation annotation, Object object) {
	 @After(value = "controllerlogAspect() && @annotation(annotation) &&args(object,..) ", argNames = "")
	 public void doAfter(JoinPoint joinPoint, ControllerLogAnnotation annotation, Object object) {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		try {
			// String title = getAnnotationValue(joinPoint);
			String title = getAnnotationValue(annotation);
			saveLog(request, title);
		} catch (Exception e) {
			e.printStackTrace();
			// 记录本地异常日志
			logger.error("==异常通知异常==");
			logger.error("异常信息:{}", e.getMessage());
		}
	}

	/**
	 * 
	 * 异常通知 用于拦截service层记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
	 //异常日志无法跟异常信息在前台显示兼容，取消
	// 方法  catch住异常的话，这里执行不到
//	@AfterThrowing(pointcut = "logAspect()", throwing = "e")
	 
//	@AfterThrowing(value = "logAspect() && @annotation(annotation) &&args(..) " , throwing = "e")
	@AfterThrowing(value = "servicelogAspect() && @annotation(annotation) &&args(..) " , throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint,  ServiceLogAnnotation annotation, Exception e) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		try {

			String title = getAnnotationValue(annotation);
			saveLog(request, title, e);
		} catch (Exception ex) {
			// 记录本地异常日志
			logger.error("==异常通知异常==");
			logger.error("异常信息:{}", ex.getMessage());
		}

	}

	public static void saveLog(HttpServletRequest request, String title) {
		saveLog(request, title, null);
	}

	/**
	 * 保存日志
	 */
	@Transactional
	public static void saveLog(HttpServletRequest request, String title, Exception ex) {
		User user = UserUtils.getCurrentUser();
		if (user != null && user.getUserId() != null) {
			Log log = new Log();
			log.setCreateDate(DateUtils.getDateTime());
			log.setTitle(title);
			log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
			log.setRemoteAddr(StringUtils.getRemoteAddr(request));
			log.setUserAgent(user.getUsername());
			// log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			log.setParams(request.getParameterMap());
			// 如果有异常，设置异常信息
			log.setException(ex == null ? null : ex.getMessage());
//			log.setException(ex == null ? null : Exceptions.getStackTraceAsString(ex));
//			log.setStatus(ex == null ? Constants.StatusEnum.Success.getIndex() : Constants.StatusEnum.Fail.getIndex());
			// log.setMethod(request.getMethod());
			// 异步保存日志
			// new SaveLogThread(log, handler, ex).start();
			logService.saveLog(log);
		}
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * 
	 * @param joinPoint
	 *            切点
	 * @return 方法描述
	 * @throws Exception
	 */
//	@Deprecated
//	public static String getAnnotationValue(JoinPoint joinPoint) throws Exception {
//		String targetName = joinPoint.getTarget().getClass().getName();
//		String methodName = joinPoint.getSignature().getName();
//		Object[] arguments = joinPoint.getArgs();
//		Class targetClass = Class.forName(targetName);
//		Method[] methods = targetClass.getMethods();
//		String description = "";
//		for (Method method : methods) {
//			if (method.getName().equals(methodName)) {
//				Class[] clazzs = method.getParameterTypes();
//				if (clazzs.length == arguments.length) {
//					String moduleName = method.getAnnotation(LogAnnotation.class).moduleName();
//					String option = method.getAnnotation(LogAnnotation.class).option();
//					Assert.hasText(moduleName, "模块名字不应为空");
//					Assert.hasText(option, "操作名字不应为空");
//					description = moduleName + "-" + option;
//					break;
//				}
//			}
//		}
//		return description;
//	}

	public static String getAnnotationValue(ControllerLogAnnotation anno) throws Exception {
		String moduleName = anno.moduleName();
		String option = anno.option();
		Assert.hasText(moduleName, "模块名字不应为空");
		Assert.hasText(option, "操作名字不应为空");
		String description = moduleName + " - " + option;
		return description;
	}
	public static String getAnnotationValue(ServiceLogAnnotation anno) throws Exception {
		String action = anno.action();
		Assert.hasText(action, "操作名字不应为空");
		return action;
	}
}