package com.ecosio.linkcollector.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkScrapper {
    private static final Logger log = Logger.getLogger(LinkScrapper.class.getName());
    public static final String REGEX_URL = "\\bhttps?://(?:[a-zA-Z0-9\\-]+\\.)+[A-Za-z]{2,}(?::\\d+)?(?:/[^\\s'\"]*)?\\b";
    private final HttpClient client;

    public LinkScrapper() {
        this(HttpClient.newHttpClient());
    }

    public LinkScrapper(HttpClient client) {
        this.client = client;
    }

    public CompletableFuture<Set<String>> searchLinks(String link) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(link))
                    .build();
        } catch (Exception e) {
            log.warning("Invalid URI: " + link);
            return CompletableFuture.completedFuture(Set.of());
        }

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::extractLinks)
                .exceptionally(ex -> {
                    log.warning("Error searching for links at " + link + ": " + ex.getMessage());
                    return Set.of();
                });
    }

    public Set<String> extractLinks(String body) {
        Pattern pattern = Pattern.compile(REGEX_URL);
        Matcher matcher = pattern.matcher(body);
        Set<String> links = new HashSet<>();
        while (matcher.find()) {
            links.add(matcher.group());
        }
        return links;
    }
}
