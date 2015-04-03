# Google Plus Mini App

Google Plus Mini App allows user to view his/her profile, circles, friends by integrating with **Google Plus (G+) Domain APIs**.

# Application Navigation and Views

1. **Login View:**
This view will be shown when user starts the application. It will have **Sign in with Google** Button that redirects user to *Oauth2** module for authentication. It wil show account picker for accounts already integrated with the phone and user can directly use them as well.  
Login Access token is generated using following code
```token = GoogleAuthUtil
						.getToken(
								firstActivitySignIn.this,
								mEmail,
								"oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/plus.circles.read https://www.googleapis.com/auth/plus.profiles.read");```
2. **User Profile View:**
It will show user profile which will be featch user information from Google Plus using Google Plus Domain APIs. It will display **Profile Picture, Name, Occupation, Organization, and About Me**.  
User details are requested by following code
```Person mePerson = plusDomains.people().get("me").execute();```  
3. **Circle View:**
It will show list of circle of user and upon selecting specific circle friend list in that circle will be displayed.  
Circle List view are requested by following code
```Person mePerson = plusDomains.people().get("me").execute();

					PlusDomains.Circles.List listCircles = plusDomains
							.circles().list("me");
					listCircles.setMaxResults(5L);
					CircleFeed circleFeed = listCircles.execute();
					List<Circle> circles = circleFeed.getItems();```
