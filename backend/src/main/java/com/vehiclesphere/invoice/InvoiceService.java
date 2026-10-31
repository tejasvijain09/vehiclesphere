package com.vehiclesphere.invoice;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.vehiclesphere.booking.Booking;
import com.vehiclesphere.booking.BookingRepository;
import com.vehiclesphere.exception.ResourceNotFoundException;
import com.vehiclesphere.exception.UnauthorizedException;
import com.vehiclesphere.payment.Payment;
import com.vehiclesphere.payment.PaymentRepository;
import com.vehiclesphere.payment.PaymentStatus;
import com.vehiclesphere.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public byte[] generateInvoicePdf(Long bookingId, Long userId, Role userRole) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (userRole != Role.ADMIN && !booking.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to download this invoice");
        }

        List<Payment> payments = paymentRepository.findByBookingIdOrderByCreatedAtDesc(bookingId);
        Payment successfulPayment = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .findFirst()
                .orElse(null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Color Palette
            Color navyPrimary = new Color(30, 58, 138); // #1E3A8A
            Color slateDark = new Color(15, 23, 42); // #0F172A
            Color slateMuted = new Color(100, 116, 139); // #64748B
            Color emeraldGreen = new Color(22, 163, 74); // #16A34A
            Color amberWarning = new Color(217, 119, 6); // #D97706
            Color tableBg = new Color(248, 250, 252); // #F8FAFC

            // Fonts
            Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, navyPrimary);
            Font brandSubFont = FontFactory.getFont(FontFactory.HELVETICA, 10, slateMuted);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, slateDark);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, slateDark);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, slateDark);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, slateDark);
            Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 9, slateMuted);

            // 1. Header Banner Table
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{60, 40});

            PdfPCell leftHeader = new PdfPCell();
            leftHeader.setBorder(Rectangle.NO_BORDER);
            leftHeader.addElement(new Paragraph("VehicleSphere", brandFont));
            leftHeader.addElement(new Paragraph("Premium Wedding & Event Vehicle Rentals", brandSubFont));
            leftHeader.addElement(new Paragraph("Lovely Professional University, Punjab &bull; tejasvijain09@gmail.com", smallFont));
            headerTable.addCell(leftHeader);

            PdfPCell rightHeader = new PdfPCell();
            rightHeader.setBorder(Rectangle.NO_BORDER);
            rightHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
            Paragraph invTitle = new Paragraph("TAX INVOICE", titleFont);
            invTitle.setAlignment(Element.ALIGN_RIGHT);
            rightHeader.addElement(invTitle);

            String invoiceNo = "INV-" + booking.getBookingReference().replace("VSB-", "");
            Paragraph invNumPara = new Paragraph("Invoice No: " + invoiceNo, boldFont);
            invNumPara.setAlignment(Element.ALIGN_RIGHT);
            rightHeader.addElement(invNumPara);

            String dateStr = booking.getCreatedAt() != null ?
                    booking.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) : "N/A";
            Paragraph invDatePara = new Paragraph("Date: " + dateStr, smallFont);
            invDatePara.setAlignment(Element.ALIGN_RIGHT);
            rightHeader.addElement(invDatePara);
            headerTable.addCell(rightHeader);

            document.add(headerTable);

            // Separator
            document.add(new Chunk(new LineSeparator(1, 100, slateMuted, Element.ALIGN_CENTER, -6)));
            document.add(new Paragraph(" "));

            // 2. Customer & Booking Details Table
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setWidths(new float[]{50, 50});

            PdfPCell custCell = new PdfPCell();
            custCell.setBorder(Rectangle.NO_BORDER);
            custCell.addElement(new Paragraph("BILLED TO:", headerFont));
            custCell.addElement(new Paragraph(booking.getUser().getFullName(), boldFont));
            custCell.addElement(new Paragraph("Email: " + booking.getUser().getEmail(), normalFont));
            custCell.addElement(new Paragraph("Phone: " + booking.getUser().getPhoneNumber(), normalFont));
            custCell.addElement(new Paragraph("Pickup Location: " + booking.getPickupLocation(), normalFont));
            detailsTable.addCell(custCell);

            PdfPCell bookCell = new PdfPCell();
            bookCell.setBorder(Rectangle.NO_BORDER);
            bookCell.addElement(new Paragraph("RESERVATION DETAILS:", headerFont));
            bookCell.addElement(new Paragraph("Booking Reference: " + booking.getBookingReference(), boldFont));
            bookCell.addElement(new Paragraph("Booking Status: " + booking.getStatus(), normalFont));

            String payStatus = booking.getPaymentStatus() != null ? booking.getPaymentStatus() : "UNPAID";
            Font payFont = "PAID".equalsIgnoreCase(payStatus) ?
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, emeraldGreen) :
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, amberWarning);
            bookCell.addElement(new Paragraph("Payment Status: " + payStatus, payFont));

            if (successfulPayment != null) {
                bookCell.addElement(new Paragraph("Transaction ID: " + successfulPayment.getRazorpayPaymentId(), smallFont));
            }
            detailsTable.addCell(bookCell);

            document.add(detailsTable);
            document.add(new Paragraph(" "));

            // 3. Vehicle & Pricing Breakdown Table
            PdfPTable itemsTable = new PdfPTable(5);
            itemsTable.setWidthPercentage(100);
            itemsTable.setWidths(new float[]{35, 20, 15, 15, 15});

            // Table Header Row
            String[] headers = {"Vehicle Description", "Rental Period", "Daily Rate", "Days", "Total Amount"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(tableBg);
                cell.setPadding(8);
                cell.setBorderColor(new Color(226, 232, 240));
                itemsTable.addCell(cell);
            }

            // Table Item Row
            String vehicleDesc = booking.getVehicle().getName() + " (" + booking.getVehicle().getRegistrationNumber() + ")";
            PdfPCell descCell = new PdfPCell(new Phrase(vehicleDesc, boldFont));
            descCell.setPadding(8);
            descCell.setBorderColor(new Color(226, 232, 240));
            itemsTable.addCell(descCell);

            String dates = booking.getPickupDate().format(DateTimeFormatter.ofPattern("dd/MM/yy")) + " - " +
                    booking.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
            PdfPCell datesCell = new PdfPCell(new Phrase(dates, normalFont));
            datesCell.setPadding(8);
            datesCell.setBorderColor(new Color(226, 232, 240));
            itemsTable.addCell(datesCell);

            PdfPCell rateCell = new PdfPCell(new Phrase("INR " + String.format("%,.2f", booking.getPricePerDay()), normalFont));
            rateCell.setPadding(8);
            rateCell.setBorderColor(new Color(226, 232, 240));
            itemsTable.addCell(rateCell);

            PdfPCell daysCell = new PdfPCell(new Phrase(String.valueOf(booking.getNumberOfDays()), normalFont));
            daysCell.setPadding(8);
            daysCell.setBorderColor(new Color(226, 232, 240));
            itemsTable.addCell(daysCell);

            PdfPCell totalCell = new PdfPCell(new Phrase("INR " + String.format("%,.2f", booking.getTotalAmount()), boldFont));
            totalCell.setPadding(8);
            totalCell.setBorderColor(new Color(226, 232, 240));
            itemsTable.addCell(totalCell);

            document.add(itemsTable);
            document.add(new Paragraph(" "));

            // 4. Summary & Grand Total
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100);
            summaryTable.setWidths(new float[]{60, 40});

            PdfPCell notesCell = new PdfPCell();
            notesCell.setBorder(Rectangle.NO_BORDER);
            notesCell.addElement(new Paragraph("Special Wedding Notes:", boldFont));
            notesCell.addElement(new Paragraph(booking.getNotes() != null && !booking.getNotes().isBlank() ?
                    booking.getNotes() : "Vehicle sanitized, floral-bonnet preparation ready upon delivery.", smallFont));
            summaryTable.addCell(notesCell);

            PdfPCell totalSummaryCell = new PdfPCell();
            totalSummaryCell.setBorder(Rectangle.NO_BORDER);
            Paragraph grandTotal = new Paragraph("Grand Total: INR " + String.format("%,.2f", booking.getTotalAmount()),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, navyPrimary));
            grandTotal.setAlignment(Element.ALIGN_RIGHT);
            totalSummaryCell.addElement(grandTotal);
            summaryTable.addCell(totalSummaryCell);

            document.add(summaryTable);

            // 5. Footer Sign-off
            document.add(new Paragraph(" "));
            document.add(new Chunk(new LineSeparator(1, 100, slateMuted, Element.ALIGN_CENTER, -6)));
            Paragraph footer = new Paragraph("Thank you for choosing VehicleSphere for your auspicious celebrations. This is a computer-generated tax invoice and requires no physical signature.", smallFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate PDF invoice for booking " + bookingId, e);
            throw new RuntimeException("Error generating PDF rental invoice: " + e.getMessage());
        }
    }
}
