package controller.home;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import controller.cart.CartFormController;
import controller.products.MyListener;
import controller.products.ProductFormController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CartDetails;
import model.Product;
import model.Supplier;
import service.custom.*;
import util.AppModule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeDashboardFormController implements Initializable {

    @FXML
    public TextField txtFieldQty;
    public Label lblCartNumber;
    public TableColumn colSupplierItem;
    public TableColumn colSupilerEmail;
    public TableColumn colSupplierCompany;
    public TableColumn colSupplierName;
    public TableColumn colSupplierId;
    public TableView tblSupplier;
    public ComboBox cmbSupplierProduct;
    public TextField txtSupplierEmail;
    public TextField txtSupplierCompany;
    public TextField txtSupplierName;
    public TextField txtSearchSupplierId;
    public TableColumn colProductStock;
    public TableColumn colProductPrice;
    public TableColumn colProductSize;
    public TableColumn colProductSupplierName;
    public TableColumn colProductCategory;
    public TableColumn colProductName;
    public TableColumn colProductId;
    public TableView tblInventory;

    @FXML
    private ComboBox cmbProductCategory;

    @FXML
    private ComboBox cmbProductSize;

    @FXML
    private ComboBox cmbProductSupplier;

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
    private TextField txtProductId;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtProductPrice;

    @FXML
    private TextField txtProductQty;

    private List<Product> products = new ArrayList();
    private Image image;
    private MyListener myListener;
    int cartCount = 0;
    public ArrayList<CartDetails> cartArray = new ArrayList<>();
    private String currentImagePath;

    @Inject
    private EmployeeService service;

    @Inject
    CustomerService customerService;

    @Inject
    SupplierService supplierService;

    @Inject
    OrderService orderService;

    @Inject
    ProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set Tables Cols-Supplier
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupplierCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colSupilerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSupplierItem.setCellValueFactory(new PropertyValueFactory<>("item"));

        //set Tables Cols-Product
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colProductSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colProductStock.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProductSupplierName.setCellValueFactory(new PropertyValueFactory<>("Suppler"));

        loadcatalogue();
        loadTables();

        reloadCmbSupplierItem();
        reloadCmbCat();
        reloadCmbSupplier();
        reloadCmbsize();

    }

    private void loadcatalogue() {
        this.products.addAll(this.getData());
        lblCartNumber.setText(cartCount + "");
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
            for (int i = 0; i < this.products.size(); ++i) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(this.getClass().getResource("/view/productForm.fxml"));
                AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
                ProductFormController itemController = (ProductFormController) fxmlLoader.getController();
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

    private void setChosenProduct(Product product) {
        this.productNameLable.setText(product.getName());
        this.productPriceLabel.setText("$" + product.getPrice());
        this.image = new Image(product.getImgPath());
        this.ProductImg.setImage(this.image);
    }

    private List<Product> getData() {
        List<Product> product = new ArrayList();
        for (Product p : productService.getAll()) {
            product.add(p);
        }
        return product;
    }

    public void loadTables() {

        ObservableList<Supplier> supplierObservableList = FXCollections.observableArrayList();
        for (Supplier supplier : supplierService.getAll()) {
            supplierObservableList.add(supplier);
        }
        tblSupplier.setItems(supplierObservableList);

        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        for (Product product : productService.getAll()) {
            productObservableList.add(product);
        }
        tblInventory.setItems(productObservableList);
    }

    //load comboBox
    public void reloadCmbSupplierItem() {
        ObservableList<String> supItem = FXCollections.observableArrayList();
        supItem.add("Full Suits");
        supItem.add("Jackets");
        supItem.add("Trousers");
        supItem.add("Cap/Hats");
        supItem.add("Watch");

        cmbSupplierProduct.setItems(supItem);
    }

    public void reloadCmbCat() {
        ObservableList<String> cat = FXCollections.observableArrayList();
        cat.add("Ladies");
        cat.add("Gents");
        cat.add("Kids");

        cmbProductCategory.setItems(cat);
    }

    public void reloadCmbSupplier() {
        ObservableList<String> sup = FXCollections.observableArrayList();
        for (String supplierName : supplierService.getAllSupplierNames()) {
            sup.add(supplierName);
        }
        cmbProductSupplier.setItems(sup);
    }

    public void reloadCmbsize() {
        ObservableList<String> size = FXCollections.observableArrayList();
        size.add("Small");
        size.add("Medium");
        size.add("Large");
        size.add("XL");
        size.add("XXL");

        cmbProductSize.setItems(size);
    }

    public void addToCart(MouseEvent mouseEvent) {

        if (txtFieldQty.getText() == null || Integer.parseInt(txtFieldQty.getText()) > productService.getProduct(productNameLable.getText()).getQty()) {
            new Alert(Alert.AlertType.INFORMATION, "Please enter less qty").show();
        } else {
            cartCount++;
            String price = productPriceLabel.getText();
            int index = cartArray.indexOf(new CartDetails(productNameLable.getText(), Double.parseDouble(price.substring(1)), 1));

            if (index != -1) {
                CartDetails alreadyExiteCartItem = cartArray.get(index);
                cartArray.set(index, new CartDetails(productNameLable.getText(), Double.parseDouble(price.substring(1)), alreadyExiteCartItem.getQty() + Integer.parseInt(txtFieldQty.getText())));
            } else {
                cartArray.add(new CartDetails(productNameLable.getText(), Double.parseDouble(price.substring(1)), Integer.parseInt(txtFieldQty.getText())));
            }
            lblCartNumber.setText(cartCount + "");
        }

    }


    @FXML
    void btnOnActionImportImg(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter exxtFilterJpg = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter exxtFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(exxtFilterJpg, exxtFilterPNG);

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            currentImagePath = file.getAbsolutePath();
            Image image = new Image(currentImagePath);
            imgViewUpImg.setImage(image);
        }
    }


    public void setCartZero() {
        cartCount=0;
        cartArray.clear();
        System.out.println(cartArray);
    }

    //productCrud
    public void btnonClickActionAdd(ActionEvent actionEvent) {
        if (productService.addProducts(new Product(0, txtProductName.getText(), cmbProductCategory.getSelectionModel().getSelectedItem().toString(), cmbProductSize.getSelectionModel().getSelectedItem().toString(), Double.parseDouble(txtProductPrice.getText()), Integer.parseInt(txtProductQty.getText()), currentImagePath, cmbProductSupplier.getSelectionModel().getSelectedItem().toString()))) {
            new Alert(Alert.AlertType.INFORMATION, "Employee Added successfully").show();
            loadcatalogue();
            loadTables();
            txtProductName.setText("");
            txtProductPrice.setText("");
            txtProductQty.setText("");
            imgViewUpImg.setImage(null);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Employee Added Fail").show();
        }
    }

    public void btnOnClickActionProductSearch(ActionEvent actionEvent) {

        Product product = productService.searchProduct(Integer.parseInt(txtProductId.getText()));
        if (product == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please Enter Valid Product Id").show();
        } else {
            txtProductName.setText(product.getName());
            txtProductPrice.setText(product.getPrice() + "");
            txtProductQty.setText(product.getQty() + "");
            Image img = new Image(product.getImgPath());
            imgViewUpImg.setImage(img);
            cmbProductCategory.setValue(product.getCategory());
            cmbProductSize.setValue(product.getSize());
            cmbProductSupplier.setValue(product.getSuppler());

        }
    }

    public void btnOnClickActionProductDelete(ActionEvent actionEvent) {
        if (txtProductId.getText() != null) {
            if (productService.deleteProduct(Integer.parseInt(txtProductId.getText()))) {
                loadTables();
                loadcatalogue();
                txtProductName.setText("");
                txtProductPrice.setText("");
                txtProductQty.setText("");
                imgViewUpImg.setImage(null);
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete successfully").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete fail").show();
            }
        }
    }

    public void btnOnClickActionProductUpdate(ActionEvent actionEvent) {
        if (txtProductId.getText() != null && txtProductName.getText() != null && txtProductQty.getText() != null && txtProductPrice.getText() != null) {
            if (productService.updateProduct(new Product(Integer.parseInt(txtProductId.getText()), txtProductName.getText(), cmbProductCategory.getSelectionModel().getSelectedItem().toString(), cmbProductSize.getSelectionModel().getSelectedItem().toString(), Double.parseDouble(txtProductPrice.getText()), Integer.parseInt(txtProductQty.getText()), currentImagePath, cmbProductSupplier.getSelectionModel().getSelectedItem().toString()))) {
                loadTables();
                loadcatalogue();
                txtProductName.setText("");
                txtProductPrice.setText("");
                txtProductQty.setText("");
                imgViewUpImg.setImage(null);
                new Alert(Alert.AlertType.INFORMATION, "Product updated successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Product updated fail").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Please enter All Details").show();
        }
    }

    //supplierCrud
    @FXML
    void btnOnClickActionAdd(ActionEvent event) {
        if (txtSupplierName.getText() != null && txtSupplierCompany.getText() != null && txtSupplierEmail.getText() != null && txtSearchSupplierId.getText() != null) {
            if (supplierService.addSupplier(new Supplier(0, txtSupplierName.getText(), txtSupplierEmail.getText(), txtSupplierCompany.getText(), cmbSupplierProduct.getSelectionModel().getSelectedItem().toString()))) {
                loadTables();
                txtSupplierName.setText(null);
                txtSupplierCompany.setText(null);
                txtSupplierEmail.setText(null);
                txtSupplierEmail.setText(null);
                new Alert(Alert.AlertType.INFORMATION, "Supplier added successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier added fail").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Please enter All Details").show();
        }
    }

    public void onClickActionSearchSupplier(ActionEvent actionEvent) {
        Supplier supplier = supplierService.searchSupplier(Integer.parseInt(txtSearchSupplierId.getText()));

        if (supplier == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please Enter Valid Supplier Id").show();
        } else {
            txtSupplierName.setText(supplier.getName());
            txtSupplierCompany.setText(supplier.getCompany());
            txtSupplierEmail.setText(supplier.getEmail());
            txtSupplierEmail.setText(supplier.getEmail());
            cmbSupplierProduct.setValue(supplier.getItem());

        }
    }

    public void btnOnClickActionUpdate(ActionEvent actionEvent) {
        if (txtSupplierName.getText() != null && txtSupplierCompany.getText() != null && txtSupplierEmail.getText() != null && txtSearchSupplierId.getText() != null) {
            if (supplierService.updateSupplier(new Supplier(Integer.parseInt(txtSearchSupplierId.getText()), txtSupplierName.getText(), txtSupplierEmail.getText(), txtSupplierCompany.getText(), cmbSupplierProduct.getSelectionModel().getSelectedItem().toString()))) {
                loadTables();
                txtSupplierName.setText(null);
                txtSupplierCompany.setText(null);
                txtSupplierEmail.setText(null);
                txtSupplierEmail.setText(null);
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated fail").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Please enter All Details").show();
        }
    }

    public void btnonClickActionDelete(ActionEvent actionEvent) {
        if (txtSearchSupplierId.getText() != null) {
            if (supplierService.deleteSupplier(Integer.parseInt(txtSearchSupplierId.getText()))) {
                loadTables();
                txtSupplierName.setText(null);
                txtSupplierCompany.setText(null);
                txtSupplierEmail.setText(null);
                txtSupplierEmail.setText(null);
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete fail").show();
            }
        }
    }


    public void onClickViewCart(MouseEvent mouseEvent) {

        CartFormController.setCart(cartArray);
        Stage stage = new Stage();
        try {
            Injector injector = Guice.createInjector(new AppModule());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cartForm.fxml"));
            loader.setControllerFactory(injector::getInstance);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
