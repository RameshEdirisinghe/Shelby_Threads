package controller.home;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import controller.cart.CartController;
import controller.products.MyListener;
import controller.products.ProductFormController;
import controller.products.ProductsController;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CartDetails;
import model.Product;
import service.custom.EmployeeService;
import util.AppModule;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeDashboardFormController implements Initializable {


    public ComboBox cmbQty;

    @FXML
    public TextField txtFieldQty;
    public Label lblCartNumber;
    @FXML
    private VBox chosenFruitCard;

    @FXML
    private ComboBox<?> cmbProductCategory;

    @FXML
    private ComboBox<?> cmbProductSize;

    @FXML
    private ComboBox<?> cmbProductSupplier;

    @FXML
    private TableColumn<?, ?> colEmpCompany;

    @FXML
    private TableColumn<?, ?> colEmpEmail;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colEmpName;

    @FXML
    private ImageView ProductImg;

    @FXML
    private Label productNameLable;

    @FXML
    private Label productPriceLabel;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView imgViewUpImg;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TableView<?> tblEmployee;

    @FXML
    private TextField txtEmpCompany;

    @FXML
    private TextField txtEmpEmail;

    @FXML
    private TextField txtEmpName;

    @FXML
    private TextField txtEmpPassword;

    @FXML
    private TextField txtProductId;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtProductPrice;

    @FXML
    private TextField txtProductQty;

    @FXML
    private TextField txtSearchEmpId;
    private List<Product> products = new ArrayList();
    private Image image;
    private MyListener myListener;
    int cartCount =0;
     public  ArrayList<CartDetails> cartArray = new ArrayList<>();

    @Inject
    private EmployeeService service;

    @FXML
    void btnOnActionImportImg(ActionEvent event) {

    }

    @FXML
    void btnOnClickActionAdd(ActionEvent event) {

    }

    @FXML
    void btnOnClickActionEmpAdd(ActionEvent event) {

    }

    @FXML
    void btnOnClickActionEmpDelete(ActionEvent event) {

    }

    @FXML
    void btnOnClickActionEmpSearch(ActionEvent event) {

    }

    @FXML
    void btnOnClickActionEmpUpdate(ActionEvent event) {

    }

    public void setCartZero(){



    }


    private void setChosenProduct(Product product) {
        this.productNameLable.setText(product.getName());
        this.productPriceLabel.setText("$" + product.getPrice());
        this.image = new Image(product.getImgPath());
        this.ProductImg.setImage(this.image);
    }

    private List<Product> getData() {
        List<Product> product = new ArrayList();
        for(Product p:ProductsController.getInstance().getAllProduct()){
            product.add(p);
        }
        return product;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.products.addAll(this.getData());
        lblCartNumber.setText(cartCount+"");
        if (this.products.size() > 0) {
            this.setChosenProduct(this.products.get(0));
            this.myListener = new MyListener() {
                @Override
                public void onClickListener(Product product) {
                    setChosenProduct(product);
                }
            };

        }



        int column = 0;
        int row = 1;

        try {
            for(int i = 0; i < this.products.size(); ++i) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(this.getClass().getResource("/view/product.fxml"));
                AnchorPane anchorPane = (AnchorPane)fxmlLoader.load();
                ProductFormController itemController = (ProductFormController)fxmlLoader.getController();
                itemController.setData(this.products.get(i), this.myListener);
                if (column == 4) {
                    column = 0;
                    ++row;
                }
                this.grid.add(anchorPane, column++, row);
                this.grid.setMinWidth(-1.0);
                this.grid.setPrefWidth(-1.0);
                this.grid.setMaxWidth(Double.NEGATIVE_INFINITY);
                this.grid.setMinHeight(-1.0);
                this.grid.setPrefHeight(-1.0);
                this.grid.setMaxHeight(Double.NEGATIVE_INFINITY);
                GridPane.setMargin(anchorPane, new Insets(10.0));
            }
        } catch (IOException var9) {
            var9.printStackTrace();
        }
    }

    public void addToCart(MouseEvent mouseEvent) {

        if(txtFieldQty.getText()==null || Integer.parseInt(txtFieldQty.getText())>ProductsController.getInstance().getProduct(productNameLable.getText()).getQty()){
            new Alert(Alert.AlertType.INFORMATION,"Please enter less qty").show();
        }
        cartCount++;
        String price =productPriceLabel.getText();
        int index =  cartArray.indexOf(new CartDetails(productNameLable.getText(),Double.parseDouble(price.substring(1)),1));

        if(index!=-1){
            CartDetails alreadyExiteCartItem = cartArray.get(index);
            cartArray.set(index,new CartDetails(productNameLable.getText(),Double.parseDouble(price.substring(1)),alreadyExiteCartItem.getQty()+Integer.parseInt(txtFieldQty.getText())) );
        }else{
            cartArray.add(new CartDetails(productNameLable.getText(),Double.parseDouble(price.substring(1)),Integer.parseInt(txtFieldQty.getText())));
        }

        lblCartNumber.setText(cartCount+"");
    }

    public void btnOnClickActionAddToCart(ActionEvent actionEvent) {

    }

    public void onClickViewCart(MouseEvent mouseEvent) {
        CartController.getInstance().setCart(cartArray);
        Stage stage = new Stage();
        try {
            Injector injector = Guice.createInjector(new AppModule());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cart.fxml"));
            loader.setControllerFactory(injector::getInstance);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
