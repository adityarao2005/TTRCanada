package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

// BY RISANTH & VITHURSH
public class GameRulesFrame extends JFrame implements ActionListener {

	JLabel rulesTitle = new JLabel("RULES");
	JTextArea textArea = new JTextArea(20, 60);
	JButton rightArrow = new JButton(new ImageIcon("Images\\right-arrow.png"));
	JButton leftArrow = new JButton(new ImageIcon("Images\\left-arrow.png"));

	JButton pauseButton = new JButton(new ImageIcon("Images\\pause.png"));
	JButton playButton = new JButton(new ImageIcon("Images\\play-buttton.png"));

	private Timer timer;
	private int delay = 2000; // 2000 milliseconds = 2 seconds
    
    String[] text; // Declare the 'text' variable

    int currentSlide = 0;

    public GameRulesFrame(){

        //Creating a GUI
        setSize(600, 600);    // Set the size of the window
        setTitle("Help");    // Set the title of the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   // Set the close operation for the window
        setLocationRelativeTo(null);   // Set the location of the window to the center of the screen
        getContentPane().setBackground(new Color(0, 90, 96).brighter().brighter());   // Set the background color of the window
        setResizable(false); // Sets the window to be not resizable
        setVisible(true);   // Set the window to be visible
        setLayout(null);   // Set the layout of the components in the window to null (i.e. custom positioning)

        //Sets rulesTitles font, bold and size
        rulesTitle.setFont(new Font("Arial", Font.BOLD, 48));   // Set the font, bold, and size for the title
        int labelWidth = rulesTitle.getPreferredSize().width;   // Get the preferred width of the title
        int frameWidth = getWidth();   // Get the width of the frame
        rulesTitle.setBounds((frameWidth/2)-(labelWidth/2), 50, labelWidth, 50);   // Set the position and size of the title label
        
        String filePath = "files/Rules.txt"; // Replace with the actual path to your file
		text = splitTextFromFile(filePath); // Assign the text from the file to the 'text' variable
		// ...

        textArea.setText(text[currentSlide]); // Set the initial text to the first slide's text
        
        textArea.setFont(new Font("Ink Free", Font.BOLD, 18));   // Set the font, bold, and size for the text area
        textArea.setText(text[currentSlide]);   // Set the text of the text area to the current slide's text
        textArea.setEditable(false);   // Set the text area to not be editable
        int x = (getWidth() - 400) / 2;   // Calculate the x position of the scroll pane
        textArea.setLineWrap(true); // Allows for line wrap
        textArea.setWrapStyleWord(true); // Allows for word wrap
        textArea.setBounds(x, 200, 400, 250); // Set the position and size of the scroll pane

        rightArrow.setBounds(475, 200, 150, 150);   // Set the position and size of the right arrow button
        rightArrow.setOpaque(false);   // Make the background of the right arrow button transparent
        rightArrow.setContentAreaFilled(false);   // Remove the fill of the right arrow button
		rightArrow.setBorderPainted(false);   // Remove the border of the right arrow button
        
        leftArrow.setBounds(-40, 200, 150, 150);   // Set the position and size of the left arrow button
        leftArrow.setOpaque(false);   // Make the background of the left arrow button transparent
        leftArrow.setContentAreaFilled(false);   // Remove the fill of the left arrow button
		leftArrow.setBorderPainted(false);   // Remove the border of the left arrow button
        
        rightArrow.addActionListener(this);   // Add an action listener to the right arrow button
        leftArrow.addActionListener(this);   // Add an action listener to the left arrow button
        
        timer = new Timer(delay, this);   // Create a timer with a delay and add an action listener to it
        timer.start();   // Start the timer
        
        pauseButton.setOpaque(false);   // Make the background of the pause button transparent
        pauseButton.setContentAreaFilled(false);   // Remove the fill of the pause button
        pauseButton.setBorderPainted(false);   // Remove the border of the
		
        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        
        pauseButton.setBounds(300, 470, 70, 70); // set the position and size of the pause button
        pauseButton.addActionListener(this); // add an action listener to the pause button

        playButton.setBounds(220, 470, 70, 70); // set the position and size of the play button
        playButton.addActionListener(this); // add an action listener to the play button

        //Add rulesTitle to GUI
        add(rulesTitle); // add the rulesTitle label to the GUI
        //add(trainGif); // (commented out) add a trainGif to the GUI
        add(rightArrow); // add the rightArrow button to the GUI
        add(leftArrow); // add the leftArrow button to the GUI
        add(pauseButton); // add the pauseButton to the GUI
        add(playButton); // add the playButton to the GUI
        add(textArea); // add the scrollPane to the GUI

        repaint(); // call the repaint method to refresh the GUI
    }
    
    public static String[] splitTextFromFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\r\n"); // Append each line of the file to the StringBuilder, along with a line break
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if an exception occurs during file reading
        }
        return sb.toString().split(","); // Split the contents of the StringBuilder using commas and return as a String array
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the right arrow or the timer triggered this action
        if (e.getSource() == rightArrow || e.getSource() == timer) {
            // Increment the current slide
            currentSlide++;
            // If we've reached the end of the slides, loop back to the beginning
            if (currentSlide >= text.length) {
                currentSlide = 0;
            }
        }
        // Check if the left arrow triggered this action
        else if (e.getSource() == leftArrow) {
            // Decrement the current slide
            currentSlide--;
            // If we've reached the beginning of the slides, loop back to the end
            if (currentSlide < 0) {
                currentSlide = text.length - 1;
            }
        }
        // Update the text area with the current slide's text
        textArea.setText(text[currentSlide]);
        
        // Check if the pause button triggered this action
        if(e.getSource() == pauseButton) {
        	// Stop the timer to pause the slide show
        	timer.stop();
        }
        // Check if the play button triggered this action
        else if(e.getSource() == playButton) {
        	// Start the timer to resume the slide show
        	timer.start();
        }
    }
}