import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Checkbox.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/*Cellular Explorer Prototype proof of concept
 * Copyright(C) 02013 Matt Ahlschwede
 *  This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

public class selectionControl extends JComponent implements ActionListener, ItemListener{
// controlPanel variables
JButton[] butts = new JButton[5];
Checkbox hide;

// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;
boolean boolo = false;


public selectionControl(){
	setLayout(new FlowLayout());
		butts[0] = new JButton("Select by Brush");
		butts[1] = new JButton("Select Rectangle");
		butts[2] = new JButton("Select All");
		butts[3] = new JButton("Invert Selection");
		butts[4] = new JButton("De-select");
	for( JButton item: butts){
		item.addActionListener(this);
		add(item);
		item.setVisible(true);
		}
		
		hide = new Checkbox("Hide Selection");
		hide.addItemListener(this);
		add(hide);
		hide.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		int buttnum = 0;
		for(int a = 0; a < butts.length; a++){
			if(e.getSource() == butts[a]){buttnum  = a;}
		}
		switch(buttnum){
			case 0:command = 0; fireucEvent(); break;
			case 1:command = 1; fireucEvent(); break;
			case 2:command = 2; fireucEvent(); break;
			case 3:command = 3; fireucEvent(); break;
			case 4:command = 4; fireucEvent(); break;
		}
		}
	
	public void itemStateChanged(ItemEvent e){
		if(e.getSource() == hide){command = 5; boolo = hide.getState(); fireucEvent(); }
		}

//event generation
//adds listeners for command events
public synchronized void adducListener(ucListener listener){
	_audience.add(listener);}
	
//removes listeners for command events	
public synchronized void removeucListener(ucListener listener){
	_audience.remove(listener);}
	
//notifies application when a command is sent	
private synchronized void fireucEvent(){
	ucEvent cmd = new ucEvent(this);
	cmd.setCommand(command);
	Iterator i = _audience.iterator();
	while(i.hasNext()){
		((ucListener) i.next()).handleControl(cmd);}
	}
	
	public boolean getBoolo(){
		return boolo;}
}
