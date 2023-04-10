package springboot.demo.mysql.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@SuppressWarnings(value = "unused")
@Aspect
@Component
public class TargetDataSourceAspect {
    @Around("@annotation(targetDataSource)")
    public Object advice(ProceedingJoinPoint point, TargetDataSource targetDataSource) throws Throwable {
        try {
            MySQLHolder.setDataSource(targetDataSource.value());
            return point.proceed();
        } finally {
            // 将数据源恢复为默认值
            MySQLHolder.clearDataSource();
        }
    }
}
