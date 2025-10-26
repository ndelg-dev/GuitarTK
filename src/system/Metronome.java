package system;

import java.awt.Toolkit;

public class Metronome {
	
	private int bpm;
	private boolean running;
	private Thread metronomeThread;
	
	public Metronome(int bpm) {
		this.bpm = bpm;
		this.running = false;
	}
	
	public void start() {
		if(running) {
			System.out.println("Metronome is already running.");
			return;
		}
		running = true;
		metronomeThread = new Thread(() -> {
			try {
				long intervalMillis = (long) (60000.0 / bpm);
				while(running) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("Beat");
					Thread.sleep(intervalMillis);
				}
				
			} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println("Metronome stopped unexpectedly.");
				}
		});
	}
	
	public void stop() {
		if(!running) {
			System.out.println("Metronome is not running.");
			return;
		}
		running = false;
		if(metronomeThread != null) {
			metronomeThread.interrupt();
			System.out.println("Metronome stopped");
		}
	}
	
	public void setBpm(int newBpm) {
		if(newBpm > 0) {
			this.bpm = newBpm;
			if(running) {
				stop();
				start();
			}
			System.out.println("BPM set to "+ newBpm);
		} else {
			System.out.println("BPM must be a positive value.");
		}
	}

	public static void main(String[] args) throws InterruptedException {
        Metronome metronome = new Metronome(120); // Initialize with 120 BPM
        metronome.start();

        // Let it run for a few seconds
        Thread.sleep(5000);
        
        metronome.stop();

    }
	
}
