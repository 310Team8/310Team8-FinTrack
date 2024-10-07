package org.vaadin.application.model;

import java.math.BigDecimal;
import java.awt.Color;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 * Represents the financial summary pdf.
 * Use this class to make changes to the resulting file.
 */
public class SummaryFile {
    public static final String INCOME_LABEL = "Income: ";
    public static final String EXPENSE_LABEL = "     ||Expense: ";
    public static final String NET_LABEL = "     ||Net: ";
    public static final String PDF_FAIL_STRING = "Failed to generate PDF: ";
    private PDDocument document;
    private PDPageContentStream contentStream;
    private float pageHeight;
    private float pageWidth;

    public SummaryFile() {
        try {
            PDPage page = new PDPage();
            document = new PDDocument();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
            pageHeight = page.getMediaBox().getHeight();
            pageWidth = page.getMediaBox().getWidth();
        } catch (IOException e) {
            System.out.println(PDF_FAIL_STRING + e.getMessage());
        }
    }

    /**
     * Adds provided strings to the current page of a document. Each string is a new line.
     * 
     * @param text string array containing lines of text
     * @param font font for all text in the array
     * @param fontSize font size for all text in the array
     * @param xPosition xPosition on the page to place the text (leftmost = 0)
     * @param yPosition yPosition on the page to place the text (bottommost = 0)
     * @param leading space between each line of text
     * @param color color of text
     */
    public void addLinesofText(
            String[] text,
            PDType1Font font,
            float fontSize,
            float xPosition,
            float yPosition,
            float leading, Color color) {
        try {
            contentStream.setNonStrokingColor(color);
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(xPosition, yPosition);
            contentStream.setLeading(leading);
            for (String line : text) {
                contentStream.showText(line);
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.moveTo(0, 0);
        } catch (IOException e) {
            System.out.println(PDF_FAIL_STRING + e.getMessage());
        }
    }

    /**
     * Retrieves the current PDF document.
     * 
     * @return the {@link PDDocument} representing the PDF document
     */
    public PDDocument getDocument() {
        return document;
    }

    /**
     * Closes the contentStream for the current page of the PDF document.
     */
    public void closeStream() {
        try {
            contentStream.close();
        } catch (IOException e) {
            System.out.println(PDF_FAIL_STRING + e.getMessage());
        }
    }

    /**
     * Retrieves the max height of the PDF page.
     * 
     * @return the height of the page as a float
     */
    public float getHeight() {
        return pageHeight;
    }

    /**
     * Retrieves the max width of the PDF page.
     * 
     * @return the width of the page as a float
     */
    public float getWidth() {
        return pageWidth;
    }

    /**
     * Creates a table containing all transaction and their basic information.
     * 
     * @param recordsQueue priority queue that sorts transactions based on date of transaction (latest to earliest)
     */
    public void addRecords(PriorityQueue<TransactionRecord> recordsQueue) {
        try {
            BigDecimal outgoingTotal = BigDecimal.ZERO;
            BigDecimal incomingTotal = BigDecimal.ZERO;
            float xPosition = 30;
            float yPosition = pageHeight - 342;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String[] header = { "CASH FLOW SUMMARY" };
            addLinesofText(
                    header,
                    new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),
                    24,
                    30,
                    pageHeight - 296,
                    14.5f, Color.BLACK);
            String[] columnHeaders = { "Date", "Description", "Outgoing", "Incoming", "Net" };
            addRow(columnHeaders, Color.BLUE, xPosition, yPosition + 16, Color.WHITE,
                    new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD));

            //Pops queue in order of latest to earliest and adds a row to the Cash Flow Summary for each transaction.
            while (!recordsQueue.isEmpty()) {
                //If the position is at the bottom of a page, overflow to the next.
                if (yPosition <= 54) {
                    PDPage newPage = new PDPage();
                    document.addPage(newPage);
                    contentStream.close();
                    contentStream = new PDPageContentStream(document, newPage);
                    yPosition = pageHeight - 54;
                }

                TransactionRecord transactionRecord = recordsQueue.poll();
                if (transactionRecord.getDetails() == null) {
                    transactionRecord.setDetails("");
                }
                String[] recordDetails = new String[5];
                if (transactionRecord.getType().equals("Income")) {
                    recordDetails[0] = transactionRecord.getTransactionDate().format(formatter);
                    recordDetails[1] = transactionRecord.getDetails();
                    recordDetails[2] = "";
                    recordDetails[3] = transactionRecord.getAmount().toString();
                    recordDetails[4] = "";
                    incomingTotal = incomingTotal.add(transactionRecord.getAmount());
                } else { //Transaction is an expense
                    recordDetails[0] = transactionRecord.getTransactionDate().format(formatter);
                    recordDetails[1] = transactionRecord.getDetails();
                    recordDetails[2] = transactionRecord.getAmount().toString();
                    recordDetails[3] = "";
                    recordDetails[4] = "";
                    outgoingTotal = outgoingTotal.add(transactionRecord.getAmount());
                }
                addRow(recordDetails, Color.LIGHT_GRAY, xPosition, yPosition, Color.BLACK,
                        new PDType1Font(Standard14Fonts.FontName.COURIER));
                yPosition -= 16;
            }
            
            //Summarises table information into totals and net cashflow.
            String[] lastRow = { "", "", outgoingTotal.toString(), incomingTotal.toString(),
                    incomingTotal.subtract(outgoingTotal).toString() };
            addRow(lastRow, Color.LIGHT_GRAY, xPosition, yPosition, Color.BLACK,
                    new PDType1Font(Standard14Fonts.FontName.COURIER));
        } catch (IOException e) {
            System.out.println(PDF_FAIL_STRING + e.getMessage());
        }
    }

    /**
     * Adds a row to the cash flow summary table.
     * 
     * @param inputs string array containing the text for each of the five columns
     * @param bgColor background colour of row
     * @param xPosition xPosition of the row on the current page
     * @param yPosition xPosition of the row on the current page
     * @param textColor colour of the text within the row
     * @param font font of the text within the row
     */
    private void addRow(String[] inputs, Color bgColor, float xPosition, float yPosition, Color textColor,
            PDType1Font font) {
        int[] colWidths = { 90, 200, 80, 80, 80 };
        int cellHeight = 14;
        float xPos = xPosition;
        try {
            for (int i = 0; i < colWidths.length; i++) {
                String[] header = { inputs[i] };
                contentStream.addRect(xPos, yPosition, colWidths[i], cellHeight);
                contentStream.setNonStrokingColor(bgColor);
                contentStream.fill();
                addLinesofText(header, font, 12, xPos + 2, yPosition + 4, 14.5f, textColor);
                xPos += colWidths[i] + 1;
            }
            contentStream.moveTo(0, 0);
        } catch (IOException e) {
            System.out.println(PDF_FAIL_STRING + e.getMessage());
        }
    }
}
