module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens com.UI to javafx.fxml;
    exports com.UI;
}
