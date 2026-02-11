import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * HTML Analyzer - Internship Technical Test
 * 
 * This program analyzes HTML structure from a given URL and returns
 * the text content at the deepest level of the HTML tree.
 * 
 * @author Pedro
 * @version 1.0
 */
public class HtmlAnalyzer {

    private static final String MALFORMED_HTML = "malformed HTML";
    private static final String CONNECTION_ERROR = "URL connection error";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java HtmlAnalyzer <url>");
            System.exit(1);
        }

        String urlString = args[0];
        
        try {
            String htmlContent = fetchHtmlContent(urlString);
            String result = analyzeHtml(htmlContent);
            System.out.println(result);
        } catch (IOException e) {
            System.out.println(CONNECTION_ERROR);
        } catch (MalformedHtmlException e) {
            System.out.println(MALFORMED_HTML);
        }
    }

    /**
     * Fetches HTML content from the given URL
     * 
     * @param urlString the URL to fetch content from
     * @return the HTML content as a string
     * @throws IOException if connection fails
     */
    private static String fetchHtmlContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP response code: " + responseCode);
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    /**
     * Analyzes HTML content and returns the text at the deepest level
     * 
     * @param htmlContent the HTML content to analyze
     * @return the text at the deepest level
     * @throws MalformedHtmlException if HTML structure is invalid
     */
    private static String analyzeHtml(String htmlContent) throws MalformedHtmlException {
        String[] lines = htmlContent.split("\n");
        List<HtmlNode> nodes = parseHtmlLines(lines);
        
        int maxDepth = 0;
        String deepestText = "";

        for (HtmlNode node : nodes) {
            if (node.isText() && node.getDepth() > maxDepth) {
                maxDepth = node.getDepth();
                deepestText = node.getContent();
            }
        }

        return deepestText;
    }

    /**
     * Parses HTML lines and builds a list of nodes with depth information
     * 
     * @param lines array of HTML lines
     * @return list of HTML nodes
     * @throws MalformedHtmlException if HTML structure is invalid
     */
    private static List<HtmlNode> parseHtmlLines(String[] lines) throws MalformedHtmlException {
        List<HtmlNode> nodes = new ArrayList<>();
        Stack<String> tagStack = new Stack<>();
        int currentDepth = 0;

        for (String line : lines) {
            String trimmedLine = line.trim();
            
            // Ignore empty lines
            if (trimmedLine.isEmpty()) {
                continue;
            }

            LineType lineType = identifyLineType(trimmedLine);

            switch (lineType) {
                case OPENING_TAG:
                    String openingTag = extractTagName(trimmedLine);
                    tagStack.push(openingTag);
                    currentDepth++;
                    break;

                case CLOSING_TAG:
                    if (tagStack.isEmpty()) {
                        throw new MalformedHtmlException("Closing tag without opening tag");
                    }
                    String closingTag = extractTagName(trimmedLine);
                    String expectedTag = tagStack.pop();
                    
                    if (!expectedTag.equals(closingTag)) {
                        throw new MalformedHtmlException(
                            "Mismatched tags: expected </" + expectedTag + "> but found </" + closingTag + ">");
                    }
                    currentDepth--;
                    break;

                case TEXT:
                    nodes.add(new HtmlNode(trimmedLine, currentDepth, true));
                    break;

                case UNKNOWN:
                    throw new MalformedHtmlException("Unknown line type: " + trimmedLine);
            }
        }

        // Check if all tags are closed
        if (!tagStack.isEmpty()) {
            throw new MalformedHtmlException("Unclosed tags remaining: " + tagStack);
        }

        return nodes;
    }

    /**
     * Identifies the type of an HTML line
     * 
     * @param line the line to identify
     * @return the line type
     */
    private static LineType identifyLineType(String line) {
        if (line.startsWith("<") && line.endsWith(">")) {
            if (line.startsWith("</")) {
                return LineType.CLOSING_TAG;
            } else if (!line.contains(" ") && !line.contains("/>")) {
                return LineType.OPENING_TAG;
            } else {
                return LineType.UNKNOWN;
            }
        } else if (!line.contains("<") && !line.contains(">")) {
            return LineType.TEXT;
        } else {
            return LineType.UNKNOWN;
        }
    }

    /**
     * Extracts tag name from a tag line
     * 
     * @param tagLine the tag line
     * @return the tag name
     */
    private static String extractTagName(String tagLine) {
        String tag = tagLine.substring(1, tagLine.length() - 1);
        if (tag.startsWith("/")) {
            tag = tag.substring(1);
        }
        return tag.toLowerCase();
    }

    /**
     * Enum representing different types of HTML lines
     */
    private enum LineType {
        OPENING_TAG,
        CLOSING_TAG,
        TEXT,
        UNKNOWN
    }

    /**
     * Class representing an HTML node with depth information
     */
    private static class HtmlNode {
        private final String content;
        private final int depth;
        private final boolean isText;

        public HtmlNode(String content, int depth, boolean isText) {
            this.content = content;
            this.depth = depth;
            this.isText = isText;
        }

        public String getContent() {
            return content;
        }

        public int getDepth() {
            return depth;
        }

        public boolean isText() {
            return isText;
        }
    }

    /**
     * Custom exception for malformed HTML
     */
    private static class MalformedHtmlException extends Exception {
        public MalformedHtmlException(String message) {
            super(message);
        }
    }
}
