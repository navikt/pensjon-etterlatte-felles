FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21
ENV TZ="Europe/Oslo"
ENV JDK_JAVA_OPTIONS="-Dhttp.proxyHost=webproxy.nais -Dhttps.proxyHost=webproxy.nais -Dhttp.proxyPort=8088 -Dhttps.proxyPort=8088 -Dhttp.nonProxyHosts=localhost|127.0.0.1|10.254.0.1|*.local|*.adeo.no|*.nav.no|*.aetat.no|*.devillo.no|*.oera.no|*.nais.io|*.aivencloud.com|*.intern.dev.nav.no"
WORKDIR /app
COPY build/libs/*.jar ./
EXPOSE 8080
USER nonroot
CMD ["-jar", "app.jar"]