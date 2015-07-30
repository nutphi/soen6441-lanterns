package project;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * The game named Lanterns : Harvest Festival This class is used to run the game
 * 
 * @author Nuttakit
 * @version 2.0
 */

public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the list of players in the game
	 */
	private Queue<Player> players;
	/**
	 * the list of players' name in the game
	 */
	private String[] playersNames;
	/**
	 * the play area which provided lantern cards, lake tiles and dedication
	 * token
	 */
	private PlayArea playArea;

	/**
	 * 
	 * Get a group of player
	 * 
	 * @return the list of players
	 */
	public Queue<Player> getPlayers() 
	{
		return players;
	}

	/**
	 * Set a group of Player
	 * 
	 * @param players
	 *            the list of players
	 */
	public void setPlayers(Queue<Player> players) 
	{
		this.players = players;
	}

	/**
	 * Get a play area
	 * 
	 * @return PlayArea the area to place the card stacks and token piles
	 */
	public PlayArea getPlayArea() 
	{
		return playArea;
	}

	/**
	 * Set a play area
	 * 
	 * @param playArea
	 *            the area to place the card stacks and token piles
	 */
	public void setPlayArea(PlayArea playArea) 
	{
		this.playArea = playArea;
	}

	/**
	 * 
	 * constructor of the game
	 * 
	 * @param playersNames
	 *            the name of players
	 * @throws Exception
	 *             used when the players are more than 4 or less than 1
	 */
	public Game(String... playersNames) throws Exception 
	{
		this.playersNames = playersNames;

		if (playersNames.length > 1 && playersNames.length < 5) 
		{
			startGame();
		} 
		else 
		{
			throw new Exception();
		}
	}

	/**
	 * Start the game this method create player, lantern cards, lake tiles and
	 * dedication tokens on play area and give 3 lake tiles to each players
	 */
	public void startGame() 
	{
		createPlayers(playersNames);
		playArea = new PlayArea(players);
		// give lakeTiles to player
		for (Player player : players) 
		{
			for (int i = 0; i < 3; i++) 
			{
				player.getLakeTiles().add(playArea.getLakeTiles().pop());
			}
		}
	}

	/**
	 * this method is used to create players and add them into arraylist of
	 * players
	 * 
	 * @param names
	 *            create the game of players
	 * 
	 */
	private void createPlayers(String... names) 
	{
		Player player = null;
		players = new LinkedList<Player>();

		// create players according to number of players
		for (int i = 0; i < names.length; i++) 
		{
			player = new Player(names[i]);
			// initialize all the stuff for the new player
			players.add(player);
		}
	}

	/**
	 * this main method is used to control and run the game
	 * 
	 * @param args
	 *            [] the first input from command line
	 * @throws Exception
	 *             used to when the game load or save are error
	 */
	public static void main(String args[]) throws Exception 
	{
		Game game = null;
		System.out.println("1. New Game");
		System.out.println("2. Download");
		System.out.println("3. Exit");
		int in = game.inputOption(3);
		
		if (in == 1) 
		{
			game = putPlayerNamesOption();
		} 
		else if (in == 2)
		{
			game = loadGameOption();
			game.showInformation();
		} else 
		{
			System.out.print("Goodbye");
			System.exit(0);
		}
		// Start Game
		game.play();
	}

	/**
	 * This method displays the number of player allowed and collect number of player and their names
	 * @return player name
	 * @throws Exception use when player enters incorrect number of player
	 */
	public static Game putPlayerNamesOption() throws Exception 
	{
		Scanner scanner = new Scanner(System.in);
		String in = null;
		String[] names = null;
		System.out.print("How many players? (select 2,3 or 4) : ");
		
		do {
			if (in != null) 
			{
				System.out.println(in + " is not in the option");
			}
			in = scanner.next();
		} 
		while (!in.equals("2") && !in.equals("3") && !in.equals("4"));

		int nplayer = Integer.parseInt(in);
		names = new String[nplayer];
		
		for (int i = 0; i < nplayer; i++) 
		{
			System.out.println("Player[" + i + "] name:");
			names[i] = new String(scanner.next());
		}
		return new Game(names);
	}

	public static void saveGameOption(Game game) 
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Put File Name To Save");
		Game.saveGame(game, scan.next());
	}

	/**
	 * Save state of the game
	 * 
	 * @param g
	 *            the Game
	 * @param fname
	 *            the name of the saved file
	 */
	private static void saveGame(Game g, String fname) 
	{
		GameFile.save(g, fname);
	}

	/**
	 * This method load game from saved file
	 * @return  game
	 */
	public static Game loadGameOption()
	{
		Game game = null;
		Scanner scanner = new Scanner(System.in);
		String in = null;
		System.out.println("Put File Name");
		game = Game.loadGame(scanner.next());
		if (game == null)
		{
			System.out.println("Put Another File Name");
			game = loadGameOption();
		}
		return game;
	}

	/**
	 * Load state of the game
	 * 
	 * @param fname
	 *            the name of the saved file
	 * @return Game the state of the game
	 */
	private static Game loadGame(String fname) 
	{
		return GameFile.load(fname);
	}

	/**
	 * Show information of the card on play area and players hand
	 * 
	 * @throws Exception
	 */
	private void showInformation() throws Exception 
	{
		System.out.println("\nFour Of A Kind Token Stack");
		for (int i = 0; i < this.playArea.getFourOfAKindTokens().size(); i++) 
		{
			System.out.println(this.playArea.getFourOfAKindTokens().get(i)
					.getHonor());
		}
		
		System.out.println("\nSeven Unique Token Stack");
		for (int i = 0; i < this.playArea.getSevenUniqueTokens().size(); i++) 
		{
			System.out.println(this.playArea.getSevenUniqueTokens().get(i)
					.getHonor());
		}

		System.out.println("\nThree Pair Token Stack");
		for (int i = 0; i < this.playArea.getThreePairTokens().size(); i++) 
		{
			System.out.println(this.playArea.getThreePairTokens().get(i)
					.getHonor());
		}

		System.out.println("\nGeneric Token Stack");
		for (int i = 0; i < this.playArea.getGenericTokens().size(); i++) 
		{
			System.out.println(this.playArea.getGenericTokens().get(i)
					.getHonor());
		}

		System.out.println("\nLantern Card Supply :");
		for (Color c : Color.values()) 
		{
			System.out
					.println(Color.getColorText(c, Symbol.BULLET)
							+ " : "
							+ this.playArea.getSupply().getLanternStack()
									.get(c).size());
		}
		
		System.out.println("Amount of Favor Token :"
				+ this.playArea.getNumberOfFavorTokens());
		System.out.println("\nLake Tiles Stack");
		
		for (int i = 0; i < this.playArea.getLakeTiles().size(); i++)
		{
			LakeTile l = this.playArea.getLakeTiles().get(i);
			System.out.printf("%4s", l.getIndex() + " :");
			for (Color c : l.getColorOfFourSides()) 
			{
				System.out.print(Color.getColorText(c, " ") + " ");
			}
			
			if (l.isPlatform()) 
			{
				System.out.println(Symbol.PLATFORM);
			} 
			else 
			{
				System.out.println();
			}
		}

		System.out.print("\nStart Lake Tile\n");
		System.out.print(playArea.getStartLakeTile().getIndex() + " : ");
		for (Color c : playArea.getStartLakeTile().getColorOfFourSides()) 
		{
			System.out.print(Color.getColorText(c, Symbol.BULLET) + " ");
		}
		
		if (this.playArea.getStartLakeTile().isPlatform())
		{
			System.out.print(Symbol.PLATFORM);
		}
		System.out.print(" \n\n");

		for (Player player : players) 
		{
			showPlayerInformation(player);
		}
	}

	/**
	 * Show player information such as name, active or inactve, lantern card Favor token and dedication token
	 * @param player
	 * @throws Exception
	 */
	public void showPlayerInformation(Player player) throws Exception 
	{
		System.out.print("Player name : " + player.getName());
		if (players.element().equals(player))
		{
			System.out.println(": (active)");
			// currentTurn = i+1;
		} 
		else 
		{
			System.out.println();
		}
		
		System.out.println("Lantern Cards");
		
		for(Color c : Color.values())
		{
			System.out.print(Color.getColorText(c, " ")+player.numOfCardColor(c));
		}
		System.out.println();
		
		System.out.println("Number of Favor Tokens::"
				+ player.getNumberOfFavorTokens());
		System.out.println("\nValue Dedication Token : "
				+ player.countHonorValue());
		System.out.println("\nLake Tiles :");
		for (int j = 0; j < player.getLakeTiles().size(); j++) 
		{
			System.out.printf("%5s", "No." + (j + 1));
			System.out.print("-");
			System.out.printf("%2s", player.getLakeTiles().get(j).getIndex());
			System.out.print(" ");
			ArrayList<Color> laketile_colors = new ArrayList<Color>(player
					.getLakeTiles().get(j).getColorOfFourSides());
			System.out.print(Color.getColorText(laketile_colors.get(0),
					Symbol.UP) + " ");// up
			System.out.print(Color.getColorText(laketile_colors.get(1),
					Symbol.RIGHT) + " ");// right
			System.out.print(Color.getColorText(laketile_colors.get(2),
					Symbol.DOWN) + " ");// down
			System.out.print(Color.getColorText(laketile_colors.get(3),
					Symbol.LEFT) + " ");// left

			if (player.getLakeTiles().get(j).isPlatform()) 
			{
				System.out.print(Symbol.PLATFORM);
			}
			System.out.println();
		}
		System.out.println("");
	}

	// Build-2

	/**
	 * Options to be displayed
	 * 
	 * @return in input selected by user
	 */
	public static int Menu() {
		System.out.println("Select the one of the options(0-3):");
		System.out.println(" 0. Exit");
		System.out.println(" 1. Exchange a Lantern Card (optional) ");
		System.out.println(" 2. Make a Dedication (optional) ");
		System.out.println(" 3. Place a Lake Tile (mandatory) ");
		System.out.println(" 4. Save Game ");
		System.out.println(" 5. Load Game ");
		return inputOption(6);
	}

	/**
	 * Start Game
	 * 
	 * @throws Exception
	 */
	public void play() throws Exception 
	{
		int input = 0;
		String choice;
		boolean quit = false;
		do {
			Player current_player = players.element();
			System.out.println("Player - " + current_player.getName()
					+ " will start to play :");
			if (!playArea.getLakeTiles().empty()
					&& current_player.getNumberOfLakeTile() < 3)
			{
				System.out.println("Draw New LakeTile");
				LakeTile new_laketile = playArea.getLakeTiles().pop();
				current_player.getLakeTiles().add(new_laketile);
			}
			playArea.showLakeTileBoard();

			System.out.println("Number of Favor Tokens::"
					+ current_player.getNumberOfFavorTokens());
			System.out.println("\nValue Dedication Token : "
					+ current_player.countHonorValue());
			// /code
			System.out.println("Lantern Cards");
			
			for(Color c : Color.values())
			{
				System.out.print(Color.getColorText(c, " ")+current_player.numOfCardColor(c));
			}
			
			System.out.println();
			// //
			gameCoreOption(current_player);

		} while (!quit);
		System.out.println("Good Bye");
	}

	/**
	 * Selection and removal of a lantern card form player's stack
	 * 
	 * @param player
	 *            active player object
	 * @throws Exception
	 * 
	 */
	public void exchangePlayerLanternCard(Player player) throws Exception 
	{
		Scanner inputscan = new Scanner(System.in);
		System.out.println("Choose a lantern card you want to exchange");
		ArrayList<LanternCard> lanternCards = player.getLanternCards();

		System.out.println();

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
				System.out.println("Index:"
						+ counter
						+ " : "
						+ Color.getColorText(lanternCards.get(i).getColor(),
								" ") + " ");
				counter++;
			}
		}

		String in = null;
		boolean existCard = true;
		do {
			in = inputscan.next();
			HashMap<Color, Stack<LanternCard>> supply = playArea.getSupply()
					.getLanternStack();
			for (int i = 0; i < arrays.size(); i++) 
			{
				if (in.equals("" + i)) 
				{
					System.out.print("");
					Stack<LanternCard> lantern_stack = supply.get(arrays.get(i)
							.getColor());
					lantern_stack.add(arrays.get(i));
					lanternCards.remove(arrays.get(i));
					existCard = false;
				}
			}
		} while (existCard);

	}

	/**
	 * Selection and removal of a lantern card form supply stack
	 * 
	 * @param player
	 *            active player object1
	 */
	public void exchangeSupplyLanternCard(Player player)
	{
		Scanner inputscan = new Scanner(System.in);
		System.out.println("\nLantern Card Supply :");
		int i = 0;
		ArrayList<Color> buffer = new ArrayList<Color>();
		for (Color c : Color.values()) 
		{
			try {
				if (this.playArea.getSupply().getLanternStack().get(c).size() > 0) {
					System.out.println("Index :"
							+ i
							+ " :"
							+ Color.getColorText(c, Symbol.BULLET)
							+ " : "
							+ this.playArea.getSupply().getLanternStack()
									.get(c).size());
					buffer.add(c);
					i++;
				}

			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		String in = null;
		boolean flag = true;
		do 
		{
			in = inputscan.next();
			for (i = 0; i < buffer.size(); i++) 
			{
				if (in.equals("" + i)) 
				{
					Supply supply = playArea.getSupply();
					HashMap<Color, Stack<LanternCard>> stacks_list = supply
							.getLanternStack();
					Stack<LanternCard> stack = stacks_list.get(buffer.get(i));
					player.getLanternCards().add(stack.pop());
					flag = false;
				}
			}
		} while (flag);

	}

	/**
	 * get number of lantern cards a player has, to check it should not 
	 * exceed 12
	 * @return count number of the lantern card a player has
	 */
	public int getNumberOfLanternCardsOnHand() 
	{
		int count = players.element().getLanternCards().size();
		return count;
	}

	/**
	 * display the lake tile a player need to put next for example : 1. (Index,
	 * RED -P1, BLUE - P2, BLACK -P3, PURPLE- P4, Rotation)
	 * 
	 * @throws Exception
	 */
	public void showPlayerLakeTile() throws Exception 
	{
		for (int j = 1; j < 5; j++) 
		{
			Player current_player = players.element();
			System.out.print(j + " : index :"
					+ current_player.getLakeTiles().get(0).getIndex() + " ");
			int i = 1;
			for (Color c : current_player.getLakeTiles().get(0)
					.getColorOfFourSides()) 
			{
				System.out.print(Color.getColorText(c, Symbol.BULLET) + "P"
						+ (i++));
			}
			System.out.print("platform::");
			
			if (current_player.getLakeTiles().get(0).isPlatform()) 
			{
				System.out.println(Symbol.PLATFORM);
			}
			
			if (j == 1) {
				System.out.print("Rotation : 0");
			} else if (j == 2) {
				System.out.print("Rotation : 90");
			} else if (j == 3) {
				System.out.print("Rotation : 180");
			} else if (j == 4) {
				System.out.print("Rotation : 270");
			}
		}
	}

	/**
	 * Get all the tiles around a tile provided
	 * 
	 * @param x	position of x-coordinates
	 * @param y position of y-coordinates
	 * @return list_laketiles list of all the lake tiles available 
	 * 			around a lake tile on board
	 */
	public ArrayList<LakeTile> getTilesAround(int x, int y) 
	{
		ArrayList<LakeTile> list_laketiles = new ArrayList<LakeTile>();
		list_laketiles.add(playArea.getLakeTilesOnBoard()[x][y + 1]);
		list_laketiles.add(playArea.getLakeTilesOnBoard()[x + 1][y]);
		list_laketiles.add(playArea.getLakeTilesOnBoard()[x][y - 1]);
		list_laketiles.add(playArea.getLakeTilesOnBoard()[x - 1][y]);
		return list_laketiles;
	}

	/**
	 * Execute this method, if the third option is selected by player.
	 * @param pos Position instance
	 * @param lakeTile LakeTile instance
	 */
	public void placeALakeTile(Position pos, LakeTile lakeTile) 
	{
		int x = pos.getX();
		int y = pos.getY();
		playArea.getLakeTilesOnBoard()[x][y] = lakeTile;
	}

	public boolean isNumberOfLanternCardsOnHandsOver() 
	{
		int lanternCards;
		lanternCards = getNumberOfLanternCardsOnHand();
		return lanternCards > 12;
	}

	/**
	 * Display the current lake tile on the board
	 * @throws Exception exception
	 */
	public void showCurrentPlayerLakeTile() throws Exception 
	{
		ArrayList<LakeTile> current_player_laketiles = players.element()
				.getLakeTiles();
		for (LakeTile lake_tile : current_player_laketiles)
		{
			int index = current_player_laketiles.indexOf(lake_tile);
			System.out.print("index : " + index + ":");
			System.out.printf("%2s -", lake_tile.getIndex());
			
			for (Color c : lake_tile.getColorOfFourSides())
			{
				System.out.print(Color.getColorText(c, Symbol.BULLET) + " ");
			}
			if (lake_tile.isPlatform()) 
			{
				System.out.print(Symbol.PLATFORM);
			}
			System.out.println("");
		}
	}

	/**
	 * Shows the all the possible ways to rotate a lake tile
	 * @param l LakeTile
	 * @throws Exception exception
	 */
	public void showPossibleRotation(LakeTile l) throws Exception 
	{
		int sideOfLakeTile = 4;
		System.out.println("How do you want to rotate the lake tile?");
		for (int i = 0; i < sideOfLakeTile; i++)
		{
			System.out.print(i + ":");
			ArrayList<Color> four_side_colors = new ArrayList<Color>(
					l.getColorOfFourSides());
			System.out.print(Color.getColorText(four_side_colors.get(0),
					Symbol.UP) + " ");
			System.out.print(Color.getColorText(four_side_colors.get(1),
					Symbol.RIGHT) + " ");
			System.out.print(Color.getColorText(four_side_colors.get(2),
					Symbol.DOWN) + " ");
			System.out.print(Color.getColorText(four_side_colors.get(3),
					Symbol.LEFT) + " ");
			System.out.println();
			l.getColorOfFourSides().add(l.getColorOfFourSides().remove());
		}
	}

	/**
	 * The active player may perform each of these actions once per turn
	 * @param current_player active player
	 * @throws Exception exception
	 */
	public void gameCoreOption(Player current_player) throws Exception 
	{
		int input = Menu();
		switch (input) 
		{
		case 1:

			exchangeLanCard(current_player);
			break;

		case 2:
			makeADedicationMenu(current_player);
			break;

		case 3:
			conditionToPlaceALakeTile(current_player);
			break;
		case 4:
			saveGameOption(this);
			break;
		case 5:
			Game g = loadGameOption();
			g.play();
			break;
		case 0:
			System.out.print("GoodBye");
			System.exit(0);
			break;

		default:
			System.out.println("Invalid input!!! Please do right selection...");
			break;
		}

	}

	/**
	 * If a player begins his turn with more than 12 Lantern
	 * Cards, he must make a dedication or discard cards until
	 * he has 12 or fewer cards before placing a Lake Tile. 
	 * @param current_player active player
	 * @throws Exception exception
	 */
	private void conditionToPlaceALakeTile(Player current_player)
			throws Exception 
	{
		if(current_player.getLanternCards().size()>12)
		{
			System.out.println("You have more than twelve lantern cards");
			System.out.println("You need to make a dedication or discard a lantern card");
		}
		else
		{
			placeLakeTileMenu(current_player);
		}
	}

	/**
	 * Exchange a lantern card option
	 * @param current_player Active Player
	 * @throws Exception exception
	 */
	private void exchangeLanCard(Player current_player) throws Exception 
	{
		if ((current_player.getNumberOfFavorTokens() < 2)
				|| (current_player.getLanternCards().size() == 0))
		{
			System.out.println("Sorry..you can not perform this action.");
			System.out.println("you do not have enough favor tokens or you " +
					"don't have a lantern card to exchange.");
		} 
		
		else 
		{
			// remove lantern card from player's hand and add that card
			// to supply stack
			exchangePlayerLanternCard(current_player);
			// remove lantern card from supply stack and add it to
			// player's hand
			exchangeSupplyLanternCard(current_player);
		}
	}

	/**
	 * This method check if a player can place a lake tile and display position to place a lake tile
	 * @param current_player
	 * @throws Exception
	 */
	private void placeLakeTileMenu(Player current_player) throws Exception 
	{
		if (isNumberOfLanternCardsOnHandsOver()) 
		{
			System.out.println("You must make a dedication token or discard cards");
			gameCoreOption(current_player);
		}
		
		System.out.println("Place a lake tile selected");
		
		// **discard card or return to menu.

		// show player position
		showPlayerPosition();
		System.out.println();
		showCurrentPlayerLakeTile();
		int in = inputOption(current_player.getLakeTiles().size());

		// get the laketile which player wants to put then remove the
		// tile on their hand
		LakeTile active_laketile = current_player.getLakeTiles().remove(in);
		ArrayList<Position> list = playArea
				.showIndexAvailableToPutLakeTileOnBoard();
		ArrayList<HashMap<Rotation, Vector<Object>>> adjacent_color_list = new ArrayList<HashMap<Rotation, Vector<Object>>>();
		
		optionOnBoard(list, adjacent_color_list);
		System.out.print("which position you want to put laketile::");
		// input position and check

		int pos_laketile_opt = inputOption(list.size());

		placeALakeTile(list.get(pos_laketile_opt), active_laketile);
		HashMap<Rotation, Vector<Object>> adjacent_colors = adjacent_color_list
				.get(pos_laketile_opt);
		System.out.println();
		showPossibleRotation(active_laketile);

		int rotation_opt = inputOption(4);
		int rotation = rotation_opt * 90;
		active_laketile.setRotation(Rotation.getRotation(rotation));
		// change the side of lake tile to put on board
		// / new
		active_laketile.changeRotation(active_laketile.getRotation());

		// get lanterncard from supply stacks to each players after
		// putting lake tile

		ArrayList<Player> players_list = new ArrayList<Player>(players);
		HashMap<Color, Stack<LanternCard>> lanternStacks = playArea
				.getSupply().getLanternStack();
		LanternCard l = null;

		distributeLanternCard(active_laketile, players_list, lanternStacks);

		getBonusPlaceLakeTile(current_player, active_laketile,
				adjacent_colors);
		
		showPlayerInformation(current_player);
		// change turn
		players.add(players.remove());
		// to get the winner
		ArrayList<LakeTile> laketile = players.element().getLakeTiles();
		
		if (laketile.size() == 0) 
		{
			System.out.println(getTheWinner());
			System.exit(0);
		}
	}
	
	/**
	 * This method distribute lantern card from supply to player
	 * @param active_laketile lake tile from the stack
	 * @param players_list list of player currently playing the game
	 * @param lanternStacks lantern card stack 
	 */

	private void distributeLanternCard(LakeTile active_laketile,
			ArrayList<Player> players_list,
			HashMap<Color, Stack<LanternCard>> lanternStacks) 
	{
		LanternCard l;
		for (int i = 0; i < players.size(); i++) 
		{
			Player getting_lanterncard_player = players_list.get(i);
			int index = getting_lanterncard_player.getIndex();
			ArrayList<Color> color_list = new ArrayList<Color>(
					active_laketile.getColorOfFourSides());
			if (index == 0) 
			{
				Stack<LanternCard> lanternCard = lanternStacks
						.get(color_list.get(0));
				if (!lanternCard.empty())
				{
					l = lanternCard.pop();
					getting_lanterncard_player.getLanternCards().add(l);
				}
			} 
			else if (index == 1) 
			{
				Stack<LanternCard> lanternCard = lanternStacks
						.get(color_list.get(1));
				if (!lanternCard.empty()) 
				{
					l = lanternCard.pop();
					getting_lanterncard_player.getLanternCards().add(l);
				}
			} 
			else if (index == 2) 
			{
				Stack<LanternCard> lanternCard = lanternStacks
						.get(color_list.get(2));
				if (!lanternCard.empty()) 
				{
					l = lanternCard.pop();
					getting_lanterncard_player.getLanternCards().add(l);
				}
			} 
			else if (index == 3) 
			{
				Stack<LanternCard> lanternCard = lanternStacks
						.get(color_list.get(3));
				if (!lanternCard.empty()) 
				{
					l = lanternCard.pop();
					getting_lanterncard_player.getLanternCards().add(l);
				}
			}
		}
	}
	
	/**
	 * This method display the options a player can place the lake tile
	 * @param list list of possible options
	 * @param adjacent_color_list Adjacent color
	 * @throws Exception
	 */

	private void optionOnBoard(ArrayList<Position> list,
			ArrayList<HashMap<Rotation, Vector<Object>>> adjacent_color_list)
			throws Exception
	{
		System.out.println("Available index :::");
		
		for (int i = 0; i < list.size(); i++) 
		{
			Position index = list.get(i);
			System.out.print("option " + i + " ::" + index.getText());

			// show adjacent color
			adjacent_color_list.add(playArea.showAdjacentColor(index));
			System.out.println();
		}
	}

	/**
	 * This method shows the player position
	 */
	private void showPlayerPosition() 
	{
		for (Player player : players) 
		{
			System.out.print(player.getName());
			if (player.getIndex() == 0) {
				System.out.print(Symbol.UP);
			} else if (player.getIndex() == 1) {
				System.out.print(Symbol.RIGHT);
			}
			if (player.getIndex() == 2) {
				System.out.print(Symbol.DOWN);
			}
			if (player.getIndex() == 3) {
				System.out.print(Symbol.LEFT);
			}
			System.out.print(" ");
		}
	}

	/**
	 * This method display the menu for the types of dedication a player can make 
	 * @param current_player active player
	 */
	private void makeADedicationMenu(Player current_player)
	{
		int choice;
		System.out.println("What type of dedication do you want to make? ");
		System.out.println(" 0. Four of A Kind");
		System.out.println(" 1. Three Pair");
		System.out.println(" 2. Seven Unique");
		do 
		{
			choice = inputOption(3);
		} 
		while (choice < 0 && choice > 2);
		
		if (choice == 0) 
		{
			current_player.makeFourOfAKind(playArea.getFourOfAKindTokens(),
					playArea.getGenericTokens(), playArea.getSupply());
		} 
		else if (choice == 1) 
		{
			current_player.makeThreePair(playArea.getThreePairTokens(),
					playArea.getGenericTokens(), playArea.getSupply());
		} 
		else if (choice == 2)
		{
			current_player.makeSevenUnique(playArea.getSevenUniqueTokens(),
					playArea.getGenericTokens(), playArea.getSupply());
		}
	}

	/**
	 * This method give bonus lake tile if two color of the same are facing each other
	 * @param current_player active player
	 * @param active_laketile lake tile on the play area
	 * @param adjacent_colors adjacent color
	 * @throws Exception exception
	 */
	public void getBonusPlaceLakeTile(Player current_player, LakeTile active_laketile, HashMap<Rotation, Vector<Object>> adjacent_colors) throws Exception 
	{
		int favor_playarea = playArea.getNumberOfFavorTokens();
		// get bonus for adjacent and platform
		for (int i = 0; i < adjacent_colors.size(); i++) 
		{
			for (Entry<Rotation, Vector<Object>> c : adjacent_colors.entrySet()) 
			{
				Vector<Object> color_platform = (Vector<Object>) c.getValue();
				// up -adjacent laketile color and down - (active color)
				if (c.getKey().equals(Rotation.D0)) 
				{
					favor_playarea = checkBonusDirection(Rotation.D0, current_player,
							active_laketile, favor_playarea, color_platform);
				} else if (c.getKey().equals(Rotation.D90)) 
				{
					
					favor_playarea = checkBonusDirection(Rotation.D90, current_player,
							active_laketile, favor_playarea, color_platform);
				} else if (c.getKey().equals(Rotation.D180)) 
				{
					favor_playarea = checkBonusDirection(Rotation.D180, current_player,
							active_laketile, favor_playarea, color_platform);
					
				} 
				else if (c.getKey().equals(Rotation.D270)) 
				{
					favor_playarea = checkBonusDirection(Rotation.D270, current_player,
							active_laketile, favor_playarea, color_platform);
					
				}
			}
		}
	}

	/**
	 * This method checks the direction to give bonus
	 * @param r Degree of rotation
	 * @param current_player active player
	 * @param active_laketile lake tile on play area
	 * @param favor_playarea player that get the bonus
	 * @param color_platform color of platform
	 * @return player that get the bonus
	 * @throws Exception exception
	 */
	private int checkBonusDirection(Rotation r, Player current_player, LakeTile active_laketile,
			int favor_playarea, Vector<Object> color_platform) throws Exception 
	{
		// get(0) is color
		if (active_laketile.getSideOfColor(r) == color_platform.get(0)) 
		{

			Stack<LanternCard> lantern_stack = playArea.getSupply()
					.getLanternStack()
					.get(color_platform.get(0));
			
			if (!lantern_stack.empty()) {
				current_player.getLanternCards().add(
						lantern_stack.pop());
			}
			
			if (active_laketile.isPlatform()&&favor_playarea>0)
			{
				favor_playarea = favor_playarea -1 ;
				playArea.setNumberOfFavorTokens(favor_playarea);
				current_player.setNumberOfFavorTokens(current_player.getNumberOfFavorTokens() + 1);
			}
			
			if ((Boolean) color_platform.get(1)&&favor_playarea>0) 
			{
				favor_playarea = favor_playarea -1 ;
				playArea.setNumberOfFavorTokens(favor_playarea);
				current_player.setNumberOfFavorTokens(current_player.getNumberOfFavorTokens() + 1);
			}
		}
		return favor_playarea;
	}

	/**
	 * This method get and displays the winner of the game
	 * @return string with the winner name, number of favor tokens and number of lantern
	 * cards
	 */
	public String getTheWinner() {
		int winner_honor = 0;
		int winner_favor_token = 0;
		int winner_lan_card = 0;
		ArrayList<Player> winner_honor_players = new ArrayList<Player>();
		ArrayList<Player> winner_favor_players = new ArrayList<Player>();
		ArrayList<Player> winners = new ArrayList<Player>();
		String winnerStr = "The winner";
		for (Player p : players) 
		{
			if (p.countHonorValue() == winner_honor)
			{
				winner_honor_players.add(p);
			}
			if (p.countHonorValue() > winner_honor) 
			{
				winner_honor_players = new ArrayList<Player>();
				winner_honor_players.add(p);
				winner_honor = p.countHonorValue();
			}
		}

		for (Player p : winner_honor_players)
		{
			if (p.getNumberOfFavorTokens() == winner_favor_token) 
			{
				winner_favor_players.add(p);
			}
			if (p.getNumberOfFavorTokens() > winner_favor_token)
			{
				winner_favor_players = new ArrayList<Player>();
				winner_favor_players.add(p);
				winner_favor_token = p.getNumberOfFavorTokens();
			}
		}

		for (Player p : winner_favor_players) 
		{
			int current_lan_size = p.getLanternCards().size();
			if (current_lan_size == winner_lan_card) {
				winners.add(p);
			}
			if (current_lan_size > winner_lan_card) 
			{
				winners = new ArrayList<Player>();
				winners.add(p);
				winner_lan_card = current_lan_size;
			}
		}
		
		if (winners.size() > 1) 
		{
			winnerStr += "s are";
		} 
		
		else 
		{
			winnerStr += " is";
		}
		
		for (int i = 0; i<winners.size();i++)
		{
			if(i==0){
				winnerStr += (" "+winners.get(i).getName());
			}
			else if(i<winners.size()-1)
			{
				winnerStr += (", "+winners.get(i).getName());
			}
			else
			{
				winnerStr += (" and " + winners.get(i).getName());
			}
			
			if(i!=winners.size()-1){
				
			}
		}
		winnerStr +=" with " + winner_honor + " honor value, ";
		winnerStr +=winner_favor_token + " favor, and ";
		winnerStr +=winner_lan_card + " lantern card";
		return winnerStr;
	}

	/**
	 * This method check the input the user provides
	 * @param n_option
	 * @return integer value of the option selected
	 */
	public static int inputOption(int n_option)
	{
		Scanner inputscan = new Scanner(System.in);
		String in = null;
		boolean flag = true;
		do
		{
			if (in != null) 
			{
				System.out.println(in + " is not in the option");
			}
			in = inputscan.next();
			for (int i = 0; i < n_option; i++)
			{
				if (in.equals("" + i)) 
				{
					flag = false;
				}
			}
		}
		while (flag);
		return Integer.parseInt(in);
	}
}
