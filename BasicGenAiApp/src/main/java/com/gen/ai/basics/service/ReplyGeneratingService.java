package com.gen.ai.basics.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReplyGeneratingService {

    @Value("${client-openai-model-name}")
    private String modelName;

    private final OpenAIAsyncClient client;

    public ReplyGeneratingService(
            OpenAIAsyncClient client,
            @Value("${client-openai-model-name}") String modelName) {
        this.modelName = modelName;
        this.client = client;
    }

    public List<String> answerQuery(String query) {
        ChatCompletionsOptions options = new ChatCompletionsOptions(
                List.of(new ChatRequestUserMessage(query))
        );
        ChatCompletions completions=null;
        List <String> response =new ArrayList<>();
        try {
            completions = client
                    .getChatCompletions(modelName, options)
                    .block();
        }catch (Exception exception){
            response.add(exception.getMessage());
        }
        if (completions != null && !completions.getChoices().isEmpty()) {
            response.add(completions.getChoices().get(0).getMessage().getContent());
        }
        return response;
    }
}
