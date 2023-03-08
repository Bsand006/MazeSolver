package com.briansandMazeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class MazeSolver {

	char[][] maze; // 2D maze array
	Paramaters param = new Paramaters();
	Position pos = new Position();

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

		public void setup(int x, int y, int rows, int cols) {
			this.x = x;
			this.y = y;
			this.rows = rows;
			this.cols = cols;
		}

		public void EAST() {
			if (x < cols) {
				x++;
			} else
				System.out.println("EAST OUT OF BOUNDS");
		}

		public void WEST() {
			if (x > 0)
				x--;
			else
				System.out.println("WEST OUT OF BOUNDS");
		}

		public void NORTH() {
			if (y > 0)
				y--;
			else
				System.out.println("NORTH OUT OF BOUNDS");
		}

		public void SOUTH() {
			if (y < rows)
				y++;
			else
				System.out.println("SOUTH OUT OF BOUNDS");
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

		pos.setup(param.x, param.y, param.rows, param.cols);
		solveMaze();
	}

	public void solveMaze() {

		pos.EAST();
		System.out.println(pos.x + " " + pos.y);

		if (pos.y < pos.cols - 1 || pos.x < pos.rows - 1) {

			if (maze[pos.y][pos.x] == '.') {
				System.out.println("MOVING EAST, NEW COORDS ARE : " + pos.x + " + " + pos.y);
				solveMaze();
			} else if (maze[pos.x][pos.y] == '#') {
				System.out.println("INVALID SQUARE");

			}
		} else if (pos.y == pos.cols || pos.x == pos.rows) {
			System.out.println("OUT OF BOUNDS!");
			
		}
	}

	public static void main(String[] args) {

		try {
			new MazeSolver().mazeLoader();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
