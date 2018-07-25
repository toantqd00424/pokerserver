package com.fsc.pokerserver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Deck;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

public class EndGameTest {
	Player master;
	Room room;

	@Before
	public void setUp() throws Exception {
		master = new Player("Room master");
		room = new Room(master, BlindLevel.BLIND_10_20);
		
	}

	/*--------------------- End Game -----------------------*/
	
	/*  
	 * Error order number from player Check, bet, fold before end game.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckBetFoldBeforeEndGame() {
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
		game.addPlayer(player5);

		game.setDealer(player5);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
		master.check();
		player2.bet(20);
		player5.bet(20);
		master.fold();
		
		game.river();
		player2.bet(20);
//		player5 need bet before player2
		player2.fold();
		player5.bet(30);
		
	}
	
	
	/*  
	 * call end game although current round bet not equal
	 * */
	@Test(expected = AssertionError.class)
	public void testCallEndGameAlthoughCurrentRoundBetNotEqual() {
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
		game.addPlayer(player5);

		game.setDealer(player5);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
		master.check();
		player2.bet(20);
		player5.bet(20);
		master.fold();
		
		game.river();
		player2.bet(20);
		player5.bet(30);
		player2.bet(20);
		
		game.endGame();
		
	}
	
	/*  
	 * Get pot from game after end game.
	 * */
	@Test
	public void testGetPotAfterEndGame() {
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
		game.addPlayer(player5);

		game.setDealer(player5);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
		master.check();
		player2.bet(20);
		player5.bet(20);
		master.fold();
		
		game.river();
		player2.bet(20);
		player5.bet(30);
		player2.fold();
		
		game.endGame();
		
		assertEquals(game.getPotBalance(), 240);
	}
	
	/*  
	 * Get cards from game after end game.
	 * */
	@Test
	public void testGetCardsAfterEndGame() {
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
		game.addPlayer(player5);

		game.setDealer(player5);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
		master.check();
		player2.bet(20);
		player5.bet(20);
		master.fold();
		
		game.river();
		player2.bet(20);
		player5.bet(30);
		player2.fold();
		
		game.endGame();
		
		assertEquals(game.getBoard().getCardNumber(),5);
	} 
	
	
}
