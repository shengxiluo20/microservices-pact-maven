package io.pivotal.microservices.pact.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslRequestWithoutPath;
import au.com.dius.pact.consumer.dsl.PactDslResponse;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.Request;
import au.com.dius.pact.model.RequestResponseInteraction;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PactJunitRuleMultipleInteractionsTest {

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("Foo_Provider",this);

    private RequestResponsePact pact;

    @Pact(provider="Foo_Provider", consumer="Foo_Consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        PactDslResponse body = builder
                .given("")
                .uponReceiving("post")
                .path("/hello")
                .query("")
                .method("POST")
                .body("{\"name\": \"harry\"}")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{\"hello\": \"harry\"}");

        body = body.given("")
                .uponReceiving("Nanoha")
                .path("/information")
                .query("name=Nanoha")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{\n" +
                        "    \"salary\": 80000,\n" +
                        "    \"name\": \"Takamachi Nanoha\",\n" +
                        "    \"nationality\": \"Japan\",\n" +
                        "    \"contact\": {\n" +
                        "        \"Email\": \"takamachi.nanoha@ariman.com\",\n" +
                        "        \"Phone Number\": \"9090940\"\n" +
                        "    }\n" +
                        "}");



        RequestResponsePact pact = body.toPact();
        this.pact = pact;
        return pact;
    }

    @Test
    @PactVerification("Foo_Provider")
    public void runTest() {
        ProviderHandler providerHandler = new ProviderHandler(mockProvider.getUrl());
        List<RequestResponseInteraction> interactions = pact.getInteractions();
        for (RequestResponseInteraction interaction : interactions) {
            Request request = interaction.getRequest();
            providerHandler.getInformation(request.getPath(),request.getBody().getValue(),request.getMethod(),request.getQuery());
        }
    }
}
