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

public class cellPicker extends JComponent implements ActionListener, ItemListener, ChangeListener{
	
cellOptionHandler gate;
// controlPanel variables
JComboBox cellpick;
String[] Cells = new String[]{"Cell", "Wolfram", "MBOT", "Randomly-active cell", "OnCell", "OffCell", 
	"BlinkCell", "Symmetrical", "Conveyor", "Strobe Cell", "Total Cell", "Average Cell","EVBOT"};

JComboBox  MBOTPick;
String[] MBOTCells = new String[]{"Custom", "2x2", "3/4 Life", "Amoeba", "Assimilation", "Coagulations", "Coral", "Day and Night", "Diamoeba", "Dot Life",
"Dry Life", "Fredkin", "Gnarl", "High Life", "Life", "Life without Death", "Live Free or Die", "Long Life", "Maze", "Mazectric",
"Move", "Pseudo-life", "Replicator", "Seeds", "Serviettes", "Stains", "Vote", "Vote 4/5", "Walled Cities"};
String MBOTtype = "Custom";

Checkbox[] opts = new Checkbox[31];//All the options

JSlider matslider;//Sets the maturity variable (number of iterations between state calculations)
JLabel matlabel;//Maturity indicator
JSlider fadeslider;//Sets the Fade rule max. lifespan
JLabel fadelabel;//Fade indicator

JLabel jack;//born
JLabel jill;//survives
JLabel rulab;//rule label
JLabel[] wlab = new JLabel[8];//Wolfram rule labels
JLabel wrlab;//Wolfram rule indicator
JRadioButton[] wdirs = new JRadioButton[4];//4 orientations used by Wolfram & Symmetric
JLabel orlabel;//orientation label
JRadioButton[] dirs = new JRadioButton[8];//8 directions used by conveyor
JLabel dirlabel;//direction label

JButton mirbutt;// button to set mirror
JButton refsetbutt;//button to set mirror reference point

JSlider xfslider;//Neighborhood expansion factor
JLabel xflabel;//Labels the slider
JLabel xfind;//displays current value of xfslider

JRadioButton[] inm = new JRadioButton[3];//input mode
int mymode;//cell's input mode
ButtonGroup inpmod;//group the buttons
JLabel inmodlabel;//labels input mode controls

// relate to sending command events
int ct = 0; //cell-type
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;
/* ct = cell type:
 * 0 = cell
 * 1 = wolfram
 * 2 = MBOT
 * 3 = randCell. . . 
 */


public cellPicker(){
	cellpick = new JComboBox(Cells);
	MBOTPick = new JComboBox(MBOTCells);
	jack = new JLabel("Born");
	jill = new JLabel("Survives");
	rulab = new JLabel();
	//labels for wolfram rules
	wlab[0] = new JLabel("111");
	wlab[1] = new JLabel("110");
	wlab[2] = new JLabel("101");
	wlab[3] = new JLabel("100");
	wlab[4] = new JLabel("011");
	wlab[5] = new JLabel("010");
	wlab[6] = new JLabel("001");
	wlab[7] = new JLabel("000");
	wrlab = new JLabel("Wolfram Rule: 0");
	//Wolfram directions
	orlabel = new JLabel("Direction:");
	wdirs[0] = new JRadioButton("|"); wdirs[1] = new JRadioButton("/");
	wdirs[2] = new JRadioButton("--"); wdirs[3] = new JRadioButton("\\");
	ButtonGroup orients = new ButtonGroup(); orients.add(wdirs[0]); orients.add(wdirs[1]);
	orients.add(wdirs[2]); orients.add(wdirs[3]);wdirs[0].setSelected(true);
	// 8 directions
	dirlabel = new JLabel("Direction:");
	dirs[0] = new JRadioButton("Up"); dirs[1] = new JRadioButton("U-R"); dirs[2] = new JRadioButton("R"); dirs[3] = new JRadioButton("L-R");
	dirs[4] = new JRadioButton("Down"); dirs[5] = new JRadioButton("L-L"); dirs[6] = new JRadioButton("L"); dirs[7] = new JRadioButton("U-L");
	ButtonGroup directs = new ButtonGroup(); directs.add(dirs[0]);directs.add(dirs[1]);directs.add(dirs[2]);directs.add(dirs[3]);directs.add(dirs[4]);
	directs.add(dirs[5]);directs.add(dirs[6]);directs.add(dirs[7]); dirs[0].setSelected(true);
	
	// age and fade options
	opts[0] = new Checkbox("Ages"); 
	opts[1] = new Checkbox("Fades");
	fadeslider = new JSlider(1,1024);
	fadelabel = new JLabel();
	
	//maturity option
	matslider = new JSlider(1,512);
	matlabel = new JLabel();
	
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
	// Mirror 
	opts[28] = new Checkbox("Mirror Cell");
	mirbutt = new JButton("Set Mirror");
	refsetbutt = new JButton("Set Reference");
	//Any & All
	opts[29] = new Checkbox("Any");
	opts[30] = new Checkbox("All");
	
	//Expansion Factor
	xfslider = new JSlider(1,8);
	xfind = new JLabel("");
	xflabel = new JLabel("Expansion Factor");
	
	//Input mode
	inmodlabel = new JLabel("Input Mode:");
	inm[0] = new JRadioButton("Void");
	inm[1] = new JRadioButton("Binary");
	inm[2] = new JRadioButton("Integer");
	inpmod = new ButtonGroup(); inpmod.add(inm[0]); inpmod.add(inm[1]); inpmod.add(inm[2]); inm[1].setSelected(true);
	
	
	GroupLayout cpLayout = new GroupLayout(this);
	cpLayout.setAutoCreateGaps(false);
	cpLayout.setAutoCreateContainerGaps(false);
	
	cpLayout.setHorizontalGroup(
		cpLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(cellpick)
			.addComponent(MBOTPick)
			// ages/fades
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(opts[0])
				.addComponent(opts[1])
				.addComponent(fadeslider)
				.addComponent(fadelabel))
				//maturity
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(matslider)
				.addComponent(matlabel))
				// MBOT rule
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(rulab))
				//MBOT born
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
				//MBOT survives
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
				// Wolfram rule number
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(wrlab))
				// Wolfram rules
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
					// 4 orientations
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(orlabel)
				.addComponent(wdirs[0])
				.addComponent(wdirs[1])
				.addComponent(wdirs[2])
				.addComponent(wdirs[3])
				.addComponent(opts[29])
				.addComponent(opts[30]))
				// 8 directions
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(dirlabel)
				.addComponent(dirs[0])
				.addComponent(dirs[1])
				.addComponent(dirs[2])
				.addComponent(dirs[3]))
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(dirs[4])
				.addComponent(dirs[5])
				.addComponent(dirs[6])
				.addComponent(dirs[7]))
				// Mirror
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(opts[28])
				.addComponent(mirbutt)
				.addComponent(refsetbutt))
				//expansion Factor
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(xflabel)
				.addComponent(xfslider)
				.addComponent(xfind))
				//Input Mode
			.addComponent(inmodlabel)
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(inm[0])
				.addComponent(inm[1])
				.addComponent(inm[2]))
				);
				
	cpLayout.setVerticalGroup(
		cpLayout.createSequentialGroup()
			.addComponent(cellpick)
			.addComponent(MBOTPick)
			// ages/ fades
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(opts[0])
				.addComponent(opts[1])
				.addComponent(fadeslider)
				.addComponent(fadelabel))
				// maturity
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(matslider)
				.addComponent(matlabel))
				//MBOT rule
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(rulab))
				//MBOT born
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
				// MBOT survives
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
				//Wolfram rule number
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(wrlab))
				// Wolfram rules
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
				// 4 orientations
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(orlabel)
				.addComponent(wdirs[0])
				.addComponent(wdirs[1])
				.addComponent(wdirs[2])
				.addComponent(wdirs[3])
				.addComponent(opts[29])
				.addComponent(opts[30]))
				//8 directions
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(dirlabel)
				.addComponent(dirs[0])
				.addComponent(dirs[1])
				.addComponent(dirs[2])
				.addComponent(dirs[3]))
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(dirs[4])
				.addComponent(dirs[5])
				.addComponent(dirs[6])
				.addComponent(dirs[7]))
				//mirror
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(opts[28])
				.addComponent(mirbutt)
				.addComponent(refsetbutt))
			//Expansion Factor
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(xflabel)
				.addComponent(xfslider)
				.addComponent(xfind))
			//Input Mode
			.addComponent(inmodlabel)
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(inm[0])
				.addComponent(inm[1])
				.addComponent(inm[2]))
				);	
		setLayout(cpLayout);
		setPreferredSize(new Dimension(325,200));
		cellpick.setMaximumSize(new Dimension(250, 10));
		MBOTPick.setMaximumSize(new Dimension(250, 10));
		
		
		//init wolfram labels
		for (int aa = 0; aa < wlab.length; aa++){
			wlab[aa].setVisible(false);}
			wrlab.setVisible(false);
		//Wolfram direction
		for(int ac = 0; ac < wdirs.length; ac++){
			wdirs[ac].setVisible(false); wdirs[ac].addActionListener(this);}
		orlabel.setVisible(false);
		// 8 directions
		for(int ad = 0; ad < dirs.length; ad++){
			dirs[ad].setVisible(false); dirs[ad].addActionListener(this);}
			dirlabel.setVisible(false);
		//init opts	
		for (int ab = 0; ab < opts.length; ab++){
			opts[ab].setMaximumSize(new Dimension(10,10));opts[ab].setVisible(false);
			 opts[ab].addItemListener(this);}
		// MBOT stuff
		jack.setVisible(false); jill.setVisible(false); rulab.setVisible(false);
		
		//init fade components
		fadeslider.setVisible(false); fadeslider.setEnabled(false);fadeslider.setValue(256);
		fadeslider.addChangeListener(this); fadeslider.setMaximumSize(new Dimension(100,15));
		fadelabel.setVisible(false);fadelabel.setText(Integer.toString(fadeslider.getValue()));
		
		//set maturity components
		matslider.setVisible(false); matslider.setEnabled(false); matslider.setValue(1);
		matslider.addChangeListener(this); matslider.setMaximumSize(new Dimension(100,15));
		matlabel.setVisible(false); matlabel.setText("Maturity: "+Integer.toString(matslider.getValue()));
		
		//init mirror
		mirbutt.setVisible(false); mirbutt.setEnabled(false); mirbutt.addActionListener(this);
		refsetbutt.setVisible(false); refsetbutt.setEnabled(false); refsetbutt.addActionListener(this);
		
		//Expansion Factor
		xflabel.setVisible(false); xfslider.setVisible(false); xfslider.setEnabled(false);xfslider.setValue(1);
		 xfslider.addChangeListener(this);xfslider.setMaximumSize(new Dimension(100,15));
		xfind.setText("1"); xfind.setVisible(false);
		
		//Input Mode
		inmodlabel.setVisible(false);
		inm[0].setVisible(false); inm[0].addActionListener(this);inm[1].setVisible(false); inm[1].addActionListener(this);
		inm[2].setVisible(false); inm[2].addActionListener(this);
		
	//init MBOT picker
	MBOTPick.setVisible(false);
	if(ct == 2 || ct == 12){MBOTPick.setVisible(true); } else{MBOTPick.setVisible(false); }
	
	cellpick.addActionListener(this);
	MBOTPick.addActionListener(this);
	
	
	}
	
