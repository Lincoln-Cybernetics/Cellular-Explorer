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

public class stateEditor extends JComponent implements ActionListener, ItemListener{
	
// controlPanel variables
// controls
JButton[] statebutts = new JButton[4];
Checkbox[] statechecks = new Checkbox[5]; 

// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int control = 0;
boolean bset = false;
int sdo = 0;
int sfo = 0;
boolean clearflag = false;
boolean invflag = false;

public stateEditor(){
	// create the controls
	statebutts[0] = new JButton("State Editing Mode");
	statebutts[1] = new JButton("Fill");
	statebutts[2] = new JButton("Clear");
	statebutts[3] = new JButton("Invert");
	
	statechecks[0] = new Checkbox("Interactive Mode");
	statechecks[1] = new Checkbox("Random");
	statechecks[2] = new Checkbox("Check");
	statechecks[3] = new Checkbox("Random");
	statechecks[4] = new Checkbox("Check");
	
	//layout
	GroupLayout seLayout = new GroupLayout(this);
	seLayout.setAutoCreateGaps(false);
	seLayout.setAutoCreateContainerGaps(true);
	
	seLayout.setHorizontalGroup(
		seLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(statebutts[0])
			.addComponent(statechecks[0])
			.addGroup(seLayout.createSequentialGroup()
				.addComponent(statechecks[2])
				.addComponent(statechecks[1]))
			.addComponent(statebutts[1])
			.addGroup(seLayout.createSequentialGroup()
				.addComponent(statechecks[4])
				.addComponent(statechecks[3]))
			.addGroup(seLayout.createSequentialGroup()
				.addComponent(statebutts[2])
				.addComponent(statebutts[3]))
				);
				
	seLayout.setVerticalGroup(
		seLayout.createSequentialGroup()
			.addComponent(statebutts[0])
			.addComponent(statechecks[0])
			.addGroup(seLayout.createParallelGroup()
				.addComponent(statechecks[2])
				.addComponent(statechecks[1]))
			.addComponent(statebutts[1])
			.addGroup(seLayout.createParallelGroup()
				.addComponent(statechecks[4])
				.addComponent(statechecks[3]))
			.addGroup(seLayout.createParallelGroup()
				.addComponent(statebutts[2])
				.addComponent(statebutts[3]))
				);
				setLayout(seLayout);
				
				for(int cc = 0; cc <= 4; cc++){
					if(cc <= 3){statebutts[cc].addActionListener(this);statebutts[cc].setVisible(true);}
					statechecks[cc].addItemListener(this);statechecks[cc].setVisible(true);}
	}

public void actionPerformed(ActionEvent e){
	int buttnum = 0;
	for(int bc = 0; bc <= statebutts.length-1; bc++){
		if(e.getSource() == statebutts[bc]){buttnum = bc;}
	}
	switch(buttnum){
		//state edit mode
		case 0: control = 0; fireucEvent(); break;
		//fill button
		case 1: control = 1;  sfoSet(); fireucEvent(); break;
		//clear
		case 2: control = 2; clearflag = true; sfoSet(); fireucEvent(); break;
		//invert
		case 3: control = 3; invflag = true; sfoSet(); fireucEvent(); break;
	}
	}

public void itemStateChanged(ItemEvent e){
	int checknum = 0;
	for(int cbc = 0; cbc <= statechecks.length-1; cbc++){
		if(e.getItemSelectable() == statechecks[cbc]){checknum = cbc; bset = statechecks[cbc].getState();}
	}
	switch(checknum){
		//interactive
		case 0: control = 4; fireucEvent(); break;
		//draw random
		case 1: control = 5; sdoSet(); fireucEvent(); break;
		//draw check
		case 2: control = 6; sdoSet(); fireucEvent(); break;
		// fill random
		case 3: control = 7; sfoSet(); fireucEvent(); break;
		// fill check
		case 4: control = 8; sfoSet(); fireucEvent(); break;
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
	cmd.setCommand(control);
	Iterator i = _audience.iterator();
	while(i.hasNext()){
		((ucListener) i.next()).handleControl(cmd);}
	}
	// returns boolean settings
	public boolean getBSET(){
		return bset;}
		
	private void sdoSet(){
		sdo = 0;
		if (statechecks[2].getState()){sdo += 1;}
		if (statechecks[1].getState()){sdo += 2;}
	}
	
	private void sfoSet(){
		sfo = 0;
		if (statechecks[4].getState()){sfo += 1;}
		if (statechecks[3].getState()){sfo += 2;}
		if(clearflag){ sfo = 4; clearflag = false;}
		if(invflag){sfo = 5; invflag = false;}
	}
	
	public int getSDO(){ return sdo;}
	
	public int getSFO(){ return sfo;}
		
	}
