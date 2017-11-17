package gui.country.combo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CountryInfo extends Application
{
    private ObservableList<Country> countries = FXCollections.observableArrayList(new Country("Germany", "Berlin", 82349400, 357168), new Country("Japan", "Tokyo", 13617445, 218766), new Country("South Korea", "Seoul", 51446201, 100210), new Country("Indonesien", "Jakarta", 261115456, 1904569));

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        VBox root = new VBox(6);

        ComboBox<Country> countrySelector = new ComboBox<Country>(countries);
        Callback<ListView<Country>, ListCell<Country>> cellFactory = showOnlyName();
        countrySelector.setButtonCell(cellFactory.call(null));
        countrySelector.setCellFactory(cellFactory);
        CheckBox cbExactVal = new CheckBox("exacte Angaben");

        // ============ LABELS =====================
        Label countryNameText = new Label("Land: ");
        Label capitalText = new Label("Hauptstadt: ");
        Label populationText = new Label("Einwohner: ");
        Label areaText = new Label("Fläche (in qkm): ");
        Label densityText = new Label("Bevölkerungsdichte (in Personen pro qkm): ");

        Label countryNameVal = new Label();
        Label capitalVal = new Label();
        Label populationVal = new Label();
        Label areaVal = new Label();
        Label densityVal = new Label();

        // Setting Up The ID
        countryNameVal.setId("countryName");
        capitalVal.setId("capital");
        populationVal.setId("population");
        areaVal.setId("area");
        densityVal.setId("density");

        // ===================== Input Field =========================
        TextField countryField = new TextField();
        TextField capitalField = new TextField();
        TextField populationField = new TextField();
        TextField areaField = new TextField();

        countryField.setId("countryField");
        capitalField.setId("capitalField");
        populationField.setId("populationField");
        areaField.setId("areaField");

        countryField.setPromptText("Land");
        capitalField.setPromptText("Hauptstadt");
        populationField.setPromptText("Einwohner");
        areaField.setPromptText("Fläche");

        populationField.textProperty().addListener((obs, oldVal, newVal) -> restrictToNumeric(populationField, oldVal, newVal));
        areaField.textProperty().addListener((obs, oldVal, newVal) -> restrictToNumeric(areaField, oldVal, newVal));

        // ==================== Buttons ==============================
        Button btnAdd = new Button("Hinzuf\u00fcgen");
        Button btnDel = new Button("L\u00f6schen");
        btnAdd.setId("add");
        btnDel.setId("delete");

        btnAdd.setOnAction(e -> addNewCountry(e, countryField, capitalField, populationField, areaField, countrySelector));
        btnDel.setOnAction(e -> deleteCountry(e, countrySelector));

        // ===================== LISTENERS ===========================
        // == And set up some id and value of checkbox and combobox ==
        // ===========================================================
        countrySelector.valueProperty().addListener((obs, oldVal, newVal) -> showSelectedCountry(countryNameVal, capitalVal, populationVal, areaVal, densityVal, newVal, cbExactVal.isSelected()));
        cbExactVal.selectedProperty().addListener((obs, oldVal, newVal) -> showSelectedCountry(countryNameVal, capitalVal, populationVal, areaVal, densityVal, countrySelector.getValue(), newVal.booleanValue()));

        countrySelector.setId("countrySelector");
        countrySelector.setValue(countries.get(0));
        cbExactVal.setSelected(false);

        // ============= GRID FOR LABELS ================
        GridPane infoLayout = new GridPane();
        infoLayout.addColumn(0, countryNameText, capitalText, populationText, areaText, densityText);
        infoLayout.addColumn(1, countryNameVal, capitalVal, populationVal, areaVal, densityVal);

        // ============= HBOX FOR INPUT FIELD ===========
        HBox inputLayout = new HBox(5);
        inputLayout.getChildren().addAll(countryField, capitalField, populationField, areaField, btnAdd);

        root.getChildren().addAll(countrySelector, cbExactVal, infoLayout, inputLayout, btnDel);
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("L\u00e4nder-Informationen");
        primaryStage.show();
    }

    private void deleteCountry(ActionEvent e, ComboBox<Country> countrySelector)
    {
        countrySelector.getItems().remove(countrySelector.getValue());
        if (countrySelector.getItems().size() > 0)
        {
            countrySelector.setValue(countries.get(0));
        }
        else
        {
            countrySelector.getItems().add(null);
            countrySelector.setButtonCell(showOnlyName().call(null));
        }
    }

    private void showSelectedCountry(Label displayCountry, Label displayCapital, Label displayPopulation, Label displayArea, Label displayDensity, Country selectedCountry, boolean isExact)
    {
        if (selectedCountry != null)
        {
            displayCountry.setText(selectedCountry.getName());
            displayCapital.setText(selectedCountry.getCapital());

            if (isExact)
            {
                displayPopulation.setText(String.format("%,d", selectedCountry.getPeople()));
                displayArea.setText(String.format("%,d", selectedCountry.getArea()));
                displayDensity.setText(String.format("%,d", selectedCountry.getDensity()));
            }
            else
            {
                displayPopulation.setText(formatNumber(selectedCountry.getPeople()));
                displayArea.setText(formatNumber(selectedCountry.getArea()));
                displayDensity.setText(formatNumber(selectedCountry.getDensity()));
            }
        }
        else
        {
            displayCountry.setText("");
            displayCapital.setText("");
            displayPopulation.setText("");
            displayArea.setText("");
            displayDensity.setText("");
        }
    }

    private String formatNumber(long number)
    {

        if (number / 1000 > 0)
        {
            if (number / 1000000 > 0)
            {
                double result = number;
                result /= 1000000;
                return String.format("%,d Mill.", Math.round(result));
            }
            else
            {
                double roundNum = number;
                roundNum = Math.round(roundNum / 1000) * 1000;
                return String.format("%,d", (long) roundNum);
            }
        }

        return number + "";
    }

    private void addNewCountry(ActionEvent e, TextField countryName, TextField capital, TextField population, TextField area, ComboBox<Country> countrySelector)
    {
        checkIfEmpty(e, area);
        checkIfEmpty(e, population);
        checkIfEmpty(e, capital);
        checkIfEmpty(e, countryName);

        if (!e.isConsumed())
        {
            if (countries.get(0) == null)
            {
                countries.remove(0);
            }
            countries.add(new Country(countryName.getText(), capital.getText(), Long.parseLong(population.getText()), Long.parseLong(area.getText())));
            countryName.clear();
            capital.clear();
            population.clear();
            area.clear();

            countrySelector.setValue(countries.get(countries.size() - 1));
        }
    }

    private void checkIfEmpty(ActionEvent e, TextField textField)
    {
        if (textField.getText().equals(""))
        {
            e.consume();
            textField.requestFocus();
        }
    }

    private void restrictToNumeric(TextField textField, String oldVal, String newVal)
    {
        if (!newVal.matches("[0-9]*"))
        {
            textField.setText(oldVal);
        }
    }

    private Callback<ListView<Country>, ListCell<Country>> showOnlyName()
    {
        return new Callback<ListView<Country>, ListCell<Country>>()
        {
            @Override
            public ListCell<Country> call(ListView<Country> c)
            {
                final ListCell<Country> cell = new ListCell<Country>()
                {
                    {
                        super.setPrefWidth(200);
                    }

                    @Override
                    protected void updateItem(Country newC, boolean b)
                    {
                        super.updateItem(newC, b);
                        if (newC != null)
                        {
                            setText(newC.getName());
                        }
                        else
                        {
                            setText("Keine Länder vorhanden");
                        }
                    }

                };
                return cell;
            }

        };
    }
}
