package javachess.gui;


import java.util.Optional;
import javachess.backgroundmatrix.BackgroundGrid;
import javachess.launchpad.Launchpad;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author mhub - 2018
 * @version 2.0
 * 
 * This class extends the Application. 
 * It combines graphical nodes and sets the scenes, and acts to rezising. 
 *
 */
public class GUI extends Application{

	/**
	 * Stage of the current application
	 */
	public Stage S;

	/**
	 * BoardGui - for redrawing
	 */
	private BoardGui BG;

	/**
	 * Scene - also for redrawing
	 */
	private Scene sc;

	/**
	 * BorderPane - for setting the Menu where it should be
	 */
	private BorderPane root;

	/**
	 * int[][] - _BGG - the initial Meeple Positions
	 */
	private int[][] _BGG;

	/**
	 * BackgroundGrid - _BGG2 - the initial BackgroundGrid
	 */
	private BackgroundGrid _BGG2;

	/**
	 * The Menu
	 */
	private Menu M;

	/**
	 * the Constructor, rewrites _BGG & _BGG2
	 * @param BGG - BackgroundGrid - contains the initial Meeples
	 */
	public GUI(BackgroundGrid BGG){
		_BGG2 = BGG;
		_BGG = _BGG2.iBackground;
	}
	
	double withsave;

	@Override
	
	/**
	 * Method is called when the Application starts. 
	 */
	public void start(Stage _S) throws Exception {
		S = _S;
		S.setWidth(700);
		S.setHeight(500);						//setting stage defaults

		S.getIcons().add(new Image("javachess/images/JavaChess.png"));
		setRoot(new BorderPane());
		sc = new Scene(getRoot(), 600, 300);					//Creating the scene
		_BGG2.setChoose(0);
		BG = new BoardGui(this);								//new boardgui
		BG.setXY(S.getWidth()-15, S.getHeight()-75);	
		BG.setHighlighting(true);
		BG.setBGG2(_BGG2);
		BG.DrawGrid(_BGG);										//drawing the chess board
		BG.setChoose(0);
		getRoot().setCenter(BG);
		M = new Menu(this);
		S.setTitle("JavaChess");
		S.setScene(sc);
		BG.setStartupbuttonOn(true);	
		
		S.show();
		S.setMinHeight(500);
		S.setMinWidth(500);										//Showing the scene
		BG.drawStartMenu();										//drawing the startmenu
		
		
		//Setting up listeners on changes of size. Also tried out this new lamda notation - shorter, but i do not like it. 
		S.widthProperty().addListener((obs, oldVal, newVal) -> {
				BG.setXY(S.getWidth()-15, S.getHeight()-75);
		});

		S.heightProperty().addListener((obs,oldVal,newVal) -> {
				BG.setXY(S.getWidth()-15, S.getHeight()-75);
		});
	}

	/**
	 * If button new on the menu has been pressed
	 */
	public void newBG(){
		_BGG2.ResetStats();
		BG.setTeam(_BGG2.getTeam());
		BG.redraw();
		try {
			BG.DrawGrid(_BGG2.iBackground);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		try {
			M.getPp().getLoadTurn().getItems().clear();
		}catch(Exception ex) {
			//no exception
		}
		
		BG.turnProp.setValue(_BGG2.getTurnRound());
		
		M.getPp().getAICombo().getSelectionModel().select(0);
		M.getPp().getAIslider().setValue(_BGG2.getAiDepth());
		M.getPp().getHardCoreAI().setSelected(_BGG2.getHardCoreAI());
		
	}

	/**
	 * if button LoadBG has been pressed
	 * @param BGG2 - BackgroundGrid of the loaded chess
	 */
	public void LoadBG(BackgroundGrid BGG2){
		BG.setXY(S.getWidth()-15, S.getHeight()-75);
		try {
			BG.DrawGrid(BGG2.iBackground);
		} catch (Exception e) {
			e.printStackTrace();
		}
		M.setSelect(BGG2.getChoose());
		BG.setChoose(BGG2.getChoose());
		BG.setTeam(BGG2.getTeam());
		BG.setBGG2(BGG2);
		
		try {
			M.getPp().getLoadTurn().getItems().clear();
		}catch(Exception ex) {
			//no exception
		}
		
		M.getPp().getAIslider().setValue(BGG2.getAiDepth());
		M.getPp().getHardCoreAI().setSelected(BGG2.getHardCoreAI());
		if(BGG2.getAITeam()) {
			M.getPp().getAICombo().getSelectionModel().select(1);
		}else {
			M.getPp().getAICombo().getSelectionModel().select(0);
		}
		
		BG.redraw();
		try {
			BG.DrawGrid(BGG2.iBackground);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BG.turnProp.setValue(_BGG2.getTurnRound());
		getRoot().setCenter(BG);
		
		
		
		
	}

	/**
	 * 
	 * @return - Stage - returns the Stage 
	 */
	public Stage getStage(){
		return S;
	}

	/**
	 * 
	 * @return - BackgroundGrid - returns the BackgroundGrid
	 */
	public BackgroundGrid getBGG2(){
		return _BGG2;
	}

	public BoardGui getBoardGui(){
		return BG;
	}

	/**
	 * 
	 * @param BGG2 - BackgroundGrid - sets the BackgroundGrid
	 */
	public void setBGG2(BackgroundGrid BGG2){
		_BGG2 = BGG2;		
	}

	/**
	 * 
	 * @param i - int - sets the gamemode
	 */
	public void setChoose(int i){
		_BGG2.setChoose(i);
		BG.setChoose(i);
	}

	/**
	 * 
	 * @return int - returns the current selected gameMode
	 */
	public int getChoose(){
		return _BGG2.getChoose();
	}

	
	/**
	 * 
	 * @return the backgroundmatrix
	 */
	public int[][] getBGG1(){
		return _BGG;
	}
	
	/**
	 * 
	 * @param bgg sets the backgroundmatrix
	 */
	public void setBGG1(int[][] bgg){
		_BGG = bgg;
	}

	/**
	 * If you want to make a Draw
	 */
	public void wishDraw(){
		if(BG.getChoose() == 0){ // HotSeat
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Draw request from player " + !_BGG2.getTeam());
			alert.setContentText("if you agree to make a Draw press 'OK'.");

			Optional<ButtonType> answer = alert.showAndWait();

			if(answer.get() == ButtonType.OK){
				_BGG2.setDraw(true);
				getBoardGui().soundPlayer.playSound("menu");
			}else{
				_BGG2.changeTeam();
				getBoardGui().soundPlayer.playSound("menu");
			}

		} else if(BG.getChoose() == 2){
			System.out.println("line 271");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("It�t a Draw, the player gave up.");
			alert.setContentText(null);
			alert.setTitle("Draw");
			alert.show();
			_BGG2.setDraw(true);
		}
	}

	/**
	 * 
	 * @return the game menu
	 */
	public Menu getMenu(){
		return M;
	}

	
	/**
	 * Inits the Launchpad Class
	 */
	public void initLaunchad() {
		Launchpad Lauch = new Launchpad(this);		
		Lauch.start(_BGG2, BG);
		
	}
	
	public void initLaunchpad2(Launchpad Lauch) {		
		BG.setLaunchpad(Lauch);
		BG.setBLauch(true);
	}

	/**
	 * 
	 * @return  the root border pane
	 */
	public BorderPane getRoot() {
		return root;
	}

	/**
	 * set 
	 * @param the root boderpane
	 */
	public void setRoot(BorderPane root) {
		this.root = root;
	}

}
