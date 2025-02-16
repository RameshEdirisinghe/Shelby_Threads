package service.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CartDetails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import repository.DaoFactory;
import repository.custom.OrderDao;
import service.custom.CartService;
import service.custom.EmployeeService;
import service.custom.OrderService;
import util.DaoType;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartServiceImpl implements CartService {

    @Override
    public void printPDF(ObservableList<CartDetails> cartItems, Double total, String customerName) throws IOException, PrinterException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Draw border
        contentStream.setLineWidth(2);
        contentStream.moveTo(50, 800);
        contentStream.lineTo(550, 800);
        contentStream.lineTo(550, 50);
        contentStream.lineTo(50, 50);
        contentStream.lineTo(50, 800);
        contentStream.stroke();

        // Shop Name
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.beginText();
        contentStream.newLineAtOffset(200, 770);
        contentStream.showText("Shelby Threads");
        contentStream.endText();

        // Date and Time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = formatter.format(new Date());
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(70, 740);
        contentStream.showText("Date: " + dateTime);
        contentStream.endText();

        // Customer Name
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.newLineAtOffset(70, 710);
        contentStream.showText("Customer: " + customerName);
        contentStream.endText();

        // Table Headers
        contentStream.beginText();
        contentStream.newLineAtOffset(70, 680);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Item                     Qty    Price     Total");
        contentStream.endText();

        int yPosition = 660;
        double totalAmount = 0;
        for (CartDetails product : cartItems) {
            double total1 = product.getQty() * product.getPrice();
            totalAmount += total1;

            contentStream.beginText();
            contentStream.newLineAtOffset(70, yPosition);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText(String.format("%-20s %5d  $%6.2f  $%6.2f", product.getProductName(), product.getQty(), product.getPrice(), total1));
            contentStream.endText();

            yPosition -= 20;
        }

        // Total Bill Amount
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.newLineAtOffset(70, yPosition - 30);
        contentStream.showText("Total Bill: $" + String.format("%.2f", total));
        contentStream.endText();

        contentStream.close();

        String filePath = "ShelbyThreads_Bill.pdf";
        document.save(filePath);

       document = PDDocument.load(new File(filePath));

        // Get the default printer
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService != null) {
            // Create a printer job
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(printService);

            // Wrap the PDDocument in a PDFPrintable object
            PDFPrintable printable = new PDFPrintable(document, Scaling.SHRINK_TO_FIT);

            // Set the printable to the PDF document
            job.setPrintable(printable);

            // Print the document
            job.print();
            System.out.println("PDF printed successfully.");
        } else {
            System.out.println("No printer found.");
        }

        // Close the document
        document.close();
    }

    @Override
    public ObservableList<CartDetails> setCartArray(ArrayList<CartDetails> cartArray) {
        ObservableList<CartDetails> cartItems = FXCollections.observableArrayList();
        cartArray.forEach(cartItem ->{
            cartItems.add(cartItem);
        });

        return cartItems;
    }

    @Override
    public Double setTotal(ObservableList<CartDetails> cartItems) {
        Double total=0.0;
        for(CartDetails item : cartItems){
            total += item.getPrice()* item.getQty();
        }
        return total;
    }

    @Override
    public ObservableList<String> getCmbPaymentItems() {
        ObservableList<String> cat = FXCollections.observableArrayList() ;
        cat.add("Cash");
        cat.add("Card");
        cat.add("Online");
        return cat;
    }

    @Override
    public ObservableList<Integer> getCmbEmployeeIds() throws SQLException {
        ObservableList<Integer> empIds = FXCollections.observableArrayList();
        List<Integer> Ids = new EmployeeServiceImpl().getEmpIds();

        for(Integer id : Ids){
            empIds.add(id);
        }
        return empIds;
    }

    @Override
    public String getOrderId() throws SQLException {
       OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        return orderDao.getOrderId();

    }
}
