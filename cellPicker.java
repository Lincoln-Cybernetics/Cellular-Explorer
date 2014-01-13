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

public class cellPicker extends JComponent implements ActionListener, ItemListener{
	
cellOptionHandler gate;
// controlPanel variables
JComboBox cellpick;
String[] Cells = new String[]{"Cell", "Wolfram", "MBOT", "Random"};

JComboBox  MBOTPick;
String[] MBOTCells = new String[]{"Custom", "2x2", "3/4 Life", "Amoeba", "Assimilation", "Coagulations", "Coral", "Day and Night", "Diamoeba", "Dot Life",
"Dry Life", "Fredkin", "Gnarl", "High Life", "Life", "Life without Death", "Live Free or Die", "Long Life", "Maze", "Mazectric",
"Move", "Pseudo-life", "Replicator", "Seeds", "Serviettes", "Stains", "Vote", "Vote 4/5", "Walled Cities"};
String MBOTtype = "Custom";
Checkbox[] opts = new Checkbox[28];
JLabel jack;
JLabel jill;
JLabel[] wlab = new JLabel[8];
JRadioButton[] wdirs = new JRadioButton[4];
JLabel orlabel;


// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;
/* ct = cell type:
 * 0 = cell
 * 1 = wolfram
 * 2 = MBOT
 * 3 = randCell
 */
int ct = 0;

public cellPicker(){
	cellpick = new JComboBox(Cells);
	MBOTPick = new JComboBox(MBOTCells);
	jack = new JLabel("Born");
	jill = new JLabel("Survives");
	//labels for wolfram rules
	wlab[0] = new JLabel("111");
	wlab[1] = new JLabel("110");
	wlab[2] = new JLabel("101");
	wlab[3] = new JLabel("100");
	wlab[4] = new JLabel("011");
	wlab[5] = new JLabel("010");
	wlab[6] = new JLabel("001");
	wlab[7] = new JLabel("000");
	//wolfram directions
	orlabel = new JLabel("Direction:");
	wdirs[0] = new JRadioButton("|"); wdirs[1] = new JRadioButton("/");
	wdirs[2] = new JRadioButton("--"); wdirs[3] = new JRadioButton("\\");
	ButtonGroup orients = new ButtonGroup(); orients.add(wdirs[0]); orients.add(wdirs[1]);
	orients.add(wdirs[2]); orients.add(wdirs[3]);wdirs[0].setSelected(true);
	
	
	opts[0] = new Checkbox("Ages"); 
	opts[1] = new Checkbox("Fades");
	// born
	opts[2] = new Checkbox("0"); opts[3] = new Checkbox("1"); opts[4] = new Checkbox("2"); 
	opts[5] = new Checkbox("3"); opts[6] = new Checkbox("4"); opts[7] = new Checkbox("5"); 
	opts[8] = new Checkbox("6"); opts[9] = new Checkbox("7"); opts[10] = new Checkbox("8"); 
	// survives
	opts[11] = new Checkbox("0"); opts[12] = new Checkbox("1"); opts[13] = new Checkbox("2"); 
	opts[14] = new Checkbox("3"); opts[15] = new Checkbox("4"); opts[16] = new Checkbox("5"); 
	opts[17] = new Checkbox("6"); opts[18] = new Checkbox("7"); opts[19] = new Checkbox("8"); 
	//Wolfram rules
	opts[20] = new Checkbox(); opts[21] = new Checkbox(); opts[22] = new Checkbox(); opts[23] = new Checkbox();
	opts[24] = new Checkbox(); opts[25] = new Checkbox(); opts[26] = new Checkbox(); opts[27] = new Checkbox();
	
	GroupLayout cpLayout = new GroupLayout(this);
	cpLayout.setAutoCreateGaps(false);
	cpLayout.setAutoCreateContainerGaps(false);
	
	cpLayout.setHorizontalGroup(
		cpLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(cellpick)
			.addComponent(MBOTPick)
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(opts[0])
				.addComponent(opts[1]))
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(jack)
				.addComponent(opts[2])
				.addComponent(opts[3])
				.addComponent(opts[4])
				.addComponent(opts[5])
				.addComponent(opts[6])
				.addComponent(opts[7])
				.addComponent(opts[8])
				.addComponent(opts[9])
				.addComponent(opts[10]))
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(jill)
				.addComponent(opts[11])
				.addComponent(opts[12])
				.addComponent(opts[13])
				.addComponent(opts[14])
				.addComponent(opts[15])
				.addComponent(opts[16])
				.addComponent(opts[17])
				.addComponent(opts[18])
				.addComponent(opts[19]))
			.addGroup(cpLayout.createSequentialGroup()
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[0])
					.addComponent(opts[20]))
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[1])
					.addComponent(opts[21]))
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[2])
					.addComponent(opts[22]))
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[3])
					.addComponent(opts[23]))
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[4])
					.addComponent(opts[24]))
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[5])
					.addComponent(opts[25]))
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[6])
					.addComponent(opts[26]))
				.addGroup(cpLayout.createParallelGroup()
					.addComponent(wlab[7])
					.addComponent(opts[27]))
					)
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(orlabel)
				.addComponent(wdirs[0])
				.addComponent(wdirs[1])
				.addComponent(wdirs[2])
				.addComponent(wdirs[3]))
				);
				
	cpLayout.setVerticalGroup(
		cpLayout.createSequentialGroup()
			.addComponent(cellpick)
			.addComponent(MBOTPick)
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(opts[0])
				.addComponent(opts[1]))
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(jack)
				.addComponent(opts[2])
				.addComponent(opts[3])
				.addComponent(opts[4])
				.addComponent(opts[5])
				.addComponent(opts[6])
				.addComponent(opts[7])
				.addComponent(opts[8])
				.addComponent(opts[9])
				.addComponent(opts[10]))
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(jill)
				.addComponent(opts[11])
				.addComponent(opts[12])
				.addComponent(opts[13])
				.addComponent(opts[14])
				.addComponent(opts[15])
				.addComponent(opts[16])
				.addComponent(opts[17])
				.addComponent(opts[18])
				.addComponent(opts[19]))
			.addGroup(cpLayout.createParallelGroup()
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[0])
					.addComponent(opts[20]))
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[1])
					.addComponent(opts[21]))
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[2])
					.addComponent(opts[22]))
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[3])
					.addComponent(opts[23]))
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[4])
					.addComponent(opts[24]))
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[5])
					.addComponent(opts[25]))
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[6])
					.addComponent(opts[26]))
				.addGroup(cpLayout.createSequentialGroup()
					.addComponent(wlab[7])
					.addComponent(opts[27]))
				)
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(orlabel)
				.addComponent(wdirs[0])
				.addComponent(wdirs[1])
				.addComponent(wdirs[2])
				.addComponent(wdirs[3]))
				);	
		setLayout(cpLayout);
		setPreferredSize(new Dimension(325,150));
		cellpick.setMaximumSize(new Dimension(250, 10));
		MBOTPick.setMaximumSize(new Dimension(250, 10));
		//init wolfram labels
		for (int aa = 0; aa < wlab.length; aa++){
			wlab[aa].setVisible(false);}
		//wolfram direction
		for(int ac = 0; ac < wdirs.length; ac++){
			wdirs[ac].setVisible(false); wdirs[ac].addActionListener(this);}
		orlabel.setVisible(false);
		//init opts	
		for (int ab = 0; ab < opts.length; ab++){
			opts[ab].setMaximumSize(new Dimension(10,10));opts[ab].setVisible(false);
			 opts[ab].setEnabled(false); opts[ab].addItemListener(this);}
		jack.setVisible(false); jill.setVisible(false);
		
	
	MBOTPick.setVisible(false);
	MBOTPick.setEnabled(false);
	
	cellpick.addActionListener(this);
	MBOTPick.addActionListener(this);
	
	
	}
	
