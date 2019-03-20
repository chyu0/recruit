package com.cy.recruit.base.zookeeper;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

public class TestB implements Comparable<TestB>{

    private String name;

    public TestB(String name){
        this.name = name;
    }


    public static void say(TestA testA){
        //testA.sayHello("aaaa");

        testA.sayHello(Collections.EMPTY_LIST);

        Set<TestB> sets = new TreeSet<>();
        TestB a1 = new TestB("aaa");
        TestB a2 = new TestB("aaa");
        sets.add(a1);
        sets.add(a2);
        System.out.println(a1.hashCode());
        System.out.println(a2.hashCode());
        System.out.println(sets);
        Map<String,String> aa = new HashMap<>();
        Map<String,String> bb = new ConcurrentHashMap<>();

    }

    public static void main(String[] args) throws Exception{
        say(name -> {
            Callable<String> call = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return null;
                }
            };

        });
    }

    @Override
    public int compareTo(TestB o) {
       return this.name.compareTo(o.name);
    }
}
