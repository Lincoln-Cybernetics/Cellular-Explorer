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
//brush selection
JComboBox brushPicker;
String[] brushes = new String[]{"1x1", "2x2", "3x3", "Glider"};
int brush = 1;
//orientation
JLabel bdlabel;
JRadioButton[] orients = new JRadioButton[8];
int brushdir = 0;
//sample brush
brush gonzo;

// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;


public brushControl(){
	//create controls
	brushPicker = new JComboBox(brushes);
	for(int i = 0; i < orients.length; i++){
		orients[i] = new JRadioButton(Integer.toString(i));
	}
	bdlabel = new JLabel("Orientation : ");
	//create gonzo
	gonzo = new brush();
	
	//layout
	GroupLayout brushout = new GroupLayout(this);
	brushout.setAutoCreateGaps(false);
	brushout.setAutoCreateContainerGaps(true);
	
	brushout.setHorizontalGroup(
		brushout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(brushPicker)
			.addGroup(brushout.createSequentialGroup()
				.addComponent(bdlabel)
				.addComponent(orients[0])
				.addComponent(orients[1])
				.addComponent(orients[2])
				.addComponent(orients[3])
				.addComponent(orients[4])
				.addComponent(orients[5])
				.addComponent(orients[6])
				.addComponent(orients[7]))
				);
				
	brushout.setVerticalGroup(
		brushout.createSequentialGroup()
		.addComponent(brushPicker)
		.addGroup(brushout.createParallelGroup()
			.addComponent(bdlabel)
				.addComponent(orients[0])
				.addComponent(orients[1])
				.addComponent(orients[2])
				.addComponent(orients[3])
				.addComponent(orients[4])
				.addComponent(orients[5])
				.addComponent(orients[6])
				.addComponent(orients[7]))
		);
		
	setLayout(brushout);
	
	//init components
	//brush picker
	brushPicker.addActionListener(this);
	brushPicker.setMaximumSize(new Dimension(100,30));
	
	// direction controls
	ButtonGroup direcpick = new ButtonGroup();
	for(int i = 0; i < orients.length; i++){
		direcpick.add(orients[i]);
		orients[i].addActionListener(this);
		orients[i].setVisible(false);
		orients[i].setEnabled(false);
	}
	orients[0].setSelected(true);
	bdlabel.setVisible(false);
	}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == brushPicker){
	for(int ind = 0; ind < brushes.length; ind++){
		if(brushPicker.getSelectedItem() == brushes[ind]){command = 1; brush = ind+1; setGonzo(brush); toggleControls(); break;}
	}
	 
	}
	
	for(int indy = 0; indy< orients.length; indy++){
		if(e.getSource() == orients[indy]){
			command = 2;
			brushdir = indy;
			 break;
		}
	}
	fireucEvent();
}

	private void setGonzo(int a){
		switch(a){
			case 1: gonzo = new brush(); break;
			case 2: gonzo = new twobrush(); break;
			case 3: gonzo = new threebrush(); break;
			case 4: gonzo = new gliderbrush(); break;
		}
	}
	
	private void toggleControls(){
		String[] controls = new String[]{"Dir"};
		for(int abc = 0; abc < controls.length; abc++){
			boolean vis = gonzo.getControls(controls[abc]); 
			switch(abc){
				case 0: bdlabel.setVisible(vis);
						for(int def = 0; def < orients.length; def++)
						{orients[def].setVisible(vis); orients[def].setEnabled(vis);} 
						break; 
			}
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
	
public int getBrushDir(){
	return brushdir;}
}