public void setCOH(cellOptionHandler ned){ 
						gate = ned;
						//initialize starting cell type
						setCell();
						ned = null;
				}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == cellpick){command = 1; setCType(); fireucEvent(); setCell();}
	if(e.getSource() == MBOTPick){command = 2; MBOTtype = MBOTPick.getSelectedItem().toString(); setCell();  fireucEvent();}
	if(e.getSource() == wdirs[0]){gate.setInt("Dir", 0);}
	if(e.getSource() == wdirs[1]){gate.setInt("Dir", 1);}
	if(e.getSource() == wdirs[2]){gate.setInt("Dir", 2);}
	if(e.getSource() == wdirs[3]){gate.setInt("Dir", 3);}
	if(e.getSource() == mirbutt){command = 5; fireucEvent(); command = 3; fireucEvent();}
	if(e.getSource() == refsetbutt){ command = 4; fireucEvent();}
	if(e.getSource() == dirs[0]){gate.setInt("Dir", 0);}
	if(e.getSource() == dirs[1]){gate.setInt("Dir", 1);}
	if(e.getSource() == dirs[2]){gate.setInt("Dir", 2);}
	if(e.getSource() == dirs[3]){gate.setInt("Dir", 3);}
	if(e.getSource() == dirs[4]){gate.setInt("Dir", 4);}
	if(e.getSource() == dirs[5]){gate.setInt("Dir", 5);}
	if(e.getSource() == dirs[6]){gate.setInt("Dir", 6);}
	if(e.getSource() == dirs[7]){gate.setInt("Dir", 7);}
	if(e.getSource() == inm[0]){gate.setInt("InMode", 0);}
	if(e.getSource() == inm[1]){gate.setInt("InMode", 1);}
	if(e.getSource() == inm[2]){gate.setInt("InMode", 2);}
	e = null;
	}

