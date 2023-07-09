module com.screenappfree.zarubin.screenapplicationfree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.screenappfree.zarubin.screenapplicationfree to javafx.fxml;
    exports com.screenappfree.zarubin.screenapplicationfree;
}