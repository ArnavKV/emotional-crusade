 // Joshua Jeong and Arnav Kiran Vasishta
// Game Project - This public class currently handles choosing difficulty,
// choosing kingdoms, and ticking down time until the lose panel is displayed.
// GameHolder.java (EmotionalCrusade.java)
// 5/1/2023

import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.lang.NumberFormatException; //This import is cited by John Conlin's TA.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Dimension;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

// Class done by Joshua
public class GameHolder extends JPanel
{
	private boolean isNew; //Must be a FV so that when returned to EmotionalCrusade.java,
	// this FV can be passed to it to make Continue Last Game work as intended
	private boolean isEnded; //Must be a FV to prevent the game from returning to the
	//MainGame panel after returning from KingdomDisplays if the game has already ended
	private boolean isWon; //Must be a FV to switch to the correct win or lose panel
	//if the user returns from KingdomDisplays to the win/lose panel

	// The following ints are the amount values. Each one must be a field variable
	// of the main GameHolder because the amounts are crucial to the game and must
	// be constantly passed around in all classes related to the game.
	private int[] amountsPlayer;
	private int[] amountsCPU1;
	private int[] amountsCPU2;
	private int[] amountsCPU3;
	private int amountGoal;

	private byte difficulty; // Must be a FV because this variable directly affects
	// the way amounts are gained and lost.

	private CardLayout gameLayout; // Must be a FV because the game will constantly
	// switch panels in the CardLayout, so they must use the same CardLayout to be
	// passed around

	private MainGame mg; //Must be a FV to be able to set the correct values of the JLabels
	//on the left side of the panel
	private KingdomDisplays kd; //Must be a FV to update the displayed amount jlabels
	//when needed by passing the same instance down

	//Both LosePanel and WinPanel need to be FVs to access their methods in different classes
	private LosePanel lp;
	private WinPanel wp;
	private EmotionalCrusadeHolder ech; //Must be a FV because EmotionalCrusadeHolder
	// contains a method for creating images, and we need this image for several
	// classes that need to create images
	//The following FVs must be FVs for CustomPanel to change their value
	private int amtStartRange;
	private int amtGoalValue;
	// adds the panels and into the cardlayout
	public GameHolder(EmotionalCrusadeHolder echIn)
	{
		ech = echIn;
		initializeGame();
	}

	// Recreate the panels so every per-game field and listener starts fresh.
	private void initializeGame()
	{
		removeAll();
		amtStartRange = 100;
		amtGoalValue = 800;
		isNew = true;
		isEnded = false;
		isWon = false;
		difficulty = 0;
		amountGoal = 0;
		amountsPlayer = new int[4];
		amountsCPU1 = new int[4];
		amountsCPU2 = new int[4];
		amountsCPU3 = new int[4];

		gameLayout = new CardLayout();
		setLayout(gameLayout);

		KingdomChooser kingdom = new KingdomChooser(this);
		PassagesHolder ph = new PassagesHolder();
		mg = new MainGame(this, kingdom, ph);
		kd = new KingdomDisplays(this);
		DifficultyChooserHolder dch = new DifficultyChooserHolder(this, gameLayout, mg);
		CustomPanel cp = new CustomPanel(this);
		lp = new LosePanel(this);
		wp = new WinPanel(this);

		add(kingdom, "Kingdoms");
		add(dch, "Difficulties");
		add(mg, "Play");
		add(kd, "displayCPU");
		add(lp, "Lose");
		add(wp, "Win");
		add(cp, "Custom");
		gameLayout.show(this, "Difficulties");
	}

	public void restartGame()
	{
		mg.getTimer().stop();
		mg.getPassagesHolder().close();
		initializeGame();
		revalidate();
		repaint();
	}


	public void resetGoal() //This method resets the amount goal.
	{
		isEnded = false;
		isWon = false;
		amountGoal = (int)(Math.random() * 300 + (amtGoalValue + 200*difficulty));
	}


	public int[] resetAmounts(int[] amountsUser) //This method resets the amounts
	// for the player passed in, then returns the amounts for the player passed
	// in. This allows us to use the same method for different users, like the user
	// and all 3 CPUs.
	{
		if (difficulty == 0) // if difficulty is Easy
		{
			for (int i = 0; i < amountsUser.length; i++)
				amountsUser[i] = (int)(Math.random() * amtStartRange + 400);
		}
		else if (difficulty == 1) // if difficulty is Adequate
		{
			for (int i = 0; i < amountsUser.length; i++)
				amountsUser[i] = (int)(Math.random() * amtStartRange + 250);
		}
		else if (difficulty == 2) // if difficulty is Prohibitive
		{
			for (int i = 0; i < amountsUser.length; i++)
				amountsUser[i] = (int)(Math.random() * amtStartRange + 100);
		}
		else if (difficulty == 3) // if difficulty is Macabre
		{
			for (int i = 0; i < amountsUser.length; i++)
				amountsUser[i] = (int)(Math.random() * amtStartRange + 1);
		}
		return amountsUser;
	}


	///THE FOLLOWING METHODS ARE GETTER-SETTER METHODS FOR MOST/ALL FVs
	public int getAmtStartRange()
	{
		return amtStartRange;
	}
	public void setAmtStartRange(int startRangeIn)
	{
		amtStartRange = startRangeIn;
	}
	public int getGoalStartValue()
	{
		return amtGoalValue;
	}
	public void setGoalStartValue(int startIn)
	{
		amtGoalValue = startIn;
	}
	public boolean getNew()
	{
		return isNew;
	}
	public void setNew(boolean isNewIn)
	{
		isNew = isNewIn;
	}
	public boolean getEnded()
	{
		return isEnded;
	}
	public void setEnded(boolean isEndedIn)
	{
		isEnded = isEndedIn;
	}
	public boolean getWon()
	{
		return isWon;
	}
	public void setWon(boolean isWonIn)
	{
		isWon = isWonIn;
	}
	public EmotionalCrusadeHolder getEch()
	{
		return ech;
	}
	public byte getDifficulty()
	{
		return difficulty;
	}
	public void setDifficulty(byte difficultyIn)
	{
		difficulty = difficultyIn;
	}
	public int[] getPlayerAmounts()
	{
		return amountsPlayer;
	}
	public int[] getCPU1Amounts()
	{
		return amountsCPU1;
	}
	public int[] getCPU2Amounts()
	{
		return amountsCPU2;
	}
	public int[] getCPU3Amounts()
	{
		return amountsCPU3;
	}
	public void setPlayerAmounts(int[] amtPlayerIn)
	{
		amountsPlayer = amtPlayerIn;

		for (int i = 0; i < amountsPlayer.length; i++)
		{
			if (amountsPlayer[i] < 0)
				amountsPlayer[i] = 0;
		}
	}
	public void setCPU1Amounts(int[] amtCPU1In)
	{
		amountsCPU1 = amtCPU1In;

		for (int i = 0; i < amountsCPU1.length; i++)
		{
			if (amountsCPU1[i] < 0)
				amountsCPU1[i] = 0;
		}
	}
	public void setCPU2Amounts(int[] amtCPU2In)
	{
		amountsCPU2 = amtCPU2In;

		for (int i = 0; i < amountsCPU2.length; i++)
		{
			if (amountsCPU2[i] < 0)
				amountsCPU2[i] = 0;
		}
	}
	public void setCPU3Amounts(int[] amtCPU3In)
	{
		amountsCPU3 = amtCPU3In;

		for (int i = 0; i < amountsCPU3.length; i++)
		{
			if (amountsCPU3[i] < 0)
				amountsCPU3[i] = 0;
		}
	}
	public CardLayout getGameCards()
	{
		return gameLayout;
	}
	public int getGoal()
	{
		return amountGoal;
	}

	public void setKingdoms(String kingdomPlayerIn, Color playerKingdomColorIn)
	//Method made by Arnav
	//This specific method is necessary in order to change the text of the JLabel
	//in MainGame. It is still considered a setter method.
	{
		mg.getKingdomDisplay().setText("Kingdom: " + kingdomPlayerIn);
		mg.getKingdomDisplay().setForeground(playerKingdomColorIn);

		if (difficulty == -1)
		{
		mg.getDifficultyDisplay().setText("Difficulty: Custom");
		mg.getDifficultyDisplay().setForeground(Color.BLACK);
		}
		else if (difficulty == 0)
		{
		mg.getDifficultyDisplay().setText("Difficulty: Easy");
		mg.getDifficultyDisplay().setForeground(Color.GREEN);
		}
		else if (difficulty == 1)
		{
		mg.getDifficultyDisplay().setText("Difficulty: Adequate");
		mg.getDifficultyDisplay().setForeground(Color.ORANGE);
		}
		else if (difficulty == 2)
		{
		mg.getDifficultyDisplay().setText("Difficulty: Prohibitive");
		mg.getDifficultyDisplay().setForeground(Color.RED);
		}
		else if (difficulty == 3)
		{
		mg.getDifficultyDisplay().setText("Difficulty: Macabre");
		mg.getDifficultyDisplay().setForeground(new Color(54, 7, 17));
		}

		mg.getGoldDisplay().setText("Gold: " + getPlayerAmounts()[0]);
		mg.getRssDisplay().setText("Resources: " + getPlayerAmounts()[1]);
		mg.getMenDisplay().setText("Men: " + getPlayerAmounts()[2]);
		mg.getLandDisplay().setText("Land: " + getPlayerAmounts()[3]);
		mg.getGoalDisplay().setText("Amount Goal For Each: " + getGoal());
	}

	public MainGame getGame()
	{
		return mg;
	}
	public KingdomDisplays getKingdomDisplayer()
	{
		return kd;
	}
	public LosePanel getLosePanel()
	{
		return lp;
	}
	public WinPanel getWinPanel()
	{
		return wp;
	}
}

// Class done by Joshua; Overlimit done by Arnav
class KingdomChooser extends JPanel implements MouseListener
{
	// The following are variables for the player AND the three opponent kingdoms
	// Must be a FV because the panel to actually play the
	// game will constantly need to know which kingdom is being played for
	private String kingdomPlayer;
	private String kingdomCPU1;
	private String kingdomCPU2;
	private String kingdomCPU3;
	private Color playerKingdomColor;
	private Image kindomsImg;

	private boolean overLimit;//Must be a FV to check if the question limit in MainGame
	//was reached, then run specific code if so
	private JLabel clickInfo;//Must be a FV to update the JLabel instructions on
	//what to do
	private GameHolder gh; //Must be a FV because the methods in GameHolder must
	// be accessible in mouseClicked, and the same instance must be passed in

	private JLabel displayLimitForLimit; //made by Arnav
	//Must be a FV so that the JLabel saves
	public KingdomChooser(GameHolder ghIn)
	{
		setLayout(new BorderLayout());
		kingdomPlayer = "";
		kingdomCPU1 = "";
		kingdomCPU2 = "";
		kingdomCPU3 = "";
		playerKingdomColor = Color.WHITE;
		overLimit = false;
		gh = ghIn;
		setBackground(Color.RED);
		addMouseListener(this);

		clickInfo = new JLabel("CLICK ON ONE KINGDOM TO PLAY FOR IT", JLabel.CENTER);
		clickInfo.setFont(new Font("Serif", Font.ITALIC, 25));
		clickInfo.setForeground(Color.BLACK);
		displayLimitForLimit = new JLabel("", JLabel.CENTER);
		displayLimitForLimit.setFont(new Font("Serif", Font.ITALIC, 25));
		displayLimitForLimit.setForeground(Color.BLACK);
		add(clickInfo, BorderLayout.NORTH);
		add(displayLimitForLimit, BorderLayout.SOUTH);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.drawImage(gh.getEch().getImage("ech_kingdom_panel.png"), 0,0, 1360,720, this); // draws the image
		g.drawRect(1360/2, 0, 4, 720); //This DRAWS a 2x2 grid to distinguish each
		g.drawRect(0, 720/2, 1360, 4); //kingdom when clicking
		g.setColor(Color.BLACK);

		Font kingdomFont = new Font("Serif", Font.BOLD, 30);
		g.setFont(kingdomFont);
		g.drawString("KINGDOM HAPPY", 280, 180);
		g.drawString("KINGDOM SAD", 280, 540);
		g.drawString("KINGDOM FEAR", 880, 540);
		g.setColor(Color.WHITE);
		g.drawString("KINGDOM ANGRY", 880, 180);
	}

	// gets which kingdom in image was selected by player and initializes the variables
	public void mouseClicked(MouseEvent evt)
	{
		int xPos = evt.getX();
		int yPos = evt.getY();
		if(overLimit)
		{
			overLimitChooser(xPos, yPos);
		}
		else
		{
			gh.resetAmounts(gh.getPlayerAmounts());
			if (xPos >= 0 && xPos <= 1360/2 && yPos >= 0 && yPos <= 720/2)
			//if they click the kingdom for Happy
			{
				playerKingdomColor = Color.YELLOW;
				kingdomPlayer = "Happy";
				kingdomCPU1 = "Sad";
				kingdomCPU2 = "Angry";
				kingdomCPU3 = "Fear";
			}
			else if (xPos >= 0 && xPos <= 1360/2 && yPos >= 720/2 && yPos <= 720)
			// if they click the kingdom for Sad
			{
				playerKingdomColor = Color.BLUE;
				kingdomPlayer = "Sad";
				kingdomCPU1 = "Angry";
				kingdomCPU2 = "Fear";
				kingdomCPU3 = "Happy";
			}
			else if (xPos >= 1360/2 && xPos <= 1360 && yPos >= 0 && yPos <= 720/2)
			//if they click the kingdom for Angry
			{
				playerKingdomColor = Color.RED;
				kingdomPlayer = "Angry";
				kingdomCPU1 = "Fear";
				kingdomCPU2 = "Happy";
				kingdomCPU3 = "Sad";
			}
			else if (xPos >= 1360/2 && xPos <= 1360 && yPos >= 720/2 && yPos <= 720)
			//if they click the kingdom for Fear
			{
				playerKingdomColor = new Color(127, 0, 255);
				kingdomPlayer = "Fear";
				kingdomCPU1 = "Happy";
				kingdomCPU2 = "Sad";
				kingdomCPU3 = "Angry";
			}
			gh.setCPU1Amounts(gh.resetAmounts(gh.getCPU1Amounts()));
			gh.setCPU2Amounts(gh.resetAmounts(gh.getCPU2Amounts()));
			gh.setCPU3Amounts(gh.resetAmounts(gh.getCPU3Amounts()));

			gh.getGameCards().show(gh, "Play");
			gh.setKingdoms(kingdomPlayer, playerKingdomColor);
			gh.getGame().getQuestionsRemaining().setText("Questions Limit: " + 8 + "/8");
			gh.getGame().getTimer().start();
		}
	}

	public void mousePressed(MouseEvent evt) {}
	public void mouseReleased(MouseEvent evt) {}
	public void mouseEntered(MouseEvent evt) {}
	public void mouseExited(MouseEvent evt) {}
	
