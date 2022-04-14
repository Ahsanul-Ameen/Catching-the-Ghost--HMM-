package GhostBuster_HMM_Filter.Game.Controllers;
//imports
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

//skin is used to have the skinable properties!! abstract type for the controller
//story behind the scene😒😒😒😒😒😒😒
class GameControlSkin extends SkinBase<GameControl> implements Skin<GameControl> {
	public GameControlSkin(GameControl gameControl) {
		//call the super class constructor
		super(gameControl);
	}
}
