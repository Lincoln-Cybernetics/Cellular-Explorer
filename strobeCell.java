
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

public class strobeCell extends cell{
	// describe the cell's neighborhood
	int inmode;//input mode 0 = no input
	
	// describe the current state of the cell
	boolean active;
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	boolean mirror;
	
	// neighborhood variables
	
	
	//int mystate;
	int[] neighborstate;
	
	
	//maturity variables
	int mat;
	int matcount;
	
	//constructor
	public strobeCell(){
		inmode = 0;
		active = false;
		state = 0;
		name = "Strobe";
		mat = 1;
		matcount = 0;
		hoodx = -1;
		hoody = -1;
		mirror = false;
		self = false;
		//mystate = 0;
		}
			//initilization
		public void setLocation(int x, int y){
		
		}
		//Get and set controls and options
		
		public boolean getControls(String control){
			if(control == "Mat"){ return true;}
			 return false;}
		
		public boolean getOption(String opname){ 
			return false;}
		
		public void setOption(String opname, boolean b){
			}
		
		public int getParameter(String paramname){ 
			
			if(paramname == "HoodSize"){return 0;}
			if(paramname == "NextX"){return -1;}
			if(paramname == "NextY"){return -1;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			if(paramname == "InMode"){return inmode;}
			return -1;}
		
		public void setParameter(String paramname, int a){
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			}
		
		public void setRule(int a, boolean b){}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		public void iterate(){
			 matcount += 1;
			 if(matcount >= mat){matcount = 0;
			 calculate(); }
			 else{purgeState();}
			}
		
		private void calculate(){activate();}
		
		public void purgeState(){ active = false; state = 0;age = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
		
		
		public void setState( int a){ state = a;}
		
	
}
