/*
 * @(#)EstadoCuenta.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.resumenAtenciones;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EstadoCuentaDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información del 
 * estado de cuenta, este objeto no tiene ninguna
 * estructura, su único objetivo es conservar la
 * arquitectura de la aplicación, llamando a la
 * fuente de datos para obtener la información
 * requerida por cada uno de los reportes que
 * componen el resumen de atenciones
 * 
 *	@version 1.0, 5/08/2004
 */
public class EstadoCuenta
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger=Logger.getLogger(EstadoCuenta.class);
	
	/**
	 * Verificar que se hallan cargodo los cargos totales
	 */
	private boolean yaCargue;
	
	/**
	 * Valor total de los cargos por parte del paciente
	 */
	private double valorTotalPaciente;
	
	/**
	 * Valor total de los cargos
	 */
	private double valorTotalCargos;
	
	/**
	 * Valor total de los cargos por parte del convenio
	 */
	private double valorTotalConvenio;
	
	/**
	 * Valor Total de los abonos
	 */
	private double valorTotalAbonos;
	
	/**
	 * Valor neto paciente
	 */
	private double valorNetoPaciente;
	
	/**
	 * Valor devolucion 
	 */
	private double valorDevolucionPaciente;
	
	/**
	 * Valor saldo monto autorizado (Solo aplica para convenios que tienen inf. aDic poliza)
	 */
	private double valorSaldoMontoAutorizado;
	
    /**
     * Constructor del objeto EstadoCuenta
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public EstadoCuenta()
    {
        this.init(System.getProperty("TIPOBD"));
        valorTotalPaciente=0;
        valorTotalCargos=0;
        valorTotalConvenio=0;
        valorTotalAbonos = 0;
        valorNetoPaciente = 0;
        valorDevolucionPaciente =0;
        valorSaldoMontoAutorizado = 0;
        
        yaCargue=false;
    }
    
    /**
	 * El DAO usado por el objeto <code>EstadoCuenta</code> 
	 * para acceder a la fuente de datos. 
	 */
	private EstadoCuentaDao estadoCuentaDao ;

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
			estadoCuentaDao = myFactory.getEstadoCuentaDao();
			wasInited = (estadoCuentaDao != null);
		}

		return wasInited;
	}

	/**
	 * Método que carga los datos necesarios para
	 * el estado de cuenta para todas las cuentas
	 * de un paciente dado
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código del paciente
	 * al que se desean revisar sus cuentas
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<HashMap<String, Object>>  cargarTodasCuentaPaciente (Connection con, int codigoPaciente) 
	{
	    return estadoCuentaDao.cargarTodasCuentaPaciente (con, codigoPaciente) ;
	}
	
	/**
	 * Metodo que obtiene los numeros de carnet de un paciente
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @return
	 */
	public String obtenerCarnet(Connection con, int convenio, int ingreso)
	{
		return estadoCuentaDao.obtenerCarnet(con, convenio, ingreso);
	}
	
	/**
	 * Método que consulta todos los convenios de un ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public  ArrayList<HashMap<String, Object>> cargarTodosConvenioIngreso (Connection con, String idIngreso) 
	{
		return estadoCuentaDao.cargarTodosConvenioIngreso(con, idIngreso);
	}
	/**
	  * Método que consulta las solicitudes de un convenio
	  * @param con
	  * @param idSubCuenta
	  * @return
	  */
	public HashMap<String, Object> cargarTodasSolicitudesSubCuenta (Connection con, String idIngreso,String codigoConvenio, String idSubCuenta)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", idIngreso);
		campos.put("codigoConvenio", codigoConvenio);
		campos.put("idSubCuenta", idSubCuenta);
		return estadoCuentaDao.cargarTodasSolicitudesSubCuenta(con, campos);
	}
	
	/**
	 * Método que realiza la busqueda avanzada de las solicitudes de una sub cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> buscarSolicitudesSubCuenta(
		Connection con,
		String idIngreso,
		String codigoConvenio,
		String idSubCuenta,
		String tipoSolicitud,
		String orden,
		String fechaOrden,
		String fechaGrabacion,
		String estadoMedico,
		String centroCostoSolicitante,
		String centroCostoSolicitado,
		String estadofacturacion)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		campos.put("codigoConvenio",codigoConvenio);
		campos.put("idSubCuenta",idSubCuenta);
		campos.put("tipoSolicitud",tipoSolicitud);
		campos.put("orden",orden);
		campos.put("fechaOrden",fechaOrden);
		campos.put("fechaGrabacion",fechaGrabacion);
		campos.put("estadoMedico",estadoMedico);
		campos.put("centroCostoSolicitante",centroCostoSolicitante);
		campos.put("centroCostoSolicitado",centroCostoSolicitado);
		campos.put("estadoFacturacion",estadofacturacion);
		return estadoCuentaDao.buscarSolicitudesSubCuenta(con, campos);
	}
	
	public HashMap calcularTotalesPorIngreso(Connection con, ArrayList convenios)
	{
		double totalCargos=0;
		double totalPaciente=0;
		double totalConvenio=0;
		HashMap<String, Object> mapa = new HashMap<String, Object> ();
		for(int i=0;i<convenios.size();i++)
		{
			HashMap<String, Object> mapaA = new HashMap<String, Object> ();
			mapaA=(HashMap)convenios.get(i);
			totalCargos=estadoCuentaDao.valorTotalCargos(con, mapaA.get("idSubCuenta").toString(), true);
			if(mapaA.get("codigoTipoRegimen").toString().equals(ConstantesBD.codigoTipoRegimenParticular))
			{
				totalPaciente=totalCargos;
			}
			else
			{
				//FIXME montos_cobro
				/*

				HashMap monto = estadoCuentaDao.cargarMontoCobroSubCuenta(con, mapaA.get("idSubCuenta").toString());
				if(!monto.get("valor").toString().equals(""))
					totalPaciente = Double.parseDouble(monto.get("valor").toString());
				else
				{
					//Al paciente no se le cobrarán las solicitudes que tengan pyp
					double valorTotalCargosSinPyp = estadoCuentaDao.valorTotalCargos(con, mapaA.get("idSubCuenta").toString(), false);
					
					totalPaciente = valorTotalCargosSinPyp * (Utilidades.convertirADouble(monto.get("porcentaje").toString(),true)/100);
					if(Utilidades.convertirADouble(monto.get("porcentaje").toString(),true)<100)
						totalPaciente = Math.round(totalPaciente);
				}
				*/
			}
			totalConvenio=totalCargos-totalPaciente;
			if(totalConvenio<0)
				totalConvenio=0;
			mapa.put("totalcon_"+i, totalConvenio);
			mapa.put("totalcar_"+i, totalCargos);
			mapa.put("totalpac_"+i, totalPaciente);
			mapa.put("nombre_"+i, mapaA.get("nombreConvenio"));
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param idSubCuenta
	 * @param esConvenioPoliza
	 * @param codigoTipoRegimen 
	 * @param institucion Instituci&oacute;n para calcular los abonos de los pacientes
	 * @param idIngreso 
	 * @return
	 */
	public ResultadoBoolean calcularTotalesCargos(Connection con, String idSubCuenta, String idCuenta, String codigoPaciente, String esConvenioPoliza, String codigoTipoRegimen, int institucion, int idIngreso)
	{
		//Se consulta el total de los cargos
		this.valorTotalCargos = estadoCuentaDao.valorTotalCargos(con, idSubCuenta, true);
		//**********SE VERIFICA SI EL CONVENIO TIENE INFORMACION DE POLIZA****************************
		if(UtilidadTexto.getBoolean(esConvenioPoliza))
		{
			this.valorSaldoMontoAutorizado = Utilidades.obtenerSaldoConvenio(con, idSubCuenta);
			
			//*************TOTAL PACIENTE**************************************
			this.valorTotalPaciente = this.valorTotalCargos - this.valorSaldoMontoAutorizado;
			if(this.valorTotalPaciente<0)
				this.valorTotalPaciente = 0;
			//***************************************************************
			
			//**********TOTAL CONVENIO***************************************
			//Es el saldo monto autorizado
			this.valorTotalConvenio = this.valorSaldoMontoAutorizado - this.valorTotalCargos;
			if(this.valorTotalConvenio<0)
				this.valorTotalConvenio = 0;
			//***************************************************************
			
		}
		else
		{
			//****************TOTAL PACIENTE****************************************************
			
			
			//Si el convenio es particular se le cobra absolutamente todo
			if(codigoTipoRegimen.equals(ConstantesBD.codigoTipoRegimenParticular+""))
			{
				this.valorTotalPaciente = this.valorTotalCargos;
			}
			else
			{
				//Se consultan los datos del monto de cobro
				//FIXME montos_cobro
				/*
				HashMap monto = estadoCuentaDao.cargarMontoCobroSubCuenta(con, idSubCuenta);
				
				//Calculo por valor
				if(!monto.get("valor").toString().equals(""))
					this.valorTotalPaciente = Double.parseDouble(monto.get("valor").toString());
				else
				{
					//Al paciente no se le cobrarán las solicitudes que tengan pyp
					double valorTotalCargosSinPyp = estadoCuentaDao.valorTotalCargos(con, idSubCuenta, false);
					
					this.valorTotalPaciente = valorTotalCargosSinPyp * (Utilidades.convertirADouble(monto.get("porcentaje").toString(),true)/100);
					if(Utilidades.convertirADouble(monto.get("porcentaje").toString(),true)<100)
						this.valorTotalPaciente = Math.round(this.valorTotalPaciente);
				}
				*/
			}
			
			//***************TOTAL CONVENIO************************************************
			this.valorTotalConvenio = this.valorTotalCargos - this.valorTotalPaciente;
			if(this.valorTotalConvenio<0)
				this.valorTotalConvenio = 0;
		}
		
		//*************TOTAL ABONOS**********************************************
		Integer ingreso=null;
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(institucion)))
		{
			ingreso=idIngreso;
		}
		
		this.valorTotalAbonos=AbonosYDescuentos.consultarAbonosDisponibles(con, Integer.parseInt(codigoPaciente), idIngreso);
		
		//**************NETO PACIENTE ó DEVOLUCION PACIENTE******************************
		this.valorNetoPaciente = this.valorTotalPaciente - this.valorTotalAbonos;
		if(this.valorNetoPaciente<0)
		{
			this.valorDevolucionPaciente = (-1) * this.valorNetoPaciente;
			this.valorNetoPaciente = 0;
		}
		else
			this.valorDevolucionPaciente = 0;
		//*************************************************************************
		
		return new ResultadoBoolean(true,"Valores previamente cargados");
	}
	
	/**
	 * Método que consulta el detalle del cargo de un solicitud de servicio/articulo
	 * @param con
	 * @param idCuenta
	 * @param codigoConvenio
	 * @param numeroSolicitud
	 * @param idSubCuenta
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleCargoServicioArticulo(Connection con,String idCuenta,String codigoConvenio,String numeroSolicitud,String idSubCuenta, String tipoArticulo)
	{
		HashMap campos = new HashMap();
		campos.put("idCuenta",idCuenta);
		campos.put("codigoConvenio",codigoConvenio);
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("idSubCuenta",idSubCuenta);
		campos.put("tipoArticulo",tipoArticulo);
		return estadoCuentaDao.cargarDetalleCargoServicioArticulo(con, campos);
	}
	
	/**
	 * 
	 * M&eacute;todo para mostrar el detalle de la solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitud
	 * @param codigoSubCuenta
	 * @param codigoInstitucion
	 * @return
	 */
	
	public HashMap<String, Object> cargarDetalleCargoArticuloConEquivalente(Connection con,String idCuenta,String codigoConvenio,String numeroSolicitud,String idSubCuenta, String tipoArticulo)
	{
		HashMap campos = new HashMap();
		campos.put("idCuenta",idCuenta);
		campos.put("codigoConvenio",codigoConvenio);
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("idSubCuenta",idSubCuenta);
		campos.put("tipoArticulo",tipoArticulo);
		return estadoCuentaDao.cargarDetalleCargoArticuloConEquivalente(con, campos);
	}
	
	
	/**
	 * Metodo que consulta los codigos de las facturas asociadas a un cargo
	 * @param con
	 * @param subCuenta
	 * @param solicitud
	 * @return
	 */
	public String codigosFacturas(Connection con, int subCuenta, int solicitud)
	{
		return estadoCuentaDao.codigosFacturas(con, subCuenta, solicitud);
	}
	
	/**
	 * Metodo para verificar si una Consulta arroja resultados
	 * @param con
	 * @param consulta
	 * @return
	 */
	public boolean resultadosConsulta(Connection con, String consulta)
	{
		return estadoCuentaDao.resultadosConsulta(con, consulta);
	}
	
	/**
	 * Metodo que consulta Tipo Monto y Valores Paciente
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap tipoMontoValoresPaciente(Connection con, int subCuenta)
	{
		return estadoCuentaDao.tipoMontoValoresPaciente(con, subCuenta);
	}
	
	/**
	 * Método que carga el detalle del cargo de una cirugía
	 * @param con
	 * @param numeroSolicitud
	 * @param idSubcuenta
	 * @param codigoServicio
	 * @pram mostrarPaquetizacion
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleCargoCirugia(Connection con, String numeroSolicitud, String idSubcuenta, String codigoServicio,boolean mostrarPaquetizacion)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("idSubCuenta", idSubcuenta);
		campos.put("codigoServicio", codigoServicio);
		campos.put("mostrarPaquetizacion", mostrarPaquetizacion);
		return estadoCuentaDao.cargarDetalleCargoCirugia(con, campos);
	}
	
	/**
	 * Metodo que consulta la Fecha de Egreso segun Estado Cuenta
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap<String, Object> fechaEgreso(Connection con, int ingreso)
	{
		return estadoCuentaDao.fechaEgreso(con, ingreso);
	}
	
	/**
	 * Metodo para realizar la Impresion detallado por Solicitud desde Generacion y Consulta de Facturas
	 * @param con
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param codFactura
	 * @param centroAtencion
	 * @param entSub
	 * @param estadoCuenta
	 * @param tipoMonto
	 * @param estadoIngreso
	 * @param natuPaciente
	 * @param nombreConvenio
	 * @param claseEcon
	 * @param subCuenta
	 * @return
	 */
	public static String imprimirFacturaDetalladoSolicitud(Connection con, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, String codFactura, String centroAtencion, String entSub, String estadoCuenta, String tipoMonto, String estadoIngreso, String natuPaciente, String nombreConvenio, String claseEcon, String subCuenta)
	{
		String nombreRptDesign = "";
		nombreRptDesign = "EstadoCuentaDetallado.rptdesign";
				
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "ANEXO DE CUENTA DETALLADO POR SOLICITUD";
        armarEncabezado(comp, con, ins, request, paciente, nombreReporte, codFactura, "", nombreConvenio);
        
        //Se inserta la imagen del encabezado
        /*comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        //Se inserta la información de la isntitucion
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        comp.insertLabelInGridPpalOfHeader(1,0, "ANEXO DE CUENTA DETALLADO POR SOLICITUD");
        
        comp.insertLabelInGridPpalOfHeader(2,0, "Fecha de Proceso. "+UtilidadFecha.getFechaActual(con));
        
        //Se inserta grilla del encabezado del paciente
        v = new Vector();
        v.add(paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
        v.add(paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
        v.add("No.H.C.  "+paciente.getHistoriaClinica());
        v.add("Edad: "+paciente.getEdadDetallada());
        v.add("Vía ingreso: "+paciente.getUltimaViaIngreso());
        v.add("Centro de Atención: "+centroAtencion);
        v.add("N° ingreso: "+paciente.getConsecutivoIngreso());
        v.add("Fecha ingreso: "+paciente.getFechaIngreso());
        
        EstadoCuenta mundo=new EstadoCuenta();
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa=mundo.fechaEgreso(con, paciente.getCodigoIngreso());
        if(Integer.parseInt(mapa.get("numRegistros").toString())>0)
        {
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturada))
	        	v.add("Fecha de Egreso: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechae").toString()));
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial))
	        	v.add("Fecha de Egreso: Factura Parcial");
        }
        
        v.add("Estado ingreso: "+ValoresPorDefecto.getIntegridadDominio(estadoIngreso));
        
        if(!entSub.equals(""))
        	v.add("Entidad SubContratada: "+entSub);        	
        
        v.add("N° Cuenta: "+paciente.getCodigoCuenta());
        v.add("Estado Cuenta: "+estadoCuenta);
        v.add("Responsable: "+nombreConvenio);
        v.add("Naturaleza del Paciente: "+natuPaciente);
        v.add("Clasif. SocioEc: "+claseEcon);
        
        String numCarnet="";
        numCarnet=mundo.obtenerCarnet(con, paciente.getCodigoConvenio(), paciente.getCodigoIngreso());
        
        v.add("Carnet: "+numCarnet);
        v.add("Dirección: "+paciente.getDireccion());
        v.add("Teléfono: "+paciente.getTelefono());
        
        v.add("  "); ///espacios que sobrean
        comp.insertLabelInGridOfMasterPageWithProperties(3,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        //*******************************************************************************************************/
                
        //*********************SEGUNDO SE MODIFICA LA CONSULTA*************************************************** 
        comp.obtenerComponentesDataSet("detalleSolicitud");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        //Segun tipo de impresión se modifica la consulta
    	oldQuery = "SELECT "+
	    		"t.\"orden\" AS orden, "+
				"t.\"valoru\" AS valoru, " +
				"t.\"usuario\" AS usuario, " +
				"t.\"paquete\" AS paquete," +
	    		"t.\"fecha_cargo\" AS fecha_cargo, "+
	    		"t.\"profesional\" AS profesional, "+
	    		"t.\"centro_costo_solicita\" AS centro_costo_solicita," +
	    		"t.\"centro_costo_ejecuta\" AS centro_costo_ejecuta, "+
	    		"t.\"codigo\" AS codigo, "+
	    		"t.\"descripcion\" AS descripcion, "+
	    		"t.\"cantidad\" AS cantidad, "+
	    		"t.\"total_cargo\" AS total_cargo, "+
	    		"t.\"total_recargo\" AS total_recargo, "+
	    		"t.\"total_dcto\" AS total_dcto, "+
	    		"to_char(current_date, 'DD/MM/YYYY') AS fechaActual " +
    		"FROM "+ 
    		"( "+
    			"( "+
    				"SELECT "+ 
    				"sol.consecutivo_ordenes_medicas As  orden, "+
					
    				//Modificado por la Tarea 51069
    				//"(((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)))-(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0))) AS valoru, " +
    				"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
    				
    				"dc.usuario_modifica AS usuario, " +
    				"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(cast(dc.cargo_padre as int)) ELSE '' END AS paquete, " +
					//"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
    				"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
    				"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+
    				"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
    				"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
    				"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
						"getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
					"ELSE "+ 
						"coalesce(getobtenercodigointerfaz(dc.articulo),'') "+ 
					"END AS codigo, "+
    				"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
    					"getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
    				"ELSE "+ 
    					"getdescripcionarticulo(dc.articulo) "+ 
    				"END AS descripcion, "+
    				"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
    				"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
    				"coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) AS total_recargo, "+
    				"coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) AS total_dcto "+ 
    				"FROM det_cargos dc "+ 
    				"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
    				"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"WHERE ";
    				if(!codFactura.equals("-1"))
    					oldQuery+="dc.codigo_factura="+codFactura+" AND ";
    				else
    					oldQuery+="sbc.ingreso = "+paciente.getCodigoIngreso()+" AND ";
    				oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
    				"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+
    			") "+ 
    			"UNION "+ 
    			"( "+
    				"SELECT "+ 
    				"sol.consecutivo_ordenes_medicas As  orden, "+
					
    				//Modificado por la Tarea 51069
    				//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
    				"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
    				
    				"dc.usuario_modifica AS usuario, " +
    				
    				"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(cast(dc.cargo_padre as int)) ELSE '' END AS paquete, " +
					//"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
    				"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
    				"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
    				"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
    				"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
    				"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx  AS codigo, "+
    				"substr(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+"),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28)  AS descripcion, "+
    				"1 AS cantidad, "+
    				"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
    				"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
    				"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto "+ 
    				"FROM det_cargos dc "+ 
    				"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
    				"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
					"WHERE ";
    				if(!codFactura.equals("-1"))
    					oldQuery+="dc.codigo_factura="+codFactura+" AND ";
    				else
    					oldQuery+="sbc.ingreso = "+paciente.getCodigoIngreso()+" AND ";
    				oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
    				"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
    				"dc.articulo is null "+
    				"GROUP BY sol.consecutivo_ordenes_medicas,dc.solicitud,sol.centro_costo_solicitado,dc.fecha_modifica,dc.solicitud,dc.servicio_cx,tpa.nombre_asocio,dc.det_cx_honorarios,dc.usuario_modifica,dc.cargo_padre,sol.centro_costo_solicitante,dc.valor_unitario_cargado "+
				") "+
    			//Detalle de los materiales especiales
    			"UNION "+ 
    			"( "+
    				"SELECT "+ 
    				"sol.consecutivo_ordenes_medicas As  orden, "+
					
    				//Modificado por la Tarea 51069
    				//"(((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)))-(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0))) AS valoru, " +
    				"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
    				
    				"dc.usuario_modifica AS usuario, " +
    				
    				//Modificado por tarea 31113
    				"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(cast(dc.cargo_padre as int)) ELSE '' END AS paquete, " +
					//"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '' END AS paquete, " +
    				
    				"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
    				"'' AS profesional, "+
    				"getnomcentrocosto(sol.centro_costo_solicitante) AS centro_costo_solicita, "+
    				"getnomcentrocosto(sol.centro_costo_solicitado) AS centro_costo_ejecuta, "+
    				"dc.articulo ||''  AS codigo, "+
    				"getdescripcionarticulo(dc.articulo)  AS descripcion, "+
    				"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
    				"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
    				"coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0) AS total_recargo, "+
    				"coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0) AS total_dcto "+ 
    				"FROM det_cargos dc "+ 
    				"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
    				"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
					"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
					"WHERE ";
    				if(!codFactura.equals("-1"))
    					oldQuery+="dc.codigo_factura="+codFactura+" AND ";
    				else
    					oldQuery+="sbc.ingreso = "+paciente.getCodigoIngreso()+" AND ";
    				oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
    				"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
    				"dc.articulo is not null "+
    			") "+
    		") t "+ 
    	"ORDER BY \"fecha_cargo\",\"codigo\"";
        logger.info("\n\nCONSULTA IMPRESIOIN DESPUES => "+oldQuery+"\n\n");
        
        //se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR******************************************************
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD***************************************************
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals(""))
        {
        	return newPathReport;
        }
		return "";
	}
	
	/**
	 * Metodo para realizar la Impresion resumido por Centro de Costo desde Generacion y Consulta de Facturas
	 * @param con
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param codFactura
	 * @param centroAtencion
	 * @param entSub
	 * @param estadoCuenta
	 * @param tipoMonto
	 * @param estadoIngreso
	 * @param natuPaciente
	 * @param nombreConvenio
	 * @param claseEcon
	 * @param subCuenta
	 * @return
	 */
	public static String imprimirFacturaResumidoCC(Connection con, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, String codFactura, String centroAtencion, String entSub, String estadoCuenta, String tipoMonto, String estadoIngreso, String natuPaciente, String nombreConvenio, String claseEcon, String subCuenta)
	{
		String nombreRptDesign = "";
		nombreRptDesign = "EstadoCuentaResumido.rptdesign";
				
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "ANEXO DE CUENTA RESUMIDO POR CENTRO DE COSTO";
        armarEncabezado(comp, con, ins, request, paciente, nombreReporte, codFactura, "", nombreConvenio);
        
        /*//Se inserta la imagen del encabezado
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        //Se inserta la información de la isntitucion
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        comp.insertLabelInGridPpalOfHeader(1,0, "ANEXO DE CUENTA RESUMIDO POR CENTRO DE COSTO");
        
        comp.insertLabelInGridPpalOfHeader(2,0, "Fecha de Proceso. "+UtilidadFecha.getFechaActual(con));
        
        //Se inserta grilla del encabezado del paciente
        v = new Vector();
        v.add(paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
        v.add(paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
        v.add("No.H.C.  "+paciente.getHistoriaClinica());
        v.add("Edad: "+paciente.getEdadDetallada());
        v.add("Vía ingreso: "+paciente.getUltimaViaIngreso());
        v.add("Centro de Atención: "+centroAtencion);
        v.add("N° ingreso: "+paciente.getConsecutivoIngreso());
        v.add("Fecha ingreso: "+paciente.getFechaIngreso());
        
        EstadoCuenta mundo=new EstadoCuenta();
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa=mundo.fechaEgreso(con, paciente.getCodigoIngreso());
        if(Integer.parseInt(mapa.get("numRegistros").toString())>0)
        {
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturada))
	        	v.add("Fecha de Egreso: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechae").toString()));
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial))
	        	v.add("Fecha de Egreso: Factura Parcial");
        }
        
        v.add("Estado ingreso: "+ValoresPorDefecto.getIntegridadDominio(estadoIngreso));
        
        if(!entSub.equals(""))
        	v.add("Entidad SubContratada: "+entSub);        	
        
        v.add("N° Cuenta: "+paciente.getCodigoCuenta());
        v.add("Estado Cuenta: "+estadoCuenta);
        v.add("Responsable: "+nombreConvenio);
        v.add("Naturaleza del Paciente: "+natuPaciente);
        v.add("Clasif. SocioEc: "+claseEcon);
        
        String numCarnet="";
        numCarnet=mundo.obtenerCarnet(con, paciente.getCodigoConvenio(), paciente.getCodigoIngreso());
        
        v.add("Carnet: "+numCarnet);
        v.add("Dirección: "+paciente.getDireccion());
        v.add("Teléfono: "+paciente.getTelefono());
        
        v.add("  "); ///espacios que sobrean
        comp.insertLabelInGridOfMasterPageWithProperties(3,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        //*******************************************************************************************************/
         
        //*********************SEGUNDO SE MODIFICA LA CONSULTA*************************************************** 
        comp.obtenerComponentesDataSet("detalleSolicitud");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        //Segun tipo de impresión se modifica la consulta
        oldQuery = "SELECT "+ 
		    	"t.\"fecha_cargo\", "+
		    	"t.\"convenio\", " +
		    	"t.\"codigo\", "+
		    	"t.\"descripcion\", "+
		    	"sum(t.\"cantidad\") As cantidad, "+
		    	"sum(t.\"total_cargo\") AS total_cargo, "+
		    	"sum(t.\"total_recargo\") AS total_recargo, "+
		    	"sum(t.\"total_dcto\") AS total_dcto, "+
		    	"to_char(current_date, 'DD/MM/YYYY') AS fechaActual " +
		    	"FROM "+
		    	"( "+
		    		"( "+
		    			"SELECT "+ 
						"0 as solicitud, "+ //se deja vacío para acomodarse con el union de cirugias
						"c.nombre AS convenio, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
						"ELSE "+ 
							"coalesce(getobtenercodigointerfaz(dc.articulo),'') "+ 
						"END AS codigo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
						"ELSE "+ 
							"getdescripcionalternaarticulo(dc.articulo) "+ 
						"END AS descripcion, "+
						"sum(coalesce(dc.cantidad_cargada,0)) AS cantidad, "+
						"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
						"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
						"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto "+ 
						"FROM det_cargos dc "+
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"WHERE ";
						if(!codFactura.equals("-1"))
	    					oldQuery+="dc.codigo_factura="+codFactura+" AND ";
	    				else
	    					oldQuery+="sbc.ingreso = "+paciente.getCodigoIngreso()+" AND ";
	    				oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
						"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+ 
						"GROUP BY dc.fecha_modifica,dc.servicio,dc.articulo,c.nombre "+ 
		    		") "+
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"c.nombre AS convenio, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx  AS codigo, "+
		    			"getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")  AS descripcion, "+
		    			"1 AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto "+ 
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
		    			"WHERE ";
	    				if(!codFactura.equals("-1"))
	    					oldQuery+="dc.codigo_factura="+codFactura+" AND ";
	    				else
	    					oldQuery+="sbc.ingreso = "+paciente.getCodigoIngreso()+" AND ";
	    				oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,dc.servicio_cx,c.nombre "+ 
		    		") "+
		    		//La consulta de los materiales especiales
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"c.nombre AS convenio, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			"''  AS codigo, "+
		    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
		    			"sum(dc.cantidad_cargada) AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto "+ 
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
		    			"WHERE ";
	    				if(!codFactura.equals("-1"))
	    					oldQuery+="dc.codigo_factura="+codFactura+" AND ";
	    				else
	    					oldQuery+="sbc.ingreso = "+paciente.getCodigoIngreso()+" AND ";
	    				oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is not null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre "+ 
		    		") "+
		    	") t "+
		    "GROUP BY t.\"fecha_cargo\",t.\"codigo\",t.\"descripcion\",t.\"convenio\" "+
		    "ORDER BY t.\"convenio\", t.\"fecha_cargo\", t.\"codigo\""; 
        logger.info("CONSULTA IMPRESIOIN=> "+oldQuery);
        
		//se actualiza Query
        comp.modificarQueryDataSet(oldQuery);

        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR******************************************************
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD***************************************************
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals(""))
        {
        	return newPathReport;
        }
		return "";
	}
	
	/**
	 * Metodo para realizar la Impresion detallado por Item desde Generacion y Consulta de Facturas
	 * @param con
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param codFactura
	 * @param centroAtencion
	 * @param entSub
	 * @param estadoCuenta
	 * @param tipoMonto
	 * @param estadoIngreso
	 * @param natuPaciente
	 * @param nombreConvenio
	 * @param claseEcon
	 * @param subCuenta
	 * @param string 
	 * @return
	 */
	public static String imprimirFacturaDetalladoIT(Connection con, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, String codFactura, String centroAtencion, String entSub, String estadoCuenta, String tipoMonto, String estadoIngreso, String natuPaciente, String codigoConvenio, String nombreConvenio, String claseEcon, String subCuenta)
	{
		String nombreRptDesign = "";
		nombreRptDesign = "EstadoCuentaItem.rptdesign";
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "REPORTE DETALLADO DE CUENTA";
        armarEncabezado(comp, con, ins, request, paciente, nombreReporte, codFactura, codigoConvenio, nombreConvenio);
        
        //*********************SEGUNDO SE MODIFICA LA CONSULTA*************************************************** 
        comp.obtenerComponentesDataSet("detalleItem");            
        String oldQuery = "SELECT "+ 
            	"t.fecha_cargo as fecha_cargo, "+
            	"t.codigo as codigo, "+
            	"t.es_portatil as es_portatil, "+
            	"t.codigo_portatil as codigo_portatil,"+
            	"t.codigoalterno as codigoalterno, "+
            	"t.descripcion as descripcion, "+
            	"t.asocio as asocio, " +
            	"t.paquete as paquete, " +
            	"t.ctc as ctc, " +
            	"t.valoru as valoru, " +
            	"sum(t.valort) as valort, " +
            	"t.profesional as profesional, " +
            	"sum(t.cantidad) as cantidad, "+
            	"sum(t.total_cargo) as total_cargo, "+
            	"sum(t.total_recargo) as total_recargo, "+
            	"sum(t.total_dcto) as total_dcto, "+
            	"to_char(current_date, 'DD/MM/YYYY') as fechaActual " +
            	"FROM "+
            	"( "+
            		"( "+
            			"SELECT "+ 
            				"-1  servicio_cx, " +
            				"0  es_encabezado_cx, "+
    	    				"0  solicitud, "+ //se deja vacío para acomodarse con el union de cirugias
    	    				"'---'  asocio, " +
    	    				"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.solicitud) ELSE '---' END  paquete, " +
    	    				"CASE WHEN dc.servicio IS NOT NULL THEN " +
    	    				"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
    	    				"ELSE " +
    	    				"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
    	    				"END  ctc, " +
    	    				"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END  profesional, "+
    	    				
    	    				//Modificado por la Tarea 51069
    	    				//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
    	    				"coalesce(dc.valor_unitario_cargado,0)  valoru, "+
    	    				
//    	    				"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
    						"(sum((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo,0)*dc.cantidad_cargada)-((coalesce(dc.valor_unitario_dcto,0))*dc.cantidad_cargada)))  valort, " +
    						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') fecha_cargo, "+
    	    				"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
    							"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') "+ 
    						"ELSE "+ 
    							"coalesce(getobtenercodigointerfaz(dc.articulo),'---') "+ 
    						"END codigo, "+
    						"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END es_portatil, "+
    						"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN coalesce(getobtenercodigocupsserv((SELECT portatil_asociado from sol_procedimientos WHERE numero_solicitud = dc.solicitud),"+ConstantesBD.codigoTarifarioCups+"),'---') ELSE '' END codigo_portatil, "+
    						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
    							"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') "+ 
    						"ELSE "+ 
    							"'---' "+ 
    						"END codigoalterno, "+
    	    				"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
    	    					"coalesce(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') "+ 
    	    				"ELSE "+ 
    	    					"coalesce(getdescripcionalternaarticulo(dc.articulo),'---') "+ 
    	    				"END descripcion, "+
    	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.cantidad_cargada,0)) ELSE 0 END cantidad, "+
    	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END total_cargo, "+
    	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END total_recargo, "+
    	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END total_dcto "+ 
        				"FROM " +
        					"det_cargos dc "+
        					"INNER JOIN solicitudes sol ON(sol.numero_solicitud = dc.solicitud) "+
        				"WHERE " +
    	    				"dc.codigo_factura = "+codFactura+" AND " +
    	    				"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
    	    				"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" AND "+ 
    	    				"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
        				"GROUP BY " +
        					"servicio_cx, " +
