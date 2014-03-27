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

public class masterControl extends JComponent implements ActionListener, ChangeListener, ItemListener, ucListener{
	// Main Controls
	JButton[] buttons = new JButton[3];
	Checkbox[] checks = new Checkbox[3];
	JSlider throttle;
	JSlider[] rtsetter = new JSlider[3];
	JLabel[] rtslab = new JLabel[3];
	controlBox dispbox;
	controlBox wrapbox;
	JButton cidButton;
	JMenuItem[] winitem = new JMenuItem[7];
	JMenu winmen;
	JMenuBar mainbar;
	
	// relate to sending command events
	private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
	int cntrl = 0;
	
	//automaton rule numbers
	int rulnum = 0;//what rule 0= Compass Chaos, 1 = Boredom(1)
	boolean rulval;//is the rule on or off
	int rtval;// rule timer
	
	//flags
	//is there an active automaton to control?
	boolean cflag = false;
	// sets the display type
	int disptype = 1;
	// sets edge wrapping
	int wraptype = 0;
	
	public masterControl(){
		// create main controls
		buttons[0] = new JButton("New");
		buttons[1] = new JButton("Play/Pause");
		buttons[2] = new JButton("Step");
		//buttons[3] = new JButton("State Editor");
		//buttons[4] = new JButton("Cell Editor");
		//buttons[5] = new JButton("Cell Picker");
		//buttons[6] = new JButton("Selection Tools");
		//buttons[7] = new JButton("Brushes");
		//buttons[8] = new JButton("About");
		checks[0] = new Checkbox("Boredom(1)");
		checks[1] = new Checkbox("Boredom(2)");
		checks[2] = new Checkbox("Compass Chaos");
		throttle = new JSlider(0,1000);
		rtsetter[0] = new JSlider(1,100);
		rtsetter[1] = new JSlider(1,100);
		rtsetter[2] = new JSlider(1,100);
		rtslab[0] = new JLabel("50");
		rtslab[1] = new JLabel("50");
		rtslab[2] = new JLabel("50");
		dispbox = new controlBox(1);
		wrapbox = new controlBox(2);
		cidButton = new JButton("Cell info");
		winitem[0] = new JMenuItem("State Editor");
		winitem[1] = new JMenuItem("Cell Editor");
		winitem[2] = new JMenuItem("Cell Picker");
		winitem[3] = new JMenuItem("Selection Tools");
		winitem[4] = new JMenuItem("Brushes");
		winitem[5] = new JMenuItem("Cell Info");
		winitem[6] = new JMenuItem("About");
		winmen = new JMenu("Window");
		mainbar = new JMenuBar();
		
		setPreferredSize(new Dimension(675,165));
		
		// set layout
		GroupLayout mclayout = new GroupLayout(this);
		mclayout.setAutoCreateGaps(false);
		mclayout.setAutoCreateContainerGaps(false);
		
		mclayout.setHorizontalGroup(
			mclayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(mainbar)
			.addGroup(mclayout.createSequentialGroup()
				.addComponent(buttons[0])
				.addComponent(buttons[1])
				.addComponent(throttle)
				.addComponent(buttons[2]))
			/*.addGroup(mclayout.createSequentialGroup()
				.addComponent(buttons[3])
				.addComponent(buttons[4])
				.addComponent(buttons[5])
				.addComponent(buttons[6])
				.addComponent(buttons[7]))*/
			.addGroup(mclayout.createSequentialGroup()
				//.addComponent(buttons[8])
				.addComponent(dispbox)
				.addComponent(wrapbox)
				.addComponent(cidButton))
			.addGroup(mclayout.createSequentialGroup()
				.addComponent(checks[0])
				.addComponent(rtsetter[0])
				.addComponent(rtslab[0]))
			.addGroup(mclayout.createSequentialGroup()
				.addComponent(checks[1])
				.addComponent(rtsetter[1])
				.addComponent(rtslab[1])
				.addComponent(checks[2])
				.addComponent(rtsetter[2])
				.addComponent(rtslab[2]))
				);
				
		mclayout.setVerticalGroup(
			mclayout.createSequentialGroup()
			.addComponent(mainbar)
			.addGroup(mclayout.createParallelGroup()
				.addComponent(buttons[0])
				.addComponent(buttons[1])
				.addComponent(throttle)
				.addComponent(buttons[2]))
			/*.addGroup(mclayout.createParallelGroup()
				.addComponent(buttons[3])
				.addComponent(buttons[4])
				.addComponent(buttons[5])
				.addComponent(buttons[6])
				.addComponent(buttons[7]))*/
			.addGroup(mclayout.createParallelGroup()
				//.addComponent(buttons[8])
				.addComponent(dispbox)
				.addComponent(wrapbox)
				.addComponent(cidButton))
			.addGroup(mclayout.createParallelGroup()
				.addComponent(checks[0])
				.addComponent(rtsetter[0])
				.addComponent(rtslab[0]))
			.addGroup(mclayout.createParallelGroup()
				.addComponent(checks[1])
				.addComponent(rtsetter[1])
				.addComponent(rtslab[1])
				.addComponent(checks[2])
				.addComponent(rtsetter[2])
				.addComponent(rtslab[2]))
				);
		setLayout( mclayout );
		
		// setup controls
		//buttons
		for( int c = 0; c < buttons.length; c++){
			if(c == 2){ throttle.addChangeListener(this);throttle.setMaximumSize(new Dimension(100,15));
			}
			 buttons[c].addActionListener(this);
		}
	//checkboxes	
	for(int a = 0; a < checks.length; a++){	
	 checks[a].addItemListener(this);checks[a].setMaximumSize(new Dimension(100,50));
	 rtsetter[a].addChangeListener(this); rtsetter[a].setMaximumSize(new Dimension(100,15));
	 rtslab[a].setText(Integer.toString(rtsetter[a].getValue()));
		}
		
    rtslab[2].setText(Integer.toString(rtsetter[2].getValue()*2));
	 dispbox.adducListener(this);
	 wrapbox.adducListener(this); wrapbox.setMaximumSize(new Dimension(100,50));
	 cidButton.addActionListener(this);
		
	// menu bar
	for(int s = 0; s < winitem.length; s++){
		winmen.add(winitem[s]); winitem[s].addActionListener(this);}
	
	mainbar.add(winmen);
		}
		
