package controller.home;

import DBConnection.DBConnection;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import animatefx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import service.custom.*;
import util.AppModule;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class HomeFormController implements Initializable {
    public Label lbltopic2;
    public Label lbltopic1;
    public AnchorPane lblPane1;
    public AnchorPane lblPane02;

    public AnchorPane pane2ndImgs;
    public AnchorPane pane1stImgs;
    public TableColumn colEmpEmail;
    public TableColumn colEmpName;
    public TableColumn colEmpId;
    public TableView tblEmployee;
    public TextField txtEmpPassword;
    public TextField txtEmpEmail;
    public TextField txtEmpName;
    public TextField txtSearchEmpId;
    public TextField txtEmpCompany;
    public TableColumn colEmpCompany;
    public ImageView imgViewUpImg;
    public TextField txtProductName;
    public TextField txtProductId;
    public ComboBox cmbProductCategory;
    public TextField txtProductPrice;
    public ComboBox cmbProductSize;
    public TextField txtProductQty;
    public ComboBox cmbProductSupplier;
    public TextField txtSearchSupplierId;
    public TextField txtSupplierId;
    public TextField txtSupplierName;
    public TextField txtSupplierCompany;
    public TextField txtSupplierEmail;
    public ComboBox cmbSupplierProduct;
    public TableView tblSupplier;
    public TableColumn colSupplierId;
    public TableColumn colSupplierName;
    public TableColumn colSupplierCompany;
    public TableColumn colSupilerEmail;
    public TableColumn colSupplierItem;
    public TableColumn colProductId;
    public TableView tblInventory;
    public TableColumn colProductName;
    public TableColumn colProductCategory;
    public TableColumn colProductSupplierName;
    public TableColumn colProductSize;
    public TableColumn colProductPrice;
    public TableColumn colProductStock;
    public ComboBox cmbReportType;
    public BarChart BestEmployeeChart;
    public PieChart BestSaleProductPieChart;
    public Label lbltotalCustomers;
    public Label lblOrdersCount;
    public Label lblSupplierCount;
    public PieChart BestSaleProductPieChart1;

    private String currentImagePath;

    private static final String URL = "jdbc:mysql://localhost:3306/shelby_threads";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    private static final String[] PIE_COLORS = {"#6c7b8b", "#8B7D6B", "#6A6A4F", "#5a6e6c"};


    public static void setUserId(User user) {
        String userId = user.getUserId() + "";
    }

    @Inject
    HomeService homeService;

    @Inject
    EmployeeService employeeService;

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

        //Start Animation
        ZoomInLeft ZoomInLeftAnimation = new ZoomInLeft(lblPane1);
        ZoomInLeftAnimation.setSpeed(0.25);
        ZoomInLeftAnimation.play();

        ZoomInRight ZoomInRightAnimation = new ZoomInRight(lblPane02);
        ZoomInRightAnimation.setSpeed(0.25);
        ZoomInRightAnimation.play();

        //set Tables Cols-Employee
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmpEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmpCompany.setCellValueFactory(new PropertyValueFactory<>("company"));

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

        loadTables();

        //set cmbProductCategory values
        reloadCmbCat();
        reloadCmbsize();
        reloadCmbSupplier();
        reloadCmbSupplierItem();

        setCmbReportType();

        loadPieCharDahsboard();
        loadPieChartReports();
        loadLineChart();

        lbltotalCustomers.setText(customerService.getCustomersCount() + "");
        lblSupplierCount.setText(supplierService.getAllSupplierCount() + "");
        lblOrdersCount.setText(orderService.getAllOrderCount() + "");

    }

    public void loadLineChart() {
        NumberAxis xAxis = (NumberAxis) BestEmployeeChart.getXAxis();
        xAxis.setLabel("Total Sales");
        xAxis.setTickUnit(250); // Set the tick unit to 250
        xAxis.setTickLabelFill(Color.web("#fffff0")); // Change the X-axis label color
        xAxis.setStyle("-fx-text-fill: #fffff0;");

        CategoryAxis yAxis = (CategoryAxis) BestEmployeeChart.getYAxis();
        yAxis.setLabel("Employee");
        yAxis.setStyle("-fx-text-fill: #fffff0;"); // Change Y-axis label color

        // Set the chart title color
        BestEmployeeChart.setStyle("-fx-text-fill: #fffff0;");

        // Fetch and display data
        List<EmployeeSales> salesData = employeeService.getTopEmployees();

        // Add data to the chart
        XYChart.Series<Number, String> series = new XYChart.Series<>();
        BestEmployeeChart.setLegendVisible(false);

        for (EmployeeSales sales : salesData) {
            XYChart.Data<Number, String> data = new XYChart.Data<>(sales.getTotalSales(), sales.getEmployeeName());
            series.getData().add(data);
        }
        // Add series to the chart
        BestEmployeeChart.getData().add(series);
        // Change bar color to gray
        for (XYChart.Data<Number, String> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: gray;");
        }
    }

    //load charts
    public void loadPieCharDahsboard() {
        List<PieChart.Data> pieChartDataList = homeService.getSeriesMap();
        BestSaleProductPieChart1.getData().clear();
        BestSaleProductPieChart1.getData().addAll(pieChartDataList);
        BestSaleProductPieChart1.setLegendVisible(false);
    }

    public void loadPieChartReports() {
        List<PieChart.Data> pieChartDataList = homeService.getSeriesMap();
        BestSaleProductPieChart.getData().clear();
        BestSaleProductPieChart.getData().addAll(pieChartDataList);
        BestSaleProductPieChart.setLegendVisible(false);
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

    public void setCmbReportType() {
        ObservableList<String> reportType = FXCollections.observableArrayList();
        reportType.add("Last 10 Days Orders");
        reportType.add("Last 30 Days Orders");
        reportType.add("Best Employees");
        reportType.add("Best Sales Product");

        cmbReportType.setItems(reportType);
    }

    //homeDashboard TopSelling products Animation
    public void btnOnClickActionNext(MouseEvent mouseEvent) {
        pane1stImgs.setVisible(false);     // Hide img1AnchorPane
        pane2ndImgs.setVisible(true);      // Show img3AnchorPane
        new FadeIn(pane2ndImgs).play();    // Apply FadeIn animation
        pane2ndImgs.toFront();
    }

    public void btnOnActionPrevious(MouseEvent mouseEvent) {
        pane2ndImgs.setVisible(false);     // Hide img1AnchorPane
        pane1stImgs.setVisible(true);      // Show img3AnchorPane
        new FadeIn(pane1stImgs).play();    // Apply FadeIn animation
        pane1stImgs.toFront();
    }

    //TablesLoads
    public void loadTables() {
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
        for (Employee employee : employeeService.getAll()) {
            employeeObservableList.add(employee);
        }
        tblEmployee.setItems(employeeObservableList);

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

    //EmployeeCrud
    public void btnOnClickActionEmpDelete(ActionEvent actionEvent) {
        if (employeeService.deletEmployee(Integer.parseInt(txtSearchEmpId.getText()))) {
            loadTables();
            new Alert(Alert.AlertType.INFORMATION, "Employee Delete successfully").show();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Employee Delete fail").show();
        }
    }

    public void btnOnClickActionEmpAdd(ActionEvent actionEvent) {
        if (employeeService.addEmployee(new Employee(0, txtEmpName.getText(), txtEmpEmail.getText(), txtEmpPassword.getText(), txtEmpCompany.getText()))) {
            loadTables();
            new Alert(Alert.AlertType.INFORMATION, "Employee Added successfully").show();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Employee Added fail").show();
        }
    }

    public void btnOnClickActionEmpUpdate(ActionEvent actionEvent) {
        if (employeeService.updateEmployee(new Employee(Integer.parseInt(txtSearchEmpId.getText()), txtEmpName.getText(), txtEmpEmail.getText(), txtEmpPassword.getText(), txtEmpCompany.getText()))) {
            loadTables();
            new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully").show();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Employee updated fail").show();
        }
    }

    public void btnOnClickActionEmpSearch(ActionEvent actionEvent) {
        Employee employee = employeeService.searchEmployee(Integer.parseInt(txtSearchEmpId.getText()));
        if (employee == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please Enter Valid Employee Id").show();
        } else {
            txtEmpName.setText(employee.getName());
            txtEmpEmail.setText(employee.getEmail());
            txtEmpCompany.setText(employee.getCompany());
        }
    }

    //productCrud
    public void btnOnClickActionAdd(ActionEvent actionEvent) {
        if (productService.addProducts(new Product(0, txtProductName.getText(), cmbProductCategory.getSelectionModel().getSelectedItem().toString(), cmbProductSize.getSelectionModel().getSelectedItem().toString(), Double.parseDouble(txtProductPrice.getText()), Integer.parseInt(txtProductQty.getText()), currentImagePath, cmbProductSupplier.getSelectionModel().getSelectedItem().toString()))) {
            new Alert(Alert.AlertType.INFORMATION, "Employee Added successfully").show();
            loadTables();
            txtProductName.setText("");
            txtProductPrice.setText("");
            txtProductQty.setText("");
            imgViewUpImg.setImage(null);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Employee Added Fail").show();
        }
    }

    public void btnOnClickActionProductDelete(ActionEvent actionEvent) {
        if (txtProductId.getText() != null) {
            if (productService.deleteProduct(Integer.parseInt(txtProductId.getText()))) {
                loadTables();
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

    public void btnOnClickActionProductSearch(ActionEvent actionEvent) {
        Product product = productService.searchProduct(Integer.parseInt(txtProductId.getText()));

        if (product == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please Enter Valid Product Id").show();
        } else {
            loadTables();
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

    public void btnOnClickActionProductUpdate(ActionEvent actionEvent) {
        if (txtProductId.getText() != null && txtProductName.getText() != null && txtProductQty.getText() != null && txtProductPrice.getText() != null) {
            if (productService.updateProduct(new Product(Integer.parseInt(txtProductId.getText()), txtProductName.getText(), cmbProductCategory.getSelectionModel().getSelectedItem().toString(), cmbProductSize.getSelectionModel().getSelectedItem().toString(), Double.parseDouble(txtProductPrice.getText()), Integer.parseInt(txtProductQty.getText()), currentImagePath, cmbProductSupplier.getSelectionModel().getSelectedItem().toString()))) {
                loadTables();
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

    //SupplierCrud
    public void btnOnActionImportImg(ActionEvent actionEvent) throws IOException {
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

    public void btnonClickActionAdd(ActionEvent actionEvent) {
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

    public void btnclickOnActionViewReport(ActionEvent actionEvent) {
        try {
            if (cmbReportType.getSelectionModel().getSelectedItem().toString().equals("Last 10 Days Orders")) {
                JasperDesign design = JRXmlLoader.load("src/main/resources/reports/ShelbyLast10Days.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(design);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } else {
                System.out.println("Error");
            }


        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnOnclickActionPlaceOrder(ActionEvent actionEvent) {
        Stage st = new Stage();
        try {
            Injector injector = Guice.createInjector(new AppModule());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeDashboard.fxml"));
            loader.setControllerFactory(injector::getInstance);
            st.setScene(new Scene(loader.load()));
            st.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnclickOnActionPrintReport(ActionEvent actionEvent) {
    }

    public void btnclickOnActionPrint(ActionEvent actionEvent) {

    }

}
