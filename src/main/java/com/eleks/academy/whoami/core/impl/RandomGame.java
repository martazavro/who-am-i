package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {
	
	private Map<String, String> playersCharacter = new HashMap<>();
	private List<Player> players = new ArrayList<>();
	private List<String> availableCharacters;
	private Turn currentTurn;

	
	private final static String YES = "Yes";
	private final static String NO = "No";

	static {System.out.println("Let's begin!\n");}



	public RandomGame(List<String> availableCharacters) {
		this.availableCharacters = new ArrayList<String>(availableCharacters);
	}

	@Override
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	@Override
	public boolean makeTurn() {
		Player currentGuesser = currentTurn.getGuesser();
		Set<String> answers;
		if (currentGuesser.isReadyForGuess()) {
			String guess = currentGuesser.getGuess();
			answers = currentTurn.getOtherPlayers().stream()
					.map(player -> player.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName())))
					.collect(Collectors.toSet());
			long positiveCount = 0L;
			for (String answer : answers) {
				if (YES.equals(answer)) {
					positiveCount++;
				}
			}
			long negativeCount = 0L;
			for (String a : answers) {
				if (NO.equals(a)) {
					negativeCount++;
				}
			}

			boolean win = positiveCount > negativeCount;
			
			if (win) {
				players.remove(currentGuesser);
			}
			return win;
			
		} else {
			String question = currentGuesser.getQuestion();
			answers = currentTurn.getOtherPlayers().stream()
				.map(player -> player.answerQuestion(question, this.playersCharacter.get(currentGuesser.getName())))
				.collect(Collectors.toSet());
			long positiveCount = 0L;
			for (String answer : answers) {
				if (YES.equals(answer)) {
					positiveCount++;
				}
			}
			long negativeCount = 0L;
			for (String a : answers)
				if (NO.equals(a)) {
					negativeCount++;
				}
			return positiveCount > negativeCount;
		}
		
	}

	@Override
	public void assignCharacters() {
		players.stream().forEach(player -> this.playersCharacter.put(player.getName(), this.getRandomCharacter()));
		
	}



	@Override
	public int countPlayers() {
		return players.size();
	}

	@Override
	public boolean isFinished() {
		return players.size() == 1;
	}
	
	private String getRandomCharacter() {
		int randomPos = (int)(Math.random() * this.availableCharacters.size()); 
		return this.availableCharacters.remove(randomPos);
	}

	@Override
	public void changeTurn() {
		this.currentTurn.changeTurn();
	}

	@Override
	public void initGame() {

	}

}
