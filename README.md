# ServiceLog

ServiceLog to projekt pokazowy, który stworzyłem w ramach nauki Java + Spring Boot + Spring MVC + Spring JPA

Inspiracją od stworzenia Servicelog były zmiany w przepisach dotyczących urządzeń transportu bliskiego (windy, żurawie, suwnice) dotyczące możliwości prowadzenie dzienników konserwacji w formie elektronicznej. Dziennik konserwacji jest prawnie wymagany dla urządzeń transportu bliskiego, a na rynku brak sensownych rozwiązań oferujących prowadzenie go w formie elektronicznej.

ServiceLog składa się z:
- Panelu logowanie, rejestracji i strony z danymi kontaktowymi dla uzytkowników niezalogowanych.
- Service Database dla uzytkowników zalogowanych.
Service Database pozwala na przeglądanie listy maszyn danego użytkownika i podgląd wpisów serwisowych dla każdej maszyny.
Użytkownik (reprezentant firmy (ROLE_COMPANYUSER)) ma możliwości wyboru przedsiębiorstwa (z którym jest powiązany) i dodawania, edycji, usuwania maszyn.
Użytkownik (serwisant (ROLE_SERVICEMAN)) ma możliwości wyboru przedsiębiorstwa (z którym jest powiązany) i dodawania wpisów serwisowych do maszyn.
- Panelu administratora dla administratora
Administrator ma możliwość dodawania, edycji i usuwania firm, edycji i usuwania użytkowników, powiązania użytkowników z przedsiębiorstwami, nadawania uprawnień użytkownikom.

Instrukcja obsługi:
- Utworzenie bazy danych
Aplikacja ServiceLog łączy się z bazą PostgreSQL. W celu wygodnego odpalenia aplikacji polecem wykorzystać kontener Dockera.
Komenda do utworzenia bazy (nazwa, port i hasło kompatybilne z alikacją):

docker run --name servicelog-psql -p 5432:5432 -e POSTGRES_DB=servicelog -e POSTGRES_PASSWORD=password -d postgres

- Odpalenie aplikacji (przykładowe kroki dla pokazania funkcjonalności)
Zarejestrować się jako nowy użytkownik.
Zalogować się jak administrator login: admin2, hasło: admin2.
W panelu administratora:
	- dodać nowe przedsiębiorstwo do Service Database (przez listę przedsiębiorstw),
	- dodać uprawnienia dla nowego użytkownika (przez listę użytkowników),
	- powiązać zarejestrowanego użytkownika z przedsiębiorstwem (przez listę użytkowników),
Wylogować się z konta administratora
Zalogować sie jako zarejestrowany na początku użytkownik
W zależności od nadanych uprawnień można w menu Service Database -> Company (nazwa przedsięiorstwa do którego nadano wcześniej uprawnienia)
    - utworzyć nową maszynę (reprezentant firmy (ROLE_COMPANYUSER))
    - utworzyć zapisy wykonanych czynności serwisowych (serwisant (ROLE_SERVICEMAN))

- Testowanie aplikacji
Testy aplikacji działają niezależnie od głównej aplikacji - na bazie H2 z wgrywanymi do testów komendami SQL












