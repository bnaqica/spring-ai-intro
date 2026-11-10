package com.bnaqica.spring_ai_intro.controllers;

import com.bnaqica.spring_ai_intro.models.Answer;
import com.bnaqica.spring_ai_intro.models.CapitalWithInfoResponse;
import com.bnaqica.spring_ai_intro.models.GetCapitalRequest;
import com.bnaqica.spring_ai_intro.models.GetCapitalResponse;
import com.bnaqica.spring_ai_intro.models.Question;
import com.bnaqica.spring_ai_intro.services.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    private final OpenAiService openAiService;

    public QuestionController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }
    @PostMapping("/capitalWithInfo")
    public CapitalWithInfoResponse getCapitalWithInfo(@RequestBody GetCapitalRequest capitalRequest) {
        return openAiService.getCapitalWithInfo(capitalRequest);
    }

    @PostMapping("/capital")
    public GetCapitalResponse getCapital(@RequestBody GetCapitalRequest capitalRequest) {
        return openAiService.getCapital(capitalRequest);
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAiService.getAnswer(question);
    }


}
