package com.example.catnotcat;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.function.Function;

// Add imports


// Add Imports


@SpringBootApplication
public class CatFunction {

    public static void main(String[] args) {
        SpringApplication.run(CatFunction.class, args);
    }

    // This configures the Vision API settings with a credential using the
    // the scope we specified in the application.properties.
    @Bean
    public ImageAnnotatorSettings imageAnnotatorSettings(CredentialsProvider credentialsProvider) throws IOException {
        return ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(credentialsProvider).build();
    }

    @Bean
    public ImageAnnotatorClient imageAnnotatorClient(ImageAnnotatorSettings settings) throws IOException {
        return ImageAnnotatorClient.create(settings);
    }

    @Bean
    public Function<String, String> catnotcat(ImageAnnotatorClient client) {
        return (img) -> {
            // Decode the Base64 encoded input into bytes.
            byte[] bytes = Base64.getDecoder().decode(img);

            // Make a Vision API request to detect labels
            BatchAnnotateImagesResponse response = client
                    .batchAnnotateImages(Collections.singletonList(
                            AnnotateImageRequest.newBuilder()
                                    .setImage(Image.newBuilder()
                                            .setContent(ByteString.copyFrom(bytes)))
                                    .addFeatures(Feature.newBuilder()
                                            .setType(Feature.Type.LABEL_DETECTION))
                                    .build()));

            // For debugging purposes :)
            System.out.println(response.toString());

            // If any label matches "cat" with score >= 90%,
            // then return "cat", otherwise return "not cat"
            return response.getResponses(0).getLabelAnnotationsList()
                    .stream().anyMatch(label -> "cat".equals(label.getDescription())
                            && label.getScore() >= 0.90f) ?
                    "cat" : "not cat";

        };
    }


}
