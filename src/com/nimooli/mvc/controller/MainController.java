package com.nimooli.mvc.controller;

import com.nimooli.mvc.Utils;
import com.nimooli.mvc.model.Cat;
import com.nimooli.mvc.model.Config;
import com.nimooli.mvc.model.DIRECTION;
import com.nimooli.mvc.model.Mouse;
import com.nimooli.mvc.view.BoardView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

/**
 * Created by Nima Ghafoori on 5/9/14.
 */
public class MainController extends JFrame implements ActionListener,
        KeyListener {
    public static final String ACTION_NEW_GAME1 = "newGame1";
    public static final String ACTION_NEW_GAME2 = "newGame2";
    public static final String ACTION_NEW_GAME3 = "newGame3";
    public static final String ACTION_NEW_GAME4 = "newGame4";
    public static final String ACTION_NEW_GAME5 = "newGame5";
    public static final String ACTION_EXIT_GAME = "exitGame";
    public static final String ACTION_ABOUT = "helpAbout";

    private Mouse mouse;
    private BoardView boardView;
    private java.util.List<Cat> cats;
    private JPanel mainPanel;

    public MainController() {
        initUI();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainController mainController = new MainController();
                mainController.setVisible(true);
            }
        });
    }

    /*
     * Setup the GUI and add ActionListeners
     */
    private void initUI() {

        setTitle("Cat vs Mouse - Team IMA");
        setSize(380, 460);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getRootPane());

        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // This will contains the menu bar
        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);

        // Setup the menu and its action listeners
        initMenu(menuBar);

        // Setup keyboard input listener
        addKeyListener(this);

        newGame(1);
    }
    
                
    //refactor
    private int levelCount = 0;
    private void addNewLevel(String actionCommand, JMenu fileMenu ){
        levelCount+=1;
        String input,level = ((Integer)levelCount).toString();
        if(levelCount==1){
            input = "new game";
        }
        else{
            input = "Level "+level;
        }
        JMenuItem newGame = new JMenuItem(input);
        newGame.setToolTipText("Start level "+level);
        newGame.setActionCommand(actionCommand);
        newGame.addActionListener(this);
        fileMenu.add(newGame);
    }
    private void addNewMenu(String desc, String tooltip, String actionCommand, JMenu menu){
        JMenuItem item = new JMenuItem(desc);
        item.setToolTipText(tooltip);
        item.setActionCommand(actionCommand);
        item.addActionListener(this);
        menu.add(item);
    }
    /*
     * Setup the menu
     */
    private void initMenu(JMenuBar menuBar) {

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        // File -> New game menu item
        addNewLevel(ACTION_NEW_GAME1, fileMenu );
        addNewLevel(ACTION_NEW_GAME2, fileMenu );
        addNewLevel(ACTION_NEW_GAME3, fileMenu );
        addNewLevel(ACTION_NEW_GAME4, fileMenu );
        addNewLevel(ACTION_NEW_GAME5, fileMenu );
            
        // File -> Exit game menu item
        addNewMenu("Exit (Esc)","Exit the game", ACTION_EXIT_GAME, fileMenu);
                
        // Help -> About menu item
        addNewMenu("About", "About the developers", ACTION_ABOUT , helpMenu);

    }

    private void newGame(int level) {
        if (boardView != null) {
            mainPanel.remove(boardView);
        }

        // Add the board view to main panel
        URL configFileUrl = Utils.class.getResource("levels/" + level);
        Config config = null;
        try {
            config = Utils.getElementsFromFile(configFileUrl.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mouse = config.getMouse();
        cats = config.getCats();

        boardView = new BoardView(config);
        boardView.notifyLivesChanged(mouse.getLives());
        mainPanel.add(boardView, BorderLayout.CENTER);

        for (Cat c : cats) {
            Thread t = new Thread(c);
            t.start();
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /*
     * Used for responding to menu clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals(ACTION_NEW_GAME1)) {
            System.out.println("New Game");
            newGame(1);
        } else if (action.equals(ACTION_NEW_GAME2)) {
            System.out.println("Start level 2");
            newGame(2);
        } else if (action.equals(ACTION_NEW_GAME3)) {
            System.out.println("Start level 3");
            newGame(3);
        } else if (action.equals(ACTION_NEW_GAME4)) {
            System.out.println("Start level 4");
            newGame(4);
        } else if (action.equals(ACTION_NEW_GAME5)) {
            System.out.println("Start level 5");
            newGame(5);
        } else if (action.equals(ACTION_EXIT_GAME)) {
            System.out.println("Exit the game");
            System.exit(0);
        } else if (action.equals(ACTION_ABOUT)) {
            System.out.println("Show about dialog");
        }
    }

    /*
     * Used for responding to keyboard inputs
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                mouse.move(DIRECTION.NORTH, null);
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                mouse.move(DIRECTION.SOUTH, null);
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                mouse.move(DIRECTION.WEST, null);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                mouse.move(DIRECTION.EAST, null);
                break;

            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }

        boardView.notifyDataChanged();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}
