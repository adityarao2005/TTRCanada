package view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

// By Vithursh
// The game frame
public class GameFrame extends JFrame {

	// Fields
	private BoardPanel boardPanel = new BoardPanel();
	private ScorePanel scorePanel = new ScorePanel();
	private CardPanel cardPanel = new CardPanel();
	private PlayerPanel playerPanel = new PlayerPanel();
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem newItem = new JMenuItem("New");
	private JMenuItem loadItem = new JMenuItem("Load");
	private JMenuItem saveItem = new JMenuItem("Save");
	private JMenuItem exitItem = new JMenuItem("Exit");
	private JMenuItem helpItem = new JMenuItem("Help");

	// Constructors
	public GameFrame() {
		// Sets the title of the GUI
		setTitle("Ticket To Ride Canada");
		setSize(1920, 1080);
		// Sets the size of the JFrame window to 1920 pixels wide and 1080 pixels tall.

		fileMenu.add(newItem);
		// Adds the 'newItem' to the 'fileMenu' (presumably a menu option for creating a new file).

		fileMenu.add(loadItem);
		// Adds the 'loadItem' to the 'fileMenu' (presumably a menu option for loading a file).

		fileMenu.add(saveItem);
		// Adds the 'saveItem' to the 'fileMenu' (presumably a menu option for saving a file).

		saveItem.setEnabled(false);
		// Disables the 'saveItem', making it unclickable and grayed out.

		fileMenu.add(exitItem);
		// Adds the 'exitItem' to the 'fileMenu' (presumably a menu option for exiting the program).

		JMenuBar menuBar = new JMenuBar();
		// Creates a new JMenuBar instance to hold the menu items.

		menuBar.add(fileMenu);
		// Adds the 'fileMenu' to the 'menuBar' (presumably as a dropdown menu).

		menuBar.add(helpItem);
		// Adds the 'helpItem' to the 'menuBar' (presumably as a standalone menu item).

		setJMenuBar(menuBar);
		// Sets the menu bar of the JFrame to the 'menuBar' created earlier.

		setLayout(null);
		// Sets the layout manager of the JFrame to null, indicating custom positioning of components.

		cardPanel.setLocation(0, 900);
		// Sets the location (coordinates) of the 'cardPanel' component to (0, 900) on the JFrame.

		cardPanel.setSize(1400, 117);
		// Sets the size of the 'cardPanel' component to be 1400 pixels wide and 117 pixels tall.

		playerPanel.setLocation(1400, 400);
		// Sets the location (coordinates) of the 'playerPanel' component to (1400, 400) on the JFrame.

		scorePanel.setLocation(1400, 0);
		// Sets the location (coordinates) of the 'scorePanel' component to (1400, 0) on the JFrame.

		scorePanel.setSize(504, 400);
		// Sets the size of the 'scorePanel' component to be 504 pixels wide and 400 pixels tall.

		boardPanel.setSize(1400, 900);
		// Sets the size of the 'boardPanel' component to be 1400 pixels wide and 900 pixels tall.

		add(cardPanel);
		// Adds the 'cardPanel' component to the JFrame.

		add(playerPanel);
		// Adds the 'playerPanel' component to the JFrame.

		add(scorePanel);
		// Adds the 'scorePanel' component to the JFrame.

		add(boardPanel);
		// Adds the 'boardPanel' component to the JFrame.

		setSize(1920, 1080);
		// Sets the size of the JFrame window to 1920 pixels wide and 1080 pixels tall.


		setLocationRelativeTo(null);
		// Centers the JFrame window on the screen.

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Sets the default close operation of the JFrame to exit the program when the frame is closed.
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public ScorePanel getScorePanel() {
		return scorePanel;
	}

	public CardPanel getCardPanel() {
		return cardPanel;
	}

	public PlayerPanel getPlayerPanel() {
		return playerPanel;
	}

	public JMenu getFileMenu() {
		return fileMenu;
	}

	public void setFileMenu(JMenu fileMenu) {
		this.fileMenu = fileMenu;
	}

	public JMenuItem getNewItem() {
		return newItem;
	}

	public void setNewItem(JMenuItem newItem) {
		this.newItem = newItem;
	}

	public JMenuItem getLoadItem() {
		return loadItem;
	}

	public void setLoadItem(JMenuItem loadItem) {
		this.loadItem = loadItem;
	}

	public JMenuItem getSaveItem() {
		return saveItem;
	}

	public void setSaveItem(JMenuItem saveItem) {
		this.saveItem = saveItem;
	}

	public JMenuItem getExitItem() {
		return exitItem;
	}

	public void setExitItem(JMenuItem exitItem) {
		this.exitItem = exitItem;
	}

	public JMenuItem getHelpItem() {
		return helpItem;
	}

}