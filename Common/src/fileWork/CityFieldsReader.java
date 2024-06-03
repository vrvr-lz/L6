package fileWork;

import collectionn.*;
import exceptions.ValidValuesRangeException;
import io.UserIO;

import java.time.Instant;
import java.time.LocalDateTime;

// Для заполнения полей нового объекта класса City
public class CityFieldsReader {
    private final UserIO userIO;

    public CityFieldsReader(UserIO userIO) {
        this.userIO = userIO;
    }

    public UserIO getUserIO() {
        return userIO;
    }

    // Метод, производящий чтение данных из консоли
    // Запрашивает ввод полей в строго определенном порядке
    public City read(Integer id) {
        LocalDateTime now = LocalDateTime.now(); // Получаем текущее время и дату
        return new City(id, readName(), readCoordinates(), now,
                readArea(), readPopulation(), readMetersAboveSeaLevel(), readPopulationDensity(),
                readTelephoneCode(), readGovernment(), readGovernor());
    }

    public City read() {
        userIO.printCommandText("Введите значения полей для элемента коллекции\n");
        LocalDateTime now = LocalDateTime.now();
        String i = Instant.now().toString();
        return new City(readName(), readCoordinates(), now,
                readArea(), readPopulation(), readMetersAboveSeaLevel(), readPopulationDensity(),
                readTelephoneCode(), readGovernment(), readGovernor());
    }

    public String readName() {
        String str;
        while (true) {
            userIO.printCommandText("name (not null): ");
            str = userIO.readLine().trim();
            if (str.equals("") || str == null)
                userIO.printCommandError("\nЗначение поля не может быть null или пустой строкой\n");
            else return str;
        }
    }

    public Coordinates readCoordinates() {
        return new Coordinates(readCoordinateX(), readCoordinateY());
    }

    public Long readCoordinateX() {
        long x;
        while (true) {
            try {
                userIO.printCommandText("coordinate_x (long): ");
                String str = userIO.readLine().trim();
                if (str.equals("") || str == null) x = 0;
                else {
                    x = Long.parseLong(str);
                }
                return x;
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа long");
            }
        }
    }

    public Integer readCoordinateY() {
        int y;
        while (true) {
            try {
                userIO.printCommandText("coordinate_y (int & y > -232): ");
                String str = userIO.readLine().trim();
                if (str.equals("") || str == null) y = Integer.valueOf(0);
                else {
                    y = Integer.parseInt(str);
                    if (y <= -232) {
                        System.out.println("Координата y должна быть больше -232");
                    } else {
                        return y;
                    }
                }
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа int");
            }
        }
    }

    public double readArea() {
        double area;
        while (true) {
            try {
                userIO.printCommandText("area (double x > 0): ");
                String str = userIO.readLine().trim();
                if (str.equals("") || str == null) area = 0;
                else {
                    area = Double.parseDouble(str);
                    if (area <= 0) throw new ValidValuesRangeException();
                    else return area;
                }
            } catch (ValidValuesRangeException ex) {
                System.out.println("area должен быть больше 0");
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа double");
            }
        }

    }

    public Integer readPopulation() {
        Integer population;
        while (true) {
            try {
                userIO.printCommandText("population (not null && Integer x > 0): ");
                String str = userIO.readLine().trim();
                population = Integer.parseInt(str);
                if (population < 0) throw new ValidValuesRangeException();
                else return population;
            } catch (ValidValuesRangeException ex) {
                System.out.println("population должен быть больше 0");
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа Integer и не null");
            }
        }
    }

    public Double readMetersAboveSeaLevel() {
        Double metersAboveSeaLevel;
        while (true) {
            try {
                userIO.printCommandText("metersAboveSeaLevel: ");
                String str = userIO.readLine().trim();
                metersAboveSeaLevel = Double.parseDouble(str);
                return metersAboveSeaLevel;
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа Double");
            }
        }
    }

    public Long readPopulationDensity() {
        Long populationDensity;
        while (true) {
            try {
                userIO.printCommandText("populationDensity (Long x > 0): ");
                String str = userIO.readLine().trim();
                if (str.equals("") || str == null) populationDensity = Long.valueOf(0);
                else {
                    populationDensity = Long.parseLong(str);
                    if (populationDensity <= 0) throw new ValidValuesRangeException();
                    else return populationDensity;
                }
            } catch (ValidValuesRangeException ex) {
                System.out.println("populationDensity должен быть больше 0");
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа Long");
            }
        }

    }

    public Integer readTelephoneCode() {
        int telephoneCode;
        while (true) {
            try {
                userIO.printCommandText("telephoneCode (x > 0 && x <= 100000): ");
                String str = userIO.readLine().trim();
                telephoneCode = Integer.parseInt(str);
                if (telephoneCode <= 0 && telephoneCode > 100000) throw new ValidValuesRangeException();
                else return telephoneCode;
            } catch (ValidValuesRangeException ex) {
                System.out.println("telephoneCode должен быть больше 0 и не больше 100.000");
            } catch (NumberFormatException ex) {
                System.err.println("Число должно быть типа int");
            }
        }
    }

    public Government readGovernment() {
        Government government;
        while (true) {
            try {
                userIO.printCommandText("Допустимые значения government:\n");
                for (Government val : Government.values()) {
                    userIO.printCommandText(val.name() + "\n");
                }
                userIO.printCommandText("government: ");
                government = Government.valueOf(userIO.readLine().toUpperCase().trim());
                return government;
            } catch (IllegalArgumentException ex) {
                System.err.println("Значение введенной константы не представлено в перечислении government");
            }
        }
    }

    public Human readGovernor() {
        return new Human(readHumanAge());
    }

    public int readHumanAge() {
        int age;
        while (true) {
            try {
                userIO.printCommandText("age (int && x > 0): ");
                String str = userIO.readLine().trim();
                if (str.equals("") || str == null) age = 0;
                else {
                    age = Integer.parseInt(str);
                    if (age <= 0) throw new ValidValuesRangeException();
                }
                return age;
            } catch (ValidValuesRangeException ex) {
                System.out.println("age должен быть больше 0");
            } catch (NumberFormatException ex) {
                System.err.println("Вводимое значение должно быть int");
            }
        }
    }
}
