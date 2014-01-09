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

public class brushControl extends JComponent implements ActionListener{
// controlPanel variables
JComboBox brushPicker;
String[] brushes = new String[]{"1x1", "2x2", "3x3", "Glider"};
int brush = 1;
// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;


public brushControl(){
	brushPicker = new JComboBox(brushes);
	
	setLayout(new FlowLayout());
	add (brushPicker);
	brushPicker.addActionListener(this);
	}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == brushPicker){
	for(int ind = 0; ind < brushes.length; ind++){
		if(brushPicker.getSelectedItem() == brushes[ind]){brush = ind+1; break;}
	}
	command = 1; fireucEvent();
	}
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
public int getBrush(){
	return brush;}
}
