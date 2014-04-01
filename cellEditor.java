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

public class cellEditor extends JComponent implements ActionListener, ItemListener, ChangeListener{
// controlPanel variables
JButton[] mainbutts = new JButton[4];
Checkbox[] mainchecks = new Checkbox[4];
JComboBox dtPick;
JComboBox ftPick;
JSlider dslid;
JSlider fslid;
JLabel drawlabel;
JLabel filllabel;
JSeparator dfline;
JSeparator mdline;


String[] tools = new String[]{"", "Dir", "Mat", "Ages", "Fades", "Fade"};//for editing
String[] toolsDisp = new String[]{"Cell", "Direction", "Maturity", "Ages", "Fade Rule", "Fade"};//for display
String[] toolsDispB = toolsDisp;
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
	ftPick = new JComboBox(toolsDispB);
	
	dslid = new JSlider(1,512);
	fslid = new JSlider(1,512);
	
	//make labels, etc
	drawlabel = new JLabel("***");
	filllabel = new JLabel("***");
	dfline = new JSeparator(JSeparator.HORIZONTAL);
	mdline = new JSeparator(JSeparator.HORIZONTAL);
	
	// layout
	GroupLayout ceLayout = new GroupLayout(this);
	ceLayout.setAutoCreateGaps(false);
	ceLayout.setAutoCreateContainerGaps(true);
	
	ceLayout.setHorizontalGroup(
		ceLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(mainbutts[0])
			.addComponent(mdline)
			.addComponent(mainbutts[3])
			.addComponent(dtPick)
			.addComponent(drawlabel)
			.addComponent(dslid)
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainchecks[0])
				.addComponent(mainchecks[1]))
			.addComponent(dfline)
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainbutts[1])
				.addComponent(mainbutts[2]))
				.addComponent(ftPick)
				.addComponent(filllabel)
				.addComponent(fslid)
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainchecks[2])
				.addComponent(mainchecks[3]))
				);
				
	ceLayout.setVerticalGroup(
		ceLayout.createSequentialGroup()
			.addComponent(mainbutts[0])
			.addComponent(mdline)
			.addComponent(mainbutts[3])
			.addComponent(dtPick)
			.addComponent(drawlabel)
			.addComponent(dslid)
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainchecks[0])
				.addComponent(mainchecks[1]))
			.addComponent(dfline)
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainbutts[1])
				.addComponent(mainbutts[2]))
				.addComponent(ftPick)
				.addComponent(filllabel)
				.addComponent(fslid)
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
		dslid.addChangeListener(this); dslid.setVisible(true);dslid.setEnabled(false);
		dslid.setMajorTickSpacing(16);dslid.setPaintTicks(true);
		ftPick.addActionListener(this); ftPick.setVisible(true);
		fslid.addChangeListener(this); fslid.setVisible(true);fslid.setEnabled(false);
		fslid.setMajorTickSpacing(16);fslid.setPaintTicks(true);
	dfline.setPreferredSize(new Dimension(150, 50));
	mdline.setPreferredSize(new Dimension(150, 50));
	}



