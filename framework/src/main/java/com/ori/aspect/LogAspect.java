package com.ori.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ori.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Pointcut("@annotation(com.ori.annotation.SystemLog)")
    public void pt() {}

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            log.info("=======End=======" + System.lineSeparator());
        }

        return ret;
    }

    private void handleAfter(Object ret) {
        log.info("Response       :{}",JSON.toJSONString(ret));
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("=======Start=======");
        //打印请求URL
        log.info("URL            :{}",request.getRequestURL());
        //打印描述信息
        log.info("BusinessName   :{}",systemLog.businessName());
        //打印 Http method
        log.info("HTTP Method    :{}",request.getMethod());
        //打印调用 Controller 的全路径以及执行方法
        log.info("Class Method   :{}.{}",joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        //打印请求的IP
        log.info("IP             :{}",request.getRemoteHost());

        //打印请求入参
        log.info("Request Args   :{}", JSON.toJSONString(filteredArgs(joinPoint)));
//        log.info("Request Args   :{}", JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 在序列化前过滤掉HttpServletRequest对象
     *
     * @param joinPoint
     * @return
     */
    private static Object[] filteredArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] filteredArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                filteredArgs[i] = "HttpServletRequest";  // 替换为字符串占位符
            } else {
                filteredArgs[i] = args[i];
            }
        }
        return filteredArgs;
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;
    }
}
