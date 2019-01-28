package com.yash.microservices.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feedback", schema = "feedback_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback implements Identifiable<Long> {
   

	
	@Id
	@SequenceGenerator(name="SEQ_GEN", sequenceName="feedback_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;

    private String username;
    private String source;
    private Integer rating;
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = new Date();
    }

    @Override
    public Long getId() {
        return this.id;
    }
    
}
