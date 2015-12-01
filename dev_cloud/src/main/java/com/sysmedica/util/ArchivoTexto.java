package com.sysmedica.util;

import java.io.*;
import java.util.Vector;

public class ArchivoTexto {

	public int generarArchivoPlano(String tipoArchivo,int codigo,int semana,Vector elementos) 
    {
        int resultado = 0;
        String nombreArchivo = tipoArchivo+Integer.toString(codigo)+Integer.toString(semana)+".txt";
        
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object

        try
        {
                // Create a new file output stream
                // connected to "myfile.txt"
                out = new FileOutputStream(nombreArchivo);

                // Connect print stream to the output stream
                p = new PrintStream( out );
                
                String contenido = "";
                
                for (int i=0;i<elementos.size();i++) {
                    String elemento = elementos.get(i).toString();
                    contenido += elemento+",";
                }

                p.println (contenido);
                
                resultado = 1;

                p.close();
        }
        catch (Exception e)
        {
            resultado = 0;
            System.err.println ("Error writing to file");
        }
        
        return resultado;
    }
}
