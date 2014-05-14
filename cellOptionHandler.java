import java.util.Random;
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
public class cellOptionHandler implements ucListener{
cellPicker source;
cell tiamat;//the cell
int celltype = 0;
String mbotname = "Custom";
int maturity = 1;// maturity setting 
int direction = 0;
boolean inver = false;
boolean parity = false;
boolean recursive = false;
int mirrorx = 0;//Mirror x-coordinate
int mirrory = 0;//Mirror y-coordinate
int dir = 0;//direction
int xfactor = 1;//Neighborhood expansion factor
boolean omni = false;//any orientation (symmetriCell)
boolean total = false;//all orientation(symmetriCell)
boolean[] rule = new boolean[8];//wolfram rules
boolean[] bornon = new boolean[9];//born on
boolean[] surv = new boolean[9];// survives on
boolean[] sequence = new boolean[8];
boolean doesage = false;// sets aging
boolean doesfade = false;// sets the fade rule
int fadenum = 0;// maximum age for the fade rule
boolean mirr = false; // mirror setting for cell
int inmod = 1;//the cell's input mode
public cellOptionHandler(){
}

//sets the source for the cell info
public void setCP( cellPicker lorenzo){
	source = lorenzo;
	lorenzo = null;
	}

//handles events from the cell info source
public void handleControl( ucEvent e){
	switch(e.getCommand()){
		case 1: setCT(source.getCT()); break;
		case 2: setMBOT(source.getMBOT()); source.setRULAB(); break;
	}
	e = null;
	}

//option setting methods
private void setCT(int a){
	celltype = a;
	//if(a == 0 || a == 1){source.opts[0].setVisible(true);}
	}
	
private void setMBOT(String nameo){ mbotname = nameo;}	
	


public void setInt(String a, int b){
	if(a == "MirrX"){ mirrorx = b;}
	if(a == "MirrY"){ mirrory = b;}
	if(a == "Dir"){dir = b;}
	if(a == "Fade"){fadenum = b;}
	if(a == "Mat"){maturity = b;}
	if(a == "Xfact"){xfactor = b;}
	if(a == "InMode"){inmod = b;}
}

public void setBool(String a, boolean b){
	if(a == "Ages"){ doesage = b; if(!b){doesfade = false;}}
	if(a == "Fades"){ doesfade = b;if(b){doesage = true;}} 
	if(a == "B0"){ bornon[0] = b;}
	if(a == "B1"){ bornon[1] = b;}
	if(a == "B2"){ bornon[2] = b;}
	if(a == "B3"){ bornon[3] = b;}
	if(a == "B4"){ bornon[4] = b;}
	if(a == "B5"){ bornon[5] = b;}
	if(a == "B6"){ bornon[6] = b;}
	if(a == "B7"){ bornon[7] = b;}
	if(a == "B8"){ bornon[8] = b;}
	if(a == "S0"){ surv[0] = b;} if(a == "W0"){ rule[0] = b;}
	if(a == "S1"){ surv[1] = b;} if(a == "W1"){ rule[1] = b;}
	if(a == "S2"){ surv[2] = b;} if(a == "W2"){ rule[2] = b;}
	if(a == "S3"){ surv[3] = b;} if(a == "W3"){ rule[3] = b;}
	if(a == "S4"){ surv[4] = b;} if(a == "W4"){ rule[4] = b;}
	if(a == "S5"){ surv[5] = b;} if(a == "W5"){ rule[5] = b;}
	if(a == "S6"){ surv[6] = b;} if(a == "W6"){ rule[6] = b;}
	if(a == "S7"){ surv[7] = b;} if(a == "W7"){ rule[7] = b;}
	if(a == "S8"){ surv[8] = b;}
	if(a == "Mirror"){ mirr = b;}
	if(a == "Any"){omni = b;}
	if(a == "All"){total = b;}
}

public void setBoola( String a, boolean[] b){
	if(a == "Seq"){sequence = b;}
	if(a == "Rule"){rule = b;}
}

//option getting methods
public int getCT(){ return celltype;}

public cell getCell(){ return generateCell();}


public cell generateCell(){
	
	if(source == null){celltype = 0;}
	else{setCT(source.getCT());}
	// makes the right type of cell
	switch(celltype){
		case 0: tiamat = new cell();break;
		case 1: tiamat =new wolfram(); setRules(); break;
		case 2:	if(mbotname == "Custom"){tiamat = new mbot(); setRules();}else{tiamat = new mbot(mbotname);}break;
		case 3: tiamat = new randCell(); break;
		case 4: tiamat = new mbot("OnCell"); break;
		case 5: tiamat = new mbot("OffCell"); break;
		case 6: tiamat = new mbot("BlinkCell"); break;
		case 7: tiamat = new symmetriCell(); break;
		case 8: tiamat = new conveyorCell(); break;
		case 9: tiamat = new strobeCell(); break;
		case 10: tiamat = new totalCell(); break;
		case 11: tiamat = new averageCell(); break;
		case 12: if(mbotname == "Custom"){tiamat = new evbot(); setRules();}else{tiamat = new evbot(mbotname);}break;
		case 13: tiamat = new minCell(); break;
		case 14: tiamat = new maxCell(); break;
		default: tiamat = new cell();break;}
		// set options and parameters
		if(tiamat.getControls("Age")){ tiamat.setOption("Ages", doesage);}
		if(tiamat.getControls("Fade")){ tiamat.setOption("Fades", doesfade); tiamat.setParameter("Fade", fadenum);}
		if(tiamat.getControls("Mat")){ tiamat.setParameter("Mat", maturity);}
		if(tiamat.getControls("Orient")){ tiamat.setParameter("Dir", dir);}
		if(tiamat.getControls("Mirror")){  if(mirr){tiamat.setOption("Mirror", mirr); tiamat.setParameter("MirrX", mirrorx); tiamat.setParameter("MirrY", mirrory);}}
		if(tiamat.getControls("All")){ tiamat.setOption("All", total);}
		if(tiamat.getControls("Any")){ tiamat.setOption("Any", omni);}
		if(tiamat.getControls("Dir")){ tiamat.setParameter("Dir", dir);}
		if(tiamat.getControls("Xfact")){ tiamat.setParameter("Xfact", xfactor);}
		if(tiamat.getControls("InMode")){ tiamat.setParameter("InMode", inmod);}
 return tiamat;
}

private void setRules(){
	// sets custom rules for MBOT cells
	if(tiamat.getName() == "M.B.O.T." || tiamat.getName() == "E.V.B.O.T."){ for(int n = 0; n<9; n++){ 
		String bstr = "B0"; String sstr = "S0"; 
		switch(n){
			case 0: bstr = "B0"; sstr = "S0"; break;
			case 1: bstr = "B1"; sstr = "S1"; break;
			case 2: bstr = "B2"; sstr = "S2"; break;
			case 3: bstr = "B3"; sstr = "S3"; break;
			case 4: bstr = "B4"; sstr = "S4"; break;
			case 5: bstr = "B5"; sstr = "S5"; break;
			case 6: bstr = "B6"; sstr = "S6"; break;
			case 7: bstr = "B7"; sstr = "S7"; break;
			case 8: bstr = "B8"; sstr = "S8"; break;
		}
	tiamat.setOption(bstr, bornon[n]);
	tiamat.setOption(sstr, surv[n]);}
									}
									
	if(tiamat.getName() == "Wolfram"){ for(int n = 0; n < 8; n++){
		String rst = "WR0"; 
		switch(n){
			case 0: rst = "WR0"; break;
			case 1: rst = "WR1"; break;
			case 2: rst = "WR2"; break;
			case 3: rst = "WR3"; break;
			case 4: rst = "WR4"; break;
			case 5: rst = "WR5"; break;
			case 6: rst = "WR6"; break;
			case 7: rst = "WR7"; break;
		}
	tiamat.setOption(rst, rule[n]);}
									}
								
		
	}

}

