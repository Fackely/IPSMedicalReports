/*
 * @(#)SqlBaseExcepcionesFarmacia.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares a Excepciones de Farmacia
 *
 * @version 1.0 Nov 29, 2004
 */
public class SqlBaseExcepcionesFarmacia 
{

    /**
	 * Cadena constante con el <i>statement</i> necesario para
     * actualizar una base de excepci�n de farmacia
     */
    private final static String modificarBaseExcepcionFarmaciaStr="UPDATE excepciones_farmacia set convenio=?, centro_costo=?, articulo=?, no_cubre=? where codigo=?";
    
    /**
	 * Cadena constante con el <i>statement</i> necesario para
     * eliminar una base de excepci�n de farmacia
     */
    private final static String eliminarBaseExcepcionFarmaciaStr="DELETE from excepciones_farmacia where codigo=?";
    
    /**
	 * Cadena constante con el <i>statement</i> necesario para
     * consultar todas las bases de excepci�n de farmacia para
     * un convenio dado (primera parte)
     */
    //private final static String consultarBaseExcepcionFarmaciaParte1Str="SELECT exf.codigo, exf.convenio as codigoConvenio, conv.nombre as convenio, exf.centro_costo as codigoCentroCosto, ccosto.nombre as centroCosto, exf.articulo as codigoArticulo, art.descripcion || ' ' || art.concentracion as articulo, exf.no_cubre as noCubre, " + ValoresPorDefecto.getValorTrueParaConsultas() + " as vieneBD  from excepciones_farmacia exf INNER JOIN convenios conv ON (exf.convenio=conv.codigo) INNER JOIN centros_costo ccosto ON (exf.centro_costo=ccosto.codigo) INNER JOIN articulo art ON (exf.articulo=art.codigo) ";
    private final static String consultarBaseExcepcionFarmaciaParte1Str="SELECT exf.codigo, exf.convenio as codigoConvenio, conv.nombre as convenio, exf.centro_costo as codigoCentroCosto, ccosto.nombre as centroCosto, exf.articulo as codigoArticulo, getdescarticulo(exf.articulo) as articulo, exf.no_cubre as noCubre, " + ValoresPorDefecto.getValorTrueParaConsultas() + " as vieneBD,art.es_pos || '' AS esPos  from excepciones_farmacia exf INNER JOIN convenios conv ON (exf.convenio=conv.codigo) INNER JOIN centros_costo ccosto ON (exf.centro_costo=ccosto.codigo) INNER JOIN view_articulos art ON (exf.articulo=art.codigo) ";
    
    /**
	 * Cadena constante con el <i>statement</i> necesario para
     * consultar todas las bases de excepci�n de farmacia para
     * un convenio dado (segunda parte)
     */
    private final static String consultarBaseExcepcionFarmaciaParte2Str=" order by ccosto.nombre, art.descripcion";
    
    /**
	 * Cadena constante con el <i>statement</i> necesario para
     * revisar si una base de excepci�n de farmacia existe o
     * no previamente en la fuente de datos
     */
    private final static String existeBaseExcepcionFarmaciaPrevioStr="SELECT count(1) as numResultados from excepciones_farmacia where convenio=? and centro_costo=? and articulo=? and codigo<>?";
    
