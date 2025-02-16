package Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SupplierEntity {
    private int id;
    private String name;
    private String company;
    private String email;
    private String item;


}
