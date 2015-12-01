
/*
 * Creado   22/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.postgresql;

import java.math.BigDecimal;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.RangosConsecutivos;

import com.princetonsa.dao.ParamInstitucionDao;
import com.princetonsa.dao.sqlbase.SqlBaseParamInstitucionDao;

/**
 *
 *
 * @version 1.0, 22/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class PostgresqlParamInstitucionDao implements ParamInstitucionDao 
{
    /**
	 * Inserta los datos de una institución
     * @param path
	 * @param con, Connection con la fuente de datos
	 * @param codigo, Código de la institución
	 * @param nit, Nit de la institución
	 * @param razon, Razon Social de la institución
	 * @param depto, Departamento al que pertenece la ciudad
	 * @param ciudad, Ciudad de la institución
	 * @param direccion, Dirección de la institución
	 * @param telefono, Telefono de la institución
	 * @param codMinSalud,  Codigo del Ministerio de Salud
	 * @param actividadEco, Actividad Economica
	 * @param resolucion, Número de Resolución 
	 * @param prefijo, Prefijo de la factura
	 * @param rangoInic, Rango inicial de la factura
	 * @param rangoFin, Rango Final de la factura
	 * @return int, 0 no efectivo, >0 efectivo.
	 * @see com.princetonsa.dao.sqlbase.SqlBaseParamInstitucion#insertar(java.sql.Connection, int,String,String,int,int,String,String,int,String,String,int,String,int,int)
	 */
	public  int insertarInstitucion (Connection con,        int codigo,
													        String nit,
													        String razon,
													        String depto,
													        String ciudad,
													        String direccion,
													        String telefono,
													        String codMinSalud,
													        String actividadEco,
													        String resolucion,
													        String prefijo,
													        int rangoInic,
													        int rangoFin,
															String path, String encabezado, String pie, String pais,
															String logo,
															String tipoins,
															String codEmpTransEsp,
															String ubicacionLogo,
															String indicativo,
															String extension,
															String celular,
															String codigoInterfaz,
															String representanteLegal ,
															String niveLogo,
															String resolucionFacturaVaria, 
															String prefijoFacturaVaria, 
															BigDecimal rangoInicFacturaVaria, 
															BigDecimal rangoFinFacturaVaria, 
															String encabezadoFacturaVaria, 
															String pieFacturaVaria)
	{
	    return SqlBaseParamInstitucionDao.insertarInstitucion(con,
														            codigo, 
														            nit,
														            razon,
														            depto,
														            ciudad, 
														            direccion, 
														            telefono, 
														            codMinSalud, 
														            actividadEco, 
														            resolucion, 
														            prefijo, 
														            rangoInic, 
														            rangoFin,
																	path, encabezado, pie, pais,
																	logo,
																	tipoins,
																	codEmpTransEsp,
																	ubicacionLogo,
																	indicativo,
																	extension,
																	celular,
																	codigoInterfaz,
																	representanteLegal ,
																	niveLogo,
																	resolucionFacturaVaria, 
																	prefijoFacturaVaria, 
																	rangoInicFacturaVaria, 
																	rangoFinFacturaVaria, 
																	encabezadoFacturaVaria, 
																	pieFacturaVaria);
	}
	
	/**
	 * Metodo que realiza la consulta de uno ó varios registros de instituciones.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo de la institución.
	 * @param codigo_depto, Codigo del departamento.
	 * @param codigo_ciudad, Codigo de la ciudad
	 * @param consultaUno, Boolean indica que tipo de consulta se realiza.
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.sqlbase.SqlBaseParamInstitucion#consultaInstituciones(java.sql.Connection,int,int,int,boolean)
	 */
	public ResultSetDecorator consultaInstituciones (Connection con, 
													        int codigo,
													        String codigo_pais,
													        String codigo_depto,
													        String codigo_ciudad,
													        boolean consultaUno)
	{
	    return SqlBaseParamInstitucionDao.consultaInstituciones(con,
														            codigo,
														            codigo_pais,
														            codigo_depto,
														            codigo_ciudad,
														            consultaUno);
	}
	
	/**
	 * modifica los datos de una institución
	 * @param path
	 * @param con, Connection con la fuente de datos
	 * @param codigo, Código de la institución
	 * @param nit, Nit de la institución
	 * @param nombreNit, String con el nombre del tipo de identificacion
	 * @param razon, Razon Social de la institución
	 * @param depto, Departamento al que pertenece la ciudad
	 * @param ciudad, Ciudad de la institución
	 * @param direccion, Dirección de la institución
	 * @param telefono, Telefono de la institución
	 * @param codMinSalud,  Codigo del Ministerio de Salud
	 * @param actividadEco, Actividad Economica
	 * @param resolucion, Número de Resolución 
	 * @param prefijo, Prefijo de la factura
	 * @param rangoInic, Rango inicial de la factura
	 * @param rangoFin, Rango Final de la factura
	 * @return boolean, false no efectivo, true efectivo.
	 * @throws SQLException
	 * @see com.princetonsa.dao.sqlbase.SqlBaseParamInstitucion#modificar(java.sql.Connection, int,String,String,int,int,String,String,int,String,String,String,String,int,int)
	 */
	public boolean modificar (Connection con,int codigo,
														        String nit,
														        String digv,
														        String nombreNit,
														        String razon,
														        String depto,
														        String ciudad,
														        String direccion,
														        String telefono,
														        String codMinSalud,
														        String actividadEco,
														        String resolucion,
														        String prefijo,
														        int rangoInic,
														        int rangoFin,
																String path, 
																String encabezado, 
																String pie,
																String pieHisCli,
																String pais,
																String logo, 
																String tipoins,
																String codEmpTransEsp,
																String ubicacionLogo,
																String indicativo,
																String extension,
																String celular,
																String codigoInterfaz ,
																String representanteLegal ,
																String nivelLogo,
																String resolucionFacturaVaria, 
																String prefijoFacturaVaria, 
																BigDecimal rangoInicFacturaVaria, 
																BigDecimal rangoFinFacturaVaria, 
																String encabezadoFacturaVaria, 
																String pieFacturaVaria,
																String pieAmbMedicamentos)
	{
	    return SqlBaseParamInstitucionDao.modificar(con,
														            codigo, 
														            nit,
														            digv,
														            nombreNit,
														            razon,
														            depto,
														            ciudad, 
														            direccion, 
														            telefono, 
														            codMinSalud, 
														            actividadEco, 
														            resolucion, 
														            prefijo, 
														            rangoInic, 
														            rangoFin,
																	path, encabezado, pie, pieHisCli, pais,
																	logo,tipoins,codEmpTransEsp,
																	ubicacionLogo,
																	indicativo,
																	extension,
																	celular, 
																	codigoInterfaz,
																	representanteLegal ,
																	nivelLogo,
																	resolucionFacturaVaria, 
																	prefijoFacturaVaria, 
																	rangoInicFacturaVaria, 
																	rangoFinFacturaVaria, 
																	encabezadoFacturaVaria, 
																	pieFacturaVaria,
																	pieAmbMedicamentos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public String obtenerPrefijoFacturas(Connection con, int codigoInstitucion)
	{
		return SqlBaseParamInstitucionDao.obtenerPrefijoFacturas(con, codigoInstitucion);
	}
	
	/**
	 * Insertar tipos de monedas por institucion
	 */
	public boolean insertarTiposMonedaInstitucion(Connection con,HashMap vo)
	{
		return SqlBaseParamInstitucionDao.insertarTiposMonedaInstitucion(con, vo);
	}
	
	/**
	 * Consultar tipos de monedas por institucion
	 */
	public HashMap consultarTiposMonedaInstitucion(Connection con,int institucion)
	{
		return SqlBaseParamInstitucionDao.consultarTiposMonedaInstitucion(con, institucion);
	}
	
	/**
	 * Modificar tipos de monedas por institucion
	 */
	public boolean modificarTiposMonedaInstitucion(Connection con,HashMap vo)
	{
		return SqlBaseParamInstitucionDao.modificarTiposMonedaInstitucion(con, vo);
	}
	
	/**
	 * Eliminar los tipos de moneda por institucion
	 */
	public boolean eliminarTiposMonedaInstitucion(Connection con,int tipoMoneda)
	{
		return SqlBaseParamInstitucionDao.eliminarTiposMonedaInstitucion(con, tipoMoneda);
	}
	
	/**
	 * Consultar los tipos de moneda por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerEmpresasInstitucion(Connection con,int institucion)
	{
		return SqlBaseParamInstitucionDao.obtenerEmpresasInstitucion(con, institucion);
	}
	
	/**
	 * Metodo encargado de insertar Empresas En la BD
	 * @param connection
	 * @param datos
	 * ------------------------------------------
	 * 			KEY'S HASHMAP DATOS 
	 * ------------------------------------------
	 * -- nit2_ --> Requerido
	 * -- institucion3_ --> Requerido
	 * -- razonSocial4_ --> Requerido
	 * -- depto5_ --> Requerido
	 * -- ciudad6_ --> Requerido
	 * -- direccion7_ --> Requerido
	 * -- telefono8_ --> Requerido
	 * -- minSalud9_ --> Opcional
	 * -- actividad10_ --> Opcional
	 * -- resolucion11_ --> Opcional
	 * -- prefijo12_ --> Opcional
	 * -- rangoInicial13_ --> Opcional
	 * -- rangoFinal14_ --> Opcional
	 * -- tipoNit15_ --> Requerido
	 * -- logo17_ --> Requerido
	 * -- vigente18_ --> Requerido
	 * -- encabezado19_ --> Opcional
	 * -- pie20_ --> Opcional
	 * -- pais21_ --> Requerido
	 * -- usuarioModifica27_ --> Requerido
	 * @return
	 */
	public double insertarEmpresas (Connection connection, HashMap datos)
	{
		return SqlBaseParamInstitucionDao.insertarEmpresas(connection, datos);
	}
	
	/**
	 * Metodo encargado de consultar las empresas 
	 * pertenecientes a una institucion. 
	 * @param connection
	 * @param criterios
	 * --------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * --------------------------------------
	 * -- codigo1_
	 * -- institucion3_
	 * 
	 * @return HashMap mapa
	 * ----------------------------------
	 * 		KEY'S DEL HASHMAP MAPA
	 * ----------------------------------
	 * codigo1_, nit2_ , institucion3_ , razonSocial4_,
	 * depto5_, ciudad6_, direccion7_, telefono8_,
	 * minSalud9_, actividad10_, resolucion11_, prefijo12_,
	 * rangoInicial13_, rangoFinal14_, tipoNit15_, 
	 * nombreTipoNit16_, logo17_, vigente18_, encabezado19_,
	 * pie20_, pais21_, desTipoIdent22_, nombreDepto23_,
	 * nombreCiudad24_, nombrePais25_, estaBd26_
	 */
	public HashMap consultarEmpresas (Connection connection, HashMap criterios)
	{
		return SqlBaseParamInstitucionDao.consultarEmpresas(connection, criterios);
	}
	
	/**
	 * Metodo encargado de modificar los datos de la Empresas En la BD
	 * @param connection
	 * @param datos
	 * ------------------------------------------
	 * 			KEY'S HASHMAP DATOS 
	 * ------------------------------------------
	 * -- nit2_ --> Requerido
	 * -- razonSocial4_ --> Requerido
	 * -- depto5_ --> Requerido
	 * -- ciudad6_ --> Requerido
	 * -- direccion7_ --> Requerido
	 * -- telefono8_ --> Requerido
	 * -- minSalud9_ --> Opcional
	 * -- actividad10_ --> Opcional
	 * -- resolucion11_ --> Opcional
	 * -- prefijo12_ --> Opcional
	 * -- rangoInicial13_ --> Opcional
	 * -- rangoFinal14_ --> Opcional
	 * -- tipoNit15_ --> Requerido
	 * -- logo17_ --> Requerido
	 * -- vigente18_ --> Requerido
	 * -- encabezado19_ --> Opcional
	 * -- pie20_ --> Opcional
	 * -- pais21_ --> Requerido
	 * -- usuarioModifica27_ --> Requerido
	 * -- codigo1_ --> Requerido
	 * @return
	 */
	public boolean modificarEmpresas (Connection connection,HashMap datos)
	{
		return SqlBaseParamInstitucionDao.modificarEmpresas(connection, datos);
	}
	
	/**
	 * Metodo que mustra si una empresa esta siendo usada 
	 * o no, para poderla eliminar.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esEmpresaUsada(Connection con, String codigo)
	{
		return SqlBaseParamInstitucionDao.esEmpresaUsada(con, codigo);
	}
	

	/**
	 * Metodo encargado de eliminar
	 * una empresa
	 * @param connection
	 * @param datos
	 * -----------------------
	 *	 KEY'S HASHMAP DATOS
	 * -----------------------
	 * -- codigo1_
	 * @return
	 */
	public boolean eliminarEmpresas (Connection connection,HashMap datos)
	{
		return SqlBaseParamInstitucionDao.eliminarEmpresas(connection, datos);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean esInstitucionPublica(Connection con, int institucion)
	{
		return SqlBaseParamInstitucionDao.esInstitucionPublica(con, institucion);
	}
	
	@Override
	public RangosConsecutivos obtenerRangosFacturacionXInstitucion(int institucion) 
	{
		return SqlBaseParamInstitucionDao.obtenerRangosFacturacionXInstitucion(institucion);
	}

	@Override
	public ArrayList<DtoEmpresasInstitucion> listaInstitucionEmpresa(
			DtoEmpresasInstitucion dto) {
		
		return SqlBaseParamInstitucionDao.listaInstitucionEmpresa(dto);
	}
	
	/**
	 * @see com.princetonsa.dao.ParamInstitucionDao#obtenerNivelLogo(java.sql.Connection, java.lang.Integer)
	 */
	public Boolean obtenerNivelLogo(Connection con , Integer codigoInstitucion) throws SQLException
	{
		return SqlBaseParamInstitucionDao.obtenerNivelLogo(con, codigoInstitucion);
	}
}
