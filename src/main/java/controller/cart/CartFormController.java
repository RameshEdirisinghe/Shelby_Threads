package controller.cart;

import com.google.inject.Inject;
import controller.home.EmployeeDashboardFormController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.CartDetails;
import model.Order;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import service.ServiceFactory;
import service.custom.CartService;
import service.custom.OrderService;
import service.custom.impl.CartServiceImpl;
import util.ServiceType;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;



public class CartController implements Initializable {

    private static ObservableList<CartDetails> cartItems;
    public ComboBox cmbPaymentType;
    public ComboBox cmbEmployeeId;
    public Label lblOrderId;
    Double total;
    private static CartController instance;




    @Inject
    private OrderService orderService;

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

    @Inject
    private CartService service;





    @FXML
    void btnOnClickActionPrintBill(ActionEvent event) {

        try {


            boolean isPlaced =  orderService.placeOrder(new Order(Integer.parseInt(lblOrderId.getText()),txtFieldCustomerName.getText(),cartItems,Double.parseDouble(lnlTotal.getText()),cmbPaymentType.getSelectionModel().getSelectedItem().toString(),Integer.parseInt(cmbEmployeeId.getSelectionModel().getSelectedItem().toString())));
            if (isPlaced){

                new Alert(Alert.AlertType.INFORMATION,"Order Placed Successfully Placed collect your Bill ").show();
                service.printPDF(cartItems,total,txtFieldCustomerName.getText());
                cartItems.clear();

                new EmployeeDashboardFormController().setCartZero();
                new EmployeeDashboardFormController().cartArray.clear();
                Stage currentStage = (Stage) txtFieldCustomerName.getScene().getWindow();
                if (currentStage != null){
                    currentStage.close();
                }


            }else{
                new Alert(Alert.AlertType.INFORMATION,"Order Not Placed please try Again").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setCart(ArrayList<CartDetails> cartArray){
        CartService cartService =ServiceFactory.getInstance().getServiceType(ServiceType.CART);
        cartItems = cartService.setCartArray(cartArray);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProductQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        try {
            lblOrderId.setText(  service.getOrderId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        loadCmb();
        loadTable();

    }
    public void loadCmb(){
        ObservableList<String> paymentTypes = service.getCmbPaymentItems();
        cmbPaymentType.setItems(paymentTypes);


            ObservableList<Integer> employeeId = service.getCmbEmployeeIds();
            cmbEmployeeId.setItems(employeeId);

    }

    public void loadTable(){
        tblCart.setItems(cartItems);
        total=service.setTotal(cartItems);
        lnlTotal.setText(total.toString());
    }
}
