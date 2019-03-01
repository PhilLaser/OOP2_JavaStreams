import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Philippe Leiser on 06.05.2017.
 */
public class main {
    private static List<Movie> movies;

    public static void main(String[] args) throws FileNotFoundException {
        /**
         * Reading in all the movies from the .txt file.
         */
        String file = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/movies.txt";

        /**
         * Depending on the setup of the executing java environment,
         * one may need to change the absolute path to the "movies.txt" file.
         */

        // String file = "--ABSOLUTE PATH HERE--"
        // uncomment line above and paste the absolute path if line 17 causes problems

        movies = Movie.readMovies(file);

        /**
         * printing out all the numbers
         */

        System.out.println("Number of movies contained in the database: \n" + countMovies() + "\n");
        System.out.println("Number of movies for which no director name is registered: \n" + countEmptyDirectors() + "\n");

        System.out.println("List of movies whose title begins with 'X': \n");
        moviesBeginningWithX().forEach(x -> System.out.println(x.getTitle()));

        System.out.println("\nNumber of directors who are also mentioned as actors: \n" + directorsWhoAreActors() + "\n");
        System.out.println("The movie with maximum number of actors: \n" + "Title: " + movieWithMaxActors().getTitle() + "\n"
                + "Director(s): " + movieWithMaxActors().getDirectors() + "\n"
                + "Actors: " + movieWithMaxActors().getActors() + "\n");
        System.out.println("Number of all actors: \n" + getNumberOfAllActors() + "\n");
        System.out.println("Number of all distinct actors: \n" + getNumberOfEveryDistinctActor() + "\n");
        System.out.println("Map of characters \n" + getMapOfMoviesWithSameStartingLetter() + "\n");
        System.out.println("List of most common words in movie title: \n" + getMostCommonWords() + "\n");
        System.out.println("Director with most movies: \n" + getDirectorWithMostMovies().get(0).getDirectors() + "\n");
        System.out.println("Films: ");
        getDirectorWithMostMovies().forEach(x -> System.out.println(x.getTitle()));




    }

    /**
     * @return the number of movies contained in the database.
     */

    private static long countMovies() {
        return movies.stream()
                .parallel()
                .filter(x -> !x.getTitle().isEmpty())
                .count();
    }

    /**
     * @return the number of movies with no directors registered.
     */

    private static long countEmptyDirectors() {
        return movies.stream()
                .parallel()
                .filter(movie -> movie.getDirectors().toString().charAt(1) == ']')
                .count();
    }

    /**
     * @return the list of movies starting with an 'X'.
     */

    private static List<Movie> moviesBeginningWithX() {
        return movies.stream()
                .filter(movie -> movie.getTitle().charAt(0) == 'X')
                .collect(Collectors.toList());
    }

    /**
     * @return the number of movies where directors are also mentioned as actors.
     */

    private static long directorsWhoAreActors() {
        return movies.stream().filter(x -> x.getDirectors().stream().anyMatch(y -> x.getActors().contains(y))).count();
    }

    /**
     *
     * @return the Movie with most actors.
     */

    private static Movie movieWithMaxActors() {
        Comparator<Movie> comparator = Comparator.comparingInt(m -> m.getActors().size());
        return movies.stream().max(comparator).get();

    }

    /**
     *
     * @return the number of all actors.
     */

    private static long getNumberOfAllActors() {
        List<Integer> actorSize = movies.stream()
                .parallel()
                .filter(movie -> movie.getActors().size() > 0)
                .map(movie -> movie.getActors().size())
                .collect(Collectors.toList());
        return actorSize.stream().mapToLong(Integer::intValue).sum();
    }

    /**
     *
     * @return the number of all distinct actors.
     */

    private static long getNumberOfEveryDistinctActor() {
        Set<String> distinctActors = new HashSet<>();
        List<List<String>> actors = movies.stream()
                .parallel()
                .filter(movie -> !movie.getActors().isEmpty())
                .map(Movie::getActors)
                .collect(Collectors.toList());
        for (List<String> actor : actors) {
            distinctActors.addAll(actor);
        }
        return distinctActors.size();
    }

    /**
     *
     * @return a map which shows how many movies start with each letter.
     */

    private static Map<String, Long> getMapOfMoviesWithSameStartingLetter(){
        return movies.stream()
                .parallel()
                .filter(movie -> movie.getTitle().matches("[a-zA-Z]"))
                .collect(Collectors.groupingBy(w -> w.getTitle().substring(0, 1), Collectors.counting()));
    }

    /**
     *
     * @return a list with the most common words the movie titles start.
     */

    private static List<String> getMostCommonWords(){
        return movies.stream()
                .map(p -> p.getTitle().split(" ")[0])
                .map(Object::toString)
                .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10).map(Object::toString).collect(Collectors.toList());
    }

    private static List<Movie> getDirectorWithMostMovies(){
        Map.Entry<String, Long> directors = movies.stream().filter(movie -> !movie.getDirectors().get(0).equals(""))
                .map(movie -> movie.getDirectors().get(0))
                .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(1).collect(Collectors.toList()).get(0);

        return movies.stream().filter(movie -> movie.getDirectors().get(0).equals(directors.getKey()))
                .collect(Collectors.toList());
    }

}


