FROM postgres:9.5


COPY init.sql /docker-entrypoint-initdb.d/init.sql
COPY set-pg-conf.sh /docker-entrypoint-initdb.d/set-config.sh