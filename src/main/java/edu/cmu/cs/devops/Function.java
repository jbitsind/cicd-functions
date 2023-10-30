package edu.cmu.cs.devops;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.apache.commons.codec.binary.Base64; // import Base64


import java.util.Arrays;
import java.util.Collections; // Add import for Collections
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String input = request.getBody().orElse(query);

        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            // String decoded = decodeBase64(input);
            // String sorted = sortWords(decoded);
            // String encoded = encodeBase64(sorted);
            // Decode Base64
            String decoded = decodeBase64(input);
            context.getLogger().info("Decoded String: " + decoded);

            // Sort words
            String sorted = sortWords(decoded);
            context.getLogger().info("Sorted String: " + sorted);

            // Encode Base64
            String encoded = encodeBase64(sorted);
            context.getLogger().info("Encoded String: " + encoded);
            return request.createResponseBuilder(HttpStatus.OK).body(encoded).build();
        }
    }

    private String decodeBase64(String base64Input) {
        byte[] decodedBytes = Base64.decodeBase64(base64Input);
        return new String(decodedBytes);
    }

    protected String sortWords(String input) {
            // Split the input into words
            String[] words = input.split("\\s+");
            // Sort the words in descending order
            Arrays.sort(words, (s1, s2) -> s2.compareToIgnoreCase(s1) == 0 ? s2.compareTo(s1) : s2.compareToIgnoreCase(s1) );
            // Join the sorted words with whitespace
            return String.join(" ", words);
    }

    private String encodeBase64(String input) {
        byte[] encodedBytes = Base64.encodeBase64(input.getBytes());
        return new String(encodedBytes);
    }
}