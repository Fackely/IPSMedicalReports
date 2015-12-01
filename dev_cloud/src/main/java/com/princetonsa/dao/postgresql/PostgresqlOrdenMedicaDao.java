/*
 * Creado en Jun 1, 2005
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.OrdenMedicaDao;
import com.princetonsa.dao.sqlbase.SqlBaseOrdenMedicaDao;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PostgresqlOrdenMedicaDao implements OrdenMedicaDao
{
	String cadenaHoraOrden="to_char(enc.hora_orden, 'HH24:MI')";
	String cadenaHoraGrabacion="to_char(enc.hora_grabacion, 'HH24:MI:SS')";
	
	/**
	 * Método para obtener el codigo de la orden que está asociado a una secuencia
	 * @param con -> conexion
	 * @return codigoOrden
	 */
	public int obtenerCodigoOrden(Connection con)
	{
		String secuencia="nextval('seq_ordenes_medicas') as codigo";
		return SqlBaseOrdenMedicaDao.obtenerCodigoOrden(con, secuencia);
	}

	/**
	 * Método para insertar una orden médica
	 * @param con
	 * @param cuenta
	 * @param descripcionDieta
	 * @param observacionesGenerales
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @param descripcionDiete
	 * @param descripcionSoporte
	 * @param tipoMonitoreo
	 * @param fechaOrden
	 * @param horaOrden
	 * @param descripcionSoporte
	 * @return
	 */
	

	public  DtoResultado insertarOrdenMedica(Connection con,  int cuenta, String descripcionSoporteRespiratorio, String descripcionDieta,  String observacionesGenerales, String descripcionDietaParenteral,Boolean tieneDatos)
	{
		String secuencia="nextval('seq_ordenes_medicas') as codigo";
		String codigoDetalleObservacion="select nextval('seq_ordenes_medicas') as codigo";
		return SqlBaseOrdenMedicaDao.insertarOrdenMedica(con,  secuencia, cuenta, descripcionSoporteRespiratorio, descripcionDieta,  observacionesGenerales, descripcionDietaParenteral,codigoDetalleObservacion,tieneDatos);
	}
	

	/**
	 * 
	 */
	
	public int insertarEncabezadoOrdenMedica(Connection con,int codigoOrden, String fechaOrden, String horaOrden, String login, String  datosMedico)
	{
		String secuencia="seq_encabezado_histo_orden_m";
		return SqlBaseOrdenMedicaDao.insertarEncabezadoOrdenMedica(con,  secuencia, codigoOrden, fechaOrden, horaOrden, login, datosMedico);
	}
	
	
	/**
	 * Método para insertar el tipo de monitoreo a una orden médica
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @param tipoMonitoreo
	 * @return
	 */
	
	public int insertarOrdenTipoMonitoreo (Connection con, int codigoEncabezado, int tipoMonitoreo)
	{
		return SqlBaseOrdenMedicaDao.insertarOrdenTipoMonitoreo (con, codigoEncabezado, tipoMonitoreo);
	}
	
	
	/**
	 * Método para insertar el soporteRespiratorio a una orden médica
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param equipoElemento
	 * @param cantidadSoporteRespiratorio
	 * @param oxigenoTerapia
	 * @return
	 */
	public int insertarOrdenSoporteRespiratorio(Connection	con, int codigoEncabezado, int equipoElemento, float cantidadSoporteRespiratorio, String oxigenoTerapia, String descripcionSoporteRespiratorio)
	{
		return SqlBaseOrdenMedicaDao.insertarOrdenSoporteRespiratorio (con, codigoEncabezado, equipoElemento, cantidadSoporteRespiratorio, oxigenoTerapia, descripcionSoporteRespiratorio);
	}
	
	/**
	 * Funcion Para retornar una collecion con el listado de los tipos de nutricion Oral
	 * @param con
	 * @param mezcla
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param codigo de la institucion
	 * @param codigo del centro_costo
	 * @param Nro Consulta parametro que indica la informacion a sacar
	 *		1  Listado de tipos de nutricion Oral		
	 * 		  2  Listado de tipos de nutricion parenteral
	 * 		  3  Listado de los tipos de cuidados de emfermeria
	 * 		  4  Listado de tipos de elemnetos en el soporte respiratorio
	 *  	  5  Listado de tipos de monitoreo	
	 * @return Una  Collection 
	 */	
	
	
	public Collection consultarTipos(Connection con,int institucion, int centro_costo, int Nro_consulta, int mezcla, int codigoCuenta, int cuentaAsocio, int inicioEncabezado, int finEncabezado)
	//public Collection consultarTipos(Connection con, int institucion, int centro_costo, int nroConsulta)
	{
		return SqlBaseOrdenMedicaDao.consultarTipos(con, institucion, centro_costo, Nro_consulta, mezcla, codigoCuenta, cuentaAsocio, inicioEncabezado, finEncabezado,DaoFactory.POSTGRESQL);
	}

	/**
	 * Metodo para insertar los tipo de nutricion (oral y parenteral)
	 * recibiendo como parametro la orden medica.
	 * @throws SQLException
	 * @throws SQLException
	 * 
	 */
	public int insertarNutricion(Connection con, int codigoEncabezado, int tipoNutricion, float volumen, String unidadVolumen, int tipoNut, UsuarioBasico usuario, PersonaBasica paciente,String esMedicamento)throws IPSException 
	{
		return SqlBaseOrdenMedicaDao.insertarNutricion(con, codigoEncabezado, tipoNutricion, volumen,unidadVolumen, tipoNut,usuario,paciente,esMedicamento);
	}

	/**
	 *  Metodo para insertar los datos de Orden Dieta 
	 * @param con
	 * @param codigoEncabezado
	 * @param velocidadInfusion
	 * @param farmacia
	 * @param nutricionOral
	 * @param nutricionParenteral
	 * @param finalizarDieta
	 * @param volumentTotal
	 * @return
	 */
								
	public int insertarOrdenDieta(Connection con,int codigoEncabezado, String volumenTotal,String unidadVolumenTotal, String velocidadInfusion, int farmacia,
			  					  							 boolean nutricionOral, boolean nutricionParenteral, boolean finalizarDieta, int mezcla, int numeroSolicitud, String codigoInstitucion,String dosificacion)
	{
		return SqlBaseOrdenMedicaDao.insertarOrdenDieta(con, codigoEncabezado, volumenTotal,unidadVolumenTotal, velocidadInfusion, farmacia, nutricionOral, nutricionParenteral,finalizarDieta, mezcla, numeroSolicitud, codigoInstitucion,dosificacion);	
	}

	/**
	 * Metodo Para Insertar Los cuidados especiales de emfermeria 
	 * @param con
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @return
	 */
	public int insertarOrdenCuidadoEnf(Connection con, int codigoOrden, String login, String datosMedico)
	{
		String codigoCuidadoEnf = "nextval('seq_orden_cuidado_enfermeria') as codigo ";
	
		return SqlBaseOrdenMedicaDao.insertarOrdenCuidadoEnf(con, codigoCuidadoEnf, codigoOrden, login,  datosMedico);	
	}

	/**
	 * Metodo Para Insertar Los detalles de cuidados especiales de emfermeria
	 * 
	 * @param con
	 * @param codigoOrdenCuidadoEnf
	 * @param codigosTipoCuidado
	 * @param presenta
	 * @param descripcion
	 * @param OtroCuidadoEnf
	 * @return
	 */
	
	public int insertarDetalleOrdenCuidadoEnf(Connection con, int codigoOrdenCuidadoEnf, int codigosTipoCuidado, String presenta, String descripcion, int OtroCuidadoEnf)
	{
		return SqlBaseOrdenMedicaDao.insertarDetalleOrdenCuidadoEnf(con, codigoOrdenCuidadoEnf, codigosTipoCuidado, presenta, descripcion, OtroCuidadoEnf);	
	}
	
	/**
	 * Funcion para cargar la orden Medica de un paciente Especifico
	 * @param con
	 * @param codigoCuenta
	 * @return Collection con los datos de la orden médica
	 */
	public Collection cargarOrdenMedica(Connection con, int codigoCuenta)
	{
		return SqlBaseOrdenMedicaDao.cargarOrdenMedica(con, codigoCuenta);	
	}
	
	/**
	 * Funcion para consultar los historicos de nutricion parenteral 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarNutricionParentHisto(Connection con, int codigoCuenta, int cuentaAsocio, int mezcla)
	{
		return SqlBaseOrdenMedicaDao.consultarNutricionParentHisto(con, codigoCuenta, cuentaAsocio, mezcla);
	}	

	
	/**
	 * Metodo para consultar el historico de nutricion Oral  
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarNutricionOralHisto(Connection con, int codigoCuenta, int cuentaAsocio)
	{
		return SqlBaseOrdenMedicaDao.consultarNutricionOralHisto(con, codigoCuenta, cuentaAsocio, cadenaHoraOrden, cadenaHoraGrabacion);
	}
	
	/**
	 * Funcion para retornar los historicos de los monitoreos a una persona especifica 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarMonitoreosHisto(Connection con, int codigoCuenta, int cuentaAsocio)
	{
		return SqlBaseOrdenMedicaDao.consultarMonitoreosHisto(con, codigoCuenta, cuentaAsocio, cadenaHoraOrden);
	}
	
	/**
	 * Metodo para consultar los historicos del soporte respiratorio
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	
	public Collection consultarSoporteRespiraHisto(Connection con, int codigoCuenta, int cuentaAsocio)
	{
		return SqlBaseOrdenMedicaDao.consultarSoporteRespiraHisto(con, codigoCuenta, cuentaAsocio, cadenaHoraOrden, cadenaHoraGrabacion);
	}

	
	/**
	 * Metodo para consultar los historicos de cuidado enfermeria 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param institucion
	 * @param centroCosto
	 * @return
	 */
	public Collection consultarCuidadosEnfHisto(Connection con, int codigoCuenta, int cuentaAsocio, int institucion, int centroCosto,String fechaInicial,String fechaFinal)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		String cue = "";
		
		if ( cuentaAsocio != 0) 
		{ 
			cue = "	WHERE re.cuenta IN ("+ codigoCuenta + ", " + cuentaAsocio + " )"; 
		}
		else
		{ 
			cue = "	WHERE re.cuenta = " + codigoCuenta; 
		}
		
		
		
		
		consultaBuffer.append( " SELECT * FROM (" +
							   					" (SELECT   1 as medico," +
							   						" to_char (enc.fecha_grabacion,'DD/MM/YYYY')  || '  --  ' || to_char(enc.hora_grabacion, 'HH:MI') as fecha_hora_grab, " +
							   						" to_char (enc.fecha_orden,'DD/MM/YYYY')  || '  --  ' || to_char(enc.hora_orden, 'HH:MI') as fecha_hora," +
							   						" dce.cod_histo_enca as  codenf, ceci.codigo AS codigo, tce.descripcion as tipodescripcion, " +
							   						" dce.presenta as presenta, dce.descripcion as detalledescripcion, null as totro_otro_cual, " +
							   						" null as totro_presenta, null as totro_descripcion,tce.control_especial as controlespecial " +
							   					" FROM ordenes_medicas om " + 
							   					" INNER JOIN encabezado_histo_orden_m enc ON (enc.orden_medica=om.codigo) " + 
							   					" INNER JOIN detalle_cuidado_enfer dce ON (enc.codigo = dce.cod_histo_enca) " +
							   					" INNER JOIN cuidado_enfer_cc_inst ceci ON (ceci.codigo=dce.cuidado_enfer_cc_inst) " +
							   					" INNER JOIN tipo_cuidado_enfermeria tce ON (ceci.cuidado_enfermeria=tce.codigo) " );
		if(cuentaAsocio != 0)
			consultaBuffer.append(" WHERE om.cuenta IN (?,?) ");
		else		
			consultaBuffer.append(" WHERE om.cuenta = ? ");

		consultaBuffer.append(" AND enc.fecha_orden between '"+fechaInicial+"' and '"+fechaFinal+"'");
				
		consultaBuffer.append(" UNION " +
								   " SELECT 1 as medico, " +
								   		" to_char (enc.fecha_grabacion,'DD/MM/YYYY')  || '  --  ' || to_char(enc.hora_grabacion, 'HH:MI') as fecha_hora_grab, " +
								   		" to_char (enc.fecha_orden,'DD/MM/YYYY')  || '  --  ' || to_char(enc.hora_orden, 'HH:MI') as fecha_hora, " +
								   			" doce.cod_histo_cuidado_enfer as  codenf, doce.otro_cual AS codigo, toce.descripcion as tipodescripcion, " + 
								   		" doce.presenta as presenta, doce.descripcion as detalledescripcion, null as totro_otro_cual, " +
								   		" null as totro_presenta, null as totro_descripcion, '' as controlespecial  " + 
								   " FROM ordenes_medicas om " + 
								   " INNER JOIN encabezado_histo_orden_m enc ON (enc.orden_medica=om.codigo) " + 
								   " INNER JOIN detalle_otro_cuidado_enf doce ON (enc.codigo=doce.cod_histo_cuidado_enfer) " +
								   " INNER JOIN tipo_otro_cuidado_enf toce ON (doce.otro_cual=toce.codigo) ");

		if(cuentaAsocio != 0)
			consultaBuffer.append(" WHERE om.cuenta IN (?,?) ");
		else		
			consultaBuffer.append(" WHERE om.cuenta = ? ");

		consultaBuffer.append(" AND enc.fecha_orden between '"+fechaInicial+"' and '"+fechaFinal+"'");
		
			consultaBuffer.append(") UNION ALL ( "); 
			consultaBuffer.append(" SELECT 0 as medico, " +
								  		" to_char (ehre.fecha_grabacion,'DD/MM/YYYY')  || '  -- ' || to_char(ehre.hora_grabacion, 'HH:MI') as fecha_hora_grab, " +  
								  		" to_char (ehre.fecha_registro,'DD/MM/YYYY')  || '  --  ' || to_char(ehre.hora_registro, 'HH:MI') as fecha_hora, " +
								  		" dcre.codigo_histo_enfer as codenf, ceci.codigo AS codigo, tce.descripcion as tipodescripcion, " +
								  		" dcre.presenta as presenta, dcre.descripcion as detalledescripcion, null as totro_otro_cual, " +
								  		" null as totro_presenta, null as totro_descripcion,tce.control_especial as controlespecial " +
					  			  " FROM registro_enfermeria re " +			
								  " INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.registro_enfer = re.codigo ) " +
								  " INNER JOIN detalle_cuidado_reg_enfer dcre ON ( ehre.codigo = dcre.codigo_histo_enfer ) " +	
								  " INNER JOIN cuidado_enfer_cc_inst ceci ON ( dcre.cuidado_enfer_cc_inst = ceci.codigo ) " +
								  " INNER JOIN tipo_cuidado_enfermeria tce ON ( ceci.cuidado_enfermeria = tce.codigo ) " +
								  "	" + cue + " " +
								  " AND ehre.fecha_registro between '"+fechaInicial+"' and '"+fechaFinal+"'" +
								  "	UNION " +
								  "	SELECT 0 as medico, " +
								  		" to_char (ehre.fecha_grabacion,'DD/MM/YYYY')  || '  --  ' || to_char(ehre.hora_grabacion, 'HH:MI') as fecha_hora_grab, " +	 
								  		" to_char (ehre.fecha_registro,'DD/MM/YYYY')  || '  --  ' || to_char(ehre.hora_registro, 'HH:MI') as fecha_hora, " + 
								  		" docr.codigo_histo_enfer as codenf, docr.otro_cual AS codigo, docr.descripcion as tipodescripcion, " +   
								  		" docr.presenta as presenta, docr.descripcion as detalledescripcion, null as totro_otro_cual, " +	 
								  		" null as totro_presenta, null as totro_descripcion,'' as controlespecial  " +	 
								  " FROM registro_enfermeria re " +  
								  " INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.registro_enfer = re.codigo ) " +
								  " INNER JOIN detalle_otro_cuidado_renf docr ON ( ehre.codigo = docr.codigo_histo_enfer ) " + 
								  " INNER JOIN tipo_otro_cuidado_enf toce ON ( docr.otro_cual = toce.codigo ) " +
								  " " + cue + " " +
								  " AND ehre.fecha_registro between '"+fechaInicial+"' and '"+fechaFinal+"'" +
								  "	) " +			   	   
			"	) tabla order by fecha_hora DESC ");
		return SqlBaseOrdenMedicaDao.consultarCuidadosEnfHisto(con, codigoCuenta, cuentaAsocio, institucion, centroCosto,fechaInicial,fechaFinal, consultaBuffer);
	}	
	/**
	 * Metodo para consultar el tipo de monitoreo para una orden médica
	 * @param con
	 * @param codigoOrden
   * @return Collection con los datos del tipo de monitoreo
	 */
	public Collection cargarTipoMonitoreo(Connection con, int codigoOrden)
	{
		return SqlBaseOrdenMedicaDao.cargarTipoMonitoreo(con, codigoOrden);
	}
	
	 /**
	 * Metodo para consultar y cargar la información en la sección soporte respiratorio 
	 * @param con
	 * @param codigoCuenta
	 * @param asocio
	 * @param institucion
	 * @param centroCosto
	 * @return Collection con los datos del soporte respiratorio
	 */
	public Collection cargarSoporteRespiratorio(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto)
	{
		return SqlBaseOrdenMedicaDao.cargarSoporteRespiratorio(con, codigoCuenta, asocio, institucion, centroCosto);
	}
	
	/**
	 * Metodo para consultar y cargar la información de la dieta 
	 * @param con
	 * @param codigoCuenta
	 * @return Collection con los datos de la última dieta
	 */
	public Collection cargarDieta(Connection con, int codigoCuenta)
	{
		return SqlBaseOrdenMedicaDao.cargarDieta(con, codigoCuenta);
	}
	
	/**
	 * Metodo para consultar y cargar la información de la nutrición oral
	 * @param con
	 * @param codigoCuenta
	 * @param asocio
	 * @param institucion
	 * @param centroCosto
	 * @return Collection con los datos de la última nutrición oral
	 */
	public Collection cargarNutricionOral(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto)
	{
		return SqlBaseOrdenMedicaDao.cargarNutricionOral(con, codigoCuenta, asocio, institucion, centroCosto);
	}
	
	/**
	 * Metodo para insertar otra nutrición oral
	 * @param con
	 * @param otroNutOral
	  * @return
	 */
	public int insertarOtroNutricionOral(Connection con, String otroNutOral)
	{
		String secuencia="nextval('seq_otro_nutricion_oral') as codigo";
		return SqlBaseOrdenMedicaDao.insertarOtroNutricionOral (con, secuencia, otroNutOral );
	}
	
	/**
	 * Metodo para insertar el detalle de otro nutrición oral
	 * @param con
	 * @param codigoDieta
	 * @param codigoOtroOral
	  * @return
	 **/
	public int insertarDetalleOtroNutriOral(Connection con, int codigoDieta, int codigoOtroOral)
	{
		return SqlBaseOrdenMedicaDao.insertarDetalleOtroNutriOral(con, codigoDieta, codigoOtroOral);
	}
	
	/**
 	 * Funcion que retorna una collecion con el listado de los otros tipos de nutrición oral a una cuenta específica
 	 * @param con
 	 * @param cuenta
 	 * @param cuentaAsocio
 	 * @return Collection 
 	 */
	  public Collection consultarOtrosNutricionOral(Connection con, int cuenta, int cuentaAsocio)
	  {
	  	return SqlBaseOrdenMedicaDao.consultarOtrosNutricionOral(con, cuenta, cuentaAsocio);
	  }
	
	  /**
	   * Metodo para consultar y cargar la información de otros tipos de nutrición oral ingresados
	   * @param con
	   * @param codigoCuenta
	   * @return Collection con los datos de los últimos tipos de nutrición oral ingresados
	   */
	  public Collection cargarOtroNutricionOral(Connection con, int codigoCuenta)
	  {
	  	return SqlBaseOrdenMedicaDao.cargarOtroNutricionOral(con, codigoCuenta);
	  }
	  
	  /**
	   * Metodo para cargar la información ingresada en la cuenta de asocio, referente a descripción del soporte, dieta
	   * y observaciones generales 
	   * @param con
	   * @param cuentaAsocio
	   * @return Collection con los datos de la cuenta de asocio
	   */
	  
	  public Collection cargarDatosUrgencias(Connection con, int cuentaAsocio)
	  {
	  	return SqlBaseOrdenMedicaDao.cargarDatosUrgencias (con, cuentaAsocio);
	  }
	  
	  /**
	 	 * Funcion que retorna una collecion con el listado de los otros tipos de cuidados de enfermería  a una(s) cuenta(s) específica
	 	 * @param con
	 	 * @param cuenta
	 	 * @param cuentaAsocio
	 	 * @return Collection 
	 	 */
		  public Collection consultarOtrosCuidadosEnfer(Connection con, int cuenta, int cuentaAsocio )
		  {
		  	return SqlBaseOrdenMedicaDao.consultarOtrosCuidadosEnfer(con, cuenta, cuentaAsocio);
		  }
		 
		  /**
	  	 *  Metodo para insertar otro tipo de cuidado de enfermería
	  	 * @param con
	  	 * @param otroCuidadoEnf
	  	  * @return
	  	 **/
	  	public int insertarOtroTipoCuidadoEnf(Connection con, String otroCuidadoEnf)
	  	{
	  		
	  		return SqlBaseOrdenMedicaDao.insertarOtroTipoCuidadoEnf(con, otroCuidadoEnf);
	  	}
	  	
	  	/**
		 * Metodo para cargar la información de la orden de la hoja
		 * neurológica
		 * @param con
		 * @param codigoOrden
		 * @return true si existe orden hoja neurológica
		 */
		public Collection cargarOrdenHojaNeurologica(Connection con, int codigoOrden)
		{
			return SqlBaseOrdenMedicaDao.cargarOrdenHojaNeurologica (con, codigoOrden);
		}
		
		/**
		 * Método que inserta o modifica la orden de la Hoja Neurológica
		 * @param con
		 * @param codigoOrden
		 * @param presenta
		 * @param observaciones
		 * @param finalizada
		 * @return
		 */
		public int insertarModificarOrdenHojaNeurologica(Connection con, int codigoOrden, boolean presenta, String observaciones, boolean finalizada, String fechaFin, String horaFin, String login)
		{
			return SqlBaseOrdenMedicaDao.insertarModificarOrdenHojaNeurologica (con, codigoOrden, presenta, observaciones, finalizada, fechaFin, horaFin, login);
		}
		
		/**
		 * Método que consulta las mezclas parenterales y los articulos con su correspondiente
		 * información de detalle
		 * @param con
		 * @param codigoCuenta
		 * @param codigoCuentaAsocio
		 * @param nroConsulta 
		 * @return
		 */
		public Collection consultarMezclasParenteral(Connection con, int codigoCuenta, int codigoCuentaAsocio, int nroConsulta)
		{
			return SqlBaseOrdenMedicaDao.consultarMezclasParenteral (con, codigoCuenta, codigoCuentaAsocio, nroConsulta);
		}
		
		  /**
		 * Método que consulta la información de los articulos asociados a la mezcla en el ver anteriores
		 * @param con
		 * @param codigoCuenta
		 * @param codigoCuentaAsocio
		 * @param codMezcla
		 * @param codEncaMin
		 * @param codEncabezadoAnterior
		 * @return
		 */
		public Collection consultarDetalleMezclaAnteriores(Connection con, int codigoCuenta, int codigoCuentaAsocio, int codMezcla, int codEncaMin, int codEncabezadoAnterior)
		{
			return SqlBaseOrdenMedicaDao.consultarDetalleMezclaAnteriores (con, codigoCuenta, codigoCuentaAsocio, codMezcla, codEncaMin, codEncabezadoAnterior);
		}

		public int finalizarParenteral(Connection con, Vector codHistoricosParent)
		{
			return SqlBaseOrdenMedicaDao.finalizarParenteral(con, codHistoricosParent);
		}

		/**
		 * 
		 * @param orden
		 * @param solicitud
		 * @return
		 */
		public HashMap consultarMezclaModificar(Connection con,String orden, String solicitud)
		{
			return SqlBaseOrdenMedicaDao.consultarMezclaModificar(con,orden,solicitud);
		}
		

		/**
		 * 
		 * @param con
		 * @param mezclaModificar
		 * @return
		 */
		public boolean guardarModificacionMezcla(Connection con, HashMap mezclaModificar, UsuarioBasico medico, PersonaBasica paciente) throws IPSException
		{
			return SqlBaseOrdenMedicaDao.guardarModificacionMezcla(con,mezclaModificar,medico,paciente);
		}
		

		/**
		 * 
		 * @param con
		 * @param mezcla
		 * @return
		 */
		public boolean accionAnularMezcla(Connection con, HashMap mezcla)
		{
			return SqlBaseOrdenMedicaDao.accionAnularMezcla(con,mezcla);
		}
		
		
		/**
		 * 
		 * @param con
		 * @param codigoCuenta
		 * @param codigoCuentaAsocio
		 * @param codigoInstitucionInt
		 * @param codigoArea
		 * @return
		 */
		public HashMap consultarFechasHistoCuidadosEspe(Connection con, int codigoCuenta, int codigoCuentaAsocio)
		{
			return SqlBaseOrdenMedicaDao.consultarFechasHistoCuidadosEspe(con,codigoCuenta,codigoCuentaAsocio);
		}

		/**
		 * Metodo para consultar el estado del Parametro Interfaz Nutricion
		 */
		public String consultarParametroInterfazNutricion(Connection con) 
		{
			return SqlBaseOrdenMedicaDao.consultarParametroInterfaz(con);
		}

		/**
		 * Metodo para consultar el Piso al que pertence una Cama
		 */
		public String consultarPisoCama(Connection con, int codigoCama) 
		{
			return SqlBaseOrdenMedicaDao.consultarPisoCama(con, codigoCama);
		}
		
		/**
		 * Metodo para consultar el Numero de la cama comun para la interfaz
		 */
		public String consultarNumeroCama(Connection con, int codigoCama) 
		{
			return SqlBaseOrdenMedicaDao.consultarNumeroCama(con, codigoCama);
		}
		
		/**
		 * Metodo para consultar la Fecha de la Orden Dieta
		 */
		public String consultarFechaDieta(Connection con, int codigoEncabezado) 
		{
			return SqlBaseOrdenMedicaDao.consultarFechaDieta(con, codigoEncabezado);
		}

		/**
		 *  Metodo para consultar la Hora de la Orden Dieta
		 */
		public String consultarHoraDieta(Connection con, int codigoEncabezado) 
		{
			return SqlBaseOrdenMedicaDao.consultarHoraDieta(con, codigoEncabezado);
		}

		
		/**
		 * Metodo para consultar la Fecha de Grabacion de la Dieta
		 */
		public String consultarFechaGrabacion(Connection con, int codigoEncabezado) 
		{
			return SqlBaseOrdenMedicaDao.consultarFechaGrabacion(con, codigoEncabezado);
		}

		/**
		 * Metodo para consultar la Hora de Grabacion de la Dieta
		 */
		public String consultarHoraGrabacion(Connection con, int codigoEncabezado) 
		{
			return SqlBaseOrdenMedicaDao.consultarHoraGrabacion(con, codigoEncabezado);
		}

		/**
		 * Metodo para Consultar el campo VIP del Convenio asociado al Ingreso del Paciente
		 */
		public String consultarConvenioVip(Connection con, int codigoConvenio) 
		{
			return SqlBaseOrdenMedicaDao.consultarConvenioVip(con, codigoConvenio);
		}

		/**
		 * Metodo para Consultar los tipos de dieta activos para la dieta actual del paciente
		 */
		public HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta) 
		{
			return SqlBaseOrdenMedicaDao.tiposNutricionOralActivo(con, codigoCuenta);
		}

		/**
		 * Metodo para Consultar la Descripcion de la Dieta del Paciente
		 */
		public String consultarDescripcionDieta(Connection con, int codigoCuenta) 
		{
			return SqlBaseOrdenMedicaDao.consultarDescripcionDieta(con, codigoCuenta);
		}
		
		/**
		 * Método para consultar el arreglo de un campo de la seccion prescripcion diálisis según tipo de consulta
		 * @param con
		 * @param campos
		 * @return
		 */
		public ArrayList<HashMap<String, Object>> cargarArregloPrescripcionDialisis(Connection con,HashMap campos)
		{
			return SqlBaseOrdenMedicaDao.cargarArregloPrescripcionDialisis(con, campos);
		}
		
		/**
		 * Método para insertar una prescripcion de diálisis
		 * @param con
		 * @param dialisis
		 * @return
		 */
		public int insertarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis)
		{
			return SqlBaseOrdenMedicaDao.insertarPrescripcionDialisis(con, dialisis);
		}
		
		/**
		 * Método implementado para cargar el histórico de prescripciones diálisis de un paciente
		 * @param con
		 * @param campos
		 * @return
		 */
		public ArrayList<DtoPrescripcionDialisis> getHistoricoPrescripcionDialisis(Connection con,HashMap campos)
		{
			return SqlBaseOrdenMedicaDao.getHistoricoPrescripcionDialisis(con, campos);
		}
		
		/**
		 * Método implementado para modificar una prescripción de dialisis
		 * @param con
		 * @param dialisis
		 * @return
		 */
		public int modificarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis)
		{
			return SqlBaseOrdenMedicaDao.modificarPrescripcionDialisis(con, dialisis);
		}

		/**
		 * 
		 */
		public String consultarDosificacionMezcla(Connection con, int mezcla,int codigoCuenta, int cuentaAsocio) 
		{
			return SqlBaseOrdenMedicaDao.consultarDosificacionMezcla(con, mezcla,codigoCuenta,cuentaAsocio);
		}
		
		/**
		 * Consulta las observaciones realizadas a las mezclas 
		 * @param Connection con
		 * @param HashMap parametros 
		 * */
		public HashMap obtenerObservacionesMezcla(Connection con, HashMap parametros)
		{
			return SqlBaseOrdenMedicaDao.obtenerObservacionesMezcla(con, parametros);
		}
		
		/**
		 * Actualiza las observaciones realizadas a las mezclas 
		 * @param Connection con
		 * @param HashMap parametros 
		 * */
		public boolean actualizarObservacionesMezcla(Connection con, HashMap parametros)
		{
			return SqlBaseOrdenMedicaDao.actualizarObservacionesMezcla(con, parametros);
		}
		
		/**
		 * Metodo encargado de suspender una mezcla, esta mezcla puede ser suspendida desde ordenes de medicamentos,
		 * para suspender una mezcla se debe poner el el campo finaliza_sol en true.
		 * @param connection
		 * @param supender
		 * @param codigoHistoEnca
		 * @return
		 */
		public boolean suspenderMezcla (Connection connection, String supender,int codigoHistoEnca,String usuario)
		{
			return SqlBaseOrdenMedicaDao.suspenderMezcla(connection, supender, codigoHistoEnca,usuario);
		}
		
		/**
		 * Metodo encargado de Finalizar una Mezcla, esto se hace desde el registro de enfermeria.
		 * Se actualiza el campo que se llama suspendido en true, y se llenan los campos de fecha, hora y usuario
		 * finaliza
		 * @param connection
		 * @param finalizar
		 * @param codigoHistoEnca
		 * @param usuario
		 * @return
		 */
		public boolean finalizarMezcla (Connection connection,String finalizar,int codigoHistoEnca,String usuario )
		{
			return SqlBaseOrdenMedicaDao.finalizarMezcla(connection, finalizar, codigoHistoEnca, usuario);
		}


		@Override
		public ArrayList<Object> cargarResultadoLaboratorios(
				Connection con, int ingresoPaciente, int cuentaPaciente, int centroCostoPaciente,
				boolean cargarParametrizacion,boolean esHistorico) {
			return SqlBaseOrdenMedicaDao.cargarResultadoLaboratorios(con,ingresoPaciente, cuentaPaciente, centroCostoPaciente, cargarParametrizacion,esHistorico);
		}

		@Override
		public int insertaResultadosLaboratorios(Connection con,
				int codigoEncabezado,
				ArrayList<Object> resultadoLaboratorios) {
			return SqlBaseOrdenMedicaDao.insertaResultadosLaboratorios(con,codigoEncabezado,resultadoLaboratorios);
		}
		
		/**
		 * @see com.princetonsa.dao.OrdenMedicaDao#cargarOtrosDietaReproteHC(java.sql.Connection, int)
		 */
		public  ArrayList<String> cargarOtrosDietaReproteHC(Connection con, int codigoCuenta){
			return  SqlBaseOrdenMedicaDao.cargarOtrosDietaReproteHC(con, codigoCuenta);
		}
		
		/**
		 * @see com.princetonsa.dao.OrdenMedicaDao#consultarObservacionesOrdenMedica(java.sql.Connection, int)
		 */
		public List<DtoObservacionesGeneralesOrdenesMedicas> consultarObservacionesOrdenMedica(Connection con, int codigoCuenta) throws SQLException
		{
			return SqlBaseOrdenMedicaDao.consultarObservacionesOrdenMedica(con, codigoCuenta);
		}
		
		/** (non-Javadoc)
		 * @see com.princetonsa.dao.OrdenMedicaDao#asociarDetalleObservacionEncabezado(java.sql.Connection, int, int)
		 */
		public void asociarDetalleObservacionEncabezado(Connection con,	int codigoPkDetalleObservacion, int codigoPkEncabezado)
				throws SQLException {
			SqlBaseOrdenMedicaDao.asociarDetalleObservacionEncabezado(con, codigoPkDetalleObservacion, codigoPkEncabezado);			
		}
		
		/** (non-Javadoc)
		 * @see com.princetonsa.dao.OrdenMedicaDao#actualizarDescripcionSoporteRespiratorio(java.sql.Connection, int, java.lang.String)
		 */
		@Override
		public boolean actualizarDescripcionSoporteRespiratorio(Connection con,	int codigoEncabezadoSoporteRespira,	String descripcionSoporteRespiratorio) {
			return SqlBaseOrdenMedicaDao.actualizarDescripcionSoporteRespiratorio(con, codigoEncabezadoSoporteRespira, descripcionSoporteRespiratorio);
		}

		/** (non-Javadoc)
		 * @see com.princetonsa.dao.OrdenMedicaDao#consultarDescripcionSoporteRespiratorio(java.sql.Connection, int)
		 */
		@Override
		public String consultarDescripcionSoporteRespiratorio(Connection con, int codigoCuenta) {
			return SqlBaseOrdenMedicaDao.consultarDescripcionSoporteRespiratorio(con, codigoCuenta);
		}
}
