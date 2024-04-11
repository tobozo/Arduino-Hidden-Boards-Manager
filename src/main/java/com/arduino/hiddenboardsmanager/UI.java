package com.arduino.hiddenboardsmanager;

import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;


@SuppressWarnings("serial")

public class UI extends JPanel
{

  private static final long serialVersionUID = 1L;


  private BoardsManager boardsManager;
  private AboutPanel aboutPanel;

  private JButton aboutBtn;
  private JScrollPane boardsPanel;
  private JPanel topPanel;
  private JPanel bottomPanel;

  static Color transparentColor = new Color( 0xff, 0xff, 0xff, 0x80 );

  static Font defaultFont;
  static Font defaultBoldFont;
  static Font condensedFont;
  static Font monotypeFont;
  static Font monotypeBoldFont;


  public UI()
  {
    // Show tool tips immediately
    ToolTipManager.sharedInstance().setInitialDelay(0);

    defaultFont = this.loadFont("DejaVuSans", 12, 0);
    defaultBoldFont = this.loadFont("DejaVuSans-Bold", 12, 2);
    monotypeFont = this.loadFont("DejaVuSansMono", 12, 1);
    monotypeBoldFont = this.loadFont("DejaVuSansMono-Bold", 12, 3);
    condensedFont= this.loadFont("DejaVuSansCondensed", 12, 1);

    setLayout(new BorderLayout(0, 0));
    setOpaque(false); // transparent background!
    init();
  }

  private void init()
  {
    boardsManager = new BoardsManager();
    aboutPanel = new AboutPanel();
    createPanels();
  }

  private void createPanels()
  {
    boardsPanel = boardsManager.getHidePanel();
    topPanel = boardsManager.getTopPanel();
    bottomPanel = boardsManager.getBottomPanel();
    aboutBtn = boardsManager.getAboutButton( aboutPanel );

    bottomPanel.add(aboutBtn);

    add( topPanel, BorderLayout.NORTH);
    add( boardsPanel, BorderLayout.CENTER);
    add( bottomPanel, BorderLayout.SOUTH);

  }

  private Font loadFont(String fontBaseName, int defaultSize, int fallbackType) // no path no extension
  {
    try {
      InputStream is = getClass().getResourceAsStream("/Fonts/"+fontBaseName+".ttf");
      Font matchedFont = Font.createFont(Font.TRUETYPE_FONT, is);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(matchedFont);
      //System.out.printf("Registered font %s size %d type %d\n", fontBaseName, defaultSize, fallbackType);
      return matchedFont;
    } catch (IOException e) {
        e.printStackTrace();
    } catch (FontFormatException e) {
        e.printStackTrace();
    }
    System.err.println("Failed to load font " + fontBaseName );
    switch( fallbackType ) {
      case 0: return new Font("Tahoma", Font.PLAIN, defaultSize);
      case 1: return new Font("Monospaced", Font.PLAIN, defaultSize);
      case 2: return new Font("Tahoma", Font.BOLD, defaultSize);
      case 3: return new Font("Monospaced", Font.BOLD, defaultSize);
      default:
        System.err.printf("Error font fallbackType %d unsupported\n", fallbackType );
        return new Font("Tahoma", Font.PLAIN, defaultSize);
    }
  }

  public static final class JTransparentPanel extends JPanel
  {
    public JTransparentPanel(Color color) {
      if( color.getAlpha() >= 0xff ) {
        // force alpha 0.5
        color = new Color( color.getRed(), color.getGreen(), color.getBlue(), 0x80 );
      }
      setBackground( color );
    }
    public JTransparentPanel() {
      setOpaque(false);
    }
  }

  public static final class JButtonIcon extends JButton
  {
    public JButtonIcon( String title, String iconPath) {
      try {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        setIcon(icon);
        setBorder(null);
        //setRolloverIcon(icon);
        //setBorderPainted(false);
        //setContentAreaFilled(false);
        //setFocusPainted(false);
        setOpaque( false );
        setPreferredSize( new Dimension( 36, 26 ) );
        setToolTipText(" " + title + " ");
      } catch (Exception ex) {
        setText(" " + title + " ");
      }
    }
  }

}
