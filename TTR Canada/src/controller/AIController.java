package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import model.CardColour;
import model.City;
import model.Player;
import model.Route;
import model.Ticket;
import model.TrainCard;

// BY ADITYA
// When running AI controller in TTRController store it in map
public class AIController {
	// Singleton instance of the main controller
	private TTRController controller;
	// AI player
	private Player ai;
	// Required routes in the MST
	private Set<Route> requiredRoutes;
	// Cities that are apart of the tickets
	private Set<City> anchors = new HashSet<>();

	// Map of traincards required
	private Map<CardColour, ArrayList<List<Route>>> requiredCards;
	// Minimum amount of traincards required for each card
	private Map<CardColour, Integer> minCards;

	// Create an AI controller based on the player
	public AIController(Player ai) {
		// Call the other constructor
		this(ai, false);
	}

	// Create an AI controller based on the player. Checks whether we are getting
	// the AI from serial state
	public AIController(Player ai, boolean fromFile) {
		// Set the player to the player passed into the constructor
		this.ai = ai;

		// Initialize the fields
		controller = TTRController.getInstance();
		requiredRoutes = new HashSet<>();
		requiredCards = new HashMap<>();
		minCards = new HashMap<>();

		// Initialize the minimum cards required map
		clearCollections();

		// If we are recieving the AI from the file
		if (fromFile) {
			// Go through all the tickets the player has
			for (Ticket ticket : ai.getTickets()) {
				// Add the anchor cities
				anchors.add(ticket.getStartCity());
				anchors.add(ticket.getDestinationCity());
			}
		}
	}

	// Clears the collections so that we can create a new fresh MST every time
	private void clearCollections() {
		// Clear the collections
		requiredCards.clear();
		requiredRoutes.clear();
		// Initialize the minimum cards required map
		for (CardColour color : CardColour.values()) {
			// Put an empty array/list in the
			requiredCards.putIfAbsent(color, new ArrayList<>(7));

			// Clears the list if there are stuff in it
			requiredCards.get(color).clear();

			// Add an arraylist to the routes based on size
			for (int i = 0; i < 7; i++)
				requiredCards.get(color).add(new ArrayList<>());

			// Keep the colors at 0
			minCards.put(color, Integer.MAX_VALUE);
		}

	}

	// AI chooses the tickets - only done at the start of the game
	// AI will always choose 3 tickets
	public void chooseTickets(List<Ticket> tickets) {
		// Add the tickets to the AI
		ai.getTickets().addAll(tickets);
		tickets.removeAll(tickets);

		// Then add the endpoints of the cities to the anchor set
		for (Ticket ticket : ai.getTickets()) {

			// Add anchor cities
			anchors.add(ticket.getStartCity());
			anchors.add(ticket.getDestinationCity());
		}
	}

	// Decides whether to pick up tickets, pick up cards from the deck or river, or
	// claim a route
	public void doTurn() {

		// Create the road map
		createRoadMap();

		// Now make decision on whether to pick up cards or to claim a route

		// If we do not have enough cards to claim a route, then pick up cards
		// Pointers
		Route[] toBeClaimed = new Route[1];
		CardColour[] selectedType = new CardColour[1];

		// Check if we can claim a route
		getClaimingRoute(toBeClaimed, selectedType);

		// If we are about to claim the route
		if (toBeClaimed[0] != null) {

			// Claim the route
			controller.getRouteController().claimRoute(ai, toBeClaimed[0], selectedType[0]);
			controller.getGameFrame().getScorePanel().refresh();

			// Check all the tickets if they are completed
			for (Ticket ticket : ai.getTickets())
				controller.getTicketController().checkCompleted(ticket, ai);

			// Refresh both panels
			controller.getGameFrame().getScorePanel().refresh();
			controller.getGameFrame().getPlayerPanel().refresh();

			// Checks whether we have reached the end of the game
			controller.checkForEndGame();

			// Otherwise we are picking up cards
			// We decide whether we do it from the deck or the river
		} else {

			// Create a map for the remainder
			Map<CardColour, Integer> remainderCards = new HashMap<>();

			// Get the one with the maximum number of trains
			selectedType[0] = getMaxColour();

			// Create our remainder cards map
			createPriorities(remainderCards, getMaxColour());

			// Picks up the cards from the deck or river
			pickUpCards(remainderCards);

		}

	}

