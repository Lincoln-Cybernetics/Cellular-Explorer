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
int zt = 500;// speed setting
brush sigmund;// editing brush
int brushtype;
String maction;
int mode = 1;// general operating mode
int dmode = 1;// display mode
cellOptionHandler castor;
cellOptionHandler pollux;
cellOptionHandler eris;
boolean prisec = true;//shunts settings info to castor(true) or pollux(false)
int mirrefflag = 0;//mirror reference state
int mirrefx;//mirror reference x
int mirrefy;//mirror reference y
int anchorx;//anchor coordinate x for mirrors using the reference
int anchory;//anchor coordinate y for mirrors using the reference
selector sedna;
int sdo = 0; //state drawing option
boolean interactflag = false;
int sfo = 0; //state fill option
int cdo = 0; //cell drawing option
int cfo = 0; //cell fill option
boolean rcflag = false;//right click flag
boolean rectflag = false;//flag for making rectangles
cicomp mercury; JFrame barnabus;

public  editEngine(){
	pistons = new cellBrain[1][1];
	outputs = new cellComponent[1][1];
	sigmund = new brush();
	brushtype = 1;
	castor = new cellOptionHandler();
	pollux = new cellOptionHandler();
	eris = new randcellOptionHandler();
	sedna = new selector();
	}
	
public void initialize(){
	xmax = 400; ymax = 150; 
	pistons[0][0] = new cellBrain(400,150, this);
	 outputs[0][0] = new cellComponent(400,150);
	  outputs[0][0].addMouseMotionListener(this);	
							outputs[0][0].addMouseListener(this); pistons[0][0].setDisplay(outputs[0][0]); 
							setEditBrush(1);
						  JFrame disp = new JFrame("Cellular Explorer");
						  disp.getContentPane().add(new JScrollPane(outputs[0][0]));
						  disp.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
						  disp.setSize(1000,500);
						  disp.setLocation(200,165);
						  disp.setResizable(true);
						  disp.setVisible(true);
						  eris.setInt("Xsiz", 400);
						  eris.setInt("Ysiz", 150);
						  sedna = new selector(400,150);
						  setMode(1);
						  pistons[0][0].initBoard();
						}