//        					"es_encabezado_cx, " +
        					"dc.cargo_padre, " +
        					"dc.solicitud, " +
        					"dc.servicio, " +
        					"dc.articulo, " +
        					"sol.codigo_medico_responde, " +
        					"dc.valor_unitario_cargado, " +
        					"dc.fecha_modifica, " +
        					"sol.tipo " + 
            		") "+
            		
            		
            		"UNION ALL "+
            		"( " +
            			"(" +
            				"SELECT " +
            					"DISTINCT " +
            					"coalesce(dc.servicio_cx,-1) servicio_cx, " +	
            					"1  es_encabezado_cx, "+
            					"dc.solicitud, " +
            					"'----' asocio, " +
            					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(cast(dc.cargo_padre as int)) ELSE '---' END paquete, " +
            					//"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '---' END AS paquete, " +
            					"'----' ctc, " +
            					"CASE WHEN dc.servicio_cx IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END profesional, " +
            					"-1 valoru, " +
            					"getValorCxCompleta(dc.solicitud, dc.sub_cuenta, dc.servicio_cx, dc.facturado, dc.paquetizado,"+ConstantesBD.codigoEstadoFCargada+") valort, " +
            					"to_char(dc.fecha_modifica,'DD/MM/YYYY') fecha_cargo, " +
            					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+"),'---') codigo, " +
            					"'"+ConstantesBD.acronimoNo+"' es_portatil, " +
            					"'' codigo_portatil, " +
            					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioSoat+"),'---') codigoalterno, " +
            					"UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")) descripcion, " +
            					"1 cantidad, " +
            					"0 total_cargo, " +
            					"0 total_recargo, " +
            					"0 total_dcto " +
            				"FROM " +
            					"det_cargos dc " +
            					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
            				"WHERE " +
    	        				"dc.codigo_factura="+codFactura+" AND " +
    							"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
    							"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
    							"dc.articulo is null AND "+
    							"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
            			") " +
                    	"UNION ALL "+
                		"( "+
                			"SELECT "+
                				"coalesce(dc.servicio_cx,-1) servicio_cx, " +
                				"0  es_encabezado_cx, "+
        	        			"dc.solicitud, "+
        	        			"tpa.codigo_asocio || '-' || tpa.nombre_asocio asocio, " +
        	        			"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.solicitud) ELSE '---' END paquete, " +
        	        			"CASE WHEN dc.servicio IS NOT NULL THEN " +
        	    				"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
        	    				"ELSE " +
        	    				"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
        	    				"END ctc, " +
        	    				"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'---') profesional, "+
        	    				
        	    				//Modificado por la Tarea 51069
        	    				//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
        	    				"coalesce(dc.valor_unitario_cargado,0) valoru, "+
        	    				
        						"-1 valort, " +
        	        			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') fecha_cargo, "+
        	        			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') codigo, "+
        	        			"'"+ConstantesBD.acronimoNo+"' es_portatil, " +
        						"'' codigo_portatil, "+
        	        			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') codigoalterno, "+
        	        			"coalesce(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') descripcion, "+
        	        			"1 cantidad, "+
        	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END total_cargo, "+
        	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo,0)) ELSE 0 END total_recargo, "+
        	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto,0)) ELSE 0 END total_dcto "+ 
                			"FROM " +
                				"det_cargos dc "+
        	        			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
        	        			"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
                			"WHERE " +
        						"dc.codigo_factura="+codFactura+" AND " +
        						"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
        						"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
        						"dc.articulo is null AND "+
        						"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
                			"GROUP BY " +
                				"servicio_cx, " +
