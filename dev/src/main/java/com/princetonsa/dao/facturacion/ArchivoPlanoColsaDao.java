package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosString;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseArchivosPlanosDao;
import com.princetonsa.dto.facturacion.DtoArchivoPlanoColsa;
/**
 * 
 * @author Jose Eduardo Arias Doncel
 * 
 * */
public interface ArchivoPlanoColsaDao 
{
	
	/**
	 * Consulta el ultimo Registro del Historial de Archivos Planos
	 * @param Connection con
	 * @param int institucion
	 * */
	public HashMap getUltimoHistorialArchivosPlanos(Connection con,int institucion);
	
	
	/**
	 * Insertar en Historial de Archivos Planos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean setHistorialArhivosPlanos(Connection con,HashMap parametros);		
	
	
	/**
	 * Consulta la ruta del ultimo Registro del Historial de Archivos Planos
	 * @param Connection con
	 * @param int institucion
	 * */
	public String getUltimaRutaHistorialArchivosPlanos(Connection con,int institucion);	
	
	/**
	 * Consulta la ruta del ultimo Registro del Historial de Archivos Planos
	 * @param Connection con
	 * @param int institucion
	 * */
	public ArrayList<HashMap<String,Object>> getConveniosArchivosPlanos(Connection con,int institucion);	
	
	
	/**
	 * Consulta las cuentas de cobro
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap getCuentasCobro(Connection con, HashMap parametros);
	
	
	/**
	 * Consulta las facturas para la generacion del archivo Plano Colsanitas
	 * @param Connection con 
	 * @param HashMap parametros
	 * @param Base de la Consulta, indica con que parametros se realizara la busqueda (1. a partir del numero de Envio, 
	 * 2. a partir del convenio, 
	 * 3. a partir de la cuenta de cobro)
	 * */
	public ArrayList<DtoArchivoPlanoColsa> getFacturasArchivoPlano(Connection con, HashMap parametros,int baseConsulta);
	
	
	/**
	 * Consulta datos basicos de los convenios ingresados en la paremtrica Archivo Planos Colsanitas
	 * @param Connection con
	 * @param int convenio
	 * @param int institucion
	 * */
	public InfoDatosString getConvenioBasicoParametrizacion(Connection con, int convenio,int institucion);
	
	
	/**
	 * Consulta datos basicos de las instituciones
	 * @param Connection con
	 * @param int institucion
	 * */
	public InfoDatosString getInfoBasicaInstitucion(Connection con,int institucion);	
}