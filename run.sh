./gradlew build

cp build/classes/java/main/Main.class .
cp build/classes/java/main/HeaderGenerator.class .

java Main "$@"

rm Main.class
rm HeaderGenerator.class
