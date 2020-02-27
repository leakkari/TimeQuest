# TimeQuest
Save Time in your day and reduce your phone usage with TimeQuest

## Shared Preferences

userKey -> user key to authentication
timeGoal -> time goal set by user

## API

### Server status
https://timequestapi.herokuapp.com/
Access this link to check server status

### Login
https://timequestapi.herokuapp.com/login?username=__&password=__
Return -1 if invalid data
Return -2 if wrong username or password
Return -3 if error with database
Return Failed if connection could not be made in android app
Return userKey (used to access user data) if success

### Username
https://timequestapi.herokuapp.com/username?key=__
Return -1 if invalid data
Return -2 if wrong username or password
Return -3 if error with database
Else return username

### Change password
https://timequestapi.herokuapp.com/password/edit?key=__&oldPassword=__&newPassword=__
Return -1 if invalid data
Return -2 if wrong oldPassword
Return -3 if error with database
Return 0 if success

### Register
https://timequestapi.herokuapp.com/register?fullName=__&username=__&password=__
Return -1 if invalid data
Return -3 if error with database or user already exists
Return userKey if user created

### Get score for specific date
https://timequestapi.herokuapp.com/score?key=__&date=__
Return -1 if invalid data
Return -2 if no user found with that key
Return -3 if error with database
Return -4 if date not found / no score for date specified
Else return score

### Set score for specific date
https://timequestapi.herokuapp.com/score/add?key=__&date=__&score=__
Return -1 if invalid data
Return -2 if no user found with that key
Return -3 if error with database
Return 0 if successfully added

### Get app list
https://timequestapi.herokuapp.com/apps?key=__
Return -1 if invalid data
Return -2 if no user found with that key
Return -3 if error with database
Else return app list with this format: "app.one,app.two,app.three"

### Set app list
https://timequestapi.herokuapp.com/apps/add?key=__&apps=__
Return -1 if invalid data
Return -2 if no user found with that key
Return -3 if error with database
Return 0 if successfully added
