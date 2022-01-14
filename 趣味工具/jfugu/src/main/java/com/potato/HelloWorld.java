package com.potato;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;
import org.junit.Test;

/**
 * http://www.jfugue.org/examples.html
 */
public class HelloWorld {
    @Test
    public void hello01() {
        Player player = new Player();
        player.play("C D E F G A B");
    }

    @Test
    public void hello02() {
        Player player = new Player();
        player.play("V0 I[Piano] Eq Ch. | Eq Ch. | Dq Eq Dq Cq   V1 I[Flute] Rw | Rw | GmajQQQ CmajQ");
    }

    @Test
    public void hello03() {
        Pattern p1 = new Pattern("Eq Ch. | Eq Ch. | Dq Eq Dq Cq").setVoice(0).setInstrument("Piano");
        Pattern p2 = new Pattern("Rw     | Rw     | GmajQQQ  CmajQ").setVoice(1).setInstrument("Flute");
        Player player = new Player();
        player.play(p1, p2);
    }

    @Test
    //和弦
    public void hello04() {
        ChordProgression cp = new ChordProgression("I IV V");
        Chord[] chords = cp.setKey("C").getChords();
        for (Chord chord : chords) {
            System.out.print("Chord " + chord + " has these notes: ");
            Note[] notes = chord.getNotes();
            for (Note note : notes) {
                System.out.print(note + " ");
            }
            System.out.println();
        }
        Player player = new Player();
        player.play(cp);
    }

    @Test
    //高级和弦进行曲
    public void hello05() {
        ChordProgression cp = new ChordProgression("I IV V");
        Player player = new Player();
        player.play(cp.eachChordAs("$0q $1q $2q Rq"));
        player.play(cp.allChordsAs("$0q $0q $0q $0q $1q $1q $2q $0q"));
        player.play(cp.allChordsAs("$0 $0 $0 $0 $1 $1 $2 $0").eachChordAs("V0 $0s $1s $2s Rs V1 $!q"));
    }

    @Test
    //十二小节蓝调
    public void hello06() {
        Pattern pattern = new ChordProgression("I IV V")
                .distribute("7%6")
                .allChordsAs("$0 $0 $0 $0 $1 $1 $0 $0 $2 $1 $0 $0")
                .eachChordAs("$0ia100 $1ia80 $2ia80 $3ia80 $4ia100 $3ia80 $2ia80 $1ia80")
                .getPattern()
                .setInstrument("Acoustic_Bass")
                .setTempo(100);
        new Player().play(pattern);
    }

    @Test
    //节奏
    public void hello07() {
        Rhythm rhythm = new Rhythm()
                .addLayer("O..oO...O..oOO..")
                .addLayer("..S...S...S...S.")
                .addLayer("````````````````")
                .addLayer("...............+");
        new Player().play(rhythm.getPattern().repeat(2));
    }

    @Test
    //高级节奏
    public void hello08() {
        Rhythm rhythm = new Rhythm()
                .addLayer("O..oO...O..oOO..") // This is Layer 0
                .addLayer("..S...S...S...S.")
                .addLayer("````````````````")
                .addLayer("...............+") // This is Layer 3
                .addOneTimeAltLayer(3, 3, "...+...+...+...+") // Replace Layer 3 with this string on the 4th (count from 0) measure
                .setLength(4); // Set the length of the rhythm to 4 measures
        new Player().play(rhythm.getPattern().repeat(2)); // Play 2 instances of the 4-measure-long rhythm
    }

    @Test
    //合在一起试试
    public void hello09() {
        new Player().play(new ChordProgression("I IV vi V").eachChordAs("$!i $!i Ri $!i"), new Rhythm().addLayer("..X...X...X...XO"));
    }
}
