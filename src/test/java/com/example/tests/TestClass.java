package com.example.tests;

import org.junit.Assert;
import org.junit.Test;

public class TestClass {

    @Test
    public void dummyTest() {
        int a = 4;
        int b = 2;
        Assert.assertEquals(2, a / b);
    }
}