	//Method done by Arnav
	public void overLimitChooser(int xPos, int yPos)
	//This method runs the selection for a DIFFERENT kingdom if the user reaches
	//the question limit
	{
		int [] placeholder = new int[4];
		if (xPos >= 0 && xPos <= 1360/2 && yPos >= 0 && yPos <= 720/2)
		//if they click the kingdom for Happy
		{
			if(!(kingdomPlayer.equals("Happy"))) //If they have not chosen Happy
			{
				if (kingdomCPU1.equals("Happy"))
				{
					placeholder = gh.getCPU1Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{ //If the kingdom the player is switching to has 0 amounts,
						//the amounts are increased to prevent the player from
						//instantly losing
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU1Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU2.equals("Happy"))
				{
					placeholder = gh.getCPU2Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU2Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU3.equals("Happy"))
				{
					placeholder = gh.getCPU3Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU3Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}

				playerKingdomColor = Color.YELLOW;
				kingdomPlayer = "Happy";
				kingdomCPU1 = "Sad";
				kingdomCPU2 = "Angry";
				kingdomCPU3 = "Fear";
				gh.getGameCards().show(gh, "Play");
			}
			else
				clickInfo.setText("Try Again, Click on a Kingdom that you have not choosen before");
		}
		else if (xPos >= 0 && xPos <= 1360/2 && yPos >= 720/2 && yPos <= 720)
		// if they click the kingdom for Sad
		{
			if(!(kingdomPlayer.equals("Sad"))) //If they have not chosen Sad
			{
				if (kingdomCPU1.equals("Sad"))
				{
					placeholder = gh.getCPU1Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{ //If the kingdom the player is switching to has 0 amounts,
						//the amounts are increased to prevent the player from
						//instantly losing
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU1Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU2.equals("Sad"))
				{
					placeholder = gh.getCPU2Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU2Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU3.equals("Sad"))
				{
					placeholder = gh.getCPU3Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU3Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}

				playerKingdomColor = Color.BLUE;
				kingdomPlayer = "Sad";
				kingdomCPU1 = "Angry";
				kingdomCPU2 = "Fear";
				kingdomCPU3 = "Happy";
				gh.getGameCards().show(gh, "Play");
			}
			else
				clickInfo.setText("Try Again, Click on a Kingdom that you have not choosen before");
		}
		else if (xPos >= 1360/2 && xPos <= 1360 && yPos >= 0 && yPos <= 720/2)
		//if they click the kingdom for Angry
		{
			if(!(kingdomPlayer.equals("Angry"))) //If they have not chosen Angry
			{
				if (kingdomCPU1.equals("Angry"))
				{
					placeholder = gh.getCPU1Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{ //If the kingdom the player is switching to has 0 amounts,
						//the amounts are increased to prevent the player from
						//instantly losing
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU1Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU2.equals("Angry"))
				{
					placeholder = gh.getCPU2Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU2Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU3.equals("Angry"))
				{
					placeholder = gh.getCPU3Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU3Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}

				playerKingdomColor = Color.RED;
				kingdomPlayer = "Angry";
				kingdomCPU1 = "Fear";
				kingdomCPU2 = "Happy";
				kingdomCPU3 = "Sad";
				gh.getGameCards().show(gh, "Play");
			}
			else
				clickInfo.setText("Try Again, Click on a Kingdom that you have not choosen before");
		}
		else if (xPos >= 1360/2 && xPos <= 1360 && yPos >= 720/2 && yPos <= 720)
		//if they click the kingdom for Fear
		{
			if(!(kingdomPlayer.equals("Fear"))) //If they have not chosen Fear
			{
				if (kingdomCPU1.equals("Fear"))
				{
					placeholder = gh.getCPU1Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{ //If the kingdom the player is switching to has 0 amounts,
						//the amounts are increased to prevent the player from
						//instantly losing
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU1Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU2.equals("Fear"))
				{
					placeholder = gh.getCPU2Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU2Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}
				else if (kingdomCPU3.equals("Fear"))
				{
					placeholder = gh.getCPU3Amounts();
					if (placeholder[0] == 0 && placeholder[1] == 0 && placeholder[2] == 0 && placeholder[3] == 0)
					{
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] += 100;
					}
					else if (placeholder[0] >= gh.getGoal() && placeholder[1] >= gh.getGoal() && placeholder[2] >= gh.getGoal() && placeholder[3] >= gh.getGoal())
					{ //If the kingdom the player is switching to already reached
						//the amount goal, the amounts are decreased to prevent the
						//player from instantly winning
						for (int i = 0; i < placeholder.length; i++)
							placeholder[i] -= 200;
					}
					gh.setCPU3Amounts(gh.getPlayerAmounts());
					gh.setPlayerAmounts(placeholder);
				}

				playerKingdomColor = new Color(127, 0, 255);
				kingdomPlayer = "Fear";
				kingdomCPU1 = "Happy";
				kingdomCPU2 = "Sad";
				kingdomCPU3 = "Angry";
				gh.getGameCards().show(gh, "Play");
			}
			else
			clickInfo.setText("Try Again, Click on a Kingdom that you have not choosen before");
		}

		int time = 0;
		time = gh.getGame().getTime();
		if(time < 30) //Double the time left if the time is less than 30 seconds
		{
		time = time + time;
		gh.getGame().setTime(time);
		}
		gh.setKingdoms(kingdomPlayer, playerKingdomColor); //Update the JLabels properly
		//Reset the question limit
		gh.getGame().setQuestionCount(8);
		gh.getGame().getQuestionsRemaining().setText("Questions Remaining: " +
		gh.getGame().getQuestionCount() + "/8");
		
		gh.getGame().getTimer().start(); //Starts the timer again
	}

	///THE FOLLOWING METHODS ARE GETTER SETTER METHODS
	public String getKingdomPlayer()
	{
		return kingdomPlayer;
	}
	public String getKingdomCPU1()
	{
		return kingdomCPU1;
	}
	public String getKingdomCPU2()
	{
		return kingdomCPU2;
	}
	public String getKingdomCPU3()
	{
		return kingdomCPU3;
	}
	public Color getPlayerKingdomColor()
	{
		return playerKingdomColor;
	}
	public void setOverLimit(boolean overLimitIn)
	{
		overLimit = overLimitIn;
		int limitForQuestionLimits = gh.getGame().getLimitForQuestionLimits();
		clickInfo.setText("You Are OUT OF QUESTIONS, CLICK ON ANOTHER KINGDOM TO PLAY FOR IT");
		displayLimitForLimit.setText("You WILL LOSE if you reach this " + limitForQuestionLimits + " more times!");
	}
}

// Class done by Arnav
// has the layout for the panels
class DifficultyChooserHolder extends JPanel
{
	private GameHolder gh; // card layout used in the nested handler class
	// to switch panels
	private CardLayout gameLayout; // used in the nested handler class
	// to switch panels
	private MainGame mg; // Needed to call methods to change values in the SAME
	//MainGame instance
	//Must be a FV so the handler class can check if these are selected or not
	private JCheckBox tre; 
	private JCheckBox tas;
	public DifficultyChooserHolder(GameHolder ghIn, CardLayout gameLayoutIn, MainGame mgIn)
	{
		gh = ghIn;
		mg = mgIn;
		gameLayout = gameLayoutIn;
		setLayout(new BorderLayout());

		ToggleEffects te = new ToggleEffects();
		add(te, BorderLayout.SOUTH);
		DifficultyChooser dc = new DifficultyChooser();
		add(dc, BorderLayout.CENTER);
	}

	// Creates and adds the buttons to the grid layout
	class DifficultyChooser extends JPanel
	{
		public DifficultyChooser()
		{
			setLayout(new GridLayout(2,2));
			DifficultyChooserHandler dch = new DifficultyChooserHandler();

			JButton easy = new JButton("Easy");
			easy.setBackground(Color.GREEN);
			easy.addActionListener(dch);
			add(easy);

			JButton adequate = new JButton("Adequate");
			adequate.setBackground(Color.ORANGE);
			adequate.addActionListener(dch);
			add(adequate);

			JButton prohibitive = new JButton("Prohibitive");
			prohibitive.setBackground(Color.RED);
			prohibitive.addActionListener(dch);
			add(prohibitive);

			JButton macabre = new JButton("Macabre");
			macabre.setBackground(new Color(54, 7, 17));
			macabre.setForeground(Color.WHITE);
			macabre.addActionListener(dch);
			add(macabre);
		}
	}

	// handler class
	class DifficultyChooserHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent evt )
		{
			byte difficulty = 0;
			String command = evt.getActionCommand();

			if ( command.equals("Easy"))
			{
				difficulty = 0;
			}
			else if( command.equals("Adequate"))
			{
				difficulty = 1;
			}
			else if( command.equals("Prohibitive"))
			{
				difficulty = 2;
			}
			else if(command.equals("Macabre"))
			{
				difficulty = 3;
			}

			gameLayout.show(gh,"Kingdoms");
			gh.setDifficulty(difficulty);
			gh.resetGoal();
			mg.getQuestion();
		}
	}

	//bottom bar in BorderLayout.CENTER
	class ToggleEffects extends JPanel
	{
		public ToggleEffects()
		{
			setBackground(Color.BLACK);
			setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
			ToggleHandler th = new ToggleHandler();

			tre = new JCheckBox("Toggle Random Events");
			tre.setBackground(Color.BLACK);
			tre.setForeground(new Color(220, 220, 220));
			tre.setFont(new Font("Serif", Font.PLAIN, 16));
			tre.setSelected(true);
			tre.addActionListener(th);
			add(tre);

			tas = new JCheckBox("Toggle Amount Stealing");
			tas.setBackground(Color.BLACK);
			tas.setFont(new Font("Serif", Font.PLAIN, 16));
			tas.setForeground(new Color(220, 220, 220));
			tas.setSelected(true);
			tas.addActionListener(th);
			add(tas);

			JButton customMode = new JButton("Custom Mode");
			customMode.setBackground(new Color(113, 113, 63));
			customMode.setForeground(Color.WHITE);
			customMode.setFont(new Font("Times New Roman", Font.BOLD, 20));
			customMode.addActionListener(th);
			add(customMode);
		}

	}

	class ToggleHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();
			if (command.equals("Custom Mode"))
			{
				gh.getGameCards().show(gh, "Custom");
			}
			else if(command.equals("Toggle Random Events"))
			{
				if (tre.isSelected() == false)
					mg.setEventsActive(false); //Turns off random events
				else
					mg.setEventsActive(true);
			}
			else if(command.equals("Toggle Amount Stealing"))
			{
				if (tas.isSelected() == false)
					mg.setStealingActive(false); //Turns off amount stealing
				else
					mg.setEventsActive(true);
			}
		}
	}
}

// Class done by both Joshua and Arnav
class MainGame extends JPanel
{
	private GameHolder gh; // Must be a FV because MANY of the variables in
	//GameHolder need to be used for the main game, and it's much easier to use
	//GameHolder itself instead of FVs for each variable we need

	private KingdomChooser kc; // Must be a FV because there are variables for
	//identifying which kingdom who is in, which will be necessary to the game

	private Timer timer; // to stop when a minute is done in actionPerformed
	private GameBottomBar gbb; //Must be a FV because AmountEvents must be able to
	//access the same GameBottomBar in order to perform the event of amount pausing

	private int timeLeft; // the number of time the user has which is printed in the Label
	// is a FV to update in actionPerformed.

	///The following variables need to be FVs because GameHolder will change their
	///text properly (and not unsuccessfully), so these JLabels must be sent to
	///GameHolder to do so

	private JLabel kingdomDisplay; // displays the kingdom the user choose
	private JLabel difficultyDisplay; // displays the difficulty the user choose
	private JLabel questionsRemaining; //displays the questions the user has left for
	// their current kingdom and also used to update the questions remaining in the
	// getQuestions().

	// these JLabels display the amounts the PLAYER has
	private JLabel amountsForGold;
	private JLabel amountsForRss;
	private JLabel amountsForMen;
	private JLabel amountsForLand;
	private JLabel goalDisplay;

	private JTextArea question; // used to update the questions in getQuestions().
	///The text changing FVs end above this comment

	private PassagesHolder ph; //Must be a FV because Passages holds the Scanner
	// that we crucially need to access the question + answer

	private int questionCount; //Must be a FV because this will be updated with two
	//classes working at once: the questions class and the answers class

	private int questionAnswer; //Must be a FV because the class that deals with getting
	//the question must set the answer in that class, and that acquired answer must be
	//sent to the handler class for the answering buttons

	///private String passageQuestion; //Must be a FV so that passageQuestion is saved after
	//calling getQuestion so there is no error made

	//The following FVs must be FVs so that they are not reset every time the getQuestion
	//method is called and can therefore save their values
	private boolean[] happyRepeated;
	private boolean[] sadRepeated;
	private boolean[] angryRepeated;
	private boolean[] fearRepeated;
	private int result; //Must be a FV because when the game ends, this int displays
	//the number of questions the user got right, and the classes to deal with showing
	//the questions the user got right are all nested in this class, so it's easier
	//to declare the FV up in the main class

	private JTextArea events; //Must be a FV because there are two nested classes that
	// are dealing with the same exact text area

	//The two FVs below this comment must be FVs because we must be able to choose just
	//one to display to the game depending on if the user answered the question right
	//or not
	private String correctExplain;
	private String wrongExplain;
	
	//The two FVs below this comment must be FVs to be passed on into the win/lose
	//panel; they must be seperate otherwise one won't be shown in the other
	private JLabel loseResultsDisplay;
	private JLabel winResultsDisplay;

	//The two FVs below this comment must be FVs in order to be passed into the
	//ActionListener that toggles events and stealing
	private boolean eventsActive;
	private boolean stealingActive;

	private int limitForQuestionLimits; //Must be a FV to be passed to KingdomChooser,
	//specifically overlimit, to end the game if the user runs overlimit too many times
	private CenterQuestions cq; //Must be a FV so that CustomPanel can call a method
	//in the same CenterQuestions instance to update the JButtons for emotions

	//The following FVs must be FVs in order for CustomPanel to change their amounts
	//to change the game to the user's liking
	private int correctGain;
	private int wrongLoss;
	private boolean[] emotionOn;
	private boolean isCustomPassages; //This boolean will allow questions to repeat if
	//passages available are changed in CustomPanel.

