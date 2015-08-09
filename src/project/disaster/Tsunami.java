package project.disaster;

import project.Game;
import project.LakeTile;

public class Tsunami extends TsunamiDisaster{

	/**
	 * it is used to keep the correct version
	 */
	private static final long serialVersionUID = 1939659623614440133L;

	public Tsunami(int nplayer) {
		super(nplayer);
		// TODO Auto-generated constructor stub
	}
	
	public String attack(Game game){
		LakeTile[][] board = game.getPlayArea().getLakeTilesOnBoard();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = null;
			}
		}
		return "Tsunami is attacking our Lake Tiles on Board";
	}
}