package GhostBuster_HMM_Filter.Game.PieceClasses;
import GhostBuster_HMM_Filter.Game.StartGame;
import javafx.scene.image.Image;


/**
 *This is the interface for all Pieces use image
 * @Contributor  Ahsanul Ameen Sabit
 */

//class declaration - abstract because we will not want to create a Piece object but we would
//like to specify the private fields that all pieces should have in addition to their behaviours
public abstract class Piece implements Cloneable {
	public static final int WHITE_PLAYER = 1,
							EMPTY_PLAYER = 0;
	
	//piece can be either white (1) or empty(0)
	private final int type;
	private String name;
	private String imageName;
	private int i;
	private int j;
	private Piece[][] newBoard;


    public Image image() { return image(); }
	
	public int icoord() {
		// Bounds correction
		if(i > StartGame.BOARD_SIZE) { return StartGame.BOARD_SIZE - 1; }
		return Math.max(i, 0);
	}
	
	public int jcoord() {
		// Bounds correction
		if(j > StartGame.BOARD_SIZE) { return StartGame.BOARD_SIZE - 1; }
		if(i < 0) { return 0; }
		else { return j; }
	}
	
	@Override
	public String toString(){
		return name;
	}

	public Piece(int type) { this.type=type; }

	public int type() {
		if(type == WHITE_PLAYER) { return WHITE_PLAYER; }
		else return EMPTY_PLAYER;
	}
	
	public Piece[][] moveCircle(Piece selectedPiece, Piece targetPiece, Piece[][] boardState) {
		// TODO Auto-generated method stub
		return newBoard;
	}
}
