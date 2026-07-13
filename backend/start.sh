#!/bin/bash
# Start backend -- requires JDK 17+ and Maven

# Override system JAVA_HOME (yours points to an exe file, not a JDK dir)
export JAVA_HOME="C:/Program Files/Java/jdk-21"
export PATH="$JAVA_HOME/bin:/c/maven/bin:$PATH"

echo "=== Java ==="
java -version

echo ""
echo "=== Maven ==="
mvn --version | head -2

echo ""
echo "=== Starting Spring Boot ==="
mvn spring-boot:run
