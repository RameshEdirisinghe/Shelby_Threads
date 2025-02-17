package controller.home;

import DBConnection.DBConnection;
import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.employee.EmployeeController;
import controller.products.ProductsController;
import controller.supplier.SupplierController;
import animatefx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import model.Employee;
import model.Product;
import model.Supplier;
import model.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import repository.DaoFactory;
import repository.custom.OrderDao;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.AppModule;
import util.DaoType;
import util.ServiceType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;


public class HomeFormController implements Initializable {
    public Label lbltopic2;
    public Label lbltopic1;
    public AnchorPane lblPane1;
    public AnchorPane lblPane02;
    public NumberAxis StackedAreaChartYAxis;
    public NumberAxis StackedAreaChartXAxis;
    public javafx.scene.chart.StackedAreaChart StackedAreaChart;
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

    private String currentImagePath;

    private static final String URL = "jdbc:mysql://localhost:3306/shelby_threads";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    private static final String[] PIE_COLORS = {"#6c7b8b", "#8B7D6B", "#6A6A4F", "#5a6e6c"};


    public static void setUserId(User user){
        String userId=user.getUserId()+"";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Start Animation
        ZoomInLeft ZoomInLeftAnimation = new ZoomInLeft(lblPane1);
        ZoomInLeftAnimation.setSpeed(0.25);
        ZoomInLeftAnimation.play();

        ZoomInRight ZoomInRightAnimation = new ZoomInRight(lblPane02);
        ZoomInRightAnimation.setSpeed(0.25);
        ZoomInRightAnimation.play();


        Map<String, XYChart.Series<Number, Number>> seriesMap = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Step 1: Get Top 3 Products
            String topProductsQuery = """
            WITH TopProducts AS (
                SELECT ProductName, SUM(Quantity) AS total_sales
                FROM orderProduct
                JOIN orders ON orderProduct.OrderID = orders.OrderID
                WHERE OrderDate >= CURDATE() - INTERVAL 10 DAY
                GROUP BY ProductName
                ORDER BY total_sales DESC
                LIMIT 3
            )
            SELECT ProductName FROM TopProducts;
            """;
            ResultSet topProductsResult = statement.executeQuery(topProductsQuery);

            // Store top 3 product names
            List<String> topProducts = new ArrayList<>();
            while (topProductsResult.next()) {
                topProducts.add(topProductsResult.getString("ProductName"));
            }

            // Step 2: Fetch Sales Data for the Top 3 Products
            String salesQuery = """
            WITH TopProducts AS (
                SELECT ProductName, SUM(Quantity) AS total_sales
                FROM orderProduct
                JOIN orders ON orderProduct.OrderID = orders.OrderID
                WHERE OrderDate >= CURDATE() - INTERVAL 10 DAY
                GROUP BY ProductName
                ORDER BY total_sales DESC
                LIMIT 3
            )
            SELECT DATE(OrderDate) AS order_day, orderProduct.ProductName, SUM(orderProduct.Quantity) AS sales
            FROM orderProduct
            JOIN orders ON orderProduct.OrderID = orders.OrderID
            WHERE orderProduct.ProductName IN (SELECT ProductName FROM TopProducts)
            AND OrderDate >= CURDATE() - INTERVAL 10 DAY
            GROUP BY order_day, orderProduct.ProductName
            ORDER BY order_day, orderProduct.ProductName;
            """;

            ResultSet salesResult = statement.executeQuery(salesQuery);

            // Initialize series for each product
            for (String product : topProducts) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(product);
                seriesMap.put(product, series);
            }

            // Store sales data
            Map<Integer, Map<String, Integer>> salesData = new HashMap<>();
            int minDay = Integer.MAX_VALUE, maxDay = Integer.MIN_VALUE, maxSales = 1;

            while (salesResult.next()) {
                int day = salesResult.getDate("order_day").toLocalDate().getDayOfMonth();
                String productName = salesResult.getString("ProductName");
                int sales = salesResult.getInt("sales");

                salesData.computeIfAbsent(day, k -> new HashMap<>()).put(productName, sales);
                minDay = Math.min(minDay, day);
                maxDay = Math.max(maxDay, day);
                maxSales = Math.max(maxSales, sales);
            }

            // Populate data (Fill missing days with 0)
            for (int day = minDay; day <= maxDay; day++) {
                for (String product : topProducts) {
                    int sales = salesData.getOrDefault(day, new HashMap<>()).getOrDefault(product, 0);
                    seriesMap.get(product).getData().add(new XYChart.Data<>(day, sales));
                }
            }

