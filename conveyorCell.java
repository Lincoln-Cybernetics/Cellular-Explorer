
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

public class conveyorCell extends cell{
	// describe the cell's neighborhood
	int dim;//dimensionality
	int radius;
	brush map;
	// describe the current state of the cell
	boolean active;
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	boolean mirror;
	// Conveyor variables: direction indicates direction of conveyance 
	// 0 = up; 1 = upper-right; 2 = right; 3 = lower-right; 
	// 4 =  down; 5 = lower-left; 6 = left; 7 = upper-left; 
	int direction;
	
	
	// neighborhood variables
	boolean self;
	boolean[] neighbors;
	boolean[][] neighborhood;
	boolean[][][] environment;
	
	int mystate;
	int[] neighborstate;
	int[][] hoodstate;
	int[][][] envirostate;
	
	
	// maturity setting
	int mat;
	int matcount;
	
	//age and fade rule variables
	int age;
	boolean ages;
	int fade;
	boolean fades;
	
	//constructor
	public conveyorCell(){
		map = new threebrush();
		dim = -1;
		radius = 1;
		active = false;
		state = 0;
		name = "Conveyor";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		direction = 0;
		self = false;
		mystate = 0;
		mat = 1;
		matcount = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		//neighborhood = new boolean[3][3];
		neighbors = new boolean[9];
		}
		
		//initilization
		public void setLocation(int x, int y){
			map.locate(x,y);}
		//Get and set controls and options
		
		@Override public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Fade"){return true;}
			if(control == "Mat"){ return true;}
			if(control == "Dir"){ return true;}
			 return false;}
		
		@Override public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			if(opname == "Fades"){ return fades;}
			return false;}
		
		@Override public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			}
		
		@Override public int getParameter(String paramname){ 
			if(paramname == "Dim"){ return dim;}
			if(paramname == "Rad"){ return radius;}
			if(paramname == "HoodSize"){return map.getBrushLength();}
			if(paramname == "NextX"){return map.getNextX();}
			if(paramname == "NextY"){return map.getNextY();}
			if(paramname == "Age"){ return age;}
			if(paramname == "Fade"){ return fade;}
			if(paramname == "Dir"){ return direction;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			return -1;}
		
		@Override public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "Dir"){ direction = a; if(direction < 0){ direction = 0;} if(direction > 7){direction %= 8;}}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			
			}
			
		
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		@Override public void iterate(){
			 matcount += 1;
			 if(matcount >= mat){matcount = 0;
			 calculate(); }
			 if(ages){ if(active){ if(age == 0){age = 1;} else{age += 1;}}else{ age = 0;} state = age;}
			 else{if(active){state = 1;}else{state = 0;}}
			  if(fades){ if( age >= fade){ purgeState(); age = 0;}}
			}
		
		private void calculate(){
			switch(direction){
				case 0 : active = neighbors[7]; break;
				case 1 : active = neighbors[6]; break;
				case 2 : active = neighbors[3]; break;
				case 3 : active = neighbors[0]; break;
				case 4 : active = neighbors[1]; break;
				case 5 : active = neighbors[2]; break;
				case 6 : active = neighbors[5]; break;
				case 7 : active = neighbors[8]; break;
			}
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
