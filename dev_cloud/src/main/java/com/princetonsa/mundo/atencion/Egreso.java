/*
 * @(#)Egreso.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.solicitudes.SolicitudEvolucion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.princetonsa.actionform.EgresoForm;
import com.princetonsa.actionform.manejoPaciente.OcupacionDiariaCamasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EgresoDao;

/**
 * Esta clase encapsula los atributos y métodos concernientes a un <code>Egreso</code> del sistema.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class Egreso 
{
	
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Egreso
	 */
	private Logger logger = Logger.getLogger(Egreso.class);
	
	/************************************************************************************
	 *Modificado por anexo 747 
	 */
	
		//indices para el manejo de los nuevos datos
		public static final String [] indicesFactura ={"fechaCierreIngreso0","horaCierreIngreso1","facturasFecha2","cama3","codigoCama4","ingreso5","acompanadoPor6",
														"remitidoA7","placa8","conductor9","quienRecibe10","observaciones11","codigoCuenta12"}; 
	
		/**
		 * Metodo encargado de consultar los datos de la factura
		 * para la boleta de salida
		 * @param connection
		 * @param datos
		 * ------------------------------
		 * KEY'S DEL MAPA DATOS
		 * ------------------------------
		 * --ingreso6
		 * @return  HashMap
		 * -------------------------------
		 * KEY'S DEL MAPA RESULTADO
		 * -------------------------------
		 * fechaCierreIngreso0,horaCierreIngreso1
		 * facturasFecha2,cama3,codigoCama4,ingreso6
		 */ 
		public static HashMap consultaDatosFactura (Connection connection,HashMap datos)
		{
			return egresoDao().consultaDatosFactura(connection, datos);
		}
		
		/**
		 * Metodo encargado de consultar si existe boleta de salida
		 * @param connection
		 * @param cuenta
		 *  @return false/true
		 */
		public static boolean consultarExisteBoletaSalida(Connection connection,int idCuenta){
			
			return egresoDao().consultarExisteBoletaSalida(connection, idCuenta);
		}
		
		/**
		 * Metodo encargado de consultar los nuevos datos del egreso
		 * @param connection
		 * @param cuenta
		 *  @return
		 *  -------------------------
		 *  KEY'S DEL MAPA RESULT
		 *  -------------------------
		 *  acompanadoPor6, remitidoA7,
		 *  placa8,conductor9,quienRecibe10,
		 *  observaciones11,codigoCuenta12 
		 */
		public static HashMap consultarNuevosDatosEgreso (Connection connection,String cuenta)
		{
			return egresoDao().consultarNuevosDatosEgreso(connection, cuenta);
		}
	
		
		/**
		 * Metodo encargado de actualizar los nuevos datos del egreso
		 * @param connection
		 * @param datos
		 * ----------------------------
		 * KEY'S DEL MAPA DATOS
		 * ----------------------------
		 * acompanadoPor6 --> Opcional
		 * remitidoA7 --> Opcional
		 * placa8 --> Opcional
		 * conductor9 --> Opcional
		 * quienRecibe10 --> Opcional
		 * observaciones11 --> Opcional
		 * codigoCuenta12 --> Requerido
		 * @return false/true
		 */
		public static  boolean actualizarNuevosDatosEgreso (Connection connection,HashMap datos)
		{
			return egresoDao().actualizarNuevosDatosEgreso(connection, datos);
		}
		
		
		
		/**
		 * Metodo encargado de guardar los nuevos datos del
		 * egreso
		 * @param connection
		 * @param datos
		 */
		public static boolean guardarDatosNuevos (Connection connection, HashMap datos)
		{
			
			boolean transaccion=UtilidadBD.iniciarTransaccion(connection);
			
			if (transaccion)
				transaccion=actualizarNuevosDatosEgreso(connection, datos);
			
			if(transaccion)
				UtilidadBD.finalizarTransaccion(connection);			
			else
				UtilidadBD.abortarTransaccion(connection);
			
			return transaccion;
		}
		
		/**
		 * Metodo encargado de guardar los datos del
		 * egreso Cirugia Ambulatoria
		 * @param connection
		 * @param datos
		 */
		public boolean ActualizarDatosCirugia (Connection connection, 			
				String fechaEgreso,
				String fechaGrabacionEgreso,
				String horaEgreso,
				String horaGrabacionEgreso,
				UsuarioBasico medico,
				PersonaBasica paciente)
		{
			
			try
			{
			    this.setNumeroCuenta(paciente.getCodigoCuenta());
			    this.setFechaEgreso(UtilidadFecha.conversionFormatoFechaABD(fechaEgreso));
			    this.setFechaGrabacionEgreso(UtilidadFecha.conversionFormatoFechaABD(fechaGrabacionEgreso));
			    this.setHoraEgreso(horaEgreso);
			    this.setHoraGrabacionEgreso(horaGrabacionEgreso);
			    this.setUsuarioGrabaEgreso(medico);	
			    
			    if(this.modificarEgresoUsuarioFinalizar(connection, "empezar") != 1){
			    	UtilidadBD.abortarTransaccion(connection);
			        return false;
			    }else{
			    	UtilidadBD.finalizarTransaccion(connection);
			    }
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			return true;
		}
		
		
		public static ActionForward imprimirBoletaSalida (Connection connection,UsuarioBasico usuario,EgresoForm forma,String ingreso, HttpServletRequest request,ActionMapping mapping) throws SQLException
		{
			//se hace una llamada al recolector de basura
			System.gc();
			
			DesignEngineApi comp;
			String codigoAImprimir = "";
			String reporte="",DataSet="";
			
				//logger.info("\n ---> ConRompimiento");
				DataSet="datosgenerales";
				reporte="boletaSalida.rptdesign";
			
			//LLamamos al reporte
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",reporte);
	        //Imprimimos el encabezado del reporte
			armarEncabezado(comp, connection, usuario, request);
			//se evalue el si tiene o no rompimiento
			//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
			comp.obtenerComponentesDataSet(DataSet);
			//Modificamos el DataSet con las validaciones comunes para todos
	       // String newQuery = comp.obtenerQueryDataSet().replace("WHERE", obtenerWhere(forma.getCriterios(), organizarEstadosCama(forma.getEstadosCamas())));
	        //Se modifica el query
	        comp.modificarQueryDataSet(ConsultasBirt.consultaEgreso(forma.getIdCuenta()+""));
	        
	        //se modifica el otro data set
	         DataSet="facturas";
	    	comp.obtenerComponentesDataSet(DataSet);
	    	comp.modificarQueryDataSet(ConsultasBirt.consultaFacturas(ingreso));
	    	 
	        //se modifica el otro data set
	 	    DataSet="salida";
	 	    comp.obtenerComponentesDataSet(DataSet);
	 	    comp.modificarQueryDataSet(ConsultasBirt.consultaSalida(ingreso));	 	    	
	    	
	        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	 	    comp.lowerAliasDataSet();
	 	    String newPathReport = comp.saveReport1(false);
	        comp.updateJDBCParameters(newPathReport);
	        if(!newPathReport.equals(""))
	        {
	        	request.setAttribute("isOpenReport", "true");
	        	request.setAttribute("newPathReport", newPathReport);
	        }
			
	    	UtilidadBD.cerrarConexion(connection);
	        return mapping.findForward("resultadoEgreso");
		
		}
		
		private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, HttpServletRequest request)
		{
			//Insertamos la información de la Institución
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
	        comp.insertGridHeaderOfMasterPage(0,1,1,4);
	        Vector v = new Vector();
	        v.add(institucion.getRazonSocial());
	        v.add(Utilidades.getDescripcionTipoIdentificacion(connection,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
	        v.add(institucion.getDireccion());
	        v.add("Tels. "+institucion.getTelefono());
	        comp.insertLabelInGridOfMasterPage(0, 1, v);
	        
	        //Insertamos el nombre de la funcionalidad en el reporte 
	        comp.insertLabelInGridPpalOfHeader(1,1, "BOLETA DE SALIDA");
	        
	       
	     
	        
	        //Información de Pie de Pagina
	        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
	    }
		
	/**
	 * 
	 **************************************************************************************/
	
	/**
	 * Numero de la cuenta asociado a ese egreso.
	 */
	private int numeroCuenta=0;

	/**
	 * Número de la admisión asociada a este egreso.
	 */
	private int numeroAdmision=0;
	
	/**
	 * Número de la evolución asociada a este egreso.
	 */
	private int numeroEvolucion=0;

	/**
	 * Número de autorización de este egreso.
	 */
	private String numeroAutorizacion="";

	/**
	 * Indica si el paciente salió vivo o muerto.
	 */
	private boolean estadoSalida=true;
	
	/**
	 * Fecha de egreso dada por el usuario
	 */
	private String fechaEgreso="";

	/**
	 * Hora de egreso dada por el usuario
	 */
	private String horaEgreso="";

	/**
	 * Fecha del sistema en la que se grabó este egreso.
	 */
	private String fechaGrabacionEgreso="";

	/**
	 * Hora del sistema en la que se grabó este egreso.
	 */
	private String horaGrabacionEgreso="";

	/**
	 * Descripción de otro destino de salida, cuando se da este caso.
	 */
	private String otroDestinoSalida="";

	/**
	 * Código y nombre del dstino de salida de este egreso.
	 */
	private InfoDatos destinoSalida;

	/**
	 * Código y nombre de la causa externa de este egreso.
	 */
	private InfoDatos causaExterna;

	/**
	 * Diagnóstico definitivo principal de este egreso.
	 */
	private Diagnostico diagnosticoDefinitivoPrincipal;

	/**
	 * Primer diagnóstico relacionado.
	 */
	private Diagnostico diagnosticoRelacionado_1;

	/**
	 * Segundo diagnóstico relacionado.
	 */
	private Diagnostico diagnosticoRelacionado_2;

	/**
	 * Tercer diagnóstico relacionado.
	 */
	private Diagnostico diagnosticoRelacionado_3;

	/**
	 * Diagnóstico de la causa de muerte.
	 */
	private Diagnostico diagnosticoCausaMuerte;
	
	/**
	 * Diagnóstico de la causa de muerte.
	 */
	private Diagnostico diagnosticoComplicacion;

	/**
	 * <code>UsuarioBasico</code> que efectúa este egreso. 
	 */
	private UsuarioBasico usuarioGrabaEgreso;

	/**
	 * Médico que autoriza este egreso. 
	 */
	private UsuarioBasico medicoResponsable;

	/**
	 * Objeto <code>Evolucion</code> con los datos de la última evolución asociada a este egreso.
	 */
	private EvolucionInterface ultimaEvolucion;
	
	/**
	 * Objeto <code>Cama</code> con los datos de la última evolución asociada a este egreso.
	 */
	private Cama camaReversionEgreso=null;

	/**
	 * String que define como se reversó el egreso
	 */
	private String motivoReversionEgreso="";
	
	private int codigoTipoMonitoreo;
	
	/**
	 * El DAO usado por el objeto <code>Egreso</code> para acceder a la fuente de datos. 
	 */
	private EgresoDao egresoDao;
	
	/**
	 * Datos usado para cargar la reversión
	 *
	 */
	private String fechaReversion;
	private String horaReversion;

	/**
	 * @return Returns the fechaReversion.
	 */
	public String getFechaReversion() {
		return fechaReversion;
	}
	/**
	 * @param fechaReversion The fechaReversion to set.
	 */
	public void setFechaReversion(String fechaReversion) {
		this.fechaReversion = fechaReversion;
	}
	/**
	 * @return Returns the horaReversion.
	 */
	public String getHoraReversion() {
		return horaReversion;
	}
	/**
	 * @param horaReversion The horaReversion to set.
	 */
	public void setHoraReversion(String horaReversion) {
		this.horaReversion = horaReversion;
	}
	/**
	 * Crea un nuevo objeto <code>Egreso</code>.
	 */
	public Egreso() 
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			egresoDao = myFactory.getEgresoDao();
			wasInited = (egresoDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Método para obtener de forma estática el DAO de Egreso
	 * @return
	 */
	private static EgresoDao egresoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEgresoDao();
	}
	
	/**
	 * Método que verifica si una cuenta tiene egreso efectivo
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean existeEgresoEfectivo(Connection con,int codigoCuenta)
	{
		return egresoDao().existeEgresoEfectivo(con, codigoCuenta);
	}
	
	/**
	 * Método implementado para borrar el preegreso de una cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean borrarPreEgreso(Connection con,int codigoCuenta)
	{
		if(egresoDao().borrarPreEgreso(con, codigoCuenta)>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Método implementado para borrar el egreso automatico de una cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean borrarEgresoAutomatico(Connection con,int codigoCuenta)
	{
		if(egresoDao().borrarEgresoAutomatico(con, codigoCuenta)>0)
			return true;
		else
			return false;
	}

	/**
	 * Este método crea en la fuente de almacenamiento un egreso
	 * del con los datos dados al momento de dar orden de salida
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int crearEgresoDesdeEvolucion (Connection con) throws SQLException
	{
		SolicitudEvolucion sol=new SolicitudEvolucion ();
		
		sol.interpretarSolicitudesEvolucionDadaEvolucionTransaccional (con, this.numeroEvolucion, ConstantesBD.inicioTransaccion);
		return this.crearEgresoDesdeEvolucionTransaccional(con, ConstantesBD.finTransaccion);
	}
	
	/**
	 * Este método crea en la fuente de almacenamiento un egreso
	 * del con los datos dados al momento de dar orden de salida
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int crearEgresoDesdeEvolucionTransaccional (Connection con, String estado) throws SQLException
	{
		SolicitudEvolucion sol=new SolicitudEvolucion ();
		
		if (estado.equals(ConstantesBD.finTransaccion))
		{
			sol.interpretarSolicitudesEvolucionDadaEvolucionTransaccional (con, this.numeroEvolucion, ConstantesBD.continuarTransaccion);
		}
		else
		{
			sol.interpretarSolicitudesEvolucionDadaEvolucionTransaccional (con, this.numeroEvolucion, estado);
		}
		
		if (estado.equals(ConstantesBD.inicioTransaccion))
		{
			return egresoDao.crearEgresoDesdeEvolucionTransaccional(con, this.numeroCuenta, this.numeroEvolucion, estadoSalida, destinoSalida.getCodigo(), otroDestinoSalida, numeroAutorizacion, causaExterna.getCodigo(), this.diagnosticoCausaMuerte.getAcronimo(), this.diagnosticoCausaMuerte.getTipoCIE(), this.diagnosticoDefinitivoPrincipal.getAcronimo(), this.diagnosticoDefinitivoPrincipal.getTipoCIE(), this.diagnosticoRelacionado_1.getAcronimo(), this.diagnosticoRelacionado_1.getTipoCIE(), this.diagnosticoRelacionado_2.getAcronimo(), this.diagnosticoRelacionado_2.getTipoCIE(), this.diagnosticoRelacionado_3.getAcronimo(), this.diagnosticoRelacionado_3.getTipoCIE(), this.medicoResponsable.getCodigoPersona(),this.diagnosticoComplicacion.getAcronimo(),this.diagnosticoComplicacion.getTipoCIE(), ConstantesBD.continuarTransaccion);
		}
		else
		{
			return egresoDao.crearEgresoDesdeEvolucionTransaccional(con, this.numeroCuenta, this.numeroEvolucion, estadoSalida, destinoSalida.getCodigo(), otroDestinoSalida, numeroAutorizacion, causaExterna.getCodigo(), this.diagnosticoCausaMuerte.getAcronimo(), this.diagnosticoCausaMuerte.getTipoCIE(), this.diagnosticoDefinitivoPrincipal.getAcronimo(), this.diagnosticoDefinitivoPrincipal.getTipoCIE(), this.diagnosticoRelacionado_1.getAcronimo(), this.diagnosticoRelacionado_1.getTipoCIE(), this.diagnosticoRelacionado_2.getAcronimo(), this.diagnosticoRelacionado_2.getTipoCIE(), this.diagnosticoRelacionado_3.getAcronimo(), this.diagnosticoRelacionado_3.getTipoCIE(), this.medicoResponsable.getCodigoPersona(),this.diagnosticoComplicacion.getAcronimo(),this.diagnosticoComplicacion.getTipoCIE(), estado);
		}
		
	}
	
	/**
	 * Método que inserta un egreso sin evolucion
	 * @param con
	 * @param estado
	 * @return
	 */
	public int crearEgresoSinEvolucionTransaccional(Connection con,String estado)
	{
		int resp = 0;
		
		try 
		{
			resp = egresoDao.crearEgresoDesdeEvolucionTransaccional(
				con, 
				this.numeroCuenta, 
				ConstantesBD.codigoNuncaValido, 
				estadoSalida, 
				destinoSalida.getCodigo(), 
				otroDestinoSalida, 
				numeroAutorizacion, 
				causaExterna.getCodigo(), 
				this.diagnosticoCausaMuerte.getAcronimo(), 
				this.diagnosticoCausaMuerte.getTipoCIE(), 
				this.diagnosticoDefinitivoPrincipal.getAcronimo(), 
				this.diagnosticoDefinitivoPrincipal.getTipoCIE(), 
				this.diagnosticoRelacionado_1.getAcronimo(), 
				this.diagnosticoRelacionado_1.getTipoCIE(), 
				this.diagnosticoRelacionado_2.getAcronimo(), 
				this.diagnosticoRelacionado_2.getTipoCIE(), 
				this.diagnosticoRelacionado_3.getAcronimo(), 
				this.diagnosticoRelacionado_3.getTipoCIE(), 
				this.medicoResponsable.getCodigoPersona(),
				this.diagnosticoComplicacion.getAcronimo(),this.diagnosticoComplicacion.getTipoCIE(),
				estado);
		} 
		catch (SQLException e) 
		{
			logger.error("Error en crearEgresoSinEvolucionTransaccional: "+e);
		}
		return resp;
	}
	
	/**
	 * Método que actualiza los datos del motivo de reversion de egreso que
	 * conciernen a epicrisis
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param deboMostrarMotivoReversionEpicrisis Boolean que indica si el motivo
	 * de reversion del egreso debe mostrarse en la epicrisis
	 * @param idEvolucion Evolucion en la que se debe mostrar el motivo en la
	 * epicrisis
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarInformacionMotivoReversionTransaccional (Connection con, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException
	{
		return egresoDao.actualizarInformacionMotivoReversionTransaccional(con, this.numeroCuenta, deboMostrarMotivoReversionEpicrisis, idEvolucion, estado);
	}
	
	/**
	 * Método que actualiza el boolean que define si el motivo de reversion va o no
	 * a la epicrisis
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param deboMostrarMotivoReversionEpicrisis Boolean que indica si el motivo
	 * de reversion del egreso debe mostrarse en la epicrisis
	 * @param idEvolucion Evolucion que actualizo el boolean de motivo de reversion
	 * de egreso
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int actualizarInformacionMotivoReversionSoloBooleanTransaccional (Connection con, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException
	{
		return egresoDao.actualizarInformacionMotivoReversionSoloBooleanTransaccional(con, deboMostrarMotivoReversionEpicrisis, idEvolucion, estado);
	}
	
	/**
	 * Este método se encarga de cargar el número de autorización . 
	 * NO está incluido en el método cargar, ya que estos datos se
	 * pueden cargar incluso ANTES de tener una entrada en la
	 * tabla 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoAdmision Código de la admisión de donde se quiere cargar el número de autorización
	 * @param tipoAdmision Caracter donde se define si el código dado corresponde
	 * a una admisión hospitalaria o de urgencias ('h' y 'u')
	 * @throws SQLException
	 */
	public void cargarNumeroAutorizacionAdmision(Connection con, int codigoAdmision, char tipoAdmision) throws SQLException
	{
		this.numeroAutorizacion=egresoDao.cargarNumeroAutorizacionAdmision(con, codigoAdmision, tipoAdmision);
	}

	/**
	 * Este método carga la causa externa presente en la evolución 
	 * de urgencias u hospitalizados
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta sobre la que se van 
	 * a hacer las búsquedas
	 * @param tipoAdmision Caracter donde se define si el código dado corresponde
	 * a una admisión hospitalaria o de urgencias ('h' y 'u')
	 * @throws SQLException
	 */	
	public void cargarCausaExternaUltimaValoracionUH (Connection con, int idCuenta, char tipoAdmision) throws SQLException
	{
		ResultSetDecorator rs=egresoDao.cargarCausaExternaUltimaValoracionUH(con, idCuenta, tipoAdmision);
		if (rs.next())
		{
			causaExterna.setCodigo(rs.getInt("codigoCausaExterna"));
			causaExterna.setValue(rs.getString("causaExterna"));
		}
		else
		{
			throw new SQLException ("No se encontró causa externa a pesar que el usuario especifico que este tenía (Egreso)");
		}
		
	}
	
	/**
	 * Método que completa un semi-egreso (Egreso creado automáticamente
	 * desde una valoración de urgencias para interconsulta y quirurgicos entre
	 * otros)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int completarSemiEgreso (Connection con) throws SQLException
	{
		return egresoDao.completarSemiEgreso(con, numeroCuenta, usuarioGrabaEgreso.getLoginUsuario(),
					diagnosticoDefinitivoPrincipal.getAcronimo(),diagnosticoDefinitivoPrincipal.getTipoCIE(),this.codigoTipoMonitoreo);
	}

	/**
	 * Método que reversa un semi-egreso (Egreso creado automáticamente
	 * desde una valoración de urgencias para interconsulta y quirurgicos entre
	 * otros)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int reversarSemiEgreso (Connection con) throws SQLException
	{
		return egresoDao.reversarSemiEgreso(con, numeroCuenta);
	}

	/**
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta sobre la que se 
	 * va a actualizar el egreso
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarPorReversionEgresoTransaccional (Connection con, int idCuenta, int idPersonaRealizaReversion, String estado) throws SQLException
	{
		return this.egresoDao.actualizarPorReversionEgresoTransaccional(con, idCuenta, idPersonaRealizaReversion, motivoReversionEgreso, estado);
	}
	/**
	 * Adición de Sebastián
	 * Método que pemrite consultar un semiEgreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean cargarSemiEgreso (Connection con,int idCuenta){		
		Collection semiEgreso= this.egresoDao.cargarSemiEgreso(con,idCuenta);
		Iterator iterador=semiEgreso.iterator();
		if(iterador.hasNext()){
			HashMap semiEgresoDB=(HashMap)iterador.next();
			this.setFechaEgreso(UtilidadFecha.conversionFormatoFechaAAp(semiEgresoDB.get("fecha_egreso")+""));
			this.setHoraEgreso(UtilidadFecha.convertirHoraACincoCaracteres(semiEgresoDB.get("hora_egreso")+""));
			this.usuarioGrabaEgreso = new UsuarioBasico();
			try 
			{	
				if(!UtilidadTexto.isEmpty(semiEgresoDB.get("usuario_responsable")+""))
					this.usuarioGrabaEgreso.cargarUsuarioBasico(con, semiEgresoDB.get("usuario_responsable")+"");
			} 
			catch (SQLException e) 
			{
				logger.error("Error cargando la informacion del usuario responsable: "+e);
			}
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Método que carga los datos propios de un egreso con excepción
	 * del código de admisión y la cama
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta en la que esta el egreso a
	 * cargar
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarEgresoGeneral (Connection con, int idCuenta) throws SQLException
	{
		boolean conexionInicialmenteNula=false;
		if (con==null)
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			conexionInicialmenteNula=true;
		}
		
		ResultSetDecorator rs=this.egresoDao.cargarEgresoGeneral(con, idCuenta);
		if (rs.next())
		{
			this.numeroCuenta=idCuenta;
			this.numeroEvolucion=rs.getInt("codigoEvolucion");
			this.fechaGrabacionEgreso=rs.getString("fechaGrabacion");
			this.horaGrabacionEgreso=rs.getString("horaGrabacion");
			this.fechaEgreso=rs.getString("fechaEvolucion");
			this.horaEgreso=rs.getString("horaEvolucion");
			this.numeroAutorizacion=rs.getString("numeroAutorizacion");
			this.estadoSalida=rs.getBoolean("estadoSalida");
			this.destinoSalida=new InfoDatos();
			//Aunque es en realidad un codigo, al hacer el set con un
			//entero estamos incurriendo en una conversión costosa
			this.destinoSalida.setAcronimo(rs.getString("codigoDestinoSalida"));
			this.destinoSalida.setValue(rs.getString("destinoSalida"));
			this.otroDestinoSalida=rs.getString("otroDestinoSalida");
			this.diagnosticoCausaMuerte=new Diagnostico();
			this.diagnosticoCausaMuerte.setAcronimo(rs.getString("acroDiagM"));
			this.diagnosticoCausaMuerte.setTipoCIE(rs.getInt("tipoCieDiagM"));
			this.diagnosticoCausaMuerte.setNombre(rs.getString("diagM"));
			this.causaExterna=new InfoDatos();
			this.causaExterna.setAcronimo(rs.getString("codigoCausaExterna"));
			this.causaExterna.setValue(rs.getString("causaExterna"));

			this.diagnosticoDefinitivoPrincipal=new Diagnostico();
			this.diagnosticoDefinitivoPrincipal.setAcronimo(rs.getString("acroDiagP"));
			this.diagnosticoDefinitivoPrincipal.setTipoCIE(rs.getInt("tipoCieDiagP"));
			this.diagnosticoDefinitivoPrincipal.setNombre(rs.getString("diagP"));

			this.diagnosticoRelacionado_1=new Diagnostico();
			this.diagnosticoRelacionado_1.setAcronimo(rs.getString("acroDiagR1"));
			this.diagnosticoRelacionado_1.setTipoCIE(rs.getInt("tipoCieDiagR1"));
			this.diagnosticoRelacionado_1.setNombre(rs.getString("diagR1"));
			
			this.diagnosticoRelacionado_2=new Diagnostico();
			this.diagnosticoRelacionado_2.setAcronimo(rs.getString("acroDiagR2"));
			this.diagnosticoRelacionado_2.setTipoCIE(rs.getInt("tipoCieDiagR2"));
			this.diagnosticoRelacionado_2.setNombre(rs.getString("diagR2"));
			
			this.diagnosticoRelacionado_3=new Diagnostico();
			this.diagnosticoRelacionado_3.setAcronimo(rs.getString("acroDiagR3"));
			this.diagnosticoRelacionado_3.setTipoCIE(rs.getInt("tipoCieDiagR3"));
			this.diagnosticoRelacionado_3.setNombre(rs.getString("diagR3"));

			this.medicoResponsable=new UsuarioBasico();
			String posibleTipoIdMedico=rs.getString("tipoIdMedico");
			if (posibleTipoIdMedico!=null)
			{
				medicoResponsable.cargarUsuarioBasico(con, rs.getString("tipoIdMedico"), rs.getString("numIdMedico"));
			}
			this.usuarioGrabaEgreso=new UsuarioBasico();
			String posibleUsuario=rs.getString("usuarioResponsable");
			if (posibleUsuario!=null)
			{
				usuarioGrabaEgreso.cargarUsuarioBasico(con, posibleUsuario);
			}
			
			///Cerramos la conexión si inialmente fué nula
			if (conexionInicialmenteNula&&con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
			return true;
		}
		else
		{
			//Cerramos la conexión si inialmente fué nula
			if (conexionInicialmenteNula&&con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
			return false;
		}
		
	}
	
	/**
	 * Método que cargar todos los datos necesarios para una reversión de egreso
	 * (Datos de la cama de la admisión de hospitalizados, fecha/hora egreso y medico
	 * responsable)
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta en la que esta el egreso a
	 * cargar
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarEgresoReversionEgreso (Connection con, int idCuenta) throws SQLException
	{
		ResultSetDecorator rs=this.egresoDao.cargarEgresoReversionEgreso(con, idCuenta);
		
		if (rs.next())
		{
			camaReversionEgreso=new Cama();
			camaReversionEgreso.setCodigoCama(rs.getString("codigoCama"));
			camaReversionEgreso.setNumeroCama(rs.getString("numeroCama"));
			camaReversionEgreso.setDescripcionCama(rs.getString("descripcionCama"));
			camaReversionEgreso.setPiso(rs.getString("piso"));
			camaReversionEgreso.setHabitacionCama(rs.getString("habitacion"));
			camaReversionEgreso.setTipoHabitacion(rs.getString("tipoHabitacion"));
			camaReversionEgreso.setCodigoCentroCostoCama(rs.getString("codigoCentroCostoCama"));
			camaReversionEgreso.setCentroCostoCama(rs.getString("centroCostoCama"));
			camaReversionEgreso.setCodigoTipoUsuarioCama(rs.getString("codigoTipoUsuarioCama"));
			camaReversionEgreso.setTipoUsuarioCama(rs.getString("tipoUsuarioCama"));
			camaReversionEgreso.setEstado(rs.getInt("estadoCama"));
			camaReversionEgreso.setNombreEstado(rs.getString("nombreEstadoCama"));
			this.medicoResponsable=new UsuarioBasico();
			this.medicoResponsable.cargarUsuarioBasico(con, rs.getString("tipoIdMedico"), rs.getString("numIdMedico"));
			this.fechaEgreso=rs.getString("fechaEgreso");
			this.horaEgreso=rs.getString("horaEgreso");
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Este método actualiza un egreso en la forma
	 * típica que un usuario necesita al momento de
	 * finalizar el egreso (Actualiza las fechas de egreso
	 * grabación, número de autorización y login del 
	 * usuario) - Toma todos los datos del objeto
	 * @return
	 */
	public int modificarEgresoUsuarioFinalizar (Connection con, String estado) throws SQLException
	{
		return this.egresoDao.modificarEgresoUsuarioFinalizarTransaccional(con, numeroCuenta, fechaEgreso, horaEgreso, fechaGrabacionEgreso, horaGrabacionEgreso, numeroAutorizacion, this.usuarioGrabaEgreso.getLoginUsuario(), estado);
	}
	
	/**
	 * Adición sebastián
	 * Método que carga los datos básicos de una reversión de un egreso
	 * @param con
	 * @param idCuenta
	 * @return Colección con la fecha y hora del egreso (apenas tiene esos campos)
	 */
	public boolean cargarReversionEgreso(Connection con){
		//Se carga la colección desde la base de datos
		Collection reversion=this.egresoDao.cargarReversionEgreso(con,this.getNumeroCuenta());
		//se verifica que hayan resultados
		if(reversion!=null){
			Iterator iterador=reversion.iterator();
			//se verifica que el iterador tenga datos
			if(iterador.hasNext()){
				HashMap reversionDB=(HashMap)iterador.next();
				//se cargan los resultados al mundo
				this.setFechaReversion(UtilidadFecha.conversionFormatoFechaAAp(reversionDB.get("fecha_reversion")+""));
				this.setHoraReversion(UtilidadFecha.convertirHoraACincoCaracteres(reversionDB.get("hora_reversion")+""));
				return true;
			}
			else{
					//error en el iterador
					return false;
			}
		}
		else{
			//error en la consulta SQL
			return false;
		}
		
	}
	
	/**
	 * Carga la fecha y la hora de egreso en formato BD
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean cargarFechaHoraEgreso(Connection con, int idCuenta)
	{
	    boolean respuesta=false;
	    try
	    {
		    ResultSetDecorator rs= egresoDao.cargarFechaHoraEgreso(con, idCuenta);
		    if(rs.next())
		    {
		        this.setFechaEgreso(rs.getString("fecha"));
		        this.setHoraEgreso(rs.getString("hora"));
		       respuesta=true;
		    }
	    }
	    catch( SQLException e)
	    {
	        return false;
	    }
	    return respuesta;
	}
	
	
	/**
	 * Obtiene la causa externa de este egreso.
	 * @return la causa externa de este egreso
	 */
	public InfoDatos getCausaExterna() {
		return causaExterna;
	}

	/**
	 * Obtiene el destino de salida de este egreso.
	 * @return el destino de salida de este egreso
	 */
	public InfoDatos getDestinoSalida() {
		return destinoSalida;
	}

	/**
	 * Obtiene el diagnóstico de la causa de muerte de este egreso.
	 * @return el diagnóstico de la causa de muerte de este egreso
	 */
	public Diagnostico getDiagnosticoCausaMuerte() {
		return diagnosticoCausaMuerte;
	}

	/**
	 * Obtiene el diagnóstico definitivo principal de este egreso.
	 * @return el diagnóstico definitivo principal de este egreso
	 */
	public Diagnostico getDiagnosticoDefinitivoPrincipal() {
		return diagnosticoDefinitivoPrincipal;
	}

	/**
	 * Obtiene el primer diagnóstico relacionado de este egreso.
	 * @return el primer diagnóstico relacionado de este egreso
	 */
	public Diagnostico getDiagnosticoRelacionado_1() {
		return diagnosticoRelacionado_1;
	}

	/**
	 * Obtiene el segundo diagnóstico relacionado de este egreso.
	 * @return el segundo diagnóstico relacionado de este egreso
	 */
	public Diagnostico getDiagnosticoRelacionado_2() {
		return diagnosticoRelacionado_2;
	}

	/**
	 * Obtiene el tercer diagnóstico relacionado de este egreso.
	 * @return el tercer diagnóstico relacionado de este egreso
	 */
	public Diagnostico getDiagnosticoRelacionado_3() {
		return diagnosticoRelacionado_3;
	}

	/**
	 * Retorna el estado de salida de este egreso.
	 * @return <b>true</b> si el paciente salió vivo, <b>false</b> si no
	 */
	public boolean isEstadoSalida() {
		return estadoSalida;
	}

	/**
	 * Retorna la fecha de grabación de este egreso.
	 * @return la fecha de grabación de este egreso
	 */
	public String getFechaGrabacionEgreso() {
		return fechaGrabacionEgreso;
	}

	/**
	 * Retorna la hora de grabación de este egreso.
	 * @return la hora de grabación de este egreso
	 */
	public String getHoraGrabacionEgreso() {
		return horaGrabacionEgreso;
	}

	/**
	 * Retorna el número de admisión asociado a este egreso.
	 * @return el número de admisión asociado a este egreso
	 */
	public int getNumeroAdmision() {
		return numeroAdmision;
	}

	/**
	 * Retorna el número de autorización asociado a este egreso.
	 * @return el número de autorización asociado a este egreso
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * Obtiene el destino de salida del paciente luego de este egreso.
	 * @return el destino de salida del paciente luego de este egreso
	 */
	public String getOtroDestinoSalida() {
		return otroDestinoSalida;
	}

	/**
	 * Obtiene la última evolución asociada a este egreso.
	 * @return la última evolución asociada a este egreso
	 */
	public EvolucionInterface getUltimaEvolucion() {
		return ultimaEvolucion;
	}

	/**
	 * Obtiene el <code>UsuarioBasico</code> que graba este egreso.
	 * @return el <code>UsuarioBasico</code> que graba este egreso
	 */
	public UsuarioBasico getUsuarioGrabaEgreso() {
		return usuarioGrabaEgreso;
	}

	/**
	 * Establece la causa externa de este egreso.
	 * @param causaExterna la causa externa a establecer
	 */
	public void setCausaExterna(InfoDatos causaExterna) {
		this.causaExterna = causaExterna;
	}

	/**
	 * Establece el destino de salida de este egreso.
	 * @param destinoSalida el destino de salida a establecer
	 */
	public void setDestinoSalida(InfoDatos destinoSalida) {
		this.destinoSalida = destinoSalida;
	}

	/**
	 * Establece el diagnóstico de la causa de muerte.
	 * @param diagnosticoCausaMuerte el diagnóstico de la cuasa de muerte a establecer
	 */
	public void setDiagnosticoCausaMuerte(Diagnostico diagnosticoCausaMuerte) {
		this.diagnosticoCausaMuerte = diagnosticoCausaMuerte;
	}

	/**
	 * Establece el diagnóstico definitivo principal de este egreso.
	 * @param diagnosticoDefinitivoPrincipal el diagnóstico definitivo principal a establecer
	 */
	public void setDiagnosticoDefinitivoPrincipal(Diagnostico diagnosticoDefinitivoPrincipal) {
		this.diagnosticoDefinitivoPrincipal = diagnosticoDefinitivoPrincipal;
	}

	/**
	 * Establece el primer diagnóstico relacionado de este egreso.
	 * @param diagnosticoRelacionado_1 el primer diagnóstico relacionado de este egreso a establecer.
	 */
	public void setDiagnosticoRelacionado_1(Diagnostico diagnosticoRelacionado_1) {
		this.diagnosticoRelacionado_1 = diagnosticoRelacionado_1;
	}

	/**
	 * Establece el segundo diagnóstico relacionado de este egreso.
	 * @param diagnosticoRelacionado_1 el segundo diagnóstico relacionado de este egreso a establecer.
	 */
	public void setDiagnosticoRelacionado_2(Diagnostico diagnosticoRelacionado_2) {
		this.diagnosticoRelacionado_2 = diagnosticoRelacionado_2;
	}

	/**
	 * Establece el tercer diagnóstico relacionado de este egreso.
	 * @param diagnosticoRelacionado_1 el tercer diagnóstico relacionado de este egreso a establecer.
	 */
	public void setDiagnosticoRelacionado_3(Diagnostico diagnosticoRelacionado_3) {
		this.diagnosticoRelacionado_3 = diagnosticoRelacionado_3;
	}

	/**
	 * Establece el estado de salida del paciente, en este egreso.
	 * @param estadoSalida el estado de salida del paciente a establecer
	 */
	public void setEstadoSalida(boolean estadoSalida) {
		this.estadoSalida = estadoSalida;
	}

	/**
	 * Establece la fecha de grabación de este egreso.
	 * @param fechaGrabacionEgreso la fecha de grabación a establecer
	 */
	public void setFechaGrabacionEgreso(String fechaGrabacionEgreso) {
		this.fechaGrabacionEgreso = fechaGrabacionEgreso;
	}

	/**
	 * Establece la hora de grabación de este egreso.
	 * @param horaGrabacionEgreso la hora de grabación a establecer
	 */
	public void setHoraGrabacionEgreso(String horaGrabacionEgreso) {
		this.horaGrabacionEgreso = horaGrabacionEgreso;
	}

	/**
	 * Establece el número de admisión asociado a este egreso.
	 * @param numeroAdmision el número de admisión a establecer
	 */
	public void setNumeroAdmision(int numeroAdmision) {
		this.numeroAdmision = numeroAdmision;
	}

	/**
	 * Establece el número de autorización asociado a este egreso.
	 * @param numeroAdmision el número de autorización a establecer
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	/**
	 * Establece el texto de otro destino de salida.
	 * @param otroDestinoSalida el otro destino de salida a establecer
	 */
	public void setOtroDestinoSalida(String otroDestinoSalida) {
		this.otroDestinoSalida = otroDestinoSalida;
	}

	/**
	 * Establece la última evolución asociada a este egreso.
	 * @param ultimaEvolucion la última evolución a establecer
	 */
	public void setUltimaEvolucion(EvolucionInterface ultimaEvolucion) {
		this.ultimaEvolucion = ultimaEvolucion;
	}

	/**
	 * Establece el usuario que graba este egreso.
	 * @param usuarioGrabaEgreso el usuario a establecer
	 */
	public void setUsuarioGrabaEgreso(UsuarioBasico usuarioGrabaEgreso) {
		this.usuarioGrabaEgreso = usuarioGrabaEgreso;
	}

	/**
	 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
	 */
	public void clean() 
	{
		this.numeroCuenta=0;
		this.numeroEvolucion=0;
		this.numeroAutorizacion="";
		this.estadoSalida=true;
		this.fechaEgreso="";
		this.horaEgreso="";
		this.fechaGrabacionEgreso="";
		this.horaGrabacionEgreso="";
		this.otroDestinoSalida="";
		this.causaExterna = new InfoDatos();
		this.destinoSalida = new InfoDatos();
		this.diagnosticoCausaMuerte = new Diagnostico();
		this.diagnosticoDefinitivoPrincipal = new Diagnostico();
		this.diagnosticoRelacionado_1 = new Diagnostico();
		this.diagnosticoRelacionado_2 = new Diagnostico();
		this.diagnosticoRelacionado_3 = new Diagnostico();
		this.diagnosticoComplicacion = new Diagnostico("",ConstantesBD.codigoNuncaValido);
		this.numeroAdmision = -1;
		this.ultimaEvolucion = null;
		this.usuarioGrabaEgreso = null;
		this.medicoResponsable=null;
		this.camaReversionEgreso=null;
		this.fechaReversion="";
		this.horaReversion="";
		this.codigoTipoMonitoreo = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Retorna el número de la cuenta de este egreso.
	 * @return el número de la cuenta de este egreso.
	 */
	public int getNumeroCuenta() {
		return numeroCuenta;
	}

	/**
	 * Establece el número de la cuenta de este egreso.
	 * @param i el número de la cuenta de este egreso.
	 */
	public void setNumeroCuenta(int i) {
		numeroCuenta = i;
	}

	/**
	 * Retorna la fecha de este egreso.
	 * @return la fecha de este egreso.
	 */
	public String getFechaEgresoFormatoAp() {
		return UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso);
	}

	/**
	 * Retorna la fecha de este egreso.
	 * @return la fecha de este egreso.
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}

	/**
	 * Retorna la hora de este egreso.
	 * @return la hora de este egreso.
	 */
	public String getHoraEgreso() {
		return horaEgreso;
	}

	/**
	 * Retorna la hora de este egreso en
	 * formato de cinco caracteres (hh:mm).
	 * @return la hora de este egreso.
	 */
	public String getHoraEgresoCincoCaracteres() 
	{
		return UtilidadFecha.convertirHoraACincoCaracteres(horaEgreso);
	}

	/**
	 * Establece la fecha de Egreso
	 * @param string  con la fecha de Egreso
	 */
	public void setFechaEgreso(String string) {
		fechaEgreso = string;
	}

	/**
	 * Establece la hora de Egreso
	 * @param string  con la hora de Egreso
	 */
	public void setHoraEgreso(String string) {
		horaEgreso = string;
	}

	/**
	 * Retorna el número de la evolución con la que
	 * que se dio la orden de salida que genero este
	 * egreso
	 * @return
	 */
	public int getNumeroEvolucion() {
		return numeroEvolucion;
	}

	/**
	 * Establece el número de la evolución con la que
	 * que se dio la orden de salida que genero este
	 * egreso
	 * @param i número de la evolución
	 * @return
	 */
	public void setNumeroEvolucion(int i) {
		numeroEvolucion = i;
	}

	/**
	 * Retorna el médico responsable de este
	 * egreso
	 * @return
	 */
	public UsuarioBasico getMedicoResponsable() {
		return medicoResponsable;
	}

	/**
	 * Establece el médico responsable de este
	 * egreso
	 * @param basico el médico responsable de este
	 * egreso
	 */
	public void setMedicoResponsable(UsuarioBasico basico) {
		medicoResponsable = basico;
	}

	/**
	 * Retorna la cama usada en la admisión hospitalaria, para
	 * mostrar en la reversión de egreso
	 * @return
	 */
	public Cama getCamaReversionEgreso() 
	{
		if (this.camaReversionEgreso==null)
		{
			return new Cama();
		}
		else
		{
			return camaReversionEgreso;
		}
	}

	/**
	 * Establece la cama usada en la admisión hospitalaria, para
	 * mostrar en la reversión de egreso
	 * @param cama la cama usada en la admisión hospitalaria, para
	 * mostrar en la reversión de egreso
	 */
	public void setCamaReversionEgreso(Cama cama) {
		camaReversionEgreso = cama;
	}

	/**
	 * Retorna el motivo por el cual se le hace reversión a
	 * este egreso
	 * @return
	 */
	public String getMotivoReversionEgreso() {
		return motivoReversionEgreso;
	}

	/**
	 * Estable el motivo por el cual se le hace reversión a
	 * este egreso
	 * @param string el motivo por el cual se le hace reversión a
	 * este egreso
	 */
	public void setMotivoReversionEgreso(String string) {
		motivoReversionEgreso = string;
	}

	/**
	 * @return
	 */
	public EgresoDao getEgresoDao() {
		return egresoDao;
	}
	/**
	 * @return the diagnosticoComplicacion
	 */
	public Diagnostico getDiagnosticoComplicacion() {
		return diagnosticoComplicacion;
	}
	/**
	 * @param diagnosticoComplicacion the diagnosticoComplicacion to set
	 */
	public void setDiagnosticoComplicacion(Diagnostico diagnosticoComplicacion) {
		this.diagnosticoComplicacion = diagnosticoComplicacion;
	}
	
	
	//*****************************************************************************************************	
	
	/**
	 * Genera el egreso completo
	 * @param Connection con
	 * @param int idCuenta
	 * @param String fechaEgreso
	 * @param String fechaGrabacionEgreso
	 * @param String horaEgreso
	 * @param String horaGrabacionEgreso
	 * @param UsuarioBasico medico
	 * @param PersonaBasica paciente
	 * */
	public  boolean generarEgresoCompleto(
			Connection con,			
			String fechaEgreso,
			String fechaGrabacionEgreso,
			String horaEgreso,
			String horaGrabacionEgreso,
			UsuarioBasico medico,
			PersonaBasica paciente,
			boolean validarCama)
	{			
		this.setNumeroCuenta(paciente.getCodigoCuenta());
		this.setFechaEgreso(UtilidadFecha.conversionFormatoFechaABD(fechaEgreso));
		this.setFechaGrabacionEgreso(UtilidadFecha.conversionFormatoFechaABD(fechaGrabacionEgreso));
		this.setHoraEgreso(horaEgreso);
		this.setHoraGrabacionEgreso(horaGrabacionEgreso);
		this.setUsuarioGrabaEgreso(medico);		
		
		try
		{
			int entero1=this.modificarEgresoUsuarioFinalizar(con, "empezar");
			
			int entero2=0, entero3=0;
			boolean actualizoTraslado= false;
			//Se verifica si la admision es de hospitalizacion o urgencias.
			//Las admisiones de urgencias sí tienen año de admision
			if (paciente.getAnioAdmision()<1)
			{
				
				AdmisionHospitalaria admisionHospitalaria=new AdmisionHospitalaria();
				admisionHospitalaria.init(System.getProperty("TIPOBD"));
				entero2=admisionHospitalaria.actualizarPorEgresoTransaccional(con, paciente.getCodigoAdmision(), ConstantesBD.continuarTransaccion, medico.getCodigoInstitucionInt());
				entero3=1;
				
				//Si la admision hospitalaria no es Hospital Día entonces se prosigue a liberar la cama
				if(!paciente.isHospitalDia())
				{
					if(validarCama)
					{
						int codigoCama=0;
						//se carga la ultima cama
						codigoCama = Utilidades.getUltimaCamaTraslado(con,paciente.getCodigoCuenta());
						
						//se cambia el estado de la cama				
						if(codigoCama!=0)
						{				
							Cama cama = new Cama();
							cama.cambiarEstadoCama(con,codigoCama+"",Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(medico.getCodigoInstitucionInt())+""));
							
							//ACTUALIZACION DE LA ESTANCIA DE LA CAMA
							TrasladoCamas objetoTraslado= new TrasladoCamas();
							actualizoTraslado=objetoTraslado.actualizarFechaHoraFinalizacion(con,paciente.getCodigoCuenta(),fechaEgreso,horaEgreso,ConstantesBD.finTransaccion,"");
							//////////////////////////////////////
						}
						else
							logger.info("NO TIENE CAMA");
					}
					else
					{
						actualizoTraslado = true;
						logger.info("No se tomo encuenta la Cama");
					}				
				}
				else
				{
					actualizoTraslado = true;
					UtilidadBD.finalizarTransaccion(con);
				}
	
			}
			else
			{
				AdmisionUrgencias admisionUrgencias=new AdmisionUrgencias();
				
				admisionUrgencias.init(System.getProperty("TIPOBD"));
				//y actualizar la admision hospitalaria con fecha y hora de observación
				//de la evolucion - La fecha de la ultima evolucion la tomamos del egreso
				entero2=admisionUrgencias.actualizarPorOrdenSalidaTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(),UtilidadFecha.conversionFormatoFechaABD(fechaEgreso),horaEgreso, "continuar");
				entero3=admisionUrgencias.actualizarPorEgresoTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(), "finalizar", medico.getCodigoInstitucionInt());
				actualizoTraslado=true;			
			}
						
			if (entero1<1||entero2<1||entero3<1|| !actualizoTraslado)
			{
				logger.info("No se genero el Egreso. Modificar Egreso >> "+entero1+" - "+entero3+" Admision >> "+entero2+" Cama >> "+actualizoTraslado);
				return false;
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}	
		
		return true;
	}
	/**
	 * @return the codigoTipoMonitoreo
	 */
	public int getCodigoTipoMonitoreo() {
		return codigoTipoMonitoreo;
	}
	/**
	 * @param codigoTipoMonitoreo the codigoTipoMonitoreo to set
	 */
	public void setCodigoTipoMonitoreo(int codigoTipoMonitoreo) {
		this.codigoTipoMonitoreo = codigoTipoMonitoreo;
	}
}