package view;

import java.awt.Color;
import java.io.FileReader;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import model.Route;
import model.City;

// RISANTH
// Start of the BoardPanel class which extends the JPanel
public class BoardPanel extends JPanel {

	// Fields
	private JLabel background;
	private Route[] routes;
	private City[] cities;

	// Constructor Method
	public BoardPanel() {
		super();
		// UI setup
		setLayout(null);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.BLACK));
		this.background = new JLabel(new ImageIcon("images/board.png")); // Sets the images as a JLabel
		background.setBounds(0, 0, 1400, 900);
		add(background); // Adds the background onto the panel

	}

	// Getters and Setters
	public Route[] getRoutes() {
		return routes;
	}

	public void setRoutes(Route[] routes) {
		this.routes = routes;

		// Enhanced for loop to add the route
		for (Route route : routes) {
			add(route);
		}
	}

	public City[] getCities() {
		return cities;
	}

	public void setCities(City[] cities) {
		this.cities = cities;
		
		// Enhanced for loop to add the city
		for (City city : cities) {
			add(city);
		}
	}

	// ToString method
	@Override
	public String toString() {
		return "BoardPanel [background=" + background + ", routes=" + Arrays.toString(routes) + ", cities="
				+ Arrays.toString(cities) + "]";
	}
	
	// Refreshes the panel
	public void refresh() {
		// removes all the GUI
		this.removeAll();
		
		// Adds all the routes
		for (Route route : routes) {
			this.add(route);
		}
		
		// Adds all the cities
		for (City city : cities) {
			this.add(city);
		}
		
		// Adds the background
		add(background);
	}

}
