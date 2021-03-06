package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.junit.Test;

import project.Color;
import project.DedicationToken;
import project.FourOfAKindToken;
import project.Game;
import project.LanternCard;
import project.SevenUniqueToken;
import project.ThreePairToken;
import project.rule.Base;
import project.rule.Rule;

public class GameTest {

	@Test
	public void testTwoPlayersInGame() throws Exception {
		String[] player_names = {"Player1","Player2"};
		int[] strategies = {0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		assertEquals("not 2 Players in the game ", 2, g.getPlayers().size());
	}
	
	@Test
	public void testThreePlayersInGame() throws Exception {
		String[] player_names = {"Player1","Player2","Player3"};
		int[] strategies = {0,0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		assertEquals("not 3 Players in the game ", 3, g.getPlayers().size());
	}
	
	@Test
	public void testFourPlayersInGame() throws Exception {
		String[] player_names = {"Player1","Player2","Player3", "Player4"};
		int[] strategies = {0,0,0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		assertEquals("not 4 Players in the game ", 4, g.getPlayers().size());
	}
	
	@Test
	public void testWinnerInGame() throws Exception{
		String[] player_names = {"A","A","A", "A"};
		int[] strategies = {0,0,0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		FourOfAKindToken four = new FourOfAKindToken(8);
		ArrayList<DedicationToken> dedicationList = new ArrayList<DedicationToken>() ;
		dedicationList.add(four);
		g.getPlayers().element().setDedicationTokens(dedicationList);
		assertTrue(g.getTheWinner().equals("The winner is A with 8 honor value, 0 favor, and 1 lantern card"));
	}
	
	@Test
	public void testTwoWinnersInGame() throws Exception{
		String[] player_names = {"A","A","A", "A"};
		int[] strategies = {0,0,0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		FourOfAKindToken dedicationToken1 = new FourOfAKindToken(8); // 8 value
		SevenUniqueToken dedicationToken2 = new SevenUniqueToken(4); // 8 value
		ArrayList<DedicationToken> dedicationList1 = new ArrayList<DedicationToken>() ;
		ArrayList<DedicationToken> dedicationList2 = new ArrayList<DedicationToken>() ;
		dedicationList1.add(dedicationToken1);
		g.getPlayers().element().setDedicationTokens(dedicationList1);
		g.getPlayers().add(g.getPlayers().remove());
		dedicationList2.add(dedicationToken2);
		g.getPlayers().element().setDedicationTokens(dedicationList2);
		assertTrue(g.getTheWinner().equals("The winners are A and A with 8 honor value, 0 favor, and 1 lantern card"));
	}
	
	@Test
	public void testThreeWinnersInGame() throws Exception{
		String[] player_names = {"A","A","A", "A"};
		int[] strategies = {0,0,0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		FourOfAKindToken dedicationToken1 = new FourOfAKindToken(8); // 8 value
		SevenUniqueToken dedicationToken2 = new SevenUniqueToken(4); // 8 value
		ThreePairToken dedicationToken3 = new ThreePairToken(7); // 8 value
		ArrayList<DedicationToken> dedicationList1 = new ArrayList<DedicationToken>() ;
		ArrayList<DedicationToken> dedicationList2 = new ArrayList<DedicationToken>() ;
		ArrayList<DedicationToken> dedicationList3 = new ArrayList<DedicationToken>() ;
		dedicationList1.add(dedicationToken1);
		g.getPlayers().element().setDedicationTokens(dedicationList1);
		g.getPlayers().add(g.getPlayers().remove());
		dedicationList2.add(dedicationToken2);
		g.getPlayers().element().setDedicationTokens(dedicationList2);
		g.getPlayers().add(g.getPlayers().remove());
		dedicationList3.add(dedicationToken3);
		g.getPlayers().element().setDedicationTokens(dedicationList3);
		assertTrue(g.getTheWinner().equals("The winners are A, A and A with 8 honor value, 0 favor, and 1 lantern card"));
	}
	
	@Test
	public void testOneWinnerInGameSameHonorValue() throws Exception{
		String[] player_names = {"A","A","A", "A"};
		int[] strategies = {0,0,0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		FourOfAKindToken dedicationToken1 = new FourOfAKindToken(8); // 8 value
		SevenUniqueToken dedicationToken2 = new SevenUniqueToken(4); // 8 value
		ThreePairToken dedicationToken3 = new ThreePairToken(7); // 8 value
		ArrayList<DedicationToken> dedicationList1 = new ArrayList<DedicationToken>() ;
		ArrayList<DedicationToken> dedicationList2 = new ArrayList<DedicationToken>() ;
		ArrayList<DedicationToken> dedicationList3 = new ArrayList<DedicationToken>() ;
		dedicationList1.add(dedicationToken1);
		g.getPlayers().element().setDedicationTokens(dedicationList1);
		g.getPlayers().element().setNumberOfFavorTokens(4);
		g.getPlayers().add(g.getPlayers().remove());
		dedicationList2.add(dedicationToken2);
		g.getPlayers().element().setDedicationTokens(dedicationList2);
		g.getPlayers().add(g.getPlayers().remove());
		dedicationList3.add(dedicationToken3);
		
		g.getPlayers().element().setDedicationTokens(dedicationList3);
		assertTrue(g.getTheWinner().equals("The winner is A with 8 honor value, 4 favor, and 1 lantern card"));
	}
	
	@Test
	public void testOneWinnerInGameSameHonorValueAndFavor() throws Exception{
		String[] player_names = {"A","A","A", "A"};
		int[] strategies = {0,0,0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);
		FourOfAKindToken dedicationToken1 = new FourOfAKindToken(8); // 8 value
		SevenUniqueToken dedicationToken2 = new SevenUniqueToken(4); // 8 value
		ThreePairToken dedicationToken3 = new ThreePairToken(7); // 8 value
		ArrayList<DedicationToken> dedicationList1 = new ArrayList<DedicationToken>() ;
		ArrayList<DedicationToken> dedicationList2 = new ArrayList<DedicationToken>() ;
		ArrayList<DedicationToken> dedicationList3 = new ArrayList<DedicationToken>() ;
		LanternCard lancard = new LanternCard(Color.GREEN);
		dedicationList1.add(dedicationToken1);
		g.getPlayers().element().setDedicationTokens(dedicationList1);
		g.getPlayers().element().setNumberOfFavorTokens(4);
		g.getPlayers().element().getLanternCards().add(lancard);
		g.getPlayers().add(g.getPlayers().remove());
		dedicationList2.add(dedicationToken2);
		g.getPlayers().element().setDedicationTokens(dedicationList2);
		g.getPlayers().element().setNumberOfFavorTokens(4);
		g.getPlayers().add(g.getPlayers().remove());
		dedicationList3.add(dedicationToken3);
		
		g.getPlayers().element().setDedicationTokens(dedicationList3);
		assertTrue(g.getTheWinner().equals("The winner is A with 8 honor value, 4 favor, and 2 lantern card"));
	}
	
	@Test
	public void testNumberOfLanternCardsOnHandsOver() throws Exception{
		String[] player_names = {"Player1","Player2"};
		int[] strategies = {0,0};
		Rule rule = new Base();
		Game g = new Game(player_names , strategies, rule, null);//get one lantern card from start lake tile
		ArrayList<LanternCard> playercard = g.getPlayers().element().getLanternCards();
		HashMap<Color, Stack<LanternCard>> lanternstacks  = g.getPlayArea().getSupply();
		LanternCard greenlantern1 = lanternstacks.get(Color.GREEN).pop();
		LanternCard greenlantern2 = lanternstacks.get(Color.GREEN).pop();
		LanternCard bluelantern1 = lanternstacks.get(Color.BLUE).pop();
		LanternCard bluelantern2 = lanternstacks.get(Color.BLUE).pop();
		LanternCard redlantern1 = lanternstacks.get(Color.RED).pop();
		LanternCard redlantern2 = lanternstacks.get(Color.RED).pop();
		LanternCard whitelantern1 = lanternstacks.get(Color.WHITE).pop();
		LanternCard whitelantern2 = lanternstacks.get(Color.WHITE).pop();
		LanternCard orangelantern1 = lanternstacks.get(Color.ORANGE).pop();
		LanternCard orangelantern2 = lanternstacks.get(Color.ORANGE).pop();
		LanternCard blacklantern1 = lanternstacks.get(Color.BLACK).pop();
		LanternCard blacklantern2 = lanternstacks.get(Color.BLACK).pop();
		playercard.add(greenlantern1);
		playercard.add(greenlantern2);
		playercard.add(bluelantern1);
		playercard.add(bluelantern2);
		playercard.add(redlantern1);
		playercard.add(redlantern2);
		playercard.add(whitelantern1);
		playercard.add(whitelantern2);
		playercard.add(orangelantern1);
		playercard.add(orangelantern2);
		playercard.add(blacklantern1);
		playercard.add(blacklantern2);
	}
}

