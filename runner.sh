
javac -cp lib/jOpenDocument-1.5.jar src/*.java

cd src

mv *.class production_files

cd production_files

java -cp .:jOpenDocument-1.5.jar BalanceManager

