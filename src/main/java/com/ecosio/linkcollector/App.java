package com.ecosio.linkcollector;

import com.ecosio.linkcollector.dto.CollectionResult;
import com.ecosio.linkcollector.service.LinkCollector;
import com.ecosio.linkcollector.service.LinkScrapper;

import java.util.Scanner;
import java.util.logging.Logger;

public class App {

    static Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        LinkScrapper linkScrapper = new LinkScrapper();
        LinkCollector linkCollector = new LinkCollector(linkScrapper);
        log.info("Welcome to link collector");
        log.info("Introduce a valid url from a domain to collect all the interconnected links inside");
        Scanner scanner = new Scanner(System.in);
        String link = scanner.nextLine();
        CollectionResult collectionResult = linkCollector.collectLinks(link);
        log.info("\n\n\n\n");
        log.info("Result of the search");
        log.info(collectionResult.toString());
        log.info("\n");
    }

}