	public void init(){ 
		//rtsetter and rtslab start off invisible
		for(int o = 0; o < rtsetter.length; o++){
		rtsetter[o].setVisible(false); rtsetter[o].setEnabled(false);
		rtslab[o].setVisible(false);
	}
	}
		
	//public void setWB(){remove(wrapbox);remove(cidButton);wrapbox = new controlBox(2);add(wrapbox);add(cidButton);}
	public void setCFLAG(boolean b){cflag = b;if(b){cntrl = 9; fireucEvent();}}
	public void actionPerformed(ActionEvent e){
		//create a new automaton
		if(e.getSource() == buttons[0]){if(!cflag){ cntrl = 1;  fireucEvent();}}
		//if(e.getSource() == buttons[8]){ aboutMe();}
		if(cflag){
			/* send commands based on what button is pressed
			 *  1 = New
			 *  2 = Play/Pause
			 *  3 = Step
			 *  4 = Edit State
			 *  5 = Edit Cells
			 *  6 = Cell Picker
			 *  7 = Selection Tools
			 *  8 = Brushes
			 *  9 = Set Iteration Speed
			 * 10 = Set Display Type
			 * 11 = Set Wrap Type
			 * 12 = show cell info
			 * 13 = set automaton rules
			 */ 
			 for(int r = 0; r < winitem.length-1; r++){int c[] = new int[]{4,5,6,7,8,12};
			 if(e.getSource() == winitem[r]){ cntrl = c[r]; fireucEvent();}}
			 if(e.getSource() == winitem[6]){aboutMe();}
			 
			for( int cnum = 1; cnum < buttons.length; cnum++){
				if(e.getSource() == buttons[cnum]){ cntrl = cnum+1; fireucEvent();}
			}
		if(e.getSource() == cidButton){ cntrl = 12; fireucEvent();}
			
		}
		}
		