//                				"es_encabezado_cx, " +
                				"dc.fecha_modifica," +
                				"dc.solicitud," +
                				"dc.servicio_cx," +
                				"tpa.codigo_asocio," +
                				"tpa.nombre_asocio," +
                				"dc.cargo_padre," +
                				"dc.servicio," +
                				"dc.articulo," +
                				"dc.det_cx_honorarios," +
                				"dc.valor_unitario_cargado "+ 
                		") "+
            			 
            		") "+
            		//La consulta de los materiales especiales
            		"UNION ALL "+
            		"( "+
            			"SELECT "+
            				"coalesce(dc.servicio_cx,-1) servicio_cx, " +
            				"0 es_encabezado_cx, "+
    	        			"dc.solicitud, "+
    	        			"'---' asocio, " +
    	        			"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.solicitud) ELSE '---' END paquete, " +
    	        			"CASE WHEN dc.servicio IS NOT NULL THEN " +
    	    				"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
    	    				"ELSE " +
    	    				"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
    	    				"END ctc, " +
    	    				"'---' profesional, "+
    	    				
    	    				//Modificado por la Tarea 51069
    	    				//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
    	    				"coalesce(dc.valor_unitario_cargado,0) valoru, "+
    	    				
//    						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
    						"(sum((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo,0)*dc.cantidad_cargada)-((coalesce(dc.valor_unitario_dcto,0))*dc.cantidad_cargada))) valort, " +
    						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') fecha_cargo, "+
    	        			"'---' codigo, "+
    	        			"'"+ConstantesBD.acronimoNo+"' es_portatil, " +
    						"'' codigo_portatil, "+
    	        			"'---'  codigoalterno, "+
    	        			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud) descripcion, "+
    	        			"sum(dc.cantidad_cargada)  cantidad, "+
    	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END  total_cargo, "+
    	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END  total_recargo, "+
    	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END  total_dcto "+ 
            			"FROM " +
            				"det_cargos dc "+ 
            				"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
            			"WHERE " +
    	        			"dc.codigo_factura="+codFactura+" AND " +
    	        			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
    	        			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
    	        			"dc.articulo is not null AND "+
    	        			"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
            			"GROUP BY " +
            				"servicio_cx, " +
