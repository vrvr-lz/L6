package fileWork;

import collectionn.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 */
public class XmlParser {
    /**
     *
     * @param text
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public City[] parseToCollection(InputSource text) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XmlHandler handler = new XmlHandler();
        try {
            parser.parse(text, handler);
        } catch (SAXException ignored) {
        }
        City[] city = new City[handler.citys.size()];
        return handler.citys.toArray(city);
    }

    /**
     *
     */
    private static class XmlHandler extends DefaultHandler {
        private ArrayList<City> citys = new ArrayList<>();
        private String name;
        private LocalDateTime creationDate = null;
        private Double area = null;
        private Integer population = null;
        private Double metersAboveSeaLevel = null;
        private Long populationDensity = null;
        private Integer telephoneCode = null;
        private Government government;
        private Integer humanAge;
        private Long x = null;
        private Integer y = null;
        private String lastElementName;

        /**
         *
         * @param uri The Namespace URI, or the empty string if the
         *        element has no Namespace URI or if Namespace
         *        processing is not being performed.
         * @param localName The local name (without prefix), or the
         *        empty string if Namespace processing is not being
         *        performed.
         * @param qName The qualified name (with prefix), or the
         *        empty string if qualified names are not available.
         * @param attributes The attributes attached to the element.  If
         *        there are no attributes, it shall be an empty
         *        Attributes object.
         */

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            lastElementName = qName;
        }

        /**
         *
         * @param ch The characters.
         * @param start The start position in the character array.
         * @param length The number of characters to use from the
         *               character array.
         * @throws ClassCastException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws ClassCastException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();
            try {
                if (!information.isEmpty()) {
                    switch (lastElementName) {
                        case "name":
                            name = information;
                            break;
                        case "coordinate_x":
                            x = Long.parseLong(information);
                            break;
                        case "coordinate_y":
                            y = Integer.parseInt(information);
                            break;
                        case "creation_date":
                            creationDate = LocalDateTime.parse(information);
                            break;
                        case "area":
                            area = Double.parseDouble(information);
                            break;
                        case "population":
                            population = Integer.parseInt(information);
                            break;
                        case "metersAboveSeaLevel":
                            metersAboveSeaLevel = Double.parseDouble(information);
                            break;
                        case "difficulty":
                            government = government.valueOf(information);
                            break;
                        case "populationDensity":
                            populationDensity = Long.parseLong(information);
                            break;
                        case "telephoneCode":
                            telephoneCode = Integer.parseInt(information);
                            break;
                        case "humanAge":
                            humanAge = Integer.parseInt(information);
                            break;
                    }


                }
            } catch (IllegalArgumentException ex) {
                System.err.println("Указанной константы перечисляемого типа не существует, либо невозможно преобразование типов");
                System.err.println(name);
            }
        }

        /**
         *
         * @param uri The Namespace URI, or the empty string if the
         *        element has no Namespace URI or if Namespace
         *        processing is not being performed.
         * @param localName The local name (without prefix), or the
         *        empty string if Namespace processing is not being
         *        performed.
         * @param qName The qualified name (with prefix), or the
         *        empty string if qualified names are not available.
         */
        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("City")) {
                if ((name != null && !name.isEmpty()) &&
                        (y != null && y > -232 ) &&
                        (area > 0) &&
                        (population != null && population > 0) &&
                        (populationDensity>0) &&
                        (telephoneCode >0 && telephoneCode <= 100000) &&
                        (humanAge > 0)){

                    Coordinates coordinates = new Coordinates(x,y);

                    if (creationDate == null) {
                        String i = Instant.now().toString();
                        creationDate = LocalDateTime.parse(i);
                    }
                    Human governor = new Human(humanAge);

                    City city = new City(name,coordinates,creationDate,area,population,metersAboveSeaLevel,populationDensity,telephoneCode, government, governor);
                    citys.add(city);
                } else System.err.println("Указаны не все параметры, либо параметры не принадлежат допустимой ОДЗ");

                name = null;
                x = null;
                y = null;
                creationDate = null;
                area = null;
                population = null;
                metersAboveSeaLevel = null;
                populationDensity = null;
                telephoneCode = null;
                government = null;
                humanAge = null;
            }
        }
    }

    /**
     *
     * @param citys
     * @return
     */
    public String parseToXml(City[] citys) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version = \"1.0\"?>\n");
        sb.append("<hashSet>\n");
        for (City city : citys) {
            sb.append("\t<City>\n");
            sb.append("\t\t<name>").append(city.getName()).append("</name>");
            sb.append("\n\t\t<coordinate_x>").append(city.getCoordinates().getX()).append("</coordinate_x>");
            try {
                int y = city.getCoordinates().getY();
                sb.append("\n\t\t<coordinate_y>").append(y).append("</coordinate_y>");
            } catch (NullPointerException ignored) {
            }
            sb.append("\n\t\t<creation_date>").append(city.getCreationDate()).append("</creation_date>");
            sb.append("\n\t\t<area>").append(city.getArea()).append("</area>");
            sb.append("\n\t\t<population>").append(city.getPopulation()).append("</population>");
            sb.append("\n\t\t<metersAboveSeaLevel>").append(city.getMetersAboveSeaLevel()).append("</metersAboveSeaLevel>");
            sb.append("\n\t\t<populationDensity>").append(city.getPopulationDensity()).append("</populationDensity>");
            sb.append("\n\t\t<telephoneCode>").append(city.getTelephoneCode()).append("</telephoneCode>");
            sb.append("\n\t\t<government>").append(city.getGovernment()).append("</government>");
            sb.append("\n\t\t<humanAge>").append(city.getGovernor().getAge()).append("</humanAge>");
            sb.append("\n\t</City>\n");
        }
        sb.append("</hashSet>\n");
        return sb.toString();
    }
}