public void actionPerformed(ActionEvent e){
	int buttnum = 0; boolean buttflag = false;
	for(int a = 0; a <= mainbutts.length-1; a++){
		if(e.getSource() == mainbutts[a]){ buttnum = a; buttflag = true;}
		}
	if(buttflag){	
	switch(buttnum){
		// Cell editing mode
		case 0: command = 200; fireucEvent(); break;
		// cell fill
		case 1: command = 201; fireucEvent(); break;
		//set border
		case 2: command = 202; fireucEvent(); break;
		//Cell draw
		case 3: command = 207; fireucEvent(); break;
	}}
	if(e.getSource() == dtPick){
		//drawing tools
		for(int s = 0; s < tools.length; s++){
			if(dtPick.getSelectedItem() == toolsDisp[s]){dtsel = s;}
		}
		switch(dtsel){
			//Cell
			case 0: dtool = 0; dslid.setEnabled(false); drawlabel.setText("***"); break;
			//Direction
			case 1: dtool = 2; dslid.setEnabled(true);dslid.setMinimum(0); 
			dslid.setMaximum(7);dslid.setValue(0); dslid.setMajorTickSpacing(1); dslid.setPaintTicks(true);break;
			//Maturity
			case 2: dtool = 2; dslid.setEnabled(true); dslid.setMinimum(1);
			dslid.setMaximum(512); dslid.setValue(1); dslid.setMajorTickSpacing(16); dslid.setPaintTicks(true); break;
			// Ages
			case 3: dtool = 1; dslid.setEnabled(true); dslid.setMinimum(0); dslid.setMaximum(1); dslid.setValue(1); dslid.setMajorTickSpacing(1);
			dslid.setPaintTicks(true); break;
			// Fades (Fade Rule)
			case 4: dtool = 1; dslid.setEnabled(true); dslid.setMinimum(0); dslid.setMaximum(1); dslid.setValue(1); dslid.setMajorTickSpacing(1);
			dslid.setPaintTicks(true); break; 
			// Fade
			case 5: dtool = 2; dslid.setEnabled(true); dslid.setMinimum(1); dslid.setMaximum(1024); dslid.setMajorTickSpacing(64); 
			dslid.setPaintTicks(true); break;
		}
		command = 208; fireucEvent();
		}
	if(e.getSource() == ftPick){
		// Fill Tools
		for(int s = 0; s < tools.length; s++){
			if(ftPick.getSelectedItem() == toolsDispB[s]){ftsel = s;}
		}
		switch(ftsel){
			//Cell
			case 0: ftool = 0;fslid.setEnabled(false);filllabel.setText("***"); break;
			//Direction
			case 1: ftool = 2;  fslid.setEnabled(true);fslid.setMinimum(0); 
			fslid.setMaximum(7);fslid.setValue(0); fslid.setMajorTickSpacing(1);fslid.setPaintTicks(true); break;
			// Maturity
			case 2: ftool = 2; fslid.setEnabled(true); fslid.setMinimum(1); 
			fslid.setMaximum(512); fslid.setValue(1); fslid.setMajorTickSpacing(4); fslid.setPaintTicks(true); break;
			// Ages
			case 3: ftool = 1; fslid.setEnabled(true); fslid.setMinimum(0); 
			fslid.setMaximum(1); fslid.setValue(1); fslid.setMajorTickSpacing(1); fslid.setPaintTicks(true); break;
			// Fades (Fade Rule)
			case 4: ftool = 1; fslid.setEnabled(true); fslid.setMinimum(0); 
			fslid.setMaximum(1); fslid.setValue(1); fslid.setMajorTickSpacing(1); fslid.setPaintTicks(true); break;
			//Fade
			case 5: ftool = 2; fslid.setEnabled(true); fslid.setMinimum(1); fslid.setMaximum(1024); fslid.setMajorTickSpacing(64); 
			fslid.setPaintTicks(true); break;
		}
		command = 209; fireucEvent();
		}
	}
	
public void stateChanged(ChangeEvent e){
	if(e.getSource() == dslid){
	switch(dtsel){
		case 0: break;
		case 1: switch(dslid.getValue()){
						case 0: drawlabel.setText("Up"); break;
						case 1: drawlabel.setText("Upper-Right"); break;
						case 2: drawlabel.setText("Right"); break;
						case 3: drawlabel.setText("Lower-Right"); break;
						case 4: drawlabel.setText("Down"); break;
						case 5: drawlabel.setText("Lower-Left"); break;
						case 6: drawlabel.setText("Left"); break;
						case 7: drawlabel.setText("Upper-Left"); break;
					} break;
		case 2: drawlabel.setText(Integer.toString(dslid.getValue())); break;
		case 3: if(dslid.getValue() == 1){drawlabel.setText("True");}else{drawlabel.setText("False");}break;
		case 4: if(dslid.getValue() == 1){drawlabel.setText("True");}else{drawlabel.setText("False");}break;
		case 5: drawlabel.setText(Integer.toString(dslid.getValue())); break;			
				}
		dtval = dslid.getValue(); command = 8; fireucEvent();				
	}
	if(e.getSource() == fslid){
	switch(ftsel){
		case 0: break;
		case 1: switch(fslid.getValue()){
						case 0: filllabel.setText("Up"); break;
						case 1: filllabel.setText("Upper-Right"); break;
						case 2: filllabel.setText("Right"); break;
						case 3: filllabel.setText("Lower-Right"); break;
						case 4: filllabel.setText("Down"); break;
						case 5: filllabel.setText("Lower-Left"); break;
						case 6: filllabel.setText("Left"); break;
						case 7: filllabel.setText("Upper-Left"); break;
					} break;
		case 2: filllabel.setText(Integer.toString(fslid.getValue())); break;
		case 3: if(fslid.getValue() == 1){filllabel.setText("True");} else{filllabel.setText("False");}break;
		case 4: if(fslid.getValue() == 1){filllabel.setText("True");} else{filllabel.setText("False");}break;
		case 5: filllabel.setText(Integer.toString(fslid.getValue())); break;
				}
		ftval = fslid.getValue(); command = 9; fireucEvent();
	}
}

public void itemStateChanged(ItemEvent e){
	int checknum = 0;
	for(int b = 0; b <= mainchecks.length-1; b++){
		if(e.getItemSelectable() == mainchecks[b]){ checknum = b; boolset = mainchecks[b].getState();}
	}
	switch(checknum){
		// check draw
		case 0: command = 203; cdoSet(); fireucEvent(); break;
		// rand draw
		case 1: command = 204; cdoSet(); fireucEvent(); break;
		// check fill
		case 2: command = 205; cfoSet(); fireucEvent(); break;
		// rand fill
		case 3: command = 206; cfoSet(); fireucEvent(); break;
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
