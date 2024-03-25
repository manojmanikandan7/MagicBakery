package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import bakery.*;

/**
 * A Class to help the user read the deck files and convert them into their respective objects.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 */
public class CardUtils {
    private CardUtils() {
    }

    /**
     * Reads the file in the path provided and returns a list of ingredients from the file.
     *
     * @param path The string representing the path of the ingredients file.
     * @return A list of Ingredient objects from the file.
     * @throws IOException If there are any problems reading the file.
     */
    public static List<Ingredient> readIngredientFile(String path) throws IOException {

        ArrayList<Ingredient> ingredient = new ArrayList<Ingredient>();

        try (
                FileReader file = new FileReader(path);
                BufferedReader in = new BufferedReader(file);
        ) {
            String line;
            in.readLine(); //Ignoring the column header for the file; COME BACK
            while ((line = in.readLine()) != null) {
                ingredient.addAll(stringToIngredients(line));
            }
            return ingredient;
        }
    }

    /**
     * Reads the file in the path provided and returns a list of layers from the file.
     *
     * @param path The string representing the path of the layers file.
     * @return A list of Layer objects from the file.
     * @throws IOException If there are any problems reading the file.
     */
    public static List<Layer> readLayerFile(String path) throws IOException {

        ArrayList<Layer> layer = new ArrayList<Layer>();

        try (
                FileReader file = new FileReader(path);
                BufferedReader in = new BufferedReader(file);
        ) {
            in.readLine(); //Ignoring the column header for the file; COME BACK
            String line;
            while ((line = in.readLine()) != null) {
                layer.addAll(stringToLayers(line));
            }
        }
        return layer;
    }

    /**
     * Reads the file in the path provided and returns a list of customer orders from the file.
     * The collection of layers is used to initialise each customer order.
     *
     * @param path   The string representing the path of the customers file.
     * @param layers The collection of layers used to formulate the recipe and garnish of the customer order
     * @return A list of CustomerOrder objects from the file.
     * @throws IOException If there are any problems reading the file.
     */
    public static List<CustomerOrder> readCustomerFile(String path, Collection<Layer> layers) throws IOException {

        ArrayList<CustomerOrder> customers = new ArrayList<CustomerOrder>();

        try (
                FileReader file = new FileReader(path);
                BufferedReader in = new BufferedReader(file);
        ) {
            in.readLine(); //Ignoring the column header for the file; COME BACK
            String line;
            while ((line = in.readLine()) != null) {
                customers.add(stringToCustomerOrder(line, layers));
            }
        }
        return customers;
    }

    private static List<Ingredient> stringToIngredients(String str) {
        str = str.strip();
        String[] values = str.split(", ");
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        for (int i = 0; i < Integer.parseInt(values[1]); i++) {
            if (values[0].equals("helpful duck 𓅭")) {
                list.add(Ingredient.HELPFUL_DUCK);
            } else {
                list.add(new Ingredient(values[0]));
            }
        }
        return list;
    }

    private static List<Layer> stringToLayers(String str) {
        str = str.strip();
        String[] values = str.split(", ");
        String[] ing_str = values[1].split("; ");

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (String ing : ing_str) {
            ingredients.add(new Ingredient(ing));
        }


        ArrayList<Layer> list = new ArrayList<Layer>();
        for (int i = 0; i < 4; i++) {
            list.add(new Layer(values[0], ingredients));
        }
        return list;
    }

    private static CustomerOrder stringToCustomerOrder(String str, Collection<Layer> layers) {
        ArrayList<Layer> layer_list = new ArrayList<Layer>(layers);

        str = str.strip();
        String[] items = str.split(", ");
        String[] recipes = items[2].split("; ");
        String[] garnishes = {};
        if (items.length >= 4)
            garnishes = items[3].split("; ");

        ArrayList<Ingredient> recipe_list = new ArrayList<Ingredient>();
        ArrayList<Ingredient> garnish_list = new ArrayList<Ingredient>();

        for (int j = 0; j < recipes.length; j++) {
            String item = recipes[j];
            boolean isLayer = false;
            for (int i = 0; i < layer_list.size(); i++) {
                if (layer_list.get(i).toString().equals(item)) {
                    recipe_list.addAll(layer_list.get(i).getRecipe());
                    isLayer = true;
                    break;
                }
            }
            if (!isLayer) {
                recipe_list.add(new Ingredient(item));
            }
        }

        for (int j = 0; j < garnishes.length; j++) {
            String item = garnishes[j];
            boolean isLayer = false;
            for (int i = 0; i < layers.size(); i++) {
                if (layer_list.get(i).toString().equals(item)) {
                    garnish_list.addAll(layer_list.get(i).getRecipe());
                    isLayer = true;
                    break;
                }
            }
            if (!isLayer) {
                garnish_list.add(new Ingredient(item));
            }
        }

        return new CustomerOrder(items[1], recipe_list, garnish_list, Integer.parseInt(items[0]));

    }
}
