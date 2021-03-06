import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.event.*;
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
public class cellComponent extends JComponent  
{
	int xdim;
	int ydim;
	int magnify;
	int mode = 1;
	int[][] cstate;
	boolean[][] selection;
	boolean selectionflag = false;// shows, hides selections
	int[][] species;// colors cells by type
	
	int[][] hilite; // hilights cells
	int hlx = 0;//hilight cell x
	int hly = 0;// highlight cell y
	int hlc = 0;//hilight color
	boolean hiliteflag = false;
	int rectx;
	int recty;
	int rectxb;
	int rectyb;
	int rectxc;
	int rectyc;
	boolean rectflag;
	boolean rectstateb;
	
	
	public cellComponent(){
		xdim = 500;
		ydim = 300;
		magnify = 5;
		cstate = new int[xdim][ydim];
		selection = new boolean[xdim][ydim];
		species = new int[xdim][ydim];	
		hilite = new int[xdim][ydim];
		setPreferredSize(new Dimension(xdim*magnify, ydim*magnify));
	}
	
	public cellComponent(int x, int y){
		xdim = x;
		ydim = y;
		magnify = 5;
		cstate = new int[xdim][ydim];
		selection = new boolean[xdim][ydim];
		species = new int[xdim][ydim];	
		hilite = new int[xdim][ydim];
		setPreferredSize(new Dimension(xdim*magnify, ydim*magnify));

	}
	
	
	// set variables
	//main boolean state
	public void setState(int update[][]){
		cstate = update;
		repaint();}
		
	//boolean state of one cell
	public void setAState(int x, int y, int b){
		cstate[x][y] = b;}
	
	// selects and deselects cells	
	public void setSelection(int x, int y, boolean z){
		selection[x][y] = z; }
	

		
	// 	shows or hides selection hilighting
	public void setSelect(boolean a){
		selectionflag = a;repaint();}
		
	// begin rectangle selection
	public void beginRect(int x, int y, boolean b){
		rectx = x; recty = y; rectstateb = b;}
	
	//finish rectangle	
	public void finishRect(int x, int y){	selectionflag = true;
		if(x<0){x = 0;} if(y<0){y=0;}if(x>xdim-1){x = xdim-1;} if(y>ydim-1){y = ydim-1;} 
		if(rectx > x){rectxc = rectx; rectxb = x;}else{rectxc = x; rectxb = rectx;}
		if(recty > y){rectyc = recty; rectyb = y;}else{rectyc = y; rectyb = recty;}
		for (int a = rectyb; a <= rectyc; a++){
			for(int b = rectxb; b <= rectxc; b++){
				setSelection(b,a,rectstateb); }}
		 repaint();}
		
	
		
	public void setDispMode(int a){
		mode = a; remHilite(); 
		/*Mode 1: normal rendering for running the automaton
		 *Mode 2: state editing mode
		 *Mode 3: Cell editing mode 
		 *Mode 4: multicolor mode 
		 */}
		
	public void setMag(int a){
		magnify = a;
		setPreferredSize(new Dimension(xdim*magnify, ydim*magnify));
		 repaint();}
		
	public void setSpecies(int a, int b, int c){
		species[a][b] = c;}
		
	
		
	
	


//get variables

	public int getMode(){
		return mode;}
		
	
		
