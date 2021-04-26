.PHONE: clean build

clean:
	lein clean

target/default+uberjar/ori:
	lein native-image

build: target/default+uberjar/ori

release: target/default+uberjar/ori
	zip -j ori-0.1.0.zip target/default+uberjar/ori