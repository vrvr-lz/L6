package collectionn;

public class CityFieldValidation {
    /**
     * Метод, осуществляющий валидацию полей класса City.
     * @param field поле объекта City.
     * @param value значение поля объекта City.
     * @return boolean. true - валидация пройдена, false - валидация не пройдена.
     */
    public static boolean validate(String field, String value) {
        try {
            switch (field) {
                case "id": {
                    if (value == null || value.equals("")) throw new NullPointerException();
                    if (Integer.parseInt(value) > 0) return true;
                    break;
                }
                case "name": {
                    if (value.equals("") || value == null) throw new NullPointerException();
                    return true;
                }
                case "coordinates_x":
                case "coordinates_y": {
                    if (value == null || value.equals("")) return true;
                    // Проверяем, что значение числовое
                    Double.parseDouble(value);
                    return true;
                }
                case "area": {
                    if (value == null || value.equals("")) throw new NullPointerException();
                    // Проверяем, что значение числовое и больше 0
                    if (Double.parseDouble(value) > 0) return true;
                    break;
                }
                case "population": {
                    if (value == null || value.equals("")) throw new NullPointerException();
                    if (Integer.parseInt(value) > 0) return true;
                    break;
                }
                case "metersAboveSeaLevel":
                case "populationDensity":
                case "telephoneCode": {
                    if (value == null || value.equals("")) return true;
                    // Проверяем, что значение числовое и больше 0
                    if (Long.parseLong(value) > 0) return true;
                    break;
                }
                case "government": {
                    // Проверяем, что значение существует в enum Government
                    Government.valueOf(value.toUpperCase());
                    return true;
                }
                case "governor_age": {
                    if (value == null || value.equals("")) throw new NullPointerException();
                    // Проверяем, что значение числовое и больше 0
                    if (Integer.parseInt(value) > 0) return true;
                    break;
                }
                default: {
                    return false;
                }
            }
        } catch (NumberFormatException ex) {
            // Обработка NumberFormatException
        } catch (IllegalArgumentException ex) {
            // Обработка IllegalArgumentException
        } catch (NullPointerException ex) {
            // Обработка NullPointerException
        }
        return false;
    }
}
