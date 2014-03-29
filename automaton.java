import java.util.Random;
import java.util.*;

public class automaton implements Runnable{
	
//Main array and location dta
cell[][] culture;//the array of cells
int xmin;//minimum x value
int xmax;// maximum x value
int ymin;//minimum y value
int ymax;//maximum y value
int xsiz;//the automaton's width
int ysiz;//the automaton's height
//boolean[][] current;//holds the current state of all cells
int[][] newstate;//temporary matrix for iteration

//Automaton Options
boolean xwrap;//sets edge-wrapping on the left and right borders
boolean ywrap;//sets edge-wrapping along the top and bottom
String[] rulenames = new String[]{"B1", "B2", "CC"};//names for the rules
boolean[] rules;//automaton-level rules (0- Boredom(1) 1- Boredom(2) 2- Compass Chaos)
int[] rulevalues;//a settable value for each rule (a timer for rules 0,1,2)
int[] rulestates;//current state for each rule (a counter for rules 0,1,2) Rule 2 borrows rulestates[1] while active
boolean[][] boredboard;

//Automaton State
boolean pause;//is the automaton paused? also controls pausing
boolean firstflag;//Is this the automaton's first iteration?
boolean enabledflag;//Used to enable or lock down the automaton from outside

//Control Variables
boolean iiflag;//Iteration Interrupt
int interOpt;//Iteration Interruption Option
int ztime = 0; //controls run speed by inserting a pause between iterations

//Thread
Thread combobulate;//the thread that runs the automaton in run mode

//External Relationships
cellBrain mothership;//the cellBrain that holds the Automaton

//Constructors

//Auxilury constructor generates a 10x10 automaton
public automaton(){
	int a = 1; int b = 1;
	initAutomaton(a,b);
	}
	
//Main constructor generates an automaton of the specified size
public automaton( int a, int b){
	initAutomaton(a,b);
}

//Initilization, Installation

	private void initAutomaton(int x, int y){
		xsiz = x;
		ysiz = y;
		culture = new cell[x][y];
		//current = new boolean[x][y];
		newstate = new int[x][y];
		rules = new boolean[3];
		rulevalues = new int[3];
		rulestates = new int[3];
		boredboard = new boolean[x][y];
		firstflag = true;
		mothership = new cellBrain();
		combobulate = new Thread(this);
		initBoard();
		pP();
	}

	public void locate(int x, int y){
		xmin = x; ymin = y;
		xmax = xmin + xsiz;
		ymax = ymin + ysiz;
	} 
	
	//initialize the board
	public void initBoard(){
		for(int y=0;y<= ysiz-1;y++){
		for(int x=0;x<= xsiz-1;x++){
				addCell(new cell(),x,y);
			
				}} 
			}
	
	public void imprint(cellBrain cb){ mothership = cb;}
	
// Controls

		//Enables amd disables the automaton
		public void setEnabled(boolean en){ enabledflag = en;}
		
		//Interrupts iteration
		public void Interrupt(int a){ iiflag = true; interOpt = a;}
		
		//Play/Pause
		public void pP(){
			if (firstflag){pause = true; combobulate.start(); firstflag = false;}
			else{ pause = !pause;}
			}
			
		//Pause
		public void pause(){ pause = true;}
		

// Automaton options and rules

				// set edge-wrap settings
				public void setWrap(String d, boolean e){
					if(d == "X"){xwrap = e;}
					if(d == "Y"){ywrap = e;}
				}
				
				// get wrap settings
				 public boolean getWrap(String c){
					 if (c == "X"){return xwrap;}
					 if (c == "Y"){return ywrap;}
					 return false;
				 } 
				 
				 //get an automaton rulenumber from its name
				 private int getRuleNumber(String rnam){
					 int rulnum = -1;
						for(int u = 0; u < rulenames.length; u++){
							if(rnam == rulenames[u]){rulnum = u; break;}}
							return rulnum;}
			
