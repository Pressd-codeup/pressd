package com.codeup.pressd.models;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(columnDefinition = "TEXT", length = 3000, nullable = false)
	private String body;

	@ManyToOne()
	@JoinColumn(name = "to_id")
	private User sentFrom;

	@ManyToOne()
	@JoinColumn(name = "from_id")
	private User sentTo;

	public Message() {
	}

	public Message(String body, User sentFrom, User sentTo){
		this.body = body;
		this.sentFrom = sentFrom;
		this.sentTo = sentTo;
	}

	public Message(String body) {
		this.body = body;
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
