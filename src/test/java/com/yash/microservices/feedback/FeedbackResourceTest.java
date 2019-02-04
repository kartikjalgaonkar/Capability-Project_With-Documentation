package com.yash.microservices.feedback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackResourceTest {

	FeedbackResource feedbackResourceOne;
	FeedbackResource feedbackResourceTwo;
	FeedbackResource feedbackResourceThree;

	@Before
	public void init() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date date = format.parse("2019-01-19");
		feedbackResourceOne = new FeedbackResource("jd123", "John Deere", 5, "great", date);
		feedbackResourceTwo = new FeedbackResource("jd123", "John Deere", 5, "great", date);
		feedbackResourceThree = new FeedbackResource("jd123", "John Deere", 5, "great", date);
	}

	@Test
	public void testHashCodeConsistency() throws ParseException {

		assertEquals(feedbackResourceOne, feedbackResourceOne);
	}

	@Test
	public void testHashCodeEquality() throws ParseException {

		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		assertEquals(feedbackResourceOne.hashCode(), feedbackResourceTwo.hashCode());

	}

	@Test
	public void testReflexive() throws ParseException {

		assertTrue(feedbackResourceOne.equals(feedbackResourceOne));
		assertEquals(feedbackResourceOne.hashCode(), feedbackResourceOne.hashCode());
	}

	@Test
	public void testSymmetric() throws ParseException {
		
		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo)); 
		assertTrue(feedbackResourceTwo.equals(feedbackResourceOne));
		assertEquals(feedbackResourceOne.hashCode(), feedbackResourceTwo.hashCode());
		
	}

	@Test
	public void testTransitive() throws ParseException {

		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		assertTrue(feedbackResourceTwo.equals(feedbackResourceThree));
		assertTrue(feedbackResourceThree.equals(feedbackResourceOne));
	}

	@Test
	public void testNonNullity() throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date date = format.parse("2019-01-19");

		assertFalse(feedbackResourceOne.equals(null));

		feedbackResourceOne.setUsername(null);
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceTwo.setUsername(null);
		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceTwo.setUsername("jd123");
		feedbackResourceOne.setUsername("jd123");

		feedbackResourceOne.setSource(null);
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceTwo.setSource(null);
		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setSource("John Deere");
		feedbackResourceTwo.setSource("John Deere");

		feedbackResourceOne.setRating(null);
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceTwo.setRating(null);
		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setRating(5);
		feedbackResourceTwo.setRating(5);

		feedbackResourceOne.setComment(null);
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceTwo.setComment(null);
		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setComment("great");
		feedbackResourceTwo.setComment("great");

		feedbackResourceOne.setCreatedTime(null);
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceTwo.setCreatedTime(null);
		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setCreatedTime(date);
		feedbackResourceTwo.setCreatedTime(date);
	}

	@Test
	public void testDifference() throws ParseException {
		feedbackResourceTwo.setSource("ysh123");
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));

	}

	@Test
	public void testEquality() throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse("2019-01-19");

		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
		assertTrue(feedbackResourceOne.hashCode() == feedbackResourceTwo.hashCode());

		feedbackResourceOne.setUsername("user123");
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setUsername("jd123");

		feedbackResourceOne.setComment("good");
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setComment("great");

		feedbackResourceOne.setRating(1);
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setRating(5);

		feedbackResourceOne.setSource("yash");
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setSource("John Deere");

		Date createdTime = format.parse("2019-01-01");
		feedbackResourceOne.setCreatedTime(createdTime);
		assertFalse(feedbackResourceOne.equals(feedbackResourceTwo));
		feedbackResourceOne.setCreatedTime(date);

	}
	
	@Test
	public void shouldReturnNullObject() {
		
		feedbackResourceOne.setComment(null);
		feedbackResourceOne.setCreatedTime(null);
		feedbackResourceOne.setRating(null);
		feedbackResourceOne.setSource(null);
		feedbackResourceOne.setUsername(null);
		
		feedbackResourceTwo.setComment(null);
		feedbackResourceTwo.setCreatedTime(null);
		feedbackResourceTwo.setRating(null);
		feedbackResourceTwo.setSource(null);
		feedbackResourceTwo.setUsername(null);
		
		
		assertTrue(feedbackResourceOne.hashCode() == feedbackResourceOne.hashCode());
		assertTrue(feedbackResourceOne.equals(feedbackResourceTwo));
	
	}

}
