#!/bin/bash
mysql -h 127.0.0.1 -u ${MYSQL_USER} -p${MYSQL_ROOT_PASSWORD} -e "SELECT 1" ${MYSQL_DATABASE}
