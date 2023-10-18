import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArtistTest {

	@BeforeAll
	static void setupBeforeClass() {
		String filePath = "testResult.txt";
		File file = new File(filePath);
		Artist.file = file;
	}

	@Test
	@Order(1)
	void testAddValidArtist() {

		// 1st valid artist
		Artist a1 = new Artist("678AAAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertTrue(a1.addArtist());

		// 2nd valid artist
		Artist a2 = new Artist("789BBBBB%%", "Name", "New York|New York|USA", "26-11-2000",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer", "Songwriter", "Producer")),
				new ArrayList<String>(Arrays.asList("classic", "pop", "jazz", "hip-hop", "country")),
				new ArrayList<String>(Arrays.asList("2021, The Best New Artist", "2022, The Most Popular Artist",
						"2023, Artist of the Year")));

		assertTrue(a2.addArtist());

	}

	@Test
	@Order(2)
	void testAddInvalidArtistId() {

		// id is shorter than 10 characters
		Artist a1 = new Artist("678AAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a1.addArtist());

		// mix first 3 characters contain number less than 5, id’s characters 4 – 8 are
		// not Upper A-Z
		Artist a2 = new Artist("456aaaaa##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a2.addArtist());
	}

	@Test
	@Order(3)
	void testAddInvalidArtistBirthDateFormat() {

		// date format “DD/MM/YYYY”
		Artist a1 = new Artist("678AAAAB##", "Name", "Sydney|New South Wales|Australia", "10/10/1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a1.addArtist());

		// date format “YYYY-MM-DD”
		Artist a2 = new Artist("678AAAAB##", "Name", "Sydney|New South Wales|Australia", "1979-10-10",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a2.addArtist());
	}

	@Test
	@Order(4)
	void testAddInvalidArtistAddress() {

		// address with no | delimiter
		Artist a1 = new Artist("678AAAAB##", "Name", "Sydney New South Wales Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a1.addArtist());

		// address with no country
		Artist a2 = new Artist("678AAAAB##", "Name", "Sydney|New South Wales|", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a2.addArtist());
	}

	@Test
	@Order(5)
	void testAddInvalidArtistBio() {

		// bio with 9 words
		Artist a1 = new Artist("678AAAAB##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a1.addArtist());

		// bio with 31 words
		Artist a2 = new Artist("678AAAAB##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the little birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a2.addArtist());
	}

	@Test
	@Order(6)
	void testAddInvalidArtistOccupations() {

		// no occupation
		Artist a1 = new Artist("678AAAAB##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList()), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a1.addArtist());

		// more than 5 occupations
		Artist a2 = new Artist("678AAAAB##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(
						Arrays.asList("Singer", "Songwriter", "Producer", "Sound Engineer", "Chorus", "Backing track")),
				new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));

		assertFalse(a2.addArtist());
	}

	@Test
	@Order(7)
	void testUpdateArtistAfter2000ValidOccupations() {
		// born after 2000, update with valid occupations by deleting Producer
		Artist updatedA1 = new Artist("789BBBBB%%", "Name", "New York|New York|USA", "26-11-2000",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer", "Songwriter")),
				new ArrayList<String>(Arrays.asList("classic", "pop", "jazz", "hip-hop", "country")),
				new ArrayList<String>(Arrays.asList("2021, The Best New Artist", "2022, The Most Popular Artist",
						"2023, Artist of the Year")));

		assertTrue(updatedA1.updateArtist());

		// born after 2000, update with valid occupations by adding Sound Engineer
		Artist updatedA2 = new Artist("789BBBBB%%", "Name", "New York|New York|USA", "26-11-2000",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer", "Songwriter", "Sound Engineer")),
				new ArrayList<String>(Arrays.asList("classic", "pop", "jazz", "hip-hop", "country")),
				new ArrayList<String>(Arrays.asList("2021, The Best New Artist", "2022, The Most Popular Artist",
						"2023, Artist of the Year")));

		assertTrue(updatedA2.updateArtist());
	}

	@Test
	@Order(8)
	void testUpdateArtistBefore2000ValidOccupations() {
		//born after 2000, update with valid occupations by deleting Producer
		Artist updatedA1 = new Artist("678AAAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer", "Producer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));
		
		assertFalse(updatedA1.updateArtist());
		
		//born after 2000, update with valid occupations by adding Sound Engineer
		Artist updatedA2 = new Artist("678AAAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Producer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));
		
		assertFalse(updatedA2.updateArtist());
	}
	
	@Test
	@Order(9)
	void testUpdateAwardAfter2000ValidAward() {
		//awarded after 2000, update with valid award by changing award in 2022 from The Most Popular Artist to Bestseller Album of the Year
		Artist updatedA1 = new Artist("789BBBBB%%", "Name", "New York|New York|USA", "26-11-2000",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer", "Songwriter", "Sound Engineer")),
				new ArrayList<String>(Arrays.asList("classic", "pop", "jazz", "hip-hop", "country")),
				new ArrayList<String>(Arrays.asList("2021, The Best New Artist", "2022, Bestseller Album of the Year",
						"2023, Artist of the Year")));
		
		assertTrue(updatedA1.updateArtist());
		
		//awarded after 2000, update with valid awards for 2 awards at the same time
		Artist updatedA2 = new Artist("789BBBBB%%", "Name", "New York|New York|USA", "26-11-2000",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer", "Songwriter", "Sound Engineer")),
				new ArrayList<String>(Arrays.asList("classic", "pop", "jazz", "hip-hop", "country")),
				new ArrayList<String>(Arrays.asList("2021, The Best Music Video", "2022, Bestseller Album of the Year",
						"2023, Best New Artist of the Year")));
		
		assertTrue(updatedA2.updateArtist());
	}
	
	@Test
	@Order(10)
	void testUpdateAwardBefore2000ValidAward() {
		//awarded before 2000, update with valid award from The Best New Artist to The Best Music Video
		Artist updatedA1 = new Artist("678AAAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best Music Video", "1999, The Most Popular Artist")));
		
		assertFalse(updatedA1.updateArtist());
		
		//awarded before 2000, update with valid awards for 2 awards at the same time
		Artist updatedA2 = new Artist("678AAAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Star Artist", "1999, The Most New Popular Artist")));
		
		assertFalse(updatedA2.updateArtist());
	}
	
	@Test
	@Order(11)
	void testUpdateAwardAfter2000InvalidAward() {
		//update to use only last 2-digit year
		Artist updatedA1 = new Artist("789BBBBB%%", "Name", "New York|New York|USA", "26-11-2000",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer", "Songwriter", "Sound Engineer")),
				new ArrayList<String>(Arrays.asList("classic", "pop", "jazz", "hip-hop", "country")),
				new ArrayList<String>(Arrays.asList("21, The Best Music Video", "2022, Bestseller Album of the Year",
						"2023, Artist of the Year")));
		
		assertFalse(updatedA1.updateArtist());
		
		//update award title to less than 4 words
		Artist updatedA2 = new Artist("789BBBBB%%", "Name", "New York|New York|USA", "26-11-2000",
				"The quick little brown fox highly jumps over the big lazy dog sitting comfortably in the sun, while the birds chirp and the trees sway gently in the afternoon breeze.",
				new ArrayList<String>(Arrays.asList("Singer", "Songwriter", "Sound Engineer")),
				new ArrayList<String>(Arrays.asList("classic", "pop", "jazz", "hip-hop", "country")),
				new ArrayList<String>(Arrays.asList("2021, The Best Singer", "2022, Bestseller Album of the Year",
						"2023, Best New Artist of the Year")));
		
		assertFalse(updatedA2.updateArtist());
	}
	
	@Test
	@Order(12)
	void testUpdateInvalidArtistGenres() {
		//update to use only last 2-digit year
		Artist updatedA1 = new Artist("678AAAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));
		
		assertFalse(updatedA1.updateArtist());
		
		//update award title to less than 4 words
		Artist updatedA2 = new Artist("678AAAAA##", "Name", "Sydney|New South Wales|Australia", "10-10-1979",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia",
				new ArrayList<String>(Arrays.asList("Singer")), new ArrayList<String>(Arrays.asList("classic", "pop", "rock")),
				new ArrayList<String>(Arrays.asList("1998, The Best New Artist", "1999, The Most Popular Artist")));
		
		assertFalse(updatedA2.updateArtist());
	}
}
