package com.ori;

import com.ori.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InterfaceSpeedTest {

    @Autowired
    private CommentService commentService;

    public interface TestInterface{
        void testMethod();
    }

    @Test
    public void testCommentList(){

        TestInterface testInterface = new TestInterface() {
            @Override
            public void testMethod() {
                Long articleId = 1L;
                Integer pageNum = 1;
                Integer size = 1;

                commentService.commentList("0", articleId,pageNum,size);
            }
        };
        long startTime = System.currentTimeMillis();

        testInterface.testMethod();

        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;

        System.out.println("接口调用时间" + timeElapsed + "ms");
    }

}
