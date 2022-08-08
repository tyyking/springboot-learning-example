package org.spring.springboot.common.log;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.springboot.domain.SysOperLog;
import org.spring.springboot.service.SysOperLogService;
import org.spring.springboot.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 操作日志记录处理
 * @author niejun
 * @date: 2018年9月30日 下午1:40:33
 */
@Aspect
@Component
@EnableAsync
public class LogAspect
{
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private SysOperLogService operLogService;
    @Autowired
    protected HttpServletRequest request;

    // 配置织入点
    @Pointcut("@annotation(org.spring.springboot.common.log.Log)")
    public void logPointCut()
    {
        System.out.println("logPointCut = ");
    }

    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doBefore(JoinPoint joinPoint)
    {
        System.out.println("doBefore = ");
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     * 
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e)
    {
        System.out.println("doAfter = ");
        handleLog(joinPoint, e);
    }

    @Async
    protected void handleLog(final JoinPoint joinPoint, final Exception e)
    {
        try
        {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null)
            {
                return;
            }

            // TODO:获取当前的用户
//            TsysUser currentUser = ShiroUtils.getUser();
//            TsysUser currentUser=new TsysUser();
            // *========数据库日志=========*//
            SysOperLog operLog = new SysOperLog();
           
            //赋值操作
            /*String ip = ShiroUtils.getIp();
            operLog.setOperIp(ip);*/
            // 操作地点
            //operLog.setOperLocation(AddressUtils.getRealAddressByIP(ip));
            // 请求的地址
            operLog.setOperUrl(((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI());
           /* if (currentUser != null)
            {
//            	//操作人
                operLog.setOperName(currentUser.getUsername());
//                if (StringUtils.isNotNull(currentUser.getDept())
//                        && StringUtils.isNotEmpty(currentUser.getDept().getDeptName()))
//                {
//                    operLog.setDeptName(currentUser.getDept().getDeptName());
//                }
            }*/

            if (e != null)
            {
            	//错误日志
            	operLog.setErrorMsg(e.getMessage().substring(0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            operLog.setOperTime(new Date());
            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, operLog);
            // 保存数据库
            //System.out.println("-----------------");
            //System.out.println(new Gson().toJson(operLog));
            //System.out.println("-----------------");
            operLogService.save(operLog);
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * 
     * @param log
     * @param operLog
     * @throws Exception
     */
    public void getControllerMethodDescription(Log log, SysOperLog operLog) throws Exception
    {
        // 设置action动作
       // operLog.setAction(log.action());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置channel
        //operLog.setChannel(log.channel());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(operLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     * 
     * @param operLog
     */
    private void setRequestValue(SysOperLog operLog)
    {
        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
        Gson gson=new Gson();
        String params = gson.toJson(map);
        operLog.setOperParam(params.substring(0, 255));
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