public void itemStateChanged(ItemEvent e){//age, fade, born, survives
	boolean option = false;
	for(int i = 0; i< opts.length; i++){
		if(e.getItemSelectable() == opts[i]){
		switch(i){
			case 0: gate.setBool("Ages", opts[i].getState()); if(!opts[0].getState() && opts[1].getState()){opts[1].setState(false);} break;
			case 1: gate.setBool("Fades", opts[i].getState());if(opts[1].getState() && !opts[0].getState()){opts[0].setState(true);} break;
			// Born on for MBOT & EVBOT
			case 2: gate.setBool("B0", opts[i].getState()); setRULAB(); break;
			case 3: gate.setBool("B1", opts[i].getState()); setRULAB();break;
			case 4: gate.setBool("B2", opts[i].getState()); setRULAB();break;
			case 5: gate.setBool("B3", opts[i].getState()); setRULAB();break;
			case 6: gate.setBool("B4", opts[i].getState()); setRULAB();break;
			case 7: gate.setBool("B5", opts[i].getState()); setRULAB();break;
			case 8: gate.setBool("B6", opts[i].getState()); setRULAB();break;
			case 9: gate.setBool("B7", opts[i].getState()); setRULAB();break;
			case 10: gate.setBool("B8", opts[i].getState());setRULAB();break;
			// Survives for MBOT & EVBOT
			case 11: gate.setBool("S0", opts[i].getState());setRULAB();break; 
			case 12: gate.setBool("S1", opts[i].getState());setRULAB();break;
			case 13: gate.setBool("S2", opts[i].getState());setRULAB();break;
			case 14: gate.setBool("S3", opts[i].getState());setRULAB();break;
			case 15: gate.setBool("S4", opts[i].getState());setRULAB();break;
			case 16: gate.setBool("S5", opts[i].getState());setRULAB();break;
			case 17: gate.setBool("S6", opts[i].getState());setRULAB();break;
			case 18: gate.setBool("S7", opts[i].getState());setRULAB();break;
			case 19: gate.setBool("S8", opts[i].getState());setRULAB();break;
			// Wolfram rules
			case 20: gate.setBool("W7", opts[i].getState());break;
			case 21: gate.setBool("W6", opts[i].getState());break;
			case 22: gate.setBool("W5", opts[i].getState());break;
			case 23: gate.setBool("W4", opts[i].getState());break;
			case 24: gate.setBool("W3", opts[i].getState());break;
			case 25: gate.setBool("W2", opts[i].getState());break;
			case 26: gate.setBool("W1", opts[i].getState());break;
			case 27: gate.setBool("W0", opts[i].getState());break;
			// mirror
			case 28: gate.setBool("Mirror", opts[i].getState()); mirbutt.setVisible(opts[i].getState()); mirbutt.setEnabled(opts[i].getState()); 
			refsetbutt.setVisible(opts[i].getState()); refsetbutt.setEnabled(opts[i].getState());
			 break;
			//Any & All
			case 29: gate.setBool("Any",opts[i].getState());if(opts[i].getState()){opts[30].setState(false);gate.setBool("All", false);}break;
			case 30: gate.setBool("All",opts[i].getState());if(opts[i].getState()){opts[29].setState(false);gate.setBool("Any", false);}break;
			}
			// set Wolfram rule#
			if(i > 19 && i < 28){ wrlab.setText("Wolfram Rule : "+Integer.toString(gate.generateCell().getParameter("WolfRule")));}
			
		}
	}
	e = null;
}

