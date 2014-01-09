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
JButton[] mainbutts = new JButton[3];
Checkbox[] mainchecks = new Checkbox[4];
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
	
	mainchecks[0] = new Checkbox("Check");
	mainchecks[1] = new Checkbox("Random");
	mainchecks[2] = new Checkbox("Check");
	mainchecks[3] = new Checkbox("Random");
	
	// layout
	GroupLayout ceLayout = new GroupLayout(this);
	ceLayout.setAutoCreateGaps(false);
	ceLayout.setAutoCreateContainerGaps(true);
	
	ceLayout.setHorizontalGroup(
		ceLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(mainbutts[0])
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainchecks[0])
				.addComponent(mainchecks[1]))
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainbutts[1])
				.addComponent(mainbutts[2]))
			.addGroup(ceLayout.createSequentialGroup()
				.addComponent(mainchecks[2])
				.addComponent(mainchecks[3]))
				);
				
	ceLayout.setVerticalGroup(
		ceLayout.createSequentialGroup()
			.addComponent(mainbutts[0])
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainchecks[0])
				.addComponent(mainchecks[1]))
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainbutts[1])
				.addComponent(mainbutts[2]))
			.addGroup(ceLayout.createParallelGroup()
				.addComponent(mainchecks[2])
				.addComponent(mainchecks[3]))
				);
				
		setLayout(ceLayout);
		
		
	// plug in controls
	for(int cont = 0; cont <= 3; cont++){
		if(cont <= 2){mainbutts[cont].addActionListener(this);mainbutts[cont].setVisible(true);}
		mainchecks[cont].addItemListener(this);mainchecks[cont].setVisible(true);}
		
	
	}

public void actionPerformed(ActionEvent e){
	int buttnum = 0;
	for(int a = 0; a <= mainbutts.length-1; a++){
		if(e.getSource() == mainbutts[a]){ buttnum = a;}
		}
		
	switch(buttnum){
		case 0: command = 0; fireucEvent(); break;
		case 1: command = 1; fireucEvent(); break;
		case 2: command = 2; fireucEvent(); break;
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

}
