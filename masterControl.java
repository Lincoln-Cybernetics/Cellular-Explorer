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
	JButton[] buttons = new JButton[9];
	Checkbox[] checks = new Checkbox[1];
	JSlider throttle;
	JSlider rtsetter;
	JLabel rtslab;
	controlBox dispbox;
	controlBox wrapbox;
	JButton cidButton;
	
	// relate to sending command events
	private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
	int cntrl = 0;
	
	//automaton rule numbers
	int rulnum = 0;
	boolean rulval;
	
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
		buttons[3] = new JButton("State Editor");
		buttons[4] = new JButton("Cell Editor");
		buttons[5] = new JButton("Cell Picker");
		buttons[6] = new JButton("Selection Tools");
		buttons[7] = new JButton("Brushes");
		buttons[8] = new JButton("About");
		checks[0] = new Checkbox("Compass Chaos");
		throttle = new JSlider(0,1000);
		rtsetter = new JSlider(1,100);
		rtslab = new JLabel("50");
		dispbox = new controlBox(1);
		wrapbox = new controlBox(2);
		cidButton = new JButton("Cell info");
		
		setPreferredSize(new Dimension(675,165));
		
		// set layout
		GroupLayout mclayout = new GroupLayout(this);
		mclayout.setAutoCreateGaps(false);
		mclayout.setAutoCreateContainerGaps(false);
		
		mclayout.setHorizontalGroup(
			mclayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addGroup(mclayout.createSequentialGroup()
				.addComponent(buttons[0])
				.addComponent(buttons[1])
				.addComponent(throttle)
				.addComponent(buttons[2]))
			.addGroup(mclayout.createSequentialGroup()
				.addComponent(buttons[3])
				.addComponent(buttons[4])
				.addComponent(buttons[5])
				.addComponent(buttons[6])
				.addComponent(buttons[7]))
			.addGroup(mclayout.createSequentialGroup()
				.addComponent(buttons[8])
				.addComponent(dispbox)
				.addComponent(wrapbox)
				.addComponent(cidButton))
			.addGroup(mclayout.createSequentialGroup()
				.addComponent(checks[0])
				.addComponent(rtsetter)
				.addComponent(rtslab))
				);
				
		mclayout.setVerticalGroup(
			mclayout.createSequentialGroup()
			.addGroup(mclayout.createParallelGroup()
				.addComponent(buttons[0])
				.addComponent(buttons[1])
				.addComponent(throttle)
				.addComponent(buttons[2]))
			.addGroup(mclayout.createParallelGroup()
				.addComponent(buttons[3])
				.addComponent(buttons[4])
				.addComponent(buttons[5])
				.addComponent(buttons[6])
				.addComponent(buttons[7]))
			.addGroup(mclayout.createParallelGroup()
				.addComponent(buttons[8])
				.addComponent(dispbox)
				.addComponent(wrapbox)
				.addComponent(cidButton))
			.addGroup(mclayout.createParallelGroup()
				.addComponent(checks[0])
				.addComponent(rtsetter)
				.addComponent(rtslab))
				);
		setLayout( mclayout );
		
		// setup controls
		for( int c = 0; c<= buttons.length-1; c++){
			if(c == 2){ throttle.addChangeListener(this);throttle.setMaximumSize(new Dimension(100,15));
			}
			 buttons[c].addActionListener(this);
		}
	 checks[0].addItemListener(this);checks[0].setMaximumSize(new Dimension(100,50));
	 rtsetter.addChangeListener(this); rtsetter.setMaximumSize(new Dimension(100,15));
	 rtslab.setText(Integer.toString(rtsetter.getValue()));
	 dispbox.adducListener(this);
	 wrapbox.adducListener(this); wrapbox.setMaximumSize(new Dimension(100,50));
	 cidButton.addActionListener(this);
		
		}
		
	public void init(){ 
		//rtsetter and rtslab start off invisible
		rtsetter.setVisible(false); rtsetter.setEnabled(false);
		rtslab.setVisible(false);
	}
		
	//public void setWB(){remove(wrapbox);remove(cidButton);wrapbox = new controlBox(2);add(wrapbox);add(cidButton);}
	public void setCFLAG(boolean b){cflag = b;}
	public void actionPerformed(ActionEvent e){
		//create a new automaton
		if(e.getSource() == buttons[0]){if(!cflag){ cntrl = 1;  fireucEvent();}}
		if(e.getSource() == buttons[8]){ aboutMe();}
		if(cflag){
			/* send commands based on what button is pressed
			 *  1 = New
			 *  2 = Play/Pause
			 *  3 = Step
			 *  4 = Edit State
			 *  5 = Edit Cells
			 *  6 = Cell Picker
			 *  6 = Selection Tools
			 *  7 = Brushes
			 *  8 = Set Iteration Speed
			 *  9 = Set Speed
			 * 10 = Set Display Type
			 * 11 = Set Wrap Type
			 * 12 = show cell info
			 * 13 = set automaton rules
			 */ 
			for( int cnum = 1; cnum <= buttons.length-2; cnum++){
				if(e.getSource() == buttons[cnum]){ cntrl = cnum+1; fireucEvent();}
			}
		if(e.getSource() == cidButton){ cntrl = 12; fireucEvent();}
			
		}
		}
		
		public void stateChanged(ChangeEvent e){
			if(cflag){
			if(e.getSource() == throttle) { cntrl = 9; fireucEvent();}
			if(e.getSource() == rtsetter){ rtslab.setText(Integer.toString(rtsetter.getValue())); cntrl = 13; fireucEvent();}
		}
		}
		
		public void itemStateChanged(ItemEvent e){
			if(cflag){
				if(e.getSource() == checks[0]){ 
					rtsetter.setVisible(checks[0].getState()); rtsetter.setEnabled(checks[0].getState());
					rtslab.setVisible(checks[0].getState());
					rulnum = 0; rulval = checks[0].getState(); cntrl = 13; fireucEvent();}
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
		String noticea = "Cellular Explorer Prototype v. 0.0.6 (Valentine 02014)\nPowered by Lincoln Cybernetics.\n http://lincolncybernetics.com \n";
  String noticeb ="Copyright(C) 02014 Matt Ahlschwede\n\n";
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
	public int getRN(){
		return rulnum;}
		
	public int getRT(){
		return rtsetter.getValue();}
		
	public boolean getRV(){
		return rulval;}
}
