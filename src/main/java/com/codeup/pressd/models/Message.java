package com.codeup.pressd.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;


	@Column(columnDefinition = "TEXT", nullable = false)
	private String body;

	@Column(name = "date_posted", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime datePosted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_id")
	private User sentFrom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_id")
	private User sentTo;

	public Message() {
	}

	public Message(long id, String body, LocalDateTime datePosted, User sentFrom, User sentTo) {
		this.id = id;
		this.body = body;
		this.datePosted = datePosted;
		this.sentFrom = sentFrom;
		this.sentTo = sentTo;
	}

	public Message(String body, LocalDateTime datePosted, User sentFrom, User sentTo) {
		this.body = body;
		this.datePosted = datePosted;
		this.sentFrom = sentFrom;
		this.sentTo = sentTo;
	}

	public User getSentTo() {
		return sentTo;
	}

	public void setSentTo(User sentTo) {
		this.sentTo = sentTo;
	}

	public User getSentFrom() {
		return sentFrom;
	}

	public void setSentFrom(User sentFrom) {
		this.sentFrom = sentFrom;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
