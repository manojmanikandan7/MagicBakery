package bakery;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Random;

import util.CardUtils;
import bakery.CustomerOrder.CustomerOrderStatus;

public class Customers {
    private Collection<CustomerOrder> activeCustomers;
    private Collection<CustomerOrder> customerDeck;
    private List<CustomerOrder> inactiveCustomers;
    private Random random;
    
    public Customers(String deckFile, Random random, Collection<Layer> layers, int numPlayers){
        initialiseCustomerDeck(deckFile, layers, numPlayers);
        this.activeCustomers=new LinkedList<CustomerOrder>();
        this.customerDeck=new Stack<CustomerOrder>();
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
        return ((Stack<CustomerOrder>)customerDeck).pop();
    }

    public Collection<CustomerOrder> getActiveCustomers(){
        return activeCustomers;
    }

    public Collection<CustomerOrder> getCustomerDeck(){
        return customerDeck;
    }

    public Collection<CustomerOrder> getFulfillable(List<Ingredient> hand){
        return null;
    }

    public Collection<CustomerOrder> getInactiveCustomersWithStatus(CustomerOrderStatus status){
        return null;
    }

    private void initialiseCustomerDeck(String deckFile, Collection<Layer> layers, int numPlayers){
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
                        customerDeck.add(level1.pop());
                        break;
                    case 1:
                        customerDeck.add(level2.pop());
                        break; 
                    case 2:
                        customerDeck.add(level3.pop());
                        break;
                }
            }

        }
        Collections.shuffle(((List<CustomerOrder>)customerDeck), random);
    }

    public boolean isEmpty(){
        return activeCustomers.isEmpty();
    }

    public CustomerOrder peek(){
        if(isEmpty()){
            return null;
        }
        else{
            return ((LinkedList<CustomerOrder>)activeCustomers).peek();
        }
    }

    public void remove(CustomerOrder customer){
        activeCustomers.remove(customer); //Should not blindly remove. COME BACK.
    }

    public int size(){
        return activeCustomers.size();  
    }

    public CustomerOrder timePasses(){
        return null;
    }
}
