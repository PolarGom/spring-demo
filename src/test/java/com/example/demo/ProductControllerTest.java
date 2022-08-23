package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    /**
     * MariaDB 에서 총 개수 조회 후 물건 구매
     *  description: Lock 이 걸려있지않아 구매를 해도 남은 갯수가 정확하지 않다.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("락_X_마리아DB_조회_후_물건_구매")
    public void testNoLockMariaDBSelectAfterProductBuy() throws Exception {

        int executeCount = 10;

        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        int productKey = 1;
        int buyCount = 2;

        String buyUrl = "/buy/{0}/{1}";

        for ( int index = 0; index < executeCount; index++ ) {

            new Thread(new Worker(mockMvc, countDownLatch, productKey, buyCount, buyUrl)).start();
        }

        countDownLatch.await();
    }

    /**
     * Redis 에서 총 개수 조회 후 물건 구매- Lock
     *  description: Lock 이 걸려있어 정상적으로 구매
     *
     * @throws Exception
     */
    @Test
    @DisplayName("락_O_Redis_조회_후_물건_구매")
    public void testLockRedisLockSelectAfterProductBuy() throws Exception {

        int executeCount = 20;

        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        int productKey = 1;
        int buyCount = 2;

        String buyUrl = "/buy/redis-lock/{0}/{1}";

        for ( int index = 0; index < executeCount; index++ ) {

            new Thread(new Worker(mockMvc, countDownLatch, productKey, buyCount, buyUrl)).start();
        }

        countDownLatch.await();
    }

    /**
     * Redis 에서 총 개수 조회 후 물건 구매- No Lock
     *  description: Lock 이 걸려있지않아 정상적으로 구매 X
     *
     * @throws Exception
     */
    @Test
    @DisplayName("락_X_Redis_조회_후_물건_구매")
    public void testLockRedisNoLockSelectAfterProductBuy() throws Exception {

        int executeCount = 20;

        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        int productKey = 1;
        int buyCount = 2;

        String buyUrl = "/buy/redis-no-lock/{0}/{1}";

        for ( int index = 0; index < executeCount; index++ ) {

            new Thread(new Worker(mockMvc, countDownLatch, productKey, buyCount, buyUrl)).start();
        }

        countDownLatch.await();
    }

    public static class Worker implements Runnable {

        private MockMvc mockMvc;

        private CountDownLatch countDownLatch;

        private int productKey;

        private int buyCount;

        private String buyUrl;

        public Worker(MockMvc mockMvc, CountDownLatch countDownLatch, int productKey, int buyCount, String buyUrl) {

            this.mockMvc = mockMvc;
            this.countDownLatch = countDownLatch;
            this.productKey = productKey;
            this.buyCount = buyCount;
            this.buyUrl = buyUrl;
        }

        @Override
        public void run() {

            try {

                MvcResult mvcResult =  this.mockMvc.perform(MockMvcRequestBuilders.patch(this.buyUrl
                                        , this.productKey, this.buyCount)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

                assertEquals(200, mvcResult.getResponse().getStatus());
            } catch ( Exception e ) {

                e.printStackTrace();
            } finally {

                countDownLatch.countDown();
            }
        }
    }
}