		public void stateChanged(ChangeEvent e){
			if(cflag){
			if(e.getSource() == throttle) { cntrl = 9; fireucEvent();}
			for(int r = 0; r < rtsetter.length; r++){
			if(e.getSource() == rtsetter[r]){ rtslab[r].setText(Integer.toString(rtsetter[r].getValue()));
			rtval = rtsetter[r].getValue(); }
			//double time length for boredom(2)
			if(e.getSource() == rtsetter[1]){rtslab[1].setText(Integer.toString(rtsetter[1].getValue()*2));}
			cntrl = 13; fireucEvent();
		}
		}
		}
		
		public void itemStateChanged(ItemEvent e){
			if(cflag){
				for(int w = 0; w < checks.length; w++){
				if(e.getSource() == checks[w]){ 
					rtsetter[w].setVisible(checks[w].getState()); rtsetter[w].setEnabled(checks[w].getState());
					rtslab[w].setVisible(checks[w].getState());
					rulnum = w; rulval = checks[w].getState(); rtval = rtsetter[w].getValue(); cntrl = 13; fireucEvent();}
				}
				//Boredom(1) and Boredom(2) are mutually exclusive
				if(e.getSource() == checks[1]){if(checks[2].getState()){checks[2].setState(false);}}
				if(e.getSource() == checks[2]){if(checks[1].getState()){checks[1].setState(false);}}
			}
		}
		
		public void handleControl(ucEvent e){
			if(cflag){
			if(e.getSource() == dispbox){ cntrl = 10; disptype = e.getCommand(); fireucEvent();}
			if(e.getSource() == wrapbox){ cntrl = 11;
						switch(e.getCommand()){
							case 0: wraptype -= 1;  if(wraptype < 0){wraptype = 0;} break;
							case 1: wraptype += 1;  if(wraptype > 3){wraptype = 3;} break;
							case 2: wraptype -= 2;  if(wraptype < 0){wraptype = 0;} break;
							case 3: wraptype += 2;  if(wraptype > 3){wraptype = 3;} break;
							}fireucEvent();}
						}
				}
		
		// get control values
		// speed control
		public int getZTime(){
			return throttle.getValue();}
		// display type
		public int getDispType(){ return disptype;}
		// automaton edge wrapping
		public int getWrapType(){ return wraptype;}
		
		//control event generation
		//adds listeners for command events
		public synchronized void adducListener(ucListener listener){
		_audience.add(listener);}
	
		//removes listeners for command events	
		public synchronized void removeucListener(ucListener listener){
		_audience.remove(listener);}
	
		
		//notifies application when a command is sent	
		private synchronized void fireucEvent(){
		ucEvent cmd = new ucEvent(this);
		cmd.setCommand(cntrl);
		Iterator i = _audience.iterator();
		while(i.hasNext()){
		((ucListener) i.next()).handleControl(cmd);}
		}
		
		public void aboutMe(){
				JFrame cpanel = new JFrame("About");
		String noticea = "Cellular Explorer Prototype v. 0.0.7 (Daylight 02014)\nPowered by Lincoln Cybernetics.\n http://lincolncybernetics.com \n";
  String noticeb ="Copyright(C) 02014 Matt Ahlschwede\n\n Special thanks to:  James Woody, Brian Prentice, and Alex V.\n for the feedback and ideas that have made this program possible. \n\n";
  String noticec = " This program is free software: you can redistribute it and/or\nmodify";
  String noticed ="  it under the terms of the GNU General Public\nLicense as published by";
  String noticee=  "the Free Software Foundation,\neither version 3 of the License, or";
  String noticef =  "(at your option) any later version.\n\n";
  String noticeg =  " This program is distributed in the hope that it will be useful,\n";
  String noticeh =  "but WITHOUT ANY WARRANTY; without even the implied warranty of\n";
  String noticei =  " MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.\n  See the";
  String noticej =  "GNU General Public License for more details.\n\n";
  String noticek =  " You should have received a copy of the GNU General Public License\n";
  String noticel =  "along with this program.\n  If not, see <http://www.gnu.org/licenses/>.\n";
  String noticem = "The GPL is appended to the end of the file cellularExplorer.java";
  String  notice = noticea+noticeb+noticec+noticed+noticee+noticef+noticeg+noticeh+noticei+noticej+noticek+noticel+noticem;
		JTextPane sign = new JTextPane();
		sign.setEditable(false);
		sign.setText(notice);
		cpanel.getContentPane().add(sign);
		cpanel.setLocation(675,0);
		cpanel.pack();
		cpanel.setVisible(true);
		cpanel.setResizable(true);
	}
	
