package controller;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;

//By JOE
//Route Controller that 
//- manipulate the claimed status of the routes
//- read and find all the unclaimed routes
//- Find the longest continuous route owned by a player
public class RouteController {

	private TTRController controller;

	// constructor
	public RouteController() {
		controller = TTRController.getInstance();

	}

	// claim a route for a player; if failed, returns false
	public boolean claimRoute(Player player, Route route) {

		// check if the player has enough trains to claim this route
		if (player.getNumberOfTrain() >= route.getLength()) {

			CardColour selectedType = null;
			ArrayList<CardColour> choice = new ArrayList<>();

			// check if the player has enough cards to claim the route
			
			// if the current route type is grey
			if (route.getTrainType() == CardColour.RAINBOW) {

				//check if the player's rainbow cards 
				// plus the cards with types other than rainbow that has the max amount
				// is enough to claim this route
				//eg. if player has 2 red, 5 blue, 1 green, 4 pink, 1 rainbow
				//check if the amount of blue cards and rainbow cards is enough to claim the route
				
				//if the player has enough cards to claim this route, get all the possible choices of card types
				if (player.checkMaxTrainCard() + player.checkTrainCard(CardColour.RAINBOW) >= route.getLength()) {

					//check each types(other than rainbow) of the cards that the player has
					for (CardColour type : CardColour.values()) {

						if (type != CardColour.RAINBOW) {

							//if the amount of current type of cards plus the amount of rainbow cards
							//is enough to claim the route, add this type into the choice array list
							if (player.checkTrainCard(type) + player.checkTrainCard(CardColour.RAINBOW) >= route
									.getLength()) {

								choice.add(type);
							}

						}

					}

					// Ask which type of card the player want to use
					selectedType = (CardColour) JOptionPane.showInputDialog(null,
							"Choose the type of the Cards you wanna use", "Card Type Selection",
							JOptionPane.QUESTION_MESSAGE, null, choice.toArray(), choice.get(0));

				} 
				//if the player does not have enough cards to claim this route, tell the user and return false
				else {

					JOptionPane.showMessageDialog(controller.getGameFrame(), "You are unable to claim the route");

					return false;
				}
				
			} 
			
			//if the current route type is not grey
			else {

				// check if the player has enough card with the type that matches the current route type
				//and rainbow cards to claim this route
				if (player.checkTrainCard(route.getTrainType()) + player.checkTrainCard(CardColour.RAINBOW) >= route
						.getLength()) {

					selectedType = route.getTrainType();
				} 
				
				//if the player does not have enough cards to claim this route, tell the user and return false.
				else {

					JOptionPane.showMessageDialog(controller.getGameFrame(), "You are unable to claim the route");

					return false;
				}

			}

			//claim the route for the player
			claimRoute(player, route, selectedType);

			return true;
		}

		//if the player does not have enough trains to claim this route, tell the user and return false
		JOptionPane.showMessageDialog(controller.getGameFrame(), "You are unable to claim the route");
		return false;
	}

	// claim a route for a player; if failed, returns false
	public void claimRoute(Player player, Route route, CardColour selectedType) {

		//Store the amount of card that is going to be used into selected_count
		int selected_count = Math.min(route.getLength(), player.checkTrainCard(selectedType));
		//Store the amount of additional required rainbow card to be used into rainbow_count
		int rainbow_count = route.getLength() - selected_count;
		/*
		 * String rainbow_card_remove = (rainbow_count > 0 ?
		 * String.format("and %d Rainbow Cards", rainbow_count) : "");
		 * System.out.printf("Removed %d %s Cards %s from your inventory\n",
		 * selected_count, selectedType.toString(), rainbow_card_remove);
		 */

		
		ArrayList<TrainCard> delegateRemove = new ArrayList<>();

		//get all the cards that is going to be removed from the player's cards
		for (TrainCard x : player.getTrainCards()) {

			if (selected_count > 0 || rainbow_count > 0) {

				if (selected_count > 0) {

					if (x.getType().equals(selectedType)) {
						delegateRemove.add(x);
						selected_count--;

					}

				}

				if (rainbow_count > 0) {
					if (x.getType().equals(CardColour.RAINBOW)) {
						delegateRemove.add(x);
						rainbow_count--;
					}
				}
			} else {
				break;
			}
		}

		//remove the cards that the player used to claim the route and add the removed cards into the dump deck
		player.getTrainCards().removeAll(delegateRemove);
		controller.getDumpDeck().addAll(delegateRemove);

		// Claim the route to the player
		route.setClaimer(player);
		player.setNumberOfTrain(player.getNumberOfTrain() - route.getLength());
		player.getClaimedRoutes().add(route);

		// Add score that this route worth to player's score
		int scoreWorth = 0;
		switch (route.getLength()) {
		case 1:
			scoreWorth = 1;
			break;
		case 2:
			scoreWorth = 2;
			break;
		case 3:
			scoreWorth = 4;
			break;
		case 4:
			scoreWorth = 7;
			break;
		case 5:
			scoreWorth = 10;
			break;
		case 6:
			scoreWorth = 15;
			break;
		case 7:
			scoreWorth = 18;
			break;
		}

		player.setScore(player.getScore() + scoreWorth);

		// prompt the route the player claimed
		JOptionPane.showMessageDialog(controller.getGameFrame(),
				String.format("%s have claimed %s, Congrats!", player.getName(), route.toString()));

	}

