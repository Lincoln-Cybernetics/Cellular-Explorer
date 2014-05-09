
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
	int inmode; //input mode 1 = binary
	brush map;
	int outmode;//output mode
	
	// describe the current state of the cell
	boolean active;
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	int direction;
	boolean mirror;
	// in Wolfram, direction indicates the direction traveled 
	//from the center cell towards the least significant bit in the neighborhood
	
	// neighborhood variables

	
	
	int[] neighborstate;

	
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
		map = new spinbrush();
		inmode = 1;
		outmode = 1;
		active = false;
		state = 0;
		name = "Wolfram";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		direction = 0;
		self = false;
		//mystate = 0;
		mat = 1;
		matcount = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		rule = new boolean[8];
		neighborstate = new int[3];
		}
		//initilization
		public void setLocation(int x, int y){
			map.locate(x,y);
		}
		
		//Get and set controls and options
		
		@Override public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Fade"){ return true;}
			if(control == "WolfRule"){ return true;}
			if(control == "Mat"){ return true;}
			if(control == "Orient"){ return true;}
			if(control == "Mirror"){ return true;}
			if(control == "Xfact"){return true;}
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
			if(opname == "Mirror"){ return mirror;}
			return false;}
		
		@Override public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b; if(b == false){outmode = 1; fades = false; if(active){age = 1;}else{age = 0;}}else{outmode = 2;}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			if(opname == "WR0"){rule[0] = b;}
			if(opname == "WR1"){rule[1] = b;}
			if(opname == "WR2"){rule[2] = b;}
			if(opname == "WR3"){rule[3] = b;}
			if(opname == "WR4"){rule[4] = b;}
			if(opname == "WR5"){rule[5] = b;}
			if(opname == "WR6"){rule[6] = b;}
			if(opname == "WR7"){rule[7] = b;}
			if(opname == "Mirror"){mirror = b; if(b){hoodx = -1; hoody = 0; name  ="Mirror-"+name;}else{hoodx = -1; hoody = -1; name = "Wolfram";}}
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
			if(paramname == "WolfRule"){int wn = 0; if(rule[7]){wn += 128;} if(rule[6]){wn += 64;} if(rule[5]){wn += 32;} if(rule[4]){wn += 16;}
				if(rule[3]){wn += 8;} if(rule[2]){wn += 4;} if(rule[1]){wn += 2;} if(rule[0]){wn += 1;} return wn;}
			if(paramname == "InMode"){return inmode;}
			if(paramname == "Xfact"){return map.getParameter("Xfact");}
			if(paramname == "OutMode"){return outmode;}
			return -1;}
		
		@Override public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "Dir"){ direction = a; if(direction < 0){ direction = 0;} if(direction > 3){direction %= 4;}map.setOrientation(direction);}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			if(paramname == "MirrX"){hoodx = a;if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "MirrY"){hoody = a;if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "Xfact"){map.setParameter("Xfact", a);}
			}
			
		public void setRule(int a, boolean b){if(a < 8){rule[a] = b;}}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		@Override public void iterate(){
			 matcount += 1;
			 if(matcount >= mat){matcount = 0;
			 calculate(); }
			 if(ages){ if(active){ if(age == 0){age = 1;} else{age += 1;}}else{ age = 0;} 
				if(fades){ if( age >= fade){ purgeState(); age = 0;}}
				if( age > 1023){ age = 1023; }state = agify(age);}
			 else{if(active){state = 1;}else{state = 0;}}
			
			}
		
		private void calculate(){int cellstate = 0; if(neighborstate[2] == 1){cellstate ++;}else{} 
					if(neighborstate[1] == 1){cellstate += 2;}else{}  if(neighborstate[0] == 1){cellstate +=4;}else{} 
					active = rule[cellstate];
					
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
