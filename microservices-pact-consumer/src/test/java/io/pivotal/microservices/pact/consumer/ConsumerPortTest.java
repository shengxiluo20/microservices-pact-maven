package io.pivotal.microservices.pact.consumer;

import au.com.dius.pact.consumer.*;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class ConsumerPortTest {

    @Rule
    public PactProviderRule rule = new PactProviderRule("Foo_Provider", this);

    @Pact(provider="Foo_Provider", consumer="Foo_Consumer")
    public PactFragment createFragment(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return builder.uponReceiving("a request for Foos")
                .path("/foos")
                .method("GET")

                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("[{\"value\":42}, {\"value\":100}]").toFragment();
    }

    @Test
    @PactVerification("Foo_Provider")
    public void runTest() {
        List<Foo> foos = new ConsumerPort(rule.getConfig().url()).foos();
    }
}