	public MainGame(GameHolder ghIn, KingdomChooser kcIn, PassagesHolder phIn)
	{
		correctGain = 460;
		wrongLoss = 100;
		emotionOn = new boolean[]{true, true, true, true};
		isCustomPassages = false;
		stealingActive = true;
		eventsActive = true;

		ph = phIn;
		gh = ghIn;
		kc = kcIn;
		questionCount = 8;
		timeLeft = 59;
		correctExplain = "";
		wrongExplain = "";
		happyRepeated = new boolean[]{false, false, false, false, false, false};
		sadRepeated = new boolean[]{false, false, false, false, false, false};
		angryRepeated = new boolean[]{false, false, false, false, false, false};
		fearRepeated = new boolean[]{false, false, false, false, false, false};
		result = 0;
		limitForQuestionLimits = 3;
		loseResultsDisplay = new JLabel("You Got Correct", JLabel.CENTER);
		winResultsDisplay = new JLabel("You Got Correct", JLabel.CENTER);
		setLayout(new BorderLayout());

		LeftSideInfo lsi = new LeftSideInfo();
		add(lsi, BorderLayout.WEST);
		RightSideInfo rsi = new RightSideInfo();
		add(rsi, BorderLayout.EAST);
		cq = new CenterQuestions();
		add(cq, BorderLayout.CENTER);
		gbb = new GameBottomBar();
		add(gbb, BorderLayout.SOUTH);
		timer = new Timer(1000, gbb);
	}
	///THE FOLLOWING METHODS ARE GETTER SETTER METHODS
	public boolean getIsCustomPassages()
	{
		return isCustomPassages;
	}
	public void setIsCustomPassages(boolean isCPassIn)
	{
		isCustomPassages = isCPassIn;
	}
	public boolean[] getHappyRepeated()
	{
		return happyRepeated;
	}
	public void setHappyRepeated(boolean[] repeatedIn)
	{
		happyRepeated = repeatedIn;
	}
	public boolean[] getSadRepeated()
	{
		return sadRepeated;
	}
	public void setSadRepeated(boolean[] repeatedIn)
	{
		sadRepeated = repeatedIn;
	}
	public boolean[] getAngryRepeated()
	{
		return angryRepeated;
	}
	public void setAngryRepeated(boolean[] repeatedIn)
	{
		angryRepeated = repeatedIn;
	}
	public boolean[] getFearRepeated()
	{
		return fearRepeated;
	}
	public void setFearRepeated(boolean[] repeatedIn)
	{
		fearRepeated = repeatedIn;
	}
	public int getLimitForQuestionLimits()
	{
		return limitForQuestionLimits;
	}
	public void setEventsActive(boolean eventsActiveIn)
	{
		eventsActive = eventsActiveIn;
	}
	public void setStealingActive(boolean stealingActiveIn)
	{
		stealingActive = stealingActiveIn;
	}
	public GameBottomBar getGameBottomBar()
	{
		return gbb;
	}
	public KingdomChooser getKingdomChooser()
	{
		return kc;
	}
	public PassagesHolder getPassagesHolder()
	{
		return ph;
	}
	public CenterQuestions getCenterQuestions()
	{
		return cq;
	}
	public int getResult()
	{
		return result;
	}
	public JLabel getLoseResultsDisplay()
	{
		return loseResultsDisplay;
	}
	public JLabel getWinResultsDisplay()
	{
		return winResultsDisplay;
	}
	public JLabel getKingdomDisplay()
	{
		return kingdomDisplay;
	}
	public JLabel getDifficultyDisplay()
	{
		return difficultyDisplay;
	}
	public JLabel getQuestionsRemaining()
	{
		return questionsRemaining;
	}
	public JLabel getGoldDisplay()
	{
		return amountsForGold;
	}
	public JLabel getRssDisplay()
	{
		return amountsForRss;
	}
	public JLabel getMenDisplay()
	{
		return amountsForMen;
	}
	public JLabel getLandDisplay()
	{
		return amountsForLand;
	}
	public Timer getTimer()
	{
		return timer;
	}
	public JLabel getGoalDisplay()
	{
		return goalDisplay;
	}
	public void setTime(int timeLeftIn)
	{
		timeLeft = timeLeftIn;
	}
	public int getTime()
	{
		return timeLeft;
	}
	public int getQuestionCount()
	{
		return questionCount;
	}
	public void setQuestionCount(int questionCountIn)
	{
		questionCount = questionCountIn;
	}
	public int getCorrectGain()
	{
		return correctGain;
	}
	public void setCorrectGain(int gainIn)
	{
		correctGain = gainIn;
	}
	public int getWrongLoss()
	{
		return wrongLoss;
	}
	public void setWrongLoss(int lossIn)
	{
		wrongLoss = lossIn;
	}
	public boolean[] getEmotionOn()
	{
		return emotionOn;
	}
	public void setEmotionOn(boolean[] emotionOnIn)
	{
		emotionOn = emotionOnIn;
	}
	//Class done by Joshua
	// has the info on the left of the game panel, which are the kingdom and difficulty
	//the user is playing on, and the 4 amount values that the user has
	class LeftSideInfo extends JPanel
	{
		public LeftSideInfo()
		{
			setLayout(new GridLayout(6, 1, 7, 15));
			setBackground(new Color(220, 220, 220));

			kingdomDisplay = new JLabel("Kingdom: ", JLabel.CENTER);
			kingdomDisplay.setForeground(kc.getPlayerKingdomColor());
			kingdomDisplay.setBackground(Color.BLACK);

			difficultyDisplay = new JLabel("Difficulty: ", JLabel.CENTER);
			difficultyDisplay.setBackground(Color.BLACK);

			add(kingdomDisplay);
			add(difficultyDisplay);

			amountsForGold = new JLabel("Gold: ", JLabel.CENTER);
			amountsForRss = new JLabel("Resources: ", JLabel.CENTER);
			amountsForMen = new JLabel("Men: ", JLabel.CENTER);
			amountsForLand = new JLabel("Land: ", JLabel.CENTER);
			add(amountsForGold);
			add(amountsForRss);
			add(amountsForMen);
			add(amountsForLand);
		}
	}

	//Class done by Joshua
	// has the info on the right side of the game, including the image of the kingdoms,
	// as well as the amount goal, and a JTextArea for displaying events that happen
	class RightSideInfo extends JPanel
	{
		public RightSideInfo()
		{
			setLayout(new GridLayout(2, 1));
			ImageHolder ih = new ImageHolder();
			add(ih);
			InformationDisplay id = new InformationDisplay();
			add(id);
		}
	}

	//Class done by Arnav
	//Holds the image that the user can click to see amounts for all kingdoms
	class ImageHolder extends JPanel implements MouseListener
	{
		// contains the amount goal
		public ImageHolder()
		{
			setLayout(new BorderLayout());
			setBackground(new Color(40, 40, 40));
			goalDisplay = new JLabel("Amount Goal For Each: 0");
			goalDisplay.setForeground(Color.ORANGE);
			goalDisplay.setFont(new Font("Serif", Font.BOLD, 20));
			goalDisplay.setBackground(Color.BLACK);
			add(goalDisplay, BorderLayout.SOUTH);
			addMouseListener(this);
		}

		// draw the image at the top right corner
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(gh.getEch().getImage("ech_kingdom_panel.png"), 0, 0, 250,
				250, this);
		}

