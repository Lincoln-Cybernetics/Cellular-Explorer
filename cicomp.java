import javax.swing.*;

public class cicomp extends JComponent{
JLabel[] fields = new JLabel[10];
JLabel moniker;
JLabel aLabel;
JLabel fLabel;
JLabel dLabel;
JLabel mcLabel;
JLabel mtLabel;
JLabel wrLabel;
JLabel bLabel;
JLabel sLabel;
JLabel mxLabel;
JLabel myLabel;
cell xerxes;

public cicomp(){
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
	moniker = new JLabel();
	aLabel = new JLabel();
	fLabel = new JLabel();
	dLabel = new JLabel();
	mcLabel = new JLabel();
	mtLabel = new JLabel();
	wrLabel = new JLabel();
	bLabel = new JLabel();
	sLabel = new JLabel();
	mxLabel = new JLabel();
	myLabel = new JLabel();
	xerxes = new cell();
	
	GroupLayout ccl = new GroupLayout(this);
	ccl.setAutoCreateGaps(false);
	ccl.setAutoCreateContainerGaps(true);
	
	ccl.setHorizontalGroup(
		ccl.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[0])
				.addComponent(moniker))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[1])
				.addComponent(aLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[2])
				.addComponent(fLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[3])
				.addComponent(dLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[4])
				.addComponent(mcLabel)
				.addComponent(mtLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[6])
				.addComponent(bLabel)
				.addComponent(fields[7])
				.addComponent(sLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[5])
				.addComponent(wrLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[8])
				.addComponent(mxLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[9])
				.addComponent(myLabel))
				);
		
		ccl.setVerticalGroup(
		ccl.createSequentialGroup()
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[0])
				.addComponent(moniker))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[1])
				.addComponent(aLabel))
			.addGroup(ccl.createSequentialGroup()
				.addComponent(fields[2])
				.addComponent(fLabel))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[3])
				.addComponent(dLabel))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[4])
				.addComponent(mcLabel)
				.addComponent(mtLabel))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[6])
				.addComponent(bLabel)
				.addComponent(fields[7])
				.addComponent(sLabel))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[5])
				.addComponent(wrLabel))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[8])
				.addComponent(mxLabel))
			.addGroup(ccl.createParallelGroup()
				.addComponent(fields[9])
				.addComponent(myLabel))
				);
	setLayout(ccl);
	for( JLabel sign : fields){ sign.setVisible(false);} fields[0].setVisible(true);
	}
public void setCell(cell hammurabi){
	xerxes = hammurabi;
	refCell();}
	
public void refCell(){
	moniker.setText(xerxes.getName());
	// set labels for Aging
	if(xerxes.getOption("Ages")){fields[1].setVisible(true); aLabel.setText(Integer.toString(xerxes.getParameter("Age"))); aLabel.setVisible(true);}
	else{fields[1].setVisible(false); aLabel.setVisible(false);}
	//set labels for Fade rule
	if(xerxes.getOption("Fades")){fields[2].setVisible(true); fLabel.setText(Integer.toString(xerxes.getParameter("Fade"))); fLabel.setVisible(true);}
	else{fields[2].setVisible(false); fLabel.setVisible(false);}
	//set labels for 4-orientations
	if(xerxes.getControls("Orient")){fields[3].setText("Orientation: ");fields[3].setVisible(true); dLabel.setText(Integer.toString(xerxes.getParameter("Dir")));
		if(xerxes.getOption("Any")){dLabel.setText("Any");}if(xerxes.getOption("All")){dLabel.setText("All");}
		dLabel.setVisible(true);}else{fields[3].setVisible(false); dLabel.setVisible(false);}
	//set labels for 8-directions
	if(xerxes.getControls("Dir")){fields[3].setText("Direction: ");fields[3].setVisible(true);
		switch(xerxes.getParameter("Dir")){
			case 0 : dLabel.setText("Up"); break;
			case 1 : dLabel.setText("Upper-Right"); break;
			case 2 : dLabel.setText("Right"); break;
			case 3 : dLabel.setText("Lower-Right"); break;
			case 4 : dLabel.setText("Down"); break;
			case 5 : dLabel.setText("Lower-Left"); break;
			case 6 : dLabel.setText("Left"); break;
			case 7 : dLabel.setText("Upper-Left"); break;
			default: dLabel.setText("ERROR"); break;}
			dLabel.setVisible(true);}
	else{fields[3].setVisible(false); dLabel.setVisible(false);}
	// Labels for Maturity
	if(xerxes.getControls("Mat")){fields[4].setVisible(true); mcLabel.setText(Integer.toString(xerxes.getParameter("Matcount"))); mtLabel.setText("/" + Integer.toString(xerxes.getParameter("Mat"))); mcLabel.setVisible(true); mtLabel.setVisible(true);}
	else{fields[4].setVisible(false); mcLabel.setVisible(false); mtLabel.setVisible(false);}
	// labels for MBOT rules
	if(xerxes.getControls("Born")){fields[6].setVisible(true);String bstr = ""; for(int i = 0; i < 9; i++){String[] bbs = new String[]{"B0","B1","B2","B3","B4","B5","B6","B7","B8"};
	if(xerxes.getOption(bbs[i])){bstr = bstr + Integer.toString(i);}}bLabel.setText(bstr);bLabel.setVisible(true);}
	else{fields[6].setVisible(false); bLabel.setVisible(false);}
	if(xerxes.getControls("Survives")){fields[7].setVisible(true);String sstr = ""; for(int v = 0; v < 9; v++){String[] sss = new String[]{"S0","S1","S2","S3","S4","S5","S6","S7","S8"};
	if(xerxes.getOption(sss[v])){sstr = sstr+ Integer.toString(v);}}sLabel.setText(sstr);sLabel.setVisible(true); }
	else{fields[7].setVisible(false);sLabel.setVisible(false);}
	//labels for Wolfram rule
	if(xerxes.getControls("WolfRule")){fields[5].setVisible(true);wrLabel.setText(Integer.toString(xerxes.getParameter("WolfRule")));wrLabel.setVisible(true);}
	else{fields[5].setVisible(false);wrLabel.setVisible(false);}
	//labels for Mirroring
	if(xerxes.getOption("Mirror")){fields[8].setVisible(true); mxLabel.setText(Integer.toString(xerxes.getParameter("MirrX"))); mxLabel.setVisible(true); 
		fields[9].setVisible(true); myLabel.setText(Integer.toString(xerxes.getParameter("MirrY"))); myLabel.setVisible(true);}
	else{fields[8].setVisible(false); mxLabel.setVisible(false); fields[9].setVisible(false); myLabel.setVisible(false);}
	
	}

}
