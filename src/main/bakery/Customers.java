package bakery;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Random;
import java.util.EmptyStackException;
import java.io.IOException;
import java.io.Serializable;

import util.CardUtils;
import util.ConsoleUtils;
import bakery.CustomerOrder.CustomerOrderStatus;

/**
 * A Class representing the customers of the game.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class Customers implements Serializable{
    private Collection<CustomerOrder> activeCustomers;
    private Collection<CustomerOrder> customerDeck;
    private List<CustomerOrder> inactiveCustomers;
    private Random random;

    private static final long serialVersionUID=42L;

    /**
     * Initialises a Customer object with the specified parameters
     * and initialises the active and the inactive customers.
     *
     * @param deckFile the file path of the deck used to initialise the customer deck.
     * @param random the random object used to shuffle the customer deck.
     * @param layers the collection of layers used for initialising the customer deck.
     * @param numPlayers the number of players in the game.
     * @throws IOException If there are any problems reading the file.
     */
    public Customers(String deckFile, Random random, Collection<Layer> layers, int numPlayers) throws IOException{
        this.activeCustomers=new LinkedList<CustomerOrder>();
        for(int i=0; i<3; i++){
            this.activeCustomers.add(null);
        }
        this.inactiveCustomers=new Stack<CustomerOrder>(); //COMEBACK
        this.random=random;
        initialiseCustomerDeck(deckFile, layers, numPlayers);
    }

    /**
     * Adds a customer order from the top of the customerDeck, after a space is created by the timePasses method.
     * Returns the customer who left, if applicable, null, otherwise.
     *
     * @return The customer if they left the bakery after this round, null, otherwise.
     */
    public CustomerOrder addCustomerOrder(){
        CustomerOrder leaving=timePasses();
        if(this.customerDeck.isEmpty()){
            throw new EmptyStackException();
        }
        else {
            ((LinkedList<CustomerOrder>)this.activeCustomers).removeLast();
            this.activeCustomers.add(drawCustomer());
        }
        return leaving;
    }

    /**
     * States if the customer is about to leave soon after this round.
     *
     * @return True, if the customer will leave soon, False, otherwise.
     */
    public boolean customerWillLeaveSoon(){
        if(!this.activeCustomers.contains(null)){
            ((LinkedList<CustomerOrder>)this.activeCustomers).getFirst().setStatus(CustomerOrderStatus.IMPATIENT);
            return true;
        }
        if(this.customerDeck.isEmpty()) {
            if(((LinkedList<CustomerOrder>) this.activeCustomers).getFirst() != null &&
                    ((LinkedList<CustomerOrder>) this.activeCustomers).getLast() == null){
                ((LinkedList<CustomerOrder>)this.activeCustomers).getFirst().setStatus(CustomerOrderStatus.IMPATIENT);
                return true;
            }
        }
        return false;
    }

    /**
     * Draws a card from the top of the customerDeck pile.
     *
     * @return A customer order from the customerDeck.
     */
    public CustomerOrder drawCustomer(){
        return ((Stack<CustomerOrder>)this.customerDeck).pop();
    }

    /**
     * Gets the current active customers in the bakery.
     *
     * @return A collection of customer orders indicating the customers in the bakery.
     */
    public Collection<CustomerOrder> getActiveCustomers(){
        Collection<CustomerOrder> customerOrders=new LinkedList<CustomerOrder>();
        for(CustomerOrder customer: this.activeCustomers){
            if(customer!=null){
                customerOrders.add(customer);
            }
        }
        return customerOrders;
    }

    /**
     * Gets the customerDeck holding the cards to be used for the customers.
     *
     * @return A collection of customer orders representing the customerDeck.
     */
    public Collection<CustomerOrder> getCustomerDeck(){
        return this.customerDeck;
    }

    /**
     * Gets the customer orders fulfillable only with the ingredients contained the parameter hand.
     *
     * @param hand The list of ingredients to check against the recipe of the customer orders.
     * @return  A collection of customer orders which can be fulfilled by the ingredients in the hand.
     */
    public Collection<CustomerOrder> getFulfilable(List<Ingredient> hand){
        Collection<CustomerOrder> fulfillable=new ArrayList<CustomerOrder>();
        if(isEmpty()) {
            return fulfillable;
        }
        for(CustomerOrder customer : getActiveCustomers()){
            if(customer.canFulfill(hand)){
                fulfillable.add(customer);
            }
        }

        return fulfillable;
    }

    /**
     * Returns the customer orders with the particular CustomerOrderStatus given by parameter status.
     *
     * @param status The status to filter the customer orders by.
     * @return A collection of customer orders with the given status.
     */
    public Collection<CustomerOrder> getInactiveCustomersWithStatus(CustomerOrderStatus status){

        Collection<CustomerOrder> filtered_list=new ArrayList<CustomerOrder>();

        for(CustomerOrder customer: this.inactiveCustomers){
            if(customer.getStatus() == status){
                filtered_list.add(customer);
            }
        }
        return filtered_list;
    }

    /**
     * Initialises the customerDeck pile from the deckFile file, according to the number of players, numPlayers.
     *
     * @param deckFile The path to the customer deck file.
     * @param layers The layers available for the customer orders.
     * @param numPlayers The number of players in the game.
     * @throws IOException If there are any problems reading the file.
     */
    private void initialiseCustomerDeck(String deckFile, Collection<Layer> layers, int numPlayers) throws IOException {
        this.customerDeck=new Stack<CustomerOrder>();

        ArrayList<CustomerOrder> list = new ArrayList<CustomerOrder>(CardUtils.readCustomerFile(deckFile, layers));
        Collections.shuffle(list, this.random);
        LinkedList<CustomerOrder> level1=new LinkedList<CustomerOrder>();
        LinkedList<CustomerOrder> level2=new LinkedList<CustomerOrder>();
        LinkedList<CustomerOrder> level3=new LinkedList<CustomerOrder>();
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
                nums[1]=1;
                nums[2]=6;
                break;
        }
        for(CustomerOrder customer:list){
            if(customer.getLevel()==1){
                level1.add(customer);
            }
            if(customer.getLevel()==2){
                level2.add(customer);
            }
            if(customer.getLevel()==3){
                level3.add(customer);
            }
        }

        for(int i=0; i<3; i++) {
            for (int j = 0; j < nums[i]; j++) {
                switch (i) {
                    case 0:
                        this.customerDeck.add(level1.remove());
                        break;
                    case 1:
                        this.customerDeck.add(level2.remove());
                        break;
                    case 2:
                        this.customerDeck.add(level3.remove());
                        break;
                }
            }
        }
        Collections.shuffle(((List<CustomerOrder>)this.customerDeck), this.random);
    }

    /**
     * Returns whether there are active customers in the bakery.
     *
     * @return True, if there are one or more customers in the bakery, false, otherwise.
     */
    public boolean isEmpty(){
        for(CustomerOrder customer:this.activeCustomers){
            if(customer!=null){
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the customer at the rightmost space of the shop or null, if there is no one in that spot.
     *
     * @return The customer at the rightmost space of the shop, or null, if empty.
     */
    public CustomerOrder peek(){
        if(isEmpty()){
            return null;
        }
        else{
            return ((LinkedList<CustomerOrder>)this.activeCustomers).getFirst();
        }
    }

    /**
     * Removes the customer from the activeCustomers queue.
     *
     * @param customer The customer to remove from the queue.
     */
    public void remove(CustomerOrder customer){
        ((LinkedList<CustomerOrder>)this.activeCustomers).set(((LinkedList<CustomerOrder>)this.activeCustomers).indexOf(customer), null);
        ((Stack<CustomerOrder>)this.inactiveCustomers).push(customer);
    }

    /**
     * Returns the number of activeCustomers in the bakery.
     *
     * @return The number of active customers.
     */
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

    /**
     * Moves the customers along the queue in the bakery after end of a round.
     * Returns the customer who left, if applicable, null, otherwise.
     *
     * @return The customer if they left the bakery after this round, null, otherwise.
     */
    public CustomerOrder timePasses(){

        if(customerWillLeaveSoon()) {
            CustomerOrder leaving = ((LinkedList<CustomerOrder>) this.activeCustomers).remove(); //Removes the customer about to leave soon
            this.activeCustomers.add(null);
            leaving.abandon();
            ((Stack<CustomerOrder>)this.inactiveCustomers).push(leaving);
            return leaving;
        }
        if (this.customerDeck.isEmpty()) {
            this.activeCustomers.remove(null);  //Removes the first occurrence of null (i.e. Empty spot)
        }
        else{
            ((LinkedList<CustomerOrder>)this.activeCustomers).removeLastOccurrence(null);
        }
        this.activeCustomers.add(null);
        return null;
    }
}
