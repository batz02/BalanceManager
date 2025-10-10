
Progetto svolto da Matteo Battilori (matricola n.164901).

Tutti i punti della traccia sono stati svolti, compresi quelli facoltativi.



Sono presenti 3 diverse directory:

- doc: contenente i file di documentazione con prima pagina index.html

- lib: contenente la libreria jOpenDocument-1.5.jar per l'esportazione in file .ods

- src: contenente tutti i file .java per lo sviluppo del progetto


Inoltre in src si trovano le seguenti sotto-directory:

- production_files: contenente tutti i file .class derivanti dalla compilazione con il comando "javac"

- META-INF: contenente il file MANIFEST.MF per la creazione del file .jar a partire dalla Main-Class



Sempre nella directory principale si trovano: 

- runner.sh: uno script per la compilazione e l'esecuzione del progetto

- BalanceManager.jar: l'applicazione vera e propria, facilmente eseguibile con il comando "java -jar BalanceManager.jar"



Ogni 90 secondi un Thread avvia il salvataggio automatico della tabella principale su file .tmp in un path comunicato periodicamente su terminale

