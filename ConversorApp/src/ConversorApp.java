import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorApp {
    // Clave de la API (Reemplazar "TU_API_KEY" con tu clave de ExchangeRate-API)
    private static final String API_KEY = "31156d1a70ce19a22a8708f2";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("*************************************");
            //sé que no es lo mejor, pero bueno, tenía prisa
            System.out.println();
            System.out.println("Sea bienvenido/a al Conversor de Moneda =]");
            System.out.println();
            System.out.println("1) Dólar =>> Peso argentino");
            System.out.println("2) Peso argentino =>> Dólar");
            System.out.println("3) Dólar =>> Real brasileño");
            System.out.println("4) Real brasileño =>> Dólar");
            System.out.println("5) Dólar =>> Peso colombiano");
            System.out.println("6) Peso colombiano =>> Dólar");
            System.out.println("7) Salir");
            //aqui tambien jajaja
            System.out.println();
            System.out.println("Elija una opción válida:");
            System.out.println();
            System.out.println("**************************************");

            int option = scanner.nextInt();

            if (option == 7) {
                System.out.println("Gracias por usar el Conversor de Moneda. ¡Adiós!");
                break;
            }

            String fromCurrency = "";
            String toCurrency = "";

            switch (option) {
                case 1 -> { fromCurrency = "USD"; toCurrency = "ARS"; }
                case 2 -> { fromCurrency = "ARS"; toCurrency = "USD"; }
                case 3 -> { fromCurrency = "USD"; toCurrency = "BRL"; }
                case 4 -> { fromCurrency = "BRL"; toCurrency = "USD"; }
                case 5 -> { fromCurrency = "USD"; toCurrency = "COP"; }
                case 6 -> { fromCurrency = "COP"; toCurrency = "USD"; }
                default -> {
                    System.out.println("Opción inválida. Intente nuevamente.");
                    continue;
                }
            }

            System.out.println("Ingrese el valor que deseas convertir:");
            double amount = scanner.nextDouble();

            try {
                double exchangeRate = getExchangeRate(fromCurrency, toCurrency);
                if (exchangeRate != -1) {
                    double convertedValue = amount * exchangeRate;
                    System.out.printf("El valor %.2f [%s] corresponde al valor final de =>> %.2f [%s]\n",
                            amount, fromCurrency, convertedValue, toCurrency);
                } else {
                    System.out.println("No se pudo realizar la conversión debido a un error.");
                }
            } catch (Exception e) {
                System.out.println("Error al realizar la conversión: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static double getExchangeRate(String fromCurrency, String toCurrency) throws Exception {
        String urlStr = API_URL + API_KEY + "/latest/" + fromCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            String jsonResponse = response.toString();
            int index = jsonResponse.indexOf("\"" + toCurrency + "\":");
            if (index != -1) {
                String rate = jsonResponse.substring(index + toCurrency.length() + 3, jsonResponse.indexOf(",", index));
                return Double.parseDouble(rate);
            } else {
                System.out.println("Moneda no encontrada en la respuesta de la API.");
            }
        } else {
            System.out.println("Error al conectar con la API. Código de respuesta: " + responseCode);
        }

        return -1;
    }
}

