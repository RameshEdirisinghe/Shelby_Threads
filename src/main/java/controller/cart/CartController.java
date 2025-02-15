package controller.cart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CartDetails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import service.custom.CartService;
import service.custom.impl.CartServiceImpl;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    private static ObservableList<CartDetails> cartItems;
    Double total;

    @FXML
    private TableColumn colProductName;

    @FXML
    private TableColumn colProductPrice;

    @FXML
    private TableColumn colProductQty;

    @FXML
    private Label lnlTotal;

    @FXML
    private TableView tblCart;

    @FXML
    private TextField txtFieldCustomerName;

    @FXML
    void btnOnClickActionCancel(ActionEvent event) {

    }

    @FXML
    void btnOnClickActionPrintBill(ActionEvent event) {
        try {
            CartService cart = new CartServiceImpl();
            cart.printPDF(cartItems,total,txtFieldCustomerName.getText());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void printPDF(String filePath) {
//        try {
//            // Load the PDF file
//            PDDocument document = PDDocument.load(new File(filePath));
//
//            // Get the default printer
//            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
//            if (printService != null) {
//                // Create a printer job
//                PrinterJob job = PrinterJob.getPrinterJob();
//                job.setPrintService(printService);
//
//                // Wrap the PDDocument in a PDFPrintable object
//                PDFPrintable printable = new PDFPrintable(document, Scaling.SHRINK_TO_FIT);
//
//                // Set the printable to the PDF document
//                job.setPrintable(printable);
//
//                // Print the document
//                job.print();
//                System.out.println("PDF printed successfully.");
//            } else {
//                System.out.println("No printer found.");
//            }
//
//            // Close the document
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void setCartArray(ArrayList<CartDetails> cartArray){
        cartItems = FXCollections.observableArrayList();
        cartArray.forEach(cartItem ->{
            cartItems.add(cartItem);
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProductQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        loadTable();
    }

    public void loadTable(){
        tblCart.setItems(cartItems);
        total=0.0;

       for(CartDetails item : cartItems){
            total += item.getPrice()* item.getQty();
        }
        lnlTotal.setText(total.toString());
    }
}
