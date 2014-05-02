Cellular-Explorer
=================

Cellular Explorer is a mixed rule cellular automaton editor created in Java. 

Thanks to Tarl Carpenter, Brian Prentice, Alexander Viasov, and James Woody for the feedback and ideas that have made this program possible.  Also thanks to everyone at www.conwaylife.com 


This is a basic description of its functionality:

Double click on the .jar file to open it.  The master control panel should appear in the upper-left of the screen.

The master controls are the following:  

"New" :  Opens a dialog to create a new automaton.  From the dialog you can set the dimensions of the automaton.  There is also an option to create a square automaton, which links the x- and y- dimensions together.

"Play/Pause" : Starts and stops the automaton.

Speed control : Moving the slider to the left increases the play speed; moving it to the right decreases the speed.

"Step" : While the automaton is paused, this button allows you to step the automaton ahead one generation.

"State Editor" : Opens the state editing window with all the state editing functions.

"Cell Editor" : Opens the cell editing window with all the cell editing options.

"Cell Picker" : Opens the cell selection window.  This allows you to chose two types of cells at once.  The primary cell selection (on the left), and the secondary cell selection (on the right).   

"Selection tools" : These tools allow you to select a portion of the automaton to work on. If you wish to edit the cells or the state of part of the automaton without affecting the whole system, the selection tools are the place to do it.

"Brushes" : Opens the brush selection menu.  You can chose a number of brushes for cell or state editing.

"About" : Reveals the incredible true story behind cellular explorer.  A modern legend.

"Display Type" : Lets you chose between Normal (green on black), and Multicolor rendering options for the automaton.  The colors in multicolor mode are keyed to the age of the active cells.  

"Wrap Edges" : Allows some control over the topology of the automaton. Default is flat but bounded (x-wrap and y-wrap both off).  Activate x-wrap for a vertical-tubular topology.  Activate y-wrap for a horizontal topology, and activate both for torroidal.

"Cell Info" : Opens a window with the relevant details of each cell as the mouse passes over it.  Information includes rule name, rule string( B/S for MBOT types), rule number (for Wolfram elementals), age (number of generations the cell has been active), maturity (number of generations until the cell calculates a new state), fade(if the fade rule iss applied, the maximum lifespan of active cells) direction( how the cell is oriented in space for rules where that matters), and mirror location for mirror cells.



State Editing:

"State Editing Mode" : Enters a operational mode especially for editing the state of the automaton.  The automaton pauses (if playing), and grid lines appear for convenience.

"State Draw" : Sets the action of the mouse to draw into the automaton's current state with the selected brush.  Left clicking activates cells and right clicking de-activates them.  

"Interactive mode" : enables the possibility of drawing to the state in the normal run mode (even while the automaton is playing)

"Check" : This option causes state drawing to activate every other cell in a checkerboard pattern.

"Random" : This option causes state drawing to randomly activate cells as the brush passes over them.

"Fill" : Fills the entire automaton (or the selected porton).  May be used in state editing, or the normal running mode.

"Check" : This option is the same as the check option for drawing, but for the fill tool.

"Random" : This option is the same as the random option for drawing, but for the fill tool.

"Clear" : Clears the state of the entire automaton, all cellsare de-activated.

"Invert" : Inverts the state of the entire automaton (or the selected portion).  Cells with binary output are switched: active cells(state 1) are deactivated(state 0) and vice-versa.  Cells with an integer output have their states multiplied by -1.



State Editing tools:  
State drawing and filling can affect more than the cell's current output state.  The following is a description of the tools available from the state editor.

Binary State:  For Drawing:  activating(left-click) cells sets their current state to 1, and de-activating(right-click) sets their state to zero.  For Filling:  the same as drwing, except the "Random" option randomly sets the states of each cell to a random state between 0 and 1024. This is a temporary arrangement until a Integer State tool is implemented. 

The Age tool sets the current age (for cells with the aging option turned on) to the value of the slider.

The Maturity counter tool sets the cell's internal maturity counter (for cells with a maturity setting) to the value on the slider.  The maturity counter determines how long it will be before a cell calculates its next state.


