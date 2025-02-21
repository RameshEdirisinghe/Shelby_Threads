package service.custom.impl;

import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import model.ProductSales;
import repository.custom.HomeDao;
import service.custom.HomeService;

import java.sql.*;
import java.util.*;



public class HomeServiceImpl implements HomeService {
    private static final String[] PIE_COLORS = {"#6c7b8b", "#8B7D6B", "#6A6A4F", "#5a6e6c"};
    @Inject
    HomeDao homeDao;

    @Override
    public List<PieChart.Data>  getSeriesMap() {
        try {
            List<ProductSales> productSalesList = homeDao.getTop3Product();
            List<PieChart.Data> pieChartDataList = new ArrayList<>();
            int colorIndex = 0;

            // Generate the data for the chart
            for (ProductSales productSales : productSalesList) {
                PieChart.Data slice = new PieChart.Data(productSales.getProductName(), productSales.getTotalSold());
                pieChartDataList.add(slice);

                // Apply custom color (loop through colors)
                String color = PIE_COLORS[colorIndex % PIE_COLORS.length];

                // Use Platform.runLater to ensure this happens after chart rendering
                Platform.runLater(() -> slice.getNode().setStyle("-fx-pie-color: " + color + ";"));

                colorIndex++;
            }

            return pieChartDataList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
