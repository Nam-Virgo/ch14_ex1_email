package murach.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class MailUtilGmail {

    public static void sendMail(String to, String from, String subject, String body, boolean bodyIsHTML)
            throws Exception {

        String apiKey = System.getenv("API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Missing SENDGRID_API_KEY environment variable");
        }
        // Build JSON payload (simple version)
        String contentType = bodyIsHTML ? "text/html" : "text/plain";
        String json = "{"
                + "\"personalizations\":[{\"to\":[{\"email\":\"" + escapeJson(to) + "\"}]}],"
                + "\"from\":{\"email\":\"" + escapeJson(from) + "\"},"
                + "\"subject\":\"" + escapeJson(subject) + "\","
                + "\"content\":[{\"type\":\"" + contentType + "\",\"value\":\"" + escapeJson(body) + "\"}]"
                + "}";

        URL url = new URL("https://api.sendgrid.com/v3/mail/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        byte[] out = json.getBytes(StandardCharsets.UTF_8);
        conn.setFixedLengthStreamingMode(out.length);
        conn.connect();
        try (OutputStream os = conn.getOutputStream()) {
            os.write(out);
        }

        int status = conn.getResponseCode();
        if (status != 202) { // 202 = accepted
            throw new RuntimeException("SendGrid API returned HTTP " + status);
        }
        // success
    }

    // minimal JSON string escaper (for our simple payload)
    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
