package com.briansand.mazesolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class MazeSolver {

	char[][] maze; // 2D maze array
	Paramaters param = new Paramaters();

	LinkedList<Position> stack = new LinkedList<>(); // Stack

	/*
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

	/*
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
		boolean movedEast;
		boolean movedWest;
		boolean movedSouth;
		boolean movedNorth;

		public void setup(int x, int y, int rows, int cols) {
			this.x = x;
			this.y = y;
			this.rows = rows;
			this.cols = cols;
		}

		public void EAST() {
			if (x < cols) {
				x++;
				movedEast = true;
			}
		}

		public void WEST() {
			if (x > 0) {
				x--;
				movedWest = true;
			}
		}

		public void NORTH() {
			if (y > 0) {
				y--;
				movedNorth = true;
			}
		}

		public void SOUTH() {
			if (y < rows) {
				y++;
				movedSouth = true;
			}
		}
	}

	public void mazeLoader() throws FileNotFoundException {

		Scanner file = new Scanner(new File("maze.txt"));

		int rows = 0;
		int cols = 0;

		// Finds the dimensions of the maze
		while (file.hasNext()) {
			String line = file.next();
			rows++;
			cols = line.length();
		}

		maze = new char[rows][cols];

		file = new Scanner(new File("maze.txt"));

		// Reads characters into 2D array
		for (int r = 0; r < rows; r++) {
			String thisRow = file.next();
			for (int c = 0; c < cols; c++) {
				maze[r][c] = thisRow.charAt(c);
			}
		}

		// Finds the start coordinates and loads it into paramaters object

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (maze[i][j] == '$') {
					param.setup(i, j, rows, cols);
				}
			}
		}

		solveMaze(param.x, param.y);
	}

	public boolean solveMaze(int x, int y) {

		Position pos = new Position();
		pos.setup(x, y, param.rows, param.cols);

		if (maze[y][x] == '@') {
			System.out.println("MAZE SOLVED!");
			return true;
		}

		if (y < pos.cols - 1 && x < pos.rows - 1) {

			pos.EAST();
			x = pos.x;
			y = pos.y;

			if (maze[y][x] == '.') {
				System.out.println("MOVING EAST, NEW COORDS ARE : " + x + " + " + y);
				stack.push(pos);
				solveMaze(x, y);

			} else if (maze[y][x] == '#') {
				System.out.println("INVALID SQUARE, CHECKING SOUTH");
				pos.SOUTH();
				x = pos.x;
				y = pos.y;

				if (maze[y][x] == '.') {
					System.out.println("MOVING SOUTH, NEW COORDS ARE : " + x + " + " + y);
					solveMaze(x, y);

				} else if (maze[y][x] == '#') {
					System.out.println("INVALID SQUARE, CHECKING NORTH");
					pos.NORTH();
					x = pos.x;
					y = pos.y;

					if (maze[y][x] == '.') {
						System.out.println("MOVING NORTH, NEW COORDS ARE : " + x + " + " + y);
						solveMaze(x, y);

					} else if (maze[y][x] == '#') {
						System.out.println("DEAD END! BACKTRACKING...");
					}
				}

			}

		} else {
			System.out.println("OUT OF BOUNDS!");
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
