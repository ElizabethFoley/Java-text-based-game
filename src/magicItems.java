
public class magicItems {
	//creates a new magic item class
	//this creates a variable to store the name
	private String itemName;
	//this creates a variable to store the gold value
	private int price;
	
	public magicItems() {
		//sets the initial values for the variables
		itemName= null;
		price = 0;
	}
	public magicItems(String name, int pri) {
		//allows the variable values to be set when the object is created
		itemName = name;
		price = pri;
	}
	public String getName() {
		//allows the user to get the name of the item
		return itemName;
	}
	public int getPrice() {
		//allows the price to be accessed
		return price;
	}
	public String toString() {
		//creates a unique toString for this object
		String itmAndgld = itemName+" is "+price+" "+"gold";
		return itmAndgld;
	}

	}


