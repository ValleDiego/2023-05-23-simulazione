package it.polito.tdp.baseball.model;

public class Arco {
	private People player1;
	private People player2;
	
	
	public Arco(People player1, People player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
	}


	public People getPlayer1() {
		return player1;
	}


	public void setPlayer1(People player1) {
		this.player1 = player1;
	}


	public People getPlayer2() {
		return player2;
	}


	public void setPlayer2(People player2) {
		this.player2 = player2;
	}
	
	
	
	
}