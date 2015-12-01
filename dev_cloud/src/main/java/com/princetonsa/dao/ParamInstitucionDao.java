
/*
 * Creado   22/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.RangosConsecutivos;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;

/**
 * Clase para manejar
 *
 * @version 1.0, 22/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public interface ParamInstitucionDao 
{
    /**
	 * Inserta los datos de una institución
     * @param path
     * @param encabezado @todo
     * @param pie @todo
     * @param pieFacturaVaria 
     * @param encabezadoFacturaVaria 
     * @param rangoFinFacturaVaria 
     * @param rangoInicFacturaVaria 
     * @param prefijoFacturaVaria 
     * @param resolucionFacturaVaria 
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
	 * @see com.princetonsa.dao.sqlbase.SqlBaseParamInstitucion#insertar(java.sql.Connection, int,String,String,int,int,String,String,int,String,String,String,String,int,int)
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
															String pieFacturaVaria);
	
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
	public ResultSetDecorator consultaInstituciones (Connection con, int codigo, String codigoPais, String codigo_depto,String codigo_ciudad,boolean consultaUno);
	
	/**
	 * /**
	 * modifica los datos de una institución
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
	 * @param digv
	 * @param path
	 * @param encabezado
	 * @param pie
	 * @param piesHisCli
	 * @param pais
	 * @param logo
	 * @param tipoins
	 * @param codEmpTransEsp
	 * @param ubicacionLogo
	 * @param indicativo
	 * @param extension
	 * @param celular
	 * @param codigoInterfaz
	 * @param representanteLegal
	 * @param nivelLogo
	 * @param resolucionFacturaVaria
	 * @param prefijoFacturaVaria
	 * @param rangoInicFacturaVaria
	 * @param rangoFinFacturaVaria
	 * @param encabezadoFacturaVaria
	 * @param pieFacturaVari
	 * 
	 * @throws SQLException
	 * @see com.princetonsa.dao.sqlbase.SqlBaseParamInstitucion#modificar(java.sql.Connection, int,String,String,int,int,String,String,int,String,String,String,String,String,String)
	 * @return
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
																String piesHisCli,
																String pais,
																String logo,
																String tipoins,
																String codEmpTransEsp,
																String ubicacionLogo,
																String indicativo,
																String extension,
																String celular, 
																String codigoInterfaz,
																String representanteLegal ,
																String nivelLogo,
																String resolucionFacturaVaria, 
																String prefijoFacturaVaria, 
																BigDecimal rangoInicFacturaVaria, 
																BigDecimal rangoFinFacturaVaria, 
																String encabezadoFacturaVaria, 
																String pieFacturaVaria,
																String pieAmbMedicamentos) ;
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public String obtenerPrefijoFacturas(Connection con, int codigoInstitucion);
	
	/**
	 * Insertar los tipos de monedas por institucion
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarTiposMonedaInstitucion(Connection con,HashMap vo);
	
	/**
	 * Consultar los tipos de monedas por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarTiposMonedaInstitucion(Connection con,int institucion);
	
	/**
	 * Modificar los tipos de monedas por institucion
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarTiposMonedaInstitucion(Connection con,HashMap vo);
	
	/**
	 * Eliminar los tipos de monedas por institucion
	 * @param con
	 * @param tipoMoneda
	 * @return
	 */
	public boolean eliminarTiposMonedaInstitucion(Connection con,int tipoMoneda);
	
	/**
	 * Consultar los tipos de moneda por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerEmpresasInstitucion(Connection con,int institucion);
	
	
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
	public HashMap consultarEmpresas (Connection connection, HashMap criterios);
	
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
	public double insertarEmpresas (Connection connection, HashMap datos);
	
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
	public boolean modificarEmpresas (Connection connection,HashMap datos);
	
	
	/**
	 * Metodo que mustra si una empresa esta siendo usada 
	 * o no, para poderla eliminar.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esEmpresaUsada(Connection con, String codigo);
	
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
	public boolean eliminarEmpresas (Connection connection,HashMap datos);
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean esInstitucionPublica(Connection con, int institucion);

	/**
	 * Metodo que consulta el rango inicial y final de los consecutivos de facturacion
	 * @param centroAtencion
	 * @return
	 */
	public RangosConsecutivos obtenerRangosFacturacionXInstitucion(	int institucion);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoEmpresasInstitucion> listaInstitucionEmpresa(DtoEmpresasInstitucion dto);
	
	/**
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public Boolean obtenerNivelLogo(Connection con , Integer codigoInstitucion) throws SQLException;
	
}
