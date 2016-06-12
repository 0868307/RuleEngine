
# Requests

## Auth
```
GET
/login?username=xxx&password=xxx
Geeft een authtoken terug
```

```
GET
/logout
maakt de authtoken ongeldig
```

## Users
```
GET
/user/{uuid}
geeft de gegevens van een user op met {uuid}
```
```
GET
/whoami
Geeft de huidige user terug
```

```
GET
/user/achievements
geeft de achievements van een user terug
```

## Projects
```
GET
/projects/{uuid}
geeft de informatie van een project terug
```
```
GET
/projects
geeft alle projecten van een user terug
```

## Sonar
```
POST
/sonar
Bedoeld voor Jenkins om zijn antwoord naar te sturen
```
## Ranking
```
GET
/ranking
returns the ranking of the all the users in all projects
```


