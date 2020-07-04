## springboot-base
在使用springboot框架之前，一直使用的play框架。play中处处都是REST API。所以借鉴了play的一些想法，封装了一个简单的springboot框架。

## 使用
目前，项目仅在公司使用，并没有打包到maven仓库中，但是您可以自己拉取下来，使用maven clean install即可
```$xslt
<dependency>
    <groupId>com.xinput</groupId>
        <artifactId>springboot-base</artifactId>
        <version>0.0.3</version>
</dependency>
```

## 注意
#### 一、BaseController 和 BaseService 
这两个抽象类的内容是一样的(不要问我为什么是一样的，因为我不会告诉你的)，您可以在任意一个注解了@RestController的类上继承BaseController或者在任意一个注解了@Service的服务类上继承 BaseService
```$xslt
@RestController
public class Doctors extends BaseController {

}

@Service
public class AuthService extends BaseService {

}
```

#### 二、关于拦截器，不要有任何类继承WebMvcConfigurationSupport，否则springboot-base中的很多类不生效，包括BaseController和BaseServcie
如果您想使用 BaseController 和 BaseService 的任何方法，你需要先添加以下类
```$xslt
@Configuration
public class MyAdpter implements WebMvcConfigurer {

    @Autowired
    private BaseHandlerInterceptor baseHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1.加入的顺序就是拦截器执行的顺序，
        //2.按顺序执行所有拦截器的preHandle
        //3.所有的preHandle 执行完再执行全部postHandle 最后是postHandle
        registry.addInterceptor(baseHandlerInterceptor).addPathPatterns("/**");
//        registry.addInterceptor(secondBaseHandlerInterceptor).addPathPatterns("/**");

    }
}
```

#### 三、启动类注解
因为springboot的类都在包名com.precisource下，所以需要添加一下注解进行扫描
```$xslt
@ComponentScan({"你自己写的主类的包的类名", "com.precisource"})
```

#### 四、关于Mongo
我在springboot-base中引入了mongo的包，如果您不想使用，可以在main方法上通过注解进行过滤
```$xslt
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class App {
    public static void main(String[] args) {
        
    }
}
```

