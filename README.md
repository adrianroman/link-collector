# Link Collector

Link Collector is a Java application that collects and filters interconnected links from a given URL. It scrapes links from the page, then recursively fetches only those links that belong to the same domain as the original URL.

## How It Works

1. **Input URL & Domain Extraction**  
   When you supply a URL (e.g., `http://www.example.com`), the application extracts the host part (i.e., `www.example.com`) and stores it as the original domain.

2. **Link Scraping & Filtering**  
   The application uses an asynchronous `LinkScrapper` service to fetch the page contents and applies a regular expression to extract all absolute URLs.
    - **Filtering:** Only URLs that match the original domain are queued for further processing. For example, if the original URL is from `www.example.com`, only links from that domain (like `http://www.example.com/about`) will be processed.

4. **Results**  
   After processing, all collected links are sorted and returned as a `CollectionResult`, which includes the original URL plus any interconnected same-domain links.

## Tested URLs

You can test the application using URLs such as:

- **Example 1:**  
  **URL:** `https://martinfowler.com/architecture/`  
  **Expected Outcome:**  
  Should return around 13 links
- **Example 2:**  
  **URL:** `https://ecosio.com/en/careers/`  
  **Expected Outcome:**  
    Should return around 336 links
## Running the Application with Maven

### Prerequisites

- Java 21 
- Maven 

### Build and run the Project

Open a terminal in the project’s root directory (where the `pom.xml` file is located) and run:

```bash
mvn clean install
````
```bash
mvn exec:java -Dexec.mainClass="com.ecosio.linkcollector.App"
````

## Next Steps & Improvements

- **Request Rate Limiting and Client Limitation:**  
  The current HTTP client can become a bottleneck when too many requests are opened concurrently. Additionally, some websites may block your requests for security reasons if too many are sent in a short time period. Implementing rate limiting or throttling can help mitigate these issues.

- **Enhanced URL Filtering:**  
  The current URL filtering works for common URL patterns. However, it may miss or misinterpret less common URL formats. Refining the regex or using a dedicated URL parsing library could improve accuracy and coverage.
