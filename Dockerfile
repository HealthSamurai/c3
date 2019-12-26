FROM bellsoft/liberica-openjdk-alpine:13

RUN apk update && apk add jq curl

ADD target/c3-0.0.1-standalone.jar /c3.jar

CMD java -XX:-OmitStackTraceInFastThrow \
  -Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.host=127.0.0.1 \
  -Djava.rmi.server.hostname=127.0.0.1 \
  -Dcom.sun.management.jmxremote.port=9099 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -jar /c3.jar -m c3
