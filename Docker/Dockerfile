FROM openjdk:11

WORKDIR /worker
COPY worker-project/src/ .

RUN javac Main.java

CMD ["java", "Main"]
