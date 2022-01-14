package com.potato;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

public class TwoTigers {
    public static void main(String[] args) {
        Player player = new Player();
        //两只老虎
        Pattern pattern1 = new Pattern("I[40] C5q D5q E5q C5q");
        //跑得快
        Pattern pattern2 = new Pattern("E5q F5q G5h");
        //一只没有眼睛
        Pattern pattern3 = new Pattern("G5i A5i G5i F5i E5q C5q");
        //真奇怪
        Pattern pattern4 = new Pattern("C5q G4q C5h");

        // Put all of the patters together to form the song
        Pattern song = new Pattern();
        song.add(pattern1, 2); // Adds 'pattern1' to 'song' twice
        song.add(pattern2, 2); // Adds 'pattern2' to 'song' twice
        song.add(pattern3, 2); // Adds 'pattern3' to 'song' twice
        song.add(pattern4, 2); // Adds 'pattern4' to 'song' twice
        // Play the song!
        player.play(song);
    }
}
