# Remove previously compiled code
rm ./bin/*.class ./bin/bakery/*.class ./bin/util/*.class 

# Make the bin, if not present
mkdir -p ./bin/

# Compile the game
javac src/main/BakeryDriver.java src/main/bakery/*.java src/main/util/*.java -d ./bin/

# Play the game
java --class-path ./bin/ BakeryDriver