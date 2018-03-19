package com.hnqc.ironhand.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class IdWorkerTest {
    @Test
    public void testNextId() {
        long id1 = IdWorker.nextId();
        long id2 = IdWorker.nextId();
        Assert.assertNotEquals(id1, id2);
        System.out.println(id1);
        System.out.println(id2);
        Assert.assertTrue(Long.toString(IdWorker.nextId()).length() == 18);
    }
}