class randcellOptionHandler extends cellOptionHandler{
	int xsiz = 1;
	int ysiz = 1;
	cell tiamat;
	String[] MBOTCell = new String[]{"2x2", "3/4 Life", "Amoeba", "Assimilation", "Coagulations", "Coral", "Day and Night", "Diamoeba", "Dot Life",
"Dry Life", "Fredkin", "Gnarl", "High Life", "Life", "Life without Death", "Live Free or Die", "Long Life", "Maze", "Mazectric",
"Move", "Pseudo-life", "Replicator", "Seeds", "Serviettes", "Stains", "Vote", "Vote 4/5", "Walled Cities"};
	Random shovel = new Random();
	//can not set parameters
	public void setCT(int a){}
	public void setMaturity(int a){}
	public void setDirection(int a){}
	public void setInvert(boolean a){}
	public void setInt( String a, int b){
		if(a == "Xsiz"){xsiz = b;}
		if(a == "Ysiz"){ysiz = b;}
	}
		
	// generate random cells
	public cell generateCell(){
	switch(celltype){
		case 0: tiamat = new cell(); break;
		case 1: tiamat = new wolfram(); 
				for(int ace = 0; ace < 8; ace ++){ tiamat.setRule(ace, shovel.nextBoolean());}
				break;
		case 2: if(shovel.nextInt(10) <= 5){tiamat =new mbot(MBOTCell[shovel.nextInt(MBOTCell.length)]);}
				else{tiamat = new mbot();
				for(int ace = 0; ace <= 9; ace++){
				tiamat.setRule(ace, shovel.nextBoolean()); tiamat.setRule( ace+9, shovel.nextBoolean());}}
				break;
		case 3: tiamat = new randCell(); break;
		case 4: tiamat = new mbot("OnCell"); break;
		case 5: tiamat = new mbot("OffCell"); break;
		case 6: tiamat = new mbot("BlinkCell"); break;
		case 7: tiamat = new symmetriCell();break;
		case 8: tiamat = new conveyorCell(); break;
		case 9: tiamat = new strobeCell(); break;
		case 10: tiamat = new totalCell(); break;
		case 11: tiamat = new averageCell(); break;
		case 12: if(shovel.nextInt(10) <= 5){tiamat =new evbot(MBOTCell[shovel.nextInt(MBOTCell.length)]);}
				else{tiamat = new evbot();
				for(int ace = 0; ace <= 9; ace++){
				tiamat.setRule(ace, shovel.nextBoolean()); tiamat.setRule( ace+9, shovel.nextBoolean());}}
				break;
		case 13: tiamat = new minCell(); break;
		case 14: tiamat = new maxCell(); break;
		default: tiamat = new cell();break;}
		
		// randomly set mirror options
		if(tiamat.getControls("Mirror")){
		if(shovel.nextInt(10) <= 5){tiamat.setOption("Mirror", true); tiamat.setParameter("MirrX", shovel.nextInt(xsiz));
					tiamat.setParameter("MirrY", shovel.nextInt(ysiz));}}
		
		//randomly set maturity
		if(tiamat.getControls("Mat")){			
		tiamat.setParameter("Mat", shovel.nextInt(256)); tiamat.setParameter("Matcount", shovel.nextInt(256));}
		
		//randomly set directional options
		if(tiamat.getControls("Dir") || tiamat.getControls("Orient")){
		tiamat.setParameter("Dir", shovel.nextInt(8)); 
		if(shovel.nextInt(10) > 3){
		tiamat.setOption("All", shovel.nextBoolean()); 
		tiamat.setOption("Any", shovel.nextBoolean());}
		} 
		
		// randomly set aging options
		if(tiamat.getControls("Age")){
		tiamat.setOption("Ages", shovel.nextBoolean());tiamat.setParameter("Age", shovel.nextInt(256));}
		
		// random fade rule
		if(tiamat.getControls("Fade")){
		tiamat.setOption("Fades", shovel.nextBoolean());tiamat.setParameter("Fade", shovel.nextInt(256));}
		
		//Neighborhood Expansion factor
		if(tiamat.getControls("Xfact")){
			if(shovel.nextBoolean()){tiamat.setParameter("Xfact", 1);}else{tiamat.setParameter("Xfact", shovel.nextInt(4)+1);}}
		 
		
		return tiamat;
		}
		
