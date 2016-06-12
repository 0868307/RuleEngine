
# Requests

## GET
----
/login
----
### Description
----
Geeft een authtoken terug
----
## GET
----
/whoami
----
### Description
----
Geeft alle gegevens van de huidige user terug
----
## GET
----
/user/{uuid}
----
### Description
----
geeft de gegevens van een user op met {uuid}
----
(nog niet functioneel)
## GET
----
/projects/{uuid}
----

### Parameters
----
from (epoch)
to (epoch)
----
### Description
----
geeft de projecten van een user op met {uuid}
----

## POST
----
/sonar
----

### Description
----
Bedoeld voor Jenkins om zijn antwoord naar te sturen
----

