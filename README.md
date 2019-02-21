### Member1:
**Name:** Mohammad Mahdi Amini
**Phone:** 0939 824 0640
**Email:** bmd579@gmail.com

### Member2:
**Name:** Ahmad Jahanbin
**Phone:** 0937 477 8378
**Email:** ajahanbin74@gmail.com?

## Project Information:

**Architecture:**
* n-tier + event-driven : back-end

**Languages:**
* Java : back-end
* Groovy : back-end
* HTML : front-end
* JavaScript : front-end

**Frameworks:**
* Spring-Boot 2.1.0.BUILD-SNAPSHOT

**Libraries:**
* JSF (PrimeFaces)
* Material Kit. 
* jQuery

## Installation Guide: 
* 1- You need a Up and Running MySql instance .
* 2- Create a user account on the MySql instance as this.
 {Username: fan_q2_amini, Password:fan_q2_amini} 
 And User should have permission to answer requests from the IP you send's requests from: localhost or container ip.
* 3- Create a database named "fan_q2_amini" on your MySql instance with that user, by this command (on mysql client): 
"CREATE 
DATABASE fan_q2_amini CHARACTER SET utf8 COLLATE utf8_persian_ci;"
<br>
(Pay attention to charset in the command!) 
* 4- Give full access to the user for newly created database. 
* 5- Change artifact/application.properties file located near q2.jar considering the MySql instance IP and User info (if 
needed). 
* 6- You need Java 8 (neither 7 nor 9) installed and ready to use (environment variables configured) 
* 7- In the artifact folder, execute this command: "java -jar q2.jar" 
* 8- Check database connection errors (if any) on the output
* 9- The web application is now up and running 
* 10- Hit localhost:8080/register.xhtml in browser to begin your journey
* 11- Main page is located on localhost:8080/view/main.html 

##### More Information:
* We used JSF, JSP and Pure HTML (pure jQuery) to show our skills. So please don't consider it as anarchy
* Although it was not a UI contest, we have tried hardly to develop a responsive and beautiful UI. And we 
paid attention to smoothness of client side experience