//            				"es_encabezado_cx, " +
            				"dc.fecha_modifica," +
            				"dc.solicitud," +
            				"dc.cargo_padre," +
            				"dc.servicio," +
            				"dc.articulo," +
            				"dc.valor_unitario_cargado "+ 
            		") "+
            	") t "+
            "GROUP BY t.servicio_cx,  fecha_cargo,t.solicitud, codigo,codigoalterno,descripcion,asocio,paquete,ctc,valoru,valort,profesional,es_portatil,codigo_portatil "+
            "ORDER BY fecha_cargo ASC, t.solicitud ASC, t.servicio_cx ASC  ";

        
        logger.info("CONSULTA IMPRESIOIN=> "+oldQuery);
        
        //*********************TERCERO SE OBTIENEN LOS TOTALES*******************************************
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa = Utilidades.consultaTotalesFactura(con, Utilidades.convertirAEntero(codFactura));

        Vector v = new Vector();
        v.add("VALOR TOTAL DE CARGOS:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vtotal_0")+""));
        v.add("VALOR TOTAL RECAUDO PACIENTE:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vabonos_0")+""));
        v.add("VALOR TOTAL A CARGO DEL PACIENTE:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vbruto_0")+""));
        v.add("NETO A PAGAR:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vneto_0")+""));
        comp.insertLabelInGridOfBodyPage(2, 0, v);
        //*********************FIN TOTALES***************************************************************
        
		//se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR******************************************************
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD***************************************************
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals(""))
        {
        	return newPathReport;
        }
		return "";
	}

	/** Tipo Modificacion: Segun incidencia 6078
	* Funcion: imprimirFacturaDetalladoIT
	* Autor: Alejandro Aguirre Luna
	* Usuario: aleagulu
	* Fecha: 19/02/2013
	* Descripcion: 
	* Actualización como @Deprecated debido a que se realizan cambios en DCU
	* IPS-M Consultar Facturas por Rangos-234
	* IPS-M Consultar Facturas por Paciente-122 versión 
	* los cuales indican como deben tratarse las facturas anuladas
	* Argumentos:
	* 	- Connection con	
	* 	- HttpServletRequest request
	* 	- UsuarioBasico medico
	* 	- PersonaBasica paciente
	*   - String codFactura
	*   - String centroAtencion
	*   - String entSub
	*   - String estadoCuenta
	*   - String tipoMonto
	*   - String estadoIngreso 
	*   - String natuPaciente
	*   - String codigoConvenio
	*   - String nombreConvenio
	*   - String claseEcon
	*   - String subCuenta
	*   - String estadoFac
	* Retorno: String
	**/
	@Deprecated
	public static String imprimirFacturaDetalladoIT(Connection con, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, String codFactura, String centroAtencion, String entSub, String estadoCuenta, String tipoMonto, String estadoIngreso, String natuPaciente, String codigoConvenio, String nombreConvenio, String claseEcon, String subCuenta, String estadoFac)
	{
		String nombreRptDesign = "";
		nombreRptDesign = "EstadoCuentaItem.rptdesign";
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "REPORTE DETALLADO DE CUENTA";
        armarEncabezado(comp, con, ins, request, paciente, nombreReporte, codFactura, codigoConvenio, nombreConvenio);
        
        //*********************SEGUNDO SE MODIFICA LA CONSULTA*************************************************** 
        comp.obtenerComponentesDataSet("detalleItem");            
        
        //Variable String oldQuery que almacena la consulta
        String oldQuery = "";
        //Verificar si el estadoFac es Facturada = 1 o Anulada = 2
        if(estadoFac.equals("1")){
        	oldQuery = "SELECT "+ 
                	"t.fecha_cargo as fecha_cargo, "+
                	"t.codigo as codigo, "+
                	"t.codigoalterno as codigoalterno, "+
                	"t.paquete as paquete, " +
                	"t.ctc as ctc, " +
                	"t.descripcion as descripcion, "+
                	"t.profesional as profesional, " +
                	"sum(t.cantidad) as cantidad, "+
                	"t.valoru as valoru, " +
                	"sum(t.total_dcto) as total_dcto, "+
                	"sum(t.valort) as valort, " +
                	"to_char(current_date, 'DD/MM/YYYY') as fechaActual " +
                	//"t.es_portatil as es_portatil, "+
                	//"t.codigo_portatil as codigo_portatil,"+
                	//"t.asocio as asocio, " +
                	//"sum(t.total_cargo) as total_cargo, "+
                	//"sum(t.total_recargo) as total_recargo, "+
                	"FROM "+
                	"( "+
                		"( "+
                			"SELECT "+ 
                				"-1  servicio_cx, " +
                				"0  es_encabezado_cx, "+
        	    				"0  solicitud, "+ //se deja vacío para acomodarse con el union de cirugias
        	    				"'---'  asocio, " +
        	    				"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.solicitud) ELSE '---' END  paquete, " +
        	    				"CASE WHEN dc.servicio IS NOT NULL THEN " +
        	    				"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
        	    				"ELSE " +
        	    				"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
        	    				"END  ctc, " +
        	    				"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END  profesional, "+
        	    				
        	    				//Modificado por la Tarea 51069
        	    				//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
        	    				"coalesce(dc.valor_unitario_cargado,0)  valoru, "+
        	    				
//        	    				"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
        						"(sum((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo,0)*dc.cantidad_cargada)-((coalesce(dc.valor_unitario_dcto,0))*dc.cantidad_cargada)))  valort, " +
        						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') fecha_cargo, "+
        	    				"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
        							"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') "+ 
        						"ELSE "+ 
        							"coalesce(getobtenercodigointerfaz(dc.articulo),'---') "+ 
        						"END codigo, "+
        						"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END es_portatil, "+
        						"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND (SELECT count(1) from sol_procedimientos WHERE numero_solicitud = dc.solicitud and portatil_asociado = dc.servicio) > 0 THEN coalesce(getobtenercodigocupsserv((SELECT portatil_asociado from sol_procedimientos WHERE numero_solicitud = dc.solicitud),"+ConstantesBD.codigoTarifarioCups+"),'---') ELSE '' END codigo_portatil, "+
        						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
        							"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') "+ 
        						"ELSE "+ 
        							"'---' "+ 
        						"END codigoalterno, "+
        	    				"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
        	    					"coalesce(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') "+ 
        	    				"ELSE "+ 
        	    					"coalesce(getdescripcionalternaarticulo(dc.articulo),'---') "+ 
        	    				"END descripcion, "+
        	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.cantidad_cargada,0)) ELSE 0 END cantidad, "+
        	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END total_cargo, "+
        	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END total_recargo, "+
        	    				"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END total_dcto "+ 
            				"FROM " +
            					"det_cargos dc "+
            					"INNER JOIN solicitudes sol ON(sol.numero_solicitud = dc.solicitud) "+
            				"WHERE " +
        	    				"dc.codigo_factura = "+codFactura+" AND " +
        	    				"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
        	    				"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" AND "+ 
        	    				"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
            				"GROUP BY " +
            					"servicio_cx, " +
