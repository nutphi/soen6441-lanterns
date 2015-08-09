package project.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.Random;

import project.Color;
import project.Game;
import project.LakeTile;
import project.LanternCard;
import project.PlayArea;
import project.Player;
import project.Position;
import project.Rotation;
import project.Supply;

public class Unfriendliness extends UnfriendlyStrategy{
    
	/**
	 * it is used to keep the correct version
	 */
	private static final long serialVersionUID = 7175461946886588653L;
	int lakeTiletochoose = 0;
    
    /**
     * to check if the player can make a dedication or not for the
     * current situation
     * @param players Queue of all the players
     * @return flag true if make a dedication is possible, false otherwise
     */
    protected boolean canMakeDedication(Queue<Player> players){
        boolean flag;
        ArrayList<Player> playerList = new ArrayList<Player>(players);
        Player player = playerList.get(0);
        if(player.isFourOfAKind() || player.isThreePair() || player.isSevenUnique()){
            //System.out.println("Dedication is possible to make");
            flag = true;
        }
        else{
           // System.out.println("Dedication is not possible");
            flag=false;
        }
        return flag;
    }
    
    /**
     * check which dedication is possible to make, and return the choice based on the possibilities
     * @param players queue of all the players
     * @return choice number based on the possible dedication 
     */
    protected int whichDedication(Queue<Player> players){
        int choice=0;
        ArrayList<Player> playerList = new ArrayList<Player>(players);
        Player player = playerList.get(0);
        if(player.isSevenUnique()){
            choice = 2;
        }
        else if(player.isFourOfAKind()){
            choice = 0;
        }
        else if(player.isThreePair()){
            choice = 1;
        }
        
        return choice;
    }
    
    /**
     * To check if lantern card exchange is possible for a player
     * @param players queue of all the players
     * @param game instance of the game class
     * @return true if the exchange is possible, false otherwise 
     */
    protected boolean canExchange(Queue<Player> players, Game game){
        boolean flag;
        int[] ExchnageOptions = new int[2];
        ArrayList<Player> playerList = new ArrayList<Player>(players);
        Player player = playerList.get(0);
        
        ExchnageOptions = performExchange(player,game);

        if(player.getNumberOfFavorTokens() > 2 && (ExchnageOptions[0] < 9 && ExchnageOptions[1] < 9)){
            //System.out.println("Exchange a lantern card is possible");
            flag = true;
        }
        else{
           // System.out.println("Can not Exchange lantern card");
            flag=false;
        }
        return flag;
        
    }
    
    /**
     * to perform an exchange of a lantern card, to get all the lantern cards on a player hand
     * and put it in a HashMap
     * @param player current player
     * @param game clone of the game instance
     * @return pair of solution to be selected for the exchange of a lantern card
     */
    protected int[] performExchange(Player player,Game game){
        boolean DESC = false;
        int[] ExchnageOptions = new int[2];
        
        int ORANGE = player.numOfCardColor(Color.ORANGE);
        int GREEN = player.numOfCardColor(Color.GREEN);
        int PURPLE = player.numOfCardColor(Color.PURPLE);
        int WHITE = player.numOfCardColor(Color.WHITE);
        int BLUE = player.numOfCardColor(Color.BLUE);
        int RED = player.numOfCardColor(Color.RED);
        int BLACK = player.numOfCardColor(Color.BLACK);
        
         HashMap<Color,Integer> colors = new HashMap<Color,Integer>();
          // Put elements to the map
         colors.put(Color.ORANGE, ORANGE);
         colors.put(Color.GREEN, GREEN);
         colors.put(Color.PURPLE, PURPLE);
         colors.put(Color.WHITE, WHITE);
         colors.put(Color.BLUE, BLUE);
         colors.put(Color.RED, RED);
         colors.put(Color.BLACK, BLACK);
         
            
         Map<Color,Integer> sortedMap = sortByComparator(colors, DESC);
         //System.out.println(sortedMap);
         ExchnageOptions = ExchangeLanternCard(sortedMap,player,game);

         return ExchnageOptions;
    }
    
