

Spring Batch Implementation

samples:

1) Load csv file from specified path, process and save to database. 

    use cases for 100k records
   
        1) chunk size is 100, completed in 26 seconds
   
        2) chunk size is 1000, completed in 10 seconds

        3) Multithreaded approach(5 threads) + chunk size is 1000, completed in  4 seconds

2) Read from database, process and write to csv file
   
   query used: fetch employees from IT department and salary in asc order
