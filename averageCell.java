
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

public class averageCell extends cell{
	// describe the cell's neighborhood
	int inmode;//input mode
	int outmode;//output mode
	/*Input/Output Modes
	 * 0 = no input
	 * 1 = binary
	 * 2 = Integer
	 */
	 
	brush map;
	// describe the current state of the cell
	
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	boolean mirror;
	
	// neighborhood variables
	
	int[] neighborstate;
	
	// maturity setting
	int mat;
	int matcount;
	
	
	//constructor
	public averageCell(){
		map = new threebrush();
		inmode = 2;
		outmode = 2;
		
		state = 0;
		name = "Average Cell";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		mat = 1;
		matcount = 0;
		neighborstate = new int[9];
		}
		
		//initilization
		public void setLocation(int x, int y){
			map.locate(x,y);
		}
		
		//Get and set controls and options
		
		public boolean getControls(String control){
			if(control == "Xfact"){return true;}
			if(control == "Mirror"){ return true;}
			if(control == "Mat"){ return true;}
			//if(control == "InMode"){return true;}
			 return false;}
		
		public boolean getOption(String opname){ 
			if(opname == "Mirror"){ return mirror;}
			return false;}
		
		public void setOption(String opname, boolean b){
			if(opname == "Mirror"){mirror = b; if(b){hoodx = -1; hoody = 0; name  ="Mirror Average";}else{hoodx = -1; hoody = -1; name = "Average Cell";}}
			}
		
		public int getParameter(String paramname){ 
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			if(paramname == "HoodSize"){return map.getBrushLength();}
			if(paramname == "NextX"){return map.getNextX();}
			if(paramname == "NextY"){return map.getNextY();}
			if(paramname == "MirrX"){ return hoodx;}
			if(paramname == "MirrY"){ return hoody;}
			if(paramname == "InMode"){return inmode;}
			if(paramname == "OutMode"){return outmode;}
			if(paramname == "Xfact"){return map.getParameter("Xfact");}
			return -1;}
		
		public void setParameter(String paramname, int a){
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "MirrX"){hoodx = a; if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "MirrY"){hoody = a;if(mirror){setLocation(hoodx, hoody);}}
			//if(paramname == "InMode"){inmode = a; if(a == 2){ages = false; fades = false;}}
			if(paramname == "Xfact"){map.setParameter("Xfact", a);}
			}
		
		public void setRule(int a, boolean b){}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		public void iterate(){
			 matcount += 1;
			 if(matcount >= mat){matcount = 0;
			 calculate(); }
			}
		
		private void calculate(){
			int total = 0;int samsiz = map.getBrushLength()-1;
			for(int count = 0; count < neighborstate.length; count++){
				if(count == 4){}
				else{total += neighborstate[count];}
				if(total > 1024){total = 1024;}
				if(total < -1024){total = -1024;}
			}
			state = total/samsiz;
			}
			
		
		
		public void purgeState(){  state = 0; }
		
		public void activate(){  state = 1;}
		
		// current state returning methods
		
		public int getState(){ return state;}
		
		public void setState( int a){ state = a;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
	
		
		public void setNeighbors( int[] truckdrivin){
			
		for(int g = 0; g <= truckdrivin.length-1; g++){
		switch(inmode){
			case 0:  break;	
			case 1: if(truckdrivin[g] > 0){neighborstate[g] = 1;}
					else{neighborstate[g] = 0;} break;
			case 2: neighborstate[g] = truckdrivin[g]; break;
			default: neighborstate[g] = truckdrivin[g]; break; 
			}
			}
		}
		
}
