import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
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

public class cellEditor extends JComponent implements ActionListener, ItemListener{
// controlPanel variables
JButton[] mainbutts = new JButton[4];
Checkbox[] mainchecks = new Checkbox[4];
JComboBox dtPick;
JComboBox ftPick;
String[] tools = new String[]{"", "Dir"};//for editing
String[] toolsDisp = new String[]{"Cell", "Direction"};//for display
int dtval = 0;//drawing tool value
int dtool = 0;//drawing tool
int dtsel = 0;//drawing tool selected
int ftval = 0;//fill tool value
int ftool = 0;//fill tool
int ftsel = 0;// fill tool selected

// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;
boolean boolset = false;
int cdo = 0;
int cfo = 0;

public cellEditor(){
	//make controls
	mainbutts[0] = new JButton("Cell Editing Mode");
	mainbutts[1] = new JButton("Fill");
	mainbutts[2] = new JButton("Border");
	mainbutts[3] = new JButton("Cell Draw");
	
	mainchecks[0] = new Checkbox("Check");
	mainchecks[1] = new Checkbox("Random");
	mainchecks[2] = new Checkbox("Check");
	mainchecks[3] = new Checkbox("Random");
	
	dtPick = new JComboBox(toolsDisp);
	ftPick = new JComboBox(toolsDisp);
	
	// layout
	GroupLayout ceLayout = new GroupLayout(this);
	ceLayout.setAutoCreateGaps(false);
	ceLayout.setAutoCreateContainerGaps(true);
	
	ceLayout.setHorizontalGroup(
		ceLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(mainbutts[0])
			.addComponent(mainbutts[3])
			.addComponent(dtPick)
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainchecks[0])
				.addComponent(mainchecks[1]))
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainbutts[1])
				.addComponent(mainbutts[2]))
				.addComponent(ftPick)
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainchecks[2])
				.addComponent(mainchecks[3]))
				);
				
	ceLayout.setVerticalGroup(
		ceLayout.createSequentialGroup()
			.addComponent(mainbutts[0])
			.addComponent(mainbutts[3])
			.addComponent(dtPick)
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainchecks[0])
				.addComponent(mainchecks[1]))
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainbutts[1])
				.addComponent(mainbutts[2]))
				.addComponent(ftPick)
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainchecks[2])
				.addComponent(mainchecks[3]))
				);
				
		setLayout(ceLayout);
		
		
	// plug in controls
	for(int cont = 0; cont <= 3; cont++){
		mainbutts[cont].addActionListener(this);mainbutts[cont].setVisible(true);
		mainchecks[cont].addItemListener(this);mainchecks[cont].setVisible(true);}
		dtPick.addActionListener(this); dtPick.setVisible(true);
		ftPick.addActionListener(this); ftPick.setVisible(true);
	
	}

public void actionPerformed(ActionEvent e){
	int buttnum = 0; boolean buttflag = false;
	for(int a = 0; a <= mainbutts.length-1; a++){
		if(e.getSource() == mainbutts[a]){ buttnum = a; buttflag = true;}
		}
	if(buttflag){	
	switch(buttnum){
		// Cell editing mode
		case 0: command = 0; fireucEvent(); break;
		// cell fill
		case 1: command = 1; fireucEvent(); break;
		//set border
		case 2: command = 2; fireucEvent(); break;
		//Cell draw
		case 3: command = 7; fireucEvent(); break;
	}}
	if(e.getSource() == dtPick){
		
		for(int s = 0; s < tools.length; s++){
			if(dtPick.getSelectedItem() == toolsDisp[s]){dtsel = s;}
		}
		switch(dtsel){
			case 0: dtool = 0; break;
			case 1: dtool = 2; break;
		}
		command = 8; fireucEvent();
		}
	if(e.getSource() == ftPick){
		
		for(int s = 0; s < tools.length; s++){
			if(ftPick.getSelectedItem() == toolsDisp[s]){ftsel = s;}
		}
		switch(ftsel){
			case 0: ftool = 0; break;
			case 1: ftool = 2; break;
		}
		command = 9; fireucEvent();
		}
	}

public void itemStateChanged(ItemEvent e){
	int checknum = 0;
	for(int b = 0; b <= mainchecks.length-1; b++){
		if(e.getItemSelectable() == mainchecks[b]){ checknum = b; boolset = mainchecks[b].getState();}
	}
	switch(checknum){
		// check draw
		case 0: command = 3; cdoSet(); fireucEvent(); break;
		// rand draw
		case 1: command = 4; cdoSet(); fireucEvent(); break;
		// check fill
		case 2: command = 5; cfoSet(); fireucEvent(); break;
		// rand fill
		case 3: command = 6; cfoSet(); fireucEvent(); break;
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
	/*Commands
	 * 0 = cell editing mode
	 * 1 = cell fill
	 * 2 = set border
	 * 3 = toggle check draw
	 * 4 = togglr rand draw
	 * 5 = toggle check fill
	 * 6 = toggle rand fill
	 * 7 = Cell Drawing tool
	 * 8 = set Drawing tool
	 * 9 = set Fill Tool
	*/
public boolean getBoolSet(){
	return boolset;}
	
private void cdoSet(){
	cdo = 0;
	if(mainchecks[0].getState()){cdo += 1;}
	if(mainchecks[1].getState()){cdo += 2;}
}

public int cdoGet(){return cdo;}

private void cfoSet(){
	cfo = 0;
	if(mainchecks[2].getState()){cfo += 1;}
	if(mainchecks[3].getState()){cfo += 2;}
}

public int cfoGet(){ return cfo;}

public int getTool(int g){
	if(g == 2){ return dtool;}
	if(g == 3){ return ftool;}
	return -1;
}

public String getTstr(int h){
	if(h == 2){ return tools[dtsel];}
	if(h == 3){ return tools[ftsel];}
	return "Error";
}

public int getTval(int h){
	if(h == 2){ return dtval;}
	if(h == 3){ return ftval;}
	return -1;
}

}
