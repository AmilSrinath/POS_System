package pos.system.project.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pos.system.project.entity.OrderDetail;
import pos.system.project.entity.tm.SelectOrderDetail;

import java.util.List;

/**
 * @author Amil Srinath
 */
public class SelectOrderDetailsController {
    @FXML
    public TableColumn<?, ?> colOrderDetailID;
    @FXML
    public TableColumn<SelectOrderDetail, Integer> colItemID;
    @FXML
    public TableColumn<?, ?> colItemPrice;
    @FXML
    public TableColumn<?, ?> colQuantity;
    @FXML
    public TableColumn<?, ?> colSubTotal;
    @FXML
    public TableColumn<?, ?> colDate;
    @FXML
    public TableView<SelectOrderDetail> tblOrderDetails;
    @FXML
    public Label lblTotal;
    @FXML
    public Label lblOrderID;

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        lblOrderID.setText(String.valueOf(orderDetails.get(0).getOrder().getOrderId()));
        lblTotal.setText(String.valueOf(orderDetails.get(0).getOrder().getTotal()));

        ObservableList<SelectOrderDetail> observableList = FXCollections.observableArrayList();
        for (OrderDetail orderDetail : orderDetails) {
            int itemId = (orderDetail.getItem() != null)
                    ? orderDetail.getItem().getItemId()
                    : -1; // Use -1 as a placeholder for missing IDs

            observableList.add(
                    new SelectOrderDetail(
                            orderDetail.getOrderDetailId(),
                            itemId,
                            orderDetail.getItemPrice(),
                            orderDetail.getQuantity(),
                            orderDetail.getSubTotal(),
                            orderDetail.getCreatedDate()
                    ));
        }

        tblOrderDetails.setItems(observableList);

        // Format the `itemId` column to display `"-"` when the value is -1
        colOrderDetailID.setCellValueFactory(new PropertyValueFactory<>("orderDetailID"));
        colItemID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        colItemID.setCellFactory(column -> new TableCell<SelectOrderDetail, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item == -1) {
                    setText("-"); // Replace -1 with "-" in the UI
                } else {
                    setText(String.valueOf(item)); // Display the actual ID
                }
            }
        });
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
}
