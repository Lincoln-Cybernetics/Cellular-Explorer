
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

public class cell{
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
	boolean mirror;
	
	// neighborhood variables
	boolean self;
	boolean[] neighbors;
	boolean[][] neighborhood;
	boolean[][][] environment;
	
	int mystate;
	int[] neighborstate;
	int[][] hoodstate;
	int[][][] envirostate;
	
	//age and fade rule variables
	int age;
	boolean ages;
	int fade;
	boolean fades;
	
	//constructor
	public cell(){
		dim = 0;
		radius = 0;
		active = false;
		state = 0;
		name = "Cell";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		self = false;
		mystate = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;}
		
		//Get and set controls and options
		
		public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Fade"){ return true;}
			if(control == "Mirror"){ return true;}
			 return false;}
		
		public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			if(opname == "Fades"){ return fades;}
			if(opname == "Mirror"){ return mirror;}
			return false;}
		
		public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			if(opname == "Mirror"){mirror = b; if(b){hoodx = 0; hoody = 0; name  ="Mirror";}else{hoodx = -1; hoody = -1; name = "Cell";}}
			}
		
		public int getParameter(String paramname){ 
			if(paramname == "Dim"){ return dim;}
			if(paramname == "Rad"){ return radius;}
			if(paramname == "Age"){ return age;}
			if(paramname == "Fade"){ return fade;}
			if(paramname == "MirrX"){ return hoodx;}
			if(paramname == "MirrY"){ return hoody;}
			return -1;}
		
		public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "MirrX"){hoodx = a;}
			if(paramname == "MirrY"){hoody = a;}
			}
		
		public void setRule(int a, boolean b){}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		public void iterate(){
			 calculate(); 
			 if(ages){ if(active){ if(age == 0){age = 1;} else{age += 1;}}else{ age = 0;} state = age;}
			 if(fades){ if( age >= fade){ purgeState(); age = 0;}}
			}
		
		private void calculate(){if(self){active = true;}else{active = false;}}
		
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