	// Decides to either pick up cards from the deck or the river then picks them up
	private void pickUpCards(Map<CardColour, Integer> remainderCards) {

		// Create a list of cards required based on priority
		List<CardColour> priorityCards = new ArrayList<>(remainderCards.keySet());

		// Sort the list based on the number of cards required
		Collections.sort(priorityCards, Comparator.comparing(remainderCards::get));

		// Keep the priority of picking a rainbow card at half
		priorityCards.add(priorityCards.size() / 2, CardColour.RAINBOW);

		// Check the river and pick cards
		// Flag for 2nd Turn
		boolean secondTurn = false;

		// Do while loop for decision
		do {

			// If this is the second turn
			if (secondTurn) {
				// Remove the joker from there
				priorityCards.remove(CardColour.RAINBOW);

				// Sort the list based on the number of cards required
				Collections.sort(priorityCards, Comparator.comparing(remainderCards::get));

			}

			// Check whether we picked up a card
			CardColour removal = aiPickUp(priorityCards, remainderCards);

			// If we did not pick up a card from the river, then pick up a card from the
			// deck
			if (removal == null) {

				// Pick up from the deck
				controller.getTrainCardController().DealSingleCard(ai);

				// Get the card added just now
				CardColour last = ai.getTrainCards().get(ai.getTrainCards().size() - 1).getType();

				// Check if we picked up something that we neeed
				if (remainderCards.containsKey(last)) {
					// Decrement the amount we need for priority
					remainderCards.put(last, remainderCards.get(last) - 1);

				}

				// Send a message to the players
				JOptionPane.showMessageDialog(controller.getGameFrame(),
						String.format("%s has picked up a card from the deck", ai.getName()));

			} else {

				// Send a message to the players
				JOptionPane.showMessageDialog(controller.getGameFrame(),
						String.format("%s has picked up %s from the card panel", ai.getName(), removal));

				// If we picked up a rainbow card, we are done
				if (removal == CardColour.RAINBOW)
					break;
			}

			// If we have reached our goal, we can remove it from our priority
			if (remainderCards.get(removal) == 0) {
				remainderCards.remove(removal);
				priorityCards.remove(removal);
			}

			// Invert the second turn
			secondTurn = !secondTurn;
			controller.getGameFrame().getPlayerPanel().refresh();

			// Go again for a second time
		} while (secondTurn);
	}

	// Picks up a card on the priority, otherwise returns null
	private CardColour aiPickUp(List<CardColour> priorityCards, Map<CardColour, Integer> remainderCards) {
		// Go through all the cards in our priority list
		for (CardColour card : priorityCards) {

			// Go through all the cards from the river
			for (TrainCard riverCard : controller.getGameFrame().getCardPanel().getRiver()) {
				// If the cards are of the same type
				if (riverCard.getType() == card) {
					// We are going to pick up

					// Pick up the card
					controller.getTrainCardController().FlippingCard(riverCard, ai);

					// Decrement the amount we need for priority
					if (card != CardColour.RAINBOW)
						remainderCards.put(card, remainderCards.get(card) - 1);

					// Exit the loop
					return card;

				}
			}

		}

		// Indicate that we have not picked up a card
		return null;
	}

	// Create our priority mapping
	private void createPriorities(Map<CardColour, Integer> remainderCards, CardColour selectedType) {
		// Subtract the minimum cards needed from what the AI has in their hand
		for (CardColour colour : CardColour.values()) {
			// If the colour is a rainbow card, then get the colour with the max value
			if (colour == CardColour.RAINBOW) {
				// Calculate the amount of cards needed
				int cardsReq = minCards.get(colour) - ai.checkTrainCard(selectedType);

				// If we require more cards
				if (cardsReq > 0) // Select the smaller of this or the older card
					// If the card does not exist in the map, then choose the the other value
					remainderCards.put(selectedType,
							Math.min(remainderCards.getOrDefault(selectedType, Integer.MAX_VALUE), cardsReq));
				// Otherwise subtract the values
			} else {
				// Calculate the amount of cards needed
				int cardsReq = minCards.get(colour) - ai.checkTrainCard(colour);

				// If we require more cards
				if (cardsReq > 0)
					remainderCards.put(colour, cardsReq);
			}

		}
	}

