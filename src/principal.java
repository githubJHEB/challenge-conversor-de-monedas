import com.google.gson.Gson;
import modelos.monedas;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Scanner;

public class principal {
    public static void main(String[] args) {
        boolean ciclo = true;

        while (ciclo) {
                Gson gson = new Gson();

                monedas lecturaDatosJson = gson.fromJson(solicitudApi(), monedas.class);
                Scanner lectura = new Scanner(System.in);
                System.out.println("*******************************************************");

                System.out.println("1. USD ==>> Peso argentino ARS");
                System.out.println("2. ARS Peso argentino ==>> USD");
                System.out.println("3. USD ==>> Real brasileño BRL");
                System.out.println("4. BRL Real brasileño ==>> USD");
                System.out.println("5. USD ==>> Peso Colombiano COP");
                System.out.println("6. COP Peso Colombiano ==>> USD");
                System.out.println("7. Salir");
                System.out.println("Elija el número de la opción de conversión que desea (1..7): ");
                System.out.println("*******************************************************");

                int opcionDeCambio = 0;
                if (lectura.hasNextInt()) {
                    opcionDeCambio = lectura.nextInt();
                } else {
                    System.out.println("Solo es permitido ingresar números entre 1 y 7");
                }


                if (opcionDeCambio < 8 && opcionDeCambio >= 1) {
                    if (opcionDeCambio == 7) {
                        ciclo = false;
                        System.out.println("A elegido la opción de salir ");
                    } else {
                        DecimalFormat doubleFormat = new DecimalFormat("#.##");
                        System.out.println("Ingresa el valor que desea convertir: ");


                        double valor = 0;
                        if (lectura.hasNextInt()) {
                            valor = lectura.nextDouble();
                        } else {
                            System.out.println("Solo es permitido ingreso de número sin decimales.");
                        }

                        if (lecturaDatosJson!=null) {
                            switch (opcionDeCambio) {
                                case 1, 2:
                                    System.out.print(Double.valueOf(doubleFormat.format(convertirValores(opcionDeCambio, valor, (Double) lecturaDatosJson.getBase_code().get("ARS")))));
                                    if (opcionDeCambio == 1) {
                                        System.out.print(" ARS, por " + valor + " USD");
                                    } else {
                                        System.out.print(" USD, por " + valor + " ARS");
                                    }
                                    System.out.println();
                                    break;
                                case 3, 4:
                                    System.out.print(Double.valueOf(doubleFormat.format(convertirValores(opcionDeCambio, valor, (Double) lecturaDatosJson.getBase_code().get("BRL")))));
                                    if (opcionDeCambio == 3) {
                                        System.out.print(" BRL, por " + valor + " USD");
                                    } else {
                                        System.out.print(" USD, por " + valor + " BRL");
                                    }
                                    System.out.println();
                                    break;
                                case 5, 6:
                                    System.out.print(Double.valueOf(doubleFormat.format(convertirValores(opcionDeCambio, valor, (Double) lecturaDatosJson.getBase_code().get("COP")))));
                                    if (opcionDeCambio == 5) {
                                        System.out.print(" COP, por " + valor + " USD");
                                    } else {
                                        System.out.print(" USD, por " + valor + " COP");
                                    }
                                    System.out.println();
                                    break;
                            }
                        }
                    }
                } else {
                    System.out.println("Por favor ingrese un número valido entre 1 y 7 ");
                }

        }
    }

    static double convertirValores(int opcion, double valorConvertir, double factorConversion) {
        double valorConvertido = 0;
        switch (opcion) { //USD to ARS
            case 1, 3, 5:
                valorConvertido = valorConvertir * factorConversion;
                break;
            case 2, 4, 6:
                valorConvertido = valorConvertir / factorConversion;
                break;
        }

        return valorConvertido;
    }

    static String solicitudApi() {
        String json = "";
        String clave = "";
        String direccion = "https://v6.exchangerate-api.com/v6/" + clave + "/latest/USD";


        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            json = response.body();

        } catch (IOException e) {
            System.out.println("Ocurrió algún tipo de error de conexión a Internet: ");
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Ocurrió algún tipo de error: ");
            System.out.println(e.getMessage());
        }

        return json;
    }

    ;
}
