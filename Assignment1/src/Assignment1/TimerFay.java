package Assignment1;

public class TimerFay {

	Long startTime;
	Long stopTime;

	void start() {
		startTime = System.currentTimeMillis();
	}

	void stop() {

		stopTime = System.currentTimeMillis();
	}

	Long getDuration() {
		return stopTime - startTime;
	}

}
