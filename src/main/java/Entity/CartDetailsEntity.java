package Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartDetailsEntity {

    private String productName;
    private Double price;
    private int qty;

    @Override
    public boolean equals(Object o) {
        CartDetailsEntity cart = (CartDetailsEntity) o;
        if (this.productName == cart.getProductName() ) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, price, qty);
    }
}
