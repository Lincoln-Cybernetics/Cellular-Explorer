import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class editEngine extends logicEngine implements ucListener, MouseInputListener{
int xlocal = 0;// used for mouse events
int ylocal = 0;// used for mouse events
int xmax;//x-dimension size of the automaton
int ymax;// y-dimension size of the automaton
int xaut;// x-dimension size of automaton array
int yaut;// y-dimension size of automaton array
int xaw; //working xaut
int yaw; // working y aut
int zt = 500;// speed setting
brush sigmund;// editing brush
int brushtype;
String maction = "None";
String remaction = "None";
int mode = 1;// general operating mode
int dmode = 1;// display mode
cellOptionHandler castor;
cellOptionHandler pollux;
cellOptionHandler eris;
boolean prisec = true;//shunts settings info to castor(true) or pollux(false)
int[] mirrefflag = new int[]{0,0,0,0};//mirror reference state
int[] mirrefx = new int[]{0,0,0,0};//mirror reference x
int[] mirrefy = new int[]{0,0,0,0};//mirror reference y
int[] anchorx = new int[]{0,0,0,0};//anchor coordinate x for mirrors using the reference
int[] anchory = new int[]{0,0,0,0};//anchor coordinate y for mirrors using the reference
JFrame disp; // window for the automaton
selector sedna;
//parameter names
String[] parameters = new String[]{"CDO", "CFO", "SDO", "SFO","WSX","WSY","WH", "SDT", "SFT", "CDT", "CFT", "SDP", "SFP", "CDP", "CFP"};
int sdo = 0; //state drawing option
boolean interactflag = false;
int sfo = 0; //state fill option
int cdo = 0; //cell drawing option
int cfo = 0; //cell fill option
int sdt = 0;//state draw tool
int sft = 0;//state fill tool
int cdt = 0;//cell draw tool
int cft = 0;//cell fill tool
int[] paramval = new int[4];// parameter tool values(0= StateDdraw, 1 = StateFill, 2 = CellDraw, 3 = CellFill)
boolean[] opval = new boolean[4];//option tool values (0= StateDdraw, 1 = StateFill, 2 = CellDraw, 3 = CellFill)
String[] toolstring = new String[4];//tool string (0= StateDdraw, 1 = StateFill, 2 = CellDraw, 3 = CellFill)
boolean cellfillflag = false;
boolean statefillflag = false;
//String[] cellopts = new String[]{"Ages", "Fades"};// for cell editing
//String[] cellparams = new String[]{"Age", "Fade", "Mat", "Matcount"};// for cell/state editing
int wsx = 0; //window start: X
int wsy = 0; //window start: Y
int wh = 0; // window height
boolean rcflag = false;//right click flag
boolean rectflag = false;//flag for making rectangles
cicomp mercury; JFrame barnabus;

public  editEngine(){
	xaut = 1;
	yaut = 1;
	pistons = new cellBrain[xaut][yaut];
	outputs = new cellComponent[xaut][yaut];
	sigmund = new onebrush();
	brushtype = 1;
	castor = new cellOptionHandler();
	pollux = new cellOptionHandler();
	eris = new randcellOptionHandler();
	sedna = new selector();
	}
	
public editEngine(int cbx, int cby){
	xaut = cbx;
	yaut = cby;
	pistons = new cellBrain[xaut][yaut];
	outputs = new cellComponent[xaut][yaut];
	sigmund = new brush();
	brushtype = 1;
	castor = new cellOptionHandler();
	pollux = new cellOptionHandler();
	eris = new randcellOptionHandler();
	sedna = new selector();
}
	
public void initialize(int xmx, int ymx){
	xmax = xmx; ymax = ymx; 
	for(int y = 0; y <= yaut-1; y++){
		for(int x = 0; x <= xaut-1; x++){
	pistons[x][y] = new cellBrain(xmx,ymx, this);
	 outputs[x][y] = new cellComponent(xmx,ymx);
	  outputs[x][y].addMouseMotionListener(this);	
		outputs[x][y].addMouseListener(this); 
		pistons[x][y].setDisplay(outputs[x][y]); 
		pistons[x][y].initBoard();
		pistons[x][y].pP();
		}}
							setEditBrush(1);
						  disp = new JFrame("Cellular Explorer");
						  disp.getContentPane().add(new JScrollPane(outputs[0][0]));
						  disp.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
						  disp.setSize(800,wh);
						  disp.setLocation(wsx,wsy);
						  disp.setResizable(true);
						  disp.setVisible(true);
						  eris.setInt("Xsiz", xmx);
						  eris.setInt("Ysiz", ymx);
						  sedna = new selector(xmx,ymx);
						  setMode(1);
						  
						}

public void handleControl(ucEvent e){
	if(e.getSource() == castor.source){
		switch(e.getCommand()){
			case 3: setMouseAction("Mirsel"); prisec = true; mirrefflag[1] = 0; break;
			case 4: mirrefflag[1] = 1; prisec = true; setMouseAction("Mirsel");break;
		}
		}
	if(e.getSource() == pollux.source){
		switch(e.getCommand()){
			case 3: setMouseAction("Mirsel"); prisec = false; mirrefflag[1] = 0; break;
			case 4: mirrefflag[1] = 1; prisec = false; setMouseAction("Mirsel");break;
		}
		} 
	}
	
	//start/stop the automaton
	public void playPause(){
		if(!pistons[0][0].mayIterate()){pistons[0][0].setOpMode(pistons[0][0].getDispType());
		outputs[0][0].setDispMode(pistons[0][0].getDispType());}
								pistons[0][0].pP(); }
		
	// step forward one generation						
	public void step(int a){
		for (int n = 0; n < a; n++){
			pistons[0][0].iterate();}
		}

	// recieves a signal from an interrupted cellBrain
	public void iterateInterrupt(int a){if (a == 1){stateFillSelect();} if(a == 2){fillCell();}}
	
	//speed settings
	public void setMasterSpeed(int a){
		pistons[0][0].setZT(a); zt = a;}
		
	public int getMasterSpeed(){
		return zt;}
	
	// mouse methods	
	public void setMouseAction(String action){
		if(action == "Mirsel" || action == "SRect"){remaction = maction;}
		maction = action;
	/*mouse actions:
	 * "BSel" select with the brush
	 * "CDraw" cell draw
	 * "SDraw" state draw
	 * "SRect" select rectangle
	 * "SRaction"  selecting rectangle
	 * "Mirsel" select mirror cell target
	 * "None" none
	 */
	}
	
	public boolean isMouseUsed(){
		if (maction == "SRect"){return false;}
		if (maction == "SRaction"){return false;}
		if (maction == "Mirsel"){return false;}
		if (maction == "None"){return false;}
		return true;}
	
	// edit options
	
	 
	public void setInteract(boolean b){ interactflag = b;}
	
	public boolean getInteract(){ return interactflag;}
	 
	
	 
	 //set parameters
	 public void setParameter(String pname, int a ){
		 int j = 0;
		 for(int n = 0; n < parameters.length; n++){ if(pname == parameters[n]){ j = n; break;}}
		 switch(j){
			 /* General Editing Tool Options
		 * 0 = Regular fill 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 * 4 = Clear state (sfo only)
		 * 5 = Invert state (sfo only)
		 */
			 case 0: cdo = a; break;
			 case 1: cfo = a; break;
			 case 2: sdo = a; break;
			 case 3: sfo = a; break;
			 
			 //Automaton window setup 
			 case 4: wsx = a; break;
			 case 5: wsy = a; break;
			 case 6: wh = a; break;
			 
			 /*Tools
			  * 0 = default
			  * 1 = option tool
			  * 2 = parameter tool
			  */
			  case 7: sdt = a; break;
			  case 8: sft = a; break;
			  case 9: cdt = a; break;
			  case 10: cft = a; break;
			  
			  //Parameter Tool values (0= state draw, 1 = state fill, 2 = cell draw, 3 = cell fil)
			  case 11: paramval[0] = a; break;
			  case 12: paramval[1] = a; break;
			  case 13: paramval[2] = a; break;
			  case 14: paramval[3] = a; break;
		 }
		 
	 }
	 //sets editing tools
	 public void setTool(int type, int tool, String tstr, int tval){
		 //(0= state draw, 1 = state fill, 2 = cell draw, 3 = cell fil)
		 switch(type){
			 case 0: sdt = tool; toolstring[0] = tstr; break;
			 case 1: sft = tool; toolstring[1] = tstr; break;
			 case 2: cdt = tool; toolstring[2] = tstr; break;
			 case 3: cft = tool; toolstring[3] = tstr; break;
		 }
		  /*Tools
			  * 0 = default
			  * 1 = option tool
			  * 2 = parameter tool
			  */
		 switch(tool){
			 case 0: break;
			 case 1: if(tval < 1){opval[type] = false;}else{opval[type] = true;}break;
			 case 2: paramval[type] = tval; break;
		 }
	 }
	 
	 //generates random values for parameter tools
	 private int randToolNum(int a){
		 Random sprue = new Random();
		 if(toolstring[a] == "Dir"){ return sprue.nextInt(8);}
		 if(toolstring[a] == "Fade"){ return sprue.nextInt(1024)+1;}
		 return sprue.nextInt(512)+1;
	 }
	 
	 // show cell info
	 public void showCI(){
		// barnabus.setVisible(false);
		 if(barnabus == null){
		 mercury = new cicomp();
		 barnabus = new JFrame("Cell Info");
		 barnabus.getContentPane().add(mercury);
		 barnabus.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		 barnabus.pack();
		 barnabus.setLocation(675,0);
		 barnabus.setResizable(false);
		 mercury.init();
		 barnabus.setVisible(true);
		 barnabus.toFront();
		 mercury.setVisible(true);}
		 else{barnabus.toFront();barnabus.setVisible(true);}
		 
		 }
	
	//Each time the automaton iterates, this method is called at the end of the iteration 
	public void iterateNotify(){ 
		if(mercury != null){mercury.refCell();}
	}
	
	//general mode setting for display and automaton
	public void setMode(int m){
		/*Modes:
		 * 0 = revert to previously selected display mode
		 * 1 = Normal Running
		 * 2 = State Editing
		 * 3 = Cell Editing
		 * 4 = Multicolor
		 */
		 switch(m){
			 case 0: if(maction == "SDraw" && interactflag){}else{setMouseAction("None");}
						mode = dmode; pistons[0][0].setOpMode(dmode); outputs[0][0].setDispMode(dmode);  break;
						
			 case 1: if(maction == "SDraw" && interactflag){}else{setMouseAction("None");}
						mode = 1; dmode = 1; pistons[0][0].setOpMode(1); outputs[0][0].setDispMode(1);  break;
						
			 case 2: setMouseAction("None");mode = 2; pistons[0][0].setOpMode(2); outputs[0][0].setDispMode(2);  break;
			 
			 case 3: setMouseAction("None");mode = 3; pistons[0][0].setOpMode(3); outputs[0][0].setDispMode(3);  break;
			 
			 case 4: if(maction == "SDraw" && interactflag){}else{setMouseAction("None");}
						mode = 4; dmode = 4; pistons[0][0].setOpMode(4); outputs[0][0].setDispMode(4);  break;
						
			 default:  setMouseAction("None"); mode = dmode; pistons[0][0].setOpMode(dmode); outputs[0][0].setDispMode(dmode);  break;
		 }
		}
		
		public int getMode(){return mode;}
		
		// sets display type
		public void setDisplayMode(int a){
			switch(a){
				case 1: dmode = 1; break;
				case 4: dmode = 4; break;
				default: dmode = 1; break;
			}
		}
		
		//sets automaton topology
		public void setWrap(int a){
			switch(a){
				case 0: pistons[0][0].setXYwrap(false, false); break;
				case 1: pistons[0][0].setXYwrap(true, false); break;
				case 2: pistons[0][0].setXYwrap(false, true); break;
				case 3: pistons[0][0].setXYwrap(true, true); break;
			}
		}
		
		//Checks for out of bounds points, one axis at a time
		//Edge-wrap does not apply for editing
		private int checkPoint(String axis,int value){
			if(axis == "X"){if (value >= xmax){return -1;}
							if (value < 0){ return -1;}
							return value;}
							
			if(axis == "Y"){if (value >= ymax){return -1;}
							if (value < 0){ return -1;}
							return value;}
						return -1;	
						}
			
		
		//Sets automaton-level rules
		public void setAutomatonRule(int rn, boolean b){
			pistons[0][0].setRule(rn, b);}
			
		public void setAutomatonRuleTimer(int rn, int t){
			pistons[0][0].setRuleTimer(rn, t);}

	// refreshes the selection in the display
					public void refreshSel(){
						sedna.detectSelection();
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){
							outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));
							}}
							outputs[0][0].repaint();
						}
						
						
