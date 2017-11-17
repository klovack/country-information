package gui.country.combo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
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
        Callback cellFactory = showOnlyName();
        countrySelector.setButtonCell((ListCell) cellFactory.call(null));
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

        root.getChildren().addAll(countrySelector, cbExactVal, infoLayout);
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("L\u00e4nder-Informationen");
        primaryStage.show();
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
                return String.format("%d Mill.", number / 1000000);
            }
            else
            {
                long roundNum = (long) Math.ceil(number / 1000) * 1000;
                return String.format("%d", roundNum);
            }
        }

        return number + "";
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
