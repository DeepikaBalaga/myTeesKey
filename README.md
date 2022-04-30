# Week3_Android - create repository feb 10
**commit 2:** setup remote repo and push the modified readme for test

**commit 3:**
        -> Added structure for the repo
        -> included images, logos, xml files
        -> apart from mainactivity, added splash screens, register and login user screens but functionality are yet to be added
        -> tested a single api for username and password and added URL to the RegisterAcitity
        -> yet to change some logos as I got the dimensioned images from google
	-> added volley library, butterknife library's to the repo
git push -u origin main
git remote add main https://github.com/DeepikaBalaga/Week3_Android.git

**commit 4:**
        -> created new API and changed the url in app code
        -> as there is issue with variable name missmatch internal server error at app side
        -> error com.android.volley.ServerError resolved

**commit 5:**
        -> renamed repo

**commit 6:**
	-> fixed logout on back and login user types multicard issue

**commit 7:**
        -> changed list of owners api as the previous api is not having IAM permissions and so not working
        -> inorder to show profile data for the logged in user, some from shared preference and the others from the API followed by commit to preference
        -> created 3 new api's(listof owners,register agent, login agent)
	-> changed the app name to tees superKey and added appropriate logo
	-> fixed some api errors from the previous commit
	-> yet to add some more multicards and UI changes as well

**commit 8:**
	-> added permissions for access network state
	-> registered a braodcast receiver for connectivity changes(wifi & mobile data)
	-> new api's for owner lock creds and delete access is added for agent cards
	-> new feature to hide cards/options after lock release
	-> changed the request url for ownerlockcreds API to GET(instead of POST)
	-> agent UI and it's logic are done by this commit and next steps will be for Owner UI & their backend

**commit 9:**
	-> Added feature contents for usertype owner(share lockdetails & otp)
	-> added fingerprint to lock/unlock door feature
	-> reference url for biometric - https://www.geeksforgeeks.org/how-to-add-fingerprint-authentication-in-your-android-app/
	-> updated some api's as per their usage for latest features

**commit 10:**
	-> modified fingerprint authentication as required
	-> updated owner's disable lock access feature
	-> added access history api but not completed due to date response parsing from lambda
**commit 11:**
	-> Readme changes only

**commit 11:**
	-> Code cleanup, remove unnecessary imports, code reformattings and follow Android standard coding guidelines
	-> update jar repositories
