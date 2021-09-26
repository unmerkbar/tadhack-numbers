#!/bin/bash

FILE=$1.csv
for (( i = 0; i < 10; i++ )); do
	curl -X GET "https://randommer.io/api/Phone/Generate?CountryCode=$1&Quantity=1000" -H "accept: */*" -H "X-Api-Key: YOUR_API_KEY" | jq -j | tr -d '[]",- ' | sed "1 d" >> $FILE
done

awk '!seen[$0]++' $FILE >> $FILE"_sorted_unique.csv"
rm -rf $FILE
mv $FILE"_sorted_unique.csv" $FILE

docker run --rm -it -v /YOUR_ABSOLUTE_PATH_TO_EXPORTED_LIB:/home:Z openjdk /bin/bash -c "/usr/bin/java -jar /home/processFile.jar /home/$FILE $1"

mv $FILE"_filtered" $1"_filtered.csv"
