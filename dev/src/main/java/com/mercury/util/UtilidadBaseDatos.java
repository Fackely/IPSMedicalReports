/*
 * Created on 26/07/2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.mercury.util;

import java.sql.Connection;
//import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
//import java.text.DateFormat;
//import java.text.ParseException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * 
 * @author Alejo
 *
 */public class UtilidadBaseDatos
{
	private static Logger logger = Logger.getLogger(UtilidadBaseDatos.class);
	
    public static void establecerParametro(int numeroParametro, int tipoSQL, String valor, PreparedStatementDecorator sentencia) throws SQLException    
    {
        try
        {
            switch(tipoSQL)
            {
                case Types.VARCHAR: case Types.CHAR:            
                    if(UtilidadCadena.noEsVacio(valor))
                        sentencia.setString(numeroParametro, valor);
                    else
                        sentencia.setNull(numeroParametro, Types.VARCHAR);
                    break;
                case Types.DATE:            
                    if(UtilidadCadena.noEsVacio(valor))
                    {
                    	valor = UtilidadFecha.conversionFormatoFechaABD(valor);
                        sentencia.setString(numeroParametro, valor);
                    }
                    else
                        sentencia.setNull(numeroParametro, Types.VARCHAR);
                    break;
                case Types.BOOLEAN:
                    if(UtilidadCadena.noEsVacio(valor))
                        sentencia.setBoolean(numeroParametro, UtilidadTexto.getBoolean(valor));
                    else
                        sentencia.setBoolean(numeroParametro, false);
                    break;
                case Types.INTEGER:
                    if(UtilidadCadena.noEsVacio(valor))
                        sentencia.setInt(numeroParametro, Integer.parseInt(valor));
                    else
                        sentencia.setNull(numeroParametro, Types.INTEGER);
                    break;
            }
        }
        catch(NumberFormatException nfe)
        {
            logger.warn("Error convirtiendo tipo de datos");
        }
        /*catch(ParseException pe)
        {
           logger.warn("Error convirtiendo formato fecha: "+pe.getMessage());
        }*/
    }

    /**
     * Método para obtener el codigo siguiente de la sencuencia especificada 
     * @param con : conexion
     * @param secuencia : string que tiene la secuencia
     * @return codigo del registro a insertar
     */
    public static int obtenerCodigoSiguiente(Connection con, String secuencia) throws SQLException
    {
        String consultaSecuencia="SELECT "+secuencia;
        try
        {
            
            PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
            if(resultado.next())
            {
                return resultado.getInt("codigo");
            }
            return 0;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la consulta del siguiente codigo de la secuencia :"+secuencia);
            throw e;
        }
    }
}