public void setCOH(cellOptionHandler ned){ 
						gate = ned;
						//initialize starting cell type
						setCell();
				}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == cellpick){command = 1; setCell(); fireucEvent();}
	if(e.getSource() == MBOTPick){command = 2; MBOTtype = MBOTPick.getSelectedItem().toString(); setCell(); fireucEvent();}
	if(e.getSource() == wdirs[0]){gate.setInt("Dir", 0);}
	if(e.getSource() == wdirs[1]){gate.setInt("Dir", 1);}
	if(e.getSource() == wdirs[2]){gate.setInt("Dir", 2);}
	if(e.getSource() == wdirs[3]){gate.setInt("Dir", 3);}
	}

public void itemStateChanged(ItemEvent e){//age, fade, born, survives
	boolean option = false;
	for(int i = 0; i< opts.length; i++){
		if(e.getItemSelectable() == opts[i]){
		switch(i){
			case 0: gate.setBool("Ages", opts[i].getState()); break;
			case 1: gate.setBool("Fades", opts[i].getState()); break;
			case 2: gate.setBool("B0", opts[i].getState()); break;
			case 3: gate.setBool("B1", opts[i].getState()); break;
			case 4: gate.setBool("B2", opts[i].getState()); break;
			case 5: gate.setBool("B3", opts[i].getState()); break;
			case 6: gate.setBool("B4", opts[i].getState()); break;
			case 7: gate.setBool("B5", opts[i].getState()); break;
			case 8: gate.setBool("B6", opts[i].getState()); break;
			case 9: gate.setBool("B7", opts[i].getState()); break;
			case 10: gate.setBool("B8", opts[i].getState());break;
			case 11: gate.setBool("S0", opts[i].getState());break; 
			case 12: gate.setBool("S1", opts[i].getState());break;
			case 13: gate.setBool("S2", opts[i].getState());break;
			case 14: gate.setBool("S3", opts[i].getState());break;
			case 15: gate.setBool("S4", opts[i].getState());break;
			case 16: gate.setBool("S5", opts[i].getState());break;
			case 17: gate.setBool("S6", opts[i].getState());break;
			case 18: gate.setBool("S7", opts[i].getState());break;
			case 19: gate.setBool("S8", opts[i].getState());break;
			case 20: gate.setBool("W7", opts[i].getState());break;
			case 21: gate.setBool("W6", opts[i].getState());break;
			case 22: gate.setBool("W5", opts[i].getState());break;
			case 23: gate.setBool("W4", opts[i].getState());break;
			case 24: gate.setBool("W3", opts[i].getState());break;
			case 25: gate.setBool("W2", opts[i].getState());break;
			case 26: gate.setBool("W1", opts[i].getState());break;
			case 27: gate.setBool("W0", opts[i].getState());break;
			}
		}
	}
}

