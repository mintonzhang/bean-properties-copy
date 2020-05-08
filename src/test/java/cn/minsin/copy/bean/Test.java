package cn.minsin.copy.bean;

import cn.minsin.copy.bean.classes.A;
import cn.minsin.copy.bean.classes.B;

/**
 * @author: minton.zhang
 * @since: 2020/5/8 12:53
 */
public class Test {

    public static void main(String[] args) {
        BPC.copy(A.class, B.class);
    }
}
