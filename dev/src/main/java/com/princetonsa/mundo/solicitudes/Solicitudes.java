/*
* @(#)Solicitudes.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudesDao;
import com.princetonsa.mundo.Especialidad;
import com.princetonsa.mundo.Especialidades;
import com.servinte.axioma.fwk.exception.IPSException;

/**
* Esta clase genera listados de solicitudes de órdenes médicas(Interconsulta, Procedimientos,
* Farmacia, etc.)
*
* @version 1.1, Feb 12, 2004
* @author <a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
* @author <a href="mailto:raul@princetonsa.com">Raúl Cancino</a>
*/
public class Solicitudes
{
	/**
	 * Manejo de la cuenta en resumen de atenciones
	 * Listar solo solicitudes referentes a la cuenta específica
	 */
	private int codigoCuenta=0;
	
	/**
	* Código del centro de costo solicitado sobre el cual se va a realizar el filtro del listado
	*/
	private int ii_codigoCentroCostoSolicitado;

	/**
	* Código del centro de costo solicitante sobre el cual se va a realizar el filtro del listado
	*/
	private int ii_codigoCentroCostoSolicitante;

	/**
	* Código del centro de costo del tratante de la cuenta. Se utiliza para discriminar las
	* solicitudes cuyo servicio no se encuentra parametrizado en el sistema
	*/
	private int ii_codigoCentroCostoTratante;


	/** Código del médico sobre el cual se va a realizar el filtro del listado */
	private int ii_codigoMedico;

	/** Código de la ocupación solicitada sobre la cual se va a realizar el filtro del listado */
	private int ii_codigoOcupacionSolicitada;

	/** Código del paciente sobre el cual se va a realizar el filtro del listado */
	private int ii_codigoPaciente;

	/** Código del tipo de solicitud sobre el cual se va a realizar el filtro del listado */
	private int ii_codigoTipoSolicitud;

	/** Estado de cuenta sobre el cual se va a realizar el filtro del listado  */
	private int ii_estadoCuenta;


	/** Estado de historia clínica sobre el cual se va a realizar el filtro del listado  */
	private int ii_estadoHistoriaClinica;

	/**
	* Códigos de la especialidades solicitadas sobre la cual se va a realizar el filtro del listado
	*/
	private int iia_codigoEspecialidadSolicitada[];

	/** El DAO usado por el objeto <code>Solicitudes</code> para acceder a la fuente de datos. */
	private SolicitudesDao isd_sd = null;

	/** Contenedor de solicitudes */
	private Vector iv_solicitudes;
	
	/**
	 * Cuenta la cual tiene asocio
	 */
	private int codigoCuentaasociada;
	
	/**
	 * Código del centro de costo que intenta acceder
	 * al listado
	 */
	private int codigoCentroCostoIntentaAcceso;
	
	/**
	 * el codigo del centro de atencion al que pertence las cuentas de las solicitudes.
	 */
	private int codigoCentroAtencionCuenta;
	
	
	/**
	 * indica si la consulta es por resumen de atenciones
	 */
	private boolean resumenAtenciones;
	
	private String fechaInicialFiltro;
	private String fechaFinalFiltro;
	private String centroCostoSolicitanteFiltro;
	private String estadoHCFiltro;
	private String tipoOrdenFiltro;
	
	/**
	 * 
	 */
	private String areaFiltro;
	
	/**
	 * 
	 */
	private String centroCostoSolicitadoFiltro;
	
	
	/**
	 * 
	 */
	private String pisoFiltro;
	
	/**
	 * 
	 */
	private String habitacionFiltro;
	
	/**
	 * 
	 */
	private String camaFiltro;
	
	
	/**
	 * 
	 *
	 */
	private boolean requierePortatilFiltro=false;
	
	/**
	 * Codigo del medico en sesion
	 */
	private String codigoMedicoEnSesion;
	
	public boolean isRequierePortatilFiltro() {
		return requierePortatilFiltro;
	}

	public void setRequierePortatilFiltro(boolean requierePortatilFiltro) {
		this.requierePortatilFiltro = requierePortatilFiltro;
	}

	public Solicitudes()
	{
		/** Inicializar los parámetros de filtro */
		setCodigoCentroCostoSolicitado(-1);
		setCodigoCentroCostoSolicitante(-1);
		setCodigoCentroCostoTratante(-1);
		setCodigosEspecialidadSolicitada(null);
		setCodigoMedico(-1);
		setCodigoOcupacionSolicitada(-1);
		setCodigoPaciente(-1);
		setCodigoTipoSolicitud(-1);
		setEstadoCuenta(-1);
		setEstadoHistoriaClinica(-1);
		setListadoSolicitudes(new Vector() );
		this.setCodigoCentroCostoIntentaAcceso(-1);
		this.fechaFinalFiltro="";
		this.fechaInicialFiltro="";
		this.centroCostoSolicitanteFiltro="";
		this.resumenAtenciones = false;
		this.codigoCentroAtencionCuenta=ConstantesBD.codigoNuncaValido;
		this.estadoHCFiltro="";
		this.tipoOrdenFiltro="";
		this.areaFiltro="";
		this.centroCostoSolicitadoFiltro="";
		this.pisoFiltro="";
		this.habitacionFiltro="";
		this.camaFiltro="";
		this.requierePortatilFiltro=false;
		this.codigoMedicoEnSesion="";
		init();
	}

