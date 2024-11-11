package pos.system.project.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pos.system.project.dto.BadgeDTO;
import pos.system.project.dto.ItemDTO;
import pos.system.project.entity.Badge;
import pos.system.project.entity.Category;
import pos.system.project.entity.Item;
import pos.system.project.entity.tm.ItemTM;
import pos.system.project.service.BadgeService;
import pos.system.project.service.CategoryService;
import pos.system.project.service.ItemService;
import pos.system.project.service.impl.BadgeServiceImpl;
import pos.system.project.service.impl.CategoryServiceImpl;
import pos.system.project.service.impl.ItemServiceImpl;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * @author Amil Srinath
 */
public class ItemController {
    ItemService itemService = new ItemServiceImpl();
    BadgeService badgeService = new BadgeServiceImpl();
    CategoryService categoryService = new CategoryServiceImpl();

    public static List<Category> categoryList;
    public static List<Item> itemList;
    public static List<Badge> badgeList;
    private String base64Image;
    ObservableList<Item> observableList;
    ObservableList<Badge> observableListBadge;

    int currentBadgeSelectedId = -1;
    int currentItemSelectedId = -1;

    //i want to set this image for imageView
    String getBase64Image = "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAACXBIWXMAAAsTAAALEwEAmpwYAAAOI0lEQVR4nO1dC" +
            "XAUVRruObsDqOu6HrtWrbtW6Z66bgnpISEGckACOYEQSEKO7iGJhIQzhEMNlxI5ww0BgSBXSLiUIILKoSBXAAVEVEAUCLjisep6Yf6" +
            "t/yUzTnfPhJ6ZnnQ69F/1VaUmyevu/+v3X+9/byhKF1100UUXXXRp4/J4riXIlnk/3THnQR05snTAhOb+kYoppJUjISXFRLNcNs3y" +
            "exiWv8HYeNDBe6uDBtrGHWVYbgRlSwnymQtklwykkwBK6YBm+Y+DOnEdvSaDmCaWv6STwQfAInDfB" +
            "dn4YK8IoW3cPslAXfLBmlQK1tSpOlJl6iB5ItDhQ9zNlEtUCHebLDKsNj5ePIBl0GKg1n4CVM1nOmq81MGGejCPXA9MSK6YmGfkzQ6W" +
            "rxKQkTNPJ6HG/xfRXFwtJITlPpRJCHfR+U+d7WBY8aFOSI0ClqH6KtCRwwSktO+cf89NCWFs/I9OW9e1UCejRjkzbe1fJiCkXYj9MTm" +
            "E/Op8IofphNQoSEjGTAEhsqItVQipvgaGynNgWPQuGGceAOPCE2BYeY583pYCiVZPiHHW22DJrQA6aoT7RCqiCCz2hWB8fl+bIKfVEm" +
            "KcWwfWvs96lVBZk0rJ7FFbqW2OENPT24AJGeRbltvZDuaiVSRiUVu5bYIQS+4Sj8p+OC4LEvPSIHNYKiTnD4B/JmV6/FtLxkxNktKqC" +
            "CHZqkixt3fhoWh8Cry3twc0fNhNgo/2d4cxk/vCb8M5KSmDX1BdwZolxDjnMDE3rmM/kpwJJ3e7J0KMc/ujgU3NkJBimrpbdSVrj5Dq" +
            "a2BNmiC4kUeTMuHz45GyyGhowjenIiA0TUgKHTMaqKorqitaU4QYp78luIk7unDwwZvdvSKjoQn1R6Lg3ogc4SyZuEN1RWuKEKwUu44" +
            "5dlJfn8hoaMLsOcnCcDhtuuqK1hQhdORQwU2c3efb7GhoApq6DiEuTj40F6iqetWVrQ1C1l0S3MBf4rL8IqOhCWHpQl9iWPa+6srWBC" +
            "GGivcENxCVla4IIWlF/QXjGssPq65sbRCy7IzgBiIUImRAUaqQkDk6ITJN1mWB4h7qpYzJChWFv7rJ8mKW0FHCVTJPWblcfHYsCtqHu" +
            "CSIoXm6U/eGEEuesH41qjTFL0KmzRKFvRkzVPcNmvEhCOOMA5L61SmZJZMGET49FA13dxUlhpNfVV3R2iud9J4ouJG/JWTB1aNRXpHx" +
            "1bsRENxPVDrpOYa02aitaG0RgrNkXp2kuIg5Sd3OGFlknNnXA/7dR1qON057U3Ula5IQhLlko0Sh6JwHFfeDY7t6wC8fSIk4ubsHDHu" +
            "qLzFz4v81F1WqrmBNE4LANQyxYh34U0wWSRwx6evBpZMQmfHwt5bsuZpcY291hDiXcEPzPCpb3hLuNdWVqwohWLhDBSq9XIptPtYBz3" +
            "tFhrXvZDDOOaK6UtUlxDFT4saBccZ+xW/QOO8omAtWAB1T7JYEOnokWPIrSLuQotdedwlMU3aRvmV8NseMpbsVko5185CVjeQrPBMVI" +
            "8T5lvYvA8Pik4F5g9Z+CoaK00QRhiUngVpzUflrVF0hTc90tyJ5szLxGTA99wYYy/aCaexmYi7NhZXkJSE/j64G06SdYJxfR+6/xQlp" +
            "tOODSPZtqDwfGGICBEPFabAmPO2b75Ll3wYRAnHGk+qzm9nlPyFhucCEe3DATwwGc3EVUOsvq65sSs4ycniBZ2V28bFPrBlgN6Z5+Fr" +
            "SIqsYIXRsERg+eAss46cDE2L3aOdJ+aKVRj6mcVvI2yu5794jwVS5CsyLlgEdLyyAKorQXLDkLwXDqgvKEEJ9+Q6B8dBOsNqfab7Vsz" +
            "UtFG2olxQ2CULsYC5fDMY9tWDNHNesMnGpuPOADOBGpcLUGb1h6ZJEWP9iPNSsiYNlFQlQPjcJhoxNge45aXCXm94xoUUpADpquHKEO" +
            "GCq3QR0n5EeL2zJnE0WptQkw7D6Y7D2e056f13zwfTyRjDPr/A447EpL3tEKrxU1YvUz+TW2n58PwL218ZCyaQ+JMmVM3sUIYTgs2Ng" +
            "XrES6GgPdjk0j4SOhkBESjU3IaPiNOnXkpio+GFgfP1lsBYIC50OoBKxo+Xrk971i7nDz2e7wcY18W6b+wJDSBMMnxwCc9n8Rufvzr9" +
            "0LSThYktVZI0z9rvd9WrNHg/GgzuBThKaDMRvwjiYPK03/O89+bNBLrA+V7UqHv4ck+1eP8H2AkUJcRJzei9YRroxEY5xepaAqWxPQM" +
            "kwjd/qtqveMmYaGE7tBTpZugcF/YOvTXveLh2gH5LohpyOkZOpOCFOx//m9mYdJe4JMS44HgDnXeG+7rVoGRjOHXDr89AZf39G+VnRH" +
            "JYvS5BWrln+RhDL9QkIIQRfnADTSzVAJ0rNg0NRlsxyMLxw1n9/seaie+cdlgemzRuAun4CrHmlkt+Pn9KnRYlwxRtbesI93SQR2bcW" +
            "W/YjgSHEgWt15A1luj3pnpiwfDAPX0PqSj6RsfQ00LElUvMYNxSMR3aRe8DwVvz7spnJqpHhwN6XY4nvEt3bOSo4/fbAEeLwL+cPgGX" +
            "ibM+JZdQwMJXWelVRNs444N55Z40Dw7m3G83n7lrJNYc/7V+DhZLYvC4OgjqL9cEtDDghTmKOv+4x5CTXih9PFC3vmIpBUudd8jyZle" +
            "R6n58Auu8owe+7DkwnuYLaRLgCTaeIkF+YYHtYixDidPy7tgKdWtyM458MpgmvNPqYDVdJhRa3S5tLatyX6dF5L1hKfJczeV29WvA32" +
            "Kly8WC06gSI8dPZCOgi3vPCcocpijK0GCEE10+AqWY9Gd8TMbIQlgemTVXCseuPSsadMy9JdeV7AvYTCBr/8KW08fEtS4gDl4+AedZC" +
            "olhvyaBTi8FQ95pkTKwgiHdr4ZuohPIwA8caFu5x8XdbhSueHJMiniWH1CHE4V/OvtXo+D1k/IzrPfUoAPOS5b/6CxGsacLIa+ULCYq" +
            "RkTOyn3Nc3Ml1YHusImPjHkrBnhf0iY4wWA1CnMRcOAimdWvBUlIG1oyxJLtGWLOfAsuk2WDavskjEeT/614TPNQDMdmKOPIfznSDfk" +
            "MGSF4OrPBiXqEEKelDhVssaJabpToh/sI8fb7goUZP9G/7HAKz+b6DpWS4Vod3bfaflO3VvcRjX9A8Idb0MYKHOvqqvM5IT/j2VAT0s" +
            "qfd1IziRtat6+P8uhb6uT9ECfuWrWz2Q9olpP4oMKG/JoK/j8yBG2d9VxBuw47hb06GA1ijwmTPH1L6F4rMlo3P0ywhxn21godJKejv" +
            "s2K+fCcSwjOaX8twBwxfV6+I9/m6CxYmisdcoFlCzMuF4e6Eqb4VEL84ESVJ1hB4Dgu2uIrrYnc+wUlIqVye4HONS3hd7nXtEjJrkeB" +
            "h1qxM8GlBCfuLxWTgbMNIKylf6NwxqXttS0/JWSztQnjyubfXv3IkWhxpXaQYlvvZ+UGPAtUVTckEhsWuD+OLQi4djpKQkTEs1Rk6uy" +
            "MEP9+3LVaykWjoU94XMtHnIZkuM+QrPET5U+cHIXaSG2iCkOKpAoUcfMX7pA0Vj4fiOMbgR6WShNDxe0+EIPB6rsd+YGeKL2ZLQCzL3" +
            "8Bzeze5XtQyqVx1ZVMyYB0yWZGQ91pdFGnvwYhJvGelOUIQF96Ohumzk+HVTb18ujbi/mjh+jvF2HL6Saqp5YtJp0mrniEjhCuH/ijF" +
            "E25GiBLAnMZlhnxPUVSpkWb5Y2JbSscWgnXoFLAUl7VK0KLuw7WV8ZojBDtfRE79GsnWMUOkWe66t3F4a8Lc+UmaI+T0nu5iQo47q76" +
            "WTvZ/MCz3kdqKZXyEL1GO2oRsWSesZ+E5/ML1XFtKEMNyJQzLv6+2ghkvgcdvaI0Q8ZIuzfITPLYHteuUdR+2PtK2nKhWCZaPxi9Lc" +
            "TzMbaEcybq1RIjkCENbThSlZWFYbkcgHXsgCbl8RHSGC8v94Nd3VrUGsbLcUHH9SSuEzCpPEppdlnuF0rq065R1n2vpB8sQmKwppTRx" +
            "edzfk40cwGoArv0L/SCXTrUFYWz8NtcHKxzXTzFCMHt3mJUnMtIFZRV/sOHFeHFQ8jX1eG47qi0IHWyPEC8c4QnZSuYKtdW9FNu+gFX" +
            "kfyUPFDlzbirVloRmuUOuD5iQO0AxQpQG1r5Es+PbDl1y7qbakjDB9jD8hk3XB/V10SiQwHPDxGspzeYeWhaa5daKW3bknjXfEsA1e8" +
            "mRUyz3ERWexVBtUTo8nvs72sZdcX3gv8ZnknhfbTKwwwS/kkMUVf2C/o9qy0Lb+BjSUe7y4B1TBnp9mp3SZLjb3kaz3ETqVhDGxo8WP" +
            "zyuCp4/EPi9hWJ8d9pj0912/GZu6lYRhuUXiZWATWk7Niq/iOUJuLlUfGZkY4jLH6QezWhP3WJiYFhuqVgZmMnjEYJK7E1vrnFh8eJE" +
            "SRNEk5k6fFungXdRt6gYaBs3xV2pHveRV1QkKrZtwQHs+Q1x0+PVFFHtoMIHd6BudWFYPoNhuW/cKQnPecSDmrE/ylcS/nsqEl5cES/" +
            "5BgcXNNA2vowKLzWrrYtWI9ZO9ofRdntQGDFl+MUBz81Iht1bY+H6ichmCcDjbuctSCINdc0dQoMtVhj5qf38rVRKjdjMTNu4/3hSoC" +
            "uw5worsnj6A5qhx3oP9HhkhhTcT7SNn66bKDkSPrgDw/JjaBt3VZ5yvQDLf48N00xn7gFZ96KLi/w9xdrUk7atSZG+EtGAxU3axg/Ba" +
            "oHrJXTxVR7NaI87YWmWf5Zh+V2MjTvvuvAlyiPqGRu3Gzf9MyzXvz3L3+vzdXXxQsJLzXd0efJOND90x5wH8Wc9UtJFF1100UUXXSi5" +
            "8n+TO+hdoyH3kwAAAABJRU5ErkJggg==";

