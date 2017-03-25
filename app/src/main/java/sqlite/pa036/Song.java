package sqlite.pa036;

import java.util.Date;

/**
 * Created by Martin on 25-Mar-17.
 */

public class Song {
    private int id;
    private String nameOfSong;
    private String nameOfAuthor;
    private String textOfSong;
    private double length;
    private Date dateAdded;

    public Song(){}

    public Song(String nameOfSong, String nameOfAuthor, String textOfSong, double length, Date dateAdded) {
        this.nameOfSong = nameOfSong;
        this.nameOfAuthor = nameOfAuthor;
        this.textOfSong = textOfSong;
        this.length = length;
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOfSong() {
        return nameOfSong;
    }

    public void setNameOfSong(String nameOfSong) {
        this.nameOfSong = nameOfSong;
    }

    public String getNameOfAuthor() {
        return nameOfAuthor;
    }

    public void setNameOfAuthor(String nameOfAuthor) {
        this.nameOfAuthor = nameOfAuthor;
    }

    public String getTextOfSong() {
        return textOfSong;
    }

    public void setTextOfSong(String textOfSong) {
        this.textOfSong = textOfSong;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return  "nameOfSong = '" + nameOfSong + '\n' +
                "nameOfAuthor = '" + nameOfAuthor + '\n' +
                "length = " + length + '\n' +
                "dateAdded = " + dateAdded + '\n' +
                "textOfSong = " + textOfSong + '\n';
    }
}
