

Spring Batch Implementation

samples:

1) Load csv file from specified path, process and save to database.
    use cases for 100k records
        1) chunk size is 100, completed in 26 seconds
        2) chunk size is 1000, completed in 10 seconds
