package com.arduino.esp32boardstool;

import javax.swing.*;
import java.awt.Font;


@SuppressWarnings("serial")
class AboutPanel extends JEditorPane
{

  public AboutPanel()
  {
    createPanel();
  }

  private void createPanel()
  {

    setFont( UI.defaultFont.deriveFont(Font.PLAIN, 12) );

    final String fontFace = "<font face=sans-serif>";
    final String boxpadding = "padding-top: 0px;padding-right: 10px;padding-bottom: 10px;padding-left: 10px;";
    final String titleSpanned = "<span style=\"background-color: #00838b\">&nbsp;Arduino&nbsp;</span>"
        + "<span style=\"color:#00838b; background-color: #dddddd\">&nbsp;HiddenBoardsManager&nbsp;</span>"
        + "<span style=\"background-color: #aaaaaa; color:#000000\">&nbsp;v1.0&nbsp;</span>";
    final String title = "<h2 align=center style=\"color: #ffffff;\">" + titleSpanned + "</h2>";
    final String description =
          "<p><b>Arduino HiddenBoardsManager</b> is a tool to manage hidden<br>"
        + "boards and declutter the <i>Tools/Board</i> scrollable menu.</p>"
        + "<p>While the clutterness is solved in <i>Arduino IDE 2.x.x</i> with a<br>"
        + "very handy search box on the top of the list, older IDE <br>"
        + "versions (<i>1.8.x</i>) are still cursed by a couple of UX problems:"
        + "<ul>"
        + " <li>Scrolling speed in the menu is sluggish</li>"
        + " <li>Boards aren't sorted alphabetically</li>"
        + "</ul>"
        + "This tool doesn't pretend to fix those problems, it acts more<br>"
        + "like a painkiller by providing a way to shorten the boards list.</p>"
        + "<p>Health issues addressed by the <b>HiddenBoardsManager</b>:"
        + "<ul>"
        + "  <li>Carpal tunnel syndrome</li>"
        + "  <li>UX anxiety</li>"
        + "  <li>UI megalophobia</li>"
        + "</ul>"
        + "<b>Use without moderation!</b></p>"
    ;
    final String projectlink = "<p><b>Source:</b><br>https://github.com/tobozo/Arduino-Hidden-Boards-Manager</p>";
    final String copyright = "<p><b>Copyright (c) 2024 @tobozo</b><br>https://github.com/tobozo</p>";
    final String message = "<html>" + fontFace + title + "<div style=\"" + boxpadding + "\">" + description + projectlink
        + copyright + "</div></html>";

    // fixes weird font problem with the standalone version
    putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    setContentType("text/html");
    setText(message);
  }

}
