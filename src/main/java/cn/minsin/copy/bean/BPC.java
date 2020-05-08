package cn.minsin.copy.bean;

import cn.minsin.copy.bean.core.Template;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * @author: minton.zhang
 * @since: 2020/5/8 10:21
 */
public interface BPC {

    static void copy(@NonNull Class<?> source, @NonNull Class<?>... copies) {
        for (Class<?> copy : copies) {
            String format = String.format("'%s' ===============转换为===============>> '%s'\n", source.getName(), copy.getName());
            System.err.println(format);
            Template.convert(source, copy);
            Template.convertList(source, copy);
            System.out.println("==============================================================================================================================\n\n");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
                //
            }
        }

    }
}
