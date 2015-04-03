# Google Plus Mini App

Google Plus Mini App allows user to view his/her profile, circles, friends by integrating with **Google Plus (G+) Domain APIs**.

# Application Navigation and Views

* **Login View:**
This view will be shown when user starts the application. It will have **Sign in with Google** Button that redirects user to *Oauth2** module for authentication. It wil show account picker for accounts already integrated with the phone and user can directly use them as well.   
Login Access token is generated using following code
```java
token = GoogleAuthUtil
		.getToken(
				firstActivitySignIn.this,
				mEmail,
				"oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/plus.circles.read https://www.googleapis.com/auth/plus.profiles.read");
```
* **User Profile View:**
It will show user profile which will be featch user information from Google Plus using Google Plus Domain APIs. It will display **Profile Picture, Name, Occupation, Organization, and About Me**.  
User details are requested by following code
```java
Person mePerson = plusDomains.people().get("me").execute();
```
* **Circle View:**
It will show list of circle of user and upon selecting specific circle friend list in that circle will be displayed.  
Circle List view are requested by following code
```java
Person mePerson = plusDomains.people().get("me").execute();

					PlusDomains.Circles.List listCircles = plusDomains
							.circles().list("me");
					listCircles.setMaxResults(5L);
					CircleFeed circleFeed = listCircles.execute();
					List<Circle> circles = circleFeed.getItems();
```
* **Friend Profile View:**
It will show friends profile information like **Profile Picture, Name, Occupation, Organization, and About Me**. It also has Email friend button which will open Implicit Intent to built-in Email Clients to send email.  
We can access friends profile using follwing code
```java
Person person = plusDomains.people().get(friendId).execute();
```

# Screenshots

####Login Screen
![Login Screen](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Login%20Screen.png?raw=true "Login Screen")

####Account Picker
![Account Picker](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Account%20Picker.png?raw=true "Account Picker")

####User Dashboard
![User Dashboard](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/User%20Dashboard.png?raw=true "User Dashboard")

####Circle Listing
![Circle Listing](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Circle%20listing.png?raw=true "Circle Listing")

####Select Circle
![Circle Listing](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Select%20Circle.png?raw=true "Select Circle")

####Get Friend Details
![Get Friend Details](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Get%20Friend%20details.png?raw=true "Get Friend Details")

####Circle Friend Data with Detailed Information
![Circle Friend Data with Detailed Information](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Circle%20Friend%20Data%20with%20info.png?raw=true "Circle Friend Data with Detailed Information")

####Circle Friend Data with No Information
![Circle Friend Data with No Information Information](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Circle%20Friend%20Data%20with%20no%20info.png?raw=true "Circle Friend Data with No Information")

####Email Friend
![Email Friend](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Send%20Email%20Intent%20picker.png?raw=true "Email Friend")

####Send Email
![Send Email](https://github.com/deepmehtait/Android-Programming/blob/master/Google-Plus-Mini/Screenshots/Send%20Email.png?raw=true "Send Email")
