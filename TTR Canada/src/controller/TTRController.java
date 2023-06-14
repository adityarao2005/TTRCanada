package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.CardColour;
import model.City;
import model.Player;
import model.PlayerColour;
import model.Route;
import model.Ticket;
import model.TrainCard;
import view.GameFrame;
import view.GameRulesFrame;

// BY ADITYA
// The TTRController controls all aspects of the game
// It uses the GUI and its sub-controller to assist in providing consistant game flow
// It implements ActionListener to handle button clicks and extends MouseAdapter to
// selectively handle label mouse clicks
// It itself is a Singleton as there is only one instance of it throughout the entire application
public class TTRController extends MouseAdapter implements ActionListener {
	// Fields
	// The Game Frame
	private GameFrame gameFrame;

	// Singletoned variable
	private static TTRController instance;

	// use
	// Stacks to represent the decks, quickly shuffled
	// Cities and Routes - city list and route list
	// Make the coding flexible
	// Make references to each of the 4 controllers
	// Player array
	// Ticket list - class called DefaultListModel look into it
	// current turn - use (currentTurn + 1) % 4
	// Keep track of what they have drawn so far
	// Keep track of minimum number of trains

	// The Sub Controllers
	private FileImportController fileImportController;
	private RouteController routeController;
	private TicketController ticketController;
	private TrainCardController trainCardController;
	private CityController cityController;
	private Map<Player, AIController> aiControllers;

	// The players
	private Player[] players = new Player[4];
	// The current player
	private int currentPlayer = 0;
	// Boolean to indicate whether we are picking up
	private boolean pickUp2nd = false;

	// All the raw routes, tickets and cities from the text file
	private Ticket[] totalTickets;
	private Route[] totalRoutes;
	private City[] totalCities;

	// Constant city name map
	public static final Map<String, City> NAME_MAP = new HashMap<>();

	// Decks
	private Stack<Ticket> ticketDeck = new Stack<>();
	private Stack<TrainCard> trainCardDeck = new Stack<>();
	private Stack<TrainCard> dumpDeck = new Stack<>();

	// Player who has had the list tickets
	private Player endPlayer = null;

	// The limit for the number of trains that causes the game to end
	public static final int TRAIN_LIMIT = 15;

	// HANDLE THE IMPORTING FROM THE DATA FROM THE TEXT FILES
	// FILE IMPORT CONTOLLER import cities and tickets
	// Runs the import cities, routes, and tickets
	//
	// Create the game frame
	// Send the city list and route list so that the game frame knows about the
	// cities and the routes
	// Setup the listeners
	// Write a method to enable or disable the GUI
	// Make the gameFrame visible

	public TTRController() {
		// Create singleton
		instance = this;

		// Create all the controllers
		fileImportController = new FileImportController();
		routeController = new RouteController();
		ticketController = new TicketController();
		trainCardController = new TrainCardController();
		cityController = new CityController();
		aiControllers = new HashMap<>();

		// Read all the routes
		totalCities = fileImportController.readCities().toArray(City[]::new);
		totalRoutes = fileImportController.readRoutes().toArray(Route[]::new);
		totalTickets = fileImportController.readTickets().toArray(Ticket[]::new);

		// Add the mouse listener to all the total cities
		for (City city : totalCities) {
			city.addMouseListener(this);
		}

		// Set the deck and dump stacks
		trainCardController.setDeck(trainCardDeck);
		trainCardController.setDump(dumpDeck);

		// Create the game frame
		gameFrame = new GameFrame();

		// Set the cities and routes for the board panel
		gameFrame.getBoardPanel().setCities(totalCities);
		gameFrame.getBoardPanel().setRoutes(totalRoutes);

		// Refresh the GUI for the board panel
		gameFrame.getBoardPanel().refresh();

		// Make the UI components listeners be handled by the frame
		gameFrame.getCardPanel().getTicketImage().addMouseListener(this);
		gameFrame.getCardPanel().getDeckImage().addMouseListener(this);

		// Setup all the things that can get interacted
		// Menus, buttons, labels, click on the 5 cards
		// Labels use mouselistener and buttons use actionlisteners

		// Turn off or on the buttons and the labels
		// setEnabled method
		gameFrame.getPlayerPanel().getNextTurnButton().addActionListener(this);
		gameFrame.getPlayerPanel().getClaimRouteButton().addActionListener(this);

		// Set the action listeners for the menus
		gameFrame.getNewItem().addActionListener(this);
		gameFrame.getSaveItem().addActionListener(this);
		gameFrame.getLoadItem().addActionListener(this);
		gameFrame.getExitItem().addActionListener(this);
		gameFrame.getHelpItem().addActionListener(this);

		// Make gameframe visible
		gameFrame.setVisible(true);
	}

