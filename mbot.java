
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

public class mbot extends cell{
	// describe the cell's neighborhood
	int dim = -1;//dimensionality
	int radius = 1;// neighborhood radius
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
	boolean[][] neighborhood;
	boolean[][][] environment;
	
	int mystate;
	int[] neighborstate;
	int[][] hoodstate;
	int[][][] envirostate;
	
	// maturity setting
	int mat;
	int matcount;
	
	// born on
	boolean[] born;
	
	//survives on
	boolean[] survives;
	
	//age and fade rule variables
	int age;
	boolean ages;
	int fade;
	boolean fades;
	
	//constructors
	public mbot(){
		map = new threebrush();
		neighbors = new boolean[9];
		//dim = 2;
		//radius = 1;
		active = false;
		state = 0;
		name = "M.B.O.T.";
		hoodx = -1;
		hoody = -1;
		mirror = false;
		self = false;
		mystate = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		matcount = 0;
		mat = 1;
		born = new boolean[9];
		survives = new boolean[9];
		//neighborhood = new boolean[3][3];
		}
		
		public mbot(String type){
			map = new threebrush();
			neighbors = new boolean[9];
		//dim = 2;
		//radius = 1;
		active = false;
		state = 0;
		name = type;
		hoodx = -1;
		hoody = -1;
		mirror = false;
		self = false;
		mystate = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		matcount = 0;
		mat = 1;
		born = new boolean[9];
		survives = new boolean[9];
		//neighborhood = new boolean[3][3];
		
		for(int n = 0; n < 9; n++){
			born[n] = false; survives[n] = false;
		if( name == "2x2"){ if(n == 3 || n == 6){born[n] = true;} if(n == 1|| n == 2|| n == 5){survives[n] = true;}}
		if( name == "3/4 Life"){ if(n == 3 || n == 4){born[n] = true; survives[n] = true;} }
		if( name == "Amoeba"){ if(n == 3 || n == 5 || n == 7){born[n] = true;} if(n == 1 || n == 3||  n == 5|| n == 8){survives[n] = true;}}
		if( name == "Assimilation"){ if(n == 3 || n==4 || n==5){born[n] = true;} if(n == 4 || n == 5 || n==6 || n==7 ){survives[n] = true;}}
		if( name == "Coagulations"){ if(n == 3 ||  n == 7 || n == 8){born[n] = true;} if( n == 2|| n == 3||n == 5|| n == 6||n == 7 || n == 8){survives[n] = true;}}
		if( name == "Coral"){ if(n == 3){born[n] = true;} if(n == 4 || n == 5 || n==6 || n==7 || n == 8){survives[n] = true;}}
		if( name == "Day and Night"){ if(n == 3 ||  n == 6 || n == 7 || n == 8){born[n] = true;} if( n == 3|| n == 4 || n == 6|| n == 7|| n == 8){survives[n] = true;}}
		if( name == "Diamoeba"){ if(n == 3 || n == 5 || n == 6 || n == 7 || n == 8){born[n] = true;} if( n == 5|| n == 6 || n == 7 || n == 8){survives[n] = true;}}
		if( name == "Dot Life"){ if(n == 3){born[n] = true;} if(n == 0|| n == 2|| n == 3){survives[n] = true;}}
		if( name == "Dry Life"){ if(n == 3 ||  n == 7){born[n] = true;} if( n == 2|| n == 3){survives[n] = true;}}
		if(name == "Fredkin"){if(n == 1 || n == 3 || n == 5 || n == 7){born[n] = true;}else{ survives[n] = true;} }
		if(name == "Gnarl"){if(n == 1){born[n] = true;} if (n == 1){survives[n] = true;}}
		if( name == "High Life"){ if(n == 3 ||  n == 6){born[n] = true;} if( n == 2|| n == 3){survives[n] = true;}}
		if(name == "Life"){if(n == 3){born[n] = true;} if (n == 3 || n == 2){survives[n] = true;}}
		if( name == "Life without Death"){ if(n == 3){born[n] = true;} survives[n] = true;}
		if( name == "Live Free or Die"){ if(n == 2){born[n] = true;} if(n == 0){survives[n] = true;}}
		if( name == "Long Life"){ if(n == 3 || n == 4 || n == 5){born[n] = true;} if( n == 5){survives[n] = true;}}
		if( name == "Maze"){ if(n == 3){born[n] = true;} if(n == 1 || n == 2 || n==3 || n==4 || n == 5){survives[n] = true;}}
		if( name == "Mazectric"){ if(n == 3){born[n] = true;} if(n == 1 || n == 2 || n==3 || n==4 ){survives[n] = true;}}
		if( name == "Move"){ if(n == 3 || n == 6 || n == 8){born[n] = true;} if(n == 2|| n == 4|| n == 5){survives[n] = true;}}
		if( name == "Pseudo-life"){ if(n == 3 || n == 5 || n == 7){born[n] = true;} if( n == 2 || n == 3 || n == 8){survives[n] = true;}}
		if(name == "Replicator"){if(n == 1 || n == 3 || n == 5 || n == 7){born[n] = true; survives[n] = true;} }
		if(name == "Seeds"){if(n == 2){born[n] = true;}}
		if( name == "Serviettes"){ if(n == 2 || n == 3 || n == 4){born[n] = true;}}
		if( name == "Stains"){ if(n == 3 || n == 6 || n == 7|| n == 8){born[n] = true;} if( n == 2|| n == 3|| n == 5|| n == 6|| n == 7|| n == 8){survives[n] = true;}}
		if( name == "Vote"){ if(n > 4){born[n] = true;} if(n >3){survives[n] = true;}}
		if( name == "Vote 4/5"){ if(n ==4 || n == 6 || n == 7|| n ==8){born[n] = true;} if( n == 3|| n == 5|| n == 6|| n == 7|| n == 8){survives[n] = true;}}
		if( name == "Walled Cities"){ if(n > 3){born[n] = true;} if( n == 2|| n == 3|| n == 4|| n == 5){survives[n] = true;}}
		if( name == "OnCell"){born[n] = true; survives[n] = true;}
		if( name == "OffCell"){born[n] = false; survives[n] = false;}
		if( name == "BlinkCell"){born[n] = true; survives[n] = false;}
		}
		if(name =="Live Free or Die"){name = "L.F.O.D.";}
		
	}
		
