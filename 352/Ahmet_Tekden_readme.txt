I used dataset "Turkish Writers who born after 1960, where they born and their gender."

I used Sparql Query:
  SELECT ?Writer ?WriterLabel ?BirthLabel ?GenderLabel
  WHERE
  {
    ?Writer wdt:P106 wd:Q36180.
    ?Writer wdt:P27 wd:Q43.
    ?Writer wdt:P19 ?Birth.
    ?Writer wdt:P21 ?Gender.
    ?Writer wdt:P569 ?year.
    SERVICE wikibase:label { bd:serviceParam wikibase:language "en" }
    FILTER (YEAR(?year) >= 1960)
  }
Few Example Data:
 wd:Q297454	  ------- Ömer Asan --------- Trabzon	--------------- male  
 wd:Q270739  	------- Elif Şafak -------- Strasbourg ------------	female
