#!/bin/bash

usage=$(df -h | grep 'C:' | awk '{print $6}' | cut -d'%' -f1)
if [ $usage -gt 30 ]
then
	echo "$(date)" >> log.log
	echo "Disk Usage has exceeded limit !!" >> log.log
fi		


# Oracle Database Connection Details
DB_USER=CS
DB_PASSWORD=123
DB_SID=XE

# Date Format for Backup File
DATE_FORMAT=$(date +"%Y%m%d_%H%M%S")

# Export File Name (only the file name, not the full path)
EXPORT_FILE="backup_${DATE_FORMAT}.dmp"

# Oracle Data Pump Export Command
expdp ${DB_USER}/${DB_PASSWORD}@${DB_SID} DIRECTORY=DATA_PUMP_DIR DUMPFILE=${EXPORT_FILE} FULL=Y

# Check if the export was successful
if [ $? -eq 0 ]; then
    echo "Database backup successful. File: ${EXPORT_FILE}"
else
    echo "Error: Database backup failed."
fi
