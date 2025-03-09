package com.ecosio.linkcollector.dto;

import java.util.List;

public class CollectionResult {

    private final List<String> links;

    public CollectionResult(List<String> sortedLinks) {
        this.links = sortedLinks;
    }

    @Override
    public String toString() {
        return "CollectionResult{" +
                "numberOfLinks=" + links.size() +
                ", links=" + links +
                '}';
    }

    public List<String> getLinks() {
        return links;
    }
}
