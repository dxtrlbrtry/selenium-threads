FROM --platform=linux/amd64 openjdk:17-jdk-slim

ARG HOST
ARG SERVER_PORT
ENV HOST=$HOST
ENV SERVER_PORT=$SERVER_PORT

# # Install system libraries
RUN apt-get update && apt-get install -y curl unzip bzip2 maven

# Install Google Chrome
RUN curl -LO  https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt-get install -y ./google-chrome-stable_current_amd64.deb && \
    rm google-chrome-stable_current_amd64.deb

# Install chromedriver
RUN curl -LO https://storage.googleapis.com/chrome-for-testing-public/131.0.6778.204/linux64/chromedriver-linux64.zip && \
    unzip ./chromedriver-linux64.zip && \
    mv ./chromedriver-linux64/chromedriver /usr/bin/chromedriver && \
    rm -rf ./chromedriver-linux64.zip ./chromedriver-linux64

# Install firefox
RUN curl -LO https://download-installer.cdn.mozilla.net/pub/firefox/releases/133.0.3/linux-x86_64/en-US/firefox-133.0.3.tar.bz2 && \
    tar -xvf firefox-133.0.3.tar.bz2 -C /opt/ && \
    ln -s /opt/firefox/firefox /usr/local/bin/firefox && \
    rm -rf firefox-133.0.3.tar.bz2

# Install geckodriver
RUN curl -LO https://github.com/mozilla/geckodriver/releases/download/v0.29.1/geckodriver-v0.29.1-linux64.tar.gz && \
    tar -xvf geckodriver-v0.29.1-linux64.tar.gz -C /usr/bin/ && \
    rm -rf geckodriver-v0.29.1-linux64.tar.gz

# Copy Test project
WORKDIR /test
COPY . /test/
RUN mvn -Dmaven.test.skip package

ENTRYPOINT ["mvn", "test", "-DsuiteXmlFile=${SUITE_XML}"]