//            					"es_encabezado_cx, " +
            					"dc.cargo_padre, " +
            					"dc.solicitud, " +
            					"dc.servicio, " +
            					"dc.articulo, " +
            					"sol.codigo_medico_responde, " +
            					"dc.valor_unitario_cargado, " +
            					"dc.fecha_modifica, " +
            					"sol.tipo " + 
                		") "+
                		
                		
                		"UNION ALL "+
                		"( " +
                			"(" +
                				"SELECT " +
                					"DISTINCT " +
                					"coalesce(dc.servicio_cx,-1) servicio_cx, " +	
                					"1  es_encabezado_cx, "+
                					"dc.solicitud, " +
                					"'----' asocio, " +
                					"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(cast(dc.cargo_padre as int)) ELSE '---' END paquete, " +
                					//"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.cargo_padre::int) ELSE '---' END AS paquete, " +
                					"'----' ctc, " +
                					"CASE WHEN dc.servicio_cx IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '---' END profesional, " +
                					"-1 valoru, " +
                					"getValorCxCompleta(dc.solicitud, dc.sub_cuenta, dc.servicio_cx, dc.facturado, dc.paquetizado,"+ConstantesBD.codigoEstadoFCargada+") valort, " +
                					"to_char(dc.fecha_modifica,'DD/MM/YYYY') fecha_cargo, " +
                					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+"),'---') codigo, " +
                					"'"+ConstantesBD.acronimoNo+"' es_portatil, " +
                					"'' codigo_portatil, " +
                					"coalesce(getobtenercodigocupsserv(dc.servicio_cx,"+ConstantesBD.codigoTarifarioSoat+"),'---') codigoalterno, " +
                					"UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")) descripcion, " +
                					"1 cantidad, " +
                					"0 total_cargo, " +
                					"0 total_recargo, " +
                					"0 total_dcto " +
                				"FROM " +
                					"det_cargos dc " +
                					"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) " +
                				"WHERE " +
        	        				"dc.codigo_factura="+codFactura+" AND " +
        							"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
        							"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
        							"dc.articulo is null AND "+
        							"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
                			") " +
                        	"UNION ALL "+
                    		"( "+
                    			"SELECT "+
                    				"coalesce(dc.servicio_cx,-1) servicio_cx, " +
                    				"0  es_encabezado_cx, "+
            	        			"dc.solicitud, "+
            	        			"tpa.codigo_asocio || '-' || tpa.nombre_asocio asocio, " +
            	        			"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.solicitud) ELSE '---' END paquete, " +
            	        			"CASE WHEN dc.servicio IS NOT NULL THEN " +
            	    				"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
            	    				"ELSE " +
            	    				"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
            	    				"END ctc, " +
            	    				"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'---') profesional, "+
            	    				
            	    				//Modificado por la Tarea 51069
            	    				//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
            	    				"coalesce(dc.valor_unitario_cargado,0) valoru, "+
            	    				
            						"-1 valort, " +
            	        			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') fecha_cargo, "+
            	        			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') codigo, "+
            	        			"'"+ConstantesBD.acronimoNo+"' es_portatil, " +
            						"'' codigo_portatil, "+
            	        			"coalesce(getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioSoat+"),'---') codigoalterno, "+
            	        			"coalesce(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),'---') descripcion, "+
            	        			"1 cantidad, "+
            	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END total_cargo, "+
            	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo,0)) ELSE 0 END total_recargo, "+
            	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto,0)) ELSE 0 END total_dcto "+ 
                    			"FROM " +
                    				"det_cargos dc "+
            	        			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
            	        			"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
                    			"WHERE " +
            						"dc.codigo_factura="+codFactura+" AND " +
            						"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
            						"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
            						"dc.articulo is null AND "+
            						"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
                    			"GROUP BY " +
                    				"servicio_cx, " +
