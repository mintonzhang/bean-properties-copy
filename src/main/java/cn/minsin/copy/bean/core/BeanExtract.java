package cn.minsin.copy.bean.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author: minton.zhang
 * @since: 2020/5/8 10:45
 */
@Getter
@Setter
@ToString
public class BeanExtract {

    protected String beanName;

    protected Set<Method> methodsGet = new HashSet<>(30);

    protected Set<Method> methodsSet = new HashSet<>(30);

    protected Class<?> clazz;

    protected String className;

    public BeanExtract(@NonNull Class<?> clazz) {
        this.init(clazz);
    }

    protected void init(Class<?> clazz) {
        this.className = clazz.getSimpleName();
        this.beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
        this.clazz = clazz;
        Set<Method> methods = this.extractMethods();
        this.filterGetMethod().accept(methods, methodsGet);
        this.filterSetMethod().accept(methods, methodsSet);
    }

    protected Set<Method> extractMethods() {
        Set<Method> methods = new HashSet<>();
        //第一个superClass
        Class<?> superClass = clazz.getSuperclass();

        //如果父类不是Object才进行添加
        if (superClass != null && !superClass.equals(Object.class)) {
            while (superClass != null) {
                Method[] declaredMethods = superClass.getDeclaredMethods();
                methods.addAll(Arrays.asList(declaredMethods));
                superClass = superClass.getSuperclass();
            }
        }
        Method[] declaredMethods = clazz.getDeclaredMethods();
        methods.addAll(Arrays.asList(declaredMethods));
        return methods;
    }

    /**
     * function的一个参数 是getMethods集合
     * function的第二参数 该类的所有方法
     *
     * @return
     */
    protected BiConsumer<Set<Method>, Set<Method>> filterGetMethod() {
        return (a, b) -> a.forEach(e -> {
            String name = e.getName();
            String substring = name.substring(0, 3);
            boolean nameContains = "GET".equalsIgnoreCase(substring);
            if (nameContains && this.modifierCheck(e) && e.getParameterCount() == 0) {
                b.add(e);
            }
        });
    }

    /**
     * function的一个参数 是setMethods集合
     * function的第二参数 该类的所有方法
     *
     * @return
     */
    protected BiConsumer<Set<Method>, Set<Method>> filterSetMethod() {
        return (a, b) -> a.forEach(e -> {
            String name = e.getName();
            String substring = name.substring(0, 3);
            boolean nameContains = "SET".equalsIgnoreCase(substring);
            if (nameContains && this.modifierCheck(e)) {
                b.add(e);
            }
        });
    }

    protected boolean modifierCheck(Method e) {
        return Modifier.isPublic(e.getModifiers())
                && !Modifier.isStatic(e.getModifiers())
                && !Modifier.isTransient(e.getModifiers())
                && !Modifier.isVolatile(e.getModifiers());
    }

    /**
     * 方法合并
     *
     * @param set 对象a 只拿set方法
     * @param get 对象b 只拿get方法
     * @return
     */
    public static Map<Method, Method> merge(BeanExtract set, BeanExtract get) {
        HashMap<Method, Method> mergeMethod = new HashMap<>(50);

        for (Method method : set.getMethodsSet()) {
            String substring = method.getName().substring(2);
            Class<?> parameterType = method.getParameterTypes()[0];
            for (Method method1 : get.getMethodsGet()) {
                String substring2 = method1.getName().substring(2);
                Class<?> returnType = method1.getReturnType();

                if (substring.equalsIgnoreCase(substring2) && parameterType.equals(returnType)) {
                    mergeMethod.put(method, method1);
                }
            }
        }
        return mergeMethod;
    }
}
