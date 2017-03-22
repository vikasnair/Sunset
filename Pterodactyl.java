import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

public class Pterodactyl implements PterodactylInterface {
	private Random random = new Random();
	private int x = 216;
	private int y = 576;
	private static PImage[] img_pterodactyl;
	private static PImage img_splat;

	public Pterodactyl() {
	}

	public Pterodactyl(int x) {
		this.x = x;
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

	public static void set_image(PImage img_pterodactyl_1, PImage img_pterodactyl_2) {
		img_pterodactyl = new PImage[2];

		Pterodactyl.img_pterodactyl[0] = img_pterodactyl_1;
		Pterodactyl.img_pterodactyl[1] = img_pterodactyl_2;
	}
	
	public static PImage get_image(int life, int moves) {
		if (life > 0) {
			if (moves % 2 == 0) {
				return Pterodactyl.img_pterodactyl[1];
			}

			else {
				return Pterodactyl.img_pterodactyl[0];
			}
		}

		else {
			return Pterodactyl.img_splat;
		}
	}

	public static void set_splat(PImage img_splat) {
		Pterodactyl.img_splat = img_splat;
	}

	public static PImage get_splat() {
		return Pterodactyl.img_splat;
	}

	public void move(int x) {
		if (x == 36) {
			this.x += x;
		}

		if (x == -36) {
			this.x += x;
		}
	}
}