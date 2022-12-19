Instructions de déploiement :

1) Installer un client mongoDB comme MongoDB Compass, avec une base de données s'appelant "pp3-db" et une collection à l'intérieur s'appelant "users".
2) Installer maven en ligne de commande (en tappant "mvn" dans le cmd, il ne doit pas y avoir d'erreur)

Puis dans /pp3, exécuter les commandes suivantes :

3) faire un "mvn clean install" pour installer toutes les dépendances, attendre la fin
4) écrire "run A B C D"        (où A, B, C et D sont des nombres)

Cela va lancer tout le projet avec :
A instances de registration-service
B instances de login-service
C instances de balance-service
D instances de payment-service

4) Attendez environ 1min le temps que tout se mette bien en place
5) L'application est disponible, ouvrez /frontend/home.html

![alt text](https://cdn.discordapp.com/attachments/1025467385978826905/1053498275749179412/image.png)
