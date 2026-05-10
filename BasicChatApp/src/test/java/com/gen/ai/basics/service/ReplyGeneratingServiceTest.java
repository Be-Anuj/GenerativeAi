package com.gen.ai.basics.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplyGeneratingServiceTest {

    @Mock
    private OpenAIAsyncClient client;

    @InjectMocks
    private ReplyGeneratingService answerGeneratingService;

    private ChatCompletions chatCompletions;
    private ChatChoice chatChoice;
    private ChatResponseMessage chatResponseMessage;

    @BeforeEach
    void setUp() {
        chatResponseMessage = mock(ChatResponseMessage.class);
        chatChoice = mock(ChatChoice.class);
        chatCompletions = mock(ChatCompletions.class);
    }

    @Test
    void answerQuery_shouldReturnResponse_whenCompletionsAreSuccessful() {
        // Arrange
        String query = "What can I do to go to sleep?";
        String expectedResponse = "Try a consistent bedtime routine.";

        when(chatResponseMessage.getContent()).thenReturn(expectedResponse);
        when(chatChoice.getMessage()).thenReturn(chatResponseMessage);
        when(chatCompletions.getChoices()).thenReturn(List.of(chatChoice));
        when(client.getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class)))
                .thenReturn(Mono.just(chatCompletions));

        // Act
        List<String> result = answerGeneratingService.answerQuery(query);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).contains(expectedResponse);
        verify(client, times(1))
                .getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class));
    }

    @Test
    void answerQuery_shouldReturnNoResponse_whenCompletionsAreNull() {
        // Arrange
        String query = "What can I do to go to sleep?";

        when(client.getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class)))
                .thenReturn(Mono.empty()); // returns null after block()

        // Act
        List<String> result = answerGeneratingService.answerQuery(query);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).contains("No response received.");
    }

    @Test
    void answerQuery_shouldReturnNoResponse_whenChoicesAreEmpty() {
        // Arrange
        String query = "What can I do to go to sleep?";

        when(chatCompletions.getChoices()).thenReturn(List.of()); // empty choices
        when(client.getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class)))
                .thenReturn(Mono.just(chatCompletions));

        // Act
        List<String> result = answerGeneratingService.answerQuery(query);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).contains("No response received.");
    }

    @Test
    void answerQuery_shouldReturnExceptionMessage_whenClientThrowsException() {
        // Arrange
        String query = "What can I do to go to sleep?";
        String errorMessage = "Service unavailable";

        when(client.getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        List<String> result = answerGeneratingService.answerQuery(query);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).contains(errorMessage);
    }

    @Test
    void answerQuery_shouldPassCorrectQueryToClient() {
        // Arrange
        String query = "What can I do to go to sleep?";

        when(chatResponseMessage.getContent()).thenReturn("Some response");
        when(chatChoice.getMessage()).thenReturn(chatResponseMessage);
        when(chatCompletions.getChoices()).thenReturn(List.of(chatChoice));
        when(client.getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class)))
                .thenReturn(Mono.just(chatCompletions));

        // Act
        answerGeneratingService.answerQuery(query);

        // Assert - verify correct deployment name was used
        verify(client).getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class));
    }

    @Test
    void answerQuery_shouldReturnBothExceptionAndNoResponse_whenExceptionThenNullCompletions() {
        // Arrange
        String query = "What can I do to go to sleep?";
        String errorMessage = "Timeout occurred";

        when(client.getChatCompletions(eq("gpt-4"), any(ChatCompletionsOptions.class)))
                .thenReturn(Mono.error(new RuntimeException(errorMessage)));

        // Act
        List<String> result = answerGeneratingService.answerQuery(query);

        // Assert - both error message and no response should be in list
        assertThat(result).hasSize(2);
        assertThat(result).contains(errorMessage);
        assertThat(result).contains("No response received.");
    }
}

