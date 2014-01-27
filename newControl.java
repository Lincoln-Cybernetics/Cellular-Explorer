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

public class newControl extends JComponent implements ActionListener, ChangeListener{
	JSlider xsli;
	JSlider ysli;
	JLabel xlab;
	JLabel ylab;
	JButton cre;
	int xval;
		int yval;
	
	// relate to sending command events
	private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
	int cntrl = 0;
	
	public newControl(){
		xsli = new JSlider(1,400); 
		ysli = new JSlider(1,150);
		xlab = new JLabel();
		ylab = new JLabel();
		cre = new JButton("Create");
		
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
					);
					
			ncLayout.setVerticalGroup(
				ncLayout.createSequentialGroup()
					.addGroup(ncLayout.createParallelGroup()
						.addComponent(xsli)
						.addComponent(xlab))
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
					}
					
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == cre){cntrl = 1; fireucEvent();}
			}
		
		public void stateChanged(ChangeEvent e){
			if(e.getSource() == xsli){xlab.setText("X: " + Integer.toString(xsli.getValue())); xval = xsli.getValue();}
			if(e.getSource() == ysli){ylab.setText("Y: "+Integer.toString(ysli.getValue())); yval = ysli.getValue();}
			}
		
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
