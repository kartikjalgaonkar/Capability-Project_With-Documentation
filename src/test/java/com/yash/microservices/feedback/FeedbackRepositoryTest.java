package com.yash.microservices.feedback;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class FeedbackRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    FeedbackRepository repository;

    @Test
    public void should_find_no_feedback_if_repository_is_empty() {
        Iterable<Feedback> feedback = repository.findAllByOrderByIdDesc();

        assertThat(feedback).isEmpty();
    }

    @Test
    public void should_store_a_feedback() {
        Feedback feedback = repository.save(new Feedback(null, "SM75719", "My Application", 5, "This is a test.", null));

        assertThat(feedback).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(feedback).hasFieldOrPropertyWithValue("username", "SM75719");
        assertThat(feedback).hasFieldOrPropertyWithValue("source", "My Application");
        assertThat(feedback).hasFieldOrPropertyWithValue("rating", 5);
        assertThat(feedback).hasFieldOrPropertyWithValue("comment", "This is a test.");
        assertThat(feedback.getCreatedTime()).isNotNull();
        assertThat(feedback.getCreatedTime()).isEqualToIgnoringMinutes(new Date());
    }

    @Test
    public void should_find_all_feedback() {
        Feedback feedback1 = repository.save(new Feedback(null, "SM75719", "My Application 1", 5, "This is a test 1.", null));
        Feedback feedback2 = repository.save(new Feedback(null, "SM75729", "My Application 2", 4, "This is a test 2.", null));
        Feedback feedback3 = repository.save(new Feedback(null, "SM75739", "My Application 3", 3, "This is a test 3.", null));

        List<Feedback> feedback = repository.findAllByOrderByIdDesc();

        assertThat(feedback).hasSize(3).contains(feedback1, feedback2, feedback3);
        assertThat(feedback.get(0)).isEqualTo(feedback3);
    }

    @Test
    public void should_find_feedback_by_id() {
        Feedback feedback1 = repository.save(new Feedback(null, "SM75719", "My Application 1", 5, "This is a test 1.", null));
        Feedback feedback2 = repository.save(new Feedback(null, "SM75729", "My Application 2", 4, "This is a test 2.", null));

        Optional<Feedback> foundFeedback = repository.findById(feedback2.getId());

        assertThat(Optional.ofNullable(foundFeedback)).isNotEmpty();
        assertThat(foundFeedback.get()).isEqualTo(feedback2);
    }

}
