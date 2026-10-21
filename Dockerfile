FROM eclipse-temurin:17-jdk AS build
WORKDIR /build
COPY lib ./lib
COPY src ./src
RUN javac -encoding UTF-8 \
      -cp "lib/*" \
      -processorpath "lib/lombok-1.18.30.jar" \
      -d out \
      $(find src -name "*.java")

FROM eclipse-temurin:17-jre
ENV TZ=Asia/Shanghai \
    LANG=C.UTF-8 \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
WORKDIR /app
COPY --from=build /build/out ./out
COPY --from=build /build/lib ./lib
CMD ["java", "-cp", "out:lib/*", "controller.App"]
