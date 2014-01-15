import javax.swing.*;

public class cicomp extends JComponent{
JLabel[] fields = new JLabel[8];
JLabel moniker;
JLabel aLabel;
JLabel fLabel;
JLabel dLabel;
JLabel mcLabel;
JLabel mtLabel;
JLabel wrLabel;
JLabel bLabel;
JLabel sLabel;
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
	moniker = new JLabel();
	aLabel = new JLabel();
	fLabel = new JLabel();
	dLabel = new JLabel();
	mcLabel = new JLabel();
	mtLabel = new JLabel();
	wrLabel = new JLabel();
	bLabel = new JLabel();
	sLabel = new JLabel();
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
				);
	setLayout(ccl);
	}
public void setCell(cell hammurabi){
	xerxes = hammurabi;
	refCell();}
	
public void refCell(){
	moniker.setText(xerxes.getName());
	if(xerxes.getOption("Ages")){fields[1].setVisible(true); aLabel.setText(Integer.toString(xerxes.getParameter("Age"))); aLabel.setVisible(true);}
	else{fields[1].setVisible(false); aLabel.setVisible(false);}
	if(xerxes.getOption("Fades")){fields[2].setVisible(true); fLabel.setText(Integer.toString(xerxes.getParameter("Fade"))); fLabel.setVisible(true);}
	else{fields[2].setVisible(false); fLabel.setVisible(false);}
	if(xerxes.getControls("Dir")){fields[3].setVisible(true); dLabel.setText(Integer.toString(xerxes.getParameter("Dir"))); dLabel.setVisible(true);}
	else{fields[3].setVisible(false); dLabel.setVisible(false);}
	if(xerxes.getControls("Mat")){fields[4].setVisible(true); mcLabel.setText(Integer.toString(xerxes.getParameter("Matcount"))); mtLabel.setText("/" + Integer.toString(xerxes.getParameter("Mat"))); mcLabel.setVisible(true); mtLabel.setVisible(true);}
	else{fields[4].setVisible(false); mcLabel.setVisible(false); mtLabel.setVisible(false);}
	if(xerxes.getControls("Born")){fields[6].setVisible(true);String bstr = ""; for(int i = 0; i < 9; i++){String[] bbs = new String[]{"B0","B1","B2","B3","B4","B5","B6","B7","B8"};
	if(xerxes.getOption(bbs[i])){bstr = bstr + Integer.toString(i);}}bLabel.setText(bstr);bLabel.setVisible(true);}
	else{fields[6].setVisible(false); bLabel.setVisible(false);}
	if(xerxes.getControls("Survives")){fields[7].setVisible(true);String sstr = ""; for(int v = 0; v < 9; v++){String[] sss = new String[]{"S0","S1","S2","S3","S4","S5","S6","S7","S8"};
	if(xerxes.getOption(sss[v])){sstr = sstr+ Integer.toString(v);}}sLabel.setText(sstr);sLabel.setVisible(true); }
	else{fields[7].setVisible(false);sLabel.setVisible(false);}
	if(xerxes.getControls("WolfRule")){fields[5].setVisible(true);wrLabel.setText(Integer.toString(xerxes.getParameter("WolfRule")));wrLabel.setVisible(true);}
	else{fields[5].setVisible(false);wrLabel.setVisible(false);}
	
	}

}
