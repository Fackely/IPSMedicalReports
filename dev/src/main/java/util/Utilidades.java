/*
 * Creado en 4/02/2005
 *
 * Princeton S.A.
 */
package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.interfaz.DtoInterfazAbonos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.tesoreria.DtoConceptosIngTesoreria;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.sysmedica.util.UtilidadFichas;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
@SuppressWarnings({"unchecked", "unused"})
public class Utilidades
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(Utilidades.class);
	
	/**
	 * Metodo para verificar si una solicitud de medicamentos tiene despachos
	 * @param con Conexión con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene despachos, false de lo contrario
	 */
	public static boolean hayDespachosEnSolicitud(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().hayDespachosEnSolicitud(con, numeroSolicitud);
	}

	/**
	 * Método que carga la fecha de la base de datos para no utilizar la del sistema
	 * @return String con la fecha del sistema formato BD
	 */
	public static String capturarFechaBD()
	{
		String fecha="";
		Connection con=UtilidadBD.abrirConexion();
		fecha=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().capturarFechaBD(con);
		UtilidadBD.closeConnection(con);
		return fecha;
	}
	
	/**
	 * Método que carga la fecha de la base de datos para no utilizar la del sistema
	 * @return String con la fecha del sistema formato BD
	 */
	public static String capturarFechaBD(Connection con)
	{
		String fecha="";
		fecha=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().capturarFechaBD(con);
		return fecha;
	}
	
	/**
	 * Método que carga la hora de la base de datos para no utilizar la del sistema
	 * @return String con la Hora del sistema cinco caracteres
	 */
	public static String capturarHoraBD()
	{
		String hora="";
		Connection con=UtilidadBD.abrirConexion();
		hora=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().capturarHoraBD(con);
		UtilidadBD.closeConnection(con);
		return hora;
	}
	
	/**
	 * Método que carga la hora de la base de datos para no utilizar la del sistema
	 * @return String con la Hora del sistema cinco caracteres
	 */
	public static String capturarHoraBD(Connection con)
	{
		String hora="";
		hora=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().capturarHoraBD(con);
		return hora;
	}
	/**
	 * Método para cargar la Fecha-Hora del egreso de la cuenta Urgencias cuando
	 * se hace un Asocio de Cuenta
	 * @param con
	 * @param codigo_paciente
	 * @return
	 */
	public static String capturarFechayHoraEgresoUrgenciasEnAsocio(String idIngreso,String viaIngreso) 
	{
		String fecha_hora="";
		Connection con=UtilidadBD.abrirConexion();
		fecha_hora=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().capturarFechayHoraEgresoUrgenciasEnAsocio(con,idIngreso,viaIngreso);
		UtilidadBD.closeConnection(con);
		return fecha_hora;
	}
	
	/**
	 * Método para obtener el codigo del contrato el cual
	 * fue utilizado en su ï¿½ltima cuenta abierta
	 * @param con Conexiï¿½n con la BD
	 * @param codigoPaciente Cï¿½digo del paciente
	 * @return Codigo del ultimo contrato del paciente
	 */
	public static int obtenerUltimoContrtatoPaciente(Connection con, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUltimoContrtatoPaciente(con, codigoPaciente);
	}
	

	/**
	 * Metodo que retorna el codigo del Cie Actual.
	 * @param con
	 */
	public static int codigoCieActual(Connection con) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().codigoCieActual(con);
		
	}
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Método usado para obtener el destino de salida de un egreso de evoluciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param obtenerDestinoSalidaStr
	 * @return
	 */
	public static int obtenerDestinoSalidaEgresoEvolucion(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDestinoSalidaEgresoEvolucion(con,idCuenta);
	}

	/**
	 * Método que carga el numero de solicitud
	 * de la ultima valoraciï¿½n de interconsulta
	 * @param con
	 * @param codigoPersona
	 * @return numeroSolicitud ï¿½ltima interconsulta
	 */
	public static int obtenerUltimaValoracionInterconsulta(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUltimaValoracionInterconsulta(con, codigoPersona);
	}
	
	
	/**
	 * Metodo encargado de consultar los centros de costo
	 * y devolverlos en un ArrayList de HashMap con los keys
	 * codigo,nombre.
	 * @author Jhony Alexander Duque A.
	 * Se puede filtrar por los siguientes criterios:
	 * -- viaIngreso --> Opcional 
	 * -- centroAtencion --> Opcional
	 * -- institucion --> Requerido
	 * -- tipoPaciente --> Opcional
	 * -- tipoMonitoreo --> Opcional
	 * -- filtroTipoMonitoreo --> Requerido
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @param tipoPaciente
	 * @param tipoMonitoreo
	 * @param filtroTipoMonitoreo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getCentrosCosto(Connection con,String viaIngreso,int centroAtencion,int institucion, String tipoPaciente, int tipoMonitoreo, boolean filtroTipoMonitoreo,boolean noTipoMonitoreo,boolean consultarViaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCentrosCosto(con, viaIngreso, centroAtencion, institucion, tipoPaciente, tipoMonitoreo, filtroTipoMonitoreo,noTipoMonitoreo,consultarViaIngreso);
	}
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Método para saber el nï¿½mero de solicitudes de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return nï¿½mero de solicitudes
	 */
	public static int obtenerNumeroSolicitudesCuenta(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNumeroSolicitudesCuenta(con,idCuenta);
	}
	
	/**
	 * Metodo para obtener el codigo de una factura dado su consecutivo. retorna la primera que encuentra.
	 * Retorna ConstantesBD.codigoNuncaValido si la factura no existe.
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static int obtenerCodigoFactura(int consecutivoFactura) 
	{
		int factura=0;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			factura=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoFactura(con,consecutivoFactura);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return factura;
	}
	
	/**
	 * Metodo para obtener el codigo de una factura dado su consecutivo. dado la institucion, en caso de ser multiempresa retorna la primera que encuentra.
	 * Retorna ConstantesBD.codigoNuncaValido si la factura no existe.
	 * @param consecutivoFactura
	 * @param consecutivoFactura
	 * @return
	 */
	public static int obtenerCodigoFactura(int consecutivoFactura,int institucion) 
	{
		int factura=0;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			factura=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoFactura(con,consecutivoFactura,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return factura;
	}
	
	/**
	 * Metodo para obtener la fecha de facturacion de una factura.
	 * la fecha es retornada en formato de la aplicacion.
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerFechaFacturacion(Connection con,int codigoFactura) 
	{
		return UtilidadFecha.conversionFormatoFechaAAp(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaFacturacion(con,codigoFactura));
	}
	
	
	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public static double obtenerCuentaCobroFactura(int codigoFactura) 
	{
		double cc=0;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			cc=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCuentaCobroFactura(con,codigoFactura);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return cc;
	}

	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public static double obtenerCuentaCobroFactura(Connection con,int codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCuentaCobroFactura(con,codigoFactura);
	}

	/**
	 * metodo que retorna el estado de la siguiente forma codigo+ConstantesBD.separadorSplit+descripcion
	 * @param codigoFactura
	 * @return Estado.
	 */
	public static String obtenerEstadoFactura(int codigoFactura) 
	{
		String estado="";
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			estado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoFactura(con,codigoFactura);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return estado;
	}
	
	/**
	 * metodo que retorna el estado de la siguiente forma codigo+ConstantesBD.separadorSplit+descripcion
	 * @param codigoFactura
	 * @return Estado.
	 */
	public static String obtenerEstadoFactura(Connection con,int codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoFactura(con,codigoFactura);
	}
	
	
	/**
	 * Este Método devuelve los cï¿½digos de las justificaciones
	 * filtrando por instituciï¿½n del usuario
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @param esArticulo Restringir si es justificaciï¿½n de Artï¿½culo o de Servicio
	 * @return Array de enteros con los cï¿½digos de las justificaciones
	 */
	public static int[] buscarCodigosJustificaciones(Connection con, int codigoInstitucion, boolean restringirSoloRequeridas, boolean esArticulo)
	{
		try 
		{
			String tipoBd=System.getProperty("TIPOBD");
			int[] codigos=DaoFactory.getDaoFactory(tipoBd).getUtilidadesDao().buscarCodigosJustificaciones(con, codigoInstitucion, restringirSoloRequeridas, esArticulo);
			return codigos;
		} catch (Exception e) 
		{
			Log4JManager.error("Error buscarCodigosJustificaciones " + e);
			return null;
		}
	}
	
	/**
	 * Este Método devuelve los cï¿½digos y los nombres de las justificaciones
	 * filtrando por instituciï¿½n del usuario
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @param esArticulo Restringir si es justificaciï¿½n de Artï¿½culo o de Servicio
	 * @return Vector con los cï¿½digos y los nombres de los Atributos de la justificaciï¿½n
	 */
	public static Vector buscarCodigosNombresJustificaciones(int codigoInstitucion, boolean restringirSoloRequeridas, boolean esArticulo)
	{
		Connection con=null;
		try 
		{
			String tipoBd=System.getProperty("TIPOBD");
			con=DaoFactory.getDaoFactory(tipoBd).getConnection();
			Vector nombresCodigosJustificacion=DaoFactory.getDaoFactory(tipoBd).getUtilidadesDao().buscarCodigosNombresJustificaciones(con, codigoInstitucion, restringirSoloRequeridas, esArticulo);
			UtilidadBD.closeConnection(con);
			return nombresCodigosJustificacion;
		} catch (SQLException e) 
		{
			logger.error("Error cerrando la conexiï¿½n"+e);
			return null;
		}
	}
	
	/**
	 * Método que verifica si el tipo de identificaciï¿½n maneja consecutivo
	 * automï¿½tico
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static boolean esAutomaticoTipoId(Connection con,String acronimo,int codigoInstitucion)
	{
		String tipoBd=System.getProperty("TIPOBD");
		return DaoFactory.getDaoFactory(tipoBd).getUtilidadesDao().esAutomaticoTipoId(con,acronimo,codigoInstitucion);
	}
	
	/**
	 * Método para actualizar el diagnï¿½stico del tope por paciente de una cita
	 * cuya cuenta de cosnulta externa ya se encuentre facturada.
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param diagnostico
	 * @param tipoCie
	 * @return
	 */
	public static boolean actualizarDiagnosticoTopesCita(Connection con,int numeroSolicitud,int codigoPaciente,String diagnostico,String tipoCie) 
	{
		try
		{
			String tipoBd=System.getProperty("TIPOBD");
			String numeroFactura=DaoFactory.getDaoFactory(tipoBd).getUtilidadesDao().obtenerNumeroFactura(con,numeroSolicitud);
			
			if(numeroFactura!=null)
			{
				int resp=DaoFactory.getDaoFactory(tipoBd).getUtilidadesDao().actualizarDiagnosticoTopesPaciente(con,codigoPaciente,numeroFactura.trim(),diagnostico,tipoCie);
				
				if(resp>0)
					return true;
				else
					return false;
				
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			logger.error("Error en actualizarDiagnosticoTopesCita de Utilidades: "+e);
			return false;
		}
	}
	
	/**
	 * Método para capturar el cï¿½digo de la ultima via de ingreso del paciente
	 * que no tenga cuenta activa.
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static int obtenerCodigoUltimaViaIngreso(Connection con,int codigoPaciente)
	{
		String tipoBd=System.getProperty("TIPOBD");
		return DaoFactory.getDaoFactory(tipoBd).getUtilidadesDao().obtenerCodigoUltimaViaIngreso(con,codigoPaciente);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Método obtener el ID de la ï¿½ltima cuenta del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static int obtenerIdUltimaCuenta(Connection con,int codigoPaciente)
	{
		String tipoBd=System.getProperty("TIPOBD");
		return DaoFactory.getDaoFactory(tipoBd).getUtilidadesDao().obtenerIdUltimaCuenta(con,codigoPaciente);
	}

    /**
     * Funciï¿½n que verifica el estado true o false de la autorizacion en la tabla atributo_solicitud
     * @param con
     * @param codAtributoSolicitud
     * @return
     */
    public static boolean verificarAutorizacion(Connection con, int codAtributoSolicitud) 
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().verificarAutorizacion(con, codAtributoSolicitud);            
    }

    /**
     * Funciï¿½n que verifica si una cadena tiene solo espacios
     * @param descripcion -> Cadena que verificarï¿½
     * @return false si la cadena tiene solo espacios      
     * */
    public static boolean validarEspacios(String cadena) 
    {
      cadena=cadena.trim();
      
      if(cadena.length()==0)
          return false;
      else
          return true;   
    }
    
    /**
	 * Adiciï¿½n Sebastiï¿½n
	 * Método usado para verificar si un servicio o un artï¿½culo que requiera justificaciï¿½n
	 * ha sido justificado. 
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param codigoInstitucion
	 * @param esArticulo
	 * @return
	 */
	public static boolean esSolicitudJustificada(Connection con,int numeroSolicitud,int parametro,int codigoInstitucion,boolean esArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esSolicitudJustificada(con,numeroSolicitud,parametro,codigoInstitucion,esArticulo);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Método usado para obtener el cï¿½digo y la descripcion de cada atributo de la justificacion del medicamento
	 * o servicio
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param esArticulo
	 * @return
	 */
	public static Collection obtenerAtributosJustificacion(Connection con,int numeroSolicitud,int parametro,boolean esArticulo)
	{
		try
		{
			ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerAtributosJustificacion(con,numeroSolicitud,parametro,esArticulo);
			
			Collection listado=new ArrayList();
			
			while(rs.next())
			{
				InfoDatos atributo=new InfoDatos(rs.getInt("atributo"),rs.getString("descripcion"));
				
				listado.add(atributo);
			}
			
			return listado;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerAtributosJustificacion de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}

	/**
	 * Metodo para cargar los datos del embarazo necesarios en la valoracion de gineco!
	 * Embarazada - FUR - FPP - Edad Gestacional
	 * @param con
	 * @param codigoPersona
	 * @return Vector con los datos del embarazo
	 */
	public static Vector cargarDatosEmbarazo(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().cargarDatosEmbarazo(con, codigoPersona);
	}

	/**
	 * Método para el ï¿½ltimo tipo de parto
	 * @param con
	 * @param codigoPaciente
	 * @return InfoDatosInt con codigo y nombre del ï¿½ltimo tipo de parto
	 */
	public static InfoDatosInt obtenerUltimoTipoParto(Connection con, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUltimoTipoParto(con, codigoPaciente);
	}

	/**
	 * Metodo para cargar los datos de los antecedentes gineco-obstetricos necesarios en la valoracion de gineco!
	 * EdadMenarquia - OtraEdadMenarquia - EdadMenopausia - OtraEdadMenopausia
	 * @param con
	 * @param codigoPersona
	 * @return Vector con los datos del embarazo
	 */
	public static Vector cargarDatosAntGineco(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().cargarDatosAntGineco(con, codigoPersona);
	}

	/**
	 * Método para obtener el ï¿½ltimo nï¿½mero del embarazo del paciente
	 * @param con -> Connection
	 * @param codigoPaciente -> int
	 * @return numeroEmbarazo -> int
	 */
	public static int obtenerUltimoNumeroEmbarazo(Connection con, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUltimoNumeroEmbarazo(con, codigoPaciente);
	}

	/**
	 * @param con
	 * @param string
	 * @return
	 */
	public static int[] consultarTiposPartoVaginal(Connection con, String padre)
	{

		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarTiposPartoVaginal(con, padre);
	}
	
	/**
	 * Método para consultar embarazos sin finalizar
	 * @param con Conexion con la BD
	 * @param codigoPaciente Codigo del paciente
	 * @param codigoEmbarazo Codigo del embarazo
	 * @return true si el embarazo no se a finalizado
	 */
	public static boolean esEmbarazoSinTerminar(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esEmbarazoSinTerminar(con, codigoPaciente, codigoEmbarazo);
	}
	
	/**
	 * Método para consultar en la BD el nombre de la edad menarquia
	 * o menopausia
	 * @param con
	 * @param codigoRango
	 * @param esMenarquia true consulta la menarqui, false la menopausia
	 * @return Striong con el nombre obtenido
	 */
	public static String obtenerNombreEdadMenarquiaOMenopausia(Connection con, int codigoRango, boolean esMenarquia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreEdadMenarquiaOMenopausia(con, codigoRango, esMenarquia);
	}
	
	/**
	 * Método para obtener el ï¿½ltimo valor de la altura uterina en la valoraciï¿½n gineco-obstï¿½trica
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoPaciente
	 */
	
	public static String obtenerAlturaUterinaValoracion(Connection con, int institucion, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerAlturaUterinaValoracion(con, institucion, codigoPaciente);
	}
		
	/**
	 * Método para obtener el noimbre del concepto de menstruaciï¿½n
	 * recibiendo como parï¿½mero el cï¿½digo
	 * @param con Connection Conexiï¿½n con la BD
	 * @param conceptoMenstruacion int Codigo del concepto Menstruaciï¿½n
	 * @return
	 */
	public static String obtenerNombreConceptoMenstruacion(Connection con, int conceptoMenstruacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreConceptoMenstruacion(con, conceptoMenstruacion);
	}
	
	/**	 
	 * Método usado para obtener los pooles vigentes en los cuales 
	 * se encuentra el medico, 
	 * @param con Connection, conexiï¿½n con la fuente de datos
	 * @param fecha String
	 * @param codMedico int  
	 * @return ResultSet
	 * @author jarloc
	 */
	public static ArrayList obtenerPoolesMedico(Connection con, String fecha, int codMedico)
	{		
	    ArrayList array =new ArrayList();   
	    int pos = 0;
	    ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPoolesMedico(con, fecha, codMedico, true);
	    try 
	    {
            while(rs.next())
            {
                array.add(pos,rs.getInt("codPool")+"");
                pos ++;              
            }
        } catch (SQLException e) 
        {           
            e.printStackTrace();
        }
	    return array;	
	}
	
	/**	 
	 * Método usado para obtener los pooles vigentes en los cuales 
	 * se encuentra el medico, 
	 * @param con Connection, conexiï¿½n con la fuente de datos
	 * @param fecha String
	 * @param codMedico int  
	 * @return ResultSet
	 * @author jarloc
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPoolesMedicoArrayMap(Connection con, String fecha, int codMedico)
	{		
	    ArrayList<HashMap<String, Object>> array =new ArrayList<HashMap<String, Object>>();   
	    ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPoolesMedico(con, fecha, codMedico, true);
	    try {
            while(rs.next()) {
            	HashMap<String, Object> poolMap = new HashMap<String, Object>();
            	poolMap.put("codPool", rs.getInt("codPool")+"");
            	poolMap.put("descripcion", rs.getString("descripcion")+"");
                array.add(poolMap);             
            }
        } catch (SQLException e) {           
            logger.error("ERROR", e);
        }
	    return array;	
	}

	/**
	 * Metodo para obtener el valor de una secuencia exite un metodo llamado
	 * obtenerUltimoValorSecuencia que se encuentra en los DaoFactory respectivos
	 * de cada motor debido a que este metodo no incementa a secuencia he creado el 
	 * siguiente metodo que incrementa la secuencia y se puede utilizar en una 
	 * transaccion ya que si se produce error, no se incrementa la secuencia.
	 * @param con
	 * @param nombreSecuencia
	 */
	public static int getSiguienteValorSecuencia(Connection con, String nombreSecuencia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getSiguienteValorSecuencia(con,nombreSecuencia);
	}
	
	/**
	 * carga el esuqema tarifario de una solicitud de cx
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int getEsquemaTarifarioSolCx(Connection con, String codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getEsquemaTarifarioSolCx(con, codigo);
	}
	
		
	

	/**
	 * Método para obtener todos los pooles
	 * a los cuales pertenecen los mï¿½dicos que
	 * realizaron cualquier atenciï¿½n al
	 * paciente
	 * @param con
	 * @param idCuenta
	 * @param esquemaTarifario
	 * @return Vector
	 */
	public static Vector obtenerPoolesCuenta(Connection con, int idCuenta)
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPoolesCuenta(con, idCuenta);
	}

	
	

	/**
	 * @param cuentaCobro
	 * @param cuentaCobro
	 */
	public static int obtenerEstadoCuentaCobro(Connection con,double cuentaCobro, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoCuentaCobro(con, cuentaCobro,institucion);
	}

	/**
	 * @param codigoCuentaCobro.
	 * @return
	 */
	public static int obtenerEstadoCuentaCobro(double codigoCuentaCobro,int institucion) 
	{
		int estadoCuentaCobro=ConstantesBD.codigoNuncaValido;
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			estadoCuentaCobro=obtenerEstadoCuentaCobro(con,codigoCuentaCobro,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return estadoCuentaCobro;
	}

	/**
	 * @param cuentaCobro.
	 * @return fecha
	 */
	public static String obtenerFechaRadicacionCuentaCobro(int cuentaCobro) 
	{
		String fecha="";
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			fecha=UtilidadFecha.conversionFormatoFechaAAp(obtenerFechaRadicacionCuentaCobro(con,cuentaCobro));
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fecha;
	}
	/**
	 * @param con
	 * @param cuentaCobro.
	 * @param fecha
	 */
	public static String obtenerFechaRadicacionCuentaCobro(Connection con, double cuentaCobro) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaRadicacionCuentaCobro(con, cuentaCobro);
	}
	
	/**
	 * Metodo que retorna true si una factura tiene aplicado un determinado ajuste.
	 * @param con
	 * @return
	 */
	public static boolean existeAjusteNivelFacturaEmpresa(Connection con,double codigo,int factura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeAjusteNivelFacturaEmpresa(con, codigo,factura);
	}
	
	public static int numFacturasCuentaCobro(Connection con,double cuentacobro,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().numFacturasCuentaCobro(con, cuentacobro,institucion);
	}
	
	public static int numServicosArticulosFactura(Connection con,int factura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().numServicosArticulosFactura(con, factura);
	}
    
    public static int numAsociosCirugiaDetFactura(Connection con,int codDetFactura)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().numAsociosCirugiaDetFactura(con, codDetFactura);
    }
	
	/**
	 * Método que consulta los datos del tipo de monitoreo
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	public static String[] getTipoMonitoreo(Connection con,int tipoMonitoreo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getTipoMonitoreo(con,tipoMonitoreo);
	}
	
	/**
	 * Método usado para consultar la fecha de la orden de salida
	 * de una cuenta de Urgencias u Hospitalizaciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param institucion
	 * @return si no se encuentra la fecha devuelve cadena vacï¿½a
	 */
	public static String obtenerFechaOrdenSalida(Connection con,int idCuenta,int institucion)
	{
		try 
		{
			if (UtilidadValidacion.tieneEgreso(con, idCuenta))
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaOrdenSalida(con,idCuenta,institucion);
			else
				return "";	
		} catch (Exception e) 
		{
			logger.info("\n problema obtenerFechaOrdenSalida "+e);
		}
		
		return "";
	}
	
	/**
	 * Método que retorna TRUE para saber si la cuenta tiene solicitud de estancia
	 * para la fecha estipulada, de lo contrario retorna false
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public static boolean haySolicitudEstancia(Connection con,int idCuenta,String fechaEstancia,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().haySolicitudEstancia(con,idCuenta,fechaEstancia,institucion);
	}

	/**
	 * @param cuentacobro
	 * @param institucion
	 * @return
	 */
	public static int numFacturasCuentaCobro(double cuentacobro, int institucion) 
	{
		int temp=ConstantesBD.codigoNuncaValido;
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			temp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().numFacturasCuentaCobro(con,cuentacobro,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * @param con
	 * @param detFactSolicitud
	 * @return
	 */
	public static double obtenerPorcentajePoolFacturacion(Connection con, int detFactSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPorcentajePoolFacturacion(con,detFactSolicitud);
	}

	/**
	 * Metodo que realiza la busqueda de un ajuste y retorna su codigo.
	 * retorna el codigo del ajuste
	 * @param numeroAjuste
	 * @param tipoAjuste
	 * @param institucion
	 * @return
	 */
	public static double obtenercodigoAjusteEmpresa(String numeroAjuste, String tipoAjuste, int institucion) 
	{
		double temp=ConstantesBD.codigoNuncaValido;
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			temp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenercodigoAjusteEmpresa(con,numeroAjuste,tipoAjuste,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Metodo que retorna true si un usuario tien el rol de una funcionalidad asignado.
	 * @param con
	 * @param loginUsuario
	 * @param codigo_func
	 * @return true or false
	 * @author artotor
	 */
	public static boolean tieneRolFuncionalidad(Connection con,String loginUsuario, int codigo_func) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().tieneRolFuncionalidad(con,loginUsuario,codigo_func);
	}

	/**
	 * Metodo que retorna true si un usuario tien el rol de una funcionalidad asignado.
	 * @param con
	 * @param loginUsuario
	 * @param codigo_func
	 * @return true or false
	 * @author artotor
	 */
	public static boolean tieneRolFuncionalidad(String loginUsuario, int codigo_func) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean a=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().tieneRolFuncionalidad(con,loginUsuario,codigo_func);
		UtilidadBD.closeConnection(con);
		return a;
	}
	
	
	/**
	 * Metodo que retorn true si ya se encuentra definido el valor del consecutivo de ajuste debito
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean isDefinidoConsecutivoAjustesDebito(Connection con, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().isDefinidoConsecutivoAjustesDebito(con,institucion);
	}
	
	/**
	 * Metodo que retorn true si ya se encuentra definido el valor del consecutivo de ajuste debito
	 * @param con
	 * @return
	 */
	public static boolean isDefinidoConsecutivoAjustesCredito(Connection con, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().isDefinidoConsecutivoAjustesCredito(con,institucion);
	}

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static InfoDatosInt obtenerConvenioCuentaCobro(Connection con, double cuentaCobro, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenioCuentaCobro(con,cuentaCobro,institucion); 
	}

	/**
	 * Metodo que retorna la descripcion de Concepto de Ajuste dado su codigo y la institucion
	 * @param conceptoAjuste
	 * @param institucion
	 * @return
	 */
	public static String obtenerDescripcionConceptoAjuste(Connection con,String conceptoAjuste,int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDescripcionConceptoAjuste(con,conceptoAjuste,institucion); 
	}
	
	/**
	 * Metodo que retorna la descripcion del metodo de ajuste dado su codigoy la institucion
	 * @param metodoAjuste
	 * @return
	 */
	public static String obtenerDescripcionMetodoAjuste(Connection con,String metodoAjuste) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDescripcionMetodoAjuste(con,metodoAjuste); 
	}

	/**
	 * Metodo que retorna el codigo de una factura dado el codigo del ajuste
	 * registrado, este metodo es util cuando el ajuste se hizo por facturas, ya
	 * que si se trata de un ajuste por cuentas de cobro, este retorna la primera
	 * factura que encuentre.
	 * @param con
	 * @param codigoAjuste
	 */
	public static int obtenerFacturaAjuste(Connection con, double codigoAjuste) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFacturaAjuste(con,codigoAjuste); 
	}

	/**
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerConsecutivoFactura(Connection con,int codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConsecutivoFactura(con,codigoFactura);
	}

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static InfoDatosInt obtenerConvenioFactura(Connection con, int factura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenioFactura(con,factura);
	}

	/**
	 * metodo que retorna la fecha de aprobacion de un ajuste en formato dd/mm/aaaa
	 * @param con
	 * @param d
	 * @return
	 */
	public static String obtenerFechaAprobacionAjustes(Connection con, double codigoAjuste) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaAprobacionAjustes(con,codigoAjuste);
	}
	
	/**
	 * @param d
	 * @return
	 */
	public static String obtenerestadoAjuste(double codigoAjuste) 
	{
		String cod="";
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			cod=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerestadoAjuste(con,codigoAjuste);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cod;
	}
	
	/**
	 * Método para consultar los datos generales de una persona en AXIOMA
	 * sin importar si es mï¿½dico, usuario o paciente
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	public static HashMap obtenerDatosPersona(Connection con,String tipoId,String numeroId)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDatosPersona(con,tipoId,numeroId);
	}

	/**
	 * Metodo que retorn true si un ajuste es generado por una reversion.
	 * es decir si el ajuster es reversion de otro.
	 * @param codigo de aluste
	 * @return
	 */
	public static boolean esAjusteDeReversion(double codigoAjuste) 
	{
		boolean respuesta=true;//producir error por defecto
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esAjusteDeReversion(con,codigoAjuste);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static double obtenerSaldoFactura(Connection con, int codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSaldoFactura(con,codigoFactura);
	}
	
	/**
	 * Método usado (en el cabezote Superior) para verificar si en la funcionalidad de 2ï¿½ Nivel
	 * se debe esconder los datos del paciente del cabezaote superior
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean deboEsconderPaciente(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().deboEsconderPaciente(con,codigo);
	}
	
	/**
	 * Método usado (en el cabezote Superior) para verificar si en la funcionalidad de 2ï¿½ Nivel
	 * se debe esconder los datos del paciente del cabezaote superior
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean deboEsconderPaciente(int codigo)
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean resp= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().deboEsconderPaciente(con,codigo);
		UtilidadBD.closeConnection(con);
		return resp;
	}
	
	/**
	 * Método usado para consultar la fecha de reversiï¿½n de egreso a una cuenta especï¿½fica
	  * @param con
	 * @param idCuenta
	 * @return fecha de reversiï¿½n
	 */
	public static String obtenerFechaReversionEgreso(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaReversionEgreso(con,idCuenta);
	}
	
	/**
	 * Salas Cirugï¿½a:
	 * Método usado para obtener el nombre de un tipo de sala
	 * parametrizado en el sistema
	 * @param con
	 * @param codigoTipoSala
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreTipoSala(Connection con,int codigoTipoSala)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoSala(con,codigoTipoSala);
	}
	
	/**
	 * Salas Cirugï¿½a:
	 * Método que consulta el nombre [0] y acrï¿½nimo [1] del tipo
	 * de asocio.
	 * @param con
	 * @param codigoTipoAsocio
	 * @return
	 */
	public static String[] obtenerNombreTipoAsocio(Connection con,int codigoTipoAsocio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoAsocio(con,codigoTipoAsocio);
	}
	
	/**
	 * Método que consulta la descripciï¿½n de un esquema tarifario
	 * esGeneral => "true" se consultarï¿½ un esquema tarifario general de la tabla  esq_tar_porcen_cx
	 * esGeneral=> "false" se cosnultarï¿½ un esquema tarifario especï¿½fico de la tabla esquemas_tarifarios
	 * @param con
	 * @param esGeneral
	 * @param codigo
	 * @param obtenerNomEsquemaTarifarioStr
	 * @return retorna null si no hay resultados
	 */
	public static String obtenerNomEsquemaTarifario(Connection con,String esGeneral,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNomEsquemaTarifario(con,esGeneral,codigo);
	}
	
	
	public static String obtenerNombreConvenio(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreConvenio(con, cuenta);
	}

    
    /**
     * @param session
     * @return
     */
    public static UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
    {
    	if(session != null){
	    	UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null){
			    logger.error("El usuario no esta cargado (null)");
			}
			return usuario;
    	}
    	logger.error("La session esta nula");
    	return null;
    }

    
    /**
     * @param string
     * @param i
     * @return
     */
    public static String obtenerEstadoReciboCaja(String numeroReciboCaja, int institucion)
    {
        String respuesta=""+ConstantesBD.separadorSplit;
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoReciboCaja(con, numeroReciboCaja,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return respuesta;
    }

    
    /**
     * @param con
     * @param institucion
     * @param nombreConsecutivo
     * @return
     */
    public static boolean isDefinidoConsecutivo(Connection con, int institucion, String nombreConsecutivo)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().isDefinidoConsecutivo(con,institucion,nombreConsecutivo);
    }

    
    /**
     * Metodo que dado el codigo de la factura cambia el estado_paciente al enviado
     * @param con
     * @param codigoFactura
     * @param estado
     */
    public static boolean cambiarEstadoPacienteFactura(Connection con, int codigoFactura, int estado)
    {
       return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().cambiarEstadoPacienteFactura(con,codigoFactura,estado);
    }

    
    /**
     * Metodo que retorna la factura a la que se le hizo un pago de paciente.
     * @param con
     * @param codigoPagoPaciente
     * @return
     */
    public static int obtenerCodigoFacturaPagosPaciente(Connection con, int codigoPagoPaciente)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoFacturaPagosPaciente(con,codigoPagoPaciente);
    }

    
    /**
     * Metodo que cambia el estado de un pago factura paciente, dado el codigo del pago y su nuevo estado
     * @param con
     * @param codigoPago
     * @param codigoEstado
     */
    public static boolean cambiarEstadoPagoFacturaPacient(Connection con, int codigoPago, int codigoEstado)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().cambiarEstadoPagoFacturaPacient(con,codigoPago,codigoEstado);
    }

    
    /**
     * Metodo que cambia el estado de un pago General Empresa, dado el codigo del pago y su nuevo estado.
     * @param con
     * @param codigoPago
     * @param codigoEstado
     * @return
     */
    public static boolean cambiarEstadoPagosGeneralEmpresa(Connection con, int codigoPago, int codigoEstado)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().cambiarEstadoPagosGeneralEmpresa(con,codigoPago,codigoEstado);
    }

    
    /**
     * @param con
     * @param codigoPago
     * @return
     */
    public static String obtenerEstadoPagosEmpresa(Connection con, int codigoPago)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoPagosEmpresa(con,codigoPago);
    }

    
    /**
     * @param con
     * @param codigoTipoDocumentoPagosReciboCaja
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static int pacienteAbonoReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().pacienteAbonoReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
    }

    
    /**
     * Metodo que retorna el numero de cajas Activas que tiene asociado un usuario
     * @param string
     * @return
     */
    public static int numCajasAsociadasUsuario(String loginUsuario)
    {
        int numCajas=0;
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			numCajas=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().numCajasAsociadasUsuario(con,loginUsuario);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numCajas;
    }

    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public static int numCajasAsociadasUsuarioXcentroAtencion(String loginUsuario, String codigoCentroAtencion)
    {
    	int numCajas=0;
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			numCajas=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().numCajasAsociadasUsuarioXcentroAtencion(con,loginUsuario, codigoCentroAtencion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numCajas;
    }

	/**
	 * Numero de cajas de un usuario por centro de atenciï¿½n, segï¿½n el o los tipos de caja
	 * @param con
	 * @param loginUsuario
	 * @param codigoCentroAtencion
	 * @param tipos
	 * @return (int) nï¿½mero de cajas
	 */
    public static int numCajasAsociadasUsuarioXcentroAtencion(String loginUsuario, String codigoCentroAtencion, int[] tipos)
    {
    	int numCajas=0;
		String tipoBd=System.getProperty("TIPOBD");
		Connection con;
		try {
			con = DaoFactory.getDaoFactory(tipoBd).getConnection();
			numCajas=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().numCajasAsociadasUsuarioXcentroAtencion(con,loginUsuario, codigoCentroAtencion,  tipos);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numCajas;
    }

    
    /**
     * Metodo que retorna los consecutivos de las cajas(Activas) asociada a un usuario.
     * @param con
     * @param string
     * @return
     */
    public static ResultSetDecorator getConsecutivosCajaUsuario(Connection con, String loginUsuario, String codigoCentroAtencion)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getConsecutivoCajaUsuario(con,loginUsuario, codigoCentroAtencion);
    }

    
    /**
     * Metodo que retorna el codigo de una caja dado su consecutivo.
     * @param con
     * @param consecutivoCaja
     * @return
     */
    public static int getCodigoCaja(Connection con, int consecutivoCaja)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCodigoCaja(con,consecutivoCaja);
    }

    
    /**
     * Metodo que optine la descripcion de una caja, dado su consecutivo
     * @param con
     * @param i
     * @return
     */
    public static String getDescripcionCaja(Connection con, int consecutivoCaja)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getDescripcionCaja(con,consecutivoCaja);
    }
    
    /**
     * Método que verifica si una solicitud de cirugï¿½as tiene un pedido Qx.
     * asociado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean tienePedidoSolicitudQx(Connection con,int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().tienePedidoSolicitudQx(con,numeroSolicitud);
    }
    
    /**
     * Método que verifica si una Peticion Qx tiene solicitud asociada
     * @param con
     * @param numeroPeticion
     * @return
     */
    public static boolean tieneSolicitudPeticionQxStr(Connection con, int numeroPeticion)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().tieneSolicitudPeticionQxStr(con, numeroPeticion);
    }
    
    /**
     * Método que consulta la subcuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static int getSubCuentaSolicitud(Connection con,int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getSubCuentaSolicitud(con,numeroSolicitud);
    }
    
    /**
     * Método que consulta la cuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static int getCuentaSolicitud(Connection con,int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCuentaSolicitud(con,numeroSolicitud);
    }
    
    /**
     * Método que consulta el nombre de una especialidad
     * @param con
     * @param especialidad
     * @return
     */
    public static String getNombreEspecialidad(Connection con,int especialidad)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreEspecialidad(con,especialidad);
    }
    
    /**
     * Método que consulta el nombre de un pool
     * @param con
     * @param pool
     * @return
     */
    public static String getNombrePool(Connection con,int pool)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombrePool(con,pool);
    }
    
    /**
     * Método que retorna la peticiï¿½n de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static int getPeticionSolicitudCx(Connection con,int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getPeticionSolicitudCx(con,numeroSolicitud);
    }
    
    /**
     * Método que consulta la fecha del cargo de una orden quirï¿½rgica
     * retorna una cadena vacï¿½a en el caso de que la orden no tenga cargo generado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static String getFechaCargoOrdenCirugia(Connection con,int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getFechaCargoOrdenCirugia(con,numeroSolicitud);
    }
    
    /**
     * Método para saber si un convenio tiene (False - True) el atributo
	 * de info_adic_ingreso_convenios
     * @param con
     * @param codigoConvenio
     * @return
     */
	public static boolean convenioTieneIngresoInfoAdic(Connection con, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().convenioTieneIngresoInfoAdic(con, codigoConvenio);
	}
	/**
	 * Método para obtener el saldo de un convenio dada su cuenta y el convenio
	 * @param con
	 * @param idCuenta
	 * @param codigoConvenio
	 * @return
	 */
    public static double obtenerSaldoConvenio(Connection con,String idSubCuenta)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSaldoConvenio(con, idSubCuenta);
    }
    
    /**
     * Método para obtener el codigo de un convenio segun una cuenta dada
     * @param con
     * @param idCuenta
     * @return
     */
    public static int obtenerCodigoConvenio(Connection con, int idCuenta)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoConvenio(con, idCuenta);
    }
    
    /**
     * Método para obtener la fecha de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerFechaAdmisionUrg(Connection con, int idCuenta)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaAdmisionUrg(con, idCuenta);
    }
 
    /**
     * Método para obtener la fecha de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerFechaAdmisionHosp(Connection con, int idCuenta)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaAdmisionHosp(con, idCuenta);
    }
    
    /**
     * Método para obtener el tipo de anestesia de una solicitud de cirugï¿½as
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static InfoDatosInt obtenerTipoAnestesia(Connection con, int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoAnestesia(con, numeroSolicitud);
    }
    
    
    /**
	 * Método que carga la hora con segundos de la base de datos para no utilizar la del sistema
	 * @return String con la Hora del sistema con segundos
	 */
	public static String capturarHoraSegundosBD()
	{
		Connection con;
		String hora="";
		try
		{
			con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			hora=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().capturarHoraSegundosBD(con);
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e)
		{
			logger.error("Error capturando la hora con segundos del sistema "+e);
			return null;
		}
		return hora;
	}
	
    
    /**
	 * Método que carga la hora con segundos de la base de datos para no utilizar la del sistema
	 * @return String con la Hora del sistema con segundos
	 */
	public static String capturarHoraSegundosBD(Connection con)
	{
		String hora="";
		hora=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().capturarHoraSegundosBD(con);
		return hora;
	}
	
	/**
	 * Método para obtener el nombre de un grupo de servicio dado su codigo
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	public static String getNombreGrupoServicios(Connection con, int codigoGrupo)
    {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreGrupoServicios(con, codigoGrupo);
    }
	/**
	 * Método para obtener el total de un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 */
	public static double obtenerTotalPresupuesto(Connection con, int codigoPresupuesto)
    {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTotalPresupuesto(con, codigoPresupuesto);
    }
	
	/**
	 * Método para obtener el total de un grupo de servicios dado, relacionado
	 * con un presupuesto de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public static double obtenertotalGrupoServicios(Connection con, int presupuesto, int grupo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenertotalGrupoServicios(con, presupuesto, grupo);
	}
	
	
	/**
	 * Método para obtener el total de los articulos de un presupuesto
	 * @param con
	 * @param presupuesto
	 * @return
	 */
	public static double obtenerTotalArticulosPresupuesto(Connection con, int presupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTotalArticulosPresupuesto(con, presupuesto);
	}

    /**
     * 
     * @param numeroSolicitud
     * @return
     */
    public static int obtenerNumeroOrdenSolicitud(int numeroSolicitud)
    {
        Connection con;
        int var=ConstantesBD.codigoNuncaValido;
        try
        {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
            var=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNumeroOrdenSolicitud(con,numeroSolicitud);
            UtilidadBD.closeConnection(con);
        }
        catch (SQLException e)
        {
            logger.error("Error capturando la hora con segundos del sistema "+e);
            return ConstantesBD.codigoNuncaValido;
        }
        return var;
    }
    
    /**
	 * Método para obtener el total de un grupo de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public static int obtenerTotalGrupoArticulos(Connection con, int presupuesto, int grupo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTotalGrupoArticulos(con, presupuesto, grupo);
	}
	
	/**
	 * Método para obtener el total de un subgrupo de articulos
	 * @param con
	 * @param presupuesto
	 * @param subgrupo
	 * @return
	 */
	public static int obtenerTotalSubGrupoArticulos(Connection con, int presupuesto, int subgrupo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTotalSubGrupoArticulos(con, presupuesto, subgrupo);
	}
	/**
	 * Método para obtener el total de los articulos de  un presupuesto dado asociado por calse de inventario
	 * @param con
	 * @param presupuesto
	 * @param claseinventario
	 * @return
	 */
	public static int obtenerTotalClaseInventarioArticulos(Connection con, int presupuesto, int claseinventario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTotalClaseInventarioArticulos(con, presupuesto, claseinventario);
	}
	/**
	 * MT 7068
	 * Metodo para mostar el codigo del articulo segun como lo defina el parametro general 'Codigo Manual Estandar Busqueda y Presentacion de Articulos' del modulo Administracion 
	 * se encuentra definido.
	 * 
	 * Este metodo recibe los tres posibles codigos para mostrar en el sistema y hace la validacion con la cofiguracion actual del sistema
	 * 
	 * @param codigoNaturaleza	String  si es medicamento Pos o nos 
	 * @param codigoAxioma		Integer el coodigo axioma del codigo 
	 * @param codigoInstitucion Integer institucion a la que pertenece el profesional de la salud 
	 * @param codigoInterfaz	String  codigo de interfaz definida en la creacion del articulo 
	 * @param codigoCUM			String  Codigo cum consulado en la base de datos 
	 * @return	Retorna el codigo que se debe mostrar para el articulo de tipo medicamento
	 * @since 29 de agosto 2013
	 * @author LuiFerJa
	 * 
	 */
	public static String getPresentacionCodigoArticulo(String codigoNaturaleza,Integer codigoAxioma,Integer codigoInstitucion,String codigoInterfaz,String codigoCUM){
		String result ="";
		  //Si es un medicamento pos 
		if(codigoNaturaleza.equals(ConstantesBD.codigoNaturalezaArticuloMedicamentoNoPos)){
			if (!codigoCUM.equals(""+codigoAxioma)) {
				return codigoCUM;
			} 
		} 
		if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos( codigoInstitucion).equals(ConstantesBD.codigoNuncaValido)){
			result = codigoAxioma.toString();								
		}else{
			if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
			{				
					result = codigoAxioma.toString();					 
			}
			else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
			{
				if(codigoInterfaz !=null && !codigoInterfaz.equals("") && !codigoInterfaz.equals(""+ConstantesBD.codigoNuncaValido)){
					result = codigoInterfaz;
				}else{						
					result = codigoAxioma.toString(); 
				}
			}else{
				//Si el articulo NO tiene definio el codigo del parametro general se debe mostrar el Codigo Axioma  del articulo seleccionado
				result = codigoAxioma.toString();					
			}
		}
		
		return result;
	}

	/**
	 * Método que me indica si una cuenta tiene subcuentas
	 * independiente del estado de la misma
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean haySubcuentasParaCuentaDada(Connection con, int codigoCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().haySubcuentasParaCuentaDada(con, codigoCuenta);
	}
	
	
	
    /**
     * Método que consulta la fecha de apertura de la cuenta de un paciente dado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static String getFechaSolicitud(Connection con,int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getFechaSolicitud(con,numeroSolicitud);
    }

    /**
     * Método que consulta la fecha de apertura de la cuenta de un paciente dado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static String getFechaAperturaCuenta(Connection con,int numeroCuenta)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getFechaAperturaCuenta(con,numeroCuenta);
    }
    /**
	 * Método para obtener el consecutivo de ordenes medicas dado su numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int getConsecutivoOrdenesMedicas(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getConsecutivoOrdenesMedicas(con, numeroSolicitud);
	}

	/**
	 * Método para obtener el consecutivo de ordenes medicas dado su numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int getConsecutivoOrdenesMedicas(int numeroSolicitud)
	{
		Connection con=UtilidadBD.abrirConexion();
		int retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getConsecutivoOrdenesMedicas(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * Método para obtener las observaciones de la informacion adicional de un paciente cargado en session
	 * @param con
	 * @param codPaciente
	 * @return
	 */
	public static String getObservacionesPacienteActivo(Connection con, int codPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getObservacionesPacienteActivo(con, codPaciente);
	}
	
	/**
	 * obtener el codigo de la cuenta asociada dado el codigo de la cuenta final
	 * retorna "" en caso de que no exista
	 */
	public static String getCuentaAsociada(Connection con, String codigoCuentaFinal,boolean activo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCuentaAsociada(con, codigoCuentaFinal,activo);
	}
	
	/**
     * Método para obtener la hora de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerHoraAdmisionHosp(Connection con, int idCuenta)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerHoraAdmisionHosp(con, idCuenta);
    }
    
    /**
     * Método para obtener la hora de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerHoraAdmisionUrg(Connection con, int idCuenta)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerHoraAdmisionUrg(con, idCuenta);
    }

    /**
     * Metodo que retorna el consecutivo de la factura
     * @param codFactura
     * @return
     */
	public static String obtenerConsecutivoFactura(int codFactura) 
	{
		 Connection con;
	        String var=ConstantesBD.codigoNuncaValido+"";
	        try
	        {
	            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
	            var=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConsecutivoFactura(con,codFactura);
	            UtilidadBD.closeConnection(con);
	        }
	        catch (SQLException e)
	        {
	            logger.error("Error consultando el consecutivo de la factura "+e);
	            return ConstantesBD.codigoNuncaValido+"";
	        }
	        return var;
	}

	/**
	 * Metodo que retorna el responasble de una factura.
	 * @param i
	 * @return
	 */
	public static String getResponsableFactura(int codFactura) 
	{
		Connection con;
        String var=ConstantesBD.codigoNuncaValido+"";
        try
        {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
            var=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getResponsableFactura(con,codFactura);
            UtilidadBD.closeConnection(con);
        }
        catch (SQLException e)
        {
            logger.error("Error consultando el consecutivo de la factura "+e);
            return ConstantesBD.codigoNuncaValido+"";
        }
        return var;
	}

	public static double obtenerValorTotalReciboCaja(int codigoInstitucionInt, String numReciboCaja) 
	{
		Connection con;
        double var=0;
        try
        {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
            var=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerValorTotalReciboCaja(con,codigoInstitucionInt,numReciboCaja);
            UtilidadBD.closeConnection(con);
        }
        catch (SQLException e)
        {
            logger.error("Error consultando el consecutivo de la factura "+e);
            return 0;
        }
        return var;
	}

	/**
	 * 
	 * @param codigoInstitucionInt
	 * @param numReciboCaja
	 * @return
	 */
	public static String reciboCajaRecibidoDe(int institucion, String numReciboCaja) 
	{
		Connection con;
        String var="";
        try
        {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
            var=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().reciboCajaRecibidoDe(con,institucion,numReciboCaja);
            UtilidadBD.closeConnection(con);
        }
        catch (SQLException e)
        {
            logger.error("Error consultando el consecutivo de la factura "+e);
            return "";
        }
        return var;
	}

	/**
	 * Metodo que retorna la informacion basica de la anulacion de una factura.
	 * codigoFactura+separadorSplit+fechaanulacion+separadorSplit+horaanulacion+separadorSplit+usuarioanula+separadorSplit+consecutivoanulacion;
	 * La fecha ya viene en formato aplicacion.
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerInformacionAnulacionFactura(Connection con, String codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInformacionAnulacionFactura(con, codigoFactura);
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static int obtenerCodigoEstadoFacturacionFactura(int codigoFactura) 
	{
		Connection con;
        int estado=ConstantesBD.codigoNuncaValido;
        try
        {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
            estado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoEstadoFacturacionFactura(con,codigoFactura);
            UtilidadBD.closeConnection(con);
        }
        catch (SQLException e)
        {
            logger.error("Error consultando el consecutivo de la factura "+e);
        }
        return estado;
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public static int obtenerCodigoEstadoPacienteFactura(int codigoFactura) 
	{
		Connection con;
        int estado=ConstantesBD.codigoNuncaValido;
        try
        {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
            estado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoEstadoPacienteFactura(con,codigoFactura);
            UtilidadBD.closeConnection(con);
        }
        catch (SQLException e)
        {
            logger.error("Error consultando el consecutivo de la factura "+e);
        }
        return estado;
	}
	
		
	/**
	 * obtiene el total admin farmacia
	 * @param con
	 * @param codigoArticuloStr
	 * @param numeroSolicitudStr
	 * @param traidoUsuario
	 * @return
	 */
	public static int getTotalAdminFarmacia(Connection con, String codigoArticuloStr, String numeroSolicitudStr, boolean traidoUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getTotalAdminFarmacia(con, codigoArticuloStr, numeroSolicitudStr, traidoUsuario);
	}
	
	/**
	 * Método que devuelve el nombre del ï¿½ltimo tipo de monitoreo del paciente cuando estï¿½ en Cama UCI,
	 * ya sea de la orden mï¿½dica o la evoluciï¿½n
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @param obtenerNombre @todo
	 * @return nombre del ï¿½ltimo tipo de monitoreo
	 */
	public static String obtenerUltimoTipoMonitoreo (Connection con, int codigoCuenta, int institucion, boolean obtenerNombre)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUltimoTipoMonitoreo(con, codigoCuenta, institucion, obtenerNombre);
	}
	
	/**
	 * Método implementado para obtener el codigo de la cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public static int getUltimaCamaTraslado(Connection con,int cuenta)
	{
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getUltimaCamaTraslado(con,cuenta);
	}
	/**
	 * Metodo para obtener el codigo de la ocupacion Medica dado el codigo del medico.
	 * Retorna -1 si no encuentra la ocupacion con el codigo del medico dado.
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
    public static int obtenerOcupacionMedica(Connection con, int codigoMedico)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerOcupacionMedica(con, codigoMedico);
    }

    /**
     * 
     * @param con
     * @param codigoCama
     * @return
     */
	public static int obtenerCodigoEstadoCama(Connection con, int codigoCama) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoEstadoCama(con,codigoCama);
	}


	
	/**
	 * Metodo realiza una consulta y devuelve el nro de registros. 
	 * @return
	 */
	public static int nroRegistrosConsulta (Connection con,String consulta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().nroRegistrosConsulta(con,consulta);
	}

	/**
	 * 
	 * @param con
	 * @param numeroAjuste
	 * @param tipoAjuste
	 * @param institucion
	 * @return
	 */
	public static double obtenercodigoAjusteEmpresa(Connection con, String numeroAjuste, String tipoAjuste, int institucion) 
	{
		double temp=ConstantesBD.codigoNuncaValido;
		temp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenercodigoAjusteEmpresa(con,numeroAjuste,tipoAjuste,institucion);
		return temp;
	}
	
	/**
	 * Método que consulta la fecha de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getFechaEgreso(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getFechaEgreso(con,idCuenta);
	}
	
	/**
	 * Método implementado para consultar la descripcion completa
	 * del diagnï¿½stico de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getNombreDiagnosticoEgreso(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreDiagnosticoEgreso(con,idCuenta);
	}
	
	/**
	 * Método implementado para consultar la descripcion compelta
	 * del diagnï¿½stico de ingreso de un admision hospitalaria
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getNombreDiagnosticoIngreso(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreDiagnosticoIngreso(con,idCuenta);
	}
	
	/**
	 * Método para saber si un tipo de rompimiento parametrizado en un formato de impresion de factura
	 * tien true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getImprimirDetalleTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getImprimirDetalleTipoRompimientoServ(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Método para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tien true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getImprimirDetalleTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getImprimirDetalleTipoRompimientoArt(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Método para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirSubTotalTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getImprimirSubTotalTipoRompimientoArt(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Método para saber si un tipo de rompimiento de servicios parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirSubTotalTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getImprimirSubTotalTipoRompimientoServ(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Método para actualizar en la agenda el medico que atiende una cita 
	 * @param con
	 * @param codigoMedico
	 * @param codigoAgenda
	 * @return
	 */
	public static boolean actualizarMedicoAgenda(Connection con, int codigoMedico, int codigoAgenda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarMedicoAgenda(con, codigoMedico, codigoAgenda);
	}
	
	/**
	 * Metodo para saber si un centro de costo esta asociado a un usuairo x alamacen
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static boolean centroCostoAsociadoUsuarioXAlmacen(Connection con, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().centroCostoAsociadoUsuarioXAlmacen(con, centroCosto, institucion);
	}
	
	/**
	 * Método para saber si existe un centro de costo en la tabla centros_costo
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static boolean existeCentroCosto(Connection con, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeCentroCosto(con, centroCosto, institucion);
	}
	
	/**
	 * Método para saber si un centro de costo esta asociado a algun usuario
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static boolean centroCostoAsociadoUsuario(Connection con, int centroCosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().centroCostoAsociadoUsuario(con, centroCosto);
	}
	
	/**
	 * Método usado para obtener el nombre de un centro de atenciï¿½n,
	 * de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreCentroAtencion(Connection con,int consecutivoCentroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreCentroAtencion(con,consecutivoCentroAtencion);
	}
	
	
	
	/**
	 * Metodo para obtener el nombre (razon social) de la institucion 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreInstitucion(Connection con,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreInstitucion(con,institucion);
	}
	
	
	
	/**
	 * Método para obtener el nombre de un centro de costo dado su codigo para una
	 * institucion determinada
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreCentroCosto(Connection con, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreCentroCosto(con, centroCosto, institucion);
	}
	
	/**
	 * Método para obtener el nombre de un centro de costo dado su codigo para una
	 * institucion determinada
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreCentroCosto(int centroCosto, int institucion)
	{
		String resultado;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreCentroCosto(con, centroCosto, institucion);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	
	/**
	 * Método para obtener el nombre de una via de ingreso dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreViaIngreso(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreViaIngreso(con, codigo);
	}


	
	
	/**
	 * Metodo que retorna la descripcion de un centro de atencion dado su codigo.
	 * @param con
	 * @param codigoArea
	 */
	public static int obtenerCentroAtencionPaciente(Connection con, int codigoArea) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentroAtencionPaciente(con, codigoArea);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCentroCostoTratante
	 */
	public static boolean actualizarAreaCuenta(Connection con, int codigoCuenta, int codigoCentroCostoTratante) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarAreaCuenta(con,codigoCuenta,codigoCentroCostoTratante);
	}
	
	/**
	 * Método que consulta los centros de costo de una vï¿½a de ingreso
	 * en un centro de atenciï¿½n especï¿½fico
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public static HashMap getCentrosCostoXViaIngresoXCAtencion(Connection con,int viaIngreso,int centroAtencion,int institucion,String tipoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCentrosCostoXViaIngresoXCAtencion(con,viaIngreso,centroAtencion,institucion, tipoPaciente);
	}
	
	/**
	 * Metodo que consulta el nombre del convenio dado el codigo del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNombreConvenioOriginal(Connection con, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreConvenioOriginal(con, codigoConvenio);
	}
	
	/**
	 * Metodo que consulta el nombre del convenio dado el codigo del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNombreConvenioOriginal(int codigoConvenio)
	{
		Connection con= UtilidadBD.abrirConexion();
		String retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreConvenioOriginal(con, codigoConvenio);
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerLLaveTraigeAdmisionUrgeciasXCuenta(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerLLaveTraigeAdmisionUrgeciasXCuenta(con,idCuenta);
	}
	
	/**
	 * Metodo que actualiza la cantidad de una solicitud de mezcla es decir la cantidad de un articulo en orden_nutricion_parente(Orden_dieta)
	 * @param con
	 * @param numOrdenDieta
	 * @param codigoArticulo
	 * @param cantidad
	 * @return
	 */
	public static boolean actualizarCantidadArticuloSolMedicamentos(Connection con,String numSolicitud,String codigoArticulo,String cantidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarCantidadArticuloSolMedicamentos(con,numSolicitud,codigoArticulo,cantidad);
	}
	
	/**
	 * Metodo para saber si una orden de dieta esta finalizada. 
	 * como la finalizacion genera otro registro en orden_dieta,  
	 * entonces buscar los registros siguientes a este.  
	 * @param con
	 * @param codigoOrdenDieta
	 */
	public static boolean getOrdenDietaFinaliza(Connection con, String codigoOrdenDieta, int codigoCuenta, int codigoCuentaAsocio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getOrdenDietaFinaliza(con,codigoOrdenDieta, codigoCuenta, codigoCuentaAsocio);
	}

	/**
	 * Método para obtener el valor inicial de la cuenta de cobro
	 * @param con
	 * @param codigoCuentaCobro
	 * @param codigoInsititucion
	 * @return
	 */
	public static double obtenerValorInicialCuentaCobroCapitacion (Connection con, int codigoCuentaCobro, int codigoInstitucion)
    {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerValorInicialCuentaCobroCapitacion(con, codigoCuentaCobro, codigoInstitucion);
    }
	
	/**
	 * Metodo apra saber si como tiene parametrizado un formato de impresion 
	 * determinado el campo que indica si se deben mostrar los servicios qx o no 
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getMostrarServiciosQx(Connection con, int codigoFormato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getMostrarServiciosQx(con, codigoFormato);
	}
	
	/**
	 * Utilidad para obtener los datos de los campos parametrizables
	 * @param con
	 * @param medico
	 * @param funcionalidad
	 * @param seccion
	 * @param centroCosto
	 * @param institucion
	 * @param codigoTabla
	 * @return
	 */
	public static HashMap<Object, Object> obtenerInformacionParametrizada(Connection con, int medico, String funcionalidad, String seccion, int centroCosto, String institucion, int codigoTabla)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInformacionParametrizada(con, medico, funcionalidad, seccion, centroCosto, institucion, codigoTabla);
	}
	
	/**
	 * Método para obtener el nï¿½mero de la ï¿½ltima cuenta de cobro
	 * de capitaciï¿½n
	 * @param con -> Connection
	 * @param codigoInstitucion
	 * @return ultimaCuentaCobro
	 */
	public static int obtenerUltimaCuentaCobroCapitacion(Connection con, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUltimaCuentaCobroCapitacion(con, codigoInstitucion);
	}
	
	/**
	 * Método para obtener el codigo del registro de enfermeria para una cuenta determinada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCodigoRegistroEnfermeria(Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoRegistroEnfermeria(con, idCuenta);
	}
	
	/**
	 * Método que recibe una coleccion de datos y de acuerdo a la columna que se
	 * especifique como ï¿½nica forma una nueva colecciï¿½n sin registros repetidos,
	 * se utiliza para los tipos parametrizados por instituciï¿½n centro de costo
	 * @param coleccion
	 * @param campoUnico
	 * @author amruiz
	 */
	public static Collection coleccionSinRegistrosRepetidos (Collection coleccionTipos, String campoUnico)
	{
		Collection nuevaColeccion;
		HashMap			nuevoHashMap;
		
		//-----Se crea una nueva colecciï¿½n para ingresar los tipos no repetidos
		nuevaColeccion=new LinkedList();
		
		//----Se obtiene el nï¿½mero de registros de la colecciï¿½n
		int nroRegistros=coleccionTipos.size();
				
		if(nroRegistros > 0)
		{
			Iterator iterador1=coleccionTipos.iterator();
			String tempo="";
			//-----Se itera la colecciï¿½n y se carga en la nueva de forma que no queden repetidos
			//---de acuerdo al nombre del key enviado por parï¿½metro (campoUnico)
			for(int i=0; i<nroRegistros; i++)
			{
				HashMap fila=(HashMap)iterador1.next();
				
				String tipo=fila.get(campoUnico)+"";
				//-----Si son diferentes tipo y tempo entonces se agrega a la nueva colecciï¿½n
				if(!tipo.equals(tempo))
				{
					nuevoHashMap = new HashMap();
					nuevoHashMap= fila;
					
					tempo=fila.get(campoUnico)+"";
				
					nuevaColeccion.add(nuevoHashMap);
				}
			}//for
			//logger.info("\n nueva coleccion -->"+nuevaColeccion.size()+"\n");
			return nuevaColeccion;
		}//if
		
		else
			return coleccionTipos;
	}

	/**
	 * Metodo que retorna true or false indicando si un servicio es contratado para una cuenta determinada
	 * @param con
	 * @param cuenta
	 */
	public static boolean esNivelServicioContratado(Connection con, String cuenta,String servicio) 
	{
		int contrato=Contrato.getContratoCuenta(con,cuenta);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esNivelServicioContratado(con,servicio,contrato);
	}
	
	/**
	 * Método usado para obtener el nombre de un tipo de calificaciï¿½n
	 * pyp de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreTipoCalificacionPyP(Connection con,int consecutivo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoCalificacionPyP(con,consecutivo);
	}
	
	/**
	 * Método usado para obtener el nombre de un tipo de tipo de transacciï¿½n de inventario
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreTransaccionInventario(Connection con,int consecutivo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTransaccionInventario(con,consecutivo);
	}
	
	/**
	 * Método implementado para consultar el nombre de una ocupaciï¿½n mï¿½dica
	 * @param con
	 * @param codigoOcupacion
	 * @return
	 */
	public static String obtenerNombreOcupacionMedica(Connection con,int codigoOcupacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreOcupacionMedica(con,codigoOcupacion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoSubCuenta
	 * @param totalSubCuenta 
	 */
	public static boolean actualizarTotalSubCuenta(Connection con, int codigoSubCuenta, double totalSubCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarTotalSubCuenta(con,codigoSubCuenta,totalSubCuenta);
	}

	/**
	 * Metodo para obtener el tipo de servicio de un servicio.
	 * @param con
	 * @param servicio
	 */
	public static String obtenerTipoServicio(Connection con, String servicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoServicio(con,servicio);
	}

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param codigoTarifarioCups
	 * @return
	 */
	public static String obtenerCodigoPropietarioServicio(Connection con, String servicio, int codigoTarifario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoPropietarioServicio(con,servicio,codigoTarifario);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerEstadoHistoriaClinicaSolicitud(Connection con, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoHistoriaClinicaSolicitud(con,numeroSolicitud);
	}

	/**
	 * Metodo que retorna un info datos con la informacion del estado de historia clinica de una solicitud.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatosString obtenerEstadoHC(Connection con,String numeroSolicitud)throws SQLException,Exception
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoHC(con,numeroSolicitud);
	}
	/**
	 * Metodo que retorna el codigo del tipo de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCodigoTipoSolicitud(Connection con, String numeroSolicitud) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoSolicitud(con,numeroSolicitud);
	}

	/**
	 * 
	 * @param grEtareo
	 * @return
	 */
	public static String[] obtenerRango_UnidadMedidaGrupoEtareoPYP(String grEtareo) 
	{
		Connection con=UtilidadBD.abrirConexion();
		String respu[]=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerRango_UnidadMedidaGrupoEtareoPYP(con,grEtareo);
		UtilidadBD.closeConnection(con);
		return respu;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudPYP(Connection con, int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esSolicitudPYP(con,numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudPYP(int numeroSolicitud) 
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esSolicitudPYP(con,numeroSolicitud);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param i
	 * @return
	 */
	public static String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroOrden, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoOrdenAmbulatoria(con,numeroOrden,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param numeroSolicitud
	 */
	public static boolean asignarSolicitudToActividadPYP(Connection con, String codigoOrden, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().asignarSolicitudToActividadPYP(con,codigoOrden,numeroSolicitud);
	}
	
	/**
	 * M+etodo que verifica si el convenio de una solicitud tiene
	 * activo el campo de unificar PYP
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean tieneConvenioUnificarPYP(Connection con,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().tieneConvenioUnificarPYP(con,numeroSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 */
	public static HashMap consultarDetalleOrdenAmbulatoriaArticulos(Connection con, String ordenAmbulatoria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarDetalleOrdenAmbulatoriaArticulos(con,ordenAmbulatoria);
	}
	
	/**
	 * Método implementado para actualizar el acumulado de PYP por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @return
	 */
	public static int actualizarAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarAcumuladoPYP(con,numeroSolicitud,centroAtencion);
	}

	/**
	 * Metotdo que retorna el codigo de la actividad de una solicitud pero para el paciente. (No es el codigo de la actividad como tal, sino el codigo para el paciente.)
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(Connection con, int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,numeroSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param codigoActividad
	 * @param codigoEstadoProgramaPYPEjecutado
	 * @param loginUsuario
	 * @param string
	 */
	public static boolean actualizarEstadoActividadProgramaPypPaciente(Connection con, String codigoActividad, String estado, String loginUsuario, String comentario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarEstadoActividadProgramaPypPaciente(con,codigoActividad,estado,loginUsuario,comentario);
	}

	/**
	 * Metodo para obtener el codigo de una orden ambulatoria de una actividad, dado el numero de solicitud asociado a la actividad
	 * de no tener orden asociada, o no encontrar informacion se retorn CodigoNuncaValido
	 * @param con
	 * @param numeroSolicitud
	 */
	public static String obtenerCodigoOrdenAmbulatoriaActividad(Connection con, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoOrdenAmbulatoriaActividad(con,numeroSolicitud);
	}
	
	/**
	 *  Metodo para obtener el codigo de una orden ambulatoria dado el numero de solicitud,de no tener orden asociada, o no encontrar informacion se retorn CodigoNuncaValido
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoOrdenAmbulatoria(con,numeroSolicitud);
	}
	
	
	/**
	 * Método que verifica si en Actividades por Programa estï¿½
	 * definida como requerida 
	 * @param con
	 * @param codigoActividadProgPypPac
	 * @return
	 */
	public static String esRequeridoActividadesXPrograma(Connection con, String codigoActividadProgPypPac)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esRequeridoActividadesXPrograma(con,codigoActividadProgPypPac);
	}
	
	/**
	 * Método que consulta si existen programas y actividades por convenio, y
	 * verifica si estï¿½n activas 
	 * @param con
	 * @param actividadProgramaPyp
	 * @param convenio 
	 * @return true si hay actividad(es) en true para la actividad de programa pyp
	 */
	public static boolean activaProgramaActividadesConvenio (Connection con, String actividadProgramaPyp, String convenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().activaProgramaActividadesConvenio(con,actividadProgramaPyp,convenio);
	}
	
		
	/**
	 * metodo que verifica si una solicitud es de pyp y si esta cubierta o no por el convenio
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudPypCubiertaXConvenio(Connection con, String numeroSolicitud, int codigoConvenio)
	{
		int numeroSolicitudInt= Integer.parseInt(numeroSolicitud);
		String actividadPac="";
		String actividad="";
		//primero verificamos que sea de pyp
		if(!esSolicitudPYP(con, numeroSolicitudInt))
		{
			//en caso de no ser pyp que retorne true
			return true; 
		}
		//de lo contrario es pyp, entonces verificar si existe en la informaciï¿½n que se encuentra en la funcionalidad ?Actividades por Programa?
		actividadPac=obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con, numeroSolicitudInt);
		
		//se verifica si esta definida como requerida
		actividad=esRequeridoActividadesXPrograma(con, actividadPac);
		
		if(!actividad.equals(""))
		{
			if(!activaProgramaActividadesConvenio(con, actividad, codigoConvenio+""))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return true;
	}
	

	private static String obtenerCodigoConvenioSolicitud(Connection con, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoConvenioSolicitud(con,numeroSolicitud);
	}

	public static String obtenerSexoGrupoEtareoPYP(String grEtareo) 
	{
		Connection con=UtilidadBD.abrirConexion();
		String respu=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSexoGrupoEtareoPYP(con,grEtareo);
		UtilidadBD.closeConnection(con);
		return respu;
	}
	
	/**
	 * Método usado para obtener el nombre de una cuenta contable
	 * de acuerdo al codigo enviado como parï¿½metro
	 * @param con
	 * @param codigo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreCuentaContable(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreCuentaContable(con,codigo);
	}
	
	/**
	 * metodo que obtiene el nombre de la finalidad del servicio dado el codigo de la finalidad
	 * @param codigoFinalidad
	 * @return
	 */
	public static String getNombreFinalidadServicio(String codigoFinalidad)
	{
		Connection con=UtilidadBD.abrirConexion();
		String respu= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreFinalidadServicio(con, codigoFinalidad);
		UtilidadBD.closeConnection(con);
		return respu;
	}
	
	/**
	 * metodo que obtiene el nombre de la finalidad del servicio dado el codigo de la finalidad
	 * @param codigoFinalidad
	 * @return
	 */
	public static String getNombreFinalidadServicio(Connection con,String codigoFinalidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreFinalidadServicio(con, codigoFinalidad);
	}
	
	/**
	 * metodo que obtiene tipo del servicio dado el codigo del servicio
	 * @param codigoServicio
	 * @return
	 */
	public static String getTipoServicio(String codigoServicio)
	{
		Connection con=UtilidadBD.abrirConexion();
		String respu= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getTipoServicio(con, codigoServicio);
		UtilidadBD.closeConnection(con);
		return respu;
	}

	/**
	 * 
	 * @param codigoConvenio
	 * @return
	 */
	public static double obtenerPorcentajePypContratoConvenio(String codigoConvenio) 
	{

		Connection con=UtilidadBD.abrirConexion();
		double respu= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPorcentajePypContratoConvenio(con,codigoConvenio);
		UtilidadBD.closeConnection(con);
		return respu;
	}
	

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return frecuencia@@@@@tipofrecuencia
	 */
	public static String obtenerFrecuenciaTipoFrecSolPoc(Connection con,int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFrecuenciaTipoFrecSolPoc(con, numeroSolicitud);
	}

	/**
	 *Metodo que retorna la via de ingreso de una factura dado su codigo. 
	 * @param con 
	 * @param string
	 * @return
	 */
	public static int obtenerViaIngresoFactura(Connection con, String codigoFactura) 
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViaIngresoFactura(con,codigoFactura);
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerCuentaFactura(Connection con, String codigoFactura) 
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCuentaFactura(con,codigoFactura);
	}
	
	/**
	 * Metodo que verica si una solicitud pyp es cubierta para un convenio especï¿½fico, esta validacion es requerida en distribucion de la cuenta.
	 * @return
	 */
	public static boolean esActividadSolicitudPYPCubiertaConvenio(Connection con,String numeroSolicitud,String codigoPaciente,String codigoConvenio)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esActividadSolicitudCubiertaConvenio(con,numeroSolicitud,codigoPaciente,codigoConvenio);
	}

	/**
	 * Metodo que indica si una determinada solicitud se encuentra facturada o no.
	 * es decir si se encuentra asociada a una factura o no.
	 * @param con
	 * @param numeroSolicitud
	 * @return -1 si no esta facturas, en caso contrario retorna el codigo de la factura
	 */
	public static String esSolicitudFacturada(Connection con, String numeroSolicitud)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esSolicitudFacturada(con,numeroSolicitud);
	}
	
	/**
	 * Método que verifica que el paciente no tenga cuentas abiertas en otros centros de atencion
	 * y en el caso de que la encuentre retorna la descripcion de su centro de atencion asociado
	 * @param con
	 * @param codigoPaciente
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static String getCentroAtencionIngresoAbiertoPaciente(Connection con,String codigoPaciente,String institucion,String centroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCentroAtencionIngresoAbiertoPaciente(con,codigoPaciente,institucion,centroAtencion);
	}

	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String[] obtenerCentroAtencionFactura(Connection con, int codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentroAtencionFactura(con,codigoFactura);
	}
	
	/**
	 * Método usado para obtener el nombre del usuario dado el login y el parï¿½metro primeroApellidos
	 * para indicar como se quiere el orden primero los nombres o los apellidos
	 * @param con
	 * @param login
	 * @param primeroApellidos
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreUsuarioXLogin(Connection con,String login,boolean primeroApellidos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreUsuarioXLogin(con,login, primeroApellidos);
	}
	
	/**
	 * Método usado para obtener el nombre del usuario dado el login y el parï¿½metro primeroApellidos
	 * para indicar como se quiere el orden primero los nombres o los apellidos
	 * @param login
	 * @param primeroApellidos
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreUsuarioXLogin(String login,boolean primeroApellidos)
	{
		Connection con=UtilidadBD.abrirConexion();
		String retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreUsuarioXLogin(con,login, primeroApellidos);
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * Método implementado para consultar el centro de atencion de la ultima cuenta
	 * activa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static String getNomCentroAtencionIngresoAbierto(Connection con,String codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNomCentroAtencionIngresoAbierto(con,codigoPaciente);
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actvidad
	 * @return
	 */
	public static String obtenerCodigoActividadPorPrograma(Connection con, int institucion, String programa, String actividad) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoActividadPorPrograma(con,institucion,programa,actividad);
	}
	/**
	 * Método que verifica si una solicitud estï¿½ asociada a una cita
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeCitaParaSolicitud(Connection con,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeCitaParaSolicitud(con,numeroSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 */
	public static String obtenerCodigoActividadDadaOrdenAmbulatoria(Connection con, String ordenAmbulatoria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoActividadDadaOrdenAmbulatoria(con,ordenAmbulatoria);
	}

	
	
	/**
	 * 
	 * @return
	 */
	public static HashMap obtenerEspecialidades() 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEspecialidades(con,new HashMap());
		UtilidadBD.closeConnection(con);
		return (HashMap)mapa.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public static HashMap obtenerEspecialidades(String codigosInsertados) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		mapa.put("especialidadesIngresadas",codigosInsertados);
		Connection con=UtilidadBD.abrirConexion();
		mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEspecialidades(con,mapa);
		UtilidadBD.closeConnection(con);
		return (HashMap)mapa.clone();
	}

	/**
	 * 
	 * @param codigoEspecilidad
	 * @return
	 */
	public static HashMap obtenerMedicosEspecialidad(String codigoEspecilidad) {
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerMedicosEspecialidad(con,codigoEspecilidad);
		UtilidadBD.closeConnection(con);
		return (HashMap)mapa.clone();
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerUnidadesConsulta() {
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUnidadesConsulta(con);
		UtilidadBD.closeConnection(con);
		return (HashMap)mapa.clone();
	}

	/**
	 * 
	 * @param codigoEspecilidad
	 * @return
	 */
	public static HashMap obtenerMedicosUnidadConsulta(String codigoUnidadConsulta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerMedicosUnidadConsulta(con, codigoUnidadConsulta);
		UtilidadBD.closeConnection(con);
		return (HashMap)mapa.clone(); 
	}

	/**
	 * Metodo para consultar la agenda de un medico en una determinada unidad de consulta.
	 * en caso de que el codigo medico es el codigo nunca valido consulta la agenda para la unidad de consulta en general.
	 * @param codigoUnidadConsulta
	 * @param codigoMedico
	 * @return
	 */
	public static HashMap obtenerAgendaDisponibleMedicoUnidadConsulta(String codigoUnidadConsulta,String codigoMedico) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerAgendaDisponibleMedicoUnidadConsulta(con, codigoUnidadConsulta,codigoMedico);
		UtilidadBD.closeConnection(con);
		return (HashMap)mapa.clone(); 
	}

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public static String obtenerNaturalezaServicio(Connection con, String servicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNaturalezaServicio(con,servicio);
	}

	/**
	 * 
	 * Retorna el codigo del servicio o el codigo del articulo relacionado a una actividad.
	 * @param con
	 * @param actividad
	 * @return
	 */
	public static String obtenerArtSerActividadPYP(Connection con, String actividad) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerArtSerActividadPYP(con,actividad);
	}


	/**
	 * Metodo que retorna un ArrayList con las ciudades y el respectivo departamento al que pertenceson
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoCiudad,nombreCiudad,codigoDepartamento,nombreDepartamento)
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerCiudades(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCiudades(con);
	}
	
	/**
	 * Método que retorna en un ArrayList las localidades relacionadas con la ciudad, departamento y pais
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerLocalidadesCiudades(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerLocalidadesCiudades(con);
	}
	
	/**
	 * Método que consulta las ciudades por pais
	 * @param con
	 * @param codigoPais
	 * @return
	 */
	public static ArrayList obtenerCiudadesXPais(Connection con,String codigoPais)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCiudadesXPais(con, codigoPais);
	}
	
	/**
	 * Método que consulta las ciudades por pais
	 * @param codigoPais
	 * @return
	 */
	public static ArrayList obtenerCiudadesXPais(String codigoPais)
	{
		ArrayList<HashMap<String, Object>> retorno=new ArrayList<HashMap<String,Object>>();
		Connection con=UtilidadBD.abrirConexion();
		retorno=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCiudadesXPais(con, codigoPais);
		UtilidadBD.closeConnection(con);
		return retorno;
		
	}
	
	
	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @param validarContratosAsociados boolean que indica si se debe validad que el convenio tenga contratos asociados
	 * @param aseguradora  ""--> no filtra por asegurado || "S" -->filtra por los asegurados en S || "N" --> filtra por los asegurados en N
	 * @param boolean ReporteAtencioniniUrge 
	 * @return
	 */
	public static ArrayList obtenerConvenios(
			Connection con,
			String tipoRegimen,
			String tipoContrato,
			boolean verificarVencimientoContrato,
			String fechaReferencia,
			boolean activo,
			boolean validarContratosAsociados,
			String asegurado)
	{
		HashMap campos = new HashMap();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados",validarContratosAsociados+"");
		campos.put("aseguradora",asegurado);		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
	}
	
	public static ArrayList obtenerConveniosAseguradoras()
	{
		ArrayList resultado=new ArrayList();
		Connection con=UtilidadBD.abrirConexion();
		resultado=obtenerConvenios(con, "", "", false, "", false, false, ConstantesBD.acronimoSi);
		UtilidadBD.finalizarTransaccion(con);
		return resultado;
	}
	
	
	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @param validarContratosAsociados boolean que indica si se debe validad que el convenio tenga contratos asociados
	 * @param aseguradora  ""--> no filtra por asegurado || "S" -->filtra por los asegurados en S || "N" --> filtra por los asegurados en N
	 * @param boolean ReporteAtencioniniUrge 
	 * @return
	 */
	public static ArrayList obtenerConvenios(
			Connection con,
			String tipoRegimen,
			String tipoContrato,
			boolean verificarVencimientoContrato,
			String fechaReferencia,
			boolean activo,
			boolean validarContratosAsociados,
			String asegurado,
			boolean ReporteAtencioniniUrge)
	{
		HashMap campos = new HashMap();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados",validarContratosAsociados+"");
		campos.put("aseguradora",asegurado);
		campos.put("ReporteAtencioniniUrge",ReporteAtencioniniUrge);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
	}
	
	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @return
	 */
	public static ArrayList obtenerConvenios(Connection con, String tipoRegimen, String tipoContrato,boolean verificarVencimientoContrato,String fechaReferencia,boolean activo)
	{
		HashMap campos = new HashMap();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados","false");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
	}
	
	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @param atencionOdontologica: valida los convenios con esta caracteristica
	 * @return
	 */
	public static ArrayList obtenerConvenios(Connection con, String tipoRegimen, String tipoContrato,boolean verificarVencimientoContrato,boolean activo, boolean atencionOdontologica)
	{
		HashMap campos = new HashMap();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia", UtilidadFecha.getFechaActual());
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados","false");
		campos.put("atencionOdontologica", atencionOdontologica);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
	}
	
	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConvenios(String tipoRegimen, String tipoContrato,boolean verificarVencimientoContrato,String fechaReferencia,boolean activo, String tipoAtencion, String manejaBonos , String manejaPromociones)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados","false");
		campos.put("tipoAtencion",tipoAtencion);
		campos.put("manejaBonos", manejaBonos);
		campos.put("manejaPromociones",manejaPromociones );
		/*
		 * XPlanner 2008: 173878
		 * campos.put("manejeConveniosTarjetaCliente", ConstantesBD.acronimoSi);
		 */
		logger.info(" Cargar Convenios --------------------------------------------------------------");
		ArrayList<HashMap<String, Object>> array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
		UtilidadBD.closeConnection(con);
		return array;
	}
	
	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConveniosTarjetaCliente(String tipoRegimen, String tipoContrato,boolean verificarVencimientoContrato,String fechaReferencia,boolean activo, String tipoAtencion, String manejaBonos , String manejaPromociones)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados","false");
		campos.put("tipoAtencion",tipoAtencion);
		campos.put("manejaBonos", manejaBonos);
		campos.put("manejaPromociones",manejaPromociones );
		logger.info(" Cargar Convenios --------------------------------------------------------------");
		ArrayList<HashMap<String, Object>> array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
		UtilidadBD.closeConnection(con);
		return array;
	}
	
	
	
	

	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param validarContratosAsociados boolean que indica si se debe validad que el convenio tenga contratos asociados
	 * @param activo: validar activos
	 * @return
	 */
	public static ArrayList obtenerConvenios(Connection con, String tipoRegimen, String tipoContrato,boolean verificarVencimientoContrato,String fechaReferencia,boolean activo, boolean validarContratosAsociados)
	{
		HashMap campos = new HashMap();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados",validarContratosAsociados+"");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
	}
	
	/**
	 * Metodo que retorna un ArrayList con los convenios/contrato. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @return
	 */
	public static ArrayList obtenerConveniosContrato(Connection con, String tipoRegimen, String tipoContrato,boolean verificarVencimientoContrato,String fechaReferencia,boolean activo)
	{
		HashMap campos = new HashMap();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("validarContratosAsociados","false");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConveniosContrato(con,campos);
	}

	/**
	 * Metodo para verificar si un convenio tiene chekeado Reporte de Atencion de Urgencias
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public static boolean esConvenioConReportAtencionUrg(Connection con, String codConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esConvenioConReportAtencionUrg(con,codConvenio);
	}
	
	
	/**
	 * Metodo que retorna un ArrayList con los tipos id.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (acronimo,nombre)
	 * @param con
	 * @param tipoFiltro: No es obligatorio se puede enviar por defecto vacï¿½o
	 * @return
	 */
	public static ArrayList obtenerTiposIdentificacion(Connection con,String tipoFiltro,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposIdentificacion(con,tipoFiltro,institucion);
	}
	
	/**
	 * Metodo que retorna un ArrayList con los tipos id.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (acronimo,nombre)
	 * @param tipoFiltro: No es obligatorio se puede enviar por defecto vacï¿½o
	 * @return
	 */
	public static ArrayList obtenerTiposIdentificacion(String tipoFiltro,int institucion)
	{
		ArrayList tiposID=new ArrayList();
		Connection con= UtilidadBD.abrirConexion();
		tiposID=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposIdentificacion(con,tipoFiltro,institucion);
		UtilidadBD.closeConnection(con);
		return (ArrayList)tiposID.clone();
	}
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerCodigoIngresoDadaCuenta(String idCuenta)
	{
		Connection con= UtilidadBD.abrirConexion();
		String toReturn=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoIngresoDadaCuenta(con, idCuenta);
		UtilidadBD.closeConnection(con);
		return toReturn;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerCodigoIngresoDadaCuenta(Connection con,String idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoIngresoDadaCuenta(con, idCuenta);
		
	}

	/**
	 * 
	 * @param con
	 * @param tipoSolicitud 
	 * @param codigoEstadoHCRespondida
	 * @return
	 */
	public static ArrayList obtenerSolicitudesEstado(Connection con, int estado, int tipoSolicitud) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSolicitudesEstado(con, estado,tipoSolicitud);
	}
	
	/**
	 * Método implementado para consultar la fecha de ingreso de una cuenta dependiendo de su via de ingreso
	 * @param con
	 * @param idCuenta
	 * @param viaIngreso
	 * @return
	 */
	public static String getFechaIngreso(Connection con,String idCuenta,int viaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getFechaIngreso(con,idCuenta,viaIngreso);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param numeroIdentificacion
	 */
	public static String getCodigoPaisDeptoCiudadPersona(Connection con, String tipoId, String numeroIdentificacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCodigoPaisDeptoCiudadPersona(con,tipoId,numeroIdentificacion);		
	}
	
	/**
	 * 
	 * @param idCuenta
	 * @param fechaFormatoApp
	 * @return
	 */
	public static boolean esCamaUciDadaFecha(String idCuenta, String fechaFormatoApp, String hora)
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esCamaUciDadaFecha(con, idCuenta, fechaFormatoApp, hora);
		UtilidadBD.closeConnection(con);
		return resp;
	}
	

	/**
	 * 
	 * @param consecutivo
	 * @return
	 */
	public static boolean esMotivoSircUsado(String consecutivo)
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esMotivoSircUsado(con, consecutivo);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}
	
	/**
	 * 
	 * @param codigo
	 * @param instiucion
	 * @return
	 */
	public static boolean esServicioSircUsado(String codigo, String institucion)
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esServicioSircUsado(con, codigo, institucion);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}
	
	/**
	 * Metodo que indica si concepto esta asociado en otra tabala.
	 * @param codigo
	 * @param instiucion
	 * @return
	 */
	public static boolean esConceptosPagoPoolesUsado(String codigo, String institucion)
	{	//logger.info("los valores son "+codigo+" "+institucion);
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esConceptosPagoPoolesUsado(con, codigo, institucion);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}
	
	
	
	/**
	 * Metodo que indica si concepto esta asociado en otra tabala.
	 * @param codigo
	 * @param instiucion
	 * @return
	 */
	public static boolean esEntidadSubcontratadaUsado(String codigo, String institucion)
	{	//logger.info("los valores son "+codigo+" "+institucion);
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esEntidadSubContratadaUsado(con, codigo, institucion);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}
	
	/**
	 * 
	 * @param nombreTabla
	 * @return
	 */
	public static HashMap obtenerCodigoNombreTablaMap(String nombreTabla)
	{
		Connection con=UtilidadBD.abrirConexion();
		HashMap mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoNombreTablaMap(con, nombreTabla);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * Método que consulta el id de la ultima cuenta valida del paciente de su ingreso abierto o cerrado,
	 * 
	 * en el caso de que no haya cuenta abierta se devuelve cadena vacï¿½a
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static String obtenerIdCuentaValidaIngresoAbiertoCerrado(Connection con,String codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerIdCuentaValidaIngresoAbiertoCerrado(con,codigoPaciente);
	}

	/**
	 * Metodo que retorna un mapa con la vias de ingreso ordenadas por descripcion
	 * @param con
	 * @return
	 */
	public static Object obtenerViasIngreso(Connection con,boolean usarVector)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViasIngreso(con,usarVector);
	}

	/**
	 * Metodo que retorna un objeto con la vias de ingreso ordenadas por descripcion
	 * @param con
	 * @return
	 */
	public static Object obtenerViasIngreso(boolean usarVector)
	{
		Connection con=UtilidadBD.abrirConexion();
		Object mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViasIngreso(con,usarVector);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * Metodo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param codigosViasIngresos: este campo debe ser asï¿½: 1,2,3
	 * @return
	 */
	public static ArrayList obtenerViasIngreso(Connection con,String codigosViasIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigosViasIngreso", codigosViasIngreso);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViasIngreso(con,campos);
	}
	
	/**
	 * Metodo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param codigosViasIngresos: este campo debe ser asï¿½: 1,2,3
	 * @return
	 */
	public static ArrayList obtenerViasIngresoBloqueaDeudorBloqueaPaciente(Connection con,String codigosViasIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigosViasIngreso", codigosViasIngreso);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViasIngresoBloqueaDeudorBloqueaPaciente(con,campos);
	}
	
	/**
	 * Metodo encargado de devolver las vias de ingreso-tiposPaciente 
	 * en un HashMap
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerViasIngresoTipoPaciente(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViasIngresoTipoPaciente(con);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public static String obtenerViaIngresoCuenta(Connection con, String codigoCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViaIngresoCuenta(con,codigoCuenta);
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	public static PersonaBasica getPersonaBasicaSesion(HttpSession session)
	{
		if(session != null){
			PersonaBasica persona = (PersonaBasica) session.getAttribute("pacienteActivo");
			if(persona == null){
			    logger.error("El persona no esta cargado (null)");
			}
			return persona;
		}
		logger.error("El session esta nula");
		return null;
	}
	
	/**
	 * Método que verifica si un procedimiento tiene respuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean tieneProcedimientoRespuestaMultiple(Connection con,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().tieneProcedimientoRespuestaMultiple(con,numeroSolicitud);
	}
	
	/**
	 * Método que verifica si un procedimiento ya tiene finalizada
	 * la repsuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean estaFinalizadaRespuestaMultiple(Connection con,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().estaFinalizadaRespuestaMultiple(con,numeroSolicitud);
	}
	
	 /**
	  * 
	  * @param con
	  * @param codigoServicio
	  * @return
	  */
	public static boolean esServicioRespuestaMultiple(Connection con, String codigoServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esServicioRespuestaMultiple(con,codigoServicio);
	}

	/**
	 * Metodo para obtener los resultados de las diferentes respuestas de las solicitudes de procedimientos.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap obtenerResultadosRespuestasSolicitudesProcedimientos(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerResultadosRespuestasSolicitudesProcedimientos(con,numeroSolicitud);
	}
	
	/**
	 * Metodo que consulta los numeros de solicitud de procedimiento que tienen el
	 * estado de historia clï¿½nica especificado en el parï¿½metro, para el paciente de acuedo a la cuenta
	 * @param con
	 * @param cuenta
	 * @param estadoHistoriaClinica
	 * @return
	 */
	public static HashMap getSolProcedimientosEstadoHistoClinica (Connection con, int cuenta, int estadoHistoriaClinica)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getSolProcedimientosEstadoHistoClinica(con, cuenta, estadoHistoriaClinica);
	}
	
	/**
	 * Método que consulta los diangosticos de un paciente
	 * @param con
	 * @param campos
	 * @return
	 * acronimo + ConstantesBD.separadorSplit + tipoCie 
	 * si no encuentra nada retorna vacï¿½o
	 */
	public static String consultarDiagnosticosPaciente(Connection con,String idCuenta,int viaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarDiagnosticosPaciente(con,idCuenta,viaIngreso);
	}

	
	
		/**
	 * Método que consulta los usuarios por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerUsuarios(Connection con,int institucion, boolean estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUsuarios(con,institucion,estado);
	}
	
	/**
	 * Método que consulta los conceptos generales del mï¿½dulo de glosas
	 * @param con
	 * @param institucion
	 * @return
	 */
	
	public static HashMap obtenerConceptosGenerales (Connection con,int institucion, String tipoGlosa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConceptosGenerales(con,institucion, tipoGlosa);
	}
	
	/**
	 * Método que consulta los conceptos especificos del mï¿½dulo de glosas
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerConceptosEspecificos (Connection con,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConceptosEspecificos(con,institucion);
	}
	
	/**
	 * Método que consulta los conceptos glosa del mï¿½dulo de glosas
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerConceptosGlosa(Connection con,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConceptosGlosa(con,institucion);
	}
	
	/**
	 * Método para obtener los conceptos de glosas parametrizados
	 * @param con
	 * @param tipo
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosGlosa(Connection con,String tipo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConceptosGlosa(con,tipo,institucion); 
	}
	

	/**
	 * Método que consulta los conceptos ajustes del mï¿½dulo de cartera
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerConceptosAjuste(Connection con,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConceptosAjuste(con,institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitudTraslado
	 * @return
	 */
	public static int obtenerEstadoSolicitudTraslado(Connection con, String numeroSolicitudTraslado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEstadoSolicitudTraslado(con,numeroSolicitudTraslado);
	}

	/**
	 * 
	 * @param con
	 * @param login
	 * @return
	 */
	public static String obtenerCentrosCostoUsuario(Connection con, String login)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentrosCostoUsuario(con,login);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */	
	public static boolean esSolicitudProcedimientosFinalizada(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esSolicitudProcedimientosFinalizada(con, numeroSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param codigoEstado
	 * @return
	 */
	public static String obtenerDescripcionEstadoHC(Connection con, String codigoEstado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDescripcionEstadoHC(con,codigoEstado);
	}

	/**
	 * Metodo para consultar el detalle de un ajuste para la impresion
	 * @param con
	 * @param string
	 * @return
	 */
	public static HashMap consultarDetalleAjustesImpresion(Connection con, String codigoAjuste)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarDetalleAjustesImpresion(con,codigoAjuste);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static HashMap consultarEncabezadoAjuste(Connection con, String codigoAjuste)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarEncabezadoAjuste(con,codigoAjuste);
	}

	/**
	 * Metodo para Extraer el numero de embarazos de la funcionalidad informacion del parto.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerNrosEmbarazosInformacionParto(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNrosEmbarazosInformacionParto(con,codigoPersona);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList obtenerMedicosInstitucion(Connection con, String codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerMedicosInstitucion(con,codigoInstitucion);
	}
	
	/**
     * Metodo que consulta la informacion del paciente que se coloca en cada una
     * de las fichas de epidemiologï¿½a
     * @param con
     * @param codigoPaciente
     * @return
     */
    public static ResultSetDecorator consultarDatosPacienteFicha(Connection con, int codigoPaciente)
    {
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarDatosPacienteFicha(con,codigoPaciente);
    }

    /**
     * Metodo que retorn la fecha de egreso de una cuenta en formato de la aplicacion
     * @param cuenta
     */
	public static String obtenerFechaEgreso(String cuenta)
	{
		String respuesta="";
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			respuesta=getFechaEgreso(con, Integer.parseInt(cuenta));
		}
		catch (NumberFormatException e)
		{
			respuesta="";
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * Método que consulta la fecha de admisionde la cuenta de una cirugia
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public static String consultarFechaAdmisionCirugia(Connection con,String codigoCirugia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarFechaAdmisionCirugia(con, codigoCirugia);
	}

	/**
	 * Metodo para obtener de la informacion del parto el numero del embarazo de acuerdo al codigo
	 * de la cirugï¿½a y al paciente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoCirugia
	 * @return -1 si no existe de lo contrario retorna el nro del embarazo
	 */
	public static int obtenerNroEmbarazoCirugiaPaciente(Connection con, int codigoPaciente, String codigoCirugia) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNroEmbarazoCirugiaPaciente(con, codigoPaciente, codigoCirugia);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarTiposidentificacion(Connection con,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarTiposidentificacion(con, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarTiposidentificacion(int institucion)
	{
		Connection con=UtilidadBD.abrirConexion();
		HashMap mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarTiposidentificacion(con, institucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * M?todo que consulta las Ciudades
	 * @param con
	 * @return
	 */
	public static HashMap consultarCiudades(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarCiudades(con);
	}
	
	/**
	 * M?todo que consulta las Tipos de Sangre
	 * @param con
	 * @return
	 */
	
	public static HashMap consultarTiposSangre(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarTiposSangre(con);
	}

	/**
	 * M?todo que consulta las Estados Civiles
	 * @param con
	 * @return
	 */

	public static HashMap consultarEstadosCiviles(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarEstadosCiviles(con);
	}

	/**
	 * M?todo que consulta las Zonas Domiciliarias
	 * @param con
	 * @return
	 */

	public static HashMap consultarZonasDomicilio(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarZonasDomicilio(con);
	}

	/**
	 * M?todo que consulta las Ocupaciones
	 * @param con
	 * @return
	 */

	public static HashMap consultarOcupaciones(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarOcupaciones(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerNumeroMedicamentosSolicitud(Connection con,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNumeroMedicamentosSolicitud(con,numeroSolicitud);
	}
	
	
	/**
	 * Método para saber si el tipo y nï¿½mero de identificaciï¿½n del paciente
	 * existe en usuario capitados, si existe retorna el codigo sino retorna -1
	 * @param con
	 * @param tipoIdentificacion
	 * @param nroIdentificacion
	 * @return
	 */
	public static int getCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String nroIdentificacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCodigoUsuarioCapitado(con, tipoIdentificacion, nroIdentificacion);
	}
	
	/**
	 * Metodo que obtiene la informaciï¿½n del convenio capitado, de acuerdo
	 * al codigo del usuario capitado enviado por parï¿½metro
	 * @param con
	 * @param codigoUsuarioCapitado
	 */
	public static HashMap convenioUsuarioCapitado(Connection con, int codigoUsuarioCapitado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().convenioUsuarioCapitado(con, codigoUsuarioCapitado);
	}
	
	/**
	 * Método implementado para consultar tipo id, numero id y nombre de las personas
	 * que tienen el mismo numero id
	 * @param con
	 * @param numeroIdentificacion
	 * @return
	 */
	public static HashMap personasConMismoNumeroId(Connection con,String numeroIdentificacion,String tipoIdentificacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().personasConMismoNumeroId(con, numeroIdentificacion,tipoIdentificacion);
	}
	
	/**
	 * Método que marca como atendido los registros de pacientes para triage
	 * del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static int actualizarPacienteParaTriageVencido(Connection con,String codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarPacienteParaTriageVencido(con, codigoPaciente);
	}
	
	/**
	 * Método que condulta el codigo UPGD del centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static String getCodigoUPGDCentroAtencion(Connection con,int centroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCodigoUPGDCentroAtencion(con, centroAtencion);
	}

	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static String getLoginUsuarioSolicitaOrdenAmbulatoria(Connection con,String consecutivoOrden, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getLoginUsuarioSolicitaOrdenAmbulatoria(con, consecutivoOrden, institucion);
	}	
	
	
	/**
	 * Método implementado para actualizar los datos de epidemiologï¿½a de un diagnostico
	 * @param con
	 * @param diagnostico: es de la forma acronimo @@@@@ tipoCie @@@@@ nombre 
	 * @param valorFicha: resultado de la ficha del diagnostico
	 * @param numeroSolicitud
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	public static int actualizarDatosEpidemiologia(
		Connection con,
		String diagnostico,
		String valorFicha,
		String numeroSolicitud,
		PersonaBasica paciente,
		UsuarioBasico usuario)
	{
		String[] vector = new String[0];
		int resp = 1;
		
		//Se verifica valor ficha para el diagnostico principal
		if(valorFicha!=null&&!valorFicha.equals(""))
		{
			if(valorFicha.equalsIgnoreCase(ConstantesBD.respuestaLogFichaEpidemiologia))
			{
				vector = diagnostico.split(ConstantesBD.separadorSplit);
				resp = UtilidadFichas.insertarLogFichasReportadas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona(), vector[0],Integer.parseInt(vector[1]), Integer.parseInt(numeroSolicitud));
			}
			else
			{
				vector = valorFicha.split(ConstantesBD.separadorSplit);
				resp = UtilidadFichas.activarFichaPorCodigo(con, Integer.parseInt(vector[0]), Integer.parseInt(vector[1]));
			}
				
		}
		
		return resp;
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public static HashMap consultarInformacionServicio(Connection con, String codigoServicio, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarInformacionServicio(con, codigoServicio, institucion);
	}

	/**
	 * Metodo para consultar los servicios activos relacionados a una enfermedad epidemiologica.
	 * @param con
	 * @param codigoEnfermedadEpidemiologia
	 * @param institucion
	 * @return
	 */
	public static ArrayList consultarServiciosEnfermedadEpidemiologia(Connection con, String codigoEnfermedad, String institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarServiciosEnfermedadEpidemiologia(con,codigoEnfermedad,institucion);
	}

	/**
	 * Metodo que retorna el codigo la descripcion del sexo
	 * @param con
	 * @param sexo
	 */
	public static String getDescripcionSexo(Connection con, int sexo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getDescripcionSexo(con,sexo);
	}

	/**
	 * 
	 * Metodo que retorna el numero de solicitudes que tiene una cuenta, ya sea de.
	 *  pyp  --> parametro true, 
	 *  !pyp --> parametro false,
	 *  todas --> parametro ""  
	 * @param con
	 * @param cuenta
	 * @param busquedaPYP 
	 * @return
	 */
	public static int obtenerNumeroSolicitudesPypXCuenta(Connection con, String cuenta, String busquedaPYP)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNumeroSolicitudesPypXCuenta(con,cuenta,busquedaPYP);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerCajasCajero(String loginUsuario, int codigoInstitucion, int codigoTipoCaja, int codigoCentroAtencion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCajasCajero(con, loginUsuario, codigoInstitucion, codigoTipoCaja, codigoCentroAtencion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}

	/**
	 * Metodo que retorna el tipo de caja. dada la llave principal.
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public static int obtenerTipoCaja(int consecutivoCaja)
	{
		Connection con=UtilidadBD.abrirConexion();
		int resultado= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoCaja(con,consecutivoCaja);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerCajas(int codigoInstitucion, int codigoTipoCaja)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCajas(con, codigoInstitucion, codigoTipoCaja);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	
	/**
	 * 
	 * @param numeroDocumento
	 * @return
	 */
	public static HashMap consultarDatosGeneralesPaciente(String numeroDocumento)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarDatosGeneralesPaciente(con, numeroDocumento);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param fecha
	 * @param institucion
	 * @return
	 */
	public static InfoDatosString obtenerTipoRegimenSolicitudUsuarioCapitado(String codigoPersona, String fecha, int institucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoDatosString info=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoRegimenSolicitudUsuarioCapitado(con, codigoPersona, fecha, institucion);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param fecha
	 * @param institucion
	 * @return
	 */
	public static InfoDatosString obtenerTipoRegimenSolicitudUsuarioCapitado(Connection con, String codigoPersona, String fecha, int institucion)
	{
		InfoDatosString info=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoRegimenSolicitudUsuarioCapitado(con, codigoPersona, fecha, institucion);
		return info;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap obtenerCentrosAtencion(int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentrosAtencion(con, codigoInstitucion);
		
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * 
	 * @param codProvedor
	 * @return
	 */
	public static HashMap obtenerProveedores(int codProvedor)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerProveedores(con, codProvedor);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * Metodo usado para consultar los centros de atencion
	 * segun el parametro inactivos 
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap obtenerCentrosAtencionInactivos(int codigoInstitucion, boolean inactivos)
	{
		Connection con = UtilidadBD.abrirConexion();
		HashMap mapa = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentrosAtencionInactivos(con, codigoInstitucion, inactivos);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param cuentaContable
	 * @param descripcion
	 * @return
	 */
	public static HashMap obtenerCuentasContables(int codigoInstitucion, String cuentaContable, String descripcion,String anioVigencia, String naturCuenta)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCuentasContables(con, codigoInstitucion, cuentaContable, descripcion ,anioVigencia, naturCuenta);
		UtilidadBD.closeConnection(con);
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerNombreArticulo(Connection con, int codigoArticulo)
	{
		if(codigoArticulo>0)
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreArticulo(con, codigoArticulo);
		return "";
		
	}

	/**
	 * Metodo que retorna el acronimo de la unidad de medida de un registro de unidosis_x_articulo, dada su llave primaria.
	 * @param con
	 * @param unidosis
	 * @return
	 */
	public static String obtenerUnidadMedidadUnidosisArticulo(Connection con, String unidosis)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUnidadMedidadUnidosisArticulo(con,unidosis);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarTerceros(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarTerceros(con,institucion);
	}

	public static void imprimirObjeto(Object objeto)
	{
		Class objClass=objeto.getClass();
		for(Method campo:objClass.getMethods())
		{
			if(campo.getName().substring(0,3).equals("get"))
				try {
					logger.info(campo.getName()+": "+campo.invoke(objeto, null));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 
	 * @param mapa
	 * @param archivoClass
	 */
	public static void imprimirMapa(HashMap mapa)
	{
		/*logger.info("\n\n\n\n\n\n\n\n\n\n");
		logger.info("*******************************************************************************************");
		logger.info("*******************************IMPRESION MAPA**********************************************");
		
		if(mapa!=null && mapa.entrySet()!=null){
			
			Iterator llaves=mapa.entrySet().iterator();
			
			while(llaves.hasNext())
			{
				String contenido=llaves.next()+"";
				logger.info("\t\t"+contenido);
			}
		}
		
		logger.info("*******************************************************************************************");
		logger.info("\n\n\n\n\n\n\n\n\n\n");*/
	}

	
	public static void imprimirArrayList(ArrayList array)
	{
		/*//logger.info("\n\n\n\n\n\n\n\n\n\n");
		//logger.info("*******************************************************************************************");
		//logger.info("*******************************IMPRESION ARRAYLIST**********************************************");
		Iterator llaves=array.iterator();
		while(llaves.hasNext())
		{
			String contenido=llaves.next()+"";
			//logger.info("\t\t"+contenido);
		}
		
		//logger.info("*******************************************************************************************");
		//logger.info("\n\n\n\n\n\n\n\n\n\n");
*/	}
	
	
	public static void imprimirArreglo(String [] arreglo)
	{
		/*//logger.info("\n\n\n\n\n\n\n\n\n\n");
		//logger.info("*******************************************************************************************");
		//logger.info("*******************************IMPRESION ARREGLO**********************************************");
		int numReg = arreglo.length;
		for (int i=0;i<numReg;i++)
		{
			String contenido=arreglo[i];
			//logger.info("\t\t"+contenido);
		}
		
		//logger.info("*******************************************************************************************");
		//logger.info("\n\n\n\n\n\n\n\n\n\n");
*/	}
	/**
	 * Metodo que retorna el tipo de caja. dada la llave principal.
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public static int obtenerTipoCaja(Connection con, int consecutivoCaja)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoCaja(con,consecutivoCaja);
	}

	/**
	 * Metodo que retorna si una Forma Farmaceutica es utilizada o no
	 * @param consecutivo
	 * @return
	 */
	public static boolean esFormaFarmaceuticaUsada(String consecutivo)
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esFormaFarmaceuticaUsada(con, consecutivo);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}	
	
	
	
	public static boolean esClaseInventarioUsada(int consecutivo)
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esClaseInventarioUsada(con, consecutivo);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}	

	public static boolean  esGrupoInventarioUsada( int consecutivo, int clase)
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esGrupoInventarioUsada(con, consecutivo, clase);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}	
	
	public static boolean  esSubgrupoInventarioUsada(  int consecutivo, int subgrupo , int grupo, int clase)
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esSubgrupoInventarioUsada(con, consecutivo, subgrupo, grupo, clase);
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		return resp;
	}

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerTercerosInstitucionActivos(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTercerosInstitucionActivos(con,institucion);
	}	
	
	
	/**
	 * Metodo encargado de consultar los terceros que se encuentren activos
	 * y no se encuentren relacionados a alguna empresa.
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return
	 * ArrayList<HashMap<String, Object>>
	 * -----------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * -----------------------------------
	 * codigo,numeroid,descripcion
	 */
	public static  ArrayList obtenerTercerosSinEmpresaInstitucionActivos(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTercerosSinEmpresaInstitucionActivos(con, institucion);
	}
	
	/**
	 * Método que consulta las unidades funcionales por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarUnidadesFuncionales(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",institucion+"");
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarUnidadesFuncionales(con, campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param codEstadoCuenta
	 * @return
	 */
	public static int obtenerCuentaDadoPacienteEstado(Connection con, int codPaciente, int codEstadoCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCuentaDadoPacienteEstado(con, codPaciente, codEstadoCuenta);
	}
	
	/**
	 * Método que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerTipoRegimenConvenio(Connection con,String codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoRegimenConvenio(con, codigoConvenio);
	}

	/**
	 * Método que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerCodigoTipoRegimenConvenio(Connection con,String codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoTipoRegimenConvenio(con, codigoConvenio);
	}
	
	/**
	 * Metodo que retorna los contratos de un convenio.
	 * dependiendo del valor del atributo todos(true--> todos los contratos del convenio,false ---> contratos vigentes)
	 * @param con
	 * @param codigoConvenio
	 * @param todos
	 * @param validarNumeroContratos: verifica que el numero del contrato no sea NULL ni cadena vacion
	 * @param pendientesFinalizar valida que la fecha de finalizacion sea mayor igual a la fecha actual, sin importar la fecha de inicio, este parametro solo tiene efecto si se solicitan todos los contratos
	 * @return
	 */
	public static ArrayList obtenerContratos(Connection con, int codigoConvenio, boolean todos,boolean validarNumeroContratos,boolean pendientesFinalizar) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerContratos(con,codigoConvenio,todos,validarNumeroContratos,pendientesFinalizar);
	}
	
	
	/**
	 * Metodo que retorna los contratos de un convenio.
	 * dependiendo del valor del atributo todos(true--> todos los contratos del convenio,false ---> contratos vigentes)
	 * @param con
	 * @param codigoConvenio
	 * @param todos
	 * @param validarNumeroContratos: verifica que el numero del contrato no sea NULL ni cadena vacion
	 * @return
	 */
	public static ArrayList obtenerContratos(Connection con, int codigoConvenio, boolean todos,boolean validarNumeroContratos) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerContratos(con,codigoConvenio,todos,validarNumeroContratos,false);
	}
	
	
	/**
	 * Metodo que retorna los contratos de un convenio.
	 * dependiendo del valor del atributo todos(true--> todos los contratos del convenio,false ---> contratos vigentes)
	 * @param con
	 * @param codigoConvenio
	 * @param todos
	 * @param validarNumeroContratos: verifica que el numero del contrato no sea NULL ni cadena vacï¿½a
	 * @param pendientesFinalizar valida que la fecha de finalizacion sea mayor igual a la fecha actual, sin importar la fecha de inicio, este parametro solo tiene efecto si se solicitan todos los contratos
	 * @return
	 */
	public static ArrayList obtenerContratos( int codigoConvenio, boolean todos,boolean validarNumeroContratos,boolean pendientesFinalizar) 
	{
		ArrayList resultado=new ArrayList();
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerContratos(con,codigoConvenio,todos,validarNumeroContratos,pendientesFinalizar);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Metodo que retorna los contratos de un convenio.
	 * dependiendo del valor del atributo todos(true--> todos los contratos del convenio,false ---> contratos vigentes)
	 * @param con
	 * @param codigoConvenio
	 * @param todos
	 * @param validarNumeroContratos: verifica que el numero del contrato no sea NULL ni cadena vacï¿½a
	 * @return
	 */
	public static ArrayList obtenerContratos( int codigoConvenio, boolean todos,boolean validarNumeroContratos) 
	{
		ArrayList resultado=new ArrayList();
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerContratos(con,codigoConvenio,todos,validarNumeroContratos,false);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Metodo que retorna un mapa que contiene tres atributos:
	 * 			ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoNatural
	 * 			ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTecnologico
	 * 			ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTerrorista
	 * 
	 * y cada uno de estos atributos contiene otro mapa con el codigo y la descripcion den evento.
	 * @return
	 */
	public static HashMap<String,Object> obtenerNaturalezasEventosCatastroficos(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNaturalezasEventosCatastroficos(con);
	}

	
	/**
	 * Metodo que elimina un objeto de una mapa, se debe enviar la referencia del mapa original y del mapa donde se almacenan los objetos eliminados.
	 * se almacenan los objetos almacenados, solo cuando son tipo base de datos.
	 * @param mapa
	 * @param mapaEliminados
	 * @param posEliminar
	 * @param indices
	 * @param valorTipoRegistrosBD ---> indice que contiene el numero de registro de los mapas 
	 * @param indiceTipoRegistro ----> indice que indica si el registro es de tipo memoria o bd
	 * @param indiceNumeroRegistros   ---> valor de atributo tipo registro cuando el registro es de la bd..
	 * @author Jorge Armando Osorio Velasquez.
	 * @param todosRegistros ----> parametro que indica si se debe mantener el historico de todos los registros, o solo los que son tipo BD.
	 */
	public static void eliminarRegistroMapaGenerico(HashMap mapa, HashMap mapaEliminados, int posEliminar, String[] indices, String indiceNumeroRegistros, String indiceTipoRegistro, String valorTipoRegistrosBD, boolean todosRegistros)
	{
		int ultimaPosMapa=(Integer.parseInt(mapa.get(indiceNumeroRegistros)+"")-1);

		try
		{
			int numRegMapEliminados=Integer.parseInt(mapaEliminados.get(indiceNumeroRegistros)+"");
			
			//cargamos el mapa de eliminados solo para registro de la bd.
			if(todosRegistros||(!todosRegistros&&(mapa.get(indiceTipoRegistro+posEliminar)+"").trim().equalsIgnoreCase(valorTipoRegistrosBD)))
			{
				for(int i=0;i<indices.length;i++)
				{
				
					mapaEliminados.put(indices[i]+""+numRegMapEliminados, mapa.get(indices[i]+""+posEliminar));
				}
				mapaEliminados.put(indiceNumeroRegistros, (numRegMapEliminados+1));
			}
		}
		catch(Exception e)
		{
			logger.info("NO TIENE MAPA DE REGISTROS ELIMINADOS");
		}

		//acomodar los registros del mapa en su nueva posicion
		for(int i=posEliminar;i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				mapa.put(indices[j]+""+i,mapa.get(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			mapa.remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		mapa.put(indiceNumeroRegistros,ultimaPosMapa);
	
	}

	/**
	 * Metodo generico que permite crear un nuevo registro en un mapa.
	 * Todas las llaves del mapa quedan con valor "".
	 * @param mapa  --->mapa en el que se crear el registro
	 * @param indices  ---> vector de indices que contiene el mapa.
	 * @param indiceNumeroRegistros   ---->indice que contiene el numero de registros del mapa
	 * @param indiceTipoRegistro  ---> identificador para los registros que son de memoria.
	 * @param valorTipoRegistrosMemoria   --->valor de atributo para identificar los registros que son de memoria
	 */
	public static void nuevoRegistroMapaGenerico(HashMap mapa, String[] indices, String indiceNumeroRegistros, String indiceTipoRegistro, String valorTipoRegistrosMemoria)
	{
		int tamanio=Integer.parseInt(mapa.get(indiceNumeroRegistros)+"");
		for(int i=0;i<indices.length;i++)
		{
			mapa.put(indices[i]+""+tamanio, "");
		}
		mapa.put(indiceTipoRegistro+tamanio, valorTipoRegistrosMemoria);
		mapa.put(indiceNumeroRegistros, (tamanio+1));
	}
	
	/**
	 * Metodo para generar los logs sencillos dinamicamentem, recibe 2 mapas con la informacion antes y despues de la modificacion.
	 * en caso de ser un log eliminacion, solo es necesario enviar el mapa de registros eliminados. 
	 * @param mapaModificado
	 * @param mapaOriginal
	 * @param usuario
	 * @param isEliminacion
	 * @param posicionRegModificado
	 * @param constanteTipoArchivo
	 * @param indices 
	 */
	public static void generarLogGenerico(HashMap mapaModificado, HashMap mapaOriginal, String usuario, boolean isEliminacion, int posicionRegModificado, int constanteTipoArchivo, String[] indices)
	{
		String log = "";
		int tipoLog=0;
		logger.info("\n\n************ entre al generador de logs indices ==> "+indices+"  mapa modificado==> "+mapaModificado);
		logger.info("valor del log "+constanteTipoArchivo);
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+mapaModificado.get(indices[i]+""+posicionRegModificado)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +mapaOriginal.get(indices[i]+"0") + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +mapaModificado.get(indices[i]+""+posicionRegModificado) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(constanteTipoArchivo,log,tipoLog,usuario);
	}
	
	
	
	//*********************Propuesta de nueva funcion de creacion de lOGS	
	/**
	 * La funcionalidad es igual o semejante a la funcion analoga o referente (generarLogGenerico), la unica diferencia es que no se da por hecho que los registros originales estan en la posicion 0 del mapa
	 */
	public static void generarLogGenerico(HashMap mapaModificado, int posicionRegModificado, HashMap mapaOriginal, String usuario, boolean isEliminacion, int posicionRegOriginal, int constanteTipoArchivo, String[] indices)
	{
		String log = "";
		int tipoLog=0;
		logger.info("\n\n************ entre al generador de logs indices ==> "+indices+"  mapa modificado==> "+mapaModificado);
		logger.info("valor del log "+constanteTipoArchivo);
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+mapaModificado.get(indices[i]+""+posicionRegModificado)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				// unico cambio presente entre las dos funcionalidades generarLogGenerico
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +mapaOriginal.get(indices[i]+""+posicionRegOriginal) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +mapaModificado.get(indices[i]+""+posicionRegModificado) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(constanteTipoArchivo,log,tipoLog,usuario);
	}
	
	//********************
	
	
	
	
	
	
	
	/**
	 * 
	 * Metodo que retorna un mapa con la informacion de referencia, en caso de no tener informacion de referencia.
	 * el mapa es retornado con el atributo numRegistros=0
	 * indices {profesionalRefiere,ciudadRefiere,fechaRefiere,institucionReferida,ciudadReferida,fechaReferida,numeroReferencia,tipoReferencia}
	 * @param con
	 * @param codigoPaciente
	 * @param idIngreso
	 * @return
	 */
	public static HashMap<String,Object> obtenerInformacionReferenciaTramite(Connection con,int codigoPaciente,int idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInformacionReferenciaTramite(con,codigoPaciente,idIngreso);
	}

	/**
	 * Metodo que recotoran un vector con las naturalezas paciente
	 * @param con
	 * @param viaIngreso 
	 * @param convenio 
	 * @param tipoRegimen 
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerNaturalezasPaciente(Connection con, String tipoRegimen, int convenio, int viaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNaturalezasPaciente(con,tipoRegimen,convenio,viaIngreso);
	}
	
	/**
	 * Metodo que retorna un vector con las naturalezas paciente
	 * @param con
	 * @param viaIngreso 
	 * @param convenio 
	 * @param tipoRegimen 
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
			Connection con, String tipoRegimen, int convenio, int viaIngreso,
			String codigoTipoAfiliado, int codigoEstratoSocial, String fechaReferencia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().
		obtenerNaturalezasPacienteXTipoAfiliadoEstrato(con,tipoRegimen,convenio,viaIngreso,codigoTipoAfiliado,
				codigoEstratoSocial, fechaReferencia);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String getNombreEstadoCartera(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreEstadoCartera(con, codigo);
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerListadoCoberturasInstitucion(con, institucion);
	}

	/**
	 * Metodo que retorna los centros de costo de una instituciones
	 * indices: codigo_ |nomcentrocosto_ |codigotipoarea_ |nombretipoarea_ |activo_ |identificador_ |manejacamas_ |unidadfuncional_ |descunidadfuncional_ |codcentroatencion_ |nomcentroatencion_
	 * @param con
	 * @param institucion
	 * @param tiposArea ---> cadena con los codigos de los tipos de area que se desea consultar separados por el separadorSplit
	 * @param todos  --->indica si se consultan todos los centros de costo o solo los activos.
	 * @param filtrar_externos --> indica true para que filtreo los externos false para que funcione normal
	 * @return
	 */
	public static HashMap obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos,int centroAtencion, boolean filtro_externos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentrosCosto(con,institucion,tiposArea,todos,centroAtencion,filtro_externos);
	}
	
	/**
	 * Metodo que retorna los centros de costo  con consignacion de una instituciones
	 * indices: codigo_ |nomcentrocosto_ |codigotipoarea_ |nombretipoarea_ |activo_ |identificador_ |manejacamas_ |unidadfuncional_ |descunidadfuncional_ |codcentroatencion_ |nomcentroatencion_
	 * @param con
	 * @param institucion
	 * @param tiposArea ---> cadena con los codigos de los tipos de area que se desea consultar separados por el separadorSplit
	 * @param todos  --->indica si se consultan todos los centros de costo o solo los activos.
	 * @param filtrar_externos --> indica true para que filtreo los externos false para que funcione normal
	 * @return
	 */
	public static HashMap obtenerCentrosCostoTipoConsignacion(Connection con, int institucion, String tiposArea, boolean todos,int centroAtencion, boolean filtro_externos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentrosCostoTipoConsignacion(con,institucion,tiposArea,todos,centroAtencion,filtro_externos);
	}

	
	public static HashMap obtenerEmpresasInstitucion(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEmpresasInstitucion(con,institucion);
	}
	
	/**
	 * Metodo encargado de consultar las empresas parametrizadas en le sistema, con un deudor
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return ArrayList<HashMap<String, Object>>
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con, String institucion)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEmpresas(con, institucion);
	}
	
	/**
	 * Metodo que retorna los centros de costo de una instituciones
	 * indices: codigo_ |nomcentrocosto_ |codigotipoarea_ |nombretipoarea_ |activo_ |identificador_ |manejacamas_ |unidadfuncional_ |descunidadfuncional_ |codcentroatencion_ |nomcentroatencion_
	 * @param con
	 * @param institucion
	 * @param tiposArea ---> cadena con los codigos de los tipos de area que se desea consultar separados por el separadorSplit
	 * @param todos  --->indica si se consultan todos los centros de costo o solo los activos.
	 * @param filtrar_externos --> indica true para que filtreo los externos false para que funcione normal
	 * @return
	 */
	public static HashMap obtenerCentrosCosto( int institucion, String tiposArea, boolean todos,int centroAtencion, boolean filtrar_externos)
	{
		
		Connection con=UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentrosCosto(con,institucion,tiposArea,todos,centroAtencion,filtrar_externos);
		UtilidadBD.closeConnection(con);
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerTiposComplejidad(Connection con) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposComplejidad(con);
	}

	/**
	 * 
	 * @param institucion 
	 * @param identificadorTablaPacientes
	 * @return
	 */
	public static String consultarNombreTablaInterfaz(String identificadorTabla, int institucion) 
	{
		String nombreTabla="";
		Connection con=UtilidadBD.abrirConexion();
		nombreTabla=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarNombreTablaInterfaz(con,identificadorTabla,institucion);
		UtilidadBD.closeConnection(con);
		return nombreTabla;

	}

	/**
	 * Metodo para insertar un abono a partir del DtoInterfazAbonos
	 * @param abonos
	 */
	public static boolean insertarAbono(DtoInterfazAbonos abono) 
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().insertarAbono(con,abono);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Utilidad que retorna un vector de enteros con los codigos de las instituciones.
	 * @return
	 */
	public static Vector<String> obetenerCodigosInstituciones() 
	{
		Connection con=UtilidadBD.abrirConexion();
		Vector<String> instituciones=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obetenerCodigosInstituciones(con);
		UtilidadBD.closeConnection(con);
		return instituciones;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static InfoDatosString obtenerInstitucionSirCentroAtencion(Connection con,int codigoCentroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInstitucionSirCentroAtencion(con,codigoCentroAtencion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap obtenerInstituciones(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInstituciones(con);
	}
	
	
	/**
	 * Método implementado parta obtener los paises
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPaises(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPaises(con);
	}
	
	
	/**
	 * Método implementado parta obtener los paises
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPaises()
	{
	
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<HashMap<String, Object>> resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPaises(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	
	
	/**
	 * Método que consulta las LOCALIDADES 
	 * Los parï¿½metros que se envï¿½an son opcionales, sino se desea filtrar simplemente se manda cadena vacï¿½a
	 * @param con
	 * @param codigoPais
	 * @param codigoDepartamento
	 * @param codigoCiudad
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerLocalidades(Connection con,String codigoPais,String codigoDepartamento,String codigoCiudad)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPais", codigoPais);
		campos.put("codigoDepartamento", codigoDepartamento);
		campos.put("codigoCiudad", codigoCiudad);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerLocalidades(con, campos);
	}
	
	
	/**
	 * Mï¿½todo que consulta la localidad de un barrio
	 * @param con
	 * @param codigoPais
	 * @param codigoDepartamento
	 * @param codigoCiudad
	 * @param codigoBarrio
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerLocalidadDeBarrio(Connection con,String codigoPais,String codigoDepartamento,String codigoCiudad,String codigoBarrio)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPais", codigoPais);
		campos.put("codigoDepartamento", codigoDepartamento);
		campos.put("codigoCiudad", codigoCiudad);
		campos.put("codigoBarrio", codigoBarrio);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerLocalidadDeBarrio(con, campos);
	}
	
	
	/**
	 * Método que consulta los sexos parametrizados en el sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerSexos(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSexos(con);
	}

	/**
	 * Metodo que retorna el codigo del responsable de un ingreso, dantod el codigo de ingreso y el codigo del convenio.
	 * en caso de encontrar varias coinsidencias, se retorna el primero que encuentre y no este facturado.
	 * @param con
	 * @param codigoCuenta
	 * @param codigoConvenio
	 * @return
	 */
	public static int getCodigoResponsable(Connection con, int idIngreso, int codigoConvenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCodigoResponsable(con,idIngreso,codigoConvenio);
	}

	/**
	 * Metodo para convertiru un string, en entero, en caso de no poder hacer la conversion se retorn codigonuncavalido
	 * se maneja un boolean para indicar si la cadena cumple con UtilidadTexto.isEmpty se toma como 0. 
	 * @param valor
	 * @return
	 */
	public static int convertirAEntero(String valor,boolean manejaCadenaVacia0) 
	{
		try
		{
			if(manejaCadenaVacia0)
				if(UtilidadTexto.isEmpty(valor))
					return 0;
			//toca hacer la conversion a double y luego tomar la parte int, por algunos registros que se manejan como int.0
			//si la cadena tiene formato ingles, se deben eliminar las comas(,).
			if(valor.indexOf(",")>0)
				valor=valor.replace(",", "");
			return (int)Double.parseDouble(valor);
		}
		catch(Exception e)
		{
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Metodo para convertiru un string, en entero, en caso de no poder hacer la conversion se retorn codigonuncavalido
	 * @param valor
	 * @return
	 */
	public static int convertirAEntero(String valor) 
	{
		if(valor==null)
			return ConstantesBD.codigoNuncaValido;
		return convertirAEntero(valor,false);
	}

	/**
	 * Metodo para obtener la prioridad de un responsable.
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static int obtenerPrioridadResponsabe(Connection con, String subCuenta) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPrioridadResponsabe(con,subCuenta);
	}
	/**
	 * Metodo para obtener la prioridad de un responsable.
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static int obtenerPrioridadResponsabe(String subCuenta) throws IPSException
	{
		int resultado;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPrioridadResponsabe(con,subCuenta);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public static boolean convenioManejaComplejidad(int codigoConvenio) 
	{
		boolean resultado;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().convenioManejaComplejidad(con,codigoConvenio);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public static boolean convenioManejaPyp(int codigoConvenio) 
	{
		boolean resultado;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().convenioManejaPyp(con,codigoConvenio);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Metodo para obtener el tipo de complejidad de una cuenta, si la cuenta no tiene tipo complejidad retorna ConstantesBD.codigonNuncaValido.
	 * @param cuenta
	 * @return
	 */
	public static InfoDatosInt obtenerTipoComplejidadCuenta(Connection con,int cuenta) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoComplejidadCuenta(con,cuenta);
	}
	
	
	/**
	 * Adiciï¿½n Jhony Alexander Duque
	 * Método usado para obtener los grupos de servicios y con la posibilidad de ser filtrado
	 * con cualquiera de los campos de la tabla
	 * @param Connection connection
	 * @param HashMap grupoServicios
	 * @return ArrayList<HashMap> 
	 */
	public static ArrayList<HashMap<String, Object>> obtenerGrupoServicios (Connection connection, HashMap grupoServicios)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerGrupoServicios(connection, grupoServicios);
	}
	
	
	/**
	 * Adiciï¿½n Vï¿½ctor Hugo Gï¿½mez L.
	 * Método usado para obtener los grupos de servicios y con la posibilidad de ser filtrado
	 * con cualquiera de los campos de la tabla
	 * @param Connection connection
	 * @param HashMap grupoServicios
	 * @return ArrayList<HashMap> 
	 */
	public static ArrayList<HashMap<String, Object>> obtenerGrupoServicios (Connection connection, int institucion, boolean activo)
	{
		HashMap grupoServicios = new HashMap();
		grupoServicios.put("institucion", institucion);
		if(activo)
			grupoServicios.put("activo", activo);
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerGrupoServicios(connection, grupoServicios);
	}
	
	
	/**
	 * Metodo que obtiene la via de ingreso de una cuenta, y las vias de las cuentas asociadas.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerViasIngresoCuenta(Connection con,int cuenta)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViasIngresoCuenta(con, cuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerTiposPacienteCuenta(Connection con,int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposPacienteCuenta(con, cuenta);
	}

	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static String obtenerPorcentajeAutorizadoVerficacionDerechos(Connection con, String subCuenta) 
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPorcentajeAutorizadoVerficacionDerechos(con, subCuenta);
	}

	/**
	 * Metodo para convertiru un string, en entero, en caso de no poder hacer la conversion se retorn codigonuncavalido
	 * se maneja un boolean para indicar si la cadena cumple con UtilidadTexto.isEmpty se toma como 0. 
	 * @param valor
	 * @return
	 */
	public static double convertirADouble(String valor, boolean manejaCadenaVacia0) 
	{
		try
		{
			if(manejaCadenaVacia0)
				if(UtilidadTexto.isEmpty(valor))
					return 0.0;
			//si la cadena tiene formato ingles, se deben eliminar las comas(,).
			if(valor.indexOf(",")>0)
				valor=valor.replace(",", "");
			return Double.parseDouble(valor);
		}
		catch(Exception e)
		{
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	
	/**
	 * Metodo para convertiru un string, en float, en caso de no poder hacer la conversion se retorn codigonuncavalido
	 * se maneja un boolean para indicar si la cadena cumple con UtilidadTexto.isEmpty se toma como 0. 
	 * @param valor
	 * @return
	 */
	public static float convertirAFloat(String valor, boolean manejaCadenaVacia0) 
	{
		try
		{
			if(manejaCadenaVacia0)
				if(UtilidadTexto.isEmpty(valor))
					return 0;
			return Float.parseFloat(valor);
		}
		catch(Exception e)
		{
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Metodo para convertiru un string, en float, en caso de no poder hacer la conversion se retorn codigonuncavalido
	 * se maneja un boolean para indicar si la cadena cumple con UtilidadTexto.isEmpty se toma como 0. 
	 * @param valor
	 * @return
	 */
	public static long convertirALong(String valor, boolean manejaCadenaVacia0) 
	{
		try
		{
			if(manejaCadenaVacia0)
				if(UtilidadTexto.isEmpty(valor))
					return 0;
			return new BigDecimal(valor).longValue();
		}
		catch(Exception e)
		{
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * 
	 * @param valor
	 * @return
	 */
	public static double convertirADouble(String valor) 
	{
		return convertirADouble(valor,false);
	}
	
	/**
	 * 
	 * @param valor
	 * @return
	 */
	public static float convertirAFloat(String valor) 
	{
		return convertirAFloat(valor,false);
	}
	
	/**
	 * 
	 * @param valor
	 * @return
	 */
	public static long convertirALong(String valor) 
	{
		return convertirALong(valor,false);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public static HashMap obtenerCoberturaAccidenteTransitos(Connection con, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCoberturaAccidenteTransitos(con,institucion);
	}

	/**
	 * Metodo que retorna la fecha de un accidente de transito, dado su ingreso,
	 * @param con
	 * @param codigoIngreso
	 * @param formatoAplicacion 
	 * @return
	 */
	public static String obtenerFechaAccidenteTransito(Connection con, int codigoIngreso, boolean formatoAplicacion) 
	{
		String fecha=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaAccidenteTransito(con,codigoIngreso);
		if(formatoAplicacion)
			return UtilidadFecha.conversionFormatoFechaABD(fecha);
			//return UtilidadFecha.conversionFormatoFechaAAp(fecha);
		return fecha;
	}

	/**
	 * 
	 * Metodo que retorna el salario minimo vigente para una fecha.
	 * @param con
	 * @param fecha
	 * @return
	 */
	public static double obtenerSalarioMinimoVigente(Connection con, String fecha) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSalarioMinimoVigente(con,fecha);
	}
	
	
	/**
	 * Metodo que retorna un ResultadoBoolean indicando de si el monto,es porcentaje o valor, en la descripcion contine el valor del campo.
	 * @param con
	 * @param CodigoMonto
	 * @return
	 * @deprecated
	 */
	public static ResultadoBoolean esPorcentajeMonto(Connection con, int CodigoMonto)throws Exception
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esPorcentajeMonto(con,CodigoMonto);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static double obtenerTotalCargadoResponsable(Connection con,String subCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTotalCargadoResponsable(con,subCuenta);	
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerNombreTipoComplejidad (Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoComplejidad(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param subcuenta
	 * @return
	 */
	public static String obtenerNumeroCarnet(Connection con, double subcuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNumeroCarnet(con, subcuenta);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoFuncionalidad
	 * @return
	 */
	public static String obtenerPathFuncionalidad(Connection con, int codigoFuncionalidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPathFuncionalidad(con, codigoFuncionalidad);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFuncionalidad
	 * @return
	 */
	public static String obtenerPathFuncionalidad(int codigoFuncionalidad)
	{
		Connection con= UtilidadBD.abrirConexion();
		String link= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerPathFuncionalidad(con, codigoFuncionalidad);
		UtilidadBD.closeConnection(con);
		return link;
	}	


	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param tipoSolicitud
	 * @return
	 */
	public static boolean esSolicitudTipo(Connection con, String numeroSolicitud, int tipoSolicitud) 
	{
		int tipoSol=convertirAEntero(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoSolicitud(con, numeroSolicitud));
		return (tipoSol==tipoSolicitud);
	}

	/**
	 * 
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap obtenerEsquemasTarifarios(boolean esInventarios, int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEsquemasTarifarios(con, esInventarios, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	
	/**
	 * Metodo que devuelve un arraylist con los valores de esquemas tarifarios
	 * @param con
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosInArray(boolean esInventarios, int codigoInstitucion,int tarifarioOficial) 
	{
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<HashMap<String, Object>> mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEsquemasTarifariosInArray(con, esInventarios, codigoInstitucion, tarifarioOficial);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * Método que consulta los dias de la semana
	 * @param con	
	 * @return
	 */
	public static HashMap obtenerDiasSemana(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDiasSemana(con);				
	}
	
	/**
	 * Método que consulta el nombre del estado de liquidacion
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static String getNombreEstadoLiquidacion(Connection con,String acronimo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreEstadoLiquidacion(con, acronimo);
	}
	
	/**
	 * Método que consulta el nombre del estado de la cita
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String getNombreEstadoCita(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreEstadoCita(con, codigo);
	}
	
	/**
	 * Método que consulta las finalidades de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerFinalidadesServicio(Connection con,int codigoServicio,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoServicio", codigoServicio);
		campos.put("institucion",institucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFinalidadesServicio(con, campos);
	}
	
	/**
	 * Metodo que retorna el nombre del esquema tarifario segun el codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String getNombreEsquemaTarifario(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getNombreEsquemaTarifario(con, codigo);
	}
	
	public static int getTarifarioOficial(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getTarifarioOficial(con, codigo);
	}
	
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static Object obtenerValor(String valor) 
	{
		if(UtilidadTexto.isEmpty(valor))
			return null;
		return valor;
	}
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String obtenerValor(Object valor) 
	{
		if(UtilidadTexto.isEmpty(valor+""))
			return "";
		return valor+"";
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 */
	public static int obtenerSexoServicio(Connection con, String codigoServicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSexoServicio(con,codigoServicio);
	}
	
	/**
	 * Metodo encargado de consultar todos los servicios,
	 * pudiendolos filtrar por diferentes tipos
	 * de servicio y por el estado activo:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --tiposServicio --> ejemplo, este es el valor de esta variable  'R','Q','D'
	 * --activo --> debe indicar "t" o "f". 
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerServicos(Connection connection,String tiposServicio, String activo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerServicos(connection, tiposServicio, activo);
	}
	
	/**
	 * Metodo encargado de consultar todos los tipos de cirugia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTipoCirugia(Connection connection,String acronimo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoCirugia(connection, acronimo);
	}
	
	
	/**
	 * Metodo encargado de consultar todos los tipos de anestecia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTipoAnestecia(Connection connection,String acronimos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoAnestecia(connection, acronimos);
	}
	
	
	/**
	 * Metodo encargado de consultar todos los asocios,
	 * pudiendolos filtrar por el codigo_asocio,
	 * tipo de servicio y si participa por cirugia.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --codAsocio --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 * --tipoServ --> ejemplo, este es el valor de esta variable  'Q','R','D'
	 * --participa --> es S o N
	 * @param connection
	 * @param codAsocio
	 * @param tipoServ
	 * @param participa
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerAsocios(Connection connection,String codAsocio,String tipoServ,String participa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerAsocios(connection, codAsocio, tipoServ, participa);
		
	}
	
	
	/** 
	 * Método implementado parta obtener Tipos de Sala
	 * si se envian vacios esQuirurjica y esUrgencias no se toman en cuenta
	 * @param con
	 * @param int institucion
	 * @param String esQuirurjica (t,f) 
	 * @param String esUrgencias (t,f)
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposSala(Connection con,int institucion,String esQuirurjica,String esUrgencias)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposSala(con, institucion, esQuirurjica, esUrgencias);
	}	
	
	
	/**
	 * Metodo que devuelve un arraylist con los valores de esquemas tarifarios Generales y Particulares
	 * adicionalmente se carga el codigo del encabezado asociado a la parametrica (porcentajescx,asociosserv)
	 * @param con
	 * @param codigoInstitucion
	 * @param String parametrica
	 * @return
	 */
	public static  ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosGenPartInArray(
			Connection con,			
			int codigoInstitucion,
			String parametrica) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEsquemasTarifariosGenPartInArray(con, codigoInstitucion,parametrica);			
	}
	
	/**
	 * Metodo que retorna los esquemas tarifarios ISS
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEsquemastarifariosIss(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEsquemastarifariosIss(con);
	}
	
	/**
	 * Metodo que retorna todos los tipos de asocio
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerTiposAsocio(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposAsocio(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param mostrarEnHqx
	 * @return
	 */
	public static ArrayList obtenerTiposAnestesia(Connection con,String mostrarEnHqx)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposAnestesia(con, mostrarEnHqx);
	}
	
	/**
	 * Metodo que carga un array list con las ocupaciones medicas 
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerOcupacionesMedicas(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerOcupacionesMedicas(con);	
	}
	
	/**
	 * Metodo que retorna un array list con las especialidades
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEspecialidadesEnArray(Connection con,int codigoMedico,int codigoEspecialidad)
	{
		HashMap campos = new HashMap();
		campos.put("codigoMedico", codigoMedico);
		campos.put("codigoEspecialidad", codigoEspecialidad);
		campos.put("tipoEspecialidad", "");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEspecialidadesEnArray(con,campos);
	}
	
	/**
	 * Metodo que retorna un array list con las especialidades buscando por tipo de especialidad
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEspecialidadesEnArray(Connection con, String tipoEspecialidad)
	{
		HashMap campos = new HashMap();
		campos.put("codigoMedico", "");
		campos.put("codigoEspecialidad", "");
		campos.put("tipoEspecialidad", tipoEspecialidad);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEspecialidadesEnArray(con,campos);
	}
	
	/**
	 * Metodo que retorna un array list con los tarifarios oficiales filtrando por la columna tarifarios si se quiere se le envia un string con 'S' , 'N' o vacio, ejemplo sin filtro-->>Utilidades.obtenerTarifariosOficiales(con,""); ejemplo con filtro-->>Utilidades.obtenerTarifariosOficiales(con,"S");
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerTarifariosOficiales(Connection con,String tarifarios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTarifariosOficiales(con, tarifarios);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap<String, Object> obtenerTransaccionesCentroCosto(Connection con, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTransaccionesCentroCosto(con, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param almacen
	 * @param claseInventario
	 * @param grupoInventario
	 * @param existenciasPositivas
	 * @return
	 */
	public static HashMap<String, Object> obtenerArticulosCantidadPosNeg(Connection con, String almacen, String claseInventario, String grupoInventario, boolean existenciasPositivas) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerArticulosCantidadPosNeg(con, almacen,claseInventario,grupoInventario,existenciasPositivas);
	}

	/**
	 * 
	 * @param loginUsuario
	 * @param almacen
	 * @param transaccionEntrada
	 * @param transaccionSalida
	 * @param observaciones
	 */
	public static boolean generarLogTransSaldosInventario(Connection con,String loginUsuario, String almacen, String transaccionEntrada, String transaccionSalida, String observaciones) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().generarLogTransSaldosInventario(con,loginUsuario,almacen,transaccionEntrada,transaccionSalida,observaciones);
	}


	/**
	 * 
	 * @param con
	 * @param factura
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap consultarFacturasMismoConsecutivo(Connection con, int factura, int codigoInstitucionInt) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarFacturasMismoConsecutivo(con,factura,codigoInstitucionInt);

	}
		
	
	/**
     * @param con
     * @param Acronimo Tipo Identificacion
     * @return
     */
    public static String getDescripcionTipoIdentificacion(Connection con, String acronimoTipoIdentificacion)
    {
    	return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getDescripcionTipoIdentificacion(con, acronimoTipoIdentificacion);
    }

	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public static String obtenerRazonSocialEmpresaInstitucion(Connection con, String empresaInstitucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerRazonSocialEmpresaInstitucion(con,empresaInstitucion);
	}
	
	/**
	 * Consulta las empresas que pertenecen a la institucion
	 * @param Connection con
	 * @param int Institucion
	 * */
	public static ArrayList<HashMap<String, Object>> obtenerEmpresasXInstitucion(Connection con, int institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEmpresasXInstitucion(con, parametros);
	} 
	
    /**
     * 
     * @param con
     * @param numDiasControl
     * @param articulos
     * @param codigoPersona
     * @return
     */
	public static HashMap<String,Object> consultarArticulosSolicitadosUltimosXDias(Connection con, int numDiasControl, HashMap articulos, int codigoPersona) 
	{
    	return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarArticulosSolicitadosUltimosXDias(con, numDiasControl,articulos,codigoPersona);
	}

	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param loginUsuario
	 * @param articulosConfirmacion
	 * @return
	 */
	public static boolean generarLogConfirmacionOrdenAmbSolMed(Connection con, String codigoOrden, String loginUsuario,String esSolOrdenConfirmada, HashMap<String, Object> articulosConfirmacion) 
	{
    	return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().generarLogConfirmacionOrdenAmbSolMed(con, codigoOrden,loginUsuario,esSolOrdenConfirmada,articulosConfirmacion);
	}
	
	/**
	 * Obtener centros Costos usuario
	 * @param Connection con
	 * @param String usuarioLogin
	 * @param int Institucion 
	 * @param String tipoArea
	 * @param String codigoViaIngreso
	 * @return
	 */
	public  static HashMap obtenerCentrosCostoUsuario(Connection con,String usuarioLogin, int institucion, String tipoArea, String codigoViaIngreso) 
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentrosCostoUsuario(con, usuarioLogin, institucion, tipoArea, codigoViaIngreso);
	}
	
	/**
	 * Obtener Especialidades Medicos
	 * @param Connection con
	 * @param String codigoMedico 
	 * @return
	 */
	public static HashMap obtenerEspecialidadesMedico(Connection con,String codigoMedico) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEspecialidadesMedico(con,codigoMedico);		
	}
	

	

	/**
	 * 
	 * @param tipoFrecuencia
	 * @param diasTratamiento
	 * @param frecuencia
	 * @return
	 */
	public static double calculoNroDosisTotal(String tipoFrecuencia, String diasTratamiento, int frecuencia,boolean aproximarSiguienteUnidad)
	{
		double nroDosisTotal=0;
		double diasTratamientoInt=0;
		if(UtilidadTexto.isEmpty(diasTratamiento))
			return -1;
		diasTratamientoInt=Integer.parseInt(diasTratamiento);
		
		if(tipoFrecuencia.equals(ConstantesBD.codigoTipoFrecuenciaHoras) || tipoFrecuencia.equals(ConstantesBD.nombreTipoFrecuenciaHoras))
		{
			nroDosisTotal=(diasTratamientoInt*24.0)/frecuencia;
		}
		else if(tipoFrecuencia.equals(ConstantesBD.codigoTipoFrecuenciaMinutos) || tipoFrecuencia.equals(ConstantesBD.nombreTipoFrecuenciaMinutos))
		{
			nroDosisTotal=(diasTratamientoInt*60*24.0)/frecuencia;
		}
		else if(tipoFrecuencia.equals(ConstantesBD.codigoTipoFrecuenciaDias) || tipoFrecuencia.equals(ConstantesBD.nombreTipoFrecuenciaDias))
		{
			nroDosisTotal=diasTratamientoInt/frecuencia;
		}
		if(aproximarSiguienteUnidad)
			return UtilidadTexto.aproximarSiguienteUnidad(nroDosisTotal+"");
		return nroDosisTotal;
	}
	
	/**
	 * 
	 * @param concepto
	 * @param institucion 
	 * @return
	 */
	public static int obtenerTipoConceptoTesoreria(String concepto, int institucion) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int resultado=ConstantesBD.codigoNuncaValido;
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoConceptoTesoreria(con,concepto,institucion);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoPaciente
	 * @param ingreso
	 * @param codigoInstitucion 
	 * @return
	 */
	public static double obtenerAbonosDisponiblesPaciente(int codigoPaciente, Integer ingreso, int codigoInstitucion)
	{
		Integer idIngreso=null;
		Connection con = UtilidadBD.abrirConexion();
		if(ingreso!=null)
		{
			boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(codigoInstitucion));
			if(controlarAbonoPaciente)
			{
				idIngreso=ingreso;
			}
		}
		double retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().consultarAbonosDisponibles(con, codigoPaciente, idIngreso);
		UtilidadBD.closeConnection(con);
		return retorna;
	}

	/**
	 * 
	 * @param reciboCaja
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static boolean reciboCajaConPagosGeneralEmpresa(String reciboCaja, int institucion) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean resultado=false;
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().reciboCajaConPagosGeneralEmpresa(con,reciboCaja,institucion);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoTipoDocumento
	 * @param numeroReciboCaja
	 * @param institucion
	 * @return
	 */
	public static String[] getPagosFacturaPacienteReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion) 
	{
		ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAnulacionRecibosCajaDao().consultarPagosFacturaPacienteReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
        String codigo="";
        int numeroCodigo=0;
        try
        {
            while(rs.next())
            {
                if(numeroCodigo==0)
                {
                    codigo=codigo+rs.getString("codigo");
                }
                else
                {
                    codigo=codigo+ConstantesBD.separadorSplit+rs.getString("codigo");
                }
                numeroCodigo++;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado codigos [uTILIDADES.java]");
            e.printStackTrace();
        }
        return codigo.split(ConstantesBD.separadorSplit);
    }

	/**
	 * 
	 * @param con
	 * @param codigoTipoDocumentoPagosReciboCaja
	 * @param reciboCaja
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String[] getPagosEmpresaReciboCaja(Connection con, int codigoTipoDocumentoPagosReciboCaja, String reciboCaja, int codigoInstitucionInt) 
	{
		ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAnulacionRecibosCajaDao().consultarPagosEmpresaReciboCaja(con,codigoTipoDocumentoPagosReciboCaja,reciboCaja,codigoInstitucionInt);
        String codigo="";
        int numeroCodigo=0;
        try
        {
            while(rs.next())
            {
                if(numeroCodigo==0)
                {
                    codigo=codigo+rs.getString("codigo");
                }
                else
                {
                    codigo=codigo+ConstantesBD.separadorSplit+rs.getString("codigo");
                }
                numeroCodigo++;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado codigos [AnulacionRecibosCaja.java]");
            e.printStackTrace();
        }
        return codigo.split(ConstantesBD.separadorSplit);
	}
	/**
	 * Método implementado para disminuir el acumulado de PYP por solicitud 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @param secuencia
	 * @param fechaSalPac --> dd/mm/yyyy
	 * @return
	 */
	public static int disminuirAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion, String fechaSalPac)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().disminuirAcumuladoPYP(con, numeroSolicitud, centroAtencion,fechaSalPac);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta 
	 * @param servart
	 * @param servicio 
	 * @param esServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public static int obtenerEsquemasTarifarioServicioArticulo(Connection con, String subCuenta, int contrato , int servart, boolean esServicio, String fechaCalculoVigencia, int centroAtencion) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEsquemasTarifarioServicioArticulo(con,subCuenta,contrato,servart,esServicio,fechaCalculoVigencia, centroAtencion);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta 
	 * @param servart
	 * @param servicio 
	 * @param esServicio
	 * @param fechaCalculoVigencia 
	 * @return
	
	public static int obtenerEsquemasTarifarioServicioArticulo(Connection con, String subCuenta, int contrato , int servart, boolean esServicio, String fechaCalculoVigencia) 
	{
		int centroAtencion= ConstantesBD.codigoNuncaValido;
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEsquemasTarifarioServicioArticulo(con,subCuenta,contrato,servart,esServicio,fechaCalculoVigencia, centroAtencion);
	}*/
	
	/**
	 * 
	 * @param subCuenta
	 * @return
	 */
	public static int obtenerCodigoContratoSubcuenta(double subCuenta) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int resultado=0;
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoContratoSubcuenta(con,subCuenta);
		UtilidadBD.closeConnection(con);
		return resultado;	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param esServicios
	 * @return
	 */
	public static boolean requiererJustificacionServiciosArticulos(Connection con, int codigoIngreso, boolean esServicios) 
	{
		boolean resultado=false;
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().requiererJustificacionServiciosArticulos(con,codigoIngreso,esServicios);
		return resultado;
	}
	
	/**
	 * Metodo encargado de  obtener los tipos de convenio
	 * en un arrayList de Hashmap
	 * @param con
	 * @return
	 */
	public  static ArrayList obtenerTiposConvenio(Connection con, String institucion)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposConvenio(con, institucion);
	}
	
	/**
	 * Metodo que retorna un ArrayList con los convenios. puede retornar todos los convenios o filtrados por el tipo de regimen y/o tipocontrato y/o tipo convenio.
	 * cada registro del ArrayList contiene un mapa con la siguientes llaves (codigoConvenio,codigoEmpresa,codigoTipoRegimen,nombreConvenio,codigoTipoContrato,pyp)
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @param verificarVencimientoContrato
	 * @param fechaReferencia : fecha de referencia para verificar el vencimiento del contrato
	 * @param activo: validar activos
	 * @return
	 */
	public static ArrayList obtenerConvenios(Connection con, String tipoRegimen, String tipoContrato,boolean verificarVencimientoContrato,String fechaReferencia,boolean activo, String tipoConvenio)
	{
		HashMap campos = new HashMap();
		campos.put("tipoRegimen",tipoRegimen);
		campos.put("tipoContrato",tipoContrato);
		campos.put("verificarVencimientoContrato",verificarVencimientoContrato+"");
		campos.put("fechaReferencia",fechaReferencia);
		campos.put("activo",activo+"");
		campos.put("tipoConvenio",tipoConvenio);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConvenios(con,campos);
	}
	
	public static ArrayList<HashMap<String, Object>> obtenerMotivosAperturaCierre(Connection con, String tipo)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerMotivosAperturaCierre(con, tipo);
	}
	
	/**
	 * Metodo consulta Ultimo egreso segun via ingreso para evolucion
	 * @param con
	 * @param cuenta
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultaUltimoEgresoEvolucion(Connection con, int codigoPaciente, int viaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultaUltimoEgresoEvolucion(con, codigoPaciente, viaIngreso);
	}
	
	/**
	 * Metodo que devuelve los usuarios que hayan realizado cierre/apertura de ingresos
	 * @param con
	 * @param tipo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerUsuarioAperturaCierre(Connection con, String tipo)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerUsuarioAperturaCierre(con, tipo);
	}
	
	/**
	 * Metodo para marcar un Reingreso
	 * @param con
	 * @param codIngreso
	 * @param codPaciente
	 * @return
	 */
	public static boolean marcarReingreso(Connection con, int codIngreso, int codPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().marcarReingreso(con, codIngreso, codPaciente);
	}
	
	/**
	 * @param Connection con
	 * */
	public static ArrayList consultarUnidadesCampoParam(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarUnidadesCampoParam(con,new HashMap());	
	}
	
	/**
	 * @param Connection con
	 * */
	public static ArrayList consultarTiposCampo(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarTiposCamp(con,new HashMap());	
	}
	
	/**
	 * Metodo consulta Centro Costo por Via Ingreso mas Filtros
	 * @param con
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public static HashMap consultaCentrosCostoFiltros(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultaCentrosCostoFiltros(con);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static ArrayList consultarEscalasParam(Connection con,String codigoInsertados,String mostrarModificacion)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoEscalaInsertados",codigoInsertados);
		mapa.put("mostrarModificacion",mostrarModificacion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarEscalasParam(con,mapa);	
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static ArrayList consultarComponentesParam(Connection con, String funParam,String codigosInsertados)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoComponentesInsertados",codigosInsertados);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarComponentesParam(con, funParam,mapa);	
	}
	
	/**
	 * Metodo consulta Tipo de Monitoreo por Centro de Costo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static HashMap consultaTipoMonitoreoxCC(Connection con, int centroCosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultaTipoMonitoreoxCC(con, centroCosto);
	}
	
	/**
	 * Metodo inserta nuevo registro Ingreso Cuidados Especiales
	 * @param con
	 * @param ingreso
	 * @param estado
	 * @param indicativo
	 * @param tipoMonitoreo
	 * @param usuario
	 * @return
	 */
	public static boolean insertarIngresoCuidadosEspeciales(Connection con, int ingreso, String estado, String indicativo, int tipoMonitoreo, String usuario, int evolucion, int valoracion, String centroCosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().insertarIngresoCuidadosEspeciales(con, ingreso, estado, indicativo, tipoMonitoreo, usuario, evolucion, valoracion,centroCosto);
	}
	
	/**
	 * Metodo actualiza el estado a Finalizado de un Ingreso de Cuidados Especiales
	 * @param con
	 * @param ingreso
	 * @param usuario 
	 * @return
	 */
	public static boolean actualizarEstadoCuidadosEspeciales(Connection con, int ingreso, String usuario,String fecha,String hora)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", ingreso);
		campos.put("usuario", usuario);
		campos.put("fecha", fecha);
		campos.put("hora", hora);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarEstadoCuidadosEspeciales(con, campos);
	}
	
	/**
	 * Metodo verificar Ingreso Cuidados Especiales y requiere valoracion
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int verificarValoracionIngresoCuidadosEspeciales(Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().verificarValoracionIngresoCuidadosEspeciales(con, ingreso);
	}
	
	/**
	 * Metodo de Consulta Tipo Monitoreo por Ingreso de Cuidados especiales
	 * @param ingreso
	 * @return
	 */
	public static String consultaTipoMonitoreoxCE(int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultaTipoMonitoreoxCE(ingreso);
	}
	
	/**
	 * Metodo encargado de devolver los tipo de paciente
	 * segun la via de ingreso en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerTiposPacientePorViaIngreso(Connection con, String viaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposPacientePorViaIngreso(con, viaIngreso);
	}
	
	/**
	 * Metodo encargado de devolver los motivos de devolucion
	 * en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerMotivosDevolucionInventarios(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerMotivosDevolucionInventarios(con, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public static int obtenerGrupoServicio(Connection con, int servicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerGrupoServicio(con, servicio);
	}

	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @return
	 */
	public static int obtenertipoTarifarioEsquema(Connection con, int esquemaTarifario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenertipoTarifarioEsquema(con, esquemaTarifario);
	}
	
	/**
	 * Metodo implementado para traer las
	 * naturalezas de articulo filtradas por
	 * la institucion
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerNaturalezasArticulo(Connection con, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNaturalezasArticulo(con, institucion);
	}
	
	/**
	 * Metodo para devolver si una naturaleza es medicamento o elemento segun el campo es_medicamento 
	 * @param con
	 * @param acronimo
	 * @param Institucion
	 * @return
	 */
	public static String esMedicamento(Connection con, String acronimo, int Institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esMedicamento(con, acronimo, Institucion);
	}
	
	
	/**
	 * Consulta cuantas solicitudes se encuentran o no en los estado medicos dados a partir de una cuenta
	 * @param Connection con
	 * @param String cuenta
	 * @param String tipoSolicitud
	 * @param String estados medicos separados por coma
	 * @param boolean seEncuentreEstadoMedico. indica si la busqueda se realiza con un NOT IN o IN 
	 * */
	public static int consultarCuantosSolicitudesEstadoMedico(
			Connection con,
			int cuenta,
			int tipoSolicitud,
			String estadoMedico,
			boolean seEncuentreEstadoMedico)
	{
		HashMap parametros = new HashMap();
		parametros.put("cuenta",cuenta);
		parametros.put("tipo",tipoSolicitud);
		parametros.put("noEsten",seEncuentreEstadoMedico?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("estadosHistoriaMed",estadoMedico);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarCuantosSolicitudesEstadoMedico(con, parametros);
	}
	
	/**
	 * Metodo implementado para traer las
	 * naturalezas de articulo filtradas por
	 * la institucion
	 * @param con
	 * @return
	 */
	public static String obtenerNombreNaturalezasArticulo(Connection con, String acronimoNaturaleza) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreNaturalezasArticulo(con, acronimoNaturaleza);
	}
	
	
	/**
	 * Metodo encargado de consultar el nombre del piso
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public static  String obtenerNombrePiso(Connection con, String codigoPiso)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombrePiso(con, codigoPiso);
	}
	

	/**
	 * Metodo encargado de consultar el nombre del estado de la cama
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public static String obtenerNombreEstadoCama (Connection con, String codigoEstadoCama)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreEstadoCama(con, codigoEstadoCama);
	}
	
	/**
	 * Metodo encargado de consultar la cantidad de camas
	 * de una institucion
	 * @param con
	 * @param centroAtencion 
	 * @return
	 */
	public static  int obtenerNumeroCamas(Connection con,String institucion, String centroAtencion)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNumeroCamas(con, institucion, centroAtencion);
	}
	
	/**
	 * Metodo encargado de consultar el codigo del centro
	 * de costo principal segun el centro de costo solicitado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static String obtenerCentroCostoPrincipal (Connection con, String centroCosto, String institucion)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentroCostoPrincipal(con, centroCosto, institucion);
	}
	
	/**
	 * Metodo de consulta de los totales de una factura para su impresion
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static HashMap consultaTotalesFactura(Connection con, int codFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultaTotalesFactura(con, codFactura);
	}
	
	/**
	 * Metodo encargado de verificar si existe evolucion con 
	 * orden de salida por Admision de Urgencias
	 * @param con
	 * @param codigoAdminUrg
	 * @param anioAdminUrg
	 * @return
	 */
	public static  boolean  tieneEvolucionConSalidaXAdminUrg  (Connection con, String codigoAdminUrg, String anioAdminUrg)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().tieneEvolucionConSalidaXAdminUrg(con, codigoAdminUrg, anioAdminUrg);
	}
	
	
	/**
	 * Metodo encargado de identificar si un ingreso es reingreso;
	 * si es reingreso devuelve el ingreso asociado, si no devuelve -1
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public static int esIngresoReingreso (Connection connection, String ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esIngresoReingreso(connection, ingreso);
	}

	/**
	 * Metodo encargado de consultar la descripcion del estado 
	 * de aplicacion de pagos segun el codigo del estado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static String obtenerNombreEstadoAplicacionPagos (Connection con, int codigoEstado)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreEstadoAplicacionPagos(con, codigoEstado);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public static String obtenerNombreComponente(Connection con, String tipoComponente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreComponente(con, tipoComponente);
	}
	
	/**
	 * 
	 * @param con
	 * @param plantilla
	 * @return
	 */
	public static String obtenerNombrePlantilla(Connection con, String plantilla)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombrePlantilla(con, plantilla);
	}
	
	
	 /** Consulta los Centros de Costo para Area de Asocio de Cuentas segun Ingreso Cuidados Especiales
	 * @param con
	 * @param centroAtencion
	 * @param cuenta
	 * @param egresoV
	 * @return
	 */
	public static HashMap areasAsocioEspeciales(Connection con, int centroAtencion, int cuenta, boolean egresoV)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().areasAsocioEspeciales(con, centroAtencion, cuenta, egresoV);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public static String obtenerDescripcionComponente(Connection con, String codigoComponente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDescripcionComponente(con, codigoComponente);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEscala
	 * @return
	 */
	public static String obtenerDescripcionEscala(Connection con, String codigoEscala)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDescripcionEscala(con, codigoEscala);
	}
	
	/**
	 * Metodo que consulta la Evolucion de una Cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static int consultaEvolucionCuentaUrgencias(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultaEvolucionCuentaUrgencias(con, cuenta);
	}
	
	/**
	 * Metodo que consulta la Valoracion de una Cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static int consultaValoracionCuentaUrgencias(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultaValoracionCuentaUrgencias(con, cuenta);
	}
	
	/**
	 * Metodo implementado para traer los
	 * conceptos de pagos por institucion
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerConceptosPagos(Connection con, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConceptosPagos(con, institucion);
	}
	
	/**
	 * Metodo que consulta la Valoracion de una Cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static int obtenerSubCuentaIngreso(Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSubCuentaIngreso(con, ingreso);
	}
	
	/**
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	public static String obtenerNombreTipoMonitoreo(Connection con, String tipoMonitoreo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoMonitoreo(con, tipoMonitoreo);
	}
	
	/**
	 * Método implementado para obtener el codigo del tralado de cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public static  int getCodigoTrasladoUltimoTraslado(Connection con,int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getCodigoTrasladoUltimoTraslado(con, cuenta);
	}
	
	/**
	 * Metodo encargado de Actualizar la fecha y hora de finalizacion del 
	 * traslado de la cama
	 * @param connection
	 * @param codigoTrasladoCama
	 * @return
	 */
	public static boolean actualizarFechaHoraActualizacion (Connection connection, HashMap datos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarFechaHoraActualizacion(connection, datos);
	}
	
	/**
	 * Metodo que consulta si un Ingreso es un Preingreso sin importar estado
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean esPreingresoNormal(Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esPreingresoNormal(con, ingreso);
	}

	/**
	 * Metodo que consulta la descripcion de un tipo de convenio especifico
	 * @param con
	 * @param tipoConvenio
	 * @param codigoInstitucion
	 * @return
	 */
	public static String obtenerDescripcionTipoConvenio(Connection con, String tipoConvenio, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDescripcionTipoConvenio(con, tipoConvenio, codigoInstitucion);
	}
	
	/**
	 * Metodo encargado de consultar los tipos de tratamiento
	 * odontologico
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- activo --> Opcional (S --> filtra los activos, N --> filtra inactivos, "" --> no filtra)
	 * -- codigo --> Opcional
	 * -- institucion --> Requerido
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposTratamientoOdontologico (Connection connection,HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposTratamientoOdontologico(connection, criterios);
	}
	
	/**
	 * Metodo encargado de obtener los tipos de Habitacion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposHabitacion (Connection connection,String institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposHabitacion(connection, institucion);
	}
	
	/**
	 * Metodo encargado de obtener los grupos parametrizados en la funcionalidad transacciones validas por centro de costo concatenados por comas
	 * @param connection
	 * @param institucion
	 * @param centro_costo
	 * @param tipos_trans_inventario
	 * @param clase_inventario
	 * @return
	 */
	public static String obtenerGruposTransValidasXCC (Connection connection,int institucion,int centro_costo,int tipos_trans_inventario, int clase_inventario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerGruposTransValidasXCC(connection, institucion, centro_costo, tipos_trans_inventario, clase_inventario);
	}
	
	/**
	 * Método para obtener el nombre de un tipo de paciente
	 * @param con
	 * @param tipoPaciente
	 * @return
	 */
	public static String obtenerNombreTipoPaciente(Connection con, String tipoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoPaciente(con, tipoPaciente);
	}
	
	
	/**
	 * Método para obtener el nombre de un tipo de paciente
	 * @param con
	 * @param tipoPaciente
	 * @return
	 */
	public static String obtenerNombreTipoPaciente(String tipoPaciente)
	{
		Connection con =UtilidadBD.abrirConexion();
		String tmpNombreTipo=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoPaciente(con, tipoPaciente);
		UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		return tmpNombreTipo;
	}
	
	/**
	 * Metodo encargado de obtener los deudores
	 * segï¿½n el tipo de deudor
	 * @author Carlos Mauricio Jaramillo H.
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerDeudores(Connection con, String tipoDeudor)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDeudores(con, tipoDeudor);
	}
	
	/**
	 * Método que indica si una Entidad Financiera se encuentra
	 * inactiva ï¿½ activa, segï¿½n el consecutivo de la entidad
	 * financiera
	 * @author Carlos Mauricio Jaramillo H. 
	 * @param con
	 * @param consecutivoEntidad
	 * @return
	 */
	public static boolean obtenerActivoInactivoEntidadFinanciera(Connection con, int consecutivoEntidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerActivoInactivoEntidadFinanciera(con, consecutivoEntidad);
	}
	
	/**
	 * Metodo Que retorna el numero de la cuenta contable para un codigo de cuenta
	 * @author Andres Silva Monsalve
	 * @param cuentaConvenio
	 * @return
	 */
	public static String obtenerCuentaContable(String cuentaConvenio) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado="";
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCuentaContable(con,cuentaConvenio);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEmpresa
	 * @return
	 */
	public static String obtenerNombreEmpresa(Connection con, String codigoEmpresa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreEmpresa(con, codigoEmpresa);
	}

	/**
	 * Método que retorna el codigo de procedimiento principal
	 * asociado al servicio principal antes ingresado
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerCodigoProcedimientoPrincipalIncluidos(Connection con, String codigoServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoProcedimientoPrincipalIncluidos(con, codigoServicio);
	}

	
/** Método que retorna el codigo de procedimiento principal
 * asociado al servicio principal antes ingresado
 * @param con, codigoServiPpal, codigoServiInclu
 * @return */
	public static int obtenerCodigoServicioIncluido(Connection con, String codigoServiPpal, String codigoServiInclu)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoServicioIncluido(con, codigoServiPpal, codigoServiInclu);
	}
	
	
	/**
	 * Metodo encargado de Consultar los subgrupos
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * -- codigo
	 * -- subgrupo
	 * -- grupo
	 * -- clase
	 * -- cuentaInventario
	 * -- cuentaCosto
	 * @return ARRAYLIST <HASHMAP< > >
	 * ---------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ---------------------------------------
	 * codigo,subgrupo,grupo,clase,nombre,
	 * cuentaInventario,cuentaCosto
	 */
	public static  ArrayList<HashMap<String, Object>> obtenerSubGrupo(Connection con, HashMap criterios)
	{
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSubGrupo(con, criterios);
	}
	
	/**
	 * Método encargado de averiguar si hay una descripciï¿½n de textos en la BD
	 * @autor Ing. Felipe Pï¿½rez G.
	 * @param con
	 * @param descripcionTexto
	 */
	
	public static boolean existeDescripcionTextoProcedimiento (Connection con, String descripcionTexto, String servicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeDescripcionTextoProcedimiento(con, descripcionTexto, servicio);
	}

	/**
	 * Metodo para Traer el codigo del Convenio a partir de un codigo Interfaz
	 * @author Andres Silva Monsalve
	 * @param codigoInterfaz
	 * @return
	 */
	public static int obtenerCodigoConvenioDeCodInterfaz(String codigoInterfaz) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoConvenioDeCodInterfaz(con,codigoInterfaz);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	public static String obtenerCodigoInterfazConvenioDeCodigo(int codigoConvenio) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado="";
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoInterfazConvenioDeCodigo(con,codigoConvenio);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Método que devuelve la fecha del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String obtenerFechaIngreso(Connection con, String idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFechaIngreso(con, idIngreso);
	}
	
	/**
	 * Método que devuelve la hora del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String obtenerHoraIngreso(Connection con, String idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerHoraIngreso(con, idIngreso);
	}
	
	/**
	 * Método que devuelve el consecutivo de ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String obtenerConsecutivoIngreso(Connection con, String idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConsecutivoIngreso(con, idIngreso);
	}
	
	/**
	 * Método que devuelve el nombre del tipo
	 * de solicitud segï¿½n un tipo de solicitud
	 * dado
	 * @param con
	 * @param tipoSolicitud
	 * @return
	 */
	public static String obtenerNombreTipoSolicitud(Connection con, String tipoSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoSolicitud(con, tipoSolicitud);
	}
	
	/**
	 * Método que devuelve el nombre del tipo
	 * de solicitud segï¿½n un tipo de solicitud
	 * dado
	 * @param con
	 * @param tipoSolicitud
	 * @return
	 */
	public static String obtenerNombreTipoSolicitud(String tipoSolicitud)
	{
		Connection con= UtilidadBD.abrirConexion();
		String r= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoSolicitud(con, tipoSolicitud);
		UtilidadBD.closeConnection(con);
		return r;
	}
	
	/**
	 * Consultar el Centro de Costo Interfaz a partir del Codigo del Centro de Costo. 
	 * Tener en cuenta para los centros de costo SubAlmacen enviar el codigoInterfaz del Centro de Costo Interfaz
	 * Octubre 20 de 2008
	 * @author Andres Silva M
	 * @param codigoCentroCosto
	 * @return
	 */
	public static String obtenerCentroCostoInterfaz(int codigoCentroCosto) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado="";
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCentroCostoInterfaz(con, codigoCentroCosto);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Metodo encargado de obtener los tipos de servicio
	 * de vehiculos
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param codigo --> Opcional
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposServiciosVehiculos (Connection connection,String codigo)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposServiciosVehiculos(connection, codigo); 
	}
	
	/**
	 * Consulta el NIT del Convenio por medio del codigo interfaz del mismo
	 * @author Andres Silva M --Oct 21 / 08
	 * @param con
	 * @param codigoInterfazConvenio
	 * @return
	 */
	public static String obtenerNitConveniodeCodInterfaz(Connection con, String codigoInterfazConvenio) 
	{
		String resultado = "";
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNitConveniodeCodInterfaz(con, codigoInterfazConvenio);
		return resultado;
	}
	
	/**
	 * Método encargado de traer los tipos de regimen
	 * segï¿½n el filtro
	 * @author Carlos Mauricio Jaramillo H.
	 * @param con
	 * @param todos
	 * @param codigoFiltros
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposRegimen(Connection con, boolean todos, String codigoFiltros)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTiposRegimen(con, todos, codigoFiltros); 
	}
	
	
	/**
	 * Consulta el NIT del convenio por medio del codigo de la factura
	 * @author Andres Silva M --Oct 24 /08
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerNitConveniodeCodfactura(Connection con, int codigoFactura) 
	{
		String resultado = "";
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNitConveniodeCodfactura(con, codigoFactura);
		return resultado;
	}

	/**
	 * Consulta el codigo Interfaz del tipo de Identificacion
	 * @author Andres Silva Monsalve --Oct 25/08
	 * @param codigoTipoIdentificacionPersona
	 * @return
	 */
	public static String codigoInterfaztipoIdentificacion( String codigoTipoIdentificacionPersona) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado = "";
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().codigoInterfaztipoIdentificacion(con, codigoTipoIdentificacionPersona);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Consulta para saber si el paciente tiene informacion en la historia del sistema anterior
	 * @author Andres Silva M
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public static boolean existeHistoriaSistemaAnterior(Connection con, String tipoIdentificacion, String numeroIdentificacion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeHistoriaSistemaAnterior(con, tipoIdentificacion, numeroIdentificacion);
	}
	
	/**
	 * Metodo encargado de consultar las instituciones Sirc
	 * pudiendo filtrar por diferentes criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * --institucion --> Requerido
	 * --nivelServicio --> Opcional
	 * --tipoRed --> Opcional
	 * --tipoInstReferencia --> Opcional
	 * --tipoInstAmbulancia --> Opcional
	 * --activo --> Opcional
	 * @param connection
	 * @param criterios
	 * @return ArrayList<HashMap>
	 * --------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * --------------------------------------
	 * codigo,descripcion,tipoRed,
	 * tipoInstReferencia,tipoInstAmbulancia,
	 * nivelServicio, activo
	 */
	public static ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection connection,HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInstitucionesSirc(connection, criterios);
	}
	
	public static ArrayList<HashMap<String, Object>> getNombreInstitucionesSirc(Connection connection,String codigo,String institucion)
	{
		HashMap criterios = new HashMap ();
		
		criterios.put("institucion", institucion);
		criterios.put("codigo", codigo);
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInstitucionesSirc(connection, criterios);
	}
	
	
	/**
	 * Consulta codigo Interfaz del centro de costo subalmacen en Parametros Almacen
	 * @author Andres Silva M Oct28/08
	 * @param convertirAEntero
	 * @return
	 */
	public static String obtenerCodigoInterfazParametroAlmacen(int codigoAlmacen) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado = "";
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoInterfazParametroAlmacen(con, codigoAlmacen);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Consulta el Codigo Interfaz para un Articulo
	 * @author Andres Silva M Oct30/08
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerCodigoInterfazArticulo(int codigoArticulo) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado = "";
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoInterfazArticulo(con, codigoArticulo);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Consulta el Codigo del Articulo a partir del codigo interfaz del mismo
	 * @author Andres Silva M Oct30/08
	 * @param codigoInterfazArticulo
	 * @return
	 */
	public static int obtenerCodigoArticulodeCodInterfaz(String codigoInterfazArticulo) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoArticulodeCodInterfaz(con,codigoInterfazArticulo);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Consulta el codigo del centro de costo asociado al interfaz del almacen en la tabla parametros almacen
	 * @author Andres Silva Monsalve Oct30/08
	 * @param codigoInterfazAlmacen
	 * @return
	 */
	public static int obtenerAlmacenDeCodigoInterfaz(String codigoInterfazAlmacen) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerAlmacenDeCodigoInterfaz(con,codigoInterfazAlmacen);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Consultar Consecutivo de la Transaccion Inventarios a Partir del Codigo
	 * @author Andres Silva M Oct30/08
	 * @param codigoTrans
	 * @return
	 */
	public static int obtenerConsecutivoDeTransaccionInv(String codigoTrans) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConsecutivoDeTransaccionInv(con,codigoTrans);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	
	
	/**
	 * Metodo encargado de identificar si un ingreso tiene facturas
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public static int esIngresoFacturado(Connection connection, String ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esIngresoFacturado(connection, ingreso);
	}
	
	/**
	 * Consulta el Codigo del Tercero A partir del Numero de Identificacion del Mismo
	 * @author Andres Silva Monsalve Oct31/08
	 * @param numeroIdTercero
	 * @return
	 */
	public static int obtenercodigoTercerodeNumeroIdentificacion(String numeroIdTercero) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenercodigoTercerodeNumeroIdentificacion(con,numeroIdTercero);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Pregunta Si el login del usuario Existe en Axioma
	 * @author Andres Silva M Nov06/08
	 * @param loginUsuario
	 * @return
	 */
	public static boolean existeUsuario(String loginUsuario) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeUsuario(con,loginUsuario);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * @param con
	 * @param consecutivo
	 * @param todos
	 * @return
	 */
	public static HashMap<String, Object> consultarTransaccionesInventarios(Connection con, String consecutivo, boolean todos, boolean validarCodigos, String codigosValidados)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarTransaccionesInventarios(con, consecutivo, todos, validarCodigos, codigosValidados);
	}
	
	
	/**
	 * Metodo encargado de obtener el tipo de sala standar
	 * que esta en el grupo del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerTipoSalaStandar(Connection con, String codigoServicio ) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoSalaStandar(con, codigoServicio);
	}
	
	/**
	 * Metodo para Consultar el Consecutivo del Ingreso de un paciente y el Codigo Persona a partir de un Numero de Pedido QX
	 * @param con
	 * @param numeroPedido
	 * @return
	 */
	public static HashMap consultarIngresoYpersonadeunPedidoQx(Connection con, int numeroPedido) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarIngresoYpersonadeunPedidoQx(con,numeroPedido);
	}
	
	/**
	 * Metodo encargado de consultar el nombre de la 
	 * salida del paciente.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreSalidaPaciente(Connection con, String codigo ) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreSalidaPaciente(con, codigo);
	}

	/**
	 * Método que devuelve la sumatoria del valor total cargado
	 * de los N paquetes que tiene uns subcuenta
	 * @param con
	 * @param subCuenta
	 */
	public static double obtenerValorTotalPaquetesResponsable(Connection con, String subCuenta) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerValorTotalPaquetesResponsable(con, subCuenta);
	}
	
	/**
	 * Metodo encargado de consultar el codigo de un servicio 
	 * dependiendo del tarifario y del codigo.
	 * @param con
	 * @param codigo
	 * @param tarifario
	 * @return
	 */
	public static int obtenerCodigoServicio(Connection con, String codigo,String tarifario ) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoServicio(con, codigo, tarifario);
	}
	
	/**
	 * @param con
	 * @param codigoTercero
	 * @return
	 */
	public static String obtenerNitProveedor(Connection con, String codigoTercero) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNitProveedor(con, codigoTercero);
	}
	
	/**
	 * Verificar si Abono generado por otro sistema existe en Axioma
	 * @param reciboCaja
	 * @param tipoMovimiento
	 * @return
	 */
	public static boolean existeRegistroAbonosRC(String reciboCaja, String tipoMovimiento) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean resultado= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeRegistroAbonosRC(con, reciboCaja, tipoMovimiento);
		UtilidadBD.closeConnection(con);
		return resultado;
		
	}
	
	/**
	 * Método que permite verificar el valor del parametro "Asignar Valor Paciente a Valor Abonos"
	 * en la parametrizaciï¿½n del convenio 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String asignarValorPacienteValorAbonos(String codigoConvenio) 
	{
		Connection con = UtilidadBD.abrirConexion();
		String resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().asignarValorPacienteValorAbonos(con, codigoConvenio);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Metodo que consulta todos los Rublos Presupuestales
	 * @param con
	 * @param anioVigenciaRublo
	 * @param codigoRublo
	 * @param descripcionRublo
	 * @return
	 */
	public static HashMap obtenerRublosPresupuestales(Connection con, String anioVigenciaRublo, String codigoRublo, String descripcionRublo, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerRublosPresupuestales(con, anioVigenciaRublo,codigoRublo,descripcionRublo,institucion);
	}
	
	/**
	 * Método encargado de Obtener el cï¿½digo de un centro de atenciï¿½n dado su nombre
	 * @author Felipe Pï¿½rez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public static int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoCentroAtencion(con, nombreCentroAtencion);
	}

	/**
	 * Encargado de devolver la via de ingreso de una cuenta.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static  HashMap obtenerViaIngresoCuenta(Connection con, int cuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerViaIngresoCuenta(con, cuenta);
	}
	
	/**
	 * Encargado de devolver las solicitudes de procedimientos, cirugias, interconsultas, cargos directos y asociados a una factra
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static  HashMap obtenerSolicitudesFacturas(Connection con, int codFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerSolicitudesFacturas(con, codFactura);
	}
	
	/**
	 * Metodo encargado de consultar los datos del Centro
	 * de Atencion, pudiendo se filtrar por diferentes criterios.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- consecutivo --> Requerido
	 * -- institucion --> Opcional
	 * -- codigo --> Opcional
	 * -- activo --> Opcional
	 * @return Mapa
	 * ---------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------------
	 * codigo,descripcion,codigo_inst_sirc,
	 * empresa_institucion,direccion
	 */
	public static HashMap obtenerDatosCentroAtencion (Connection connection,HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDatosCentroAtencion(connection, criterios);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivos
	 * @param matrizLaboratorios
	 */
	public static boolean insertarRegistrosInterfazWINLAB(Connection con, ArrayList consecutivos, HashMap matrizLaboratorios) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().insertarRegistrosInterfazWINLAB(con, consecutivos,matrizLaboratorios);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public static String obtenerCodigoPisoCama(Connection con, String codigoCama) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoPisoCama(con, codigoCama);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarEncabezadoLaboratoriosPendientesWINLAB(Connection con) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarEncabezadoLaboratoriosPendientesWINLAB(con);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap consultarDetalleLaboratoriosPendientesWINLAB(Connection con, String consecutivo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarDetalleLaboratoriosPendientesWINLAB(con,consecutivo);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivos
	 */
	public static boolean eliminarLaboratoriosPendientesWINLAB(Connection con,String consecutivos) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().eliminarLaboratoriosPendientesWINLAB(con,consecutivos);
	}
	
	/**
	 * Consulta si un centro de costos esta parametrizado como un Registro Respuesta Proce X Terceros
	 * @param Connection con
	 * @param int codigoCentroCosto
	 * */
	public static String esCentroCostoRespuestaProcTercero(Connection con,int codigoCentroCosto)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPk",codigoCentroCosto);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esCentroCostoRespuestaProcTercero(con,parametros);				
	}
	
	/**
	 * Metodo encargado de verificar si existe algun registro de la cuenta
	 * en la tabla egresos.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static boolean existeAlgunRegistroEgreso(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeAlgunRegistroEgreso(con, cuenta);
	}
	
	/**
	 * Metodo encargado de consultar las empresas parametrizadas en el sistema
	 * @param con
	 * @param int institucion
	 * @param boolean dirTerritorial
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con,int institucion,boolean dirTerritorial)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("dirTerritorial",dirTerritorial?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEmpresas(con, parametros);	
	}
	
	/**
	 * Metodo encargado para consultar el codigo de clase inventario de un articulo especifico
	 * @param con 
	 * @param codaxi
	 * @return
	 */
	public static String consultarClaseInterfazArticulo(Connection con, String articulo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarClaseInterfazArticulo(con, articulo);
	}
	
	/**
	 * @param con
	 * @param codigoCC
	 * @return
	 */
	public static String getTipoEntidadEjecutaCC(Connection con, String codigoCC) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getTipoEntidadEjecutaCC(con,codigoCC);
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCCSolicitadoSolicitud(Connection con, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCCSolicitadoSolicitud(con,numeroSolicitud);
	}	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeAutorizacionSolicitud(Connection con, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existeAutorizacionSolicitud(con,numeroSolicitud);
	}
		
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerTipoAutorizacionEntidadSubcontratada(Connection con, String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoAutorizacionEntidadSubcontratada(con,numeroSolicitud);
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String verificarregistroUsuarioEntidadSubcontratada(Connection con, String numeroSolicitud, String login) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().verificarregistroUsuarioEntidadSubcontratada(con,numeroSolicitud,login);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param cuentaContable
	 * @param descripcion
	 * @return
	 */
	public static HashMap obtenerCuentaContableXCodigo(int codigoInstitucion, String cuentaContable)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCuentaContableXCodigo(con, codigoInstitucion, cuentaContable);
		UtilidadBD.closeConnection(con);
		return mapa;
	}

	public static ArrayList<ArrayList<Object>> listarProgramasPyP(boolean activo, int codigoInstitucion) {
		Connection con= UtilidadBD.abrirConexion();
		ArrayList listado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().listarProgramasPyP(con, activo, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return listado;
	}
	
	/**
	 * Método que consulta los dias de la semana
	 * @param con	
	 * @return ArrayList<HashMap>
	 * @author Vï¿½ctor Gï¿½mez 
	 */
	public static ArrayList<HashMap> obtenerDiasSemanaArray(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDiasSemanaArray(con);
	}

	/**
	 * Metodo que consulta los parentezcos
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarParentezcos(Connection con) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarParentezcos(con);
	}
	
	
	public static ArrayList<HashMap<String, Object>> consultarMotivosCita(Connection con, int codInstitucion,String tipoMotivo) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarMotivosCita(con, codInstitucion,tipoMotivo);
	}

	public static ArrayList<HashMap<String, Object>> consultarMediosConocimientoServ(Connection con, int codInstitucion) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarMediosConocimientoServ(con,codInstitucion);
	}
	
	/**
	 * Obtener los convenios del parametro general "Convenios a mostrar por defecto en el presupuesto odontolÃ³gico"
	 * 
	 * Keys del mapa
	 * 		- codigo
	 * 		- descripcion
	 * 
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConveniosAMostrarPresupuestoOdo(Connection con, int codInstitucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConveniosAMostrarPresupuestoOdo(con,codInstitucion);
	}

	/**
	 * Actualizar el parametro general "Convenios a mostrar por defecto en el presupuesto odontolÃ³gico"
	 * @param con
	 * @param conveniosAMostrarPresupuestoOdo
	 * @param institucion 
	 * @param usuario 
	 * @return 
	 */
	public static Object actualizarConveniosAMostrarPresupuestoOdo(Connection con,ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo, String usuario, int institucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarConveniosAMostrarPresupuestoOdo(con,conveniosAMostrarPresupuestoOdo, usuario, institucion);
	}
	
	/**
	 * Obtener informacion sobre la cuenta solicitante de una orden ambulatoria
	 * @param con
	 * @param numeroOrden
	 * @return
	 */
	public static HashMap obtenerInfoCuentaSolicitanteOrdenAmbulatoria(Connection con, int codigoOrden) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInfoCuentaSolicitanteOrdenAmbulatoria(con, codigoOrden);
	}

	
	/**
	 * M&eacute;todo que retorna un ArrayList con las descripciones de los acronimos pasados en el arreglo como par&aacute;metro.
	 * 
	 * @param con
	 * @param listaConstantes
	 * @param ordenar
	 * @return ArrayList<DtoIntegridadDominio> con los acr&oacute;nimos y las descripciones respectivas.
	 */
	public static ArrayList<DtoIntegridadDominio> generarListadoConstantesIntegridadDominio(Connection con, String[] listaConstantes, boolean ordenar){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().generarListadoConstantesIntegridadDominio(con, listaConstantes, ordenar);
	}

	/**
	 * Obtener Motivos Atencion Odontologica
	 * @param con
	 * @param tipo atencion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMotivosAtencionOdontologica(Connection con, int tipo) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerMotivosAtencionOdontologica(con, tipo);
	}
	
	/**
	 * 
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarProcAutoEstados(DtoLogProcAutoEstados dto) {
		
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().guardarProcAutoEstados(dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerListadoCoberturasXTipo(Connection con, int institucion, String tipoCobertura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerListadoCoberturasXTipo(con, institucion,tipoCobertura);
	}
	
	/**
	 * 
	 */
	public static ArrayList<DtoConceptosIngTesoreria> obtenerConceptosIngresoTesoreria(int tipoIngreso,String valorFiltro)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerConceptosIngresoTesoreria(tipoIngreso, valorFiltro);
	}
	
	/**
	 * 
	 */
	public static String obtenerIndicativoInterfazPersona(Connection con, String nroId)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerIndicativoInterfazPersona(con,nroId);
	}
	
	/**
	 * 
	 */
	public static String obtenerIndicativoInterfazMedico(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerIndicativoInterfazMedico(con,codigoPersona);
	}
	
	/**
	 * 
	 */
	public static HashMap obtenerDatosTercero(Connection con, int codigoTercero)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerDatosTercero(con,codigoTercero);
	}
	
	/**
	 * 
	 * Método que se encarga de consultar el filtro asociado al concepto de ingreso
	 * de tesoreria.
	 * 
	 * @param con
	 * @param concepto
	 * @return
	 */
	public static String obtenerFiltroValorConceptoIngreso(Connection con, int concepto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerFiltroValorConceptoIngreso(con,concepto);
	}
	
	/***
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static boolean esOrdenAmbulatoriaPYP(Connection con,String orden, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esOrdenAmbulatoriaPYP(con, orden,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param orden
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreTipoIdentificacion(String acronimo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreTipoIdentificacion(acronimo);
	}
	
	/**
	 * Método encargado de arreglar los consecutivos de una tabla
	 * @param parametros
	 * @return
	 */
	public static boolean arreglarConsecutivos(HashMap<String, Object> parametros) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().arreglarConsecutivos(parametros);
	}

	/**
	 * 
	 * @param loginUsuario
	 * @return
	 */
	public static String obtenerEmailUsuario(String loginUsuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerEmailUsuario(loginUsuario);
	}

	
	/**
	 * 
	 * @param codigoPersona
	 * @param tipoMovimiento
	 * @param codigoCita
	 * @return
	 */
	public static double obtenerAbonoPacienteTipoYNumeroDocumento(int codigoPersona, int tipoMovimiento,int codigoCita) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerAbonoPacienteTipoYNumeroDocumento(codigoPersona,tipoMovimiento,codigoCita);
	}
	
	
	
	
	
	/**
	 * Método encargado verificar si una lista esta vacia o es nula
	 * @param ArrayList
	 * @return boolean
	 * @author Cristhian Murillo
	 */
	public static boolean isEmpty(List lista) 
	{
		boolean vacia = true;
		if(lista != null) 
		{
			if(lista.isEmpty()) 
			{
				vacia = true;
			}
			else
			{
				vacia = false;
			}
		}
		
		return vacia;
	}
	
	
	/***
	 * Metodo que convierte de set Implementado con hashSet a un List implementado con Array list
	 * @author Cristhian Murillo
	 * @return
	 */
	public static List<Object> convertirSetAList(HashSet<Object> conjuntoSet){
		
		List<Object> lista = new ArrayList<Object>();
		
		try {
			
			lista = new ArrayList(conjuntoSet);
			
		} catch (Exception e) {
			
		}
		
		return lista;
	}
	
	/***
	 * Metodo que convierte de set Implementado con hashSet a un List implementado con Array list
	 * @author Cristhian Murillo
	 * @return
	 */
	public static List<Object> convertirHashSetAList(Set conjuntoSet){
		
		List<Object> lista = new ArrayList<Object>();
		
		try {
			
			lista = new ArrayList(conjuntoSet);
			
		} catch (Exception e) {
			
		}
		
		return lista;
	}


	/**
	 * 
	 * @param con 
	 * @param tipoID
	 * @param numeroID
	 * @return
	 */
	public static HashMap obtenerInformacionUsuarioCapitado(Connection con, String tipoID,String numeroID) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerInformacionUsuarioCapitado(con,tipoID,numeroID);
	}
	

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static DtoDiagnostico getDiagnosticoPacienteIngreso(Connection con,int idIngreso) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getDiagnosticoPacienteIngreso(con, idIngreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static DtoDiagnostico getDiagnosticoPacienteCuenta(Connection con,int idCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getDiagnosticoPacienteCuenta(con, idCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static DtoDiagnostico getDiagnosticoPacienteIngreso(int idIngreso) 
	{
		DtoDiagnostico resultado=new DtoDiagnostico();
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getDiagnosticoPacienteIngreso(con, idIngreso);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static DtoDiagnostico getDiagnosticoPacienteCuenta(int idCuenta) 
	{
		DtoDiagnostico resultado=new DtoDiagnostico();
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().getDiagnosticoPacienteCuenta(con, idCuenta);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param numReciboCaja
	 * @param codigoInstitucionInt
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static String obtenerCodigoReciboCaja(String numReciboCaja,int codigoInstitucionInt, int codigoCentroAtencion) 
	{
		String resultado="";
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCodigoReciboCaja(con, numReciboCaja,codigoInstitucionInt,codigoCentroAtencion);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	

	
	/**
	 * Valida si el consecutivo enviado no es nulo o un valor invalido
	 * @author Cristhian Murillo
	 * @param valorConsecutivo
	 */
	public static boolean validarExisteConsecutivo(String valorConsecutivo)
	{
		boolean existe =  false;
		
		if(UtilidadTexto.isEmpty(valorConsecutivo)){
			existe = false;
		}
		else
		{
			try 
			{
				int valorConsecutivoInt = Integer.parseInt(valorConsecutivo);
				
				if(valorConsecutivoInt == ConstantesBD.codigoNuncaValido){
					existe = false;
				}
				else
				{
					existe = true;
				}
			} catch (Exception e) {
				existe = false;
			}
		}
		
		return existe;
	}

	/**
	 * 
	 * @param con
	 * @param clasificacionSE
	 * @return
	 */
	public static String obtenerTipoRegimenClasificacionSocioEconomica(Connection con, int clasificacionSE) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerTipoRegimenClasificacionSocioEconomica(con, clasificacionSE);
	}
	
	
	
	/**
	 * Retorna el nombre organizado validando los campos que sean nulos.
	 * El formato entregado es: primerApellido segundoApellido primerNombre segundoNombre (login)
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param login 
	 * @return nombreOrganizado
	 *
	 * @autor Cristhian Murillo
	 */
	public static String obtenerNombreLoginOrganizado(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String login)
	{
		String nombreOrganizado = "";
		
		if(!UtilidadTexto.isEmpty(primerApellido)){
			nombreOrganizado += primerApellido +" ";
		}
		
		if(!UtilidadTexto.isEmpty(segundoApellido)){
			nombreOrganizado += segundoApellido +" ";
		}
		
		if(!UtilidadTexto.isEmpty(primerNombre)){
			nombreOrganizado += primerNombre +" ";
		}
		
		if(!UtilidadTexto.isEmpty(segundoNombre)){
			nombreOrganizado += segundoNombre +" ";
		}
		
		if(!UtilidadTexto.isEmpty(login)){
			nombreOrganizado += " ("+login+")";
		}
		
		return nombreOrganizado;
	}
	
	/**
	 * Verfica si existen conceptos notas pacientes creados
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public static boolean existenConceptosNotasPaciente() {
		boolean resultado;
		Connection con = UtilidadBD.abrirConexion();
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existenConceptosNotasPaciente(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Verfica si existen notas pacientes creadas
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public static boolean existenNotasPaciente() {
		boolean resultado;
		Connection con = UtilidadBD.abrirConexion();
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existenNotasPaciente(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Verfica si existen notas pacientes creadas
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public static boolean existenMovimientosAbonos() {
		boolean resultado;
		Connection con = UtilidadBD.abrirConexion();
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().existenMovimientosAbonos(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Este metodo consulta los centros de atención por usuario y estado activo
	 * @param con
	 * @param usuario
	 * @return Un ArrayList<String[]> donde la posición 0 tiene el consecutivo del centro de atención
	 * 		   y en la posición 1 la descripción del mismo
	 */
	public static ArrayList<String[]> obtenerCentrosAtencionxUsarioEstadoActivo(String usuario) {
		ArrayList<String[]> listaCentrosAtencion = new ArrayList<String[]>(); 
		try {
			Connection con = UtilidadBD.abrirConexion();
			listaCentrosAtencion = 
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().
				obtenerCentrosAtencionxUsarioEstadoActivo(con, usuario);
			UtilidadBD.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaCentrosAtencion;
	}
	
	/**
	 * Este metodo consulta los centros de costo por usuario
	 * @param con
	 * @param usuario
	 * @return Un ArrayList<String[]> donde la posición 0 tiene el codigo del centro de costo,
	 * 		   en la posición 1 el nombrem en la 2 la descripción del centro de atención
	 * 		   y la posición 3 el consecutivo del centro de atención al que pertenece
	 */
	public static ArrayList<String[]> obtenerCentrosCostoxUsario(String usuario) {
		ArrayList<String[]> listaCentrosCosto = new ArrayList<String[]>();
		try {
			Connection con = UtilidadBD.abrirConexion();
			listaCentrosCosto = 
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().
				obtenerCentrosCostoxUsario(con, usuario);
			UtilidadBD.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaCentrosCosto;
	}
	
	
	public static Integer obtenerCantidadCajasCajero(Connection con,int centroAtencion, String usuario,String valorParametro) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerCantidadCajasCajero(con, centroAtencion, usuario, valorParametro);
	}
	
    /**
     * Método que busca el numero de cuenta de la solicitus por orden ambulatoria
     * @param con
     * @param numeroSolicitud
     * @return numero de cuenta
     */
    public static Integer obtenerNumeroCuentaAutorizacionPorOrdenAmbulatoria(Connection con,Integer numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNumeroCuentaAutorizacionPorOrdenAmbulatoria(con,numeroSolicitud);
    }
    
	/**
	 * Este metodo consulta las via de amdinistracion
	 * @param con
	 * @param codigoVia
	 * @return String
	 */
	public static String obtenerNombreViaAdministracion(Connection con,int codigoVia) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().obtenerNombreViaAdministracion(con,codigoVia);
	}
	
	/**
	 * @param con
	 * @param codigoConvenio
	 * @return si un convenio en capitacion subcontrata 
	 */
	public static Boolean esCapitacionSubcontratada(Connection con,Integer codigoConvenio){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().esCapitacionSubcontratada(con, codigoConvenio);
	}
	
	public static boolean liberarUltimaCamaPaciente(Connection con, Integer codigoPersona){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().liberarUltimaCamaPaciente(con, codigoPersona);
	}
	
	/**
	 * Consulta todas las especialidades que posee un profesional de la salud
	 * 
	 * @param connection
	 * @param profesionalHQxDto
	 * @param descartarEspecialidades
	 * @return lista de especialidades de un profesional
	 * @throws BDException
	 * @author jeilones
	 * @created 5/07/2013
	 */
	public static List<EspecialidadDto> consultarEspecialidadesProfesional(Connection connection,ProfesionalHQxDto profesionalHQxDto, int codigoInstitucion, Boolean especiliadadesActivas,List<EspecialidadDto>descartarEspecialidades) throws BDException{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().consultarEspecialidadesProfesional(connection, profesionalHQxDto, codigoInstitucion, especiliadadesActivas, descartarEspecialidades);
	}
}	