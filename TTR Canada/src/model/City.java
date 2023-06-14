package model;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Point;
import java.util.Objects;

// BY JOE
// Represents a city on the map
public class City extends JLabel {
	private static final long serialVersionUID = 1L;

	// Fields
	private String name;
	private Point point;

	// Constructor
	public City(String name, Point point) {
		this.name = name;
		setPoint(point);
		setSize(15, 15);
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
		setLocation(point);
	}

	// ToString method
	@Override
	public String toString() {
		return "City [name=" + name + "]";
	}

	// Hash and equals method
	@Override
	public int hashCode() {
		return Objects.hash(name, point);
	}
	
	// This will show the city icon
	public void showCity() {
		setIcon(new ImageIcon("images/city.png"));
		
	}
	
	// This will hide the city icons
	public void hideCity() {
		setIcon(null);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		return Objects.equals(name, other.name) && Objects.equals(point, other.point);
	}

}