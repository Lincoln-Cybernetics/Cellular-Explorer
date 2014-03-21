
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

public class brush{
	int xloc;
	int[][] xlocs;
	int yloc;
	int[][] ylocs;
	int xcount = 0;
	int ycount = 0;
	int bristles;
	int orientation = 0;
	
	//constructor
	public brush(){ 
		bristles = 1;
		xlocs = new int[bristles][1];
		ylocs = new int[bristles][1];
		xlocs[0][0] = 0; ylocs[0][0] = 0;
		}
	
		//tells how many cells the brush affects
		public int getBrushLength(){
			return bristles;}
			
		//tells the brush where it is
		public void locate(int x, int y){
			xloc = x;
			yloc = y;}

		//tells the brush what direction it is facing	
		public void setOrientation(int a){
			orientation = 0;}
			
		//tells what options and settings are available for this brush
		public boolean getControls(String a){
			return false;}
			
		//used for setting options
		public void setOption(String opname, boolean b){
		}
		
		//returns the x coordinates of each of the affected cells in turn	
		public int getNextX(){
			int currentx;
			currentx = calculateX();
			xcount += 1;
			if(xcount >= bristles){xcount = 0;}
			return currentx;
		}
		
		//calculates the x coordinates
		private int calculateX(){
			return xloc + xlocs[xcount][orientation];}
		
		//returns the y coordinates of each of the affected cells in turn	
		public int getNextY(){
			int currenty;
			currenty = calculateY();
			ycount += 1;
			if(ycount >= bristles){ycount = 0;}
			return currenty;
		}
		
		// calculates the y-coordinates
		private int calculateY(){
			return yloc + ylocs[ycount][orientation];}
			

}

class onebrush extends brush{
	
	//constructor
	public onebrush(){
		int bristles = 1;
		xlocs = new int[bristles][1];
		ylocs = new int[bristles][1];
		xlocs[0][0] = 0;
		ylocs[0][0] = 0;
		}
	
		
		//calculates the x-coordinates for each cell the brush affects
		private int calculateX(){
				return xloc + xlocs[xcount][orientation];}
		
	//calculates the y-coordinates for each cell the brush affects
		private int calculateY(){
			return yloc + ylocs[ycount][orientation];
			}
			

}

class twobrush extends brush{
	
	public twobrush(){
		bristles = 4;
		xlocs = new int[bristles][1];
		ylocs = new int[bristles][1];
		xlocs[0][0] = 0; xlocs[1][0] = 1; xlocs[2][0] = 0; xlocs[3][0] = 1;
		ylocs[0][0] = 0; ylocs[1][0] = 0; ylocs[2][0] = 1; ylocs[3][0] = 1;
		}
	
			
		//calculates the x-coordinates for each cell the brush affects
		private int calculateX(){
				return xloc + xlocs[xcount][orientation];}
		
	//calculates the y-coordinates for each cell the brush affects
		private int calculateY(){
			return yloc + ylocs[ycount][orientation];
			}
}

class threebrush extends brush{
	
	public threebrush(){
		bristles = 9;
		xlocs = new int[9][1];
		ylocs = new int[9][1];
		xlocs[0][0] = -1; xlocs[1][0] = 0; xlocs[2][0] = 1;
		ylocs[0][0] = -1; ylocs[1][0] = -1;ylocs[2][0] = -1;
		xlocs[3][0] = -1; xlocs[4][0] = 0; xlocs[5][0] = 1;
		ylocs[3][0] = 0; ylocs[4][0] = 0; ylocs[5][0] = 0;
		xlocs[6][0] = -1; xlocs[7][0] = 0; xlocs[8][0] = 1;
		ylocs[6][0] = 1; ylocs[7][0] = 1; ylocs[8][0] = 1;
		}
	
		//calculates the x-coordinates for each cell the brush affects
		private int calculateX(){
				return xloc + xlocs[xcount][orientation];}
				
		//calculates the y-coordinates for each cell the brush affects
		private int calculateY(){
			return yloc + ylocs[ycount][orientation];
			}
}
		
class gliderbrush extends brush{
	
