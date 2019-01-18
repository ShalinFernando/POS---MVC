/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.dep.app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.app.business.BOFactory;
import lk.ijse.dep.app.business.custom.ManageCustomersBO;
import lk.ijse.dep.app.business.custom.impl.ManageCustomersBOImpl;
import lk.ijse.dep.app.main.AppInitializer;
import lk.ijse.dep.app.dto.CustomerDTO;
import lk.ijse.dep.app.view.util.CustomerTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author ranjith-suranga
 */
public class ManageCustomerFormController{

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField txtCustomerId;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtCustomerAddress;

    @FXML
    private TableView<CustomerTM> tblCustomers;

    private ManageCustomersBO manageCustomersBO = BOFactory.getInstance().getBO(BOFactory.BOTypes.MANAGE_CUSTOMERS);

    /**
     * Initializes the controller class.
     */
    public void initialize() {
        tblCustomers.getColumns().get(0).setStyle("-app-alignment:center");
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        btnSave.setDisable(true);
        btnDelete.setDisable(true);

        List<CustomerDTO> customersDB = null;
        try {
            customersDB = manageCustomersBO.getCustomers();
        } catch (Exception e) {
           Logger.getLogger("").log(Level.SEVERE,null,e);
        }
        ObservableList<CustomerDTO> customerDTOS = FXCollections.observableArrayList(customersDB);
        ObservableList<CustomerTM> tblItems = FXCollections.observableArrayList();
        for (CustomerDTO customerDTO : customerDTOS) {
            tblItems.add(new CustomerTM(customerDTO.getId(), customerDTO.getName(), customerDTO.getAddress()));
        }
        tblCustomers.setItems(tblItems);

        tblCustomers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM selectedCustomer) {

                if (selectedCustomer == null) {
                    // Clear Selection
                    return;
                }

                txtCustomerId.setText(selectedCustomer.getId());
                txtCustomerName.setText(selectedCustomer.getName());
                txtCustomerAddress.setText(selectedCustomer.getAddress());

                txtCustomerId.setEditable(false);

                btnSave.setDisable(false);
                btnDelete.setDisable(false);

            }
        });
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        AppInitializer.navigateToHome(root, (Stage) this.root.getScene().getWindow());
    }

    @FXML
    private void btnSave_OnAction(ActionEvent event) throws SQLException {

        if (txtCustomerId.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Customer ID is empty", ButtonType.OK).showAndWait();
            txtCustomerId.requestFocus();
            return;
        } else if (txtCustomerName.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Customer Name is empty", ButtonType.OK).showAndWait();
            txtCustomerName.requestFocus();
            return;
        } else if (txtCustomerAddress.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Customer Address is empty", ButtonType.OK).showAndWait();
            txtCustomerAddress.requestFocus();
            return;
        }

        if (tblCustomers.getSelectionModel().isEmpty()) {
            // New

            ObservableList<CustomerTM> items = tblCustomers.getItems();
            for (CustomerTM customerTM : items) {
                if (customerTM.getId().equals(txtCustomerId.getText())) {
                    new Alert(Alert.AlertType.ERROR, "Duplicate Customer IDs are not allowed").showAndWait();
                    txtCustomerId.requestFocus();
                    return;
                }
            }

            CustomerTM customerTM = new CustomerTM(txtCustomerId.getText(), txtCustomerName.getText(), txtCustomerAddress.getText());
            tblCustomers.getItems().add(customerTM);
            CustomerDTO customerDTO = new CustomerDTO(txtCustomerId.getText(), txtCustomerName.getText(), txtCustomerAddress.getText());
            boolean result = false;
            try {
                result = manageCustomersBO.createCustomer(customerDTO);
            } catch (Exception e) {
                Logger.getLogger("").log(Level.SEVERE, null, e);
            }

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Customer has been saved successfully", ButtonType.OK).showAndWait();
                tblCustomers.scrollTo(customerTM);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer", ButtonType.OK).showAndWait();
            }

        } else {
            // Update

            CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
            selectedCustomer.setName(txtCustomerName.getText());
            selectedCustomer.setAddress(txtCustomerAddress.getText());
            tblCustomers.refresh();

            String selectedCustomerID = tblCustomers.getSelectionModel().getSelectedItem().getId();

            boolean result = false;
            try {
                result = manageCustomersBO.updateCustomer(new CustomerDTO(txtCustomerId.getText(),
                        txtCustomerName.getText(),
                        txtCustomerAddress.getText()));
            } catch (Exception e) {
                Logger.getLogger("").log(Level.SEVERE, null, e);
            }

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Customer has been updated successfully", ButtonType.OK).showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer", ButtonType.OK).showAndWait();
            }
        }

        reset();

    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) throws SQLException {
        Alert confirmMsg = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = confirmMsg.showAndWait();

        if (buttonType.get() == ButtonType.YES) {
            String selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem().getId();

            tblCustomers.getItems().remove(tblCustomers.getSelectionModel().getSelectedItem());
            boolean result = false;
            try {
                result = manageCustomersBO.deleteCustomer(selectedCustomer);
            } catch (Exception e) {
                Logger.getLogger("").log(Level.SEVERE, null, e);
            }
            if (!result){
                new Alert(Alert.AlertType.ERROR, "Failed to delete the customer", ButtonType.OK).showAndWait();
            }else{
                reset();
            }
        }

    }

    @FXML
    private void btnAddNew_OnAction(ActionEvent actionEvent) {
        reset();
    }

    private void reset() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerId.requestFocus();
        txtCustomerId.setEditable(true);
        btnSave.setDisable(false);
        btnDelete.setDisable(true);
        tblCustomers.getSelectionModel().clearSelection();
    }

}
