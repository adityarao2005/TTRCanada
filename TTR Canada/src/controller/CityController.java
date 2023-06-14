package controller;

import java.util.ArrayList;

import model.City;
import model.Player;
import model.Route;

// BY RISANTH
// This controller is used to highlight the available cities to the user
public class CityController {

	// Get the city that is clicked
	// Get the total cities and routes in the game
	// Both of these informations will be passed in from the TTRController

	// Find the adjacent cities
	public void adjacentCities(City[] totalCities, Route[] totalRoutes, Player currentPlayer, City city) {

		// Call this method at the start to clear the board
		setCitiesIconsToOrange(totalCities);

		// Adds to the adjacent city list array
		ArrayList<City> adjacentCity = new ArrayList<City>();

		// Loop through all the routes and outer label is used to start the loop
		// again from the top
		routeLoop: for (Route route : totalRoutes) {

			// Check if the route has already been claimed by the current player
			if (route.getClaimer() != null) {

				// Skip to the next iteration of the loop
				continue;
			}

			// If this is a double route, check the other the double route and
			// check if it has been claimed by the current
			// player (nested for loop)
			if (route.isDualRail()) {

				// Enhanced for loop
				for (Route otherRoute : totalRoutes) {

					// If the other route is a dual rail and not a single route
					if (otherRoute.isDualRail() && otherRoute != route) {

						// Get the current player
						if (otherRoute.getClaimer() == currentPlayer) {

							// If it has the current player has the double route
							// skip it and start the loop again from the top
							continue routeLoop;
						}
					}
				}
			}

			// If the city is either in the starting or destination, if its in
			// the start add the destination, and if its the destination add the
			// starting city to the adjacent city list
			if (city.equals(route.getStartCity()) | city.equals(route.getDestinationCity())) {

				// If the city is the start city, it adds the destination city to the array list
				if (city.equals(route.getStartCity())) {
					adjacentCity.add(route.getDestinationCity());

				}

				// If the city is the destination city, it adds the start city to the array list
				else if (city.equals(route.getDestinationCity())) {
					adjacentCity.add(route.getStartCity());

				}

			}

			// Sets all the cities to green
			setAdjacentCitesToGreen(adjacentCity);

		}

	}

	// Go through all other cities and make sure they are hidden (set icons back
	// to orange)
	public void setCitiesIconsToOrange(City[] totalCities) {

		for (City notAjacentCities : totalCities) {

			// Hides when the cities that aren't ajacent city
			notAjacentCities.hideCity();

		}

	}

	// Set the adjacent cities to the green city icon
	public void setAdjacentCitesToGreen(ArrayList<City> adjacentCity) {

		for (City currentCity : adjacentCity) {

			// Shows the current city
			currentCity.showCity();

		}

	}

}