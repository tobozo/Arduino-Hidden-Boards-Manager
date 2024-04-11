package com.arduino.hiddenboardsmanager;

import processing.app.BaseNoGui;
import processing.app.debug.TargetPlatform;
import processing.app.helpers.FileUtils;

import com.arduino.hiddenboardsmanager.UI;
import com.arduino.hiddenboardsmanager.UI.JButtonIcon;
import com.arduino.hiddenboardsmanager.UI.JTransparentPanel;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.util.*;
import java.util.Map.Entry;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;



@SuppressWarnings("serial")
public class BoardsManager
{

  private Map<String, String> boardLongNames = new HashMap<String, String>();

  //private HashSet<String> prominentBoards = new HashSet<String>( Arrays.asList("menu", "esp32", "esp32s2", "esp32s3", "esp32c3", "esp32wrover") );
  private HashSet<String> boards = new HashSet<String>();
  private HashSet<String> hiddenBoards = new HashSet<String>();

  private JPanel boardsPanel;
  private JScrollPane hideScrollPane;
  private JTextArea boardsLocalTextArea;

  private String packageName;
  private String boardsLocalPath;
  private String boardsPath;
  private String boardsLocalTxt;

  public BoardsManager()
  {
    init();
  }


  public void init()
  {
    BaseNoGui.onBoardOrPortChange();
    this.packageName     = BaseNoGui.getTargetPlatform().getId();
    this.boardsLocalPath = BaseNoGui.getTargetPlatform().getFolder().toString() + "/boards.local.txt";
    this.boardsPath      = BaseNoGui.getTargetPlatform().getFolder().toString() + "/boards.txt";

    boardsPanel = new JTransparentPanel();

    hideScrollPane = new JScrollPane( boardsPanel );
    hideScrollPane.setOpaque(false);
    hideScrollPane.setViewportBorder(null);
    hideScrollPane.setBorder( BorderFactory.createEmptyBorder(0, 0, 0, 0) );
    hideScrollPane.getViewport().setOpaque(false);
    hideScrollPane.getViewport().setBorder(null);
    hideScrollPane.getViewport().getInsets().set(0, 0, 0, 0);
    hideScrollPane.getVerticalScrollBar().setUnitIncrement(100); // prevent the scroll wheel from going sloth

    boardLongNames.clear();
    boards.clear();
    hiddenBoards.clear();

    processBoardsFile( boardsPath );
    processBoardsFile( boardsLocalPath );

    processBoardsLocal();
    processBoards();
  }


