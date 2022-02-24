package com.potato;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.staccato.ReplacementMapPreprocessor;
import org.staccato.maps.SolfegeReplacementMap;

public class SolfegeReplacementMapDemo {
	public static void main(String[] args) {
		ReplacementMapPreprocessor rmp = ReplacementMapPreprocessor.getInstance();
		rmp.setReplacementMap(new SolfegeReplacementMap()).setRequireAngleBrackets(false);
		Player player = new Player();
		player.play(new Pattern("do re mi fa so la ti do")); // This will play "C D E F G A B"

		// This next example brings back the brackets so durations can be added
		rmp.setRequireAngleBrackets(true);
		player.play(new Pattern("<Do>q <Re>q <Mi>h | <Mi>q <Fa>q <So>h | <So>q <Fa>q <Mi>h | <Mi>q <Re>q <Do>h"));
	}
}