	public gliderbrush(){
		bristles = 5;
		xlocs = new int[5][8];
		ylocs = new int[5][8];
		//orientation 0
		xlocs[0][0] = 0; xlocs[1][0] = -1; xlocs[2][0] = -2; xlocs[3][0] = 0; xlocs[4][0] = -1;
		ylocs[0][0] = 0; ylocs[1][0] = 0; ylocs[2][0] = 0; ylocs[3][0] = -1; ylocs[4][0] = -2;
		//orientation 1
		xlocs[0][1] = 0; xlocs[1][1] = 1; xlocs[2][1] = 2; xlocs[3][1] = 0; xlocs[4][1] = 1;
		ylocs[0][1] = 0; ylocs[1][1] = 0; ylocs[2][1] = 0; ylocs[3][1] = -1; ylocs[4][1] = -2;
		//orientation 2
		xlocs[0][2] = 0; xlocs[1][2] = -1; xlocs[2][2] = -2; xlocs[3][2] = 0; xlocs[4][2] = -1;
		ylocs[0][2] = 0; ylocs[1][2] = 0; ylocs[2][2] = 0; ylocs[3][2] = 1; ylocs[4][2] = 2;
		//orientation 3
		xlocs[0][3] = 0; xlocs[1][3] = 1; xlocs[2][3] = 2; xlocs[3][3] = 0; xlocs[4][3] = 1;
		ylocs[0][3] = 0; ylocs[1][3] = 0; ylocs[2][3] = 0; ylocs[3][3] = 1; ylocs[4][3] = 2;
		//orientation 4
		xlocs[0][4] = 0; xlocs[1][4] = 0; xlocs[2][4] = 0; xlocs[3][4] = -1; xlocs[4][4] = -2;
		ylocs[0][4] = 0; ylocs[1][4] = -1; ylocs[2][4] = -2; ylocs[3][4] = 0; ylocs[4][4] = -1;
		//orientation 5
		xlocs[0][5] = 0; xlocs[1][5] = 0; xlocs[2][5] = 0; xlocs[3][5] = 1; xlocs[4][5] = 2;
		ylocs[0][5] = 0; ylocs[1][5] = -1; ylocs[2][5] = -2; ylocs[3][5] = 0; ylocs[4][5] = -1;
		//orientation 6
		xlocs[0][6] = 0; xlocs[1][6] = 0; xlocs[2][6] = 0; xlocs[3][6] = -1; xlocs[4][6] = -2;
		ylocs[0][6] = 0; ylocs[1][6] = 1; ylocs[2][6] = 2; ylocs[3][6] = 0; ylocs[4][6] = 1;
		//orientation 7
		xlocs[0][7] = 0; xlocs[1][7] = 0; xlocs[2][7] = 0; xlocs[3][7] = 1; xlocs[4][7] = 2;
		ylocs[0][7] = 0; ylocs[1][7] = 1; ylocs[2][7] = 2; ylocs[3][7] = 0; ylocs[4][7] = 1;
		
		}
		
	@Override public void setOrientation(int a){
			if(a > 7 || a < 0){orientation = a%8;} else{orientation = a;}}
	
	private int calculateX(){
		return xloc+xlocs[xcount][orientation];
		}
	
	private int calculateY(){
		return yloc+ylocs[ycount][orientation];
			}
			
@Override public boolean getControls(String a){
			if(a == "Dir"){return true;}
			return false;}
}

class spinbrush extends brush{
	//orientation only goes 0-3
	//0 =  vertical
	//1 =  starts lower-left
	//2 =  horizontal
	//3 =  starts upper-left
	public spinbrush(){
		bristles = 3;
		xlocs = new int[bristles][4];
		ylocs = new int[bristles][4];
		//orient 0
		xlocs[0][0] = 0; xlocs[1][0] = 0; xlocs[2][0] = 0;
		ylocs[0][0] = -1; ylocs[1][0] = 0; ylocs[2][0] = 1;
		//orient 1
		xlocs[0][1] = -1; xlocs[1][1] = 0; xlocs[2][1] = 1;
		ylocs[0][1] = 1; ylocs[1][1] = 0; ylocs[2][0] = -1;
		//orient 2
		xlocs[0][2] = -1; xlocs[1][2] = 0; xlocs[2][2] = 1;
		ylocs[0][2] = 0; ylocs[1][2] = 0; ylocs[2][2] = 0;
		//orient 3
		xlocs[0][3] = -1; xlocs[1][3] = 0; xlocs[2][3] = 1;
		ylocs[0][3] = -1; ylocs[1][3] = 0; ylocs[2][0] = 1;
		}
		
