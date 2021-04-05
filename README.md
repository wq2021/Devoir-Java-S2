# Projet Java : Interaction avec base de données relationnelle

## Info sur le projet

Pour lancer le programme:

Lancer `Main` avec `path/vers/le/fichierCSV` : va initialiser (créer des tables) et remplir automatiquement la base avec donées dans le fichier csv ;

Lancer `Main` seulement : va entre directement dans la stade de création.

## Configuration de Base de données avant l'utilisation (pour Windows10)

### Télécharger et Installer MySQL

Pour utiliser notre programme, vous devez avoir MySQL installé dans votre machine.

#### Si vous ne l'avez pas:

1. Allez dans sa [page officielle](https://dev.mysql.com/downloads/) pour la version community, choisir le **MySQL Community Server** et puis télécharger;

    ![image1](img/image1.png)
    ![image2](img/image2.png)

2. Mettez le fichier zip dans votre répertoire choisi et puis le dézippé.
    ![image3](img/image3.png)

    Entrez dans son répertoire:
    ![image4](img/image4.png)

3. Ouvrez le cmd de Windows:

    - allez dans le sous-répertoire `bin` de mysql et puis lancez l'installation avec commande `mysqld -install`
    ![image5](img/image5.png)

        **Si vous rencontrez l'erreur comme "Install/Remove of the Service Denied!", c'est un problème de droit, rouvrir la fenêtre cmd comme administrator et puis vous aurez pas de problème.**

        Installation avec succès:
        ![image6](img/image6.png)

#### Configuration de MySQL

Toujours dans cmd, dans `mysql-8.0.23-winx64\bin`, lancez `mysqld -initialize`, cette commande va créer un sous-répertoire `data` dans `mysql-8.0.23-winx64`, entrez dans ce sous-répertoire et trouver un fichier avec extension `.err`, qui contient un mot de passe temporaire pour la première utilisation de MySQL. Ouvrez-le avec votre éditeur et puis trouver le mot de passe.
![image7](img/image7.png)
![image8](img/image8.png)
![image9](img/image9.png)

Rentrez maintenant dans `mysql-8.0.23-winx64`, créez un nouveau fichier `my.ini`, qui est la fichier de configuration de mysql, dans lequel, écrivez ces lignes:

```
[mysqld]
basedir=C:\Program Files\mysql-8.0.23-winx64
datadir=C:\Program Files\mysql-8.0.23-winx64\data
port=3306
```

**Attention: le chemin écrit après `basedir` et `datadir` est l'endroit où vous avez dézippé mysql**

![image10](img/image10.png)

Sauvegardez-le, et puis on peut démarrer le service `mysql` avec `net start mysql` :
![image11](img/image11.png)

Tapez `mysql -u root -p` pour login (comme utilisateur `root`), utilisez le mot de passe qu'on vient de trouver.

Login avec succès:
![image12](img/image12.png)

Ensuite, changez le mot de passe avec `ALTER USER root@localhost IDENTIFIED BY 'new_password'`
![image13](img/image13.png)

**Attention: pour utiliser directement le fichier properties dans notre programme, il vaut mieux garder l'user `root` et changer le mot de passe à `ljy`. Si vous utilisez d'autre configuration, il faut aussi changer ces infos dans `projetBDR/resources/properties`**

Maintenant, vous démarrez MySQL dans répertoire 
`mysql-8.0.23-winx64\bin`, pour pouvoir l'utilisez dans cmd, n'importe où vous êtes, il faut ajouter le chemin absolu vers `\mysql-8.0.23-winx64\bin` au **PATH** dans la partie "variable système", on ne le traite pas ici.

Finalement, nous créons notre base de données avec la ligne de commande suivante :

`create database paristournage default character set utf8 collate utf8_bin;`

**Attention: idem, pour pouvoir directement utilisez la configuration dans notre programme, il vaux mieux garder le nom de base comme "paristournage", et on spécifie ici l'encodage à utiliser pour que notre base de données prenne en compte la casse et les accents**

Maintenant, notre base de données est prête pour être connectée !

### Télécharger MySQL driver

Pour pouvoir se connecter ay MySQL avec java, nous avons besoin d'un driver mysql-connector, téléchargez-le [ici](https://dev.mysql.com/downloads/connector/j/), choisissez "Platform Independent" pour OS.
![image14](img/image14.png)

Ensuite, ajouter le `mysql-connector-java-8.0.23.jar` dans notre projet dans eclipse. Maintenant, tout est prêt pour le lancement!