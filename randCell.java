import java.util.Random;
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

public class randCell extends cell{
	// describe the cell's neighborhood
	int inmode;//input mode 0 = no input
	
	// describe the current state of the cell
	boolean active;
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	
	// neighborhood variables
	
	
	int mystate;
	
	
	// maturity setting
	int mat;
	int matcount;
	
	//age and fade rule variables
	int age;
	boolean ages;
	
	// Random number generator
	Random bombadil;
	
	
	//constructor
	public randCell(){
		inmode = 0;
		active = false;
		state = 0;
		name = "Random";
		hoodx = -1;
		hoody = -1;
		self = false;
		//mystate = 0;
		age = 0;
		ages = false;
		mat = 1;
		matcount = 0;
		bombadil = new Random();
		}
			//initilization
		public void setLocation(int x, int y){
			
		}
		//Get and set controls and options
		
		public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Mat"){return true;}
			 return false;}
		
		public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			return false;}
		
		public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			
			}
		
		public int getParameter(String paramname){ 
			
			if(paramname == "HoodSize"){return 0;}
			if(paramname == "NextX"){return -1;}
			if(paramname == "NextY"){return -1;}
			if(paramname == "Age"){ return age;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			if(paramname == "InMode"){return inmode;}
			return -1;}
		
		public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
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
			 if(ages){ if(active){ age = bombadil.nextInt(1024);}else{ age = bombadil.nextInt(1024)*-1;} state = agify(age);}
			 else{if(active){state = 1;}else{state = 0;}}
			
			}
			
		
		
		private void calculate(){if(bombadil.nextBoolean()){active = true;}else{active = false;}}
		
		public void purgeState(){ active = false; state = 0;age = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
		
		public void setState( int a){ state = a;}
		
		
}
