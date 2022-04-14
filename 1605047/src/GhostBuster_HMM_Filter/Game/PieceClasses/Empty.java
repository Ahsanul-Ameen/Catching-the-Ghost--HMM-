package GhostBuster_HMM_Filter.Game.PieceClasses;

import GhostBuster_HMM_Filter.Game.StartGame;

public class Empty extends Piece {
	public static final int EMPTY_PLAYER = 0, WHITE_PLAYER = 1;
	private final int i;
	private final int j;

	public Empty(int type, int ii, int jj) {
		super(type);
		i = ii;
		j = jj;
	}

	@Override
	public String toString(){
		return "Empty";
	}

	@Override
	public int icoord(){
		// Bounds correction
		if(i > StartGame.BOARD_SIZE )
			return StartGame.BOARD_SIZE - 1;
		return Math.max(i, 0);
	}

	@Override
	public int jcoord() {
		// Bounds correction
		if(j > StartGame.BOARD_SIZE )
			return StartGame.BOARD_SIZE - 1;
		return Math.max(j, 0);
	}

}
