/*
  MIT License

  Copyright (c) 2024 tobozo

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
*/

package com.arduino.hiddenboardsmanager;

import processing.app.Editor;
import processing.app.tools.Tool;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import com.arduino.hiddenboardsmanager.UI;


@SuppressWarnings("serial")
final class JFrameArduino extends JFrame
{

  public JFrameArduino( String title )
  {
    JFrame frame = this;
    frame.setTitle(title);
    setSize(512, 640);
    //setResizable(false);
    setLocationRelativeTo(null);

    JLabel background = new JLabel(new ImageIcon(getClass().getResource("/bg.png")));
    background.setLayout(new BorderLayout());
    setContentPane(background);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        frame.setVisible(false);
        frame.remove(HiddenBoardsManager.contentPane);
        HiddenBoardsManager.contentPane = null;
      }
    });

    // [esc] key hides the app
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
          frame.setVisible(false);
          frame.remove(HiddenBoardsManager.contentPane);
          HiddenBoardsManager.contentPane = null;
        }
      }
    });
  }
}


@SuppressWarnings("serial")
public class HiddenBoardsManager extends JFrame implements Tool
{

  private JFrame frame;
  static UI contentPane;
  private Editor editor;

  public void init(Editor editor)
  {
    this.editor = editor;
  }

  public String getMenuTitle()
  {
    return "Manage Hidden Boards";
  }

  private void initGUI()
  {
    if( HiddenBoardsManager.contentPane == null ) HiddenBoardsManager.contentPane = new UI();
    if( frame == null ) frame = new JFrameArduino( "Arduino Hidden Boards Manager" );

    contentPane.repaint();
    contentPane.revalidate();


    frame.add(HiddenBoardsManager.contentPane);
    frame.setFocusable(true);
    frame.requestFocus();
    frame.toFront();
    EventQueue.invokeLater( () -> { frame.revalidate(); frame.repaint(); frame.setVisible(true); } );
  }

  public void run()
  {
    initGUI();
  }
}
