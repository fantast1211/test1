package com.yc.springframework.context;

import com.yc.springframework.steretype.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class MyAnnotationConfigApplicationContext implements MyApplicationContext{
    private Map<String,Object> beanMap = new HashMap<String,Object>();
    //private Map<String,Class> classMap =new HashMap<String,Class>();

    //管理bean的容器可能是多个
    public MyAnnotationConfigApplicationContext(Class<?>... componentClasses) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException, ClassNotFoundException {
        register(componentClasses);
    }

    private void register(Class<?>[] componentClasses) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, ClassNotFoundException {
        //实现这个下午作业
        //判断这些AppConfig是否为空
        if(componentClasses==null||componentClasses.length<=0){
            throw new RuntimeException("没有指定配置类");
        }
        //不为空迭代每一个AppConfig
        for(Class cl:componentClasses){
            //判断这个这个类是否有Configuration注解，是否为托管类
            //如果不是，跳出循环，迭代下一个class
            if(!cl.isAnnotationPresent(MyConfiguration.class)){
                continue;
            }
            //getAppConfigBasePackages得到配置类的当前路径
            String[] basePackages=getAppConfigBasePackages(cl);
            System.out.println(basePackages);
            if(cl.isAnnotationPresent(MyComponentScan.class)){
                MyComponentScan mcs=(MyComponentScan) cl.getAnnotation(MyComponentScan.class);
                if(mcs.basePackages()!=null&&mcs.basePackages().length>0){
                    basePackages = mcs.basePackages();
                }
            }
            //处理@MyBean的情况
            Object obj =cl.newInstance();
            handleAtMyBean(cl,obj);
            //处理 basePackages  基础包下的所有的托管bean
            //可能托管了多个路径下面的bean
            //迭代所有路径
            for(String basePackage : basePackages){
                scanPackageAndSubPackageClasses(basePackage);
            }
            //继续其他托管bean
            handleManageBean();
            //版本2:   循环  beanMap中的每个bean , 找到它们每个类中的每个由@Autowired @Resource注解的方法以实现di,
            handleDi(beanMap);
        }
    }

    private void handleDi(Map<String, Object> beanMap) throws InvocationTargetException, IllegalAccessException {
        Collection<Object> objectCollection = beanMap.values();
        for (Object obj : objectCollection) {
            Class cls = obj.getClass();
            Method[] ms = cls.getDeclaredMethods();
            for (Method m : ms) {
                if (m.isAnnotationPresent(MyAutowired.class) && m.getName().startsWith("set")) {
                    invokeAutowiredMethod(m, obj);
                } else if (m.isAnnotationPresent(MyResource.class) && m.getName().startsWith("set")) {
                    invokeResourceMethod(m, obj);
                }
            }
            Field[] fs = cls.getDeclaredFields();
            for (Field field : fs) {
                if (field.isAnnotationPresent(MyAutowired.class)) {

                } else if (field.isAnnotationPresent(MyResource.class)) {

                }
            }
        }
    }

    private void invokeResourceMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1. 取出  MyResource中的name属性值 ,当成   beanId
        MyResource mr = m.getAnnotation(MyResource.class);
        String beanId = mr.name();
        //2. 如果没有，则取出  m方法中参数的类型名, 改成首字小写   当成beanId
        if (beanId == null || beanId.equalsIgnoreCase("")) {
            String pname = m.getParameterTypes()[0].getSimpleName();
            beanId = pname.substring(0, 1).toLowerCase() + pname.substring(1);
        }
        //3. 从beanMap取出
        Object o = beanMap.get(beanId);
        //4. invoke
        m.invoke(obj, o);
    }

    private void invokeAutowiredMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1. 取出  m的参数的类型
        Class typeClass = m.getParameterTypes()[0];
        //2. 从beanMap中循环所有的object,
        Set<String> keys = beanMap.keySet();
        for (String key : keys) {
            // 4.  如果是，则从beanMap取出.
            Object o = beanMap.get(key);
            //3. 判断 这些object 是否为   参数类型的实例  instanceof
           Class[] interfaces=o.getClass().getInterfaces();
           for (Class c :interfaces){
               if (c==typeClass){
                   m.invoke(obj,o);
                   break;
               }
           }
        }
    }
     /*
    循环  beanMap中的每个bean , 找到它们每个类中的每个由@Autowired @Resource注解的方法以实现di,
     */

    /**
     * 处理managedBeanClasses所有的Class类 筛选出所有的@Component @Service,@Repository的类
     * 并实例化存到beanMap中
     */
    private void handleManageBean() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        for(Class c:managedBeanClasses){
            if(c.isAnnotationPresent(MyComponent.class)){
                saveManageBean(c);
            }else if(c.isAnnotationPresent(MyService.class)){
                saveManageBean(c);
            }else if(c.isAnnotationPresent(MyRepository.class)){
                saveManageBean(c);
            }else if(c.isAnnotationPresent(MyController.class)){
                saveManageBean(c);
            }else {
                continue;
            }
        }
    }

    private void saveManageBean(Class c) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object o =c.newInstance();
        handlePostConstruct(o, c);
        //getSimpleName()得到类的简写名称
        System.out.println(c.getSimpleName());
        String beanId=c.getSimpleName().substring(0,1).toLowerCase()+c.getSimpleName().substring(1);
        beanMap.put(beanId,o);
    }

    private void scanPackageAndSubPackageClasses(String basePackage) throws IOException, ClassNotFoundException {
        String basePackPath = basePackage.replaceAll("\\.","/");
        System.out.println("扫描包路径"+basePackage+"替换后:"+basePackPath);
        //Thread.currentThread(),相当于调用当前线程
        //getContextClassLoader().getResources(basePackPath)使用相对于当前项目的classpath的相对路径来查找资源
        Enumeration<URL> files=Thread.currentThread().getContextClassLoader().getResources(basePackPath);
        //hasMoreElements测试此枚举是否包含更多的元素,是boolean类型
        while(files.hasMoreElements()){
            URL url = files.nextElement();
            System.out.println("配置的扫描路径为"+url.getFile());
            findClassesInPackages(url.getFile(),basePackage);
        }
    }

    private Set<Class> managedBeanClasses = new HashSet<Class>();
    /**
     * 查找 file 下面以及子类所有的要托管的class，存到一个set(managedBeanClasses)中
     * @param file
     * @param basePackage
     */
    private void findClassesInPackages(String file, String basePackage) throws ClassNotFoundException {
        File f= new File( file);
        //FileFilter文件过滤器
        //实现accept方法
        //判断这个文件下的是class文件还是目录
        File[] classFiles =f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class")||pathname.isDirectory();
            }
        });
        for (File cf:classFiles){
            //如果目录，则递归
            //拼接子目录
            if(cf.isDirectory()){
                basePackage+="."+cf.getName().substring(cf.getName().lastIndexOf("/")+1);
                findClassesInPackages(cf.getAbsolutePath(),basePackage);
            }else{
                //加载 cf 作为 class文件

                URL[] urls = new URL[]{};
                URLClassLoader ucl = new URLClassLoader(urls);
                //com.yc.bean.hello.class-->//com.yc.bean.hello
                Class c = ucl.loadClass(basePackage+"."+cf.getName().replace(".class",""));
                managedBeanClasses.add(c);
            }
        }

    }
    /*
    扫描子包
     */
    private  void  handleAtMyBean(Class cls,Object obj) throws InvocationTargetException, IllegalAccessException {
        //获取 cls中所有的method
        Method[] ms =cls.getDeclaredMethods();
        //循环 判断每个method 是否有@myBean注解
        for(Method s :ms){
            if(s.isAnnotationPresent(MyBean.class)){
                //有 则invoke它  ，它有返回值  ，将返回值存到beanmap ，键是方法名 ，值是返回值对象
                //obj是对象，s是obj里面的方法，判断这个方法是否有bean注解，如果有就激活obj这个对象里面的s方法
                //返回值是激活方法后return 的东西
                Object o=s.invoke(obj);
                //TODO: 加入处理 @MyBean注解对应的方法所视力话的类中@myPostConstruct
                //o在这里指  HelloWorld对象 o.getClass()  它的反射对象
                //在这里o取出来的是一个对象 new xxxx();
                handlePostConstruct(o,o.getClass());
                //键是方法名，存放的值是new出来的对象
                beanMap.put(s.getName(),o);
            }
        }


    }

    /**
     * @PostConstruct注释允许在实例化并执行所有注入之后执行方法的定义。
     * 相当于后构造方法, 需要使用构造方法之后的成员变量来初始化另外一些变量.
     * 比如b依赖a，当需要调用b中的一些操作的时候，可以在这个要调用的方法上面加上一个@postConstrust注解
     * 那么在b完成注入以后，调用这个方法
     *
     * 判断这个对象里面有没有@postConstrust
     * @param o
     * @param cls
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void handlePostConstruct(Object o, Class<?> cls) throws InvocationTargetException, IllegalAccessException {
        Method[] ms =cls.getDeclaredMethods();
        for(Method m:ms){
            if(m.isAnnotationPresent(MyPostConstruct.class)){
                m.invoke(o);
            }
        }
    }


    /**
     * 获取当前  AppConfig类所在的包路径
     * 传入一个class文件,得到当前配置类的路径
     * 这样做的目的是防止配置AppConfig时，没有给指定的路径，这样可以获取配置类默认的路径位置
     * @param
     * @return
     */
    private String[] getAppConfigBasePackages( Class cl){
        String[] paths =new String[1];
        paths[0] =cl.getPackage().getName();
        return paths;
    }
    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
