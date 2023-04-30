# Mathmouse

The technologic evolution of the last decades triggered an increase in the volume of time series to process, in numerous fields such as experimental science. The experimental time series data are processed to find a mathematical model (usually a differential equation) fitting the series. Due to the amount of available data and technologies to process them, the amount of available mathematical models is increasing as well. Therefore, storing and organizing those models to ease their management and retrieval has become an essential challenge. 

The **MathMOuse** project (Mathematical MOdels warehoUSE) is an enriched Data Warehouse structure able to: 

* store mathematical models in a relational database, 
* provide the user a "query by data" system to query the Warehouse using raw time series as input parameter.

## Software requirements

For all OS:
* Java JRE >= 1.8
* Docker (for people who can't install Docker, a vagrant configuration file is supplied)

**NOTE**: If you want to use vagrant, you will need:

* Virtualbox >= 5
* Vagrant >= 1.8.7

In a command prompt, browse to your local copy of the project and type:

    $ vagrant up

A virtual machine, will be automatically set up. Then:

    $ vagrant ssh

The command will connect you to the newly created virtual machine. 

## Download

The **MathMOuse** project files can be downloaded at https://github.com/lias-laboratory/mathmouse/releases

The archive file (_mathmouse-bda-2017.zip_) is composed of:

* _docker-compose.yml_: is a configuration file, that will be used to create the necessary services
* _dockermanager-0.0.1-SNAPSHOT.jar_: a jar file, that contains binary code for building the services and manage them.
* _README.md_:
* _Vagrantfile_: a configuration file to create a virtual machine
* _bootstrap.sh_: an additional configuration file to set up a virtual machine created by vagrant 

The _microservices_ directory is composed of:
* _javarabbitmq_: a directory containing files (_Dockerfile_ and _init.sh_) to configure the rabbitmq service.
* _mathmousedb_: a directory containing files (_init-db.sh_ and _init-db.sql_) to configure the postgresql service and the database.
* _mathmouse_: a directory containing binary jars for other java services of the projects. As well as _Dockerfile_ configuration files, for the services. In the _dbgui_ there is an additional _resources_ directory with XML and CSV files, used as data samples of equations and time series, respectively. In the _populator_ directory, there is another _resources_ files, with  XML files, that can be used to quickly populate the database for tests purposes.

## Prototype architecture

The application contains the following services:

* mmw_db, build from the official postgreSQL container of docker (https://hub.docker.com/_/postgres/), version 9.6.1. It contains the database. Its configuration files are in the folder microservices/mmw_db.
* rabbitmq, the rabbitMQ official container of docker (https://hub.docker.com/_/rabbitmq/), version 3-management. It is the communication server of the services.
* dbservice, that manage data flow between the database and the other services.
* generator, that takes the equations contained in the database, solves them numerically to generate time series and sends them via rabbitmq.
* comparator, takes input series (it is possible to use the GUI to send a series to it) and ask the generator for series to compare the input with. Then, it sorts out the series that matches the input, those that do not and those that could not be sorted into the previous categories. The result of the comparison is then sent via rabbitmq (the GUI can intercept them, to print them out).

## Installation

The prototype where developped and tested under Linux. 
Using a command prompt, browse to your local copy of the project and type:

    $ cd NOM_DU_PROJET
    $ java -jar dockermanager-0.0.1-SNAPSHOT.jar

This command will build and set up the necessary services (**_rabbitmq_**, **_postgresql_**, **_comparator manager_**, **_comparator_**, **_generator_**). To start the GUI, you will need to use the command prompt and browse to your local copy of the project. Then, type:

    $ cd microservices/mathmouse/dbgui

And:

    $ java -jar dbgui-0.0.1-SNAPSHOT.jar

to start the GUI.

## Compilation

### With Docker

**Prerequisite**: docker version 1.12.6 + oracle java 8.

**help**: for linux users, you can use the following commands with super admin permissions
to install oracle java 8, if not already installed:

```bash
$ apt-get install -y oracle-java8-installer
$ apt-get install -y oracle-java8-set-default
```

Download the files of the project on your local machine. Open a command prompt, browse to your local copy of the project and type:

```bash
$ sudo ./build_install.sh
```

This will automatically build the jars files of each needed projects, build the containers and launch them all.

### With Vagrant (Docker inside the Vagrant VM)

**Prerequisite**: vagrant + virtualbox installed and functionnal.
**Warning**: The prototype were built with the version 5.1.0 of virtualbox and the version 1.8.7 of vagrant.
It has not been test with other versions, nor other virtualizing tools.

Open a command prompt and browse to the Vagrantfile directory:

```bash
$ vagrant up
$ vagrant ssh
```

Once in the VM Machine command line:

```bash
$ cd /vagrant
```

The command:

```bash
$ sudo ./build_install.sh
```

will automatically build the jars files of each needed projects, build the containers and launch them all.

*Note:* you will be asked to accept the license terms of Oracle.

## Logs the applications

So each application should now be running in background and you can access the logs of each application separately, by using:

```bash
$ sudo docker logs [-f] put_container_name
```

You can access the rabbitmq webadmin interface with the following link: http://localhost:15672

* Login: guest
* Password: guest

Once on the server, you can click on the "Queues" tab and then on the queue named "topPopulateRequest". If you go to the "Publish Message" section, you can click on the "Publish Message" button. This will send an event to the db_populator service, that will a set of three equations to the API service, that will store them into the database. Every two minute the db_reader service asks the API for all the equations contained in the database and prints the result.

## Using the GUI

The GUI needs graphics to work. It is possible to start from host command line. In the dbreadergui root directory: `microservices/mathematicalmodelswarehouseprototype/dbreadergui`

type:

```bash
$ java -jar target/dbreadergui-0.0.1-SNAPSHOT.jar localhost
```

The GUI contains three buttons:

* Send, to send an equation from an XML formatted file. Three files examples are available in the file src/main/resources/xmls1.0 directory in the dbreadergui project.
* Pull, retrives all the equations available in the database and displays them on a dedicated window.
* Query, allows to write a query to send to the dbservice, to execute it. The results are also displayed in another dedicated window.
* Compare, to ask for series comparison, using a series stored in csv formatted text file. There are series exemples available in the directory src/main/resources/timeseries-sources in the dbreadergui project.

A text space shows the different queries that are executed on the database by the dbservice. For more details on how to use the GUI, with a use case scenario check the docs/tutorial.md file.

## Scenario

### Add a model

Some XML files are available in the _microservices/mathmouse/dbgui/resources/xmls_ directory. To add the file content into the database:

* Click on the "Upload" (above the navigation tree in the Equation tab) button of the GUI window
* Select a file
* Click on "Open"
* Click on "Validate"

You can check if the new equation appeared in the navigation tree.

### Query the database

Some CSV files are available in the _microservices/mathmouse/dbgui/resources/csvs_. To query the database for a model fitting the data:

* Click on the "Create Jobs" Tab
* Click on "Open"
* Select a file
* Click on "Open"
* A line should have appear in the main window
* Cross the box on the right
* Click "Start"

If you click on the "Jobs Manager" tab, you will see a new line, with the jobs state and progression. To check the results, click on "Details" and a new window should pop-up. The results should appear be constantly updating. The window is divided in three tabs.

* The tab "Accepted Models" is the list of models that fits the data.
* The tab "Rejected Models" is the list of models that does not fit the data.
* The tab "Undetermined Models" is the list of models that could not be classified into the two first categories.

**NOTE**: if you want to quickly populate the database, a sample of 100 models is ready and available. You can check the XML files in the microservices/mathmouse/populator/resources/xmls directory.
If you want to add them quickly in the database, you will have to close the GUI, if it is running. Then browse to the _microservices/mathmouse/populator_ directory and type:

    $ java -jar populator-0.0.1-SNAPSHOT.jar

## References

* Cyrille PONCHATEAU, Ladjel BELLATRECHE, Carlos Ordonez, Mickael BARON, A Database Model for Time Series : From a traditional Data Warehouse to a Mathematical Models Warehouse, 32th French Conference On Advanced Databases (BDA 2016), 2016

* Cyrille PONCHATEAU, Ladjel BELLATRECHE, Mickael BARON, Entrepôt de Données dans l'ère Data Science : De la Donnée au Modèle, 12émes Journées Francophone sur les Entrepôts de Données et analyse en ligne (EDA 2016), edited by RNTI, 2016, pp. 65-79

## Software licence agreement

Details the license agreement of Mathmouse: [LICENSE](LICENSE)

## Historic Contributors (core developers first followed by alphabetical order)

* [Cyrille PONCHATEAU (core developer](https://www.lias-lab.fr/members/cyrilleponchateau/)
* [Mickael BARON](https://www.lias-lab.fr/members/mickaelbaron/)
* [Ladjel BELLATRECHE](https://www.lias-lab.fr/members/bellatreche/)
* [Carlos ORDONEZ](http://www2.cs.uh.edu/~ordonez/)

## Code Analysis

* Lines of Code: 13 077
* Programming Language: Java
