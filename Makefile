SRC= ./src/ulysses/*.java\
	 ./src/ulysses/planet/lithosphere/*.java\
	 ./src/ulysses/planet/hydrosphere/*.java\
	 ./src/ulysses/planet/atmosphere/*.java\
	 ./src/ulysses/planet/biosphere/*.java\
     ./src/ulysses/planet/utilities/*.java \
	 ./src/ulysses/planet/utilities/generators/*.java

OUT= ./bin/

.PHONY: clean run debug dist

all: $(SRC)
	javac $(SRC) -g -Xlint:unchecked -d $(OUT)

run:
	cd bin; java ulysses/Ulysses

debug:
	cd bin; jdb ulysses/Ulysses

clean:
	rm -rf bin/ulysses/
	rm -rf dist*

dist: 
	rm -rf dist
	mkdir dist
	cp -r src dist/
	cp -r bin dist/
	cp Makefile dist/
	cp README dist/
	tar -cvf dist.tar dist/
	rm -rf dist/
