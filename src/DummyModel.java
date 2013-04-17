import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DummyModel implements IBouncingBallsModel {

	// private static final double SHELL_THICKNESS = 0.002; // 0.002 m = 2 mm
	// private static final double DENSITY_OF_RUBBER = 730; // kg/m^3
	private static final double GRAVITATION = 9.82; // m/s^2

	private final double areaWidth;
	private final double areaHeight;

	private List<Ball> ballList;

	protected class Ball {
		protected double x, y, vx, vy, r;

		public Ball(double x, double y, double r) {
			this.x = x;
			this.y = y;
			this.r = r;
			vx = 1;
			vy = 1;

			// Random rand = new Random();
			// double xs, ys;
			//
			// wh: while (true) {
			// xs = rand.nextDouble() % areaWidth;
			// ys = rand.nextDouble() % areaHeight;
			//
			// for (Ball b : list) {
			// if ((b.x-b.r) >= x && x < (b.x+b.r)) {
			// break wh;
			// }
			// }
			//
			// this.x = xs;
			// this.y = ys;
			// }
			// For every ball in the list
			// Check that the coordinates doesn't spawn on top of another ball
			// If there is a ball there, start over with new coordinates
			// Else place the ball there
		}

		public void tick(double deltaT) {
			if (x < r || x > areaWidth - r) {
				vx *= -1;
			}
			if (y < r || y > areaHeight - r) { // ifall den studsar i taket
				vy *= -1;
			}

			vy -= GRAVITATION * deltaT;

			x += vx * deltaT;
			y += vy * deltaT;
			/*
			 * Om vi representerar en boll som ett klot så kommer det inte att
			 * stämma överens med verkligheten riktigt. Utan det vi vill göra är
			 * snarare att ta mantelarean av klotet med radien r (4*pi*r*r) med
			 * skalets tjocklek som vi antar är 0.002 m. Därför blir volymen av
			 * mantelarean*0.002 volymen av själva plasten som vi multiplicerar
			 * med densiteten 730 kg/m^3 vilket ger massan
			 */
			// double m = 4 * Math.PI * r * r * SHELL_THICKNESS *
			// DENSITY_OF_RUBBER;

			// f = m * y''
		}

	}

	public DummyModel(double width, double height) {
		this(width, height, 10);
	}

	public DummyModel(double width, double height, int ballAmount) {
		this.areaWidth = width;
		this.areaHeight = height;

		ballList = new LinkedList<Ball>();
		Random rand = new Random();
		for (int i = 0; i < ballAmount; i++) {
			double tempR = 0.5 + rand.nextDouble() % 1.5;
			ballList.add(new Ball(tempR + (i * tempR), areaHeight - tempR
					- (areaHeight / 2) * rand.nextDouble(), tempR));
		}
		System.out.println("" + areaHeight + " " + areaWidth);
	}

	@Override
	public void tick(double deltaT) {
		for (Ball b : ballList) {
			b.tick(deltaT);
		}

		// if (x < r || x > areaWidth - r) {
		// vx *= -1;
		// }
		// if (y < r) { // ifall den studsar i taket
		// vy *= -1;
		// }
		// if (y > areaHeight - r) { // ifall den studsar i golvet
		// vy *= -1;
		// }
		//
		// vy -= GRAVITATION * deltaT;
		//
		// x += vx * deltaT;
		// y += vy * deltaT;

		/*
		 * Om vi representerar en boll som ett klot så kommer det inte att
		 * stämma överens med verkligheten riktigt. Utan det vi vill göra är
		 * snarare att ta mantelarean av klotet med radien r (4*pi*r*r) med
		 * skalets tjocklek som vi antar är 0.002 m. Därför blir volymen av
		 * mantelarean*0.002 volymen av själva plasten som vi multiplicerar med
		 * densiteten 730 kg/m^3 vilket ger massan
		 */
		// double m = 4 * Math.PI * r * r * SHELL_THICKNESS * DENSITY_OF_RUBBER;

		// f = m * y''
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
		for (Ball b : ballList) {
			myBalls.add(new Ellipse2D.Double(b.x - b.r, b.y - b.r, 2 * b.r,
					2 * b.r));
		}
		return myBalls;

		// List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
		// myBalls.add(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
		// return myBalls;
	}
}