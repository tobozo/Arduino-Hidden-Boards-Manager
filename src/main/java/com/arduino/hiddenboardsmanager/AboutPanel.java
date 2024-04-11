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
