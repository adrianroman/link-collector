package com.ecosio.linkcollector.service;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkScrapperTest {

    @Test
    public void givenHtmlBodyWithUrls_whenExtractLinks_thenReturnSetOfUrls() {
        // Given
        LinkScrapper scrapper = new LinkScrapper();
        String html = "<html><body>" +
                "Visit https://www.example.com for details. " +
                "Also check out http://www.example.org/about for more info." +
                "</body></html>";

        // When
        Set<String> links = scrapper.extractLinks(html);

        // Then
        assertThat(links)
                .contains("https://www.example.com")
                .contains("http://www.example.org/about");
    }

    @Test
    public void givenInvalidUri_whenSearchLinks_thenReturnEmptySet() {
        // Given
        LinkScrapper scrapper = new LinkScrapper();
        String invalidUri = "htp:/invalid-url";

        // When
        CompletableFuture<Set<String>> futureResult = scrapper.searchLinks(invalidUri);
        Set<String> result = futureResult.join();

        // Then
        assertThat(result).isEmpty();
    }
}