	// Did someone click on the menuitems or the buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		// If the source was a jmenuitem, handle it in the menuclicks
		if (e.getSource() instanceof JMenuItem) {
			try {
				handleMenuClick((JMenuItem) e.getSource());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() instanceof JButton) {
			// else handle the button clicks
			handleButtonClick((JButton) e.getSource());
		}

	}

	// Handle the label click events
	@Override
	public void mouseClicked(MouseEvent e) {
		// If the game has not started
		if (players[0] == null)
			return;

		// Get the label that was clicked
		JLabel label = ((JLabel) e.getSource());

		// Don't run the mouse listener if the labels are not clicked
		if (!label.isEnabled())
			return;

		// THis should be done in traincardcontroller
		// If the label that was clicked is in the river (i.e. is a traincard)
		if (label instanceof TrainCard) {
			TrainCard card = (TrainCard) label;

			// Makes current player pick up card
			trainCardController.FlippingCard(card, getCurrentRealPlayer());

			// Pick up 2nd means that whether the player has to pick up a second card
			if (card.getType() == CardColour.RAINBOW || pickUp2nd) {

				// Disable all controls but the pass button
				disableEverythingButPass();

				// Reset the boolean pickUp2nd
				pickUp2nd = false;

				// Message the player to alert him
				JOptionPane.showMessageDialog(gameFrame,
						"Turn for " + getCurrentRealPlayer().getName() + " is over. Please click the next button");

			} else {

				// Disable every other control but train cards
				disableGUIButTrainCards();
				// Disable all wild cards in the river
				trainCardController.disableWildCards();

				// Set pickup 2nd to true
				pickUp2nd = true;

				// Disable the save menu until the next turn
				gameFrame.getSaveItem().setEnabled(false);

			}

			// If the label is the ticket deck image
		} else if (label.equals(gameFrame.getCardPanel().getTicketImage())) {

			// If the user successfully selected their new tickets
			if (ticketController.pickUpTicket(getCurrentRealPlayer())) {
				// Disable all controls but the pass button
				disableEverythingButPass();

				// Message the player to alert him
				JOptionPane.showMessageDialog(gameFrame,
						"Turn for " + getCurrentRealPlayer().getName() + " is over. Please click the next button");
			}

			// If the label is the trainCard deck image
		} else if (label.equals(gameFrame.getCardPanel().getDeckImage())) {
			trainCardController.DealSingleCard(getCurrentRealPlayer());

			// Pick up 2nd means that whether the player has to pick up a second card
			if (pickUp2nd) {

				// Disable all controls but the pass button
				disableEverythingButPass();

				// Reset the boolean pickUp2nd
				pickUp2nd = false;

				// Message the player to alert him
				JOptionPane.showMessageDialog(gameFrame,
						"Turn for " + getCurrentRealPlayer().getName() + " is over. Please click the next button");

			} else {

				// Disable every other control but train cards
				disableGUIButTrainCards();
				// Disable all wild cards in the river
				trainCardController.disableWildCards();

				// Set pickup 2nd to true
				pickUp2nd = true;

				// Disable the save item until the next turn
				gameFrame.getSaveItem().setEnabled(false);

			}

			// Now we know that the only mouse clicks come from the cities
		} else {

			// Cast the label to a city
			City clicked = (City) label;

			// Make the adjacent cities visible
			cityController.adjacentCities(totalCities, totalRoutes, getCurrentRealPlayer(), clicked);
		}

		// Refresh the player panel
		gameFrame.getPlayerPanel().refresh();
	}

