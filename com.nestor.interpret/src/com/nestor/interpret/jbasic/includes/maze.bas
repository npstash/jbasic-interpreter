'
'   File:   scnmazeb.bas
'   Creation Date:  Wed 12-Jul-2000 18:23:01        Jonathan D. Kirwan
'   Last Modified:  Thu 17-Jun-2004 00:26:02        Initial version.
'
'   Copyright (C) 2000, 2004 Jonathan Dale Kirwan
'   All Rights Reserved: See the file COPYRGHT for a full description.
'
'
'   DESCRIPTION
'
'   This module demonstrates generating and displaying a maze on the
'   screen in mode 12 and adds an animated path-finding algorithm.  See,
'
'       http://users.easystreet.com/jkirwan/maze.htm
'
'   for more information on the design.
'
'
'   MODIFICATIONS
'
'   No modifications.
'
'
'   COPYRIGHT NOTICE
'
'   Jonathan Dale Kirwan grants you a non-transferable, non-exclusive,
'   royalty-free worldwide license to use, copy, modify, prepare deriva-
'   tive works of and distribute this software, subject to your agreement
'   that you acquire no ownership right, title, or interest in this soft-
'   ware and your agreement that this software is research work which is
'   provided 'as is', where Jonathan Dale Kirwan disclaims all warranties
'   with regard to this software, including all implied warranties of
'   merchantability and fitness of purpose.  In no event shall Jonathan
'   Dale Kirwan be liable for any direct, indirect, consequential or
'   special damages or any damages whatsoever resulting from loss of use,
'   data or profits, whether in an action of contract, negligence or
'   other tortious action, arising out of or in connection with the use
'   or performance of this software.

DECLARE SUB GenerateMaze (MazeWidth AS INTEGER, MazeHeight AS INTEGER, WestWalls() AS INTEGER, SouthWalls() AS INTEGER)
DECLARE SUB DrawMaze (MazeWidth AS INTEGER, MazeHeight AS INTEGER, dx AS INTEGER, dy AS INTEGER, WestWalls() AS INTEGER, SouthWalls() AS INTEGER)
DECLARE SUB PushPosition ()
DECLARE SUB MoveRight (count AS INTEGER)
DECLARE SUB DrawRight (count AS INTEGER)
DECLARE SUB PopPosition ()
DECLARE SUB MoveDown (count AS INTEGER)
DECLARE SUB DrawDown (count AS INTEGER)
DECLARE SUB PenDown ()
DECLARE SUB PenUp ()
DECLARE SUB DrawRightVec ()
DECLARE SUB DrawDownVec ()
DECLARE SUB MoveVec ()

