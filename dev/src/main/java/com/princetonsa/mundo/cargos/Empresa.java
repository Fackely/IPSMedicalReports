/*
 * @(#)Empresa.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EmpresaDao;

/**
 * Clase para el manejo de una empresa.
 *
 * @version 1.0, Abril 29, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class Empresa
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Empresa.class);
	
	/**
	 * Código axioma de la empresa
	 */
	private int codigo;
	
	/**
	 * Nit de la empresa 
	 */
	private int tercero;
	
	/**
	 * entero que se utiliza para la modificación del nit de una empresa
	 */
	private int terceroNuevo;
	
	/**
	 * Razón social de la empresa
	 */
	private String razonSocial;
	
	/**
	 * Nombre del contacto de la empresa
	 */
	private String nombreContacto;
	
	/**
	 * Telefono de la empresa
	 */
	private String telefono;

	/**
	 * Dirección de la empresa
	 */
	private String direccion;

	/**
	 * Correo electronico del contacto de la empresa
	 */
	private String correo;
	
	/**
	 * Dice si la empresa está activa en el sistema o no.
	 */
	private boolean activa;
	
	/**
	 * Nit (terceros)
	 */
	private String nit;
	
	/**
	 * Direccion de radicacion de cuentas
	 */
	private String direccionCuentas;
	
	/**
	 * Direccion de Sucursal local
	 */
	private String direccionSucursal;
	
	/**
	 * Telefono de Sucursal local
	 */
	private String telefonoSucursal;
	
	/**
	 * Nombre del representante legal
	 */
	private String representante;
	
	/**
	 * Observaciones sobre la empresa
	 */
	private String observaciones;
	
	/**
	 * Piis sede principal empresa
	 */
	private String paisPrincipal;
	
	/**
	 * Ciudad sede principal empresa
	 */
	private String ciudadPrincipal;
	
	/**
	 * Pais de radicacion cuentas
	 */
	private String paisCuentas;
	
	/**
	 * Ciudad radicacion cuentas
	 */
	private String ciudadCuentas;
	
	/**
	 * 
	 */
	private String codigoPaisPrincipal;
	
	/**
	 * 
	 */
	private String codigoCiudadPrincipal;
	
	/**
	 * 
	 */
	private String codigoPaisCuentas;
	
	/**
	 * 
	 */
	private String codigoCiudadCuentas;
	
	
	private String deptoPrincipal;
	
	private String deptoCuentas;
	
	/**
	 * Contiene el nombre del tercero 
	 */
	private String descripcionTercero;
	
	/**
	 * Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	private int activaAux;
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private EmpresaDao empresaDao;
	
	
	/**
	 * 
	 * */
	private String faxSedePrincipal;
	
	/**
	 * 
	 * */
	private String faxSucursalLocal;
	
	/**
	 * 
	 * */
	private String direccionTerritorial;
	
	/**
	 * 
	 */
	private String numeroAfiliados;
	
	/**
	 * 
	 */
	private InfoDatosDouble nivelIngreso;
	
	/**
	 * 
	 */
	private InfoDatosInt formaPago;
	
	/**
	 * 
	 */
	private String direccionTercero;
	
	/**
	 * 
	 */
	public String telefonoTercero;
	
	
	/**
	 * Creadora de la clase, inicializa en vacio todos los parámetros
	 */
	public Empresa()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Creadora de la clase, inicializa los atributos con los datos correspondientes.
	 * @param codigo. int, código de la empresa
	 * @param nombreArchivoLogs. String, nombre relativo del archivo de logs
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 */
	public Empresa(	int codigo,
					int tercero,
					String razonSocial,
					String nombreContacto,
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
					InfoDatosDouble nivelIngreso,
					InfoDatosInt formaPago)
	{
		this.codigo=codigo;
		this.tercero=tercero;
		this.razonSocial=razonSocial;
		this.nombreContacto=nombreContacto;
		this.telefono=telefono;
		this.direccion=direccion;
		this.correo=correo;
		this.activa=activa;
		this.direccionCuentas=direccionCuentas;
		this.direccionSucursal=direccionSucursal;
		this.telefonoSucursal=telefonoSucursal;
		this.representante=representante;
		this.observaciones=observaciones;
		this.paisPrincipal=paisPrincipal;
		this.ciudadPrincipal=ciudadPrincipal;
		this.paisCuentas=paisCuentas;
		this.ciudadCuentas=ciudadCuentas;
		this.deptoPrincipal=deptoPrincipal;
		this.deptoCuentas=deptoCuentas;
		this.faxSedePrincipal = faxSedePrincipal;
		this.faxSucursalLocal = faxSucursalLocal;
		this.direccionTerritorial = direccionTerritorial;
		this.numeroAfiliados=numeroAfiliados;
		this.nivelIngreso= nivelIngreso;
		this.formaPago= formaPago;
		this.init(System.getProperty("TIPOBD"));			
	}
	

	/**
	 * Método para insertar una empresa 
	 * @param con una conexion abierta con una fuente de datos
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public int insertarEmpresa(Connection con) throws SQLException 
	{
		int  resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		if (empresaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (EmpresaDao - insertarEmpresa )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		//Tarea 147674
		boolean terceroExisteComoEmpresa=empresaDao.terceroExisteComoEmpresa(con,this.tercero);
		logger.info("EL TERCERO ASOCIADO A LA EMPRESA EXISTE EN DEUDORES?--->"+terceroExisteComoEmpresa);

		resp1=empresaDao.insertar(con, this.tercero, this.razonSocial, this.telefono, this.direccion, this.correo, this.activa, this.direccionCuentas, this.direccionSucursal, this.telefonoSucursal, this.representante, this.observaciones,this.paisPrincipal, this.ciudadPrincipal, this.paisCuentas, this.ciudadCuentas, this.deptoPrincipal, this.deptoCuentas, this.faxSedePrincipal,this.faxSucursalLocal, this.direccionTerritorial, this.numeroAfiliados, this.nivelIngreso.getCodigo(), this.formaPago.getCodigo());
		
		//Si el tercero asociado a la empresa si existe como deudor, lo actualizo en la tabla deudores como empresa
		if (terceroExisteComoEmpresa)
			empresaDao.actualizarADeudorEmpresaDeudorTercero(con,resp1,this.tercero);

		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}

	/**
	 * Método para cargar los datos pertinentes al resumen
	 * @param con conexión
	 * @param tercero nit de la empresa
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarResumen(Connection con, int tercero) throws SQLException
	{
		ResultSetDecorator rs=empresaDao.cargarResumen(con,tercero);
		
		if (rs.next())
		{
			this.activa= rs.getBoolean("activa");
			this.correo=rs.getString("correo")+"";
			this.direccion=rs.getString("direccion")+"";
			this.nombreContacto=rs.getString("nombreContacto")+"";
			this.razonSocial= rs.getString("razonSocial")+"";
			this.telefono= rs.getString("telefono")+"";
			this.tercero= rs.getInt("tercero");
			this.codigo=rs.getInt("codigo");
			this.direccionCuentas=rs.getString("direccionCuentas")+"";
			this.direccionSucursal=rs.getString("direccionSucursal")+"";
			this.telefonoSucursal=rs.getString("telefonoSucursal")+"";
			this.representante=rs.getString("representante")+"";
			this.observaciones=rs.getString("observaciones")+"";
			this.paisPrincipal=rs.getString("paisPrincipal")+"";
			this.ciudadPrincipal=rs.getString("ciudadPrincipal")+"";
			this.paisCuentas=rs.getString("paisCuentas")+"";
			this.ciudadCuentas=rs.getString("ciudadCuentas")+"";
			this.deptoPrincipal=rs.getString("deptoPrincipal")+"";
			this.deptoCuentas=rs.getString("deptoCuentas")+"";
			this.codigoPaisPrincipal=rs.getString("codigoPaisPrincipal")+"";
			this.codigoCiudadPrincipal=rs.getString("codigoCiudadPrincipal")+"";
			this.codigoPaisCuentas=rs.getString("codigoPaisCuentas")+"";
			this.codigoCiudadCuentas=rs.getString("codigoCiudadCuentas")+"";
			this.faxSedePrincipal = rs.getString("faxSedePrincipal")+"";
			this.faxSucursalLocal = rs.getString("faxSucursalLocal")+"";
			this.direccionTerritorial = rs.getString("direccionTerritorial")+"";
			this.faxSedePrincipal = rs.getString("faxsedeprincipal")+"";
			this.faxSucursalLocal = rs.getString("faxsucursallocal")+"";
			this.direccionTerritorial = rs.getString("direccionterritorial")+"";
		
			this.numeroAfiliados= rs.getString("numeroafiliados");
			this.nivelIngreso= new InfoDatosDouble(rs.getDouble("nivelingreso"), rs.getString("nombrenivelingreso"));
			this.formaPago= new InfoDatosInt(rs.getInt("formapago"), rs.getString("nombreformapago"));
			rs.close();
			
			return true;
		}
		else
			return false;
	}
	
	
	/**
	 * Método implementado para cargar los datos de una empresa dado su codigo Axioma
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean cargar(Connection con,int codigo)
	{
		HashMap datos=empresaDao.cargar(con,codigo);
		
		if (Integer.parseInt(datos.get("numRegistros")+"")>0)
		{
			this.activa= UtilidadTexto.getBoolean(datos.get("activa")+"");
			this.correo=datos.get("correo") + "";
			this.direccion=datos.get("direccion") + "";
			this.nombreContacto=datos.get("nombrecontacto") + "";
			this.razonSocial= datos.get("razonsocial") + "";
			this.telefono= datos.get("telefono") + "";
			this.tercero= Integer.parseInt(datos.get("tercero")+"");
			this.codigo=Integer.parseInt(datos.get("codigo")+"");
			this.direccionCuentas=datos.get("direccioncuentas") + "";
			this.direccionSucursal=datos.get("direccionsucursal") + "";
			this.telefonoSucursal=datos.get("telefonosucursal") + "";
			this.representante=datos.get("representante") + "";
			this.observaciones=datos.get("observaciones") + "";
			this.codigoPaisPrincipal=datos.get("paisprincipal") + "";
			this.codigoCiudadPrincipal=datos.get("ciudadprincipal") + "";
			this.codigoPaisCuentas=datos.get("paiscuentas") + "";
			this.codigoCiudadCuentas=datos.get("ciudadcuentas") + "";
			this.deptoPrincipal=datos.get("deptoprincipal") + "";
			this.deptoCuentas=datos.get("deptocuentas") + "";
			this.paisPrincipal=datos.get("nombrepaisprincipal") + "";
			this.ciudadPrincipal=datos.get("nombreciudadprincipal") + "";
			this.paisCuentas=datos.get("nombrepaiscuentas") + "";
			this.ciudadCuentas=datos.get("nombreciudadcuentas") + "";
			this.faxSedePrincipal = datos.get("faxsedeprincipal")+"";
			this.faxSucursalLocal = datos.get("faxsucursallocal")+"";
			this.direccionTerritorial = datos.get("direccionterritorial")+"";
			
			this.numeroAfiliados= datos.get("numeroafiliados")+"";
			this.nivelIngreso= new InfoDatosDouble( Utilidades.convertirADouble(datos.get("nivelingreso")+""), datos.get("nombrenivelingreso")+"");
			this.formaPago= new InfoDatosInt( Utilidades.convertirAEntero(datos.get("formapago")+""), datos.get("nombreformapago")+"");
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Método utilizado en la funcionalidad Modificar empresa
	 * que tiene como finalidad modificar el estado activo, el nit,
	 * la razón social, nombreContacto, tel, dir y email.
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarEmpresa(Connection con) throws SQLException 
	{
		int resp=0;
		
		//Tarea 147674
		boolean terceroExisteComoEmpresa=empresaDao.terceroExisteComoEmpresa(con,this.tercero);
		logger.info("EL TERCERO ASOCIADO A LA EMPRESA EXISTE EN DEUDORES?--->"+terceroExisteComoEmpresa);
		
		resp=empresaDao.modificar(con,this.getCodigo(), this.getTercero(),this.getRazonSocial(),this.getTelefono(),this.getDireccion(),this.getCorreo(),this.getDireccionCuentas(),this.getDireccionSucursal(),this.getTelefonoSucursal(),this.getRepresentante(),this.getObservaciones(),this.getPaisPrincipal(),this.getCiudadPrincipal(),this.getPaisCuentas(),this.getCiudadCuentas(),this.getDeptoPrincipal(),this.getDeptoCuentas(),this.getActiva(),this.getCodigoCiudadPrincipal(),this.getCodigoCiudadCuentas(),this.codigoPaisCuentas,this.codigoPaisPrincipal,this.faxSedePrincipal,this.faxSucursalLocal,this.direccionTerritorial, this.numeroAfiliados, this.nivelIngreso.getCodigo(), this.formaPago.getCodigo());
		
		//Si el tercero asociado a la empresa si existe como deudor, lo actualizo en la tabla deudores como empresa
		if (terceroExisteComoEmpresa)
			empresaDao.actualizarADeudorEmpresaDeudorTercero(con,resp,this.tercero);
		
		return resp;
	}
				
	/**
	 * Actualiza los datos del objeto en una fuente de datos, reutilizando una conexión existente,
	 * con la información presente en los atributos del objeto.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return número de filas insertadas (1 o 0)
	 */
	public int modificarEmpresaTransaccional (Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
		    myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Empresa - modificarEmpresaTransaccional ) no esta especificado");
		}
		if (empresaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (empresaDao - modificarEmpresalTransaccional )");
		}
			
		//Iniciamos la transacción, si el estado es empezar
			
		boolean inicioTrans;
			
		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
			
		resp1=empresaDao.modificar(con,this.getCodigo(), this.getTercero(),this.getRazonSocial(),this.getTelefono(),this.getDireccion(),this.getCorreo(),this.getDireccionCuentas(),this.getDireccionSucursal(),this.getTelefonoSucursal(),this.getRepresentante(),this.getObservaciones(),this.getPaisPrincipal(),this.getCiudadPrincipal(),this.getPaisCuentas(),this.getCiudadCuentas(),this.getDeptoPrincipal(),this.getDeptoCuentas(),this.getActiva(),this.getCodigoCiudadPrincipal(),this.getCodigoCiudadCuentas(),this.getCodigoPaisCuentas(),this.getCodigoPaisPrincipal(),this.faxSedePrincipal,this.faxSucursalLocal,this.direccionTerritorial, this.numeroAfiliados, this.nivelIngreso.getCodigo(), this.formaPago.getCodigo());
			
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
			    myFactory.endTransaction(con);
			}
		}
		return resp1;
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public void init(String tipoBD)
	{
		if( empresaDao == null)
		{
			if(tipoBD==null)
			{
				logger.error("No esta llegando el tipo de base de datos");
				System.exit(1);
			}
			else
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				if (myFactory != null)
				{
					empresaDao = myFactory.getEmpresaDao();
				}					
			}
		}
	}
	
	/**
	 * resetea los datos pertinentes al registro de empresa
	 */
	public void reset()
	{
		this.codigo = 0;
		this.tercero = 0;
		this.razonSocial = "";
		this.nombreContacto = "";
		this.telefono = "";
		this.direccion = "";
		this.correo = "";
		this.activa = false;
		this.direccionCuentas="";
		this.direccionSucursal="";
		this.telefonoSucursal="";
		this.representante="";
		this.observaciones="";
		this.paisPrincipal="";
		this.ciudadPrincipal="";
		this.paisCuentas="";
		this.ciudadCuentas="";
		this.terceroNuevo = 0;
		this.nit="";
		this.descripcionTercero="";
		this.deptoPrincipal="";
		this.deptoCuentas="";
		this.activaAux=0;
		this.codigoPaisPrincipal="";
		this.codigoCiudadPrincipal="";
		this.codigoPaisCuentas="";
		this.codigoCiudadCuentas="";
		this.faxSedePrincipal = "";
		this.faxSucursalLocal = "";
		this.direccionTerritorial = "";
		this.numeroAfiliados= "";
		this.nivelIngreso= new InfoDatosDouble();
		this.formaPago= new InfoDatosInt();
		this.telefonoTercero="";
		this.direccionTercero="";
	}
	
	/**
	 * Método que obtiene todos los resultados de las empresas
	 * ingresadas para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoEmpresa(Connection con,  int codigoInstitucion)
	{
		EmpresaDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmpresaDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(consulta.listado(con, codigoInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo empresa " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * Método que contiene el Resulset validando que todos los terceros se relacionen con las empresas
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public Boolean consultarTercerosRelacionados(Connection con,  int codigoInstitucion)
	{
		EmpresaDao consulta = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmpresaDao();
		Boolean resultado = null;
		try	{
			resultado = consulta.consultarTercerosRelacionados(con, codigoInstitucion);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo empresa " +e.toString());
		}
		return resultado;
	}
	
	/**
	 * Método que contiene los resultados de la búsqueda de empresas,
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @return
	 */
	public Collection resultadoBusquedaAvanzada(Connection con, int codigoInstitucion)
	{
		EmpresaDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmpresaDao();
		Collection coleccion=null;
		try
		{	
			if(consulta.busqueda(con, this.nit, this.descripcionTercero, this.tercero, this.razonSocial, this.nombreContacto,
												this.telefono, this.direccion, this.correo, this.activaAux, codigoInstitucion, this.ciudadPrincipal,  this.numeroAfiliados, this.nivelIngreso.getCodigo(), this.formaPago.getCodigo()) == null )
				return null;
			else	
				coleccion=UtilidadBD.resultSet2Collection(consulta.busqueda(con, this.nit, this.descripcionTercero, this.tercero, this.razonSocial, this.nombreContacto, this.telefono, this.direccion, this.correo, this.activaAux, codigoInstitucion, this.ciudadPrincipal, this.numeroAfiliados, this.nivelIngreso.getCodigo(), this.formaPago.getCodigo()));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo empresa " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}

	
	/**
	 * Retorna el código axioma de la empresa
	 * @return
	 */
	public int getCodigo() 
	{
		return codigo;
	}

	/**
	 * Asigna el código axioma de la empresa
	 * @param codigo
	 */
	public void setCodigo(int codigo) 
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna la razón social de la empresa
	 * @return
	 */
	public String getRazonSocial()
	{
		return razonSocial;
	}

	/**
	 * Asigna la razón social de la empresa
	 * @param string
	 */
	public void setRazonSocial(String razonSocial)
	{
		this.razonSocial = razonSocial;
	}

	/**
	 * Retorna el nombre del contacto de la empresa
	 * @return
	 */
	public String getNombreContacto()
	{
		return nombreContacto;
	}

	/**
	 * Asigna el nombre del contacto de la empresa
	 * @param string
	 */
	public void setNombreContacto(String nombreContacto)
	{
		this.nombreContacto = nombreContacto;
	}

	/**
	 * Retorna el telefono de la empresa
	 * @return
	 */
	public String getTelefono()
	{
		return telefono;
	}

	/**
	 * Asigna el telefono de la empresa
	 * @param string
	 */
	public void setTelefono(String telefono)
	{
		this.telefono = telefono;
	}

	/**
	 * Retorna la dirección de la empresa
	 * @return
	 */
	public String getDireccion()
	{
		return direccion;
	}

	/**
	 * Asigna la dirección de la empresa
	 * @param string
	 */
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}

	/**
	 * Retorna el correo electronico del contacto de la empresa
	 * @return
	 */
	public String getCorreo()
	{
		return correo;
	}

	/**
	 * Asigna el correo electronico del contacto de la empresa
	 * @param string
	 */
	public void setCorreo(String correo)
	{
		this.correo = correo;
	}

	/**
	 * Retorna si la empresa está activa en el sistema o no
	 * @return
	 */
	public boolean getActiva()
	{
		return activa;
	}

	/**
	 * Asigna si la empresa está activa en el sistema o no
	 * @param b
	 */
	public void setActiva(boolean activa)
	{
		this.activa = activa;
	}

	/**
	 * Retorna el nit de la empresa
	 * @return
	 */
	public int getTercero() {
		return tercero;
	}

	/**
	 * Asigna el nit de la empresa
	 * @param i
	 */
	public void setTercero(int i) {
		tercero = i;
	}

	/**
	 * Retorna el nit de la empresa que se desea actualizar
	 * para modificar éste campo de la empresa
	 * @return
	 */
	public int getTerceroNuevo() {
		return terceroNuevo;
	}

	/**
	 * Asigna el nit nuevo
	 * para modificar éste campo de la empresa
	 * @param i
	 */
	public void setTerceroNuevo(int i) {
		terceroNuevo = i;
	}

	/**
	 * Retorna el nombre del tercero
	 * @return
	 */
	public String getDescripcionTercero() {
		return descripcionTercero;
	}

	/**
	 * Retorna el nit (tercero)
	 * @return
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * Asigna el nombre del tercero
	 * @param string
	 */
	public void setDescripcionTercero(String string) {
		descripcionTercero = string;
	}

	/**
	 * Asigna el nit (tercero) 
	 * @param string
	 */
	public void setNit(String string) {
		nit = string;
	}

	/**
	 * Retorna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public int getActivaAux() {
		return activaAux;
	}

	/**
	 * Asigna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public void setActivaAux(int i) {
		activaAux = i;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCiudadCuentas() {
		return ciudadCuentas;
	}
	
	/**
	 * 
	 * @param ciudadCuentas
	 */
	public void setCiudadCuentas(String ciudadCuentas) {
		this.ciudadCuentas = ciudadCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCiudadPrincipal() {
		return ciudadPrincipal;
	}
	
	/**
	 * 
	 * @param ciudadPrincipal
	 */
	public void setCiudadPrincipal(String ciudadPrincipal) {
		this.ciudadPrincipal = ciudadPrincipal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDireccionCuentas() {
		return direccionCuentas;
	}
	
	/**
	 * 
	 * @param direccionCuentas
	 */
	public void setDireccionCuentas(String direccionCuentas) {
		this.direccionCuentas = direccionCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDireccionSucursal() {
		return direccionSucursal;
	}
	
	/**
	 * 
	 * @param direccionSucursal
	 */
	public void setDireccionSucursal(String direccionSucursal) {
		this.direccionSucursal = direccionSucursal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}
	
	/**
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisCuentas() {
		return paisCuentas;
	}
	
	/**
	 * 
	 * @param paisCuentas
	 */
	public void setPaisCuentas(String paisCuentas) {
		this.paisCuentas = paisCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisPrincipal() {
		return paisPrincipal;
	}
	
	/**
	 * 
	 * @param paisPrincipal
	 */
	public void setPaisPrincipal(String paisPrincipal) {
		this.paisPrincipal = paisPrincipal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRepresentante() {
		return representante;
	}
	
	/**
	 * 
	 * @param representante
	 */
	public void setRepresentante(String representante) {
		this.representante = representante;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTelefonoSucursal() {
		return telefonoSucursal;
	}
	
	/**
	 * 
	 * @param telefonoSucursal
	 */
	public void setTelefonoSucursal(String telefonoSucursal) {
		this.telefonoSucursal = telefonoSucursal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDeptoCuentas() {
		return deptoCuentas;
	}
	
	/**
	 * 
	 * @param deptoCuentas
	 */
	public void setDeptoCuentas(String deptoCuentas) {
		this.deptoCuentas = deptoCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDeptoPrincipal() {
		return deptoPrincipal;
	}
	
	/**
	 * 
	 * @param deptoPrincipal
	 */
	public void setDeptoPrincipal(String deptoPrincipal) {
		this.deptoPrincipal = deptoPrincipal;
	}

	/**
	 * @return the codigoCiudadCuentas
	 */
	public String getCodigoCiudadCuentas() {
		return codigoCiudadCuentas;
	}

	/**
	 * @param codigoCiudadCuentas the codigoCiudadCuentas to set
	 */
	public void setCodigoCiudadCuentas(String codigoCiudadCuentas) {
		this.codigoCiudadCuentas = codigoCiudadCuentas;
	}

	/**
	 * @return the codigoCiudadPrincipal
	 */
	public String getCodigoCiudadPrincipal() {
		return codigoCiudadPrincipal;
	}

	/**
	 * @param codigoCiudadPrincipal the codigoCiudadPrincipal to set
	 */
	public void setCodigoCiudadPrincipal(String codigoCiudadPrincipal) {
		this.codigoCiudadPrincipal = codigoCiudadPrincipal;
	}

	/**
	 * @return the codigoPaisCuentas
	 */
	public String getCodigoPaisCuentas() {
		return codigoPaisCuentas;
	}

	/**
	 * @param codigoPaisCuentas the codigoPaisCuentas to set
	 */
	public void setCodigoPaisCuentas(String codigoPaisCuentas) {
		this.codigoPaisCuentas = codigoPaisCuentas;
	}

	/**
	 * @return the codigoPaisPrincipal
	 */
	public String getCodigoPaisPrincipal() {
		return codigoPaisPrincipal;
	}

	/**
	 * @param codigoPaisPrincipal the codigoPaisPrincipal to set
	 */
	public void setCodigoPaisPrincipal(String codigoPaisPrincipal) {
		this.codigoPaisPrincipal = codigoPaisPrincipal;
	}

	public String getFaxSedePrincipal() {
		return faxSedePrincipal;
	}

	public void setFaxSedePrincipal(String faxSedePrincipal) {
		this.faxSedePrincipal = faxSedePrincipal;
	}

	public String getFaxSucursalLocal() {
		return faxSucursalLocal;
	}

	public void setFaxSucursalLocal(String faxSucursalLocal) {
		this.faxSucursalLocal = faxSucursalLocal;
	}

	public String getDireccionTerritorial() {
		return direccionTerritorial;
	}

	public void setDireccionTerritorial(String direccionTerritorial) {
		this.direccionTerritorial = direccionTerritorial;
	}

	/**
	 * @return the numeroAfiliados
	 */
	public String getNumeroAfiliados() {
		return numeroAfiliados;
	}

	/**
	 * @param numeroAfiliados the numeroAfiliados to set
	 */
	public void setNumeroAfiliados(String numeroAfiliados) {
		this.numeroAfiliados = numeroAfiliados;
	}

	/**
	 * @return the nivelIngreso
	 */
	public InfoDatosDouble getNivelIngreso() {
		return nivelIngreso;
	}

	/**
	 * @param nivelIngreso the nivelIngreso to set
	 */
	public void setNivelIngreso(InfoDatosDouble nivelIngreso) {
		this.nivelIngreso = nivelIngreso;
	}

	/**
	 * @return the formaPago
	 */
	public InfoDatosInt getFormaPago() {
		return formaPago;
	}

	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(InfoDatosInt formaPago) {
		this.formaPago = formaPago;
	}

	public String getDireccionTercero() {
		return direccionTercero;
	}

	public void setDireccionTercero(String direccionTercero) {
		this.direccionTercero = direccionTercero;
	}

	public String getTelefonoTercero() {
		return telefonoTercero;
	}

	public void setTelefonoTercero(String telefonoTercero) {
		this.telefonoTercero = telefonoTercero;
	}

	
		

}