public void stateChanged(ChangeEvent e){
	// set fade rule maximum age
	if(e.getSource() == fadeslider){gate.setInt("Fade", fadeslider.getValue()); fadelabel.setText(Integer.toString(fadeslider.getValue()));}
	//set maturity setting
	if(e.getSource() == matslider){gate.setInt("Mat", matslider.getValue()); matlabel.setText("Maturity: "+Integer.toString(matslider.getValue()));}
	//Expansion Factor
	if(e.getSource() == xfslider){gate.setInt("Xfact", xfslider.getValue()); xfind.setText(Integer.toString(xfslider.getValue()));}
	e = null;
	}

private void setCType(){
	for(int cn = 0; cn < Cells.length; cn++){
		if(cellpick.getSelectedItem() == Cells[cn]){ ct = cn;  }
	}
}

private void setCell(){
	
		setOpts(gate.generateCell());
		//MBOT & EVBOT stuff
		if(ct == 2 || ct == 12){MBOTPick.setVisible(true); MBOTPick.setEnabled(true);setRULAB();rulab.setVisible(true);} else{MBOTPick.setVisible(false); MBOTPick.setEnabled(false);rulab.setVisible(false);}
}

private void setOpts(cell darwin){
	boolean visifier = false;
	String[] names = new String[]{"Age", "Fade", "Mat", "Born", "Survives", "WolfRule", "Orient", "Mirror", "Any", "All", "Dir","Xfact", "InMode"};
	for(int concount = 0; concount< names.length; concount++){
		 visifier =  darwin.getControls(names[concount]); 
		switch(concount){
			case 3: if(darwin.getControls(names[concount]) && MBOTtype == "Custom"){visifier = true;} else{visifier = false;} break;
			case 4: if(darwin.getControls(names[concount]) && MBOTtype == "Custom"){visifier = true;} else{visifier = false;} break;
			case 12: mymode = darwin.getParameter("InMode"); break;
			default: break;
		}
		toggleControl(concount, visifier);
	}
	darwin = null;
}

