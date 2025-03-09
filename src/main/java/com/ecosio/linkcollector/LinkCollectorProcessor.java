package com.ecosio.linkcollector;

import com.ecosio.linkcollector.service.LinkScrapper;
import com.ecosio.linkcollector.util.LinkCollectorUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LinkCollectorProcessor {
    private static final int N_THREADS = 10;
    private static final Logger log = Logger.getLogger(LinkCollectorProcessor.class.getName());
    private final Set<String> visitedLinks = ConcurrentHashMap.newKeySet();
    private final ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
    private final LinkScrapper linkScrapper;

    public LinkCollectorProcessor(LinkScrapper linkScrapper) {
        this.linkScrapper = linkScrapper;
    }

    public Set<String> process(String link, String originalDomain) {
        visitedLinks.clear();
        CompletableFuture<Void> future = processLinkAsync(link, originalDomain);
        future.whenComplete((res, ex) -> executor.shutdown()).join();
        return new HashSet<>(visitedLinks);
    }

    private CompletableFuture<Void> processLinkAsync(String link, String originalDomain) {
        if (!visitedLinks.add(link)) {
            return CompletableFuture.completedFuture(null);
        }
        return linkScrapper.searchLinks(link)
                .thenComposeAsync(links -> {
                    var newLinks = links.stream()
                            .filter(l -> LinkCollectorUtil.hasSameDomain(originalDomain, l))
                            .filter(l -> !visitedLinks.contains(l))
                            .collect(Collectors.toSet());
                    var futures = newLinks.stream()
                            .map(newLink -> processLinkAsync(newLink, originalDomain))
                            .toArray(CompletableFuture[]::new);
                    return CompletableFuture.allOf(futures);
                }, executor)
                .exceptionally(ex -> {
                    log.severe("Error scraping links from " + link + ": " + ex.getMessage());
                    return null;
                });
    }
}
