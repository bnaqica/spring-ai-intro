package com.bnaqica.spring_ai_intro.services;

import com.bnaqica.spring_ai_intro.models.Answer;
import com.bnaqica.spring_ai_intro.models.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpenAiServiceImplTest {
    @Autowired
    OpenAiServiceImpl openAiServiceImpl;

    @Test
    void getAnswer() {
        Answer answer = openAiServiceImpl.getAnswer(new Question("If it takes one person 5 hours to dig a 10 foot " +
                "hole in the ground. How lown would it take 5 people?"));

        System.out.println(answer);
    }
}