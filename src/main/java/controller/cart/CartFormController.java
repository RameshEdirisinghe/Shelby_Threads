package controller.cart;

import com.google.inject.Inject;
import controller.home.EmployeeDashboardFormController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CartDetails;
import model.Order;
import service.ServiceFactory;
import service.custom.CartService;
import service.custom.OrderService;
import util.ServiceType;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class CartFormController implements Initializable {

    private static ObservableList<CartDetails> cartItems;
    public ComboBox cmbPaymentType;
    public ComboBox cmbEmployeeId;
    public Label lblOrderId;
    public TableColumn colCartProductName;
    public TableColumn colCartProductPrice;
    Double total;

    @FXML
    private TableColumn colProductQty;

    @FXML
    private Label lnlTotal;

    @FXML
    private TableView tblCart;

    @FXML
    private TextField txtFieldCustomerName;

    @Inject
    private OrderService orderService;

    @Inject
    private CartService service;

    EmployeeDashboardFormController employeeDashboardFormController = new EmployeeDashboardFormController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCartProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCartProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProductQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        lblOrderId.setText(service.getOrderId());
        loadCmb();
        loadTable();
    }

    @FXML
    void btnOnClickActionCancel(ActionEvent event) {
        employeeDashboardFormController.setCartZero();
        cartItems.clear();
        loadTable();
    }

    public static void setCart(ArrayList<CartDetails> cartArray) {
        CartService cartService = ServiceFactory.getInstance().getServiceType(ServiceType.CART);
        cartItems = cartService.setCartArray(cartArray);
    }

    @FXML
    void btnOnClickActionPrintBill(ActionEvent event) {
        boolean isPlaced = orderService.placeOrder(new Order(Integer.parseInt(lblOrderId.getText()), txtFieldCustomerName.getText(), cartItems, Double.parseDouble(lnlTotal.getText()), cmbPaymentType.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(cmbEmployeeId.getSelectionModel().getSelectedItem().toString())));
        try {
            if (isPlaced) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully Placed collect your Bill ").show();
                service.printPDF(cartItems, total, txtFieldCustomerName.getText());
                cartItems.clear();
                employeeDashboardFormController.setCartZero();
                cartItems.clear();
                loadTable();
                Stage currentStage = (Stage) txtFieldCustomerName.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close();
                }
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Order Not Placed please try Again").show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCmb() {
        ObservableList<String> paymentTypes = service.getCmbPaymentItems();
        cmbPaymentType.setItems(paymentTypes);
        ObservableList<Integer> employeeId = service.getCmbEmployeeIds();
        cmbEmployeeId.setItems(employeeId);

    }

    public void loadTable() {
        tblCart.setItems(cartItems);
        total = service.setTotal(cartItems);
        lnlTotal.setText(total.toString());
    }
}
