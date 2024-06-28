package io.github.renomizer.packages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FFmpegDownloader {
  public static void downloadFFmpeg(Path downloadPath) throws IOException, URISyntaxException {
    File downloadFolder = downloadPath.toFile();
    if (!downloadFolder.exists())
      downloadFolder.mkdir();

    InputStream inputStream = new URI(
        "https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip").toURL()
        .openStream();

    byte[] buffer = new byte[1024];
    ZipInputStream ffmpegArchiveStream = new ZipInputStream(inputStream);
    ZipEntry entry = ffmpegArchiveStream.getNextEntry();

    while (entry != null) {
      String entryName = entry.getName().substring(entry.getName().lastIndexOf('/') + 1);

      if (entryName.matches("ff.*\\.exe")) {
        FileOutputStream unzipOutputStream = new FileOutputStream(
            new File(downloadPath + "//" + entryName));

        int len;
        while ((len = ffmpegArchiveStream.read(buffer)) > 0) {
          unzipOutputStream.write(buffer, 0, len);
        }

        unzipOutputStream.close();
      }

      entry = ffmpegArchiveStream.getNextEntry();
    }

    inputStream.close();
    ffmpegArchiveStream.close();
  }
}
