package net.kpw.slackboat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Singleton text file parser class.
 * 
 * @author kwilliford
 * @created Nov 3, 2017
 *
 */
public class FileParser {
    private static class LazyHolder {
        static final FileParser INSTANCE = new FileParser();

        private LazyHolder() {
        }
    }

    static final Log LOG = LogFactory.getLog(FileParser.class);

    private FileParser() {
    }

    /**
     * Return the singleton instance of this class.
     * 
     * @return The instance.
     */
    public static FileParser getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Parse the content of a {@link InputStream}, expecting lines to separate relevant content pieces. Returns a {@link TreeSet} of type
     * {@link String}. Returns an empty set if file parsing failed.
     * 
     * @param inputStream
     *            The input stream.
     * @return A set of string objects.
     */
    public Set<String> parseLines(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        Set<String> lines = new TreeSet<>();
        try {
            lines = br.lines().map(line -> line).filter(StringUtils::isNotBlank)
                    .collect(Collectors.toCollection(() -> new TreeSet<>()));
            LOG.debug("Parsed " + lines.size() + " lines.");
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return lines;
    }

    /**
     * Parse the content of a {@link InputStream}, expecting commas to separate relevant content pieces. Returns a {@link TreeSet} of type
     * {@link String}. Returns an empty set if file parsing failed. Returns only the "url" column (expected to be the second column).
     * 
     * @param inputStream
     *            The input stream.
     * @return A set of string objects.
     */
    public Set<String> parseURLsSecondColumn(InputStream inputStream) {
        final Set<String> lines = this.parseLines(inputStream);
        // return a set of the values from the second column
        Set<String> urls = new TreeSet<>();
        for (String line : lines) {
            Pattern regex = Pattern.compile("[^,\"']+|\"([^\"]*)\"");
            Matcher regexMatcher = regex.matcher(line);
            int counter = 0;
            while (regexMatcher.find()) {
                if (counter == 1) { // url column
                    String url = "";
                    if (regexMatcher.group(1) != null) { // url was escaped with quotes
                        url = regexMatcher.group(1).trim();
                    } else if (regexMatcher.group(0) != null) {
                        url = regexMatcher.group(0).trim();
                    }
                    if (!"url".equalsIgnoreCase(url) && StringUtils.isNotBlank(url)) {
                        urls.add(url);
                    }
                    break;
                }
                counter++;
            } 
        }
        LOG.debug("Parsed " + urls.size() + " urls.");
        return urls;
    }
}
