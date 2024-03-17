package util;

import bakery.*;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class CardUtils {
   private CardUtils(){}
   
   public static ArrayList<Ingredient> readIngredientFile(String path){
    
    ArrayList<Ingredient> ingredient=new ArrayList<Ingredient>();

    try(
        FileReader file=new FileReader(path);
        BufferedReader in=new BufferedReader(file);
    ){
        in.readLine(); //Ignoring the column header for the file; COME BACK
        String line=in.readLine();
        while(line != null){
            ingredient.addAll(stringToIngredients(line));
            line=in.readLine();
        }
    }
    catch(FileNotFoundException e){
        System.out.println("Ingredients file not found!");
        e.printStackTrace();
    }
    catch(IOException e){
        System.out.println("IOException: "+e);
        e.printStackTrace();
    }
    return ingredient;
   }

   public static ArrayList<Layer> readLayerFile(String path){

    ArrayList<Layer> layer=new ArrayList<Layer>();

    try(
        FileReader file=new FileReader(path);
        BufferedReader in=new BufferedReader(file);
    ){
        in.readLine(); //Ignoring the column header for the file; COME BACK
        String line=in.readLine();
        while(line != null){
            layer.addAll(stringToLayers(line));
            line=in.readLine();
        }
    }
    catch(FileNotFoundException e){
        System.out.println("Layers file not found!");
        e.printStackTrace();
    }
    catch(IOException e){
        System.out.println("IOException: "+e);
        e.printStackTrace();
    }
    return layer;
   }

   public static ArrayList<CustomerOrder> readCustomerFile(String path, ArrayList<Layer> layers){

    ArrayList<CustomerOrder> customers=new ArrayList<CustomerOrder>();

    try(
        FileReader file=new FileReader(path);
        BufferedReader in=new BufferedReader(file);
    ){
        in.readLine(); //Ignoring the column header for the file; COME BACK
        String line=in.readLine();
        while(line != null){
            customers.add(stringToCustomerOrder(line, layers));
            line=in.readLine();
        }
    }
    catch(FileNotFoundException e){
        System.out.println("Customer orders file not found!");
        e.printStackTrace();
    }
    catch(IOException e){
        System.out.println("IOException: "+e);
        e.printStackTrace();
    }
    return customers;
   }

   private static ArrayList<Ingredient> stringToIngredients(String str){
    str=str.strip();
    String[] values=str.split(", ");
    ArrayList<Ingredient> list=new ArrayList<Ingredient>();
    for(int i = 0; i < Integer.parseInt(values[1]); i++){
        list.add(new Ingredient(values[0]));
    }
    return list;
   }
   
   private static ArrayList<Layer> stringToLayers(String str){
    str=str.strip();
    String[] values=str.split(", ");
    String[] ing_str=values[1].split("; ");

    ArrayList<Ingredient> ingredients=new ArrayList<Ingredient>();
    for(String ing:ing_str){
        ingredients.add(new Ingredient(ing));
    }


    ArrayList<Layer> list=new ArrayList<Layer>();
    for(int i=0; i<4; i++){
        list.add(new Layer(values[0], ingredients));
    }
    return list;
   }

   private static CustomerOrder stringToCustomerOrder(String str, ArrayList<Layer> layers){
    str=str.strip();
    String[] items=str.split(", ");
    String[] recipes=items[2].split("; ");
    String[] garnishes={};
    if(items.length>=4)
        garnishes=items[3].split("; ");

    ArrayList<Ingredient> recipe_list=new ArrayList<Ingredient>(); 
    ArrayList<Ingredient> garnish_list=new ArrayList<Ingredient>();

    for(int j=0;j<recipes.length;j++){
        String item=recipes[j];
        boolean isLayer=false;
        for(int i=0; i<layers.size();i++){
            if(layers.get(i).toString().equals(item)){
                recipe_list.addAll(layers.get(i).getRecipe());
                isLayer=true;
                break;
            }
        }
        if(!isLayer){
            recipe_list.add(new Ingredient(item));
        }
    }

    for(int j=0;j<garnishes.length;j++){
        String item=garnishes[j];
        boolean isLayer=false;
        for(int i=0;i<layers.size();i++){
            if(layers.get(i).toString().equals(item)){
                garnish_list.addAll(layers.get(i).getRecipe());
                isLayer=true;
                break;
            }
        }
        if(!isLayer){
            garnish_list.add(new Ingredient(item));
        }
    }

    return new CustomerOrder(items[1], recipe_list, garnish_list, Integer.parseInt(items[0])); 

   }
}
