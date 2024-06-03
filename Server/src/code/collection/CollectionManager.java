package code.collection;

import collectionn.*;
import fileWork.FileManager;
import fileWork.XmlParser;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CollectionManager {
    private HashSet<City> hashSet;
    private LocalDateTime collectionInitializator;

    public CollectionManager() {
        this.hashSet = new HashSet<>();
        this.collectionInitializator = LocalDateTime.now(); // Устанавливаем текущее местное время
    }

    public String info(PrintStream printStream) {
        StringBuilder sb = new StringBuilder();
        sb.append("Коллекция ").append(hashSet.getClass().getSimpleName()).append("\n");
        sb.append("Тип элементов коллекции: ").append(City.class.getSimpleName()).append("\n");

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(pattern);
        sb.append("Время инициализации коллекции: ").append(collectionInitializator.plusHours(3).format(europeanDateFormatter)).append("\n");
        sb.append("Количество элементов в коллекции: ").append(hashSet.size()).append("\n");
        printStream.println(sb.toString());
        return sb.toString();
    }

    public String show() {
        StringBuilder sb = new StringBuilder();
        if (hashSet.isEmpty()) {
            sb.append("Коллекция пуста.");
        } else {
            for (City city : hashSet) {
                sb.append(city.toString()).append("\n"); // перебор элементов коллекции
            }
        }
        return sb.toString();
    }


    public void clear() {
        hashSet.clear();
    }

    public void save(String filePath) {
        XmlParser xmlParser = new XmlParser();
        FileManager fileManager = new FileManager();
        City[] city = new City[hashSet.size()];
        city = hashSet.toArray(city);
        String str = xmlParser.parseToXml(city);
        fileManager.writeCollection(str, filePath);
    }

    public void update(Integer id, String field, String value, PrintStream printStream) {
        City cityToUpdate = null;
        for (City city : hashSet) {
            if (city.getId() == id) {
                cityToUpdate = city;
                break;
            }
        }

        if (cityToUpdate == null) {
            printStream.println("Город с указанным ID не найден.");
            return;
        }

        if (CityFieldValidation.validate(field, value)) {
            try {
                switch (field) {
                    case "name": {
                        if (value.isEmpty()) throw new NullPointerException();
                        cityToUpdate.setName(value);
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "coordinates_x": {
                        if (value.isEmpty()) value = null;
                        cityToUpdate.getCoordinates().setX(Long.valueOf(value));
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "coordinates_y": {
                        if (value.isEmpty()) value = null;
                        cityToUpdate.getCoordinates().setY(Integer.parseInt(value));
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "area": {
                        double area = Double.parseDouble(value);
                        if (area <= 0) throw new IllegalArgumentException("Значение поля должно быть больше 0");
                        cityToUpdate.setArea(area);
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "population": {
                        int population = Integer.parseInt(value);
                        if (population <= 0)
                            throw new IllegalArgumentException("Значение поля должно быть больше 0");
                        cityToUpdate.setPopulation(population);
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "metersAboveSeaLevel": {
                        cityToUpdate.setMetersAboveSeaLevel(Double.parseDouble(value));
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "populationDensity": {
                        long populationDensity = Long.parseLong(value);
                        if (populationDensity <= 0)
                            throw new IllegalArgumentException("Значение поля должно быть больше 0");
                        cityToUpdate.setPopulationDensity(populationDensity);
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "telephoneCode": {
                        int telephoneCode = Integer.parseInt(value);
                        if (telephoneCode <= 0 || telephoneCode > 100000)
                            throw new IllegalArgumentException("Значение поля должно быть больше 0 и не больше 100000");
                        cityToUpdate.setTelephoneCode(telephoneCode);
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "government": {
                        cityToUpdate.setGovernment(Government.valueOf(value.toUpperCase(Locale.ROOT)));
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    case "governor_age": {
                        cityToUpdate.getGovernor().setAge(Integer.parseInt(value));
                        printStream.println("Значение поля было изменено");
                        break;
                    }
                    default: {
                        printStream.println("Поле не распознано");
                        break;
                    }
                }
            } catch (NumberFormatException ex) {
                printStream.println("Неверный формат числа");
            } catch (IllegalArgumentException ex) {
                printStream.println("Ошибка: " + ex.getMessage());
            }
        } else {
            printStream.println("Неверно указано название поля или значение не принадлежит допустимому.");
        }
    }

    public void removeById(int id, PrintStream printStream) {
        boolean removed = false;
        Iterator<City> iterator = hashSet.iterator();
        while (iterator.hasNext()) {
            City city = iterator.next();
            if (city.getId() == id) {
                iterator.remove();
                removed = true;
                printStream.println("Элемент с ID " + id + " успешно удален из коллекции.");
                break;
            }
        }
        if (!removed) {
            printStream.println("Элемент с ID " + id + " не найден в коллекции.");
        }
    }

    public void averageOfPopulationDensity(PrintStream printStream) {
        if (hashSet.isEmpty()) {
            printStream.println("Коллекция пуста.");
            return;
        }

        long totalPopulationDensity = 0;
        int numberOfCities = 0;
        for (City city : hashSet) {
            if (city.getPopulationDensity() != null) {
                totalPopulationDensity += city.getPopulationDensity();
                numberOfCities++;
            }
        }

        if (numberOfCities > 0) {
            double averagePopulationDensity = (double) totalPopulationDensity / numberOfCities;
            printStream.println("Среднее значение плотности населения: " + averagePopulationDensity);
        } else {
            printStream.println("Для всех городов в коллекции не указана плотность населения.");
        }
    }
    public String groupCountingByArea() {
        if (hashSet.isEmpty()) {
            return "Коллекция пуста.";
        }
        int countSmall = 0;
        int countMedium = 0;
        int countLarge = 0;

        for (City city : hashSet) {
            double area = city.getArea();
            if (area < 1000) {
                countSmall++;
            } else if (area >= 1000 && area < 10000) {
                countMedium++;
            } else {
                countLarge++;
            }
        }
        return "Группировка элементов по площади:\nМаленькие (< 1000) - " + countSmall + "\nСредние (1000-10000) - "
                + countMedium + "\nБольшие (>= 10000) - " + countLarge;
    }

    public void printDescending(PrintStream printStream) {
        if (hashSet.isEmpty()) {
            printStream.println("Коллекция пуста.");
            return;
        }
        ArrayList<City> tempCollection = new ArrayList<>(hashSet);
        tempCollection.sort(Comparator.comparingInt(City::getId).reversed());
        printStream.println("Элементы коллекции в порядке убывания:");
        for (City city : tempCollection) {
            printStream.println(city);
        }
    }

    public void add(City city, PrintStream printStream) {
        hashSet.add(city);
        printStream.println("Элемент добавлен в коллекцию");
    }

    public void addIfMax(City city, PrintStream printStream) {
        if (hashSet.isEmpty()) {
            hashSet.add(city);
            printStream.println("\nЭлемент успешно добавлен в коллекцию, так как коллекция пуста.");
        } else {
            boolean isMax = true;
            for (City existingCity : hashSet) {
                if (city.getArea() <= existingCity.getArea()) {
                    isMax = false;
                    break;
                }
            }
            if (isMax) {
                hashSet.add(city);
                printStream.println("\nЭлемент успешно добавлен в коллекцию, так как значение поля area наибольшее среди других элементов коллекции.");
            } else {
                printStream.println("\nЭлемент не добавлен в коллекцию, так как значение поля area меньше, чем у одного из элементов коллекции.");
            }
        }
    }

    public void addIfMin(City city, PrintStream printStream) {
        if (hashSet.isEmpty()) {
            hashSet.add(city);
            printStream.println("\nЭлемент успешно добавлен в коллекцию, так как коллекция пуста.");
        } else {
            boolean isMin = true;
            for (City existingCity : hashSet) {
                if (city.getArea() >= existingCity.getArea()) {
                    isMin = false;
                    break;
                }
            }
            if (isMin) {
                hashSet.add(city);
                printStream.println("\nЭлемент успешно добавлен в коллекцию, так как значение поля area наименьшее среди других элементов коллекции.");
            } else {
                printStream.println("\nЭлемент не добавлен в коллекцию, так как значение поля area больше, чем у одного из элементов коллекции.");
            }
        }
    }

    public static String getFieldNames() {
        return "Список всех полей: \nname (String)\ncoordinate_x (long)\ncoordinate_y (int)\n" +
                "area (double)\npopulation (Integer)\nmetersAboveSeaLevel (Double)\npopulationDensity (Long)\ntelephoneCode (int)\ngovernment" +
                Arrays.toString(Government.values()) + "\nhumanAge (int)\n";
    }

    public void removeLower(City cityToRemove, PrintStream printStream) {
        double area = cityToRemove.getArea();
        Iterator<City> iterator = hashSet.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            City city = iterator.next();
            if (city.getArea() < area) {
                iterator.remove();
                count++;
            }
        }
        printStream.println("Удалено " + count + " городов с площадью меньше " + area);
    }

    public boolean containsKey(Integer id) {
        for (City city : hashSet) {
            if (city.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
