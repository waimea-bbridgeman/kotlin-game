# Results of Testing

The test results show the actual outcome of the testing, following the [Test Plan](test-plan.md)

---

## Valid movements 

When the user presses a valid button does the location change?

### Test Data Used

I pressed valid buttons 

### Test Result

![validMovement.gif](screenshots/validMovement.gif)

When valid buttons were pressed the location updated like it was supposed to. 

---

## Boundary Movements 

If the user goes to the edge of the map can they go off the edge of it? Or does the game break? 

### Test Data Used

Going to the boundary of the map 

### Test Result

![boundary-invalid.gif](screenshots/boundary-invalid.gif)

When I went to the boundary the game didn't break and didn't allow them to fall off the map. 

---

## Invalid Movements

If the user tries to move to a location that isn't connected or shouldn't be allowed, does the game move?

### Test Data Used

Pressing invalid buttons 

### Test Result

![boundary-invalid.gif](screenshots/boundary-invalid.gif)

As the buttons are disabled when a move is not possible the game did not allow any invalid movement. 

---

## Is the game winable? 

Once the user fills the bucket and finds the fire do they win the game? 

### Test Data Used

Winning the game 

### Test Result

![winning.gif](screenshots/winning.gif)

Yes. The player successfully won the game.  

---

## Is the game losable? 

Can the user run out of time if they take too long?  

### Test Data Used

Running out of time 

### Test Result

![lose.gif](screenshots/lose.gif)

The player lost the game. 

---

