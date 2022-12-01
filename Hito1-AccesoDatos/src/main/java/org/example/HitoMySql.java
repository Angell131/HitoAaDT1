package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;

public class HitoMySql {
    static Scanner sc;
    static Connection con;
    static Statement st;
    static ResultSet rs;

    //static PrintWriter pw;



    public static void main(String[] args) {


        String cadenaConexion = "jdbc:mysql://localhost:3306/juegos";
        String user = "root";
        String pass = "curso";


        try {
            con = DriverManager.getConnection(cadenaConexion, user, pass);
        } catch (SQLException e) {
            System.out.println("Error en la conexión con la BD");
            System.out.println(e.getMessage());
            return;
        }

        sc = new Scanner(System.in);
        System.out.println("1 : Añadir juego ");
        System.out.println("2 : Listar juegos ");
        System.out.println("3 : Modificar juego ");
        System.out.println("4 : Eliminar juego ");
        System.out.println("5 : Importar los datos a CSV");
        System.out.println("6 : Generar copia de seguridad de juegos en CSV");
        System.out.println("7 : Cerrar programa ");
        String opcion = sc.next();

        switch (opcion) {

            case "1":
                add();
                break;
            case "2":
                listAll();
                break;
            case "3":
                modify();
                break;
            case "4":
                delete();
                break;
            case "5":
                importar();
                break;
            case "6":
                copia();
                break;
            case "7":
                break;
            default:
                System.out.println("valor erroneo");
        }


    }

    private static void modify() {

        System.out.println("Id del juego a actualizar");
        int idJu = sc.nextInt();
        System.out.println("Nombre nuevo");
        String nameJu = sc.next();

        try {

            PreparedStatement ps = con.prepareStatement("UPDATE juego SET nombre = ? WHERE idJuego = ?");
            ps.setString(1, nameJu);
            ps.setInt(2, idJu);
            ps.execute();
            System.out.println("juego actualizado correctamente");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        listAll();

    }

    private static void listAll() {
        System.out.println("Listando todos los registros...");
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM juego");
            while (rs.next()) {
                System.out.print(rs.getString("idJuego"));
                System.out.print(" - ");
                System.out.print(rs.getString("nombre"));
                System.out.println(); // Retorno de carro
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void add() {

        System.out.println("Id del juego");
        int idJu = sc.nextInt();
        System.out.println("Nombre del juego");
        String nameJu = sc.next();

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO juego VALUES (?,?)");
            ps.setInt(1, idJu);
            ps.setString(2, nameJu);
            ps.execute();
            listAll();

            System.out.println("Juego añadido correctamente");
        } catch (SQLException e) {
            System.out.println("Juego no añadido");
        }


    }

    private static void delete() {
        System.out.println("Id del juego que quieres eliminar");
        int idJu = sc.nextInt();
        try {
            PreparedStatement ps = con.prepareStatement("delete FROM juego WHERE idJuego = ?");
            ps.setInt(1, idJu);
            ps.execute();
            //boolean existe = rs.next();
            //if (existe) {
            //    rs.deleteRow(idJu);
            //}

            listAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void importar() {

        try{
            //CREAMOS EL FICHERO DONDE GUARDAREMOS LOS DATOS.
            PrintWriter pw= new PrintWriter(new File("datos_MySql.csv"));
            //UTILIZAMOS UN OBJETO StringBuilder PARA ALMACENAR CADENAS DE TEXTO.
            StringBuilder sb=new StringBuilder();


            //SENTENCIA SELECT DONDE ALMACENARE LOS DATOS EN MEMORIA.
            ResultSet rsP=null;
            String queryP="select * from juego";
            PreparedStatement psP=con.prepareStatement(queryP);
            rsP=psP.executeQuery();

            //MIENTRAS EL RESULTSET TENGA UN SIGUIENTE OBJETO CREARA UN "HIJO" CON STRINGBUILDER.
            while(rsP.next()){
                sb.append(rsP.getInt(1));
                sb.append(",");
                sb.append(rsP.getString(2));
                //HACEMOS UN SALTO DE LINEA POR CADA OBJETO QUE PINTEMOS.
                sb.append("\r\n");
            }

            System.out.println("Los juegos han sido almacenados");


            //GUARDAMOS LOS DATOS DEL STRINGBUILDER COMO UN STRING.
            pw.write(sb.toString());
            //CERRAMOS EL PRINTWRITER.
            pw.close();
        } catch (Exception e) {
            System.out.println("Error al generar copia de seguridad");
            System.out.println(e.getMessage());
        }


    }
    public static void copia(){
        try {
            //COPIA DE SEGURIDAD.
            //CREAMOS EL FICHERO DONDE GUARDAREMOS LOS DATOS.
            PrintWriter pw= new PrintWriter(new File("copiaSeguridadMySqlJuegos.csv"));
            //UTILIZAMOS UN OBJETO StringBuilder PARA ALMACENAR CADENAS DE TEXTO.
            StringBuilder sb=new StringBuilder();

            //SENTENCIA SELECT DONDE ALMACENARE LOS DATOS EN MEMORIA.
            ResultSet rs2=null;
            String query="select * from juego";
            PreparedStatement ps=con.prepareStatement(query);
            rs2=ps.executeQuery();

            //MIENTRAS EL RESULTSET TENGA UN SIGUIENTE OBJETO CREARA UN "HIJO" CON STRINGBUILDER.
            while(rs2.next()){
                sb.append(rs2.getInt(1));
                sb.append(",");
                sb.append(rs2.getString(2));
                //HACEMOS UN SALTO DE LINEA POR CADA OBJETO QUE PINTEMOS.
                sb.append("\r\n");
            }
            System.out.println("Los juegos han sido almacenados correctamente.");


            //GUARDAMOS LOS DATOS DEL STRINGBUILDER COMO UN STRING.
            pw.write(sb.toString());
            //CERRAMOS EL PRINTWRITER.
            pw.close();
            System.out.println("La copia de seguridad de juegos se ha generado con exito");

        } catch (Exception e) {
            System.out.println("Error al generar copia de seguridad");
            System.out.println(e.getMessage());
        }
    }

}
