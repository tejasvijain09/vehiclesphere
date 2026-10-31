package com.vehiclesphere.notification;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:notifications@vehiclesphere.com}")
    private String fromEmail;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Async
    public void sendRegistrationEmail(String toEmail, String customerName) {
        String subject = "Welcome to VehicleSphere — Your Luxury Wedding Rental Partner";
        String htmlContent = buildHtmlTemplate(
                "Welcome to VehicleSphere!",
                "Hello " + customerName + ",<br/><br/>" +
                        "Thank you for registering with VehicleSphere. We are honoured to be part of your special wedding celebrations.<br/>" +
                        "Browse our exclusive fleet of Rolls-Royce, Defender, Range Rover, Land Cruiser, and luxury sedans anytime."
        );
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendBookingCreatedEmail(String toEmail, String customerName, String bookingRef, String vehicleName, BigDecimal amount) {
        String subject = "Booking Request Received: " + bookingRef;
        String htmlContent = buildHtmlTemplate(
                "Booking Request Received",
                "Hello " + customerName + ",<br/><br/>" +
                        "Your booking request <strong>" + bookingRef + "</strong> for <strong>" + vehicleName + "</strong> has been submitted successfully.<br/>" +
                        "Total Estimated Amount: <strong>₹ " + String.format("%,.2f", amount) + "</strong><br/><br/>" +
                        "Our wedding reservation concierge is reviewing your request. You will receive an update once confirmed."
        );
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendBookingStatusEmail(String toEmail, String customerName, String bookingRef, String vehicleName, String status, String notes) {
        String subject = "Booking Update [" + status + "]: " + bookingRef;
        String statusColor = "CONFIRMED".equalsIgnoreCase(status) ? "#16a34a" : ("REJECTED".equalsIgnoreCase(status) ? "#dc2626" : "#2563eb");

        String htmlContent = buildHtmlTemplate(
                "Booking Status: " + status,
                "Hello " + customerName + ",<br/><br/>" +
                        "Your booking <strong>" + bookingRef + "</strong> for <strong>" + vehicleName + "</strong> has been updated to:<br/>" +
                        "<div style='display:inline-block;padding:6px 14px;background-color:" + statusColor + ";color:#ffffff;border-radius:6px;font-weight:bold;margin:10px 0;'>" + status + "</div><br/>" +
                        (notes != null && !notes.isBlank() ? "<em>Notes from Concierge: " + notes + "</em><br/><br/>" : "") +
                        "You can check your reservation details anytime on your customer dashboard."
        );
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendPaymentSuccessEmail(String toEmail, String customerName, String bookingRef, String vehicleName, BigDecimal amount, String paymentId) {
        String subject = "Payment Confirmed: " + bookingRef;
        String htmlContent = buildHtmlTemplate(
                "Payment Received Successfully",
                "Hello " + customerName + ",<br/><br/>" +
                        "We have received your payment for booking <strong>" + bookingRef + "</strong> (" + vehicleName + ").<br/>" +
                        "Amount Paid: <strong>₹ " + String.format("%,.2f", amount) + "</strong><br/>" +
                        "Transaction Reference: <code>" + paymentId + "</code><br/><br/>" +
                        "Your vehicle is securely locked in for your celebration. You can download your official PDF invoice from your booking details."
        );
        sendEmail(toEmail, subject, htmlContent);
    }

    private void sendEmail(String toEmail, String subject, String htmlBody) {
        if (!mailEnabled) {
            log.info("[NOTIFICATION LOG] (Dev Mode - SMTP disabled) To: {} | Subject: {} | Length: {} chars", toEmail, subject, htmlBody.length());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Email successfully dispatched to {}", toEmail);
        } catch (Exception e) {
            log.warn("Email delivery failed for {}: {}. Proceeding without breaking operations.", toEmail, e.getMessage());
        }
    }

    private String buildHtmlTemplate(String header, String bodyContent) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='utf-8'></head>" +
                "<body style='font-family:-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,sans-serif;background-color:#f8fafc;margin:0;padding:24px;'>" +
                "<div style='max-width:600px;margin:0 auto;background:#ffffff;border:1px solid #e2e8f0;border-radius:12px;overflow:hidden;box-shadow:0 4px 6px -1px rgba(0,0,0,0.05);'>" +
                "<div style='background:linear-gradient(135deg, #1e3a8a 0%, #2563eb 100%);padding:24px;text-align:center;color:#ffffff;'>" +
                "<h1 style='margin:0;font-size:22px;letter-spacing:-0.5px;'>VehicleSphere</h1>" +
                "<p style='margin:4px 0 0 0;font-size:12px;opacity:0.9;'>Premium Wedding & Special Event Rentals</p>" +
                "</div>" +
                "<div style='padding:28px 24px;color:#1e293b;line-height:1.6;font-size:14px;'>" +
                "<h2 style='margin-top:0;font-size:18px;color:#0f172a;'>" + header + "</h2>" +
                bodyContent +
                "</div>" +
                "<div style='background-color:#f1f5f9;padding:16px;text-align:center;font-size:11px;color:#64748b;border-top:1px solid #e2e8f0;'>" +
                "VehicleSphere &bull; Tejasvi, B.Tech CSE, Lovely Professional University &bull; tejasvijain09@gmail.com" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }
}
