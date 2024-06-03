package collectionn;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 */
public class City implements Comparable<City>, Serializable {
    private static int uniqueId=1;
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private double area; //З начение поля должно быть больше 0
    private Integer population; //Значение поля должно быть больше 0, Поле не может быть null
    private Double metersAboveSeaLevel;
    private Long populationDensity; //Значение поля должно быть больше 0
    private int telephoneCode; //Значение поля должно быть больше 0, Максимальное значение поля: 100000
    private Government government; //Поле может быть null
    private Human governor; //Поле не может быть null

    //Конструктор без id

    /**
     *
     * @param name
     * @param coordinates
     * @param creationDate
     * @param area
     * @param population
     * @param metersAboveSeaLevel
     * @param populationDensity
     * @param telephoneCode
     * @param government
     * @param governor
     */
    public City (String name, Coordinates coordinates, LocalDateTime creationDate, double area,
                 Integer population, Double metersAboveSeaLevel, Long populationDensity,
                 int telephoneCode, Government government, Human governor){
        this.id = uniqueId++;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.populationDensity = populationDensity;
        this.telephoneCode = telephoneCode;
        this.government = government;
        this.governor = governor;
    }
    //Конструктор со всеми параметрами

    /**
     *
     * @param id
     * @param name
     * @param coordinates
     * @param creationDate
     * @param area
     * @param population
     * @param metersAboveSeaLevel
     * @param populationDensity
     * @param telephoneCode
     * @param government
     * @param governor
     */
    public City(int id, String name, Coordinates coordinates, LocalDateTime creationDate, double area,
                Integer population, Double metersAboveSeaLevel, Long populationDensity,
                int telephoneCode, Government government, Human governor){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.populationDensity = populationDensity;
        this.telephoneCode = telephoneCode;
        this.government = government;
        this.governor = governor;
    }


    //Возвращение уникального id

    /**
     *
     * @return
     */
    public  int getUniqueId(){
        return uniqueId;
    }

    //Присваивание уникального id
    public void setUniqueId(){
        City.uniqueId = uniqueId;
    }

    /**
     *
     * @return
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(long x) {
        this.getCoordinates().setX(x);
    }
    public void setCoordinates(int y) {
        this.getCoordinates().setY(y);
    }

    public String getCreationDate() {
        return ""+creationDate;
    }

    /**
     *
     * @return
     */
    public String getFormattedCreationDate(){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter europpeanDateFormatter = DateTimeFormatter.ofPattern(pattern);
        return creationDate.plusHours(3).format(europpeanDateFormatter);

    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public void setMetersAboveSeaLevel(Double metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public Long getPopulationDensity() {
        return populationDensity;
    }

    public void setPopulationDensity(Long populationDensity) {
        this.populationDensity = populationDensity;
    }

    public int getTelephoneCode() {
        return telephoneCode;
    }

    public void setTelephoneCode(int telephoneCode) {
        this.telephoneCode = telephoneCode;
    }

    public Government getGovernment() {
        return government;
    }

    public void setGovernment(Government government) {
        this.government = government;
    }

    public Human getGovernor() {
        return governor;
    }

    public void setGovernor(Human governor) {
        this.governor = governor;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "City!\n" +
                "\nid: " + id +
                "\nname: " + name +
                "\ncoordinates: " + coordinates +
                "\ncreationDate: " + creationDate +
                "\narea: " + area +
                "\npopulation: " + population +
                "\nmetersAboveSeaLevel: " + metersAboveSeaLevel +
                "\npopulationDensity: " + populationDensity +
                "\ntelephoneCode: " + telephoneCode +
                "\ngovernment: " + government +
                "\ngovernor: " + governor ;
    }

    /**
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id; // Сравниваем идентификаторы городов
    }

    /**
     *
     * @param otherCity the object to be compared.
     * @return
     */
    @Override
    public int compareTo(City otherCity) {
        // Сравниваем города по значению поля area
        return Double.compare(this.area, otherCity.area);
    }
}

