package com.adsizzler.mangolaa.kafkaconsumer.wins.storage.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Ankush on 16/02/17.
 */
public class Gzip {

    public static byte[] compress(final String str) {
        if (!Strings.hasText(str)) {
            throw new IllegalArgumentException("Cannot zip null or empty string");
        }

        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
            }
            return byteArrayOutputStream.toByteArray();
        }
        catch(final IOException e) {
            throw new RuntimeException("Failed to zip content", e);
        }
    }

    public static String decompress(final byte[] compressed) {
        if ((compressed == null) || (compressed.length == 0)) {
            throw new IllegalArgumentException("Cannot unzip null or empty bytes");
        }
        if (!isZipped(compressed)) {
            return new String(compressed);
        }

        try (final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed) ) {
            try (final GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
                try (final InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8)) {
                    try (final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        StringBuilder output = new StringBuilder();
                        String line;
                        while((line = bufferedReader.readLine()) != null ){
                            output.append(line);
                        }
                        return output.toString();
                    }
                }
            }
        } catch(IOException e) {
            throw new RuntimeException("Failed  to unzip content", e);
        }
    }
    public static boolean isZipped(final byte[] compressed ) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }


}
