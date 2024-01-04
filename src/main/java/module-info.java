/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
module com.zinko.studentmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.zinko.studentmanagement;
    opens com.zinko.studentmanagement.models;
    opens com.zinko.studentmanagement.controller;
    opens com.zinko.studentmanagement.service;
    exports com.zinko.studentmanagement;
}
