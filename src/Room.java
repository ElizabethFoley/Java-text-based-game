import java.util.ArrayList;

//creates a new Room object
public class Room {
	// This creates a new variable in Room to track the name
	private String roomName;
	// This creates a new variable to track the description of the room
	private String roomDescription;
	// creates a new int to track the value of the room
	private int id;
	private int goldAmount;

	public Room() {
		// sets the default values of the variable created above
		roomName = null;
		roomDescription = null;
		id = 0;
		goldAmount = 0;
	}

	// This shows the parameters needed to create a new Room
	public Room(int identity, String locName, String descrip, int gldAmt) {
		// This assigns the default variables to the passed in values
		roomName = locName;
		roomDescription = descrip;
		id = identity;
		goldAmount = gldAmt;
	}

	// This provides a way for the roomName variable to be retrieved outside this
	// class
	public String getRoomName() {
		return roomName;
	}

	// This provides a way for the roomDescription variable to be retrieved outside
	// this class
	public String getRoomDiscription() {
		return roomDescription;
	}
	public void setGold(int g) {
		goldAmount = g;
		
	}

	// This provides a way for the id variable to be retrieved outside this class
	public int getID() {
		return id;
	}
	public String roomInfo(String g) {
		String info = roomName;
		return info;
	}

}
