
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
	int inmode;//input mode
	/*Input Modes
	 * 0 = no input
	 * 1 = binary
	 * 2 = Integer
	 */
	brush map;
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
	
	
	int mystate;
	int[] neighborstate;
	
	
	//age and fade rule variables
	int age;
	boolean ages;
	int fade;
	boolean fades;
	
	//constructor
	public cell(){
		map = new onebrush();
		inmode = 1;
		active = false;
		state = 0;
		name = "Cell";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		self = false;
		
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		neighbors = new boolean[1];
		neighborstate = new int[1];
		}
		
		//initilization
		public void setLocation(int x, int y){
			map.locate(x,y);
		}
		
		//Get and set controls and options
		
		public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Fade"){ return true;}
			if(control == "Mirror"){ return true;}
			if(control == "InMode"){return true;}
			 return false;}
		
		public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			if(opname == "Fades"){ return fades;}
			if(opname == "Mirror"){ return mirror;}
			return false;}
		
		public void setOption(String opname, boolean b){
			
			if(inmode != 2){
			if(opname == "Ages"){ages = b; if(b == false){ fades = false; if(active){age = 1;}else{age = 0;}}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			}
			
			if(opname == "Mirror"){mirror = b; if(b){hoodx = -1; hoody = 0; name  ="Mirror";}else{hoodx = -1; hoody = -1; name = "Cell";}}
			}
		
		public int getParameter(String paramname){ 
			
			if(paramname == "HoodSize"){return map.getBrushLength();}
			if(paramname == "NextX"){return map.getNextX();}
			if(paramname == "NextY"){return map.getNextY();}
			if(paramname == "Age"){ return age;}
			if(paramname == "Fade"){ return fade;}
			if(paramname == "MirrX"){ return hoodx;}
			if(paramname == "MirrY"){ return hoody;}
			if(paramname == "InMode"){return inmode;}
			return -1;}
		
		public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "MirrX"){hoodx = a; if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "MirrY"){hoody = a;if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "InMode"){inmode = a; if(a == 2){ages = false; fades = false;}}
			}
		
		public void setRule(int a, boolean b){}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		public void iterate(){
			 calculate(); 
			 if(ages){ if(active){ if(age == 0){age = 1;} else{age += 1;}}else{ age = 0;} 
				if(fades){ if( age >= fade){ purgeState(); age = 0;}} 
				if( age > 1023){ age = 1023; }state = agify(age);}
			}
		
		private void calculate(){
			switch(inmode){
				case 0: break;
				case 1: if(neighborstate[0] == 1){active = true; state = 1;}else{active = false; state = 0;}break;
				case 2: state = neighborstate[0]; if(state < 1){active = false;} else{active = true;}break;
				default:state = neighborstate[0]; if(state < 1){active = false;} else{active = true;} break;}
			}
			
		
			
		protected int agify(int a){
									if(a <= 0){return 0;}
									if(a == 1){return 1;}
									if(a > 1 && a < 4){return 2;}
									if(a > 3 && a < 8){return 3;}
									if(a > 7 && a < 16){return 4;}
									if(a > 15 && a < 32){return 5;}
									if(a > 31 && a < 64){return 6;}
									if(a > 63 && a < 128){return 7;}
									if(a > 127 && a < 256){return 8;}
									if(a > 255 && a < 512){return 9;}
									if( a > 511 && a < 1024){return 10;}
									 
									return -1;
								}
		
		public void purgeState(){ active = false; state = 0; age = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		public void setState( int a){ state = a;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
	
		
		public void setNeighbors( int[] truckdrivin){
			
		for(int g = 0; g <= truckdrivin.length-1; g++){
		switch(inmode){
			case 0: neighborstate[g] = 0; break;	
			case 1: if(truckdrivin[g] > 0){neighborstate[g] = 1;}
					else{neighborstate[g] = 0;} break;
			case 2: neighborstate[g] = truckdrivin[g]; break;
			default: neighborstate[g] = truckdrivin[g]; break; 
			}
			}
		}
		
		
		
		
		
		
		
		
		
}
