package bakery;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Random;
import java.io.Serializable;

import util.CardUtils;
import bakery.CustomerOrder.CustomerOrderStatus;

public class Customers implements Serializable{
    private Collection<CustomerOrder> activeCustomers;
    private Collection<CustomerOrder> customerDeck;
    private List<CustomerOrder> inactiveCustomers;
    private Random random;

    private static final long serialVersionUID=42L;
    
    public Customers(String deckFile, Random random, Collection<Layer> layers, int numPlayers){
        initialiseCustomerDeck(deckFile, layers, numPlayers);
        this.activeCustomers=new LinkedList<CustomerOrder>();
        this.inactiveCustomers=new Stack<CustomerOrder>(); //COMEBACK
        this.random=random;
    }

    public CustomerOrder addCustomerOrder(){
        return null;
    }

    public boolean customerWillLeaveSoon(){
        return false;
    }

    public CustomerOrder drawCustomer(){
        return ((Stack<CustomerOrder>)this.customerDeck).pop();
    }

    public Collection<CustomerOrder> getActiveCustomers(){
        return this.activeCustomers;
    }

    public Collection<CustomerOrder> getCustomerDeck(){
        return this.customerDeck;
    }

    public Collection<CustomerOrder> getFulfillable(List<Ingredient> hand){
        Collection<CustomerOrder> fulfillable=new ArrayList<CustomerOrder>();
        for(CustomerOrder customer: this.activeCustomers){
            if(hand.containsAll(customer.getRecipe())){
                fulfillable.add(customer);
            }
        }

        return fulfillable;
    }

    public Collection<CustomerOrder> getInactiveCustomersWithStatus(CustomerOrderStatus status){
        
        Collection<CustomerOrder> filtered_list=new ArrayList<CustomerOrder>();

        for(CustomerOrder customer: this.inactiveCustomers){
            if(customer.getStatus() == status){
                filtered_list.add(customer);
            }
        }
        return filtered_list;
    }

    private void initialiseCustomerDeck(String deckFile, Collection<Layer> layers, int numPlayers){
        this.customerDeck=new Stack<CustomerOrder>();
        
        Stack<CustomerOrder> list=new Stack<CustomerOrder>();
        list.addAll(CardUtils.readCustomerFile(deckFile, layers));
        Collections.shuffle(((List<CustomerOrder>)customerDeck), random);
        Stack<CustomerOrder> level1=new Stack<CustomerOrder>();
        Stack<CustomerOrder> level2=new Stack<CustomerOrder>();
        Stack<CustomerOrder> level3=new Stack<CustomerOrder>();
        int[] nums=new int[3];
        switch (numPlayers) {
            case 2:
                nums[0]=4;
                nums[1]=2;
                nums[2]=1;
                break;
            case 3:
            case 4:
                nums[0]=1;
                nums[1]=2;
                nums[2]=4;
                break;
            case 5:
                nums[0]=0;
                nums[1]=2;
                nums[2]=3;
                break;
            default:
                nums[0]=0;
                nums[1]=0;
                nums[2]=0;
                break;
        }
        for(CustomerOrder customer:list){
            if(customer.getLevel()==1){
                level1.add(customer);
            }
            if(customer.getLevel()==2){
                level2.add(customer);
            }
            if(customer.getLevel()==2){
                level3.add(customer);
            }
        }

        for(int i=0; i<3; i++){
            for(int j=0; j<nums[i]; j++){
                switch (i) {
                    case 0:
                        this.customerDeck.add(level1.pop());
                        break;
                    case 1:
                        this.customerDeck.add(level2.pop());
                        break; 
                    case 2:
                        this.customerDeck.add(level3.pop());
                        break;
                }
            }

        }
        Collections.shuffle(((List<CustomerOrder>)this.customerDeck), this.random);
    }

    public boolean isEmpty(){
        return this.activeCustomers.isEmpty();
    }

    public CustomerOrder peek(){
        if(isEmpty()){
            return null;
        }
        else{
            return ((LinkedList<CustomerOrder>)this.activeCustomers).peek();
        }
    }

    public void remove(CustomerOrder customer){
        ((LinkedList<CustomerOrder>)this.activeCustomers).set(((LinkedList<CustomerOrder>)this.activeCustomers).indexOf(customer), null);
        this.activeCustomers.remove(customer);
    }

    public int size(){
        if(isEmpty()){
            return 0;
        }

        int size=0;

        for(CustomerOrder customer: this.activeCustomers){
            if(customer!=null){
                size++;
            }
        }

        return size;
    }

    public CustomerOrder timePasses(){
        return null;
    }
}
