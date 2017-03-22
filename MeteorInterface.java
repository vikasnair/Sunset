public interface MeteorInterface {
	public abstract void move();
	public abstract void home(int x);
	public abstract boolean check_collision(int x, int y);
}