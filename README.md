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
For each refresh of the logo there will be a validation of the blockchain.
To insert the widget in a web page, it's necessary to integrate two HTML lines :

```html
<div id="byochain"></div>
<script src="byochain.js"></script>
```

and to modify the byochain.js using the right hash code and certification server URL:

```javascript
/** ****************************************** **/
/** MODIFY SPECIFIC CERTIFICATION INFORMATIONS **/
	
var hash = "3580fca4d10ac45046bf7ad7873134c10422af62ffa9335dc56237a8f2e306c8";
var username = "gvincenzi";
var password = "p6I8Blm!CI@HEc1";

var widthLogo = "100px";
var descriptionLogoFontFamily="Montserrat"; //Font from Google Fonts >> https://fonts.googleapis.com/css?family=Montserrat
var descriptionLogoFontSize="8px";
	
var certificationServer = "http://192.168.1.17:8080/byochain/";
	
/** ****************************************** **/
/** ****************************************** **/
```

## Installation
### Database
The MariaDB will be initialized by the following 2 SQL scripts : [Tables SQL init script](https://github.com/gvincenzi/byochain-certification/blob/master/sql/init_datatables.sql) and [Data SQL init script](https://github.com/gvincenzi/byochain-certification/blob/master/sql/init_data.sql)

### Start BYOChain server
To start the BYOChain server, you must deploy the WAR builded by Maven (module byochain-rest-api) in your Application Server, or launch it directly by command line:

```
java -Dspring.profiles.active=production -jar byochain-rest-api/target/byochain-rest-api-1.1.0-SNAPSHOT.war
```

### Spring Profile
The project must be launched using spring active profile "production" to work correctly in a production environment.

## Swagger GUI
### Swagger URL
After installation you can find the Swagger of Administration REST API at the URL:

```
http://localhost:8080/swagger-ui.html
```

### Admin test user
The default Admin user has the following credentials : admin/admin

## How register a new certification in BYOChain system
### Create users
The first step is to create one users for eache member of your community.
Create users by specific API **POST /api/v1/admin/users** : in the response you will receive his assigned password.

### Create new block
Create a new block using the credentials of your own user and by specific API **POST /api/v1/blocks/** : you need only the official name of your new certificated member.
Using the mined block hash to configure the [JS script](https://github.com/gvincenzi/byochain-certification/blob/master/widget/byochain.js) for your certificated member web site.

### Add a referer
Create a new referer for your new block using the credentials of your own user and by specific API **POST /api/v1/certifications/admin/referers**.
This referer must be the URL of the web page containing your certification logo.
You can insert a specific URL, or a URL ending with '*' to accept all web page responding to an URL starting with your referer string.

### Validation by other users
The last step is the validation of the other users registered in your BYOChain system : each user must only validate the blockchain to add a validation to your block.
Your block will be validated when it have a number of validations greater or equal than the parameter **required.validation.number** in [application-service.properties](https://github.com/gvincenzi/byochain-certification/blob/master/byochain-services/src/main/resources/application-service.properties)

### Visit referer URL
Now your logo is correctly visible in your referer web page.

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
