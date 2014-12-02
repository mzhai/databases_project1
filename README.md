databases_project1
==================

Project 1 for CMU Fall 2011 Databases Course
This project with done with Bill Ge for the Fall 2011 Databases Course at Carnegie Mellon University.

This databases application was written from the ground up and contains information about popular music. The application reads in a CSV file containing information about the Billboard Top 100 Songs from 2006-2010 and creates three relational tables that display information about the songs, the artists, and the song files.

Other features include:
- adding and deleting a record (which causes all relational tables to be updated)
- sorting by a specific column in a table
- data statistics, such as averages for appropriate fields
- exporting the data from the current view to a CSV file

The front-end of the application was written in Java using Java Swing. The backend was written in SQLite, and queried using JDBC.
