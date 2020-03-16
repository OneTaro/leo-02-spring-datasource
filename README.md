## spring全家桶系列
### 02 leo-spring-datasource
本例子使用h2 springboot内置数据源进行演示

引入jar：
* actuator
* web
* jdbc
* h2 -- 内存数据库
* lombok

查看有哪些bean：`http://localhost:8081/actuator/beans` -- 这里可能有些问题，可以展示，但是展示的太生硬

**本节知识点及问题:**  
知识点1：在springboot启动以后执行指定程序的方法，主要有三种
* 第一种方式，使用 @PostConstruct 注解  
直接在方式上面注入，但是会影响服务提供，比如这个方法要执行五分钟 这五分钟之内是无法提供服务的，这个方法是在服务初始化后之前运行， 所以 此方法运行不结束，服务就无法初始化， 在这过程路也无法提供服务
```java
    @PostConstruct
     public void pingStart(){
        System.out.println(" ping start:");
        getPingip();
        System.out.println(" ping end: ");
    }
```
* 第二方式，是通过监听接口方式启动，服务已经初始化过，不影响 服务启动，并且启动之后可以正常提供服务
```java
@Component
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent>{
 
	@Autowired
    private QuartzManager quartzManager;
 
    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
        	quartzManager.start();
            System.out.println("任务已经启动...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```
* 第三种也是监听接口方式，启动服务，执行方式时仍然提供服务，服务初始化之后，执行方法
```java
@Component
public class StartPingService implements CommandLineRunner{
 
	@Autowired
	Ping ping;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		ping.pingStart();
	}
 
}
```

知识点2：h2内存数据库
* 体积小
* 启动快
* java开发以jar的形式存在

在程序中使用h2作为内存数据库的时候，请加上DB_CLOSE_DELAY=-1，因为内存数据库在jvm启动期间存在，jvm关闭以后消失  
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
```

* h2作为本地文件连接语法（[] 可选，<>可变）：`jdbc:h2:[file:][<path>]<databaseName>`
```properties
jdbc:h2:~/test       //连接位于用户目录下的test数据库
 
jdbc:h2:file:/data/sample
 
jdbc:h2:file:E:/H2/gacl  //只在Windows下使用
```
在Window操作系统下，"~"这个符号代表的就是当前登录到操作系统的用户对应的用户目录，比如我当前是使用Administrator用户登录操作系统的，所以在"C:\Documents and Settings\Administrator.h2"目录中就可以找到test数据库对应的数据库文件了。

* h2作为内存数据库连接语法：`jdbc:h2:mem:<databasename>`
```properties
jdbc:h2:mem:test_mem
```

* h2远程连接 这种连接方式就和其他数据库类似了，是基于Service的形式进行连接的，因此允许多个客户端同时连接到H2数据库。连接语法：`jdbc:h2:tcp://<server>[:<port>]/[<path>]<databaseName>`
```properties
jdbc:h2:tcp://localhost/~/test  //用户目录下
 
jdbc:h2:tcp://localhost/E:/H2/gacl  //指定目录
 
jdbc:h2:tcp://localhost/mem:gacl  //内存数据库
```
