import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Artist {

	private String id;
	private String name;
	private String address;
	private String birthDate;
	private String bio;
	private ArrayList<String> occupations;
	private ArrayList<String> genres;
	private ArrayList<String> awards;

	public static File file;

	private HashMap<String, Artist> artists;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public String getBio() {
		return bio;
	}

	public ArrayList<String> getOccupations() {
		return occupations;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public ArrayList<String> getAwards() {
		return awards;
	}

	public Artist(String id, String name, String address, String birthDate, String bio, ArrayList<String> occupations,
			ArrayList<String> genres, ArrayList<String> awards) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.birthDate = birthDate;
		this.bio = bio;
		this.occupations = occupations;
		this.genres = genres;
		this.awards = awards;
		Collections.sort(occupations);
		Collections.sort(awards);
		Collections.sort(genres);
	}

	public boolean addArtist() {

		// validate field
		if (!checkId()) {
			return false;
		}

		if (!checkDateFormat()) {
			return false;
		}

		if (!checkAddress()) {
			return false;
		}

		if (!checkBio()) {
			return false;
		}

		if (!checkOccupations()) {
			return false;
		}

		if (!checkAwards()) {
			return false;
		}

		if (!checkGenres()) {
			return false;
		}

		// if the file is exist load data to the collection
		long lastUpdated = 0;
		long currentUpdate = 0;
		if (file.exists()) {
			lastUpdated = file.lastModified();
			loadData();
		} else {
			this.artists = new HashMap<String, Artist>();
		}

		artists.put(id, new Artist(id, name, address, birthDate, bio, occupations, genres, awards));
		currentUpdate = writeData();

		// check if file is exist
		if (lastUpdated == currentUpdate) {
			return false;
		}
		return true;

	}

	public boolean updateArtist() {

		// get a collection of artists from file
		if (!file.exists()) {
			// if their is no specific file in Test first run will fail
			System.out.println("File not found");
			return false;
		}
		loadData();

		// get current artist by id
		Artist currentArtist = artists.get(id);
		if (currentArtist == null) {
			System.out.println("Their is no artist id " + id);
			return false;
		}

		String updatedInfo = "Update success: ";
		// set field = updateArtist's field by validating each field
		if (!checkDateFormat()) {
			return false;
		}
		currentArtist.birthDate = this.birthDate;

		if (!checkAddress()) {
			return false;
		}
		currentArtist.address = this.address;

		if (!checkBio()) {
			return false;
		}
		currentArtist.bio = this.bio;

		// if try to update occupation, artist has to born after 2000
		// check if collection has been changed
		if (!currentArtist.occupations.equals(this.occupations)) {
			// check artist birth year
			if (currentArtist.isBornBefore2000()) {
				return false;
			}
			if (!checkOccupations()) {
				return false;
			}
			updatedInfo += "Update artist's occupations from " + currentArtist.occupations + " to " + this.occupations
					+ "\s";
			currentArtist.occupations = this.occupations;
		}

		// if try to update awards, each award has to be awarded after 2000
		if (!currentArtist.awards.equals(this.awards)) {

			if (!checkAwards()) {
				return false;
			}

			// convert to Map for data manipulation
			HashMap<Integer, String> currentArtistAwards = mapAwards(currentArtist.awards);
			HashMap<Integer, String> thisArtistAwards = mapAwards(this.awards);

			// for each previous award
			for (Map.Entry<Integer, String> entry : currentArtistAwards.entrySet()) {
				if (entry.getValue() != thisArtistAwards.get(entry.getKey())) {
					if (isAwardedBefore2000(entry)) {
						return false;
					}
					currentArtistAwards.put(entry.getKey(), thisArtistAwards.get(entry.getKey()));
				}
			}
			// convert back to array list of string
			ArrayList<String> updatedCurrentArtistAward = new ArrayList<String>();
			for (Map.Entry<Integer, String> entry : currentArtistAwards.entrySet()) {
				updatedCurrentArtistAward.add(entry.getKey() + ", " + entry.getValue());
			}

			updatedInfo += "Update artist's occupations from " + currentArtist.awards + " to "
					+ updatedCurrentArtistAward;
			currentArtist.awards = updatedCurrentArtistAward;

			// Note: This method cannot add or delete an award

		}

		if (!checkGenres()) {
			return false;
		}
		updatedInfo += "Update artist's genres from " + currentArtist.genres + " to " + this.genres + "\s";
		currentArtist.genres = this.genres;

		// check if the designed artist has been update, not included add or delete
		// awards
		if (!(currentArtist.id.equals(this.id) && currentArtist.name.equals(this.name)
				&& currentArtist.address.equals(this.address) && currentArtist.bio.equals(this.bio)
				&& currentArtist.occupations.equals(this.occupations) && currentArtist.awards.equals(this.awards)
				&& currentArtist.genres.equals(this.genres))) {
			System.out.println("Artist was not updated");
			return false;
		}

		// put to collection
		artists.put(currentArtist.id, currentArtist);

		// write to file
		long currentUpdate = writeData();

		// check if file is updated
		if (currentUpdate == 0) {
			return false;
		}

		System.out.println(updatedInfo);
		return true;

	}

	private boolean checkId() {
		String regExPattern = "^[5-9]{3}[A-Z]{5}[^a-zA-Z0-9]{2}$";
		Pattern pattern = Pattern.compile(regExPattern);
		Matcher matcher = pattern.matcher(id);

		if (!matcher.matches()) {
			System.out.println("InvalidId " + id);
			return false;
		}

		return true;
	}

	private boolean checkDateFormat() {
		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try {
			LocalDate.parse(birthDate, dateformat);
		} catch (DateTimeParseException e) {
			System.out.println("InvalidDateTime " + birthDate);
			return false;
		}
		return true;
	}

	private boolean checkAddress() {
		String[] fullAddress = address.split("\\|");

		if (fullAddress.length != 3) {
			System.out.println("InvalidAddress " + address);
			return false;
		}

		String city = fullAddress[0].strip();
		String state = fullAddress[1].strip();
		String country = fullAddress[2].strip();

		if (city.isBlank() || state.isBlank() || country.isBlank()) {
			System.out.println("InvalidAddress " + address);
			return false;
		}

		return true;
	}

	private boolean checkBio() {
		int wordCount = bio.split("\s").length;
		if (wordCount < 10 || wordCount > 30) {
			System.out.println("InvalidBio " + bio);
			return false;
		}

		return true;
	}

	private boolean checkOccupations() {
		if (occupations.size() < 1 || occupations.size() > 5) {
			System.out.println("InvalidOccupations " + formatArrayString(occupations));
			return false;
		}

		return true;
	}

	private boolean checkAwards() {
		if (awards.size() > 3) {
			System.out.println("InvalidAward " + formatArrayString(awards));
			return false;
		}

		for (String award : awards) {
			String[] part = award.split(", ");
			String year = part[0];
			String title = part[1];
			Matcher matcher = Pattern.compile("[0-9]{4}").matcher(year);
			if (!matcher.matches()) {
				System.out.println("InvalidAward " + formatArrayString(awards));
				return false;
			}
			if (title.split("\s").length < 4 || title.split("\s").length > 10) {
				System.out.println("InvalidAward " + formatArrayString(awards));
				return false;
			}
		}

		return true;
	}

	private boolean checkGenres() {
		if (genres.size() < 2 || genres.size() > 5) {
			System.out.println("InvalidGenres " + formatArrayString(genres));
			return false;
		}

		boolean isPop = false;
		boolean isRock = false;
		for (String genre : genres) {
			if (genre.toLowerCase() == "pop") {
				isPop = true;
			}
			if (genre.toLowerCase() == "rock") {
				isRock = true;
			}
		}

		if (isPop && isRock) {
			System.out.println("InvalidGenres " + formatArrayString(genres));
			return false;
		}

		return true;
	}

	private void loadData() {
		try (Scanner scanner = new Scanner(file)) {
			HashMap<String, Artist> artists = new HashMap<String, Artist>();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
//				System.out.println(line);
				if (line.isBlank()) {
					break;
				}
				Artist artist = extractLine(line);
				artists.put(artist.getId(), artist);
			}
			this.artists = artists;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private long writeData() {
		String writeData = "";
		long currentUpdate = 0;

		for (Artist artist : artists.values()) {
			writeData += artist.toString();
		}

		// write to file
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(writeData);
			fileWriter.close();
			currentUpdate = file.lastModified();
		} catch (IOException e) {
			System.out.println("Cannot write to file");
		}

		return currentUpdate;
	}

	@Override
	public String toString() {
		String occupationsString = formatArrayString(occupations);
		String genresString = formatArrayString(genres);
		String awardsString = formatArrayString(awards);
		return String.format("\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", [%s], [%s], [%s]\n", id, name, address, birthDate,
				bio, occupationsString, genresString, awardsString);
	}

	private String formatArrayString(ArrayList<String> arrayList) {
		String string = "";
		for (int i = 0; i < arrayList.size(); i++) {
			if (i + 1 == arrayList.size()) {
				string += String.format("\"%s\"", arrayList.get(i));
			} else {
				string += String.format("\"%s\",\s", arrayList.get(i));
			}
		}
		return string;
	}

	private Artist extractLine(String input) {
		String[] parts = input.split("\", ", 6);

		String id = parts[0].replace("\"", "");
		String name = parts[1].replace("\"", "");
		String address = parts[2].replace("\"", "");
		String birthDate = parts[3].replace("\"", "");
		String bio = parts[4].replace("\"", "");

		String[] secondParts = parts[parts.length - 1].split("\\], \\[");

		ArrayList<String> occupations = extractArray(secondParts[0]);
		ArrayList<String> genres = extractArray(secondParts[1]);
		ArrayList<String> awards = extractArray(secondParts[2]);

		return new Artist(id, name, address, birthDate, bio, occupations, genres, awards);
	}

	private ArrayList<String> extractArray(String stringArray) {
		String[] array = stringArray.split("\", ");
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String item : array) {
			arrayList.add(item.replace("[", "").replace("\"", "").replace("]", ""));
		}
		return arrayList;
	}

	private boolean isBornBefore2000() {
		if (Integer.parseInt(birthDate.split("-")[2]) >= 2000) {
			return false;
		}
		System.out.println(
				"Artist was borned at " + Integer.parseInt(birthDate.split("-")[2]) + ". Cannot update occupation");
		return true;
	}

	private boolean isAwardedBefore2000(Map.Entry<Integer, String> award) {
		if (award.getKey() >= 2000) {
			return false;
		}
		System.out.println("\"" + award.getKey() + ", " + award.getValue()
				+ "\" was gived before 2000. Cannot update award title");
		return true;
	}

	private HashMap<Integer, String> mapAwards(ArrayList<String> awards) {
		HashMap<Integer, String> awardsMap = new HashMap<Integer, String>();
		for (String award : awards) {
			String[] split = award.split(", ");
			awardsMap.put(Integer.parseInt(split[0]), split[1]);
		}
		return awardsMap;
	}

}
