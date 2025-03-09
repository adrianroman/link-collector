package com.ecosio.linkcollector.util;

import com.ecosio.linkcollector.dto.CollectionResult;
import com.ecosio.linkcollector.exception.InvalidLinkException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

 public class LinkCollectorUtilTest {

    @Test
     void givenSortedLinks_whenCreateResponse_thenReturnCollectionResult() {
        // Given
        List<String> sortedLinks = List.of("http://www.example.com", "http://www.example.com/about");

        // When
        CollectionResult result = LinkCollectorUtil.createResponse(sortedLinks);

        // Then
        assertThat(result.getLinks()).isEqualTo(sortedLinks);
    }

    @Test
     void givenValidLink_whenExtractHost_thenReturnHost() throws InvalidLinkException {
        // Given
        String link = "http://www.example.com/path";

        // When
        String host = LinkCollectorUtil.extractHost(link);

        // Then
        assertThat(host).isEqualTo("www.example.com");
    }

    @Test
     void givenInvalidLink_whenExtractHost_thenThrowInvalidLinkException() {
        // Given
        String invalidLink = "invalid://";

        // When/Then
        assertThrows(InvalidLinkException.class, () -> LinkCollectorUtil.extractHost(invalidLink));
    }

    @Test
    void givenSameDomain_whenHasSameDomain_thenReturnTrue() {
        // Given
        String originalDomain = "www.example.com";
        String link = "http://www.example.com/somepage";

        // When
        boolean result = LinkCollectorUtil.hasSameDomain(originalDomain, link);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void givenDifferentDomain_whenHasSameDomain_thenReturnFalse() {
        // Given
        String originalDomain = "www.example.com";
        String link = "http://www.other.com/page";

        // When
        boolean result = LinkCollectorUtil.hasSameDomain(originalDomain, link);

        // Then
        assertThat(result).isFalse();
    }

    @Test
     void givenMalformedLink_whenHasSameDomain_thenReturnFalse() {
        // Given
        String originalDomain = "www.example.com";
        String link = "not a valid url";

        // When
        boolean result = LinkCollectorUtil.hasSameDomain(originalDomain, link);

        // Then
        assertThat(result).isFalse();
    }
}
