
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

class cellBrain  { 

	//main variables
	automaton pete;
	logicEngine controller;
	cellComponent display;
	int myopt = 1;//option for iterate interrupts
	int xsiz;
	int ysiz; 
	int[][] state; //current binary state of all cells	
	
	
	//keep track of mouse position in edit mode
	int ztime;
	


	int opmode = 1;
	/*operation modes:
	 * 1 = normal running
	 * 2 = state editing
	 * 3 = cell editing
	 * */
	//int dt = 1;//display type
	boolean xwrap = false;//edge wrapping x-axis
	boolean ywrap = false;// edge wrapping y-axis
	//Thread t = new Thread(this);
	
	
	// constructors
	
	//default
	public cellBrain(){
		controller = new logicEngine();
		xsiz = 400;
		ysiz = 150;
		state = new int[xsiz][ysiz];
		//pete = new automaton(xsiz,ysiz);
		//pete.locate(0,0);
		//setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
			
		
			}
	
	//set size		
	public cellBrain(int a, int b, logicEngine vostok){
		controller = vostok;
		xsiz = a;
		ysiz = b;
		state = new int[xsiz][ysiz];
		pete = new automaton(xsiz,ysiz);
		pete.locate(0,0);
		pete.imprint(this);
		pete.setEnabled(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();	
			}
		
		//gets the current state
		public void refreshState(){
			controller.outputs[0][0].setState(state);}
			
			//Stubs for new structure
			//iterate interrupt reciever
			public void recieveInterrupt(int h){controller.iterateInterrupt(h);}
			
			public void setState(int[][] update, automaton gopher){
				for(int y = gopher.ymin; y <= gopher.ymax-1; y++){
					for(int x = gopher.xmin; x <= gopher.xmax-1; x++){
						state[x][y] = update[x][y];}}
			}
			//when an automaton iterates, it calls this
			public void iterateReport(){refreshState();}
			//end of stubs
			public void playPause(){ pete.pP();}
		
		
			// sets iteration Speed
			public void setZT(int z){
				ztime = z;pete.setParameter("ZT", z);}
			
		
				
				// mode setting methods
				
				//set operational mode
				public void setOpMode(int mode){
					switch(mode){
						case 1: pete.setEnabled(true); opmode = 1;  break;// normal running
						case 2: pete.setEnabled(false); opmode = 2; break;// state editing
						case 3: pete.setEnabled(false); opmode = 3; break;// cell editing
						case 4: pete.setEnabled(true); opmode = 4;  break;// multicolor
					}
					}
					
			
				
				// set edge-wrapping
				public void setXYwrap(boolean xwr, boolean ywr){xwrap = xwr; ywrap = ywr; 
				pete.setWrap("X", xwr); pete.setWrap("Y", ywr);
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
				
				
				
				
					
					// general editing methods
					
				
				
					
						
				
					// cell editing methods	
				public void addCell(int x, int y, cell wilbur){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					pete.addCell(wilbur, x, y);}
				
				
					
								
								
				
				// State editing methods
				
				//changes the state array
				public void setCellState(int x, int y, int b){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					if(b > 0){pete.culture[x][y].activate();} else{pete.culture[x][y].purgeState();}
					//if(opmode == 4){controller.outputs[0][0].setAge(x,y, pete.culture[x][y].getState());}
					state[x][y] = b;
					
				}
				
			
						
						
		
					
				
					
					
		
		//Automaton-level rules
		/* Rules
		 * rule 0 = Boredom(1) (Randomizes the state if the automaton is static too long)
		 * rule 1 = Boredom(2) (Same as Boredom(1), but checks the state every other generation to filter out p2 oscilators)
		 * rule 2 = Compass Chaos (randomizes direction parameters)
		 */
		public void setRule(String name, boolean rs, int rv ){ 
			pete.setRule(name, rs, rv);
			}
		
		
	//main logic
	public void iterate(){ pete.iterate();}

			
			
		
}
