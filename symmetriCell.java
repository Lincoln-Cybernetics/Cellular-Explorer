
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

public class symmetriCell extends cell{
	// describe the cell's neighborhood
	int inmode; //input mode 0 = no input, 1 = binary, 2 = integer
	brush map;
	// describe the current state of the cell
	boolean active;
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	boolean mirror;
	// Symmetry variables: direction indicates the axis of symmetry 
	// 0 = vertical; 1 = lower-left to upper right; 2 = horizontal; 3 = upper-left to lower-right
	int direction;
	boolean any;
	boolean all;
	
	// neighborhood variables
	
	
	int mystate;
	int[] neighborstate;
	
	
	
	// maturity setting
	int mat;
	int matcount;
	
	//age and fade rule variables
	int age;
	boolean ages;
	int fade;
	boolean fades;
	
	//constructor
	public symmetriCell(){
		map = new threebrush();
		inmode = 1;
		active = false;
		state = 0;
		name = "Symmetrical";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		direction = 0;
		any = false;
		all = false;
		self = false;
		
		mat = 1;
		matcount = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		//neighbors = new boolean[9];
		neighborstate = new int[9];
		}
		//initilization
		public void setLocation(int x, int y){
			map.locate(x,y);
		}
		
		//Get and set controls and options
		
		@Override public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Fade"){ return true;}
			if(control == "Mat"){ return true;}
			if(control == "Orient"){ return true;}
			if(control == "Mirror"){ return true;}
			if(control == "Any"){return true;}
			if(control == "All"){return true;}
			if(control == "InMode"){return true;}
			if(control == "Xfact"){return true;}
			 return false;}
		
		@Override public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			if(opname == "Fades"){ return fades;}
			if(opname == "Any"){return any;}
			if(opname == "All"){return all;}
			if(opname == "Mirror"){ return mirror;}
			return false;}
		
		@Override public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			if(opname == "Any"){any = b; if(b){all = false;}}
			if(opname == "All"){all = b; if(b){any = false;}}
			if(opname == "Mirror"){mirror = b; if(b){hoodx = -1; hoody = 0; name  ="Mirror-"+name;}else{hoodx = -1; hoody = -1; name = "Symmetrical";}}
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
			if(paramname == "MirrX"){ return hoodx;}
			if(paramname == "MirrY"){ return hoody;}
			if(paramname == "InMode"){return inmode;}
			if(paramname == "Xfact"){return map.getParameter("Xfact");}
			return -1;}
		
		@Override public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "Dir"){ direction = a; if(direction < 0){ direction = 0;}
			 if(direction > 3){direction %= 4;}any = false; all = false;}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			if(paramname == "MirrX"){hoodx = a;if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "MirrY"){hoody = a;if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "InMode"){inmode = a;}
			if(paramname == "Xfact"){map.setParameter("Xfact", a);}
			}
			
		
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		@Override public void iterate(){
			 matcount += 1;
			 if(matcount >= mat){matcount = 0;
			 calculate(); }
			 if(ages){ if(active){ if(age == 0){age = 1;} else{age += 1;}}else{ age = 0;} 
				if(fades){ if( age >= fade){ purgeState(); age = 0;}}
				state = agify(age);}
			 else{if(active){state = 1;}else{state = 0;}}
			
			}
		
		private void calculate(){
			 boolean[] symms = new boolean[4];
				//y-axis
				if(direction == 0 || any || all){ 
					 symms[0] = false;
					if(neighborstate[0] == neighborstate[2] && neighborstate[3] == neighborstate[5] && neighborstate[6] == neighborstate[8])
					{symms[0] = true;}
					}
				//LL-UR
				if(direction == 1 || any || all){
					symms[1] = false;
					if(neighborstate[1] == neighborstate[5] && neighborstate[0] == neighborstate[8] && neighborstate[3] == neighborstate[7])
					{symms[1] = true;}
				}
				//x-axis
				if(direction == 2 || any || all){
					 symms[2] = false;
					if(neighborstate[0] == neighborstate[6] && neighborstate[1] == neighborstate[7] && neighborstate[2] == neighborstate[8])
					{symms[2] = true;}
				}
				//UL-LR
				if(direction == 3 || any || all){
					 symms[3] = false;
					if(neighborstate[3] == neighborstate[1] && neighborstate[6] == neighborstate[2] && neighborstate[7] == neighborstate[5])
					{symms[3] = true;}
				}
				
				if(any){active = false;if(symms[0] || symms[1] || symms[2] || symms[3]){active = true;}}
				else{ active = symms[direction];}
				if(all){active = false;if(symms[0] && symms[1] && symms[2] && symms[3]){active = true;}}
				}
		
		public void purgeState(){ active = false; state = 0;age = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
		
		public void setNeighbors( int[] truckdrivin){
			//System.out.println(truckdrivin.length);
			//System.out.println(neighborstate.length);
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
		
		public void setState( int a){ state = a;}
		
		
}
