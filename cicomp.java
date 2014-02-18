import javax.swing.*;

public class cicomp extends JComponent{
JLabel[] fields = new JLabel[10];
JLabel[] info = new JLabel[10];

cell xerxes;

public cicomp(){
	//field name labels
	fields[0] = new JLabel("Cell Type: ");
	fields[1] = new JLabel("Age: ");
	fields[2] = new JLabel("Fades: ");
	fields[3] = new JLabel("Direction: ");
	fields[4] = new JLabel("Maturity: ");
	fields[5] = new JLabel("Wolfram Rule :");
	fields[6] = new JLabel("B ");
	fields[7] = new JLabel("/S ");
	fields[8] = new JLabel("Mirror x: ");
	fields[9] = new JLabel("Mirror y: ");
	//labels to hold the values
	info[0] = new JLabel("Long-winded Moniker");
	info[1] = new JLabel("999");
	info[2] = new JLabel("999");
	info[3] = new JLabel("Upper-Right");
	info[4] = new JLabel("999/999");
	info[5] = new JLabel("999");
	info[6] = new JLabel("012345678");
	info[7] = new JLabel("012345678");
	info[8] = new JLabel("999");
	info[9] = new JLabel("999");
	
	xerxes = new cell();
	
	GroupLayout ccl = new GroupLayout(this);
	ccl.setAutoCreateGaps(false);
	ccl.setAutoCreateContainerGaps(true);
	
	ccl.setHorizontalGroup(
		ccl.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[0])
				.addComponent(info[0]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[1])
				.addComponent(info[1]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[2])
				.addComponent(info[2]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[3])
				.addComponent(info[3]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[4])
				.addComponent(info[4]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[6])
				.addComponent(info[6])
				.addComponent(fields[7])
				.addComponent(info[7]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[5])
				.addComponent(info[5]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[8])
				.addComponent(info[8]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[9])
				.addComponent(info[9]))
				);
		
		ccl.setVerticalGroup(
		ccl.createSequentialGroup()
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[0])
				.addComponent(info[0]))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[1])
				.addComponent(info[1]))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[2])
				.addComponent(info[2]))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[3])
				.addComponent(info[3]))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[4])
				.addComponent(info[4]))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[6])
				.addComponent(info[6])
				.addComponent(fields[7])
				.addComponent(info[7]))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[5])
				.addComponent(info[5]))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[8])
				.addComponent(info[8]))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[9])
				.addComponent(info[9]))
				);
	setLayout(ccl);
	for(int g = 0; g < fields.length; g++){
		fields[g].setVisible(true);
		info[g].setVisible(true);
	}
	}
	
public void init(){
	for(int h = 1; h < fields.length; h++){
		fields[h].setVisible(false);
		info[h].setVisible(false);}
		info[0].setText("");
	}
	
public void setCell(cell hammurabi){
	xerxes = hammurabi;
	refCell();}
	
public void refCell(){
	info[0].setText(xerxes.getName());
	// set labels for Aging
	if(xerxes.getOption("Ages")){fields[1].setVisible(true); info[1].setText(Integer.toString(xerxes.getParameter("Age"))); info[1].setVisible(true);}
	else{fields[1].setVisible(false); info[1].setVisible(false);}
	//set labels for Fade rule
	if(xerxes.getOption("Fades")){fields[2].setVisible(true); info[2].setText(Integer.toString(xerxes.getParameter("Fade"))); info[2].setVisible(true);}
	else{fields[2].setVisible(false); info[2].setVisible(false);}
	//set labels for 4-orientations
	if(xerxes.getControls("Orient") || xerxes.getControls("Dir")){
	if(xerxes.getControls("Orient")){fields[3].setText("Orientation: ");fields[3].setVisible(true); info[3].setText(Integer.toString(xerxes.getParameter("Dir")));
		if(xerxes.getOption("Any")){info[3].setText("Any");}if(xerxes.getOption("All")){info[3].setText("All");}
		info[3].setVisible(true);}
	//set labels for 8-directions
	if(xerxes.getControls("Dir")){fields[3].setText("Direction: ");fields[3].setVisible(true);
		switch(xerxes.getParameter("Dir")){
			case 0 : info[3].setText("Up"); break;
			case 1 : info[3].setText("Upper-Right"); break;
			case 2 : info[3].setText("Right"); break;
			case 3 : info[3].setText("Lower-Right"); break;
			case 4 : info[3].setText("Down"); break;
			case 5 : info[3].setText("Lower-Left"); break;
			case 6 : info[3].setText("Left"); break;
			case 7 : info[3].setText("Upper-Left"); break;
			default: info[3].setText("ERROR"); break;}
			info[3].setVisible(true);}
		}
	else{fields[3].setVisible(false); info[3].setVisible(false);}
	// Labels for Maturity
	if(xerxes.getControls("Mat")){fields[4].setVisible(true); info[4].setText(Integer.toString(xerxes.getParameter("Matcount"))+"/" + Integer.toString(xerxes.getParameter("Mat"))); info[4].setVisible(true);}
	else{fields[4].setVisible(false); info[4].setVisible(false);}
	// labels for MBOT rules
	if(xerxes.getControls("Born")){fields[6].setVisible(true);String bstr = ""; for(int i = 0; i < 9; i++){String[] bbs = new String[]{"B0","B1","B2","B3","B4","B5","B6","B7","B8"};
	if(xerxes.getOption(bbs[i])){bstr = bstr + Integer.toString(i);}}info[6].setText(bstr);info[6].setVisible(true);}
	else{fields[6].setVisible(false); info[6].setVisible(false);}
	if(xerxes.getControls("Survives")){fields[7].setVisible(true);String sstr = ""; for(int v = 0; v < 9; v++){String[] sss = new String[]{"S0","S1","S2","S3","S4","S5","S6","S7","S8"};
	if(xerxes.getOption(sss[v])){sstr = sstr+ Integer.toString(v);}}info[7].setText(sstr);info[7].setVisible(true); }
	else{fields[7].setVisible(false);info[7].setVisible(false);}
	//labels for Wolfram rule
	if(xerxes.getControls("WolfRule")){fields[5].setVisible(true);info[5].setText(Integer.toString(xerxes.getParameter("WolfRule")));info[5].setVisible(true);}
	else{fields[5].setVisible(false);info[5].setVisible(false);}
	//labels for Mirroring
	if(xerxes.getOption("Mirror")){fields[8].setVisible(true); info[8].setText(Integer.toString(xerxes.getParameter("MirrX"))); info[8].setVisible(true); 
		fields[9].setVisible(true); info[9].setText(Integer.toString(xerxes.getParameter("MirrY"))); info[9].setVisible(true);}
	else{fields[8].setVisible(false); info[8].setVisible(false); fields[9].setVisible(false); info[9].setVisible(false);}
	
	}

}
