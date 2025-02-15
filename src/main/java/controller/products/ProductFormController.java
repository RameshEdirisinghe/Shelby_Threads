package controller.products;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Product;

public class ProductFormController {

    public Label priceLable;
    public ImageView img;


    @FXML
    private Label nameLabel;



    @FXML
    void click(MouseEvent mouseEvent) {
        if (myListener != null && product != null) {
            myListener.onClickListener(product); // Ensures both listener and product are valid
        }
    }

    public static final String CURRENCY = "$";
    private Product product;
    private MyListener myListener;


    public void setData(Product product, MyListener myListener) {
        this.product = product;
        this.myListener = myListener;

        nameLabel.setText(product.getName());
        priceLable.setText(CURRENCY + product.getPrice());

        Image image = new Image(product.getImgPath());
        img.setImage(image);
    }
}
