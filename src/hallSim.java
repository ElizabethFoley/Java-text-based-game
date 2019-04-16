
//Hall Sim 1.0 text based game
//Seamus Doyle
//Elizabeth Foley
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.Comparator;

public class hallSim {
	// Below 8 locations have been created by creating new Room objects that take in
	// an Id a name and a discription
	public static Room Lab = new Room(0, "the Lab:", "Seems like an old chemistry lab, in the corner are 9 gold coins",
			9);
	public static Room Kitchen = new Room(1, "The kitchen:",
			"An old out dated kitchen that seems to have never been used, there is a map on the table next to 4 gold coins",
			4);
	public static Room Office = new Room(2, "The Office:",
			"It seems like and old paper sales company, there are 3 gold coins in the room", 3);
	public static Room MagicShop = new Room(3, "The Magic Shop:",
			"A place where you can aquire magic items, in the corner is an old robe", 8);
	public static Room ManCave = new Room(4, "The Man Cave:",
			"It seems to be the home of a man, a sword is mounted on the wall, and a chest with 9 coins is in the corner",
			9);
	public static Room Classroom = new Room(5, "The Classroom:",
			"You enter an old physic classroom, there is 1 gold coin on the table", 1);
	public static Room Bathroom = new Room(6, "The Bathroom:",
			"A moderm bathroom with a pink theme, there is a spell book on the toilet, inside the book you find 30 gold coins",
			30);
	public static Room Dorm = new Room(7, "The Dorm:",
			"It has a two desks and two beds, there is a potion in the old fridge, and on the counter is 15 gold coins",
			15);
	// The Following Line creates an array of locations based of the information it
	// gains from the room objects above
	public static String[] roomList = { Lab.getRoomName() + " " + Lab.getRoomDiscription(),
			Kitchen.getRoomName() + " " + Kitchen.getRoomDiscription(),
			Office.getRoomName() + " " + Office.getRoomDiscription(),
			MagicShop.getRoomName() + " " + MagicShop.getRoomDiscription(),
			ManCave.getRoomName() + " " + ManCave.getRoomDiscription(),
			Classroom.getRoomName() + " " + Classroom.getRoomDiscription(),
			Bathroom.getRoomName() + " " + Bathroom.getRoomDiscription(),
			Dorm.getRoomName() + " " + Dorm.getRoomDiscription() };
	// this creates two ArrayLists of Strings that store the items left on the map
	// and the ones in the players inventory
	public static ArrayList<String> inventory = new ArrayList<String>();
	public static ArrayList<String> itemList = new ArrayList<String>();
	public static ArrayList<magicItems> magicShopInventory = new ArrayList<magicItems>();
	public static ArrayList<String> sortingStuff = new ArrayList<String>();
	// The following variable keeps track of the location
	public static String currentLoc = roomList[0];
	// The following scanner looks for user inputs
	public static Scanner scan = new Scanner(System.in);
	// Score keeps track of the players score
	public static int score = 0;
	public static int playerGold = 0;
	public static Stack path = new Stack();
	public static Stack pathFoward = new Stack();