CONST GOODPATH% = 9
CONST BADPATH% = 7

    DIM answer AS STRING, idx AS INTEGER, PosX AS DOUBLE, PosY AS DOUBLE
    DIM part1 AS STRING, part2 AS STRING, DimX AS DOUBLE, DimY AS DOUBLE
    DIM unit AS INTEGER, MazeWidth AS DOUBLE, MazeHeight AS DOUBLE
    REDIM SouthWalls(0 TO 0) AS INTEGER, WestWalls(0 TO 0) AS INTEGER

        RANDOMIZE TIMER

        CLS

        COLOR 9
        PRINT STRING$(80, 196)
        COLOR 2
        PRINT TAB(39); "MAZE"
        COLOR 9
        PRINT STRING$(80, 196)
        COLOR 7

        PRINT
        PRINT "  This program allows you to generate several mazes onto a single page from an"
        PRINT "  HP LaserJet printer.  The program will first ask you to give a file name for"
        PRINT "  placing the final maze composite.  This file can be LPT1:BIN, if you want to"
        PRINT "  directly send information to the printer.  Or if you prefer, you can specify"
        PRINT "  a regular DOS-style file name and then later send or copy it to the printer."
        PRINT "  If you decide to quit, just hit ENTER for the file name and the program will"
        PRINT "  stop.  Otherwise, you will continue into some questions about the mazes you"
        PRINT "  want to generate.  You'll be allowed to enter any number of mazes and place"
        PRINT "  them where you want.  When you don't want any more mazes, just hit ENTER at"
        PRINT "  question asking for the maze's width and height.  That signals you are done."
        PRINT
        PRINT "  You can enter the page position and the room sizes using 'in' for inches and"
        PRINT "  mm for millimeters.  If you don't use either, then HP dots are assumed.  You"
        PRINT "  may also just enter the room size in the X direction alone, if you want.  If"
        PRINT "  so, the program simply assumes that the Y size of the room is the same."
        PRINT
        PRINT "  (You will be allowed to place a generated maze, several times.  So don't be"
        PRINT "  confused by the program asking you to enter a position more than once.  It's"
        PRINT "  asking that so you have an opportunity to test out different sizes for the"
        PRINT "  same generated maze or to place it multiple times so you can cut them out,"
        PRINT "  once the page is printed and hand them to several people for time trials.)"
        PRINT
        PRINT
        PRINT

        DO
            PRINT "Enter the maze width and height: ";
            LINE INPUT answer
            LET answer = LTRIM$(RTRIM$(answer))
            IF answer = "" THEN
                END
            END IF
            LET idx = INSTR(answer, ",")
            IF idx >= 2 THEN
                LET MazeWidth = INT(VAL(LEFT$(answer, idx - 1)))
                LET MazeHeight = INT(VAL(MID$(answer, idx + 1)))
                IF MazeWidth < 1# THEN
                    PRINT "  The width is either missing or too small."
                ELSEIF MazeHeight < 1# THEN
                    PRINT "  The height is either missing or too small."
                ELSEIF MazeWidth > 32760# THEN
                    PRINT "  The width is way too big."
                ELSEIF MazeHeight > 32760# THEN
                    PRINT "  The height is way too big."
                ELSEIF (CLNG(MazeWidth + 2) * CLNG(MazeHeight + 2) + 15&) > 32760& * &H10& THEN
                    PRINT "  The maze area is way too big."
                ELSE
                    EXIT DO
                END IF
            ELSE
                PRINT "  You must enter both values, separated by a comma."
            END IF
        LOOP

        DO
            PRINT "Enter the size of a maze room (width, height): ";
            LINE INPUT answer
            LET answer = LTRIM$(RTRIM$(answer))
            IF answer = "" THEN
                END
            END IF
            LET idx = INSTR(answer, ",")
            IF idx = 0 THEN
                LET DimX = VAL(answer)
                IF INSTR(answer, "in") > 0 THEN
                    LET DimX = DimX * 300#
                ELSEIF INSTR(answer, "mm") > 0 THEN
                    LET DimX = (DimX / 25.4#) * 300#
                END IF
                LET DimX = INT(DimX + .5#)
                LET DimY = DimX
            ELSEIF idx = 1 THEN
                LET DimY = VAL(answer)
                IF INSTR(answer, "in") > 0 THEN
                    LET DimY = DimY * 300#
                ELSEIF INSTR(answer, "mm") > 0 THEN
                    LET DimY = (DimY / 25.4#) * 300#
                END IF
                LET DimY = INT(DimY + .5#)
                LET DimX = DimY
            ELSEIF idx >= 2 THEN
                LET part1 = LEFT$(answer, idx - 1)
                LET DimX = VAL(part1)
                IF INSTR(part1, "in") > 0 THEN
                    LET DimX = DimX * 300#
                ELSEIF INSTR(part1, "mm") > 0 THEN
                    LET DimX = (DimX / 25.4#) * 300#
                END IF
                LET DimX = INT(DimX + .5#)
                LET part2 = MID$(answer, idx + 1)
                LET DimY = VAL(part2)
                IF INSTR(part2, "in") > 0 THEN
                    LET DimY = DimY * 300#
                ELSEIF INSTR(part2, "mm") > 0 THEN
                    LET DimY = (DimY / 25.4#) * 300#
                END IF
                LET DimY = INT(DimY + .5#)
            END IF
            IF DimX < 2# OR DimY < 2# THEN
                PRINT "  Both X-width and Y-height must be at least 2."
            ELSE
                EXIT DO
            END IF
        LOOP

        SCREEN 12, 0, 0, 0
        GenerateMaze CINT(MazeWidth), CINT(MazeHeight), WestWalls(), SouthWalls()
        DrawMaze CINT(MazeWidth), CINT(MazeHeight), CINT(DimX), CINT(DimY), WestWalls(), SouthWalls()


        ' Follow the path by looking at the screen instead of the arrays.

    DIM x AS INTEGER, y AS INTEGER, i AS INTEGER, j AS INTEGER
    DIM ex AS INTEGER, ey AS INTEGER, Exits(0 TO 3) AS INTEGER
    DIM ExitCount AS INTEGER, Selection AS INTEGER

        LET x = 1 + (640 - (MazeWidth * DimX + 1)) \ 2
        LET y = 1 + (480 - (MazeHeight * DimY + 1)) \ 2
        LET ex = x + (MazeWidth - 1) * DimX
        LET ey = y + (MazeHeight - 1) * DimY

        DO
            ' fill in the current room with color.
            FOR i = 0 TO DimX - 2
                FOR j = 0 TO DimY - 2
                    PSET (x + i, y + j), GOODPATH
                NEXT j
            NEXT i
            ' quit, if we are done.
            IF x = ex AND y = ey THEN
                EXIT DO
            END IF

            DO
                LET ExitCount = 0
                ' check possible directions to go.
                IF POINT(x + DimX - 1, y) = 0 AND POINT(x + DimX, y) = 0 THEN
                    LET Exits(ExitCount) = 1
                    LET ExitCount = ExitCount + 1
                END IF
                IF POINT(x, y + DimY - 1) = 0 AND POINT(x, y + DimY) = 0 THEN
                    LET Exits(ExitCount) = 2
                    LET ExitCount = ExitCount + 1
                END IF
                IF POINT(x - 1, y) = 0 AND POINT(x - DimX, y) = 0 THEN
                    LET Exits(ExitCount) = 3
                    LET ExitCount = ExitCount + 1
                END IF
                IF POINT(x, y - 1) = 0 AND POINT(x, y - DimY) = 0 THEN
                    LET Exits(ExitCount) = 4
                    LET ExitCount = ExitCount + 1
                END IF
                IF ExitCount > 0 THEN
                    LET Selection = INT(RND * ExitCount)
                    SELECT CASE Exits(Selection)
                    CASE 1
                        LET x = x + DimX
                        FOR j = 0 TO DimY - 2
                            PSET (x - 1, y + j), GOODPATH
                        NEXT j
                    CASE 2
                        LET y = y + DimY
                        FOR i = 0 TO DimY - 2
                            PSET (x + i, y - 1), GOODPATH
                        NEXT i
                    CASE 3
                        FOR j = 0 TO DimY - 2
                            PSET (x - 1, y + j), GOODPATH
                        NEXT j
                        LET x = x - DimX
                    CASE 4
                        FOR i = 0 TO DimY - 2
                            PSET (x + i, y - 1), GOODPATH
                        NEXT i
                        LET y = y - DimY
                    END SELECT
                    EXIT DO
                ELSE
                    ' none possible.  mark room as bad.
                    FOR i = 0 TO DimX - 2
                        FOR j = 0 TO DimY - 2
                            PSET (x + i, y + j), BADPATH
                        NEXT j
                    NEXT i
                    ' back-up one room.
                    IF POINT(x + DimX - 1, y) = GOODPATH AND POINT(x + DimX, y) = GOODPATH THEN
                        LET x = x + DimX
                        FOR j = 0 TO DimY - 2
                            PSET (x - 1, y + j), BADPATH
                        NEXT j
                    ELSEIF POINT(x, y + DimY - 1) = GOODPATH AND POINT(x, y + DimY) = GOODPATH THEN
                        LET y = y + DimY
                        FOR i = 0 TO DimY - 2
                            PSET (x + i, y - 1), BADPATH
                        NEXT i
                    ELSEIF POINT(x - 1, y) = GOODPATH AND POINT(x - DimX, y) = GOODPATH THEN
                        FOR j = 0 TO DimY - 2
                            PSET (x - 1, y + j), BADPATH
                        NEXT j
                        LET x = x - DimX
                    ELSE
                        FOR i = 0 TO DimY - 2
                            PSET (x + i, y - 1), BADPATH
                        NEXT i
                        LET y = y - DimY
                    END IF
                END IF
            LOOP
        LOOP

        DO WHILE INKEY$ = ""
        LOOP

        SCREEN 0, 0, 0, 0
        WIDTH 80, 50

        END

    DIM SHARED PenState AS INTEGER, DrawCount AS INTEGER
    DIM SHARED MoveCountX AS INTEGER, MoveCountY AS INTEGER
    DIM SHARED SavedX(1 TO 20) AS INTEGER, SavedY(1 TO 20) AS INTEGER
    DIM SHARED CurrentX AS INTEGER, CurrentY AS INTEGER
    DIM SHARED StackPtr AS INTEGER

SUB DrawDown (count AS INTEGER)

        SELECT CASE PenState
        CASE 0
            PenDown
            LET PenState = 2
        CASE 1
            PenUp
            PenDown
            LET PenState = 2
        CASE 2
        END SELECT

        LET DrawCount = DrawCount + count

END SUB

SUB DrawDownVec

        LINE (CurrentX, CurrentY)-(CurrentX, CurrentY + DrawCount - 1), 15
        LET CurrentY = CurrentY + DrawCount
        LET DrawCount = 0

END SUB

SUB DrawMaze (MazeWidth AS INTEGER, MazeHeight AS INTEGER, dx AS INTEGER, dy AS INTEGER, WestWalls() AS INTEGER, SouthWalls() AS INTEGER)
'
' This routine accepts a width and height and the west and south walls for
' a maze and prints out the maze to the given file unit.
'
' An entirely different approach is used here, than was used in displaying
' the text style mazes.  Here, we draw all the south walls first, then all
' the west walls.  This allows us to take advantage of vectorizing.
'

    STATIC Masks() AS INTEGER, MaskFlag AS INTEGER

    DIM i AS INTEGER, j AS INTEGER, gap AS INTEGER, vector AS INTEGER
    DIM p AS LONG, prior AS INTEGER, count AS INTEGER

        IF NOT MaskFlag THEN
            DIM Masks(0 TO 15) AS INTEGER
            FOR i = 0 TO 14
                LET Masks(i) = 2 ^ i
            NEXT i
            LET Masks(15) = &H8000
            LET MaskFlag = -1
        END IF

        LET PenState = 0
        LET DrawCount = 0
        LET MoveCountX = 0
        LET MoveCountY = 0
        LET StackPtr = 0
        LET CurrentX = (640 - (MazeWidth * dx + 1)) \ 2
        LET CurrentY = (480 - (MazeHeight * dy + 1)) \ 2

        PushPosition

        LET p = 0&
        FOR i = 0 TO MazeHeight
            PushPosition
            FOR j = 1 TO MazeWidth
                IF (SouthWalls((p + j) \ &H10) AND Masks((p + j) AND &HF)) <> 0 THEN
                    IF PenState = 0 THEN
                        MoveRight dx
                    ELSE
                        DrawRight 1
                        MoveRight dx - 1
                    END IF
                ELSE
                    DrawRight dx
                END IF
            NEXT j
            IF PenState <> 0 THEN
                DrawRight 1
            END IF
            PopPosition
            MoveDown dy
            LET p = p + MazeWidth + 2&
        NEXT i

        PopPosition

        FOR j = 1 TO MazeWidth + 1
            PushPosition
            LET p = j
            FOR i = 1 TO MazeHeight
                LET p = p + MazeWidth + 2&
                IF (WestWalls(p \ &H10) AND Masks(p AND &HF)) <> 0 THEN
                    IF PenState = 0 THEN
                        MoveDown dy
                    ELSE
                        DrawDown 1
                        MoveDown dy - 1
                    END IF
                ELSE
                    DrawDown dy
                END IF
            NEXT i
            IF PenState <> 0 THEN
                DrawDown 1
            END IF
            PopPosition
            MoveRight dx
        NEXT j

END SUB

SUB DrawRight (count AS INTEGER)

        SELECT CASE PenState
        CASE 0
            PenDown
            LET PenState = 1
        CASE 1
        CASE 2
            PenUp
            PenDown
            LET PenState = 1
        END SELECT

        LET DrawCount = DrawCount + count

END SUB

SUB DrawRightVec

        LINE (CurrentX, CurrentY)-(CurrentX + DrawCount - 1, CurrentY), 15
        LET CurrentX = CurrentX + DrawCount
        LET DrawCount = 0

END SUB

SUB GenerateMaze (MazeWidth AS INTEGER, MazeHeight AS INTEGER, WestWalls() AS INTEGER, SouthWalls() AS INTEGER)
'
' This routine accepts a width and height for a maze and calculates a
' random maze into two arrays designed to hold the west and south walls
' of each room or cell in the maze grid.  These can then be used to print
' or use the maze, as desired (such as a random labyrinth for a game.)
'

    STATIC Masks() AS INTEGER, MaskFlag AS INTEGER

    DIM i AS INTEGER, j AS LONG, k AS LONG
    DIM PathLimit AS INTEGER, PathCount AS INTEGER
    DIM Exits(0 TO 3) AS INTEGER, ExitCount AS INTEGER, Selection AS INTEGER
    DIM UnvisitedRoomCount AS LONG, CurrentRoom AS LONG, count AS INTEGER

    ' Since we are packing the west and south walls, 16 to an INTEGER,
    ' we need a way to pack and unpack them from the arrays.  This array
    ' is set up exactly once; on the first call to the routine.

        IF NOT MaskFlag THEN
            DIM Masks(0 TO 15) AS INTEGER
            FOR i = 0 TO 14
                LET Masks(i) = 2 ^ i
            NEXT i
            LET Masks(15) = &H8000
            LET MaskFlag = -1
        END IF

    ' This code redimensions the west and south wall arrays, as needed.
    ' These arrays must be redimensionable, or an error will result.
    ' As an important side effect I'm depending on, redimensioning
    ' these arrays causes their element values to be initialized to 0.

        LET count = CINT(((CLNG(MazeWidth + 2) * CLNG(MazeHeight + 2)) + 15&) \ 16&)
        ERASE WestWalls
        ERASE SouthWalls
        REDIM WestWalls(0 TO count - 1) AS INTEGER
        REDIM SouthWalls(0 TO count - 1) AS INTEGER

    ' Set up our local copy of the visitation status array.  Since the
    ' grid uses a perimeter around the maze itself, we need to mark the
    ' rooms in the perimeter as having been used, so that the intervening
    ' walls are not removed (since those walls are the maze's boundary.)

        DIM Visited(0 TO count - 1) AS INTEGER
        LET j = CLNG(MazeWidth + 2) * (MazeHeight + 1) - 1&
        FOR i = 0 TO MazeWidth + 2
            LET Visited(i \ &H10) = Visited(i \ &H10) OR Masks(i AND &HF)
            LET Visited((i + j) \ &H10&) = Visited((i + j) \ &H10&) OR Masks((i + j) AND &HF&)
        NEXT i
        LET j = MazeWidth + MazeWidth + 3
        FOR i = 1 TO MazeHeight
            LET Visited(j \ &H10&) = Visited(j \ &H10&) OR Masks(j AND &HF&)
            LET Visited((j + 1) \ &H10&) = Visited((j + 1) \ &H10&) OR Masks((j + 1) AND &HF&)
            LET j = j + MazeWidth + 2
        NEXT i

    ' Set up our local copy of the rooms viable for a path branch.

        DIM Paths(0 TO count - 1) AS INTEGER

    ' Arrays are set up, the perimeter is initialized, we're ready to go.
    ' Compute the maze!  (See the discussion on the web site for details.)

        LET PathCount = 0
        LET PathLimit = CINT(SQR(MazeWidth * CDBL(MazeWidth) + MazeHeight * CDBL(MazeHeight)))
        LET UnvisitedRoomCount = CLNG(MazeWidth) * MazeHeight
        LET j = INT(RND * UnvisitedRoomCount)
        LET CurrentRoom = (1 + j \ MazeWidth) * (MazeWidth + 2) + (j MOD MazeWidth) + 1
        DO WHILE UnvisitedRoomCount > 1
            LET UnvisitedRoomCount = UnvisitedRoomCount - 1
            LET Visited(CurrentRoom \ &H10&) = Visited(CurrentRoom \ &H10&) OR Masks(CurrentRoom AND &HF&)
            DO
                LET ExitCount = 0
                IF (Visited((CurrentRoom - MazeWidth - 2) \ &H10&) AND Masks((CurrentRoom - MazeWidth - 2) AND &HF&)) = 0 THEN
                    LET Exits(ExitCount) = 1
                    LET ExitCount = ExitCount + 1
                END IF
                IF (Visited((CurrentRoom + MazeWidth + 2) \ &H10&) AND Masks((CurrentRoom + MazeWidth + 2) AND &HF&)) = 0 THEN
                    LET Exits(ExitCount) = 2
                    LET ExitCount = ExitCount + 1
                END IF
                IF (Visited((CurrentRoom - 1) \ &H10&) AND Masks((CurrentRoom - 1) AND &HF&)) = 0 THEN
                    LET Exits(ExitCount) = 3
                    LET ExitCount = ExitCount + 1
                END IF
                IF (Visited((CurrentRoom + 1) \ &H10&) AND Masks((CurrentRoom + 1) AND &HF&)) = 0 THEN
                    LET Exits(ExitCount) = 4
                    LET ExitCount = ExitCount + 1
                END IF
                IF ExitCount >= 1 AND PathCount * 0 < PathLimit THEN
                    EXIT DO
                END IF
                LET j = INT(RND * MazeWidth * MazeHeight)
                LET k = ((1& + j \ MazeWidth) * (MazeWidth + 2) + (j MOD MazeWidth) + 1&) \ &H10&
                DO WHILE Paths(k) = 0
                    LET k = k - 1&
                    IF k < 0& THEN
                        LET k = (CLNG(MazeWidth + 2) * CLNG(MazeHeight + 2) - 1&) \ &H10
                    END IF
                LOOP
                FOR i = 0 TO 15
                    IF (Paths(k) AND Masks(i)) <> 0 THEN
                        EXIT FOR
                    END IF
                NEXT i
                LET Paths(k) = Paths(k) AND NOT Masks(i)
                LET CurrentRoom = k * &H10& + i
                LET PathCount = 0
            LOOP
            IF ExitCount > 1 THEN
                LET Paths(CurrentRoom \ &H10&) = Paths(CurrentRoom \ &H10&) OR Masks(CurrentRoom AND &HF&)
            END IF
            LET PathCount = PathCount + 1
            LET Selection = INT(RND * ExitCount)
            SELECT CASE Exits(Selection)
            CASE 1
                LET CurrentRoom = CurrentRoom - MazeWidth - 2
                LET SouthWalls(CurrentRoom \ &H10&) = SouthWalls(CurrentRoom \ &H10&) OR Masks(CurrentRoom AND &HF&)
            CASE 2
                LET SouthWalls(CurrentRoom \ &H10&) = SouthWalls(CurrentRoom \ &H10&) OR Masks(CurrentRoom AND &HF&)
                LET CurrentRoom = CurrentRoom + MazeWidth + 2
            CASE 3
                LET WestWalls(CurrentRoom \ &H10&) = WestWalls(CurrentRoom \ &H10&) OR Masks(CurrentRoom AND &HF&)
                LET CurrentRoom = CurrentRoom - 1
            CASE 4
                LET CurrentRoom = CurrentRoom + 1
                LET WestWalls(CurrentRoom \ &H10&) = WestWalls(CurrentRoom \ &H10&) OR Masks(CurrentRoom AND &HF&)
            END SELECT
        LOOP

    ' Add an entrance and exit to the maze.  These could be placed
    ' anywhere around the perimeter, if we wanted to.  For now, it's
    ' hard-coded at the upper-left corner and the lower-right corner.

        LET SouthWalls(0) = SouthWalls(0) OR Masks(1)
        LET j = CLNG(MazeHeight + 1) * (MazeWidth + 2) - 2
        LET SouthWalls(j \ &H10&) = SouthWalls(j \ &H10&) OR Masks(j AND &HF&)

END SUB

SUB MoveDown (count AS INTEGER)

        SELECT CASE PenState
        CASE 0
        CASE 1
            PenUp
            LET PenState = 0
        CASE 2
            PenUp
            LET PenState = 0
        END SELECT

        LET MoveCountY = MoveCountY + count

END SUB

SUB MoveRight (count AS INTEGER)

        SELECT CASE PenState
        CASE 0
        CASE 1
            PenUp
        CASE 2
            PenUp
        END SELECT

        LET MoveCountX = MoveCountX + count

END SUB

SUB MoveVec

        IF MoveCountX <> 0 THEN
            LET CurrentX = CurrentX + MoveCountX
            LET MoveCountX = 0
        END IF

        IF MoveCountY <> 0 THEN
            LET CurrentY = CurrentY + MoveCountY
            LET MoveCountY = 0
        END IF

END SUB

SUB PenDown

        SELECT CASE PenState
        CASE 0
            MoveVec
        CASE 1
        CASE 2
        END SELECT

END SUB

SUB PenUp

        SELECT CASE PenState
        CASE 0
        CASE 1
            DrawRightVec
            LET PenState = 0
        CASE 2
            DrawDownVec
            LET PenState = 0
        END SELECT

END SUB

SUB PopPosition

        PenUp
        LET MoveCountX = 0
        LET MoveCountY = 0

        LET CurrentX = SavedX(StackPtr)
        LET CurrentY = SavedY(StackPtr)
        LET StackPtr = StackPtr - 1

END SUB

SUB PushPosition

        PenUp
        MoveVec

        LET StackPtr = StackPtr + 1
        LET SavedX(StackPtr) = CurrentX
        LET SavedY(StackPtr) = CurrentY

END SUB

