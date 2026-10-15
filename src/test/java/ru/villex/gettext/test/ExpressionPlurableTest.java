package ru.villex.gettext.test;

import org.junit.Assert;
import org.junit.Test;
import ru.villex.gettext.plurals.ExpressionPlurable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExpressionPlurableTest {

    @Test
    public void testEnglishPluralForms() {
        ExpressionPlurable plural = new ExpressionPlurable("(n != 1);");
        Assert.assertEquals(1, plural.pluralEval(0));
        Assert.assertEquals(0, plural.pluralEval(1));
        Assert.assertEquals(1, plural.pluralEval(3));
        Assert.assertEquals(1, plural.pluralEval(5));
    }

    @Test
    public void testNumericResult() {
        ExpressionPlurable plural = new ExpressionPlurable("n / 2;");
        Assert.assertEquals(2, plural.pluralEval(4));
    }

    @Test
    public void testConcurrentPluralFormsAcrossInstances() throws Exception {
        final ExpressionPlurable[] plurals = {
                new ExpressionPlurable("(n != 1);"),
                new ExpressionPlurable("(n != 1);")
        };
        final int threads = 8;
        final CountDownLatch ready = new CountDownLatch(threads);
        final CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<Integer>> results = new ArrayList<>();
        try {
            for (int thread = 0; thread < threads; thread++) {
                final long n = thread % 2 == 0 ? 1 : 5;
                final ExpressionPlurable plural = plurals[(thread / 2) % plurals.length];
                results.add(pool.submit(() -> {
                    ready.countDown();
                    start.await();
                    int failures = 0;
                    for (int i = 0; i < 10000; i++) {
                        if (plural.pluralEval(n) != (n == 1 ? 0 : 1)) {
                            failures++;
                        }
                    }
                    return failures;
                }));
            }
            Assert.assertTrue("Workers did not start", ready.await(10, TimeUnit.SECONDS));
            start.countDown();
            int failures = 0;
            for (Future<Integer> result : results) {
                failures += result.get(30, TimeUnit.SECONDS);
            }
            Assert.assertEquals("Concurrent calls selected the wrong plural form", 0, failures);
        } finally {
            start.countDown();
            pool.shutdownNow();
            Assert.assertTrue("Workers did not stop", pool.awaitTermination(10, TimeUnit.SECONDS));
        }
    }
}