    @FXML
    public ComboBox<String> cmbQuantityType;
    @FXML
    public TableColumn<?,?> colItemQtyType;
    @FXML
    public TextField itemDescription;
    @FXML
    public ImageView iconEdit;
    @FXML
    public ImageView imageView;
    @FXML
    public TableColumn<?,?> colTableItemID;
    @FXML
    public TableColumn<Item,String> colItemDescription;
    @FXML
    public TableColumn<Item,String> colItemSellBy;
    @FXML
    public TableView<Badge> tblBadge;
    @FXML
    public TableColumn<Badge, Integer> colItemID;
    @FXML
    public TableColumn<?,?> colItemQty;
    @FXML
    public TextField itemImage;
    @FXML
    public Button btnBack;
    @FXML
    public TextField itemSellingPrice;
    @FXML
    public TextField itemPurchasePrice;
    @FXML
    public TextField itemQty;
    @FXML
    public TextField itemName;
    @FXML
    public Text lblPurchasePrice;
    @FXML
    public Text lblQuantity;
    @FXML
    public Text lblSellingPrice;
    @FXML
    public ComboBox<String> cmbSellBy;
    @FXML
    public TableView<Item> tblItem;
    @FXML
    public TableColumn<?,?> colItemExpireDate;
    @FXML
    public TableColumn<?,?> colItemName;
    @FXML
    public TableColumn<Item,String> colItemImage;
    @FXML
    public TableColumn<?,?> colItemBarcode;
    @FXML
    public TableColumn<Item,String> colItemCategory;
    @FXML
    public TableColumn<?,?> colItemPurchasePrice;
    @FXML
    public TableColumn<?,?> colItemSellingPrice;
    @FXML
    public DatePicker itemExpireDate;
    @FXML
    public ComboBox<String> cmbCategory;
    @FXML
    public TextField itemBarcode;