	// Check for the route with the least amount of things to be claimed
	private void getClaimingRoute(Route[] toBeClaimed, CardColour[] selectedType) {
		// We start from the cheapest routes to the most expensive routes to be claimed
		// We can do the other way later, if we feel that it would make it better
		// Go through all the card colours
		for (CardColour colour : CardColour.values()) {

			// If the AI does not have enough train cards for the smallest route with the
			// card
			// We cannot claim it
			if (minCards.get(colour) > ai.getNumberOfTrain()) {
				continue;
			}

			// First check if it is a rainbow route
			if (colour == CardColour.RAINBOW) {
				// Check if the cards in the AI's hand is or exceeds the minimum
				if (minCards.get(colour) <= ai.checkMaxTrainCard()) {

					// Get the first route of that colour
					toBeClaimed[0] = requiredCards.get(colour).get(minCards.get(colour)).remove(0);

					// Get the colour with the max amount of cards
					selectedType[0] = getMaxColour();

					// Break out of the loop, we are claiming this route
					break;
				}

			} else {
				// Check if the cards in the AI's hand is or exceeds the minimum
				if (minCards.get(colour) <= ai.checkTrainCard(colour) + ai.checkTrainCard(CardColour.RAINBOW)) {
					// Get the first route
					toBeClaimed[0] = requiredCards.get(colour).get(minCards.get(colour)).remove(0);

					selectedType[0] = colour;
					// Break out of the loop, we are claiming this route
					break;
				}
			}
		}
	}

	// Creates a route tree and then prunes it so we can only focus on the anchor
	// cities and their connections
	// MST Along with pruning algorithm
	private void createRoadMap() {
		// Clear the collections
		clearCollections();
		// Add the claimed routes from the player into it
		// Create a disjoint set data structure
		Map<City, Set<City>> disjointSet = new HashMap<>();

		// Default set of city path is itself
		for (City city : controller.getTotalCities()) {
			disjointSet.put(city, Set.of(city));
		}

		// Add all the claimed routes into the Disjoint Set
		addClaimedRoutes(disjointSet);

		// Get the unclaimed routes
		List<Route> unclaimedRoutes = controller.getRouteController().getUnclaimedRoutes();

		// Remove duplicate routes
		removeDuplicates(unclaimedRoutes);

		// Then add all the values of the unclaimed routes
		krustalsMST(unclaimedRoutes, disjointSet);

		// If all the tickets are completed, then just cause chaos by claiming other
		// routes, otherwise, just stick to the main longest path
		if (!checkTicketsCompleted()) {
			// Prunes the tree
			pruneTree();

		}

		// Remove the claimed routes from the MST
		for (Route claimed : ai.getClaimedRoutes()) {
			requiredRoutes.remove(claimed);
		}

		// Go through all the routes and add the values to the maps
		for (Route route : requiredRoutes) {
			// Adds the route at the train type and length
			// basicaly in C/C++: requiredCards[route.trainType][route.length].add(route);
			requiredCards.get(route.getTrainType()).get(route.getLength()).add(route);

			// basically in C/C++: minCards[route.trainType] =
			// max(minCards[route.trainType], route.length)
			minCards.put(route.getTrainType(), Math.min(minCards.get(route.getTrainType()), route.getLength()));
		}

	}

	// Check if all the tickets are completed
	private boolean checkTicketsCompleted() {
		// Go through all the tickets
		for (Ticket ticket : ai.getTickets())
			// If the ticket is not completed, return false
			if (!ticket.isCompleted())
				return false;

		// Return completed
		return true;
	}

	// Adds the claimed routes to the disjoint set ds
	private void addClaimedRoutes(Map<City, Set<City>> disjointSet) {
		// Then add all the values of the claimed route into the disjoint set
		for (Route claimed : ai.getClaimedRoutes()) {
			// Merge the cities into the disjoint set
			City first = claimed.getStartCity();
			City second = claimed.getDestinationCity();

			// Get the sets
			Set<City> set1 = disjointSet.getOrDefault(first, Set.of(first));
			Set<City> set2 = disjointSet.getOrDefault(second, Set.of(second));

			// Merge the sets
			Set<City> set = new HashSet<>();
			set.addAll(set1);
			set.addAll(set2);

			// Keep it in the map
			for (City value : set) {
				disjointSet.replace(value, set);
				requiredRoutes.add(claimed);
			}

		}
	}

	// Removes the possibility of running into dual routes when choosing routes
	// If the route is a dual route, choose the route that is gray
	// Removes dual routes from the list and picks the best of them
	private void removeDuplicates(List<Route> unclaimedRoutes) {
		// Avoiding concurrent modification exception
		ArrayList<Route> removeDelegate = new ArrayList<>();

		// Get rid of double routes, Choose rainbow routes over any other double route
		for (Route route : unclaimedRoutes) {
			// Check if the route is a dual rail
			if (route.isDualRail()) {
				// Get the other dual route
				Route other = controller.getRouteController().getOtherRoute(route);

				// Make sure that no one has claimed the other route
				if (other.getClaimer() == null) {
					// Choose if the other card is RAINBOW then remove this card
					// Otherwise remove it from the list only if it is there
					if (other.getTrainType() == CardColour.RAINBOW) {
						removeDelegate.add(route);
					} else {
						removeDelegate.add(other);
					}
				}
			}

		}

		// Removes all the unwanted routes
		unclaimedRoutes.removeAll(removeDelegate);
	}

