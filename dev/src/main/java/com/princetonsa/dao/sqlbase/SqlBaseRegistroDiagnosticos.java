/*
 * @(#)SqlBaseRegistroDiagnosticosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 * Created on 17-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;


/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Registro
 * de Diagnosticos
 *
 * @author armando
 * Princeton 17-ago-2004
 * Clase para manejar el SqlBase de RegistroDiagnosticos.
 */
public class SqlBaseRegistroDiagnosticos 
{


    /**
     * Variable para manejar los errores loger de la funcionalidad
     */
    private static Logger logger = Logger.getLogger(SqlBaseRegistroDiagnosticos.class);

    /**
     * variables que almacenan las sentecias SQL que se ejecutan en los metodos.
     */
    static String insertarRegistroDiagnosticoStr="INSERT INTO manejopaciente.diagnosticos (acronimo , tipo_cie , nombre, activo , sexo , edad_inicial , edad_final , es_principal , es_muerte)  VALUES (upper(?),?,?,?, ?,?,?,?,?)";

    /**
	 * Cadena constante con el <i>statement</i> necesario 
	 * para modificar un diagnostico
	 */
    static String modificarRegistroDiagnosticoStr="UPDATE manejopaciente.diagnosticos set nombre=?,activo=?, sexo=?,edad_inicial=?,edad_final=?,es_principal=?,es_muerte=? WHERE acronimo=? and tipo_cie=?";

    /**
	 * Cadena constante con el <i>statement</i> necesario 
	 * para eliminar un diagnostico
	 */
    static String eliminarRegistroDiagnosticoStr="delete from manejopaciente.diagnosticos where upper(acronimo)=upper(?) and tipo_cie=?";

    /**
	 * Cadena constante con el <i>statement</i> necesario 
	 * para consultar un diagnostico
	 */
    static String consultarRegistroDiagnosticoStr="select acronimo as codigo,tipo_cie as tipoCIE,manejopaciente.getNombreTipoCIE(tipo_cie) as nombreTipoCIE,nombre as descripcion,activo as estado, sexo AS sexo, edad_inicial AS edad_inicial, edad_final AS edad_final, es_principal AS es_principal, es_muerte AS es_muerte , CASE  when administracion.getDescripcionSexo(sexo)  IS NULL THEN 'SIN RESTRICCION' ELSE administracion.getDescripcionSexo(sexo) end as nomhSexo FROM manejopaciente.diagnosticos WHERE upper(acronimo)=upper(?) and tipo_cie=? and acronimo <> '1'";
    
        
    
    /**
	 * Cadena constante con el <i>statement</i> necesario 
	 * para realizar una búsqueda avanzada
	 */
    static String consultarRegistroDiagnosticoAvanzadaStr="select acronimo as codigo,tipo_cie as tipoCIE,manejopaciente.getNombreTipoCIE(tipo_cie) as nombreTipoCIE,nombre as descripcion, administracion.getBooleanSiNo(activo) as estado , sexo AS sexo, edad_inicial AS edad_inicial, edad_final AS edad_final, getintegridaddominio(es_principal) AS es_principal, getintegridaddominio(es_muerte) AS es_muerte , CASE  when administracion.getDescripcionSexo(sexo)  IS NULL THEN 'SIN RESTRICCION' ELSE administracion.getDescripcionSexo(sexo) end as nomhSexo from manejopaciente.diagnosticos where  acronimo <> '1' ";
            
    /**
	 * Cadena constante con el <i>statement</i> necesario 
	 * para realizar el combo de Sexo
	 */
    static String consultarSexoStr="SELECT codigo as codigo, nombre AS nombre FROM administracion.sexo ";
    
    /**
	 * String utilizado como sentyencia SQL para cargar el diagnóstico de egreso
	 * de una cuenta de hospitalización ó urgencias
	 */
	private static final String cargarDiagnosticoHospUrgStr = "SELECT manejopaciente.getDiagnosticoEgreso(?) AS diagnostico";
	
