package compilador.lexer;

import compilador.lexer.accionesSemanticas.*;
import compilador.lexer.accionesSemanticas.correctas.*;
import compilador.lexer.accionesSemanticas.errores.*;

import java.io.*;

public class CSVAMatriz {
    public static void main(String[] args) {
//        AccionSemantica[][] matrix = leerMatrizDeAccionesSemanticas("src/compilador/lexer/accionesSemanticas.csv", 28, 18);

//        for (int[] row : matrix) {
//            for (int cell : row) {
//                System.out.print(cell + " ");
//            }
//            System.out.println();
//        }
    }

    public static int[][] leerMatrizDeTransicion(String path, int filas, int columnas) {
        String csvFile = path;
        String line;
        String cvsSplitBy = ",";
        int[][] matrix = new int[filas][columnas];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int row = 0;
            while ((line = br.readLine()) != null && row < filas) {
                String[] values = line.split(cvsSplitBy);
                for (int col = 0; col < 16 && col < values.length; col++) {
                    try {
                        matrix[row][col] = Integer.parseInt(values[col].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Non-integer value found at row " + (row + 1) + ", column " + (col + 1) + ". Setting to 0.");
                        matrix[row][col] = 0;
                    }
                }
                row++;
            }

            if (row < filas) {
                System.out.println("Warning: CSV file has fewer than " + filas + " rows. Remaining rows will be filled with zeros.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Warning: CSV file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    public static AccionSemantica[][] leerMatrizDeAccionesSemanticas(String path, int filas, int columnas) {
        String csvFile = path;
        String line;
        String cvsSplitBy = ",";
        AccionSemantica[][] matrix = new AccionSemantica[filas][columnas];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int row = 0;
            while ((line = br.readLine()) != null && row < filas) {
                String[] values = line.split(cvsSplitBy);
                for (int col = 0; col < 16 && col < values.length; col++) {
                    matrix[row][col] = crearAccionSemantica(values[col].trim());
                }
                row++;
            }

            if (row < filas) {
                System.out.println("Warning: CSV file has fewer than " + filas + " rows. Remaining rows will be filled with zeros.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Warning: CSV file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    private static AccionSemantica crearAccionSemantica(String accionSemantica) {
        switch (accionSemantica) {
            case "AS1": return new AS1();
            case "AS2": return new AS2();
            case "AS3": return new AS3();
            case "AS4": return new AS4();
            case "AS5": return new AS5();
            case "AS6": return new AS6();
            case "AS7": return new AS7();
            case "AS8": return new AS8();
            case "AS9": return new AS9();
            case "AS10": return new AS10();
            case "AS11": return new AS11();
            case "AS12": return new AS12();
            case "ASEGenerica": return new ASEGenerica();
            case "ASDesigualdad": return new ASEDesigualdad();
            case "ASECaracterNoPermitido": return new ASECaracterNoPermitido();
            case "ASEAsignacion": return new ASEAsignacion();
            case "ASEDigitoEsperado": return new ASEDigitoEsperado();
            case "Null": return null;
            default: throw new IllegalArgumentException("Error: Accion semantica no reconocida: " + accionSemantica + ". Fijarse de agregarla a el archivo 'CSVMatriz'");
        }
    }
}
