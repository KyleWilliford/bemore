package net.kpw.slackboat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
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
    static final Log LOG = LogFactory.getLog(FileParser.class);

    private FileParser() {}

    private static class LazyHolder {
        static final FileParser INSTANCE = new FileParser();

        private LazyHolder() {}
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
            lines = br.lines().map(line -> line.trim().toLowerCase()).filter(StringUtils::isNotBlank)
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
        String[] split;
        for (String line : lines) {
            split = line.split(",");
            if (split.length >= 2) {
                String url = split[1].trim().toLowerCase(); // url column
                if ("url".equalsIgnoreCase(url) || StringUtils.isBlank(url)) {
                    LOG.debug("filtering out " + url);
                    lines.remove(line);
                } else {
                    urls.add(url);
                }
            }
        }
        LOG.debug("Parsed " + urls.size() + " urls.");
        return urls;
    }
}
