
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
	automaton pete;//main automaton
	automaton[][] grid;//array of automata
	int gridx;//x-axis of grid
	int gridy; //y-axis of grid
	logicEngine controller;
	cellComponent display;
	int myopt = 1;//option for iterate interrupts
	int xsiz;//width of the integer array
	int ysiz; //height of the integer array
	int[][] state; //current binary state of all cells	
	boolean paused;//is the brain paused?
	
	
	int ztime;//length of pause between iterations
	Thread clock;//thread for running the brain

	//operation modes
	int opmode = 1;
	/*operation modes:
	 * 1 = classicly rendered normal running mode
	 * 2 = state editing
	 * 3 = cell editing
	 * 4 = multicolor rendered normal running mode
	 * */
	 
	 //automaton array mode
	 int aamode;//1 = single automaton, 2 = multiple automata in a 2-D grid
	
	boolean xwrap = false;//edge wrapping x-axis
	boolean ywrap = false;// edge wrapping y-axis
	
	int[] borpol;// border polcies 0 = closed; 1 = wrap 
	//border #s follow the cell direction convention: 0 = top; 1 = top-right; 2 = right; 3 = lower right; 4 = bottom;
	// 5 = lower-left; 6 = left; 7 = upper-left;

	int interrupt;//used to interrupt the running of the brain 
	
	// constructors
	
	//default
	public cellBrain(){
		controller = new logicEngine();
		xsiz = 400;
		ysiz = 150;
		state = new int[xsiz][ysiz];
		borpol = new int[8];	
		//pete = new automaton(xsiz,ysiz);
		//pete.locate(0,0);
		//setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		interrupt = 0;
		
			}
	
	//set size		
	public cellBrain(int a, int b, logicEngine vostok){
		controller = vostok;
		xsiz = a;
		ysiz = b;
		state = new int[xsiz][ysiz];
		borpol = new int[8];
		pete = new automaton(xsiz,ysiz);
		pete.locate(0,0);
		pete.imprint(this);
		pete.setEnabled(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		aamode = 1;
		paused = true;
		interrupt = 0;
			}
			
		//set size and number of automata
		public cellBrain(int a, int b, int x, int y){
			xsiz = a*x;
			ysiz = b*y;
			grid = new automaton[x][y];
			gridx = x; gridy = y;
			state = new int[xsiz][ysiz];
			borpol = new int[8];
			for( int h = 0; h < y; h++){
				for(int w = 0; w < x; w++){
					grid[w][h] = new automaton(a,b);
					grid[w][h].locate(w*a,h*b);
					grid[w][h].imprint(this);
					grid[w][h].setUBP(2);
				}}
			setXYwrap(false, false);
			aamode = 2;
			controller = new logicEngine();
			paused = true;
			ztime = controller.getMasterSpeed();
			clock = new Thread(this);
			clock.start();
			interrupt = 0;
		}
			
// initialization
		
		//initializes the logic engine reference
		public void imprint( logicEngine sputnik){
			controller = sputnik;
			ztime = controller.getMasterSpeed();
			}
		
		//gets the current state
		public void refreshState(){
			controller.viewer.setState(state);}
			
			
			//iterate interrupt reciever
			public void recieveInterrupt(int h, automaton myaut){
				switch(aamode){
				case 1: controller.iterateInterrupt(h); break;
				case 2: break;
				}
			}
			
			//updates the main state array from the automata
			public void setState(int[][] update, automaton gopher){
				for(int y = gopher.ymin; y <= gopher.ymax-1; y++){
					for(int x = gopher.xmin; x <= gopher.xmax-1; x++){
						state[x][y] = update[x-gopher.xmin][y-gopher.ymin];}}
			}
			
			//when an automaton iterates, it calls this
			public void iterateReport(){
				switch(aamode){
				case 1:refreshState();controller.iterateNotify();break;
				case 2: break;
				}
			}
		
			// Start/stop the automaton
			public void playPause(){
				switch(aamode){
				 case 1: pete.pP(); break;
				 case 2: paused = !paused; break;
				}
				 
			 }
			 
			 //Pause
			 public void pause(){
				 switch(aamode){
					 case 1: pete.pause();break;
					 case 2:  paused = true; break;
				 }
			 }
		
		
			// sets iteration Speed
			public void setZT(int z){
				switch(aamode){
				case 1: ztime = z;pete.setParameter("ZT", z); break;
				case 2: break;
				}
			}
			
			//set an interrupt
			public void setInterrupt(int a){
				switch(aamode){
					case 1: pete.Interrupt(a); break;
					case 2: interrupt = a; break;
				}
				}
				
				// mode setting methods
				
				//set operational mode
				public void setOpMode(int mode){
					opmode = mode;
					boolean[] es = new boolean[]{false, true, false, false, true};
					switch(aamode){
						case 1: pete.setEnabled(es[mode]);  break;
						case 2: for(int y = 0; y < gridy; y++){
									for(int x = 0; x < gridx; x++){
										grid[x][y].setEnabled(es[mode]);}} 
							 break;
					}
					}
					
			
				
				// set edge-wrapping
				public void setXYwrap(boolean xwr, boolean ywr){xwrap = xwr; ywrap = ywr; 
					int xpol; if(xwr){xpol = 1;}else{xpol = 0;}
					int ypol; if(ywr){ypol = 1;}else{ypol = 0;}
					borpol[2] = xpol; borpol[6] = xpol;
					borpol[0] = ypol; borpol[4] = ypol;
					if(xwr && ywr){borpol[1] = 1; borpol[3] = 1; borpol[5] = 1; borpol[7] = 1;}
					else{borpol[1] = 0; borpol[3] = 0; borpol[5] = 0; borpol[7] = 0;}
				if(aamode == 1){pete.setWrap("X", xwr); pete.setWrap("Y", ywr);}
				}
				 
				 // get wrap settings
				 public boolean getWrap(String c){
					 if (c == "X"){return xwrap;}
					 if (c == "Y"){return ywrap;}
					 return false;
				 } 
				
				//checks for out of bounds points, 
				//edge wrapping does apply for neighborhoods
				public int checkLoc(String axis, int value){
					if(axis == "X"){ 
						if(value >= xsiz){if(borpol[2] == 1){return value - xsiz;}else{return -1;}}
						if(value < 0){if(borpol[6] == 1){return xsiz + value;} else{return -1;}}
						return value;
					}
					
					if(axis == "Y"){
						if(value >= ysiz){if(borpol[4] == 1){return value - ysiz;}else{return -1;}}
						if(value < 0){if(borpol[6] == 1){return ysiz + value;}else{return -1;}}
						return value;
					}
					return -1;
				}
				
				
				
				
					
					// general editing methods
					
				
				
					
						
				
					// cell editing methods	
				public void addCell(int x, int y, cell wilbur){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					switch(aamode){
						case 1: pete.addCell(wilbur, x, y); break;
						case 2:for(int h = 0; h <= gridy-1; h++){
								for(int w = 0; w <= gridx-1; w++){
									if(x >= grid[w][h].xmin && y >= grid[w][h].ymin){
									if(x <= grid[w][h].xmax && y <= grid[w][h].ymax){
									grid[w][h].addCell(wilbur,x,y);
									}}}}break;
						}
					}
				
				
					
								
								
				
				// State editing methods
				
				//changes the state array
				public void setCellState(int x, int y, int b){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					switch(aamode){
					case 1: if(b > 0){pete.culture[x][y].activate();} else{pete.culture[x][y].purgeState();}break;
					case 2:for(int h = 0; h <= gridy-1; h++){
							for(int w = 0; w <= gridx-1; w++){
								if(x >= grid[w][h].xmin && y >= grid[w][h].ymin){
									if(x <= grid[w][h].xmax && y >= grid[w][h].ymax){
										if(b > 0){ grid[w][h].getCell(x,y).activate(); grid[w][h].getCell(x,y).setState(b);}
										else{grid[w][h].getCell(x,y).purgeState(); grid[w][h].getCell(x,y).setState(b);}
									}}}}break;
					}
					state[x][y] = b;
					
				}
				
			
			//returns the specified cell
			public cell getCell(int x, int y){
				cell hyperion = new conveyorCell();
				switch(aamode){
					case 1: hyperion = pete.culture[x][y];
					case 2: for(int b = 0; b < gridy; b++){
										for(int a = 0; a < gridx; a++){
											if(x >= grid[a][b].xmin && y >= grid[a][b].ymin){
												if(x <= grid[a][b].xmax && y <= grid[a][b].ymax){
													hyperion =  grid[a][b].getCell(x,y);}}}}
													break;
				}
				
				
				return hyperion; 
			}		
		
					
				
					
					
		
		//Automaton-level rules
		/* Rules
		 * rule 0 = Boredom(1) (Randomizes the state if the automaton is static too long)
		 * rule 1 = Boredom(2) (Same as Boredom(1), but checks the state every other generation to filter out p2 oscilators)
		 * rule 2 = Compass Chaos (randomizes direction parameters)
		 */
		public void setRule(String name, boolean rs, int rv ){ 
			switch(aamode){
				case 1:pete.setRule(name, rs, rv); break;
				case 2: for(int h = 0; h <= gridy-1; h++){
						for(int w = 0; w <= gridx-1; w++){
							grid[w][h].setRule(name, rs, rv);}}	break;
			}
		}
		
		
	//main logic
	public void iterate(){ 
		switch(interrupt){
			case 0: break;
			default: controller.iterateInterrupt(interrupt); interrupt = 0; break;
		}
		switch(aamode){
		case 1: pete.iterate(); break;
		case 2: for(int h = 0; h <= gridy-1; h++){
						for(int w = 0; w <= gridx-1; w++){
							grid[w][h].checkRules();
							grid[w][h].calculateState();}}	
				for(int h = 0; h <= gridy-1; h++){
						for(int w = 0; w <= gridx-1; w++){
							grid[w][h].advanceState();}}	
							controller.iterateNotify(); 
							refreshState();
								break;
		}
		//System.out.print("MBI");
	}

	//runs the brain
	public void run(){
		//loops indefinitely
	while(true){
		
			//pauses the automaton
			try{	while (paused ==true){	
			  Thread.sleep(1);} 
			   }  catch(InterruptedException ie) {}
			   
			  //main cell logic
			 iterate();
			 
			//timeout between grid-wide iterations
			try{
				Thread.sleep(controller.getMasterSpeed());
			} catch(InterruptedException ie){}
	}
		
	}	
			
		
}
