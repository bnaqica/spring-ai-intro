package com.bnaqica.spring_ai_intro.services;

import com.bnaqica.spring_ai_intro.models.Answer;
import com.bnaqica.spring_ai_intro.models.CapitalWithInfoResponse;
import com.bnaqica.spring_ai_intro.models.GetCapitalRequest;
import com.bnaqica.spring_ai_intro.models.GetCapitalResponse;
import com.bnaqica.spring_ai_intro.models.Question;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Service
public class OpenAiServiceImpl implements OpenAiService {
    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    public OpenAiServiceImpl(ChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource capitalPrompt;

    @Value("classpath:templates/get-capital-with-info.st")
    private Resource getCapitalWithInfo;

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatModel.call(prompt);

        return new Answer(requireNonNull(chatResponse.getResult()).getOutput().getText());
    }

    @Override
    public GetCapitalResponse getCapital(GetCapitalRequest capitalRequest) {
        BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = converter.getFormat();
        System.out.println("format: \n" + format);

        PromptTemplate promptTemplate = new PromptTemplate(capitalPrompt);
        Prompt prompt = promptTemplate.create(Map.of(
                "stateOrCountry", capitalRequest.stateOrCountry(),
                "format", format));
        ChatResponse chatResponse = chatModel.call(prompt);
        System.out.println("chatResponse: \n" + chatResponse.getResult().getOutput().getText());

        return converter.convert(requireNonNull(requireNonNull(chatResponse.getResult()).getOutput().getText()));
    }

    @Override
    public CapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest capitalRequest) {
        BeanOutputConverter<CapitalWithInfoResponse> converter = new BeanOutputConverter<>(CapitalWithInfoResponse.class);
        String format = converter.getFormat();
        System.out.println("format: \n" + format);

        PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithInfo);
        Prompt prompt = promptTemplate.create(Map.of(
                "stateOrCountry", capitalRequest.stateOrCountry(),
                "format", format));
        ChatResponse chatResponse = chatModel.call(prompt);

        return converter.convert(requireNonNull(requireNonNull(chatResponse.getResult()).getOutput().getText()));
    }
}
