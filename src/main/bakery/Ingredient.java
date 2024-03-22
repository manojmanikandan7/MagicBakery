package bakery;

import java.lang.Object;
import java.lang.Comparable;

public class Ingredient implements Comparable<Ingredient>{
    private String name;
    public static final Ingredient HELPFUL_DUCK=new Ingredient("helpful duck 𓅭");

    private static long serialVersionUID;

    public Ingredient(String name){
        this.name=name;
    }

    @Override
    public boolean equals(Object o){
        if((this.toString()).equals(o.toString())){
            return true;
        }
        else{
            return false;
        }
    }
 
    @Override
    public int hashCode(){
        return this.name.hashCode();
    }

    public String toString(){
        return this.name;
    }

    @Override
    public int compareTo(Ingredient o){
        return (this.toString()).compareTo(o.toString());
    }
}
