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
//int xaut;// x-dimension size of automaton array
//int yaut;// y-dimension size of automaton array
//int xaw; //working xaut
//int yaw; // working y aut
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
String[] parameters = new String[]{"CDO", "CFO", "SDO", "SFO","WSX","WSY","WH", "SDT", "SFT", "CDT", "CFT", "SDP", "SFP", "CDP", "CFP","Magnify"};
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
int wsx = 0; //window start: X
int wsy = 0; //window start: Y
int wh = 0; // window height
boolean rcflag = false;//right click flag
boolean rectflag = false;//flag for making rectangles
cicomp mercury; JFrame barnabus;
JScrollPane mypane;//for holding the display

public  editEngine(){
	//xaut = 1;
	//yaut = 1;
	//pistons = new cellBrain[xaut][yaut];
	//outputs = new cellComponent[xaut][yaut];
	//mainBrain = new cellBrain();
	//viewer = new cellComponent();
	sigmund = new onebrush();
	brushtype = 1;
	castor = new cellOptionHandler();
	pollux = new cellOptionHandler();
	eris = new randcellOptionHandler();
	sedna = new selector();
	}
	
public editEngine(int cbx, int cby){
	//xaut = cbx;
	//yaut = cby;
	//pistons = new cellBrain[xaut][yaut];
	//outputs = new cellComponent[xaut][yaut];
	//mainBrain = new cellBrain();
	//viewer = new cellComponent();
	sigmund = new brush();
	brushtype = 1;
	castor = new cellOptionHandler();
	pollux = new cellOptionHandler();
	eris = new randcellOptionHandler();
	sedna = new selector();
}
	