//All about brushes

							// brush setting method
					public void setEditBrush(int b){
						brushtype = b;
						switch(brushtype){
							// 1x1
						case 1: sigmund = new onebrush(); break;
							//2x2
						case 2: sigmund = new twobrush(); break;
							//3x3
						case 3: sigmund = new threebrush(); break;
							//Glider
						case 4: sigmund = new gliderbrush(); sigmund.setOrientation(0); break;
							//R-pentomino
						case 5: sigmund = new rpentbrush(); sigmund.setOrientation(0); break;
						
						default : sigmund = new onebrush(); break;}
					}
					
					//draws the brush onto the display
					private void drawBrush(int x, int y){
							sigmund.locate(x, y);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = checkPoint("X", sigmund.getNextX());
						int thisy = checkPoint("Y", sigmund.getNextY());
						if(thisx == -1 || thisy == -1){}
						else{
						if(!sedna.getSelected() ||sedna.getSelection(thisx,thisy)){
						outputs[xaw][yaw].setHiLite(thisx, thisy, 5);}
						}}
						outputs[xaw][yaw].repaint();
					}
					
					// applies the brush to a point in the automaton
					public void applyBrush(int x, int y){
						sigmund.locate(x, y);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = checkPoint("X", sigmund.getNextX());
						int thisy = checkPoint("Y", sigmund.getNextY());
						if(thisx == -1 || thisy == -1){}
						else{
						
						//edit state
						if(maction == "SDraw"){
						if(mode == 2){
								drawState(thisx, thisy);
							}
						if(mode != 3 && interactflag){
							drawState(thisx, thisy);
						}
							}
						
						//editcell
						if(maction == "CDraw"){
						if (mode == 3 ){
							drawCell(thisx, thisy);
							}
						}
							
							// cell selection
						if(maction == "BSel"){
							outputs[xaw][yaw].setSelect(true);
							 if(rcflag){sedna.removeCell(thisx,thisy);}
							 else{sedna.selectCell(thisx,thisy);}
						}
					}
					}
						
						refreshSel();
		
	}
					
					//makes the cells
				public void populate(int a, int b, int c){
					cell ed;
					if(sedna.getSelected() == false || sedna.getSelection(a,b)){
					cellOptionHandler decider;
					switch(c){
						case 1: decider = castor;break;
						case 2: decider = pollux;break;
						case 3: decider = eris; break;
						default: decider = castor;break;}
				
							ed = decider.getCell();	
								
								// set mirror reference anchor, and cell mirror coordinates
								
								if(mirrefflag[c] == 2 && ed.getOption("Mirror")){
									anchorx[c] = a; ed.setParameter("MirrX", a);
									anchory[c] = b; ed.setParameter("MirrY", b);
									mirrefflag[c] = 3;}
								//set mirror coordinates 
								if(mirrefflag[c] == 3 && ed.getOption("Mirror")){
									int myx = anchorx[c] - a; int myy = anchory[c] - b;
									myx = mirrefx[c]-myx; myy = mirrefy[c]-myy;
									if(myx > xmax-1){if(pistons[xaw][yaw].getWrap("X")){myx = myx-xmax;}else{myx = xmax-1;}} 
									if(myx < 0){if(pistons[xaw][yaw].getWrap("X")){ myx = myx+xmax;}else{myx = 0;}}
									if(myy > ymax-1){if(pistons[xaw][yaw].getWrap("Y")){myy = myy-ymax;}else{myy = ymax-1;}}
									if(myy < 0){if(pistons[xaw][yaw].getWrap("Y")){myy = myy+ymax;}else{myy = 0;}}
									ed.setParameter("MirrX",myx); ed.setParameter("MirrY",myy);}
								
								pistons[xaw][yaw].addCell(a,b,ed);
						outputs[xaw][yaw].setSpecies(a,b,decider.getCT());
						//outputs[xaw][yaw].setLifespan(a,b,ed.getParameter("Mat")); 
						
					}}
					
					
					// cell drawing methods
					
					//gateway method
					public void drawCell(int x, int y){
						/* Cell Drawing Option
						* 0 = Regular drawing 
						* 1 = Checker Board pattern
						* 2 = Randomized
						* 3 = Randomized/Checker Board
						*/
						if(rcflag){if(cdo == 1){ cellCheckDraw(x,y, true);}else{cellAltDraw(x,y);}}
						else{
							switch(cdo){
								case 0: cellDraw(x,y); break;
								case 1: cellCheckDraw(x,y, false); break;
								case 2: cellRandDraw(x,y); break;
								case 3: cellRCDraw(x,y); break;
								default: cellDraw(x,y); break;}}
						}
					
					//Main draws
					public void cellDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){
							if(cellfillflag){
								switch(cft){
									case 0: populate(x,y,1); break;
									case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[3], opval[3]); break;
									case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[3], paramval[3]); break;
									default: populate(x,y,1); break;}
								}
							else{ switch(cdt){
									case 0: populate(x,y,1);break;
									case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[2], opval[2]); break;
									case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[2], paramval[2]); break;
									default: populate(x,y,1); break;}
								}
									}}
						
					public void cellAltDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){
							if(cellfillflag){
								switch(cft){
									case 0: populate(x,y,2); break;
									case 1: //pistons[xaw][yaw].culture[x][y].setOption(toolstring[3], opval[3]);
									 break;
									case 2: //pistons[xaw][yaw].culture[x][y].setParameter(toolstring[3], paramval[3]);
									 break;
									default: populate(x,y,2); break;}
								}
							else{ switch(cdt){
									case 0: populate(x,y,2);break;
									case 1: //pistons[xaw][yaw].culture[x][y].setOption(toolstring[2], opval[2]); 
									break;
									case 2: //pistons[xaw][yaw].culture[x][y].setParameter(toolstring[2], paramval[2]);
									 break;
									default: populate(x,y,2); break;}
								}
							}} 
						
					public void cellRandDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){
							Random snail = new Random();
							if(cellfillflag){
								int nose = randToolNum(3);
								switch(cft){
									case 0: populate(x,y,3); break;
									case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[3], snail.nextBoolean());
									 break;
									case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[3], nose);
									 break;
									default: populate(x,y,3); break;}
								}
							else{ 
								int elbow = randToolNum(2);
								switch(cdt){
									case 0: populate(x,y,2);break;
									case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[2], snail.nextBoolean()); 
									break;
									case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[2], elbow);
									 break;
									default: populate(x,y,3); break;}
								}
								}
					}
					
					//calculated draws
					public void cellCheckDraw(int x, int y, boolean b){
						if( y % 2 == 1 ^ x % 2 == 1){
							if(b){if(!sedna.getSelected() ||sedna.getSelection(x,y)){cellAltDraw(x,y);}}
							else{if(!sedna.getSelected() ||sedna.getSelection(x,y)){cellDraw(x,y);}}
							}
						else{
							if(b){if(!sedna.getSelected() ||sedna.getSelection(x,y)){cellDraw(x,y);}}
							else{if(!sedna.getSelected() ||sedna.getSelection(x,y)){cellAltDraw(x,y);}}
							}
						}
						
					public void cellRCDraw(int x, int y){
						if( y % 2 == 1 ^ x % 2 == 1){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){cellDraw(x,y);}}
						else{
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){cellRandDraw(x,y);}}
						}
						
					
						
					// cell filling methods
					public void fillCellinit(){
						if(pistons[xaw][yaw].paused){fillCell();}
						else{ pistons[xaw][yaw].setII(2);}
					}
					
					// gateway method
					public void fillCell(){
						cellfillflag = true;
						for(int y=0;y<= pistons[xaw][yaw].ysiz-1;y++){
						for(int x=0;x<= pistons[xaw][yaw].xsiz-1;x++){
						switch(cfo){
							case 0: cellDraw(x,y); break;
							case 1: cellCheckDraw(x,y,false); break;
							case 2: cellRandDraw(x,y); break;
							case 3: cellRCDraw(x,y); break;
							default: cellDraw(x,y); break;}
						}}
						if( mode == 3){outputs[xaw][yaw].repaint();}
						cellfillflag = false;
							}
						
					
					
						
						// sets the cells around the outside edge of the automaton 
						public void setBorder(){	
							cellfillflag = true;
						for(int x = 0; x <= pistons[xaw][yaw].xsiz-1; x++){
							switch(cfo){
								case 0: cellDraw(x,0); cellDraw(x, pistons[xaw][yaw].ysiz-1); break;
								case 1: cellCheckDraw(x,0, false); cellCheckDraw(x,pistons[xaw][yaw].ysiz-1, false); break;
								case 2: cellRandDraw(x,0); cellRandDraw(x, pistons[xaw][yaw].ysiz-1); break;
								case 3: cellRCDraw(x,0); cellRCDraw(x, pistons[xaw][yaw].ysiz-1);break;
								default: cellDraw(x,0); cellDraw(x, pistons[xaw][yaw].ysiz-1); break;}}
						for(int y = 0; y<= pistons[xaw][yaw].ysiz-1; y++){
							switch(cfo){
								case 0: cellDraw(0,y); cellDraw(pistons[xaw][yaw].xsiz-1, y); break;
								case 1: cellCheckDraw(0,y, false); cellCheckDraw(pistons[xaw][yaw].xsiz-1,y, false); break;
								case 2: cellRandDraw(0,y); cellRandDraw(pistons[xaw][yaw].xsiz-1, y); break;
								case 3: cellRCDraw(0,y); cellRCDraw(pistons[xaw][yaw].xsiz-1, y); break;
								default: cellDraw(0,y); cellDraw(pistons[xaw][yaw].xsiz-1, y); break;}}
								if(mode == 3){outputs[xaw][yaw].repaint();}
								cellfillflag = false;
					}
					
					
								
	//State Editing Methods
	
						//state drawing methods
						
							//gateway method
				public void drawState(int x, int y){
						/* State Drawing Option
						* 0 = Regular drawing 
						* 1 = Checker Board pattern
						* 2 = Randomized
						* 3 = Randomized/Checker Board
						*/
						if(rcflag){if(sdo == 1){ stateCheckDraw(x,y, false);}else{stateAltDraw(x,y);}}
						else{
							switch(sdo){
								case 0: stateDraw(x,y); break;
								case 1: stateCheckDraw(x,y, true); break;
								case 2: stateRandDraw(x,y); break;
								case 3: stateRCDraw(x,y); break;
								default: stateDraw(x,y); break;}}
						}
						
				//main draws	
				private void stateDraw(int x,int y){
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){
						if(statefillflag){
							switch(sft){
								case 0: pistons[xaw][yaw].setCellState(x,y,true); break;
								case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[1], opval[1]); break;
								case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[1], paramval[1]); break;
								default: pistons[xaw][yaw].setCellState(x,y,true); break;}
							}
							else{
								switch(sdt){
									case 0: pistons[xaw][yaw].setCellState(x,y,true); break;
									case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[0], opval[0]); break;
									case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[0], paramval[0]); break;
									default: pistons[xaw][yaw].setCellState(x,y,true); break;}
								}
							}
						}
						
						
						
					
				private void stateAltDraw(int x,int y){
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){
						if(statefillflag){
							switch(sft){
								case 0: pistons[xaw][yaw].setCellState(x,y,false); break;
								case 1: //pistons[xaw][yaw].culture[x][y].setOption(toolstring[1], opval[1]);
								 break;
								case 2: //pistons[xaw][yaw].culture[x][y].setParameter(toolstring[1], paramval[1]);
								 break;
								default: pistons[xaw][yaw].setCellState(x,y,false); break;}
							}
							else{
								switch(sdt){
									case 0: pistons[xaw][yaw].setCellState(x,y,false); break;
									case 1: //pistons[xaw][yaw].culture[x][y].setOption(toolstring[0], opval[0]);
									 break;
									case 2: //pistons[xaw][yaw].culture[x][y].setParameter(toolstring[0], paramval[0]);
									 break;
									 default: pistons[xaw][yaw].setCellState(x,y,false); break;
								}
							}
						}}
					
				private void stateRandDraw(int x, int y){
					Random foghorn = new Random();
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){
						int dunebuggy = randToolNum(1);
						if(statefillflag){
							switch(sft){
								case 0: pistons[xaw][yaw].setCellState(x,y,foghorn.nextBoolean()); break;
								case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[1], foghorn.nextBoolean());
								 break;
								case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[1], dunebuggy);
								 break;
								default: pistons[xaw][yaw].setCellState(x,y,false); break;}
							}
							else{
								int pickaxe = randToolNum(0);
								switch(sdt){
									case 0: pistons[xaw][yaw].setCellState(x,y,foghorn.nextBoolean()); break;
									case 1: pistons[xaw][yaw].culture[x][y].setOption(toolstring[0], foghorn.nextBoolean());
									 break;
									case 2: pistons[xaw][yaw].culture[x][y].setParameter(toolstring[0], pickaxe);
									 break;
									 default: pistons[xaw][yaw].setCellState(x,y,foghorn.nextBoolean()); break;
								}
							}
						}
					}	
					
				//calculated draws
				public void stateCheckDraw(int x,int y, boolean fill){
					if( y % 2 == 1 ^ x % 2 == 1){ if(!sedna.getSelected() ||sedna.getSelection(x,y)){if(fill){stateDraw(x,y);}else{stateAltDraw(x,y);}}}
						else{if(fill){if(!sedna.getSelected() ||sedna.getSelection(x,y)){stateAltDraw(x,y);}}}}
					
				private void stateRCDraw(int x, int y){
						if( y % 2 == 1 || x % 2 == 1){stateCheckDraw(x,y,true);}
						else{stateRandDraw(x,y);}}
								
				//state fill methods
				
				//gateway method
				public void fillState(){
					if(pistons[xaw][yaw].paused){stateFillSelect();}
					else{ pistons[xaw][yaw].setII(1);}
					}
				
					
					//selects the right state fill to do
					public void stateFillSelect(){
						/* State Fill Option
						* 0 = Regular fill 
						* 1 = Checker Board pattern
						* 2 = Randomized
						* 3 = Randomized/Checker Board
						* 4 = Clear state
						* 5 = Invert state
						*/
					statefillflag = true;
						switch(sfo){
							case 0: stateFill(); break;
							case 1: stateCheckFill(); break;
							case 2: stateRandFill(); break;
							case 3: stateRCFill(); break;
							case 4: stateClearFill(); break;
							case 5: stateInvert(); break;
							
							default: stateCheckFill(); break;}
							statefillflag = false;
						}
					
				private void stateFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						stateDraw(x,y);}}
						if(mode != 3){pistons[xaw][yaw].refreshState();}
					} 
						
				private void stateRandFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						stateRandDraw(x,y);
					}} 
					if( mode != 3){ pistons[xaw][yaw].refreshState();}
					}
					
				private void stateClearFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						pistons[xaw][yaw].current[x][y] = false; pistons[xaw][yaw].culture[x][y].purgeState();}}
						if(mode != 3){pistons[xaw][yaw].refreshState();}
						}
					
				private void stateCheckFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
					stateCheckDraw(x,y, true);
					}} 
					if(mode != 3){pistons[xaw][yaw].refreshState();}
					}
					
				private void stateRCFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						if( y % 2 == 1 || x % 2 == 1){stateCheckDraw(x,y,true);}
						else{stateRandDraw(x,y);}}}
						if(mode != 3){pistons[xaw][yaw].refreshState();}
					}
				
				private void stateInvert(){
					for(int y = 0; y<= ymax-1; y++){
						for(int x=0; x<= xmax-1; x++){
							if(!sedna.getSelected() ||sedna.getSelection(x,y)){
								pistons[xaw][yaw].setCellState(x,y,!pistons[xaw][yaw].current[x][y]);}}}
					if(mode != 3){pistons[xaw][yaw].refreshState();}
				}		
				
			
						
			

	// mouse interaction methods
					private void setWorkAut(MouseEvent o){
						for(int y = 0; y <= yaut-1; y++){
							for(int x = 0; x <= xaut-1; x++){
								if(o.getSource() == outputs[x][y]){
									xaw = x; yaw = y;}
								}}
							}
							
					
	
					public void mouseMoved( MouseEvent e){
						setWorkAut(e);
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/outputs[xaw][yaw].magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/outputs[xaw][yaw].magnify;}
						if (xlocal > pistons[xaw][yaw].xsiz-1){xlocal = pistons[xaw][yaw].xsiz-1;}
						if (ylocal > pistons[xaw][yaw].ysiz-1){ylocal = pistons[xaw][yaw].ysiz-1;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[xaw][yaw].culture[xlocal][ylocal]);}
						outputs[xaw][yaw].remHilite();
						if(isMouseUsed()){
							drawBrush(xlocal,ylocal);
					}
						 // draws rectangle during rectangle selection
						 if(maction == "SRaction"){for(int y = 0; y <= pistons[xaw][yaw].ysiz-1; y++){
												for(int x = 0; x <= pistons[xaw][yaw].xsiz-1; x++){
													outputs[xaw][yaw].setSelection(x,y,sedna.getSelection(x,y));}}
							 outputs[xaw][yaw].finishRect(xlocal,ylocal); }
							 
						 if(mode == 3){
							 if(pistons[xaw][yaw].culture[xlocal][ylocal].getOption("Mirror")){
							 outputs[xaw][yaw].setHiLite(pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrX"), 
							 pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
						
					public void mouseDragged(MouseEvent e) {
						setWorkAut(e);
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/outputs[xaw][yaw].magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/outputs[xaw][yaw].magnify;}
						if (xlocal > pistons[xaw][yaw].xsiz-1){xlocal = pistons[xaw][yaw].xsiz-1;}
						if (ylocal > pistons[xaw][yaw].ysiz-1){ylocal = pistons[xaw][yaw].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[xaw][yaw].culture[xlocal][ylocal]);}
						outputs[xaw][yaw].remHilite();
						if(isMouseUsed()){
							drawBrush(xlocal,ylocal);
						applyBrush(xlocal, ylocal);
					}
					else{
						 //  rectangle selection
						 if(maction == "SRaction"){
						 for(int y = 0; y <= pistons[xaw][yaw].ysiz-1; y++){
							for(int x = 0; x <= pistons[xaw][yaw].xsiz-1; x++){
								outputs[xaw][yaw].setSelection(x,y,sedna.getSelection(x,y));}}
							 outputs[xaw][yaw].finishRect(xlocal,ylocal); }}
							 
							  if(mode == 3){
							 if(pistons[xaw][yaw].culture[xlocal][ylocal].getOption("Mirror")){
							 outputs[xaw][yaw].setHiLite(pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrX"), 
							 pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrY"), 4);
								}
							}
							 
							}
							
					public void mouseEntered(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[xaw][yaw].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[xaw][yaw].magnify;}
						if (xlocal > pistons[xaw][yaw].xsiz-1){xlocal = pistons[xaw][yaw].xsiz-1;}
						if (ylocal > pistons[xaw][yaw].ysiz-1){ylocal = pistons[xaw][yaw].ysiz-1;}
						outputs[xaw][yaw].remHilite();
						if(isMouseUsed()){
							drawBrush(xlocal,ylocal);
						}
						
						 if(mode == 3){
							 if(pistons[xaw][yaw].culture[xlocal][ylocal].getOption("Mirror")){
							 outputs[xaw][yaw].setHiLite(pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrX"), 
							 pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
					
					public void mouseExited(MouseEvent e){
						outputs[xaw][yaw].remHilite();
						}
					
					public void mousePressed(MouseEvent e){
						setWorkAut(e);
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[xaw][yaw].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[xaw][yaw].magnify;}
						if (xlocal > pistons[xaw][yaw].xsiz-1){xlocal = pistons[xaw][yaw].xsiz-1;}
						if (ylocal > pistons[xaw][yaw].ysiz-1){ylocal = pistons[xaw][yaw].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[xaw][yaw].culture[xlocal][ylocal]);}
						
						if(isMouseUsed()){
							//outputs[xaw][yaw].remHilite();
							drawBrush(xlocal,ylocal);
						applyBrush(xlocal, ylocal);
					}
					else{
						
						//rectangle selection
						if(maction == "SRect"){
						sedna.startRect(xlocal,ylocal, !rcflag);
						 outputs[xaw][yaw].remHilite();outputs[xaw][yaw].beginRect(xlocal,ylocal,!rcflag); maction = "SRaction";}
						 
					 }
					 
					  if(mode == 3){
							 if(pistons[xaw][yaw].culture[xlocal][ylocal].getOption("Mirror")){
							 outputs[xaw][yaw].setHiLite(pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrX"), 
							 pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
					
					public void mouseReleased(MouseEvent e){
						setWorkAut(e);
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[xaw][yaw].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[xaw][yaw].magnify;}
						if (xlocal > pistons[xaw][yaw].xsiz-1){xlocal = pistons[xaw][yaw].xsiz-1;}
						if (ylocal > pistons[xaw][yaw].ysiz-1){ylocal = pistons[xaw][yaw].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[xaw][yaw].culture[xlocal][ylocal]);}
						
						if(isMouseUsed()){ 
							//outputs[xaw][yaw].remHilite();
							drawBrush(xlocal,ylocal);
							//no action taken
						}
					else{
						//rectangle selection
						if(maction == "SRaction"){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= pistons[xaw][yaw].ysiz-1; y++){
							for(int x = 0; x<= pistons[xaw][yaw].xsiz-1; x++){
								outputs[xaw][yaw].setSelection(x,y,sedna.getSelection(x,y));}}
								outputs[xaw][yaw].repaint(); sedna.detectSelection();
								/*switch(mode){
									case 1: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									case 2: setMouseAction("SDraw"); break;
									case 3: setMouseAction("CDraw"); break;
									case 4: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									default: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;}*/
									setMouseAction(remaction); remaction = "None";
								}
							}
							
							 if(mode == 3){
							 if(pistons[xaw][yaw].culture[xlocal][ylocal].getOption("Mirror")){
							 outputs[xaw][yaw].setHiLite(pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrX"), 
							 pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
						
					
					public void mouseClicked(MouseEvent e){
						setWorkAut(e);
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[xaw][yaw].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[xaw][yaw].magnify;}
						if (xlocal > pistons[xaw][yaw].xsiz-1){xlocal = pistons[xaw][yaw].xsiz-1;}
						if (ylocal > pistons[xaw][yaw].ysiz-1){ylocal = pistons[xaw][yaw].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[xaw][yaw].culture[xlocal][ylocal]);}
						//
						if(isMouseUsed()){
							//outputs[xaw][yaw].remHilite();
							drawBrush(xlocal,ylocal);
						applyBrush(xlocal, ylocal);
						}
						else{
						
						//rectangle selection
						if(maction == "SRect"){
							rectflag = true;
							maction = "SRaction";
							sedna.startRect(xlocal,ylocal, !rcflag);
							 outputs[xaw][yaw].remHilite();outputs[xaw][yaw].beginRect(xlocal,ylocal,!rcflag);
							  
						}
						
						if(maction == "SRaction" && rectflag == false){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= pistons[xaw][yaw].ysiz-1; y++){
							for(int x = 0; x<= pistons[xaw][yaw].xsiz-1; x++){
								outputs[xaw][yaw].setSelection(x,y,sedna.getSelection(x,y));}}
								outputs[xaw][yaw].repaint(); sedna.detectSelection();
								/*switch(mode){
									case 1: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									case 2: setMouseAction("SDraw"); break;
									case 3:setMouseAction("CDraw"); break;
									case 4: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									default: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									}*/setMouseAction(remaction); remaction = "None";
							}
						rectflag = false;
						
						//mirror selection
						if(maction == "Mirsel"){
						if(mode == 3){
							//primary cell
							if(prisec){
							switch(mirrefflag[1]){
							case 0:	
							castor.setInt("MirrX", xlocal);castor.setInt("MirrY", ylocal);
							outputs[xaw][yaw].setHiLite(xlocal, ylocal, 1);setMouseAction(remaction); break;
							case 1: mirrefflag[1] = 2; mirrefx[1] = xlocal; mirrefy[1] = ylocal; outputs[xaw][yaw].setHiLite(xlocal, ylocal, 3);
							setMouseAction(remaction); break;
						}}
							//secondary cell
							else{
								switch(mirrefflag[2]){
							case 0:	
							castor.setInt("MirrX", xlocal);castor.setInt("MirrY", ylocal);
							outputs[0][0].setHiLite(xlocal, ylocal, 2);setMouseAction(remaction); break;
							case 1: mirrefflag[2] = 2; mirrefx[2] = xlocal; mirrefy[2] = ylocal; outputs[0][0].setHiLite(xlocal, ylocal, 4);
							setMouseAction(remaction); break;
						}}
						
						}remaction = "None";
						}
						
				}
					 if(mode == 3){
							 if(pistons[xaw][yaw].culture[xlocal][ylocal].getOption("Mirror")){
							 outputs[xaw][yaw].setHiLite(pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrX"), 
							 pistons[xaw][yaw].culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
				
		}
}
