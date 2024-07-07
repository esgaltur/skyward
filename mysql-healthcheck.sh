#!/bin/bash
echo "checking the health of the "${MYSQL_DATABASE}
mysql -h database -u ${MYSQL_ROOT_USER} -p${MYSQL_ROOT_PASSWORD} -e "SELECT 1" ${MYSQL_DATABASE}