private void toggleControl(int a, boolean b){
	
	switch(a){
		//Ages
		case 0: opts[0].setVisible(b); opts[0].setEnabled(b); break;
		//fades
		case 1: opts[1].setVisible(b); opts[1].setEnabled(b); fadeslider.setVisible(b); fadeslider.setEnabled(b);if(b){ fadeslider.setValue(256); gate.setInt("Fade", fadeslider.getValue());} fadelabel.setVisible(b);if(b){fadelabel.setText(Integer.toString(fadeslider.getValue()));} break;
		//maturity
		case 2: matslider.setVisible(b); matslider.setEnabled(b); matlabel.setVisible(b); if(b){matslider.setValue(1);gate.setInt("Mat", matslider.getValue());matlabel.setText("Maturity: "+ Integer.toString(matslider.getValue()));} break;
		//Born/Survives
		case 3: for(int c = 0; c < 9; c++){opts[c+2].setLabel(String.valueOf(c)); opts[c+2].setVisible(b); opts[c+2].setEnabled(b);} jack.setVisible(b);rulab.setVisible(b); break;
		case 4: for(int c = 0; c < 9; c++){opts[c+11].setLabel(String.valueOf(c)); opts[c+11].setVisible(b); opts[c+11].setEnabled(b);} jill.setVisible(b); rulab.setVisible(b); break;
		//Wolfram rules
		case 5: for(int c = 0; c < 8; c++){wlab[c].setVisible(b); opts[c+20].setVisible(b); opts[c+20].setEnabled(b); wrlab.setVisible(b); wrlab.setText("Wolfram Rule : "+Integer.toString(gate.generateCell().getParameter("WolfRule")));} break;
		//orientation
		case 6: for(int c = 0; c < 4; c++){wdirs[c].setVisible(b); wdirs[c].setEnabled(b);}orlabel.setVisible(b); break;
		//Mirror
		case 7: opts[28].setVisible(b); opts[28].setEnabled(b);if(!b){mirbutt.setVisible(false); mirbutt.setEnabled(false);refsetbutt.setVisible(false); refsetbutt.setEnabled(false);}
		else{mirbutt.setVisible(opts[28].getState()); mirbutt.setEnabled(opts[28].getState());
		refsetbutt.setVisible(opts[28].getState());refsetbutt.setEnabled(opts[28].getState());}break;
		//Any
		case 8: opts[29].setVisible(b); opts[29].setEnabled(b); break;
		//All
		case 9: opts[30].setVisible(b); opts[30].setEnabled(b); break;
		//Directions
		case 10: for(int c = 0; c < 8; c++){dirs[c].setVisible(b); dirs[c].setEnabled(b);}dirlabel.setVisible(b); break;
		//Expansion Factor
		case 11: xflabel.setVisible(b); xfslider.setVisible(b);xfslider.setEnabled(b);xfind.setVisible(b);if(b){xfslider.setValue(1);gate.setInt("Xfact",1);xfind.setText("1");} break;
		//Input Mode
		case 12: inmodlabel.setVisible(b);inm[0].setVisible(b); inm[1].setVisible(b); inm[2].setVisible(b); /*if(b){inm[mymode].setSelected(true);}*/ break;
	}
}

//set rule label for MBOT & EVBOT
public void setRULAB(){
	cell gonzo = gate.generateCell();
	String bst = ""; String sst = ""; String[] brn = new String[]{"B0","B1","B2","B3","B4","B5","B6","B7","B8"};
	String[] srv = new String[]{"S0","S1","S2","S3","S4","S5","S6","S7","S8"};
	for(int i = 0; i<9; i++){ if(gonzo.getOption(brn[i])){bst = bst + Integer.toString(i);} if(gonzo.getOption(srv[i])){sst = sst+ Integer.toString(i);}}
	rulab.setText("B "+bst+"\\"+"S "+sst); 
	gonzo = null;
}
	


//event generation
//adds listeners for command events
public synchronized void adducListener(ucListener listener){
	_audience.add(listener);}
	
//removes listeners for command events	
public synchronized void removeucListener(ucListener listener){
	_audience.remove(listener);}
	
//notifies application when a command is sent	
/*commands
 * 1= set Cell type
 * 2 = set MBOT type
 * 3 = set mirror point
 * 4 = set mirror reference
 * 5 = clear mirror reference
 */
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