	// Perform MST, more specifically krustal's MST to get all the routes necessary
	// for the roadmap
	// Krustals MST
	private void krustalsMST(List<Route> unclaimedRoutes, Map<City, Set<City>> disjointSet) {

		// Sort the routes
		Collections.sort(unclaimedRoutes, Comparator.comparing(Route::getLength));

		// Go through the all the routes, add the values route if there is no cycle
		// detected
		for (Route route : unclaimedRoutes) {
			// Merge the cities into the disjoint set
			City first = route.getStartCity();
			City second = route.getDestinationCity();

			// Get the sets
			Set<City> set1 = disjointSet.get(first);
			Set<City> set2 = disjointSet.get(second);

			// If the sets are disjoint, merge them
			if (Collections.disjoint(set1, set2)) {
				// Merge the sets
				Set<City> set = new HashSet<>();
				set.addAll(set1);
				set.addAll(set2);

				// Keep it in the map
				for (City value : set) {
					disjointSet.replace(value, set);
				}

				// Add the route to the required routes
				requiredRoutes.add(route);

			}
		}
	}

	// Makes sure that once we do have the MST we can focus only on the anchor
	// cities and their connections
	// Pruning algorithm
	// Start with all the leafs, adjacency list and check that there is only 1 node
	// that they are adjacent to
	// If the node is not a required node then remove it from the graph and check
	// its parent
	// Otherwise end
	private void pruneTree() {
		// Create an adjacency map of all the cities to its routes
		Map<City, List<Route>> adj = new HashMap<>();

		// Go through all the required routes and add them to the adjacency list
		for (Route route : requiredRoutes) {
			// Get the cities
			City a = route.getStartCity();
			City b = route.getDestinationCity();

			// If the list is not mapped already then add them
			adj.putIfAbsent(a, new ArrayList<>());
			adj.putIfAbsent(b, new ArrayList<>());

			// Map the route to the cities
			adj.get(a).add(route);
			adj.get(b).add(route);
		}

		// Get all the leaves, leaves will have only 1 route connected to them
		// DFS implementation to prune a branch
		List<Route> prunable = new ArrayList<>();

		// Go through all the cities and check whether they are leaves
		// If they are, begin the pruning process
		for (City city : adj.keySet())
			if (adj.get(city).size() == 1)
				pruneBranch(city, prunable, adj);

		// Remove the pruned branches from the requiredRoutes list
		requiredRoutes.removeAll(prunable);

		// Now we've got a direct path for all the tickets!!!
	}

	// The main pruning algorthm works like so
	// We create an adjacency list to hold the connections
	// We then go through all the leaves and work our way up the tree until we find
	// a anchor city
	// Once we have found an anchor city, we can remove that branch (excluding the
	// anchor city)
	// This process repeats recursively
	private void pruneBranch(City start, List<Route> prunable, Map<City, List<Route>> adj) {
		// Base case: If we have encountered one of the cities from the tickets, stop
		// pruning
		if (anchors.contains(start))
			return;

		// Recursive call

		// Get a copy of the adjacent routes
		List<Route> routes = new ArrayList<>(adj.get(start));

		// Remove any routes that are being pruned
		routes.removeAll(prunable);

		// If there are more than one, skip
		if (routes.size() > 1)
			return;

		// If we have reached a dead end
		if (routes.size() == 0)
			return;

		// Else prune the last route and move to the other city
		// Get the route that is being pruned
		Route route = routes.get(0);

		// Prune the last route
		prunable.add(route);

		// Recurse the process onto the next city
		pruneBranch(route.getOther(start), prunable, adj);

	}

	// Utility method to return the traincard color that is most present in the AI's
	// hand
	private CardColour getMaxColour() {
		// Create the selected type
		CardColour selectedType = null;
		// Get the one with the maximum number of trains
		int maxValue = 0;
		// Go through all the colours
		for (CardColour c : CardColour.values()) {
			// If we have gotten more cards than the maximum value
			if (maxValue < ai.checkTrainCard(c)) {
				// Set the value and the selected type
				maxValue = ai.checkTrainCard(c);
				selectedType = c;
			}
		}
		return selectedType;
	}
}
