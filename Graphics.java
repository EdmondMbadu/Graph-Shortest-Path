import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import processing.core.PApplet;
import processing.core.PVector;

public class Graphics extends PApplet {
	// Declare global variables

	ArrayList<PVector> circles; // the array will be used to save the coordinate of the circles drawn
	int count; // the variable that will hold the id/name of circles/vertex. It will be incremented
				// after every iteration

	// the message dialog instance
	JPanel jtp;
	// the variable that will keep track of the x coordinate of the mouse
	// for point one
	float Xmouse1, Ymouse1,
			// for the second point
			Xmouse2, Ymouse2;
	// The PVector that holds x,y, and name of the vertex
	PVector a1, a2;
	// the radius of circles
	int radius;
	// the array that contains the vertices 
	ArrayList<Vertex> vert;
	Vertex verticesA, verticesB;
	// the class that does all the computation to find the shortest path
	DijkstraShortestPath shortestPath;

	// the main method
	public static void main(String[] args) {
		PApplet.main("Graphics");
//		
	}

	/**
	 * 
	 * @param element the name of the circle
	 * @param vert the array list that contains all the other circles
	 * @return the circle (vertex) whose name is element
	 */
	public static Vertex find(String element, ArrayList<Vertex> vert) {
		for (int i = 0; i < vert.size(); i++) {
			if (vert.get(i).toString().equalsIgnoreCase(element))
				return vert.get(i);
		}
		// return null if it is not found
		return null;
	}

/**
 * set the size of the screen 
 */
	public void settings() {
		// set the size of the screen to match the size of the screen of the computer
		size(displayWidth, displayHeight);

	}

	/**
	 * Initialize all the global variables 
	 */
	public void setup() {

		count = 0;
		radius = 100;
		jtp = new JPanel();
		jtp.setSize(new Dimension(600, 600));
		jtp.setPreferredSize(new Dimension(600, jtp.getPreferredSize().height));
		vert = new ArrayList<>();
		shortestPath = new DijkstraShortestPath();
		circles = new ArrayList<PVector>();
		background(0);

	}

	// the method that draws the graphics
	public void draw() {

	}

/**
 * if the key a,b,c,d,e is pressed
 *do something
 */
	public void keyPressed() {
		String el = str(count);
		// if the key A is pressed, make a circle
		// at the position where the mouse is
		if (key == 'a' || key == 'A') {
			// record the x, y coordinate of the mouse
			float Xmouse = pmouseX;
			float Ymouse = pmouseY;
			float radius = 100;
			// make the circle white
			fill(255);
			// draw the circle
			ellipse(Xmouse, Ymouse, radius, radius);
			// draw a text in the center of the circle
			// text size is 50 pixels
			textSize(50);
			// make the text black
			fill(0);
			// error correction to center the text in the middle of the circle
			float error = 20;
			// draw a text in the center of the circle
			text(count, Xmouse - error, Ymouse + error);
			// record the coordinates of the center, and its name
			PVector v = new PVector(Xmouse, Ymouse, count);
			// save it in the circles array list
			circles.add(v);
			// create a vertex from it
			Vertex v1 = new Vertex(Integer.toString(count));
			vert.add(v1);
			count++;

		}
		// if the key b is pressed, save the coordinate
		// of the first point
		// so that you draw a line afterwards
		else if (key == 'b' || key == 'B') {
			Xmouse1 = pmouseX;
			Ymouse1 = pmouseY;
			verticesA = search(Xmouse1, Ymouse1);
			// find the coordinate of this vertex 
			a1 = searching(verticesA.getName());

		}
		// if the key c is pressed, use  the coordinate saved from 
		// the key b and make a line with the one recorded from the c
		else if (key == 'c' || key == 'C') {
			Xmouse2 = pmouseX;
			Ymouse2 = pmouseY;
			// float radius=100;
			stroke(255);
			strokeWeight(4);
			verticesB = search(Xmouse2, Ymouse2);
			verticesA.addNeighbour(new Edge(1, verticesA, verticesB));
			a2 = searching(verticesB.getName());
			line(a1.x, a1.y, a2.x, a2.y);
			// PVector v= new PVector(Xmouse,Ymouse,100);
			// circles.add(v);
		} else if (key == 'd' || key == 'D') {
			Xmouse1 = pmouseX;
			Ymouse1 = pmouseY;
			verticesA = search(Xmouse1, Ymouse1);
			shortestPath.computeShortestPaths(verticesA);
		} else if (key == 'E' || key == 'e') {
			Xmouse2 = pmouseX;
			Ymouse2 = pmouseY;
			verticesB = search(Xmouse2, Ymouse2);
			ArrayList<Vertex> path = (ArrayList<Vertex>) shortestPath.getShortestPathTo(verticesB);
			jtp.setBackground(new Color(102, 205, 170));

			for (int i = 0; i < path.size() - 1; i++) {
				// find the the coordinate of the center of the circle
				// whose name is given
				PVector p1 = searching(path.get(i).getName());
				PVector p2 = searching(path.get(i + 1).getName());
				stroke(0, 255, 100);
				strokeWeight(8);
				line(p1.x, p1.y, p2.x, p2.y);

				//
				stroke(255);
				strokeWeight(4);
			}
			// customize the dialog box
			UIManager.put("OptionPane.minimumSize", new Dimension(600, 300));
			UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 40));
			UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 25));
			UIManager.put("OptionPane.background", Color.white);
			UIManager.put("TextArea.selectionForeground", Color.WHITE);
			// display the message
			JOptionPane.showMessageDialog(jtp,
					shortestPath.getShortestPathTo(verticesB) + " the  distance is " + (int) verticesB.getDistance(),
					"Shortest Path from " + verticesA.toString() + " to " + verticesB.toString(),
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	/**
	 * this method searches for the vector given the x and y coordinate
	 * @param x the x coordinate of where the mouse is clicked
	 * @param y the y coordinate of where the mouse is clicked
	 * @return the vertex that corresponds to the position
	 */
	public Vertex search(double x, double y) {

		for (int i = 0; i < circles.size(); i++) {
			double X = circles.get(i).x;
			double Y = circles.get(i).y;
			double distance = Math.sqrt(Math.pow((X - x), 2) + Math.pow((Y - y), 2));
			// this an integer so there will be no problem
			if (distance <= radius) {
				int counting = (int) circles.get(i).z;
				String element = Integer.toString(counting);
				// return the particular vertex. with the corresponding number
				return find(element, vert);
			}
		}
		return null;

	}

	/**
	 * This method search in the array list circles for the vertex whose name is element
	 * .if found, return the PVector which contains the coordinate of the vertex, and its name
	 
	 * @param element1 the name of the vertex
	 * @return a 3d vector that contains, x,y coordinate of the vertex and its name
	 * (count) whose name is element
	 * the name of the vertex is recorded in the `
	 */
	public PVector searching(String element1) {
		for (int i = 0; i < circles.size(); i++) {
			int el = (int) (circles.get(i).z);
			String name = Integer.toString(el);
			if (element1.equalsIgnoreCase(name)) {
				return circles.get(i);
			}

		}
		return null;
	}

}
