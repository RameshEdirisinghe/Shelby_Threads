package service.custom;

import javafx.collections.ObservableList;
import model.CartDetails;
import service.SuperService;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CartService extends SuperService {
    void printPDF(ObservableList<CartDetails> cartItems,Double total,String customerName) throws IOException, PrinterException;

    ObservableList<CartDetails> setCartArray(ArrayList<CartDetails> cartArray);

    Double setTotal(ObservableList<CartDetails> cartItems);

    ObservableList<String> getCmbPaymentItems();

    ObservableList<Integer> getCmbEmployeeIds();

    String getOrderId() throws SQLException;
}
