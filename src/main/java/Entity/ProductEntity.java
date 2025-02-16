package Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductEntity {

    private int id;
    private String name;
    private String category;
    private String size;
    private Double price;
    private int qty;
    private String imgPath;
    private String Suppler;
}