	// Handles menu clicks
	private void handleMenuClick(JMenuItem source) throws IOException, ClassNotFoundException {
		// Check if the source is the Menu item labelled "new"
		if (source.equals(gameFrame.getNewItem())) {

			// Create a new game
			newGame();

			// Disable the load item
			gameFrame.getLoadItem().setEnabled(false);

			// CHeck if the menu item is labelled "save"
		} else if (source.equals(gameFrame.getSaveItem())) {
			// Saves the game
			fileImportController.saveData(players, ticketDeck, trainCardDeck, dumpDeck,
					gameFrame.getCardPanel().getRiver(), currentPlayer);
			// Displays game saved message
			JOptionPane.showMessageDialog(gameFrame, "Game Saved");

			// Check if the menu item is labelled "load"
		} else if (source.equals(gameFrame.getLoadItem())) {

			try {
				// Pointer to store index of current player
				int[] currents = new int[1];
				TrainCard[] river = new TrainCard[5];

				// Loads the game data
				fileImportController.loadData(players, ticketDeck, trainCardDeck, dumpDeck, river, currents);

				// Set the current player to be the one in the
				currentPlayer = currents[0];

				// Sets the river
				for (int i = 0; i < 5; i++) {
					river[i].addMouseListener(this);
					gameFrame.getCardPanel().setCardAt(i, river[i]);
				}

				// GO through all the players
				for (Player player : players) {
					// If the player is an AI, map the AI contoller to it
					if (player.isAI()) {
						aiControllers.put(player, new AIController(player, true));
					}

					// Go through all the total routes
					for (int i = 0; i < totalRoutes.length; i++) {
						// GO through the players routes and replace any routes which are identical
						for (Route claimedRoute : player.getClaimedRoutes()) {
							if (totalRoutes[i].isIdentical(claimedRoute)) {
								// Set the cities
								claimedRoute.setStartCity(totalRoutes[i].getStartCity());
								claimedRoute.setDestinationCity(totalRoutes[i].getDestinationCity());
								totalRoutes[i] = claimedRoute;
							}
						}
					}
				}

				// Refresh the board panel
				gameFrame.getBoardPanel().refresh();

				// Refreshes the the UI
				gameFrame.getScorePanel().refresh();
				gameFrame.getPlayerPanel().setCurrent(getCurrentRealPlayer());

				gameFrame.getLoadItem().setEnabled(false);
				enableGUIForStartTurn();

				// Send user message
				JOptionPane.showMessageDialog(gameFrame,
						"Game Loaded - " + getCurrentRealPlayer().getName() + "'s Turn");
				
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(gameFrame, "Unable to load old data: File not found");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(gameFrame, "Unable to load old data: I/O error");
			}

			// Check if the menu item is labbelled "exit"
		} else if (source.equals(gameFrame.getExitItem())) {
			// Display a modal to check if they want to exit the game
			if (JOptionPane.showConfirmDialog(gameFrame, "Are you sure you want to exit?") == JOptionPane.YES_OPTION) {
				// Check if they want to save the game first
				if (JOptionPane.showConfirmDialog(gameFrame,
						"Would you like to save the game first?") == JOptionPane.YES_OPTION) {
					// Make sure they complete their turn
					if (!pickUp2nd)
						handleMenuClick(gameFrame.getSaveItem());
					else {
						// Tell the user to complete the turn first
						JOptionPane.showMessageDialog(gameFrame, "Complete the turn first");

						return;
					}
				}

				// Exit
				System.exit(0);
			}
			// Displays the help screen
		} else if (source.equals(gameFrame.getHelpItem())) {
			GameRulesFrame frame = new GameRulesFrame();
			frame.setVisible(true);
		}
	}

