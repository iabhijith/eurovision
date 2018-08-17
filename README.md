# Dependencies
* JDK 8

# Steps to run the project
~~~~~
* ./gradlew build
* java -jar build/libs/voting-system-0.0.1-SNAPSHOT.jar
~~~~~

# Generate sample
`bash generator.bash > votes.txt`
> By default 1 million records are generated. Edit the bash file to change as required.


# Usage commands
> Upload the votes file using the below command.

`eurovision load "/path/to/votes.txt" 2020`

> Check results using the below command

`eurovision results Netherlands 2020`