Cell Editing:

"Cell Editing Mode" : Enters a special mode for editing the cells in the automaton.  The automaton pauses(if playing), grid lines appear, and each cell is rendered in a color determined by its basic type.
    Cell: gray
    Wolfram: red
    MBOT: Green
    Randomly active cells: orange
    OnCell: white
    OffCell: black
    BlinkCell: blue
    Symmetrical: cyan
    Conveyor: yellow
    Strobe Cell: pink
    Total Cell: purple
    Average Cell: burnt orange
    
"CellDraw":  Sets the mouse action to draw cells into the automaton with the selected brush.  Left clicking places cells selected with the primary cell picker, right clicking places cells from the secondary cell picker.

"Check": This option causes cell drawing to alternate between the primary and secondary cell selections in a checkerboard pattern.

"Random": This option causes cell drawing to place randomly generated cells into the automaton.

"Fill": Fills the entire automaton (or the selected porton).  May be used in cell editing, or the normal running mode.

"Border": Behaves like the fill tool, but only affects cells on the outer border of the automaton.

"Check" : This option is the same as the check option for drawing, but for the fill tool.

"Random" : This option is the same as the random option for drawing, but for the fill tool.


Cell Editing Tools:
Cell Drawing and filling can do more than change the type of cell in a location, these tools can set a cell's internal options and parameters to alter its behavior. 

The Cell tool: generates and places new cells into the automaton.

The Direction tool is used to alter the direction parameter of cells that have one. The following cells can be edited: Conveyor(direction of motion), Symmetrical(axis of symmetry), and Wolfram(for cell orientation).

The Maturity tool changes a cell's maturity setting(for cells that have a maturity setting). The maturity setting determines how many generations a cell will go through before calculating a new state.

The Ages tool sets (turns on or off) the aging option for cells that have it.  Cells that age have their states gradually increase as they remain active.

The Fade Rule tool sets (turns on or off) the Fade Rule for cells that have it.  Cells with the Fade rule de-activate after a maximum number of generations in the active state.

The Fade tool sets the Fade parameter (maximum number of generations in an active state) of cells with the Fade Rule turned on.

The Neighborhood Expansion tool sets the expansion factor of cells with the Neighborhood Expansion option.  In a normal cell, the expansion factor is one, and the cell determines its state based on the cells directly adjacent to it.  As the expansion factor increases, the cell bases its state on cells farther away. (expansion factor 2 means that a cell's right-hand neighbor (for example), is now the cell that is two cells away from it to the right, at expansion factor 3, the neighbor is three cells away, and so on)



Description of Cell Types:

Cell:  This type of cell is completely passive.  Its state can be edited, but left to its own devices it doesn't change states.

Wolfram:  This cell can follow any of the 256 elemental Wolfram rules.  

MBOT:  Stands for: Moore Binary Outer- Totalistic.  This is one of the better known types of cellular automata, and can follow any the rules of this type.  The cell is activated ("Born"), if the number of active neighbors it has matches one of the numbers in the first half of its rule-string.  An active cell of this type stays active ("Survives"), if the number of active neighbors it has matches one of the numbers in the second half of its rule string.  Includes pre-sets for some of the better known rules of this type (Life, Seeds, Maze, Coral, etc.)

Randomly active cells: Are just that, random.  They randomly turn on and off, and if the ages option is on, they have a randomly assigned age.

OnCell: is always on.

OffCell is always off.

BlinkCell: Alternates between on and off.

Symmetrical cell: This cell detects symmetry within its Moore neighborhood.  If symmetry is present it activates, otherwise it is inactive.  The axis of symmetry is set in its options.

Conveyor Cell: Takes on the state of one of its neighbors.  In a group they act as a conveyor belt for patterns within the automaton.  The direction the conveyor runs is set in its options.

Strobe cell:  Remains inactive for a length of time set by its maturity option, and then becomes active for one generation.

Total Cell:  Adds the states of its neighbors.

Average Cell:  Totals the states of its neighbors, then divides by the size of its neighborhood.