	//variable getters
	public String getRName(){
		 switch(rulnum){
			 case 0: return "B1";
			 case 1: return "B2";
			 case 2: return "CC";
		 }
		 return "ERROR";
		 }
		
	public int getRT(){
		return rtval;}
		
	public boolean getRV(){
		return rulval;}
}
//End of Master Control


//Component to control wrap and display mode options
 class controlBox extends JComponent implements ActionListener, ItemListener{
	
	JLabel name;
	Component[] settings = new Component[2];
	int type = 1;
	boolean[] selecto = new boolean[2];
	GroupLayout cblayout;
	
// relate to sending command events
	private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
	int cntrl = 1;

public controlBox(int a){
	cblayout = new GroupLayout(this);
	cblayout.setAutoCreateGaps(false);
	cblayout.setAutoCreateContainerGaps(true);
	type = a;
	selecto[0] = false; selecto[1] = false;
	switch(a){
		case 1:
		JRadioButton[] dispmod = new JRadioButton[2];
		name = new JLabel("Display Type");
		dispmod[0] = new JRadioButton("Classic");
		dispmod[1] = new JRadioButton("Multicolor");
		ButtonGroup dselector = new ButtonGroup();
		dselector.add(dispmod[0]); dispmod[0].addActionListener(this);
		dselector.add(dispmod[1]); dispmod[1].addActionListener(this);
		dispmod[0].setSelected(true);
		settings[0] = dispmod[0];  settings[1] = dispmod[1];
		break;
		
		case 2:
		Checkbox[] wrapmod = new Checkbox[2];
		name = new JLabel("Wrap Edges");
		wrapmod[0] = new Checkbox("X-wrap");
		wrapmod[1] = new Checkbox("Y-wrap");
		wrapmod[0].addItemListener(this);
		wrapmod[1].addItemListener(this);
		settings[0] = wrapmod[0];
		settings[1] = wrapmod[1];
		break;
		
		default : break;
	}
	cblayout.setHorizontalGroup(
		cblayout.createParallelGroup(GroupLayout.Alignment.CENTER)
		.addComponent(name)
		.addGroup(cblayout.createSequentialGroup()
			.addComponent(settings[0])
			.addComponent(settings[1]))
			);
			
		cblayout.setVerticalGroup(
		cblayout.createSequentialGroup()
		.addComponent(name)
		.addGroup(cblayout.createParallelGroup()
			.addComponent(settings[0])
			.addComponent(settings[1]))
			);
	setLayout(cblayout);
	setBorder(BorderFactory.createLoweredBevelBorder());
}

	public void actionPerformed(ActionEvent e){
		switch(type){
		case 1:
		if(e.getSource() == settings[0]){cntrl = 1; fireucEvent();}
		if(e.getSource() == settings[1]){cntrl = 4; fireucEvent();}
		break;
			}
		}
		
	public void itemStateChanged(ItemEvent e){
		switch(type){
			case 2:
			if(e.getSource() == settings[0]){selecto[0] = !selecto[0];if(selecto[0]){cntrl = 1;}else{cntrl = 0;}}
			if(e.getSource() == settings[1]){selecto[1] = !selecto[1];if(selecto[1]){cntrl = 3;}else{cntrl = 2;}}
			fireucEvent();
			break;
				}
		}

	//control event generation
		//adds listeners for command events
		public synchronized void adducListener(ucListener listener){
		_audience.add(listener);}
	
		//removes listeners for command events	
		public synchronized void removeucListener(ucListener listener){
		_audience.remove(listener);}
	
		
		//notifies application when a command is sent	
		private synchronized void fireucEvent(){
		ucEvent cmd = new ucEvent(this);
		cmd.setCommand(cntrl);
		Iterator i = _audience.iterator();
		while(i.hasNext()){
		((ucListener) i.next()).handleControl(cmd);}
		}

}
//End controlBox


