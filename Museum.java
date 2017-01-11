

import java.util.Random;

public class Museum {
	public static int numVisitors = 0;// num of visitors currently in museum
	public static boolean canEnter = false;// if museum is open
	public static boolean canExit = false;// if museum is closed
	public static East east = new East();// entrance thread
	public static West west = new West();// exit thread
	public static Director dir = new Director();// director thread
	public static Control ctrl = new Control();// control thread
	

	public static void main(String args[]){
		dir.begin();// all threads begin
		east.begin();
		west.begin();
		ctrl.begin();
	}
	
}

class East implements Runnable {// CLass EAST i.e Visitors arriving
	public void run() {
			while (!Museum.canEnter) {// if museum closed
				try {
					synchronized(Museum.east) {
						Museum.east.wait();// then noone can enter through
					}
										// EAST(Entrance)
				} catch (InterruptedException e) {
					e.printStackTrace();
				} // if musemOpened let visitors enter
			}
	}

	public void begin() {
		(new Thread(new East())).start();
	}
}

class West implements Runnable {// PROCESS WEST i.e Visitors leaving
	public void run() {
			while (!Museum.canExit) {// while museum is closed
				try {
					synchronized(Museum.west) {
						Museum.west.wait();// then noone can enter through
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

	public void begin() {
		(new Thread(new West())).start();
	}
}

class Control implements Runnable {// handles the notifying of threads based on conditions
	private static Random random = new Random();// either entering OR exiting

	public void run() {
			while (true) {
				int coinFlip = random.nextInt(2);// ;random between 0 and 1
				if (Museum.canEnter) {
						if (coinFlip == 1) {
							synchronized (Museum.east) {
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								System.out.println("Visitor Entering");
								Museum.numVisitors++;// the number of visitors increase when they enter
								Museum.east.notify();// east enter}
							}	
						}
						else {
							synchronized (Museum.west) {
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								System.out.println("Visitor Leaving");
								Museum.numVisitors--;// the number of visitors decrease when they enter
								Museum.west.notify();// west leave
						}
					}
				}
				else{//Only allowed to leave 
					synchronized (Museum.west) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("Visitor Leaving");
						Museum.numVisitors--;
						if(Museum.numVisitors<0)break;
						Museum.west.notify();// west leave
						
				}
					
				}
			}
	}

	public void begin() {
		(new Thread(new Control())).start();
	}
}

class Director implements Runnable {// PROCESS DIRECTOR
	public void run() {// initially opens the museum
		Museum.canEnter = true;
		Museum.canExit = true;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Museum.canEnter = false;
		System.out.println("\nMuseum is now closed!\n");
	}

	public void begin() {
		(new Thread(new Director())).start();
	}
}