public void initialize(int xmx, int ymx){
	xmax = xmx; ymax = ymx; 
	//for(int y = 0; y <= yaut-1; y++){
		//for(int x = 0; x <= xaut-1; x++){
		mainBrain = new cellBrain(xmx,ymx,this);
	viewer = new cellComponent(xmx,ymx);
	//pistons[x][y] = new cellBrain(xmx,ymx, this);
	// outputs[x][y] = new cellComponent(xmx,ymx);
	  viewer.addMouseMotionListener(this);	
		viewer.addMouseListener(this); 
		
		//}}
							setEditBrush(1);
						  disp = new JFrame("Cellular Explorer");
						  mypane = new JScrollPane(viewer);
						  disp.getContentPane().add(mypane);
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
		if(mode == 2 || mode == 3){}
		else{mainBrain.playPause();} }
		
	// step forward one generation						
	public void step(int a){
		for (int n = 0; n < a; n++){
			mainBrain.iterate();}
		}

	// recieves a signal from an interrupted cellBrain
	public void iterateInterrupt(int a){if (a == 1){stateFillSelect();} if(a == 2){fillCell();}}
	
	//speed settings
	public void setMasterSpeed(int a){
		mainBrain.setZT(a); zt = a;}
		
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
			  
			  //Magnify (cell size in the cell component)
			  case 15: viewer.setMag(a); //mypane.setPreferredSize(new Dimension(xmax*a, ymax*a));
			  mypane.setViewportView(viewer);
			   break;
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
		 * 1 = Normal Running (Classic)
		 * 2 = State Editing
		 * 3 = Cell Editing
		 * 4 = Multicolor
		 */
		 switch(m){
			 case 0: if(maction == "SDraw" && interactflag){}else{setMouseAction("None");}
						mode = dmode; mainBrain.setOpMode(dmode); viewer.setDispMode(dmode);  break;
						
			 case 1: if(maction == "SDraw" && interactflag){}else{setMouseAction("None");}
						mode = 1; dmode = 1; mainBrain.setOpMode(1); viewer.setDispMode(1);  break;
						
			 case 2: setMouseAction("None");mode = 2; mainBrain.setOpMode(2); viewer.setDispMode(2);  break;
			 
			 case 3: setMouseAction("None");mode = 3; mainBrain.setOpMode(3); viewer.setDispMode(3);  break;
			 
			 case 4: if(maction == "SDraw" && interactflag){}else{setMouseAction("None");}
						mode = 4; dmode = 4; mainBrain.setOpMode(4); viewer.setDispMode(4);  break;
						
			 default:  setMouseAction("None"); mode = dmode; mainBrain.setOpMode(dmode); viewer.setDispMode(dmode);  break;
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
				case 0: mainBrain.setXYwrap(false, false); break;
				case 1: mainBrain.setXYwrap(true, false); break;
				case 2: mainBrain.setXYwrap(false, true); break;
				case 3: mainBrain.setXYwrap(true, true); break;
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
		public void setAutomatonRule(String rn, boolean b, int t){
			mainBrain.setRule(rn, b, t);}
			
		

	// refreshes the selection in the display
					public void refreshSel(){
						sedna.detectSelection();
						for(int y=0;y<= mainBrain.ysiz-1;y++){
						for(int x=0;x<= mainBrain.xsiz-1;x++){
							viewer.setSelection(x,y,sedna.getSelection(x,y));
							}}
							viewer.repaint();
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
						viewer.setHiLite(thisx, thisy, 5);}
						}}
						viewer.repaint();
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
							viewer.setSelect(true);
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
									if(myx > xmax-1){if(mainBrain.getWrap("X")){myx = myx-xmax;}else{myx = xmax-1;}} 
									if(myx < 0){if(mainBrain.getWrap("X")){ myx = myx+xmax;}else{myx = 0;}}
									if(myy > ymax-1){if(mainBrain.getWrap("Y")){myy = myy-ymax;}else{myy = ymax-1;}}
									if(myy < 0){if(mainBrain.getWrap("Y")){myy = myy+ymax;}else{myy = 0;}}
									ed.setParameter("MirrX",myx); ed.setParameter("MirrY",myy);}
								
								mainBrain.addCell(a,b,ed);
						viewer.setSpecies(a,b,decider.getCT());
						
						
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
									case 1: mainBrain.pete.culture[x][y].setOption(toolstring[3], opval[3]); break;
									case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[3], paramval[3]); break;
									default: populate(x,y,1); break;}
								}
							else{ switch(cdt){
									case 0: populate(x,y,1);break;
									case 1: mainBrain.pete.culture[x][y].setOption(toolstring[2], opval[2]); break;
									case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[2], paramval[2]); break;
									default: populate(x,y,1); break;}
								}
									}}
						
					public void cellAltDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){
							if(cellfillflag){
								switch(cft){
									case 0: populate(x,y,2); break;
									case 1:  break;
									case 2: break;
									default: populate(x,y,2); break;}
								}
							else{ switch(cdt){
									case 0: populate(x,y,2);break;
									case 1: break;
									case 2: break;
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
									case 1: mainBrain.pete.culture[x][y].setOption(toolstring[3], snail.nextBoolean());
									 break;
									case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[3], nose);
									 break;
									default: populate(x,y,3); break;}
								}
							else{ 
								int elbow = randToolNum(2);
								switch(cdt){
									case 0: populate(x,y,3);break;
									case 1: mainBrain.pete.culture[x][y].setOption(toolstring[2], snail.nextBoolean()); 
									break;
									case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[2], elbow);
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
						if(mainBrain.pete.pause){fillCell();}
						else{ mainBrain.pete.Interrupt(2);}
					}
					
					// gateway method
					public void fillCell(){
						cellfillflag = true;
						for(int y=0;y<= mainBrain.ysiz-1;y++){
						for(int x=0;x<= mainBrain.xsiz-1;x++){
						switch(cfo){
							case 0: cellDraw(x,y); break;
							case 1: cellCheckDraw(x,y,false); break;
							case 2: cellRandDraw(x,y); break;
							case 3: cellRCDraw(x,y); break;
							default: cellDraw(x,y); break;}
						}}
						if( mode == 3){viewer.repaint();}
						cellfillflag = false;
							}
						
					
					
						
						// sets the cells around the outside edge of the automaton 
						public void setBorder(){	
							cellfillflag = true;
						for(int x = 0; x <= mainBrain.xsiz-1; x++){
							switch(cfo){
								case 0: cellDraw(x,0); cellDraw(x, mainBrain.ysiz-1); break;
								case 1: cellCheckDraw(x,0, false); cellCheckDraw(x,mainBrain.ysiz-1, false); break;
								case 2: cellRandDraw(x,0); cellRandDraw(x, mainBrain.ysiz-1); break;
								case 3: cellRCDraw(x,0); cellRCDraw(x, mainBrain.ysiz-1);break;
								default: cellDraw(x,0); cellDraw(x, mainBrain.ysiz-1); break;}}
						for(int y = 0; y<= mainBrain.ysiz-1; y++){
							switch(cfo){
								case 0: cellDraw(0,y); cellDraw(mainBrain.xsiz-1, y); break;
								case 1: cellCheckDraw(0,y, false); cellCheckDraw(mainBrain.xsiz-1,y, false); break;
								case 2: cellRandDraw(0,y); cellRandDraw(mainBrain.xsiz-1, y); break;
								case 3: cellRCDraw(0,y); cellRCDraw(mainBrain.xsiz-1, y); break;
								default: cellDraw(0,y); cellDraw(mainBrain.xsiz-1, y); break;}}
								if(mode == 3){viewer.repaint();}
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
								case 0: mainBrain.setCellState(x,y,1);viewer.setAState(x,y,1); break;
								case 1: mainBrain.pete.culture[x][y].setOption(toolstring[1], opval[1]); break;
								case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[1], paramval[1]); break;
								default: mainBrain.setCellState(x,y,1);viewer.setAState(x,y,1); break;}
							}
							else{
								switch(sdt){
									case 0: mainBrain.setCellState(x,y,1);viewer.setAState(x,y,1); break;
									case 1: mainBrain.pete.culture[x][y].setOption(toolstring[0], opval[0]); break;
									case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[0], paramval[0]); break;
									default: mainBrain.setCellState(x,y,1);viewer.setAState(x,y,1); break;}
								}
							}
						}
						
						
						
					
				private void stateAltDraw(int x,int y){
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){
						if(statefillflag){
							switch(sft){
								case 0: mainBrain.setCellState(x,y,0);viewer.setAState(x,y,0); break;
								case 1: mainBrain.pete.culture[x][y].setOption(toolstring[1], false);
								 break;
								case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[1], 0);
								 break;
								default: mainBrain.setCellState(x,y,0);viewer.setAState(x,y,0); break;}
							}
							else{
								switch(sdt){
									case 0: mainBrain.setCellState(x,y,0);viewer.setAState(x,y,0); break;
									case 1: mainBrain.pete.culture[x][y].setOption(toolstring[0], false);
									 break;
									case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[0], 0);
									 break;
									 default: mainBrain.setCellState(x,y,0);viewer.setAState(x,y,0); break;
								}
							}
						}}
					
				private void stateRandDraw(int x, int y){
					Random foghorn = new Random();
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){
						int dunebuggy = randToolNum(1);
						int twog;
						boolean twig = foghorn.nextBoolean();
						if(twig){twog = foghorn.nextInt(256)+1;}else{twog = 0;}
						if(statefillflag){
							switch(sft){
								case 0: mainBrain.setCellState(x,y,twog);viewer.setAState(x,y,twog); break;
								case 1: mainBrain.pete.culture[x][y].setOption(toolstring[1], foghorn.nextBoolean());
								 break;
								case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[1], dunebuggy);
								 break;
								default: mainBrain.setCellState(x,y,twog);viewer.setAState(x,y,twog); break;}
							}
							else{
								int pickaxe = randToolNum(0);
								switch(sdt){
									case 0: mainBrain.setCellState(x,y,twog);viewer.setAState(x,y,twog); break;
									case 1: mainBrain.pete.culture[x][y].setOption(toolstring[0], foghorn.nextBoolean());
									 break;
									case 2: mainBrain.pete.culture[x][y].setParameter(toolstring[0], pickaxe);
									 break;
									 default: mainBrain.setCellState(x,y,twog);viewer.setAState(x,y,twog); break;
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
					if(mainBrain.pete.pause){stateFillSelect();}
					else{ mainBrain.pete.Interrupt(1);}
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
						if(mode != 3){mainBrain.refreshState();}
					} 
						
				private void stateRandFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						stateRandDraw(x,y);
					}} 
					if( mode != 3){ mainBrain.refreshState();}
					}
					
				private void stateClearFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						mainBrain.state[x][y] = 0; mainBrain.pete.culture[x][y].purgeState();}}
						if(mode != 3){mainBrain.refreshState();}
						}
					
				private void stateCheckFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
					stateCheckDraw(x,y, true);
					}} 
					if(mode != 3){mainBrain.refreshState();}
					}
					
				private void stateRCFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						if( y % 2 == 1 || x % 2 == 1){stateCheckDraw(x,y,true);}
						else{stateRandDraw(x,y);}}}
						if(mode != 3){mainBrain.refreshState();}
					}
				
				private void stateInvert(){
					for(int y = 0; y<= ymax-1; y++){
						for(int x=0; x<= xmax-1; x++){
							if(!sedna.getSelected() ||sedna.getSelection(x,y)){
								if(mainBrain.state[x][y] == 0){mainBrain.setCellState(x,y,-1);}
								mainBrain.setCellState(x,y,mainBrain.state[x][y]*-1);}}}
					if(mode != 3){mainBrain.refreshState();}
				}		
			
			
			//Mouse Interaction Methods		
	
					public void mouseMoved( MouseEvent e){
						//setWorkAut(e);
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/viewer.magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/viewer.magnify;}
						if (xlocal > mainBrain.xsiz-1){xlocal = mainBrain.xsiz-1;}
						if (ylocal > mainBrain.ysiz-1){ylocal = mainBrain.ysiz-1;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(mainBrain.pete.culture[xlocal][ylocal]);}
						viewer.remHilite();
						if(isMouseUsed()){
							drawBrush(xlocal,ylocal);
					}
						 // draws rectangle during rectangle selection
						 if(maction == "SRaction"){for(int y = 0; y <= mainBrain.ysiz-1; y++){
												for(int x = 0; x <= mainBrain.xsiz-1; x++){
													viewer.setSelection(x,y,sedna.getSelection(x,y));}}
							 viewer.finishRect(xlocal,ylocal); }
							 
						 if(mode == 3){
							 if(mainBrain.pete.culture[xlocal][ylocal].getOption("Mirror")){
							 viewer.setHiLite(mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrX"), 
							 mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
						
					public void mouseDragged(MouseEvent e) {
						//setWorkAut(e);
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/viewer.magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/viewer.magnify;}
						if (xlocal > mainBrain.xsiz-1){xlocal = mainBrain.xsiz-1;}
						if (ylocal > mainBrain.ysiz-1){ylocal = mainBrain.ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(mainBrain.pete.culture[xlocal][ylocal]);}
						viewer.remHilite();
						if(isMouseUsed()){
							drawBrush(xlocal,ylocal);
						applyBrush(xlocal, ylocal);
					}
					else{
						 //  rectangle selection
						 if(maction == "SRaction"){
						 for(int y = 0; y <= mainBrain.ysiz-1; y++){
							for(int x = 0; x <= mainBrain.xsiz-1; x++){
								viewer.setSelection(x,y,sedna.getSelection(x,y));}}
							 viewer.finishRect(xlocal,ylocal); }}
							 
							  if(mode == 3){
							 if(mainBrain.pete.culture[xlocal][ylocal].getOption("Mirror")){
							 viewer.setHiLite(mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrX"), 
							 mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrY"), 4);
								}
							}
							 
							}
							
					public void mouseEntered(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/viewer.magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/viewer.magnify;}
						if (xlocal > mainBrain.xsiz-1){xlocal = mainBrain.xsiz-1;}
						if (ylocal > mainBrain.ysiz-1){ylocal = mainBrain.ysiz-1;}
						viewer.remHilite();
						if(isMouseUsed()){
							drawBrush(xlocal,ylocal);
						}
						
						 if(mode == 3){
							 if(mainBrain.pete.culture[xlocal][ylocal].getOption("Mirror")){
							 viewer.setHiLite(mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrX"), 
							 mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
					
					public void mouseExited(MouseEvent e){
						viewer.remHilite();
						}
					
					public void mousePressed(MouseEvent e){
						//setWorkAut(e);
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/viewer.magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/viewer.magnify;}
						if (xlocal > mainBrain.xsiz-1){xlocal = mainBrain.xsiz-1;}
						if (ylocal > mainBrain.ysiz-1){ylocal = mainBrain.ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(mainBrain.pete.culture[xlocal][ylocal]);}
						
						if(isMouseUsed()){
							
							drawBrush(xlocal,ylocal);
						applyBrush(xlocal, ylocal);
					}
					else{
						
						//rectangle selection
						if(maction == "SRect"){
						sedna.startRect(xlocal,ylocal, !rcflag);
						 viewer.remHilite();viewer.beginRect(xlocal,ylocal,!rcflag); maction = "SRaction";}
						 
					 }
					 
					  if(mode == 3){
							 if(mainBrain.pete.culture[xlocal][ylocal].getOption("Mirror")){
							 viewer.setHiLite(mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrX"), 
							 mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
					
					public void mouseReleased(MouseEvent e){
						//setWorkAut(e);
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/viewer.magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/viewer.magnify;}
						if (xlocal > mainBrain.xsiz-1){xlocal = mainBrain.xsiz-1;}
						if (ylocal > mainBrain.ysiz-1){ylocal = mainBrain.ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(mainBrain.pete.culture[xlocal][ylocal]);}
						
						if(isMouseUsed()){ 
							
							drawBrush(xlocal,ylocal);
							//no action taken
						}
					else{
						//rectangle selection
						if(maction == "SRaction"){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= mainBrain.ysiz-1; y++){
							for(int x = 0; x<= mainBrain.xsiz-1; x++){
								viewer.setSelection(x,y,sedna.getSelection(x,y));}}
								viewer.repaint(); sedna.detectSelection();
								
									setMouseAction(remaction); remaction = "None";
								}
							}
							
							 if(mode == 3){
							 if(mainBrain.pete.culture[xlocal][ylocal].getOption("Mirror")){
							 viewer.setHiLite(mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrX"), 
							 mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
						
						}
						
					
					public void mouseClicked(MouseEvent e){
						//setWorkAut(e);
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/viewer.magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/viewer.magnify;}
						if (xlocal > mainBrain.xsiz-1){xlocal = mainBrain.xsiz-1;}
						if (ylocal > mainBrain.ysiz-1){ylocal = mainBrain.ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(mainBrain.pete.culture[xlocal][ylocal]);}
						//
						if(isMouseUsed()){
							
							drawBrush(xlocal,ylocal);
						applyBrush(xlocal, ylocal);
						}
						else{
						
						//rectangle selection
						if(maction == "SRect"){
							rectflag = true;
							maction = "SRaction";
							sedna.startRect(xlocal,ylocal, !rcflag);
							 viewer.remHilite();viewer.beginRect(xlocal,ylocal,!rcflag);
							  
						}
						
						if(maction == "SRaction" && rectflag == false){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= mainBrain.ysiz-1; y++){
							for(int x = 0; x<= mainBrain.xsiz-1; x++){
								viewer.setSelection(x,y,sedna.getSelection(x,y));}}
								viewer.repaint(); sedna.detectSelection();
								setMouseAction(remaction); remaction = "None";
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
							viewer.setHiLite(xlocal, ylocal, 1);setMouseAction(remaction); break;
							case 1: mirrefflag[1] = 2; mirrefx[1] = xlocal; mirrefy[1] = ylocal; viewer.setHiLite(xlocal, ylocal, 3);
							setMouseAction(remaction); break;
						}}
							//secondary cell
							else{
								switch(mirrefflag[2]){
							case 0:	
							castor.setInt("MirrX", xlocal);castor.setInt("MirrY", ylocal);
							viewer.setHiLite(xlocal, ylocal, 2);setMouseAction(remaction); break;
							case 1: mirrefflag[2] = 2; mirrefx[2] = xlocal; mirrefy[2] = ylocal; viewer.setHiLite(xlocal, ylocal, 4);
							setMouseAction(remaction); break;
						}}
						
						}remaction = "None";
						}
						
				}
					 if(mode == 3){
							 if(mainBrain.pete.culture[xlocal][ylocal].getOption("Mirror")){
							 viewer.setHiLite(mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrX"), 
							 mainBrain.pete.culture[xlocal][ylocal].getParameter("MirrY"), 4);
							}
						}
				
		}
}
//End of Edit Engine

//Cell information viewer
class cicomp extends JComponent{
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
