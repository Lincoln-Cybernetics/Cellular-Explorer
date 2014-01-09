
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

public class wolfram extends cell{
	// describe the cell's neighborhood
	int dim;//dimensionality
	int radius;
	
	// describe the current state of the cell
	boolean active;
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	int direction;
	// in Wolfram, direction indicates the direction traveled 
	//from the center cell towards the least significant bit in the neighborhood
	
	// neighborhood variables
	boolean self;
	boolean[] neighbors;
	boolean[][] neighborhood;
	boolean[][][] environment;
	
	int mystate;
	int[] neighborstate;
	int[][] hoodstate;
	int[][][] envirostate;
	
	//Wolfram rule
	boolean[] rule;
	
	// maturity setting
	int mat;
	int matcount;
	
	//age and fade rule variables
	int age;
	boolean ages;
	int fade;
	boolean fades;
	
	//constructor
	public wolfram(){
		dim = 1;
		radius = 1;
		active = false;
		state = 0;
		name = "Wolfram";
		hoodx = -1;
		hoody = -1;
		direction = 0;
		self = false;
		mystate = 0;
		mat = 1;
		matcount = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		rule = new boolean[8];
		neighbors = new boolean[3];
		}
		
		
		//Get and set controls and options
		
		@Override public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Fade"){ return true;}
			if(control == "Dir"){ return true;}
			if(control == "WolfRule"){ return true;}
			if(control == "Mat"){ return true;}
			if(control == "Orient"){ return true;}
			 return false;}
		
		@Override public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			if(opname == "Fades"){ return fades;}
			if(opname == "WR0"){return rule[0];}
			if(opname == "WR1"){return rule[1];}
			if(opname == "WR2"){return rule[2];}
			if(opname == "WR3"){return rule[3];}
			if(opname == "WR4"){return rule[4];}
			if(opname == "WR5"){return rule[5];}
			if(opname == "WR6"){return rule[6];}
			if(opname == "WR7"){return rule[7];}
			return false;}
		
		@Override public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			if(opname == "WR0"){rule[0] = b;}
			if(opname == "WR1"){rule[1] = b;}
			if(opname == "WR2"){rule[2] = b;}
			if(opname == "WR3"){rule[3] = b;}
			if(opname == "WR4"){rule[4] = b;}
			if(opname == "WR5"){rule[5] = b;}
			if(opname == "WR6"){rule[6] = b;}
			if(opname == "WR7"){rule[7] = b;}
			}
		
		@Override public int getParameter(String paramname){ 
			if(paramname == "Dim"){ return dim;}
			if(paramname == "Rad"){ return radius;}
			if(paramname == "Age"){ return age;}
			if(paramname == "Fade"){ return fade;}
			if(paramname == "Dir"){ return direction;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			return -1;}
		
		@Override public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "Dir"){ direction = a; if(direction < 0){ direction = 0;} if(direction > 3){direction -= 4;}}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			}
			
		public void setRule(int a, boolean b){if(a < 8){rule[a] = b;}}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		@Override public void iterate(){
			 matcount += 1;
			 if(matcount >= mat){matcount = 0;
			 calculate(); }
			 if(ages){ if(active){ if(age == 0){age = 1;} else{age += 1;}}else{ age = 0;} state = age;}
			 if(fades){ if( age >= fade){ purgeState(); age = 0;}}
			
			}
		
		private void calculate(){int cellstate = 0; if(neighbors[2]){cellstate += 4;} 
					if(neighbors[1]){cellstate += 2;} if(neighbors[0]){cellstate ++;}
					active = rule[cellstate];
					
				}
		
		public void purgeState(){ active = false; state = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
		public void setSelf(boolean b){ self = b;}
		
		public void setNeighbors( boolean[] truckdrivin){neighbors = truckdrivin;}
		
		public void setNeighborhood( boolean[][] spozak){neighborhood = spozak;}
		
		public void setEnvironment( boolean[][][] biome){environment = biome;}
		
		public void setState( int a){ state = a;}
		
		public void setNeighborState( int[] address){ neighborstate = address;}
		
		public void setHoodState( int[][] zipcode){ hoodstate = zipcode;}
		
		public void setEnvironmentState( int[][][] planet){envirostate = planet;}
		
		
		
		
}