	// Handles the button clicks
	private void handleButtonClick(JButton source) {
		// Button clicks either come from next turn button or claim route button
		// If the next turn button was pressed
		if (source.equals(gameFrame.getPlayerPanel().getNextTurnButton())) {
			// We change the player

			changePlayer();

			// Whether the game has ended
			if (getCurrentRealPlayer().equals(endPlayer)) {
				// Disable everything
				disableEverything();

				// Assert that the game is over
				JOptionPane.showMessageDialog(gameFrame, "Game is Over! Time to determine the winner");

				// Determine the winner
				determineWinner();

				return;
			}

			// If the player is an AI, run his turn
			if (getCurrentRealPlayer().isAI()) {
				// Run his turn
				aiControllers.get(getCurrentRealPlayer()).doTurn();
				// Make the player click next turn
				disableEverythingButPass();

				// Return
				return;
			}

			// Enable all controls for next player
			enableGUIForStartTurn();
		} else {
			// Other wise claim the route
			if (claimRoute()) {
				// Check all the tickets if they are completed
				for (Ticket ticket : getCurrentRealPlayer().getTickets())
					ticketController.checkCompleted(ticket, getCurrentRealPlayer());

				// Disable everything but the next turn button
				disableEverythingButPass();

				// Perform a check for if the player has reached the train threshold
				checkForEndGame();

				// Refresh the board
				gameFrame.getBoardPanel().refresh();

			}
		}
	}

	// Creates a new game
	private void newGame() {
		// Reinitialize the stacks
		// Create the decks
		// Reinitialize the stacks //
		getDumpDeck().clear();
		getTrainCardDeck().clear();
		getTicketDeck().clear();
		getTicketDeck().addAll(Arrays.asList(getTotalTickets()));
		trainCardController.CreateCardDeck();

		Collections.shuffle(getTicketDeck()); // Reset the routes

		// Reset the routes
		for (Route route : totalRoutes) {
			route.setClaimer(null);
		}

		// Disable the load menu item
		gameFrame.getLoadItem().setEnabled(true);

		// Gives the players their starting cards
		createPlayers();

		trainCardController.DealCards();

		// Flip the first 5 cards
		// Assume that train card controller automatically adds
		trainCardController.FlipCards();

		// Enables all the GUI
		enableGUIForStartTurn();

		// Set the current player as the first player
		gameFrame.getPlayerPanel().setCurrent(players[0]);

		// Send player 1 message
		JOptionPane.showMessageDialog(gameFrame, "Game Ready - Player 1 Turn");

		// Refresh the score
		gameFrame.getScorePanel().refresh();
	}

	// Changes the player
	private void changePlayer() {
		// Allows for player cycling
		currentPlayer = (currentPlayer + 1) % 4;

		// Update the player panel
		gameFrame.getPlayerPanel().setCurrent(getCurrentRealPlayer());
	}

