# MIdTrans_AutoFW

###This is Web application automation project designed to test Web applications. i have used Selenium WebDriver libraries for testing GUI through automation.

## Tools & Libraries
  # > Selenium WebDriver
  # > Maven
  # > TestNg
  # > Java(programming language)
  # > Apache POI
  # > Fillo
  # > Extent Reports
  # > WebDriverManager
  
## Requirement
  In order to run tests you should have following softwares installed in your system.
  #> Java 1.8
  #> Maven
  
# Usage
 To run all the test, Navigate to MIdTrans_AutoFW directory and run below command
 
 ```bash
mvn test
```

## Reporting
After every run the report will be generated in directory - /MIdTrans_AutoFW/Execution-Summary-Report.html

## Resources

  There are 2 resources used in this framework:-
  # 1. /MIdTrans_AutoFW/src/main/java/com/midtrans/qa/testdata/MidTrans_Test-Data.xlsx
   here i kept data to be used while doing execution, for different screen data can be stored in different sheets and can be
   fethched on run time by passing SheetName in function.
   
  # 2. /MIdTrans_AutoFW/src/main/java/com/midtrans/qa/config/config.properties
  Here i kept all the confguration level data like- browser name,application url,User credentials.
  this file can be used to store any test confuguration data which will later drive the test according to data in this file.
  
  