            // Clear old data & add new series
            StackedAreaChart.getData().clear();
            StackedAreaChart.getData().addAll(seriesMap.values());


                // Adjust Axis Dynamically
                ((NumberAxis) StackedAreaChartXAxis).setLowerBound(minDay); // Start at first recorded sale
                ((NumberAxis) StackedAreaChartXAxis).setUpperBound(maxDay + 1); // Stop at last sale
                ((NumberAxis) StackedAreaChartYAxis).setLowerBound(0);
                ((NumberAxis) StackedAreaChartYAxis).setUpperBound(Math.max(10, maxSales + 36)); // Ensure visibility

            } catch (SQLException e) {
                e.printStackTrace();
            }



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

        //set cmbSupplierproduct values
        reloadCmbSupplierItem();

        setCmbReportType();



        String query = "SELECT e.Name AS EmployeeName, SUM(o.TotalCost) AS TotalSales\n" +
                "FROM orders o\n" +
                "JOIN employee e ON o.EmployeeID = e.EmployeeID\n" +
                "GROUP BY e.Name\n" +
                "ORDER BY TotalSales DESC;";

        // Set up the X and Y axes for BarChart
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

        // Fetch data and add to the chart
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<Number, String> series = new XYChart.Series<>();
//            series.setName("Sales by Employees");
            BestEmployeeChart.setLegendVisible(false);


            // Populate series with data
            while (resultSet.next()) {
                String employeeName = resultSet.getString("EmployeeName");
                double totalSales = resultSet.getDouble("TotalSales");

                // Add data to the chart (employee name and their total sales)
                XYChart.Data<Number, String> data = new XYChart.Data<>(totalSales, employeeName);
                series.getData().add(data);
            }

            // Add series to the chart
            BestEmployeeChart.getData().add(series);

            // Change bar color to gray
            for (XYChart.Data<Number, String> data : series.getData()) {
                data.getNode().setStyle("-fx-bar-fill: gray;");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String query1 = "SELECT op.ProductName, SUM(op.Quantity) AS TotalSold " +
                "FROM OrderProduct op " +
                "GROUP BY op.ProductName " +
                "ORDER BY TotalSold DESC";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query1);
             ResultSet resultSet = statement.executeQuery()) {

            // Clear existing data in PieChart
            BestSaleProductPieChart.getData().clear();

            int colorIndex = 0;
            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                int totalSold = resultSet.getInt("TotalSold");

                // Add data with original label
                PieChart.Data slice = new PieChart.Data(productName, totalSold);
                BestSaleProductPieChart.getData().add(slice);
                BestSaleProductPieChart.setLegendVisible(false);

                // Apply custom color (loop through colors)
                String color = PIE_COLORS[colorIndex % PIE_COLORS.length];
                slice.getNode().setStyle("-fx-pie-color: " + color + ";");

                colorIndex++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomerService cuService=ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

        lbltotalCustomers.setText(cuService.getCustomersCount()+"");

        lblSupplierCount.setText(SupplierController.getInstance().getAllSupplierCount()+"");

        OrderDao order = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        lblOrdersCount.setText(order.getAllOrderCount()+"");



    }

    public void reloadCmbSupplierItem(){
        ObservableList<String> supItem = FXCollections.observableArrayList();
        supItem.add("Full Suits");
        supItem.add("Jackets");
        supItem.add("Trousers");
        supItem.add("Cap/Hats");
        supItem.add("Watch");


        cmbSupplierProduct.setItems(supItem);
    }

    public void reloadCmbCat(){
        ObservableList<String> cat = FXCollections.observableArrayList();
        cat.add("Ladies");
        cat.add("Gents");
        cat.add("Kids");


        cmbProductCategory.setItems(cat);
    }

    public void reloadCmbSupplier(){
        ObservableList<String> sup = FXCollections.observableArrayList();
        for (String supplierName : SupplierController.getInstance().getAllSupplierNames()) {
            sup.add(supplierName);
        }


        cmbProductSupplier.setItems(sup);
    }


    public void reloadCmbsize(){
        ObservableList<String> size = FXCollections.observableArrayList();
        size.add("Small");
        size.add("Medium");
        size.add("Large");
        size.add("XL");
        size.add("XXL");


        cmbProductSize.setItems(size);
    }

    public void setCmbReportType(){
        ObservableList<String> reportType = FXCollections.observableArrayList();
        reportType.add("Last 10 Days Orders");
        reportType.add("Last 30 Days Orders");
        reportType.add("Best Employees");
        reportType.add("Best Sales Product");

        cmbReportType.setItems(reportType);
    }



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

    public void loadTables(){
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
        for (Employee employee : EmployeeController.getInstance().getAllEmployee()) {
            employeeObservableList.add(employee);
        }
        tblEmployee.setItems(employeeObservableList);

        ObservableList<Supplier> supplierObservableList = FXCollections.observableArrayList();
        for (Supplier supplier : SupplierController.getInstance().getAllSuppliers()) {
            supplierObservableList.add(supplier);
        }
        tblSupplier.setItems(supplierObservableList);


        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        for (Product product : ProductsController.getInstance().getAllProduct()) {
            productObservableList.add(product);
        }
        tblInventory.setItems(productObservableList);
    }

    public void btnOnClickActionEmpDelete(ActionEvent actionEvent) {
        if (EmployeeController.getInstance().deleteEmployee(txtSearchEmpId.getText(),txtEmpName.getText())){
            loadTables();
            new Alert(Alert.AlertType.INFORMATION,"Employee Delete successfully").show();

        }else {
            new Alert(Alert.AlertType.INFORMATION,"Employee Delete fail").show();
        }

    }

    public void btnOnClickActionEmpAdd(ActionEvent actionEvent) {
        if (EmployeeController.getInstance().addEmployee(new Employee(0,txtEmpName.getText(),txtEmpEmail.getText(),txtEmpPassword.getText(),txtEmpCompany.getText()))){
            loadTables();
            new Alert(Alert.AlertType.INFORMATION,"Employee Added successfully").show();

        }else {
            new Alert(Alert.AlertType.INFORMATION,"Employee Added fail").show();
        }
    }

    public void btnOnClickActionEmpUpdate(ActionEvent actionEvent) {
        if (EmployeeController.getInstance().updateEmployee(new Employee( Integer.parseInt(txtSearchEmpId.getText()),txtEmpName.getText(),txtEmpEmail.getText(),txtEmpPassword.getText(),txtEmpCompany.getText()))){
            loadTables();
            new Alert(Alert.AlertType.INFORMATION,"Employee updated successfully").show();

        }else {
            new Alert(Alert.AlertType.INFORMATION,"Employee updated fail").show();
        }
    }

    public void btnOnClickActionEmpSearch(ActionEvent actionEvent) {
        Employee employee = EmployeeController.getInstance().searchEmployee(txtSearchEmpId.getText());

        if (employee==null){
            new Alert(Alert.AlertType.INFORMATION,"Please Enter Valid Employee Id").show();
        }else {

            txtEmpName.setText(employee.getName());
            txtEmpEmail.setText(employee.getEmail());
            txtEmpCompany.setText(employee.getCompany());
            
        }
    }

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

    public void btnOnClickActionAdd(ActionEvent actionEvent) {
        if (ProductsController.getInstance().addProducts(new Product(0,txtProductName.getText(),cmbProductCategory.getSelectionModel().getSelectedItem().toString(),cmbProductSize.getSelectionModel().getSelectedItem().toString(),Double.parseDouble(txtProductPrice.getText()),Integer.parseInt(txtProductQty.getText()),currentImagePath,cmbProductSupplier.getSelectionModel().getSelectedItem().toString()))){
            new Alert(Alert.AlertType.INFORMATION,"Employee Added successfully").show();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Employee Added Fail").show();
        }

    }

    public void btnOnclickActionPlaceOrder(ActionEvent actionEvent) {
        Stage st = new Stage();
        try {
            Injector injector = Guice.createInjector(new AppModule());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeDashboard.fxml"));
            loader.setController(injector.getInstance(EmployeeDashboardFormController.class));
            st.setScene(new Scene(loader.load()));
            st.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onClickActionSearchSupplier(ActionEvent actionEvent) {
        Supplier supplier = SupplierController.getInstance().searchSupplier(txtSearchSupplierId.getText());

        if (supplier==null){
            new Alert(Alert.AlertType.INFORMATION,"Please Enter Valid Employee Id").show();
        }else {

            txtSupplierName.setText(supplier.getName());
            txtSupplierCompany.setText(supplier.getCompany());
            txtSupplierEmail.setText(supplier.getEmail());
            txtSupplierEmail.setText(supplier.getEmail());
            cmbSupplierProduct.setValue(supplier.getItem());

        }
    }

    public void btnOnClickActionUpdate(ActionEvent actionEvent) {
        if(txtSupplierName.getText()!=null && txtSupplierCompany.getText()!=null && txtSupplierEmail.getText()!=null && txtSearchSupplierId.getText()!=null) {
            if (SupplierController.getInstance().updateSupplier(new Supplier(Integer.parseInt(txtSearchSupplierId.getText()), txtSupplierName.getText(), txtSupplierEmail.getText(), txtSupplierCompany.getText(), cmbSupplierProduct.getSelectionModel().getSelectedItem().toString()))) {
                loadTables();
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated fail").show();
            }
        }
        else{
            new Alert(Alert.AlertType.INFORMATION, "Please enter All Details").show();
        }
    }

    public void btnonClickActionDelete(ActionEvent actionEvent) {
        if (txtSearchSupplierId.getText()!=null) {
            if (SupplierController.getInstance().deleteSupplier(txtSearchSupplierId.getText())) {
                loadTables();
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete fail").show();
            }
        }

    }

    public void btnonClickActionAdd(ActionEvent actionEvent) {
        if(txtSupplierName.getText()!=null && txtSupplierCompany.getText()!=null && txtSupplierEmail.getText()!=null && txtSearchSupplierId.getText()!=null) {
            if (SupplierController.getInstance().addSupplier(new Supplier(0, txtSupplierName.getText(), txtSupplierEmail.getText(), txtSupplierCompany.getText(), cmbSupplierProduct.getSelectionModel().getSelectedItem().toString()))) {
                loadTables();
                new Alert(Alert.AlertType.INFORMATION, "Supplier added successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier added fail").show();
            }
        }
        else{
            new Alert(Alert.AlertType.INFORMATION, "Please enter All Details").show();
        }

    }

    public void btnOnClickActionProductDelete(ActionEvent actionEvent) {

        if (txtProductId.getText()!=null) {
            if (ProductsController.getInstance().deleteProduct(txtProductId.getText())) {
                loadTables();
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Delete fail").show();
            }
        }
    }

    public void btnOnClickActionProductSearch(ActionEvent actionEvent) {
        Product product = ProductsController.getInstance().searchProduct(txtProductId.getText());

        if (product==null){
            new Alert(Alert.AlertType.INFORMATION,"Please Enter Valid Employee Id").show();
        }else {

            txtProductName.setText(product.getName());
            txtProductPrice.setText(product.getPrice()+"");
            txtProductQty.setText(product.getQty()+"");
            Image img = new Image(product.getImgPath());
            imgViewUpImg.setImage(img);
            cmbProductCategory.setValue(product.getCategory());
            cmbProductSize.setValue(product.getSize());
            cmbProductSupplier.setValue(product.getSuppler());

        }
    }

    public void btnOnClickActionProductUpdate(ActionEvent actionEvent) {
        if(txtProductId.getText()!=null && txtProductName.getText()!=null && txtProductQty.getText()!=null && txtProductPrice.getText()!=null) {
            if (ProductsController.getInstance().updateProduct(new Product(Integer.parseInt(txtProductId.getText()),txtProductName.getText(),cmbProductCategory.getSelectionModel().getSelectedItem().toString(),cmbProductSize.getSelectionModel().getSelectedItem().toString(),Double.parseDouble(txtProductPrice.getText()),Integer.parseInt(txtProductQty.getText()),currentImagePath,cmbProductSupplier.getSelectionModel().getSelectedItem().toString()))) {
                loadTables();
                new Alert(Alert.AlertType.INFORMATION, "Product updated successfully").show();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Product updated fail").show();
            }
        }
        else{
            new Alert(Alert.AlertType.INFORMATION, "Please enter All Details").show();
        }
    }

    public void btnclickOnActionPrint(ActionEvent actionEvent) {

    }

    public void btnclickOnActionViewReport(ActionEvent actionEvent) {
        try {
            if (cmbReportType.getSelectionModel().getSelectedItem().toString().equals("Last 10 Days Orders")) {
                JasperDesign design = JRXmlLoader.load("src/main/resources/reports/ShelbyLast10Days.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(design);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint,false);
            }else {
                System.out.println("Error");
            }


        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnclickOnActionPrintReport(ActionEvent actionEvent) {
    }
}