	/**
	 * Implementaci�n del m�todo que inserta
	 * una excepci�n de farmacia en una BD Gen�rica
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#insertarBaseExcepcionFarmacia (Connection , int , int , int , double ) throws SQLException
	 */
    public static int insertarBaseExcepcionFarmacia (Connection con, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre, String insertarBaseExcepcionFarmaciaStr) throws SQLException
    {
        PreparedStatementDecorator insertarBaseExcepcionFarmaciaStatement= new PreparedStatementDecorator(con.prepareStatement(insertarBaseExcepcionFarmaciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        insertarBaseExcepcionFarmaciaStatement.setInt(1, codigoConvenio);
        insertarBaseExcepcionFarmaciaStatement.setInt(2, codigoCentroCosto);
        insertarBaseExcepcionFarmaciaStatement.setInt(3, articulo);
        insertarBaseExcepcionFarmaciaStatement.setDouble(4, noCubre);
        return insertarBaseExcepcionFarmaciaStatement.executeUpdate();
    }
    
	/**
	 * Implementaci�n del m�todo que modifica
	 * una excepci�n de farmacia en una BD Gen�rica
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#modificarBaseExcepcionFarmacia(Connection , int , int , int , int , double ) throws SQLException
	 */
    public static int modificarBaseExcepcionFarmacia(Connection con, int codigo, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre) throws SQLException
    {
        PreparedStatementDecorator modificarBaseExcepcionFarmaciaStatement= new PreparedStatementDecorator(con.prepareStatement(modificarBaseExcepcionFarmaciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        modificarBaseExcepcionFarmaciaStatement.setInt(1, codigoConvenio);
        modificarBaseExcepcionFarmaciaStatement.setInt(2, codigoCentroCosto);
        modificarBaseExcepcionFarmaciaStatement.setInt(3, articulo);
        modificarBaseExcepcionFarmaciaStatement.setDouble(4, noCubre);
        modificarBaseExcepcionFarmaciaStatement.setInt(5, codigo);
        return modificarBaseExcepcionFarmaciaStatement.executeUpdate();
    }
    
	/**
	 * Implementaci�n del m�todo que elimina
	 * una excepci�n de farmacia en una BD Gen�rica
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#eliminarBaseExcepcionFarmacia (Connection , int ) throws SQLException
	 */
    public static int eliminarBaseExcepcionFarmacia (Connection con, int codigo) throws SQLException
    {
        int llaves[]={codigo};
        return UtilidadBD.updateGenerico(con, llaves, eliminarBaseExcepcionFarmaciaStr);
    }
    
	/**
	 * Implementaci�n del m�todo que busca todas 
	 * las excepciones de farmacia en una BD Gen�rica
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#consultarBaseExcepcionFarmacia (Connection , int, int, int ) throws SQLException
	 */
    public static ResultSetDecorator consultarBaseExcepcionFarmacia (Connection con, int codigoConvenioBusqueda, int codigoCentroCostoBusqueda, int codigoArticuloBusqueda, String restriccionPorNombreArticulo) throws SQLException
    {
        String restriccionConvenio="", restriccionCentroCosto="", restriccionArticulo="";
        boolean primeraRestriccion=true;
        
        if (codigoConvenioBusqueda!=ConstantesBD.codigoNuncaValido)
        {
            if (primeraRestriccion)
            {
                restriccionConvenio=" where exf.convenio=" +codigoConvenioBusqueda;
            }
            else
            {
                restriccionConvenio=" and exf.convenio=" +codigoConvenioBusqueda;
            }
            
            primeraRestriccion=false;
        }
        if (codigoCentroCostoBusqueda!=ConstantesBD.codigoNuncaValido)
        {
            if (primeraRestriccion)
            {
                restriccionCentroCosto=" where exf.centro_costo="+codigoCentroCostoBusqueda;
            }
            else
            {
                restriccionCentroCosto=" and exf.centro_costo="+codigoCentroCostoBusqueda;
            }
            
            primeraRestriccion=false;
        }
            
        if(codigoArticuloBusqueda!=ConstantesBD.codigoNuncaValido)
        {
            if (primeraRestriccion)
            {
                restriccionArticulo=" where exf.articulo="+codigoArticuloBusqueda;
            }
            else
            {
                restriccionArticulo=" and exf.articulo="+codigoArticuloBusqueda;
            }
            
            primeraRestriccion=false;
        }
        PreparedStatementDecorator consultarBaseExcepcionFarmaciaStatement= new PreparedStatementDecorator(con.prepareStatement(consultarBaseExcepcionFarmaciaParte1Str + restriccionConvenio + restriccionArticulo + restriccionCentroCosto + restriccionPorNombreArticulo + consultarBaseExcepcionFarmaciaParte2Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        return new ResultSetDecorator(consultarBaseExcepcionFarmaciaStatement.executeQuery());
    }
    
	/**
	 * Implementaci�n del m�todo que revisa la existencia 
	 * de un registro previo en una BD Gen�rica
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#existeBaseExcepcionFarmaciaPrevio (Connection , int , int , int , int) throws SQLException
	 */
    public static boolean existeBaseExcepcionFarmaciaPrevio (Connection con, int codigoConvenio, int codigoCentroCosto, int codigoArticulo, int codigo) throws SQLException
    {
        PreparedStatementDecorator existeBaseExcepcionFarmaciaPrevioStatement= new PreparedStatementDecorator(con.prepareStatement(existeBaseExcepcionFarmaciaPrevioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        existeBaseExcepcionFarmaciaPrevioStatement.setInt(1, codigoConvenio);
        existeBaseExcepcionFarmaciaPrevioStatement.setInt(2, codigoCentroCosto);
        existeBaseExcepcionFarmaciaPrevioStatement.setInt(3, codigoArticulo);
        existeBaseExcepcionFarmaciaPrevioStatement.setInt(4, codigo);
        ResultSetDecorator rs=new ResultSetDecorator(existeBaseExcepcionFarmaciaPrevioStatement.executeQuery());
        
        if (rs.next())
        {
            if (rs.getInt("numResultados")>0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            throw new SQLException ("Error en un count en existeBaseExcepcionFarmaciaPrevio");
        }
            
    }
    
}
