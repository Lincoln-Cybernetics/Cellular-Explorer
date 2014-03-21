
import java.util.Random;
import java.util.*;

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

class cellBrain  implements Runnable{ 

	//main variables
	
	logicEngine controller;
	cellComponent display;
	int myopt = 1;//option for iterate interrupts
	int xsiz;
	int ysiz; 
	boolean[][] current; //current binary state of all cells	
	boolean[][] newstate; //for updating
	public cell[][] culture; 	
	//general array counters Int x, and Int y are instantiated locally for each use
	
	//keep track of mouse position in edit mode
	int ztime;
	
	//variables for automaton-level rules
	boolean[] rule = new boolean[3];
	int[] ruletimer = new int[3];
	int[] ruletcount = new int[3];
	boolean[][] boredboard;//comparison for the boredom rule
	boolean bored2flag;//flag for boredom(2)
	
	// moore neighborhood brush
	threebrush ariadne;
	//Wolfram neighborhood brush
	spinbrush andromeda;
	//changestate flag 
	boolean pauseflag = false;
	boolean paused = true;
	
	//general editing flags
	
	//for selecting a single cell
	boolean singleselflag = false;
	//rectangle selection
	int rectselflag = 0;
	
	
	//state editing flags
	boolean editflag = false;
	boolean iiflag = false;
	boolean interruptedflag = false;
	int sfopt = 0;
	int sdopt = 0;
	boolean rcflag = false;
	//cell editing flags
	boolean editcellflag = false;
	boolean mirselflag = false;//toggles mirror selection mode
	
	// display flags
	boolean hiliteflag = false; //used to signal when to hilite a cell
	
	//automaton flags
	boolean firstflag = true;
	int opmode = 1;
	/*operation modes:
	 * 1 = normal running
	 * 2 = state editing
	 * 3 = cell editing
	 * */
	int dt = 1;//display type
	boolean xwrap = false;//edge wrapping x-axis
	boolean ywrap = false;// edge wrapping y-axis
	Thread t = new Thread(this);
	
	
	// constructors
	
	//default
	public cellBrain(){
		controller = new logicEngine();
		xsiz = 400;
		ysiz = 150;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		boredboard = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush();
		//ariadne.setType(true);
		andromeda = new spinbrush();
		//andromeda.setType(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		//pP();
			}
	
	//set size		
	public cellBrain(int a, int b, logicEngine vostok){
		controller = vostok;
		xsiz = a;
		ysiz = b;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		boredboard = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush();
		//ariadne.setType(true);
		andromeda = new spinbrush();
		//andromeda.setType(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		//pP();	
			}
		
		// use Strings to create	
		public cellBrain(String option){
			controller = new logicEngine();
		xsiz = 400;
		ysiz = 150;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		boredboard = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush();
		//ariadne.setType(true);
		andromeda = new spinbrush();
		//andromeda.setType(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		//pP();
			}
			
		public void initBoard(){
			//initialize the board
		for(int y=0;y<= ysiz-1;y++){
		for(int x=0;x<= xsiz-1;x++){
				addCell(x,y, new cell());
			if(display != null){	display.setSpecies(x,y,0);}
				}} 
			}
		
			// sets display	object
		public void setDisplay(cellComponent bigboard){
			display = bigboard;
			display.setState(current);
			}
			// sets iteration Speed
			public void setZT(int z){
				ztime = z;}
			
			//Play/Pause	
				
			public void pP(){
				if (firstflag){setPause(true); t.start();}
				else{ setPause(!pauseflag);}
			}
			
			//pause/unpause
				public void setPause(boolean a){
					if(opmode == 2 || opmode == 3){pauseflag = true;}
					else{pauseflag = a;}}
			
			public boolean getFirst(){
				return firstflag;}
				
			// sets iterate interrupt
			public void setII(int a){
				iiflag = true; myopt = a;
			}
				
				// mode setting methods
				
				//set operational mode
				public void setOpMode(int mode){
					switch(mode){
						case 1: opmode = 1; dt = 1; break;// normal running
						case 2: setPause(true); opmode = 2; break;// state editing
						case 3: setPause(true); opmode = 3; break;// cell editing
						case 4: opmode = 4; dt = 4; break;// multicolor
					}
					}
					
				//tells whether the automaton may iterate(not allowed during editing)
				public boolean mayIterate(){
					if(opmode == 2 || opmode == 3){return false;}
					else{return true;}
				}
				