	// return an arraylist of the routes that have not get claimed by any player yet
	public ArrayList<Route> getUnclaimedRoutes() {

		ArrayList<Route> unclaimedRoutes = new ArrayList<Route>();

		for (Route route : controller.getTotalRoutes()) {
			if (route.getClaimer() == null) {
				unclaimedRoutes.add(route);
			}
		}

		return unclaimedRoutes;
	}

	public int getLongestRoute(Player player) {

		Map<City, ArrayList<Route>> map = new HashMap<>();
		ArrayList<City> connectedCityList = new ArrayList<City>();
		ArrayList<Route> routeList = new ArrayList<Route>();

		// Go through all the routes that the player owns
		for (Route curRoute : player.getClaimedRoutes()) {

			// Get the cities that are being connected by the routes(aka start city &
			// destination city) that the player owns
			// Add all of them into connected city list

			// Check if the start/destination city of the current route has already existed
			// in the connected city list
			// if not, add it into the list
			if (!connectedCityList.contains(curRoute.getStartCity())) {
				connectedCityList.add(curRoute.getStartCity());
			}
			if (!connectedCityList.contains(curRoute.getDestinationCity())) {
				connectedCityList.add(curRoute.getDestinationCity());
			}
		}

		// System.out.println(connectedCityList.toString());

		// Go through all the cities in the connected city list
		for (City curCity : connectedCityList) {

			routeList = new ArrayList<Route>();

			// Go through all the routes that the player owns
			for (Route route : player.getClaimedRoutes()) {

				// Get the routes that connects the current city
				// Add them into the routeList if they are not present in the routeList
				if (!routeList.contains(route)
						&& (route.getStartCity().equals(curCity) || route.getDestinationCity().equals(curCity))) {
					routeList.add(route);
				}
			}

			// Add the set into map
			map.put(curCity, routeList);

		}
		// System.out.println(map.toString());

		ArrayList<ArrayList<Route>> continuousRouteList = new ArrayList<ArrayList<Route>>();

		// Get all the possible continuous routes by starting the route from all the
		// cities in the connected city list
		// Add them into continuous route list
		for (Map.Entry<City, ArrayList<Route>> element : map.entrySet()) {

			continuousRouteList = (getContinuousRoutes(continuousRouteList, map, new ArrayList<Route>(),
					element.getKey()));
			// System.out.println("One RUN COMPLETE");
		}
		// System.out.println(continuousRouteList.toString());

		// Calculate total length of each continuous route
		ArrayList<Integer> lengthOfAllContinuousRoute = new ArrayList<Integer>();
		int length = 0;
		for (ArrayList<Route> curContinuousRoute : continuousRouteList) {

			for (Route curRoute : curContinuousRoute) {

				length += curRoute.getLength();

			}

			lengthOfAllContinuousRoute.add(length);
			// System.out.println(length);
			length = 0;

		}

		// Compare the length of all continuous routes and determine the longest
		for (int curLength : lengthOfAllContinuousRoute) {
			length = Math.max(curLength, length);
		}
		return length;

	}

	//method that gets all the possible route based on the given start city and the available routes
	public ArrayList<ArrayList<Route>> getContinuousRoutes(ArrayList<ArrayList<Route>> continuousRouteList,
			Map<City, ArrayList<Route>> map, ArrayList<Route> continuousRoute, City startCity) {

		// check if the player owns only one route that connects the start city
		// Stop expanding current continuous route if the start city only has one route
		// that connects to the other city,
		// which has to be the last start city in this case
		//
		// otherwise, if the continuous route arraylist is empty and the start city will
		// be the first start city
		// (which is the start point) in the current continuous route, continue the
		// method
		// System.out.println(map.toString());

		// System.out.println("start:" +startCity);
		// System.out.println(map.get(startCity).toString());
		// System.out.println("list: " + continuousRouteList.toString());
		if (map.get(startCity).size() > 1 || continuousRoute.isEmpty()) {
			// System.out.println("got in");

			// go through each routes that player owns that is connected to start city
			for (Route curRoute : map.get(startCity)) {

				// check if the current route that is connected to start city, is already
				// existed in the continuous route arraylist
				// if not, extend the continuous route by adding the current route into the
				// continuous route arraylist
				if (!continuousRoute.contains(curRoute)) {

					continuousRoute.add(curRoute);
					// System.out.println("continuous route: " + continuousRoute.toString());

					// get the city that is at the other end of the current route, set it to be the
					// next city
					City nextCity = curRoute.getStartCity().equals(startCity) ? curRoute.getDestinationCity()
							: curRoute.getStartCity();

					// System.out.println("end:" + nextCity);

					// get all the possible result of current continuous route once it expands to
					// next city
					continuousRouteList = getContinuousRoutes(continuousRouteList, map, continuousRoute, nextCity);

					// System.out.println("temp list: " + continuousRouteList.toString());
				}

			}

		}
		// System.out.println("exited");

		// Add the current continuous route into the list
		if (!continuousRouteList.contains(continuousRoute)) {
			continuousRouteList.add(continuousRoute);
		}

		// End this method
		// System.out.println("final list: " + continuousRouteList.toString());
		return continuousRouteList;

	}

	// Gets other route if its a double route
	public Route getOtherRoute(Route route) {
		for (Route other : controller.getTotalRoutes())
			if (other != route && Objects.equals(other.getStartCity(), route.getStartCity()) && Objects.equals(other.getDestinationCity(), route.getDestinationCity()))
				return other;
		return null;
	}
}