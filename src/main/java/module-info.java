module com.mycompany.seniorproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.seniorproject to javafx.fxml;
    exports com.mycompany.seniorproject;
}