//                    				"es_encabezado_cx, " +
                    				"dc.fecha_modifica," +
                    				"dc.solicitud," +
                    				"dc.servicio_cx," +
                    				"tpa.codigo_asocio," +
                    				"tpa.nombre_asocio," +
                    				"dc.cargo_padre," +
                    				"dc.servicio," +
                    				"dc.articulo," +
                    				"dc.det_cx_honorarios," +
                    				"dc.valor_unitario_cargado "+ 
                    		") "+
                			 
                		") "+
                		//La consulta de los materiales especiales
                		"UNION ALL "+
                		"( "+
                			"SELECT "+
                				"coalesce(dc.servicio_cx,-1) servicio_cx, " +
                				"0 es_encabezado_cx, "+
        	        			"dc.solicitud, "+
        	        			"'---' asocio, " +
        	        			"CASE WHEN dc.cargo_padre IS NOT NULL THEN getobtenercodigopaquete(dc.solicitud) ELSE '---' END paquete, " +
        	        			"CASE WHEN dc.servicio IS NOT NULL THEN " +
        	    				"	(CASE WHEN gettienejuspendserv(dc.servicio,dc.solicitud)=1 OR gettienejustificacionnoposserv(dc.servicio,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
        	    				"ELSE " +
        	    				"	(CASE WHEN gettienejuspendart(dc.articulo,dc.solicitud)=1 OR gettienejustificacionnopos(dc.articulo,dc.solicitud)=1 THEN 'SI' ELSE '---' END) " +
        	    				"END ctc, " +
        	    				"'---' profesional, "+
        	    				
        	    				//Modificado por la Tarea 51069
        	    				//"(((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0)))) AS valoru, " +
        	    				"coalesce(dc.valor_unitario_cargado,0) valoru, "+
        	    				