//Component to create new Brain/automata
class newControl extends JComponent implements ActionListener, ChangeListener, ItemListener{
	JSlider xsli;
	JSlider ysli;
	JLabel xlab;
	JLabel ylab;
	JButton cre;
	int xval;
	int yval;
	Checkbox sqr;
	boolean sqrflag;
	
	// relate to sending command events
	private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
	int cntrl = 0;
	
	public newControl(){
		xsli = new JSlider(1,400); 
		ysli = new JSlider(1,150);
		xlab = new JLabel();
		ylab = new JLabel();
		cre = new JButton("Create");
		sqr = new Checkbox("Square");
		
		GroupLayout ncLayout = new GroupLayout(this);
		ncLayout.setAutoCreateGaps(false);
		ncLayout.setAutoCreateContainerGaps(true);
		
		ncLayout.setHorizontalGroup(
			ncLayout.createSequentialGroup()
				.addGroup(ncLayout.createParallelGroup()
					.addComponent(xsli)
					.addComponent(ysli))
				.addGroup(ncLayout.createParallelGroup()
					.addComponent(cre))
				.addGroup(ncLayout.createParallelGroup()
					.addComponent(xlab)
					.addComponent(ylab))
				.addGroup(ncLayout.createParallelGroup()
					.addComponent(sqr))
					);
					
			ncLayout.setVerticalGroup(
				ncLayout.createSequentialGroup()
					.addGroup(ncLayout.createParallelGroup()
						.addComponent(xsli)
						.addComponent(xlab))
					.addGroup(ncLayout.createParallelGroup()
						.addComponent(sqr))
					.addGroup(ncLayout.createParallelGroup()
						.addComponent(ysli)
						.addComponent(ylab))
					.addGroup(ncLayout.createParallelGroup()
						.addComponent(cre))
						);
						
					setLayout(ncLayout);
						
				xlab.setText("X :" + Integer.toString(xsli.getValue()));
				xval = xsli.getValue();
				ylab.setText("Y :" + Integer.toString(ysli.getValue()));
				yval = ysli.getValue();
				xsli.addChangeListener(this); ysli.addChangeListener(this);
				cre.addActionListener(this);
				sqrflag = sqr.getState();
				sqr.addItemListener(this);
					}
					
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == cre){cntrl = 1; fireucEvent();}
			}
		
		public void stateChanged(ChangeEvent e){
			if(e.getSource() == xsli){
			if(sqrflag){if(xsli.getValue() > 150){xsli.setValue(150);}ysli.setValue(xsli.getValue()); yval = ysli.getValue(); setYLAB();}
			 setXLAB(); xval = xsli.getValue();}
			if(e.getSource() == ysli){
				if(sqrflag){xsli.setValue(ysli.getValue()); xval = xsli.getValue(); setXLAB();}
				 setYLAB(); yval = ysli.getValue();}
			}
			
		public void itemStateChanged(ItemEvent e){
			if(e.getSource() == sqr){sqrflag = sqr.getState();if(sqrflag){xsli.setValue(ysli.getValue());setXLAB();xval = xsli.getValue();}}
		}
		
		private void setXLAB(){xlab.setText("X: " + Integer.toString(xsli.getValue()));}
		
		private void setYLAB(){ylab.setText("Y: "+Integer.toString(ysli.getValue()));}
		
		public int getXVAL(){ return xval;}
		
		public int getYVAL(){ return yval;}
		
		//control event generation
		//adds listeners for command events
		public synchronized void adducListener(ucListener listener){
		_audience.add(listener);}
	
		//removes listeners for command events	
		public synchronized void removeucListener(ucListener listener){
		_audience.remove(listener);}
	
		
		//notifies application when a command is sent	
		private synchronized void fireucEvent(){
		ucEvent cmd = new ucEvent(this);
		cmd.setCommand(cntrl);
		Iterator i = _audience.iterator();
		while(i.hasNext()){
		((ucListener) i.next()).handleControl(cmd);}
		}
		
	
	}