	/** Inicializar el objeto de acceso a base de datos */
	public void init()
	{
		/* Obtener el DAO que encapsula las operaciones de BD de este objeto */
		if(isd_sd == null)
			isd_sd = DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getSolicitudesDao();
	}

	/** Obtiene el listado de solicitudes */
	public Collection getListadoSolicitudes()
	{
		
		return iv_solicitudes;
	}

	/** Obtiene el número de solicitudes contenida en la lista de solicitudes */
	public Integer getSize()
	{
		return new Integer(size() );
	}
	/**
	 * adición de Sebastián
	 * Método que obtiene la lista de solicitudes de acuerdo a los parámetros
	 * especificados en RevisionCuenta del módulo facturación
	 * @param con
	 * @param idCuenta
	 * @param contrato
	 * @param opcion: si es true se refiere a una cuenta, si es false se refiere a una subcuenta
	 * @return
	 */
	public HashMap listarSolicitudes(Connection con, int idCuenta, int contrato,boolean opcion) {
		return isd_sd.listarSolicitudes(con,idCuenta,contrato,opcion);
	}
	
	/**
	 * Adición Sebastián
	 * Método usado en la revisión de la cuenta para la búsqueda
	 * avanzada de solicitudes
	 * @param con
	 * @param orden
	 * @param tipo
	 * @param fecha
	 * @param estado
	 * @param contrato
	 * @param opcion
	 * @return
	 */
	public HashMap busquedaRevisionSolicitudes(Connection con,int idCuenta,HashMap parametros,int contrato,boolean opcion)
	{
		int orden=-1;
		int tipo=-1;
		String fecha="";
		int estado=-1;
		
		if((parametros.get("check_orden")+"").equals("true"))
		{
			orden=Integer.parseInt(parametros.get("orden")+"");
		}
		if((parametros.get("check_tipo")+"").equals("true"))
		{
			String[] aux;
			aux=(parametros.get("tipo")+"").split("-");
			tipo=Integer.parseInt(aux[0]);
		}
		if((parametros.get("check_fecha")+"").equals("true"))
		{
			String aux=parametros.get("fecha")+"";
			if(UtilidadFecha.validarFecha(aux))
				fecha=aux;
			
		}
		if((parametros.get("check_estado")+"").equals("true"))
		{
			String[] aux;
			aux=(parametros.get("estado")+"").split("-");
			estado=Integer.parseInt(aux[0]);
		}
		return isd_sd.busquedaRevisionSolicitudes(con,idCuenta,orden,tipo,fecha,estado,contrato,opcion);
	}
	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de servicios
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesServicios(Connection con,int solicitud){
		return isd_sd.listarSolicitudesServicios(con,solicitud);
	}
	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de medicamentos
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesMedicamentos(Connection con,int solicitud){
		return isd_sd.listarSolicitudesMedicamentos(con,solicitud);
	}
	/**
	 * adición de Sebastián
	 * Método que inserta un pool de medico por solicitud en el caso de que el médico pertenezca
	 * a varios pooles
	 * @param con
	 * @param solicitud
	 * @param pool
	 * @return
	*/	
	public int insertarPoolMedicoXSolicitud(Connection con, int solicitud, int pool) {
		return isd_sd.insertarPoolMedicoXSolicitud(con,solicitud,pool);
	}
	/**
	* Lista las solicitudes de acuerdo a los parámetros configurados
	* @param	ac_con Conexión a una fuente de datos
	* @return	Colección de solicitudes que cumplen los parámetros de filtro
	*/
	@SuppressWarnings("unused")
	public Collection listarSolicitudes(Connection ac_con, int institucion)throws Exception
	{
		HashMap	ldb_db;
		Date date;
		int			li_aux;
		Iterator	li_i;
		Object		lo_o;
		String		ls_aux;
		String		ls_apellidos;
		String		ls_nombres;
		String      fecha;
		Solicitud	ls_s;
		Vector		lv_v;
        lv_v = new Vector();
		li_i =
			isd_sd.listarSolicitudes(
				ac_con,
				ii_codigoCentroCostoSolicitado,
				ii_codigoCentroCostoSolicitante,
				ii_codigoCentroCostoTratante,
				ii_codigoMedico,
				ii_codigoOcupacionSolicitada,
				ii_codigoPaciente,
				ii_codigoTipoSolicitud,
				ii_estadoCuenta,
				ii_estadoHistoriaClinica,
				iia_codigoEspecialidadSolicitada,
				codigoCuentaasociada,
				codigoCentroCostoIntentaAcceso,
				codigoCuenta,
				institucion,
				resumenAtenciones,
				codigoCentroAtencionCuenta ,
				UtilidadFecha.conversionFormatoFechaABD(fechaInicialFiltro),
				UtilidadFecha.conversionFormatoFechaABD(fechaFinalFiltro),
				centroCostoSolicitanteFiltro,
				estadoHCFiltro,
				tipoOrdenFiltro,
				areaFiltro,
				centroCostoSolicitadoFiltro,
				pisoFiltro,
				habitacionFiltro,
				camaFiltro,
				requierePortatilFiltro,
				codigoMedicoEnSesion
			).iterator();

		while(li_i.hasNext() )
		{
			lo_o = li_i.next();

			if(lo_o instanceof HashMap)
			{
				ldb_db	= (HashMap)lo_o;
				ls_s	= new Solicitud();

				ls_s.setNombrePaciente(""+ldb_db.get("nombrepaciente"));
				ls_s.setNombreMedico(""+ldb_db.get("nombremedicosolicita"));
				ls_s.setNombreMedicoResponde(""+ldb_db.get("nombremedicoresponde"));

				//***************FIN MEDICO QUE RESPONDE***************///
				
				//////////////////////////////////////////////////////////////////////////
				//portatil
				ls_s.setPortatil(ldb_db.get("portatil")+"");
				//////////////////////////////////////////////////////////////////////////
				
				ls_s.setCodigoCentroAtencionCuentaSol( ( Utilidades.convertirAEntero(ldb_db.get("codcentroatencion")+"") ) );
				ls_s.setNombreCentroAtencionCuentaSol( ldb_db.get("nomcentroatencion")+"" );
				ls_s.setSolPYP(UtilidadTexto.getBoolean(ldb_db.get("pyp") +""));
				ls_s.setCobrable( ( UtilidadTexto.getBoolean(ldb_db.get("cobrable")+"" )));
				ls_s.setCodigoCuenta( ( Utilidades.convertirAEntero(ldb_db.get("codigocuenta")+"") ) );
				ls_s.setCodigoPaciente( ( Utilidades.convertirAEntero(ldb_db.get("codigopaciente")+"") ) );
				ls_s.setHoraGrabacion( ""+ldb_db.get("horagrabacion") );
				ls_s.setHoraInterpretacion( ""+ldb_db.get("horainterpretacion") );
				ls_s.setHoraSolicitud( ""+ldb_db.get("horasolicitud") );
				ls_s.setInterpretacion( ""+ldb_db.get("interpretacion") );
				//ls_s.setNumeroAutorizacion( ""+ldb_db.get("numeroautorizacion") );
				ls_s.setNumeroSolicitud(  Utilidades.convertirAEntero(ldb_db.get("numerosolicitud")+"" ) );
				ls_s.setUrgente( UtilidadTexto.getBoolean( ldb_db.get("urgente") +"") );
				ls_s.setVaAEpicrisis( UtilidadTexto.getBoolean(ldb_db.get("vaepicrisis") +"") );
				ls_s.setCama(Utilidades.obtenerValor(ldb_db.get("cama")));
				ls_s.setIncluyeServiciosArticulos( UtilidadTexto.getBoolean(ldb_db.get("incluyeserviciosarticulos")+""));
				
				ls_s.setCodigoMedicoInterpretacion(
					 Utilidades.convertirAEntero(ldb_db.get("codigomedicointerpretacion") +"")
				);

				ls_s.setConsecutivoOrdenesMedicas(
						Utilidades.convertirAEntero(ldb_db.get("consecutivoordenesmedicas") +"")
					);

				ls_s.setCodigoMedicoSolicitante(Utilidades.convertirAEntero(ldb_db.get("codigomedicosolicitante") +""));

				ls_s.setFechaGrabacion(
					UtilidadFecha.conversionFormatoFechaAAp( ""+ldb_db.get("fechagrabacion") )
				);

				ls_s.setFechaInterpretacion(
					UtilidadFecha.conversionFormatoFechaAAp(
						""+ldb_db.get("fechainterpretacion")
					)
				);

				ls_s.setFechaRespuesta(
					UtilidadFecha.conversionFormatoFechaAAp( ""+ldb_db.get("fecharespuesta") )
				);
				
				ls_s.setHoraRespuesta(""+ldb_db.get("horarespuesta"));
                
				/**
				 * MT 6791, Mt 6825 Version Cambio 1.1.4
				 * @author leoquico
				 * @fecha 15/04/2013
				 */
							
				fecha = UtilidadFecha.conversionFormatoFechaAAp( ""+ldb_db.get("fechasolicitud") );
				ls_s.setFechaSolicitud(fecha);
				date = UtilidadFecha.conversionFormatoFechaHoraStringDate(fecha+" "+ldb_db.get("horasolicitud"));
				ls_s.setFechaSolicitudDate(date);
								
				ls_s.setCentroCostoSolicitado(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigocentrocostosolicitado") +""),
						""+ldb_db.get("nombrecentrocostosolicitado")
					)
				);

				ls_s.setCentroCostoSolicitante(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigocentrocostosolicitante") +""),
						""+ldb_db.get("nombrecentrocostosolicitante")
					)
				);

				ls_s.setEspecialidadSolicitante(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigoespecialidadsolicitante") +""),
						""+ldb_db.get("nombreespecialidadsolicitante")
					)
				);

