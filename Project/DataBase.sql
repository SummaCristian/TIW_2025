create database if not exists Progetto_Tiw;
use Progetto_Tiw;

create table Utente(
		IdUtente int  not null auto_increment primary key,
        Username varchar(255) not null unique,
        Psw varchar(255) not null,
        Nome varchar(255) not null,
        Cognome varchar(255) not null,
        Indirizzo varchar(255) not null
);

create table Immagine(
	Id int not null auto_increment primary key,
    NomeFile varchar(255) not null,
    Percorso varchar(255) not null unique
);

create table Asta(
	IdAsta int not null auto_increment primary key,
    BaseDAsta int not null,
    RialzoMinimo int not null,
    OffertaMax int not null,
    DataChiusura date not null,
    Venduto Boolean default false,
    IdUtenteCompratore int,
    IdUtenteVenditore int not null,
    PrezzoVendita int,
    foreign key(IdUtenteCompratore) references Utente(IdUtente),
	foreign key(IdUtenteVenditore) references Utente(IdUtente)
);

create table Articolo(
	Codice int not null auto_increment primary key,
    Nome varchar(255) not null,
    Descrizione varchar(1023) not null,
    Prezzo int not null,
    IdImmagine int not null,
    IdUtenteCreante int not null,
    idAsta int not null,
    foreign key(IdImmagine) references Immagine(Id),
    foreign key(IdUtenteCreante) references Utente(IdUtente),
    foreign key(IdAsta) references Asta(IdAsta)
);

create table Offerta(
	IdOfferta int not null auto_increment primary key,
    IdUtente int not null,
    PrezzoOfferta int not null,
    DataOfferta date not null,
    OraOfferta time not null,
    foreign key(IdOfferta) references Utente(IdUtente)
)

