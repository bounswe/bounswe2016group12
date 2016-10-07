I have used the following query, which extracts the people who were Physicists and Astronomers at the same time, as well as were born between years 1800 and 1950. Their name, birth coordinates and birth dates were asked for:

	SELECT DISTINCT ?person (SAMPLE(?personlab) AS ?personlabsampl) (SAMPLE(?birthcoord) AS ?birthcoordsampl) (SAMPLE(?birthdate) AS ?birthdatesampl)
	WHERE
	{
		?person wdt:P106 wd:Q169470 .
		?person wdt:P106 wd:Q11063 .
		SERVICE wikibase:label
	    {
	      bd:serviceParam wikibase:language "en,de,es,it,ru" .
	      ?person rdfs:label ?personlab
	    }

		?person wdt:P19 ?birthplace .
		?person wdt:P569 ?birthdate filter(?birthdate > "1800-01-01T00:00:00+00:00"^^xsd:dateTime) filter(?birthdate < "1951-01-01T00:00:00+00:00"^^xsd:dateTime) .
		
		?birthplace wdt:P625 ?birthcoord .
	  }
	GROUP BY ?person
	ORDER BY ?birthdatesampl

Two examples from the results of that query are:
- wd:Q20018 / Geogre Biddell Airy / Point(-1.71, 55.41) / 1801-07-27
- wd:Q731321 / James Challis / Point(0.55, 51.88) / 1803-12-12