private void setCell(){
	for(int cn = 0; cn < Cells.length; cn++){
		if(cellpick.getSelectedItem() == Cells[cn]){ ct = cn;}
		setOpts(gate.generateCell());
	}
	if(ct == 2){MBOTPick.setVisible(true); MBOTPick.setEnabled(true);} else{MBOTPick.setVisible(false); MBOTPick.setEnabled(false);}
}

private void setOpts(cell darwin){
	boolean visifier = false;
	String[] names = new String[]{"Age", "Fade", "Mat", "Born", "Survives", "WolfRule", "Orient"};
	for(int concount = 0; concount< names.length; concount++){
		switch(concount){
			case 3: if(darwin.getControls(names[concount]) && MBOTtype == "Custom"){visifier = true;} else{visifier = false;} break;
			case 4: if(darwin.getControls(names[concount]) && MBOTtype == "Custom"){visifier = true;} else{visifier = false;} break;
			
			default: visifier =  darwin.getControls(names[concount]);  break;
		}
		toggleControl(concount, visifier);
	}
}

private void toggleControl(int a, boolean b){
	switch(a){
		case 0: opts[0].setVisible(b); opts[0].setEnabled(b); break;
		case 1: opts[1].setVisible(b); opts[1].setEnabled(b); break;
		case 2: break;
		case 3: for(int c = 0; c < 9; c++){opts[c+2].setLabel(String.valueOf(c)); opts[c+2].setVisible(b); opts[c+2].setEnabled(b);} jack.setVisible(b); break;
		case 4: for(int c = 0; c < 9; c++){opts[c+11].setLabel(String.valueOf(c)); opts[c+11].setVisible(b); opts[c+11].setEnabled(b);} jill.setVisible(b); break;
		case 5: for(int c = 0; c < 8; c++){wlab[c].setVisible(b); opts[c+20].setVisible(b); opts[c+20].setEnabled(b);} break;
		case 6: for(int c = 0; c < 4; c++){wdirs[c].setVisible(b); wdirs[c].setEnabled(b);}orlabel.setVisible(b); break;
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
	
public int getCT(){ return ct;}

public String getMBOT(){ return MBOTtype;}
	
}
