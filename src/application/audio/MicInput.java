package application.audio;

import javax.sound.sampled.*;

public class MicInput {

    private final int sampleRate = 44100;
    private final int bufferSize = 2048;

    public boolean running = false;
    private Thread micThread;

    public interface FrequencyListener {
        void onFrequencyDetected(double freq);
    }

    private final FrequencyListener listener;

    public MicInput(FrequencyListener listener) {
        this.listener = listener;
    }

    
    public void start() {
        if (running) return;
        running = true;

        micThread = new Thread(() -> {
            TargetDataLine mic = null;
            try {
                AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                mic = (TargetDataLine) AudioSystem.getLine(info);
                mic.open(format);
                mic.start();

                byte[] buffer = new byte[bufferSize * 2];

                while (running) {
                    int bytesRead = mic.read(buffer, 0, buffer.length);
                    if (bytesRead <= 0) continue;

                    short[] samples = new short[bytesRead / 2];
                    for (int i = 0; i < samples.length; i++) {
                        samples[i] = (short) ((buffer[2 * i] << 8) | (buffer[2 * i + 1] & 0xFF));
                    }

                    double freq = detectFrequency(samples, sampleRate);


                    if (freq > 50 && freq < 1000 && listener != null) {
                        listener.onFrequencyDetected(freq);
                    }

                    Thread.sleep(20);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mic != null) {
                    try {
                        mic.stop();
                        mic.close();
                    } catch (Exception ignored) {}
                }
            }
        });

        micThread.setDaemon(true);
        micThread.start();
    }


    public void stop() {
        running = false;
        if (micThread != null && micThread.isAlive()) {
            try {
                micThread.join(200);
            } catch (InterruptedException ignored) {}
        }
    }

    private double detectFrequency(short[] samples, int sampleRate) {
        int size = samples.length;
        double[] autocorr = new double[size];

        double rms = 0;
        for (int i = 0; i < size; i++) rms += samples[i] * samples[i];
        rms = Math.sqrt(rms / size);
        if (rms < 500) return -1;

        for (int lag = 0; lag < size; lag++) {
            double sum = 0;
            for (int i = 0; i < size - lag; i++) sum += samples[i] * samples[i + lag];
            autocorr[lag] = sum;
        }

        int peakIndex = 0;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = sampleRate / 1000; i < size / 2; i++) {
            if (autocorr[i] > max) {
                max = autocorr[i];
                peakIndex = i;
            }
        }

        return (peakIndex > 0) ? (double) sampleRate / peakIndex : -1;
    }
}
