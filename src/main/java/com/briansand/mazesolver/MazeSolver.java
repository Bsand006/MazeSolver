package main.java.com.briansand.mazesolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class MazeSolver {

	Position[][] maze;
	Paramaters param = new Paramaters();
	int turns = 0;

	LinkedList<Position> stack = new LinkedList<>(); // Stack

	/**
	 * The paramaters object stores the x and y coordinates of the start location,
	 * and contains the dimensions of the maze for out of bounds checking.
	 */
	static class Paramaters {
		int x;
		int y;
		int rows;
		int cols;

		public void setup(int x, int y, int rows, int cols) {
			this.x = x;
			this.y = y;
			this.rows = rows;
			this.cols = cols;
		}
	}

	/**
	 * The position object stores the current coordinates that the game is on. Every
	 * time the game moves to a new space a new object of this class will be
	 * created, updated, and then pushed onto the stack to keep track of every move
	 * that has been made.
	 */
	static class Position {
		int x;
		int y;
		int rows;
		int cols;

		int state;

		public void setup(int x, int y, int rows, int cols) {
			this.x = x;
			this.y = y;
			this.rows = rows;
			this.cols = cols;
		}

		public void EAST() {
			if (x < cols) {
				x++;
			}
		}

		public void WEST() {
			if (x > 0) {
				x--;
			}
		}

		public void NORTH() {
			if (y > 0) {
				y--;
			}
		}

		public void SOUTH() {
			if (y < rows) {
				y++;
			}
		}
	}

	/**
	 * mazeLoader creates the 2D array. The maze array is a maze of Position
	 * objects. Creating the maze in this way allows me to store more information in
	 * a cell. I can store what the that cell is, and more importantly I can store
	 * information about the path that I have taken, which prevents the program for
	 * constantly going back and forth and allows me to backtrack if required.
	 * 
	 * This data is stored using the state integer in the Position object. If it is
	 * 0, then that cell is the starting cell. If it is 1, then it is an open path.
	 * 2 is a wall and 3 is the end of the maze. 4 represents the "path", which are
	 * the cells that I went to previously to get to where I am now.
	 */

	public void mazeLoader() throws FileNotFoundException {
		Scanner file = new Scanner(new File("maze.txt")); // File with the maze

		int rows = 0; // @param rows integer
		int cols = 0; // @param columns integer

		// Sets the rows and cols integer to the dimensions of the maze
		while (file.hasNext()) {
			String line = file.next();
			rows++;
			cols = line.length();
		}

		System.out.println("Making " + rows + "," + cols + " maze");
		maze = new Position[rows][cols]; // maze array

		file = new Scanner(new File("maze.txt")); // Resets the scanner

		// This loop initializes every cell in the maze and sets the proper state value
		for (int i = 0; i < rows; i++) {
			String thisRow = file.next();
			for (int j = 0; j < cols; j++) {
				maze[i][j] = new Position();
				if (thisRow.charAt(j) == '$') {
					maze[i][j].state = 0;
					param.setup(i, j, rows, cols); // Passes start value data into Paramaters
					System.out.println("Starting position " + i + "," + j);
				} else if (thisRow.charAt(j) == '.') {
					maze[i][j].state = 1;
				} else if (thisRow.charAt(j) == '#') {
					maze[i][j].state = 2;
				} else if (thisRow.charAt(j) == '@') {
					maze[i][j].state = 3;
					System.out.println("Ending position " + i + "," + j);
				}
			}
		}

		// Launch solveMaze
		solveMaze(param.x, param.y);
	}

	/**
	 * solveMaze is the actual recursion solve method. It works by creating a new
	 * Position object. The program then will check all directions until it finds a
	 * valid move. I then sets the x and y coords of that point into the x and y
	 * integers in the method. The Position object is then pushed onto the stack and
	 * calls itself, taking in the x and y integers as parameters. This is done
	 * because a new blank Position object is created each time it runs, so the
	 * current x and y coords need to be tracked seperately.
	 */
	public boolean solveMaze(int x, int y) {
		Position pos = new Position(); // Creates new Postion object
	
		pos.setup(x, y, param.rows, param.cols); // Sets values of pos

		// EAST
		if (x < pos.cols - 1 && maze[y][x + 1].state == 1) {
			pos.EAST();
			x = pos.x;
			y = pos.y;
			maze[y][x].state = 4;
			System.out.println("MOVING EAST... COORDS ARE : " + x + " + " + y);
			turns++;
			stack.push(pos);
			solveMaze(x, y);

			// WEST
		} else if (x > 0 && maze[y][x - 1].state == 1) {
			pos.WEST();
			x = pos.x;
			y = pos.y;
			maze[y][x].state = 4;
			System.out.println("MOVING WEST... COORDS ARE : " + x + " + " + y);
			turns++;
			stack.push(pos);
			solveMaze(x, y);

			// SOUTH
		} else if (y < pos.rows - 1 && maze[y + 1][x].state == 1) {
			pos.SOUTH();
			x = pos.x;
			y = pos.y;
			maze[y][x].state = 4;
			System.out.println("MOVING SOUTH... COORDS ARE : " + x + " + " + y);
			turns++;
			stack.push(pos);
			solveMaze(x, y);

			// NORTH
		} else if (y > 0 && maze[y - 1][x].state == 1) {
			pos.NORTH();
			x = pos.x;
			y = pos.y;
			maze[y][x].state = 4;
			System.out.println("MOVING NORTH... COORDS ARE : " + x + " + " + y);
			turns++;
			stack.push(pos);
			solveMaze(x, y);

			// Win condition check
		} else if (x < pos.cols - 1 && x > 0 && y < pos.rows - 1 && y > 0) {
			if (maze[y][x - 1].state == 3 || maze[y][x + 1].state == 3 || maze[y - 1][x].state == 3
					|| maze[y + 1][x].state == 3) {
				System.out.println("MAZE SOLVED!");
				System.out.println("SOLVED IN " + turns + " TURNS");
				return true;
			}
		} else {
			System.out.println("DEAD END! BACKTRACKING...");
		}

		return false;
	}

	public static void main(String[] args) {
		try {
			new MazeSolver().mazeLoader();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