public void handleControl(ucEvent e){
	if(e.getSource() == castor.source){
		switch(e.getCommand()){
			case 3: setMouseAction("Mirsel"); prisec = true; break;
		}
		}
	if(e.getSource() == pollux.source){
		switch(e.getCommand()){
			case 3: setMouseAction("Mirsel"); prisec = false; break;
		}
		} 
	if(e.getSource() == castor.source || e.getSource() == pollux.source){
		switch(e.getCommand()){
			case 4: mirrefflag = 1; setMouseAction("Mirsel");break;
			case 5: mirrefflag = 0;break;
		}
	}
	}
	//start/stop the automaton
	public void playPause(){
		if(!pistons[0][0].mayIterate()){pistons[0][0].setOpMode(pistons[0][0].getDispType());
		outputs[0][0].setDispMode(pistons[0][0].getDispType());}
								pistons[0][0].pP(); }
								
	public void step(int a){
		for (int n = 0; n < a; n++){
			pistons[0][0].iterate();}
		}

	public void iterateInterrupt(int a){if (a == 1){stateFillSelect();}}
	
	//speed settings
	public void setMasterSpeed(int a){
		pistons[0][0].setZT(a); zt = a;}
		
	public int getMasterSpeed(){
		return zt;}
	
	// mouse methods	
	public void setMouseAction(String action){
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
	public void setSDO(int u){
		sdo = u;
		/* State Drawing Option
		 * 0 = Regular drawing 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 */
	 }
	 
	public void setInteract(boolean b){ interactflag = b;}
	 
	public void setSFO(int v){
		sfo = v;
		/* State Fill Option
		 * 0 = Regular fill 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 * 4 = Clear state
		 * 5 = Invert state
		 */
	 } 
	 
	 public void setCDO(int w){
		cdo = w;
		/* Cell Drawing Option
		 * 0 = Regular drawing 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 */
	 }
	 
	 public void setCFO(int t){
		cfo = t;
		/* Cell Fill Option
		 * 0 = Regular drawing 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 */
	 }
	 
	 // show cell info
	 public void showCI(){
		// barnabus.setVisible(false);
		 if(barnabus == null){
		 mercury = new cicomp();
		 barnabus = new JFrame("Cell Info");
		 barnabus.getContentPane().add(mercury);
		 barnabus.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		 barnabus.setSize(250,250);
		 barnabus.setLocation(675,0);
		 barnabus.setResizable(false);
		 barnabus.setVisible(true);
		 mercury.setVisible(true);}
		 else{barnabus.setVisible(true);}
		 
		 }
		 
	public void iterateNotify(){ 
		if(mercury != null){mercury.refCell();}
	}
	
	//general mode setting for display and automaton
	public void setMode(int m){
		/*Modes:
		 * 1 = Normal Running
		 * 2 = State Editing
		 * 3 = Cell Editing
		 * 4 = Multicolor
		 */
		 switch(m){
			 case 0: mode = dmode; pistons[0][0].setOpMode(dmode); outputs[0][0].setDispMode(dmode); setMouseAction("SDraw"); break;
			 case 1: mode = 1; dmode = 1; pistons[0][0].setOpMode(1); outputs[0][0].setDispMode(1); setMouseAction("SDraw"); break;
			 case 2: mode = 2; pistons[0][0].setOpMode(2); outputs[0][0].setDispMode(2); setMouseAction("SDraw"); break;
			 case 3: mode = 3; pistons[0][0].setOpMode(3); outputs[0][0].setDispMode(3); setMouseAction("CDraw"); break;
			 case 4: mode = 4; dmode = 4; pistons[0][0].setOpMode(4); outputs[0][0].setDispMode(4); setMouseAction("SDraw"); break;
			 default:  mode = dmode; pistons[0][0].setOpMode(dmode); outputs[0][0].setDispMode(dmode); setMouseAction("SDraw"); break;
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
		
		public void setWrap(int a){
			switch(a){
				case 0: pistons[0][0].setXYwrap(false, false); break;
				case 1: pistons[0][0].setXYwrap(true, false); break;
				case 2: pistons[0][0].setXYwrap(false, true); break;
				case 3: pistons[0][0].setXYwrap(true, true); break;
			}
		}

	// refreshes the selection in the display
					public void refreshSel(){
						sedna.detectSelection();
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){
							outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));
							}}
							outputs[0][0].repaint();
						}
						
							// brush setting method
					public void setEditBrush(int b){
						brushtype = b;
						switch(brushtype){
							// 1x1
						case 1: sigmund = new brush(pistons[0][0].xsiz, pistons[0][0].ysiz); break;
							//2x2
						case 2: sigmund = new twobrush(pistons[0][0].xsiz, pistons[0][0].ysiz); break;
							//3x3
						case 3: sigmund = new threebrush(pistons[0][0].xsiz,pistons[0][0].ysiz); break;
							//Glider
						case 4: sigmund = new gliderbrush(pistons[0][0].xsiz, pistons[0][0].ysiz); sigmund.setOrientation(0); break;
						
						default : sigmund = new brush(pistons[0][0].xsiz, pistons[0][0].ysiz); break;}
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
								if(mirrefflag == 2 && ed.getOption("Mirror")){
									anchorx = a; ed.setParameter("MirrX", a);
									anchory = b; ed.setParameter("MirrY", b);
									mirrefflag = 3;}
								//set mirror coordinates 
								if(mirrefflag == 3 && ed.getOption("Mirror")){
									int myx = anchorx - a; int myy = anchory - b;
									myx = mirrefx-myx; myy = mirrefy-myy;
									if(myx > xmax-1){if(pistons[0][0].getWrap("X")){myx = myx-xmax;}else{myx = xmax-1;}} 
									if(myx < 0){if(pistons[0][0].getWrap("X")){ myx = myx+xmax;}else{myx = 0;}}
									if(myy > ymax-1){if(pistons[0][0].getWrap("Y")){myy = myy-ymax;}else{myy = ymax-1;}}
									if(myy < 0){if(pistons[0][0].getWrap("Y")){myy = myy+ymax;}else{myy = 0;}}
									ed.setParameter("MirrX",myx); ed.setParameter("MirrY",myy);}
								
								pistons[0][0].addCell(a,b,ed);
						outputs[0][0].setSpecies(a,b,decider.getCT());
						outputs[0][0].setLifespan(a,b,ed.getParameter("Mat")); 
						
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
					
					public void cellDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
						
					public void cellAltDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,2);}} 
					
					public void cellCheckDraw(int x, int y, boolean b){
						if( y % 2 == 1 ^ x % 2 == 1){
							if(b){if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,2);}}
							else{if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
							}
						else{
							if(b){if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
							else{if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,2);}}
							}
						}
						
					public void cellRCDraw(int x, int y){
						if( y % 2 == 1 ^ x % 2 == 1){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
						else{
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,3);}}
						}
						
					public void cellRandDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,3);}
					}
						
					// cell filling methods
					
					// gateway method
					public void fillCell(){
		
						switch(cfo){
							case 0: cellFill(); break;
							case 1: cellCheckFill(); break;
							case 2: cellRandFill(); break;
							case 3: cellRCFill(); break;
							default: cellFill(); break;}
							}
						
					private void cellFill(){
					for(int y=0;y<= pistons[0][0].ysiz-1;y++){
					for(int x=0;x<= pistons[0][0].xsiz-1;x++){
						cellDraw(x,y);}}
						if( mode == 3){outputs[0][0].repaint();}
						}
					
					
					private void cellCheckFill(){
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){	
							cellCheckDraw(x,y, false);
							}}
						if( mode == 3){outputs[0][0].repaint();}
						
					}
					
					private void cellRCFill(){
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){	
							cellRCDraw(x,y);
							}}
						if( mode == 3){outputs[0][0].repaint();}
						
					}
					
				
					
					private void cellRandFill(){
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){	
							cellRandDraw(x,y);
						}}
						if( mode== 3){outputs[0][0].repaint();}
						
					}
					
					
						
						// sets the cells around the outside edge of the automaton 
						public void setBorder(){	
						for(int x = 0; x <= pistons[0][0].xsiz-1; x++){
							switch(cfo){
								case 0: cellDraw(x,0); cellDraw(x, pistons[0][0].ysiz-1); break;
								case 1: cellCheckDraw(x,0, false); cellCheckDraw(x,pistons[0][0].ysiz-1, false); break;
								case 2: cellRandDraw(x,0); cellRandDraw(x, pistons[0][0].ysiz-1); break;
								case 3: cellRCDraw(x,0); cellRCDraw(x, pistons[0][0].ysiz-1);break;
								default: cellDraw(x,0); cellDraw(x, pistons[0][0].ysiz-1); break;}}
						for(int y = 0; y<= pistons[0][0].ysiz-1; y++){
							switch(cfo){
								case 0: cellDraw(0,y); cellDraw(pistons[0][0].xsiz-1, y); break;
								case 1: cellCheckDraw(0,y, false); cellCheckDraw(pistons[0][0].xsiz-1,y, false); break;
								case 2: cellRandDraw(0,y); cellRandDraw(pistons[0][0].xsiz-1, y); break;
								case 3: cellRCDraw(0,y); cellRCDraw(pistons[0][0].xsiz-1, y); break;
								default: cellDraw(0,y); cellDraw(pistons[0][0].xsiz-1, y); break;}}
								if(mode == 3){outputs[0][0].repaint();}
								
					}
					//cell settings editing methods
					
					//fills boolean settings
					//public void boolFill(String a, boolean b){
					//	for(int y=0;y<=ysiz-1;y++){
					//	for(int x=0;x<=xsiz-1;x++){
						//	if(harry.getSelected() == false || harry.getSelection(x,y)){
								//culture[x][y].setBool(a,b);
					//	}}}
					//}
					
					//fills Int settings
					//public void intFill(String a, int b){
					//	for(int y=0;y<=ysiz-1;y++){
					//	for(int x=0;x<=xsiz-1;x++){
					//		if(harry.getSelected() == false || harry.getSelection(x,y)){
						//		culture[x][y].setInt(a,b);}}}
						//	}
								
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
					
				private void stateDraw(int x,int y){
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,true);}}
					
				private void stateAltDraw(int x,int y){
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,false);}}
					
				public void stateCheckDraw(int x,int y, boolean fill){
					if( y % 2 == 1 ^ x % 2 == 1){ if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,fill);}}
						else{if(fill){if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,false);}}}}
					
				private void stateRandDraw(int x, int y){
					Random foghorn = new Random();
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,foghorn.nextBoolean());}}	
					
				private void stateRCDraw(int x, int y){
						if( y % 2 == 1 || x % 2 == 1){stateCheckDraw(x,y,true);}
						else{stateRandDraw(x,y);}}
								
				//state fill methods
				
				//gateway method
				public void fillState(){
					if(pistons[0][0].paused){stateFillSelect();}
					else{pistons[0][0].myopt = 1; pistons[0][0].setII();}
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
					
						switch(sfo){
							case 0: stateFill(); break;
							case 1: stateCheckFill(); break;
							case 2: stateRandFill(); break;
							case 3: stateRCFill(); break;
							case 4: stateClearFill(); break;
							case 5: stateInvert(); break;
							
							default: stateCheckFill(); break;}
						}
					
				private void stateFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						stateDraw(x,y);}}
						if(mode != 3){pistons[0][0].refreshState();}
					} 
						
				private void stateRandFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						stateRandDraw(x,y);
					}} 
					if( mode != 3){ pistons[0][0].refreshState();}
					}
					
				private void stateClearFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						pistons[0][0].current[x][y] = false; pistons[0][0].culture[x][y].purgeState();}}
						if(mode != 3){pistons[0][0].refreshState();}
						}
					
				private void stateCheckFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
					stateCheckDraw(x,y, true);
					}} 
					if(mode != 3){pistons[0][0].refreshState();}
					}
					
				private void stateRCFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						if( y % 2 == 1 || x % 2 == 1){stateCheckDraw(x,y,true);}
						else{stateRandDraw(x,y);}}}
						if(mode != 3){pistons[0][0].refreshState();}
					}
				
				private void stateInvert(){
					for(int y = 0; y<= ymax-1; y++){
						for(int x=0; x<= xmax-1; x++){
							if(!sedna.getSelected() ||sedna.getSelection(x,y)){
								pistons[0][0].setCellState(x,y,!pistons[0][0].current[x][y]);}}}
					if(mode != 3){pistons[0][0].refreshState();}
				}		
				
			
						
			

	// mouse interaction methods
					public void mouseMoved( MouseEvent e){
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[0][0].culture[xlocal][ylocal]);}
						
						 // draws rectangle during rectangle selection
						 if(maction == "SRaction"){for(int y = 0; y <= pistons[0][0].ysiz-1; y++){
												for(int x = 0; x <= pistons[0][0].xsiz-1; x++){
													outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
							 outputs[0][0].finishRect(xlocal,ylocal); }
						 
						
						
						}
						
					public void mouseDragged(MouseEvent e) {
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[0][0].culture[xlocal][ylocal]);}
						//
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
						//edit state
						if(maction == "SDraw"){
						if(mode == 2 || interactflag){
								drawState(thisx, thisy);
							}}
						
						//editcell
						if(maction == "CDraw"){
						if (mode == 3 ){
							drawCell(thisx, thisy);
							}
						}
							
							// cell selection
						if(maction == "BSel"){
							outputs[0][0].setSelect(true);
							 if(rcflag){sedna.removeCell(thisx,thisy);}
							 else{sedna.selectCell(thisx,thisy);}
						}
						
					}
						
						refreshSel();
					}
					else{
						 //  rectangle selection
						 if(maction == "SRaction"){
						 for(int y = 0; y <= pistons[0][0].ysiz-1; y++){
							for(int x = 0; x <= pistons[0][0].xsiz-1; x++){
								outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
							 outputs[0][0].finishRect(xlocal,ylocal); }}
							}
							
					public void mouseEntered(MouseEvent e){}
					
					public void mouseExited(MouseEvent e){}
					
					public void mousePressed(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[0][0].culture[xlocal][ylocal]);}
						//
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
							//edit state
						if(maction == "SDraw"){
						if(mode == 2 || interactflag){
								drawState(thisx, thisy);
							}}
						
						//editcell
						if(maction == "CDraw"){
						if (mode == 3 ){
							drawCell(thisx, thisy);
							}
						}
						
						// cell selection
						if(maction == "BSel"){
							outputs[0][0].setSelect(true);
							 if(rcflag){sedna.removeCell(thisx,thisy);}
							 else{sedna.selectCell(thisx,thisy);}
						}
						
					}
					refreshSel();
					}
					else{
						
						//rectangle selection
						if(maction == "SRect"){
						sedna.startRect(xlocal,ylocal, !rcflag);
						 outputs[0][0].remHilite();outputs[0][0].beginRect(xlocal,ylocal,!rcflag); maction = "SRaction";}
						 
					 }
						}
					
					public void mouseReleased(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[0][0].culture[xlocal][ylocal]);}
						//
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						//nothin' doin'
					}}
					else{
						//rectangle selection
						if(maction == "SRaction"){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= pistons[0][0].ysiz-1; y++){
							for(int x = 0; x<= pistons[0][0].xsiz-1; x++){
								outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
								outputs[0][0].repaint(); sedna.detectSelection();
								switch(mode){
									case 1: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									case 2: setMouseAction("SDraw"); break;
									case 3: setMouseAction("CDraw"); break;
									case 4: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									default: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;}
								}
							}
							
						}
						
					
					public void mouseClicked(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						// send info to cell info display
						if(mercury != null){mercury.setCell(pistons[0][0].culture[xlocal][ylocal]);}
						//
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
						//edit state
						if(maction == "SDraw"){
						if(mode == 2 || interactflag){
								drawState(thisx, thisy);
						 }}
						 
						//edit cells
						if(maction == "CDraw"){
						if (mode == 3 ){
							drawCell(thisx, thisy);
							}
						}
							
						
						
						// cell selection
						if(maction == "BSel"){
							outputs[0][0].setSelect(true);
							if(rcflag){sedna.removeCell(thisx,thisy);}
							else{sedna.selectCell(thisx,thisy);}	
						}
					}
						
								refreshSel();
						}
						else{
						
						//rectangle selection
						if(maction == "SRect"){
							rectflag = true;
							maction = "SRaction";
							sedna.startRect(xlocal,ylocal, !rcflag);
							 outputs[0][0].remHilite();outputs[0][0].beginRect(xlocal,ylocal,!rcflag);
							  
						}
						
						if(maction == "SRaction" && rectflag == false){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= pistons[0][0].ysiz-1; y++){
							for(int x = 0; x<= pistons[0][0].xsiz-1; x++){
								outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
								outputs[0][0].repaint(); sedna.detectSelection();
								switch(mode){
									case 1: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									case 2: setMouseAction("SDraw"); break;
									case 3:setMouseAction("CDraw"); break;
									case 4: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									default: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									}
							}
						rectflag = false;
						
						//mirror selection
						if(maction == "Mirsel"){
						if(mode == 3){
							switch(mirrefflag){
							case 0:	
							if(prisec){castor.setInt("MirrX", xlocal);castor.setInt("MirrY", ylocal);}
							else{pollux.setInt("MirrX", xlocal); pollux.setInt("MirrY", ylocal);}
							outputs[0][0].setHiLite(xlocal, ylocal, 3);setMouseAction("CDraw"); break;
							case 1: mirrefflag = 2; mirrefx = xlocal; mirrefy = ylocal; outputs[0][0].setHiLite(xlocal, ylocal, 2);
							setMouseAction("CDraw"); break;
						}
						}}
				}
					
				
		}
}