	public static void main(String[] args) {
		File file = new File("C:/Users/NYgia/Documents/magicitems.txt");
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String item = scan.nextLine();
				int cost = (int) (Math.random() * 20) + 1;
				magicItems item1 = new magicItems(item, cost);
				sortingStuff.add(item);
				magicShopInventory.add(item1);

			}
		} catch (FileNotFoundException e) {
			// this throws and error if the file cannot be found
			e.printStackTrace();

		}
		itemList.add("map");
		itemList.add("sword");
		itemList.add("Spell Book");
		itemList.add("potion");
		itemList.add("old robe");
		sortInventory();
		// The main method that call all the methods needed to run the game
		System.out.println("Welcome to Building Explorer");
		System.out.println("You start in the lab, south of the kitchen");
		
		gameLoop();
	}

	// gameLoop is how the game detects inputs, once the inputs are in it checks for
	// a valid command
	// if it is valid then it will call the respective method for each input unless
	// the input it "quit"
	// in which case it returns and breaks the loop
	public static void gameLoop() {
		while (true) {
			String input = scan.nextLine();
			trackPath();
			
			if (input.equals("n")) {
				moveNorth();
				//trackPath();
			}
			if (input.equals("e")) {
				moveEast();
				//trackPath();
			}
			if (input.equals("s")) {
				moveSouth();
				//trackPath();
			}
			if (input.equals("w")) {
				moveWest();
				//trackPath();
			}
			if (input.equals("h")) {
				System.out.println("Valid commands are n, e, s, w, h, m, t, v, quit");
				System.out.println("your score " + score);
			}
			// this will check if you have the map and open it if true
			if (input.equals("m") && inventory.contains("map")) {
				map();
				System.out.println("your score " + score);
			}
			// this will check if you have the map, if false then it will tell you to find
			// the map to open it
			if (input.equals("m") && inventory.contains("map") == false) {
				System.out.println("You need to find the map to open it");
			}
			if (input.equals("t")) {
				takeItem();
			}
			if (input.equals("v")) {
				System.out.println("This is whats in your inventory");
				for (int i = 0; i < inventory.size(); i++) {
					System.out.println(inventory.get(i));
				}
			}
			if (currentLoc == roomList[3]) {
				magicShop();
			}
			 if (input.equals("quit")) {
				System.out.println("thanks for playing");
				System.out.println("your final score was " + score);
				System.out.println("Would you like to review you game forward 'f' or backward");
				String secondInput = scan.nextLine();
				if(secondInput.equals("b")) {
					reversePath();
				}
				if(secondInput.equals("f")) {
					normalPath();
				}
				return;
			}
			System.out.println("your gold is " + playerGold);
		}
	}
	
	public static void sortInventory() {
		List a = new ArrayList();
		for (int i = 0; i < sortingStuff.size(); i++) {
			a.add(sortingStuff.get(i));
			//System.out.println(a.get(i));
		}
		
		Collections.sort(a);
		for (int i = 0; i < sortingStuff.size(); i++) {
			System.out.println(a.get(i));
		}
	}

	// this method handles the function of the shop
	// it prompts the user to search for the item they would like to buy
	// if they have enough gold then allow them to buy it, if not tell tells them to
	// exit the shop
	public static void magicShop() {
		while (true) {
			System.out.println("enter the name of and item");
			String input = scan.nextLine();
			for (int i = 0; i < magicShopInventory.size(); i++) {
				if (magicShopInventory.get(i).getName().equals(input)) {
					System.out.println(magicShopInventory.get(i));
					if (playerGold >= magicShopInventory.get(i).getPrice()) {
						System.out.println("would you like to buy the item?");
					} else {
						System.out.println("you do not have enough gold for this item");
						System.out.println("please exit either s or e");
						return;
					}
					String buy = scan.nextLine();
					if (buy.equals("y")) {
						inventory.add(magicShopInventory.get(i).getName());
						playerGold = playerGold - magicShopInventory.get(i).getPrice();
						magicShopInventory.remove(i);
						System.out.println(
								"the item has been added to your inventory, your remaining gold is " + playerGold);
						System.out.println("you may exit either s or e");
						return;
					} else if (buy.equals("n")) {
						System.out.println("thanks for shopping, you may exit either s or e");
						return;
					}

				} else {
					System.out.println("the item was not found please enter a direction to leave either s or e");
					return;
				}

				if (input.equals("n")) {
					System.out.println("please enter the direction you would wish to go, either 's' or 'e'");
					return;
				}
			}
			System.out.println("The item was not found, search again or enter 'e' or 's' to exit east or south");
		}
	}
	
	public static void trackPath() {
		if(currentLoc==roomList[0]) {
			path.push(roomList[0]);
		}
		 if(currentLoc==roomList[1]) {
			path.push(roomList[1]);
		}
		if(currentLoc==roomList[2]) {
			path.push(roomList[2]);
		}
		if(currentLoc==roomList[3]) {
			path.push(roomList[3]);
		}
		if(currentLoc==roomList[4]) {
			path.push(roomList[4]);
		}
		if(currentLoc==roomList[5]) {
			path.push(roomList[5]);
		}
		if(currentLoc==roomList[6]) {
			path.push(roomList[6]);
		}
		if(currentLoc==roomList[7]) {
			path.push(roomList[7]);
		}
	}

	public static void reversePath() {
		System.out.println("This is the path in reverse order");
		//System.out.println(currentLoc);
		for (int i = 0; i <= path.size()+1; i++) {
			System.out.println(path.pop());
		}
		
		System.out.println(roomList[0]);
	}
	
	public static void normalPath() {
		System.out.println("This is the path in the order you went");
		System.out.println(roomList[0]);
		for (int i = 0; i <= path.size(); i++) {
			pathFoward.push(path.pop());
		}
		for (int i = 0; i <=pathFoward.size() ; i++) {
			System.out.println(pathFoward.pop());
			
		}
		System.out.println(currentLoc);
	}
	

	// the following method will check to see if you are in a location with an item
	// if this is true then you will pick up the item and add it to your inventory
	// as well it will remove the item from the room so you cannot pick
	// up multiples of the each item, and if the item has already been retrieved
	// it will tell them it has already been picked up
	// this is tracked through two ArrayLists
	// as well this will allow you to pick up the gold available in all the rooms
	public static String takeItem() {
		if (currentLoc == roomList[4] && itemList.contains("sword")) {
			itemList.remove("sword");
			inventory.add("sword");
			playerGold += 9;
			System.out.println("You found a sword");
		} else if (currentLoc == roomList[4] && itemList.contains("sword") == false) {
			System.out.println("you already aquired the items in this room");
		}
		if (currentLoc == roomList[6] && itemList.contains("Spell Book")) {
			inventory.add("Spell Book");
			System.out.println("You found a Spell Book");
			itemList.remove("Spell Book");
			playerGold += 30;
		} else if (currentLoc == roomList[6] && itemList.contains("Spell Book") == false) {
			System.out.println("you already aquired the item in the room");
		}
		if (currentLoc == roomList[7] && itemList.contains("potion")) {
			inventory.add("potion");
			System.out.println("you found a potion");
			itemList.remove("potion");
			playerGold += 15;
		} else if (currentLoc == roomList[7] && itemList.contains("potion") == false) {
			System.out.println("you already aquired the item in this room");
		}
		if (currentLoc == roomList[3] && itemList.contains("old robe")) {
			inventory.add("old robe");
			System.out.println("you found an old robe");
			itemList.remove("old robe");
		} else if (currentLoc == roomList[3] && itemList.contains("old robe") == false) {
			System.out.println("you already aquired the item in this room");
		}
		if (currentLoc == roomList[1] && itemList.contains("map")) {
			inventory.add("map");
			System.out.println("you found the map");
			itemList.remove("map");
			playerGold += 4;
		} else if (currentLoc == roomList[1] && itemList.contains("map") == false) {
			System.out.println("you already found the map");
		}
		if (currentLoc == roomList[0]) {
			playerGold += 9;
		}
		if (currentLoc == roomList[2]) {
			playerGold += 3;
		}
		if (currentLoc == roomList[5]) {
			playerGold += 1;
		}
		return null;
	}

	// The following method prints an ASCII art map of the entire game
	public static void map() {
		System.out.println("-----------     ----------");
		System.out.println("|MagicShop|-----|Man Cave|");
		System.out.println("-----------     |  Sword |");
		System.out.println("     |          ----------");
		System.out.println("     |               |");
		System.out.println("     |               |");
		System.out.println("----------      -----------");
		System.out.println("| Office |------|Classroom|");
		System.out.println("----------      -----------");
		System.out.println("     |               |");
		System.out.println("     |               |");
		System.out.println("----------      ----------");
		System.out.println("| Kitchen|------|Bathroom|");
		System.out.println("----------      ----------");
		System.out.println("     |                |");
		System.out.println("     |                |");
		System.out.println("----------      ----------");
		System.out.println("|  Lab   |------|  Dorm  |");
		System.out.println("----------      ----------");

	}

	// moveNorth is called when a player inputs "n" once called it checks the
	// currentLoc variable
	// if the player is in a valid position to move north then the game will execute
	// the command and give more movement options
	// as well it will increase the score for a valid move
	public static void moveNorth() {

		if (currentLoc == roomList[0]) {
			currentLoc = roomList[1];
			System.out.println(roomList[1]);
			System.out.println("You can move North to the Office, South to the Lab or East to the Bathroom");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[0]);
			//path.push(roomList[1]);

		} else if (currentLoc == roomList[1]) {
			currentLoc = roomList[2];
			System.out.println(roomList[2]);
			System.out.println("You can move North to the Magic Shop, South to the Office, or East to the Classroom");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[1]);
			//path.add(roomList[2]);

		} else if (currentLoc == roomList[2]) {
			currentLoc = roomList[3];
			System.out.println(roomList[3]);
			System.out.println("You can move South to the Office or East to the Man Cave");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[2]);
			//path.add(roomList[3]);

		} else if (currentLoc == roomList[7]) {
			currentLoc = roomList[6];
			System.out.println(roomList[6]);
			System.out.println("You can move North to the Classroom or South to th Dorm, or West to the Kitchen");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[7]);
			//path.add(roomList[6]);
		} else if (currentLoc == roomList[6]) {
			currentLoc = roomList[5];
			System.out.println(roomList[5]);
			System.out.println("You can move North to the Man Cave, South the Bathroom, or west to Office");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[6]);
			//path.add(roomList[5]);
		} else if (currentLoc == roomList[5]) {
			currentLoc = roomList[4];
			System.out.println(roomList[4]);
			System.out.println("You can move South to the Classroom or West to the Magic shop");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[5]);
			//path.add(roomList[4]);
		} else if (currentLoc == roomList[4]) {
			System.out.println("you cannot go farther north, try going West or South");
		} else if (currentLoc == roomList[3]) {
			System.out.println("you cannot go farther north, try going East or South");

		}
	}

	// moveSouth is called when a player inputs "s" once called it checks the
	// currentLoc variable
	// if the player is in a valid position to move south then the game will execute
	// the command and give more movement options
	// as well it will increase the score for a valid move
	public static void moveSouth() {
		if (currentLoc == roomList[3]) {
			currentLoc = roomList[2];
			System.out.println(roomList[2]);
			System.out.println("You can move North to the Magic Shop, South to the Office, or East to the Classroom");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[3]);
		} else if (currentLoc == roomList[2]) {
			currentLoc = roomList[1];
			System.out.println(roomList[1]);
			System.out.println("You can move North to the Office, South to the Lab or East to the Bathroom");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[2]);
		} else if (currentLoc == roomList[1]) {
			currentLoc = roomList[0];
			System.out.println(roomList[0]);
			System.out.println("You can go North to the Office or east to the Dorm");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[1]);
		} else if (currentLoc == roomList[6]) {
			currentLoc = roomList[7];
			System.out.println(roomList[7]);
			System.out.println("You can Move North to the Bathroom or West to the Lab");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[6]);
		} else if (currentLoc == roomList[5]) {
			currentLoc = roomList[6];
			System.out.println(roomList[6]);
			System.out.println("You can move North to the Classroom or South to the Dorm or West to the Kitchen");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[5]);
		} else if (currentLoc == roomList[4]) {
			currentLoc = roomList[5];
			System.out.println(roomList[5]);
			System.out.println("You can move North to the Man Cave or South to the Bathroom or West tot eh Office");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[4]);
		} else if (currentLoc == roomList[7]) {
			System.out.println("you cannot go farther south, try going North, or East");
		} else if (currentLoc == roomList[0]) {
			System.out.println("you cannot go farther south, try going North, or East");
		}
	}

	// moveEast is called when a player inputs "e" once called it checks the
	// currentLoc variable
	// if the player is in a valid position to move east then the game will execute
	// the command and give more movement options
	// as well it will increase the score for a valid move
	public static void moveEast() {
		if (currentLoc == roomList[0]) {
			currentLoc = roomList[7];
			System.out.println(roomList[7]);
			System.out.println("You can Move North to the Bathroom or West to the Lab");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[0]);
			//path.push(roomList[7]);
		} else if (currentLoc == roomList[1]) {
			currentLoc = roomList[6];
			System.out.println(roomList[6]);
			System.out.println("You can move North to the Classroom or South to the Dorm or West to the Kitchen");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[1]);
			//path.add(roomList[6]);

		} else if (currentLoc == roomList[2]) {
			currentLoc = roomList[5];
			System.out.println(roomList[5]);
			System.out.println("You can move North to the Man Cave or South to the Bathroom or West to the Office");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[2]);
			//path.add(roomList[5]);
		} else if (currentLoc == roomList[3]) {
			System.out.println(roomList[4]);
			currentLoc = roomList[4];
			//path.add(roomList[3]);
			//path.add(roomList[4]);
		} else if (currentLoc == roomList[7] || currentLoc == roomList[6] || currentLoc == roomList[5]
				|| currentLoc == roomList[4]) {
			System.out.println("you cannot go farther East, maybe try going West");
		}

	}

	// moveWest is called when a player inputs "w" once called it checks the
	// currentLoc variable
	// if the player is in a valid position to move west then the game will execute
	// the command and give more movement options
	// as well it will increase the score for a valid move
	public static void moveWest() {
		if (currentLoc == roomList[7]) {
			currentLoc = roomList[0];
			System.out.println(roomList[0]);
			System.out.println("You can go North to the Office or east to the Dorm");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[7]);
		} else if (currentLoc == roomList[6]) {
			currentLoc = roomList[1];
			System.out.println(roomList[1]);
			System.out.println("You can move North to the Office, South to the Lab or East to the Bathroom");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[6]);

		} else if (currentLoc == roomList[5]) {
			currentLoc = roomList[2];
			System.out.println(roomList[2]);
			System.out.println("You can move North to the Magic Shop, South to the Office, or East to the Classroom");
			score += 5;
			System.out.println("your score: " + score);
			//path.add(roomList[5]);
		} else if (currentLoc == roomList[4]) {
			currentLoc = roomList[3];
			System.out.println(roomList[3]);
			System.out.println("You can move South to the Office or East to the Man Cave");
			//path.add(roomList[4]);
		} else if (currentLoc == roomList[0] || currentLoc == roomList[1] || currentLoc == roomList[2]
				|| currentLoc == roomList[3]) {
			System.out.println("you cannot go farther West, maybe try going East");
		}

	}
}
