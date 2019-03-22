import time
import os
import os.path
print('run> SCiPNet.exe')
time.sleep(0.5)
print('Successfully executed SCiPNet')
time.sleep(2)
print('Connecting to the foundation database')
time.sleep(5)
print('Connexion failed ! Turning to local save !')
f = open('txt/assets/logo.txt', 'r')
file_contents = f.read()
print (file_contents)
f.close()
setwhile = 1

while setwhile == 1:
    cmd = input('@SCP Terminal> ')
    liste = cmd.split(' ')
    nbargs = len(liste)
    if nbargs == 1:
        cmd = cmd + ' 1' + ' 1'
    else:
        if nbargs == 2:
            cmd = cmd + ' 1'
    x,y,z = cmd.split(' ')
    if x == 'stop' or x == 'close' or x == 'logout' or x == 'disconnect':
        setwhile = 0
    else:
        if x == 'scp':
            if y == '1':
                print('[ERROR]: Syntaxe: scp list|view [NOM]')
            else:
                if y == 'view':
                    if z == '1':
                        print('Faites scp list pour avoir la liste des SCP')
                    else:
                        path = 'txt/SCP/' + z + '.txt'
                        if os.path.exists(path):
                            f = open(path, 'r')
                            file_contents = f.read()
                            print (file_contents)
                            f.close()
                        else:
                            print('SCP Inconnu: ' + z)
                else:
                    if y == 'list':
                        scplist = os.listdir('txt/SCP/')
                        print('LISTE DES SCP DE LA BASE DE DONNEE:')
                        print(scplist)
        else:
            if x == 'commands':
                f = open('txt/assets/cmd.txt', 'r')
                file_contents = f.read()
                print (file_contents)
                f.close()
            else:
                if x == 'rapport':
                    if y == 'view':
                        if z == 1:
                            print('Faites rapport list pour avoir la liste des rapports')
                        else:
                            path = 'txt/rapports/' + z + '.txt'
                            if os.path.exists(path):
                                f = open(path, 'r')
                                file_contents = f.read()
                                print(file_contents)
                                f.close()
                            else:
                                print('Rapport inconnu: ' + z)
                    else:
                        if y == 'list':
                            print('LISTE DES RAPPORTS:')
                            rlist = os.listdir('txt/rapports/')
                            print(rlist)
                        else:
                            if y == '1':
                                print('[ERROR]: Syntaxe: rapport list|view [NOM]')
                else:
                    print('[ERROR]: Commande inconnue ! Faites commands pour voir la liste des commandes')