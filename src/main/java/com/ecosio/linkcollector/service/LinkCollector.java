package com.ecosio.linkcollector.service;

import com.ecosio.linkcollector.LinkCollectorProcessor;
import com.ecosio.linkcollector.dto.CollectionResult;
import com.ecosio.linkcollector.exception.InvalidLinkException;
import com.ecosio.linkcollector.util.LinkCollectorUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class LinkCollector {
    private static final Logger log = Logger.getLogger(LinkCollector.class.getName());
    private String originalDomain;
    private final LinkCollectorProcessor processor;

    public LinkCollector(LinkScrapper linkScrapper) {
        this.processor = new LinkCollectorProcessor(linkScrapper);
    }

    public CollectionResult collectLinks(final String originalLink) {
        try {
            originalDomain = LinkCollectorUtil.extractHost(originalLink);
        } catch (InvalidLinkException e) {
            log.warning("Invalid original link: " + e.getMessage());
            return LinkCollectorUtil.createResponse(Collections.emptyList());
        }
        Set<String> visitedLinks = processor.process(originalLink, originalDomain);
        List<String> sortedLinks = visitedLinks.stream().sorted().toList();
        return LinkCollectorUtil.createResponse(sortedLinks);
    }
}
