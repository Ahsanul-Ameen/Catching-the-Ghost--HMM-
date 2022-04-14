package GhostBuster_HMM_Filter.Game.Controllers;

import GhostBuster_HMM_Filter.Game.PieceClasses.*;
import GhostBuster_HMM_Filter.Game.StartGame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

public class BoardImplementation extends Pane {
	private final Piece[][] pieces;
	private final Rectangle[][] board;
	private final Text[][] text;
	private final ImageView[][] imageviews;
    private final Translate position;

	public Rectangle[][] getBoard() { return this.board; }

	public ImageView[][] getImageviews() { return this.imageviews; }

	//essential
    public void placeBoard(final int i, final int j) { getChildren().add(board[i][j]); }

    //essential
    public void placeImageViews(final int i, final int j) { getChildren().addAll(imageviews[i][j]); }

	//essential
	public void placeText(final int i, final int j) { getChildren().addAll(text[i][j]); }

    public BoardImplementation() {
        position = new Translate();

        // Declares new board
        int boardWidth = StartGame.BOARD_SIZE;
        int boardHeight = StartGame.BOARD_SIZE;

        board = new Rectangle[boardWidth][boardHeight];//StartGame.BOARD_SIZE*StartGame.BOARD_SIZE size 2D array
		Image[][] images = new Image[boardWidth][boardHeight];
		imageviews = new ImageView[StartGame.BOARD_SIZE][StartGame.BOARD_SIZE];
		//initialize the board: background, data structures, initial layout of pieces
		pieces = new Piece[boardWidth][boardHeight];
		text = new Text[boardWidth][boardHeight];

        // Initializes new Chess Board first time
        for(int x = 0; x < boardWidth; x++) {
            for(int y=0; y < boardHeight; y++) {
				{
					board[x][y] = new Rectangle(50,50);
					board[x][y].setFill(Color.DEEPSKYBLUE);
					board[x][y].setStroke(Color.LIGHTSKYBLUE);
					board[x][y].setStrokeType(StrokeType.INSIDE);
					board[x][y].setStrokeWidth(0.2);
				}
				{
					// Initializes imageView and windows and init it
					imageviews[x][y] = new ImageView(images[x][y]);
					imageviews[x][y].setFitWidth(50);
					imageviews[x][y].setFitHeight(80);
					imageviews[x][y].setPreserveRatio(true);
					imageviews[x][y].setSmooth(true);
					imageviews[x][y].setCache(true);
					imageviews[x][y].setTranslateX(board[x][y].getWidth() / boardWidth);
					imageviews[x][y].setTranslateY(board[x][y].getHeight() / boardHeight);
				}
				{
					pieces[x][y] = new Empty(Empty.EMPTY_PLAYER, x, y);
				}
				{
					text[x][y] = new Text();
					text[x][y].setText("0.000");
					//Setting font to the text
					text[x][y].setFont(Font.font("consoles", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 12));

					//setting the position of the text
					text[x][y].setX(board[x][y].getWidth() * x + 10);
					text[x][y].setY(board[x][y].getHeight() * y + 20);
					text[x][y].setSmooth(true);
					text[x][y].setCache(true);
					text[x][y].setTranslateX(board[x][y].getHeight() / boardHeight);
					text[x][y].setTranslateY(board[x][y].getHeight() / boardHeight);
				}
            }
        }
    }

	//resize method
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
        double cell_width = width / StartGame.BOARD_SIZE ;
        double cell_height = height / StartGame.BOARD_SIZE ;

		for(int x = 0; x < StartGame.BOARD_SIZE ; x++) {
			for(int y = 0; y < StartGame.BOARD_SIZE ; y++) {
				board[x][y].resize(x * cell_width, y * cell_height);
				board[x][y].relocate(x * cell_width, y * cell_height);
				board[x][y].setStrokeWidth(cell_width / 15);//size of movable box boundaries
				board[x][y].setWidth(cell_width);
				board[x][y].setHeight(cell_height);

				//Image/Shapes of Black Royalty, White Royalty and Norm Royalty
				imageviews[x][y].resize(cell_width / StartGame.BOARD_SIZE , cell_height / StartGame.BOARD_SIZE );
				imageviews[x][y].relocate(x * cell_width, y * cell_height);
				imageviews[x][y].setFitWidth(cell_width / 1.25);
				imageviews[x][y].setFitHeight(cell_height / 1.25);
				imageviews[x][y].setTranslateX(board[x][y].getWidth() / StartGame.BOARD_SIZE );
				imageviews[x][y].setTranslateY(board[x][y].getHeight() / StartGame.BOARD_SIZE);

				//setting the position of the text
				text[x][y].setX(cell_width * x + cell_width/10);
				text[x][y].setY(cell_height * y + cell_height/2.5);
				text[x][y].setSmooth(true);
				text[x][y].setCache(true);
				text[x][y].setTranslateX(board[x][y].getHeight() / StartGame.BOARD_SIZE);
				text[x][y].setTranslateY(board[x][y].getHeight() / StartGame.BOARD_SIZE);
			}
		}
	}

	@Override
	public void relocate(double x, double y) {
		super.relocate(x, y);
		position.setX(x);
		position.setY(x);
	}

	public void clearHighLights() {
		for(int x = 0; x < StartGame.BOARD_SIZE ; x++) {
			for(int y = 0; y < StartGame.BOARD_SIZE ; y++) {
				clearColor(x, y);
			}
		}
	}

    // Returns state of the chess board ..
    public Piece[][] getState() { return this.pieces; }


	// set probability table
	void setCurrentTable(Double[][] table) {
		System.out.println("updating table...");
		for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
			for(int y = 0; y < StartGame.BOARD_SIZE; ++y) {
				if(table[x][y] < 0.010001) {
					text[x][y].setText("<0.01");
				} else {
					text[x][y].setText(Double.toString(table[x][y]));
					text[x][y].setText(String.format("%.3f", table[x][y]));
				}
			}
		}
	}

	void callTheGhost(int x, int y) {
		board[x][y].setFill(Color.DARKVIOLET);
		board[x][y].setStroke(Color.HOTPINK);
	}

	void leaveTheGhost(int x, int y) {
		clearColor(x, y);
	}

	public void assignColor(int x, int y, Paint paint) {
		// TODO
		board[x][y].setFill(paint);
		board[x][y].setStroke(Color.DARKCYAN);
	}

	public void clearColor(int x, int y) {
		board[x][y].setFill(Color.DEEPSKYBLUE);
		board[x][y].setStroke(Color.LIGHTSKYBLUE);
	}

	boolean isSelected(int x, int y) {
		return board[x][y].getFill() != Color.DEEPSKYBLUE;
	}
}
