package com.solvians.helloworld;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyApiTest {

    @Test
    public void testGreeting(){
        MyApi myApi = new MyApi();
        assertEquals("Hello World", myApi.greeting());
    }

}