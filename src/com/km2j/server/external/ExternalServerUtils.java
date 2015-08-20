package com.km2j.server.external;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import com.km2j.server.Utils;
import com.km2j.shared.Func;

public class ExternalServerUtils {
  private ExternalServerUtils() {}

  public static InputStream openStream(final String urlString) throws IOException {
    final URL url = new URL(urlString);
    return url.openStream();
  }

  public static <T> Map<String, T> requestForMap(final String urlString,
      final Func<Reader, Map<String, T>> convertFunc) throws IOException {
    final InputStream inputStream = openStream(urlString);
    final InputStreamReader reader = Utils.toInputStreamReader(inputStream);
    return convertFunc.apply(reader);
  }

  public static <T> Collection<T> requestForCollection(final String urlString,
      final Func<Reader, Collection<T>> convertFunc) throws IOException {
    final InputStream inputStream = openStream(urlString);
    final InputStreamReader reader = Utils.toInputStreamReader(inputStream);
    return convertFunc.apply(reader);
  }
}
