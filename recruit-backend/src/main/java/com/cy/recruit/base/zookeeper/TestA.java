package com.cy.recruit.base.zookeeper;

import java.util.List;

public interface TestA<T> {

   // String sayHello(String a);

    //TestB sayHello(TestB testB);
    void sayHello(T t);
}
