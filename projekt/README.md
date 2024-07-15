# Projekt Darwin World z przedmiotu Programowanie Obiektowe
## Semestr zimowy, rok akademicki 2023/24
## Autorzy: Wojciech Michaluk & Michał Proć

### Struktura głównej części projektu
W katalogu src/main/java/agh/dg znajdują się główne pliki projektu.
Wyróżniamy w nim pliki:
- klasa *DarwinWorldApp*, która przygotowuje aplikację do wyświetlenia.
- klasa *ExtendedThread*, która zawiera implementację rozszerzenia klasy Thread, co jest używane w obsłudze wątków przy pauzowaniu symulacji i jej zamykaniu.
- klasa *Main*, która uruchamia program.
- klasa *Simulation*, która zawiera implementację symulacji świata zwierząt.
- klasa *Statistics*, która przechowuje dane statystyczne, wyświetlane w symulacji.

Ponadto znajdują się tu pakiety:   
*enums*, który zawiera typy wyliczeniowe używane w aplikacji
- klasa *GameCommunicat* zawiera komunikaty używane przy wyświetlaniu statystyk.
- klasa *MoveDirection* zawiera możliwe kierunki ruchu zwierzaków.

*exceptions*, który zawiera używane własne klasy wyjątków
- klasa *InvalidGameParamsException* zawiera implementację wyjątku przy podaniu nieprawidłowych parametrów gry.

*helpers*, który zawiera klasy pomocnicze do obsługi plików i konwersji danych
- klasa *CsvFileHandler* zawiera obsługę zapisu danych do pliu .csv.
- klasa *GameStatic* zawiera konwersję zwierzaka na kolor i danych statystycznych na String

*models*, który zawiera implementację świata symulacji i jego elementów
- pakiet *abstracts*, który zawiera klasy abstrakcyjne w ramach modeli
- - klasa abstrakcyjna *Animal* zawiera implementację cech wspólnych obu wariantów zwierząt, dziedziczy po WorldElement.
- - klasa abstrakcyjna *World* zawiera implementację cech wspólnych obu wariantów mapy.
- - klasa abstrakcyjna *WorldElement* zawiera implementację cech wspólnych elementów mapy.
- klasa *BasicAnimal* zawiera implementację standardowej wersji zwierzaka, dziedziczy po Animal.
- klasa *CrazyAnimal* zawiera implementację zwierzaka w wariancie ```nieco szaleństwa```, dziedziczy po Animal.
- klasa *CreepingJungle* zawiera implementację mapy w wariancie ```pełzająca dżungla```, dziedziczy po World.
- klasa *Plant* zawiera implementację rośliny na mapie, dziedziczy po WorldElement.
- klasa *RegularWorld* zawiera implementację standardowej wersji mapy, dziedziczy po World.
- klasa *Vector2d* zawiera implementację pozycji na płaszczyźnie.
- klasa *WorldElementBox* zawiera przedstawienie graficzne elementu mapy.

*observers*, który zawiera klasy realizujące wzorzec observer.
- interfejs *MapChangeListener*, który przedstawia schemat reagowania na zmiany mapy.

*presenters*, który zawiera klasy prezenterów
- klasa abstrakcyjna *BasePresenter*, która zawiera implementację cech wspólnych prezenterów.
- klasa *GamePresenter*, która opisuje prezenter okienka symulacji, dziedziczy po BasePresenter.
- klasa *MenuPresenter*, która opisuje prezenter panelu głównego programu - menu.

*records*, który zawiera rekordy używane w ramach projektu
- rekord *Boundary* zawiera pozycje Vector2d opisujące wymiary mapy.
- rekord *WorldConfig*, zawiera parametry symulacji wybrane w menu. 

W katalogu src/main/resources znajdują się zasoby projektu - pliki .csv, style, pliki .fxml.
- katalog *configs* zawiera przykładowe konfiguracje symulacji
- - plik *crazy_animal_config.csv* zawiera przykładową konfigurację dla wariantu ```nieco szaleństwa```.
- - plik *creeping_jungle_and_crazy_animal_config.csv* zawiera przykładową konfigurację dla obu zastosowanych wariantów ```pełzająca dżungla``` oraz ```nieco szaleństwa```.
- - plik *creeping_jungle_config.csv* zawiera przykładową konfigurację dla wariantu ```pełzająca dżungla```.
- katalog *statistics*, do którego zapisywane są pliki .csv ze statystykami (jeżeli zaznaczono taką opcję)
- - plik *.gitignore* wskazuje, że nie chcemy zapisywać plików ze statystykami do repozytorium.
- katalog *styles*, który zawiera opis styli .css
- - plik *game.css* zawiera opis styli dla okienka z symulacją.
- - plik *menu.css* zawiera opis styli dla głównego panelu - menu programu.
- plik *game.fxml* zawiera opis widoku dla okienka z symulacją.
- plik *menu.fxml* zawiera opis widoku dla menu.

W katalogu src/test/java/agh/dg znajdują się testy do wybranych klas, istotnych dla działania programu.
- pakiet *enums* zawiera testy dla typów wyliczeniowych
- - klasa *MoveDirectionTest* zawiera testy dla klasy *MoveDirection*.
- pakiet *models* zawiera testy dla klas tworzących model aplikacji
- - klasa *BasicAnimalTest* zawiera testy dla klasy BasicAnimal.
- - klasa *CrazyAnimalTest* zawiera testy dla klasy CrazyAnimal.
- - klasa *CreepingJungleTest* zawiera testy dla klasy CreepingJungle.
- - klasa *RegularWorldTest* zawiera testy dla klasy RegularWorld.
- - klasa *Vector2dTest* zawiera testy dla klasy Vector2d.
- klasa *SimulationTest* testuje kod metod z klasy Simulation (choć sama w sobie nie uruchamia symulacji).
- klasa *StatisticsTest* testuje poprawność obsługiwania danych statystycznych. 

### Podział pracy w ramach projektu

#### Wojciech Michaluk
- logika map i zwierząt (klasy z pakietu *models*)
- przykładowe konfiguracje w katalogu src/main/resources/configs
- testy (wszystkie klasy z katalogu src/test/java/agh/dg)

#### Michał Proć
- ogólna struktura i kształt projektu
- obsługa interfejsu graficznego (pliki .fxml, .css, pakiet *presenters*)
- klasy z pakietów *exceptions*, *helpers*, *observers*, rekordy z pakietu *records*
- klasy *GameCommunicat*, *DarwinWorldApp*, *ExtendedThread*, *Main*.


Klasy *MoveDirection*, *Simulation* i *Statistics* były pisane wspólnie - każdy z nas miał swój istotny wkład.
