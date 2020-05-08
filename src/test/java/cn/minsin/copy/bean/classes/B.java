package cn.minsin.copy.bean.classes;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author: minton.zhang
 * @since: 2020/5/8 11:43
 */
@Getter
@Setter
public class B {

    private Integer level;

    private String name;

    private Long age;

    private Date birthDay;


    private List<Integer> list;
}
