package Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import Model.Answer;
import Model.Board;
import Model.Game;
import Model.Piece;
import Model.Player;
import Model.Question;
import Utils.PrimaryColor;
import View.BoardEdit;
import View.BoardGUI;
import View.InstructionsGame;
import View.MainscreenGUI;
import View.ManageQuestions;
import View.Nicknames;
import View.QuestionGUI;
import View.Scoreboard;
import View.Winner;
import View.pointsTable;

public class DisplayController {

	private static DisplayController instance;
	public static MainscreenGUI mainscreen;
	public static BoardGUI boardGUI;
	public static pointsTable points;
	public static ManageQuestions manageQuestions;
	public static Scoreboard scoreboard;
	public static Nicknames nicknames;
	public static QuestionGUI questions;
	public static InstructionsGame instructions;
	public static Winner winner;
	public static BoardEdit boardEdit;

	private DisplayController() {
		
	}
	
	public static DisplayController getInstance() 
	{ 
		if (instance == null) 
		{ 
			instance = new DisplayController(); 
		} 
		return instance; 
	}
	
	public void showBoard(Player[] players) {
		if(players != null) {
			System.out.print("Player 1 : " + players[0].getNickname() + " || ");
			System.out.println("Player 2 : " + players[1].getNickname());
		}
		Game game = Game.getInstance();
		
		try {
			game.startGame(players);
		} catch (Exception e) {
			System.out.println(game.getGameTime());
			nicknames.notifyError(e.getMessage());
			this.showMainScreen();
			if(boardGUI != null)
				if(boardGUI.getPrimary() != null)
					if(boardGUI.getPrimary().isShowing())
						closeBoard();
			if(!mainscreen.getPrimary().isShowing())
				showMainScreen();
			return;
		}
		
		boardGUI = new BoardGUI();
		BoardController.getInstance().setBoard(boardGUI);
		boardGUI.start(boardGUI.getPrimary());
		boardGUI.initiateGamePlayers(Player.getInstance(0).getNickname(), Player.getInstance(1).getNickname());
		boardGUI.setNewTurn(Game.getInstance().getTurn().getCurrentPlayer().getColor());
		GameTimerController fullTimer = new GameTimerController();
		game.getTimer().startTimer();
		fullTimer.start(); //TODO thread should be killed
	}
	
	public void showBoard(Player[] players,File file) {
		HashMap<Character, ArrayList<Piece>> load = null;
		load = MiscController.getInstance().loadGame(file); 
		
		if(load == null) {
			nicknames.notifyError("File cannot be loaded.. check the file format!");
			this.showMainScreen();
			return;
		}
	
		if(players != null) {
			System.out.print("Player 1 : " + players[0].getNickname() + " || ");
			System.out.println("Player 2 : " + players[1].getNickname());
		}
		
		try {
			if(load.containsKey('W')) {
				Game.getInstance().startGame(players, load.get('W'), 'W');
			}else {
				Game.getInstance().startGame(players, load.get('B'), 'B');
			}
		}catch (Exception e) {
			System.out.println(Game.getInstance().getGameTime());
			nicknames.notifyError(e.getMessage());
			e.printStackTrace();
			if(boardGUI != null)
				if(boardGUI.getPrimary() != null)
					if(boardGUI.getPrimary().isShowing())
						closeBoard();
			if(!mainscreen.getPrimary().isShowing())
				showMainScreen();
			return;
		}
		
		boardGUI = new BoardGUI();
		BoardController.getInstance().setBoard(boardGUI);
		boardGUI.start(boardGUI.getPrimary());
		boardGUI.initiateGamePlayers(Player.getInstance(0).getNickname(), Player.getInstance(1).getNickname());
		boardGUI.setNewTurn(Game.getInstance().getTurn().getCurrentPlayer().getColor());
		
		if(Board.getInstance().isPlayerStuck(Game.getInstance().getTurn().getCurrentPlayer().getColor())) {
			Player player = BoardController.getInstance().getWinner();
			if(player != null)
				DisplayController.boardGUI.notifyWinner(player.getNickname(), player.getCurrentScore(), player.getColor());
			else
				DisplayController.boardGUI.notifyWinner(null, Integer.MIN_VALUE, PrimaryColor.WHITE);
			closeBoard();
			boardGUI.destruct();
			return;
		}

		GameTimerController fullTimer = new GameTimerController(); 
		Game.getInstance().getTimer().startTimer();
		fullTimer.start();
	}
	
