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
import lk.ijse.dep.app.business.custom.ManageItemsBO;
import lk.ijse.dep.app.business.custom.impl.ManageItemsBOImpl;
import lk.ijse.dep.app.main.AppInitializer;
import lk.ijse.dep.app.dto.ItemDTO;
import lk.ijse.dep.app.view.util.ItemTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ranjith-suranga
 */
public class ManageItemFormController {

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXTextField txtUnitPrice;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private AnchorPane root;
    @FXML
    private TableView<ItemTM> tblItems;

    private ManageItemsBO manageItemsBO = BOFactory.getInstance().getBO(BOFactory.BOTypes.MANAGE_ITEMS);

    public void initialize() {

        tblItems.getColumns().get(0).setStyle("-app-alignment: center");
        tblItems.getColumns().get(2).setStyle("-app-alignment: center-right");
        tblItems.getColumns().get(3).setStyle("-app-alignment: center-right");

        tblItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        btnSave.setDisable(true);
        btnDelete.setDisable(true);

        List<ItemDTO> itemsDB = null;
        try {
            itemsDB = manageItemsBO.getItems();
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }
        ObservableList<ItemDTO> itemDTOS = FXCollections.observableArrayList(itemsDB);
        ObservableList<ItemTM> itemTMS = FXCollections.observableArrayList();
        for (ItemDTO itemDTO : itemDTOS) {
            itemTMS.add(new ItemTM(itemDTO.getCode(), itemDTO.getDescription(), itemDTO.getUnitPrice(), itemDTO.getQtyOnHand()));
        }
        tblItems.setItems(itemTMS);

        tblItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM selectedItem) {

                if (selectedItem == null) {
                    // Clear Selection
                    return;
                }

                txtItemCode.setText(selectedItem.getCode());
                txtDescription.setText(selectedItem.getDescription());
                txtUnitPrice.setText(selectedItem.getUnitPrice() + "");
                txtQty.setText(selectedItem.getQtyOnHand() + "");

                txtItemCode.setEditable(false);

                btnSave.setDisable(false);
                btnDelete.setDisable(false);

            }
        });
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        AppInitializer.navigateToHome(root, (Stage) root.getScene().getWindow());
    }

    @FXML
    private void btnSave_OnAction(ActionEvent event) throws SQLException {

        if (txtItemCode.getText().trim().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Item Code is empty",ButtonType.OK).showAndWait();
            txtItemCode.requestFocus();
            return;
        }else if(txtDescription.getText().trim().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Item Description is empty",ButtonType.OK).showAndWait();
            txtDescription.requestFocus();
            return;
        }else if(txtUnitPrice.getText().trim().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Unit Price is empty",ButtonType.OK).showAndWait();
            txtUnitPrice.requestFocus();
            return;
        }else if (txtQty.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Qty On Hand is empty", ButtonType.OK).showAndWait();
            txtQty.requestFocus();
            return;
        }else if(!isDouble(txtUnitPrice.getText()) || Double.parseDouble(txtUnitPrice.getText())< 0){
            new Alert(Alert.AlertType.ERROR,"Invalid Unit Price",ButtonType.OK).showAndWait();
            txtUnitPrice.requestFocus();
            return;
        }else if(!isInt(txtQty.getText())){
            new Alert(Alert.AlertType.ERROR, "Invalid Qty", ButtonType.OK).showAndWait();
            txtQty.requestFocus();
            return;
        }

        if (tblItems.getSelectionModel().isEmpty()) {
            // New

            ObservableList<ItemTM> items = tblItems.getItems();
            for (ItemTM itemTM : items) {
                if (itemTM.getCode().equals(txtItemCode.getText())){
                    new Alert(Alert.AlertType.ERROR,"Duplicate Item Codes are not allowed").showAndWait();
                    txtItemCode.requestFocus();
                    return;
                }
            }

            ItemTM itemTM = new ItemTM(txtItemCode.getText(), txtDescription.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQty.getText()));
            tblItems.getItems().add(itemTM);
            ItemDTO itemDTO = new ItemDTO(txtItemCode.getText(), txtDescription.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQty.getText()));
            boolean result = false;
            try {
                result = manageItemsBO.createItem(itemDTO);
            } catch (Exception e) {
                Logger.getLogger("").log(Level.SEVERE, null, e);
            }

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Item has been saved successfully", ButtonType.OK).showAndWait();
                tblItems.scrollTo(itemTM);
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to save the item", ButtonType.OK).showAndWait();
            }

        } else {
            // Update

            ItemTM selectedItem = tblItems.getSelectionModel().getSelectedItem();
            selectedItem.setDescription(txtDescription.getText());
            selectedItem.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
            selectedItem.setQtyOnHand(Integer.parseInt(txtQty.getText()));
            tblItems.refresh();

            String selectedItemCode = tblItems.getSelectionModel().getSelectedItem().getCode();

            boolean result = false;
            try {
                result = manageItemsBO.updateItem(new ItemDTO(txtItemCode.getText(), txtDescription.getText(),
                        Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQty.getText())));
            } catch (Exception e) {
                Logger.getLogger("").log(Level.SEVERE, null, e);
            }

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Item has been updated successfully", ButtonType.OK).showAndWait();
            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to update the item", ButtonType.OK).showAndWait();
            }
        }

        reset();

    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) throws SQLException {

        Alert confirmMsg = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this item?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = confirmMsg.showAndWait();

        if (buttonType.get() == ButtonType.YES) {
            String selectedRow = tblItems.getSelectionModel().getSelectedItem().getCode();

            tblItems.getItems().remove(tblItems.getSelectionModel().getSelectedItem());
            boolean result = false;
            try {
                result = manageItemsBO.deleteItem(selectedRow);
            } catch (Exception e) {
                Logger.getLogger("").log(Level.SEVERE, null, e);
            }
            if (!result){
                new Alert(Alert.AlertType.ERROR,"Failed to delete the item", ButtonType.OK).showAndWait();
            }else{
                reset();
            }
        }

    }

    private void reset() {
        txtItemCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQty.clear();
        txtItemCode.requestFocus();
        txtItemCode.setEditable(true);
        btnSave.setDisable(false);
        btnDelete.setDisable(true);
        tblItems.getSelectionModel().clearSelection();
    }

    @FXML
    private void btnAddNew_OnAction(ActionEvent actionEvent) {
        reset();
    }

    private boolean isInt(String number){
//        try {
//            Integer.parseInt(number);
//            return true;
//        }catch (NumberFormatException ex){
//            return false;
//        }
        char[] chars = number.toCharArray();
        for (char aChar : chars) {
            if (!Character.isDigit(aChar)){
                return false;
            }
        }
        return true;
    }

    private boolean isDouble(String number){
        try {
            Double.parseDouble(number);
            return true;
        }catch(Exception ex){
            return false;
        }
    }

}
