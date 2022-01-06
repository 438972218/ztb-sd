package com.xdcplus.biz.common.aop;

import com.xdcplus.biz.common.pojo.dto.OperationLogDTO;
import com.xdcplus.biz.service.OperationLogService;
import com.xdcplus.ztb.common.mp.utils.AuthUtils;
import com.xdcplus.ztb.common.tool.anotation.SystemControllerLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 系统日志切点类
 * @author linrx
 *
 */
@Aspect
@Component
public class SystemLogAspect {
	private  static  final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);
	
	private static final ThreadLocal<Date> beginTimeThreadLocal =
			new NamedThreadLocal<Date>("ThreadLocal beginTime");
	private static final ThreadLocal<OperationLogDTO> logThreadLocal =
			new NamedThreadLocal<OperationLogDTO>("ThreadLocal log");
	
	private static final ThreadLocal<String> currentUser=new NamedThreadLocal<>("ThreadLocal user");
	
	@Autowired(required=false)
	private HttpServletRequest request;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private OperationLogService logService;

	/**
	 * Service层切点 
	 */
/*	@Pointcut("@annotation(com.myron.ims.annotation.SystemServiceLog)")
	public void serviceAspect(){}*/
	
	/**
	 * Controller层切点 注解拦截
	 */
	@Pointcut("@annotation(com.xdcplus.ztb.common.tool.anotation.SystemControllerLog)")
	public void controllerAspect(){}
	
	/**
	 * 前置通知 用于拦截Controller层记录用户的操作的开始时间
	 * @param joinPoint 切点
	 * @throws InterruptedException 
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) throws InterruptedException{
		Date beginTime=new Date();
		beginTimeThreadLocal.set(beginTime);
		//debug模式下 显式打印开始时间用于调试
		if (logger.isDebugEnabled()){
	        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
	        	.format(beginTime), request.getRequestURI());
		}

		//读取session中的用户
		String user = AuthUtils.getCurrentUser();
		currentUser.set(user);

	}
	
	/**
	 * 后置通知 用于拦截Controller层记录用户的操作
	 * @param joinPoint 切点
	 */
	@SuppressWarnings("unchecked")
	@After("controllerAspect()")
	public void doAfter(JoinPoint joinPoint) throws IOException {
    	String title="";
    	String type="info";						  //日志类型(info:入库,error:错误)
    	String remoteAddr=request.getRemoteAddr();//请求的IP
    	String requestUri=request.getRequestURI();//请求的Uri
    	String method=request.getMethod();		  //请求的方法类型(post/get)
    	Map<String,String[]> params=request.getParameterMap(); //请求提交的参数
    	try {
    		title=getControllerMethodDescription2(joinPoint);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}


		Object[] args = joinPoint.getArgs(); // 参数值
		// 得到类对象
		Class userCla = (Class) args.getClass();
		/* 得到类中的所有属性集合 */
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			try {

				if (f.getName().endsWith("requestId")) {
					System.out.println("单个对象的某个键的值==反射==" + f.get(args));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames(); // 参数名
		String a =Arrays.toString(joinPoint.getArgs());

//		String requestId = request.getParameter(ZtbConstant.REQUESTID);

		OperationLogDTO log=new OperationLogDTO();
    	log.setTitle(title);
    	log.setType(type);
    	log.setRemoteAddr(remoteAddr);
    	log.setRequestUri(requestUri);
    	log.setMethod(method);
    	log.setUserName(currentUser.get());

    	 //1.直接执行保存操作
//        this.logService.saveOperationLog(log);

        //2.优化:异步保存日志
        //new SaveLogThread(log, logService).start();

        //3.再优化:通过线程池来执行日志保存
        threadPoolTaskExecutor.execute(new SaveLogThread(log, logService));
        logThreadLocal.set(log);
       
 
	}
	
	/**
	 *  异常通知 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")  
	public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		OperationLogDTO log = logThreadLocal.get();
		if(log != null){
			log.setType("error");
			log.setException(e.toString());
			new UpdateLogThread(log, logService).start();			
		}
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * 
	 * @param joinPoint 切点
	 * @return 方法描述
	 */
	public static String getControllerMethodDescription2(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		SystemControllerLog controllerLog = method
				.getAnnotation(SystemControllerLog.class);
		String discription = controllerLog.description();
		return discription;
	}

	/**
	 * 保存日志线程
	 * 
	 * @author lin.r.x
	 *
	 */
	private static class SaveLogThread implements Runnable {
		private OperationLogDTO log;
		private OperationLogService logService;

		public SaveLogThread(OperationLogDTO log, OperationLogService logService) {
			this.log = log;
			this.logService = logService;
		}

		@Override
		public void run() {
			logService.saveOperationLog(log);
		}
	}

	/**
	 * 日志更新线程
	 * 
	 * @author lin.r.x
	 *
	 */
	private static class UpdateLogThread extends Thread {
		private OperationLogDTO log;
		private OperationLogService logService;

		public UpdateLogThread(OperationLogDTO log, OperationLogService logService) {
			super(UpdateLogThread.class.getSimpleName());
			this.log = log;
			this.logService = logService;
		}

		@Override
		public void run() {
			this.logService.updateOperationLog(log);
		}
	}
}