	public void showBoard(Player[] players,ArrayList<Piece> pieces, PrimaryColor turn) {	
		if(players != null) {
			System.out.print("Player 1 : " + players[0].getNickname() + " || ");
			System.out.println("Player 2 : " + players[1].getNickname());
		}
		System.out.println(pieces);
		try {
			Game.getInstance().startGame(players, pieces, (turn.equals(PrimaryColor.WHITE) ? 'W' : 'B'));
		}catch (Exception e) {
			System.out.println(Game.getInstance().getGameTime());
			nicknames.notifyError(e.getMessage());
			e.printStackTrace();
			if(boardGUI != null)
				if(boardGUI.getPrimary() != null)
					if(boardGUI.getPrimary().isShowing())
						closeBoard();
			if(!mainscreen.getPrimary().isShowing())
				showMainScreen();
			return;
		}
		DisplayController.getInstance().closeBoardEit();
		boardGUI = new BoardGUI();
		BoardController.getInstance().setBoard(boardGUI);
		boardGUI.start(boardGUI.getPrimary());
		boardGUI.initiateGamePlayers(Player.getInstance(0).getNickname(), Player.getInstance(1).getNickname());
		boardGUI.setNewTurn(Game.getInstance().getTurn().getCurrentPlayer().getColor());
		
		if(Board.getInstance().isPlayerStuck(Game.getInstance().getTurn().getCurrentPlayer().getColor())) {
			Player player = BoardController.getInstance().getWinner();
			if(player != null)
				DisplayController.boardGUI.notifyWinner(player.getNickname(), player.getCurrentScore(), player.getColor());
			else
				DisplayController.boardGUI.notifyWinner(null, Integer.MIN_VALUE, PrimaryColor.WHITE);
			closeBoard();
			boardGUI.destruct();
			return;
		}

		GameTimerController fullTimer = new GameTimerController(); 
		Game.getInstance().getTimer().startTimer();
		fullTimer.start();
	}
	
	public void showMainScreen() {
		mainscreen = new MainscreenGUI();
		mainscreen.start(mainscreen.getPrimary());

	}
	
	
	public void showManageQuestions(){
		
		manageQuestions = QuestionMgmtController.getInstance().getQuestionScreen();
		try {
			manageQuestions.start(manageQuestions.getPrimary());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showInstructionsGame() {
		instructions = new InstructionsGame();
		//System.out.println(instructions);

		instructions.start(instructions .getPrimary());
		
	}
	public void showPointsTable() {
		points = new pointsTable();
		//System.out.println(instructions);

		points.start(instructions .getPrimary());
		
	}
	
	public void showScoreboard() {
		ScoreBoardController.getInstance().loadHistory();
		scoreboard = new Scoreboard();
		scoreboard.start(scoreboard.getPrimary());
	}
	
	public void showNicknames() {
		nicknames = new Nicknames();
		nicknames.start(nicknames.getPrimary());
	}
	
	public void showNicknames(File file) {
		nicknames = new Nicknames();
		nicknames.start(nicknames.getPrimary());
		nicknames.setFile(file);
	}
	
	public void showNicknames(HashMap<String,String> pieces, PrimaryColor turn) {
		nicknames = new Nicknames();
		nicknames.start(nicknames.getPrimary());
		nicknames.setPieces(pieces);
		nicknames.setTurn(turn);

	}
	
	public void showQuestion(Question question, PrimaryColor turnColor) throws Exception {
		questions = new QuestionGUI(turnColor);
		questions.start(questions.getPrimary());
		HashMap<Integer, String> answers = new HashMap<>();
		
		for(Answer a : question.getAnswers()) {
			answers.put(a.getId(), a.getContent());
		}
		
		questions.loadDesign(question.getId(), question.getContent(), answers, question.getDifficulty());
	}
	
	public void showWinner(String name, int score,PrimaryColor color) {
		winner = new Winner();
		winner.start(winner.getPrimary());
		winner.loadDisplay(name, score, color);
	}
	
	public void showBoardEdit() {
		boardEdit = new BoardEdit();
		boardEdit.start(boardEdit.getPrimary());
	}
	
	
	public void closeBoardEit() {
		boardEdit.getPrimary().hide();
	}
	
	
	public void closeMainscreen() {
		mainscreen.getPrimary().hide();
	}
	
	public void closeBoard() {
		boardGUI.getPrimary().hide();
		boardGUI.destruct();
		Game.destruct();
		Board.destruct();
		Player.destruct();
	}
	
	public void closeScoreboard() {
		scoreboard.getPrimary().hide();
		showMainScreen();
	}
	
	public void closeManageQuestions() {
		QuestionMgmtController.getInstance().getQuestionScreen().getPrimary().hide();
		showMainScreen();

	}
	
	public void closeInstructionsGame() {
		instructions.getPrimary().hide();
		showMainScreen();

	}
	public void closePointstable() {
		points.getPrimary().hide();
		showInstructionsGame();

	}
	
	public void closeWinner() {
		winner.getPrimary().hide();
	}
	
	public void closeNicknames() {
		nicknames.getPrimary().hide();
	}
}
