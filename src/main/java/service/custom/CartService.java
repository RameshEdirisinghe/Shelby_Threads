package service.custom;

import javafx.collections.ObservableList;
import model.CartDetails;
import service.SuperService;

import java.awt.print.PrinterException;
import java.io.IOException;

public interface CartService extends SuperService {
    void printPDF(ObservableList<CartDetails> cartItems,Double total,String customerName) throws IOException, PrinterException;
}
