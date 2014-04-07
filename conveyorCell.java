
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
	int inmode; // Input mode: 0 = no input, 1 = binary, 2 = integer
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
	int [] neighborstate;
	
	
	
	
	
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
		inmode = 1;
		active = false;
		state = 0;
		name = "Conveyor";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		direction = 0;
		self = false;
		
		mat = 1;
		matcount = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		neighborstate = new int[9];
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
			if(control == "InMode"){return true;}
			if(control == "Xfact"){return true;}
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

			if(paramname == "HoodSize"){return map.getBrushLength();}
			if(paramname == "NextX"){return map.getNextX();}
			if(paramname == "NextY"){return map.getNextY();}
			if(paramname == "Age"){ return age;}
			if(paramname == "Fade"){ return fade;}
			if(paramname == "Dir"){ return direction;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			if(paramname == "InMode"){return inmode;}
			if(paramname == "Xfact"){return map.getParameter("Xfact");}
			return -1;}
		
		@Override public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "Dir"){ direction = a; if(direction < 0){ direction = 0;} if(direction > 7){direction %= 8;}}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			if(paramname == "InMode"){inmode = a; if(a == 2){ages = false; fades = false;}}
			if(paramname == "Xfact"){map.setParameter("Xfact", a);}
			
			}
			
		
		
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
		
		private void calculate(){
			switch(inmode){
				case 0 : break;
				case 1: active = false; state = 0;
					switch(direction){
						case 0 : if(neighborstate[7] == 1){active = true; state = 1;} break;
						case 1 : if(neighborstate[6] == 1){active = true; state = 1;} break;
						case 2 : if(neighborstate[3] == 1){active = true; state = 1;} break;
						case 3 : if(neighborstate[0] == 1){active = true; state = 1;} break;
						case 4 : if(neighborstate[1] == 1){active = true; state = 1;} break;
						case 5 : if(neighborstate[2] == 1){active = true; state = 1;} break;
						case 6 : if(neighborstate[5] == 1){active = true; state = 1;} break;
						case 7 : if(neighborstate[8] == 1){active = true; state = 1;} break;
						}break;
				case 2:
					switch(direction){
						case 0 : state = neighborstate[7]; break;
						case 1 : state = neighborstate[6]; break;
						case 2 : state = neighborstate[3]; break;
						case 3 : state = neighborstate[0]; break;
						case 4 : state = neighborstate[1]; break;
						case 5 : state = neighborstate[2]; break;
						case 6 : state = neighborstate[5]; break;
						case 7 : state = neighborstate[8]; break;
					}
					if(state < 1){active = false;}else{active = true;}
					break;
					default : break;}
				}
		
		public void purgeState(){ active = false; state = 0;age = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
		public void setSelf(boolean b){ self = b;}
		
		public void setState( int a){ state = a;}
		
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