	/**
	 * String utilizado como sentyencia SQL para cargar el diagnóstico de egreso
	 * de una cuenta de consulta Externa
	 */
	private static final String cargarDiagnosticoConsultaExternaStr = "SELECT manejopaciente.getDiagnosticoPrinVal(?) AS diagnostico";

	/**
	 * Cadena de consulta de Diagnostico por Cuenta
	 */
	private static final String strCadenaConsultaDiagnosticoXCuenta = "SELECT "
			+ "getultdiagpac(?) AS diagnostico FROM DUAL";
	
    /**
     * Metodo para insertar un registro en la tabla diagnosticos
     * @param con Conexion
     * @param codigo Codigo del diagnostico
     * @param tipoCIE CIE del diagnostico
     * @param activo Para manejar el estado del diagnostico si es activo o inactivo
     * @param sexo Para manejar el sexo del diagnostico si masculino, fenmenino o sin restriccion
     * @param edad_inicial Para manejar la edad inicial del diagnostico 
     * @param edad_final Para manejar la edad final del diagnostico 
     * @param es_principal Para manejar el indicativo del  diagnostico si es principal o no
     * @param es_muerte Para manejar el indicativo del  diagnostico si es de muerte o no
     * @return executeUpdate retorna -1 si la insercion fallo. 
     */
    public static int insertarRegistroDiagnostico(Connection con, String codigo, int tipoCIE,String descripcion, boolean activo, int sexo , int edad_inicial , int edad_final, String es_principal, String es_muerte ) 
    {
        PreparedStatementDecorator ps;
        try 
        {   
            ps =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,codigo);
            ps.setInt(2,tipoCIE);
            ps.setString(3,descripcion);
            ps.setBoolean(4,activo);
            
			if (sexo != 0)
				ps.setInt(5,sexo);
			else
				ps.setNull(5,Types.INTEGER);
            
            
            ps.setInt(6,edad_inicial);
            ps.setInt(7,edad_final);
            ps.setString(8,es_principal);
            ps.setString(9,es_muerte);
            return ps.executeUpdate();
        } catch (SQLException e) 
        {
            logger.warn("No se pudo realizar la insercion "+e.toString());
        }
        return -1;
    }
    /**
     * Metodo para modificar un diagnostico
     * @param con Conexion
     * @param descripcion Nuevo valor del campo descripcion
     * @param activo Nuevo valor del campo estado
     * @param sexo Para manejar el sexo del diagnostico si masculino, fenmenino o sin restriccion
     * @param edad_inicial Para manejar la edad inicial del diagnostico 
     * @param edad_final Para manejar la edad final del diagnostico 
     * @param es_principal Para manejar el indicativo del  diagnostico si es principal o no
     * @param es_muerte Para manejar el indicativo del  diagnostico si es de muerte o no
     * @return executeUpdate retorna -1 si la insercion fallo.
     */
    public static int modificarRegistroDiagnostico(Connection con,String descripcion,boolean activo,String codigo,int tipoCIE , int sexo , int edad_inicial , int edad_final, String es_principal, String es_muerte)
    {
        PreparedStatementDecorator ps;
        try
        {
		    ps =  new PreparedStatementDecorator(con.prepareStatement(modificarRegistroDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    ps.setString(1,descripcion);
		    ps.setBoolean(2,activo);
		    
			if (sexo != 0)
				ps.setInt(3,sexo);
			else
				ps.setNull(3,Types.INTEGER);
		    
		    
		    ps.setInt(4,edad_inicial);
		    ps.setInt(5,edad_final);
		    ps.setString(6,es_principal);
		    ps.setString(7,es_muerte);
		    
		    ps.setString(8,codigo);
		    ps.setInt(9,tipoCIE);
		    
		    return ps.executeUpdate();
        }catch(SQLException e) 
        { 
            logger.warn("No se pudo realizar la modificacion "+e.toString());
        }
		 return -1;
    }
    
    /**
     * Metodo que eliminar Un diagnostico, solo lo elimina si el diagnostico no esta siendo utilizado en la BD
     * @param con Conexion
     * @param codigo Codigo del diaganostico a eliminar
     * @param tipoCIE CIE del diagnostico a eliminar
     * @see SqlBaseRegistroDiagnosticos.eliminarRegistroDiagnostico
     * @return Integer -1 si no se pudo eliminar debido a que ese diagnosticos es usado.
     */
    public static int eliminarRegistroDiagnostico(Connection con,String codigo,int tipoCIE)
    {
        PreparedStatementDecorator ps;
        try
        {
		    ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistroDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    ps.setString(1,codigo);
		    ps.setInt(2,tipoCIE);
		    return ps.executeUpdate();
        }
        
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO ELIMINAR EL DIAGNOSTICO"+e.toString());
            return -1;
        }
    }

    /**
	 * Implementación del método que permite consultar
	 * un diagnostico en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.RegistroDiagnosticosDao#consultarRegistroDiagnostico(Connection ,String ,int )
	 */
    public static ResultSetDecorator consultarRegistroDiagnostico(Connection con,String codigo,int tipoCIE)
    {
        ResultSetDecorator rs=null;
        PreparedStatementDecorator ps;
        try
        {	
            ps= new PreparedStatementDecorator(con.prepareStatement(consultarRegistroDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,codigo);
            ps.setInt(2,tipoCIE);
            rs=new ResultSetDecorator(ps.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO REALIZAR LA CONSULTA "+e.toString());
        }
        return rs;
    }
    
    
    /**
     * Metodo que realiza una busqueda avanzada en la tabla diagnostico por cualquir de sus columnas.
     * @param con Conexxion.
     * @param codigo Codigo del diagnostico.
     * @param conCodigo Si se busca por el codigo del diagnostico.
     * @param tipoCIE CIE del diagnostico.
     * @param conTipoCIE Si se busca por el tipo_cie del diagnostico.
     * @param descripcion Descripcion del diagnostico.
     * @param conDescripcion si se busca por la descripcion del diagnostico.
     * @param activo Estado del diagnostico
     * @param conActivo
     * @param conActivo Si se busca por el estado del diagnostico
     * @param sexo Para manejar el sexo del diagnostico si masculino, fenmenino o sin restriccion
     * @param consexo si se busca por el sexo del diagnostico.     
     * @param edad_inicial Para manejar la edad inicial del diagnostico
     * @param conedad_inicial si se busca por la edad_inicial del diagnostico. 
     * @param edad_final Para manejar la edad final del diagnostico 
     * @param conedad_final si se busca por la edad_final del diagnostico.
     * @param es_principal Para manejar el indicativo del  diagnostico si es principal o no
     * @param cones_principal si se busca por el indicativo es_principal del diagnostico.
     * @param es_muerte Para manejar el indicativo del  diagnostico si es de muerte o no
     * @param cones_muerte si se busca por el indicativo de es_muerte del diagnostico.     
     * @see SqlBaseRegistroDiagnosticos.consultarRegistroDiagnosticoAvanzada
     * @return La collecion con los datos obtenidos en la consulta.
     */
    public static Collection consultarRegistroDiagnosticoAvanzada(Connection con,String codigo,boolean conCodigo,int tipoCIE,boolean conTipoCIE,String descripcion,boolean conDescripcion,boolean activo, boolean conActivo, int sexo , boolean conSexo, int edadInicial, boolean conEdadInicial, int edadFinal,  boolean conEdadFinal, String esPrincipal, boolean conEsPrincipal, String esMuerte , boolean conEsMuerte)
    {
        ResultSetDecorator rs=null; 
        Collection coleccion=null;
        Statement st;
        String sentencia="";
        sentencia=consultarRegistroDiagnosticoAvanzadaStr;
        if(conCodigo)
            sentencia+=" and UPPER(acronimo) LIKE UPPER('%"+codigo+"%')";
        if(conTipoCIE)
            sentencia+=" and tipo_cie='"+tipoCIE+"'";
        if(conDescripcion)
            sentencia+=" and UPPER(nombre) Like UPPER('%"+descripcion+"%')";
        if(conActivo)
        	sentencia+=" and activo='"+activo+"'";
        if(conSexo)
            sentencia+=" and sexo='"+sexo+"'";
        if(conEdadInicial)
            sentencia+=" and edad_inicial='"+edadInicial+"'";
        if(conEdadFinal)
            sentencia+=" and edad_final='"+edadFinal+"'";
        
        if(conEsPrincipal)
            sentencia+=" and UPPER(es_principal) = UPPER('"+esPrincipal+"')";
        if(conEsMuerte)
            sentencia+=" and UPPER(es_muerte) = UPPER('"+esMuerte+"')";        
        
        try
        {
            st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
            rs=new ResultSetDecorator(st.executeQuery(sentencia));
            coleccion=UtilidadBD.resultSet2Collection(rs);
        }
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO REALIZAR LA CONSULTA"+e.toString());
        }
        return coleccion;
    }
    
    /**
     * Metodo que implementa el combo de sexo
     * @param con
     * @return
     */
	public static HashMap consultarSexo(Connection con) {
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena=consultarSexoStr;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}    
	
	/**
	 * Método que carga el diagnóstico de egreso de una cuenta específica para
	 * vías de ingreso hospitalización y urgencias
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @param idCuenta
	 *            Codigo de la cuenta del paciente
	 * @return Acrónimo del diagnóstico buscado
	 */
	public static String cargarDiagnosticoHospUrg(Connection con, int idCuenta) 
	{
		String consulta = cargarDiagnosticoHospUrgStr + ValoresPorDefecto.getValorOrigenPL();		
		try 
		{
			PreparedStatementDecorator buscarDiagnostico =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(consulta +" "+idCuenta);
			buscarDiagnostico.setInt(1, idCuenta);
			ResultSetDecorator resultado = new ResultSetDecorator(buscarDiagnostico.executeQuery());
			if (resultado.next()) 
			{
				return resultado.getString("diagnostico");
			}
			else 
			{
				return null;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando el diagnóstico de egreso " + e);
			return null;
		}
	}
	
	/**
	 * Método que carga el diagnóstico de egreso de una cuenta específica para
	 * la vía de ingreso consulta externa
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @param numeroSolicitud
	 *            Solicitud que generó la cita
	 * @return Acrónimo del diagnóstico buscado
	 */
	public static String cargarDiagnosticoConsultaExternaAmb(	Connection con,
	        												int numeroSolicitud) 
	{
		String consulta = cargarDiagnosticoConsultaExternaStr + ValoresPorDefecto.getValorOrigenPL();
		try 
		{
			PreparedStatementDecorator buscarDiagnostico =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarDiagnostico.setInt(1, numeroSolicitud);
			ResultSetDecorator resultado = new ResultSetDecorator(buscarDiagnostico.executeQuery());
			if (resultado.next()) 
			{
				return resultado.getString("diagnostico");
			}
			else 
			{
				return null;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando el diagnóstico de egreso " + e);
			return null;
		}
	}
	
	/**
	 * Validacion que trae el ultimo diagnostico al que haya estado sujeto el paciente
	 *
	 * @param con
	 * @param idCuenta
	 * @return ultimo diagnostico
	 */
	public static String getUltimoDiagnosticoPaciente(Connection con,int idCuenta) {
		// TODO Auto-generated method stub
		String consulta = strCadenaConsultaDiagnosticoXCuenta;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			pst=  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, idCuenta);
		    rs=pst.executeQuery();
		    if(rs.next())
		    {	
		        return rs.getString("diagnostico");
		    }    	
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR getUltimoDiagnosticoPaciente",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR getUltimoDiagnosticoPaciente", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return null;
	}
}
