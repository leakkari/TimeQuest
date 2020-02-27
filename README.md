# TimeQuest
Save Time in your day and reduce your phone usage with TimeQuest.

After observing many users, one pattern we saw is that people have trouble staying off their phones for a long time.

When doing an assignment or filling up a job application for example, as soon as the task starts getting boring or frustrating, people reach out for their phones.

When people are in an awkward situation, waiting alone for their friends at a restaurant, or avoiding eye contact, people reach out for their phones.

In his article, Harris said that “the average person checks their phone 150 times a day. Why do we do this? Are we making 150 conscious choices?”

The second thing we observed is that once people start using their phones, very few put the phone away after a few minutes. Instead, we see a common pattern where people start scrolling down endlessly or opening new apps and watching new videos.

Are users aware of how much time they’re spending on their phone? Most smartphones nowadays track your usage or your “screen time”. But how effective is it? When people pick up their phones, they don’t plan on scrolling down for hours. They usually mean to use it to complete one specific task that shouldn’t take more than a minute. However, they end up getting distracted and sidetracked.

Instead, imagine if technology companies empowered you to consciously bound your experience to align with what would be “time well spent” for you. Not just bounding the quantity of time you spend, but the qualities of what would be “time well spent".  [Harris]

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
