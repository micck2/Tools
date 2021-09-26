package com.micck.tools.viewclick

import android.util.Log
import android.view.View
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 *
 * @Pointcut("execution(" +//执行语句
"@com.wpf.api.AopOnclick" +//注解筛选
"*" + //类路径,*为任意路径
"*" + //方法名,*为任意方法名
"(..)" +//方法参数,'..'为任意个任意类型参数
")" +
" && " +//并集
)
@Aspect：声明切面，标记类
@Pointcut(切点表达式)：定义切点，标记方法
@Before(切点表达式)：前置通知，切点之前执行
@Around(切点表达式)：环绕通知，切点前后执行
@After(切点表达式)：后置通知，切点之后执行
@AfterReturning(切点表达式)：返回通知，切点方法返回结果之后执行
@AfterThrowing(切点表达式)：异常通知，切点抛出异常时执行
 *
 * 参考 https://www.zhangshengrong.com/p/pDXB0AO3aP/
 *
 * @author lilin
 * @time on 2021/9/26 10:31 上午
 *
 * 目前该gradle版本不生效，因为https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx未维护了，在寻找解决方法中
 */
@Aspect
class ViewClickAspect {

    private var mLastClickTime = 0L

    companion object {
        private const val FILTER_TIME = 1000L
    }

    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    fun methodAnnotated() {

    }

    @Around("methodAnnotated()")
    @Throws(Throwable::class)
    fun onViewClickMethodAround(joinPoint: ProceedingJoinPoint) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastClickTime <= FILTER_TIME) {
            Log.e("ClickAspect", "重复点击，已过滤")
            return
        }
        mLastClickTime = currentTime

        val args: Array<Any>? = joinPoint.args
        val view = args?.find {
            it is View
        }
        joinPoint.proceed()
        Log.e("ClickAspect", "点击view: $view")
    }
}