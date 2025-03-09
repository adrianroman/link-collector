package com.ecosio.linkcollector.service;

import com.ecosio.linkcollector.LinkCollectorProcessor;
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
public class LinkCollectorProcessorTest {

    @Mock
    private LinkScrapper linkScrapper;

    @InjectMocks
    private LinkCollectorProcessor processor;


    @Test
    void shouldCollectLinksRecursively() {
        // Given
        String original = "http://www.example.com";
        String domain = "www.example.com";

        Set<String> linksOriginal = new HashSet<>(List.of("http://www.example.com/about", "http://www.example.com/contact"));
        when(linkScrapper.searchLinks(original)).thenReturn(CompletableFuture.completedFuture(linksOriginal));

        Set<String> linksAbout = new HashSet<>(List.of("http://www.example.com/team"));
        when(linkScrapper.searchLinks("http://www.example.com/about")).thenReturn(CompletableFuture.completedFuture(linksAbout));

        when(linkScrapper.searchLinks("http://www.example.com/contact")).thenReturn(CompletableFuture.completedFuture(new HashSet<>()));

        when(linkScrapper.searchLinks("http://www.example.com/team")).thenReturn(CompletableFuture.completedFuture(new HashSet<>()));

        // When
        Set<String> result = processor.process(original, domain);

        // Then
        Set<String> expected = new HashSet<>();
        expected.add(original);
        expected.add("http://www.example.com/about");
        expected.add("http://www.example.com/contact");
        expected.add("http://www.example.com/team");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldHandleExceptionsGracefully() {
        // Given
        String original = "http://www.example.com";
        String domain = "www.example.com";

        when(linkScrapper.searchLinks(original)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("error")));

        // When
        Set<String> result = processor.process(original, domain);

        // Then
        Set<String> expected = new HashSet<>();
        expected.add(original);
        assertThat(result).isEqualTo(expected);
    }
}


