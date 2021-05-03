## Grid Puzzle


### Problem Statement
Place N queens on an NxN chessboard so that none of them attack each other (the classic n-queens problem). Additionally, make sure that no three queens are in a straight line at ANY angle, so queens on A1, C2 and E3, despite not attacking each other, form a straight line at some angle.

### Design

Arguably, it becomes easier to reason if we consider the problems as placing N dots on an NxN grid with following restrictions:

- only 1 point may appear for a line with slope 1 and -1 (diagonal lines)
- only 1 point may appear for a line with slope 0 (horizontal line)
- only 1 point may appear for a vertical line
- at most 2 points may appear for lines with any other slope.

Let's consider the first three types of lines as "standard" lines and the last type of lines as "special" lines.

We can start inspecting each row, and each column, recursively inspecting each point by verifying if it can be placed on the grid. That is whether the point does not violate constraints given lines that we already keep track.
   
To test if a candidate point is viable or valid, then we create a new or lookup existing _standard_ lines. We also create new or lookup existing _special_ lines by forming straight lines out of point pairs with all other existing points.
      
If those lines do not violate the constraints, then start tracking those lines and/or update the point count on the lines.

Once we've placed a valid point in each row, then by construction, we are done.

### Example:


```
$ cd /path/to/grid-puzzle/
$ ./gradlew run -Dpuzzle.showUi=true -Dpuzzle.uiSize=800 \
  -Dpuzzle.preferRandomPlacement=true -Dpuzzle.gridSize=8
Solving for size=8 ...
Found solution in 00:00:00.034
Points:
(0, 3)
(1, 1)
(2, 7)
(3, 4)
(4, 6)
(5, 0)
(6, 2)
(7, 5)

Grid:
7   |_|_|X|_|_|_|_|_|
6   |_|_|_|_|X|_|_|_|
5   |_|_|_|_|_|_|_|X|
4   |_|_|_|X|_|_|_|_|
3   |X|_|_|_|_|_|_|_|
2   |_|_|_|_|_|_|X|_|
1   |_|X|_|_|_|_|_|_|
0   |_|_|_|_|_|X|_|_|

     0 1 2 3 4 5 6 7 

Displaying ui graph ...
```

Here is a screenshot of the ui:

![](/assets/screenshot.png)
