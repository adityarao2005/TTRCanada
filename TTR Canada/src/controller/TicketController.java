package controller;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import model.City;
import model.Player;
import model.Route;
import model.Ticket;

// BY ADITYA
public class TicketController {
	private TTRController main;

	// Constructor - Requires an initial amount of tickets
	public TicketController() {
		main = TTRController.getInstance();
	}

	// Method to pick up tickets
	// Requires both player and gameframe
	public boolean pickUpTicket(Player player) {
		Stack<Ticket> ticketDeck = main.getTicketDeck();
		// Selects the top 3 tickets on the stack
		List<Ticket> tickets = new ArrayList<>(
				Collections.list(ticketDeck.elements()).stream().limit(3).collect(Collectors.toList()));

		// Array of checkboxes
		JCheckBox[] checkBoxes = new JCheckBox[3];
		Set<Ticket> ticketSet = new HashSet<>();

		// Initialize the checkboxes
		for (int i = 0; i < checkBoxes.length; i++) {
			checkBoxes[i] = new JCheckBox(tickets.get(i).toString());
			checkBoxes[i].setSelected(true);
			ticketSet.add(tickets.get(i));
		}

		// Perform once then check flag if done properly
		do {

			// Create our parameters for our optionpane
			Object[] params = { "Select the tickets (You must pick at least two tickets)", checkBoxes[0], checkBoxes[1], checkBoxes[2] };
			// Show the JOptionPane and get the response
			int n = JOptionPane.showConfirmDialog(main.getGameFrame(), params, "Tickets", JOptionPane.OK_CANCEL_OPTION);

			// If the response is the closed or cancel option, return false, it did not work
			if (n == JOptionPane.CLOSED_OPTION || n == JOptionPane.CANCEL_OPTION) {
				return false;
			}

			// Check how many checkboxes have been selected
			int selected = 0;

			for (int i = 0; i < checkBoxes.length; i++)
				if (checkBoxes[i].isSelected())
					selected++;

			// If the selected checkboxes are 2 or 3 then break out of the loop
			if (selected > 1)
				break;

			// Continue until we break out of the loop
		} while (true);

		// Go through all the checkboxes again, the selected checkboxes
		for (int i = 0; i < checkBoxes.length; i++) {
			if (checkBoxes[i].isSelected()) {
				// Get the ticket from the checkboxes
				Ticket ticket = tickets.get(i);

				// Remove the tickets from the temporary list
				ticketSet.remove(ticket);
				// Remove them from the ticket deck
				ticketDeck.remove(ticket);
				// Add the ticket to the players hand
				player.getTickets().add(ticket);
				
				// check if the tickets have been completed
				checkCompleted(ticket, player);
			}
		}

		// What ever is left goes to the bottom of the deck
		for (Ticket ticket : ticketSet) {
			ticketDeck.remove(ticket);
			ticketDeck.add(ticket);
		}

		// Returns true
		return true;
	}

	// Checks whether the ticket has already been completed
	// Runs a BFS to check whether the routes for the starting and ending have been
	// found in the players routes
	public void checkCompleted(Ticket ticket, Player player) {
		// CHeck if the ticket has already been completed
		if (ticket.isCompleted())
			return;

		// First create an adjacency list
		Map<City, List<City>> adj = new HashMap<>();

		// For every route claimed by the player
		// Add the cities to the adjacency list
		for (Route route : player.getClaimedRoutes()) {
			// If the lists do not exist as of yet for the city, create them
			adj.putIfAbsent(route.getStartCity(), new ArrayList<>());
			adj.putIfAbsent(route.getDestinationCity(), new ArrayList<>());

			// Then set both cities as adjacent to each other
			adj.get(route.getStartCity()).add(route.getDestinationCity());
			adj.get(route.getDestinationCity()).add(route.getStartCity());
		}

		// Create the city queue and visited set
		Queue<City> queue = new ArrayDeque<>();
		Set<City> visited = new HashSet<>();
		// add the start city to both visited and queue
		queue.add(ticket.getStartCity());
		visited.add(ticket.getStartCity());

		// Run BFS algorithm
		// Until we have not visited all the adjacent cities
		while (!queue.isEmpty()) {
			// Poll the current city
			City current = queue.poll();

			// Check if the current city is the destination, if it does, return true and
			// mark the ticket as completed
			if (current.equals(ticket.getDestinationCity())) {

				// Set the ticket as completed
				ticket.setCompleted(true);

				// Update the players score
				player.setScore(player.getScore() + ticket.getPointWorth());

				// Refreshes the score panel
				JOptionPane.showMessageDialog(main.getGameFrame(), "You've just completed a ticket: " + ticket);
				
				
				// Refresh the score panel
				main.getGameFrame().getScorePanel().refresh();
				return;
			}

			// Go to all the adjacent cities
			for (City next : adj.getOrDefault(current, List.of())) {
				// If the adjacent city has not been visited then add it to the queue
				if (visited.add(next))
					queue.add(next);

			}
		}

	}
}
