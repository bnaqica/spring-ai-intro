package com.bnaqica.spring_ai_intro.services;

import com.bnaqica.spring_ai_intro.models.Answer;
import com.bnaqica.spring_ai_intro.models.CapitalWithInfoResponse;
import com.bnaqica.spring_ai_intro.models.GetCapitalRequest;
import com.bnaqica.spring_ai_intro.models.GetCapitalResponse;
import com.bnaqica.spring_ai_intro.models.Question;

public interface OpenAiService {
    Answer getAnswer(Question question);

    GetCapitalResponse getCapital(GetCapitalRequest capitalRequest);

    CapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest capitalRequest);
}
