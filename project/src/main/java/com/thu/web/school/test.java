package com.thu.web.school;

import java.time.LocalDateTime;

/**
 * Created by hetor on 16/12/13.
 */
public class test {
    public static void main(String[] args) {
        LocalDateTime l1 = LocalDateTime.now();
        LocalDateTime l2 = l1.minusHours(0);
        long de = l1.compareTo(l2);
        System.out.print(de);
    }
}
