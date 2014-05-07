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

public class brushControl extends JComponent implements ActionListener, ItemListener{
// controlPanel variables
//brush selection
JComboBox brushPicker;
String[] brushes = new String[]{"1x1", "2x2", "3x3", "Glider", "R- pentomino","Extended vonNeumann", "vonNeumann"};
int bruush = 1;
//direction
JLabel bdlabel;
JRadioButton[] orients = new JRadioButton[8];
int brushdir = 0;
// options
Checkbox[] option;
String[] optstr = new String[]{"Reflect"};
String opname; 
boolean opval;
//sample brush
brush gonzo;

// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;


public brushControl(){
	//create controls
	brushPicker = new JComboBox(brushes);
	// directions
	for(int i = 0; i < orients.length; i++){
		orients[i] = new JRadioButton(Integer.toString(i));
	}
	bdlabel = new JLabel("Orientation : ");
	//options
	option = new Checkbox[1];
	option[0] = new Checkbox("Reflect");
	opval = false;
	//create gonzo
	gonzo = new onebrush();
	
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
				.addComponent(orients[7])
				.addComponent(option[0]))
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
				.addComponent(orients[7])
				.addComponent(option[0]))
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
		orients[i].setVisible(true);
		orients[i].setEnabled(false);
	}
	orients[0].setSelected(true);
	bdlabel.setVisible(true);
	// options
	for(int i = 0; i < option.length; i++){
		option[i].addItemListener(this);
		option[i].setVisible(true);
		option[i].setEnabled(false);
	}
	}
	
public void init(){
	for(int i = 0; i < orients.length; i++){	
		orients[i].setVisible(false);
		orients[i].setEnabled(false);
	}
	bdlabel.setVisible(false);
	// options
	for(int i = 0; i < option.length; i++){
		option[i].setVisible(false);
		option[i].setEnabled(false);
	}
}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == brushPicker){
	for(int ind = 0; ind < brushes.length; ind++){
		//Set Editing Brush type
		if(brushPicker.getSelectedItem() == brushes[ind]){command = 401; bruush = ind+1; setGonzo(bruush); toggleControls(); break;}
	}
	 
	}
	
	for(int indy = 0; indy< orients.length; indy++){
		//Set Brush Orientation
		if(e.getSource() == orients[indy]){
			command = 402;
			brushdir = indy;
			 break;
		}
	}
	fireucEvent();
}
	public void itemStateChanged(ItemEvent e){
		int opnum = 0;
		for(int i = 0; i < option.length; i++){
			//Set Brush Options
			if(e.getSource() == option[i]){ opnum = i; opname = optstr[i];opval = option[i].getState();}
		}
		command = 403;
		fireucEvent();
	}

	private void setGonzo(int a){
		switch(a){
			case 1: gonzo = new onebrush(); break;
			case 2: gonzo = new twobrush(); break;
			case 3: gonzo = new threebrush(); break;
			case 4: gonzo = new gliderbrush(); break;
			case 5: gonzo = new rpentbrush(); break;
			case 6: gonzo = new evbrush(); break;
			case 7: gonzo = new vonNeumannbrush(); break;
		}
	}
	
	private void toggleControls(){
		String[] controls = new String[]{"Dir", "Orient", "Reflect"};
		boolean[] constate = new boolean[controls.length];
		int connum = 0;
		for(int abc = 0; abc < controls.length; abc++){
			constate[abc]  = gonzo.getControls(controls[abc]);} 
			if(constate[0]){connum += 1;} if(constate[1]){connum += 2;} if(constate[2]){ connum += 4;}
			switch(connum){
				case 0: bdlabel.setVisible(false);
						for(int def = 0; def < orients.length; def++)
						{orients[def].setVisible(false); orients[def].setEnabled(false);} 
						option[0].setVisible(false); option[0].setEnabled(false);
						break; 
				case 1: bdlabel.setVisible(true);
						for(int def = 0; def < orients.length; def++)
						{orients[def].setVisible(true); orients[def].setEnabled(true);} 
						orients[0].setSelected(true); brushdir = 0;
						option[0].setVisible(false); option[0].setEnabled(false);
						break; 
						
				case 2: bdlabel.setVisible(true); boolean sig;
						for(int def = 0; def < orients.length; def++)
						{if(def < 4){sig = true;}else{sig = false;}
						orients[def].setVisible(sig); orients[def].setEnabled(sig);} 
						orients[0].setSelected(true); brushdir = 0;
						option[0].setVisible(false); option[0].setEnabled(false);
						break; 
						
				case 3: break;
				
				case 4: option[0].setVisible(true); option[0].setEnabled(true); break;
				
				case 5:  bdlabel.setVisible(true);
						for(int def = 0; def < orients.length; def++)
						{orients[def].setVisible(true); orients[def].setEnabled(true);} 
						orients[0].setSelected(true); brushdir = 0;
						option[0].setVisible(true); option[0].setEnabled(true);
						break; 
						
				case 6: bdlabel.setVisible(true); boolean sigB;
						for(int def = 0; def < orients.length; def++)
						{if(def < 4){sigB = true;}else{sigB = false;}
						orients[def].setVisible(sigB); orients[def].setEnabled(sigB);} 
						orients[0].setSelected(true); brushdir = 0;
						option[0].setVisible(true); option[0].setEnabled(true);
						break; 
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
	return bruush;}
	
public int getBrushDir(){
	return brushdir;}
	
public String getOPNAM(){
	return opname;}
	
public boolean getOPVAL(){
	return opval;}
	
	
}
