FROM ubuntu:focal
RUN apt-get update && \
    apt-get upgrade -y && \
    DEBIAN_FRONTEND="noninteractive" apt-get install -y \
        unzip curl wget
RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.0/graalvm-ce-java17-linux-amd64-22.3.0.tar.gz
RUN tar -xzf graalvm-ce-java*-linux-amd64-*.tar.gz
RUN apt-get update && apt-get install -y \
    gcc zlib1g-dev clang build-essential
ENV PATH=/graalvm-ce-java17-22.3.0/bin:$PATH
ENV JAVA_HOME=/graalvm-ce-java17-22.3.0
ENV GRAALVM_HOME=/graalvm-ce-java17-22.3.0
