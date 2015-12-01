/**
 * 
 */
package util.interfaces;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.ResultadoDouble;
import util.ResultadoInteger;
import util.ResultadoString;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.salas.UtilidadesSalas;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoAsociosDetalleFactura;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturacion.DtoPaquetizacionDetalleFactura;
import com.princetonsa.dto.interfaz.DtoInterfazAbonos;
import com.princetonsa.dto.interfaz.DtoInterfazAxRips;
import com.princetonsa.dto.interfaz.DtoInterfazConsumosXFacturar;
import com.princetonsa.dto.interfaz.DtoInterfazDetalleFactura;
import com.princetonsa.dto.interfaz.DtoInterfazFactura;
import com.princetonsa.dto.interfaz.DtoInterfazNutricion;
import com.princetonsa.dto.interfaz.DtoInterfazPaciente;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.parametrizacion.Servicios;
import com.princetonsa.mundo.solicitudes.Solicitud;


/**
 * 
 * Clase para manejar los temas relacionados con la interfazTesoreia
 * No tiene desarrollo de DAO por que no se sabe a que BD se conectara.
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class UtilidadBDInterfaz 
{
	

	/**
	 * Varible de la fuente de  Datos
	 */
	private DataSource fuenteDatos = null;
	
	/**
	 * Variable para manejar conexiones a jndi variable
	 */
	private static DataSource fuenteDatosAuxiliar=null;
	
	/**
	 * Log4j para el manejo de errores
	 */
	private static Logger logger=Logger.getLogger(UtilidadBDInterfaz.class);

	/**
	 * Constructor de la clase, inicializa el pool de conexiones
	 *
	 */
	public UtilidadBDInterfaz() 
	{
		try
        {
            // recuperamos el contexto inicial y la referencia a la fuente de datos
            Context ctx = new InitialContext();
            fuenteDatos = (DataSource) ctx.lookup("java:comp/env/jdbc/interfazTesoreria");
        }
        catch (Exception e)
        {
           logger.warn("ERROR INCIALIZANDO EL POOL DE LA INTERFAZ");
        }
	}
	
	/**
	 * 
	 * Metodo que toma una conexion a la funte de datos de la interfaz, desde el pool
	 * @return
	 */
	private Connection abrirConexionInterfaz ()
	{
		Connection con=null;
		if(fuenteDatos!=null)
		{
			try 
			{
				con = fuenteDatos.getConnection();
			} catch (SQLException e) {
				logger.info("\n Problema abriendo conexión con la interfaz."
						+ e.getStackTrace());
			}
		}
		return con;
	}
	
	/**
	 * 
	 * @param jndi
	 * @return
	 */
	private static Connection abrirConexionInterfaz(String jndi)
	{
		Connection con=null;
		
		BasicDataSource fuenteDatos = new BasicDataSource();
		fuenteDatos.setDriverClassName("com.ibm.as400.access.AS400JDBCDriver");
		fuenteDatos.setUrl("jdbc:as400://193.1.1.88/FACTLIBR");
		fuenteDatos.setUsername("AS400");
		fuenteDatos.setPassword("AS400");

		fuenteDatos.setRemoveAbandoned(true);
		fuenteDatos.setRemoveAbandonedTimeout(30);
		fuenteDatos.setMaxActive(20);
		fuenteDatos.setMaxIdle(10);
		fuenteDatos.setMaxWait(5000);
		
		try 
		{
			synchronized (fuenteDatos) {
			con=fuenteDatos.getConnection();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		try {
			logger.info("CONEXION ABIERTA?????->"+!con.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;
		
		/*
		logger.info("1");
		try
        {
			logger.info("2");
            // recuperamos el contexto inicial y la referencia a la fuente de datos
            Context ctx = new InitialContext();
            logger.info("3");
            fuenteDatosAuxiliar = (DataSource) ctx.lookup("java:comp/env/jdbc/"+jndi);
            logger.info("4");
        }
        catch (Exception e)
        {
           logger.error("ERROR INCIALIZANDO EL POOL DE LA INTERFAZ");
           return null;
        }
        logger.info("5");
        Connection con=null;
        logger.info("6");
		if(fuenteDatosAuxiliar!=null)
		{
			logger.info("7");
			try 
			{
				logger.info("8");
				con = fuenteDatosAuxiliar.getConnection(); 
				logger.info("9");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			logger.info("10");
		}
		logger.info("11");
		return con;*/
	}
	
	/**
	 * Metodo que cierra la conexion a la funte de datos de la interfaz
	 * @param con
	 */
	private static void cerrarConexion(Connection con) 
	{
		try
		{
			if(con!=null&&!con.isClosed())
			{
				con.close();
			}
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR CERRANDO LA CONEXION");
			
		}
	}

	/**
	 * 
	 * @param loginUsuario 
	 * @return
	 */
	public static ResultadoString obtenerConsecutivoFacturas(int institucion, String loginUsuario, boolean incrementarConsecutivo) 
	{
		logger.warn("*******************OBTENER CONSECUTO INTERFACE***********************************");
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		Connection con = null;
		try
		{
//			Connection con=abrirConexionInterfaz("consecutivos");
			con = UtilidadBD.abrirConexion();
			if(con==null || con.isClosed())
			{
				return new ResultadoString("","Proceso cancelado, no se pudo leer-actualizar la tabla de interfaz no hay conexi�n.");
			}
			
			String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaInterfazConsecutivos,institucion);
			if(!nombreTabla.trim().equals(""))
			{
				//se queda en ciclo tratando de tomar el consecutivo hasta que salga bien, o tenga 10 intentos.
				int intentos=0;
				while(intentos<10)
				{
					String resultado="";
					String cadena="select valor from "+nombreTabla+" WHERE nit=10";
					
					
					pst = con.prepareStatement(cadena);
					rs= pst.executeQuery();
					if(rs.next())
					{
						resultado=rs.getObject(1)+"";
						if(!UtilidadTexto.isEmpty(resultado))
						{
							if(incrementarConsecutivo)
							{	
								cadena="update "+nombreTabla+" set valor=valor+1 where nit=10 and valor="+resultado;
								pst2 = con.prepareStatement(cadena);
								if(pst2.executeUpdate()>0)
								{
									//generar log
									UtilidadesFacturacion.insertarLogAdconsta(con, loginUsuario, resultado);
									return new ResultadoString(resultado,"");
								}
							}
							else
							{
								return new ResultadoString(resultado,"");
							}
						}
					}
					intentos++;
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerConsecutivoFacturas",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerConsecutivoFacturas", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
				UtilidadBD.closeConnection(con);
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return new ResultadoString("","Proceso cancelado, no se pudo leer-actualizar la tabla de interfaz no hay conexi�n.");
	}
	
	/**
	 * Metodo para insertar un registro en la tabla de pacientes de la interfaz
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean insertarPaciente(DtoInterfazPaciente dto)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaPacientes,dto.getInstitucion());
		logger.info("[INTERFAZ] NOMBRE TABLA QUE SE VA  LLENAR "+nombreTabla);
		if(!nombreTabla.trim().equals(""))
		{
			//String cadenaIsercion="INSERT INTO "+nombreTabla+" (codigo,tdoc,ndoc,pnom,snom,pape,sape,ingreso,esting,fechapac,horapac,fechan,estreg) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			//campos adicionados  numhc,codconv,nomconv,tercero
			String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
			String fechaNacimiento=UtilidadFecha.conversionFormatoFechaABD(dto.getFechaNacimiento()).replace("-", "");
			String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz no hay conexi�n.");
				}
				logger.info("[INTERFAZ] ALISTANDO EL STATEMENT");
				String numeroId=agregarCeros(dto.getNumeroIdentificacion().trim(), 13)+dto.getNumeroIdentificacion().trim();
				String pNombre=dto.getPrimerNombre().length()>20?dto.getPrimerNombre().substring(0,20):dto.getPrimerNombre();
				String sNombre=dto.getSegundoNombre().length()>20?dto.getSegundoNombre().substring(0, 20):dto.getSegundoNombre();
				String pApellido=dto.getPrimerApellido().length()>20?dto.getPrimerApellido().substring(0, 20):dto.getPrimerApellido();
				String sApellido=dto.getSegundoApellido().length()>20?dto.getSegundoApellido().substring(0, 20):dto.getSegundoApellido();
				String codInterfazConv=Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(dto.getCodconv().trim()));
				String nomConvenio=dto.getNomconv().length()>30?dto.getNomconv().substring(0,30):dto.getNomconv();
				String tercero=agregarCeros(dto.getTercero().trim(), 13)+dto.getTercero().trim();
				String cadenaIsercion="INSERT INTO "+nombreTabla+" (" +
															" codigo," +
															" tdoc," +
															" ndoc," +
															" pnom," +
															" snom," +
															" pape," +
															" sape," +
															" ingreso," +
															" esting," +
															" fechapac," +
															" horapac," +
															" fechan," +
															" estreg," +
															" numhc," +
															" codconv," +
															" nomconv," +
															" tercero," +
															" viaing," +
															" sexo," +
															" usuario" +
															") values (" +
															dto.getCodigo()+"," +//DECIMAL
															"'"+dto.getTipoIdentificiacion()+"'," +	//CHAR
															"'"+numeroId+"'," +//CHAR
															"'"+pNombre+"'," +//CHAR
															"'"+sNombre+"'," +//CHAR
															"'"+pApellido+"'," +//CHAR
															"'"+sApellido+"'," +//CHAR
															dto.getIngreso()+"," +//DECIMAL
															"'"+dto.getEstadoIngreso()+"'," +//CHAR
															fecha+"," +//DECIMAL
															hora+"," +//DECIMAL
															fechaNacimiento+"," +//DECIMAL
															dto.getEstadoRegistro()+"," +//DECIMAL
															dto.getNumhc()+"," +//DECIMAL
															"'"+codInterfazConv+"'," +//CHAR
															"'"+nomConvenio+"'," +//CHAR
															"'"+tercero+"'," +//CHAR
															dto.getViaIngreso()+"," +//DECIMAL
															"'"+dto.getSexo()+"'," +//CHAR
															"'"+dto.getUsuario()+"')";//CHAR
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
				//ps.setDouble(1, Double.parseDouble(dto.getCodigo()));
				//ps.setString(2, dto.getTipoIdentificiacion());
				//ps.setString(3, agregarCeros(dto.getNumeroIdentificacion().trim(), 13)+dto.getNumeroIdentificacion().trim());
				//ps.setString(4, dto.getPrimerNombre().length()>20?dto.getPrimerNombre().substring(0,20):dto.getPrimerNombre());
				//ps.setString(5, dto.getSegundoNombre().length()>20?dto.getSegundoNombre().substring(0, 20):dto.getSegundoNombre());
				//ps.setString(6, dto.getPrimerApellido().length()>20?dto.getPrimerApellido().substring(0, 20):dto.getPrimerApellido());
				//ps.setString(7, dto.getSegundoApellido().length()>20?dto.getSegundoApellido().substring(0, 20):dto.getSegundoApellido());
				//ps.setDouble(8, Double.parseDouble(dto.getIngreso()));
				//ps.setString(9, dto.getEstadoIngreso());
				//ps.setInt(10, Utilidades.convertirAEntero(fecha));
				//ps.setInt(11, Integer.parseInt(hora));
				//ps.setInt(12, Utilidades.convertirAEntero(fechaNacimiento));
				//ps.setString(13, dto.getEstadoRegistro());
				//ps.setInt(14, dto.getNumhc());
				//ps.setString(15, Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(dto.getCodconv().trim())));
				//ps.setString(16, dto.getNomconv().length()>30?dto.getNomconv().substring(0,30):dto.getNomconv());
				//ps.setString(17, agregarCeros(dto.getTercero().trim(), 13)+dto.getTercero().trim());
				//ps.setString(18, dto.getViaIngreso());
				//ps.setString(19, dto.getSexo());
				//ps.setString(20, dto.getUsuario());
				logger.info("insercion interfaz -->"+cadenaIsercion);
				
				if(ps.executeUpdate()>0)
				{
					ejecutarProcesoInterfazPaciente(con);
					ps.close();
					logger.info("[INTERFAZ] ¡EXITO INSERCION! ");
					cerrarConexion(con);
					return new ResultadoBoolean(true,"El registro se inserto correctamente");
				}
				logger.error("[INTERFAZ] El registro no se pudo Insertar");
				ps.close();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"El registro no se pudo Insertar");
			} 
			catch (Exception e) 
			{
				logger.error("[INTERFAZ] error en interfaz_: "+e);
				e.printStackTrace();
				cerrarConexion(con);
				return new ResultadoBoolean(
						false,
						"Proceso cancelado, no se pudo registrar información de pacientes en la interfaz ocurrio una excepcion.");
			}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE PACIENTES");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de pacientes con la interfaz ");
		}
	}
	
	/**
	 * 
	 * @param con
	 * @throws SQLException
	 */
	private void ejecutarProcesoInterfazPaciente(Connection con) 
	{
		try {
			CallableStatement cs = con.prepareCall("CALL AXGA101CP");
			cs.execute();
			cs.close();

		} catch (SQLException e) {
			logger
					.info("\n problema ejecutando el proceso CALL AXGA101CP "
							+ e);
		}

	}

	/**
	 * Metodo para insertar un registro en la tabla de pacientes de la interfaz
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean modificarPaciente(DtoInterfazPaciente dto)
	{
		
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaPacientes,dto.getInstitucion());
		logger.info("[INTERFAZ] MODIFICACION DEL REGISTRO DE LA TABLA "+nombreTabla);
		if(!nombreTabla.trim().equals(""))
		{
			String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
			String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
			String fechaNacimiento=UtilidadFecha.conversionFormatoFechaABD(dto.getFechaNacimiento()).replace("-", "");
			
			//String cadena="UPDATE "+nombreTabla+" SET tdoc=?,ndoc=?,pnom=?,snom=?,pape=?,sape=?,ingreso=?,esting=?,fechapac=?,horapac=?,fechan=?,estreg=? where codigo=?";
			logger.info("Tamano Num Identificacion ->"+dto.getNumeroIdentificacion().length());
			logger.info("Num Identificacion Completo->"+dto.getNumeroIdentificacion()+"<-");
			logger.info("Numero Identificacion->"+agregarCeros(dto.getNumeroIdentificacion().trim(), 13)+dto.getNumeroIdentificacion().trim());
			
			// campos adicionados  numhc,codconv,nomconv,tercero
			String cadena="UPDATE "+nombreTabla+" " +
								"SET tdoc='"+dto.getTipoIdentificiacion()+"'," +//1  CHAR
								"ndoc='"+agregarCeros(dto.getNumeroIdentificacion().trim(), 13)+dto.getNumeroIdentificacion().trim()+"'," +//2 CHAR
								"pnom='"+(dto.getPrimerNombre().length()>20?dto.getPrimerNombre().substring(0,20):dto.getPrimerNombre())+"'," +//3 CHAR
								"snom='"+(dto.getSegundoNombre().length()>20?dto.getSegundoNombre().substring(0, 20):dto.getSegundoNombre())+"'," +//4 CHAR
								"pape='"+(dto.getPrimerApellido().length()>20?dto.getPrimerApellido().substring(0, 20):dto.getPrimerApellido())+"'," +//5 CHAR
								"sape='"+(dto.getSegundoApellido().length()>20?dto.getSegundoApellido().substring(0, 20):dto.getSegundoApellido())+"'," +//6CHAR
								"ingreso="+Utilidades.convertirADouble(dto.getIngreso())+"," +//7 DECIMAL
								"esting='"+dto.getEstadoIngreso()+"'," +//8 CHAR
								"fechapac="+Utilidades.convertirAEntero(fecha)+"," +//9 DECIMAL
								"horapac="+Utilidades.convertirAEntero(hora)+"," +//10 DECIMAL
								"fechan="+Utilidades.convertirAEntero(fechaNacimiento)+"," +//11 DECIMAL
								"estreg="+dto.getEstadoRegistro()+", " +//12 DECIMAL
								"numhc="+dto.getNumhc()+"," +//13 DECIMAL 
								"codconv='"+Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(dto.getCodconv().trim()))+"'," +//14 CHAR
								"nomconv='"+(dto.getNomconv().length()>30?dto.getNomconv().substring(0,30):dto.getNomconv())+"'," +//15 CHAR
								"tercero='"+(agregarCeros(dto.getTercero().trim(), 13)+dto.getTercero().trim())+"'," +//16 CHAR
								"viaing="+dto.getViaIngreso()+"," +//17 DECIMAL
								"sexo='"+dto.getSexo()+"'," +//18 CHAR
								"usuario='"+dto.getUsuario()+"' " +//19 CHAR
							"where " +
								"codigo="+Utilidades.convertirADouble(dto.getCodigo())+"";//20 DECIMAL
						/**
						 * UPDATE 
						    ALPHILPB2.AX_PACIEN 
						    SET CODIGO = 0, 
						    TDOC       = '', 
						    NDOC       = '', 
						    PNOM       = '', 
						    SNOM       = '', 
						    PAPE       = '', 
						    SAPE       = '', 
						    FECHAN     = 0, 
						    INGRESO    = 0, 
						    VIAING     = 0, 
						    ESTING     = '', 
						    NUMHC      = 0, 
						    CODCONV    = '', 
						    NOMCONV    = '', 
						    TERCERO    = '', 
						    SEXO       = '', 
						    ID01       = '', 
						    ESTREG     = 0, 
						    FECHAPAC   = 0, 
						    HORAPAC    = 0 
						WHERE 
						    CODIGO = < condition> 
						 */
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz no hay conexi�n.");
				}
				logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< MODIFICAR PACIENTE INTERFAZ AX_PACIEN >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				logger.info("[INTERFAZ] ALISTANDO EL STATEMENT");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
				//ps.setString(1, dto.getTipoIdentificiacion());
				logger.info("Tamano Num Identificacion ->"+dto.getNumeroIdentificacion().length());
				logger.info("Num Identificacion Completo->"+dto.getNumeroIdentificacion()+"<-");
				logger.info("Numero Identificacion->"+agregarCeros(dto.getNumeroIdentificacion().trim(), 13)+dto.getNumeroIdentificacion().trim());
				//ps.setString(2, agregarCeros(dto.getNumeroIdentificacion().trim(), 13)+dto.getNumeroIdentificacion().trim());
				logger.info("P NOmbre->"+dto.getPrimerNombre()+"<-");
				//ps.setString(3, dto.getPrimerNombre().length()>20?dto.getPrimerNombre().substring(0,20):dto.getPrimerNombre());
				logger.info("S NOmbre->"+dto.getSegundoNombre()+"<-");
				//ps.setString(4, dto.getSegundoNombre().length()>20?dto.getSegundoNombre().substring(0, 20):dto.getSegundoNombre());
				logger.info("P Apellido->"+dto.getPrimerApellido()+"<-");
				//ps.setString(5, dto.getPrimerApellido().length()>20?dto.getPrimerApellido().substring(0, 20):dto.getPrimerApellido());
				logger.info("S Apellido->"+dto.getSegundoApellido()+"<-");
				//ps.setString(6, dto.getSegundoApellido().length()>20?dto.getSegundoApellido().substring(0, 20):dto.getSegundoApellido());
				logger.info("Ingreso->"+dto.getIngreso()+"<-");
				//ps.setDouble(7, Double.parseDouble(dto.getIngreso()));
				logger.info("Estado Ingreso->"+dto.getEstadoIngreso()+"<-");
				//ps.setString(8, dto.getEstadoIngreso());
				logger.info("fecha->"+fecha+"<-");
				//ps.setInt(9, Utilidades.convertirAEntero(fecha));
				logger.info("hora->"+hora+"<-");
				//ps.setInt(10, Integer.parseInt(hora));
				logger.info("Fec Nac->"+fechaNacimiento+"<-");
				//ps.setInt(11, Utilidades.convertirAEntero(fechaNacimiento));
				logger.info("Estado Reg->"+dto.getEstadoRegistro()+"<-");
				//ps.setString(12, dto.getEstadoRegistro());
				
				logger.info("Num His->"+dto.getNumhc()+"<-");
				
				//Campos adicionados para el manejo de la interfaz del prestamo de historias clinicas  
				//ps.setInt(13, dto.getNumhc());
				logger.info("Cod Convenio INTERFAZ->"+Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(dto.getCodconv().trim()))+"<-");
				//ps.setString(14, Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(dto.getCodconv().trim())));
				logger.info("Nom Convenio->"+dto.getNomconv()+"<-");
				//ps.setString(15, dto.getNomconv().length()>30?dto.getNomconv().substring(0,30):dto.getNomconv());
				
				logger.info("Tercero completo ->"+dto.getTercero()+"<-");
				logger.info("Tercero Shaio->"+agregarCeros(dto.getTercero().trim(), 13)+dto.getTercero().trim());
				//ps.setString(16, agregarCeros(dto.getTercero().trim(), 13)+dto.getTercero().trim());
				
				logger.info("Via Ingreso Paciente ->"+dto.getViaIngreso()+"<-");
				//ps.setString(17, dto.getViaIngreso());
				
				logger.info("Sexo Paciente ->"+dto.getSexo()+"<-");
				//ps.setString(18, dto.getSexo());
				
				logger.info("Login ->"+dto.getUsuario()+"<-");
				//ps.setString(19, dto.getUsuario());
				
				logger.info("Codigo->"+dto.getCodigo()+"<-");
				//ps.setDouble(20, Double.parseDouble(dto.getCodigo()));
				
				/*
				System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Modificador de interfaz");
				System.out.print("\n Codigo paciente " + dto.getNumeroIdentificacion());
				
				System.out.print("\n Numero de historia clinica  " + dto.getNumhc());
				System.out.print("\n Codigo nuevo convenio " + dto.getCodconv());
				System.out.print("\n Nombre buevo convenio " + dto.getNomconv());
				System.out.print("\n NIT " + dto.getTercero());
				System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");*/
				
				if(ps.executeUpdate()>0)
				{
					ps.close();
					logger.info("[INTERFAZ] �EXITO MODIFICACION! ");
					cerrarConexion(con);
					return new ResultadoBoolean(true,"El registro se modifico correctamente");
				}
				logger.error("[INTERFAZ] El registro no se pudo mODIFICAR");
				ps.close();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"El registro no se pudo Modificar");
			} 
			catch (Exception e) 
			{
				logger.error("Error al modificar el paciente en ax_pacien: "+e);
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz ocurrio una excepción.");
			}
			
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE PACIENTES");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de pacientes con la interfaz ");
		}

	}
	
	/**
	 * M�todo implementado para cargar los datos de paciente
	 * @param codigoPaciente
	 * @return
	 */
	public DtoInterfazPaciente cargarPaciente(String codigoPaciente,int institucion)
	{
		DtoInterfazPaciente dto = new DtoInterfazPaciente();
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaPacientes,institucion);
		if(!nombreTabla.trim().equals(""))
		{
			
			String cadena = "SELECT codigo, tdoc, ndoc, pnom, snom, pape, sape, ingreso, esting, fechapac, horapac, fechan,estreg,numhc,codconv,nomconv,tercero,viaing,sexo FROM "+nombreTabla+" WHERE codigo = "+codigoPaciente;
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					dto.setError(true);
					dto.setMensaje("Proceso cancelado, no se pudo actualizar la tabla de interfaz no hay conexion.");
					return dto;
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
				//ps.setObject(1, codigoPaciente);
				
				ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
				
				
				
				if(rs.next())
				{
					dto.setCodigo(rs.getString("codigo"));
					dto.setTipoIdentificiacion(rs.getString("tdoc"));
					dto.setNumeroIdentificacion(rs.getString("ndoc"));
					dto.setPrimerNombre(rs.getString("pnom"));
					dto.setSegundoNombre(rs.getString("snom"));
					dto.setPrimerApellido(rs.getString("pape"));
					dto.setSegundoApellido(rs.getString("sape"));
					dto.setIngreso(rs.getString("ingreso"));
					dto.setEstadoIngreso(rs.getString("esting"));
					String fechaNac = rs.getString("fechan");
					dto.setFechaNacimiento(fechaNac.substring(6,8)+"/"+fechaNac.substring(4,6)+"/"+fechaNac.substring(0,4));
					dto.setEstadoRegistro(rs.getString("estreg"));
					
					//campos de historia clinica
					dto.setNumhc(rs.getInt("numhc"));
					dto.setCodconv(Utilidades.obtenerCodigoConvenioDeCodInterfaz(rs.getString("codconv").trim())+"");
					dto.setNomconv(rs.getString("nomconv"));
					dto.setTercero(rs.getString("tercero"));
					
					dto.setViaIngreso(rs.getString("viaing"));
					dto.setSexo(rs.getString("sexo"));
				}
				
				ps.close();
				cerrarConexion(con);
				return dto;
			} 
			catch (Exception e) 
			{
				logger.info("\n Problema al cargar el paciente "+e);
				cerrarConexion(con);
				dto.setError(true);
				dto.setMensaje("Proceso cancelado, no se pudo actualizar la tabla de interfaz ocurrio una excepción.");
				return  dto;
			}
			
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE PACIENTES");
			return new DtoInterfazPaciente();
		}
	}
	
	
	/**
	 * Metodo para la consulta de facturas en la tabla ax_rips, filtrando la consulta por el numero de envio
	 * @param dto
	 * @param numeroEnvio
	 * @return
	 */
	public HashMap consultarFacturasEnAxRips (String numeroEnvio, int institucion)//DtoInterfazAxRips dto, String numeroEnvio )
	{
		
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaAxRips, institucion);
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		if (!nombreTabla.trim().equals(""))
		{
			String cadenaConsulta = "SELECT facrip as axFactura, convrip as axConvenio FROM "+nombreTabla+" WHERE envrip = "+numeroEnvio+" order by convrip";
			logger.info("\n\nCONSUTA EN DB--->"+cadenaConsulta+"\n");
			
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					mapa.put("isError", true);
					mapa.put("descError", "Proceso cancelado, no se pudo leer la tabla de interfaz no hay conexion.");
					return mapa;
				}
				
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta));
				//ps.setString(1, numeroEnvio);
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				ps.close();
				cerrarConexion(con);
				//UtilidadBD.closeConnection(con);
				
			} 
			catch (SQLException e) 
			{
				cerrarConexion(con);
				//UtilidadBD.closeConnection(con);
				logger.error("Se produjo una excepcion: "+e.toString());
				mapa.put("isError", true);
				mapa.put("descError", "Proceso cancelado, no se pudo leer la tabla de interfaz ocurrio una excepción.");
				return mapa;
			}
		}
		Utilidades.imprimirMapa(mapa);
		
		return mapa;
			
	/*		
			
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");					
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta);
				ps.setString(1, numeroEnvio);
				
				ResultSetDecorator rst =new ResultSetDecorator(ps.executeQuery());
				
				while(rst.next())
					resultados.add(rst.getString("facrip"));			
				ps.close();
				cerrarConexion(con);				
				//logger.error("El registro no se pudo Insertar");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				cerrarConexion(con);
				logger.error("Se produjo una excepcion: "+e.toString());
			}
		}
		
		for(String factura:resultados)
			logger.info("factura: "+factura);
		
		return resultados;*/
	}
	
	/**
	 * Metodo de actualizacion del estado en la interface de ax_rips
	 * @param estado
	 * @param numeroEnvio
	 * @param convenio 
	 * @param hora 
	 * @param fecha 
	 * @param institucion
	 * @return
	 */
	public ResultadoBoolean actualizarEstadoAxRips(String estado, String numeroEnvio, String convenio, String fecha, String hora, int institucion)
	{
		logger.info(">>>>> Numero Envio a Actualizar --->"+numeroEnvio+"<--");
		logger.info(">>>>>     Convenio a Actualizar --->"+convenio+"<--");
		logger.info(">>>>> Nuevo Estado a Actualizar --->"+estado+"<--");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaAxRips, institucion);
		
			if(!nombreTabla.trim().equals(""))
			{
				String cadenaUpdate="UPDATE "+nombreTabla+" SET estreg = "+estado+", fecha="+fecha+", hora="+(agregarCeros(hora.trim(), 6)+hora.trim())+" WHERE envrip = "+(Integer.parseInt(numeroEnvio))+" AND convrip='"+convenio+"' ";				
				Connection con=abrirConexionInterfaz();
				try 
				{
					if(con==null || con.isClosed())
					{
						logger.error("CONEXION CERRADA.");
											
						return new ResultadoBoolean(false,"Proceso cancelado, no se pudo leer la tabla de interfaz no hay conexion.");
					}
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdate));
					//ps.setString(1, estado);
					//ps.setString(2, fecha);
					//ps.setString(3, agregarCeros(hora.trim(), 6)+hora.trim());
					//ps.setInt(4, Integer.parseInt(numeroEnvio));
					//ps.setString(5, convenio);
					
					if(ps.executeUpdate()>0)
					{
						ps.close();
						cerrarConexion(con);
						return new ResultadoBoolean(true,"El registro se actualizo correctamente");
					}
					logger.error("El registro no se pudo actualizar");
					ps.close();
					cerrarConexion(con);
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo leer-actualizar la tabla de interfaz no hay conexion.");
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					cerrarConexion(con);
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo leer-actualizar la tabla de interfaz ocurrio una excepción.");
				}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de rips con la interfaz ");
		}
	}
	
	/**
	 * Metodo para insertar un registro en la tabla de abonos, de la interfaz(es diferente a la tabla de axioma.). 
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean insertarAbono(DtoInterfazAbonos dto)
	{
		logger.info("<<<<<<<<<----INSERTAR ABONO POR INTERFAZ ax_abonos---->>>>>>>>>>");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaAbonos,dto.getInstitucion());
		if(!nombreTabla.trim().equals(""))
		{
			String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
			String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
			double valorPos5=0;
			
			if(dto.getSigno().equals("-"))
			{
				logger.info("ENVIAR VALOR CON SIGNO NEGATIVO");
				double valorNegativo = Double.parseDouble(dto.getValor())*-1;
				logger.info("VALOR ->"+valorNegativo+"<--");
				valorPos5= valorNegativo; //Multiplicar por -1 para cambiar el signo
			}
			else
			{	
				logger.info("ENVIAR VALOR CON SIGNO POSITIVO");
				valorPos5= Double.parseDouble(dto.getValor());
			}
			
			
			String cadenaIsercion="INSERT INTO "+nombreTabla+" " +
										"(codpac," +
										"ndocum," +
										"tipomov," +
										"signo," +
										"valor," +
										"fechacre," +
										"horacre," +
										"estreg," +
										"fechamod," +
										"horamod," +
										"tdoc," +
										"ndoc) " +
									"values " +
										"("+(Double.parseDouble(dto.getCodigoPaciente()))+"," +//1 DECIMAL
										""+(Double.parseDouble(dto.getNumeroDocumento()))+"," +//2 DECIMAL
										""+(Integer.parseInt(dto.getTipoMov()))+"," +//3	DECIMAL
										"'"+(dto.getSigno())+"'," +//4 CHAR
										""+valorPos5+"," +//5 DECIMAL
										""+fecha+"," +//6 DECIMAL
										""+Integer.parseInt(hora)+"," +//7 DECIMAL
										""+Integer.parseInt(dto.getEstadoRegistro())+"," +//8 DECIMAL
										""+fecha+"," +//9 DECIMAL
										""+Integer.parseInt(hora)+"," +//10 DECIMAL
										"'"+dto.getTipoIdentificacion()+"'," +//11 CHAR
										"'"+agregarCeros(dto.getNumIdentificacion(), 13)+dto.getNumIdentificacion()+"')";//12 CHAR
			
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz no hay conexi�n.");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
				logger.info("dto.getCodigoPaciente() ->"+dto.getCodigoPaciente()+"<--");
				logger.info("dto.getNumeroDocumento() ->"+dto.getNumeroDocumento()+"<--");
				logger.info("dto.getTipoMov() ->"+dto.getTipoMov()+"<--");
				logger.info("dto.getSigno() ->"+dto.getSigno()+"<--");
				logger.info("fecha ->"+fecha+"<--");
				logger.info("hora ->"+hora+"<--");
				logger.info("dto.getEstadoRegistro() ->"+dto.getEstadoRegistro()+"<--");
				
				logger.info("dto.getTipoIdentificacion() ->"+dto.getTipoIdentificacion()+"<--");
				logger.info("dto.getNumIdentificacion() ->"+dto.getNumIdentificacion()+"<--");
				
				//ps.setDouble(1, Double.parseDouble(dto.getCodigoPaciente()));
				//ps.setDouble(2, Double.parseDouble(dto.getNumeroDocumento()));
				//ps.setInt(3, Integer.parseInt(dto.getTipoMov()));
				//ps.setString(4, dto.getSigno());
				
				/*if(dto.getSigno().equals("-"))
				{
					logger.info("ENVIAR VALOR CON SIGNO NEGATIVO");
					double valorNegativo = Double.parseDouble(dto.getValor())*-1;
					logger.info("VALOR ->"+valorNegativo+"<--");
					ps.setDouble(5, valorNegativo); //Multiplicar por -1 para cambiar el signo
				}
				else
					{	
						logger.info("ENVIAR VALOR CON SIGNO POSITIVO");
						ps.setDouble(5, Double.parseDouble(dto.getValor()));
					}*/
				
				//ps.setObject(6, fecha);
				//ps.setInt(7, Integer.parseInt(hora));
				//ps.setInt(8, Integer.parseInt(dto.getEstadoRegistro()));
				//ps.setObject(9, fecha);
				//ps.setInt(10, Integer.parseInt(hora));
				//ps.setString(11, dto.getTipoIdentificacion());
				logger.info("Numero Identificacion->"+agregarCeros(dto.getNumIdentificacion(), 13)+dto.getNumIdentificacion());
				//ps.setString(12, agregarCeros(dto.getNumIdentificacion(), 13)+dto.getNumIdentificacion());
				
				if(ps.executeUpdate()>0)
				{
					ps.close();
					cerrarConexion(con);
					return new ResultadoBoolean(true,"El registro se inserto correctamente");
				}
				logger.error("El registro no se pudo Insertar");
				ps.close();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz no hay conexi�n.");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz ocurrio una excepción.");
			}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE ABONOS");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de abonos con la interfaz ");
		}
	}
	
	/**
	 * Metodo para Tomar los abonos de la interfaz, que se encuentran es estado procesado, y pasarlo al sistema axioma.
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean cargarAbonosInterfazToAxioma(int institucion)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaAbonos,institucion);
		
		if(!nombreTabla.trim().equals(""))
		{
			String cadenaConsultaAbonosInterfaz="select codpac,ndocum,tipomov,signo,valor,fechacre,horacre,estreg,fechamod,horamod,tdoc,ndoc from "+nombreTabla+" where estreg="+ConstantesBDInterfaz.codigoEstadoAbonoNoProcesado;
			
			String cadenaActualizacionAbono="";
			String restriccionesUpdate="";
			
			String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
			String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz no hay conexi�n.");
				}
				
				logger.info("---->>>> CONSULTA ABONOS---->>>"+cadenaConsultaAbonosInterfaz);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAbonosInterfaz));
				//ps.setInt(1, ConstantesBDInterfaz.codigoEstadoAbonoNoProcesado);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				int codigoIncosistencia=ConstantesBD.codigoNuncaValido;
				boolean incosistencias=false;
				int contador=0;
				while(rs.next())
				{
					// Inicializar las variables por cada vuelta de While
					restriccionesUpdate="";
					incosistencias=false;
					codigoIncosistencia=ConstantesBD.codigoNuncaValido;
					
					if(UtilidadTexto.isEmpty(rs.getString("codpac").trim()))
					{
						incosistencias=true;
						codigoIncosistencia=ConstantesIncosistenciasInterfaz.codigoCodigoPacienteNull;
						restriccionesUpdate+=" and codpac is null ";
						logger.info("Codigo Paciente null");
					}
					else
					{
						if(rs.getDouble("codpac")<=0)
						{
							incosistencias=true;
							codigoIncosistencia=ConstantesIncosistenciasInterfaz.codigoCodigoPacienteMenorIgualCero;
							logger.info("Codigo Paciente menor a 0 -->"+rs.getDouble("codpac"));
						}
						restriccionesUpdate+=" and codpac = "+rs.getDouble("codpac");
					}
					if(UtilidadTexto.isEmpty(rs.getString("ndocum").trim()))
					{
						incosistencias=true;
						codigoIncosistencia=ConstantesIncosistenciasInterfaz.codigoNumeroDocumentoNull;
						restriccionesUpdate+=" and ndocum is null ";
						logger.info("Numero Docuemtno null");
					}
					else
					{
						if(rs.getDouble("ndocum")<=0)
						{
							incosistencias=true;
							codigoIncosistencia=ConstantesIncosistenciasInterfaz.codigoNumeroDocumentoMenorIgualCero;
							logger.info("Numero Docuemento menor a 0 -->"+rs.getDouble("ndocum"));
						}
						restriccionesUpdate+=" and ndocum = "+rs.getObject("ndocum");
					}
					if(UtilidadTexto.isEmpty(rs.getString("tipomov").trim()))
					{
						incosistencias=true;
						codigoIncosistencia=ConstantesIncosistenciasInterfaz.codigoTipoMovimientoNull;
						restriccionesUpdate+=" and tipomov is null ";
						logger.info("Tipo movimiento null");
					}
					else
					{
						if(rs.getInt("tipomov")<=0||rs.getInt("tipomov")>4)
						{
							incosistencias=true;
							codigoIncosistencia=ConstantesIncosistenciasInterfaz.codigoTipoMovimientoFueraRango;
							logger.info("Tipo movimiento fuera de rango {0-4} -->"+rs.getInt("tipomov"));
						}
						restriccionesUpdate+=" and tipomov = "+rs.getInt("tipomov");
					}
					if(UtilidadTexto.isEmpty(rs.getString("valor").trim()))
					{
						incosistencias=true;
						codigoIncosistencia=ConstantesIncosistenciasInterfaz.codigoValorNull;
						restriccionesUpdate+=" and valor is null ";
						logger.info("Valor null --->"+rs.getObject("valor"));
					}
					else
					{
						restriccionesUpdate+=" and tipomov = "+rs.getInt("tipomov");
					}
					
					if(UtilidadTexto.isEmpty(rs.getString("fechacre").trim()))
					{
						incosistencias=true;
						codigoIncosistencia=ConstantesIncosistenciasInterfaz.fechaCreacionErronea;
						restriccionesUpdate+=" and fechacre is null ";
						logger.info("Fecha Creacion null ");
					}
					else
					{
						if((rs.getObject("fechacre")+"").length()!=8)
						{
							if(UtilidadTexto.isEmpty((rs.getObject("fechacre")+"")))
							{
								incosistencias=true;
								codigoIncosistencia=ConstantesIncosistenciasInterfaz.fechaCreacionErronea;
								restriccionesUpdate+=" and fechacre is null ";
								logger.info("Fecha Creacion en blanco ");
							}
							else
							{
								incosistencias=true;
								codigoIncosistencia=ConstantesIncosistenciasInterfaz.fechaCreacionErronea;
								restriccionesUpdate+=" and fechacre = "+rs.getObject("fechacre");
								logger.info("Fecha Creacion formato invalido  -->"+rs.getObject("fechacre"));
							}
						}
						else
						{
							restriccionesUpdate+=" and fechacre = "+rs.getObject("fechacre");
						}
					}
					if(rs.getObject("horacre")==null)
					{
						incosistencias=true;
						codigoIncosistencia=ConstantesIncosistenciasInterfaz.horaCreacionErronea;
						restriccionesUpdate+=" and horacre is null ";
						logger.info("horacre Creacion null ");
					}
					else
					{
						if(UtilidadTexto.isEmpty((rs.getString("horacre").trim())) || (rs.getObject("horacre")+"").length()>6)
						{
							if(UtilidadTexto.isEmpty((rs.getString("horacre").trim())))
							{
								incosistencias=true;
								codigoIncosistencia=ConstantesIncosistenciasInterfaz.horaCreacionErronea;
								restriccionesUpdate+=" and horacre is null ";
								logger.info("horacre Creacion en blanco ");
							}
							else
							{
								incosistencias=true;
								codigoIncosistencia=ConstantesIncosistenciasInterfaz.horaCreacionErronea;
								restriccionesUpdate+=" and horacre = "+rs.getObject("horacre");
								logger.info("horacre Creacion formato invalido  -->"+rs.getObject("horacre")+"<-");
							}
						}
						else
						{
							restriccionesUpdate+=" and fechacre = "+rs.getObject("fechacre");
						}
					}

					if(UtilidadTexto.isEmpty((rs.getObject("fechamod")+"")))
					{
						restriccionesUpdate+=" and fechamod is null ";
					}
					else
					{
						restriccionesUpdate+=" and fechamod = "+rs.getObject("fechamod");
					}

					if(UtilidadTexto.isEmpty((rs.getObject("horamod")+"")))
					{
						restriccionesUpdate+=" and horamod is null ";
					}
					else
					{
						restriccionesUpdate+=" and horamod = "+rs.getObject("horamod");
					}
					
					try
					{
						//Si no tiene incosistencias, procesar el registro. si tiene, cambiar el estado por identificado del error.
						if(!incosistencias)
						{
							logger.info("no tiene inconsitencias ....INSERTANDO");
							String fechaTemp=(rs.getInt("fechacre")+"").substring(0,4)+"-"+(rs.getInt("fechacre")+"").substring(4,6)+"-"+(rs.getInt("fechacre")+"").substring(6,8);
							String horaTempo=rs.getInt("horacre")+"";
							int numDig=horaTempo.length();
							while(numDig<6)
							{
								horaTempo="0"+horaTempo;
								numDig=horaTempo.length();
							}
							String horaTemp=(horaTempo).substring(0,2)+":"+(horaTempo).substring(2,4)+":"+(horaTempo).substring(4,6);

							if(!Utilidades.existeRegistroAbonosRC(rs.getObject("ndocum")+"",rs.getInt("tipomov")+""))
							{
								Utilidades.insertarAbono(new DtoInterfazAbonos(rs.getObject("codpac")+"",rs.getObject("ndocum")+"",rs.getInt("tipomov")+"",rs.getString("signo"),rs.getDouble("valor")+"",rs.getInt("estreg")+"",institucion,fechaTemp,horaTemp,rs.getString("tdoc"),rs.getString("ndoc")));
							}
							cadenaActualizacionAbono="UPDATE "+nombreTabla+" SET estreg="+ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+",fechamod="+Integer.parseInt(fecha)+",horamod="+Integer.parseInt(hora)+" WHERE 1=1 "+restriccionesUpdate;
							PreparedStatementDecorator psInt= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAbono));
							//psInt.setInt(1, ConstantesBDInterfaz.codigoEstadoProcesadoAxioma);
							//psInt.setObject(2, fecha);
							//psInt.setInt(3, Integer.parseInt(hora));
							psInt.executeUpdate();
						}
						else
						{
							logger.info("tiene inconsitencias ....actualizando");
							cadenaActualizacionAbono="UPDATE "+nombreTabla+" SET estreg="+codigoIncosistencia+",fechamod="+Integer.parseInt(fecha)+",horamod="+Integer.parseInt(hora)+" WHERE  1=1 "+restriccionesUpdate;
							PreparedStatementDecorator psInt= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAbono));
							//psInt.setInt(1, codigoIncosistencia);
							//psInt.setObject(2, fecha);
							//psInt.setInt(3, Integer.parseInt(hora));
							psInt.executeUpdate();
						}
					}
					catch(SQLException e)
					{
						logger.warn("Fecha-->"+fecha);
						logger.warn("Hora-->"+hora);
						logger.error("ERROR CARGANDO EL ABONO: "+e.getStackTrace());
						e.printStackTrace();
					}
					contador++;
				}
				logger.info("   SE PROCESARON "+contador+" ABONOS");
				ps.close();
				cerrarConexion(con);
				return new ResultadoBoolean(true,"Proceso Finalizado");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz ocurrio una excepción.");
			}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE ABONOS");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de abonos con la interfaz ");
		}
	}
	
	/**
	 * M�todo que consulta el saldo del paciente desde la interfaz
	 * @param codigoPaciente
	 * @param institucion
	 * @return
	 */
	public ResultadoDouble consultarSaldoPaciente(String numeroIdentificacion,int institucion)
	{
		double saldo = 0;
		
		String nombreTabla = Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaSaldos, institucion);
		logger.info("*****************INICIO CONSULTA SALDO PACIENTE [INTERFAZ]***************************************");
		if(!nombreTabla.trim().equals(""))
		{
			String[] fecha=UtilidadFecha.getFechaActual().split("/");
			
			String cadenaConsultaSaldoPaciente="select sum(salmae) AS saldo from "+nombreTabla+" where nitmae='"+agregarCeros(numeroIdentificacion.trim(), 13)+numeroIdentificacion.trim()+"' AND aamae='"+fecha[2]+"' AND mmmae='"+fecha[1]+"'";
			
			logger.info("========CONSULTA=> "+cadenaConsultaSaldoPaciente);
			logger.info("========numeroIdentificacion=> "+agregarCeros(numeroIdentificacion.trim(), 13)+numeroIdentificacion.trim()+", a�o=> "+fecha[2]+", mes=> "+fecha[1]);
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoDouble(0,"Proceso cancelado, no se pudo leer la tabla de interfaz no hay conexi�n.");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaSaldoPaciente));
				//ps.setObject(1, agregarCeros(numeroIdentificacion.trim(), 13)+numeroIdentificacion.trim());
				//ps.setObject(2, fecha[2]);
				//ps.setObject(3, fecha[1]);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					saldo = rs.getDouble("saldo");
				}
			}
			catch (Exception e) 
			{
				logger.error("Error consultan el saldo interfaz del paciente: "+e);
				return new ResultadoDouble(0,"Proceso cancelado, no se pudo leer la tabla de interfaz ocurrio una excepción.");
			}
			cerrarConexion(con);
		}
		logger.info("*****************FIN INICIO CONSULTA SALDO PACIENTE [INTERFAZ]***************************************");
		return new ResultadoDouble(saldo,"");
	}
	
	//*************************************************************************************************************************************
	
	/**
	 * Consulta los saldos de Cartera Paciente
	 * Para manejar los errores se crearon 2 key's dentro del mapa.
	 * isError,descError.
	 * Modificado por anexo 779
	 * @parama HashMap parametros 
	 * */
	public HashMap consultarSaldosCarteraPaciente(HashMap parametros)
	{
		HashMap resultado = new HashMap();
		resultado.put("numRegistros","0");
		
		logger.info("*****************INICIO CONSULTA SALDO CARTERA PACIENTE [INTERFAZ]***************************************");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaInterfazSaldosCarteraPacientes,
				Integer.parseInt(parametros.get("institucion").toString()));		
		
		String[] fecha=UtilidadFecha.getFechaActual().split("/");
		
		String cadenaConsultaSaldo="select salmae as salmae,vlrmae as vlrmae from "+nombreTabla+" where facmae = '"+(agregarCeros(parametros.get("consecutivofactura").toString().trim(), 8)+parametros.get("consecutivofactura").toString().trim())+"' and nitmae = '"+(agregarCeros(parametros.get("nitfactura").toString().trim(), 13)+parametros.get("nitfactura").toString().trim())+"' and aamae = '"+fecha[2]+"' and mmmae = '"+fecha[1]+"' ";
		
		logger.info("========CONSULTA=> "+cadenaConsultaSaldo);
		logger.info("=========> facmae->"+agregarCeros(parametros.get("consecutivofactura").toString().trim(), 8)+parametros.get("consecutivofactura").toString().trim()+"<-");
		logger.info("=========> nitmae->"+agregarCeros(parametros.get("nitfactura").toString().trim(), 13)+parametros.get("nitfactura").toString().trim()+"<-");
		logger.info("=========> aamae->"+fecha[2]+"<-");
		logger.info("=========> mmmae->"+fecha[1]+"<-");
		
		Connection con=abrirConexionInterfaz();
		
		try 
		{
			if(con==null || con.isClosed())
			{
				logger.error("Error. CONEXION CERRADA.");
				resultado.put("isError", true);
				resultado.put("descError", "Proceso cancelado, no se pudo leer la tabla de interfaz no hay conexi�n.");
				return resultado;
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaSaldo));
			//ps.setObject(1,agregarCeros(parametros.get("consecutivofactura").toString().trim(), 8)+parametros.get("consecutivofactura").toString().trim());
			//ps.setObject(2,agregarCeros(parametros.get("nitfactura").toString().trim(), 13)+parametros.get("nitfactura").toString().trim());
			//ps.setObject(3, fecha[2]);
			//ps.setObject(4, fecha[1]);		
			
			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true);
			logger.info("valor del resultado >> "+resultado);
		}
		catch (Exception e) 
		{
			logger.error("Error consultan el saldo interfaz del paciente: "+e);
			resultado.put("isError", true);
			resultado.put("descError", "Proceso cancelado, no se pudo leer la tabla de interfaz ocurrio una excepción.");
			return resultado;
		}
		cerrarConexion(con);		
		logger.info("*****************FIN INICIO CONSULTA SALDO CARTERA PACIENTE [INTERFAZ]***************************************");
		return resultado;
	}
	
	//*************************************************************************************************************************************
	
	/**
	 * Consulta los saldos de Cartera Paciente
	 * @parama HashMap parametros 
	 * */
	public HashMap consultarEstadoCuentasMedicas(HashMap parametros)
	{
		HashMap resultado = new HashMap();
		resultado.put("numRegistros","0");
		
		logger.info("*****************INICIO CONSULTA ESTADO FACTURAS CUENTAS MEDICAS [INTERFAZ]***************************************");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaInterfazFactuCuentaMedicas,
				Integer.parseInt(parametros.get("institucion").toString()));		
		
		String cadenaConsultaEstado="select esgfac from "+nombreTabla+" where frafac = "+parametros.get("factura")+" and nitfac = "+parametros.get("idpaciente")+" ";
					

		logger.info("========CONSULTA=> "+cadenaConsultaEstado+" >> "+parametros);
		Connection con=abrirConexionInterfaz();
		
		try 
		{
			if(con==null || con.isClosed())
			{
				logger.error("Error. CONEXION CERRADA.");
				return resultado;
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaEstado));
			//ps.setObject(1,parametros.get("factura"));
			//ps.setObject(2,parametros.get("idpaciente"));					
			
			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			logger.info("valor del resultado >> "+resultado);
		}
		catch (Exception e) 
		{
			logger.error("Error consultan el saldo interfaz del paciente: "+e);
			return resultado;
		}
		cerrarConexion(con);		
		logger.info("*****************FIN INICIO CONSULTA  ESTADO FACTURAS CUENTAS MEDICAS[INTERFAZ]***************************************");
		return resultado;
	}
	
	
	//*************************************************************************************************************************************	
	
	/**
	 * Metodo para insertar un registro en la tabla de facturas de la interfaz
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean insertarFactura(DtoInterfazFactura dto)
	{
		logger.info("************INSERCION DE LA FACTURA INTERFAZ!!!");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaFacturas,dto.getCodigoInstitucion());
		String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
		String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
		if(!nombreTabla.trim().equals(""))
		{	
			String cadenaIsercion="INSERT INTO "+nombreTabla+" (	tipoec,"+ 	//NUMERIC(10,0) 	1
																	"ndocec," + // NUMERIC(10,0),	2		
																	"ingrec," + // NUMERIC(8,0),	3
																	"fedoec," + // NUMERIC(8,0),	4
																	"hodoec," + // NUMERIC(6,0),	5
																	"feveec," + // NUMERIC(8,0),	6
																	"terpec," + // VARCHAR,			7
																	"tereec," + // VARCHAR,			8
																	"cocoec," + // VARCHAR(6),		9
																	"ndvtec," + // NUMERIC(3,0),	10
																	"vpacec," + // NUMERIC(15,2),	11
																	"ventec," + // NUMERIC(15,2),	12
																	"vabpec," + // NUMERIC(15,2),	13
																	"vdepec," + // NUMERIC(15,2),	14
																	"vdesec," + // NUMERIC(15,2),	15
																	"vdtoec," + // NUMERIC(15,2),	16
																	"cuenec," + // VARCHAR(12),		17
																	"estreg," + // numeric,			18
																	"fechec," + // NUMERIC(8,0),	19	
																	"horaec," + // NUMERIC(6,0),	20
																	"id01," + 	// CHAR(1)			21
																	"indrad," + // INT 				22
																	"usuario) "+// VARCHAR(30)		23
																	//"ventec)"+//verificar dato
																	"values " +
																	"("+dto.getTipoMovimiento()+", " +//1
																	""+Double.parseDouble(dto.getDocumento())+", " +//2
																	""+Double.parseDouble(dto.getIngreso())+", " +//3
																	""+Double.parseDouble(dto.getFechaDocumentoFormatoShaio())+", " +//4
																	""+Double.parseDouble(dto.getHoraDocumentoFormatoShaio()+agregarCeros(dto.getHoraDocumentoFormatoShaio(), 6))+"," +	//5
																	""+Double.parseDouble(dto.getFechaVencimientoFormatoShaio())+", " +//6
																	"'"+(UtilidadTexto.isEmpty(dto.getTerceroPaciente())?"":agregarCeros(dto.getTerceroPaciente(), 13)+dto.getTerceroPaciente())+"', " +//7
																	"'"+(UtilidadTexto.isEmpty(dto.getTerceroEntidad())?"":agregarCeros(dto.getTerceroEntidad(), 13)+dto.getTerceroEntidad())+"', " +//8
																	"'"+Utilidades.obtenerCodigoInterfazConvenioDeCodigo(dto.getCodigoConvenio())+"', " +//9
																	""+((dto.getNumeroDiasVencimientoConvenio()<0)?0:dto.getNumeroDiasVencimientoConvenio())+"," +		//10
																	""+((dto.getValorPacienteConDescuento()<0)?0:dto.getValorPacienteConDescuento())+", " +//11
																	""+((dto.getValorEntidadConDescuento()<0)?0:dto.getValorEntidadConDescuento())+", " +//12
																	""+((dto.getValorAbonosPacienteAplicados()<0)?0:dto.getValorAbonosPacienteAplicados())+", " +//13
																	""+((dto.getValorDescuentosPaciente()<0)?0:dto.getValorDescuentosPaciente())+", " +//14
																	""+((dto.getValorDescuentoFactura()<0)?0:dto.getValorDescuentoFactura())+"," +		//15
																	""+((dto.getValorTotalFacturaConDescuento()<0)?0:dto.getValorTotalFacturaConDescuento())+", " +//16
																	"'"+(Utilidades.obtenerCuentaContable(dto.getCuentaConvenio()))+"', " +//17
																	""+(dto.getEstadoRegistro())+", " +//18
																	""+Integer.parseInt(fecha)+", " +//19
																	""+Integer.parseInt(hora+agregarCeros(hora, 6))+"," +//20
																	"'"+dto.getContabilizado()+"', " +//21
																	""+dto.getIndicativoRadicado()+" ," +//22
																	"'"+dto.getUsuario()+"')";				//23
			
			
			///NOOOOO COMENTARIAR POR FAVOR
			logger.info("cadenaIsercion------------------>"+cadenaIsercion);
			
			
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de facturaci�n en la interfaz no hay conexi�n.");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
				logger.warn("insercion->"+cadenaIsercion+"<-");
				//ps.setInt(1, dto.getTipoMovimiento());
				logger.info("tipo mov->"+dto.getTipoMovimiento()+"<-");
				//ps.setDouble(2, Double.parseDouble(dto.getDocumento()));
				logger.info("doc->"+dto.getDocumento()+"<-");
				//ps.setDouble(3, Double.parseDouble(dto.getIngreso()));
				logger.info("ingreso->"+dto.getIngreso()+"<-");
				//ps.setDouble(4, Double.parseDouble(dto.getFechaDocumentoFormatoShaio()));
				
				//ps.setDouble(5, Double.parseDouble(dto.getHoraDocumentoFormatoShaio()+agregarCeros(dto.getHoraDocumentoFormatoShaio(), 6)));
				//ps.setDouble(6, Double.parseDouble(dto.getFechaVencimientoFormatoShaio()));
				
				/*if(UtilidadTexto.isEmpty(dto.getTerceroPaciente()))
					ps.setString(7, "");
				else
				{
					logger.info("tercero pac->"+agregarCeros(dto.getTerceroPaciente(), 13)+dto.getTerceroPaciente()+"<-");
					ps.setString(7, agregarCeros(dto.getTerceroPaciente(), 13)+dto.getTerceroPaciente());
				}	
				if(UtilidadTexto.isEmpty(dto.getTerceroEntidad()))
					ps.setString(8, "");
				else
				{
					logger.info("tercero entidad->"+agregarCeros(dto.getTerceroEntidad(), 13)+dto.getTerceroEntidad()+"<-");
					ps.setString(8, agregarCeros(dto.getTerceroEntidad(), 13)+dto.getTerceroEntidad());
				}*/
				logger.info("Codigo Convenio->"+Utilidades.obtenerCodigoInterfazConvenioDeCodigo(dto.getCodigoConvenio())+"<-");
				//ps.setString(9, Utilidades.obtenerCodigoInterfazConvenioDeCodigo(dto.getCodigoConvenio()));
				
				logger.info("Dias de Vencimiento->"+dto.getNumeroDiasVencimientoConvenio()+"<-");
				/*if(dto.getNumeroDiasVencimientoConvenio()<0)
					ps.setDouble(10, 0);
				else
					ps.setDouble(10, dto.getNumeroDiasVencimientoConvenio());*/
				
				logger.info("Valor Paciente Descuento->"+dto.getValorPacienteConDescuento()+"<-");
				/*if(dto.getValorPacienteConDescuento()<0)
					ps.setDouble(11, 0);
				else	
					ps.setDouble(11, dto.getValorPacienteConDescuento());*/
				
				logger.info("Valor Entidad Descuento->"+dto.getValorEntidadConDescuento()+"<-");
				/*if(dto.getValorEntidadConDescuento()<0)
					ps.setDouble(12, 0);
				else
					ps.setDouble(12, dto.getValorEntidadConDescuento());*/
				
				logger.info("Valor Abonos Paciente Aplicados->"+dto.getValorAbonosPacienteAplicados()+"<-");
				/*if(dto.getValorAbonosPacienteAplicados()<0)
					ps.setDouble(13, 0);
				else
					ps.setDouble(13, dto.getValorAbonosPacienteAplicados());*/
				
				logger.info("Valor Descuento Paciente->"+dto.getValorDescuentosPaciente()+"<-");
				/*if(dto.getValorDescuentosPaciente()<0)
					ps.setDouble(14, 0);
				else
					ps.setDouble(14, dto.getValorDescuentosPaciente());*/
				
				logger.info("Valor Descuento Factura->"+dto.getValorDescuentoFactura()+"<-");
				/*if(dto.getValorDescuentoFactura()<0)
					ps.setDouble(15, 0);
				else
					ps.setDouble(15, dto.getValorDescuentoFactura());*/
				
				logger.info("Valor Total Factura con Descuento->"+dto.getValorTotalFacturaConDescuento()+"<-");
				/*if(dto.getValorTotalFacturaConDescuento()<0)
					ps.setDouble(16, 0);
				else
					ps.setDouble(16, dto.getValorTotalFacturaConDescuento());*/
				
				logger.info("Cuenta Convenio->"+Utilidades.obtenerCuentaContable(dto.getCuentaConvenio())+"<-");
				//ps.setString(17, Utilidades.obtenerCuentaContable(dto.getCuentaConvenio()));
				
				logger.info("Estado Registro->"+dto.getEstadoRegistro()+"<-");
				//ps.setString(18, dto.getEstadoRegistro());
				
				logger.info("Fecha->"+fecha+"<-");
				//ps.setDouble(19, Integer.parseInt(fecha));
				
				logger.info("Hora->"+hora+"<-");
				//ps.setDouble(20, Integer.parseInt(hora+agregarCeros(hora, 6)));
				
				logger.info("Contabilizado->"+dto.getContabilizado()+"<-");
				//ps.setString(21, dto.getContabilizado());
				
				logger.info("Indicativo Radicado->"+dto.getIndicativoRadicado()+"<-");
				//ps.setInt(22, dto.getIndicativoRadicado());
				
				logger.info("Usuario->"+dto.getUsuario()+"<-");
				//ps.setString(23, dto.getUsuario());
				//ps.setDouble(23, dto.getVenTec());
				
				logger.info("VA HA INSERTAR!!!!");
				if(ps.executeUpdate()>0)
				{
					logger.info("INSERTO INTERFAZ FACTURA!!!!*****************");
					ps.close();
					cerrarConexion(con);
					return new ResultadoBoolean(true,"El registro se inserto correctamente");
				}
				logger.error("El registro no se pudo Insertar***********************");
				ps.close();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de facturaci�n en la interfaz no hay conexi�n.");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de facturaci�n en la interfaz ocurrio una excepción.");
			}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE FACTURAS");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de facturas con la interfaz ");
		}
	}
	
	/**
	 * Proceso que dispara la contabilidad en el Sitema Shaio--Creado 20 de Oct de 2008 - Aesilva
	 * @param con
	 * @throws SQLException
	 */
	public void ejecutarProcesoInterfazFactura()  
	{
		Connection con=abrirConexionInterfaz();
		CallableStatement cs;
		try 
		{
			cs = con.prepareCall("CALL AXGA102CP");
			cs.execute ();
	        cs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
        cerrarConexion(con);
	}
	
	/**
	 * 
	 * @param cadena
	 * @param lengthRequerido
	 * @return
	 */
	public String agregarCeros(String cadena, int lengthRequerido)
	{
		String concatena="";
		if(cadena.length()<lengthRequerido)
		{
			int faltantes=0;
			faltantes= lengthRequerido-cadena.length();
			for(int w=0; w<faltantes; w++)
				concatena+="0";
			//logger.info("concatena->"+concatena);
		}
		return concatena;
	}	

	/**
	 * 
	 * @param numeroIdentificacionMedicoResponde
	 * @param institucion
	 * @param esDetallePaquete
	 * @param consecutivoOrdenMedica
	 * @return
	 */
	public ResultadoBoolean actualizarDetalleFactProcedimientosFacturados(
			String numeroIdentificacionMedicoResponde, int institucion,
			boolean esDetallePaquete, String consecutivoOrdenMedica,boolean isReproceso,String fechaRep,String horaRep,String consecutivo)
	{
		logger
		.info("\n\n\n\n******************************************** ACTUALIZACION DEL DETALLE DE LA FACTURA / DETALLE PAQUETES DE LOS PROCEDIMIENTOS YA FACTURADOS  ************************************************************************************");

String tabla = "";
String complemento = "", cadena = "",nomConsecutivo="",fecha="",hora="",medicoEje="";
boolean tmp = true;

if (!esDetallePaquete) {
	tabla = ConstantesBDInterfaz.identificadorTablaDetFacturas;
	complemento = "dt";
	nomConsecutivo="seq_ax_dfac";
}
else
{
	tabla = ConstantesBDInterfaz.identificadorTablaDetFacturasPaquetes;
	complemento = "pq";
	nomConsecutivo="seq_ax_dpaq";
}
String nombreTabla = Utilidades.consultarNombreTablaInterfaz(tabla,	institucion);

if (isReproceso)
{
	fecha=fechaRep;
	hora=horaRep;
	medicoEje=numeroIdentificacionMedicoResponde;
}
else
{
	 fecha = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
	 hora = UtilidadFecha.getHoraSegundosActual().replace(":", "");
	 medicoEje=(agregarCeros(numeroIdentificacionMedicoResponde,13) + numeroIdentificacionMedicoResponde);
}

if (!nombreTabla.trim().equals(""))
{
	Connection con = abrirConexionInterfaz();
	try {

		if (con == null || con.isClosed()) 
		{
			if (!isReproceso)
			{
				con = UtilidadBD.abrirConexion();
				tmp = false;
				// si no hay conexion a la interfaz se ingresan los datos el
				// BD local
				cadena = "INSERT INTO "
						+ nombreTabla
						+ "( FECM"
						+ complemento
						+ ",HORM"
						+ complemento
						+ ",meej"
						+ complemento
						+ ",estr"
						+ complemento
						+ ",cons"
						+ complemento
						+ ",TIPERR," +
						"consecutivo ) VALUES ("
						+ fecha
						+ ","
						+ hora
						+ ",'"
						+ medicoEje
						+ "',"
						+ ConstantesBDInterfaz.codigoEstadoProcesadoAxioma
						+ "," + consecutivoOrdenMedica + ",'"
						+ ConstantesIntegridadDominio.acronimoNoConexion+"',"
						+UtilidadBD.obtenerSiguienteValorSecuencia(con, nomConsecutivo)
						+ ")";
			}
			else
				return new ResultadoBoolean(false,"");
			
		}
		else// si hay conexion con la interfaz
		{

			cadena = "UPDATE "
					+ nombreTabla
					+ " SET "
					+ "FECM"
					+ complemento
					+ " = "
					+ fecha
					+ ", "
					+ "HORM"
					+ complemento
					+ " = "
					+ hora
					+ ", "
					+ "meej"+ complemento
					+ " = '"+ medicoEje+ "', "
					+ "estr" + complemento + " = "+ ConstantesBDInterfaz.codigoEstadoProcesadoAxioma 
					+ "WHERE " + "cons" + complemento + " = "
					+ consecutivoOrdenMedica + "";
		}
		logger.warn("\n\n CADENA DETALLE AX_D-->" + cadena);

		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
		ps.executeUpdate();
		ps.close();
		UtilidadBD.closeConnection(con);
		
		if (tmp)
		{
			logger.info("\n\n ################################ ELIMINAR REGISTRO LOCAL INTERFAZ "+nombreTabla+" CODIGO --> "+consecutivo);
			//se manda a elimiar el registro si existe desde en la bd local
			//*******************************************************************************************
			if (UtilidadCadena.noEsVacio(consecutivo) && isReproceso)
			{
				Connection con1=UtilidadBD.abrirConexion();
				try 
				{
					ps=new PreparedStatementDecorator(con1.prepareStatement("DELETE FROM "+nombreTabla+" WHERE consecutivo="+consecutivo));
					ps.executeUpdate();
				} 
				catch (Exception e) 
				{
					logger.info("\n problema eliminado el registro de la tabla "+nombreTabla+" local "+e);
				}
				ps.close();
				UtilidadBD.cerrarConexion(con1);	
			}
			//*************************************************************************************************
			
			return new ResultadoBoolean(true,"El registro se inserto correctamente");
		}
		
		
	} catch (SQLException e) 
	{
		logger.info("\n problema insertando ...." + e);
		UtilidadBD.closeConnection(con);
		if (!isReproceso)
			return actualizarDetalleFactProcedimientosFacturadosRespaldo(numeroIdentificacionMedicoResponde, institucion,esDetallePaquete, consecutivoOrdenMedica);
	}
	return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz no hay conexion.");
	
} 
else 
{
	logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE DETALLE FACTURAS");
	return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de detalle facturas con la interfaz ");
}

}
	
	public ResultadoBoolean actualizarDetalleFactProcedimientosFacturadosRespaldo(String numeroIdentificacionMedicoResponde, int institucion, boolean esDetallePaquete, String consecutivoOrdenMedica)
	{
		logger.info("\n\n\n\n******************************************** ACTUALIZACION DEL DETALLE DE LA FACTURA / DETALLE PAQUETES DE LOS PROCEDIMIENTOS YA FACTURADOS  ************************************************************************************");
		
		String tabla="";
		String complemento="",cadena="";
		Connection con=UtilidadBD.abrirConexion();
		
		if(!esDetallePaquete)
		{	
			tabla= ConstantesBDInterfaz.identificadorTablaDetFacturas;
			complemento="dt";
		}	
		else
		{	
			tabla=ConstantesBDInterfaz.identificadorTablaDetFacturasPaquetes;
			complemento="pq";
		}	
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(tabla,institucion);
		String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
		String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
		if(!nombreTabla.trim().equals(""))
		{
			try 
			{
					//si no hay conexion a la interfaz se ingresan los datos el BD local
					cadena="INSERT INTO "+nombreTabla+"( FECM"+complemento+",HORM"+complemento+",meej"+complemento+",estr"+complemento+
														",cons"+complemento+",TIPERR) VALUES ('"+fecha+"','"+hora+"',"+
														(agregarCeros(numeroIdentificacionMedicoResponde,13)+numeroIdentificacionMedicoResponde)+
														",'"+ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+"','"+consecutivoOrdenMedica+
														"','"+ConstantesIntegridadDominio.acronimoSinIntegridad+"')";                                                                  
			
				logger.warn("\n\n CADENA DETALLE AX_D-->"+cadena);
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
				ps.executeUpdate();
				ps.close();
				cerrarConexion(con);
				logger.info("LA INSERCION SALIO CORRECTA!!!!!");
				return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz no hay conexion");
			}
			catch (Exception e) 
			{
				cerrarConexion(con);
				logger.info("LA INSERCION SALIO INCORRECTA!!!!!"+e);
				return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz ocurrio una excepción.");
			}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE DETALLE FACTURAS");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de detalle facturas con la interfaz ");
		}
		
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de la tabla local
	 * ax_dfac o ax_dpaq 
	 *
	 * @param esDetallePaquete
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoInterfazDetalleFactura> consultaDatosResprocesoDfacDpaq (boolean esDetallePaquete,int institucion)
	{
		logger.info("\n  entro a consultaDatosResprocesoDfacDpaq ...");
		
		String tabla="",complemento="";
		ArrayList<DtoInterfazDetalleFactura> array = new ArrayList<DtoInterfazDetalleFactura>();
		
		if(!esDetallePaquete)
		{	
			tabla= ConstantesBDInterfaz.identificadorTablaDetFacturas;
			complemento="dt";
		}	
		else
		{	
			tabla=ConstantesBDInterfaz.identificadorTablaDetFacturasPaquetes;
			complemento="pq";
		}	
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(tabla,institucion);
		String consulta ="SELECT " +
									"tipo"+complemento+","+ 	//NUMERIC(1,0),		1
									"ndoc"+complemento+","+ 	//NUMERIC(10,0),	2
									"ingr"+complemento+","+ 	//NUMERIC(8,0),		3
									"cosp"+complemento+","+ 	//VARCHAR(6),		4
									"cose"+complemento+","+ 	//VARCHAR(6),		5
									"conc"+complemento+","+ 	//VARCHAR(3),		6
									"cons"+complemento+","+ 	//NUMERIC(6,0),		7
									"viai"+complemento+","+ 	//VARCHAR(2),		8
									"meor"+complemento+","+ 	//varchar,			9
									"meej"+complemento+","+ 	//varchar,			10
									"feco"+complemento+","+ 	//NUMERIC(8,0),		11
									"hoco"+complemento+","+ 	//NUMERIC(6,0),		12
									"ccup"+complemento+","+ 	//VARCHAR(8),		13
									"ipaq"+complemento+","+ 	//CHAR(1),			14
									"cant"+complemento+","+ 	//NUMERIC(6,2),		15
									"auxi"+complemento+","+ 	//VARCHAR(11),		16
									"vcou"+complemento+","+ 	//NUMERIC(15,2),	17	
									"vcon"+complemento+","+ 	//NUMERIC(15,2),	18		
									"vdes"+complemento+","+ 	//NUMERIC(15,2),	19
									"vnet"+complemento+","+ 	//NUMERIC(15,2),	20
									"tere"+complemento+","+ 	//varchar,			21
									"terp"+complemento+","+ 	//varchar ,			22
									"coco"+complemento+","+ 	//VARCHAR(6),		23
									"estr"+complemento+","+ 	//CHAR(1),			24
									"fech"+complemento+","+ 	//NUMERIC(8,0),		25
									"hora"+complemento+","+ 	//NUMERIC(6,0),		26
									"incont"+complemento+"," +	//CHAR(1)			27
									"ccplane, " +				//CHAR(?)			28
									"IND01, " +					//char(2)			29
									"fecm"+complemento+"," +	//NUMERIC(8,0),		30
									"horm"+complemento+","+
									"TIPERR,"+
									"consecutivo "+//NUMERIC(6,0),		31 			
						" FROM "+nombreTabla+" ";
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				DtoInterfazDetalleFactura dto = new DtoInterfazDetalleFactura();
				
				dto.setTipoMovimiento(rs.getInt("tipo"+complemento));//1
				dto.setDocumento(rs.getString("ndoc"+complemento));//2
				dto.setIngreso(rs.getString("ingr"+complemento));//3
				dto.setCentroCostoPaciente(rs.getString("cosp"+complemento));//4
				dto.setCentroCostoEjecuta(rs.getString("cose"+complemento));//5
				dto.setConceptoFactura(rs.getString("conc"+complemento));//6
				dto.setConsecutivoConsumo(rs.getDouble("cons"+complemento));//7
				dto.setViaIngreso(rs.getString("viai"+complemento));//8
				dto.setMedicoOrdena(rs.getString("meor"+complemento));//9
				dto.setMedicoEjecuta(rs.getString("meej"+complemento));//10
				dto.setFechaConsumo(rs.getString("feco"+complemento));//11
				dto.setHoraConsumo(rs.getString("hoco"+complemento));//12
				dto.setCups(rs.getString("ccup"+complemento));//13
				dto.setPaquete(rs.getString("ipaq"+complemento));//14
				dto.setCantidad(rs.getInt("cant"+complemento));//15
				dto.setAuxiliar(rs.getString("auxi"+complemento));//16
				dto.setValorConsumoUnitario(rs.getDouble("vcou"+complemento));//17
				dto.setValorConsumoTotal(rs.getDouble("vcon"+complemento));//18
				dto.setValorDescuentoTotalConsumo(rs.getDouble("vdes"+complemento));//19
				dto.setValorNetoTotalConsumo(rs.getDouble("vnet"+complemento));//20
				dto.setTerceroEntidad(rs.getString("tere"+complemento));//21
				dto.setTerceroPaciente(rs.getString("terp"+complemento));//22
				dto.setCodigoInterfazConvenio(rs.getString("coco"+complemento));//23
				dto.setEstadoRegistro(rs.getString("estr"+complemento));//24
				dto.setFechaRegistro(rs.getString("fech"+complemento));//25
				dto.setHoraRegistro(rs.getString("hora"+complemento));//26
				dto.setContabilizado(rs.getString("incont"+complemento));//27
				dto.setCentroCostoPlanEspecialConvenio(rs.getInt("ccplane"));//28
				dto.setIndicativoCobrable(rs.getString("IND01"));//29
				dto.setFechaModifica(rs.getString("fecm"+complemento));//30
				dto.setHoraModifica(rs.getString("horm"+complemento));//31
				dto.setConsecutivo(rs.getString("consecutivo"));//32
				array.add(dto);
			}
			rs.close();
			ps.close();
			
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando la tabla local ax_"+complemento+" "+e);
		}
		
		UtilidadBD.closeConnection(con);

		
		return array;
	}
	
	/**
	 * 
	 * @param conNormal
	 * @param primeraCuenta
	 * @param arraydto
	 * @param esDetallePaquete
	 * @param indO1_cargoExcento
	 * @param isReproceso
	 */
	public void insertarDetalleFacturaReproceso(ArrayList<DtoInterfazDetalleFactura> arraydto,boolean esDetallePaquete,int institucion)
	{
		for (int i = 0; i < arraydto.size(); i++)
			actualizarDetalleFactProcedimientosFacturados( arraydto.get(i).getMedicoEjecuta(), institucion, esDetallePaquete, arraydto.get(i).getConsecutivoConsumo()+"",true,arraydto.get(i).getFechaModifica(),arraydto.get(i).getHoraModifica(),arraydto.get(i).getConsecutivo());
	
	}
	
	
	/**
	 * Metodo para insertar un registro en la tabla de detalle facturas de la interfaz
	 * @param primeraCuenta 
	 * @param conNormal 
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean insertarDetalleFactura(Connection conNormal, String primeraCuenta, DtoInterfazDetalleFactura dto, boolean esDetallePaquete, String indO1_cargoExcento,boolean isReproceso)
	{
		String tabla="";
		String complemento="";
		String viaIng="",codIntConv="",fecha="",hora="",indCobrable="";
		if (isReproceso)
		{
			viaIng=dto.getViaIngreso();
			codIntConv=dto.getCodigoInterfazConvenio();
			fecha=dto.getFechaRegistro();
			hora=dto.getHoraRegistro();
			indCobrable=dto.getIndicativoCobrable();
		}
		else
		{
			viaIng=UtilidadesManejoPaciente.obtenerCodInterfazXCuenta(conNormal, primeraCuenta);
			codIntConv=Utilidades.obtenerCodigoInterfazConvenioDeCodigo(dto.getCodigoConvenio());
			fecha=Double.parseDouble(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "").trim())+"";
			hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
			hora=Double.parseDouble(hora+agregarCeros(hora, 6))+"";
			indCobrable=indO1_cargoExcento;
		}
		
		if(!esDetallePaquete)
		{	
			tabla= ConstantesBDInterfaz.identificadorTablaDetFacturas;
			complemento="dt";
		}	
		else
		{	
			tabla=ConstantesBDInterfaz.identificadorTablaDetFacturasPaquetes;
			complemento="pq";
		}	
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(tabla,dto.getCodigoInstitucion());
		if(!nombreTabla.trim().equals(""))
		{
			String cadenaIsercion="INSERT INTO "+nombreTabla+" (	tipo"+complemento+","+ 	//NUMERIC(1,0),		1
																	"ndoc"+complemento+","+ 	//NUMERIC(10,0),	2
																	"ingr"+complemento+","+ 	//NUMERIC(8,0),		3
																	"cosp"+complemento+","+ 	//VARCHAR(6),		4
																	"cose"+complemento+","+ 	//VARCHAR(6),		5
																	"conc"+complemento+","+ 	//VARCHAR(3),		6
																	"cons"+complemento+","+ 	//NUMERIC(6,0),		7
																	"viai"+complemento+","+ 	//VARCHAR(2),		8
																	"meor"+complemento+","+ 	//varchar,			9
																	"meej"+complemento+","+ 	//varchar,			10
																	"feco"+complemento+","+ 	//NUMERIC(8,0),		11
																	"hoco"+complemento+","+ 	//NUMERIC(6,0),		12
																	"ccup"+complemento+","+ 	//VARCHAR(8),		13
																	"ipaq"+complemento+","+ 	//CHAR(1),			14
																	"cant"+complemento+","+ 	//NUMERIC(6,2),		15
																	"auxi"+complemento+","+ 	//VARCHAR(11),		16
																	"vcou"+complemento+","+ 	//NUMERIC(15,2),	17	
																	"vcon"+complemento+","+ 	//NUMERIC(15,2),	18		
																	"vdes"+complemento+","+ 	//NUMERIC(15,2),	19
																	"vnet"+complemento+","+ 	//NUMERIC(15,2),	20
																	"tere"+complemento+","+ 	//varchar,			21
																	"terp"+complemento+","+ 	//varchar ,			22
																	"coco"+complemento+","+ 	//VARCHAR(6),		23
																	"estr"+complemento+","+ 	//CHAR(1),			24
																	"fech"+complemento+","+ 	//NUMERIC(8,0),		25
																	"hora"+complemento+","+ 	//NUMERIC(6,0),		26
																	"incont"+complemento+"," +	//CHAR(1)			27
																	"ccplane, " +				//CHAR(?)			28
																	"IND01, " +					//char(2)			29
																	"fecm"+complemento+"," +	//NUMERIC(8,0),		30
																	"horm"+complemento+") "+	//NUMERIC(6,0),		31 					
																	"values " +
																	"("+dto.getTipoMovimiento()+", " +//1
																	""+Double.parseDouble(dto.getDocumento())+", " +//2
																	""+Double.parseDouble(dto.getIngreso())+", " +//3
																	"'"+dto.getCentroCostoPaciente().trim()+"', " +//4
																	"'"+dto.getCentroCostoEjecuta().trim()+"'," +//5
																	"'"+(UtilidadTexto.isEmpty(dto.getConceptoFactura())?"400":dto.getConceptoFactura())+"', " +//6
																	""+dto.getConsecutivoConsumo()+", " +//7
																	"'"+viaIng+"', " +//8
																	"'"+agregarCeros(dto.getMedicoOrdena(), 13)+dto.getMedicoOrdena()+"', " +//9
																	"'"+agregarCeros(dto.getMedicoEjecuta(), 13)+dto.getMedicoEjecuta()+"'," +		//10
																	""+Double.parseDouble(dto.getFechaConsumoFormatoShaio())+", " +//11
																	""+Double.parseDouble(dto.getHoraConsumoFormatoShaio()+ agregarCeros(dto.getHoraConsumoFormatoShaio(), 6))+", " +//12
																	"'"+((UtilidadTexto.isEmpty(dto.getCups()))?"":dto.getCups())+"', " +//13
																	"'"+dto.getPaquete()+"', " +//14
																	""+dto.getCantidad()+"," +		//15
																	"'"+((UtilidadTexto.isEmpty(dto.getAuxiliar()))?"":dto.getAuxiliar())+"', " +//16
																	""+((dto.getValorConsumoUnitario()<0)?0:dto.getValorConsumoUnitario())+", " +//17
																	""+((dto.getValorConsumoTotal()<0)?0:dto.getValorConsumoTotal())+", " +//18
																	""+((dto.getValorDescuentoTotalConsumo()<0)?0:dto.getValorDescuentoTotalConsumo())+", " +//19
																	""+((dto.getValorNetoTotalConsumo()<0)?0:dto.getValorNetoTotalConsumo())+"," +		//20
																	"'"+agregarCeros(dto.getTerceroEntidad(), 13)+dto.getTerceroEntidad()+"', " +//21
																	"'"+agregarCeros(dto.getTerceroPaciente(), 13)+dto.getTerceroPaciente()+"', " +//22
																	"'"+codIntConv+"', " +//23
																	""+dto.getEstadoRegistro()+", " +//24
																	""+fecha+"," +		//25
																	""+hora+", " +//26
																	"'"+dto.getContabilizado()+"', " +//27
																	" "+((dto.getCentroCostoPlanEspecialConvenio()==0)?"0":Utilidades.obtenerCentroCostoInterfaz(dto.getCentroCostoPlanEspecialConvenio()))+", " +//28
																	" '"+indCobrable+"',  " +//29
																	" 0, "+//30
																	" 0 )";//31			
			logger.warn("\n\n CADENA DETALLE AX_D-->"+cadenaIsercion);
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de facturaci�n en la interfaz no hay conexi�n.");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
			//	logger.info("1 dto.getTipoMovimiento()->"+dto.getTipoMovimiento()+"<-");
				//ps.setInt(1, dto.getTipoMovimiento());
				//logger.info("2 dto.getDocumento()->"+dto.getDocumento()+"<-");
				//ps.setDouble(2, Double.parseDouble(dto.getDocumento()));
				//logger.info("3 dto.getIngreso()->"+dto.getIngreso()+"<-");
				//ps.setDouble(3, Double.parseDouble(dto.getIngreso()));
				/*
				String concatena="";
				if((dto.getCentroCostoPaciente().trim()).length()<6)
				{
					int faltantes=0;
					faltantes= 6-(dto.getCentroCostoPaciente().trim()).length();
					for(int w=0; w<faltantes; w++)
						concatena+="0";
					logger.info("AGREGAR CEROS ->"+concatena+"<-");
				}
				//logger.info("4 dto.getCentroCostoPaciente() cc paciente->"+dto.getCentroCostoPaciente().trim()+"<-");
				//ps.setString(4, dto.getCentroCostoPaciente().trim());
				
				concatena="";
				if((dto.getCentroCostoEjecuta().trim()).length()<6)
				{
					int faltantes=0;
					faltantes= 6-(dto.getCentroCostoEjecuta().trim()).length();
					for(int w=0; w<faltantes; w++)
						concatena+="0";
					logger.info("AGREGAR CEROS ->"+concatena+"<-");
				}
				*/
				//logger.info("5 dto.getCentroCostoEjecuta() ->"+dto.getCentroCostoEjecuta().trim()+"<-");
				//ps.setString(5, dto.getCentroCostoEjecuta().trim());
				
				//logger.info("6 dto.getConceptoFactura()->"+dto.getConceptoFactura()+"<-");
				/*if(UtilidadTexto.isEmpty(dto.getConceptoFactura()))
					ps.setString(6, "400");
				else
					ps.setString(6, dto.getConceptoFactura());*/
				
				//logger.info("7 dto.getConsecutivoConsumo()->"+dto.getConsecutivoConsumo()+"<-");
				//ps.setDouble(7, dto.getConsecutivoConsumo());
				//logger.info("8 dto.getCodigoViaIngreso()->"+dto.getCodigoViaIngreso()+"<-");
				//ps.setString(8, dto.getCodigoViaIngreso()+"");
				
				//logger.info("9 dto.getMedicoOrdena()->"+agregarCeros(dto.getMedicoOrdena(), 13)+dto.getMedicoOrdena()+"<-");
				//ps.setString(9, agregarCeros(dto.getMedicoOrdena(), 13)+dto.getMedicoOrdena());
				
				//logger.info("10 dto.getMedicoEjecuta()->"+agregarCeros(dto.getMedicoEjecuta(), 13)+dto.getMedicoEjecuta()+"<-");
				//ps.setString(10, agregarCeros(dto.getMedicoEjecuta(), 13)+dto.getMedicoEjecuta());
				
				//logger.info("11 dto.getFechaConsumoFormatoShaio()->"+dto.getFechaConsumoFormatoShaio()+"<-");
				//ps.setDouble(11, Double.parseDouble(dto.getFechaConsumoFormatoShaio()));
				//logger.info("12 dto.getHoraConsumoFormatoShaio()->"+dto.getHoraConsumoFormatoShaio()+"<-");
				//ps.setDouble(12, Double.parseDouble(dto.getHoraConsumoFormatoShaio()+ agregarCeros(dto.getHoraConsumoFormatoShaio(), 6)));
				
				//logger.info("13 dto.getCups()->"+dto.getCups()+"<-");
				/*if(UtilidadTexto.isEmpty(dto.getCups()))
					ps.setString(13, "");
				else
					ps.setString(13, dto.getCups());*/
				
				//logger.info("14 dto.getPaquete()->"+dto.getPaquete()+"<-");
				//ps.setString(14, dto.getPaquete());
				
				//logger.info("15 dto.getCantidad()->"+dto.getCantidad()+"<-");
				//ps.setDouble(15, dto.getCantidad());
				
				//logger.info("16 dto.getAuxiliar()->"+dto.getAuxiliar()+"<-");
				/*if(UtilidadTexto.isEmpty(dto.getAuxiliar()))
					ps.setString(16, "");
				else
					ps.setString(16, dto.getAuxiliar());*/
				
				//logger.info("17 dto.getValorConsumoUnitario()->"+dto.getValorConsumoUnitario()+"<-");
				/*if(dto.getValorConsumoUnitario()<0)
					ps.setDouble(17, 0);
				else
					ps.setDouble(17, dto.getValorConsumoUnitario());*/
				
				//logger.info("18 dto.getValorConsumoTotal()->"+dto.getValorConsumoTotal()+"<-");
				/*if(dto.getValorConsumoTotal()<0)
					ps.setDouble(18, 0);
				else
					ps.setDouble(18, dto.getValorConsumoTotal());*/
				
				//logger.info("19 dto.getValorDescuentoTotalConsumo()->"+dto.getValorDescuentoTotalConsumo()+"<-");
				/*if(dto.getValorDescuentoTotalConsumo()<0)
					ps.setDouble(19, 0);
				else
					ps.setDouble(19, dto.getValorDescuentoTotalConsumo());*/
				
				//logger.info("20 dto.getValorNetoTotalConsumo()->"+dto.getValorNetoTotalConsumo()+"<-");
				/*if(dto.getValorNetoTotalConsumo()<0)
					ps.setDouble(20, 0);
				else
					ps.setDouble(20, dto.getValorNetoTotalConsumo());*/
				
				//logger.info("21 dto.getTerceroEntidad()->"+dto.getTerceroEntidad()+"<-");
				/*if(UtilidadTexto.isEmpty(dto.getTerceroEntidad()))
					ps.setString(21, "");
				else
					ps.setString(21, dto.getTerceroEntidad());*/
				
				//logger.info("22 dto.getTerceroPaciente()->"+dto.getTerceroPaciente()+"<-");
				/*if(UtilidadTexto.isEmpty(dto.getTerceroPaciente()))
					ps.setString(22, "");
				else
					ps.setString(22, dto.getTerceroPaciente());*/
				
				//logger.info("23 dto.getCodigoConvenio()->"+Utilidades.obtenerCodigoInterfazConvenioDeCodigo(dto.getCodigoConvenio())+"<-");
				//ps.setString(23, Utilidades.obtenerCodigoInterfazConvenioDeCodigo(dto.getCodigoConvenio()));
				//logger.info("24 dto.getEstadoRegistro()->"+dto.getEstadoRegistro()+"<-");
				//ps.setString(24, dto.getEstadoRegistro());
				//logger.info("25 fecha->"+fecha+"<-");
				//ps.setDouble(25, Double.parseDouble(fecha));
				//logger.info("26 hora->"+hora+"<-");
				//ps.setDouble(26, Double.parseDouble(hora+agregarCeros(hora, 6)));
				//logger.info("27 dto.getContabilizado()->"+dto.getContabilizado()+"<-");
				//ps.setString(27, dto.getContabilizado());
				//logger.info("28 dto.getCentroCostoPlanEspecialConvenio() ->"+dto.getCentroCostoPlanEspecialConvenio()+"<-");
				//logger.info("28 dto.getCentroCostoPlanEspecialConvenio() INTERFAZ->"+Utilidades.obtenerCentroCostoInterfaz(dto.getCentroCostoPlanEspecialConvenio())+"<-");
				
				/*if(dto.getCentroCostoPlanEspecialConvenio()==0)
					ps.setString(28, dto.getCentroCostoPlanEspecialConvenio()+"");
				else
					ps.setString(28, Utilidades.obtenerCentroCostoInterfaz(dto.getCentroCostoPlanEspecialConvenio()));*/
				
				//logger.info("29 indO1_cargoExcento -> "+indO1_cargoExcento);
				//ps.setString(29, indO1_cargoExcento);
				
				if(ps.executeUpdate()>0)
				{
					logger.info("\n\n ################################ ELIMINAR REGISTRO LOCAL INTERFAZ DETALLE FACTURA CODIGO --> "+dto.getConsecutivo());
					//se manda a elimiar el registro si existe desde en la bd local
					//*******************************************************************************************
					if (UtilidadCadena.noEsVacio(dto.getConsecutivo()) && isReproceso)
					{
						Connection con1=UtilidadBD.abrirConexion();
						try 
						{
							ps=new PreparedStatementDecorator(con1.prepareStatement("DELETE FROM "+nombreTabla+" WHERE consecutivo="+dto.getConsecutivo()));
							ps.executeUpdate();
						} 
						catch (SQLException e) 
						{
							logger.info("\n problema eliminado el registro de la tabla "+nombreTabla+" local "+e);
						}
						ps.close();
						UtilidadBD.closeConnection(con1);
						
					}
					//*************************************************************************************************
					
					
					
					return new ResultadoBoolean(true,"El registro se inserto correctamente");
				}
				logger.error("El registro no se pudo Insertar");
				ps.close();
				UtilidadBD.closeConnection(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de facturaci�n en la interfaz no hay conexi�n.");
			} 
			catch (Exception e) 
			{
				
				UtilidadBD.closeConnection(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de facturaci�n en la interfaz ocurrio una excepción.");
			}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE DETALLE FACTURAS");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de detalle facturas con la interfaz ");
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public static ResultadoBoolean insertarInterfazContableFactura(Connection con, ArrayList<DtoFactura> facturas, UsuarioBasico usuario, String codigoIngreso, String numeroIdentificacionPersona, boolean esAnularFactura) 
	{
		logger.info("\n\n\n INSERCION DE LS INTERFAZ CONTABLE DE FACTURAS - SHAIO - AXIOMA ***************************** ");
		int tipoMovimiento=ConstantesBD.codigoNuncaValido;
		if(esAnularFactura)
			tipoMovimiento=ConstantesBDInterfaz.codigoTipoMovimientoAnularfactura;
		else
			tipoMovimiento=ConstantesBDInterfaz.codigoTipoMovimientoGenerarFactura;
			
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableFacturas(usuario.getCodigoInstitucionInt())))
		{
			for(int w=0; w<facturas.size(); w++)
			{
				DtoFactura dtoFactura= facturas.get(w);
				
				int centroCostoPlanEspecialConvenio= UtilidadesFacturacion.ccPlanEspecialConvenio(facturas.get(w).getConvenio().getCodigo());
				
								
				int numDiasVencimiento= Convenio.obtenerNumeroDiasVencimiento(con, dtoFactura.getConvenio().getCodigo());
				String fechaVencimiento=UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), numDiasVencimiento, false);
				double descuentoEntidad=0;
				String cuentaContableConvenio= Convenio.obtenerCuentaContableConvenio(con, dtoFactura.getConvenio().getCodigo());
				String numeroIdentificacionConvenio= Convenio.obtenerNroIdentificacionConvenio(con, dtoFactura.getConvenio().getCodigo());
				
				if(dtoFactura.getEsParticular())
					numeroIdentificacionConvenio=numeroIdentificacionPersona;
				
				int indicativoRadicado= Convenio.obtenerRadicarCuentasNegativas(con,dtoFactura.getConvenio().getCodigo());	
				
				DtoInterfazFactura dtoInterfazFactura= new DtoInterfazFactura(	tipoMovimiento /*tipoMovimiento*/, 
																				UtilidadTexto.formatearExponenciales(dtoFactura.getConsecutivoFactura())/*documento*/, 
																				codigoIngreso /*ingreso*/, 
																				UtilidadFecha.getFechaActual() /*fechaDocumento*/, 
																				UtilidadFecha.getHoraActual() /*horaDocumento*/, 
																				fechaVencimiento, 
																				numeroIdentificacionPersona/*terceroPaciente*/, 
																				numeroIdentificacionConvenio, 
																				dtoFactura.getConvenio().getCodigo(), //CODIGO INTERFAZ DEL CONVENIO 
																				numDiasVencimiento, 
																				dtoFactura.getValorBrutoPac()-dtoFactura.getValorDescuentoPaciente()/*valorPacienteConDescuento*/, 
																				dtoFactura.getValorConvenio()-descuentoEntidad/*valorEntidadConDescuento*/, 
																				dtoFactura.getValorAbonos(), 
																				dtoFactura.getValorDescuentoPaciente(), 
																				descuentoEntidad /*valorDescuentoFactura*/, 
																				dtoFactura.getValorTotal()-dtoFactura.getValorDescuentoPaciente()-descuentoEntidad, 
																				cuentaContableConvenio, 
																				ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado+"", 
																				ConstantesBD.acronimoNo /*contabilizado*/, 
																				usuario.getCodigoInstitucionInt(),
																				indicativoRadicado,
																				dtoFactura.getValorAFavorConvenio()*-1,
																				dtoFactura.getLoginUsuario());
				
				UtilidadBDInterfaz interfazUtil= new UtilidadBDInterfaz();
				//si lo inserta correctamente entonces insertamos el detalle de las facturas y los paquetes
				ResultadoBoolean resultado =interfazUtil.insertarFactura(dtoInterfazFactura);
				if(resultado.isTrue() && !esAnularFactura)
				{
					if(dtoFactura.getDetallesFactura().size()<=0)
					{
						logger.error("FACTURA SIN DETALLE");
					}
					
					//CARGOS EN ESTADO CARGADO
					for(int x=0; x<dtoFactura.getDetallesFactura().size(); x++)
					{
						ResultadoBoolean tmp=insertarDetalleFacturaInterfaz(	con, 
								usuario,
								codigoIngreso, 
								numeroIdentificacionPersona,
								tipoMovimiento, 
								dtoFactura,
								centroCostoPlanEspecialConvenio,
								numeroIdentificacionConvenio, 
								interfazUtil, 
								x,
								false /*excentos*/);
						if(!tmp.isTrue())
						{	
							logger.warn("NO INSERTO FACTURA, CARGOS EN ESTADO CARGADO");
							return new ResultadoBoolean(false,tmp.getDescripcion());
						}	
					}//for
					
					//CARGOS EN ESTADO EXCENTO
					for(int x=0; x<dtoFactura.getDetallesFacturaExcentas().size(); x++)
					{
						ResultadoBoolean tmp=insertarDetalleFacturaInterfaz(	con, 
								usuario,
								codigoIngreso, 
								numeroIdentificacionPersona,
								tipoMovimiento, 
								dtoFactura,
								centroCostoPlanEspecialConvenio,
								numeroIdentificacionConvenio, 
								interfazUtil, 
								x,
								true /*excentos*/);
						
						if(!tmp.isTrue())
						{
							logger.warn("NO INSERTO FACTURA, CARGOS EN ESTADO EXCENTO");
							return new ResultadoBoolean(false,tmp.getDescripcion());
						}
														
					}//for
				}
				else
				{
					logger.warn("NO INSERTO FACTURA");
					return new ResultadoBoolean(false,resultado.getDescripcion());
				}
			}
			
			//si todo sale bien enotnces lanzamos
			UtilidadBDInterfaz interfazUtil= new UtilidadBDInterfaz();
			interfazUtil.ejecutarProcesoInterfazFactura();
		}
		return new ResultadoBoolean(true,"");
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codigoIngreso
	 * @param numeroIdentificacionPersona
	 * @param tipoMovimiento
	 * @param dtoFactura
	 * @param centroCostoPlanEspecialConvenio
	 * @param numeroIdentificacionConvenio
	 * @param interfazUtil
	 * @param x
	 * @param insertarExcentos
	 * @return
	 */
	private static ResultadoBoolean insertarDetalleFacturaInterfaz(Connection con,
			UsuarioBasico usuario, String codigoIngreso,
			String numeroIdentificacionPersona, int tipoMovimiento,
			DtoFactura dtoFactura, int centroCostoPlanEspecialConvenio,
			String numeroIdentificacionConvenio,
			UtilidadBDInterfaz interfazUtil, int x, boolean insertarExcentos) {
		DtoDetalleFactura dtoDetalleFactura = insertarExcentos ? dtoFactura
				.getDetallesFacturaExcentas().get(x) : dtoFactura
				.getDetallesFactura().get(x);
		String indO1_cargoExcento = insertarExcentos ? "NC" : "  ";

		String conceptoFactura = "";
		String cups = "";
		if (dtoDetalleFactura.getCodigoArticulo() > 0)
			conceptoFactura = Articulo.obtenerCodigoInterfazNaturalezaArticulo(
					con, dtoDetalleFactura.getCodigoArticulo());
		else
			cups = Servicios.obtenerCodigoTarifarioServicio(con,
					dtoDetalleFactura.getCodigoServicio(),
					ConstantesBD.codigoTarifarioCups);
		HashMap infoInterfazMap = Solicitud.obteneInfoSolicitudInterfaz(con,
				dtoDetalleFactura.getNumeroSolicitud() + "");

		logger.info("infoInterfazMap-->" + infoInterfazMap);

		String auxiliar = "";
		if (dtoDetalleFactura.getCodigoServicio() > 0)
			auxiliar = cups;
		else if (dtoDetalleFactura.getCodigoArticulo() > 0)
			auxiliar = Utilidades
					.obtenerCodigoInterfazArticulo(dtoDetalleFactura
							.getCodigoArticulo());

		// CASO EN EL CUAL SE TIENE EL ARTICULO DE LA CX A NIVEL DEL DETALLE
		// PERO NO SE CARGA EN EL DTO EL SERVICIO DE LA CX PARA ESA SOLICITUD
		if (infoInterfazMap.get("tipo").toString().equals(
				ConstantesBD.codigoTipoSolicitudCirugia + "")
				&& dtoDetalleFactura.getCodigoArticulo() > 0) {
			for (int s = 0; s < dtoFactura.getDetallesFactura().size(); s++) {
				if (s != x) {
					if (dtoFactura.getDetallesFactura().get(s)
							.getCodigoServicio() > 0
							&& dtoFactura.getDetallesFactura().get(s)
									.getNumeroSolicitud() == dtoFactura
									.getDetallesFactura().get(x)
									.getNumeroSolicitud()) {
						cups = Servicios.obtenerCodigoTarifarioServicio(con,
								dtoFactura.getDetallesFactura().get(s)
										.getCodigoServicio(),
								ConstantesBD.codigoTarifarioCups);
						s = dtoFactura.getDetallesFactura().size();
					}
				}
			}
		}

		int tipoSolicitud = Utilidades.convertirAEntero((infoInterfazMap
				.get("tipo") + ""));
		int centroCostoTempo = (tipoSolicitud == ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos || tipoSolicitud == ConstantesBD.codigoTipoSolicitudMedicamentos) ? Utilidades
				.convertirAEntero(infoInterfazMap.get("ccprincipal") + "")
				: Utilidades.convertirAEntero(infoInterfazMap
						.get("ccsolicitado")
						+ "");
		DtoInterfazDetalleFactura dtoInterfazDetalleFactura = new DtoInterfazDetalleFactura(
				tipoMovimiento /* tipoMovimiento */,
				UtilidadTexto.formatearExponenciales(dtoFactura
						.getConsecutivoFactura())
						+ ""/* documento */,
				codigoIngreso /* ingreso */,
				Utilidades.obtenerCentroCostoInterfaz(Integer
						.parseInt(infoInterfazMap.get("ccsolicitante")
								.toString())) /* centroCostoPaciente */,
				Utilidades.obtenerCentroCostoInterfaz(centroCostoTempo) /* centroCostoEjecuta */,
				conceptoFactura,
				Double.parseDouble(infoInterfazMap
						.get("consecutivoordenmedica").toString()),
				dtoFactura.getViaIngreso().getCodigo(),
				infoInterfazMap.get("numidmedicosolicita").toString()/* medicoOrdena */,
				infoInterfazMap.get("numidmedicoejecuta").toString()/* medicoEjecuta */,
				UtilidadFecha.getFechaActual()/* fechaConsumo */,
				UtilidadFecha.getHoraActual()/* horaConsumo */,
				cups,
				ConstantesBD.acronimoNo/* paquete */,
				dtoDetalleFactura.getCantidadCargo(),
				auxiliar,
				dtoDetalleFactura.getValorCargo()
						+ dtoDetalleFactura.getValorRecargo()/* valorConsumoUnitario */,
				dtoDetalleFactura.getValorTotal() /* valorConsumoTotal */,
				dtoDetalleFactura.getValorDescuentoComercial() /* valorDescuentoTotalConsumo */,
				dtoDetalleFactura.getValorTotal()
						- dtoDetalleFactura.getValorDescuentoComercial() /* valorNetoTotalConsumo */,
				numeroIdentificacionConvenio,
				numeroIdentificacionPersona/* terceroPaciente */,
				dtoFactura.getConvenio().getCodigo(), // CODIGO INTERFAZ DEL
				// CONVENIO
				ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado + "" /* estadoRegistro */,
				ConstantesBD.acronimoNo/* contabilizado */, usuario
						.getCodigoInstitucionInt(),
				centroCostoPlanEspecialConvenio);

		ResultadoBoolean resulBol=interfazUtil.insertarDetalleFactura(con,
				dtoFactura.getCuentas().get(0).toString(),
				dtoInterfazDetalleFactura, false, indO1_cargoExcento, false);
		if (!resulBol.isTrue())
		{
			logger.error("no inserto la interfaz para la solicitud "+ dtoDetalleFactura.getNumeroSolicitud());
			return new ResultadoBoolean (false,resulBol.getDescripcion());
		}

		logger
				.info("\n\n\n\n\n**********************************************************************REVISION ANEXO 777");
		logger
				.info("************************************************************************************************************************");
		logger
				.info("************************************************************************************************************************");
		logger
				.info("************************************************************************************************************************");
		logger
				.info("************************************************************************************************************************");
		logger.info("TIPO --> " + infoInterfazMap.get("tipo").toString()
				+ " SERVICIO->" + dtoDetalleFactura.getCodigoServicio()
				+ " ES CX->" + dtoDetalleFactura.getEsCx());

		if (infoInterfazMap.get("tipo").toString().equals(
				ConstantesBD.codigoTipoSolicitudCirugia + "")
				&& dtoDetalleFactura.getCodigoServicio() > 0
				&& dtoDetalleFactura.getEsCx()) {
			logger.info("entra !!!!!!!!!!!!!");
			for (int a = 0; a < dtoDetalleFactura.getAsociosDetalleFactura()
					.size(); a++) {
				DtoAsociosDetalleFactura dtoAsociosCx = dtoDetalleFactura
						.getAsociosDetalleFactura().get(a);
				logger
						.info("dtoAsociosCx.getCodigoDetalleCargo()).getCodigo()---->"
								+ dtoAsociosCx.getCodigoDetalleCargo());

				// ANEXO 777-----------
				int centroCostoEjecuta = UtilidadesSalas
						.obtenerCentroCostoEjecutaHonorarios(
								dtoAsociosCx.getCodigoDetalleCargo())
						.getCodigo();
				centroCostoEjecuta = (centroCostoEjecuta > 0) ? centroCostoEjecuta
						: centroCostoTempo;
				String numIdMedicoEjecutaSalas = UtilidadesSalas
						.obtenerNumIdMedicoSalaMaterialesCx(dtoAsociosCx
								.getCodigoDetalleCargo());
				String numIdMedicoEjecutaHonorarios = UtilidadesSalas
						.obtenerNumIdMedicoHonorariosCx(dtoAsociosCx
								.getCodigoDetalleCargo());
				String numIdMedicoEjecuta = "";

				if (!UtilidadTexto.isEmpty(numIdMedicoEjecutaSalas)) {
					numIdMedicoEjecuta = numIdMedicoEjecutaSalas;
				} else if (!UtilidadTexto.isEmpty(numIdMedicoEjecutaHonorarios)) {
					numIdMedicoEjecuta = numIdMedicoEjecutaHonorarios;
				} else {
					numIdMedicoEjecuta = infoInterfazMap.get(
							"numidmedicoejecuta").toString();
				}

				// ---------------
				logger.info("ccejecuta->" + centroCostoEjecuta
						+ " idMedEjecuta->" + numIdMedicoEjecuta);

				auxiliar = Servicios.obtenerCodigoTarifarioServicio(con,
						dtoAsociosCx.getCodigoServicioAsocio(),
						ConstantesBD.codigoTarifarioCups);

				DtoInterfazDetalleFactura dtoInterfazDetalleFacturaAsocios = new DtoInterfazDetalleFactura(
						tipoMovimiento /* tipoMovimiento */,
						UtilidadTexto.formatearExponenciales(dtoFactura
								.getConsecutivoFactura())
								+ ""/* documento */,
						codigoIngreso /* ingreso */,
						Utilidades.obtenerCentroCostoInterfaz(Integer
								.parseInt(infoInterfazMap.get("ccsolicitante")
										.toString())) /* centroCostoPaciente */,
						Utilidades
								.obtenerCentroCostoInterfaz(centroCostoEjecuta) /* centroCostoEjecuta */,
						conceptoFactura,
						Double.parseDouble(infoInterfazMap.get(
								"consecutivoordenmedica").toString()),
						dtoFactura.getViaIngreso().getCodigo(),
						infoInterfazMap.get("numidmedicosolicita").toString()/* medicoOrdena */,
						numIdMedicoEjecuta /* medicoEjecuta */,
						UtilidadFecha.getFechaActual()/* fechaConsumo */,
						UtilidadFecha.getHoraActual()/* horaConsumo */,
						cups,
						ConstantesBD.acronimoNo/* paquete */,
						1 /* cantidad */,
						auxiliar,
						dtoAsociosCx.getValorCargo()
								+ dtoAsociosCx.getValorRecargo()/* valorConsumoUnitario */,
						dtoAsociosCx.getValorTotal() /* valorConsumoTotal */,
						0 /* valorDescuentoTotalConsumo */,
						dtoAsociosCx.getValorTotal() /* valorNetoTotalConsumo */,
						numeroIdentificacionConvenio,
						numeroIdentificacionPersona/* terceroPaciente */,
						dtoFactura.getConvenio().getCodigo(), // CODIGO INTERFAZ
						// DEL CONVENIO
						ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado
								+ "" /* estadoRegistro */,
						ConstantesBD.acronimoNo/* contabilizado */, usuario
								.getCodigoInstitucionInt(),
						centroCostoPlanEspecialConvenio);

				resulBol=interfazUtil.insertarDetalleFactura(con,
						dtoFactura.getCuentas().get(0).toString(),
						dtoInterfazDetalleFacturaAsocios, false,
						indO1_cargoExcento, false);
				if (!resulBol.isTrue())
				{
					logger.error("no inserto la interfaz para el asocio codigo: "+ dtoAsociosCx.getCodigo());
					return new ResultadoBoolean (false,resulBol.getDescripcion());
				}
			}
		}

		if (dtoDetalleFactura.getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes) {
			for (int y = 0; y < dtoDetalleFactura
					.getPaquetizacionDetalleFactura().size(); y++) {
				DtoPaquetizacionDetalleFactura dtoPaquetizacionDetalleFactura = dtoDetalleFactura
						.getPaquetizacionDetalleFactura().get(y);
				String conceptoFacturaPq = "";
				String cupsPq = "";
				if (dtoPaquetizacionDetalleFactura.getArticulo().getCodigo() > 0)
					conceptoFacturaPq = Articulo
							.obtenerCodigoInterfazNaturalezaArticulo(con,
									dtoPaquetizacionDetalleFactura
											.getArticulo().getCodigo());
				else
					cupsPq = Servicios.obtenerCodigoTarifarioServicio(con,
							dtoPaquetizacionDetalleFactura.getServicio()
									.getCodigo(),
							ConstantesBD.codigoTarifarioCups);
				HashMap infoInterfazMapPq = Solicitud
						.obteneInfoSolicitudInterfaz(con,
								dtoPaquetizacionDetalleFactura.getSolicitud()
										+ "");

				String auxiliarPq = "";
				if (dtoPaquetizacionDetalleFactura.getServicio().getCodigo() > 0) {
					auxiliarPq = cupsPq;
				} else {
					auxiliarPq = dtoPaquetizacionDetalleFactura.getArticulo()
							.getCodigo()
							+ "";
				}

				int tipoSolicitudPq = Utilidades
						.convertirAEntero((infoInterfazMapPq.get("tipo") + ""));
				int centroCostoTempoPq = (tipoSolicitudPq == ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos || tipoSolicitudPq == ConstantesBD.codigoTipoSolicitudMedicamentos) ? Utilidades
						.convertirAEntero(infoInterfazMapPq.get("ccprincipal")
								+ "")
						: Utilidades.convertirAEntero(infoInterfazMapPq
								.get("ccsolicitado")
								+ "");

				// ANEXO 777 ---------
				int centroCostoEjecuta = UtilidadesSalas
						.obtenerCentroCostoEjecutaHonorarios(
								dtoPaquetizacionDetalleFactura
										.getCodigoDetalleCargo()).getCodigo();
				centroCostoEjecuta = (centroCostoEjecuta > 0) ? centroCostoEjecuta
						: centroCostoTempoPq;
				logger
						.info("*****************************************************************8888888888888888888888888888888888888888888888------------------***************************************");
				logger
						.info("\n\n\n\n ************************************************** MEDICO QUE EJECUTA INTERFAZ **********************************************************************************");
				Utilidades.imprimirMapa(infoInterfazMapPq);

				String numIdMedicoEjecuta = UtilidadesSalas
						.obtenerNumIdMedicoSalaMaterialesCx(dtoPaquetizacionDetalleFactura
								.getCodigoDetalleCargo());

				String numIdMedicoEjecutaSalas = UtilidadesSalas
						.obtenerNumIdMedicoSalaMaterialesCx(dtoPaquetizacionDetalleFactura
								.getCodigoDetalleCargo());
				String numIdMedicoEjecutaHonorarios = UtilidadesSalas
						.obtenerNumIdMedicoHonorariosCx(dtoPaquetizacionDetalleFactura
								.getCodigoDetalleCargo());
				numIdMedicoEjecuta = "";

				if (!UtilidadTexto.isEmpty(numIdMedicoEjecutaSalas)) {
					numIdMedicoEjecuta = numIdMedicoEjecutaSalas;
				} else if (!UtilidadTexto.isEmpty(numIdMedicoEjecutaHonorarios)) {
					numIdMedicoEjecuta = numIdMedicoEjecutaHonorarios;
				} else {
					numIdMedicoEjecuta = infoInterfazMapPq.get(
							"numidmedicoejecuta").toString();
				}
				logger.info("numIdMedicoEjecuta de salas materiales cx-->"
						+ numIdMedicoEjecuta + "<----");
				// -------------------

				DtoInterfazDetalleFactura dtoInterfazDetallePaqFactura = new DtoInterfazDetalleFactura(
						tipoMovimiento /* tipoMovimiento */,
						UtilidadTexto.formatearExponenciales(dtoFactura
								.getConsecutivoFactura())/* documento */,
						codigoIngreso /* ingreso */,
						Utilidades.obtenerCentroCostoInterfaz(Integer
								.parseInt(infoInterfazMapPq
										.get("ccsolicitante").toString())) /* centroCostoPaciente */,
						Utilidades
								.obtenerCentroCostoInterfaz(centroCostoEjecuta) /* centroCostoEjecuta */,
						conceptoFacturaPq,
						Double.parseDouble(infoInterfazMapPq.get(
								"consecutivoordenmedica").toString()),
						dtoFactura.getViaIngreso().getCodigo(),
						infoInterfazMapPq.get("numidmedicosolicita").toString()/* medicoOrdena */,
						numIdMedicoEjecuta/* medicoEjecuta */,
						UtilidadFecha.getFechaActual()/* fechaConsumo */,
						UtilidadFecha.getHoraActual()/* horaConsumo */,
						cupsPq,
						ConstantesBD.acronimoSi/* paquete */,
						dtoPaquetizacionDetalleFactura.getCantidadCargo(),
						auxiliarPq,
						dtoPaquetizacionDetalleFactura.getValorCargo()
								+ dtoPaquetizacionDetalleFactura
										.getValorRecargo()/* valorConsumoUnitario */,
						dtoPaquetizacionDetalleFactura.getValorTotal() /* valorConsumoTotal */,
						dtoPaquetizacionDetalleFactura
								.getValorDifConsumoValor() /* valorDescuentoTotalConsumo */,
						dtoPaquetizacionDetalleFactura.getValorTotal()
								- dtoPaquetizacionDetalleFactura
										.getValorDifConsumoValor() /* valorNetoTotalConsumo */,
						numeroIdentificacionConvenio,
						numeroIdentificacionPersona/* terceroPaciente */,
						dtoFactura.getConvenio().getCodigo(),
						ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado
								+ "" /* estadoRegistro */,
						ConstantesBD.acronimoNo/* contabilizado */, usuario
								.getCodigoInstitucionInt(),
						centroCostoPlanEspecialConvenio);
				
				resulBol=interfazUtil.insertarDetalleFactura(con,
						dtoFactura.getCuentas().get(0).toString(),
						dtoInterfazDetallePaqFactura, true, indO1_cargoExcento,
						false);
				if (!resulBol.isTrue()) {
					logger.error("no inserto la interfaz para el detalle del paquete de la solicitud "+ dtoDetalleFactura.getNumeroSolicitud());
					return new ResultadoBoolean (false,resulBol.getDescripcion());
				}
			}// for

		}// else
		return new ResultadoBoolean (true,"");
	}
	
	

	/**
	 * 
	 * @param usuario
	 * @param dtoInterfazAbonos
	 * @return
	 */
	public static ResultadoBoolean insertarInterfazTesoreria(	UsuarioBasico usuario, 
																DtoInterfazAbonos dtoInterfazAbonos) 
	{
		//1. PRIMERO SE ACTUALIZA LA INFORMACION DEL PACIENTE
		
		ResultadoBoolean resp = new ResultadoBoolean(true,"");
		UtilidadBDInterfaz utilidadInterfaz = new UtilidadBDInterfaz();
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(usuario.getCodigoInstitucionInt())))
		{
			//Se verifica si existe registro para el paciente
			DtoInterfazPaciente dto = utilidadInterfaz.cargarPaciente(dtoInterfazAbonos.getCodigoPaciente(), usuario.getCodigoInstitucionInt());
			dto.setInstitucion(usuario.getCodigoInstitucionInt());
			
			if(!dto.getCodigo().equals(""))
			{
				dto.setEstadoIngreso(ConstantesBDInterfaz.codigoEstadoIngresoInactivoPaciente);
				dto.setEstadoRegistro(ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+"");
				dto.setUsuario(usuario.getLoginUsuario());
				//Se modifica el registro de la interfaz de tesoreria
				logger.info("<--------------ACTUALIZO EL REGISTRO DEL PACIENTE EN AX_PACIEN al FACTURAR--->");
				logger.info("<-------Estado del Ingreso------>"+ConstantesBDInterfaz.codigoEstadoIngresoInactivoPaciente+"<--");
				logger.info("<-------Estado del Registro----->"+ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+"<--");
				logger.info("<---------------------------------------------------------------------------->");
				resp = utilidadInterfaz.modificarPaciente(dto);
			}
		}
		
		//2. SEGUNDO SE INSERTA LA INFORMACION DE ABONOS
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazAbonosTesoreria(usuario.getCodigoInstitucionInt())) && resp.isTrue())
		{
			//Se modifica el registro de la interfaz de tesoreria
			resp = utilidadInterfaz.insertarAbono(dtoInterfazAbonos);
		}
		return resp;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean insertarAbonos(DtoInterfazAbonos dto)
	{
		logger.info("************INSERCION DE LA FACTURA ABONOS!!!");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaAbonos, dto.getInstitucion());
		String fechaCreacion=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
		String horaCreacion=UtilidadFecha.getHoraSegundosActual().replace(":", "");
		if(!nombreTabla.trim().equals(""))
		{
			String cadenaIsercion="INSERT INTO "+nombreTabla+" (	 codpac, "+   	//1 numeric(10,0) |
																	 "ndocum, "+   	//2 numeric(10,0) |
																	 "tipomov, "+  	//3 numeric(2,0)  |
																	 "signo, "+    	//4 character(1)  |
																	 "valor, "+    	//5 numeric(15,2) |
																	 "fechacre, "+ 	//6 numeric(8,0)  |
																	 "horacre, "+  	//7 numeric(6,0)  |
																	 "estreg, "+   	//8 numeric(2,0)  |
																	 "fechamod, "+ 	//9 numeric(8,0)  |
																	 "horamod) "+  	//10 numeric(6,0)  |
																	"values " +
																	"("+Double.parseDouble(dto.getCodigoPaciente())+", " +//1
																	""+Double.parseDouble(dto.getNumeroDocumento())+", " +//2
																	""+Integer.parseInt(dto.getTipoMov())+", " +//3
																	"'"+dto.getSigno()+"', " +//4
																	""+Double.parseDouble(dto.getValor())+"," +		//5
																	""+Double.parseDouble(fechaCreacion)+", " +//6
																	""+Double.parseDouble(horaCreacion)+", " +//7
																	""+dto.getEstadoRegistro()+", " +//8
																	""+Double.parseDouble(fechaCreacion)+", " +//9
																	""+Double.parseDouble(horaCreacion)+")";		//10
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz no hay conexi�n.");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
				logger.warn("insercion->"+cadenaIsercion);
				/*ps.setDouble(1, Double.parseDouble(dto.getCodigoPaciente()));
				ps.setDouble(2, Double.parseDouble(dto.getNumeroDocumento()));
				ps.setInt(3, Integer.parseInt(dto.getTipoMov()));
				ps.setString(4, dto.getSigno());
				ps.setDouble(5, Double.parseDouble(dto.getValor()));
				ps.setDouble(6, Double.parseDouble(fechaCreacion));
				ps.setDouble(7, Double.parseDouble(horaCreacion));
				ps.setString(8, dto.getEstadoRegistro());
				ps.setDouble(9, Double.parseDouble(fechaCreacion));
				ps.setDouble(10, Double.parseDouble(horaCreacion));*/
				
				logger.info("VA HA INSERTAR!!!!");
				if(ps.executeUpdate()>0)
				{
					logger.info("INSERTO INTERFAZ ABONOS!!!!*****************");
					ps.close();
					cerrarConexion(con);
					return new ResultadoBoolean(true,"El registro se inserto correctamente");
				}
				logger.error("El registro no se pudo Insertar***********************");
				ps.close();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz no hay conexi�n.");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				cerrarConexion(con);
				return new ResultadoBoolean(false,"Proceso cancelado, no se pudo registrar informaci�n de pacientes en la interfaz ocurrio una excepción.");
			}
		}
		else
		{
			logger.error("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE ABONOS");
			return new ResultadoBoolean(false,"No esta definida la tabla para el manejo de abonos con la interfaz ");
		}
	}
	
	/**
	 * 
	 * Metodo para actualizar la informacion de interfaz inventarios shaio, retorna un objeto que simila
	 * el errores struts, es decir si es nulo es que todo salio sin errores.
	 * 
	 * keys( codigosArt_, 
	 * 		cantidadADespachar_, 
	 * 		tipodespacho_, 
	 * 		proveedorCompra_, 
	 * 		almacenConsignacion_, 
	 * 		lote_, 
	 * 		fechavencimiento_, 
	 * 		manejaLoteArticulo_, 
	 * 		proveedorCatalogo_)
	 * 
	 * @param con
	 * @param mapa
	 * @param usuario
	 * @return
	 */
	public static ElementoApResource actualizarInterfazInventarios(Connection con, HashMap mapa, UsuarioBasico usuario,PersonaBasica paciente,HashMap alertas)
	{
		logger.info("\n\n*************************************************ACTUALIZAR INTERFAZ INVENTARIOS********************************************************************");
		logger.info("MAPA------------->");
		Utilidades.imprimirMapa(mapa);
		
		//debe estar definido el consecutivo de inventarios
		int codigo=0;
		int cantidadDespacho=0;
		for(int i=0;i<Integer.parseInt(mapa.get("numRegistros").toString()); i++)
		{
			//se toma el codigo del articulo
	       	codigo = Integer.parseInt(mapa.get("codigosArt_"+i)+"");
	        //se toma la cantidad a despachar
	        cantidadDespacho = Integer.parseInt(mapa.get("cantidadADespachar_"+i)+"");
	        double valorUnitario=0;
	        double valorCompraMasAlta=0;
	        boolean transaccionExitosa=false;
	        valorCompraMasAlta=UtilidadInventarios.obtenerValorCompraMasAlta(con, codigo);
	        
	        logger.info("\n\n CODIGO ARTICULO-->"+codigo+"  CANTIDAD-->"+cantidadDespacho+" TIPO_DESPACHO-->"+mapa.get("tipodespacho_"+i));
	        
	        if(mapa.containsKey("tipodespacho_"+i)	&& !UtilidadTexto.isEmpty(mapa.get("tipodespacho_"+i)+""))
	        {
	        	if((mapa.get("tipodespacho_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
		        {
	        		logger.info("el tipo de despacho es de consignacion!!!!!!!");
		        	valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorConveProveedor(con, mapa.get("proveedorCompra_"+i)+"",codigo);
		        	logger.info("valor unitario->"+valorUnitario);
		        	
		        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
		        	logger.info("tipo consecutivo-->"+tipoConsecutivo);
		        	
			        int tipoTransaccion1=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionSalidaConsignacionConsumos,usuario.getCodigoInstitucionInt());
			        logger.info("salidatipoTransaccion->"+tipoTransaccion1);
			        ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
			        String consecutivo="";
			            
			        int codigoAlmacen=Utilidades.convertirAEntero(mapa.get("almacenConsignacion_"+i)+"");
			        logger.info("codigoAlmacen-->"+codigoAlmacen);
			        int codigoAlmacenConsecutivo=0;
			            
			        if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
			        	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
			        else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
			        	codigoAlmacenConsecutivo=Utilidades.convertirAEntero(mapa.get("almacenConsignacion_"+i)+""); 

			        logger.info("codigoAlmacenConsecutivo->"+codigoAlmacenConsecutivo);
			        //validacion consecutivo
			        consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
			        
			        logger.info("consecutivo->"+consecutivo);
			        
			        if(Integer.parseInt(consecutivo)<=0)
			        {
			        	ElementoApResource elem=new ElementoApResource("error.inventarios.faltaDefinirConsecutivoTransAlmacen");
			    		elem.agregarAtributo(UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion1,usuario.getCodigoInstitucionInt()));
			    		elem.agregarAtributo(Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt()));
			    		return elem;		                      
			        }
			        
			        ArrayList filtro1=new ArrayList();
			        filtro1.add(tipoTransaccion1+"");
			        filtro1.add(usuario.getCodigoInstitucionInt()+"");
			        filtro1.add(codigoAlmacenConsecutivo+"");
			        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
		
			        int codTransaccion1=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion1,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
					consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt());
		
					transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion1,codigo,cantidadDespacho,valorUnitario+"",(mapa.get("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+i)+""),mapa.get("proveedorCompra_"+i)+"",mapa.get("proveedorCatalogo_"+i)+"");
					
					if(!transaccionExitosa)
					{
						ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
						return elem;
					}
					
					transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion1+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
					
					if(!transaccionExitosa)
					{
						ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
						return elem;
					}
				
					logger.info("manejaLote->"+mapa.get("manejaLoteArticulo_"+i));
					
					if(UtilidadTexto.getBoolean(mapa.get("manejaLoteArticulo_"+i)+"") )
					{	
						logger.info("VA POR LOTE ENTONCES VA A ACTUALIZAR EXISTENCIAS ARTICULO X LOTE!!!!!!");
						try 
						{
							transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigo,codigoAlmacen,false,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,(mapa.get("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+i)+""));
							if(!transaccionExitosa)
							{
								ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
								return elem;
							}
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
					}	
					else
					{	
						logger.info("VA sinnnnnnnnn LOTE ENTONCES VA A ACTUALIZAR EXISTENCIAS ARTICULO!!!!!!");
				   		try 
				   		{
				   			transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigo,codigoAlmacen,false,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
							if(!transaccionExitosa)
							{
								ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
								return elem;
							}
						} 
				   		catch (SQLException e) 
				   		{
							e.printStackTrace();
						}
					}	
						    
					tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
					
					logger.info("tipoConsecutivo->"+tipoConsecutivo);
					
					int tipoTransaccion2=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionEntradaCompraConsignacion,usuario.getCodigoInstitucionInt());
				    
					logger.info("entradatipoTransaccion2->"+tipoTransaccion2);
					
				    consecutivo="";
				    codigoAlmacen=usuario.getCodigoCentroCosto(); 
				    codigoAlmacenConsecutivo=0;
				            
				    if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
				       	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
				    else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
				      	codigoAlmacenConsecutivo=usuario.getCodigoCentroCosto();
				    
				    logger.info("codigoAlmacenConsecutivo->"+codigoAlmacenConsecutivo);
				    //validacion consecutivo
				    consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion2,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
				    
				    logger.info("consecutivo->"+consecutivo);
				    
				    if(Integer.parseInt(consecutivo)<=0)
			        {
				    	ElementoApResource elem=new ElementoApResource("error.inventarios.faltaDefinirConsecutivoTransAlmacen");
			    		elem.agregarAtributo(UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion2,usuario.getCodigoInstitucionInt()));
			    		elem.agregarAtributo(Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt()));
			    		return elem;
				    }
				    
				    filtro1=new ArrayList();
					filtro1.add(tipoTransaccion2+"");
					filtro1.add(usuario.getCodigoInstitucionInt()+"");
					filtro1.add(codigoAlmacenConsecutivo+"");
					UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
			
					int codTransaccion2=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion2,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
					
					logger.info("codTransaccion2->"+codTransaccion2);
					consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion2,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt());
			
					transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion2,codigo,cantidadDespacho,valorUnitario+"",(mapa.get("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+i)+""),mapa.get("proveedorCompra_"+i)+"",mapa.get("proveedorCatalogo_"+i)+"");
					
					if(!transaccionExitosa)
					{
						ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
						return elem;
					}
					
					transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion2+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
					
					if(!transaccionExitosa)
					{
						ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
						return elem;
					}
					
					if(UtilidadTexto.getBoolean(mapa.get("manejaLoteArticulo_"+i)+"") )
					{	
						logger.info("VA POR LOTE ENTONCES VA A ACTUALIZAR EXISTENCIAS ARTICULO X LOTE!!!!!!");
						try 
						{
							transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,(mapa.get("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+i)+""));
							
							if(!transaccionExitosa)
							{
								ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
								return elem;
							}
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
					}	
					else
					{	
						logger.info("VA sinnnnnnnnn LOTE ENTONCES VA A ACTUALIZAR EXISTENCIAS ARTICULO!!!!!!");
						try 
						{
							transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
							
							if(!transaccionExitosa)
							{
								ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
								return elem;
							}
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
					}	
							    
				    UtilidadInventarios.actualizarPrecioUltimaCompra(con,codigo, valorUnitario);
				    
				    if(valorUnitario>valorCompraMasAlta)
				    {
				    	UtilidadInventarios.actualizarPrecioCompraMasAlta(con,codigo, valorUnitario);
				    }
					Double valorIva = UtilidadInventarios.obtenerValorIvaArticuloProveedorConveProveedor(con,mapa.get("proveedorCompra_"+i)+"",codigo);
				    //OJO DESCOMENTAR
					//modificado por anexo 779
				    ResultadoInteger resultadoInteger=generarRegistroTransaccionInterfazConsignacion(	con,
																	usuario.getCodigoInstitucionInt(),
							    									codigo,
							    									codTransaccion1,
							    									codTransaccion2,
							    									tipoTransaccion1,
							    									tipoTransaccion2,
							    									ConstantesBD.codigoTransaccionSalidaConsignacionConsumos,
							    									ConstantesBD.codigoTransaccionEntradaCompraConsignacion,
							    									cantidadDespacho,
							    									valorUnitario,
							    									valorIva,
							    									mapa.get("proveedorCompra_"+i)+"",
							    									paciente, 
							    									codigoAlmacen, 
							    									mapa.get("ccsolicita_"+i)+"".trim(),
							    									mapa.get("ccejecuta_"+i)+"".trim(),
							    									mapa.get("almacenConsignacion_"+i)+"".trim(),
							    									usuario.getLoginUsuario());
				    
				    if (UtilidadCadena.noEsVacio(resultadoInteger.getDescripcion()))
				    	if (UtilidadCadena.noEsVacio(alertas.get("numRegistros")+""))
				    	{
				    		alertas.put("mensaje_"+alertas.get("numRegistros"), resultadoInteger.getDescripcion());
				    		alertas.put("numRegistros", Utilidades.convertirAEntero(alertas.get("numRegistros")+"")+1);
				    	}
				    	else
				    	{
				    		alertas.put("mensaje_0", resultadoInteger.getDescripcion());
				    		alertas.put("numRegistros", 0);
				    	}
				    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
				    
		        }//if tipo consignacion
	           	else if((mapa.get("tipodespacho_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
		        {
	           		logger.info("el tipo de despacho compra proveedor !!!!!!!");
	           		
		        	valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorCatalogoProveedor(con,mapa.get("proveedorCatalogo_"+i)+"",codigo);
		        	logger.info("valor unitario->"+valorUnitario);
		        	
		        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
		        	logger.info("tipoConsecutivo->"+tipoConsecutivo);
		        	int tipoTransaccion=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionEntradaComprasProveedor,usuario.getCodigoInstitucionInt());
			        ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
			        String consecutivo="";
			            
			        int codigoAlmacen=usuario.getCodigoCentroCosto(); 
			        int codigoAlmacenConsecutivo=0;
			            
			        if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
			        	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
			        else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
			        	codigoAlmacenConsecutivo=usuario.getCodigoCentroCosto(); 

		            //validacion consecutivo
		            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
			        
		            if(Integer.parseInt(consecutivo)<=0)
			        {
		            	ElementoApResource elem=new ElementoApResource("error.inventarios.faltaDefinirConsecutivoTransAlmacen");
			    		elem.agregarAtributo(UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion,usuario.getCodigoInstitucionInt()));
			    		elem.agregarAtributo(Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt()));
			    		return elem;
		            }
		            
		            ArrayList filtro1=new ArrayList();
		            filtro1.add(tipoTransaccion+"");
		            filtro1.add(usuario.getCodigoInstitucionInt()+"");
		            filtro1.add(codigoAlmacenConsecutivo+"");
		            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
		
		            int codTransaccion=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
						     
		            consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt());
		
		            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion,codigo,cantidadDespacho,valorUnitario+"",(mapa.get("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+i)+""),mapa.get("proveedorCompra_"+i)+"",mapa.get("proveedorCatalogo_"+i)+"");
		            
		            if(!transaccionExitosa)
					{
						ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
						return elem;
					}
		            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
		            
		            if(!transaccionExitosa)
					{
						ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
						return elem;
					}
		            
		            if(UtilidadTexto.getBoolean(mapa.get("manejaLoteArticulo_"+i)+"") )
		            {	
		            	try 
		            	{
							transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,(mapa.get("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+i)+""));
							if(!transaccionExitosa)
							{
								ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
								return elem;
							}
						} 
		            	catch (SQLException e) 
		            	{
							e.printStackTrace();
						}
		            }	
		            else
		            {	
		            	try 
		            	{
							transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
							if(!transaccionExitosa)
							{
								ElementoApResource elem= new ElementoApResource("error.interfazInventarios.transaccion");
								return elem;
							}
						} 
		            	catch (SQLException e) 
		            	{
							e.printStackTrace();
						}
		            }	
						    
						    
		            UtilidadInventarios.actualizarPrecioUltimaCompra(con,codigo, valorUnitario);
					
		            if(valorUnitario>valorCompraMasAlta)
				    {
				    	UtilidadInventarios.actualizarPrecioCompraMasAlta(con,codigo, valorUnitario);
				    }
		            
		            Double valorIva = UtilidadInventarios.obtenerValorIvaArticuloProveedorCatalogoProveedor(con,mapa.get("proveedorCatalogo_"+i)+"",codigo);
		            
					generarRegistroTransaccionInterfazCompraProveedor(	con,
																		usuario.getCodigoInstitucionInt(),
																		codigo,
																		codTransaccion,
																		tipoTransaccion,
																		ConstantesBD.codigoTransaccionEntradaComprasProveedor,
																		cantidadDespacho,
																		valorUnitario,
																		valorIva,
																		mapa.get("proveedorCatalogo_"+i)+"",paciente, 
																		codigoAlmacen,
																		mapa.get("ccsolicita_"+i)+"".trim(),
								    									mapa.get("ccejecuta_"+i)+"".trim(),
								    									usuario.getLoginUsuario());
												

			            
		        }
		    }
		}
		
		logger.info("\n\n*************************************************FIN ACTUALIZAR INTERFAZ INVENTARIOS********************************************************************");
		
		return null;
	}
	
	
	/**
	 * 
	 * @param con 
	 * @param codigoArticulo 
	 * @param forma
	 * @param codTransaccion 
	 * @param codTransaccion1 
	 * @param tipoTransaccion2 
	 * @param tipoTransaccion1 
	 * @param tipoTransInterfaz2 
	 * @param tipoTransInterfaz1 
	 * @param cantidadDespacho 
	 * @param codigoAlmacen 
	 * @param centroCostoSolicita 
	 * @param centroCostoEjecuta 
	 * @param codigoAlmacenConsignacion
	 * @param usuario 
	 */
	private static ResultadoInteger generarRegistroTransaccionInterfazConsignacion(Connection con,int institucion, int codigoArticulo, int codTransaccion1, int codTransaccion2, int tipoTransaccion1, int tipoTransaccion2, int tipoTransInterfaz1, int tipoTransInterfaz2, int cantidadDespacho, double valorUnitario,double valorIva,String proveedor,PersonaBasica paciente, int codigoAlmacen, String centroCostoSolicita, String centroCostoEjecuta, String almacenConsignacion, String usuario) 
	{
		UtilidadBDInventarios utilInterfaz=new UtilidadBDInventarios();
		DtoInterfazTransaccionAxInv dto= new DtoInterfazTransaccionAxInv();
		dto.setTipoTransAxioma(tipoTransaccion1+"");
		dto.setNumeroTransaccionAxioma(codTransaccion1+"");
		dto.setIndicativoTransaccion(tipoTransInterfaz1+"");
		dto.setOrigenTransaccion("2");//alimentacion por axioma
		dto.setIndicativoCostoDonacion(ConstantesBD.acronimoNo);
		dto.setFechaTransaccion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraTransaccion(UtilidadFecha.getHoraActual());
		dto.setCodigoArticuloInterfaz(UtilidadInventarios.obtenerCodigoInterfazArticulo(con,codigoArticulo));
		dto.setCodigoArticulo(codigoArticulo+"");
		dto.setCantidad(cantidadDespacho+"");
		
		logger.info("VALOR TOTAL --->"+UtilidadTexto.formatearValores(valorUnitario+"","#.######")+"<-");
		logger.info("VALOR IVA   --->"+UtilidadTexto.formatearValores(valorIva+"","#.######")+"<-");
		double resultado = valorUnitario-valorIva; 
		logger.info("VALOR RESULTADO --->"+UtilidadTexto.formatearValores(resultado+"","#.######")+"<-");
		
		dto.setValorUnitario(UtilidadTexto.formatearValores(resultado+"","#.######"));
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		dto.setIdentificacionProveedor(proveedor);
		dto.setCodigoPaciente(paciente.getCodigoPersona()+"");
		dto.setIngresoPaciente(paciente.getConsecutivoIngreso());
		dto.setFechaTransaccion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraTransaccion(UtilidadFecha.getHoraActual());
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita);
		dto.setAlmacendespacha(centroCostoEjecuta);
		dto.setAlmacenConsignacion(almacenConsignacion);
		
		dto.setUsuario(usuario);
		
		logger.info("inserta la primera-->");
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
	
		
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoArticulo
	 * @param forma
	 * @param codTransaccion
	 * @param tipoTransaccion
	 * @param tipoTransInterfaz
	 * @param cantidadDespacho
	 * @param valorUnitario
	 * @param valorIva
	 * @param codigoAlmacen 
	 * @param centroCostoEjecuta 
	 * @param centroCostoSolicita 
	 * @param usuario 
	 */
	private static ResultadoInteger generarRegistroTransaccionInterfazCompraProveedor(Connection con,int institucion, int codigoArticulo, int codTransaccion, int tipoTransaccion, int tipoTransInterfaz, int cantidadDespacho, double valorUnitario,double valorIva, String proveedor,PersonaBasica paciente, int codigoAlmacen, String centroCostoSolicita, String centroCostoEjecuta, String usuario) 
	{
		UtilidadBDInventarios utilInterfaz=new UtilidadBDInventarios();
		DtoInterfazTransaccionAxInv dto= new DtoInterfazTransaccionAxInv();
		dto.setTipoTransAxioma(tipoTransaccion+"");
		dto.setNumeroTransaccionAxioma(codTransaccion+"");
		dto.setIndicativoTransaccion(tipoTransInterfaz+"");
		dto.setOrigenTransaccion("2");//alimentacion por axioma
		dto.setIndicativoCostoDonacion(ConstantesBD.acronimoNo);
		dto.setFechaTransaccion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraTransaccion(UtilidadFecha.getHoraActual());
		dto.setCodigoArticuloInterfaz(UtilidadInventarios.obtenerCodigoInterfazArticulo(con,codigoArticulo));
		dto.setCodigoArticulo(codigoArticulo+"");
		dto.setCantidad(cantidadDespacho+"");
		
		logger.info("VALOR TOTAL --->"+UtilidadTexto.formatearValores(valorUnitario+"","#.######")+"<-");
		logger.info("VALOR IVA   --->"+UtilidadTexto.formatearValores(valorIva+"","#.######")+"<-");
		double resultado = valorUnitario-valorIva; 
		logger.info("VALOR RESULTADO --->"+UtilidadTexto.formatearValores(resultado+"","#.######")+"<-");
		
		dto.setValorUnitario(UtilidadTexto.formatearValores(resultado+"","#.######"));
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		dto.setIdentificacionProveedor(proveedor);
		dto.setCodigoPaciente(paciente.getCodigoPersona()+"");
		dto.setIngresoPaciente(paciente.getConsecutivoIngreso());
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita);
		dto.setAlmacendespacha(centroCostoEjecuta);
		
		dto.setUsuario(usuario);
		
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
	}
	
	
	/**
	 * Consulta la tabla ax_rips devuelve un array de DtoInterfazRips, donde se almacena el 
	 * Numero de Envio y dentro del Dto un array de InfoDatos relacionando convenio con numero de factura
	 * @param int institucion
	 * @param String camposTabla 
	 */
	public  HashMap consultarInterfazRips(Connection conLocal,int institucion)
	{
		HashMap respuesta = new HashMap();
		respuesta.put("error","");
		String nombreTabla = Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaAxRips,institucion);
		String conveniosParametrica = "", cadenaLocal="";
		ArrayList<DtoInterfazAxRips> array = new ArrayList<DtoInterfazAxRips>();
		ArrayList<InfoDatosString> arrayFacturas;
		ResultSetDecorator rs;
		DtoInterfazAxRips dto;
		Connection con = null;
		int pos=0;
				
		logger.info("nombre tabla Interfaz "+nombreTabla);
		
		respuesta.put("array",array);
		respuesta.put("resultado","");
		
		if(!nombreTabla.trim().equals(""))
		{
						
			String cadena = "SELECT ap.convenio " +
			"FROM archivo_plano_colsanitas ap " +			
			"WHERE ap.institucion = ? ";
								
			try 
			{								
				//Consulta los Convenios de la parametrizacion de Convenios Colsanitas
				PreparedStatementDecorator ps = new PreparedStatementDecorator(conLocal.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,institucion);				
				rs =new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())							
				{					
					conveniosParametrica += "'"+rs.getString(1).trim()+"',";				
				}				
												
				if(conveniosParametrica.equals(ConstantesBD.codigoNuncaValido+"") || conveniosParametrica.equals(""))
				{
					respuesta.put("error","error.facturacion.paramConveniosColSa");
					return respuesta;
				}
				else				
					conveniosParametrica = conveniosParametrica.substring(0,conveniosParametrica.length()-1);													
				//----------------------------	
				
								
				//Consulta numeros de envio y convenios de la interfaz				
				cadena = "SELECT envrip, convrip FROM "+nombreTabla+" WHERE convrip IN("+conveniosParametrica+") GROUP BY envrip,convrip ORDER BY envrip ASC ";
				
				logger.info("cadena para la interfaz >>  "+cadena);
				
				ResultSetDecorator rs_convenio;
				con = abrirConexionInterfaz();
				
				
				if(con==null || con.isClosed())
				{
					respuesta.put("error","error.interfaz.generacionInterfaz.falloConexion");
					logger.error("CONEXION CERRADA.");					
					return respuesta;							
				}				

				logger.info("cadena para la interfaz >>  "+cadena);
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena));				
				rs =new ResultSetDecorator(ps.executeQuery());
				//-----------------------------
				
				//Prepara la consulta de las facturas relacionadas al numero de envio				
				cadena = "SELECT convrip,facrip " +
						 "FROM "+nombreTabla+" " +
						 "WHERE envrip = ? " +
						 "AND convrip IN("+conveniosParametrica+") " +
						 "GROUP BY convrip, facrip " +
						 "ORDER BY convrip ASC ";
				
				logger.info("cadena para la interfaz 2 >>  "+cadena);
				PreparedStatementDecorator ps_convenio =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//--------------------------------
				
				
				//recorre los numero de envio de la consulta
				pos = 0;
				while(rs.next())
				{	
					//Instancia el Dto de Interfaz Ax Rips
					arrayFacturas = new ArrayList<InfoDatosString>();
					dto = new DtoInterfazAxRips();					
					dto.setNumeroEnvio(rs.getString(1).trim());
					dto.setCodigoConvenio(rs.getString(2).trim());
					dto.setIndicadorPos(pos);
					pos++;			
					//------------------------------------
					
					//Consulta las facturas relacionadas al numero de envio
					ps_convenio =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps_convenio.setInt(1,rs.getInt(1));					
					rs_convenio = new ResultSetDecorator(ps_convenio.executeQuery());
					
					//recorre los convenios y facturas asociados al numero de envio
					while(rs_convenio.next())					
						arrayFacturas.add(new InfoDatosString(rs_convenio.getString(1).trim(),rs_convenio.getString(2).trim()));
					
					ps_convenio.clearParameters();
					rs_convenio.close();
					dto.setFacturaConvenio(arrayFacturas);
					//------------------------------------------------------------------
										
					//adicciona la informacion del Dto
					cadenaLocal = "SELECT nombre FROM  convenios WHERE codigo = ? AND institucion = ?";					
					ps_convenio = new PreparedStatementDecorator(conLocal.prepareStatement(cadenaLocal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps_convenio.setInt(1,Utilidades.convertirAEntero(dto.getCodigoConvenio()));
					ps_convenio.setInt(2,institucion);
					
					rs_convenio = new ResultSetDecorator(ps_convenio.executeQuery());
					
					if(rs_convenio.next())
						dto.setDescripcionConvenio(rs_convenio.getString(1).trim());	
					else
						dto.setDescripcionConvenio("");
					
					
					ps_convenio.clearParameters();
					rs_convenio.close();
					array.add(dto);					
					//--------------------------------------
				}	
				
				logger.info("Devuelve Array de DTO");
				respuesta.put("array",array);				
				return respuesta;
			}
			catch (SQLException e) {
				logger.info("error >> "+cadena+" >> "+cadenaLocal);
				cerrarConexion(con);
				respuesta.put("error","error.interfaz.generacionInterfaz.error.interfaz.generacionInterfaz.falloConexion");
				return respuesta;
			}
		}
		
		cerrarConexion(con);
		return respuesta;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoIntPar
	 * @param generarReproceso
	 * @param descripcionConcepto
	 * @param numeroRC 
	 * @throws SQLException 
	 */
	public static void ejecucionInterfazRC(int codigoInstitucion, String fechaInicial, String fechaFinal, String tipoIntPar, String generarReproceso, String descripcionConcepto, String numeroRC) throws SQLException 
	{
		Connection con=UtilidadBD.abrirConexion();
		logger.info("EJECUCION DEL PROCESO INTERFAZ SYNERSIS");
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("select synersis.interfaz_rc(?,?,?,?,?,?,?)"));
		ps.setObject(1, codigoInstitucion+"");
		ps.setObject(2, UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"");
		ps.setObject(3, UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"");
		ps.setObject(4, tipoIntPar+"");
		ps.setObject(5, generarReproceso+"");
		ps.setObject(6, descripcionConcepto+"");
		ps.setObject(7, numeroRC+"");
		ps.executeQuery();
		logger.info("FINALIZO EJECUCION DEL PROCESO INTERFAZ SYNERSIS");
	}

	/**
	 * 
	 * @param arrayDto
	 * @param institucion
	 * @param isReproceso
	 */
	public void insertarInterfazNutricionReproceso(ArrayList<DtoInterfazNutricion> arrayDto,String institucion,boolean isReproceso)
	{
		for (int i=0;i<arrayDto.size();i++)
			insertarInterfazNutricion(arrayDto.get(i), institucion, isReproceso);
	}
	
	/**
	 * INSERTA REGISTROS EN LA TABLA ax_nutri
	 * Modificado por anexo 779
	 * @param dto
	 * @param institucion 
	 */
	public ResultadoBoolean insertarInterfazNutricion(DtoInterfazNutricion dto, String institucion,boolean isReproceso) 
	{
		logger.info("************INSERCION DE LA LA NUTRICION ORAL !!!");
		
		/*
		logger.info("dto.getIngreso()->"+dto.getIngreso()+"<-");
		logger.info("dto.getViaing()->"+dto.getViaing()+"<-");
		logger.info("dto.getSecama()->"+dto.getSecama()+"<-");
		logger.info("dto.getNucama()->"+dto.getNucama()+"<-");
		logger.info("dto.getNumhis()->"+dto.getNumhis()+"<-");
		logger.info("dto.getCoddie()->"+dto.getCoddie()+"<-");
		logger.info("dto.getDescr1()->"+dto.getDescr1()+"<-");
		logger.info("dto.getDescr2()->"+dto.getDescr2()+"<-");
		logger.info("dto.getRegusu()->"+dto.getRegusu()+"<-");
		logger.info("dto.getEstdie()->"+dto.getEstdie()+"<-");
		logger.info("dto.getFecdie()->"+dto.getFecdie()+"<-");
		logger.info("dto.getHordie()->"+dto.getHordie()+"<-");
		logger.info("dto.getEstreg()->"+dto.getEstreg()+"<-");
		logger.info("dto.getFecenv()->"+dto.getFecenv()+"<-");
		logger.info("dto.getHorenv()->"+dto.getHorenv()+"<-");
		logger.info("dto.getPacvip()->"+dto.getPacvip()+"<-");
		logger.info("dto.getIdvia() ->"+dto.getIdvia()+"<-");
		*/
		
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaInterfazNutricion, Integer.parseInt(institucion));
		boolean tmp=true;
		String column="",valor="";
		if(!nombreTabla.trim().equals(""))
		{
			//se consulta la via de ingreso
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Connection conNormal = UtilidadBD.abrirConexion();
				String viaingreso=UtilidadesManejoPaciente.obtenerCodInterfazXViaIngresoTipoPac(conNormal, Utilidades.convertirAEntero(dto.getViaing()), dto.getTipopac());
			UtilidadBD.closeConnection(conNormal);
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					if (!isReproceso)
					{
						logger.error("CONEXION CERRADA.");
						con=UtilidadBD.abrirConexion();
						tmp=false;
						column=",tiperr,consecutivo";
						valor=",'"+ConstantesIntegridadDominio.acronimoNoConexion+"',"+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ax_nutri");
					}
					else
						return new ResultadoBoolean(false,"");
				}
				
				
				String cadenaIsercion="INSERT INTO "+nombreTabla+" (	 ingreso, " +   	
																		 "viaing, " +   	
																		 "secama, " +  	
																		 "nucama, " +   
																		 "numhis, " +    
																		 "coddie, " + 	
																		 "descr1, " +  
																		 "descr2, " +   	
																		 "regusu, " + 	
																		 "estdie, " +
																		 "fecdie, " +
																		 "hordie, " +
																		 "estreg, " +
																		 "fecenv, " +
																		 "horenv, " +
																		 "pacvip, " +
																		 "idvia"+column+" ) "+  	
																		"values " +
																		"("+dto.getIngreso()+", " +//1 DECIMAL
																		"'"+viaingreso+"', " +//2 VARCHAR
																		"'"+dto.getSecama()+"', " +//3 CHAR
																		"'"+dto.getNucama()+"', " +//4 CHAR
																		""+dto.getNumhis()+", " +//5 DECIMAL
																		"'"+dto.getCoddie()+"', " +//6 CHAR
																		"'"+dto.getDescr1()+"', " +//7 CHAR
																		"'"+dto.getDescr2()+"', " +	//8 CHAR
																		"'"+dto.getRegusu()+"', " +//9 CHAR
																		""+dto.getEstdie()+", " +//10 DECIMAL
																		""+dto.getFecdie()+", " +//11 DECIMAL
																		""+dto.getHordie()+", " +//12 DECIMAL
																		""+dto.getEstreg()+", " +//13 DECIMAL
																		""+dto.getFecenv()+", " +//14 DECIMAL
																		""+dto.getHorenv()+", " +//15 DECIMAL
																		"'"+dto.getPacvip()+"', " +//16 CHAR
																		"'"+dto.getIdvia()+"' " +//17 CHAR      ------>>> total 17 campos
																				valor+")";
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
				logger.info("\n cadena ->"+cadenaIsercion);
						
				if(ps.executeUpdate()>0)
				{
					logger.info("INSERTO INTERFAZ NUTRICION!!!!*****************");
					ps.close();
					
					if (tmp)
					{
						logger.info("\n\n ################################ ELIMINAR REGISTRO LOCAL INTERFAZ NUTRICION CODIGO --> "+dto.getConsecutivo());
						//se manda a elimiar el registro si existe desde en la bd local
						//*******************************************************************************************
						if (UtilidadCadena.noEsVacio(dto.getConsecutivo()) && isReproceso)
						{
							Connection con1=UtilidadBD.abrirConexion();
							try 
							{
								ps=new PreparedStatementDecorator(con1.prepareStatement("DELETE FROM ax_nutri WHERE consecutivo="+dto.getConsecutivo()));
								ps.executeUpdate();
							} 
							catch (Exception e) 
							{
								logger.info("\n problema eliminado el registro de la tabla ax_nutri local "+e);
							}
							ps.close();
							UtilidadBD.cerrarConexion(con1);
						}
						//*************************************************************************************************
						UtilidadBD.closeConnection(con);
						return new ResultadoBoolean(true,"");
					}
					
					UtilidadBD.closeConnection(con);
					return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz no hay conexi�n.");
				}
			
			} 
			catch (SQLException e) 
			{
				logger.info("\n problema insertando ...."+e);
				UtilidadBD.closeConnection(con);
				if (!isReproceso)
					return insertarInterfazNutricionRespaldo(dto, institucion);
			}
			
			return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz no hay conexi�n.");
		}
		else
		{
			logger.warn("NO ESTA DEFINIDA LA TABLA INTERFAZ NUTRICION");
			return new ResultadoBoolean(false,"No esta definida la tabla para la Interfaz Nutricion ");
		}
		
	}
	
	/**
	 * Metodo encargado de consultar los datos de la interfaz de nutricion almacenadas localmente
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoInterfazNutricion> consultaDatosResprocesoNutricion(int institucion) 
	{
		logger.info("\n **** consultaDatosResprocesoNutricion ***");
		
		ArrayList<DtoInterfazNutricion> arrayDtos = new ArrayList<DtoInterfazNutricion>();
		String consulta = "SELECT ingreso,viaing,secama,nucama,numhis,coddie,descr1,descr2,estdie,fecdie," +
				"hordie,pacvip,idvia,regusu,estreg,fecenv,horenv,tiperr,consecutivo from  ax_nutri";
		Connection connection = UtilidadBD.abrirConexion();
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//ps.setInt(1, institucion);

			logger.info("\n\n*******************************************************************************************");
			logger.info(" consulta --->"+ consulta);
			logger.info("\n*******************************************************************************************\n\n");

			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) 
			{
				DtoInterfazNutricion dto = new DtoInterfazNutricion();
				
					dto.setIngreso(rs.getString("ingreso"));//1
					dto.setViaing(rs.getString("viaing"));//2
					dto.setSecama(rs.getString("secama"));//3
					dto.setNucama(rs.getString("nucama"));//4
					dto.setNumhis(rs.getString("numhis"));//5
					dto.setCoddie(rs.getString("coddie"));//6
					dto.setDescr1(rs.getString("descr1"));//7
					dto.setDescr2(rs.getString("descr2"));//8
					dto.setEstdie(rs.getString("estdie"));//9
					dto.setFecdie(rs.getString("fecdie"));//10
					dto.setHordie(rs.getString("hordie"));//11
					dto.setPacvip(rs.getString("pacvip"));//12
					dto.setIdvia(rs.getString("idvia"));//13
					dto.setRegusu(rs.getString("regusu"));//14
					dto.setEstreg(rs.getString("estreg"));//15
					dto.setFecenv(rs.getString("fecenv"));//16
					dto.setHorenv(rs.getString("horenv"));//17
					dto.setConsecutivo(rs.getString("consecutivo"));//18
				
					
				arrayDtos.add(dto);
			}
			rs.close();
			
		} catch (SQLException e) 
		{
			logger.warn(" Error en consultaDatosResprocesoNutricion "+e);
		}
		UtilidadBD.closeConnection(connection);
		return arrayDtos;
		
	}
	
	
	
	
	/**
	 * INSERTA REGISTROS EN LA TABLA ax_nutri
	 * @param dto
	 * @param institucion 
	 */
	public ResultadoBoolean insertarInterfazNutricionRespaldo(DtoInterfazNutricion dto, String institucion) 
	{
		logger.info("************INSERCION DE LA LA NUTRICION ORAL !!!");
	
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaInterfazNutricion, Integer.parseInt(institucion));
		if(!nombreTabla.trim().equals(""))
		{
			//se consulta la via de ingreso
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Connection conNormal = UtilidadBD.abrirConexion();
				String viaingreso=UtilidadesManejoPaciente.obtenerCodInterfazXViaIngresoTipoPac(conNormal, Utilidades.convertirAEntero(dto.getViaing()), dto.getTipopac());
			UtilidadBD.closeConnection(conNormal);
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			Connection con=UtilidadBD.abrirConexion();
			try 
			{
				
				String cadenaIsercion="INSERT INTO "+nombreTabla+" (	 ingreso, " +   	
																		 "viaing, " +   	
																		 "secama, " +  	
																		 "nucama, " +   
																		 "numhis, " +    
																		 "coddie, " + 	
																		 "descr1, " +  
																		 "descr2, " +   	
																		 "regusu, " + 	
																		 "estdie, " +
																		 "fecdie, " +
																		 "hordie, " +
																		 "estreg, " +
																		 "fecenv, " +
																		 "horenv, " +
																		 "pacvip, " +
																		 "idvia,  " +
																		 "tiperr," +
																		 "consecutivo  " +
																		 ")       " +  	
																		"values   " +
																		"("+dto.getIngreso()+", " +//1 DECIMAL
																		"'"+viaingreso+"', " +//2 VARCHAR
																		"'"+dto.getSecama()+"', " +//3 CHAR
																		"'"+dto.getNucama()+"', " +//4 CHAR
																		""+dto.getNumhis()+", " +//5 DECIMAL
																		"'"+dto.getCoddie()+"', " +//6 CHAR
																		"'"+dto.getDescr1()+"', " +//7 CHAR
																		"'"+dto.getDescr2()+"', " +	//8 CHAR
																		"'"+dto.getRegusu()+"', " +//9 CHAR
																		""+dto.getEstdie()+", " +//10 DECIMAL
																		""+dto.getFecdie()+", " +//11 DECIMAL
																		""+dto.getHordie()+", " +//12 DECIMAL
																		""+dto.getEstreg()+", " +//13 DECIMAL
																		""+dto.getFecenv()+", " +//14 DECIMAL
																		""+dto.getHorenv()+", " +//15 DECIMAL
																		"'"+dto.getPacvip()+"', " +//16 CHAR
																		"'"+dto.getIdvia()+"', " +//17 CHAR      ------>>> total 17 campos
																		"'"+ConstantesIntegridadDominio.acronimoSinIntegridad+"'," +
																		UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ax_nutri")+")";
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
				logger.info("\n cadena ->"+cadenaIsercion);
				ps.close();
				cerrarConexion(con);
				
				if(ps.executeUpdate()>0)
					return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz por problema de integridad de datos");
			
			} 
			catch (Exception e) 
			{
				
				cerrarConexion(con);
				return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz ocurrio una excepción.");
			}
			
			return new ResultadoBoolean(false,"No se pudo registrar informaci�n en la interfaz.");
		}
		else
		{
			logger.warn("NO ESTA DEFINIDA LA TABLA INTERFAZ NUTRICION");
			return new ResultadoBoolean(false,"No esta definida la tabla para la Interfaz Nutricion ");
		}
		
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean insertarInterfazConsumosXFacturar(ArrayList<DtoInterfazConsumosXFacturar> arrayDto, int institucion,boolean isReproceso)
	{
		//primero hacemos un delete de la informacion anterior y posteriormente insertamos
		//ESTO FUE ACEPTADO Y VALIDADO X NILSA CON LA CLINICA SHAIO EL 2009-04-27
		if(!eliminarConsumosXFacturar(institucion,isReproceso))
		{	
			return false;
		}	
		for(int w=0; w<arrayDto.size(); w++)
		{
			if(!insertarInterfazConsumosXFacturar(arrayDto.get(w),isReproceso))
			{	
				return false;
			}	
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean eliminarConsumosXFacturar(int institucion,boolean isReproceso) 
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaConsumosXFacturar,institucion);
		if(!nombreTabla.trim().equals(""))
		{
			String consulta= "DELETE FROM "+nombreTabla+" ";
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.......");
					if (!isReproceso)
						con=UtilidadBD.abrirConexion();
					else
						return false;
					
				}
				
				logger.info("eliminacion->"+consulta+"<-");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
				ps.executeUpdate();
				ps.close();
			}
			catch (Exception e) 
			{
				logger.info("No se eliminaron los datos de la interfaz x facturar!!!!!!!!!!!");
				
				UtilidadBD.closeConnection(con);
				return false;
			}
			UtilidadBD.closeConnection(con);
		}
		else
		{
			logger.warn("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE CONSUMOS X FACTURAR");
			return false;
		}
		return true;
	}

	/**
	 * Modificado por el anexo 779
	 * @param dto
	 * @return
	 */
	public boolean insertarInterfazConsumosXFacturar(DtoInterfazConsumosXFacturar dto,boolean isReproceso)
	{
		logger.info("************INSERCION DE LOS CONSUMOS X FACTURAR********************!!!");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaConsumosXFacturar,dto.getInstitucion());
		
		if(!nombreTabla.trim().equals(""))
		{
			//inicializacion de variable necesarias
			//--------------------------------------------------------------
			String column="", valor="";
			String codInterViaIngreso="";
			boolean tmp=true;
			//--------------------------------------------------------------
			
			if (isReproceso)
			{
				codInterViaIngreso=dto.getCodInterfazViaIngresoTipoPac();
				
				
			}
			else
			{
				///////////////////////////////////////////////////////////////////////////////
				Connection conNormal = UtilidadBD.abrirConexion();
				codInterViaIngreso=UtilidadesManejoPaciente.obtenerCodInterfazXViaIngresoTipoPac(conNormal, dto.getCodigoViaIngreso(), dto.getTipoPaciente().toString());
				UtilidadBD.closeConnection(conNormal);
				//////////////////////////////////////////////////////////////////////////////
			}
			
			
			//Connection con= UtilidadBD.abrirConexion();
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					if (!isReproceso)
					{
						con=UtilidadBD.abrirConexion();
						column=",tiperr,consecutivo";
						valor=",'"+ConstantesIntegridadDominio.acronimoNoConexion+"',"+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ax_porfac");
						tmp=false;
					}
					else
						return false;
				}
				
				String cadenaIsercion="INSERT INTO "+nombreTabla+" (	VPREST01,"+ 	//NUMERIC(20,2) 1
																				"NI1CON," + 	// VARCHAR(20),	2		
																				"PLAEST," + 	// CARACTER 6,	3
																				"DSCCON," + 	// VARCHAR(30),	4
																				"INGEST," + 	// NUMERIC(6),	5
																				"VIAING," + 	// CHARACTER(2),6
																				"FEIING," + 	// ????? 8,		7
																				"FEEING," + 	// ????? 8,		8
																				"NMIPAC," + 	// VARCHAR(30),	9
																				"APIPAC," + 	// VARCHAR(30),	10
																				"NUMHAB," + 	// VARCHAR(16),	11
																				"USRING," + 	// VARCHAR(30),	12
																				"ESTING," + 	// CHAR(1),		13
																				"ESTREG," + 	// CHAR(1),		14
																				"FECREG," + 	// ????? 8,		15
																				"HORREG," + 	// ????? 4,		16
																				"FECPRO"+column+") " + 	// ????? (8),	17
																				"values " +
																				"("+dto.getValorXFacturar()+", " +//1
																				"'"+dto.getNumeroIdentificacion()+"', " +//2
																				"'"+((UtilidadTexto.isEmpty(dto.getCodigoInterfazConvenio()))?"":dto.getCodigoInterfazConvenio())+"', " +//3
																				"'"+dto.getDescripcionConvenio()+"', " +//4
																				""+dto.getNumeroIngreso()+"," +		//5
																				"'"+codInterViaIngreso+"', " +//6
																				""+Utilidades.convertirADouble(dto.getFechaIngresoBD().replaceAll("-", ""))+", " +//7
																				""+((UtilidadTexto.isEmpty(dto.getFechaEgresoBD()))?0:Utilidades.convertirADouble(dto.getFechaEgresoBD().replaceAll("-", "")))+", " +//8
																				"'"+dto.getPrimerNombrePaciente()+"', " +//9
																				"'"+dto.getPrimerApellidoPaciente()+"'," +		//10
																				"'"+((UtilidadTexto.isEmpty(dto.getNumeroCama()))?"":dto.getNumeroCama())+"', " +//11
																				"'"+dto.getLoginUsuarioAdmision()+"', " +//12
																				"'"+dto.obtenerEstadoIngresoShaio()+"', " +//13
																				"'"+dto.getEstadoRegistro()+"', " +//14
																				""+Utilidades.convertirADouble(dto.getFechaRegistroBD().replaceAll("-", ""))+"," +		//15
																				""+Utilidades.convertirADouble(dto.getHoraRegistro().replaceAll(":", ""))+", " +//16
																				""+Utilidades.convertirADouble(dto.getFechaProcesadosBD().replaceAll("-", ""))+valor+" )";				//17

				
				
				
				logger.info("insercion->"+cadenaIsercion+"<-");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaIsercion));
				
				//logger.info("dto.getValorXFacturar()--->"+dto.getValorXFacturar());
				//ps.setDouble(1, dto.getValorXFacturar());
				
				//logger.info("dto.getNumeroIdentificacion()->"+dto.getNumeroIdentificacion());
				//ps.setString(2, dto.getNumeroIdentificacion());
				
				//@todo verificar el tipo de dato shaio=numeric, axioma=varchar, ademas debemos verificar si puede ser null
				//logger.info("dto.getCodigoInterfazConvenio()->"+dto.getCodigoInterfazConvenio());
				/*if(UtilidadTexto.isEmpty(dto.getCodigoInterfazConvenio()))
					ps.setString(3, "");
				else	
					ps.setString(3, dto.getCodigoInterfazConvenio());*/
				
				//logger.info("dto.getDescripcionConvenio()->"+dto.getDescripcionConvenio());
				//ps.setString(4, dto.getDescripcionConvenio());
				
				//logger.info("dto.getNumeroIngreso()->"+dto.getNumeroIngreso());
				//ps.setDouble(5, dto.getNumeroIngreso());
				
				//logger.info("dto.obtenerViaIngresoShaio()->"+dto.obtenerViaIngresoShaio());
				//ps.setString(6, dto.obtenerViaIngresoShaio());
				
				//@todo verificar tipo dato
				//logger.info("dto.getFechaIngresoBD()-->"+dto.getFechaIngresoBD().replaceAll("-", ""));
				//ps.setDouble(7, Utilidades.convertirADouble(dto.getFechaIngresoBD().replaceAll("-", "")));
				
				//@todo verificar tipo dato
				//logger.info("dto.getFechaEgresoBD()-->"+dto.getFechaEgresoBD().replaceAll("-", ""));
				/*if(UtilidadTexto.isEmpty(dto.getFechaEgresoBD()))
					ps.setDouble(8, 0);
				else
					ps.setDouble(8, Utilidades.convertirADouble(dto.getFechaEgresoBD().replaceAll("-", "")));
					*/
				
				//logger.info("dto.getPrimerNombrePaciente()-->"+dto.getPrimerNombrePaciente());
				//ps.setString(9, dto.getPrimerNombrePaciente());
				
				//logger.info("dto.getPrimerApellidoPaciente()-->"+dto.getPrimerApellidoPaciente());
				//ps.setString(10, dto.getPrimerApellidoPaciente());
					
				//logger.info("dto.getNumeroCama()-->"+dto.getNumeroCama());
				
				/*if(UtilidadTexto.isEmpty(dto.getNumeroCama()))
					ps.setString(11, "");
				else
					ps.setString(11, dto.getNumeroCama());*/
				
				//logger.info("dto.getLoginUsuarioAdmision()-->"+dto.getLoginUsuarioAdmision());
				//ps.setString(12, dto.getLoginUsuarioAdmision());
				
				//logger.info("dto.obtenerEstadoIngresoShaio()-->"+dto.obtenerEstadoIngresoShaio());
				//ps.setString(13, dto.obtenerEstadoIngresoShaio());
				
				//logger.info("dto.getEstadoRegistro()"+dto.getEstadoRegistro());
				//ps.setString(14, dto.getEstadoRegistro());
				
				//@todo verificar tipo dato
				//logger.info("dto.getFechaRegistroBD()->"+dto.getFechaRegistroBD().replaceAll("-", ""));
				//ps.setDouble(15, Utilidades.convertirADouble(dto.getFechaRegistroBD().replaceAll("-", "")));
				
				//@todo verificar tipo dato
				//logger.info("dto.getHoraRegistro()->"+dto.getHoraRegistro().replaceAll(":", ""));
				//ps.setDouble(16, Utilidades.convertirADouble(dto.getHoraRegistro().replaceAll(":", "")));
				
				//@todo verificar tipo dato
				//logger.info("dto.getFechaProcesadosBD()->"+dto.getFechaProcesadosBD().replaceAll("-", ""));
				//ps.setDouble(17, Utilidades.convertirADouble(dto.getFechaProcesadosBD().replaceAll("-", "")));
				
				//logger.info("VA HA INSERTAR!!!!");
				if(ps.executeUpdate()>0)
				{
					if (tmp)
					{
						//se manda a elimiar el registro si existe desde en la bd local
						//*******************************************************************************************
						if (UtilidadCadena.noEsVacio(dto.getConsecutivo()) && isReproceso)
						{
							Connection con1=UtilidadBD.abrirConexion();
							try 
							{
								ps=new PreparedStatementDecorator(con1.prepareStatement("DELETE FROM ax_porfac WHERE consecutivo="+dto.getConsecutivo()));
								ps.executeUpdate();
							} 
							catch (Exception e) 
							{
								logger.info("\n problema eliminado el registro de la tabla ax_porfac local "+e);
							}
							ps.close();
							UtilidadBD.cerrarConexion(con1);	
						}
						//*************************************************************************************************
						
						logger.info("\n \n [insercion remota] INSERTO INTERFAZ CONSUMOS X FACTURAR!!!!*****************");
					}
					else
						logger.info("\n \n[insercion local] INSERTO INTERFAZ CONSUMOS X FACTURAR!!!!*****************");
					
					ps.close();
					
					UtilidadBD.closeConnection(con);
					return true;
				}
				logger.warn("El registro no se pudo Insertar***********************");
				ps.close();
				
				UtilidadBD.closeConnection(con);
				return false;
			} 
			catch (Exception e) 
			{
				UtilidadBD.closeConnection(con);
				return false;
			}
		}
		else
		{
			logger.warn("NO ESTA DEFINIDA LA TABLA PARA EL MANEJO DE CONSUMOS X FACTURAR");
			return false;
		}
	
	}
	
	
	
	
	
	/**
	 * M�todo que consulta las respuesta de laboratorios pendientes por registrar en Axioma
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarRegistrosLaboratoriosPendientes(int codigoInstitucion)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		logger.info("************CONSULTA DE REGISTROS DE LABORATORIOS PENDIENTES*******************!!!");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaLaboratorio,codigoInstitucion);
		if(!nombreTabla.trim().equals(""))
		{
			String consulta = "SELECT " +
				"INGLAB as CONSECUTIVOINGRESO," +
				"CONLAB as NUMEROSOLICITUD," +
				"ESPLAB as ESTADO," +
				"ESCLAB as LEIDO," +
				"USRLAB as USUARIOLABORATORIO," +
				"PGMLAB as PROGRAMALABORATORIO," +
				"FECLAB as FECHALABORATORIO," +
				"HORLAB as HORALABORATORIO," +
				"UMOLAB," +
				"PMOLAB," +
				"FMOLAB," +
				"HMOLAB " +
				"FROM RESLAB WHERE ESCLAB <> 'L'"; //que no se hayan le�do 
			
			try
			{
			
					Connection con=abrirConexionInterfaz();
					Statement st = con.createStatement();
					ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
					while(rs.next())
					{
						HashMap<String, Object> elemento = new HashMap<String, Object>();
						elemento.put("consecutivoIngreso", rs.getString("CONSECUTIVOINGRESO"));
						elemento.put("numeroSolicitud", rs.getString("NUMEROSOLICITUD"));
						elemento.put("estado", rs.getString("ESTADO"));
						elemento.put("leido", rs.getString("LEIDO"));
						elemento.put("usuarioLaboratorio", rs.getString("USUARIOLABORATORIO"));
						elemento.put("programaLaboratorio", rs.getString("PROGRAMALABORATORIO"));
						elemento.put("fechaLaboratorio", rs.getString("FECHALABORATORIO"));
						elemento.put("horaLaboratorio", rs.getString("HORALABORATORIO"));
						
						if(elemento.get("horaLaboratorio").toString().length()<6)
							elemento.put("horaLaboratorio", UtilidadCadena.rellenarCaracter(elemento.get("horaLaboratorio").toString(), 6, true, "0"));
						resultados.add(elemento);
					}
					cerrarConexion(con);
						
			}
			catch(SQLException e)
			{
				logger.info("Error al consultar la interfaz de laboratorios: "+e);
			}
			
			
			
		}
		
		return resultados;
		
	}
	
	/**
	 * Metodo que consulta si el paciente de en Axioma tiene un ingreso Activo en Sistema Anterior
	 * @param codigoTipoIdentificacionPersona
	 * @param numeroIdentificacionPersona
	 * @param institucion
	 * @return
	 */
	public ResultadoBoolean verificarPacienteSistemaParalelo( String codigoTipoIdentificacionPersona, String numeroIdentificacionPersona, String institucion) 
	{
		logger.info("\n\n\n\n\n\n\n\n\n\n\n.........:::::::::........ INTERFAZ PRODUCCION EN PARALELO ACTIVA .........:::::::::........\n");
		
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaInterfazProduccionParalelo,Utilidades.convertirAEntero(institucion));
		
		if(!nombreTabla.trim().equals(""))
		{
			
			String consultaPacienteParaleloStr="SELECT ESTING FROM "+nombreTabla+" where TIDING = '"+(Utilidades.codigoInterfaztipoIdentificacion(codigoTipoIdentificacionPersona))+"' and NIDING = "+numeroIdentificacionPersona+" ";
			
			logger.info("========CONSULTA=> "+consultaPacienteParaleloStr);
			logger.info("=========> TIDING->"+codigoTipoIdentificacionPersona+"<-");
			logger.info("=========> NIDING->"+numeroIdentificacionPersona+"<-");
			
			Connection con=abrirConexionInterfaz();
			try 
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA.");
					logger.info("\n\n\n\n\n\n\n\n\n\n\n");
					return new ResultadoBoolean(true,"No se pudo verificar paciente en interfaz paralela no hay conexi�n.");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaPacienteParaleloStr));
				
				//ps.setString(1,Utilidades.codigoInterfaztipoIdentificacion(codigoTipoIdentificacionPersona));
				//ps.setString(2,numeroIdentificacionPersona);
			
				ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					int enteroEstado=rs.getInt("ESTING");
					ps.close();
					cerrarConexion(con);
					
					logger.info("RESULTADO CONSULTA TABLA RIAING ---->"+enteroEstado+"<-");
					
					if(enteroEstado==ConstantesBDInterfaz.pacienteProcesadoSistemaAnterior)
					{
						logger.info("\n\n\n\n\n\n\n\n\n\n\n");
						return new ResultadoBoolean(true,"El paciente se encuentra en Sistema Anterior");
					}
					else
					{
						logger.info("\n\n\n\n\n\n\n\n\n\n\n");
						return new ResultadoBoolean(false,"El paciente No se encuentra en Sistema Anterior");
					}
				}
				logger.warn("El Paciente no se encuentra en Sistema Anterior***********************");
				ps.close();
				cerrarConexion(con);
				logger.info("\n\n\n\n\n\n\n\n\n\n\n");
				return new ResultadoBoolean(false,"El Paciente no se encuentra en Sistema Anterior");
			} 
			catch (SQLException e) 
			{
				cerrarConexion(con);
				logger.info("\n problema verifiando paciente en la interfaz "+e);
				return new ResultadoBoolean(true,"No se pudo verificar paciente en interfaz paralela ocurrio una excepción ");
			}
			
			
		}
		else
		{
			logger.warn("NO ESTA DEFINIDA LA TABLA INTERFAZ PRODUCCION EN PARALELO CON SISTEMA ANTERIOR");
			return new ResultadoBoolean(true,"No esta definida la tabla para la Interfaz Paralelo ");
		}
	}	
	
	/**
	 * M�todo que se usa para actualizar el registro de laboratorio como l�ido
	 * @param consecutivoIngreso
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean actualizarRegistroLaboratorioLeido(String consecutivoIngreso,String numeroSolicitud,String fecha,String hora,int codigoInstitucion)
	{
		boolean exito = false;
		logger.info("************ACTUALIZACION DE REGISTROS DE LABORATORIOS A LEIDO*******************!!!");
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaLaboratorio,codigoInstitucion);
		if(!nombreTabla.trim().equals(""))
		{
			String[] vector = new String[0];
			vector = fecha.split("/");
			String[] vectorHora = new String[0];
			vectorHora = hora.split(":");
			
			String consulta = "UPDATE "+nombreTabla+" SET " +
				"ESCLAB = 'L'," +
				"UMOLAB='SERVAXIOMA'," +
				"PMOLAB='AUTOAXIOMA'," +
				"FMOLAB="+Integer.parseInt(vector[2]+vector[1]+vector[0])+"," +
				"HMOLAB="+Integer.parseInt(vectorHora[0]+vectorHora[1]+vectorHora[2])+" " +
				"WHERE " +
				"INGLAB = "+Integer.parseInt(consecutivoIngreso)+" AND CONLAB = "+Integer.parseInt(numeroSolicitud)+" ";
			
			try
			{
			
					Connection con=abrirConexionInterfaz();
					PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					/*vector = fecha.split("/");
					pst.setInt(1,Integer.parseInt(vector[2]+vector[1]+vector[0]));
					vector = hora.split(":");
					pst.setInt(2,Integer.parseInt(vector[0]+vector[1]+vector[2]));
					pst.setInt(3,Integer.parseInt(consecutivoIngreso));
					pst.setInt(4,Integer.parseInt(numeroSolicitud));*/
					
					if(pst.executeUpdate()>0)
						exito = true;
					
					cerrarConexion(con);
						
			}
			catch(SQLException e)
			{
				logger.info("Error al consultar la interfaz de laboratorios: "+e);
			}
			
		}
		
		return exito;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Modificado por el anexo 779
	

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}