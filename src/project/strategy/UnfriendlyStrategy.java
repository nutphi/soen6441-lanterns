package project.strategy;

import java.util.ArrayList;
import java.util.Queue;

import project.Color;
import project.Game;
import project.Player;


public abstract class UnfriendlyStrategy implements Strategy{
	ArrayList<Integer> solution = new ArrayList<Integer>();
	int[] ExchangeOptions = new int[2];
	
	/**
	 * This method check the input the user provides
	 * @param number_options available for the user input
	 * @param status name of the option menu
	 * @param game instance of the game
	 * @return integer value of the option selected
	 */
	public int inputOption(int number_options, Strategy.Name status, Game game)
	{
		Game gameClone = game.clone();
		
		Queue<Player> players = gameClone.getPlayers();
		ArrayList<Player> playerList = new ArrayList<Player>(players);
        Player player = playerList.get(0);
        
		Color c ;
		
		int in = 0;
		boolean validation = false;
		do
		{
			for (int i = 0; i < number_options; i++)
			{
				if (in ==  i)
				{
					validation = true;
					if(status == Name.MAINMENU){
						
						if(canMakeDedication(players)){
							in = 2;
						}
						else if(canExchange(players, gameClone)){
							ExchangeOptions = performExchange(player,gameClone);
							in = 1;
						}
						else {
							try {
								
								solution = simulateGamePlay(game);
								in = 3;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					else if(status == Name.CHOOSE_LANTERN_HAND){
						in = ExchangeOptions[0];
					}
					else if(status == Name.CHOOSE_LANTERN_SUPPLY){
						in = ExchangeOptions[1];
					}
					else if(status == Name.MAKE_DEDICATION){
						int choice = whichDedication(players);
						in = choice;
					}
					else if(status == Name.SELECT_LAKE){
						in =  solution.get(0);
					}
					else if(status == Name.SELECT_BOARD_POSITION){
						in = solution.get(1);
					}
					else if(status == Name.SELECT_LAKE_ROTATION){
						in = solution.get(2);
					}
				}
			}
		}
		while (!validation);
		System.out.println(in);
		return in;
	
	}
	
	protected abstract ArrayList<Integer> simulateGamePlay(Game game) throws Exception;
	protected abstract boolean canMakeDedication(Queue<Player> players);
	protected abstract int whichDedication(Queue<Player> players);
	protected abstract boolean canExchange(Queue<Player> players, Game game);
	protected abstract int[] performExchange(Player player,Game game);
}