				// set edge-wrapping
				public void setXYwrap(boolean xwr, boolean ywr){xwrap = xwr; ywrap = ywr; 
				// ariadne.setWrap(xwrap,ywrap); andromeda.setWrap(xwrap, ywrap);
				}
				 
				 // get wrap settings
				 public boolean getWrap(String c){
					 if (c == "X"){return xwrap;}
					 if (c == "Y"){return ywrap;}
					 return false;
				 } 
				
				//checks for out of bounds points, 
				//edge wrapping does apply for neighborhoods
				private int checkAddress(String axis, int value){
					if(axis == "X"){ 
						if(value >= xsiz){if(xwrap){return value - xsiz;}else{return -1;}}
						if(value < 0){if(xwrap){return xsiz + value;} else{return -1;}}
						return value;
					}
					
					if(axis == "Y"){
						if(value >= ysiz){if(ywrap){return value - ysiz;}else{return -1;}}
						if(value < 0){if(ywrap){return ysiz + value;}else{return -1;}}
						return value;
					}
					return -1;
				}
				
				//stores the type of display while editing	
				public int getDispType(){
					return dt;}
				
				//sends the current state to the display
				public void refreshState(){
					display.setState(current);
					
					}
					
					// general editing methods
					
					//selection methods
					//tells the brain a selection is being made
					public void setSelection(int a){
						switch(a){
							case 1: hiliteflag = true; singleselflag = true;
									break;
							case 2: hiliteflag = false; singleselflag = false; break;
							case 3: hiliteflag = true; 
								rectselflag = 1; break;
						}
					}
				
					
						
				
					// cell editing methods	
				public void addCell(int x, int y, cell wilbur){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					culture[x][y] = wilbur;
					culture[x][y].setLocation(x,y);}
				
				
					
								
								
				
				// State editing methods
				
				//changes the state array
				public void setCellState(int x, int y, boolean b){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					if(b){culture[x][y].activate();} else{culture[x][y].purgeState();}
					if(opmode == 4){display.setAge(x,y,culture[x][y].getState());}
					current[x][y] = b;
					
				}
				
			
						
						
					//neighborhood methods
					
				
					
					//returns the "self" neighborhood
					public boolean getSelf(int x, int y){
						boolean selfie;
						selfie = current[x][y];
						return selfie;}
					
					// returns the states of a cell's Moore Neighborhood
					public boolean[][] getMoore(int x, int y){
					boolean[][] neighbors = new boolean[3][3];
					ariadne.locate(x,y);
					for(int v = 0; v<=2; v++){
					for(int h = 0; h<=2; h++){
						int tempx = checkAddress("X", ariadne.getNextX());
						int tempy = checkAddress("Y", ariadne.getNextY());
						if(tempx == -1 || tempy == -1){neighbors[h][v] = false;}
						else{ neighbors[h][v] = current[tempx][tempy];}}}
						return neighbors;}
		
		
		// neighborhood for one dimensional cells with one neighbor on each side
		public boolean[] getWolfram(int x, int y, int o){
			boolean[] wolfhood = new boolean[3];
			andromeda.locate(x,y);
			andromeda.setOrientation(o);
			for(int v = 0; v <= 2; v++){
				int tempx = checkAddress("X", andromeda.getNextX());
				int tempy = checkAddress("Y", andromeda.getNextY());
				if(tempx == -1 || tempy == -1){wolfhood[v] = false;}
				else{wolfhood[v] = current[tempx][tempy];}
				} 
				return wolfhood;
			}
			
			//general-purpose neighborhood
			public void getNeighbors(int x, int y){
				int reps = culture[x][y].getParameter("HoodSize");
				if(reps == 0){return;}
				boolean[] info = new boolean[reps];
				for(int d = 0; d <= reps-1; d++){
					int tempx = checkAddress("X", culture[x][y].getParameter("NextX"));
					int tempy = checkAddress("Y", culture[x][y].getParameter("NextY"));
					if(tempx == -1 || tempy == -1){info[d] = false;}
					else{info[d] = current[tempx][tempy];}
				}
				culture[x][y].setNeighbors(info);
			}
		
		//Automaton-level rules
		/* Rules
		 * rule 0 = Compass Chaos (randomizes direction parameters)
		 * rule 1 = Boredom(1) (Randomizes the state if the automaton is static too long)
		 * rule 2 = Boredom(2) (Same as Boredom(1), but checks the state every other generation to filter out p2 oscilators)
		 */
		public void setRule(int rn, boolean rs){ 
			rule[rn] = rs;
			//Boredom(1) and Boredom(2) are mutually exclusive
			if(rn == 1 && rs == true){rule[2] = false;}
			if(rn == 2 && rs == true){rule[1] = false;}
			}
		
