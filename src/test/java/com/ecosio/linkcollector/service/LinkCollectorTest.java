package com.ecosio.linkcollector.service;

import com.ecosio.linkcollector.dto.CollectionResult;
import com.ecosio.linkcollector.service.LinkCollector;
import com.ecosio.linkcollector.service.LinkScrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkCollectorTest {

    @Mock
    private LinkScrapper linkScrapper;

    @InjectMocks
    private LinkCollector collector;


    @Test
    void givenValidLink_whenCollectLinks_thenReturnSortedResult() {
        // Given
        String originalLink = "http://www.example.com";
        Set<String> additionalLinks = new HashSet<>(
                List.of(
                        "http://www.example.com/about",
                        "http://www.example.com/contact"
                ));
        CompletableFuture<Set<String>> futureOriginal = CompletableFuture.completedFuture(additionalLinks);
        when(linkScrapper.searchLinks(originalLink)).thenReturn(futureOriginal);
        when(linkScrapper.searchLinks("http://www.example.com/about"))
                .thenReturn(CompletableFuture.completedFuture(new HashSet<>()));
        when(linkScrapper.searchLinks("http://www.example.com/contact"))
                .thenReturn(CompletableFuture.completedFuture(new HashSet<>()));

        // When
        CollectionResult result = collector.collectLinks(originalLink);

        // Then
        assertThat(result.getLinks()).containsExactly(
                "http://www.example.com",
                "http://www.example.com/about",
                "http://www.example.com/contact"
        );
    }

    @Test
    void givenInvalidLink_whenCollectLinks_thenReturnEmptyResult() {
        // Given
        String invalidLink = "invalid";
        // When
        CollectionResult result = collector.collectLinks(invalidLink);
        // Then
        assertThat(result.getLinks()).isEmpty();
    }
}