    /**
     * Decide which lantern card to exchange with which while performing an exchange
     * @param sortedMap sorted HashMap of color and value pair
     * @param player current player
     * @param game clone of the game instance
     * @return pair of solution to be selected for the exchange of a lantern card
     */
    protected int[] ExchangeLanternCard( Map<Color, Integer> sortedMap, Player player, Game game){
        int[] colors = new int[7];
        Color[] c = new Color[7];
        int[] ExchnageOptions = new int[2];
        int counter = 0;
         for (Entry<Color, Integer> entry : sortedMap.entrySet())
         {
            colors[counter] = entry.getValue();
            c[counter] = entry.getKey();
            counter++;
         }

        if(player.getLanternCards().size() >= 7)
         {
             if(colors[0] == 2 && colors[1] == 2 && colors[2] == 1 && colors[3] == 1 && colors[4] == 1 && player.getNumberOfFavorTokens() >= 4){
            	if(checkSupply(c[5],game)){
            		ExchnageOptions[0] = ChoosePlayerLanternCard(c[0],game,player);
            		ExchnageOptions[1] = ChooseSupplyLanternCard(c[5],game);
            	}
            	else{
            		ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
            	}
             }
             else if(colors[0] == 2 && colors[1] == 1 && colors[2] == 1 && colors[3] == 1 && colors[4] == 1 && colors[5] == 1 && player.getNumberOfFavorTokens() >= 2){
            	 if(checkSupply(c[6],game)){
	            	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[0],game,player);
	            	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[6],game);
            	 }
             	else{
             		ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             	}
             }
             else if(colors[0] == 3 && colors[1] == 2 && colors[2] == 1 && colors[3] == 1 && player.getNumberOfFavorTokens() >= 2){
            	 if(checkSupply(c[0],game)){
	            	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[2],game,player);
	            	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[0],game);
            	 }
             	 else{
             		ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             	 }
             }
             else{
            	 ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             }
         }
         else if(player.getLanternCards().size() <= 4){
             if(colors[0] == 3 && colors[1] == 1 && player.getNumberOfFavorTokens() >= 2){
            	 if(checkSupply(c[0],game)){
	            	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[1],game,player);
	            	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[0],game);
            	 }
             	 else{
	             	 ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             	 }
             }
             else if(colors[0] == 2 && colors[1] == 2 && player.getNumberOfFavorTokens() >= 4){
            	 if(checkSupply(c[0],game)){
	            	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[1],game,player);
	            	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[0],game);
            	 }
             	 else{
             		 ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             	 }
             }
             else if(colors[0] == 2 && colors[1] == 1 && colors[2] == 1 && player.getNumberOfFavorTokens() >= 4){
            	 if(checkSupply(c[0],game)){
	            	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[2],game,player);
	            	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[0],game);
            	 }
             	 else{
             		 ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             	 }
             }
             else{
            	 ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             }
         }
         else{
             if(colors[0] == 2 && colors[1] == 2 && colors[2] == 1 && colors[3] == 1 && player.getNumberOfFavorTokens() >= 2){
            	 if(checkSupply(c[2],game)){
            	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[3],game,player);
            	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[2],game);
            	 }
             	 else{
             		 ExchnageOptions[0] = 9;ExchnageOptions[1] = 9;
             	 }
             }
             else if(colors[0] == 2 && colors[1] == 1 && colors[2] == 1 && colors[3] == 1 && colors[4] == 1 && player.getNumberOfFavorTokens() >= 4){
            	 if(checkSupply(c[1],game)){
            		 ExchnageOptions[0] = ChoosePlayerLanternCard(c[4],game,player);
            		 ExchnageOptions[1] = ChooseSupplyLanternCard(c[1],game);
            	 }
             	 else{
             		 ExchnageOptions[0] = 9;ExchnageOptions[1] = 9;
             	 }
             }
             else{
            	 ExchnageOptions[0] = 9; ExchnageOptions[1] = 9;
             }
         }
        return ExchnageOptions;
    }
    
    /**
     * select a lantern card from player stack to exchange
     * @param c color to exchange
     * @return option number to input
     */
    protected int ChoosePlayerLanternCard(Color cl,Game game, Player player){
    	
    	int PlayerCardOptionNumber=0;
		ArrayList<LanternCard> lanternCards = player.getLanternCards();

		ArrayList<LanternCard> arrays = new ArrayList<LanternCard>();
		
		for (int i = 0, counter = 0; i < lanternCards.size(); i++) 
		{
			boolean existColor = false;
			for (LanternCard array : arrays) {
				if (array.getColor().equals(lanternCards.get(i).getColor())) 
				{
					existColor = true;
					break;
				}
			}
			if (!existColor) 
			{
				arrays.add(lanternCards.get(i));
				if(lanternCards.get(i).getColor() == cl){
					PlayerCardOptionNumber = counter;
				}
				counter++;
			}
		}
        return PlayerCardOptionNumber;
    }
    
    /**
     * select a lantern card to exchange from supply
     * @param c color to exchange
     * @return option number to input
     */
    protected int ChooseSupplyLanternCard(Color cl, Game game){
        Supply supply = game.getPlayArea().getSupply();
        ArrayList<Color> buffer = new ArrayList<Color>();
        int cardOptionNumber=0;
        int i = 0;
        for (Color color : Color.values()) 
        {
            try {
                if (supply.get(color).size() > 0) 
                {
                    if(cl == color)
                    {
                        cardOptionNumber = i;
                        buffer.add(color);
                    }
                    i++;
                }
            } catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        return cardOptionNumber;
    }
    
    /**
     * check lantern card supply to make sure the lantern card a player wants to exchange is
     * available in the supply stack otherwise return false
     * @param cl color to search for in the supply stack
     * @param game clone instance of game class
     * @return flag true if the color is available , false otherwise
     */
    private boolean checkSupply(Color cl, Game game){
    	boolean flag = false;
    	Supply supply = game.getPlayArea().getSupply();
        for (int i =0; i<Color.values().length; i++) 
        {
            try {
            	if(supply.get(cl).size() > 0){
            		flag = true;
            	}
                else{
                	flag = false;
                }

            } catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        return flag;
    }
    
    /**
     * check all possible solutions for each three steps of place a lake tile on the board and return
     * best solution to put a lake tile
     * @param game clone instance of game class 
     * @throws Exception this color does not exist exception
     */
    protected ArrayList<Integer> simulateGamePlay(Game game) throws Exception
    {
        ArrayList<Integer> ithSolution = new ArrayList<Integer>();
        ArrayList<Integer> jthSolution = new ArrayList<Integer>();
        ArrayList<Integer> kthSolution = new ArrayList<Integer>();
        ArrayList<Integer> solution = new ArrayList<Integer>(3);
        Queue<Player> realplayers = game.getPlayers();
        PlayArea realplayarea = game.getPlayArea();
        
        ArrayList<Player> realplayerList = new ArrayList<Player>(realplayers);
        Player realplayer = realplayerList.get(0);

        ArrayList<Position> realavailableList = realplayarea.getPositionAvailableLakeTileOnBoard();

        for(int i=0;i<realplayer.getLakeTiles().size();i++)
        {
           // System.out.println(" value of i : " + i);
            for(int j=0; j<realavailableList.size();j++)
            {
                //System.out.println(" value of j : " + j);
                for(int k=0;k<4;k++)
                {
                    Game gameObject = game.clone();
                    
                    Queue<Player> players = gameObject.getPlayers();
                    PlayArea playarea = gameObject.getPlayArea();
                    
                    ArrayList<Player> playerList = new ArrayList<Player>(players);
                    Player player = playerList.get(0);
                    LakeTile active_laketile = player.getLakeTiles().get(i);
                    //System.out.println(" value of k : " + k);
                    if(k>=1){
                        active_laketile.changeRotation(Rotation.D90);
                    }

                    if(checkdistributeLanternCard(active_laketile, playarea.getSupply(),gameObject)){
                    	ithSolution.add(i);
                        jthSolution.add(j);
                        kthSolution.add(k);
                    }
                    
                    
                }

            }
        }
        //System.out.println("ith : "+ithSolution);
        //System.out.println("jth : "+jthSolution);
        //System.out.println("kth : "+ kthSolution);
        
        int index =0;
        if(ithSolution.size() >0){
        	index = randInt(1,ithSolution.size()-1);
        	solution.add(ithSolution.get(index));
        	solution.add(jthSolution.get(index));
        	solution.add(kthSolution.get(index));
        	solution.add(index);
        }
        if(solution.size()==0){
            solution.add(0);
            solution.add(0);
            solution.add(0);
            solution.add(0);
        }

        //System.out.println("Best Solution is : I :" + solution.get(0)+" J : " +solution.get(1)+" K : "+solution.get(2)+" with value : " +solution.get(3));
        return solution;
    }
    
    /**
     * To pick random value from a given array
     * @param array
     * @return
     */
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    /**
     * This method distribute lantern card from supply to player
     * @param active_laketile lake tile from the stack
     * @param lanternStacks lantern card stack 
     */

    public boolean checkdistributeLanternCard(LakeTile active_laketile, Supply supply, Game game) 
    {
        Queue<Player> players = game.getPlayers();
		int valueCounter =0;
		boolean flag = false;
		
		ArrayList<Player> players_list = new ArrayList<Player>(players);
		
		for (int i = 0; i < players.size(); i++) 
		{
			if(i!=0){
				Player getting_player = players_list.get(i);
				
				int index = getting_player.getIndex();
				
				ArrayList<Color> color_list = new ArrayList<Color>(active_laketile.getColorOfFourSides());
				
				if (index >= 0 && index < players.size()) 
				{
					Stack<LanternCard> lanternCard = supply.get(color_list.get(index));
					
					if (!lanternCard.empty())
					{
						LanternCard l = lanternCard.pop();
						getting_player.getLanternCards().add(l);
					}
				}
				if(getting_player.isFourOfAKind() || getting_player.isSevenUnique() || getting_player.isThreePair()){
					//System.out.println(getting_player.getName()+" can make dedication ");
					valueCounter = valueCounter +1;
				}
				else{
					//System.out.println(getting_player.getName()+" can not make dedication ");
				}
			}
		}
		if(valueCounter == 0){
			flag = true;
		}
		return flag;
        
    }
    

    
    /**
     * perform sorting on the HashMap of Lantern cards
     * @param unsortMap unsorted map
     * @param order sorting order
     * @return sorted map
     */
    private static Map<Color, Integer> sortByComparator(Map<Color, Integer> unsortMap, final boolean order)
        {

        List<Entry<Color, Integer>> list = new LinkedList<Entry<Color, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Color, Integer>>()
        {
            public int compare(Entry<Color, Integer> o1,
                    Entry<Color, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Color, Integer> sortedMap = new LinkedHashMap<Color, Integer>();
        for (Entry<Color, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}