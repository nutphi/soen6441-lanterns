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
import java.util.Vector;
import java.util.Map.Entry;

import project.Color;
import project.Game;
import project.LakeTile;
import project.LanternCard;
import project.PlayArea;
import project.Player;
import project.Position;
import project.Rotation;
import project.Supply;
import project.strategy.Strategy.Name;

/**
 * This class represent the concrete class for the greedy player strategy, greedy player
 * always try to get more lantern cards, and to make dedication, so greedy player 
 * tries to choose a solution, which can get it more lantern cards and favor tokens. 
 * @author Nirav
 *
 */
public class Greed extends GreedyStrategy{
    /**
	 * it is used to keep the correct version
	 */
	private static final long serialVersionUID = 5692856303792375794L;
	private static final int fourOfaKind = 0;
	private static final int threePair = 1;
	private static final int sevenUnique = 2;
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
    protected int selectDedication(Queue<Player> players){
        int choice=0;
        ArrayList<Player> playerList = new ArrayList<Player>(players);
        Player player = playerList.get(0);
        if(player.isSevenUnique()){
            choice = sevenUnique;
        }
        else if(player.isFourOfAKind()){
            choice = fourOfaKind;
        }
        else if(player.isThreePair()){
            choice = threePair;
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

        if(player.getNumberOfFavorTokens() >= 2 && (ExchnageOptions[0] < 9 && ExchnageOptions[1] < 9)){
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
         
         
         //sort the map in descending order of the number of colors
         Map<Color,Integer> sortedMap = sortByComparator(colors, DESC);
         ExchnageOptions = ExchangeLanternCard(sortedMap,player,game);

         return ExchnageOptions;
    }
    
    /**
     * Decide which lantern card to exchange 
     * @param sortedMap sorted HashMap of color and value pair
     * @param player current player
     * @param game clone of the game instance
     * @return pair of solution to be selected for the exchange of a lantern card
     */
    protected int[] ExchangeLanternCard( Map<Color, Integer> sortedMap, Player player, Game game){
        int[] numOfLanterns = new int[7];
        Color[] c = new Color[7];
        int[] ExchnageOptions = new int[2];
        int counter = 0;
         for (Entry<Color, Integer> entry : sortedMap.entrySet())
         {
        	numOfLanterns[counter] = entry.getValue();
            c[counter] = entry.getKey();
            counter++;
         }

        if(player.getLanternCards().size() >= 7)
        {
        	ExchnageOptions = findSevenOfKindPattern(player, game, numOfLanterns, c, ExchnageOptions);
         }
         else if(player.getLanternCards().size() <= 4){
        	 ExchnageOptions = findFourOfKindPattern(player, game, numOfLanterns, c, ExchnageOptions);
         }
         else{
        	 ExchnageOptions = findThreePairPattern(player, game, numOfLanterns, c, ExchnageOptions);
         }
        return ExchnageOptions;
    }

    /**
     * Decide which lantern card to exchange to make a three Pair dedication
     * @param player current player
     * @param game Clone of the game instance
     * @param colors lantern cards color
     * @param c color of lantern card
     * @param ExchnageOptions Lantern cards to be exchanges options
     */
	private int[] findThreePairPattern(Player player, Game game, int[] colors,Color[] c, int[] ExchnageOptions) {
		 if(colors[0] == 2 && colors[1] == 2 && colors[2] == 1 && colors[3] == 1 && player.getNumberOfFavorTokens() >= 2){
			 if(checkSupply(c[2],game)){
			 ExchnageOptions[0] = ChoosePlayerLanternCard(c[3],game,player);
			 ExchnageOptions[1] = ChooseSupplyLanternCard(c[3],c[2],game,player);
			 }
		 	 else{
		 		 ExchnageOptions[0] = 9;
		     	 ExchnageOptions[1] = 9;
		 	 }
		 }
		 else if(colors[0] == 3 && colors[1] == 1 && colors[2] == 1 && player.getNumberOfFavorTokens() >= 2){
			 if(checkSupply(c[0],game)){
			 ExchnageOptions[0] = ChoosePlayerLanternCard(c[1],game,player);
			 ExchnageOptions[1] = ChooseSupplyLanternCard(c[1],c[0],game,player);
			 }
		 	 else{
		 		 ExchnageOptions[0] = 9;
		     	 ExchnageOptions[1] = 9;
		 	 }
		 }
		 else if(colors[0] == 3 && colors[1] == 2 && player.getNumberOfFavorTokens() >= 2){
			 if(checkSupply(c[0],game)){
			 ExchnageOptions[0] = ChoosePlayerLanternCard(c[1],game,player);
			 ExchnageOptions[1] = ChooseSupplyLanternCard(c[1],c[0],game,player);
			 }
		 	 else{
		 		 ExchnageOptions[0] = 9;
		     	 ExchnageOptions[1] = 9;
		 	 }
		 }
		 else if(colors[0] == 2 && colors[1] == 1 && colors[2] == 1 && colors[3] == 1 && colors[4] == 1 && player.getNumberOfFavorTokens() >= 4){
			 if(checkSupply(c[1],game)){
				 ExchnageOptions[0] = ChoosePlayerLanternCard(c[4],game,player);
				 ExchnageOptions[1] = ChooseSupplyLanternCard(c[4],c[1],game,player);
			 }
		 	 else{
		 		 ExchnageOptions[0] = 9;
		     	 ExchnageOptions[1] = 9;
		 	 }
		 }
		 else{
			 ExchnageOptions[0] = 9;
			 ExchnageOptions[1] = 9;
		 }
		 return ExchnageOptions;
	}
    /**
     * Decide which lantern card to exchange to make a Four of a Kind dedication
     * @param player current player
     * @param game Clone of the game instance
     * @param colors lantern cards color
     * @param c color of lantern card
     * @param ExchnageOptions Lantern cards to be exchanges options
     */

	private int[] findFourOfKindPattern(Player player, Game game, int[] colors,
			Color[] c, int[] ExchnageOptions) {
		if(colors[0] == 3 && colors[1] == 1 && player.getNumberOfFavorTokens() >= 2){
			 if(checkSupply(c[0],game)){
		    	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[1],game,player);
		    	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[1],c[0],game,player);
			 }
		 	 else{
		     	 ExchnageOptions[0] = 9;
		         ExchnageOptions[1] = 9;
		 	 }
		 }
		 else if(colors[0] == 2 && colors[1] == 2 && player.getNumberOfFavorTokens() >= 4){
			 if(checkSupply(c[0],game)){
		    	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[1],game,player);
		    	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[1],c[0],game,player);
			 }
		 	 else{
		 		 ExchnageOptions[0] = 9;
		     	 ExchnageOptions[1] = 9;
		 	 }
		 }
		 else if(colors[0] == 2 && colors[1] == 1 && colors[2] == 1 && player.getNumberOfFavorTokens() >= 4){
			 if(checkSupply(c[0],game)){
		    	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[2],game,player);
		    	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[2],c[0],game,player);
			 }
		 	 else{
		 		 ExchnageOptions[0] = 9;
		     	 ExchnageOptions[1] = 9;
		 	 }
		 }
		 else{
			 ExchnageOptions[0] = 9;
			 ExchnageOptions[1] = 9;
		 }
		return ExchnageOptions;
	}
    /**
     * Decide which lantern card to exchange to make a Seven Unique Dedication
     * @param player current player
     * @param game Clone of the game instance
     * @param colors lantern cards color
     * @param c color of lantern card
     * @param ExchnageOptions Lantern cards to be exchanges options
     */

	private int[] findSevenOfKindPattern(Player player, Game game, int[] colors,
			Color[] c, int[] ExchnageOptions) {
		if(colors[0] == 2 && colors[1] == 2 && colors[2] == 1 && colors[3] == 1 && colors[4] == 1 && player.getNumberOfFavorTokens() >= 4){
			if(checkSupply(c[5],game)){
				ExchnageOptions[0] = ChoosePlayerLanternCard(c[0],game,player);
				ExchnageOptions[1] = ChooseSupplyLanternCard(c[0],c[5],game,player);
			}
			else{
				ExchnageOptions[0] = 9;
		    	ExchnageOptions[1] = 9;
			}
		 }
		 else if(colors[0] == 2 && colors[1] == 1 && colors[2] == 1 && colors[3] == 1 && colors[4] == 1 && colors[5] == 1 && player.getNumberOfFavorTokens() >= 2){
			 if(checkSupply(c[6],game)){
		    	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[0],game,player);
		    	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[0],c[6],game,player);
			 }
		 	else{
		 		ExchnageOptions[0] = 9;
		     	ExchnageOptions[1] = 9;
		 	}
		 }
		 else if(colors[0] == 3 && colors[1] == 2 && colors[2] == 1 && colors[3] == 1 && player.getNumberOfFavorTokens() >= 2){
			 if(checkSupply(c[2],game)){
		    	 ExchnageOptions[0] = ChoosePlayerLanternCard(c[0],game,player);
		    	 ExchnageOptions[1] = ChooseSupplyLanternCard(c[0],c[2],game,player);
			 }
		 	 else{
		 		ExchnageOptions[0] = 9;
		     	ExchnageOptions[1] = 9;
		 	 }
		 }
		 else{
			 ExchnageOptions[0] = 9;
			 ExchnageOptions[1] = 9;
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
    protected int ChooseSupplyLanternCard(Color pl,Color cl, Game game, Player player){
        Supply Lanternsupply = game.getPlayArea().getSupply();
        HashMap<Color, Stack<LanternCard>> supply = Lanternsupply;
		
        for (int i = 0; i < player.getLanternCards().size(); i++) 
	{
		if (player.getLanternCards().get(i).getColor() == pl) 
		{
	        Stack<LanternCard> lantern_stack = supply.get(pl);
			lantern_stack.add(player.getLanternCards().get(i));    
		}
	}
		
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
        for (int i=0; i<Color.values().length; i++) 
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
        ArrayList<Integer> solution = new ArrayList<Integer>(4);
        int valueCounter=0; int maxValue =0;
        Queue<Player> realplayers = game.getPlayers();
        PlayArea realplayarea = game.getPlayArea();
        
        ArrayList<Player> realplayerList = new ArrayList<Player>(realplayers);
        Player realplayer = realplayerList.get(0);

        ArrayList<Position> realavailableList = realplayarea.getPositionAvailableLakeTileOnBoard();

        for(int i=0;i<realplayer.getLakeTiles().size();i++)
        {
            for(int j=0; j<realavailableList.size();j++)
            {
                for(int k=0;k<4;k++)
                {
                    Game gameObject = game.clone();
                    
                    Queue<Player> players = gameObject.getPlayers();
                    PlayArea playarea = gameObject.getPlayArea();
                    
                    ArrayList<Player> playerList = new ArrayList<Player>(players);
                    Player player = playerList.get(0);
                    
                    ArrayList<Position> availableList = playarea.getPositionAvailableLakeTileOnBoard();

                    LakeTile active_laketile = player.getLakeTiles().get(i);
                    ArrayList<HashMap<Rotation, Vector<Object>>> adjacent_color_list = player.checkPlaceLakeTile(playarea, active_laketile);

                    int pos_laketile_opt = j;
                    HashMap<Rotation, Vector<Object>> adjacent_colors = player.getPossibleRotation(availableList, adjacent_color_list, playarea, active_laketile, pos_laketile_opt);

                    if(k>=1){
                        active_laketile.changeRotation(Rotation.D90);
                    }
                    
                    checkdistributeLanternCard(active_laketile, playarea.getSupply(),gameObject);
                    valueCounter = checkgetBonusPlaceLakeTile(active_laketile, adjacent_colors,gameObject);
                    
                    if(valueCounter > maxValue)
                    {
                        solution.clear();
                        solution.add(i);
                        solution.add(j);
                        solution.add(k);
                        solution.add(valueCounter);
                        maxValue = valueCounter;
                    }
                    if(solution.size()==0){
                        solution.add(0);
                        solution.add(0);
                        solution.add(0);
                        solution.add(0);
                    }
                }
            }
        }   
        //System.out.println("Best Solution is : I :" + solution.get(0)+" J : " +solution.get(1)+" K : "+solution.get(2)+" with value : " +solution.get(3));
        return solution;
    }
    
    /**
     * This method distribute lantern card from supply to player
     * @param active_laketile lake tile from the stack
     * @param lanternStacks lantern card stack 
     */

    public String checkdistributeLanternCard(LakeTile active_laketile, Supply supply, Game game) 
    {
        ArrayList<Color> color_list = new ArrayList<Color>(active_laketile.getColorOfFourSides());
        String lanternCard = color_list.get(0).name();
        
        return lanternCard;
    }
    
    /**
     * This method give bonus lake tile if two color of the same are facing each other
     * @param active_laketile lake tile on the play area
     * @param adjacent_colors adjacent color
     * @throws Exception exception
     */
    public int checkgetBonusPlaceLakeTile(LakeTile active_laketile, HashMap<Rotation, Vector<Object>> adjacent_colors, Game game) throws Exception 
    {
        int valueCounter=0;
        // get bonus for adjacent and platform
        for (int i = 0; i < adjacent_colors.size(); i++) 
        {
            for (Entry<Rotation, Vector<Object>> c : adjacent_colors.entrySet())
            {
                Vector<Object> color_platform = (Vector<Object>) c.getValue();

                if (c.getKey().equals(Rotation.D0)) 
                {
                	int bonusValue;
                    bonusValue = checkgetBonusDirection(Rotation.D0, active_laketile, color_platform,game);
                    valueCounter = valueCounter+ bonusValue;
                    
                } else if (c.getKey().equals(Rotation.D90)) 
                {
                	int bonusValue;
                	bonusValue = checkgetBonusDirection(Rotation.D90, active_laketile, color_platform,game);
                	valueCounter = valueCounter+ bonusValue;
                } else if (c.getKey().equals(Rotation.D180)) 
                {
                	int bonusValue;
                	bonusValue = checkgetBonusDirection(Rotation.D180, active_laketile, color_platform,game);
                    valueCounter = valueCounter+ bonusValue;
                } 
                else if (c.getKey().equals(Rotation.D270)) 
                {
                	int bonusValue;
                	bonusValue = checkgetBonusDirection(Rotation.D270, active_laketile, color_platform,game);
                    valueCounter = valueCounter+ bonusValue;
                    
                }
            }
        }
        return valueCounter;
    }

    /**
     * This method checks the direction to give bonus
     * Comparing one color side of player's laketile with adjacent color
     * @param r Degree of rotation
     * @param active_laketile lake tile on play area
     * @param color_platform color of platform
     * @throws Exception exception
     */
    private int checkgetBonusDirection(Rotation r, LakeTile active_laketile,Vector<Object> color_platform,Game game) throws Exception 
    {
        int valueCounter = 0;
        Queue<Player> players = game.getPlayers();
        int checkFavorToken = 0;
        String lanterncard = null;
        PlayArea playArea = game.getPlayArea();
        Player current_player = players.element();
        Color side_color = active_laketile.getSideOfColor(r);
        if (side_color == color_platform.get(0)) 
        {
            lanterncard = color_platform.get(0).toString();

            int favor_token = playArea.getNumberOfFavorTokens();
            if (active_laketile.isPlatform()&&favor_token>0)
            {
                checkFavorToken = current_player.getNumberOfFavorTokens() + 1;
            }
            
            if ((Boolean) color_platform.get(1)&&favor_token>0) 
            {
                checkFavorToken = current_player.getNumberOfFavorTokens() + 1;
            }
        }
        if(checkFavorToken > 0){
        	valueCounter++;
        }
        if(lanterncard != null){
            valueCounter++;
        }
        return valueCounter;
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