		public void mouseClicked(MouseEvent evt)
		{
			int xClick = evt.getX();
			int yClick = evt.getY();
			if (xClick >= 0 && xClick <= 250 && yClick >= 0 && yClick <= 250)
			{
				gh.getGameCards().show(gh, "displayCPU");
				timer.stop();
			}
		}
		public void mousePressed(MouseEvent evt){} public void mouseReleased(MouseEvent evt){}
		public void mouseEntered(MouseEvent evt){} public void mouseExited(MouseEvent evt){}
	}


	//Class done by Joshua
	//This class displays the events that happen across the game.
	class InformationDisplay extends JPanel
	{
		// the text area with the events will be added here
		public InformationDisplay()
		{
			setLayout(new GridLayout(1, 1));
			events = new JTextArea("Events will be displayed here.");
			events.setLineWrap(true);
			events.setWrapStyleWord(true);
			events.setMargin(new Insets(6, 6, 6, 6));
			events.setEditable(false);
			events.setBackground(new Color(100, 100, 100));
			events.setForeground(Color.YELLOW);

			JScrollPane scroll = new JScrollPane(events);
			scroll.setBackground(new Color(150, 170, 180));
			add(scroll);
		}
	}


	// Class done by Arnav
	//This class displays both the passage as a question, and the 4 buttons for answering.
	class CenterQuestions extends JPanel
	{
		private AnswerButtons ab; //Must be a FV so that the class's JButtons can be
		//accessed to update in the method
		// adds the JButtons for answering and the TextArea for the questions
		public CenterQuestions()
		{
			setLayout(new GridLayout(2, 1));
			QuestionDisplay qd = new QuestionDisplay();
			add(qd);
			ab = new AnswerButtons();
			add(ab);
		}
	   
		public void checkButtonActive() //Will update the JButtons to say proper text
		//if they were updated in CustomPanel
		{
			if (getEmotionOn()[0] == false)
			{
				ab.getHappyButton().setForeground(Color.BLACK);
				ab.getHappyButton().setText("Happy (unused)");
				ab.getHappyButton().setEnabled(false);
			}
			else
			{
				ab.getHappyButton().setForeground(Color.YELLOW);
				ab.getHappyButton().setText("Happy");
				ab.getHappyButton().setEnabled(true);
			}
		   
			if (getEmotionOn()[1] == false)
			{
				ab.getSadButton().setForeground(Color.BLACK);
				ab.getSadButton().setText("Sad (unused)");
				ab.getSadButton().setEnabled(false);
			}
			else
			{
				ab.getSadButton().setForeground(Color.BLUE);
				ab.getSadButton().setText("Sad");
				ab.getSadButton().setEnabled(true);
			}
		   
			if (getEmotionOn()[2] == false)
			{
				ab.getAngryButton().setForeground(Color.BLACK);
				ab.getAngryButton().setText("Angry (unused)");
				ab.getAngryButton().setEnabled(false);
			}
			else
			{
				ab.getAngryButton().setForeground(Color.RED);
				ab.getAngryButton().setText("Angry");
				ab.getAngryButton().setEnabled(true);
			}
		   
			if (getEmotionOn()[3] == false)
			{
				ab.getFearButton().setForeground(Color.BLACK);
				ab.getFearButton().setText("Fear (unused)");
				ab.getFearButton().setEnabled(false);
			}
			else
			{
				ab.getFearButton().setForeground(new Color(127, 0, 255));
				ab.getFearButton().setText("Fear");
				ab.getFearButton().setEnabled(true);
			}
		}
	}

	// Class done by Arnav and Joshua
	class QuestionDisplay extends JPanel
	{
		// contains the TextArea for the Questions, as well as the amounts gained
		// and lost based on if the user answered right or not.
		public QuestionDisplay()
		{
			setBackground(new Color(60, 60, 60));
			question = new JTextArea("Questions and Passages will be displayed here");
			question.setMargin(new Insets(6, 6, 6, 6));
			question.setEditable(false);
			question.setBackground(new Color(90, 90, 90));
			question.setForeground(new Color(230, 230, 230));
			question.setPreferredSize(new Dimension(500, 250));
			question.setLineWrap(true);
			question.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane();
			question.add(scroll);
			add(question);
		}
	}

	// Method done by Arnav and a little of Joshua; Massive credits to Scott DeRuiter
	private QuestionBank questionBank = new QuestionBank(new File("passages.txt"));

	public void getQuestion()
	{
		QuestionBank.Question selected = questionBank.choose(gh.getDifficulty(), emotionOn,
			isCustomPassages);
		if (selected == null)
		{
			timer.stop();
			gh.setEnded(true);
			gh.getLosePanel().setEndgameDifficulty(gh.getDifficulty());
			gh.getLosePanel().updateEndDifficulty();
			displayResults();
			gh.getGameCards().show(gh, "Lose");
			javax.swing.JOptionPane.showMessageDialog(this,
				"No valid questions are available for the selected difficulty and emotions.\n" +
				"Check passages.txt or enable another emotion, then restart.");
			return;
		}
		String passageQuestion = selected.passage;
		questionAnswer = selected.answer;
		correctExplain = selected.correct;
		wrongExplain = selected.wrong;

		questionCount--;
		if (questionCount == 0) //If the question limit reaches zero, the user is taken to overlimit and overLimitChooser
		{
			limitForQuestionLimits--;
			if(limitForQuestionLimits == 0) //If the user has reached the question limit 3 times, they lose
			{
				timer.stop();
				gh.setEnded(true);
				gh.getLosePanel().setEndgameDifficulty(gh.getDifficulty());
				gh.getLosePanel().updateEndDifficulty();
				ph.getPrintWriter().println(result);
				gh.getGameCards().show(gh, "Lose");
				ph.getPrintWriter().close();
				displayResults();
			}
			else //If the user hasnt reached the limit for question limit, they are taken to overlimit
			{
				timer.stop();
				kc.setOverLimit(true);
				gh.getGameCards().show(gh, "Kingdoms");
			}
		}
		question.setText(passageQuestion); //This is the JTextArea with the passage
	}

	// Class done by Arnav
	//This class creates the buttons for answering the questions, and also adds the
	//handler class to those buttons.
	class AnswerButtons extends JPanel
	{
		//The following FVs are to pass down to the handler class in order for them
		// to be updated based on what the user entered
		private JButton happy;
		private JButton sad;
		private JButton angry;
		private JButton fear;
		
		public AnswerButtons()
		{
			setLayout(new GridLayout(2,2));
			setFont(new Font("Serif", Font.BOLD, 24));
			AnswerButtonsHandler abh = new AnswerButtonsHandler(ph, this);

			happy = new JButton("Happy");
			happy.setBackground(new Color(197, 197, 154));
			happy.setForeground(Color.YELLOW);
			happy.addActionListener(abh);
			add(happy);

			sad = new JButton("Sad");
			sad.setBackground(new Color(200, 210, 219));
			sad.addActionListener(abh);
			sad.setForeground(Color.BLUE);
			add(sad);

			angry = new JButton("Angry");
			angry.setBackground(new Color(189, 167, 161));
			angry.addActionListener(abh);
			angry.setForeground(Color.RED);
			add(angry);

			fear = new JButton("Fear");
			fear.setBackground(new Color(222, 210, 233));
			fear.addActionListener(abh);
			fear.setForeground(new Color(127, 0, 255));
			add(fear);
		}

		///THE FOLLOWING ARE GETTER SETTER METHODS
		public JButton getHappyButton()
		{
			return happy;
		}
		public JButton getSadButton()
		{
			return sad;
		}
		public JButton getAngryButton()
		{
			return angry;
		}
		public JButton getFearButton()
		{
			return fear;
		}
	}

	//Class done by Joshua
	//This class increases and decreases amounts, shows correct or wrong explanation,
	//based on if the user answered the question right or wrong; also adds to the
	//question limit each time answered, resets appropriate values on SECOND click,
	//adds to how many questions the user got right, and displays what amounts went up/down
	class AnswerButtonsHandler implements ActionListener
	{
		private AnswerButtons ab; //Must be a FV because the JLabels that are clicked as
		// part of the answers must be highlighted green or red
		private boolean isAnswerClicked; //Must be a FV in order to reset the JLabels once
		//the actionPerformed is ran
		public AnswerButtonsHandler(PassagesHolder phIn, AnswerButtons abIn)
		{
			ph = phIn;
			ab = abIn;
			isAnswerClicked = false;
			result = 0;
		}
		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();
			int[] newPlayerAmounts = new int[4];
			int[] newCPU1Amounts = new int[4];
			int[] newCPU2Amounts = new int[4];
			int[] newCPU3Amounts = new int[4];
			for (int i = 0; i < newPlayerAmounts.length; i++)
			{
				newPlayerAmounts[i] = gh.getPlayerAmounts()[i];
				newCPU1Amounts[i] = gh.getCPU1Amounts()[i];
				newCPU2Amounts[i] = gh.getCPU2Amounts()[i];
				newCPU3Amounts[i] = gh.getCPU3Amounts()[i];
			}
			if (isAnswerClicked == false)
			{
			if (questionAnswer == 0)
			ab.getHappyButton().setBackground(new Color(135, 255, 135));
			else if (questionAnswer == 1)
			ab.getSadButton().setBackground(new Color(135, 255, 135));
			else if (questionAnswer == 2)
			ab.getAngryButton().setBackground(new Color(135, 255, 135));
			else if (questionAnswer == 3)
			ab.getFearButton().setBackground(new Color(135, 255, 135));

			if (command.equals("Happy"))
			{
				if (questionAnswer == 0)
				{
					question.setText(correctExplain);
					result++;
					if (kc.getKingdomPlayer().equals("Happy"))
					{
					for (int i = 0; i < gh.getPlayerAmounts().length; i++)
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + correctGain - (gh.getDifficulty() * 60);
					question.append("\n\nCRITICAL! Player (" + kc.getKingdomPlayer() +") has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					}
					else
					{
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + 50 - (gh.getDifficulty() * 10);
					if (kc.getKingdomCPU1().equals("Happy"))
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU2().equals("Happy"))
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU3().equals("Happy"))
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + " has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has lost " + (-newCPU1Amounts[0]+gh.getCPU1Amounts()[0]) +
						" Gold, "+ (-newCPU1Amounts[1]+gh.getCPU1Amounts()[1]) + " Resources, "
						+ (-newCPU1Amounts[2]+gh.getCPU1Amounts()[2]) + " Men, and " +(
						-newCPU1Amounts[3]+gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has lost " + (-newCPU2Amounts[0]+gh.getCPU2Amounts()[0]) +
						" Gold, "+ (-newCPU2Amounts[1]+gh.getCPU2Amounts()[1]) + " Resources, "
						+ (-newCPU2Amounts[2]+gh.getCPU2Amounts()[2]) + " Men, and " +(
						-newCPU2Amounts[3]+gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has lost " + (-newCPU3Amounts[0]+gh.getCPU3Amounts()[0]) +
						" Gold, "+ (-newCPU3Amounts[1]+gh.getCPU3Amounts()[1]) + " Resources, "
						+ (-newCPU3Amounts[2]+gh.getCPU3Amounts()[2]) + " Men, and " +(
						-newCPU3Amounts[3]+gh.getCPU3Amounts()[3]) + " Land.");
				   
					}
				}
				else
				{
					question.setText(wrongExplain);
					ab.getHappyButton().setBackground(new Color(255, 135, 135));
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] + (gh.getDifficulty() * 66);
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] - wrongLoss - (gh.getDifficulty() * 33);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + ") has lost " + (-newPlayerAmounts[0]+gh.getPlayerAmounts()[0]) +
						" Gold, "+ (-newPlayerAmounts[1]+gh.getPlayerAmounts()[1]) + " Resources, "
						+ (-newPlayerAmounts[2]+gh.getPlayerAmounts()[2]) + " Men, and " +(
						-newPlayerAmounts[3]+gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has gained " + (newCPU1Amounts[0]-gh.getCPU1Amounts()[0]) +
						" Gold, " + (newCPU1Amounts[1]-gh.getCPU1Amounts()[1]) + " Resources, "
						+ (newCPU1Amounts[2]-gh.getCPU1Amounts()[2]) + " Men, and " +(
						newCPU1Amounts[3]-gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has gained " + (newCPU2Amounts[0]-gh.getCPU2Amounts()[0]) +
						" Gold, " + (newCPU2Amounts[1]-gh.getCPU2Amounts()[1]) + " Resources, "
						+ (newCPU2Amounts[2]-gh.getCPU2Amounts()[2]) + " Men, and " +(
						newCPU2Amounts[3]-gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has gained " + (newCPU3Amounts[0]-gh.getCPU3Amounts()[0]) +
						" Gold, " + (newCPU3Amounts[1]-gh.getCPU3Amounts()[1]) + " Resources, "
						+ (newCPU3Amounts[2]-gh.getCPU3Amounts()[2]) + " Men, and " +(
						newCPU3Amounts[3]-gh.getCPU3Amounts()[3]) + " Land.");
				}
			}
			else if (command.equals("Sad"))
			{
				if (questionAnswer == 1)
				{
					question.setText(correctExplain);
					result++;
					if (kc.getKingdomPlayer().equals("Sad"))
					{
					for (int i = 0; i < gh.getPlayerAmounts().length; i++)
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + correctGain - (gh.getDifficulty() * 60);
					question.append("\n\nCRITICAL! Player (" + kc.getKingdomPlayer() +") has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					}
					else
					{
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + 50 - (gh.getDifficulty() * 10);
					if (kc.getKingdomCPU1().equals("Sad"))
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU2().equals("Sad"))
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU3().equals("Sad"))
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + " has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has lost " + (-newCPU1Amounts[0]+gh.getCPU1Amounts()[0]) +
						" Gold, "+ (-newCPU1Amounts[1]+gh.getCPU1Amounts()[1]) + " Resources, "
						+ (-newCPU1Amounts[2]+gh.getCPU1Amounts()[2]) + " Men, and " +(
						-newCPU1Amounts[3]+gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has lost " + (-newCPU2Amounts[0]+gh.getCPU2Amounts()[0]) +
						" Gold, "+ (-newCPU2Amounts[1]+gh.getCPU2Amounts()[1]) + " Resources, "
						+ (-newCPU2Amounts[2]+gh.getCPU2Amounts()[2]) + " Men, and " +(
						-newCPU2Amounts[3]+gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has lost " + (-newCPU3Amounts[0]+gh.getCPU3Amounts()[0]) +
						" Gold, "+ (-newCPU3Amounts[1]+gh.getCPU3Amounts()[1]) + " Resources, "
						+ (-newCPU3Amounts[2]+gh.getCPU3Amounts()[2]) + " Men, and " +(
						-newCPU3Amounts[3]+gh.getCPU3Amounts()[3]) + " Land.");
				   
					}
				}
				else
				{
					question.setText(wrongExplain);
					ab.getSadButton().setBackground(new Color(255, 135, 135));
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] + (gh.getDifficulty() * 66);
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] - wrongLoss - (gh.getDifficulty() * 33);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + ") has lost " + (-newPlayerAmounts[0]+gh.getPlayerAmounts()[0]) +
						" Gold, "+ (-newPlayerAmounts[1]+gh.getPlayerAmounts()[1]) + " Resources, "
						+ (-newPlayerAmounts[2]+gh.getPlayerAmounts()[2]) + " Men, and " +(
						-newPlayerAmounts[3]+gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has gained " + (newCPU1Amounts[0]-gh.getCPU1Amounts()[0]) +
						" Gold, " + (newCPU1Amounts[1]-gh.getCPU1Amounts()[1]) + " Resources, "
						+ (newCPU1Amounts[2]-gh.getCPU1Amounts()[2]) + " Men, and " +(
						newCPU1Amounts[3]-gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has gained " + (newCPU2Amounts[0]-gh.getCPU2Amounts()[0]) +
						" Gold, " + (newCPU2Amounts[1]-gh.getCPU2Amounts()[1]) + " Resources, "
						+ (newCPU2Amounts[2]-gh.getCPU2Amounts()[2]) + " Men, and " +(
						newCPU2Amounts[3]-gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has gained " + (newCPU3Amounts[0]-gh.getCPU3Amounts()[0]) +
						" Gold, " + (newCPU3Amounts[1]-gh.getCPU3Amounts()[1]) + " Resources, "
						+ (newCPU3Amounts[2]-gh.getCPU3Amounts()[2]) + " Men, and " +(
						newCPU3Amounts[3]-gh.getCPU3Amounts()[3]) + " Land.");
				}
			}
			else if (command.equals("Angry"))
			{
				if (questionAnswer == 2)
				{
					question.setText(correctExplain);
					result++;
					if (kc.getKingdomPlayer().equals("Angry"))
					{
					for (int i = 0; i < gh.getPlayerAmounts().length; i++)
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + correctGain - (gh.getDifficulty() * 60);
					question.append("\n\nCRITICAL! Player (" + kc.getKingdomPlayer() +") has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					}
					else
					{
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + 50 - (gh.getDifficulty() * 10);
					if (kc.getKingdomCPU1().equals("Angry"))
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU2().equals("Angry"))
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU3().equals("Angry"))
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + " has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has lost " + (-newCPU1Amounts[0]+gh.getCPU1Amounts()[0]) +
						" Gold, "+ (-newCPU1Amounts[1]+gh.getCPU1Amounts()[1]) + " Resources, "
						+ (-newCPU1Amounts[2]+gh.getCPU1Amounts()[2]) + " Men, and " +(
						-newCPU1Amounts[3]+gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has lost " + (-newCPU2Amounts[0]+gh.getCPU2Amounts()[0]) +
						" Gold, "+ (-newCPU2Amounts[1]+gh.getCPU2Amounts()[1]) + " Resources, "
						+ (-newCPU2Amounts[2]+gh.getCPU2Amounts()[2]) + " Men, and " +(
						-newCPU2Amounts[3]+gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has lost " + (-newCPU3Amounts[0]+gh.getCPU3Amounts()[0]) +
						" Gold, "+ (-newCPU3Amounts[1]+gh.getCPU3Amounts()[1]) + " Resources, "
						+ (-newCPU3Amounts[2]+gh.getCPU3Amounts()[2]) + " Men, and " +(
						-newCPU3Amounts[3]+gh.getCPU3Amounts()[3]) + " Land.");
				   
					}
				}
				else
				{
					question.setText(wrongExplain);
					ab.getAngryButton().setBackground(new Color(255, 135, 135));
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] + (gh.getDifficulty() * 66);
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] - wrongLoss - (gh.getDifficulty() * 33);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + ") has lost " + (-newPlayerAmounts[0]+gh.getPlayerAmounts()[0]) +
						" Gold, "+ (-newPlayerAmounts[1]+gh.getPlayerAmounts()[1]) + " Resources, "
						+ (-newPlayerAmounts[2]+gh.getPlayerAmounts()[2]) + " Men, and " +(
						-newPlayerAmounts[3]+gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has gained " + (newCPU1Amounts[0]-gh.getCPU1Amounts()[0]) +
						" Gold, " + (newCPU1Amounts[1]-gh.getCPU1Amounts()[1]) + " Resources, "
						+ (newCPU1Amounts[2]-gh.getCPU1Amounts()[2]) + " Men, and " +(
						newCPU1Amounts[3]-gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has gained " + (newCPU2Amounts[0]-gh.getCPU2Amounts()[0]) +
						" Gold, " + (newCPU2Amounts[1]-gh.getCPU2Amounts()[1]) + " Resources, "
						+ (newCPU2Amounts[2]-gh.getCPU2Amounts()[2]) + " Men, and " +(
						newCPU2Amounts[3]-gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has gained " + (newCPU3Amounts[0]-gh.getCPU3Amounts()[0]) +
						" Gold, " + (newCPU3Amounts[1]-gh.getCPU3Amounts()[1]) + " Resources, "
						+ (newCPU3Amounts[2]-gh.getCPU3Amounts()[2]) + " Men, and " +(
						newCPU3Amounts[3]-gh.getCPU3Amounts()[3]) + " Land.");
				}
			}
			else if (command.equals("Fear"))
			{
				if (questionAnswer == 3)
				{
					question.setText(correctExplain);
					result++;
					if (kc.getKingdomPlayer().equals("Fear"))
					{
					for (int i = 0; i < gh.getPlayerAmounts().length; i++)
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + correctGain - (gh.getDifficulty() * 60);
					question.append("\n\nCRITICAL! Player (" + kc.getKingdomPlayer() +") has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					}
					else
					{
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] + 50 - (gh.getDifficulty() * 10);
					if (kc.getKingdomCPU1().equals("Fear"))
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU2().equals("Fear"))
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					else if (kc.getKingdomCPU3().equals("Fear"))
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] - 100 + (gh.getDifficulty() * 15);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + " has gained " + (newPlayerAmounts[0]-gh.getPlayerAmounts()[0]) +
						" Gold, " + (newPlayerAmounts[1]-gh.getPlayerAmounts()[1]) + " Resources, "
						+ (newPlayerAmounts[2]-gh.getPlayerAmounts()[2]) + " Men, and " +(
						newPlayerAmounts[3]-gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has lost " + (-newCPU1Amounts[0]+gh.getCPU1Amounts()[0]) +
						" Gold, "+ (-newCPU1Amounts[1]+gh.getCPU1Amounts()[1]) + " Resources, "
						+ (-newCPU1Amounts[2]+gh.getCPU1Amounts()[2]) + " Men, and " +(
						-newCPU1Amounts[3]+gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has lost " + (-newCPU2Amounts[0]+gh.getCPU2Amounts()[0]) +
						" Gold, "+ (-newCPU2Amounts[1]+gh.getCPU2Amounts()[1]) + " Resources, "
						+ (-newCPU2Amounts[2]+gh.getCPU2Amounts()[2]) + " Men, and " +(
						-newCPU2Amounts[3]+gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has lost " + (-newCPU3Amounts[0]+gh.getCPU3Amounts()[0]) +
						" Gold, "+ (-newCPU3Amounts[1]+gh.getCPU3Amounts()[1]) + " Resources, "
						+ (-newCPU3Amounts[2]+gh.getCPU3Amounts()[2]) + " Men, and " +(
						-newCPU3Amounts[3]+gh.getCPU3Amounts()[3]) + " Land.");
				   
					}
				}
				else
				{
					question.setText(wrongExplain);
					ab.getFearButton().setBackground(new Color(255, 135, 135));
					for (int i = 0; i < gh.getCPU1Amounts().length; i++)
					{
					newCPU1Amounts[i] = gh.getCPU1Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU2Amounts[i] = gh.getCPU2Amounts()[i] + (gh.getDifficulty() * 66);
					newCPU3Amounts[i] = gh.getCPU3Amounts()[i] + (gh.getDifficulty() * 66);
					newPlayerAmounts[i] = gh.getPlayerAmounts()[i] - wrongLoss - (gh.getDifficulty() * 33);
					}
					question.append("\n\nPlayer (" + kc.getKingdomPlayer() + ") has lost " + (-newPlayerAmounts[0]+gh.getPlayerAmounts()[0]) +
						" Gold, "+ (-newPlayerAmounts[1]+gh.getPlayerAmounts()[1]) + " Resources, "
						+ (-newPlayerAmounts[2]+gh.getPlayerAmounts()[2]) + " Men, and " +(
						-newPlayerAmounts[3]+gh.getPlayerAmounts()[3]) + " Land.");
					question.append("\nCPU1 (" + kc.getKingdomCPU1() + ") has gained " + (newCPU1Amounts[0]-gh.getCPU1Amounts()[0]) +
						" Gold, " + (newCPU1Amounts[1]-gh.getCPU1Amounts()[1]) + " Resources, "
						+ (newCPU1Amounts[2]-gh.getCPU1Amounts()[2]) + " Men, and " +(
						newCPU1Amounts[3]-gh.getCPU1Amounts()[3]) + " Land.");
					question.append("\nCPU2 (" + kc.getKingdomCPU2() + ") has gained " + (newCPU2Amounts[0]-gh.getCPU2Amounts()[0]) +
						" Gold, " + (newCPU2Amounts[1]-gh.getCPU2Amounts()[1]) + " Resources, "
						+ (newCPU2Amounts[2]-gh.getCPU2Amounts()[2]) + " Men, and " +(
						newCPU2Amounts[3]-gh.getCPU2Amounts()[3]) + " Land.");
					question.append("\nCPU3 (" + kc.getKingdomCPU3() + ") has gained " + (newCPU3Amounts[0]-gh.getCPU3Amounts()[0]) +
						" Gold, " + (newCPU3Amounts[1]-gh.getCPU3Amounts()[1]) + " Resources, "
						+ (newCPU3Amounts[2]-gh.getCPU3Amounts()[2]) + " Men, and " +(
						newCPU3Amounts[3]-gh.getCPU3Amounts()[3]) + " Land.");
				}
			}

			gh.setPlayerAmounts(newPlayerAmounts);
			gh.setCPU1Amounts(newCPU1Amounts);
			gh.setCPU2Amounts(newCPU2Amounts);
			gh.setCPU3Amounts(newCPU3Amounts);
			questionsRemaining.setText("Questions Limit: " + questionCount + "/8");
			gh.setKingdoms(kc.getKingdomPlayer(), kc.getPlayerKingdomColor());
			isAnswerClicked = true; //initiates the SECOND click
			}
			else //the second click
			{
			isAnswerClicked = false; //initiates the FIRST click
			//Resets all the button colors, and resets the question and the question displaying JTextArea
			ab.getHappyButton().setBackground(new Color(197, 197, 154));
			ab.getSadButton().setBackground(new Color(200, 210, 219));
			ab.getAngryButton().setBackground(new Color(189, 167, 161));
			ab.getFearButton().setBackground(new Color(222, 210, 233));
			getQuestion();
			}
		}
	}

	// Class done by Arnav
	//This class contains the timer for the whole game. This timer is ONLY started
	//while in the MainGame panel, otherwise is stopped. While this timer is running,
	//the amount displays for each kingdom is updated every second, and the time LEFT
	//goes down. Every 20 seconds, a random event happens, every 30 seconds, amount
	//stealing happens, and if the time left is 0 or the user's amounts reach zero,
	//the printwriter is written to and the user loses. If the user's amounts reach
	//the amount goal, the printwriter is written to and the user wins.
	class GameBottomBar extends JPanel implements ActionListener
	{
		private JLabel timeRemaining; //is a FV because we need to update the time remaining  
		// in this JLabel, and to do that, the actionPerformed needs to be able to access
		// the JLabel, and to do that, pageList must be a FV
		private AmountEvents ae; //Must be a FV because every 20 seconds, a method
		//in AmountEvents must be called
		private AmountStealing as; //Must be a FV because every 30 seconds, a method to
		//make amounts get stolen must be called
		public GameBottomBar()
		{
			setBackground(new Color(40, 40, 40));
			ae = new AmountEvents();
			as = new AmountStealing();
			setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
			timeRemaining = new JLabel("Time Remaining In Seconds: " + timeLeft);
			questionsRemaining = new JLabel("Questions Limit: 8/8");

			timeRemaining.setForeground(Color.WHITE);
			questionsRemaining.setForeground(Color.WHITE);

			Font bottomFont = new Font("Arial", Font.PLAIN, 17);
			timeRemaining.setFont(bottomFont);
			questionsRemaining.setFont(bottomFont);
			add(timeRemaining);
			add(questionsRemaining);
		}

		public void actionPerformed(ActionEvent evt)
		{
			if (gh.getEnded() || gh.getGame() != MainGame.this) return;
			timeLeft--;
			timeRemaining.setText("Time Remaining In Seconds: " + timeLeft);
			gh.getKingdomDisplayer().getAmountDisplays().updateAmountDisplay();
			if ( timeLeft % 20 == 0 && eventsActive) //Every 20 seconds, AmountEvents will be called
			// to cause an amount event to happen
			{
				ae.causeEvent();
			}
			if (timeLeft % 30 == 0 && stealingActive) //Every 30 seconds, AmountStealing will be called to cause
			// amount stealing to happen
			{
				as.stealAmounts();
			}
			
			///If one of the game ending conditions are met, the timer is stopped,
			///the boolean is changed to indicate the time has ended, the corresponding
			///win/lose panel JLabels are updated, the win/lose panel is shown,
			///the printwriter is written to, and the printwriter is closed.
			if ( timeLeft <= 0) // if the time runs out the user is shown the lose panel.
			{
				timer.stop();
				gh.setEnded(true);
				gh.getLosePanel().setEndgameDifficulty(gh.getDifficulty());
				gh.getLosePanel().updateEndDifficulty();
				ph.getPrintWriter().println(result);
				gh.getGameCards().show(gh, "Lose");
				ph.getPrintWriter().close();
				displayResults();
			}
			if (   gh.getPlayerAmounts()[0] >= gh.getGoal() && gh.getPlayerAmounts()[1] >= gh.getGoal() && gh.getPlayerAmounts()[2] >= gh.getGoal() && gh.getPlayerAmounts()[3] >= gh.getGoal()  )  
			// If the user reached the amount goal for each amount, then the win panel is displayed in the GameHolder card layout
			{
				timer.stop();
				gh.setWon(true);
				gh.setEnded(true);
				gh.getWinPanel().setEndgameDifficulty(gh.getDifficulty());
				gh.getWinPanel().updateEndDifficulty();
				ph.getPrintWriter().println(result);
				gh.getGameCards().show(gh, "Win");
				ph.getPrintWriter().close();
				displayResults();
			}
			if ( gh.getPlayerAmounts()[0] == 0 && gh.getPlayerAmounts()[1] == 0 && gh.getPlayerAmounts()[2] == 0 && gh.getPlayerAmounts()[3] == 0 )
			{
				timer.stop();
				gh.setEnded(true);
				gh.getLosePanel().setEndgameDifficulty(gh.getDifficulty());
				gh.getLosePanel().updateEndDifficulty();
				ph.getPrintWriter().println(result);
				gh.getGameCards().show(gh, "Lose");
				ph.getPrintWriter().close();
				displayResults();
			}
		}

		///THE FOLLOWING ARE GETTER SETTER METHODS
		public AmountEvents getAmountEvents()
		{
			return ae;
		}
		public AmountStealing getAmountStealing()
		{
			return as;
		}
	}

	//Class done by Joshua
	//In this class is a method to cause an Amount Event to happen. Three amount
	//events can happen: Each kingdom has a 50% chance to gain random amounts,
	//Each kingdom has a 50% chance to lose random amounts, and the time left is
	//reduced by 10 seconds. These events are announced in the InformationDisplay.
	class AmountEvents
	{
		//The following FVs must be a FV because CustomPanel will need to access
		//the FVs if the user wants to change the values
		private int eventValueRange;
		private int timeStealAmt;
		private int chanceRangePlayer;
		private int chanceRangeCPU1;
		private int chanceRangeCPU2;
		private int chanceRangeCPU3;
		private int startRangePlayer;
		private int startRangeCPU1;
		private int startRangeCPU2;
		private int startRangeCPU3;
		
		public AmountEvents()
		{
			eventValueRange = 50;
			timeStealAmt = 10;
			chanceRangeCPU3 = chanceRangeCPU2 = chanceRangeCPU1 = chanceRangePlayer = 2;
			startRangeCPU3 = startRangeCPU2 = startRangeCPU1 = startRangePlayer = 0;
		}

		public void causeEvent() //This method will cause an amount event when called,
		// as stated in paragraph 7 on the game proposal.
		{
			byte eventToRun = (byte)(Math.random()*3);

			byte eventPlayer = (byte)(Math.random()*chanceRangePlayer + startRangePlayer);
			byte eventCPU1 = (byte)(Math.random()*chanceRangeCPU1 + startRangeCPU1);
			byte eventCPU2 = (byte)(Math.random()*chanceRangeCPU2 + startRangeCPU2);
			byte eventCPU3 = (byte)(Math.random()*chanceRangeCPU3 + startRangeCPU3);

			//The following variables are placeholder amount arrays to eventually set
			//into the real amount arrays
			int[] newAmountPlayer = new int[4];
			int[] newAmountCPU1 = new int[4];
			int[] newAmountCPU2 = new int[4];
			int[] newAmountCPU3 = new int[4];
			for (int i = 0; i < gh.getPlayerAmounts().length; i++)
			{
				newAmountPlayer[i] = gh.getPlayerAmounts()[i];
				newAmountCPU1[i] = gh.getCPU1Amounts()[i];
				newAmountCPU2[i] = gh.getCPU2Amounts()[i];
				newAmountCPU3[i] = gh.getCPU3Amounts()[i];
			}

			if (eventToRun == 0) //The affected kingdoms will EARN random amounts
			{
				for (int i = 0; i < gh.getPlayerAmounts().length; i++)
				{
					newAmountPlayer[i] = gh.getPlayerAmounts()[i] + (eventPlayer *
					(int)(Math.random()*eventValueRange+100));
					newAmountCPU1[i] = gh.getCPU1Amounts()[i] + (eventCPU1 *
					(int)(Math.random()*eventValueRange+100));
					newAmountCPU2[i] = gh.getCPU2Amounts()[i] + (eventCPU2 *
					(int)(Math.random()*eventValueRange+100));
					newAmountCPU3[i] = gh.getCPU3Amounts()[i] + (eventCPU3 *
					(int)(Math.random()*eventValueRange+100));
				}
				events.append("\nRANDOM EVENT: Event Gain! The affected kingdoms will gain amounts!\n");
				if (eventPlayer == 0 && eventCPU1 == 0 && eventCPU2 == 0 && eventCPU3 == 0)
				{
					events.append("No kingdoms have been affected by amount gain.\n");
				}
				else
				{
					if (eventPlayer == 1)
					events.append("Kingdom " + kc.getKingdomPlayer() + " (Player) has gained " + (newAmountPlayer[0]-gh.getPlayerAmounts()[0]) + " Gold, " + (newAmountPlayer[1]-gh.getPlayerAmounts()[1]) + " Resources, " + (newAmountPlayer[2]-gh.getPlayerAmounts()[2]) + " Men, and " + (newAmountPlayer[3]-gh.getPlayerAmounts()[3]) + " Land.\n");
					if (eventCPU1 == 1)
					events.append("Kingdom " + kc.getKingdomCPU1() + " (CPU 1) has gained " + (newAmountCPU1[0]-gh.getCPU1Amounts()[0]) + " Gold, " + (newAmountCPU1[1]-gh.getCPU1Amounts()[1]) + " Resources, " + (newAmountCPU1[2]-gh.getCPU1Amounts()[2]) + " Men, and " + (newAmountCPU1[3]-gh.getCPU1Amounts()[3]) + " Land.\n");
					if (eventCPU2 == 1)
					events.append("Kingdom " + kc.getKingdomCPU2() + " (CPU 2) has gained " + (newAmountCPU2[0]-gh.getCPU2Amounts()[0]) + " Gold, " + (newAmountCPU2[1]-gh.getCPU2Amounts()[1]) + " Resources, " + (newAmountCPU2[2]-gh.getCPU2Amounts()[2]) + " Men, and " + (newAmountCPU2[3]-gh.getCPU2Amounts()[3]) + " Land.\n");
					if (eventCPU3 == 1)
					events.append("Kingdom " + kc.getKingdomCPU3() + " (CPU 3) has gained " + (newAmountCPU3[0]-gh.getCPU3Amounts()[0]) + " Gold, " + (newAmountCPU3[1]-gh.getCPU3Amounts()[1]) + " Resources, " + (newAmountCPU3[2]-gh.getCPU3Amounts()[2]) + " Men, and " + (newAmountCPU3[3]-gh.getCPU3Amounts()[3]) + " Land.\n");
				}
			}
			else if (eventToRun == 1) //The affected kingdoms will LOSE random amounts
			{
				for (int i = 0; i < gh.getPlayerAmounts().length; i++)
				{
					newAmountPlayer[i] = gh.getPlayerAmounts()[i] - (eventPlayer *
					(int)(Math.random()*eventValueRange+100));
					newAmountCPU1[i] = gh.getCPU1Amounts()[i] - (eventCPU1 *
					(int)(Math.random()*eventValueRange+100));
					newAmountCPU2[i] = gh.getCPU2Amounts()[i] - (eventCPU2 *
					(int)(Math.random()*eventValueRange+100));
					newAmountCPU3[i] = gh.getCPU3Amounts()[i] - (eventCPU3 *
					(int)(Math.random()*eventValueRange+100));
				}
				events.append("\nRANDOM EVENT: Event Loss! The affected kingdoms will lose amounts!\n");
				if (eventPlayer == 0 && eventCPU1 == 0 && eventCPU2 == 0 && eventCPU3 == 0)
				{
					events.append("No kingdoms have been affected by amount loss.\n");
				}
				else
				{
					if (eventPlayer == 1)
					events.append("Kingdom " + kc.getKingdomPlayer() + " (Player) has lost " + (-newAmountPlayer[0]+gh.getPlayerAmounts()[0]) + " Gold, " + (-newAmountPlayer[1]+gh.getPlayerAmounts()[1]) + " Resources, " + (-newAmountPlayer[2]+gh.getPlayerAmounts()[2]) + " Men, and " + (newAmountPlayer[3]+gh.getPlayerAmounts()[3]) + " Land.\n");
					if (eventCPU1 == 1)
					events.append("Kingdom " + kc.getKingdomCPU1() + " (CPU 1) has lost " + (-newAmountCPU1[0]+gh.getCPU1Amounts()[0]) + " Gold, " + (-newAmountCPU1[1]+gh.getCPU1Amounts()[1]) + " Resources, " + (-newAmountCPU1[2]+gh.getCPU1Amounts()[2]) + " Men, and " + (-newAmountCPU1[3]+gh.getCPU1Amounts()[3]) + " Land.\n");
					if (eventCPU2 == 1)
					events.append("Kingdom " + kc.getKingdomCPU2() + " (CPU 2) has lost " + (-newAmountCPU2[0]+gh.getCPU2Amounts()[0]) + " Gold, " + (-newAmountCPU2[1]+gh.getCPU2Amounts()[1]) + " Resources, " + (-newAmountCPU2[2]+gh.getCPU2Amounts()[2]) + " Men, and " + (-newAmountCPU2[3]+gh.getCPU2Amounts()[3]) + " Land.\n");
					if (eventCPU3 == 1)
					events.append("Kingdom " + kc.getKingdomCPU3() + " (CPU 3) has lost " + (-newAmountCPU3[0]+gh.getCPU3Amounts()[0]) + " Gold, " + (-newAmountCPU3[1]+gh.getCPU3Amounts()[1]) + " Resources, " + (-newAmountCPU3[2]+gh.getCPU3Amounts()[2]) + " Men, and " + (-newAmountCPU3[3]+gh.getCPU3Amounts()[3]) + " Land.\n");
				}
			}
			else if (eventToRun == 2) //Time remaining is reduced by 10 seconds
			{
				timeLeft -= timeStealAmt;
				events.append("\nRANDOM EVENT: Time steal! Time remaining is reduced by 10 seconds!\n");
			}
			gh.setPlayerAmounts(newAmountPlayer);
			gh.setCPU1Amounts(newAmountCPU1);
			gh.setCPU2Amounts(newAmountCPU2);
			gh.setCPU3Amounts(newAmountCPU3);
			gh.setKingdoms(kc.getKingdomPlayer(), kc.getPlayerKingdomColor());
		}
	   
		///THE FOLLOWING ARE GETTER SETTER METHODS
		public int getEventRange()
		{
			return eventValueRange;
		}
		public void setEventRange(int eventRangeIn)
		{
			eventValueRange = eventRangeIn;
		}
		public int getTimeSteal()
		{
			return timeStealAmt;
		}
		public void setTimeSteal(int timeStealAmtIn)
		{
			timeStealAmt = timeStealAmtIn;
		}
	   
		public int getChancePlayer()
		{
			return chanceRangePlayer;
		}
		public void setChancePlayer(int chancePlayerIn)
		{
			chanceRangePlayer = chancePlayerIn;
		}
		public int getChanceCPU1()
		{
			return chanceRangeCPU1;
		}
		public void setChanceCPU1(int chanceCPU1In)
		{
			chanceRangeCPU1 = chanceCPU1In;
		}
		public int getChanceCPU2()
		{
			return chanceRangeCPU2;
		}
		public void setChanceCPU2(int chanceCPU2In)
		{
			chanceRangeCPU2 = chanceCPU2In;
		}
		public int getChanceCPU3()
		{
			return chanceRangeCPU3;
		}
		public void setChanceCPU3(int chanceCPU3In)
		{
			chanceRangeCPU3 = chanceCPU3In;
		}
	   
		public int getStartPlayer()
		{
			return startRangePlayer;
		}
		public void setStartPlayer(int startPlayerIn)
		{
			startRangePlayer = startPlayerIn;
		}
		public int getStartCPU1()
		{
			return startRangeCPU1;
		}
		public void setStartCPU1(int startCPU1In)
		{
			startRangeCPU1 = startCPU1In;
		}
		public int getStartCPU2()
		{
			return startRangeCPU2;
		}
		public void setStartCPU2(int startCPU2In)
		{
			startRangeCPU2 = startCPU2In;
		}
		public int getStartCPU3()
		{
			return startRangeCPU3;
		}
		public void setStartCPU3(int startCPU3In)
		{
			startRangeCPU3 = startCPU3In;
		}
	}
	//Class made by Joshua
	//This class has a method where all kingdoms steal amounts from each other.
	//First, every CPU will earn 1/4 of the player's amounts, the player loses 1/4
	//of their amounts, then the player earns 1/4 of their amounts, and each CPU
	//loses 1/4 of the player's amounts.
	class AmountStealing
	{
		private int stealOrder; //Must be a FV because CustomPanel will be able to change
		// the amount order
		private int stealStrength; //Must be a FV so CustomPanel can access this FV to change it
		public AmountStealing()
		{
			stealOrder = 0;
			stealStrength = 4;
		}
		public void stealAmounts()
		{
			int[] newPlayerAmounts = new int[4];
			int[] newCPU1Amounts = new int[4];
			int[] newCPU2Amounts = new int[4];
			int[] newCPU3Amounts = new int[4];

			for (int i = 0; i < newPlayerAmounts.length; i++)
			{
				newPlayerAmounts[i] = gh.getPlayerAmounts()[i];
				newCPU1Amounts[i] = gh.getCPU1Amounts()[i];
				newCPU2Amounts[i] = gh.getCPU2Amounts()[i];
				newCPU3Amounts[i] = gh.getCPU3Amounts()[i];
			}
			events.append("\nKINGDOM EVENT: AMOUNT STEALING!\n");
			if (stealOrder == 0) //CPUs steal from player, then  player steals from CPUs
			{
				for (int i = 0; i < newPlayerAmounts.length; i++)
				{
					events.append("All CPU kingdoms have \"stolen\" " + (newPlayerAmounts[i]/stealStrength) + " ");
					if (i == 0)
					events.append("Gold ");
					else if (i == 1)
					events.append("Resources ");
					else if (i == 2)
					events.append("Men ");
					else if (i == 3)
					events.append("Land ");
					events.append("from the player.\n");
					newCPU1Amounts[i] += (newPlayerAmounts[i]/stealStrength);
					newCPU2Amounts[i] += (newPlayerAmounts[i]/stealStrength);
					newCPU3Amounts[i] += (newPlayerAmounts[i]/stealStrength);
					newPlayerAmounts[i] -= (newPlayerAmounts[i]/stealStrength);

					events.append("The player has \"stolen\" " + (newPlayerAmounts[i]/stealStrength) + " ");
					if (i == 0)
					events.append("Gold ");
					else if (i == 1)
					events.append("Resources ");
					else if (i == 2)
					events.append("Men ");
					else if (i == 3)
					events.append("Land ");
					events.append("from the CPUs.\n");
					newCPU1Amounts[i] -= (newPlayerAmounts[i]/stealStrength);
					newCPU2Amounts[i] -= (newPlayerAmounts[i]/stealStrength);
					newCPU3Amounts[i] -= (newPlayerAmounts[i]/stealStrength);
					newPlayerAmounts[i] += (newPlayerAmounts[i]/stealStrength);
				}
			}
			else if (stealOrder == 1) //player steals from CPUs, then CPUs steal from player
			{
				for (int i = 0; i < newPlayerAmounts.length; i++)
				{
					events.append("The player has \"stolen\" " + (newPlayerAmounts[i]/stealStrength) + " ");
					if (i == 0)
					events.append("Gold ");
					else if (i == 1)
					events.append("Resources ");
					else if (i == 2)
					events.append("Men ");
					else if (i == 3)
					events.append("Land ");
					events.append("from the CPUs.\n");
					newCPU1Amounts[i] -= (newPlayerAmounts[i]/stealStrength);
					newCPU2Amounts[i] -= (newPlayerAmounts[i]/stealStrength);
					newCPU3Amounts[i] -= (newPlayerAmounts[i]/stealStrength);
					newPlayerAmounts[i] += (newPlayerAmounts[i]/stealStrength);
				   
					events.append("All CPU kingdoms have \"stolen\" " + (newPlayerAmounts[i]/stealStrength) + " ");
					if (i == 0)
					events.append("Gold ");
					else if (i == 1)
					events.append("Resources ");
					else if (i == 2)
					events.append("Men ");
					else if (i == 3)
					events.append("Land ");
					events.append("from the player.\n");
					newCPU1Amounts[i] += (newPlayerAmounts[i]/stealStrength);
					newCPU2Amounts[i] += (newPlayerAmounts[i]/stealStrength);
					newCPU3Amounts[i] += (newPlayerAmounts[i]/stealStrength);
					newPlayerAmounts[i] -= (newPlayerAmounts[i]/stealStrength);
				}
			}
			gh.setPlayerAmounts(newPlayerAmounts);
			gh.setCPU1Amounts(newCPU1Amounts);
			gh.setCPU2Amounts(newCPU2Amounts);
			gh.setCPU3Amounts(newCPU3Amounts);
			gh.setKingdoms(kc.getKingdomPlayer(), kc.getPlayerKingdomColor());
		}
		///THE FOLLOWING ARE GETTER SETTER METHODS
		public int getStealOrder()
		{
			return stealOrder;
		}
		public void setStealOrder(int stealOrderIn)
		{
			stealOrder = stealOrderIn;
		}
		public int getStealStrength()
		{
			return stealStrength;
		}
		public void setStealStrength(int stealStrengthIn)
		{
			stealStrength = stealStrengthIn;
		}
	}
	//Method made by Arnav
	public void displayResults() //This method updates the lose/win results, where
	//the in-memory score is used, avoiding stale or exhausted results scanners.
	{
		loseResultsDisplay.setText("You got " + result + " questions correct throughout the game.");
		winResultsDisplay.setText("You got " + result + " questions correct throughout the game.");
	}
}
//Class done by Joshua
// shows the player that they've lost the game.
class LosePanel extends JPanel
{
	private GameHolder gh; //Must be a FV to be accessible in actionPerformed to switch
	//the displayed card panel
	private int eDifficulty;//Must be a FV because it is tricky to get this difficulty
		//to set to the correct difficulty WHEN NEEDED TO, so must be passed as a FV to
		//set the difficulty properly when the lose/win panel is shown
	private JLabel difficultyDisplay; //Must be a FV because a method updates this same
		//JLabel
	public LosePanel(GameHolder ghIn)
	{
		gh = ghIn;
		setLayout(new BorderLayout());
		setBackground(Color.RED);
		JLabel loseText = new JLabel("GAME OVER - Take what you've learned and try again next time!", JLabel.CENTER);
		loseText.setForeground(Color.WHITE);
		add(loseText, BorderLayout.CENTER);
		JPanel correctDisplayPanel = new JPanel();
		correctDisplayPanel.setLayout(new GridLayout(1, 1));
		correctDisplayPanel.setBackground(new Color(197, 138, 138));
		correctDisplayPanel.add(ghIn.getGame().getLoseResultsDisplay());
		add(correctDisplayPanel, BorderLayout.EAST);
		LoseWestSide lws = new LoseWestSide();
		add(lws, BorderLayout.WEST);
		JButton restart = new JButton("Restart Game");
		restart.addActionListener(evt -> gh.restartGame());
		add(restart, BorderLayout.SOUTH);
	}
	//Class done by Arnav
//This class displays the difficulty the user played on, as well as a button that
//takes them to the amounts each kingdom had by the end of the game.
	class LoseWestSide extends JPanel implements ActionListener
	{
		public LoseWestSide()
		{
			setLayout(new GridLayout(2,1));
			setBackground(new Color(255, 188, 188));
			eDifficulty = gh.getDifficulty();
			difficultyDisplay = new JLabel("Difficulty: " , JLabel.CENTER);
			add(difficultyDisplay);
			JButton displayAmounts = new JButton("Click Here To View The Final Amounts");
			displayAmounts.addActionListener(this);
			displayAmounts.setBackground(Color.BLACK);
			displayAmounts.setForeground(Color.WHITE);
			add(displayAmounts);
		}
	 
		public void actionPerformed(ActionEvent evt)
		{
			gh.getGameCards().show(gh, "displayCPU");
		}
	}
	public void updateEndDifficulty() //This method will update the JLabel, preferrably
			//once the difficulty is updated.
		{
			if (eDifficulty == -1)
			{
				difficultyDisplay.setText("Difficulty: Custom");
				difficultyDisplay.setForeground(Color.BLACK);
			}
			else if (eDifficulty == 0)
			{
				difficultyDisplay.setText("Difficulty: Easy");
				difficultyDisplay.setForeground(Color.GREEN);
			}
			else if (eDifficulty == 1)
			{
				difficultyDisplay.setText("Difficulty: Adequate");
				difficultyDisplay.setForeground(Color.ORANGE);
			}
			else if (eDifficulty == 2)
			{
				difficultyDisplay.setText("Difficulty: Prohibitive");
				difficultyDisplay.setForeground(Color.RED);
			}
			else if (eDifficulty == 3)
			{
				difficultyDisplay.setText("Difficulty: Macabre");
				difficultyDisplay.setForeground(new Color(54, 7, 17));
			}
		}
		   
		///THE FOLLOWING METHODS ARE GETTER SETTER METHODS
		public int getEndgameDifficulty()
		{
			return eDifficulty;
		}
		public void setEndgameDifficulty(int difficultyIn)
		{
			eDifficulty = difficultyIn;
		}
}

//Class done by Joshua
//If the player reaches the amount goal, shows that they've won the game.
class WinPanel extends JPanel
{
	private GameHolder gh; //Must be a FV to be accessible in actionPerformed to switch
	//the displayed card panel
	private int eDifficulty;//Must be a FV because it is tricky to get this difficulty
		//to set to the correct difficulty WHEN NEEDED TO, so must be passed as a FV to
		//set the difficulty properly when the lose/win panel is shown
	private JLabel difficultyDisplay; //Must be a FV because a method updates this same
		//JLabel
	public WinPanel(GameHolder ghIn)
	{
		gh = ghIn;
		setLayout(new BorderLayout());
		setBackground(Color.GREEN);
		JLabel winText = new JLabel("Congratulations - You WON the Game!", JLabel.CENTER);
		winText.setForeground(Color.WHITE);
		add(winText, BorderLayout.CENTER);
		JPanel correctDisplayPanel = new JPanel();
		correctDisplayPanel.setLayout(new GridLayout(1, 1));
		correctDisplayPanel.setBackground(new Color(138, 197, 138));
		correctDisplayPanel.add(ghIn.getGame().getWinResultsDisplay());
		add(correctDisplayPanel, BorderLayout.EAST);
		WinWestSide wws = new WinWestSide();
		add(wws, BorderLayout.WEST);
		JButton restart = new JButton("Restart Game");
		restart.addActionListener(evt -> gh.restartGame());
		add(restart, BorderLayout.SOUTH);
	}
	//Class done by Arnav
//This class displays the difficulty the user played on, as well as a button that
//takes them to the amounts each kingdom had by the end of the game.
	class WinWestSide extends JPanel implements ActionListener
	{
		public WinWestSide()
		{
			setLayout(new GridLayout(2,1));
			setBackground(new Color(188, 255, 188));
			eDifficulty = gh.getDifficulty();
			difficultyDisplay = new JLabel("Difficulty: " , JLabel.CENTER);
			add(difficultyDisplay);
			JButton displayAmounts = new JButton("Click Here To View The Final Amounts");
			displayAmounts.addActionListener(this);
			displayAmounts.setBackground(Color.BLACK);
			displayAmounts.setForeground(Color.WHITE);
			add(displayAmounts);
		}
	 
		public void actionPerformed(ActionEvent evt)
		{
			gh.getGameCards().show(gh, "displayCPU");
		}
	}
   
	public void updateEndDifficulty() //This method will update the JLabel, preferrably
			//once the difficulty is updated.
		{
			if (eDifficulty == -1)
			{
				difficultyDisplay.setText("Difficulty: Custom");
				difficultyDisplay.setForeground(Color.BLACK);
			}
			else if (eDifficulty == 0)
			{
				difficultyDisplay.setText("Difficulty: Easy");
				difficultyDisplay.setForeground(Color.GREEN);
			}
			else if (eDifficulty == 1)
			{
				difficultyDisplay.setText("Difficulty: Adequate");
				difficultyDisplay.setForeground(Color.ORANGE);
			}
			else if (eDifficulty == 2)
			{
				difficultyDisplay.setText("Difficulty: Prohibitive");
				difficultyDisplay.setForeground(Color.RED);
			}
			else if (eDifficulty == 3)
			{
				difficultyDisplay.setText("Difficulty: Macabre");
				difficultyDisplay.setForeground(new Color(54, 7, 17));
			}
		}
		   
		///THE FOLLOWING METHODS ARE GETTER SETTER METHODS
		public int getEndgameDifficulty()
		{
			return eDifficulty;
		}
		public void setEndgameDifficulty(int difficultyIn)
		{
			eDifficulty = difficultyIn;
		}
}

//Class made by Joshua
//This method allows the user to see how much amounts EACH kingdom and EACH player
//has, whether during or after the game.
class KingdomDisplays extends JPanel
{
	private AmountDisplays ad;//Must be a FV so that the GameBottomBar that can access
	//this class can access the class that actually updates the JLabels
	private GameHolder gh; //Must be a FV for access of many variables in GameHolder,
	//including the amounts of each kingdom
	public KingdomDisplays(GameHolder ghIn)
	{
		gh = ghIn;
		gh.getGame().getTimer().stop();
		setLayout(new BorderLayout(5, 5));
		setBackground(Color.YELLOW);
		ad = new AmountDisplays();
		ReturnBar rb = new ReturnBar();
	   
		add(rb, BorderLayout.SOUTH);
		add(ad, BorderLayout.CENTER);
	}
	///THE FOLLOWING IS A GETTER SETTER METHOD
	public AmountDisplays getAmountDisplays()
	{
		return ad;
	}
   
	class AmountDisplays extends JPanel
	{
		//The following FVs must be FVs because the variables are set in the constructor,
		//but THEN updated in a method
		private JTextArea displayPlayer;
		private JTextArea displayCPU1;
		private JTextArea displayCPU2;
		private JTextArea displayCPU3;
		public AmountDisplays()
		{
			setLayout(new GridLayout(1, 4, 30, 10));
			setBackground(new Color(108, 54, 85));
			Font displayAmountFont = new Font("Times New Roman", Font.PLAIN, 22);
			displayPlayer = new JTextArea("PLAYER\n");
			displayCPU1 = new JTextArea("CPU 1\n");
			displayCPU2 = new JTextArea("CPU 2\n");
			displayCPU3 = new JTextArea("CPU 3\n");
		   
			displayPlayer.setEditable(false);
			displayPlayer.setLineWrap(true);
			displayPlayer.setWrapStyleWord(true);
			displayPlayer.setMargin(new Insets(6, 6, 6, 6));
			displayPlayer.setFont(displayAmountFont);
			displayPlayer.setBackground(new Color(50, 50, 50));
			displayPlayer.setForeground(new Color(220, 220, 220));
		   
			displayCPU1.setEditable(false);
			displayCPU1.setLineWrap(true);
			displayCPU1.setWrapStyleWord(true);
			displayCPU1.setMargin(new Insets(6, 6, 6, 6));
			displayCPU1.setFont(displayAmountFont);
			displayCPU1.setBackground(new Color(50, 50, 50));
			displayCPU1.setForeground(new Color(220, 220, 220));
		   
			displayCPU2.setEditable(false);
			displayCPU2.setLineWrap(true);
			displayCPU2.setWrapStyleWord(true);
			displayCPU2.setMargin(new Insets(6, 6, 6, 6));
			displayCPU2.setFont(displayAmountFont);
			displayCPU2.setBackground(new Color(50, 50, 50));
			displayCPU2.setForeground(new Color(220, 220, 220));
		   
			displayCPU3.setEditable(false);
			displayCPU3.setLineWrap(true);
			displayCPU3.setWrapStyleWord(true);
			displayCPU3.setMargin(new Insets(6, 6, 6, 6));
			displayCPU3.setFont(displayAmountFont);
			displayCPU3.setBackground(new Color(50, 50, 50));
			displayCPU3.setForeground(new Color(220, 220, 220));
		   
			add(displayPlayer);
			add(displayCPU1);
			add(displayCPU2);
			add(displayCPU3);
		}
		public void updateAmountDisplay() //This method, when called, sets each
		//JTextArea for displaying amounts to the CORRECT amounts of every single
		//kingdom.
		{
			displayPlayer.setText("PLAYER\n");
			displayCPU1.setText("CPU 1\n");
			displayCPU2.setText("CPU 2\n");
			displayCPU3.setText("CPU 3\n");
		   
			displayPlayer.append("(Kingdom " + gh.getGame().getKingdomChooser().getKingdomPlayer() + ")\n\n");
			displayPlayer.append("Gold: " + gh.getPlayerAmounts()[0] + "\n");
			displayPlayer.append("Resources: " + gh.getPlayerAmounts()[1] + "\n");
			displayPlayer.append("Men: " + gh.getPlayerAmounts()[2] + "\n");
			displayPlayer.append("Land: " + gh.getPlayerAmounts()[3] + "\n");
		   
			displayCPU1.append("(Kingdom " + gh.getGame().getKingdomChooser().getKingdomCPU1() + ")\n\n");
			displayCPU1.append("Gold: " + gh.getCPU1Amounts()[0] + "\n");
			displayCPU1.append("Resources: " + gh.getCPU1Amounts()[1] + "\n");
			displayCPU1.append("Men: " + gh.getCPU1Amounts()[2] + "\n");
			displayCPU1.append("Land: " + gh.getCPU1Amounts()[3] + "\n");
		   
			displayCPU2.append("(Kingdom " + gh.getGame().getKingdomChooser().getKingdomCPU2() + ")\n\n");
			displayCPU2.append("Gold: " + gh.getCPU2Amounts()[0] + "\n");
			displayCPU2.append("Resources: " + gh.getCPU2Amounts()[1] + "\n");
			displayCPU2.append("Men: " + gh.getCPU2Amounts()[2] + "\n");
			displayCPU2.append("Land: " + gh.getCPU2Amounts()[3] + "\n");
		   
			displayCPU3.append("(Kingdom " + gh.getGame().getKingdomChooser().getKingdomCPU3() + ")\n\n");
			displayCPU3.append("Gold: " + gh.getCPU3Amounts()[0] + "\n");
			displayCPU3.append("Resources: " + gh.getCPU3Amounts()[1] + "\n");
			displayCPU3.append("Men: " + gh.getCPU3Amounts()[2] + "\n");
			displayCPU3.append("Land: " + gh.getCPU3Amounts()[3] + "\n");
		}
	}
   //This class contains the JButton to return to the game, or to the win/lose panel
   //if the game already ended.
	class ReturnBar extends JPanel implements ActionListener
	{
		public ReturnBar()
		{
			setLayout(new GridLayout(1, 1));
			JButton returnToGame = new JButton("Press button to return to game");
			returnToGame.setBackground(new Color(40, 40, 40));
			returnToGame.setForeground(new Color(220, 220, 220));
			returnToGame.setFont(new Font("Serif", Font.BOLD, 19));
			returnToGame.addActionListener(this);
			add(returnToGame);
		}
		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();
			if (command.equals("Press button to return to game"))
			{
				if (gh.getEnded() == false) //If the game is still in play
				{
					gh.getGameCards().show(gh, "Play");
					gh.getGame().getTimer().start();
				}
				else //If the game has ended
				{
					gh.getGame().getTimer().stop();
					if (gh.getWon() == true)
						gh.getGameCards().show(gh, "Win");
					else
						gh.getGameCards().show(gh, "Lose");
				}
			}
		}
	}
}

//Class made by Joshua
//This class allows the user to change values of the game to their liking.
class CustomPanel extends JPanel
{
	public CustomPanel(GameHolder gh)
	{
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(120, 93, 122));
		CenterAmountChanger cac = new CenterAmountChanger(gh);
		EmotionSelecter ec = new EmotionSelecter(gh);
		ChangeEventAndSteal ces = new ChangeEventAndSteal(gh);
		add(cac, BorderLayout.CENTER);
		add(ec, BorderLayout.EAST);
		add(ces, BorderLayout.WEST);
	   
		ReturnToGameBar rGB = new ReturnToGameBar(gh);
		add(rGB, BorderLayout.SOUTH);
	}

	class CustomFormat extends JPanel
	{
		//The following are FVs for all the classes that call this class to access
		//and change the information of the components
		private JTextArea tarea;
		private JSlider slider;
		private JTextField tfield;
		public CustomFormat(String jtaInfo, int defaultRange, int minRange, int maxRange)
		{
			setBackground(Color.GRAY);
			setLayout(new GridLayout(3, 1));
			tarea = new JTextArea(jtaInfo);
			tarea.setLineWrap(true);
			tarea.setWrapStyleWord(true);
			tarea.setEditable(false);
			tarea.setBackground(Color.GRAY);
			tarea.setForeground(Color.WHITE);
			tarea.setMargin(new Insets(3, 3, 3, 3));
			slider = new JSlider(minRange, maxRange, defaultRange);
			slider.setBackground(new Color(40, 40, 40));
			tfield = new JTextField(("" + defaultRange));
			tfield.setBackground(new Color(40, 40, 40));
			tfield.setForeground(Color.YELLOW);
		   
			add(tarea);
			add(slider);
			add(tfield);
		}
	   
		///THE FOLLOWING ARE GETTER SETTER METHODS
		public JTextArea getTextArea()
		{
			return tarea;
		}
		public JSlider getSlider()
		{
			return slider;
		}
		private JTextField getTField()
		{
			return tfield;
		}
	}
   
	class CenterAmountChanger extends JPanel
	{
		public CenterAmountChanger(GameHolder ghIn)
		{
			setLayout(new GridLayout(2, 2, 6, 6));
			setBackground(Color.BLACK);
			ChangeAmountGain gain = new ChangeAmountGain(ghIn);
			add(gain);
			ChangeAmountLoss loss = new ChangeAmountLoss(ghIn);
			add(loss);
			ChangeAmountStart start = new ChangeAmountStart(ghIn);
			add(start);
			ChangeAmountGoal goal = new ChangeAmountGoal(ghIn);
			add(goal);
		}
	}
   //This class changes the starting value of how much amounts the player gains 
   //when guessing the correct emotion that MATCHES their kingdom's emotion.
	class ChangeAmountGain extends JPanel implements ActionListener, ChangeListener
	{
		private GameHolder gh; //Must be a FV because actionPerformed will change
		//values in GameHolder when values are changed
		private CustomFormat cf; //Must be a FV because is declared in the constructor
		//then values in CustomFormat must be changed in actionPerformed/stateChanged
		public ChangeAmountGain(GameHolder ghIn)
		{
			setBackground(new Color(180, 180, 180));
			gh = ghIn;
			cf = new CustomFormat("Change how much amounts are gained on correct" +
				" answer (number entered is the starting value, not the RANGE)",
				460, 180, 1000);
			cf.getTField().addActionListener(this);
			cf.getSlider().addChangeListener(this);
			add(cf);
		}
	   
		public void actionPerformed(ActionEvent evt)
		{
			String input = cf.getTField().getText();
			int value = 0;
			try //This try catch block is cited from John Conlin.
			{
				value = Integer.parseInt(input);
				if (value < 180 || value > 1000)
					cf.getTField().setText("Enter within range of 180 and 1000.");
				else
				{
					gh.getGame().setWrongLoss(value);
					cf.getSlider().setValue(value);
				}
			}
			catch (NumberFormatException e)
			{
				cf.getTField().setText("Please enter a number.");
			}
		}
		public void stateChanged(ChangeEvent evt)
		{
			int value = cf.getSlider().getValue();
			gh.getGame().setCorrectGain(value);
			cf.getTField().setText("" + value);
		}
	}
   //This class changes the starting value of how much amounts the player loses 
   //when guessing the wrong emotion.
	class ChangeAmountLoss extends JPanel implements ActionListener, ChangeListener
	{
		private GameHolder gh; //Must be a FV because actionPerformed will change
		//values in GameHolder when values are changed
		private CustomFormat cf; //Must be a FV because is declared in the constructor
		//then values in CustomFormat must be changed in actionPerformed/stateChanged
		public ChangeAmountLoss(GameHolder ghIn)
		{
			setBackground(new Color(180, 180, 180));
			gh = ghIn;
			cf = new CustomFormat("Change how much amounts are lost on incorrect" +
				" answer (number entered is the starting value, not the RANGE)",
				100, 0, 800);
			cf.getTField().addActionListener(this);
			cf.getSlider().addChangeListener(this);
			add(cf);
		}
	   
		public void actionPerformed(ActionEvent evt)
		{
			String input = cf.getTField().getText();
			int value = 0;
			try //This try catch block is cited from John Conlin.
			{
				value = Integer.parseInt(input);
				if (value > 800 || value < 0)
					cf.getTField().setText("Enter within range of 0 and 800.");
				else
				{
					gh.getGame().setWrongLoss(value);
					cf.getSlider().setValue(value);
				}
			}
			catch (NumberFormatException n)
			{
				cf.getTField().setText("Please enter a number.");
			}
		   
		}
		public void stateChanged(ChangeEvent evt)
		{
			int value = cf.getSlider().getValue();
			gh.getGame().setCorrectGain(value);
			cf.getTField().setText("" + value);
		}
	}
   //This class changes the random number's range of how much amounts the player
   //STARTS with at the beginning of the game.
	class ChangeAmountStart extends JPanel implements ActionListener, ChangeListener
	{
		private GameHolder gh; //Must be a FV because actionPerformed will change
		//values in GameHolder when values are changed
		private CustomFormat cf; //Must be a FV because is declared in the constructor
		//then values in CustomFormat must be changed in actionPerformed/stateChanged
		public ChangeAmountStart(GameHolder ghIn)
		{
			setBackground(new Color(180, 180, 180));
			gh = ghIn;
			cf = new CustomFormat("Change how much amounts you start with (number"
				 + " entered is the RANGE, NOT THE STARTING VALUE)", 100, 0, 700);
			cf.getTField().addActionListener(this);
			cf.getSlider().addChangeListener(this);
			add(cf);
		}
	   
		public void actionPerformed(ActionEvent evt)
		{
			String input = cf.getTField().getText();
			int value = 0;
			try //This try catch block is cited from John Conlin.
			{
				value = Integer.parseInt(input);
				if (value > 700 || value < 0)
					cf.getTField().setText("Enter within range of 0 and 700.");
				else
				{
					gh.setAmtStartRange(value);
					cf.getSlider().setValue(value);
				}
			}
			catch (NumberFormatException n)
			{
				cf.getTField().setText("Please enter a number.");
			}
		   
		}
		public void stateChanged(ChangeEvent evt)
		{
			int value = cf.getSlider().getValue();
			gh.setAmtStartRange(value);
			cf.getTField().setText("" + value);
		}
	}
   //This class changes the starting value of the amount goal(how much amounts the 
   //player must reach on each amount to win the game).
	class ChangeAmountGoal extends JPanel implements ActionListener, ChangeListener
	{
		private GameHolder gh; //Must be a FV because actionPerformed will change
		//values in GameHolder when values are changed
		private CustomFormat cf; //Must be a FV because is declared in the constructor
		//then values in CustomFormat must be changed in actionPerformed/stateChanged
		public ChangeAmountGoal(GameHolder ghIn)
		{
			setBackground(new Color(180, 180, 180));
			gh = ghIn;
			cf = new CustomFormat("Change the amount goal (number entered is the"
				 + " entered is the starting value, NOT the RANGE)", 800, 50, 1700);
			cf.getTField().addActionListener(this);
			cf.getSlider().addChangeListener(this);
			add(cf);
		}
	   
		public void actionPerformed(ActionEvent evt)
		{
			String input = cf.getTField().getText();
			int value = 0;
			try //This try catch block is cited from John Conlin.
			{
				value = Integer.parseInt(input);
				if (value > 1700 || value < 50)
					cf.getTField().setText("Enter within range of 50 and 1700.");
				else
				{
					gh.setGoalStartValue(value);
					cf.getSlider().setValue(value);
				}
			}
			catch (NumberFormatException n)
			{
				cf.getTField().setText("Please enter a number.");
			}
		   
		}
		public void stateChanged(ChangeEvent evt)
		{
			int value = cf.getSlider().getValue();
			gh.setGoalStartValue(value);
			cf.getTField().setText("" + value);
		}
	}
   //This class changes which emotions can be displayed as passages, and can be used
   //as answers for passages. Also allows questions to repeat.
	class EmotionSelecter extends JPanel
	{
		public EmotionSelecter(GameHolder ghIn)
		{
			setLayout(new BorderLayout(3, 3));
			Color backgColor = new Color(40, 40, 40);
			setBackground(backgColor);
			JTextArea emotionInfo = new JTextArea("Choose which emotions to include"
				+ " as passages in the game");
			emotionInfo.setPreferredSize(new Dimension(100, 300));
			emotionInfo.setEditable(false);
			emotionInfo.setLineWrap(true);
			emotionInfo.setWrapStyleWord(true);
			emotionInfo.setFont(new Font("Serif", Font.BOLD, 15));
			emotionInfo.setBackground(backgColor);
			emotionInfo.setForeground(new Color(230, 230, 230));
			add(emotionInfo, BorderLayout.NORTH);
		   
			EmotionList el = new EmotionList(ghIn, emotionInfo);
			add(el, BorderLayout.CENTER);
		}
	}
	class EmotionList extends JPanel implements ActionListener
	{
		private GameHolder gh; //Must be a FV to access methods to change values
		//in the game
		//The following JCheckBoxes need to be FVs for actionPerformed to access to
		//check if they are selected or not
		private JCheckBox happyOn;
		private JCheckBox sadOn;
		private JCheckBox angryOn;
		private JCheckBox fearOn;
		private JTextArea infoE; //Is used to update the JTextArea and inform the
		//user if none of the checkboxes are selected true
		public EmotionList(GameHolder ghIn, JTextArea infoEIn)
		{
			gh = ghIn;
			infoE = infoEIn;
			Color backgCBox = new Color(70, 70, 70);
			setLayout(new GridLayout(2, 2, 4, 4));
			setBackground(new Color(30, 30, 30));
		   
			happyOn = new JCheckBox("Include happy emotion");
			happyOn.setForeground(Color.YELLOW);
			happyOn.setBackground(backgCBox);
			happyOn.setSelected(true);
			sadOn = new JCheckBox("Include sad emotion");
			sadOn.setForeground(Color.CYAN);
			sadOn.setBackground(backgCBox);
			sadOn.setSelected(true);
			angryOn = new JCheckBox("Include angry emotion");
			angryOn.setForeground(Color.RED);
			angryOn.setBackground(backgCBox);
			angryOn.setSelected(true);
			fearOn = new JCheckBox("Include fear emotion");
			fearOn.setForeground(Color.MAGENTA);
			fearOn.setBackground(backgCBox);
			fearOn.setSelected(true);
		   
			happyOn.addActionListener(this);
			sadOn.addActionListener(this);
			angryOn.addActionListener(this);
			fearOn.addActionListener(this);
			add(happyOn);
			add(sadOn);
			add(angryOn);
			add(fearOn);
		}
		public void actionPerformed(ActionEvent evt)
		{
			gh.getGame().setIsCustomPassages(true);
			infoE.setText("Choose which emotions to include as passages in the game");
			String command = evt.getActionCommand();
			boolean[] newEmotionsOn = new boolean[4];
			for (int i = 0; i < newEmotionsOn.length; i++)
				newEmotionsOn[i] = gh.getGame().getEmotionOn()[i];
			if (command.equals("Include happy emotion"))
			{
				if (!happyOn.isSelected())
				{
					newEmotionsOn[0] = false;
				}
				else
					newEmotionsOn[0] = true;
			}
			else if (command.equals("Include sad emotion"))
			{
				if (!sadOn.isSelected())
					newEmotionsOn[1] = false;
				else
					newEmotionsOn[1] = true;
			}
			else if (command.equals("Include angry emotion"))
			{
				if (!angryOn.isSelected())
					newEmotionsOn[2] = false;
				else
					newEmotionsOn[2] = true;
			}
			else if (command.equals("Include fear emotion"))
			{
				if (!fearOn.isSelected())
					newEmotionsOn[3] = false;
				else
					newEmotionsOn[3] = true;
			}
		   //If the user turns off all emotions, the game cannot function at all,
		   //so that is prevented with this if statement
			if (!happyOn.isSelected() && !sadOn.isSelected() && !angryOn.isSelected()
				&& !fearOn.isSelected())
			{
				infoE.append("\n\nYou must have at least one emotion selected true!");
				happyOn.setSelected(true);
				sadOn.setSelected(true);
				angryOn.setSelected(true);
				fearOn.setSelected(true);
				for (int i = 0; i < newEmotionsOn.length; i++)
					newEmotionsOn[i] = true;
			}
			gh.getGame().setEmotionOn(newEmotionsOn);
			gh.getGame().getCenterQuestions().checkButtonActive();
		}
	}
   
	class ChangeEventAndSteal extends JPanel
	{
		public ChangeEventAndSteal(GameHolder ghIn)
		{
			setLayout(new BorderLayout(13, 0));
			setBackground(new Color(255, 223, 244));
			//setPreferredSize(new Dimension(440, 700));///testing
			AmountEventChanger aec = new AmountEventChanger(ghIn);
			AmountStealingChanger asc = new AmountStealingChanger(ghIn);
			add(aec, BorderLayout.WEST);
			add(asc, BorderLayout.EAST);
		}
	}
	//This class has values that customizes Amount Events.
	class AmountEventChanger extends JPanel
	{
		public AmountEventChanger(GameHolder ghIn)
		{
			setLayout(new BorderLayout(3, 20));
			setBackground(new Color(59, 161, 49));
			JLabel eventInfo = new JLabel("Customize Amount Events");
			eventInfo.setFont(new Font("Serif", Font.PLAIN, 14));
			eventInfo.setForeground(Color.GREEN);
			add(eventInfo, BorderLayout.NORTH);
			ChangeEventPower cep = new ChangeEventPower(ghIn);
			add(cep, BorderLayout.CENTER);
			ChangeEventChance cec = new ChangeEventChance(ghIn);
			add(cec, BorderLayout.SOUTH);
		}
	}
	class ChangeEventPower extends JPanel
		//This class changes how MUCH you gain/lose from random events, as well as how
		//MUCH time is lost during time loss
	{
		private GameHolder gh; //Must be a FV to access methods to change values
		//in the game
		//The following FVs must be a FV because is declared in the constructor
		//then values in CustomFormat must be changed in actionPerformed/stateChanged
		//of the handler classes
		private CustomFormat eGainLoss;
		private CustomFormat eTime;
	   
		public ChangeEventPower(GameHolder ghIn)
		{
			gh = ghIn;
			setLayout(new GridLayout(2, 1, 2, 2));
			setBackground(Color.GRAY);
			GainAndLossHandler galH = new GainAndLossHandler();
			TimeStealHandler timeH = new TimeStealHandler();
		   
			eGainLoss = new CustomFormat("Change the amount GAIN AND amount LOSS"
				+ " from the Random Event(number entered is the RANGE, not the starting"
				+ " value)", 50, 0, 900);
			eGainLoss.getTField().addActionListener(galH);
			eGainLoss.getSlider().addChangeListener(galH);
			add(eGainLoss);
		   
			eTime = new CustomFormat("Change the TIME STOLEN from the Random Event"
				+ " (measured in seconds)", 10, 0, 50);
			eTime.getTField().addActionListener(timeH);
			eTime.getSlider().addChangeListener(timeH);
			add(eTime);
		}
		///The following classes are handler classes for each of the above CustomFormats
		class GainAndLossHandler implements ActionListener, ChangeListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				String input = eGainLoss.getTField().getText();
				int value = 0;
				try //This try catch block is cited from John Conlin.
				{
					value = Integer.parseInt(input);
					if (value < 50 || value > 900)
						eGainLoss.getTField().setText("Enter within range of 50 and 900.");
					else
					{
						gh.getGame().getGameBottomBar().getAmountEvents().setEventRange(value);
						eGainLoss.getSlider().setValue(value);
					}
				}
				catch (NumberFormatException e)
				{
					eGainLoss.getTField().setText("Please enter a number.");
				}
			}
			public void stateChanged(ChangeEvent evt)
			{
				int value = eGainLoss.getSlider().getValue();
				gh.getGame().getGameBottomBar().getAmountEvents().setEventRange(value);
				eGainLoss.getTField().setText("" + value);
			}
		}
		class TimeStealHandler implements ActionListener, ChangeListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				String input = eTime.getTField().getText();
				int value = 0;
				try //This try catch block is cited from John Conlin.
				{
					value = Integer.parseInt(input);
					if (value < 0 || value > 50)
						eTime.getTField().setText("Enter within range of 0 and 50.");
					else
					{
						gh.getGame().getGameBottomBar().getAmountEvents().setTimeSteal(value);
						eTime.getSlider().setValue(value);
					}
				}
				catch (NumberFormatException e)
				{
					eTime.getTField().setText("Please enter a number.");
				}
			}
			public void stateChanged(ChangeEvent evt)
			{
				int value = eTime.getSlider().getValue();
				gh.getGame().getGameBottomBar().getAmountEvents().setTimeSteal(value);
				eTime.getTField().setText("" + value);
			}
		}
	}
	class ChangeEventChance extends JPanel
	//This class allows the user to change the likelihood of EACH kingdom being
	//able to get affected by amount events
	{
		//The following must be FVs because the handler classes must be able to
		//access these variables to change them
		private GameHolder gh;
		public ChangeEventChance(GameHolder ghIn)
		{
			gh = ghIn;
			setLayout(new GridLayout(2, 2, 2, 2));
			setBackground(new Color(180, 180, 180));
			PercentFormat playerEvent = new PercentFormat("Always Player", "50% Player", "Never Player");
			PercentFormat CPU1Event = new PercentFormat("Always CPU1", "50% CPU1", "Never CPU1");
			PercentFormat CPU2Event = new PercentFormat("Always CPU2", "50% CPU2", "Never CPU2");
			PercentFormat CPU3Event = new PercentFormat("Always CPU3", "50% CPU3", "Never CPU3");
		   
			PlayerEventHandler peh = new PlayerEventHandler();
			CPU1EventHandler c1eh = new CPU1EventHandler();
			CPU2EventHandler c2eh = new CPU2EventHandler();
			CPU3EventHandler c3eh = new CPU3EventHandler();
			playerEvent.getPercent100().addActionListener(peh);
			playerEvent.getPercent50().addActionListener(peh);
			playerEvent.getPercent0().addActionListener(peh);
		   
			CPU1Event.getPercent100().addActionListener(c1eh);
			CPU1Event.getPercent50().addActionListener(c1eh);
			CPU1Event.getPercent0().addActionListener(c1eh);
		   
			CPU2Event.getPercent100().addActionListener(c2eh);
			CPU2Event.getPercent50().addActionListener(c2eh);
			CPU2Event.getPercent0().addActionListener(c2eh);
		   
			CPU3Event.getPercent100().addActionListener(c3eh);
			CPU3Event.getPercent50().addActionListener(c3eh);
			CPU3Event.getPercent0().addActionListener(c3eh);
		   
			add(playerEvent);
			add(CPU1Event);
			add(CPU2Event);
			add(CPU3Event);
		}
		///The following classes are handler classes
		class PlayerEventHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				String command = evt.getActionCommand();
				if (command.equals("Always Player"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChancePlayer(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartPlayer(1);
				}
				else if (command.equals("50% Player"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChancePlayer(2);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartPlayer(0);
				}
				else if (command.equals("Never Player"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChancePlayer(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartPlayer(0);
				}
			}
		}
		class CPU1EventHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				String command = evt.getActionCommand();
				if (command.equals("Always CPU1"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU1(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU1(1);
				}
				else if (command.equals("50% CPU1"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU1(2);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU1(0);
				}
				else if (command.equals("Never CPU1"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU1(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU1(0);
				}
			}
		}
		class CPU2EventHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				String command = evt.getActionCommand();
				if (command.equals("Always CPU2"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU2(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU2(1);
				}
				else if (command.equals("50% CPU2"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU2(2);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU2(0);
				}
				else if (command.equals("Never CPU2"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU2(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU2(0);
				}
			}
		}
		class CPU3EventHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				String command = evt.getActionCommand();
				if (command.equals("Always CPU3"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU3(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU3(1);
				}
				else if (command.equals("50% CPU3"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU3(2);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU3(0);
				}
				else if (command.equals("Never CPU3"))
				{
					gh.getGame().getGameBottomBar().getAmountEvents().setChanceCPU3(1);
					gh.getGame().getGameBottomBar().getAmountEvents().setStartCPU3(0);
				}
			}
		}
	}
	class PercentFormat extends JPanel
	{
		//The following FVs are FVs so that the classes using this class can also
		//access these FVs; mostly for handler classes
		private JRadioButton percent100;
		private JRadioButton percent50;
		private JRadioButton percent0;
		public PercentFormat(String text100, String text50, String text0)
		{
			setLayout(new GridLayout(3, 1));
			percent100 = new JRadioButton(text100);
			percent50 = new JRadioButton(text50);
			percent0 = new JRadioButton(text0);
			percent100.setBackground(new Color(100, 100, 100));
			percent50.setBackground(new Color(100, 100, 100));
			percent0.setBackground(new Color(100, 100, 100));
			percent100.setForeground(new Color(210, 210, 210));
			percent50.setForeground(new Color(210, 210, 210));
			percent0.setForeground(new Color(210, 210, 210));
		   
			ButtonGroup bg = new ButtonGroup();
			bg.add(percent100);
			bg.add(percent50);
			bg.add(percent0);
			add(percent100);
			add(percent50);
			add(percent0);
			percent50.setSelected(true);
		}
		///THE FOLLOWING ARE GETTER SETTER METHODS
		public JRadioButton getPercent100()
		{
			return percent100;
		}
		public JRadioButton getPercent50()
		{
			return percent50;
		}
		public JRadioButton getPercent0()
		{
			return percent0;
		}
	}
   //This class has values that customizes amount stealing.
	class AmountStealingChanger extends JPanel
	{
		public AmountStealingChanger(GameHolder ghIn)
		{
			setLayout(new BorderLayout(3, 3));
			setBackground(new Color(113, 40, 35));
			JLabel stealingInfo = new JLabel("Customize Amount Stealing/Kingdom Events");
			stealingInfo.setFont(new Font("Serif", Font.PLAIN, 14));
			stealingInfo.setForeground(Color.RED);
			add(stealingInfo, BorderLayout.NORTH);
			ChangeStealingPower csp = new ChangeStealingPower(ghIn);
			add(csp, BorderLayout.CENTER);
			ChangeStealOrder cso = new ChangeStealOrder(ghIn);
			add(cso, BorderLayout.SOUTH);
		}
	}
   
	class ChangeStealingPower extends JPanel implements ActionListener, ChangeListener
	//This class changes how MUCH EACH kingdom will gain/lose from amount stealing
	{
		private GameHolder gh; //Must be a FV to access methods to change values
		//in the game
		private CustomFormat changeStealStrength; //must be a FV because declared in
		//constructor, then values in CustomFormat must be changed in actionPerformed/stateChanged
		public ChangeStealingPower(GameHolder ghIn)
		{
			gh = ghIn;
			setLayout(new GridLayout(1, 1, 2, 2));
			setBackground(Color.GRAY);
			changeStealStrength = new CustomFormat("Change how MUCH amounts are stolen"
				+ " by EACH kingdom (each kingdom steals 1/X of the player's"
				+ " amounts, the number entered is X)", 4, 1, 20);
			changeStealStrength.getTField().addActionListener(this);
			changeStealStrength.getSlider().addChangeListener(this);
			add(changeStealStrength);
		}
	   
		public void actionPerformed(ActionEvent evt)
		{
			String input = changeStealStrength.getTField().getText();
			int value = 0;
			try //This try catch block is cited from John Conlin.
			{
				value = Integer.parseInt(input);
				if (value < 1 || value > 20)
					changeStealStrength.getTField().setText("Enter within range of 1 and 20.");
				else
				{
					gh.getGame().getGameBottomBar().getAmountStealing().setStealStrength(value);
					changeStealStrength.getSlider().setValue(value);
				}
			}
			catch (NumberFormatException e)
			{
				changeStealStrength.getTField().setText("Please enter a number.");
			}
		}
		public void stateChanged(ChangeEvent evt)
		{
			int value = changeStealStrength.getSlider().getValue();
			gh.getGame().getGameBottomBar().getAmountStealing().setStealStrength(value);
			changeStealStrength.getTField().setText("" + value);
		}
	}
	//This class changes the stealing order in amount stealing.
	class ChangeStealOrder extends JPanel implements ActionListener
	{
		private GameHolder gh; //Must be a FV to access methods to change values
		//in the game
		//The following FVs must be FVs for actionPerformed to access the components
		//for their properties
		private JRadioButton order1;
		private JRadioButton order2;
		public ChangeStealOrder(GameHolder ghIn)
		{
			gh = ghIn;
			Color radioBackg = new Color(60, 60, 60);
			Color radioText = Color.WHITE;
			setLayout(new GridLayout(2, 1));
			order1 = new JRadioButton("CPUs steal amounts -> You steal amounts");
			order1.setBackground(radioBackg);
			order1.setForeground(radioText);
			order2 = new JRadioButton("You steal amounts -> CPUs steal amounts");
			order2.setBackground(radioBackg);
			order2.setForeground(radioText);
		   
			order1.addActionListener(this);
			order2.addActionListener(this);
		   
			ButtonGroup bg = new ButtonGroup();
			bg.add(order1);
			bg.add(order2);
			add(order1);
			add(order2);
			order1.setSelected(true);
		}
	   
		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();
			if (command.equals("CPUs steal amounts -> You steal amounts"))
			{
				gh.getGame().getGameBottomBar().getAmountStealing().setStealOrder(0);
			}
			else if (command.equals("You steal amounts -> CPUs steal amounts"))
			{
				gh.getGame().getGameBottomBar().getAmountStealing().setStealOrder(1);
			}
		}
	}
   
   //This class allows the user to return to the actual game, that is, the difficulties
   //panel, when they are done in CustomPanel.
	class ReturnToGameBar extends JPanel implements ActionListener
	{
		private GameHolder gh; //Must be a FV in order for actionPerformed to use
		//the game's cardlayout to switch to the correct card
		public ReturnToGameBar(GameHolder ghIn)
		{
			gh = ghIn;
			setLayout(new GridLayout(1, 1));
			JButton goToGame = new JButton("PRESS WHEN READY TO START GAME");
			goToGame.setFont(new Font("Serif", Font.BOLD, 22));
			goToGame.setBackground(new Color(245, 238, 165));
			goToGame.addActionListener(this);
			add(goToGame);
		}
	   
		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();
			if (command.equals("PRESS WHEN READY TO START GAME"))
			{
				gh.getGameCards().show(gh, "Difficulties");
			}
		}
	}
}

//Class done by Joshua
//This class creates and sets up the files, Scanners, and PrintWriters essential
//to the game's functionality.
class PassagesHolder
{
	private Scanner reader; //Must be a FV because the same Scanner that was set
	// up must also be returned in a getter method to classes that pass in PassagesHolder
	private PrintWriter printResults; //Must be a FV to be accessible in classes that
	//call PassagesHolder
	private Scanner results; //Must be a FV to be accessible in classes that call PassagesHolder

	public PassagesHolder()
	{
		reader = null;
		printResults = null;
		results = null;
		setPrintWriter();
	}

	public void setFile() //The try catch block to set up the Scanner and the FileIO
	// (the passages for the questions)
	{
		if (reader != null) reader.close();
		String passageName = new String("passages.txt");
		File passages = new File(passageName);
		try
		{
			reader = new Scanner(passages);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: File " + passageName + " cannot be found.");
			e.printStackTrace();
		}
	}

	public void setPrintWriter() //The try catch block to set up the PrintWriter
	// (writing the results of how many questions the user got right)
	{
		if (printResults != null) printResults.close();
		String fileName = "results.txt";
		File resultsFile = new File(fileName);
		try
		{
			printResults = new PrintWriter(resultsFile);
		}
		catch(IOException e)
		{
			System.err.println("\n\nERROR: cannot create " + fileName + " file.\n\n");
			e.printStackTrace();
		}
	}

	public void setResults() //The try catch block to set up the Scanner for the PRINTWRITER
	{
		
String inFileName = new String("results.txt");
		File resultFile = new File(inFileName);
		try
		{
			results = new Scanner(resultFile);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: File " + inFileName + " cannot be found.");
			e.printStackTrace();
		}
	}
	public void close()
	{
		if (reader != null) reader.close();
		if (results != null) results.close();
		if (printResults != null) printResults.close();
	}
	///THE FOLLOWING ARE GETTER SETTER METHODS
	public Scanner getScanner()
	{
		return reader;
	}
	public PrintWriter getPrintWriter()
	{
		return printResults;
	}
	public Scanner getResults()
	{
		return results;
	}
}


