# BeYourChain On Line Certification System
## Technical Scope
On line Certification System is an Open Source project based on BYOChain, a simple Blockchain implementation (Proof-Of-Work protocol).
The project includes a REST API to manage the system.


## Validation of a certification
The system permits to owners of a certificator company to insert a certification as a block of a simple blockchain (stored on a MariaDB).
Each certification will be mined by a system user and will be validated by other users: all users can validate the blockchain when they want.
To validate a single certification, the system will validate all the blockchain.

## Widget to share a logo within the sites of certificated users
In the project a widget has been coded (HTML+JS) to share the logo of certification on certificated sites: the widget will print the logo (simple image file) with related certification informations (hash code and a temporary token).
To insert the widget in a web page, it's necessary to integrate two HTML lines :

```html
<div id="byochain"></div>
<script src="byochain.js"></script>
```

and to modify the byochain.js using the right hash code and certification server URL:

```javascript
/** ****************************************** **/
/** MODIFY SPECIFIC CERTIFICATION INFORMATIONS **/
	
	var hash = "0daa29144f8d70548f70065f02159b85e6007424d623786a1a941a3c44d834fa";
	var certificationLogoURL = "certification.png";
	var widthLogo = "100px";
	var descriptionLogoFontFamily="Montserrat"; //Font from Google Fonts >> https://fonts.googleapis.com/css?family=Montserrat
	var descriptionLogoFontSize="8px";
	
	var certificationServer = "http://192.168.1.17:8080/byochain/";
	
/** ****************************************** **/
/** ****************************************** **/
```



## Technologies
Technologies used in this project:
- Spring Boot
- Spring Core
- Spring MVC
- Spring Security
- Swagger
- Hibernate
- MariaDB database
- Maven

## Build Information
**Travis Ci page** : [Click here to view build history](https://travis-ci.org/gvincenzi/byochain-certification)

**Last build** : <img src="https://travis-ci.org/gvincenzi/byochain-certification.svg?branch=master" alt="last build satus">
