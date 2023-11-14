module ensisa.puissance4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens ensisa.puissance4 to javafx.fxml;
    exports ensisa.puissance4;
}