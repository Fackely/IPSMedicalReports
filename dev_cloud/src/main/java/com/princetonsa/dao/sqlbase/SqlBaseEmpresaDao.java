/*
 * @(#)SqlBaseEmpresaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para una empresa
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseEmpresaDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseEmpresaDao.class);
	
	
	/**
	 * Sección select para cargar los datos de la empresa
	 */
	private final static String cargarEmpresaStr = "SELECT e.codigo AS codigo, " +
													"e.tercero AS tercero, " +
													"e.razon_social AS razonSocial," +
													"facturacion.getNombreContacto(e.codigo) AS nombreContacto," +
													"e.telefono AS telefono, " +
													"e.direccion AS direccion, " +
													"e.email AS correo, " +
													"e.activo AS activa, " +
													"coalesce(getdescripcionpais(e.pais_principal),'') as paisPrincipal, " +
													"coalesce(getnombreciudad(e.pais_principal,e.depto_principal,e.ciudad_principal),'') as ciudadPrincipal, " +
													"coalesce(getdescripcionpais(e.pais_cuentas),'') as paisCuentas, " +
													"coalesce(getnombreciudad(e.pais_cuentas, e.depto_cuentas, e.ciudad_cuentas),'') as ciudadCuentas, " +
													"e.pais_principal as codigoPaisPrincipal," +
													"e.depto_principal||'"+ConstantesBD.separadorSplit+"'|| e.ciudad_principal as codigoCiudadPrincipal," +
													"e.pais_cuentas as codigoPaisCuentas," +
													"e.depto_cuentas||'"+ConstantesBD.separadorSplit+"'|| e.ciudad_cuentas as codigoCiudadCuentas," +
													"coalesce(e.direccion_cuentas,'') as direccionCuentas, " +
													"coalesce(e.telefono_sucursal,'') as telefonoSucursal, " +
													"coalesce(e.nombre_representante,'') as representante, " +
													"coalesce(e.observaciones,'') as observaciones, " +
													"coalesce(e.direccion_sucursal,'') as direccionSucursal, " +
													"getnombredepto(e.pais_principal, e.depto_principal) as deptoPrincipal, " +
													"getnombredepto(e.pais_cuentas,e.depto_cuentas) as deptoCuentas," +
													"coalesce(e.fax_sede_principal,'') as faxsedeprincipal," +
													"coalesce(e.fax_sucursal_local,'') as faxsucursallocal," +
													"coalesce(e.direccion_territorial,'') as direccionterritorial, " +
													"coalesce(e.numero_afiliados||'', '') as numeroafiliados, " +
													"coalesce(e.nivel_ingreso, "+ConstantesBD.codigoNuncaValido+") as nivelingreso, " +
													"(SELECT n.descripcion from facturacion.niveles_ingreso n where n.codigo=e.nivel_ingreso) as nombrenivelingreso, "+ 
													"coalesce(e.forma_pago, "+ConstantesBD.codigoNuncaValido+") as formapago, " +
													"(select f.descripcion from tesoreria.formas_pago f where f.consecutivo=e.forma_pago) as nombreformapago "+
													" FROM empresas e ";
	
	
	private final static String cargarEmpresaMStr = "SELECT e.codigo AS codigo, " +
													"e.tercero AS tercero, " +
													"e.razon_social AS razonSocial," +
													"facturacion.getNombreContacto(e.codigo) AS nombreContacto," +
													"e.telefono AS telefono, " +
													"e.direccion AS direccion, " +
													"e.email AS correo, " +
													"e.activo AS activa, " +
													"e.pais_principal as paisPrincipal, " +
													"coalesce(e.ciudad_principal,'') as ciudadPrincipal, " +
													"e.pais_cuentas as paisCuentas, " +
													"coalesce(e.ciudad_cuentas,'') as ciudadCuentas, " +
													"coalesce(e.direccion_cuentas,'') as direccionCuentas, " +
													"coalesce(e.telefono_sucursal,'') as telefonoSucursal, " +
													"coalesce(e.nombre_representante,'') as representante, " +
													"coalesce(e.observaciones,'') as observaciones, " +
													"coalesce(e.direccion_sucursal,'') as direccionSucursal, " +
													"e.depto_principal as deptoPrincipal, " +
													"e.depto_cuentas as deptoCuentas, " +
													"coalesce(getdescripcionpais(e.pais_principal),'') as nombrepaisPrincipal, " +
													"coalesce(getnombreciudad(e.pais_principal,e.depto_principal,e.ciudad_principal),'') as nombreciudadPrincipal, " +
													"coalesce(getdescripcionpais(e.pais_cuentas),'') as nombrepaisCuentas, " +
													"coalesce(getnombreciudad(e.pais_cuentas,e.depto_cuentas,e.ciudad_cuentas),'') as nombreciudadCuentas, " +
													"coalesce(e.fax_sede_principal,'') as faxsedeprincipal," +
													"coalesce(e.fax_sucursal_local,'') as faxsucursallocal," +
													"coalesce(e.direccion_territorial,'') as direccionterritorial, " +
													"coalesce(e.numero_afiliados||'', '') as numeroafiliados, " +
													"coalesce(e.nivel_ingreso, "+ConstantesBD.codigoNuncaValido+") as nivelingreso, " +
													"(SELECT n.descripcion from facturacion.niveles_ingreso n where n.codigo=e.nivel_ingreso) as nombrenivelingreso, "+
													"coalesce(forma_pago, "+ConstantesBD.codigoNuncaValido+") as formapago, " +
													"(select f.descripcion from tesoreria.formas_pago f where f.consecutivo=e.forma_pago) as nombreformapago "+
													" FROM empresas e ";
	
	/**
	 * Carga los datos para mostrarlos en el resumen
	 */
	private final static String cargarDatosEmpresa= cargarEmpresaStr + " WHERE e.tercero= ?" ;
	
	/**
	 * Cargar los datos de la empresa por código Axioma
	 */
	private final static String cargarDatosEmpresaXCodigo = cargarEmpresaMStr + " WHERE e.codigo = ? ";
	
	/**
	 * Hace la modificación de los datos de la empresa
	 */
	private final static String modificarEmpresa=	"UPDATE " +
														"empresas SET " +
														"tercero= ?, " +//1
														"razon_social= ?, " +//2
														"telefono= ?, " +//3
														"direccion= ?, " +//4
														"email= ?, " +//5
														"activo= ?, " +//6
														"pais_principal= ?, " +//7
														"ciudad_principal= ?, " +//8
														"pais_cuentas= ?, " +//9
														"ciudad_cuentas= ?, " +//10
														"direccion_cuentas= ?, " +//11
														"telefono_sucursal= ?, " +//12
														"nombre_representante= ?, " +//13
														"observaciones= ?, " +//14
														"direccion_sucursal= ?, " +//15
														"depto_principal= ?, " +//16
														"depto_cuentas= ?, " +//17
														"fax_sede_principal = ?," + //18
														"fax_sucursal_local = ?," + //19
														"direccion_territorial = ?, " + //20
														"numero_afiliados=?, " +//21
														"nivel_ingreso=?, " +//22
														"forma_pago=? "+//23
													"WHERE " +
														"codigo = ?";//24
	
	/**
	 * Seleccionar todos los datos de empresa para mostrarlos en el listado
	 */
	private final static String consultarEmpresas= 	"SELECT e.codigo," +
																					" e.tercero as tercero, "+
																				  	" t.numero_identificacion as numeroidentificacion, " +
																				  	" t.descripcion as descripcion, "+
																				  	"e.razon_social as razonsocial, " +
																				  	"facturacion.getNombreContacto(e.codigo) as nombrecontacto, " +
																				  	"e.telefono as telefono, e.direccion as direccion, " +
																				  	"e.email as email, e.activo as activo, " +
																				  	"coalesce(getdescripcionpais(e.pais_principal),'') as paisPrincipal, " +
																				  	"coalesce(getnombreciudad(e.pais_principal,e.depto_principal,e.ciudad_principal), '') as ciudadPrincipal, " +
																				  	"e.pais_cuentas as paiscuentas, coalesce(e.ciudad_cuentas,'') as ciudadcuentas, " +
																				  	"coalesce(e.direccion_cuentas,'') as direccioncuentas, coalesce(e.telefono_sucursal,'') as telefonosucursal, " +
																				  	"coalesce(e.nombre_representante,'') as nombrerepresentante, coalesce(e.observaciones,'') as observaciones, " +
																				  	"coalesce(e.direccion_sucursal,'') as direccionsucursal, e.depto_principal as deptoprincipal, " +
																				  	"e.depto_cuentas as deptocuentas, " +
																				  	"coalesce(numero_afiliados||'', '') as numeroafiliados, " +
																				  	"coalesce(e.nivel_ingreso, "+ConstantesBD.codigoNuncaValido+") as nivelingreso, " +
																				  	"(SELECT n.descripcion from facturacion.niveles_ingreso n where n.codigo=e.nivel_ingreso) as nombrenivelingreso, "+
																					"coalesce(forma_pago, "+ConstantesBD.codigoNuncaValido+") as formapago, " +
																					"(select f.descripcion from tesoreria.formas_pago f where f.consecutivo=e.forma_pago) as nombreformapago, "+
																					"getnombredepto(e.pais_principal,e.depto_principal) as nombreDepto "+
																				  	"FROM empresas e, " +
																				  	"terceros t  " +
																				  	"WHERE " +
																				  	"e.tercero=t.codigo " +
																				  	"AND e.codigo<>0 " +
																				  	"AND t.institucion = ? " +
																				  	"ORDER BY " +
																				  	"e.razon_social";

	/**
	 *  Insertar una empresa
	 */
	private final static String insertarEmpresaStr = 	"INSERT INTO empresas " +
																				"(" +
																				"codigo, " +			//1
																				"tercero, " +			//2
																				"razon_social, " +		//3	
																				"telefono, " +			//4
																				"direccion," +			//5	
																				"email, " +				//6	
																				"activo, " +			//7
																				"pais_principal, " +	//8	
																				"ciudad_principal, " +	//9
																				"pais_cuentas, " +		//10
																				"ciudad_cuentas, " +	//11
																				"direccion_cuentas, " +	//12
																				"telefono_sucursal, " +	//13
																				"nombre_representante, " +//14
																				"observaciones, " +		//15	
																				"direccion_sucursal, " +//16
																				"depto_principal, " +	//17
																				"depto_cuentas, " +		//18
																				"fax_sede_principal," +	//19
																				"fax_sucursal_local," +	//20	
																				"direccion_territorial, " +//21
																				"numero_afiliados, " +	//22
																				"nivel_ingreso, " +		//23
																				"forma_pago " +			//24
																				") " +
																				"VALUES " +
																				"(" +
																					"?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? " +
																				")";
	
	/**
	 *  Insertar una empresa
	 */
	private final static String consultarExistenTercerosRelacionados = 	"SELECT count(*) " +
																	"telefono FROM terceros WHERE institucion=? " +
																	"AND codigo<>ALL(SELECT e.tercero FROM empresas e, " +
																	"terceros t WHERE e.tercero=t.codigo AND t.institucion=? ) " +
																	"AND codigo<>0 AND activo= "+ValoresPorDefecto.getValorTrueParaConsultas();
	
	
	
	/**
	 * Inserta una empresa
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @return 1 si encuentra, 0 de lo contrario
	 */
	public static int  insertar(	Connection con,
												int tercero,
												String razonSocial,
												//String nombreContacto,
												String telefono,
												String direccion,
												String correo,
												boolean activa,
												String direccionCuentas,
												String direccionSucursal,
												String telefonoSucursal,
												String representante,
												String observaciones,
												String paisPrincipal,
												String ciudadPrincipal,
												String paisCuentas,
												String ciudadCuentas,
												String deptoPrincipal,
												String deptoCuentas,
												String faxSedePrincipal,
												String faxSucursalLocal,
												String direccionTerritorial,
												String numeroAfiliados,
												double nivelIngreso,
												int formaPago
												)
	{
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					String[] ciuDepPrincipal=ciudadPrincipal.split(ConstantesBD.separadorSplit);
					String[] ciuDepCuentas=ciudadCuentas.split(ConstantesBD.separadorSplit);
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarEmpresaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					int codigoEmpresa= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_empresas");
					
					ps.setInt(1, codigoEmpresa);
					ps.setInt(2,tercero);
					ps.setString(3, razonSocial);
					ps.setString(4,telefono);
					ps.setString(5,direccion);
					ps.setString(6,correo);
					ps.setBoolean(7,activa);
					ps.setString(8, paisPrincipal);
					ps.setString(9, ciuDepPrincipal[1]);
					ps.setString(10, paisCuentas);
					ps.setString(11, ciuDepCuentas[1]);
					ps.setString(12, direccionCuentas);
					ps.setString(13, telefonoSucursal);
					ps.setString(14, representante);
					ps.setString(15, observaciones);
					ps.setString(16, direccionSucursal);
					ps.setString(17, ciuDepPrincipal[0]);
					ps.setString(18, ciuDepCuentas[0]);
					ps.setString(19, faxSedePrincipal);
					ps.setString(20, faxSucursalLocal);
					ps.setString(21, direccionTerritorial);
					
					if(Utilidades.convertirADouble(numeroAfiliados)>0)
					{
						ps.setDouble(22, Utilidades.convertirADouble(numeroAfiliados));
					}
					else
					{
						ps.setNull(22, Types.NUMERIC);
					}
					
					if(nivelIngreso>0)
					{
						ps.setDouble(23, nivelIngreso);
					}
					else
					{
						ps.setNull(23, Types.NUMERIC);
					}
					
					if(formaPago>0)
					{
						ps.setInt(24, formaPago);
					}
					else
					{
						ps.setNull(24, Types.INTEGER);
					}
					
					if(ps.executeUpdate()>0)
					{
						ps.close();
						return codigoEmpresa;
					}
			}
			catch(SQLException e)
			{
				logger.error(" Error en la inserción de datos: SqlBaseEmpresaDao "+e);
			}
			return ConstantesBD.codigoNuncaValido;
	}


	/**
	 * Inserta una empresa dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.EmpresaDao#insertarTransaccional(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String) 
	 */
	public static ResultadoBoolean insertarTransaccional(	Connection con,
																								int tercero,	
																								String razonSocial,
																								String telefono,
																								String direccion,
																								String correo,
																								boolean activa,
																								String direccionCuentas,
																								String direccionSucursal,
																								String telefonoSucursal,
																								String representante,
																								String observaciones,
																								String paisPrincipal,
																								String ciudadPrincipal,
																								String paisCuentas,
																								String ciudadCuentas,
																								String estado,
																								String deptoPrincipal,
																								String deptoCuentas,
																								String faxSedePrincipal,
																								String faxSucursalLocal,
																								String direccionTerritorial,
																								String numeroAfiliados,
																								double nivelIngreso,
																								int formaPago
																								) throws SQLException
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo iniciar la transacción");
		    }
		}
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{	
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarEmpresaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				int codigoEmpresa= UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_empresas");
				ps.setInt(1, codigoEmpresa);
				ps.setInt(2,tercero);
				ps.setString(3, razonSocial);
				ps.setString(4,telefono);
				ps.setString(5,direccion);
				ps.setString(6,correo);
				ps.setBoolean(7,activa);
				ps.setString(8, paisPrincipal);
				ps.setString(9, ciudadPrincipal);
				ps.setString(10, paisCuentas);
				ps.setString(11, ciudadCuentas);
				ps.setString(12, direccionCuentas);
				ps.setString(13, telefonoSucursal);
				ps.setString(14, representante);
				ps.setString(15, observaciones);
				ps.setString(16, direccionSucursal);
				ps.setString(17, deptoPrincipal);
				ps.setString(18, deptoCuentas);
				ps.setString(19, faxSedePrincipal);
				ps.setString(20, faxSucursalLocal);
				ps.setString(21, direccionTerritorial);
				
				if(Utilidades.convertirADouble(numeroAfiliados)>0)
				{
					ps.setDouble(22, Utilidades.convertirADouble(numeroAfiliados));
				}
				else
				{
					ps.setNull(22, Types.NUMERIC);
				}
				
				if(nivelIngreso>0)
				{
					ps.setDouble(23, nivelIngreso);
				}
				else
				{
					ps.setNull(23, Types.NUMERIC);
				}
				
				if(formaPago>0)
				{
					ps.setInt(24, formaPago);
				}
				else
				{
					ps.setNull(24, Types.INTEGER);
				}
				
				if(ps.executeUpdate()<=0)	
				{
					myFactory.abortTransaction(con);
					resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseEmpresaDao ");
					ps.close();
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseEmpresaDao");
			myFactory.abortTransaction(con);
			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseEmpresaDao: "+e);
			return resp;		
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);		
	}
	
	/**
	 * Método que  carga  los datos de una empresa según los datos
	 * que lleguen del nit o tercero para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator cargarResumen(Connection con, int tercero) throws SQLException
	{
			PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarResumenStatement.setInt(1, tercero);
			return new ResultSetDecorator(cargarResumenStatement.executeQuery());
	}

	/**
	 * Modifica una empresa dado su código con los paramétros dados.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo. int, código de la empresa
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.EmpresaDao#modificar(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean) 
	 */																					
	public static int modificar(	Connection con,	
													int codigo,
													int tercero,
													String razonSocial,
													String telefono,
													String direccion,
													String correo,
													String direccionCuentas,
													String direccionSucursal,
													String telefonoSucursal,
													String representante,
													String observaciones,
													String paisPrincipal,
													String ciudadPrincipal,
													String paisCuentas,
													String ciudadCuentas,
													String deptoPrincipal,
													String deptoCuentas,
													boolean activa,
													String codigoCiudadPrincipal,
													String codigoCiudadCuentas,
													String codigoPaisCuentas,
													String codigoPaisPrincipal,
													String faxSedePrincipal,
													String faxSucursalLocal,
													String direccionTerritorial,
													String numeroAfiliados,
													double nivelIngreso,
													int formaPago
													)
													
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				
				String[] ciuDepPrincipal=codigoCiudadPrincipal.split(ConstantesBD.separadorSplit);
				String[] ciuDepCuentas=codigoCiudadCuentas.split(ConstantesBD.separadorSplit);
				
				PreparedStatementDecorator ps=new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(modificarEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)));
				ps.setInt(1,tercero);
				ps.setString(2, razonSocial);
				ps.setString(3,telefono);
				ps.setString(4,direccion);
				ps.setString(5,correo);
				ps.setBoolean(6,activa);
				
				if(codigoPaisPrincipal==null || codigoPaisPrincipal.trim().equals(""))
					ps.setObject(7, null);
				else
					ps.setString(7, codigoPaisPrincipal);
				
				if(ciuDepPrincipal[1]==null || ciuDepPrincipal[1].trim().equals(""))
					ps.setObject(8, null);
				else
					ps.setString(8, ciuDepPrincipal[1]);
				
				if(codigoPaisCuentas==null || codigoPaisCuentas.trim().equals(""))
					ps.setObject(9, null);
				else
					ps.setString(9, codigoPaisCuentas);
				
				if(ciuDepCuentas[1]==null || ciuDepCuentas[1].trim().equals(""))
					ps.setObject(10, null);
				else
					ps.setString(10, ciuDepCuentas[1]);
				
				ps.setString(11, direccionCuentas);
				ps.setString(12, telefonoSucursal);
				ps.setString(13, representante);
				ps.setString(14, observaciones);
				ps.setString(15, direccionSucursal);
				
				if(ciuDepPrincipal[0]==null || ciuDepPrincipal[0].trim().equals(""))
					ps.setObject(16, null);
				else
					ps.setString(16, ciuDepPrincipal[0]);
				
				if(ciuDepCuentas[0]==null || ciuDepCuentas[0].trim().equals(""))
					ps.setObject(18, null);
				else
					ps.setString(17, ciuDepCuentas[0]);
							
				if(faxSedePrincipal.trim().equals(""))
					ps.setNull(18,Types.VARCHAR);
				else
					ps.setString(18,faxSedePrincipal);
				
				if(faxSucursalLocal.trim().equals(""))
					ps.setNull(19,Types.VARCHAR);
				else
					ps.setString(19,faxSucursalLocal);
				
				if(direccionTerritorial.trim().equals(""))
					ps.setNull(20,Types.VARCHAR);
				else
					ps.setString(20,direccionTerritorial);
				
				if(Utilidades.convertirADouble(numeroAfiliados)>0)
				{
					ps.setDouble(21, Utilidades.convertirADouble(numeroAfiliados));
				}
				else
				{
					ps.setNull(21, Types.NUMERIC);
				}
				
				if(nivelIngreso>0)
				{
					ps.setDouble(22, nivelIngreso);
				}
				else
				{
					ps.setNull(22, Types.NUMERIC);
				}
				
				if(formaPago>0)
				{
					ps.setInt(23, formaPago);
				}
				else
				{
					ps.setNull(23, Types.INTEGER);
				}
				
				ps.setInt(24,codigo);
				
				
				resp = ps.executeUpdate();
				logger.info("FAX SUCURSAL LOCA: "+faxSucursalLocal);
				logger.info("FAX SEDE PRINCIPAL: "+faxSedePrincipal);
				logger.info("DIRECCION TERRITORIAL: "+direccionTerritorial);
				logger.info("RESULTADO MODIFICACION: "+resp);
				ps.close();
				//logger.info("\n\nCODIGO PAIS CUENTA-->>"+codigoPaisCuentas);
				//logger.info("\n\nCODIGO CIUDAD CUENTA-->>"+ciuDepCuentas[1]);
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseEmpresaDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * Modifica una empresa dado su código con los paramétros dados  dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo. int, código de la empresa
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @param estado. String, estado dentro de la transacción 
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.EmpresaDao#modificarTransaccional(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	public static ResultadoBoolean modificarTransaccional(Connection con,	
																								int codigo,
																								int tercero,
																								String razonSocial,
																								String telefono,
																								String direccion,
																								String correo,
																								boolean activa,
																								String direccionCuentas,
																								String direccionSucursal,
																								String telefonoSucursal,
																								String representante,
																								String observaciones,
																								String paisPrincipal,
																								String ciudadPrincipal,
																								String paisCuentas,
																								String ciudadCuentas,
																								String deptoPrincipal,
																								String deptoCuentas,
																								String estado,
																								String faxSedePrincipal,
																								String faxSucursalLocal,
																								String direccionTerritorial,
																								String numeroAfiliados,
																								double nivelIngreso,
																								int formaPago) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resp=new ResultadoBoolean(false);
	
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo iniciar la transacción");
		    }
		}	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1,tercero);
			ps.setString(2, razonSocial);
			ps.setString(3,telefono);
			ps.setString(4,direccion);
			ps.setString(5,correo);
			ps.setBoolean(6,activa);
			ps.setString(7, paisPrincipal);
			ps.setString(8, ciudadPrincipal);
			ps.setString(9, paisCuentas);
			ps.setString(10, ciudadCuentas);
			ps.setString(11, direccionCuentas);
			ps.setString(12, telefonoSucursal);
			ps.setString(13, representante);
			ps.setString(14, observaciones);
			ps.setString(15, direccionSucursal);
			ps.setString(16, deptoPrincipal);
			ps.setString(17, paisCuentas);
			ps.setString(18, faxSedePrincipal);
			ps.setString(19, faxSucursalLocal);
			ps.setString(20, direccionTerritorial);
			
			
			
			
			
			if(Utilidades.convertirADouble(numeroAfiliados)>0)
			{
				ps.setDouble(21, Utilidades.convertirADouble(numeroAfiliados));
			}
			else
			{
				ps.setNull(21, Types.NUMERIC);
			}
			
			if(nivelIngreso>0)
			{
				ps.setDouble(22, nivelIngreso);
			}
			else
			{
				ps.setNull(22, Types.NUMERIC);
			}
			
			if(formaPago>0)
			{
				ps.setInt(23, formaPago);
			}
			else
			{
				ps.setNull(23, Types.INTEGER);
			}
			
			ps.setInt(24,codigo);
			

			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseEmpresaDao");
			myFactory.abortTransaction(con);
			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseEmpresaDao: "+e);
			return resp;
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);		
	}

	/**
	 * Método que contiene el Resulset de todas las empresas
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public static  ResultSetDecorator listado(Connection con, int codigoInstitucion) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarEmpresas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("consultarEmpresas-->"+consultarEmpresas+" ->"+codigoInstitucion);
			ps.setInt(1, codigoInstitucion);
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado empresas " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * Método que contiene el Resulset validando que todos los terceros se relacionen con las empresas
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public static Boolean consultarTercerosRelacionados(Connection con, int codigoInstitucion) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		PreparedStatementDecorator ps=null;
		Boolean resultado = null;
		
		if(con == null || con.isClosed()) {
			try	{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} catch(SQLException e)	{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				return resultado;
			}
		} else {
			try {
				ps = new PreparedStatementDecorator(con.prepareStatement(consultarExistenTercerosRelacionados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoInstitucion);
				ps.setInt(2, codigoInstitucion);
				respuesta = new ResultSetDecorator(ps.executeQuery());
				if(respuesta.next()) {
					resultado = (respuesta.getInt(1) > 0) ? true : false;
					Log4JManager.info("consultarTercerosRelacionados-->"+consultarExistenTercerosRelacionados+" ->"+codigoInstitucion + "\n Número resultados = " + resultado);
				}
			}
			catch(SQLException e) {
				logger.warn("Error en la consultarTercerosRelacionados " +e.toString());
				respuesta=null;
			} finally {
				if(ps != null)
					ps.close();
				if(respuesta != null)
					respuesta.close();
			}
		}
		return resultado;
	}

	/**
	 * Método que contiene el Resulset de todas las empresas buscadas
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busqueda(	Connection con,
												String nit,
												String descripcionTercero,	
												String razonSocial,
												String nombreContacto,
												String telefono,
												String direccion,
												String correo,
												int activaAux,
												int codigoInstitucion,
												String ciudadPrincipal,
												String numeroAfiliados,
												double nivelIngreso,
												int formaPago,
												String filtroContactos) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		String consultaArmada="";
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			consultaArmada=armarConsulta(nit,descripcionTercero, razonSocial,nombreContacto,telefono,direccion, correo,activaAux, codigoInstitucion, ciudadPrincipal, numeroAfiliados, nivelIngreso, formaPago, filtroContactos);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con,consultaArmada);
			logger.info("\n\nconsulta armada:::... "+ps);
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de la empresa " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 */
	private static String armarConsulta(	String nit,
											String descripcionTercero,			
											String razonSocial,
											String nombreContacto,
											String telefono,
											String direccion,
											String correo,
											int activaAux,
											int codigoInstitucion, 
											String ciudadPrincipal,
											String numeroAfiliados,
											double nivelIngreso,
											int formaPago,
											String filtroContactos)
	{
		String consulta=	"SELECT  e.codigo as codigo, getnombredepto(e.pais_principal,e.depto_principal) AS nombredepto, t.numero_identificacion as numeroidentificacion, " +
									"t.descripcion as descripcion, e.razon_social as razonsocial, "+filtroContactos+" as nombrecontacto, " +
									"e.telefono as telefono, e.direccion as direccion, e.email as email, e.activo as activo, getnombreciudad(e.pais_principal,e.depto_principal,e.ciudad_principal) as ciudadprincipal, t.codigo AS tercero  FROM empresas e " +
									"INNER JOIN terceros t ON (e.tercero=t.codigo) " +
									" WHERE t.institucion="+codigoInstitucion+" ";

		if(nit!=null && !nit.equals(""))
		{
			consulta= consulta + " AND  UPPER(t.numero_identificacion) LIKE UPPER('%" +nit+"%') ";
		}
		if	(descripcionTercero!=null && !descripcionTercero.equals(""))								
		{
			consulta= consulta + " AND UPPER(t.descripcion) LIKE UPPER('%" +descripcionTercero+ "%') " ;	 
		}
		if(razonSocial!=null && !razonSocial.equals(""))
		{
		 	consulta= consulta +" AND UPPER(e.razon_social) LIKE UPPER('%" +razonSocial+ "%') ";
		}
		if(telefono!=null && !telefono.equals(""))
		{
			consulta= consulta +" AND UPPER(e.telefono) LIKE UPPER('%" +telefono+ "%') ";
		}
		if(direccion!=null && !direccion.equals(""))
		{
			consulta= consulta +" AND UPPER(e.direccion) LIKE UPPER('%" +direccion+ "%') ";
		}
		if(correo!=null && !correo.equals(""))
		{
			consulta= consulta +" AND UPPER(e.email) LIKE UPPER('%" +correo+ "%') ";
		}
		if(activaAux==1)
		{
			consulta= consulta +" AND e.activo= "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		}	
		if(activaAux==2)
		{
			consulta=  consulta +" AND e.activo= "+ValoresPorDefecto.getValorFalseCortoParaConsultas()+" ";
		}
		if(ciudadPrincipal!=null && !ciudadPrincipal.equals(""))
		{
			consulta= consulta +" AND UPPER(e.ciudad_principal) LIKE UPPER('%" +ciudadPrincipal+ "%') ";
		}
		if(Utilidades.convertirADouble(numeroAfiliados)>0)
		{
			consulta+=" AND e.numero_afiliados= "+numeroAfiliados+" ";
		}
		if(nivelIngreso>0)
		{
			consulta+=" AND e.nivel_ingreso = "+nivelIngreso+" ";
		}
		if(formaPago > 0)
		{
			consulta+=" AND e.forma_pago= "+formaPago+" ";
		}
		
		
		consulta = consulta + " AND e.codigo<>0  ORDER BY e.razon_social";
					
		return consulta;
	}		
	
	/**
	 * Método implementado para cargar los datos de una empresa dado su codigo Axioma
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargar(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDatosEmpresaXCodigo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargar de SqlBaseEmpresaDao: "+e);
			return null;
		}
	}
	
	/**
	 * 
	 */
	public static boolean terceroExisteComoEmpresa(Connection con, int tercero)
	{
		boolean existe=false;
		String consulta="SELECT codigo FROM facturasvarias.deudores WHERE codigo_tercero=?";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,tercero);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if (rs.next())
				existe=true;
			
			pst.close();
			
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargar de terceroExisteComoEmpresa: "+e);
			existe=false;
		}
		
		return existe;
	}
	
	/**
	 * 
	 */
	public static boolean actualizarADeudorEmpresaDeudorTercero(Connection con,int empresa, int tercero)
	{
		boolean existe=false;
		String consulta="UPDATE facturasvarias.deudores SET codigo_empresa=?,es_empresa=?,tipo=? WHERE codigo_tercero=?";
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,empresa);
			pst.setString(2,ConstantesBD.acronimoSi);
			pst.setString(3, ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa);
			pst.setInt(4,tercero);
			
			if (pst.executeUpdate()>0)
				existe=true;
			
			pst.close();
				

		}
		catch(SQLException e)
		{
			logger.error("Error en cargar de actualizarADeudorEmpresaDeudorTercero: "+e);
			existe=false;
		}
		
		return existe;
	}
	
}