		public void setRuleTimer(int rn, int rt){ ruletimer[rn] = rt;}
		
		public void invokeRule(int r){
			switch(r){
				//Compass Chaos rule
				case 0: 
				ruletcount[0] += 1; if(ruletcount[0] >= ruletimer[0]){ruletcount[0] = 0;
				for(int y = 0; y <=ysiz-1; y++){
							for(int x = 0; x <= xsiz-1; x++){
								Random iceberg = new Random();
								culture[x][y].setParameter("Dir", iceberg.nextInt(8));}}
							}
								break;
				//Boredom(1) and Boredom(2) rules
				case 1: 
				int n = 1; if(bored2flag){n = 2;}
				boolean exciteflag = false;
				for(int y = 0; y <=ysiz-1; y++){
							for(int x = 0; x <= xsiz-1; x++){
								if(current[x][y] == boredboard[x][y]){}
								else{exciteflag = true; boredboard[x][y] = current[x][y];}
							}}
							if(exciteflag){ruletcount[n] = 0;}
							else{ruletcount[n] += 1;}
							if(ruletcount[n] >= ruletimer[n]){
								for(int y = 0; y <=ysiz-1; y++){
								for(int x = 0; x <= xsiz-1; x++){
								Random nosehair = new Random();
								current[x][y]= nosehair.nextBoolean();}}}
								break;
							
				default : break;
			}
		}
	
		
		//main logic methods
		// iterate the array
		public void iterate(){
			if(mayIterate()){
			int x; int y;
			
			//check automaton-level rules
			for(int q = 0; q < rule.length; q++){
				if(q != 2){
				if(rule[q]){ invokeRule(q);}}
				else{if(rule[2]){if(bored2flag == false){bored2flag = true; invokeRule(1);}else{bored2flag = false;}}}
			} 
			
			//iterate interrupt
			 if (iiflag){ controller.iterateInterrupt(myopt);
				  iiflag = false;}
				   else{
					//gets new values from the cells
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
							switch(culture[x][y].getParameter("Dim")){
								case -1: getNeighbors(x,y); break;
								case 0: if(culture[x][y].getOption("Mirror")){culture[x][y].setSelf(getSelf(culture[x][y].getParameter("MirrX"),culture[x][y].getParameter("MirrY")));}
										else{culture[x][y].setSelf(getSelf(x,y));}break;
								case 1:  if(culture[x][y].getOption("Mirror")){culture[x][y].setNeighbors(getWolfram(culture[x][y].getParameter("MirrX"),culture[x][y].getParameter("MirrY"),culture[x][y].getParameter("Dir")));}
										else{culture[x][y].setNeighbors(getWolfram(x,y,culture[x][y].getParameter("Dir")));} break;
								case 2: if(culture[x][y].getOption("Mirror")){culture[x][y].setNeighborhood(getMoore(culture[x][y].getParameter("MirrX"),culture[x][y].getParameter("MirrY")));}
										else{culture[x][y].setNeighborhood(getMoore(x,y));} break;
								default:  culture[x][y].setSelf(getSelf(x,y)); break;}
								// iterates the cells
								culture[x][y].iterate();
								// gets the binary stateof each cell
								newstate[x][y] = culture[x][y].getActive();
							
					}}
					
					
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
						// set age for multicolor
						if(opmode == 4){display.setAge(x,y,culture[x][y].getState());}
						// cycles new values into current state
						current[x][y] = newstate[x][y]; 
						}}
					//display.setState(current);
					refreshState();	
					controller.iterateNotify();
				}
				}}
			
			// runs the main thread
			public void run(){
				paused = false;
			
					
					
				while(true){
					//output current state
					if(firstflag){firstflag = false; refreshState();}
					//else{display.setState(current);}
						
					//pauses the program
					try{	while (pauseflag ==true){
				paused = true;
			  Thread.sleep(1);} 
			   }  catch(InterruptedException ie) {}
			   paused = false;
			   
			 //main cell logic
			 iterate();
			 
						//timeout between grid-wide iterations
						try{
				Thread.sleep(ztime);
			} catch(InterruptedException ie){}
			
			}

				}
				


			
			
		
}
