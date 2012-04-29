REM my first program
CLS

TYPE person
id AS INTEGER
name AS STRING
END TYPE

FUNCTION printpeople AS INTEGER (people(1 TO 2) AS person)
FOR i=1 TO 2
print "ID:";people(i).id;" NAME:";people(i).name
NEXT
END FUNCTION

FUNCTION test1
dim t(1 TO 2) as person
t(1).id = 1
t(1).name = "Nestor"
t(2).id = 2
t(2).name = "Ania"
printpeople(t)
print PI
END FUNCTION

DIM array(0 TO 0, 0 TO 0) as integer
'print array(1, 1)

REDIM array(1 TO 3, -2 TO 0)
array(3, -2) = 5
print array(3, -2)

print 1 = 1
