# USER SERVICES ACCESS FILE READER 
Thousands of users make requests to one of our public services. To help manage the sheer volume of
these requests, we decide to place limits on the number of requests that can be made. These limits will
help us provide reliable and scalable API.
The maximum number of requests that are allowed is based on a time interval, some specified period or
window of time.
To prototype a solution, we decided to stream user access logs into a file, users_access.txt. And you’re to
help write a Java program that reads this file, loads it to a database (MySQL/PostgreSQL), and then
checks if a specific IP address makes more than a certain number of requests for a specified period or
window of time.

##Functional requirements
- Create a database (MySQL/PostgreSQL) called REQ_LIMIT with two tables: USER_ACCESS_LOG
  & BLOCKED_IP_TABLE
- Write a Java program that reads the users_access.txt file, & loads it to a database’s
  USER_ACCESS_LOG table
- The delimiter of the users_access.txt file is a pipe character, “|”
- Use a rate limit of 200 requests per hour, and 500 requests per day as limits
- Execute the Java program from the command line as JAR, with the following command-line
  arguments:
- start is of "yyyy-MM-dd.HH:mm:ss" date format,
- duration can take only "hourly", "daily" as inputs
- limit can be an integer.
- users_access.txt file format
- Date, IP, Request, Status, User Agent (pipe delimited)
- Date format is “yyyy-MM-dd HH:mm:ss.SSS"
