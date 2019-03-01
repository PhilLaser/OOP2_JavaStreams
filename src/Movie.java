import java.io.*;
import java.util.*;

/**
 * This class loads a file of movies and instantiates
 * the Movie objects. A movie consists of:
 * - The movie title
 * - The date the movie has been filmed
 * - The director(s) of the movie (comma-separated list of strings)
 * - The producer(s) of the movie (comma-separated list of strings)
 * - The actors playing in the movie (comma-separated list of strings)
 */
public class Movie {

    private String title;
    private int year;
    private List<String> directors;
    private List<String> producers;
    private List<String> actors;

    private Movie(String title, int year, List<String> directors,
                  List<String> producers, List<String> actors) {
        this.title = title;
        this.year = year;
        this.directors = directors;
        this.producers = producers;
        this.actors = actors;
    }

    /**
     * Gets the movie title.
     *
     * @return the movie title
     */
    String getTitle() {
        return title;
    }

    /**
     * Gets the year the movie was made.
     *
     * @return the movie creation year
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the list of the movie directors (may be empty).
     *
     * @return the list of the movie directors
     */
    List<String> getDirectors() {
        return Collections.unmodifiableList(directors);
    }

    /**
     * Gets the list of the movie producers (may be empty).
     *
     * @return the list of the movie producers
     */
    public List<String> getProducers() {
        return Collections.unmodifiableList(producers);
    }

    /**
     * Gets the list of the actors playing in the movie (may be empty).
     *
     * @return the list of the movie actors
     */
    List<String> getActors() {
        return Collections.unmodifiableList(actors);
    }

    /**
     * Loads the movie database and instantiates the Movie objects.
     *
     * @return the list of the movies
     */
    static List<Movie> readMovies(String filename)
            throws FileNotFoundException {
        List<Movie> movies = new ArrayList<>();
        //System.out.println(new File("movies.txt").getAbsolutePath());
        try (Scanner in = new Scanner(new File(filename))) {
            while (in.hasNextLine()) {
                String nameLine = in.nextLine();
                String yearLine = in.nextLine();
                String directorsLine = in.nextLine();
                String producersLine = in.nextLine();
                String actorsLine = in.nextLine();
                movies.add(new Movie(getString(nameLine),
                        Integer.parseInt(getString(yearLine)),
                        getList(directorsLine),
                        getList(producersLine),
                        getList(actorsLine)));
            }
        }
        return movies;
    }

    private static String getString(String line) {
        int colon = line.indexOf(":");
        return line.substring(colon + 1).trim();
    }

    private static List<String> getList(String line) {
        return Arrays.asList(getString(line).split(", "));
    }
}