    @FXML
    public void initialize() throws IOException {
        cmbQuantityType.setVisible(false);
        cmbSellBy.getItems().addAll("Unit", "Fraction","Liquid");
        cmbSellBy.getSelectionModel().select(0);

        cmbQuantityType.getItems().addAll("ml", "kg","g","l");
        cmbQuantityType.getSelectionModel().select(0);

        cmbSellBy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Unit")) {
                lblQuantity.setText("Quantity");
                lblPurchasePrice.setText("Purchase Price(unit)");
                lblSellingPrice.setText("Selling Price(unit)");
                cmbQuantityType.setVisible(false);
            } else if (newValue.equals("Fraction")) {
                lblQuantity.setText("Quantity(Kg)");
                lblPurchasePrice.setText("Purchase Price(1Kg)");
                lblSellingPrice.setText("Selling Price(1Kg)");
                cmbQuantityType.setVisible(false);
            } else if (newValue.equals("Liquid")) {
                lblQuantity.setText("Quantity(ml)");
                lblPurchasePrice.setText("Purchase Price(187.5ml)");
                lblSellingPrice.setText("Selling Price(187.5ml)");
                cmbQuantityType.setVisible(true);
                cmbQuantityType.getSelectionModel().select(0);
            }
        });

        cmbQuantityType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("ml")) {
                lblQuantity.setText("Quantity(ml)");
                lblPurchasePrice.setText("Purchase Price(187.5ml)");
                lblSellingPrice.setText("Selling Price(187.5ml)");
            } else if (newValue.equals("kg")) {
                lblQuantity.setText("Quantity(kg)");
                lblPurchasePrice.setText("Purchase Price(187.5ml)");
                lblSellingPrice.setText("Selling Price(187.5ml)");
            } else if (newValue.equals("g")) {
                lblQuantity.setText("Quantity(g)");
                lblPurchasePrice.setText("Purchase Price(187.5ml)");
                lblSellingPrice.setText("Selling Price(187.5ml)");
            } else if (newValue.equals("l")) {
                lblQuantity.setText("Quantity(l)");
                lblPurchasePrice.setText("Purchase Price(187.5ml)");
                lblSellingPrice.setText("Selling Price(187.5ml)");
            }
        });

        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();
        getAllItems();
        getAllBadges();
        iconEdit.setVisible(false);
        getAll();
    }


    public void getAll() throws IOException {
        List<ItemTM> itemTMList = itemService.getAll();
        for (ItemTM itemTM : itemTMList) {
            System.out.println(itemTM.getItemId());
            System.out.println(itemTM.getBadgeId());
        }
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) {
        btnBack.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void AddOnAction(ActionEvent actionEvent) throws IOException {
        BigDecimal quantity = null;
        double sellPrice = Double.parseDouble(itemSellingPrice.getText());
        BigDecimal lastSellPrice = null;

        int sellBy = -1;
        int quantityType = -1;
        switch (cmbSellBy.getSelectionModel().getSelectedIndex()) {
            case 0:
                sellBy = 1; // Unit
                break;
            case 1:
                sellBy = 2; // Fraction
                break;
            case 2:
                sellBy = 3; // Liquid
                break;
            default:
                break;
        }

        if (sellBy == 1) {
            quantity = new BigDecimal(itemQty.getText());
        } else if (sellBy == 2) {
            quantity = new BigDecimal(itemQty.getText()).multiply(BigDecimal.valueOf(1000));
        } else if (sellBy == 3) {
            String itemQtyText = itemQty.getText();
            switch (cmbQuantityType.getSelectionModel().getSelectedIndex()) {
                case 0://ml
                    quantityType = 1;
                    quantity = new BigDecimal(itemQtyText);
                    break;
                case 1://kg
                    quantityType = 2;
                    quantity = new BigDecimal(itemQtyText);
                    quantity = quantity.multiply(BigDecimal.valueOf(1100));
                    break;
                case 2://g
                    quantityType = 3;
                    quantity = new BigDecimal(itemQtyText);
                    quantity = quantity.divide(BigDecimal.valueOf(1000));
                    quantity = quantity.multiply(BigDecimal.valueOf(1100));
                    break;
                case 3://l
                    quantityType = 4;
                    quantity = new BigDecimal(itemQtyText);
                    quantity = quantity.multiply(BigDecimal.valueOf(1000));
                    break;
                default:
                    break;
            }
        }

        for (Item existingItem : itemList) {
            if (existingItem.getItemBarcode().equals(itemBarcode.getText())) {
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION,
                        "This item already exists. Do you want to add a badge?",
                        yes, no).showAndWait();

                if (result.orElse(no) == yes) {
                    badgeService.addBadge(
                            new Badge(
                                    0,
                                    itemDescription.getText(),
                                    Double.parseDouble(itemPurchasePrice.getText()),
                                    Double.parseDouble(itemSellingPrice.getText()),
                                    quantity,
                                    itemExpireDate.getValue().toString(),
                                    1,
                                    quantityType,
                                    existingItem
                            )
                    );
                }

                itemList = itemService.getAllItems();
                badgeList = badgeService.getAllBadges();
                getAllItems();
                getAllBadges();

                return;
            }
        }

        itemService.addItem(
                new ItemDTO(
                        itemName.getText(),
                        itemBarcode.getText(),
                        sellBy,
                        cmbCategory.getValue(),
                        base64Image
                ),
                new BadgeDTO(
                        itemDescription.getText(),
                        Double.parseDouble(itemPurchasePrice.getText()),
                        Double.parseDouble(itemSellingPrice.getText()),
                        quantity,
                        itemExpireDate.getValue().toString(),
                        quantityType
                )
        );
        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();
        getAllItems();
        getAllBadges();
        ClearOnAction(actionEvent);
    }

    @FXML
    public void EditOnAction(ActionEvent actionEvent) throws IOException {
        if (base64Image == null) {
            for (Item item : itemList) {
                if (item.getItemId() == currentItemSelectedId) {
                    base64Image = item.getImageUrl();
                }
            }
        }

        BigDecimal quantity = new BigDecimal(itemQty.getText());
        int quantityType = -1;
        int sellBy = -1;
        switch (cmbSellBy.getSelectionModel().getSelectedIndex()) {
            case 0:
                sellBy = 1; // Unit
                break;
            case 1:
                sellBy = 2; // Fraction
                break;
            case 2:
                sellBy = 3; // Liquid
                break;
            default:
                break;
        }

        if (sellBy == 1) {
            quantity = new BigDecimal(itemQty.getText());
        } else if (sellBy == 2) {
            quantity = quantity.multiply(BigDecimal.valueOf(1000));
        } else if (sellBy == 3) {
            String itemQtyText = itemQty.getText();
            switch (cmbQuantityType.getSelectionModel().getSelectedIndex()) {
                case 0://ml
                    quantityType = 1;
                    quantity = new BigDecimal(itemQtyText);
                    break;
                case 1://kg
                    quantityType = 2;
                    quantity = new BigDecimal(itemQtyText);
                    quantity = quantity.multiply(BigDecimal.valueOf(1100));
                    break;
                case 2://g
                    quantityType = 3;
                    quantity = new BigDecimal(itemQtyText);
                    quantity = quantity.divide(BigDecimal.valueOf(1000));
                    quantity = quantity.multiply(BigDecimal.valueOf(1100));
                    break;
                case 3://l
                    quantityType = 4;
                    quantity = new BigDecimal(itemQtyText);
                    quantity = quantity.multiply(BigDecimal.valueOf(1000));
                    break;
                default:
                    break;
            }
        }

        ItemDTO itemDTO = new ItemDTO(
                itemName.getText(),
                itemBarcode.getText(),
                sellBy,
                cmbCategory.getValue(),
                base64Image
        );

        BadgeDTO badgeDTO = new BadgeDTO(
                itemDescription.getText(),
                Double.parseDouble(itemPurchasePrice.getText()),
                Double.parseDouble(itemSellingPrice.getText()),
                quantity,
                itemExpireDate.getValue().toString(),
                quantityType
        );

        itemService.updateItem(itemDTO,currentItemSelectedId, currentBadgeSelectedId,badgeDTO);
        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();
        getAllItems();
        getAllBadges();
        ClearOnAction(actionEvent);
        base64Image = null;
    }

    @FXML
    public void DeleteOnAction(ActionEvent actionEvent) throws IOException {
        if (currentItemSelectedId == -1 || currentBadgeSelectedId == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select badge to delete").showAndWait();
            return;
        }

        // Create a confirmation alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Choose an option");
        alert.setContentText("Do you want to delete this badge, item, or all?");

        // Create custom buttons
        ButtonType deleteBadgeButton = new ButtonType("Delete Badge");
        ButtonType deleteItemButton = new ButtonType("Delete Item");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Add the custom buttons to the alert dialog
        alert.getButtonTypes().setAll(deleteBadgeButton, deleteItemButton, cancelButton);

        // Wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        // Handle the button click events
        if (result.isPresent()) {
            if (result.get() == deleteBadgeButton) {
                System.out.println("Delete Badge selected");
                badgeService.deleteBadge(currentBadgeSelectedId);
                getAllBadges();
                ClearOnAction(actionEvent);
            } else if (result.get() == deleteItemButton) {
                System.out.println("Delete Item selected");
                itemService.deleteItem(currentItemSelectedId);
                badgeService.deleteBadgeByItemId(currentItemSelectedId);
                getAllItems();
                getAllBadges();
                ClearOnAction(actionEvent);
            }else {
                ClearOnAction(actionEvent);
            }
        }
    }

    @FXML
    public void tableOnMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Badge badge = tblBadge.getSelectionModel().getSelectedItem();

            itemDescription.setText(badge.getDescription());
            itemBarcode.setText(badge.getItem().getItemBarcode());
            itemPurchasePrice.setText(String.valueOf(badge.getPurchasePrice()));
            itemSellingPrice.setText(String.valueOf(badge.getSellingPrice()));

            double qty = badge.getQuantity().doubleValue();

            if (badge.getItem().getSellByStatus() == 1) {
                itemQty.setText(String.valueOf(qty));
            } else if (badge.getItem().getSellByStatus() == 2) {
                itemQty.setText(String.valueOf(qty));
            } else if (badge.getItem().getSellByStatus() == 3) {
                if (badge.getQuantityType() == 1) {
                    itemQty.setText(String.valueOf(qty));
                } else if (badge.getQuantityType() == 2) {
                    itemQty.setText(String.valueOf(qty / 1100));
                } else if (badge.getQuantityType() == 3) {
                    itemQty.setText(String.valueOf(qty/1100*1000));
                }else if (badge.getQuantityType() == 4) {
                    itemQty.setText(String.valueOf(qty / 1000));
                }
            }

            itemExpireDate.setValue(LocalDate.parse(badge.getExpireDate()));

            cmbSellBy.setValue(1 == badge.getItem().getSellByStatus() ? "Unit" : 2 == badge.getItem().getSellByStatus() ? "Fraction" : "Liquid");
            cmbQuantityType.setValue(badge.getQuantityType() == 1 ? "ml" : badge.getQuantityType() == 2 ? "kg" : badge.getQuantityType() == 3 ? "g" : badge.getQuantityType() == 4 ? "l" : "Unit");

            for (Item i : itemList) {
                if (i.getItemId() == badge.getItem().getItemId()) {
                    cmbCategory.setValue(String.valueOf(i.getCategory().getName()));
                    itemName.setText(i.getItemName());

                    Image image = null;
                    byte[] imageBytes;
                    try {
                        imageBytes = Base64.getDecoder().decode(i.getImageUrl());
                        image = new Image(new ByteArrayInputStream(imageBytes));
                    }catch (Exception e){
                        imageBytes = Base64.getDecoder().decode(getBase64Image);
                        image = new Image(new ByteArrayInputStream(imageBytes));
                    }

                    // Display the image in the ImageView
                    imageView.setImage(image);
                    imageView.setFitHeight(117);  // Adjust the height of the image
                    imageView.setFitWidth(129);   // Adjust the width of the image
                    imageView.setPreserveRatio(true); // Maintain image aspect ratio
                    break;
                }
            }

            currentBadgeSelectedId = badge.getBadgeId();
            currentItemSelectedId = badge.getItem().getItemId();
            disableFields();
        }
    }

    private void disableFields() {
        itemBarcode.setDisable(true);
        cmbSellBy.setDisable(true);
        cmbQuantityType.setDisable(true);
        cmbCategory.setDisable(true);
    }

    @FXML
    public void ClearOnAction(ActionEvent actionEvent) {
        itemBarcode.setDisable(false);
        cmbSellBy.setDisable(false);
        cmbQuantityType.setDisable(false);
        cmbCategory.setDisable(false);

        itemBarcode.clear();
        itemPurchasePrice.clear();
        itemSellingPrice.clear();
        itemQty.clear();
        itemExpireDate.getEditor().clear();
        itemName.clear();
        itemDescription.clear();
        cmbCategory.setValue(null);
        cmbSellBy.setValue("Unit");

        currentItemSelectedId = -1;
        currentBadgeSelectedId = -1;

        // Convert base64 string to Image
        try {
            byte[] imageBytes = Base64.getDecoder().decode(getBase64Image);
            InputStream imageInputStream = new ByteArrayInputStream(imageBytes);
            Image image = new Image(imageInputStream);

            // Set the image to the ImageView
            imageView.setImage(image);
            imageView.setFitHeight(117); // Adjust the height of the image
            imageView.setFitWidth(129);  // Adjust the width of the image
            imageView.setPreserveRatio(true); // Maintain image aspect ratio

            imageInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cmbCategoryOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        getAllCategories();
        cmbCategory.getSelectionModel().clearSelection();
    }

    @FXML
    public void addCategoryOnMouseClicked(MouseEvent mouseEvent) {
        try {
            // Load the AddCategory FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCategory.fxml"));
            AnchorPane anchorPane = loader.load();

            // Create a new stage for the AddCategory window
            Stage stage = new Stage();
            stage.setTitle("Add Category");
            stage.setScene(new Scene(anchorPane));

            // Set the stage modality to block interaction with other windows
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnBack.getScene().getWindow());

            // Make the stage non-resizable
            stage.setResizable(false);

            // Show the window and wait until it's closed
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAllItems() throws IOException {
        itemList = itemService.getAllItems();
        observableList = FXCollections.observableArrayList();

        for (Item item : itemList) {
            if (item.getStatus() == 1) {
                observableList.add(item);
            }
        }
        tblItem.setItems(observableList);

        // Set other table columns
        colTableItemID.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colItemBarcode.setCellValueFactory(new PropertyValueFactory<>("itemBarcode"));
        colItemSellBy.setCellValueFactory(cellData -> {
            Item item = cellData.getValue(); // Get the Item object
            if (item != null) {
                int sellByStatus = item.getSellByStatus(); // Get the sellByStatus value
                // Return "Unit" if 1, "Fraction" if 2
                String sellByType = (sellByStatus == 1) ? "Unit" : (sellByStatus == 2) ? "Fraction" : (sellByStatus == 3) ? "Liquid":"Unknown";
                return new SimpleObjectProperty<>(sellByType);
            }
            // If item is null, return "Unknown"
            return new SimpleObjectProperty<>("Unknown");
        });
        colItemCategory.setCellValueFactory(cellData -> {
            Item item = cellData.getValue(); // Get the Item object from the Badge
            if (item != null) {
                Category category = item.getCategory(); // Get the Category object from the Item
                if (category != null) {
                    // Return the category name as a SimpleObjectProperty to bind it to the TableColumn
                    return new SimpleObjectProperty<>(category.getName());
                }
            }
            // If item or category is null, return an empty string or any default value for category name
            return new SimpleObjectProperty<>("");
        });

        // Handle the item image column with Base64 to Image conversion
        colItemImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        colItemImage.setCellFactory(column -> new TableCell<Item, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String base64Image, boolean empty) {
                super.updateItem(base64Image, empty);

                if (empty || base64Image == null) {
                    setGraphic(null);
                } else {
                    // Decode the Base64 image and create an ImageView
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    imageView.setImage(image);
                    imageView.setFitHeight(70); // Set height of the image (adjust as needed)
                    imageView.setFitWidth(70);  // Set width of the image (adjust as needed)
                    setGraphic(imageView);
                    setAlignment(Pos.CENTER);  // Center the image in the cell
                }
            }
        });
    }

    private void getAllBadges() throws IOException {
        // Fetch the list of badges from the service
        badgeList = badgeService.getAllBadges();

        // Initialize the observable list for the table
        observableListBadge = FXCollections.observableArrayList();

        // Filter badges where both Badge.status and Item.status are equal to 1
        for (Badge badge : badgeList) {
            Item item = badge.getItem(); // Get the associated Item
            if (badge.getStatus() == 1 && item != null && item.getStatus() == 1) {
                // Only add the badge if both statuses are 1
                observableListBadge.add(badge);
            }
        }

        // Set the filtered badges to the TableView
        tblBadge.setItems(observableListBadge);

        // Set up TableColumn mappings
        colItemID.setCellValueFactory(cellData -> {
            Badge badge = cellData.getValue(); // Get the Badge object
            Item item = badge.getItem();       // Get the Item object from the Badge
            if (item != null) {
                // Return the itemId as a SimpleObjectProperty to bind it to the TableColumn
                return new SimpleObjectProperty<>(item.getItemId());
            } else {
                // If item is null, return a default value
                return new SimpleObjectProperty<>(0);
            }
        });
        colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colItemSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colItemExpireDate.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        colItemQtyType.setCellValueFactory(new PropertyValueFactory<>("quantityType"));
    }


    private void getAllCategories() throws IOException {
        categoryList = categoryService.getAllCategories();

        cmbCategory.getItems().clear();

        for (Category category : categoryList) {
            if (category.getStatus() == 1){
                cmbCategory.getItems().add(category.getName());
            }
        }
    }

    @FXML
    public void addImageOnMouseClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();

        // Set file extension filters to accept only png, jpg, and jpeg files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Convert the image to base64 string
                String base64String = imageToBase64(selectedFile);
                base64Image = base64String;

                // Display the selected image in the ImageView
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                Image image = new Image(fileInputStream);
                imageView.setImage(image);  // Set the image to the ImageView
                imageView.setFitHeight(117); // Adjust the height of the image
                imageView.setFitWidth(129);  // Adjust the width of the image
                imageView.setPreserveRatio(true); // Maintain image aspect ratio
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToBase64(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] imageBytes = new byte[(int) file.length()];
        fileInputStream.read(imageBytes);
        fileInputStream.close();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    @FXML
    public void imageViewOnMouseEntered(MouseEvent mouseEvent) {
        iconEdit.setVisible(true);
    }

    @FXML
    public void imageViewOnMouseExited(MouseEvent mouseEvent) {
        iconEdit.setVisible(false);
    }
}
