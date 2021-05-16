# RoboRally - Projekt i Software-udvikling (02362)
I projektet har fokus været på at videreudvikle et Java program der repræsenterer det strategiske brætspil RoboRally. Roborally har indtil nu været et fysisk spil,  hvor spillerne skal programmere deres robotter i mål inden deres medspillere, samtidig med at sabotere hinandens gameplans.



## Spillerregler 
- Vælg mellem 2 - 6 spillere. 
- Hver spiller får tildelt sin farve robot. 
- Hver spiller tildeles 8 programmeringskort til deres dæk. 
- Alle spiller programmere deres robot ved at trække 5 programmeringskort i de 5 registre.
- Programmerings kortene viser forskellige retninger f.eks. 3 skridt frem, drej til højre, 1 skridt tilbage osv. så det er nu et spørgsmål om at planlægge sin robots vej til den -næste checkpoint. 
- Læg programmerings kortene i den rækkefølge, som robotten skal bevæge sig i.
- Spillet foregår i runder, der består af en programmeringsfase og derefter en aktiveringsfase, hvor hver runde eksekveres i et register hos hver af spillerne. 
- Efter programmeringsfasen vil aktiveringsfasen starte starte ved at trykke “finish programming”. 
- Derefter kan der vælges om det enkelte register  skal eksekveres ved at trykke “Execute current register”.
- Eller alle register for alle spillere skal eksekveres ved at trykke “Execute program”.
- Aktiveringsfasen vil aktivere hvert register efter  registrenes rækkefølge. Dvs register 1 eksekveres for alle robotter før register 2 vil eksekveres. 
- Eksekveringen vil ske i rækkefølgen spiller 1 til spiller 6.
- Under aktiveringsfasen vil robotten udfører de handlinger der er på de kort der er placeret i registreret. 
- Når alle 5 registre er eksekveret for alle robotterne  vil programmeringsfasen gentage sig.
- Den Spiller der først når alle checkpoints i den rigtige rækkefølge vinder spillet. 



## Features i nuværende version

- Spillere har mulighed for at skubbe hinanden et felt.

Felter:
Spillepladen indeholder forskellige udfordringer som robotterne vil møde på deres vej:
- Væg: Robotter kan ikke bevæge sig igennem felter hvor der er placeret vægge. 
- Grøn conveyorbelt: Lander en robot på den grønne conveyorbelte vil den automatisk blive rykket et felt frem. Robotten vil følge beltet i den retning pilene pejer. 
- Blåt conveyorbelt: Hvis en robot lander på det blå conveyorbelte vil robotten rykke to felter frem i pilenes retning. 
- Grøn Gear: Lander robotten på dette felt vil den blive drejet til højre.
- Rød Gear: Lander robotten på dette felt vil den blive drejet til venstre.
- Checkpoint: Robotterne skal samle checkpoints. Først checkpoint 1 derefter checkpoint 2. 



De 8 forskellige programmeringskort:
- Move one step forward
- Move two steps forward
- Move three steps forward
- Turn left
- Turn right
- Turn back
- Left or right
- Move one step back


Database:
- Mulighed for at gemme et spil i databasen med dets respektive navn.
- Hente det gemte spil fra databasen.
