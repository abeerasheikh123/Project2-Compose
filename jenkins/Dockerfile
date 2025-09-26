FROM jenkins/jenkins:lts

USER root

# install prerequisites and docker CLI (static binary) + docker-compose
RUN apt-get update && apt-get install -y curl ca-certificates tar && \
    DOCKER_CLI_VERSION="24.0.5" && \
    curl -fsSL "https://download.docker.com/linux/static/stable/x86_64/docker-${DOCKER_CLI_VERSION}.tgz" -o /tmp/docker.tgz && \
    tar -xzvf /tmp/docker.tgz -C /usr/local/bin --strip-components=1 docker/docker && \
    rm /tmp/docker.tgz && \
    DOCKER_COMPOSE_VERSION="v2.18.1" && \
    curl -L "https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

USER jenkins
