package br.com.fiap.mazyfood.payment.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SQSConfig {

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient){
        return SqsTemplate.builder().sqsAsyncClient(sqsAsyncClient).build();
    }

}


