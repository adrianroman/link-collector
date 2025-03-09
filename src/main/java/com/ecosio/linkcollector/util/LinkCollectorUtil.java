package com.ecosio.linkcollector.util;

import com.ecosio.linkcollector.dto.CollectionResult;
import com.ecosio.linkcollector.exception.InvalidLinkException;

import java.net.URI;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.logging.Logger;

public class LinkCollectorUtil {
    private static final Logger log = Logger.getLogger(LinkCollectorUtil.class.getName());

    public static CollectionResult createResponse(List<String> sortedLinks) {
        return new CollectionResult(sortedLinks);
    }

    public static String extractHost(final String originalLink) throws InvalidLinkException {
        URI uri = null;
        try {
            uri = new URI(originalLink);
            if (uri.getHost() == null) {
                throw new InvalidParameterException();
            }
            return uri.getHost();
        } catch (Exception e) {
            throw new InvalidLinkException("Invalid link" + originalLink, e);
        }
    }

    public static boolean hasSameDomain(final String originalDomain, final String link) {
        try {
            URI uri = new URI(link);
            String host = uri.getHost();
            return host != null && host.equalsIgnoreCase(originalDomain);
        } catch (Exception e) {
            log.warning("Failed to check complex link. Ignoring " + link + ": because " + e.getMessage());
            return false;
        }
    }
}