		//initilization
		public void setLocation(int x, int y){
			map.locate(x,y);
		}
		
		//Get and set controls and options
		
		public String getName(){ return name;}
		
		@Override public boolean getControls(String control){
			if(control == "Age"){ if(name == "OffCell"){return false;}else{ return true;}}
			if(control == "Fade"){ if(name == "OffCell" || name == "BlinkCell"){return false;}else{ return true;}}
			if(control == "Mat"){if(name == "OffCell" || name =="OnCell"){return false;}else{return true;}}
			if(control == "Born"){if(name == "OnCell" || name == "OffCell" || name == "BlinkCell"){return false;}else{ return true;}}
			if(control == "Survives"){if(name == "OnCell" || name == "OffCell" || name == "BlinkCell"){return false;}else{ return true;}}
			if(control == "Mirror"){if(name == "OnCell" || name == "OffCell" || name == "BlinkCell"){return false;}else{ return true;}}
			 return false;}
		
		@Override public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			if(opname == "Fades"){ return fades;}
			if(opname == "B0"){return born[0];}if(opname == "B1"){return born[1];}
			if(opname == "B2"){return born[2];}if(opname == "B3"){return born[3];}
			if(opname == "B4"){return born[4];}if(opname == "B5"){return born[5];}
			if(opname == "B6"){return born[6];}if(opname == "B7"){return born[7];}
			if(opname == "B8"){return born[8];}
			
			if(opname == "S0"){return survives[0];}if(opname == "S1"){return survives[1];}
			if(opname == "S2"){return survives[2];}if(opname == "S3"){return survives[3];}
			if(opname == "S4"){return survives[4];}if(opname == "S5"){return survives[5];}
			if(opname == "S6"){return survives[6];}if(opname == "S7"){return survives[7];}
			if(opname == "S8"){return survives[8];}
			if(opname == "Mirror"){ return mirror;}
			return false;}
		
		@Override public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			if(opname == "B0"){ born[0]=b;}if(opname == "B1"){ born[1]=b;}
			if(opname == "B2"){ born[2]=b;}if(opname == "B3"){ born[3]=b;}
			if(opname == "B4"){ born[4]=b;}if(opname == "B5"){ born[5]=b;}
			if(opname == "B6"){ born[6]=b;}if(opname == "B7"){ born[7]=b;}
			if(opname == "B8"){ born[8]=b;}
			
			if(opname == "S0"){ survives[0]=b;}if(opname == "S1"){ survives[1]=b;}
			if(opname == "S2"){ survives[2]=b;}if(opname == "S3"){ survives[3]=b;}
			if(opname == "S4"){ survives[4]=b;}if(opname == "S5"){ survives[5]=b;}
			if(opname == "S6"){survives[6]=b;}if(opname == "S7"){ survives[7]=b;}
			if(opname == "S8"){ survives[8]=b;}
			if(name == "OnCell" || name == "OffCell" || name == "BlinkCell"){} else{
				if(opname == "Mirror"){mirror = b; if(b){hoodx = -1; hoody = 0; name  ="Mirror-" + name;}
				else{hoodx = -1; hoody = -1; name = "M.B.O.T.";}}
				}
			}
		
		@Override public int getParameter(String paramname){ 
			if(paramname == "Dim"){ return dim;}
			if(paramname == "Rad"){ return radius;}
			if(paramname == "HoodSize"){return map.getBrushLength();}
			if(paramname == "NextX"){return map.getNextX();}
			if(paramname == "NextY"){return map.getNextY();}
			if(paramname == "Age"){ return age;}
			if(paramname == "Fade"){ return fade;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			if(paramname == "MirrX"){ return hoodx;}
			if(paramname == "MirrY"){ return hoody;}
			return -1;}
		
		@Override public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			if(paramname == "MirrX"){hoodx = a;if(mirror){setLocation(hoodx, hoody);}}
			if(paramname == "MirrY"){hoody = a;if(mirror){setLocation(hoodx, hoody);}}
			}
			
		@Override public void setRule(int a, boolean b){if(a < 9){born[a] = b;}else{if(a < 18){survives[a-9] =b;}}}
		
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
			int cellstate = 0;
			for (int v = 0; v < neighbors.length; v++){
				if(v == 4){self = neighbors[v];}else{if(neighbors[v]){cellstate += 1;}}
				}
				if(self){active = survives[cellstate];} else{active = born[cellstate];}
			}
		
		
		
		public void purgeState(){ active = false; state = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		
		
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
