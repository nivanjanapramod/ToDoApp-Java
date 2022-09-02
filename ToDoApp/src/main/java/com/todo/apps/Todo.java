package com.todo.apps;

public class Todo {
	public Todo(int id, String title, String description, int completed) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.completed = completed;
	}
	public Todo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Todo(String title, String description, int completed, int userid) {
		super();
		this.setUserid(userid);
		this.title = title;
		this.description = description;
		this.completed = completed;
	}
	public Todo(int id, String title, String description, int completed, int userid) {
		super();
		this.setUserid(userid);
		this.id = id;
		this.title = title;
		this.description = description;
		this.completed = completed;
	}
	private int id;
	private String title;
	private String description;
	private int completed;
	private int userid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int isCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
}
