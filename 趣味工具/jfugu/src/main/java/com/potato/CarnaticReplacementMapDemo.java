package com.potato;

import org.jfugue.player.Player;
import org.staccato.ReplacementMapPreprocessor;
import org.staccato.maps.CarnaticReplacementMap;

public class CarnaticReplacementMapDemo {
  public static void main(String[] args) {
    ReplacementMapPreprocessor.getInstance().setReplacementMap(new CarnaticReplacementMap());

    Player player = new Player();
    player.play("<S> <R1> <R2> <R3> <R4>");
  }
}