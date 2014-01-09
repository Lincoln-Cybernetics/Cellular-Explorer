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

class selector{
	int xsiz;
	int ysiz;
	boolean[][] selection;
	boolean isselected = false;
	int xl;
	int yt;
	boolean rectstate;
	public selector(int x, int y){
		xsiz = x;
		ysiz = y;
		selection = new boolean[xsiz][ysiz];
	}
	public selector(){
		xsiz = 400;
		ysiz = 150;
		selection = new boolean[xsiz][ysiz];
	}
	public void deselect(){
		isselected = false;
		for(int y = 0; y<= ysiz-1; y++){
			for(int x = 0; x<= xsiz-1; x++){
				selection[x][y] = false;}}
			}
				
	public void selectAll(){
		isselected = true;
		for(int y = 0; y<= ysiz-1; y++){
			for(int x = 0; x<= xsiz-1; x++){
				selection[x][y] = true;}}
			}
			
	public void invertSel(){

		for(int y = 0; y<= ysiz-1; y++){
			for(int x = 0; x<= xsiz-1; x++){
				selection[x][y] = !selection[x][y];}}
			}
			
	public void selectCell(int x, int y){
		selection[x][y] = true;}
		
	
			
	public void startRect(int x, int y, boolean b ){
		xl = x; yt = y; rectstate = b;}
			
	public void endRect(int x, int y){
		
		int xr; int yb;
		if(x<0){x=0;}if(y<0){y=0;}if(x>xsiz-1){x=xsiz-1;}if(y>ysiz-1){y=ysiz-1;}
		if(xl > x){xr = xl; xl = x;}else{xr = x;}
		if(yt > y){yb = yt; yt = y;}else{yb = y;}
			for(int yc = yt; yc <= yb; yc++){
			for(int xc = xl; xc <= xr; xc++){
				if(rectstate){selectCell(xc,yc);}else{removeCell(xc,yc);}}}
			}		
			
	public void removeCell(int x, int y){
		
		selection[x][y] = false;}
			
	public boolean getSelection(int x, int y){
		if(selection[x][y]){return true;}
		else{return false;}
	}
	public void detectSelection(){
		isselected = false;
		for(int y = 0; y <= ysiz-1; y++){
			for(int x = 0; x <= xsiz-1; x++){
				if(selection[x][y]){isselected = true;break;}
			}}
			
		}
		
	public boolean getSelected(){ return isselected;}
}