	// Names with colours
	private void createPlayers() {
		// Create an enum set of all the colours
		EnumSet<PlayerColour> colours = EnumSet.allOf(PlayerColour.class);

		// Deal the tickets for each player
		for (int i = 0; i < players.length; i++) {
			// Ask Player for their name
			String name = JOptionPane.showInputDialog(gameFrame, "What is your name?", "Player " + (i + 1));

			// Create a jcombobox and default the selected item to the first item
			JComboBox<PlayerColour> comboBox = new JComboBox<>(new Vector<>(colours));
			comboBox.setSelectedIndex(0);

			// Ask the user for the colour
			JOptionPane.showMessageDialog(gameFrame, new Object[] { "What colour would you like?", comboBox },
					"Player " + (i + 1), JOptionPane.QUESTION_MESSAGE);

			// Get the selected colour
			PlayerColour chosenColour = (PlayerColour) comboBox.getSelectedItem();

			// Remove the colour from the set
			colours.remove(chosenColour);

			// We need at least 1 human player
			int option = JOptionPane.NO_OPTION;

			// Make sure that AIs can only come in for player 2, 3, and/or 4
			if (i != 0)
				option = JOptionPane.showConfirmDialog(gameFrame, String.format("Is %s going to be an AI?", name), "AI",
						JOptionPane.YES_NO_OPTION);

			// Initialize the player
			players[i] = new Player(chosenColour, name, option == JOptionPane.YES_OPTION);
			players[i].setScore(0);

			// If the player is not an AI
			if (option != JOptionPane.YES_OPTION) {

				// Until the player accepts the tickets keep continuing
				while (!ticketController.pickUpTicket(players[i]))
					;
			} else {

				// Add the AI
				aiControllers.put(players[i], new AIController(players[i]));
				aiControllers.get(players[i])
						.chooseTickets(new ArrayList<>(List.of(ticketDeck.pop(), ticketDeck.pop(), ticketDeck.pop())));

			}

		}
	}

	// Claims the route for the player
	private boolean claimRoute() {
		// Display the routes for the player to take
		Player current = getCurrentRealPlayer();

		// Get all unclaimed routes
		ArrayList<Route> unclaimedRoutes = routeController.getUnclaimedRoutes();

		// Make sure we remove any double routes if one of those routes have been
		// claimed by us
		Set<Route> claimedRoutes = new HashSet<>(current.getClaimedRoutes());
		unclaimedRoutes.removeIf(claimedRoutes::contains);

		// Get the route to be claimed by the user
		Route route = null;

		// Handles the getting of the routes
		{
			// Create a jlist for all the unclaimed routes
			JList<Route> routesList = new JList<Route>(new Vector<>(unclaimedRoutes));
			routesList.setLayoutOrientation(JList.VERTICAL);
			routesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			routesList.setVisibleRowCount(-1);
			routesList.setSelectedIndex(0);

			// Create a scollpane for the list
			JScrollPane listScroller = new JScrollPane(routesList);
			listScroller.setPreferredSize(new Dimension(500, 200));
			listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			// Get the option
			int option = JOptionPane.showConfirmDialog(gameFrame,
					new Object[] { "Choose the route to claim:", listScroller }, "Claim Route",
					JOptionPane.OK_CANCEL_OPTION);

			// If the user decides not to select a route then exit
			if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION)
				return false;

			// Set the route
			route = routesList.getSelectedValue();
		}

		// If the claim route button is not successful, preserve their turn
		if (!routeController.claimRoute(getCurrentRealPlayer(), route)) {
			return false;
		}

		// Refresh
		gameFrame.getScorePanel().refresh();
		gameFrame.getPlayerPanel().refresh();

