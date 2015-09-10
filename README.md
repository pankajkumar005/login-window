# login-window
## Installation Steps
### Prerequisites
  jdk, maven and tomcat should be available
### Steps  
* Run "mvn clean intall" from root directory (This will deploy war as well as run the test cases. For test cases alone use "mvn test")
* Move to target directory, and copy the RESTfulExample.war
* Place it inside webapps folder of tomcat server
* Create a file userinfo.txt, inside bin folder of tomcat server
* Access http://localhost:8080/RESTfulExample/ui/login.html