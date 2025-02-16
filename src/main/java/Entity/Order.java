package model;

import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int orderId;
    private String customerName;
    private ObservableList<CartDetails> cartItems;
    private Double Total;
    private String paymenType;
    private int employeeId;

}