				//Set automaton rules
				public void setRule(String rulnam, boolean rulact, int rulval){
						int rulnumb = getRuleNumber(rulnam);
						//if(rulnumb == -1){}else{rules[rulnumb] = rulact;}
						if(rulval == -1){}else{rulevalues[rulnumb] = rulval;}
						if(rulnumb == 0 && rulact){rules[1] = false;}
						if(rulnumb == 1 && rulact){rules[0] = false;}
						switch(rulnumb){
							// Error case
							case -1: break;
							//Boredom(1)
							case 0: rules[0] = rulact; rulestates[0] = 0; break;
							// Boredom(2)
							case 1: rules[1] = rulact; rulestates[0] = 0; rulestates[1] = 0; break;
							//Compass Chaos
							case 2: rules[2] = rulact; rulestates[2] = 0; break;
					}
				}
				
//Invoke rules				
public void invokeRule(String rulnam){
	int rulenum = getRuleNumber(rulnam);
	boolean rfFlag = false;
	boolean excited = false;
	switch(rulenum){
		//Boredom(1)
		case 0: 
				for(int y = 0; y <= ysiz-1; y++){ 
					for(int x = 0; x <= xsiz-1; x++){
						boolean b = convertBin(mothership.state[x][y]);
						if(b == boredboard[x][y]){}
						else{excited = true; boredboard[x][y] = b;}
					}}
					if(excited){rulestates[0] = 0;}else{rulestates[0] += 1;}
					if(rulestates[0] >= rulevalues[0]){ rfFlag = true;}
					break;
		//Boredom(2)				
		case 1: rulestates[0] += 1;
				if(rulestates[0] == 1){
				for(int y = 0; y <= ysiz-1; y++){ 
					for(int x = 0; x <= xsiz-1; x++){
						boolean b = convertBin(mothership.state[x][y]);
						if(b == boredboard[x][y]){}
						else{excited = true; boredboard[x][y] = b;}
					}}
					if(excited){rulestates[1] = 0;}else{rulestates[1] += 1;}
					if(rulestates[1] >= rulevalues[1]){ rfFlag = true;}}
					else{rulestates[0] = 0;}
					break;
		//Compass Chaos
		case 2: rulestates[2] += 1;
				if(rulestates[2] >= rulevalues[2]){
					for(int y = 0; y <= ysiz-1; y++){
						for(int x = 0; x <= xsiz-1; x++){
							Random ukulele = new Random();
							culture[x][y].setParameter("Dir", ukulele.nextInt(8));
						}}} break; 
		default: break;}
		if(rfFlag){				for(int y = 0; y <= ysiz-1; y++){
								for(int x = 0; x <= xsiz-1; x++){
								Random nosehair = new Random();
								if(nosehair.nextBoolean()){newstate[x][y] = nosehair.nextInt(256);}else{newstate[x][y] = 0;}
								}}
								mothership.setState(newstate, this);
					}
	}
		
		
				
//Add cells
public void addCell(cell ecoli, int x, int y){
		culture[x][y] = ecoli; if(ecoli.getOption("Mirror")){}else{ culture[x][y].setLocation(x,y);}}
				 
//General info
public int getParameter(String pname){
			if(pname == "Xloc"){return xmin;}
			if(pname == "Yloc"){return ymin;}
			return -1;}
			
public void setParameter(String name, int a){
			if(name == "ZT"){ztime = a;}
		}
		
//Utility stuff

//converts int states into binary states
public boolean convertBin(int s){
	if(s < 1){return false;}else{return true;}}

//neighborhood methods

				//general-purpose neighborhood-getting
			public void getNeighbors(int x, int y){
				int reps = culture[x][y].getParameter("HoodSize");
				if(reps == 0){return;}
				int[] info = new int[reps];
				for(int d = 0; d <= reps-1; d++){
					int tempx = checkAddress("X", culture[x][y].getParameter("NextX"));
					int tempy = checkAddress("Y", culture[x][y].getParameter("NextY"));
					if(tempx == -1 || tempy == -1){info[d] = 0;}
					else{info[d] = mothership.state[tempx][tempy];}
				}
				culture[x][y].setNeighbors(info);
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
				 
//Main Logic



public void iterate(){
	//can not iterate if the automaton is not enabled
	if(enabledflag){
		int x; int y;
		
		//Check and activate Automaton-level rules
		for(int z = 0; z <= rules.length-1; z++){
			if(rules[z]){invokeRule(rulenames[z]);}
		}
		
		//Iterate Interrupt
		if(iiflag){ mothership.recieveInterrupt(interOpt); iiflag = false;}
		else{//skips the rest of the iteration if interrupted
		//update all the cells
		calculateState();
		//push the results of the cell updates
		advanceState();
		mothership.iterateReport();
		}
	}
	}
//finds the new state of each cell
private void calculateState(){
	for(int y = 0; y <= ysiz-1; y++){
		for(int x = 0; x <= xsiz-1; x++){
			getNeighbors(x, y);
			culture[x][y].iterate();
			newstate[x][y] = culture[x][y].getState();
		}}
	}



//Makes the new state into the current state, sends out info
private void advanceState(){
	for(int y = 0; y <= ysiz-1; y++){
		for(int x = 0; x <= xsiz-1; x++){
			mothership.state[x][y] = newstate[x][y];
		}}
	}

//Main thread for iterating the cells
public void run(){
	//loops indefinitely
	while(true){
		
			//pauses the automaton
			try{	while (pause ==true){	
			  Thread.sleep(1);} 
			   }  catch(InterruptedException ie) {}
			   
			  //main cell logic
			 iterate();
			 
			//timeout between grid-wide iterations
			try{
				Thread.sleep(ztime);
			} catch(InterruptedException ie){}
	}
	}
	
}