  public void processBoards()
  {
    boardsPanel.removeAll();
    boardsPanel.setLayout(new GridLayout(boardLongNames.size(), 0, 0, 0));
    Map<String, String> sortedBoardLongNames = sortByComparator(boardLongNames, true);
    int pos = 0;
    for (Map.Entry<String, String> entry : sortedBoardLongNames.entrySet()) {
      String board = entry.getKey();
      String boardLongName = entry.getValue();
      JCheckBox checkbox = new JCheckBox();
      checkbox.setName(board);
      checkbox.setSelected( !hiddenBoards.contains(board) );
      checkbox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JCheckBox checkbox = (JCheckBox) e.getSource();
          String label = checkbox.getName();
          if(checkbox.isSelected()) hiddenBoards.remove(label);
          else hiddenBoards.add(label);
          processBoardsLocal();
        }
      });
      Color color = new Color( 0x80, 0x80, 0x80, pos%2==0 ? 0x20 : 0x40 );
      JPanel panel = new JTransparentPanel(color);
      panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
      panel.setBorder( BorderFactory.createEmptyBorder(0, 5, 0, 0) ); // top,left,bottom,right
      panel.add(checkbox);

      JLabel boardLabel = new JLabel(boardLongName);
      boardLabel.setOpaque(false);
      boardLabel.setBorder( BorderFactory.createEmptyBorder(0, 5, 0, 0) ); // top,left,bottom,right
      panel.add( boardLabel );

      boardsPanel.add(panel, BorderLayout.CENTER);
      pos++;
    }
  }


  public void processBoardsLocal()
  {
    boardsLocalTxt = "";
    try {
      Reader reader = new FileReader(boardsLocalPath);
      BufferedReader br = new BufferedReader( reader );
      String thisLine = null;
      while ((thisLine = br.readLine()) != null) {
        thisLine = thisLine.trim();
        boolean isHiddenBoard = false;
        if( !thisLine.isEmpty() ) {
          Properties blah = new Properties();
          blah.load(new StringReader(thisLine));
          final Set<String> keys = blah.stringPropertyNames();
          // reprint all entries except those with "hide" key
          for (final String key : keys) {
            if( key.endsWith(".hide") ) {
              isHiddenBoard = true;
            } else if( key.endsWith(".name")) {
              // collect board long names
              String[] keyParts = key.split("\\.");
              if( keyParts == null || keyParts.length==0 ) continue;
              if(boardLongNames.get(keyParts[0])==null)
                boardLongNames.put(keyParts[0], blah.getProperty(key));
            }
          }
        }
        if( ! isHiddenBoard ) {
          boardsLocalTxt += thisLine + "\n";
        }
      }

    } catch ( IOException e ) {
      //System.out.println("Failed to read " + boardsLocalTxt );
    }

    if( hiddenBoards.size() > 0 ) {
      for( String hiddenBoard : hiddenBoards ) {
        boardsLocalTxt += hiddenBoard + ".hide = true\n";
      }
    }
  }


  public void saveBoardsLocal()
  {
    try {
      Writer boardsLocalWriter = new FileWriter(boardsLocalPath);
      boardsLocalWriter.write( boardsLocalTxt );
      boardsLocalWriter.close();
      System.out.println("Saved " + boardsLocalPath );
    } catch( IOException e ) {
      System.err.println("Failed to save " + boardsLocalPath );
      System.out.println( e.getMessage() );
    }
  }


  public JButton getAboutButton( JEditorPane targetPanel )
  {

    JButton aboutBtn = new JButtonIcon("About", "/about.png");
    aboutBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/tbz.png"));
        JOptionPane.showConfirmDialog(null, targetPanel, "About ESP32BoardsTool", JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE, icon);
      }
    });
    return aboutBtn;
  }


  public JLabel JBoldLabel( String content )
  {
    JLabel label = new JLabel(content);
    label.setFont( UI.defaultBoldFont.deriveFont(Font.BOLD, 13) );
    return label;
  }


  public JScrollPane getHidePanel()
  {
    return hideScrollPane;
  }


  public JPanel getTopPanel()
  {
    JPanel topPanel = new JTransparentPanel();
    topPanel.setLayout(new GridLayout(4, 0, 0, 0));
    topPanel.setBorder( BorderFactory.createEmptyBorder(10, 20, 10, 20) );  // top,left,bottom,right
    topPanel.add(JBoldLabel("Boards Package: " + this.packageName ));
    topPanel.add(JBoldLabel("1) Uncheck items to hide from Tools/Board Menu"));
    topPanel.add(JBoldLabel("2) Click on Save icon"));
    topPanel.add(JBoldLabel("3) Restart Arduino IDE"));
    return topPanel;
  }


  public JPanel getBottomPanel()
  {
    JPanel bottomPanel = new JTransparentPanel();
    bottomPanel.setLayout(new GridLayout(0, 2, 0, 0));
    JButton saveButton = new JButtonIcon("Save", "/save.png");
    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveBoardsLocal();
        // TODO: dialog message to restard Arduino IDE or change board and change back
      }
    });
    bottomPanel.add(saveButton);
    return bottomPanel;
  }


  public void processBoardsFile( String path )
  {
    try {
      String boardsTxtPath = path;
      Reader reader = new FileReader(boardsTxtPath);
      BufferedReader br = new BufferedReader( reader );
      String thisLine = null;
      while ((thisLine = br.readLine()) != null) {
        thisLine = thisLine.trim();
        if( thisLine.isEmpty() || thisLine.startsWith("#") ) continue;
        Properties blah = new Properties();
        blah.load(new StringReader(thisLine));
        final Set<String> keys = blah.stringPropertyNames();
        for (final String key : keys) {
          String[] keyParts = key.split("\\.");
          if( keyParts == null || keyParts.length==0 ) continue;
          String thisBoard = keyParts[0].trim();
          if( thisBoard==null || thisBoard.isEmpty() )  continue;
          if( !boards.contains(thisBoard)) boards.add(thisBoard);
          String value = blah.getProperty(key);
          if ( key.endsWith(".hide") ) {
            // TODO: check actual value of "hide" attribute, can be true/false; 0/1
            if(!hiddenBoards.contains(thisBoard)) {
              hiddenBoards.add(thisBoard);
            }
          } else if ( key.endsWith(".name") ) {
            if(boardLongNames.get(thisBoard)==null) boardLongNames.put(thisBoard, value);
          }
        }
      }

      // make sure base boards aren't hidden
      // for( String prominentBoard : prominentBoards ) {
      //   if( boards.contains(prominentBoard) ) {
      //     boards.remove(prominentBoard);
      //   }
      //   if( hiddenBoards.contains(prominentBoard) ) {
      //     hiddenBoards.remove(prominentBoard);
      //   }
      // }
    } catch (IOException e) {
      //System.err.println("unable to open " + path);
      //System.out.println( e.getMessage() );
    }
  }


  private static Map<String, String> sortByComparator(Map<String, String> unsortMap, final boolean order)
  {
    List<Entry<String, String>> list = new LinkedList<Entry<String, String>>(unsortMap.entrySet());
    // Sorting the list based on values
    Collections.sort(list, new Comparator<Entry<String, String>>() {
      public int compare(Entry<String, String> o1, Entry<String, String> o2) {
        if (order) {
          return o1.getValue().compareTo(o2.getValue());
        } else {
          return o2.getValue().compareTo(o1.getValue());
        }
      }
    });
    // Maintaining insertion order with the help of LinkedList
    Map<String, String> sortedMap = new LinkedHashMap<String, String>();
    for (Entry<String, String> entry : list) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }


}

