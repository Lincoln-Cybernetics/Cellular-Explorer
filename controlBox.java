import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Checkbox.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class controlBox extends JComponent implements ActionListener, ItemListener{
	
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
		dispmod[0] = new JRadioButton("Normal");
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
