package kb_hack.backend.domain.document.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class HttpFileUtil {

	public static class Fetched {
		public final File file;
		public final String fileName;
		public final String contentType;
		public Fetched(File file, String fileName, String contentType) {
			this.file = file; this.fileName = fileName; this.contentType = contentType;
		}
	}

	/** URL에서 파일을 받아 temp 저장. 파일명/컨텐트타입 기반 확장자 추정 */
	public static Fetched fetchToTemp(String urlStr) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
		conn.setInstanceFollowRedirects(true);
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (KB-Hack)");
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(20000);

		int code = conn.getResponseCode();
		if (code / 100 != 2) throw new IOException("HTTP " + code + " for " + urlStr);

		String contentType = conn.getContentType(); // e.g. application/pdf
		String disposition = conn.getHeaderField("Content-Disposition"); // filename="xxx.pdf"
		String fileName = extractFileName(disposition);
		if (fileName == null || fileName.isBlank()) {
			fileName = guessNameFromUrl(urlStr);
		}

		String ext = guessExt(fileName, contentType);
		if (ext == null) ext = "bin";

		File tmp = Files.createTempFile("bizinfo_", "." + ext).toFile();
		try (InputStream in = conn.getInputStream();
			 OutputStream out = new BufferedOutputStream(new FileOutputStream(tmp))) {
			in.transferTo(out);
		}
		return new Fetched(tmp, fileName, contentType);
	}

	private static String extractFileName(String disposition) {
		if (disposition == null) return null;
		for (String part : disposition.split(";")) {
			part = part.trim();
			if (part.toLowerCase().startsWith("filename=")) {
				String v = part.substring("filename=".length()).trim();
				if (v.startsWith("\"") && v.endsWith("\"")) v = v.substring(1, v.length()-1);
				return v;
			}
		}
		return null;
	}

	private static String guessNameFromUrl(String url) {
		int q = url.indexOf('?');
		String base = q >= 0 ? url.substring(0, q) : url;
		int slash = base.lastIndexOf('/');
		return slash >= 0 ? base.substring(slash + 1) : base;
	}

	private static String guessExt(String fileName, String contentType) {
		String lower = fileName == null ? "" : fileName.toLowerCase();
		if (lower.endsWith(".pdf")) return "pdf";
		if (contentType != null && contentType.toLowerCase().contains("pdf")) return "pdf";
		if (lower.endsWith(".hwp")) return "hwp";
		if (lower.endsWith(".hwpx")) return "hwpx";
		if (lower.endsWith(".png")) return "png";
		if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "jpg";
		return null;
	}
}