	public boolean getSelect(){
		return selectionflag;}

//hilights a cell
	public void setHiLite(int a, int b, int c){
		hilite[a][b] = c;
		 repaint();}
// remove hilight
	public void remHilite(){
		for(int y = 0; y< ydim; y++){
		for(int x = 0; x< xdim; x++){
			hilite[x][y] = 0;}}
			repaint();
		}

//main paint method
public void paintComponent( Graphics g){
					int x = 0;
					int y = 0;
					int schmagnify;
					
					
					for(y=0;y<=ydim-1;y++){
						for(x=0;x<=xdim-1;x++){
						
							//normal rendering
							if (mode == 1){
								g.setColor(cstate[x][y] > 0 ? Color.green : Color.black);
								g.fillRect(x*magnify,y*magnify,magnify,magnify);}
						
							//state editing
							if (mode == 2){
								if(magnify>4){schmagnify = magnify-1;}
								else{schmagnify = magnify;}
								g.setColor(cstate[x][y] > 0 ? Color.green : Color.black);
								g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);}
								
							// cell editing
							if(mode == 3){
								if(magnify>4){schmagnify = magnify-1;}
								else{schmagnify = magnify;}
								switch(species[x][y]){
									//cell
									case 0: g.setColor(Color.gray); break;
									//Wolfram
									case 1: g.setColor(Color.red); break;
									//MBOT
									case 2: g.setColor(Color.green); break;
									//RandCell
									case 3: g.setColor(Color.orange); break;
									//OnCell
									case 4: g.setColor(Color.white); break;
									//OffCell
									case 5: g.setColor(Color.black); break;
									//BlinkCell
									case 6: g.setColor(Color.blue); break;
									//Symmetric Cell
									case 7: g.setColor(Color.cyan); break;
									//Conveyor Cell
									case 8: g.setColor(Color.yellow); break;
									//strobe Cell
									case 9: g.setColor(Color.pink); break;
									//Total Cell
									case 10: g.setColor(new Color(119, 35, 177)); break;
									//Average Cell
									case 11: g.setColor(new Color(200, 65, 10)); break;
									//EVBOT
									case 12: g.setColor(new Color(127, 255, 127)); break;
									//mincell
									case 13: g.setColor(new Color(200, 230, 240)); break;
									//maxcell
									case 14: g.setColor(new Color(225, 95, 15)); break;
									//default
									default: g.setColor(Color.black); break;
								}
								g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);
							}
							
							//multicolor rendering
							if (mode == 4){
								
									switch(cstate[x][y]){
									case 0: g.setColor(Color.black);break;
									case 1: g.setColor(Color.white);break;
									case 2: g.setColor(new Color(127, 255, 127)); break;
									case 3: g.setColor(Color.green);break;
									case 4: g.setColor(new Color(84, 167, 0)); break;
									case 5: g.setColor(new Color(160, 211, 0)); break;  
									case 6: g.setColor(Color.yellow);break;
									case 7: g.setColor(Color.orange);break;
									case 8: g.setColor(Color.red);break;
									case 9: g.setColor(new Color(156,21,7));break;
									case 10: g.setColor(new Color(144,68,21));break;
									default: g.setColor(Color.pink); break;
									}
									
								
								g.fillRect(x*magnify,y*magnify,magnify,magnify);}
								
								//gradient mode
								if(mode == 5){
									boolean neg = false;
									int grad = cstate[x][y]/4;
									if(grad < 1){neg = true; grad = grad * -1;}
									if(grad > 255){grad = 255;}
									if(neg){g.setColor(new Color(0,0,grad));}
									else{g.setColor(new Color(grad, 0, 0));}
									g.fillRect(x*magnify,y*magnify,magnify,magnify);
								}
						
								//hilite a selection	
								if(selectionflag){ 
									if(selection[x][y]){
										g.setColor(new Color(252,76,31));
										g.drawRect(x*magnify, y*magnify, magnify, magnify);
									}}
						
								// hilite cells
								switch(hilite[x][y]){
									case 0: break;
									case 1: g.setColor(Color.red); break;
									case 2: g.setColor(Color.blue); break;
									case 3: g.setColor(Color.orange); break;
									case 4: if(selection[x][y]){g.setColor(Color.cyan);}else{g.setColor(Color.red);} break;
									case 5: g.setColor(Color.white);
											if(mode == 1){ g.setColor(Color.green); if(cstate[x][y] > 0){g.setColor(Color.black);} }
											if(mode == 3){ if( species[x][y] == 4 /*|| lifespan[x][y] == 1*/){g.setColor(Color.black);}}
											if(mode == 4 && cstate[x][y] == 1){g.setColor(Color.black);} break;
									default: g.setColor(Color.red); break;}
									if(hilite[x][y] != 0){g.drawRect(x*magnify, y*magnify, magnify, magnify);}
									
									
								
								
							
								
						}}
						
						
						
						}
}