//        						"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
        						"(sum((coalesce(dc.valor_total_cargado,0))+(coalesce(dc.valor_unitario_recargo,0)*dc.cantidad_cargada)-((coalesce(dc.valor_unitario_dcto,0))*dc.cantidad_cargada))) valort, " +
        						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') fecha_cargo, "+
        	        			"'---' codigo, "+
        	        			"'"+ConstantesBD.acronimoNo+"' es_portatil, " +
        						"'' codigo_portatil, "+
        	        			"'---'  codigoalterno, "+
        	        			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud) descripcion, "+
        	        			"sum(dc.cantidad_cargada)  cantidad, "+
        	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_total_cargado,0)) ELSE 0 END  total_cargo, "+
        	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) ELSE 0 END  total_recargo, "+
        	        			"CASE WHEN dc.cargo_padre IS NULL THEN sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) ELSE 0 END  total_dcto "+ 
                			"FROM " +
                				"det_cargos dc "+ 
                				"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
                			"WHERE " +
        	        			"dc.codigo_factura="+codFactura+" AND " +
        	        			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
        	        			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
        	        			"dc.articulo is not null AND "+
        	        			"dc.estado <> "+ConstantesBD.codigoEstadoFExento+" "+
                			"GROUP BY " +
                				"servicio_cx, " +