	@Override public void setOrientation(int a){
			if(a > 3 || a < 0){orientation = a%4;} else{orientation = a;}}
		
	private int calculateX(){
		return xloc+xlocs[xcount][orientation];
		}
	
	private int calculateY(){
		return yloc+ylocs[ycount][orientation];}
			
}

class rpentbrush extends brush{
	
	
	public rpentbrush(){
		bristles = 5;
		xlocs = new int[bristles][8];
		ylocs = new int[bristles][8];
		//orientation 0
		xlocs[0][0] = 0; xlocs[1][0] = 0; xlocs[2][0] = 0; xlocs[3][0] = -1; xlocs[4][0] = 1;
		ylocs[0][0] = 0; ylocs[1][0] = 1; ylocs[2][0] = -1; ylocs[3][0] = 0; ylocs[4][0] = -1;
		//orientation 1
		xlocs[0][1] = 0; xlocs[1][1] = -1; xlocs[2][1] = 1; xlocs[3][1] = 0; xlocs[4][1] = 1;
		ylocs[0][1] = 0; ylocs[1][1] = 0; ylocs[2][1] = 0; ylocs[3][1] = -1; ylocs[4][1] = 1;
		//orientation 2
		xlocs[0][2] = 0; xlocs[1][2] = 0; xlocs[2][2] = 0; xlocs[3][2] = 1; xlocs[4][2] = -1;
		ylocs[0][2] = 0; ylocs[1][2] = -1; ylocs[2][2] = 1; ylocs[3][2] = 0; ylocs[4][2] = 1;
		//orientation 3
		xlocs[0][3] = 0; xlocs[1][3] = 1; xlocs[2][3] = -1; xlocs[3][3] = 0; xlocs[4][3] = -1;
		ylocs[0][3] = 0; ylocs[1][3] = 0; ylocs[2][3] = 0; ylocs[3][3] = 1; ylocs[4][3] = -1;
		//orientation 4
		xlocs[0][4] = 0; xlocs[1][4] = 0; xlocs[2][4] = 0; xlocs[3][4] = 1; xlocs[4][4] = -1;
		ylocs[0][4] = 0; ylocs[1][4] = 1; ylocs[2][4] = -1; ylocs[3][4] = 0; ylocs[4][4] = -1;
		//orientation 5
		xlocs[0][5] = 0; xlocs[1][5] = -1; xlocs[2][5] = 1; xlocs[3][5] = 0; xlocs[4][5] = 1;
		ylocs[0][5] = 0; ylocs[1][5] = 0; ylocs[2][5] = 0; ylocs[3][5] = 1; ylocs[4][5] = -1;
		//orientation 6
		xlocs[0][6] = 0; xlocs[1][6] = 0; xlocs[2][6] = 0; xlocs[3][6] = -1; xlocs[4][6] = 1;
		ylocs[0][6] = 0; ylocs[1][6] = -1; ylocs[2][6] = 1; ylocs[3][6] = 0; ylocs[4][6] = 1;
		//orientation 7
		xlocs[0][7] = 0; xlocs[1][7] = 1; xlocs[2][7] = -1; xlocs[3][7] = 0; xlocs[4][7] = -1;
		ylocs[0][7] = 0; ylocs[1][7] = 0; ylocs[2][7] = 0; ylocs[3][7] = -1; ylocs[4][7] = 1;
		}

	
	private int calculateX(){
		return xloc+xlocs[xcount][orientation];
		}
	
	private int calculateY(){
		return yloc+ylocs[ycount][orientation];}
			
@Override public boolean getControls(String a){
			if(a == "Dir"){return true;}
			return false;}
			
@Override public void setOrientation(int a){
			if(a > 7 || a < 0){orientation = a%8;} else{orientation = a;}}
		
		
}
