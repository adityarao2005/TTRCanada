package controller;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import model.CardColour;
import model.City;
import model.Player;
import model.Route;
import model.Ticket;
import model.TrainCard;

import static controller.TTRController.NAME_MAP;

// BY VITHURSH
public class FileImportController {

	public FileImportController() {
	}

	// Reads the cities from the city file
	public ArrayList<City> readCities() {

		ArrayList<City> cities = new ArrayList<>();

		// Read from cities.txt file
		try {
			// Use csv delimeter
			Scanner input = new Scanner(new File("files/cities.txt")).useDelimiter(",");

			// While there is more data, parse them into cities
			while (input.hasNext()) {
				// Get the city data
				String name = input.next().trim();
				int x = input.nextInt();
				int y = input.nextInt();
				Point point = new Point(x, y);

				// create the city object
				City city = new City(name, point);
				cities.add(city);
				NAME_MAP.put(name, city);
			}

		} catch (FileNotFoundException error) {
			System.out.println("cities.txt not found");
			error.printStackTrace();
		}
		// REturn the cities
		return cities;
	}

	public ArrayList<Route> readRoutes() {

		ArrayList<Route> routes = new ArrayList<>();

		try {
			// Use csv delimeter
			Scanner input = new Scanner(new File("files/routes.txt")).useDelimiter(",");

			// While there is more data, parse them into cities
			while (input.hasNext()) {
				// Get the route data
				String place = input.next().trim();
				String otherPlace = input.next().trim();
				int worth = input.nextInt();
				String color = input.next().trim();
				int coordinateX = input.nextInt();
				int coordinateY = input.nextInt();
				boolean dualRail = input.nextBoolean();

				// Composition objects
				City a = NAME_MAP.get(place);
				City b = NAME_MAP.get(otherPlace);
				CardColour colour = color.equals("GRAY") ? CardColour.RAINBOW : CardColour.valueOf(color);
				Point point = new Point(coordinateX, coordinateY);

				// Create the route
				Route route = new Route();

				route.setStartCity(a);
				// Sets the starting city of the route to 'a'.

				route.setDestinationCity(b);
				// Sets the destination city of the route to 'b'.

				route.setPoint(point);
				// Sets the coordinates of the route on the game board to the given 'point'.

				route.setDualRail(dualRail);
				// Sets whether the route is a dual rail route based on the value of 'dualRail'.
				// If 'dualRail' is true, it indicates a dual rail route. Otherwise, it is a single rail route.

				route.setTrainType(colour);
				// Sets the type of train cards required to claim the route to the given 'colour'.

				route.setLength(worth);
				// Sets the length of the route to the given 'worth'.
				// The 'worth' represents the number of train cards required to claim the route.

				// Add the routes
				routes.add(route);
			}

		} catch (FileNotFoundException error) {
			System.out.println("cities.txt not found");
			error.printStackTrace();
		}

		// Return the list
		return routes;
	}

	public ArrayList<Ticket> readTickets() {

		ArrayList<Ticket> tickets = new ArrayList<>();

		try {
			// Create a scanner
			Scanner input = new Scanner(new File("files/tickets.txt")).useDelimiter(",");

			// check if there are more lines
			// If there are, parse them and create a ticket from them
			while (input.hasNext()) {
				// read info from text file
				String place = input.next().trim();
				String routeToPlace = input.next().trim();
				int worth = input.nextInt();

				// Add the ticket
				Ticket ticket = new Ticket(NAME_MAP.get(place), NAME_MAP.get(routeToPlace), worth);
				tickets.add(ticket);
			}

		} catch (FileNotFoundException error) {

			System.out.println("cities.txt not found");
			error.printStackTrace();
		}

		// Return the tickets
		return tickets;
	}

	public void saveData(Player[] players, Stack<Ticket> ticketStack, Stack<TrainCard> trainCardStack,
			Stack<TrainCard> dumpDeck, TrainCard[] river, int currentPlayer) throws IOException {
		// Create a new file output stream to write data to a file
		FileOutputStream fileOut = new FileOutputStream("files/savedGameData.ser");

		// Create a new object output stream to write objects to the file output stream
		ObjectOutputStream out = new ObjectOutputStream(fileOut);

		// Write the following objects to the output stream and serialize them
		for (Player player : players)
			out.writeObject(player);

		out.writeObject(ticketStack);
		out.writeObject(trainCardStack);

		for (TrainCard card : river)
			out.writeObject(card);

		out.writeInt(currentPlayer);

		// Close the output stream and file output stream
		out.close();
		fileOut.close();

	}

	@SuppressWarnings("unchecked")
	public void loadData(Player[] players, Stack<Ticket> ticketStack, Stack<TrainCard> trainCardStack,
			Stack<TrainCard> dumpDeck, TrainCard[] river, int[] currentPlayer)
			throws IOException, ClassNotFoundException {
		// Create a new file input stream to read data from a file
		FileInputStream fileIn = new FileInputStream("files/savedGameData.ser");

		// Create a new object input stream to read objects from the file input stream
		ObjectInputStream in = new ObjectInputStream(fileIn);

		// Read the following objects from the input stream and deserialize them
		for (int i = 0; i < players.length; i++)
			players[i] = (Player) in.readObject();

		ticketStack.addAll((Stack<Ticket>) in.readObject());
		
		Stack<TrainCard> deck = (Stack<TrainCard>) in.readObject();
		
		// Add the cards into the real deck and make sure they are all clickable
		for (TrainCard card : deck) {
			card.addMouseListener(TTRController.getInstance());
			trainCardStack.push(card);
		}

		for (int i = 0; i < 5; i++)
			river[i] = (TrainCard) in.readObject();

		currentPlayer[0] = in.readInt();

		// Close the input stream and file input stream
		in.close();
		fileIn.close();

	}
}