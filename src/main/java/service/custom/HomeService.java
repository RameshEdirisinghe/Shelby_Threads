package service.custom;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface HomeService {

    List<PieChart.Data> getSeriesMap();
}
