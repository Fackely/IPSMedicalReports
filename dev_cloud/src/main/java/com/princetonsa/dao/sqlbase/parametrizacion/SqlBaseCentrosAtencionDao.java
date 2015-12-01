package com.princetonsa.dao.sqlbase.parametrizacion;

import java.math.BigDecimal;
import java.sql.Connection;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RangosConsecutivos;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.manejoPaciente.ConstantesBDManejoPaciente;



/**
 * 
 * @author axioma
 *
 */
public class SqlBaseCentrosAtencionDao
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(SqlBaseCentrosAtencionDao.class);
	
	private static final String cargarVigiUnidadesPrimariasStr = 
		"SELECT codigo as codigo , nombre as nombre " +
		"FROM vigiunidadesprimarias  " +
		"WHERE NOT EXISTS ( SELECT 1 FROM centro_atencion WHERE cod_institucion = ?  AND vigiunidadesprimarias.codigo = centro_atencion.codupgd ) " +
		"UNION " +
		"SELECT codigo as codigo , nombre as nombre " +
		"FROM vigiunidadesprimarias  " +
		"WHERE  EXISTS ( SELECT 1 FROM centro_atencion WHERE cod_institucion = ?  AND vigiunidadesprimarias.codigo = centro_atencion.codupgd AND consecutivo = ?) "+ 
		"ORDER BY nombre";
	
	
	private static final String consultarInstitucionesSIRC = "SELECT codigo, descripcion " +
															 "FROM historiaclinica.instituciones_sirc " +
															 "WHERE institucion = ? AND " +
															 "codigo NOT IN (SELECT ca.codigo_inst_sirc FROM centro_atencion ca WHERE ca.cod_institucion = ? AND ca.codigo_inst_sirc != '') " +
															 "ORDER BY descripcion";
	
	/**
	 * Metodo que inserta un centro de atencion
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param codInstitucion
	 * @param codInstitucionSIRC 
	 * @param rangoFinalFacturaVaria 
	 * @param rangoInicialFacturaVaria 
	 * @param preFacturaVaria 
	 * @param resolucionFacturaVaria 
	 * @throws SQLException
	 */
	public static int insertarCentroAtencion(Connection con, String codigo, String descripcion, String activo, int codInstitucion, String codUpgd, 
			String codInstitucionSIRC, String empresaInstitucion,String direccion , String pais , String departamento , String ciudad , 
			String telefono , double regionCobertura , double categoriaAtencion, String codigoInterfaz,String piePaginaPresupuestoOdon, 
			String resolucion, String preFactura, BigDecimal rangoInicialFactura , BigDecimal rangoFinalFactura, String resolucionFacturaVaria, 
			String preFacturaVaria, BigDecimal rangoInicialFacturaVaria, BigDecimal rangoFinalFacturaVaria, String codigoEntidadSubcontratada) throws SQLException
	{		
		logger.info("pais==="+pais +" "+"ciudad ="+ciudad+"Departamento=="+departamento+" "+"Telefono=="+telefono + "region =" + regionCobertura + "categoria"+ categoriaAtencion);
		
		String insertarCentroAtencionStr = 
			"insert into centro_atencion (" +
			"consecutivo, " +
			"codigo, " +
			"descripcion, " +
			"activo, " +
			"cod_institucion, " +
			"codUpgd, " +
			"codigo_inst_sirc," +
			"empresa_institucion," +
			"direccion, " +
			"pais," +
			"departamento," +
			"ciudad," +			
			"telefono," +
			"region_cobertura," +
			"categoria_atencion," +
			"cod_interfaz," +
			"pie_pagina_presupuesto ," +
			"resolucion  , "+ 
			"pref_factura , "+              
			"rgo_inic_fact, "+          
			"rgo_fin_fact," +
			"resolucion_factura_varia, "+ 
			"pref_factura_varia, "+              
			"rgo_inic_fact_varia, "+          
			"rgo_fin_fact_varia, "+
			"entidad_subcontratada) "+
			"values (?, ?, ?, ?, ? " +
					", ?, ?, ?, ? , ? , " +
					"? , ? , ? , ? , ?," +
					" ?, ? , ? , ? , ? ," +
					"?, ?, ?, ?, ?, ?)";//26
		
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_centro_atencion");		
        PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con, insertarCentroAtencionStr);
        sentencia.setInt(1, consecutivo);
        sentencia.setString(2, codigo);
        sentencia.setString(3, descripcion);
        sentencia.setBoolean(4, UtilidadTexto.getBoolean(activo));
        sentencia.setInt(5, codInstitucion);        
        
        if(!codUpgd.equals("") && !codUpgd.equals("0"))
        	sentencia.setInt(6, Utilidades.convertirAEntero(codUpgd));
        else
        	sentencia.setNull(6, Types.INTEGER);
        
        if(!codInstitucionSIRC.equals("") && !codInstitucionSIRC.equals("0"))
        	sentencia.setString(7,codInstitucionSIRC);
        else
        	sentencia.setNull(7,Types.VARCHAR);
        
        if(!UtilidadTexto.isEmpty(empresaInstitucion))
        	sentencia.setDouble(8, Utilidades.convertirADouble(empresaInstitucion));
        else
        	sentencia.setNull(8, Types.NUMERIC);
        
        //-------------------------------------------------------------------------------------
        //direccion --> cambio por tarea 77564
        if (UtilidadCadena.noEsVacio(direccion))
        	sentencia.setString(9, direccion);
        else
        	sentencia.setNull(9, Types.VARCHAR);
                        
        //-------------------------------------------------------------------------------------
                
        if (UtilidadCadena.noEsVacio(pais))
        	sentencia.setString(10, pais);
        else
        	sentencia.setNull(10, Types.VARCHAR);
        
        
        if (UtilidadCadena.noEsVacio(departamento))
        	sentencia.setString(11, departamento);
        else
        	sentencia.setNull(11, Types.VARCHAR);
       
        
        if (UtilidadCadena.noEsVacio(ciudad))
        	sentencia.setString(12, ciudad);
        else
        	sentencia.setNull(12, Types.VARCHAR);
        
        
        
        if (UtilidadCadena.noEsVacio(telefono))
        	sentencia.setString(13, telefono);
        else
        	sentencia.setNull(13, Types.VARCHAR);
       
        
        if (regionCobertura > ConstantesBD.codigoNuncaValido)
        	sentencia.setDouble(14, regionCobertura);
        else
        	sentencia.setNull(14, Types.DOUBLE);
        
        

        if (categoriaAtencion > ConstantesBD.codigoNuncaValido)
        	sentencia.setDouble(15, categoriaAtencion);
        else
        	sentencia.setNull(15, Types.DOUBLE);
        
        if (!codigoInterfaz.equals(""))
        	sentencia.setString(16, codigoInterfaz);
        else
        	sentencia.setNull(16, Types.VARCHAR);
        
        if (!piePaginaPresupuestoOdon.equals(""))
        	sentencia.setString(17, piePaginaPresupuestoOdon);
        else
        	sentencia.setNull(17,Types.VARCHAR);

        if(!UtilidadTexto.isEmpty(resolucion))
        {
        	sentencia.setString(18, resolucion);
        }
        else
        {
        	sentencia.setNull(18, Types.VARCHAR);
        }
        
        if(!UtilidadTexto.isEmpty(preFactura))
        {
        	sentencia.setString(19, preFactura);
        }
        else
        {
        	sentencia.setNull( 19, Types.VARCHAR);
        }

        if(rangoInicialFactura!=null  && rangoInicialFactura.doubleValue()>0)
        {
        	sentencia.setBigDecimal(20, rangoInicialFactura);
        }
        else
        {
        	sentencia.setNull(20, Types.NUMERIC);
        }
        
        
        if( rangoFinalFactura!=null && rangoFinalFactura.doubleValue()>0)
        {
        	sentencia.setBigDecimal(21, rangoFinalFactura);
        }
        else
        {
        	sentencia.setNull(21, Types.NUMERIC);
        }
        
        if(!UtilidadTexto.isEmpty(resolucionFacturaVaria))
        {
        	sentencia.setString(22, resolucionFacturaVaria);
        }
        else
        {
        	sentencia.setNull(22, Types.VARCHAR);
        }
        
        if(!UtilidadTexto.isEmpty(preFacturaVaria))
        {
        	sentencia.setString(23, preFacturaVaria);
        }
        else
        {
        	sentencia.setNull( 23, Types.VARCHAR);
        }

        if(rangoInicialFacturaVaria!=null  && rangoInicialFacturaVaria.doubleValue()>0)
        {
        	sentencia.setBigDecimal(24, rangoInicialFacturaVaria);
        }
        else
        {
        	sentencia.setNull(24, Types.NUMERIC);
        }
        
        if(rangoFinalFacturaVaria!=null && rangoFinalFacturaVaria.doubleValue()>0)
        {
        	sentencia.setBigDecimal(25, rangoFinalFacturaVaria);
        }
        else
        {
        	sentencia.setNull(25, Types.NUMERIC);
        }
        
        if(codigoEntidadSubcontratada!=null && !codigoEntidadSubcontratada.equals(""))
        {
        	sentencia.setInt(26, Integer.parseInt(codigoEntidadSubcontratada));
        }
        else
        {
        	sentencia.setNull(26, Types.INTEGER);
        }
        
        logger.info(" INSERTAR CENTRO DE ATENCION \n\n\n\n");
        logger.info("\t\t"+sentencia);
        logger.info("\n\n\n\n\n");
        
        if( sentencia.executeUpdate()>0)
        {
        	return consecutivo;
        }
        
        return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método para actualizar la información de un centro de atención
	 * @param con
	 * @param consecutivo
	 * @param descripcion
	 * @param activocodigo_inst_sirc
	 * @param codInstitucion
	 * @param codInstitucionSIRC
	 * @param rangoFinalFacturaVaria 
	 * @param rangoInicialFacturaVaria 
	 * @param preFacturaVaria 
	 * @param resolucionFacturaVaria 
	 * @throws SQLException
	 */
	public static void actualizarCentroAtencion(Connection con, int consecutivo, String descripcion, String activo, String codUpgd, 
			String codInstitucionSIRC, String empresaInstitucion,String direccion,String telefono, String pais,String departamento, 
			String ciudad, double regionCobertura, double categoriaAtencion, String codInterfaz, String piePagina ,  
			String resolucion , String preFactura , BigDecimal rangoInicialFactura ,  BigDecimal rangoFinalFactura, 
			String resolucionFacturaVaria, String preFacturaVaria, BigDecimal rangoInicialFacturaVaria, 
			BigDecimal rangoFinalFacturaVaria, String codigoEntidadSubcontratada) throws SQLException
	{
		String actualizarCentroAtencionStr = 
			"update centro_atencion set " +
			"descripcion=?, " +
			"activo=? ," +
			"codupgd=?, " +
			"codigo_inst_sirc=?, " +
			"empresa_institucion=?," +
			"direccion =?, " +
			"telefono=?, "+
            "pais=?, "+
            "departamento=? , "+
             "ciudad=?, "     +
            "region_cobertura=?, "+
            "categoria_atencion=?," +
            "cod_interfaz=?," +
            "pie_pagina_presupuesto=? ," +
            "resolucion= ?, " +
            "pref_factura=?, " +
            "rgo_inic_fact=?, " +
            "rgo_fin_fact =?, "+
            "resolucion_factura_varia= ?, " +
            "pref_factura_varia=?, " +
            "rgo_inic_fact_varia=?, " +
            "rgo_fin_fact_varia =?, "+
            "entidad_subcontratada =? "+
			"where consecutivo=? ";

		try
		{			
			PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con , actualizarCentroAtencionStr);
			sentencia.setString(1, descripcion);
			sentencia.setBoolean(2, UtilidadTexto.getBoolean(activo));		
			
			if(!codUpgd.equals("") && !codUpgd.equals("0"))
				sentencia.setInt(3, Utilidades.convertirAEntero(codUpgd));
			else
				sentencia.setNull(3, Types.INTEGER);
			
			if(!codInstitucionSIRC.equals("") && !codInstitucionSIRC.equals("0") && !codInstitucionSIRC.equals(ConstantesBD.codigoNuncaValido+""))
		     	sentencia.setString(4,codInstitucionSIRC);
		    else
		      	sentencia.setNull(4,Types.VARCHAR);		
			
			if( !UtilidadTexto.isEmpty(empresaInstitucion) )
	        	sentencia.setDouble(5, Utilidades.convertirADouble(empresaInstitucion));
	        else
	        	sentencia.setNull(5, Types.NUMERIC);
			
			//--------------------------------------------------------------------
			// direccion --> modificado por tarea 77564
			if (UtilidadCadena.noEsVacio(direccion))
				sentencia.setString(6, direccion);
			else
				sentencia.setNull(6, Types.VARCHAR);
			//----------------------------------------------------------------------
			
			if (UtilidadCadena.noEsVacio(telefono))
				sentencia.setString(7, telefono);
			else
				sentencia.setNull(7, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(pais))
				sentencia.setString(8, pais);
			else
				sentencia.setNull(8, Types.VARCHAR);
			
			

			if (UtilidadCadena.noEsVacio(departamento))
				sentencia.setString(9, departamento);
			else
				sentencia.setNull(9, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(ciudad))
				sentencia.setString(10, ciudad);
			else
				sentencia.setNull(10, Types.VARCHAR);
			
			if (regionCobertura > ConstantesBD.codigoNuncaValido)
				sentencia.setDouble(11, regionCobertura);
			else
				sentencia.setNull(11, Types.DOUBLE);
			
			if (categoriaAtencion > ConstantesBD.codigoNuncaValido)
				sentencia.setDouble(12, categoriaAtencion);
			else
				sentencia.setNull(12, Types.DOUBLE);
			
			if (!codInterfaz.equals(""))
				sentencia.setString(13, codInterfaz);
			else
				sentencia.setNull(13, Types.NULL);
			
			if (!piePagina.isEmpty())
				sentencia.setString(14, piePagina);
			else
				sentencia.setNull(14, Types.NULL);
			
			if(!UtilidadTexto.isEmpty(resolucion))
			{
				sentencia.setString(15, resolucion);
			}
			else
			{
				sentencia.setNull(15, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(preFactura))
			{
				sentencia.setString(16, preFactura);
			}
			else
			{
				sentencia.setNull(16, Types.VARCHAR);
			}
			
			
			if(  (rangoInicialFactura!=null) && (rangoInicialFactura.doubleValue()>0) )
			{
				sentencia.setBigDecimal(17, rangoInicialFactura);
			}
			else
			{
				sentencia.setNull(17, Types.NUMERIC);
			}
			
			
			if( (rangoFinalFactura!=null) && (rangoFinalFactura.doubleValue()>0) )
			{
				sentencia.setBigDecimal(18, rangoFinalFactura);
			}
			else
			{
				sentencia.setNull(18, Types.NUMERIC);
			}
			
			if(!UtilidadTexto.isEmpty(resolucionFacturaVaria))
	        {
	        	sentencia.setString(19, resolucionFacturaVaria);
	        }
	        else
	        {
	        	sentencia.setNull(19, Types.VARCHAR);
	        }
	        
	        if(!UtilidadTexto.isEmpty(preFacturaVaria))
	        {
	        	sentencia.setString(20, preFacturaVaria);
	        }
	        else
	        {
	        	sentencia.setNull(20, Types.VARCHAR);
	        }

	        
	        if(rangoInicialFacturaVaria!=null  && rangoInicialFacturaVaria.doubleValue()>0)
	        {
	        	sentencia.setBigDecimal(21, rangoInicialFacturaVaria);
	        }
	        else
	        {
	        	sentencia.setNull(21, Types.NUMERIC);
	        }
	        
	        if(rangoFinalFacturaVaria!=null && rangoFinalFacturaVaria.doubleValue()>0)
	        {
	        	sentencia.setBigDecimal(22, rangoFinalFacturaVaria);
	        }
	        else
	        {
	        	sentencia.setNull(22, Types.NUMERIC);
	        }

	        if(codigoEntidadSubcontratada!=null && !codigoEntidadSubcontratada.equals(""))
	        {
	        	sentencia.setInt(23, Integer.parseInt(codigoEntidadSubcontratada));
	        }
	        else
	        {
	        	sentencia.setNull(23, Types.INTEGER);
	        }
	        
			sentencia.setInt(24, consecutivo);			
		
			Log4JManager.info("ACTUALIZACION DE CENTRO DE ATENCION "+sentencia);
			
			sentencia.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Método para eliminar un centro de atención
	 * @param con
	 * @param consecutivo
	 * @throws SQLException
	 */
	public static void eliminarCentroAtencion(Connection con, int consecutivo) throws SQLException
	{
		String eliminarCentroAtencionStr =
			"delete from centro_atencion where consecutivo=?";
		
		PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(eliminarCentroAtencionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		sentencia.setInt(1, consecutivo);
		sentencia.executeUpdate();
	}
	
	/**
	 * Método para consultar la información de un centro de atención
	 * @param con
	 * @param consecutivo
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCentroAtencion(Connection con, int consecutivo) throws SQLException
	{
		String consultarCentroAtencionStr =
			"select " +
			"ca.consecutivo as consecutivo, " +
			"ca.codigo as codigo, " +
			"ca.descripcion as descripcion, " +
			"ca.activo as activo, " +
			"ca.cod_institucion as codinstitucion, " +
			"ca.codupgd as codUpgd,  " +
			"ca.telefono as telefono,  " +
			"ca.pais as pais,  " +
			"ca.departamento as departamento,  " +
			"ca.ciudad as ciudad,  " +
			"administracion.getnombreciudad(ca.pais,ca.departamento,ca.ciudad) as descripcion_ciudad,  " +
			"ca.categoria_atencion as categoria_atencion,  " +
			"ca.region_cobertura as region_cobertura,  " +
			"fa.descripcion as descripcion_categoria, " +
			"ra.descripcion as descripcion_region," +
			"ca.cod_interfaz AS codinterfaz," +
			"ca.pie_pagina_presupuesto AS piepaginapresupuesto," +
			"ca.resolucion as resolucion,  " +
			"ca.pref_factura as pref_factura,  " +
			"ca.rgo_inic_fact as rgo_inic_fact, " +
			"ca.rgo_fin_fact as rgo_fin_fact , "+
			"CASE when ca.codigo_inst_sirc IS NULL THEN '' ELSE ca.codigo_inst_sirc END as codinstitucionsirc, " +
			"CASE when ca.codigo_inst_sirc IS NULL THEN '' ELSE inst.descripcion END as descinstitucionsirc, " +
			"CASE WHEN ca.empresa_institucion IS NULL THEN '' ELSE ca.empresa_institucion||'' END  AS empresainstitucion, " +
			"CASE WHEN ca.empresa_institucion IS NULL THEN '' ELSE facturacion.getdescempresainstitucion(ca.empresa_institucion) END  AS descempresainstitucion, " +
			"COALESCE (ca.direccion,'') As direccion, " +
			"ca.resolucion_factura_varia as resolucion_factura_varia,  " +
			"ca.pref_factura_varia as pref_factura_varia,  " +
			"ca.rgo_inic_fact_varia as rgo_inic_fact_varia, " +
			"ca.rgo_fin_fact_varia as rgo_fin_fact_varia, "+
			"ca.entidad_subcontratada as codigoentidadsubcontratada "+
			"from administracion.centro_atencion ca " +
			"LEFT JOIN historiaclinica.instituciones_sirc inst ON (inst.codigo=ca.codigo_inst_sirc) " +
			"LEFT JOIN administracion.categorias_atencion fa on (fa.codigo=ca.categoria_atencion) " +
			"LEFT JOIN administracion.regiones_cobertura ra on (ra.codigo=ca.region_cobertura) " +
			"where ca.consecutivo=?  ";
		
		logger.info("Cadena Consulta Centro Atencion >> "+consultarCentroAtencionStr + "  consecutivo->"+consecutivo );
		PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(consultarCentroAtencionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		sentencia.setInt(1, consecutivo);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(sentencia.executeQuery()));
		//Recorrer Mapa?.
		
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
			Utilidades.imprimirMapa(mapaRetorno);
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
		
		sentencia.close();
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los centros de atención de una institución especificada
	 * @param con
	 * @param codInstitucion
	 * @returnaccionEmpezar
	 * @throws SQLException
	 */
	public static Collection consultarCentrosAtencionInst(Connection con, int codInstitucion) throws SQLException
	{
		String consultarCentrosAtencionInstStr = "select " +
		"c.consecutivo as consecutivo, " +
		"c.codigo as codigo, " +
		"c.descripcion as descripcion, " +
		"c.activo as activo, " +
		"c.cod_institucion as codInstitucion, " +
		"c.codupgd as codUpgd ," +	
		"coalesce(c.direccion,'') as direccion," +
		"c.telefono as telefono,  " +
		"c.pais as pais,  " +
		"getdescripcionpais(c.pais) as descripcion_pais,  " +
		"c.departamento as departamento,  " +
		"c.ciudad as ciudad,  " +
		"getnombreciudad(c.pais,c.departamento,c.ciudad) as descripcion_ciudad,  " +
		"c.categoria_atencion as categoria_atencion,  " +
		"c.region_cobertura as region_cobertura,  " +
		"fa.descripcion as descripcion_categoria, " +
		"ra.descripcion as descripcion_region, " +
		"c.cod_interfaz AS codinterfaz, " +
		"c.resolucion as resolucion,  " +
		"c.pref_factura as pref_factura,  " +
		"c.rgo_inic_fact as rgo_inic_fact, " +
		"c.rgo_fin_fact as rgo_fin_fact , "+
		"c.pie_pagina_presupuesto AS piepaginapresupuesto, " +
		"CASE  when v.nombre  IS NULL THEN '' ELSE v.nombre end as nomUpgd, " +
		"CASE when c.codigo_inst_sirc IS NULL THEN '' ELSE c.codigo_inst_sirc END as codinstitucionsirc, " +		
		"CASE when c.codigo_inst_sirc IS NULL THEN '' ELSE inst.descripcion END as descinstitucionsirc, " +
		"CASE WHEN c.empresa_institucion IS NULL THEN '' ELSE c.empresa_institucion||'' END AS empresainstitucion, " +
		"CASE WHEN c.empresa_institucion IS NULL THEN '' ELSE getdescempresainstitucion(c.empresa_institucion) END AS descempresainstitucion ," +
		"CASE WHEN (select count(0) from administracion.consecutivos_centro_aten cca where cca.centro_atencion=c.consecutivo)>0 THEN 'S' else 'N' END  AS existeconsecutivo, "+
		"c.resolucion_factura_varia as resolucion_factura_varia,  " +
		"c.pref_factura_varia as pref_factura_varia,  " +
		"c.rgo_inic_fact_varia as rgo_inic_fact_varia, " +
		"c.rgo_fin_fact_varia as rgo_fin_fact_varia, "+
		"c.entidad_subcontratada as codigoentidadsubcontratada "+
		"from administracion.centro_atencion c " +
		"LEFT OUTER JOIN epidemiologia.vigiunidadesprimarias v ON ( c.codupgd = v.codigo)  " +
		"LEFT JOIN historiaclinica.instituciones_sirc inst ON (c.codigo_inst_sirc=inst.codigo) " +
		"LEFT JOIN administracion.categorias_atencion fa on (fa.codigo=categoria_atencion) " +
		"LEFT JOIN administracion.regiones_cobertura ra on (ra.codigo=region_cobertura) " +
		
		" "+
		"where  c.cod_institucion=? ";			
		
		logger.warn("\n\n valor sql "+consultarCentrosAtencionInstStr+"\n\n");
		
		PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(consultarCentrosAtencionInstStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		sentencia.setInt(1, codInstitucion);
		
		
		return  UtilidadBD.resultSet2Collection(new ResultSetDecorator(sentencia.executeQuery()));
	}	

	
	/**
	 * Método que carga los codigos Upgd; sin incluir los que ya estan asignados a algun centro de atencion
	 * @param con
	 * @param codInstitucio
	 * @param codInstitucio
	 * @return
	 */
	public static HashMap cargarVigiUnidadesPrimarias(Connection con, int codInstitucion, int codConsecutivo )
	{
		
		try
		{	
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarVigiUnidadesPrimariasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,codInstitucion);
			pst.setInt(2,codInstitucion);
			pst.setInt(3,codConsecutivo);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			return null;
		}
	}
	
	/**
	 * Metodo que carga los codigos de Instituciones SIRC; sin incluir los que ya estan asignados a algun centro de atencion
	 * @param con
	 * @param codInstitucion
	 * */
	public static HashMap cargarInstitucionesSIRC(Connection con, int codInstitucion)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarInstitucionesSIRC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codInstitucion);
			ps.setInt(2,codInstitucion);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}				
		
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return
	 */
	public static double obtenerEmpresaInstitucionCentroAtencion( Connection con, int consecutivoCentroAtencion)
	{
		String consulta="SELECT COALESCE(empresa_institucion, "+ConstantesBD.codigoNuncaValido+") as empresainstitucion FROM centro_atencion WHERE consecutivo=? ";
		double retorna=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try
		{			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));			
			ps.setInt(1,consecutivoCentroAtencion);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna= rs.getDouble("empresainstitucion");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return retorna;
	}

	/**
	 * Metodo que consulta el rango inicial y final de los consecutivos de facturacion
	 * @param centroAtencion
	 * @return
	 */
	public static RangosConsecutivos obtenerRangosFacturacionXCentroAtencion(int centroAtencion)
	{
		RangosConsecutivos rango= null;
		String consulta=" SELECT " +
							"coalesce(rgo_inic_fact,"+ConstantesBD.codigoNuncaValido+") as rangoinicial, " +
							"coalesce(rgo_fin_fact,"+ConstantesBD.codigoNuncaValido+") as rangofinal " +
						"from " +
							"administracion.centro_atencion " +
						"where " +
							"consecutivo=?";
		
		try
		{			
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,centroAtencion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				rango= new RangosConsecutivos(rs.getBigDecimal(1), rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		return rango;
	}
	
	/**
	 * 
	 * Metodo para obtener el pie de pagina del presupuesto
	 * @param centroAtencion
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static String obtenerPiePaginaPresupuesto(int centroAtencion)
	{
		String consulta=" select coalesce(pie_pagina_presupuesto,'') as pie from administracion.centro_atencion where consecutivo=? ";
		String resultado="";
		try
		{			
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,centroAtencion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= rs.getString("pie");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		return resultado;
	}
	
	
	/**
	 * Metodo que carga las entidades subcotnratadas activas y que tiene asociados
	 * centros de costo identificados como SubAlmacen
	 */
	public static HashMap cargarEntidadesSubcontratadas(Connection con, int codInstitucion, int centroAtencion)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT DISTINCT es.codigo_pk AS codigopk, es.razon_social AS razonsocial"+
				  " FROM facturacion.entidades_subcontratadas es, facturacion.centros_costo_entidades_sub cces,"+ 
			      " ADMINISTRACION.centros_costo cc"+
				  " WHERE es.activo='"+ConstantesBD.acronimoSiChar+"'"+
				  " AND es.institucion=?"+
				  " AND cc.tipo_area="+ConstantesBDManejoPaciente.codigoTipoAreaSubAlmacen+
				  " AND cces.entidad_subcontratada = es.codigo_pk"+
				  " AND cc.codigo= cces.centro_costo"+
				  " AND cc.centro_atencion= ?"+
				  " ORDER BY es.razon_social";
			logger.info("===>Consultar Lista De Entidades Subcontratadas: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codInstitucion);
			pst.setInt(2, centroAtencion);			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener entidades Subcontratadas: "+e);
		}
		return mapa;
	}
}