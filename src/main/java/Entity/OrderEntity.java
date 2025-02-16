package Entity;

import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    private int orderId;
    private String customerName;
    private ObservableList<CartDetailsEntity> cartItems;
    private Double Total;
    private String paymenType;
    private int employeeId;

}
