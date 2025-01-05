include .env

define check_suite_xml
	$(if $(SUITE_XML),, $(error SUITE_XML is not set. Please set SUITE_XML to the name of the testng.xml file))
endef


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
	$(call check_suite_xml)
	echo "Waiting for the service to start on localhost:${SERVER_PORT}/api/users..."
	until curl --output /dev/null --silent --head --fail http://localhost:${SERVER_PORT}/api/users; do printf '.'; sleep 1; done;
	docker run --rm -e SUITE_XML=${SUITE_XML} \
-v ./selenium-threads/src/test/resources:/test/src/test/resources \
-v ./output/webdriver/:/test/output/webdriver/ \
-v ./output/report/:/test/target/surefire-reports/ \
threads-selenium-threads
run:
	$(call check_suite_xml)
	docker-compose up -d
	echo "Waiting for the service to start on localhost:${SERVER_PORT}/api/users..."
	until curl --output /dev/null --silent --head --fail http://localhost:${SERVER_PORT}/api/users; do printf '.'; sleep 1; done;
	docker run --rm -e SUITE_XML=${SUITE_XML} \
-v ./selenium-threads/src/test/resources:/test/src/test/resources \
-v ./output/webdriver/:/test/output/webdriver/ \
-v ./output/report/:/test/target/surefire-reports/ \
threads-selenium-threads
	docker-compose down
debug:
	$(call check_suite_xml)
	docker-compose up -d
	echo "Waiting for the service to start on localhost:${SERVER_PORT}/api/users..."
	until curl --output /dev/null --silent --head --fail http://localhost:${SERVER_PORT}/api/users; do printf '.'; sleep 1; done;
	docker run --rm -e SUITE_XML=${SUITE_XML} \
-v ./selenium-threads/:/test/ \
-v ./output/webdriver/:/test/output/webdriver/ \
-v ./output/report/:/test/target/surefire-reports/ \
threads-selenium-threads
	docker-compose down