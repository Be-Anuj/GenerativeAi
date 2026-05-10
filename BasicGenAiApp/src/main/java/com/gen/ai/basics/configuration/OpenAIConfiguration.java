package com.gen.ai.basics.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Azure OpenAI Async Client.
 * <p>
 * This configuration defines a bean that provides an asynchronous client
 * for interacting with the  OpenAI Service. It uses the open api Key
 * Credential for authentication and connects to a specified endpoint.
 */
@Configuration
public class OpenAIConfiguration {

    @Value("${client-openai-endpoint}")
    String endpoint;

    @Value("${client-openai-key}")
    String apiKey;
    /**
     * Creates an  bean for interacting with  OpenAI Service asynchronously.
     *
     * @return an instance of OpenAIAsyncClient
     */
    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(endpoint)
                .buildAsyncClient();
    }
}
