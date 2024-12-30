include .env

build-server:
	docker-compose down
	docker-compose build
build-test:
	docker build --build-arg HOST=${HOST} -f ./selenium-threads/Dockerfile --build-arg SERVER_PORT=${SERVER_PORT} -t threads-selenium-threads ./selenium-threads
build:
	docker-compose down
	docker-compose build
	docker build --build-arg HOST=${HOST} -f ./selenium-threads/Dockerfile --build-arg SERVER_PORT=${SERVER_PORT} -t threads-selenium-threads ./selenium-threads
run-server:
	docker-compose up -d
run-test:
	echo "Waiting for the service to start on localhost:${SERVER_PORT}/api/users..."
	until curl --output /dev/null --silent --head --fail http://localhost:${SERVER_PORT}/api/users; do printf '.'; sleep 1; done;
	docker run --rm -e SUITE_XML=${SUITE_XML} \
-v ./selenium-threads/src/test/:/test/src/test/ \
-v ./output/:/test/output/ \
-v ./report/:/test/target/surefire-reports/ \
threads-selenium-threads
run:
	docker-compose up -d
	echo "Waiting for the service to start on localhost:${SERVER_PORT}/api/users..."
	until curl --output /dev/null --silent --head --fail http://localhost:${SERVER_PORT}/api/users; do printf '.'; sleep 1; done;
	docker run --rm -e SUITE_XML=${SUITE_XML} \
-v ./selenium-threads/src/test/:/test/src/test/ \
-v ./output/:/test/output/ \
-v ./report/:/test/target/surefire-reports/ \
threads-selenium-threads
	docker-compose down
debug:
	docker-compose up -d
	until curl --output /dev/null --silent --head --fail http://localhost:${SERVER_PORT}/api/users; do printf '.'; sleep 1; done;
	docker run --rm -it  --entrypoint="/bin/bash" -v ./selenium-threads:/test/ threads-selenium-threads
	docker-compose down