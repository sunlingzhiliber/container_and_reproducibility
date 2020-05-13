package edu.njnu.opengms.r2;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Main
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/5/3
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        Set<String> strings=new HashSet<>();
        strings.add("sunlingzhi");
        strings.add("sunlingzhi");
        strings.add("sunlingzhi");
        strings.forEach(System.out::println);
    }
}