	public int getCT(){ return celltype;}
	
	public cell getCell(){
		int cellgen = shovel.nextInt(1124);
		if(cellgen == 0 ){celltype = 5;}//offcell
		if(cellgen == 1){celltype = 4;}//oncell
		if(cellgen == 2){celltype = 6;}//blinkcell
		if(cellgen > 2 && cellgen < 4){celltype = 9;}//strobe cell
		if(cellgen > 3 && cellgen < 8){celltype = 3;}//randcell
		if(cellgen > 7 && cellgen < 16){celltype = 0;}//cell
		if(cellgen > 15 && cellgen < 32){celltype = 10;}//total cell
		if(cellgen > 31 && cellgen < 64){celltype = 11;}//average cell
		if(cellgen > 63 && cellgen < 128){celltype = 8;}//conveyorcell
		if(cellgen > 127 && cellgen < 256){celltype = 1;}//Wolfram
		if(cellgen > 255 && cellgen < 512){celltype = 7;}//symmetric
		if(cellgen > 511 && cellgen < 768){celltype = 2;}//MBOT
		if(cellgen > 767 && cellgen < 1024){celltype = 12;}//EVBOT
		if(cellgen > 1023 && cellgen < 1074){celltype = 13;}//minCell
		if(cellgen > 1073 && cellgen < 1124){celltype = 14;}//maxCell
		
	
		return generateCell();  }
	


}
