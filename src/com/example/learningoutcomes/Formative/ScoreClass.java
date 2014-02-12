package com.example.learningoutcomes.Formative;

public class ScoreClass {

	private String name;
	private int score;
	private int maxScore;

	public ScoreClass(String name, int score, int maxScore) {
		this.name = name;
		this.score = score;
		this.maxScore = maxScore;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getMaxScore() {
		return maxScore;
	}
}
