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

public class stateEditor extends JComponent implements ActionListener, ItemListener, ChangeListener{
	
// controlPanel variables
// Main controls
JButton[] statebutts = new JButton[5];
Checkbox[] statechecks = new Checkbox[5]; 

//draw tool selection/ configuration
JComboBox drawbox;  //draw tool selector
int dtsn = 0;// draw tool select number
int dtt = 0;//draw tool type
int dtv = 0;//draw tool value
JSlider drsl;//draw slider
JLabel dlbl;//draw value label


//fill tool selection/config
JComboBox fillbox; // fill tool selector
int ftsn = 0; // fill tool selected #
int ftt = 0; //fill tool type
int ftv = 0; //fill tool value
String[] toolstr = new String[]{"", "Age"};//for editing
String[] sedts = new String[]{"Binary State", "Age"};//for display
JSlider flsl;//fill slider
JLabel flbl;//fill value label

JSeparator mdborder;//sepaprate border/draw
JSeparator dfborder;//separate draw/fill

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
	statebutts[4] = new JButton("State Draw");
	
	statechecks[0] = new Checkbox("Interactive Mode");
	statechecks[1] = new Checkbox("Random");
	statechecks[2] = new Checkbox("Check");
	statechecks[3] = new Checkbox("Random");
	statechecks[4] = new Checkbox("Check");
	
	drawbox = new JComboBox(sedts);
	drsl  = new JSlider(0,512);
	fillbox = new JComboBox(sedts);
	flsl = new JSlider(0,512);
	
	//create separators, etc
	mdborder = new JSeparator(JSeparator.HORIZONTAL);
	dfborder = new JSeparator(JSeparator.HORIZONTAL);
	dlbl = new JLabel("***");
	flbl = new JLabel("***");
	
	//layout
	GroupLayout seLayout = new GroupLayout(this);
	seLayout.setAutoCreateGaps(false);
	seLayout.setAutoCreateContainerGaps(true);
	
	seLayout.setHorizontalGroup(
		seLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(statebutts[0])
			.addComponent(mdborder)
			.addComponent(statebutts[4])
			.addComponent(drawbox)
			.addComponent(dlbl)
			.addComponent(drsl)
			.addComponent(statechecks[0])
			.addGroup(seLayout.createSequentialGroup()
				.addComponent(statechecks[2])
				.addComponent(statechecks[1]))
			.addComponent(dfborder)
			.addComponent(statebutts[1])
			.addComponent(fillbox)
			.addComponent(flbl)
			.addComponent(flsl)
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
			.addComponent(mdborder)
			.addComponent(statebutts[4])
			.addComponent(drawbox)
			.addComponent(dlbl)
			.addComponent(drsl)
			.addComponent(statechecks[0])
			.addGroup(seLayout.createParallelGroup()
				.addComponent(statechecks[2])
				.addComponent(statechecks[1]))
			.addComponent(dfborder)
			.addComponent(statebutts[1])
			.addComponent(fillbox)
			.addComponent(flbl)
			.addComponent(flsl)
			.addGroup(seLayout.createParallelGroup()
				.addComponent(statechecks[4])
				.addComponent(statechecks[3]))
			.addGroup(seLayout.createParallelGroup()
				.addComponent(statebutts[2])
				.addComponent(statebutts[3]))
				);
				setLayout(seLayout);
				
				//set up controls
				for(int cc = 0; cc <= 4; cc++){
					statebutts[cc].addActionListener(this);statebutts[cc].setVisible(true);
					statechecks[cc].addItemListener(this);statechecks[cc].setVisible(true);}
					
					drawbox.addActionListener(this);
					drsl.addChangeListener(this);drsl.setEnabled(false);
					fillbox.addActionListener(this);
					flsl.addChangeListener(this);flsl.setEnabled(false);
					
				//set up separators, etc
				mdborder.setPreferredSize(new Dimension(150, 25));
				dfborder.setPreferredSize(new Dimension(150, 25));
	}

public void actionPerformed(ActionEvent e){
	int buttnum = 0; boolean buttflag = false;
	for(int bc = 0; bc <= statebutts.length-1; bc++){
		if(e.getSource() == statebutts[bc]){buttnum = bc; buttflag = true;}
	}
	if(buttflag){
	switch(buttnum){
		//state edit mode
		case 0: control = 0; fireucEvent(); break;
		//fill button
		case 1: control = 1;  sfoSet(); fireucEvent(); break;
		//clear
		case 2: control = 2; clearflag = true; sfoSet(); fireucEvent(); break;
		//invert
		case 3: control = 3; invflag = true; sfoSet(); fireucEvent(); break;
		// State Draw
		case 4: control = 9; fireucEvent(); break;
	}
	}
	
	if(e.getSource() == drawbox){
		for(int s = 0; s < sedts.length; s++){
			if(drawbox.getSelectedItem() == sedts[s]){dtsn = s;}}
			switch(dtsn){
				case 0: dtt = 0; drsl.setEnabled(false); dlbl.setText("***"); break;
				case 1: dtt = 2;  drsl.setEnabled(true); dlbl.setText(Integer.toString(drsl.getValue())); break;
			}
			control = 10; fireucEvent();
		}
	
	if(e.getSource() == fillbox){
		for(int s = 0; s < sedts.length; s++){
			if(fillbox.getSelectedItem() == sedts[s]){ftsn = s;}}
			switch(ftsn){
				case 0: ftt = 0; flsl.setEnabled(false); flbl.setText("***"); break;
				case 1: ftt = 2; flsl.setEnabled(true); flbl.setText(Integer.toString(flsl.getValue()));  break;
			}
			control = 11; fireucEvent();
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

public void stateChanged(ChangeEvent e){
	if(e.getSource() == drsl){dtv = drsl.getValue();dlbl.setText(Integer.toString(drsl.getValue())); control = 10; fireucEvent();}
	if(e.getSource() == flsl){ftv = flsl.getValue();flbl.setText(Integer.toString(flsl.getValue())); control = 11; fireucEvent();}
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
	/*Commands:
	 * 0 = state edit mode
	 * 1 = state fill
	 * 2 = clear state
	 * 3 = invert state
	 * 4 = interactive mode
	 * 5 = random draw
	 * 6 = check draw
	 * 7 = random fill
	 * 8 = check fill
	 * 9 = State Draw
	 * 10 = set draw tool
	 * 11 = set fill tool
	 */
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
	
	public int getTool(int a){
		if(a == 0){ return dtt;}
		if(a == 1){ return ftt;}
		return -1;}
		
	public int getToolVal(int a){
		if(a == 0){return dtv;}
		if(a == 1){return ftv;}
		return -1;}
		
	public String getTString(int a){
		if(a == 0){return toolstr[dtsn];}
		if(a == 1){return toolstr[ftsn];}
		return "ERROR";
	}
		
	}