		// Return true
		return true;
	}

	// Determines the winner
	private void determineWinner() {
		// Go through every player and go through their tickets
		// If they have not completed a ticket then they are deducted points
		Map<Player, Integer> completedTickets = new HashMap<>();

		// Go through all the players and deduct the score of any incomplete tickets associated
		for (Player player : players) {
			// Keep the default number of completed tickets as 0
			completedTickets.put(player, 0);
			for (Ticket ticket : player.getTickets())
				if (!ticket.isCompleted())
					// Deduct the score from the player
					player.setScore(player.getScore() - ticket.getPointWorth());
				else
					// Keep track of the completed tickets
					completedTickets.replace(player, completedTickets.get(player) + 1);
		}

		// Refresh the player panel
		gameFrame.getPlayerPanel().refresh();

		// Compute the longest route for each player
		Map<Player, Integer> longestRouteSize = new LinkedHashMap<>();

		for (Player player : players) {
			longestRouteSize.put(player, routeController.getLongestRoute(player));
		}

		// Find the longest continuous route for each player
		ArrayList<Player> longestRouteOwner = new ArrayList<Player>();

		longestRouteOwner.add(players[0]);

		// determine the player(s) that own(s) the longest continuous route
		for (Map.Entry<Player, Integer> curEntry : longestRouteSize.entrySet()) {

			if (curEntry.getValue() > longestRouteSize.get(longestRouteOwner.get(0))) {
				longestRouteOwner.clear();
				longestRouteOwner.add(curEntry.getKey());
			} else if (curEntry.getValue() == longestRouteSize.get(longestRouteOwner.get(0))) {
				longestRouteOwner.add(curEntry.getKey());
			}

		}

		// Add 10 points to the player(s) that own(s) the longest continuous route
		for (Player curPlayer : longestRouteOwner) {
			curPlayer.setScore(curPlayer.getScore() + 10);
		}

		// Refresh the score panel
		gameFrame.getScorePanel().refresh();

		// Get the final scores of each player
		Map<Player, Integer> scores = new HashMap<>();
		for (Player curPlayer : players) {
			scores.put(curPlayer, curPlayer.getScore());
		}

		// if there are more than one player who has the highest score,
		// determine the winner by checking who has the most completed tickets
		ArrayList<Player> winners = this.getWinnerList(scores, Arrays.asList(players));
		if (winners.size() > 1) {

			winners = this.getWinnerList(completedTickets, winners);

			// if there are still more than one player exists in the winner list,
			// determine the winner by checking who has the longest continuous route
			if (winners.size() > 1) {

				winners = this.getWinnerList(longestRouteSize, winners);

				// if there are still more than one player exists in the winner list(for
				// real???),
				// Congratulate them they got a tie
				if (winners.size() > 1) {

					String congrat = "";
					for (Player curPlayer : winners) {

						// if current player is the last one in the winner list
						if (winners.get(winners.size() - 1) == curPlayer) {
							congrat += " and " + curPlayer.getName() + " have a Tie!!!!!!";
						}
						// or if current player is the first one in the winner list
						else if (winners.get(0) == curPlayer) {
							congrat += curPlayer.getName();
						} else {
							congrat += ", " + curPlayer.getName();
						}

					}
					JOptionPane.showMessageDialog(gameFrame, congrat);
					return;

				}

			}

		}
		// Congratulate the only winner
		JOptionPane.showMessageDialog(gameFrame, winners.get(0).getName() + " Won!!!!!!");
	}

	// Determine the player(s) that have the highest certain number out of the other
	// players
	private ArrayList<Player> getWinnerList(Map<Player, Integer> map, List<Player> playerList) {

		ArrayList<Player> winnerList = new ArrayList<Player>();
		for (Player curPlayer : playerList) {
			if (winnerList.isEmpty() || map.get(curPlayer) > map.get(winnerList.get(0))) {
				winnerList.clear();
				winnerList.add(curPlayer);
			} else if (map.get(curPlayer) == map.get(winnerList.get(0))) {
				winnerList.add(curPlayer);
			}
		}
		return winnerList;
	}

	// Enables all the GUI required for start
	private void enableGUIForStartTurn() {
		// Enable the train deck
		// Enable the ticket deck
		// next turn button
		// claim route button
		// train cards on rivre
		// Enable the save item
		gameFrame.getSaveItem().setEnabled(true);
		gameFrame.getExitItem().setEnabled(true);
		gameFrame.getCardPanel().getTicketImage().setEnabled(true);
		gameFrame.getCardPanel().getDeckImage().setEnabled(true);
		gameFrame.getPlayerPanel().getNextTurnButton().setEnabled(true);
		gameFrame.getPlayerPanel().getClaimRouteButton().setEnabled(true);

		// Set enabled
		for (int i = 0; i < 5; i++) {
			gameFrame.getCardPanel().getCardAt(i).setEnabled(true);
		}
	}

	// Remember to also disable the pass button
	// Disables wildcards
	private void disableGUIButTrainCards() {
		// Enable the train deck
		// Enable the ticket deck
		// next turn button
		// claim route button
		// train cards on rivre
		gameFrame.getSaveItem().setEnabled(false);
		gameFrame.getExitItem().setEnabled(false);
		gameFrame.getCardPanel().getTicketImage().setEnabled(false);
		gameFrame.getCardPanel().getDeckImage().setEnabled(true);
		gameFrame.getPlayerPanel().getNextTurnButton().setEnabled(false);
		gameFrame.getPlayerPanel().getClaimRouteButton().setEnabled(false);

		// Enables the traincards
		for (int i = 0; i < 5; i++) {
			gameFrame.getCardPanel().getCardAt(i).setEnabled(true);
		}
	}

	// Disabled all the controls save for the pass button, called at the end of a turn
	private void disableEverythingButPass() {
		// Enable the train deck
		// Enable the ticket deck
		// next turn button
		// claim route button
		// train cards on river
		gameFrame.getSaveItem().setEnabled(false);
		gameFrame.getExitItem().setEnabled(false);
		gameFrame.getCardPanel().getTicketImage().setEnabled(false);
		gameFrame.getCardPanel().getDeckImage().setEnabled(false);
		gameFrame.getPlayerPanel().getNextTurnButton().setEnabled(true);
		gameFrame.getPlayerPanel().getClaimRouteButton().setEnabled(false);

		// Disables the train cards
		for (int i = 0; i < 5; i++) {
			gameFrame.getCardPanel().getCardAt(i).setEnabled(false);
		}
	}
	
	// Disables all controls, called at the end of a game
	private void disableEverything() {
		// Disables all controls but pass button
		disableEverythingButPass();
		// Disables pass button
		gameFrame.getPlayerPanel().getNextTurnButton().setEnabled(false);
	}

	// Checks whether we have reached the end of the game
	public void checkForEndGame() {
		if (getCurrentRealPlayer().endGame() && endPlayer == null) {
			// Set the player
			endPlayer = getCurrentRealPlayer();

			String text = String.format(
					"The game is almost over! %1$s has reached %2$d which is less than or equal to %3$d trains. \n Everyone gets their last turn. Turn for %1$s is over. Please click the next button",
					endPlayer.getName(), endPlayer.getNumberOfTrain(), TRAIN_LIMIT);

			// Alert user that everyone has last turn except the current player
			JOptionPane.showMessageDialog(gameFrame, text, "Alert", JOptionPane.PLAIN_MESSAGE);
		} else {
			// Alert the player that he has to take his next turn
			JOptionPane.showMessageDialog(gameFrame,
					"Turn for " + getCurrentRealPlayer().getName() + " is over. Please click the next button");
		}
	}

	// Getters
	public GameFrame getGameFrame() {
		return gameFrame;
	}

	public TrainCardController getTrainCardController() {
		return trainCardController;
	}

	public FileImportController getFileImportController() {
		return fileImportController;
	}

	public RouteController getRouteController() {
		return routeController;
	}

	public TicketController getTicketController() {
		return ticketController;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public Ticket[] getTotalTickets() {
		return totalTickets;
	}

	public Route[] getTotalRoutes() {
		return totalRoutes;
	}

	public City[] getTotalCities() {
		return totalCities;
	}

	public Stack<TrainCard> getTrainCardDeck() {
		return trainCardDeck;
	}

	public Stack<TrainCard> getDumpDeck() {
		return dumpDeck;
	}

	public Stack<Ticket> getTicketDeck() {
		return ticketDeck;
	}

	public Player[] getPlayers() {
		return players;
	}

	public Player getCurrentRealPlayer() {
		return players[currentPlayer];
	}

	// Singleton utility
	public static TTRController getInstance() {
		return instance;
	}

}