//                				"es_encabezado_cx, " +
                				"dc.fecha_modifica," +
                				"dc.solicitud," +
                				"dc.cargo_padre," +
                				"dc.servicio," +
                				"dc.articulo," +
                				"dc.valor_unitario_cargado "+ 
                		") "+
                	") t "+
                "GROUP BY t.servicio_cx,  fecha_cargo,t.solicitud, codigo,codigoalterno,descripcion,asocio,paquete,ctc,valoru,valort,profesional,es_portatil,codigo_portatil "+
                "ORDER BY fecha_cargo ASC, t.solicitud ASC, t.servicio_cx ASC  ";
        }
        else if(estadoFac.equals("2")){
        	oldQuery =
			" SELECT " +
			"to_char(dfs.fecha_cargo,'"+ConstantesBD.formatoFechaAp+"') as fecha_cargo, " +
			" dfs.codigo AS codigo," +
			" CASE WHEN dfs.servicio IS NOT NULL THEN (getobtenercodigocupsserv(dfs.servicio,"+ConstantesBD.codigoTarifarioSoat+")) ELSE '---' END as codigoalterno, " +
			" CASE WHEN dfs.servicio IS NOT NULL THEN (getobtenercodigopaquete(dfs.solicitud)) ELSE '---' END as paquete," +
			" CASE WHEN dfs.servicio IS NOT NULL THEN (CASE WHEN gettienejuspendserv(dfs.servicio,dfs.solicitud)=1 OR " +
			" gettienejustificacionnoposserv(dfs.servicio,dfs.solicitud)=1 THEN 'SI' ELSE '---' END) ELSE " +
			" (CASE WHEN gettienejuspendart(dfs.articulo,dfs.solicitud)=1 OR " +
			" gettienejustificacionnopos(dfs.articulo,dfs.solicitud)=1 THEN 'SI' ELSE '---' END) END as ctc," +
			" CASE WHEN dfs.servicio IS NOT NULL THEN (getnombreservicio(dfs.servicio ,"+ConstantesBD.codigoTarifarioCups+")) ELSE '---' END as descripcion," +
			" getnombremedico(dfs.codigo_medico) as profesional," +
			" dfs.cantidad_cargo AS cantidad," +
			" dfs.valor_cargo AS valoru," +
			" case when valor_dcto_bono is not null and valor_dcto_comercial is not null" +
			" and valor_dcto_odo is not null and valor_dcto_prom is not null " +
			" then (valor_dcto_bono + valor_dcto_comercial + valor_dcto_odo + valor_dcto_prom) " +
			" else 0 end as total_dcto," +
			" dfs.valor_total AS valort," +
			" to_char(current_date, 'DD/MM/YYYY') as fechaActual " +
				" FROM facturas f " +
    				" INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo)" +
    				" INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud)" +
    				" INNER JOIN centros_costo cc ON(sol.centro_costo_solicitado=cc.codigo) " +
    				" INNER JOIN convenios con ON (f.convenio = con.codigo)" +
    				" LEFT OUTER JOIN articulo a ON(dfs.articulo=a.codigo and f.institucion=a.institucion)" +
    				" LEFT OUTER JOIN referencias_servicio rs ON(dfs.servicio=rs.servicio and rs.tipo_tarifario=0)" +
    			" WHERE f.codigo="+codFactura+" ORDER BY sol.numero_solicitud, dfs.articulo";
        }

        logger.info("CONSULTA IMPRESIOIN=> "+oldQuery);
        
        //*********************TERCERO SE OBTIENEN LOS TOTALES*******************************************
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa = Utilidades.consultaTotalesFactura(con, Utilidades.convertirAEntero(codFactura));

        Vector v = new Vector();
        v.add("VALOR TOTAL DE CARGOS:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vtotal_0")+""));
        v.add("VALOR TOTAL RECAUDO PACIENTE:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vabonos_0")+""));
        v.add("VALOR TOTAL A CARGO DEL PACIENTE:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vbruto_0")+""));
        v.add("NETO A PAGAR:");
        v.add(UtilidadTexto.formatearValores(mapa.get("vneto_0")+""));
        comp.insertLabelInGridOfBodyPage(2, 0, v);
        //*********************FIN TOTALES***************************************************************
        
		//se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR******************************************************
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD***************************************************
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals(""))
        {
        	return newPathReport;
        }
		return "";
	}
	/**
	 * Método que arma el encabezado de los reportes
	 * de estado de la cuenta
	 * @param comp
	 * @param connection
	 * @param ins
	 * @param forma
	 * @param request
	 * @param paciente 
	 * @param nombreReporte 
	 * @param nombreConvenio 
	 * @param codigoConvenio 
	 */
	private static void armarEncabezado(DesignEngineApi comp, Connection con, InstitucionBasica ins, HttpServletRequest request, PersonaBasica paciente, String nombreReporte, String codFactura, String codigoConvenio, String nombreConvenio)
	{
		//Se inserta la información de la isntitucion
        comp.insertGridHeaderOfMasterPage(0, 0, 1, 2);
        Vector v = new Vector();
        v.add(ins.getRazonSocial());
        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());     
        comp.insertLabelInGridOfMasterPage(0, 0, v);
        comp.insertLabelInGridPpalOfHeader(0, 1, nombreReporte);
        
        logger.info("===>Código Factura: "+codFactura);
        String cuentaFactura = Utilidades.obtenerCuentaFactura(con, codFactura);
        logger.info("===>Cuenta Factura: "+cuentaFactura);
        String ingresoCuenta = Utilidades.obtenerCodigoIngresoDadaCuenta(con, cuentaFactura);
        logger.info("===>Código Ingreso Cuenta: "+ingresoCuenta);
        
        //Se inserta grilla del encabezado del paciente
        v = new Vector();
        EstadoCuenta mundo = new EstadoCuenta();
        String numCarnet = "";
        String nombreApellido = "";
        if(paciente.getPrimerApellido()!=null){
        	nombreApellido+=paciente.getPrimerApellido().toUpperCase();
        }
        if(paciente.getSegundoApellido()!=null){
        	nombreApellido+=" "+paciente.getSegundoApellido().toUpperCase();
        }
        
        if(paciente.getPrimerNombre()!=null){
        	nombreApellido+=" "+paciente.getPrimerNombre().toUpperCase();
        }
        if(paciente.getSegundoNombre()!=null){
        	nombreApellido+=" "+paciente.getSegundoNombre().toUpperCase();
        }
        nombreApellido=nombreApellido.trim();
        logger.info("===>Código Convenio: "+codigoConvenio);
        logger.info("===>Nombre Convenio: "+nombreConvenio);
        numCarnet = mundo.obtenerCarnet(con, Utilidades.convertirAEntero(codigoConvenio), Utilidades.convertirAEntero(ingresoCuenta));
        String codigoInterfazConvenio = Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(codigoConvenio));
        
        v.add("N° INGRESO: "+Utilidades.obtenerConsecutivoIngreso(con, ingresoCuenta));
        v.add("FACTURA: "+Utilidades.obtenerConsecutivoFactura(Utilidades.convertirAEntero(codFactura)));
        if(nombreConvenio.length() > 45)
        	v.add("PLAN: "+(UtilidadCadena.noEsVacio(codigoInterfazConvenio)?codigoInterfazConvenio+" - ":"")+nombreConvenio.toUpperCase().substring(0, 45));
        else
        	v.add("PLAN: "+(UtilidadCadena.noEsVacio(codigoInterfazConvenio)?codigoInterfazConvenio+" - ":"")+nombreConvenio.toUpperCase());
        v.add(" ");
        
        if(nombreApellido.length() > 30)
        	v.add("PACIENTE: "+nombreApellido.substring(0, 30));
        else
        	v.add("PACIENTE: "+nombreApellido);
        
        v.add("DOCUMENTO: "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
        v.add("N° H.C.: "+paciente.getHistoriaClinica());
        v.add("CARNET: "+numCarnet);
        
        if(paciente.getDireccion().length() > 20)
        	v.add("DIRECCIÓN: "+paciente.getDireccion().substring(0, 20));
        else
        	v.add("DIRECCIÓN: "+paciente.getDireccion());
	    v.add("TELÉFONO: "+paciente.getTelefono());
        v.add("FECHA INGRESO: "+Utilidades.obtenerFechaIngreso(con, ingresoCuenta));
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa = mundo.fechaEgreso(con, Utilidades.convertirAEntero(ingresoCuenta));
        if(Integer.parseInt(mapa.get("numRegistros").toString())>0){
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturada))
	        	v.add("FECHA EGRESO: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechae").toString()));
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial))
	        	v.add("FECHA EGRESO: Factura Parcial");
        }
        comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
        //*******************************************************************************************************
    }
	
	/**
	 * @return Retorna valorPaciente.
	 */
	public double getValorTotalPaciente()
	{
		return valorTotalPaciente;
	}
	
	/**
	 * @param valorPaciente Asigna valorPaciente.
	 */
	public void setValorTotalPaciente(double valorPaciente)
	{
		this.valorTotalPaciente = valorPaciente;
	}
	
	public double consultarAbonos (Connection con, int idCuenta, int codigoPaciente, Integer ingreso) throws SQLException
	{
	    return estadoCuentaDao.consultarAbonos (con, idCuenta, codigoPaciente, ingreso) ;
	}
	
	/**
	 * Método para calcular el valor del total a pagar por el convenio
	 * @return Valor total de cargos por parte del convenio
	 */
	private double calcularTotalConvenio()
	{
		valorTotalConvenio=valorTotalCargos-valorTotalPaciente;
		return valorTotalConvenio;
	}

	/**
	 * @return Retorna valorTotalCargos.
	 */
	public double getValorTotalCargos()
	{
		return valorTotalCargos;
	}
	
	/**
	 * @param valorTotalCargos Asigna valorTotalCargos.
	 */
	public void setValorTotalCargos(double valorTotalCargos)
	{
		this.valorTotalCargos = valorTotalCargos;
	}
	/**
	 * @return Retorna valorTotalConvenio.
	 */
	public double getValorTotalConvenio()
	{
		return valorTotalConvenio;
	}
	/**
	 * @param valorTotalConvenio Asigna valorTotalConvenio.
	 */
	public void setValorTotalConvenio(double valorTotalConvenio)
	{
		this.valorTotalConvenio = valorTotalConvenio;
	}
	/**
	 * @return Retorna yaCargue.
	 */
	public boolean getYaCargue()
	{
		return yaCargue;
	}
	/**
	 * @param yaCargue Asigna yaCargue.
	 */
	public void setYaCargue(boolean yaCargue)
	{
		this.yaCargue = yaCargue;
	}

	/**
	 * @return the valorDevolucionPaciente
	 */
	public double getValorDevolucionPaciente() {
		return valorDevolucionPaciente;
	}

	/**
	 * @param valorDevolucionPaciente the valorDevolucionPaciente to set
	 */
	public void setValorDevolucionPaciente(double valorDevolucionPaciente) {
		this.valorDevolucionPaciente = valorDevolucionPaciente;
	}

	/**
	 * @return the valorNetoPaciente
	 */
	public double getValorNetoPaciente() {
		return valorNetoPaciente;
	}

	/**
	 * @param valorNetoPaciente the valorNetoPaciente to set
	 */
	public void setValorNetoPaciente(double valorNetoPaciente) {
		this.valorNetoPaciente = valorNetoPaciente;
	}

	/**
	 * @return the valorTotalAbonos
	 */
	public double getValorTotalAbonos() {
		return valorTotalAbonos;
	}

	/**
	 * @param valorTotalAbonos the valorTotalAbonos to set
	 */
	public void setValorTotalAbonos(double valorTotalAbonos) {
		this.valorTotalAbonos = valorTotalAbonos;
	}

	/**
	 * @return the valorSaldoMontoAutorizado
	 */
	public double getValorSaldoMontoAutorizado() {
		return valorSaldoMontoAutorizado;
	}

	/**
	 * @param valorSaldoMontoAutorizado the valorSaldoMontoAutorizado to set
	 */
	public void setValorSaldoMontoAutorizado(double valorSaldoMontoAutorizado) {
		this.valorSaldoMontoAutorizado = valorSaldoMontoAutorizado;
	}

	/**
	 * Metodo que obtiene la fecha para la validacion de los esquemas tarifarios vigentes.
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public String obtenerFechaValidacionEsquemas(Connection con,String idSubCuenta) 
	{
		return estadoCuentaDao.obtenerFechaValidacionEsquemas(con,idSubCuenta);
	}
	
	/**
	 * Método que carga el detalle de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleServicioArticuloSolicitud(Connection con,int numeroSolicitud,int codigoTipoSolicitud,int codigoSubCuenta,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("codigoTipoSolicitud",codigoTipoSolicitud);
		campos.put("codigoSubCuenta",codigoSubCuenta);
		campos.put("codigoInstitucion",codigoInstitucion);
		return estadoCuentaDao.cargarDetalleServicioArticuloSolicitud(con, campos);
		
	}
	/**
	 * Método que carga el detalle de una solicitud- tipo Covenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleServicioArticuloSolicitud(Connection con,int numeroSolicitud,int codigoTipoSolicitud,int codigoSubCuenta,int codigoInstitucion, String convenio)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("codigoTipoSolicitud",codigoTipoSolicitud);
		campos.put("codigoSubCuenta",codigoSubCuenta);
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("convenio",convenio);
		return estadoCuentaDao.cargarDetalleServicioArticuloSolicitud(con, campos);
		
	}
	/**
	 * 
	 * M&eacute;todo que carga el detalle de la solicitud en el pop up 
	 * @author Diana Ruiz
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitud
	 * @param codigoSubCuenta
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleServicioArticuloSolicitudPopUp(Connection con,int numeroSolicitud,int codigoTipoSolicitud,int codigoSubCuenta,int codigoInstitucion, String convenio)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("codigoTipoSolicitud",codigoTipoSolicitud);
		campos.put("codigoSubCuenta",codigoSubCuenta);
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("convenio",convenio);
		return estadoCuentaDao.cargarDetalleServicioArticuloSolicitudPopUp(con, campos);
		
	}
	
	/**
	 * 
	 * M&eacute;todo para verificar si para la solicitud hay despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitud
	 * @param codigoSubCuenta
	 * @param codigoInstitucion
	 * @return
	 */
	
	public boolean despachoEquivalentes(Connection con,int numeroSolicitud,int codigoTipoSolicitud,int codigoSubCuenta,int codigoInstitucion)
    {
        HashMap campos = new HashMap();
        campos.put("numeroSolicitud",numeroSolicitud);
        campos.put("codigoTipoSolicitud",codigoTipoSolicitud);
        campos.put("codigoSubCuenta",codigoSubCuenta);
        campos.put("codigoInstitucion",codigoInstitucion);
        return estadoCuentaDao.despachoEquivalentes(con, campos);
       
    }
	
	
}
