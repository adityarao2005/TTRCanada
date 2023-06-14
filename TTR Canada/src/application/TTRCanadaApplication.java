package application;

import controller.TTRController;

/*
Name(s):
Aditya Rao (25%)
 - CardColour enum (100%)
 - PlayerColour enum (100%)
 - PlayerPanel class (100%)
 - TTRController class (80%)
 	- Most of the main functionality
 - TicketController class (100%)
 - AIController class (100%)
 
Joe Lin (25%)
 - City class (100%)
 - Player class (100%)
 - CardPanel class (100%)
 - RouteController class (100%)
 - TTRController class (10%)
 	- Determining the winner and Game Over logic
 - Unit Tests and Debugging (100%)

Vithursh Thananchayan (25%)
 - Route class (100%)
 - GameFrame class (100%)
 - ScorePanel class (100%)
 - FileImportController class (100%)
 - GameRulesFrame class (60%)
 - TTRController class (5%)
 	- Loading and Saving User Data
 
Risanth Sivarajah (25%)
 - Ticket class (100%)
 - TrainCard class (100%)
 - BoardPanel class (100%)
 - TrainCardController class (100%)
 - CityController class (100%)
 - GameRulesFrame class (40%)
 	- Developed the first version of the GameRulesFrame
 - TTRController class (5%)
 	- Disabling and Enabling GUI methods
 
 
Date: May 8, 2023

Course Code: ICS4U1-03 Taught by Mr.Fernandes

Title: "Ticket To Ride: Canada"

Description:
The Game is a remake of the classic Ticket To Ride board games except its on computer and the theme for the game is Canada.
The regular TTR rules apply except with a few new additions: a route of length 7: worth 18 points, the ability of being able to pass,
and an AI being able to play against you to challenge the user. Unlike the regular TTR game where when you are interrupted you cannot
go back. Here you are able to save the progress and load it once you get back to the game. The GUI automatically assists the users
through the gameplay and the rules.

Features:
 - Save and Load features
 - Longest route features
 - Highlighting adjacent cities
 - AIs playing against the user(s), at most 3
 - Instructions window to guide the user in playing the game

Major Skills:
 - Algorithms
 - Recursion
 - Object Oriented Programming
 - MVC Architecture
 - Swing and GUI Development
 - Game Logic
 - Graph Theory
     - MST
     - BFS
     - DFS
     	- Longest route algorithm
     - Pruning algorithm
 - Project and Time Management
 - File IO and Serialization

Areas of Concern:
 - If playing with 3 AIs, expect that the AIs will never be able to complete any of their tickets and expect that
   the board will be dominated by the AIs
*/
public class TTRCanadaApplication {

	public static void main(String[] args) {
		new TTRController();
	}
}
