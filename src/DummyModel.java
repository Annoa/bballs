import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DummyModel implements IBouncingBallsModel {

	 private static final double SHELL_THICKNESS = 0.002; // 0.002 m = 2 mm
	 private static final double DENSITY_OF_RUBBER = 730; // kg/m^3
	private static final double GRAVITATION = 9.82; // m/s^2

	private final double areaWidth;
	private final double areaHeight;

	private List<Ball> ballList;
	
	protected class Coordinate {
		protected double x, y;
		
		public Coordinate(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public Polar rectToPolar() {
			double r = Math.sqrt(this.x * this.x + this.y * this.y);
			double theta = Math.atan2(y, x);
			return new Polar(r, theta);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(x);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(y);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Coordinate other = (Coordinate) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
				return false;
			}
			if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
				return false;
			}
			return true;
		}

		private DummyModel getOuterType() {
			return DummyModel.this;
		}
		
	}
	
	protected class Polar {
		protected double r, theta;
		
		public Polar(double r, double theta) {
			this.r = r;
			this.theta = theta;
		}
		
		public Coordinate polarToRect() {
			double x = r * Math.cos(theta);
			double y = r * Math.sin(theta);
			return new Coordinate(x, y);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(r);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(theta);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Polar other = (Polar) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r)) {
				return false;
			}
			if (Double.doubleToLongBits(theta) != Double
					.doubleToLongBits(other.theta)) {
				return false;
			}
			return true;
		}

		private DummyModel getOuterType() {
			return DummyModel.this;
		}
	}

	protected class Ball {
		protected double x, y, vx, vy, r, mass;

		public Ball(double x, double y, double r) {
			this.x = x;
			this.y = y;
			this.r = r;
			vx = 1;
			vy = 1;
			mass = r;

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

			else {
			vy -= GRAVITATION * deltaT;
			}

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
		this(width, height, 2);
	}

	public DummyModel(double width, double height, int ballAmount) {
		this.areaWidth = width;
		this.areaHeight = height;

		ballList = new ArrayList<Ball>();
		Random rand = new Random();
		for (int i = 0; i < ballAmount; i++) {
			double tempR = 0.5 + rand.nextDouble() % 1.5;
			ballList.add(new Ball(tempR + (3*i * tempR), areaHeight - tempR
					- (areaHeight / 2) * rand.nextDouble(), tempR));
		}
		System.out.println("" + areaHeight + " " + areaWidth);
	}

	@Override
	public void tick(double deltaT) {
		for (Ball b : ballList) {
			b.tick(deltaT);
		}
		for (int i = 0; i<ballList.size();i++){
			for (int j = i+1;j<ballList.size();j++){
				if (isColliding(ballList.get(i),ballList.get(j))){
					handleCollision(ballList.get(i), ballList.get(j));
				}
			}
		}
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
	

	private void handleCollision(Ball b1, Ball b2) {
		double deltaX = b1.x - b2.x;
        double deltaY = b1.y - b2.y;
        double angle = Math.atan2(deltaY, deltaX);

        //transfer the balls into polar coordinates
        double rBall1 = Math.sqrt(b1.vx * b1.vx + b1.vy * b1.vy);
        double rBall2 = Math.sqrt(b2.vx * b2.vx + b2.vy * b2.vy);
        double thetaBall1 = Math.atan2(b1.vy, b1.vx);
        double thetaBall2 = Math.atan2(b2.vy, b2.vx);

        //rotate the balls, fixing x as a basis
        double newXBall1 = rBall1 * Math.cos(thetaBall1 - angle);
        double newYBall1 = rBall1 * Math.sin(thetaBall1 - angle);
        double newXBall2 = rBall2 * Math.cos(thetaBall2 - angle);
        double newYBall2 = rBall2 * Math.sin(thetaBall2 - angle);


        //calculate the new velocities
        double vx1 = ((b1.mass - b2.mass) * newXBall1 + (b2.mass + b2.mass) * newXBall2) / (b1.mass + b2.mass);
        double vx2 = ((b1.mass + b1.mass) * newXBall1 + (b2.mass - b1.mass) * newXBall2) / (b1.mass + b2.mass);
        double vy1 = newYBall1;
        double vy2 = newYBall2;

        //change back to cartesian coordinates and rotate back the balls
        b1.vx = Math.cos(angle) * vx1 + Math.cos(angle + Math.PI / 2) * vy1;
        b1.vy = Math.sin(angle) * vx1 + Math.sin(angle + Math.PI / 2) * vy1;
        b2.vx = Math.cos(angle) * vx2 + Math.cos(angle + Math.PI / 2) * vy2;
        b2.vy = Math.sin(angle) * vx2 + Math.sin(angle + Math.PI / 2) * vy2;
		
		/*Coordinate co = new Coordinate(a.x, a.y);
		Polar po = co.rectToPolar();
		Coordinate cd = new Coordinate(a.x - b.x, a.y - b.y);
		Polar pd = cd.rectToPolar();
		double force = Math.cos(Math.abs(po.theta - pd.theta)) * po.r;
		
		po = new Polar(force, pd.theta);
		co = po.polarToRect();
		b.vx = co.x;
		b.vy = co.y;
		
		co = new Coordinate(b.x, b.y);
		po = co.rectToPolar();
		force = Math.cos(Math.abs(po.theta - pd.theta)) * po.r;
		po = new Polar(force, pd.theta);
		co = po.polarToRect();
		a.vx = co.x;
		b.vy = co.y;*/	
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

	protected List<Ball>[] getCollisions(List<Ball> balls) {
		@SuppressWarnings("unchecked")
		List<Ball>[] collisionsArray = new LinkedList[balls.size()];

		/*
		 * För att kolla om två cirklar är inom räckhåll av varandras areor så
		 * räknar vi avståndet mellan deras koordinater i var sitt led. Sen
		 * använder vi pythagoras för att räkna ut avståndet där emellan.
		 * 
		 * Om avståndet är större än summan av deras radier så nuddas de inte
		 * Om det är lika med summan av deras radier så nuddar de varandra. Om det är mindre än
		 * summan av deras radier så genomskär de varandra
		 * 
		 * Vi måste jämföra varje cirkel med alla andra cirklar (utan en annan
		 * datastruktur och skriva en comparator som gör att man kan använda ett
		 * träd eller liknande.
		 */
		
		double ix, iy, ir;
		for (int i = 0; i < balls.size(); i++) {
			Ball b = balls.get(i);
			ix = b.x; iy = b.y; ir = b.r;
			
			double dist;
			for (Ball b2 : balls) {
				if (b == b2) {
					continue;
				} else {
					dist 	= Math.sqrt((ix - b2.x) * (ix - b2.x) 
								+ (iy - b2.y) * (iy - b2.y));
					if (dist <= (ir + b2.r)) {
						addToList(collisionsArray, i, b2);
					}
				}
			}
		}
		return collisionsArray;
	}
	
	/*
	 * Add ball b to the list at position i on the list[]
	 */
	private void addToList(List<Ball>[] array, int i, Ball b) {
		
		if (array[i] == null) {
			LinkedList<Ball> t = new LinkedList<Ball>();
			t.add(b);
			array[i] = t;
		} else {
			array[i].add(b);
		}
	}
	
	private boolean isColliding(Ball b1, Ball b2){
	        double xDist = b2.x - b1.x;
	        double yDist = b2.y - b1.y;
	        double dist = Math.sqrt(xDist * xDist + yDist * yDist);
	        return dist <= b1.r + b2.r;
	}
	    
}
