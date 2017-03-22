import processing.core.*;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sunset extends PApplet {

	// declare a randomizer

	Random random;

	// declare file and readers/writer

	// File high_score;
	// FileReader file_reader;
	// BufferedReader reader;
	// FileWriter writer;

	// declare character sprites: an array of meteors, and a main character (pterodactyl)

	Meteor[] meteors;
	Pterodactyl pterodactyl;

	// declare accumulator variables to measure game start, character movement and life, level, meteor ID, and difficulty scale
	
	boolean start;
	boolean movement;
	int life;
	int moves;
	int time_of_day;
	boolean level_changed;
	int levels_completed;
	int meteors_multiplier;
	boolean multiplier_start;
	int game_score;
	int level_score;

	// set window size

	public void settings() {
		size(360, 640);
	}

	// load and set images in setup
	
	public void setup() {

		// initialize randomizer

		 random = new Random();

		// initialize file and readers/writer

		// try {
		// 	high_score = new File("high_score.txt");
		// 	file_reader = new FileReader(high_score);
		// 	reader = new BufferedReader(file_reader);
	 // 		writer = new FileWriter(high_score);
		// }

		// catch (FileNotFoundException ex1) {
		// 	System.out.println("Could not find high score file.");
		// }

		// catch (IOException ex2) {
		// }

		// initialize characters

		meteors = new Meteor[3000];
		pterodactyl = new Pterodactyl();

		// initialize accumulator variables

		start = false;
		movement = false;
		life = 1;
		moves = 0;
		time_of_day = 0;
		level_changed = false;
		levels_completed = 0;
		meteors_multiplier = 1;
		multiplier_start = true;
		game_score = 0;
		level_score = 0;

		// load and set character sprites, formatting

		imageMode(CENTER);

		PImage img_pterodactyl_1 = loadImage("pterodactyl_1.png");
		PImage img_pterodactyl_2 = loadImage("pterodactyl_2.png");
		PImage img_splat = loadImage("splat.png");
		PImage img_meteor_1 = loadImage("meteor_1.png");
		PImage img_meteor_2 = loadImage("meteor_2.png");

		// define a typeface, formatting

		textAlign(CENTER);

		PFont type = createFont("gotham.ttf", 25);
		textFont(type);

		// set the static image for all meteor and pterodactyl instances

		Meteor.set_image(img_meteor_1, img_meteor_2);
		Pterodactyl.set_image(img_pterodactyl_1, img_pterodactyl_2);
		Pterodactyl.set_splat(img_splat);

		// populate array of meteors with variable spacing b/w non-null objects

		populate(meteors_multiplier);
	
		// ensure each next meteor has a unique x-coordinate

		validate();

		// shift the y-coordinates of each meteor so no two meteors share the same y at any given point

		shift();

		// display starting background
		
		background(0, 0, 0);
	}

	// "main" program

	public void draw() {

		// display variable environment

		display_background(time_of_day);

		// enter a conditional to handle new level screens

		if (level_changed) {
			fill(255, 255, 255);
			textSize(48);

			switch (time_of_day) {
				case 0: fill(115, 170, 191); text("MORNING", 180, 333); break;
				case 1: fill(61, 100, 113); text("MIDDAY", 180, 333); break;
				case 2: fill(22, 53, 62); text("EVENING", 180, 333); break;
				case 3: fill(82, 34, 52); text("SUNSET", 180, 333); break;
				case 4: fill(255, 255, 255); text("NIGHT", 180, 333); break;
				case 5: fill(29, 11, 15); text("DAWN", 180, 333); break;
				case 6: fill(248, 215, 158); text("TWILIGHT", 180, 333); break;
			}

			fill(255, 255, 255);
			level_changed = false;
		}

		// enter a for-loop to display meteor(s)

		for (int i = 0; i < meteors.length; i++) {
			if (meteors[i] != null && meteors[i].get_y() < 640 && meteors[i].get_y() > 0) {
				image(meteors[i].get_image(moves), meteors[i].get_x(), meteors[i].get_y());
			}
		}

		// conditional to handle game-end

		if (life == 0) {
			noLoop();
			game_over();
		}

		// display main character sprite (alive / dead)

		image(pterodactyl.get_image(life, moves), pterodactyl.get_x(), pterodactyl.get_y());

		// display opening text, begin game when user moves

		if (!start) {
			fill(255, 255, 255);

			textSize(36);
			text("USE THE", 175, 205);
			textSize(48);
			text("LEFT + RIGHT", 180, 333);
			textSize(36);
			text("ARROW KEYS", 175, 461);

			if (movement) {
				start = true;
			}
		}

		// enter game

		if (start) {

			// hide user cursor

			noCursor();

			// allow meteors to move after player moves

			if (movement) {

				// enter a for-loop (conditional on player movement) to move meteor(s), conditionals to handle homing and collision

				for (int i = 0; i < meteors.length; i++) {
   					if (meteors[i] != null && meteors[i].get_y() >= 256 && meteors[i].get_y() < 448) {
   						meteors[i].home(pterodactyl.get_x());
   					}

   					else if (meteors[i] != null && meteors[i].get_x() == pterodactyl.get_x()) {
   						meteors[i].move();
   					}

   					else if (meteors[i] != null) {
   						meteors[i].move();
   					}

   					if (meteors[i] != null && meteors[i].check_collision(pterodactyl.get_x(), pterodactyl.get_y())) {
							life--;
    				}
   				}

				// conditional to update time of day and difficulty / level (increasing multiplier and re-generating an array)

				if (moves % 100 == 0 && moves > 0) {
					while (on_screen()) {
						for (int i = 0; i < meteors.length; i++) {
							meteors[i].move();
						}
					}

					if (meteors_multiplier < 7) {
						meteors_multiplier++;
						multiplier_start = true;

						clear();
						populate(meteors_multiplier);
						validate();
						shift();
					} 

					// conditinal to handle end-game

					if (time_of_day == 7) {
						time_of_day = 0;
					}

					else {
						time_of_day++;
						game_score += level_score;
						levels_completed++;
						level_changed = true;
					}
				}
				
				// resume draw loop, update accumulators

				loop();
				moves++;

				// enter a for-loop to determine score (total number of planes evaded)

				int passed = 0;

				for (int i = 0; i < meteors.length; i++) {
					if (meteors[i] != null && meteors[i].get_y() > 640) {
						passed++;
					}
				}

				level_score = passed;
			}

			else {

				// pause draw loop to wait for next turn

				noLoop();
			}

			// re-set movement to pause game until user movement

			movement = false;
		}
	}

	// method to draw backgrounds

	public void display_background(int time_of_day) {
		noStroke();

		if (time_of_day == 0) {
			for (int i = 0; i < 5; i++) {
				switch (i) {
					case 0: fill(98, 172, 192); break;
					case 1: fill(124, 218, 243); break;
					case 2: fill(187, 255, 237); break;
					case 3: fill(254, 202, 186); break;
					case 4: fill(253, 254, 221); break;
				}

				rect(0, i * 128, 360, 128);
			}
		}

		if (time_of_day == 1) {
			for (int i = 0; i < 5; i++) {
				switch (i) {
					case 0: fill(47, 101, 115); break;
					case 1: fill(98, 172, 192); break;
					case 2: fill(187, 255, 237); break;
					case 3: fill(228, 165, 146); break;
					case 4: fill(255, 223, 214); break;
				}

				rect(0, i * 128, 360, 128);
			}
		}

		if (time_of_day == 2) {
			for (int i = 0; i < 5; i++) {
				switch (i) {
					case 0: fill(8, 54, 64); break;
					case 1: fill(27, 110, 140); break;
					case 2: fill(195, 228, 116); break;
					case 3: fill(178, 128, 113); break;
					case 4: fill(190, 177, 149); break;
				}

				rect(0, i * 128, 360, 128);
			}
		}

		if (time_of_day == 3) {
			for (int i = 0; i < 5; i++) {
				switch (i) {
					case 0: fill(89, 30, 52); break;
					case 1: fill(217, 53, 89); break;
					case 2: fill(223, 110, 91); break;
					case 3: fill(114, 106, 89); break;
					case 4: fill(64, 144, 131); break;
				}

				rect(0, i * 128, 360, 128);
			}
		}

		if (time_of_day == 4) {
			for (int i = 0; i < 5; i++) {
				switch (i) {
					case 0: fill(1, 2, 6); break;
					case 1: fill(4, 11, 20); break;
					case 2: fill(3, 15, 29); break;
					case 3: fill(5, 18, 35); break;
					case 4: fill(7, 22, 42); break;
				}

				rect(0, i * 128, 360, 128);
			}
		}

		if (time_of_day == 5) {
			for (int i = 0; i < 5; i++) {
				switch (i) {
					case 0: fill(31, 10, 15); break;
					case 1: fill(72, 14, 28); break;
					case 2: fill(195, 113, 1); break;
					case 3: fill(242, 145, 27); break;
					case 4: fill(253, 226, 25); break;
				}

				rect(0, i * 128, 360, 128);
			}
		}

		if (time_of_day == 6) {
			for (int i = 0; i < 5; i++) {
				switch (i) {
					case 0: fill(255, 214, 150); break;
					case 1: fill(255, 176, 104); break;
					case 2: fill(255, 150, 108); break;
					case 3: fill(255, 147, 151); break;
					case 4: fill(227, 194, 194); break;
				}

				rect(0, i * 128, 360, 128);
			}
		}
	}

	// method to clear array of meteors

	public void clear() {
		for (int i = 0; i < meteors.length; i++) {
			if (meteors[i] != null) {
				meteors[i] = null;
			}
		}
	}

	// method to populate array of meteors with variable distance between non-null objects (meant to increase difficulty)

	public void populate(int meteors_multiplier) {
		for (int i = 0; i < meteors.length; i++) {
			if (i % (8 - meteors_multiplier) == 0) {
				Meteor new_meteor = new Meteor((random.nextInt(12)) * 36, 0);
				meteors[i] = new_meteor;
			}

			else {
				meteors[i] = null;
			}
		}
	}

	// method to re-iterate with for-loop and enter while loops to ensure no two meteors in a row share the same x-coordinate

	public void validate() {
		for (int i = 1; i < meteors.length; i++) {
			if (meteors[i] != null && meteors[i - 1] != null) {
				while (meteors[i].get_x() == meteors[i - 1].get_x()) {
					Meteor retry = new Meteor((random.nextInt(12)) * 36, 0);
					meteors[i] = retry;
				}				
			}
		}
	}

	// method to shift the meteors' y-coordinates so all meteors share the same y value, 0

	public void shift_back() {
		for (int i = 0; i < meteors.length; i++) {
			if (meteors[i] != null) {
				meteors[i].set_y(0);
			}
		}
	}

	// method to shift the meteors' y-coordinates so no two consecutive meteors share the same y value

	public void shift() {
		for (int i = 0; i < meteors.length; i++) {
			if (meteors[i] != null) {
				meteors[i].set_y(i * -64);
			}
		}
	}

	// method to determine whether any meteors are within screen bounds

	public boolean on_screen() {
		for (int i = 0; i < meteors.length; i++) {
			if (meteors[i] != null && meteors[i].get_y() > 0 && meteors[i].get_y() <= 640) {
				return false;
			}
		}

		return true;
	}

	// passive method which checks if user inputs left or right

	public void keyPressed() {
		
		if (key == CODED) {

			// conditionals to handle direction; edits axes, switches boolean, and forces program to redraw (due to noLoop())
    	
    		if (keyCode == LEFT && pterodactyl.get_x() > 36) {
				pterodactyl.move(-36);
				movement = true;
				redraw();
    		}
    		
    		if (keyCode == RIGHT && pterodactyl.get_x() < 324) {
				pterodactyl.move(36);
				movement = true;
				redraw();
    		}

    		if (life == 0 && (keyCode == LEFT || keyCode == RIGHT)) {
    			movement = true;
    		}
    	}
    }

    // method to handle game-end, restarts game if player moves

    public void game_over() {
// 		int high_score = 0;

// 		try {
			// while (reader.readLine() != null) {
   //  			high_score = Integer.parseInt(reader.readLine());
   //  		}

   //  		if (high_score < score) {
   //  			while (reader.readLine() != null) {
   //  				writer.write(score);
   //  			}
   //  		}
// 		}

// 		catch (IOException ex3) {
// 		}

		background(0, 0, 0);

		fill (255, 255, 255);
    	textSize(144);
    	
    	if (levels_completed > 0) {
			text(game_score, 180, 340);
    	}

    	else {
    		text(level_score, 180, 340);	
    	}

    	if (movement) {
    		loop();
			setup();
    	}
    }
}