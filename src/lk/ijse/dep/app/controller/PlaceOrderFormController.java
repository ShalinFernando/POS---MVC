package lk.ijse.dep.app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.dep.app.business.BOFactory;
import lk.ijse.dep.app.business.custom.ManageCustomersBO;
import lk.ijse.dep.app.business.custom.ManageItemsBO;
import lk.ijse.dep.app.business.custom.ManageOrdersBO;
import lk.ijse.dep.app.dto.CustomerDTO;
import lk.ijse.dep.app.dto.ItemDTO;
import lk.ijse.dep.app.dto.OrderDTO;
import lk.ijse.dep.app.dto.OrderDetailDTO;
import lk.ijse.dep.app.view.util.OrderDetailTM;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaceOrderFormController {
    @FXML
    private JFXButton btnPlaceOrder;
    @FXML
    private JFXTextField txtCustomerId;
    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXTextField txtQtyOnHand;
    @FXML
    private JFXTextField txtUnitPrice;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private TableView<OrderDetailTM> tblOrderDetails;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private Label lblTotal;
    @FXML
    private JFXTextField txtOrderID;
    @FXML
    private JFXDatePicker txtOrderDate;

    private ObservableList<ItemDTO> tempItemsDB = FXCollections.observableArrayList();

    private ManageOrdersBO manageOrdersBO = BOFactory.getInstance().getBO(BOFactory.BOTypes.MANAGE_ORDERS);
    private ManageItemsBO manageItemsBO = BOFactory.getInstance().getBO(BOFactory.BOTypes.MANAGE_ITEMS);
    private ManageCustomersBO manageCustomersBO = BOFactory.getInstance().getBO(BOFactory.BOTypes.MANAGE_CUSTOMERS);

    public void initialize() {

        try {
            tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
            tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
            tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
            tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

            List<ItemDTO> itemsDB = manageItemsBO.getItems();
            for (ItemDTO itemDTO : itemsDB) {
                tempItemsDB.add(new ItemDTO(itemDTO.getCode(), itemDTO.getDescription(), itemDTO.getUnitPrice(), itemDTO.getQtyOnHand()));
            }

            txtOrderID.setEditable(false);

            txtOrderID.setText(manageOrdersBO.generateOrderId());
            txtOrderDate.setValue(LocalDate.now());

            btnRemove.setDisable(true);
            btnPlaceOrder.setDisable(true);
            calculateTotal();

            Platform.runLater(() -> {
                txtCustomerId.requestFocus();
            });

            tblOrderDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderDetailTM>() {
                @Override
                public void changed(ObservableValue<? extends OrderDetailTM> observable, OrderDetailTM oldValue, OrderDetailTM selectedOrderDetail) {

                    if (selectedOrderDetail == null) {
                        // Clear Selection
                        return;
                    }

                    txtItemCode.setText(selectedOrderDetail.getCode());
                    txtDescription.setText(selectedOrderDetail.getDescription());
                    txtUnitPrice.setText(selectedOrderDetail.getUnitPrice() + "");
                    txtQty.setText(selectedOrderDetail.getQty() + "");
                    txtQtyOnHand.setText(getItemFromTempDB(txtItemCode.getText()).getQtyOnHand() + "");

                    txtItemCode.setEditable(false);
                    btnRemove.setDisable(false);

                }
            });

            tblOrderDetails.getItems().addListener(new ListChangeListener<OrderDetailTM>() {
                @Override
                public void onChanged(Change<? extends OrderDetailTM> c) {
                    calculateTotal();

                    btnPlaceOrder.setDisable(tblOrderDetails.getItems().size() == 0);
                }
            });
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }

    }

    @FXML
    private void navigateToMain(MouseEvent event) throws IOException {
        Label lblMainNav = (Label) event.getSource();
        Stage primaryStage = (Stage) lblMainNav.getScene().getWindow();

        Parent root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/dep/app/view/MainForm.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }


    @FXML
    private void btnSaveOnAction(ActionEvent event) {

        if (validateItemCode() == null) {
            return;
        }

        String qty = txtQty.getText();
        if (!isInt(qty)) {
            showInvalidateMsgBox("Qty should be a number");
            return;
        } else if (Integer.parseInt(qty) == 0) {
            showInvalidateMsgBox("Qty can't be zero");
            return;
        } else if (Integer.parseInt(qty) > Integer.parseInt(txtQtyOnHand.getText())) {
            showInvalidateMsgBox("Invalid Qty");
            return;
        }

        if (tblOrderDetails.getSelectionModel().isEmpty()) {
            // New

            OrderDetailTM orderDetailTM = null;

            if ((orderDetailTM = isItemExist(txtItemCode.getText())) == null) {

                OrderDetailTM newOrderDetailTM = new OrderDetailTM(txtItemCode.getText(),
                        txtDescription.getText(),
                        Integer.parseInt(qty),
                        Double.parseDouble(txtUnitPrice.getText()),
                        Integer.parseInt(qty) * Double.parseDouble(txtUnitPrice.getText()));

                tblOrderDetails.getItems().add(newOrderDetailTM);

            } else {
                orderDetailTM.setQty(orderDetailTM.getQty() + Integer.parseInt(qty));
            }


        } else {
            // Update
            OrderDetailTM selectedItem = tblOrderDetails.getSelectionModel().getSelectedItem();
            synchronizeQty(selectedItem.getCode());
            selectedItem.setQty(Integer.parseInt(qty));
        }

        setTempQty(txtItemCode.getText(), Integer.parseInt(qty));
        tblOrderDetails.refresh();
        reset();

//        calculateTotal();
    }

    @FXML
    private void btnRemoveOnAction(ActionEvent actionEvent) {

        OrderDetailTM selectedItem = tblOrderDetails.getSelectionModel().getSelectedItem();
        tblOrderDetails.getItems().remove(selectedItem);

        synchronizeQty(selectedItem.getCode());
        reset();

//        calculateTotal();

    }

    @FXML
    private void btnPlaceOrderOnAction(ActionEvent actionEvent) throws SQLException {

        if (txtCustomerId.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Can't place a order without a customer Id", ButtonType.OK).showAndWait();
            txtCustomerId.requestFocus();
            return;
        }

        ObservableList<OrderDetailTM> items = tblOrderDetails.getItems();
        ArrayList<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();

        for (OrderDetailTM item : items) {
            orderDetailDTOS.add(new OrderDetailDTO(item.getCode(), item.getDescription(), item.getQty(), item.getUnitPrice()));
        }
        try {
            manageOrdersBO.createOrder(new OrderDTO(txtOrderID.getText(), txtOrderDate.getValue(), txtCustomerId.getText(), orderDetailDTOS));
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }

        new Alert(Alert.AlertType.CONFIRMATION, "Order has been placed successfully", ButtonType.OK).showAndWait();
        hardReset();

    }

    private void hardReset() {
        reset();
        tblOrderDetails.getItems().removeAll(tblOrderDetails.getItems());
        txtCustomerId.clear();
        txtCustomerName.clear();

        try {
            txtOrderID.setText(manageOrdersBO.generateOrderId());
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }

        txtCustomerId.requestFocus();
    }

    public void calculateTotal() {
        ObservableList<OrderDetailTM> items = tblOrderDetails.getItems();

        double total = 0.0;

        for (OrderDetailTM item : items) {
            total += item.getTotal();
        }

        lblTotal.setText("Total : " + total + "");
    }

    @FXML
    private void txtCustomerID_OnAction(ActionEvent actionEvent) throws SQLException {

        String customerID = txtCustomerId.getText();

        CustomerDTO customerDTO = null;
        try {
            customerDTO = manageCustomersBO.findCustomer(customerID);
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }

        if (customerDTO == null) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID", ButtonType.OK).showAndWait();
            txtCustomerName.clear();
            txtCustomerId.requestFocus();
            txtCustomerId.selectAll();
        } else {
            txtCustomerName.setText(customerDTO.getName());
            txtItemCode.requestFocus();
        }

    }

    @FXML
    private void txtItemCode_OnAction(ActionEvent actionEvent) {

        ItemDTO itemDTO = validateItemCode();

        if (itemDTO != null) {

            txtDescription.setText(itemDTO.getDescription());
            txtQtyOnHand.setText(getItemFromTempDB(itemDTO.getCode()).getQtyOnHand() + "");
            txtUnitPrice.setText(itemDTO.getUnitPrice() + "");
            txtQty.requestFocus();
        }

    }

    @FXML
    private void txtQty_OnAction(ActionEvent actionEvent) {
        btnSaveOnAction(actionEvent);
    }

    private ItemDTO validateItemCode() {
        String itemCode = txtItemCode.getText();

        ItemDTO itemDTO = null;

        try {
            itemDTO = manageItemsBO.findItem(itemCode);
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }


        if (itemDTO == null) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item Code", ButtonType.OK).showAndWait();
            txtDescription.clear();
            txtQtyOnHand.clear();
            txtUnitPrice.clear();
            txtQty.clear();
            txtItemCode.requestFocus();
            txtItemCode.selectAll();
        }
        return itemDTO;
    }

    public boolean isInt(String number) {
        char[] chars = number.toCharArray();
        for (char aChar : chars) {
            if (!Character.isDigit(aChar)) {
                return false;
            }
        }
        return true;
    }

    public ItemDTO getItemFromTempDB(String itemCode) {
        for (ItemDTO itemDTO : tempItemsDB) {
            if (itemDTO.getCode().equals(itemCode)) {
                return itemDTO;
            }
        }
        return null;
    }

    private void showInvalidateMsgBox(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
        txtQty.requestFocus();
        txtQty.selectAll();
    }

    private OrderDetailTM isItemExist(String itemCode) {
        ObservableList<OrderDetailTM> items = tblOrderDetails.getItems();
        for (OrderDetailTM item : items) {
            if (item.getCode().equals(itemCode)) {
                return item;
            }
        }
        return null;
    }

    public void reset() {
        tblOrderDetails.refresh();
        txtItemCode.clear();
        txtDescription.clear();
        txtQty.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtItemCode.setEditable(true);
        btnRemove.setDisable(true);
        tblOrderDetails.getSelectionModel().clearSelection();
        txtItemCode.requestFocus();
    }

    private void setTempQty(String itemCode, int qty) {
        for (ItemDTO itemDTO : tempItemsDB) {
            if (itemDTO.getCode().equals(itemCode)) {
                itemDTO.setQtyOnHand(itemDTO.getQtyOnHand() - qty);
                break;
            }
        }
    }

    private void synchronizeQty(String itemCode) {
        int qtyOnHand = 0;

        try {
            qtyOnHand = manageItemsBO.findItem(itemCode).getQtyOnHand();
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }

        for (ItemDTO itemDTO : tempItemsDB) {
            if (itemDTO.getCode().equals(itemCode)) {
                itemDTO.setQtyOnHand(qtyOnHand);
                return;
            }
        }
    }
}
