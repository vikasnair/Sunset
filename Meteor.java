import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

public class Meteor implements MeteorInterface {
	private Random random = new Random();
	private int x;
	private int y;
	private static PImage[] img_meteor;

	public Meteor() {
	}

	public Meteor(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void set_x(int x) {
		this.x = x;
	}

	public int get_x() {
		return x;
	}

	public void set_y(int y) {
		this.y = y;
	}

	public int get_y() {
		return y;
	}

	static void set_image(PImage img_meteor_1, PImage img_meteor_2) {
		img_meteor = new PImage[2];

		Meteor.img_meteor[0] = img_meteor_1;
		Meteor.img_meteor[1] = img_meteor_2;
	}
	
	static PImage get_image(int moves) {
		if (moves % 2 == 0) {
			return Meteor.img_meteor[1];
		}

		else {
			return Meteor.img_meteor[0];
		}
	}

	public void move() {
		y += 64;
	}

	public void home(int x) {
		if (this.x > x) {
			if ((this.x - x) < ((640 - this.x) + x)) {
				this.x -= 36;
				y += 64;
			}

			else {
				this.x += 36;
				y += 64;
			}
		}

		if (this.x < x) {
			if ((x - this.x) < ((this.x) + (640 - x))) {
				this.x += 36;
				y += 64;
			}

			else {
				this.x += 36;
				y += 64;
			}
		}
	}

	public boolean check_collision(int x, int y) {
		if (this.x == x && this.y == y) {
			return true;
		}

		return false;
	}
}