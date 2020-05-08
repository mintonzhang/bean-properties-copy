package cn.minsin.copy.bean.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: minton.zhang
 * @since: 2020/5/8 10:25
 */
public class Template {

    public interface TemplateConstant {
        String RETURN_CLASS = "RETURN_CLASS";
        String SET_METHOD = "SET_METHOD";
        String GET_METHOD = "GET_METHOD";
        String SOURCE_CLASS = "SOURCE_CLASS";
    }

    public static String beanTransferTemplate() {
        return "public static " + TemplateConstant.RETURN_CLASS + " transfer(final " + TemplateConstant.SOURCE_CLASS + " source) {\n" +
                " return source == null ? null : new " + TemplateConstant.RETURN_CLASS + "() {{\n" +
                " \tthis." + TemplateConstant.SET_METHOD + "(source." + TemplateConstant.GET_METHOD + "());\t\n" +
                " }};\n" +
                "}";
    }

    public static String listBeanTransferTemplate() {
        return "public static List<" + TemplateConstant.RETURN_CLASS + "> transfer(final List<" + TemplateConstant.SOURCE_CLASS + "> source) {\n" +
                " // 注意 此方法如果返回的空List是不能进行增删改查的,如果有必要 将Collections.emptyList()替换为Lists.newArrayList()或new ArrayList<>()\n" +
                " return (source == null || source.isEmpty()) ? Collections.emptyList() : new ArrayList<" + TemplateConstant.RETURN_CLASS + ">(source.size()) {{\n" +
                " source.forEach(f -> this.add(transfer(f)));" +
                " }};\n" +
                "}";

    }


    /**
     * A ->B
     *
     * @param source
     * @param target
     */
    public static void convert(Class<?> source, Class<?> target) {
        BeanExtract beanExtractA = new BeanExtract(source);
        BeanExtract beanExtractB = new BeanExtract(target);
        //获取应该解析的方法
        Map<Method, Method> merge = BeanExtract.merge(beanExtractB, beanExtractA);
        String beanTransferTemplate = beanTransferTemplate();
        String properties = beanTransferTemplate.substring(beanTransferTemplate.indexOf("\t"), beanTransferTemplate.indexOf("\t\n"));

        List<String> propertiesList = new ArrayList<>(20);
        Set<Method> methods = merge.keySet();
        for (Method method : methods) {
            propertiesList.add(properties.replace(TemplateConstant.SET_METHOD, method.getName())
                    .replace(TemplateConstant.GET_METHOD, merge.get(method).getName()));
        }

        String replace = beanTransferTemplate.replace(TemplateConstant.RETURN_CLASS, beanExtractB.getClassName())
                .replace(TemplateConstant.SOURCE_CLASS, beanExtractA.getClassName())
                .replace(properties, String.join("\n", propertiesList));
        System.out.println(replace);
    }

    /**
     * A ->B
     *
     * @param source
     * @param target
     */
    public static void convertList(Class<?> source, Class<?> target) {
        BeanExtract beanExtractA = new BeanExtract(source);
        BeanExtract beanExtractB = new BeanExtract(target);
        String beanTransferTemplate = listBeanTransferTemplate();
        String replace = beanTransferTemplate.replace(TemplateConstant.RETURN_CLASS, beanExtractB.getClassName())
                .replace(TemplateConstant.SOURCE_CLASS, beanExtractA.getClassName()).concat("\n");
        System.out.println(replace);
    }
}
