package com.oi.sdk.notification;

import java.util.Arrays;
import java.util.Locale;

public class NotificationConstants {

    public static final String NOTIFICATION_CONTEXT = "notification_context";

    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String EMAIL_SUBJECT = "email_subject";
    public static final String EMAIL_SENDER = "email_sender";
    public static final String EMAIL_RECIPIENTS = "email_recipients";
    public static final String EMAIL_CONTENT = "email_content";
    public static final String EMAIL_ATTACHMENTS = "email_attachments"; // relative path from UPLOAD_DIR

    public static final String EMAIL_ALERT_HTML_TEMPLATE = "<!doctype html><html><head><title>Alert Template</title></head><body><style>td,th{padding:15px}td:nth-child(2n),th:nth-child(2n){background-color:#d6eeee}</style><table><tr><th>severity</th><td>${severity}</td></tr><tr><th>Policy</th><td>${policy}</td></tr><tr><th>device</th><td>${device}</td></tr><tr><th>Object</th><td>${object}</td></tr><tr><th>Indicator</th><td>${indicator}</td></tr><tr><th>Triggred Value</th><td>${triggered.value}</td></tr><tr><th>Status</th><td>${status}</td></tr><tr><th>Message</th><td>${message}</td></tr><tr><th>Timestamp</th><td>${timestamp}</td></tr></table></body></html>";

    public enum NotificationType {
        EMAIL, SMS, WEBHOOK;

        public static NotificationType of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        }
    }

    /**
     * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">...</a>
     */
    public enum ContentType {
        PDF("application/pdf"),
        CSV("text/csv"),
        HTML("text/html"),
        PNG("image/png"),
        JSON("application/json"),
        EXCEL("application/vnd.ms-excel");

        private final String type;

        ContentType(String type) {
            this.type = type;
        }

        public static String getType(String extension) {
            return switch (extension.toLowerCase(Locale.ROOT)) {
                case "pdf" -> PDF.type;
                case "csv" -> CSV.type;
                case "png" -> PNG.type;
                case "json" -> JSON.type;
                case "xlsx" -> EXCEL.type;
                default -> HTML.type;
            };
        }
    }

}
