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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CountryInfo extends Application
{

    private ObservableList<Country> countries = FXCollections.observableArrayList(new Country("Liechtenstein", "Vaduz", 36942, 160), new Country("Taka-Tuka-Land", "Säbelweiler", 467, 25), new Country("Belgien", "Brüssel", 10839905, 30528), new Country("Brasilien", "Brasilia", 192383141, 8514215), new Country("Bangladesch", "Dhaka", 161314158, 147570), new Country("San Marino", "San Marino", 32471, 61), new Country("Andorra", "Andorra la Vella", 85015, 468), new Country("Schweden", "Stockholm", 9415570, 450295), new Country("Russland", "Moskau", 143613415, 17075400), new Country("Singapur", "Singapur", 5312431, 712), new Country("Cookinseln", "Avarua", 18631, 242), new Country("Frankreich", "Paris", 64667314, 668763), new Country("Deutschland", "Berlin", 80586314, 357121), new Country("Kanada", "Ottawa", 35158304, 9984676), new Country("Monaco", "Monaco", 36136, 2));

    private ComboBox<Country> countrySelector;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        VBox root = new VBox(6);

        countrySelector = new ComboBox<Country>(countries);
        countrySelector.setPromptText("Keine Länder vorhanden");

        CheckBox cbExactVal = new CheckBox("exakte Angaben");
        cbExactVal.setId("exactValues");

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

        // populationField.textProperty().addListener((obs, oldVal, newVal) ->
        // restrictToNumeric(populationField, oldVal, newVal));
        // areaField.textProperty().addListener((obs, oldVal, newVal) ->
        // restrictToNumeric(areaField, oldVal, newVal));

        // ==================== Buttons ==============================
        Button btnAdd = new Button("Hinzuf\u00fcgen");
        Button btnDel = new Button("L\u00f6schen");
        btnAdd.setId("add");
        btnDel.setId("delete");

        btnAdd.setOnAction(e -> addNewCountry(e, countryField, capitalField, populationField, areaField));
        btnDel.setOnAction(e -> deleteCountry());

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

    private void deleteCountry()
    {
        countrySelector.getItems().remove(countrySelector.getValue());
        if (countrySelector.getItems().size() > 0)
        {
            countrySelector.setValue(countries.get(countries.size() - 1));
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

    private void addNewCountry(ActionEvent e, TextField countryName, TextField capital, TextField population, TextField area)
    {
        checkIfEmpty(e, area);
        checkIfEmpty(e, population);
        checkIfEmpty(e, capital);
        checkIfEmpty(e, countryName);

        checkForValidNumber(e, population);
        checkForValidNumber(e, area);

        if (!e.isConsumed())
        {
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

    private void checkForValidNumber(ActionEvent e, TextField textField)
    {
        try
        {
            if (Long.parseLong(textField.getText()) < 0)
            {
                e.consume();
                textField.requestFocus();
            }
        }
        catch (NumberFormatException exception)
        {
            e.consume();
            textField.requestFocus();
        }
    }

    // private void restrictToNumeric(TextField textField, String oldVal, String
    // newVal)
    // {
    // if (!newVal.matches("[0-9]*"))
    // {
    // textField.setText(oldVal);
    // }
    // }
}