				ls_s.setEstadoHistoriaClinica(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigoestadohistoriaclinica") +""),
						""+ldb_db.get("nombreestadohistoriaclinica")
					)
				);

				ls_s.setOcupacionSolicitado(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigoocupacionsolicitada") +""),
						""+ldb_db.get("nombreocupacionsolicitada")
					)
				);

				ls_s.setTipoSolicitud(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigotiposolicitud") +""),
						""+ldb_db.get("nombretiposolicitud")
					)
				);

				li_aux = Utilidades.convertirAEntero(ldb_db.get("codigoespecialidadsolicitada") +"");

				if(li_aux > 0)
				{
					ls_s.setEspecialidadSolicitada(
						new InfoDatosInt(
							li_aux,
							""+ldb_db.get("nombreespecialidadsolicitada")
						)
					);
				}
				else
					ls_s.setEspecialidadSolicitada(null);

				lv_v.add(ls_s);
			}
		}
		
		if(lv_v.size() > 0)
			iv_solicitudes = new Vector(lv_v);

		return (Collection)iv_solicitudes;
	}
	
	
	/**
	 * Método implementado para insertar el Log de modificacion de tarifas
	 * en la funcionalidad de Revisión de la Cuenta
	 * @param con
	 * @param cuenta
	 * @param solicitud
	 * @param servicio
	 * @param articulo
	 * @param tipoCargo
	 * @param tarifaInicial
	 * @param tarifaFinal
	 * @param usuario
	 * @param institucion
	 * @param estado
	 * @return
	 */
	public int insertarLogRevisionCuenta(Connection con,int cuenta,int solicitud,int servicio,int articulo,int tipoCargo,
			double tarifaInicial,double tarifaFinal,String usuario,int institucion,String estado)
	{
		return isd_sd.insertarLogRevisionCuenta(con,cuenta,solicitud,servicio,articulo,
			tipoCargo,tarifaInicial,tarifaFinal,usuario,institucion,estado);
	}
	
	/**
	 * Mètodo que consulta los procedimientos e interconsultas asociados al paciente, para
	 * mostrarlos en la ventana al ubicarse el usuario sobre la flecha de detalle en el listado de 
	 * solicitudes  de interconsultas y procedimientos
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public Collection consultarInterconsultasProcedimientos (Connection con, int codigoCuenta)
	{
		return isd_sd.consultarInterconsultasProcedimientos(con,codigoCuenta);
	}
	
	/** Obtiene el iterador sobre la lista de solicitudes */
	public Iterator getIterador()
	{
		return iv_solicitudes.iterator();
	}

	/**
	* Establece el código del centro de costo solicitado sobre el cual se va a realizar el filtro
	* del listado
	*/
	public void setCodigoCentroCostoSolicitado(int ai_codigoCentroCostoSolicitado)
	{
		ii_codigoCentroCostoSolicitado =
			(ai_codigoCentroCostoSolicitado < 0) ? -1 : ai_codigoCentroCostoSolicitado;
	}

	/**
	* Establece el código del centro de costo solicitante sobre el cual se va a realizar el filtro
	* del listado
	*/
	public void setCodigoCentroCostoSolicitante(int ai_codigoCentroCostoSolicitante)
	{
		ii_codigoCentroCostoSolicitante =
			(ai_codigoCentroCostoSolicitante < 0) ? -1 : ai_codigoCentroCostoSolicitante;
	}

	/**
	* Establece el código del centro de costo del tratantede la cuenta, sobre el cual se va a
	* realizar el filtro del listado
	*/
	public void setCodigoCentroCostoTratante(int ai_codigoCentroCostoTratante)
	{
		ii_codigoCentroCostoTratante =
			(ai_codigoCentroCostoTratante < 0) ? -1 : ai_codigoCentroCostoTratante;
	}

	/**
	* Establece el código de la especialidad solicitada sobre la cual se va a realizar el filtro del
	* listado
	*/
	public void setCodigosEspecialidadSolicitada(Especialidades ae_e)
	{
		Collection	lc_e;
		Iterator	lI_I;

		lc_e = (ae_e != null) ? ae_e.getListadoEspecialidades() : null;
		lI_I = (lc_e != null) ? lc_e.iterator() : null;

		if(lI_I != null)
		{
			Especialidad	le_e;
			int				li_codigo;
			Object			lo_o;

			iia_codigoEspecialidadSolicitada = new int[lc_e.size()];

			for(int li_i = 0; lI_I.hasNext(); li_i++)
			{
				li_codigo	= ConstantesBD.codigoEspecialidadMedicaTodos;
				lo_o		= lI_I.next();

				if(lo_o instanceof Especialidad)
				{
					try
					{
						le_e = (Especialidad)lo_o;

						if(le_e.isActivaSistema() )
							li_codigo = Integer.valueOf(le_e.getCodigoEspecialidad() ).intValue();
					}
					catch(NumberFormatException lnfe_e)
					{
					}
				}

				iia_codigoEspecialidadSolicitada[li_i] = li_codigo;
			}
		}
		else
			iia_codigoEspecialidadSolicitada = null;
	}

	/** Establece el código del médico sobre el cual se va a realizar el filtro del listado */
	public void setCodigoMedico(int ai_codigoMedico)
	{
		ii_codigoMedico = (ai_codigoMedico < 0) ? -1 : ai_codigoMedico;
	}

	/**
	* Establece el código de la ocupación solicitada sobre la cual se va a realizar el filtro del
	* listado
	*/
	public void setCodigoOcupacionSolicitada(int ai_codigoOcupacionSolicitada)
	{
		ii_codigoOcupacionSolicitada =
			(ai_codigoOcupacionSolicitada < 0) ? -1 : ai_codigoOcupacionSolicitada;
	}

	/** Establece el código del paciente sobre el cual se va a realizar el filtro del listado */
	public void setCodigoPaciente(int ai_codigoPaciente)
	{
		ii_codigoPaciente = (ai_codigoPaciente < 0) ? -1 : ai_codigoPaciente;
	}

	/**
	* Establece el código del tipo de solicitud sobre el cual se va a realizar el filtro del listado
	*/
	public void setCodigoTipoSolicitud(int ai_codigoTipoSolicitud)
	{
		ii_codigoTipoSolicitud = (ai_codigoTipoSolicitud < 0) ? -1 : ai_codigoTipoSolicitud;
    }

	/** Establece el estado de cuenta sobre el cual se va a realizar el filtro del listado */
	public void setEstadoCuenta(int ai_estadoCuenta)
	{
		ii_estadoCuenta = (ai_estadoCuenta < 0) ? -1 : ai_estadoCuenta;
	}
	
	public int getEstadoCuenta()
	{
		return this.ii_estadoCuenta;
	}

	/**
	*	Establece el estado de historia clínica sobre el cual se va a realizar el filtro del listado
	*/
	public void setEstadoHistoriaClinica(int ai_estadoHistoriaClinica)
	{
		ii_estadoHistoriaClinica = (ai_estadoHistoriaClinica < 0) ? -1 : ai_estadoHistoriaClinica;
	}

	/** Establece el listado de solicitudes */
	public void setListadoSolicitudes(Collection lc_c)
	{
		iv_solicitudes = new Vector(lc_c);
	}

	/** Obtiene el número de solicitudes contenida en la lista de solicitudes */
	public int size()
	{
		return iv_solicitudes.size();
	}
	/**
	 * @return
	 */
	public int getCodigoCuentaasociada()
	{
		return codigoCuentaasociada;
	}

	/**
	 * @param i
	 */
	public void setCodigoCuentaasociada(int i)
	{
		codigoCuentaasociada= i;
	}

	/**
	 * @param i
	 */
	public void setCodigoCentroCostoIntentaAcceso(int i) {
		codigoCentroCostoIntentaAcceso = i;
	}

	/**
	 * @return
	 */
	public int getCodigoCuenta()
	{
		return codigoCuenta;
	}

	/**
	 * @param i
	 */
	public void setCodigoCuenta(int i)
	{
		codigoCuenta= i;
	}

	/**
	 * @return Returns the resumenAtenciones.
	 */
	public boolean isResumenAtenciones() {
		return resumenAtenciones;
	}
	/**
	 * @param resumenAtenciones The resumenAtenciones to set.
	 */
	public void setResumenAtenciones(boolean resumenAtenciones) {
		this.resumenAtenciones = resumenAtenciones;
	}

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param codigoContrato
	 * @return
	 */
	public static HashMap obtenerSolicitudesServicioIndicadorCapitado(Connection con, String idCuenta, int codigoContrato) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getSolicitudesDao().obtenerSolicitudesServicioIndicadorCapitado(con,idCuenta,codigoContrato);
	}

	public int getCodigoCentroAtencionCuenta() {
		return codigoCentroAtencionCuenta;
	}

	public void setCodigoCentroAtencionCuenta(int codigoCentroAtencionCuenta) {
		this.codigoCentroAtencionCuenta = codigoCentroAtencionCuenta;
	}

	public String getCentroCostoSolicitanteFiltro() {
		return centroCostoSolicitanteFiltro;
	}

	public void setCentroCostoSolicitanteFiltro(String centroCostoSolicitanteFiltro) {
		this.centroCostoSolicitanteFiltro = centroCostoSolicitanteFiltro;
	}

	public String getFechaFinalFiltro() {
		return fechaFinalFiltro;
	}

	public void setFechaFinalFiltro(String fechaFinalFiltro) {
		this.fechaFinalFiltro = fechaFinalFiltro;
	}

	public String getFechaInicialFiltro() {
		return fechaInicialFiltro;
	}

	public void setFechaInicialFiltro(String fechaInicialFiltro) {
		this.fechaInicialFiltro = fechaInicialFiltro;
	}

	public Collection listarSolicitudesProcedimientos(Connection con, int institucion) throws Exception
	{
		HashMap	ldb_db;
		int			li_aux;
		Iterator	li_i;
		Object		lo_o;
		String		ls_aux;
		String		ls_apellidos;
		String		ls_nombres;
		Solicitud	ls_s;
		Vector		lv_v;
        lv_v = new Vector();
        	li_i =
				isd_sd.listarSolicitudes(
					con,
					ii_codigoCentroCostoSolicitado,
					ii_codigoCentroCostoSolicitante,
					ii_codigoCentroCostoTratante,
					ii_codigoMedico,
					ii_codigoOcupacionSolicitada,
					ii_codigoPaciente,
					ii_codigoTipoSolicitud,
					ii_estadoCuenta,
					ii_estadoHistoriaClinica,
					iia_codigoEspecialidadSolicitada,
					codigoCuentaasociada,
					codigoCentroCostoIntentaAcceso,
					codigoCuenta,
					institucion,
					resumenAtenciones,
					codigoCentroAtencionCuenta ,
					UtilidadFecha.conversionFormatoFechaABD(fechaInicialFiltro),
					UtilidadFecha.conversionFormatoFechaABD(fechaFinalFiltro),
					centroCostoSolicitanteFiltro,
					estadoHCFiltro,
					tipoOrdenFiltro,
					areaFiltro,
					centroCostoSolicitadoFiltro,
					pisoFiltro,
					habitacionFiltro,
					camaFiltro,
					requierePortatilFiltro,
					codigoMedicoEnSesion
				).iterator();


		while(li_i.hasNext() )
		{
			lo_o = li_i.next();

			if(lo_o instanceof HashMap)
			{
				ldb_db	= (HashMap)lo_o;
				ls_s	= new Solicitud();

				/* Obtener los nombres y apellidos del paciente */
				ls_apellidos	= "";
				ls_nombres		= "";

				if( (ls_aux = ""+ldb_db.get("primerapellidopaciente") ) != null)
					ls_apellidos = ls_aux.trim();

				if( (ls_aux = ""+ldb_db.get("segundoapellidopaciente") ) != null)
					ls_apellidos = (ls_apellidos + " " + ls_aux).trim();

				if( (ls_aux = ""+ldb_db.get("primernombrepaciente") ) != null)
					ls_nombres = ls_aux.trim();

				if( (ls_aux = ""+ldb_db.get("segundonombrepaciente") ) != null)
					ls_nombres = (ls_nombres + " " + ls_aux).trim();

				ls_s.setApellidoPaciente(ls_apellidos);
				ls_s.setNombrePaciente(ls_nombres);

				/* Obtener los nombres y apellidos del medico solicitante */
				ls_apellidos	= "";
				ls_nombres		= "";

				if( (ls_aux = ""+ldb_db.get("primerapellidomedicosolicitante") ) != null)
					ls_apellidos = ls_aux.trim();

				if( (ls_aux = ""+ldb_db.get("segundoapellidomedicosolicitante") ) != null)
					ls_apellidos = (ls_apellidos + " " + ls_aux).trim();

				if( (ls_aux = ""+ldb_db.get("primernombremedicosolicitante") ) != null)
					ls_nombres = ls_aux.trim();

				if( (ls_aux = ""+ldb_db.get("segundonombremedicosolicitante") ) != null)
					ls_nombres = (ls_nombres + " " + ls_aux).trim();
				ls_s.setNombreMedico(ls_nombres);
				ls_s.setApellidoMedico(ls_apellidos);
				
				//***************MEDICO QUE RESPONDE***************///
				
				/* Obtener los nombres y apellidos del medico solicitante */
				ls_apellidos	= "";
				ls_nombres		= "";

				if( (ls_aux = ""+ldb_db.get("primerapellidomedicoresponde") ) != null)
					ls_apellidos = ls_aux.trim();

				if( (ls_aux = ""+ldb_db.get("segundoapellidomedicoresponde") ) != null)
					ls_apellidos = (ls_apellidos + " " + ls_aux).trim();

				if( (ls_aux = ""+ldb_db.get("primernombremedicoresponde") ) != null)
					ls_nombres = ls_aux.trim();

				if( (ls_aux = ""+ldb_db.get("segundonombremedicoresponde") ) != null)
					ls_nombres = (ls_nombres + " " + ls_aux).trim();
				ls_s.setNombreMedicoResponde(ls_nombres);
				ls_s.setApellidoMedicoResponde(ls_apellidos);

				//***************FIN MEDICO QUE RESPONDE***************///
				
				ls_s.setCodigoCentroAtencionCuentaSol( Utilidades.convertirAEntero(ldb_db.get("codcentroatencion") +"") );
				ls_s.setNombreCentroAtencionCuentaSol( ""+ldb_db.get("nomcentroatencion") );
				ls_s.setSolPYP(UtilidadTexto.getBoolean(ldb_db.get("pyp") +""));
				ls_s.setCobrable( UtilidadTexto.getBoolean( ldb_db.get("cobrable") +"") );
				ls_s.setCodigoCuenta( Utilidades.convertirAEntero(ldb_db.get("codigocuenta") +"") );
				ls_s.setCodigoPaciente( Utilidades.convertirAEntero(ldb_db.get("codigopaciente") +"") );
				ls_s.setHoraGrabacion( ""+ldb_db.get("horagrabacion") );
				ls_s.setHoraInterpretacion( ""+ldb_db.get("horainterpretacion") );
				ls_s.setHoraSolicitud( ""+ldb_db.get("horasolicitud") );
				ls_s.setInterpretacion( ""+ldb_db.get("interpretacion") );
				//ls_s.setNumeroAutorizacion( ""+ldb_db.get("numeroautorizacion") );
				ls_s.setNumeroSolicitud( Utilidades.convertirAEntero(ldb_db.get("numerosolicitud") +"") );
				ls_s.setUrgente( UtilidadTexto.getBoolean( ldb_db.get("urgente") +"") );
				ls_s.setVaAEpicrisis( UtilidadTexto.getBoolean( ldb_db.get("vaepicrisis") +"") );

				ls_s.setCodigoMedicoInterpretacion(
						Utilidades.convertirAEntero(ldb_db.get("codigomedicointerpretacion") +"")
				);

				ls_s.setConsecutivoOrdenesMedicas(
						Utilidades.convertirAEntero(ldb_db.get("consecutivoordenesmedicas") +"")
					);

				ls_s.setCodigoMedicoSolicitante(
						Utilidades.convertirAEntero(ldb_db.get("codigomedicosolicitante") +"")
				);

				/*ls_s.setFechaGrabacion(
					UtilidadFecha.conversionFormatoFechaAAp( ""+ldb_db.get("fechagrabacion") )
				);*/
				ls_s.setFechaGrabacion(""+ldb_db.get("fechagrabacion"));

				/*ls_s.setFechaInterpretacion(
					UtilidadFecha.conversionFormatoFechaAAp(
						""+ldb_db.get("fechainterpretacion")
					)
				);*/
				ls_s.setFechaInterpretacion(""+ldb_db.get("fechainterpretacion"));

				/*ls_s.setFechaRespuesta(
					UtilidadFecha.conversionFormatoFechaAAp( ""+ldb_db.get("fecharespuesta") )
				);*/
				ls_s.setFechaRespuesta(""+ldb_db.get("fecharespuesta"));
				
				ls_s.setHoraRespuesta(""+ldb_db.get("horarespuesta"));

				/*ls_s.setFechaSolicitud(
					UtilidadFecha.conversionFormatoFechaAAp( ""+ldb_db.get("fechasolicitud") )
				);*/
				ls_s.setFechaSolicitud(""+ldb_db.get("fechasolicitud"));

				ls_s.setCentroCostoSolicitado(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigocentrocostosolicitado") +""),
						""+ldb_db.get("nombrecentrocostosolicitado")
					)
				);

				ls_s.setCentroCostoSolicitante(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigocentrocostosolicitante") +""),
						""+ldb_db.get("nombrecentrocostosolicitante")
					)
				);

				ls_s.setEspecialidadSolicitante(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigoespecialidadsolicitante") +""),
						""+ldb_db.get("nombreespecialidadsolicitante")
					)
				);

				ls_s.setEstadoHistoriaClinica(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigoestadohistoriaclinica") +""),
						""+ldb_db.get("nombreestadohistoriaclinica")
					)
				);

				ls_s.setOcupacionSolicitado(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigoocupacionsolicitada") +""),
						""+ldb_db.get("nombreocupacionsolicitada")
					)
				);

				ls_s.setTipoSolicitud(
					new InfoDatosInt(
							Utilidades.convertirAEntero(ldb_db.get("codigotiposolicitud") +""),
						""+ldb_db.get("nombretiposolicitud")
					)
				);

				li_aux = Utilidades.convertirAEntero(ldb_db.get("codigoespecialidadsolicitada") +"");

				if(li_aux > 0)
				{
					ls_s.setEspecialidadSolicitada(
						new InfoDatosInt(
							li_aux,
							ldb_db.get("nombreespecialidadsolicitada")+""
						)
					);
				}
				else
					ls_s.setEspecialidadSolicitada(null);

				lv_v.add(ls_s);
			}
		}
		
		if(lv_v.size() > 0)
			iv_solicitudes = new Vector(lv_v);

		return (Collection)iv_solicitudes;
	}

	/**
	 * @return the estadoHC
	 */
	public String getEstadoHCFiltro() {
		return estadoHCFiltro;
	}

	/**
	 * @param estadoHC the estadoHC to set
	 */
	public void setEstadoHCFiltro(String estadoHCFiltro) {
		this.estadoHCFiltro = estadoHCFiltro;
	}

	/**
	 * @return the tipoOrden
	 */
	public String getTipoOrdenFiltro() {
		return tipoOrdenFiltro;
	}

	/**
	 * @param tipoOrden the tipoOrden to set
	 */
	public void setTipoOrdenFiltro(String tipoOrdenFiltro) {
		this.tipoOrdenFiltro = tipoOrdenFiltro;
	}

	public String getCentroCostoSolicitadoFiltro() {
		return centroCostoSolicitadoFiltro;
	}

	public void setCentroCostoSolicitadoFiltro(String centroCostoSolicitadoFiltro) {
		this.centroCostoSolicitadoFiltro = centroCostoSolicitadoFiltro;
	}
	
	public String getAreaFiltro() {
		return areaFiltro;
	}

	public void setAreaFiltro(String areaFiltro) {
		this.areaFiltro = areaFiltro;
	}

	public String getCamaFiltro() {
		return camaFiltro;
	}

	public void setCamaFiltro(String camaFiltro) {
		this.camaFiltro = camaFiltro;
	}

	public String getHabitacionFiltro() {
		return habitacionFiltro;
	}

	public void setHabitacionFiltro(String habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}

	public String getPisoFiltro() {
		return pisoFiltro;
	}

	public void setPisoFiltro(String pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}
	
	/**
	 * @return the codigoMedicoEnSesion
	 */
	public String getCodigoMedicoEnSesion() {
		return codigoMedicoEnSesion;
	}

	/**
	 * @param codigoMedicoEnSesion the codigoMedicoEnSesion to set
	 */
	public void setCodigoMedicoEnSesion(String codigoMedicoEnSesion) {
		this.codigoMedicoEnSesion = codigoMedicoEnSesion;
	}

	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public static int obtenerViaIngresoSolicitud(Connection con, int solicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getSolicitudesDao().obtenerViaIngresoSolicitud(con,solicitud);
	}
	
	/**
	 * Actualiza los campos de filtro para la busqueda del listado de Solicitudes
	 * String cadenaFiltro
	 * */
	public void actualizarCadenaFiltroBusqueda(String cadenaFiltro)
	{
		String filtro [] = cadenaFiltro.split(ConstantesBD.separadorSplit);
		
		//se verifica que se hubieran pasado todos los parametros de la busqueda
		if(filtro.length >= 11)
		{
			this.setFechaInicialFiltro(filtro[0].toString());
			this.setFechaFinalFiltro(filtro[1].toString());
			this.setCentroCostoSolicitanteFiltro(filtro[2].toString());
			this.setEstadoHCFiltro(filtro[3].toString());
			this.setTipoOrdenFiltro(filtro[4].toString());
			this.setAreaFiltro(filtro[5].toString());
			this.setCentroCostoSolicitadoFiltro(filtro[6].toString());
			this.setPisoFiltro(filtro[7].toString());
			this.setHabitacionFiltro(filtro[8].toString());
			this.setCamaFiltro(filtro[9].toString());
			this.setRequierePortatilFiltro(UtilidadTexto.getBoolean(filtro[10].toString()));
		}		
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param estado
	 * @param con
	 * @return
	 */
	public static boolean modificarEstadosOrdenesSolicitud(int numeroSolicitud,
			int estado, Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getSolicitudesDao().modificarEstadosOrdenesSolicitud(numeroSolicitud, estado, con);	
	}

	
	
}