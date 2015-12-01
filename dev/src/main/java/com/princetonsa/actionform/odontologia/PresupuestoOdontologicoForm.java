package com.princetonsa.actionform.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoDetallePaquetePresupuesto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoPaquetesPresupuesto;
import util.odontologia.InfoPermisosPresupuesto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoPresupuestoPrecontratado;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.odontologia.DtoCitasPresupuestoOdo;
import com.princetonsa.dto.odontologia.DtoDetalleProximaCita;
import com.princetonsa.dto.odontologia.DtoFormatoImpresionContratoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoIngresoPresupuesto;
import com.princetonsa.dto.odontologia.DtoLogPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;
import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPresupuestoPaquetes;
import com.princetonsa.dto.odontologia.DtoPresupuestoPiezas;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalesContratadoPrecontratado;
import com.princetonsa.dto.odontologia.DtoProximaCita;
import com.princetonsa.dto.odontologia.DtoValorAnticipoPresupuesto;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.odontologia.MensajesPresupuesto;
import com.princetonsa.mundo.odontologia.ValidacionesPresupuesto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IControlAnticiposContratoMundo;
import com.servinte.axioma.orm.ControlAnticiposContrato;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * 
 * @author Wilson Rios, Edgar Carvajal Ruiz
 *
 */
public class PresupuestoOdontologicoForm extends ValidatorForm {
   
	
	
	/**
	 * Atributo para guardar tmp el estado del presupuesto
	 * Modificacion agosto 31.
	 * @author Edgar Carvajal
	 */
	private String estadoPresupuestoTmp;
	
	/**
	 * Validar Presupuesto Odon contratado
	 * Atributo para validar si el presupuesto esta contratado
	 * Solo se Utiliza en la presentacion grafica
	 * @author Edgar Carvajal
	 */
	private String validaPresupuestoOdoContratado; 
	
	/**
	 * Objeto para validar si se presenta mensajes informativos del presupuesto
	 */
	private MensajesPresupuesto mensajesPresupuesto = new MensajesPresupuesto();
	
	/**
	 * Atributo para validar si es Contratar o Precontratar
	 * Solo se Utiliza en presentacion grafica
	 */
	private String esContratar;
	
	/**
	 * Indica si se permite o no contratar dependiendo de los onvenios del paciente
	 */
	private boolean permitirContratar=false;
	
	/**
	 * Almacena el mensaje que será mostrado al usuario
	 * cuando desea contratar y no tiene relacionados los
	 * convenios precontratados.
	 */
	private String mensajeInformativoConveniosNoRelacionados;
	
	/**
	 * Lista para para cargar los ingresos historicos de un paciente que tuvo presupuesto Odontologico
	 * Esta lista es utilizada para mostrar los ingresos de los pacientes en interfaz grafica
	 */
	private  List<DtoHistoricoIngresoPresupuesto> listaHistoricoPresuIngreso;
	
	/**
	 *Dto para cargar el historico  de presupouesto 
	 */
	private  DtoHistoricoIngresoPresupuesto  dtoIngresoHistoricoPresu;
	
	/**
	 * Atributo tipo entero para almacenar temporalmente el indice de la lista de ingresos historicos presupuesto
	 * 
	 */
	private int postArrayIngresoHistoricos;
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int postArrayProgServicioUno;
	/**
	 * 
	 */
	private int postArrayProgServicioDos;
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(PresupuestoOdontologicoForm.class);
	
	/*
     *OBJETOS PARA CARGAR EL CONVENIO DEL PRESUPUESTO
     *Lista que sirve para cargar  los convenios del presupuesto 
     */
	private ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto =  new ArrayList<InfoConvenioContratoPresupuesto>();
	
	
	/**
	 * 
	 */
	private InfoConvenioContratoPresupuesto dtoInfoConvenioPresupuesto;
	
	
	/**
	 * 
	 */
	private String tipoSeccion;
	
	/**
	 * 
	 */
	private int posArrayDetalle;
	
	/**
	 * ArrayList para Cargar el Detalle Programa Servicios DEL PLAN DE TRATAMIENTO.........
	 * 
	 * */
	private ArrayList<InfoProgramaServicioPlan> listaDetalleServicioProgramaPlanTratamiento = new ArrayList<InfoProgramaServicioPlan>();
	
	
	/**
	 * 
	 * 
	 */
	private String mensaje ;
	/***
	 * 
	 * 
	 */
	private DtoPresupuestoOdontologico dtoPresupuesto = new DtoPresupuestoOdontologico();
	/***
	 * 
	 * 
	 */
	private ArrayList<DtoPresupuestoOdontologico> listaPresupuestos = new ArrayList<DtoPresupuestoOdontologico>();
	
	/***
	 * 
	 */
	private ArrayList<DtoLogPresupuestoOdontologico> listaPresupuestosTmp = new ArrayList<DtoLogPresupuestoOdontologico>();
	/**
	 * 
	 * 
	 */
	private ArrayList<DtoPresuOdoProgServ> listaProgramasServicios = new ArrayList<DtoPresuOdoProgServ>();
	
	/***
	 * 
	 * 
	 */
	private ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios= new ArrayList<DtoPresupuestoTotalConvenio>();
	
	/**
	 * 
	 * 
	 * 
	 */
	private ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConveniosContratar= new ArrayList<DtoPresupuestoTotalConvenio>();
	
	/**
	 * 
	 */
	private ArrayList<DtoMotivosAtencion> listaMotivos= new ArrayList<DtoMotivosAtencion>();
	
	/**
	 * 
	 */
	private ArrayList<DtoMotivoDescuento> listaMotivosAnulacionDcto= new ArrayList<DtoMotivoDescuento>();
	
	/**
	 * 
	 */
	private double codigoMotivoAnulacionDctoSel;
	
	/**
    * 
    * 
    */
	private String estado;
   
	/***
	 * 
	 */
	private String patronOrdenar;

	/**
	 * 
	 */
	private String esDescendente;
	
	/**
	 * 
	 */
	private int posArray;
	
	/**
	 * 
	 * 
	 */
	private String tipoRelacion;
	/**
	 * 
	 */
	private InfoPlanTratamiento dtoPlanTratamiento;
    /**
     * 
     * 
     */
	private boolean puedoEliminar;
	   
	/**
	 * 
	 * 
	 * 
	 */
	private ArrayList<DtoPresuOdoProgServ> programasServiciosAInsertar= new ArrayList<DtoPresuOdoProgServ>();
	
	/**
	 * 
	 */
	private int posArrayProgramaServicio;
		
	/**
	 * 
	 */
	private boolean modificando;
		
	/**
	 * 
	 */
	private boolean esNuevo;
	
	/**
	 * 
	 */
	private String siguientePagina;
	
	/**
	 * Objeto Info. Utilizado para cargar los permisos de los usuario frente la actividades del presupuesto
	 * Utilizado para mostrar  los mensajes de validacion frente a la pagina
	 */
	private InfoPermisosPresupuesto permisos;
	
	/**
	 * 
	 */
	private boolean calcularDescuentoOdontologico;
	
	/**
	 * 
	 * 
	 */
	private DtoPresupuestoTotalesContratadoPrecontratado dtoPresupuestoContratado = new DtoPresupuestoTotalesContratadoPrecontratado();
	
	/**
	 * 
	 */
	private String deseoPrecontratar;
	
	/**
	 * 
	 */
	private boolean existeDctoOdontologico;
	
	/**
	 * 
	 */
	private boolean existeIncumplimientoParaAplicarDcto;
	
	
	private ArrayList<String> warningsContratosVencidos;
	
	/**
	 * 
	 */
	private DtoProximaCita proximaCitaDto;
	
	/**
	 * 
	 */
	private ArrayList<InfoPresupuestoPrecontratado> listaPresupuestosPrecontratados;
	
	/**
	 * 
	 */
	private ArrayList<DtoValorAnticipoPresupuesto> listaAnticiposPresupuesto;
	
	/**
	 * 
	 */
	private InfoDatosDouble encabezadoHistorico; 
	
	/**
	 * 
	 */
	private ArrayList<DtoCitasPresupuestoOdo> listaCitasPresupuesto;
	
	/**
	 * 
	 */
	private int motivoCancelacionCita;
	
	/**
	 * 
	 */
	private ArrayList<HashMap> listaMotivosCancelacionCita;
	
	/**
	 * 
	 */
	private ArrayList<InfoPaquetesPresupuesto> listaPaquetes;
	
	/**
	 * codigo del paquete seleccionado o no seleccionado
	 */
	private int detallePaqueteOdonCONVENIOSeleccionado;
	
	/**
	 * 
	 */
	private ArrayList<InfoDetallePaquetePresupuesto> detallePaquete;
	
	
	/**
	 * Define la opcion seleccionada para imprimir.
	 * @author Cristhian Murillo
	 */
	private int indexOpcionImprimir;
	
	/**
	 * Lista de opciones para impresion segun las validaciones del sistema.
	 * En el momento de creacion de esta variable las opciones eran segun el anexo 881: 
	 * 	-Presupuesto
	 *  -Contrato
	 *  -Recomendaciones
	 *  -Otro Si ---> Se muestra de manera independiente cada Otro Si disponible para el presupuesto listaOpcionesImprimirOtrosSi
	 *  @author Cristhian Murillo
	 */
	private ArrayList<DtoCheckBox> listaOpcionesImprimirMostrar;
	
	/**
	 *  Lista con cada uno de los Otros Si disponible para el presupuesto
	 */
	private ArrayList<DtoOtroSi> listaOpcionesImprimirOtrosSi;
	
	
	/**
	 * Objeto con la informacion del reporte del contrato
	 */
	private DtoFormatoImpresionContratoOdontologico objImpresionContratoPresupuesto = new DtoFormatoImpresionContratoOdontologico();
	
	
	/**
	 * Objeto con la informacion del reporte de recomendaciones
	 */
	private DtoFormatoImpresionContratoOdontologico objImpresionRecomendacionesPresupuesto = new DtoFormatoImpresionContratoOdontologico();
	
	
	/**
	 * define si se debe motrar el contrato odontologico
	 */
	private boolean mostrarImpresionContratoOdonto = false;
	
	
	/**
	 * define si se debe motrar las recomendaciones del odontologico
	 */
	private boolean mostrarImpresionRecomendacionesOdonto = false;
	
	
	
	/** * Indica si el paciente cargado tiene ingreso abierto o no */
	private boolean tieneIngresoAbierto;
	
	/**
	 * 
	 */
	private long presupuestoID;
	
	private boolean detallePresupuestoOtraFuncionalidad;
	
	/**
	 * Atributo que guarda el valor de precontratar cuando se inicia
	 * el proceso de registro de la información. 
	 */
	private boolean precontratar;
	
	
	
	/**
	 * Código de la próxima cita registrada desde el proceso centralizado
	 * Próxima Cita Programada
	 */
	private int codigoProximaCitaRegistrada;
	
	/**
	 * Atributo que guarda temporalmente el estado antes de iniciar 
	 * el proceso centralizado de Proxima Cita con  el fin de reasignarlo 
	 * nuevamente cuando se retorne de dicho proceso.
	 */
	private String estadoTemporal;
	
	
	private String estadoOtraFuncionalidad;
	
	
	/**
	 * Atributo que indica si se debe o no iniciar el proceso centralizado 
	 * de asignación de próxima Cita
	 */
	private boolean abrePopUpProximaCita;
	
	
	/**
	 *CONTRATO ODONTOLOGICO  
	 */
	
	//private ContratoOdontologico contrataOdontologico;
	
	
	/***
	 * 
	 */
	public void reset()
	{	
		this.resetDetallePresupuesto();
		this.resetComun();
		this.listaPresupuestos = new ArrayList<DtoPresupuestoOdontologico>();		
		this.posArray= ConstantesBD.codigoNuncaValido;
		this.posArrayDetalle = ConstantesBD.codigoNuncaValido;
		this.listaPresupuestosTmp = new ArrayList<DtoLogPresupuestoOdontologico>();
		this.listaMotivos = new ArrayList<DtoMotivosAtencion>();
		this.listaMotivosAnulacionDcto= new ArrayList<DtoMotivoDescuento>();
		this.codigoMotivoAnulacionDctoSel= ConstantesBD.codigoNuncaValido;
		this.resetPlanTratamiento();
		this.puedoEliminar = false;
		this.posArrayDetalle=ConstantesBD.codigoNuncaValido;
		this.tipoSeccion="";
		this.programasServiciosAInsertar = new ArrayList<DtoPresuOdoProgServ>();
		this.listaSumatoriaConveniosContratar = new ArrayList<DtoPresupuestoTotalConvenio>();	
		this.listaConvenioPresupuesto = new ArrayList<InfoConvenioContratoPresupuesto>();
		this.dtoInfoConvenioPresupuesto = new InfoConvenioContratoPresupuesto();		
		this.posArrayProgramaServicio= ConstantesBD.codigoNuncaValido;
		this.modificando=false;
		this.postArrayProgServicioUno =0;
		this.postArrayProgServicioDos=0;
		this.esNuevo=false;
		this.siguientePagina="";
		this.calcularDescuentoOdontologico=false;
		this.encabezadoHistorico= new InfoDatosDouble();
		this.dtoPresupuestoContratado = new DtoPresupuestoTotalesContratadoPrecontratado();
		this.resetContratar();
		this.listaCitasPresupuesto= new ArrayList<DtoCitasPresupuestoOdo>();
		this.motivoCancelacionCita= 0;
		this.listaMotivosCancelacionCita= new ArrayList<HashMap>();
		this.indexOpcionImprimir=ConstantesBD.codigoNuncaValido;
		this.listaOpcionesImprimirMostrar = new ArrayList<DtoCheckBox>();
		this.listaOpcionesImprimirOtrosSi = new ArrayList<DtoOtroSi>();
		//this.contrataOdontologico= new ContratoOdontologico();
		
		this.mostrarImpresionContratoOdonto= false;
		this.mostrarImpresionRecomendacionesOdonto= false;
		
		resetPaquetes();
		
		this.estadoPresupuestoTmp="";
		this.validaPresupuestoOdoContratado="";
		
		/*
		 * 
		 */
		this.esContratar=ConstantesBD.acronimoNo;
		this.mensajesPresupuesto = new MensajesPresupuesto();
		
		this.listaHistoricoPresuIngreso=new ArrayList<DtoHistoricoIngresoPresupuesto>(); //lista para cargar los ingreso historicos
		this.postArrayIngresoHistoricos=ConstantesBD.codigoNuncaValido;
		this.dtoIngresoHistoricoPresu = new DtoHistoricoIngresoPresupuesto();
		this.detallePresupuestoOtraFuncionalidad=false;
		this.estadoOtraFuncionalidad = "";
		
		this.codigoProximaCitaRegistrada = ConstantesBD.codigoNuncaValido;
		this.precontratar = false;
		this.estadoTemporal = "";
		
		this.abrePopUpProximaCita = false;
		
	}

	public void resetPaquetes()
	{
		this.listaPaquetes= new ArrayList<InfoPaquetesPresupuesto>();
		this.detallePaqueteOdonCONVENIOSeleccionado= 0;
		this.detallePaquete= new ArrayList<InfoDetallePaquetePresupuesto>();
	}
	
	public void resetComun()
	{
		this.mensaje = "";
		this.patronOrdenar = "";
		this.esDescendente="";
		this.permisos= new InfoPermisosPresupuesto();
	}
	
	
	public void resetPlanTratamiento()
	{
		this.dtoPlanTratamiento= new InfoPlanTratamiento();
		this.listaDetalleServicioProgramaPlanTratamiento = new ArrayList<InfoProgramaServicioPlan>();
	}
	
	
	public void resetDetallePresupuesto()
	{
		this.dtoPresupuesto = new DtoPresupuestoOdontologico();
		this.listaProgramasServicios= new ArrayList<DtoPresuOdoProgServ>();
		this.listaSumatoriaConvenios= new ArrayList<DtoPresupuestoTotalConvenio>();
	}
	
	
	public void resetContratar()
	{
		this.deseoPrecontratar="";
		this.existeDctoOdontologico=false;
		this.existeIncumplimientoParaAplicarDcto=false;
		this.warningsContratosVencidos= new ArrayList<String>();
		this.proximaCitaDto= new DtoProximaCita();
		this.listaPresupuestosPrecontratados= new ArrayList<InfoPresupuestoPrecontratado>();
		this.listaAnticiposPresupuesto= new ArrayList<DtoValorAnticipoPresupuesto>();
	}
	
	public void resetModificar()
	{
		this.setPuedoEliminar(true);
		this.setModificando(true);
		this.setEsNuevo(false);
		this.dtoPlanTratamiento= new InfoPlanTratamiento();
		this.setPosArrayProgramaServicio(ConstantesBD.codigoNuncaValido);
		this.resetPaquetes();
	}
	
	/**
	 * 
	 * Metodo para limpiar los paquetes de los convenios
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public void resetPaquetesConvenios()
	{
		for(DtoPresuOdoProgServ dto: this.getDtoPresupuesto().getDtoPresuOdoProgServ())
		{
			for(DtoPresupuestoOdoConvenio dtoc: dto.getListPresupuestoOdoConvenio())
			{
				dtoc.setDtoPresupuestoPaquete(new DtoPresupuestoPaquetes());
			}
		}
	}
	
	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
		
	}

	/**
	 * @return the dtoPresupuesto
	 */
	public DtoPresupuestoOdontologico getDtoPresupuesto() {
		return dtoPresupuesto;
	}

	/**
	 * @param dtoPresupuesto the dtoPresupuesto to set
	 */
	public void setDtoPresupuesto(DtoPresupuestoOdontologico dtoPresupuesto) {
		this.dtoPresupuesto = dtoPresupuesto;
	}

	/**
	 * @return the listaPresupuestos
	 */
	public ArrayList<DtoPresupuestoOdontologico> getListaPresupuestos() {
		return listaPresupuestos;
	}

	/**
	 * @param listaPresupuestos the listaPresupuestos to set
	 */
	public void setListaPresupuestos(
			ArrayList<DtoPresupuestoOdontologico> listaPresupuestos) {
		this.listaPresupuestos = listaPresupuestos;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the listaSumatoriaConvenios
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> getListaSumatoriaConvenios() {
		return listaSumatoriaConvenios;
	}

	/**
	 * @param listaSumatoriaConvenios the listaSumatoriaConvenios to set
	 */
	public void setListaSumatoriaConvenios(
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios) {
		this.listaSumatoriaConvenios = listaSumatoriaConvenios;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> getSumatoria(){
		
		this.listaSumatoriaConvenios.clear();
		ArrayList<DtoPresupuestoTotalConvenio> listaTemporal = new ArrayList<DtoPresupuestoTotalConvenio>();
		listaTemporal = this.listaSumatoriaConvenios;
		TreeSet<Integer> treeTemp= new TreeSet<Integer>();
		
		for(DtoPresupuestoOdontologico dtop: listaPresupuestos)
		{
			for(DtoPresupuestoTotalConvenio tarifa: dtop.getListaTarifas())
			{
				if(!treeTemp.contains(tarifa.getConvenio().getCodigo()))
				{	
					treeTemp.add(tarifa.getConvenio().getCodigo());
					
					DtoPresupuestoTotalConvenio dto= new DtoPresupuestoTotalConvenio();
					dto.setConvenio(tarifa.getConvenio());
					dto.setContrato(tarifa.getContrato());
					dto.setPresupuesto(tarifa.getPresupuesto());
					dto.setValorSubTotalSinContratar(tarifa.getValorSubTotalSinContratar());
					dto.setValorSubTotalContratado(tarifa.getValorSubTotalContratado());
					listaTemporal.add(dto);
				}
			}
		}
		
		treeTemp=null;

		return listaTemporal;
	}
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> getSumatoriaPorPresupuesto(BigDecimal codigoPresupuesto)
	{
		ArrayList<DtoPresupuestoTotalConvenio> listaTemporal = new ArrayList<DtoPresupuestoTotalConvenio>();
		
		listaTemporal = this.listaSumatoriaConvenios;
		
		TreeSet<Integer> treeTemp= new TreeSet<Integer>();
		
		for(DtoPresupuestoOdontologico dtop: listaPresupuestos)
		{
			if(dtop.getCodigoPK() == codigoPresupuesto)
			{
				for(DtoPresupuestoTotalConvenio tarifa: dtop.getListaTarifas())
				{
					if(!treeTemp.contains(tarifa.getConvenio().getCodigo()))
					{	
						treeTemp.add(tarifa.getConvenio().getCodigo());
						DtoPresupuestoTotalConvenio dto= new DtoPresupuestoTotalConvenio();
						dto.setConvenio(tarifa.getConvenio());
						dto.setPresupuesto(tarifa.getPresupuesto());
						dto.setValorSubTotalContratado(tarifa.getValorSubTotalContratado());
						dto.setValorSubTotalSinContratar(tarifa.getValorSubTotalSinContratar());
						listaTemporal.add(dto);
					}
				}
			}
		}
		treeTemp=null;
		
		
		return listaTemporal;
		
		 
	}

	/***
	 * 
	 * 
	 * 
	 */
	public boolean existeContratado()
	{
	   for(DtoPresuOdoProgServ dtoProSer :this.getListaProgramasServicios())
	   {
		   for(DtoPresupuestoOdoConvenio dtoConvenio : dtoProSer.getListPresupuestoOdoConvenio())
		   {
			    if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
			    {
					 if(UtilidadTexto.isEmpty(dtoConvenio.getErrorCalculoTarifa()))
					 {
			    	
							  return true;
					 }
				}
			}
		}
	  return false;
   }
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		logger.info("VALIDATE DEL PRESUPUESTO --->"+this.getEstado());
		ActionErrors errores = new ActionErrors();
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
		
	
		this.mensajesPresupuesto.setPresentaMensajeInformativoContratarPresupusto(Boolean.FALSE);
		
		if(this.getEstado().equals("generarPresupuesto"))
		{
			this.setListaProgramasServicios(new ArrayList<DtoPresuOdoProgServ>());
		}
		
		if(this.getEstado().equals("generarPresupuesto") || this.getEstado().equals("adicionarProgramaServicio") || this.getEstado().equals("recalcularPresupuestoGenerado"))
		{
			logger.info("size-->"+this.getListaProgramasServicios().size());
			if(this.getListaProgramasServicios().size()<=0)
			{	
				validarSeleccionProgramas(errores);
			}	
			validarSeleccionConvenio(errores);
			
			if(errores.isEmpty())
			{
				validarNumeroSuperficiesAgrupamiento(errores);
			}
			
		}
		else if(this.getEstado().equals("contratar") || this.getEstado().equals("cargarPrecontratados"))
		{
			validarEntrarContratar(errores, paciente, usuario);
		}
		else if(this.getEstado().equals("guardarContratado") || 
			this.getEstado().equals("guardarPresupuestoPrecontratadoDirecto") )
		{
			try {
				validarGuardarContratar(errores, paciente, usuario.getCodigoInstitucionInt());
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
		}
		
		else if(this.getEstado().equals("guardarProximaCita"))
		{
			if(!UtilidadFecha.esFechaValidaSegunAp(this.getProximaCitaDto().getFecha()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Próxima Fecha "+this.getProximaCitaDto().getFecha()));
			}
			else
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getProximaCitaDto().getFecha(), UtilidadFecha.getFechaActual()))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Próxima Cita "+this.getProximaCitaDto().getFecha(), "Actual "+UtilidadFecha.getFechaActual()));
				}
			}
			
			////validamos que por lo menos seleccione un servicio
			boolean selServicio= false;
			for(DtoDetalleProximaCita dtoDetProximaCita: this.proximaCitaDto.getListaDetalle())
			{
				for(InfoServicios servicios: dtoDetProximaCita.getServicios())
				{
					if(servicios.getServicio().getActivo())
					{
						selServicio=true;
						break;
					}
				}
			}
			
			if(!selServicio)
			{
				errores.add("", new ActionMessage("errors.required", "Al menos un servicio"));
			}
		}
		if(!errores.isEmpty())
		{		
			if(this.getEstado().equals("contratar") || this.getEstado().equals("cargarPrecontratados"))
			{
				this.setEstado("mostrarErroresContratar");
			}
			else if(this.getEstado().equals("guardarContratado"))
			{
				this.setEstado("mostrarErroresContratado");
			}
			else if(this.getEstado().equals("guardarProximaCita"))
			{
				this.setEstado("mostrarErroresProximaCita");
			}
			else
			{
				this.setEstado("continuarPresupuesto");
			}
			
		}
		/*
		 *Mensaje Presupuesto 
		 */
		if( this.mensajesPresupuesto.isPresentaMensajeInformativoContratarPresupusto() )
		{
			this.setEstado("mostrarErroresContratar");
		}
		
		return errores;
	}

	/**
	 * @return the proximaCitaDto
	 */
	public DtoProximaCita getProximaCitaDto() {
		return proximaCitaDto;
	}

	/**
	 * @param proximaCitaDto the proximaCitaDto to set
	 */
	public void setProximaCitaDto(DtoProximaCita proximaCitaDto) {
		this.proximaCitaDto = proximaCitaDto;
	}

	/**
	 * @param errores
	 * @param paciente
	 * @throws IPSException 
	 */
	private void validarGuardarContratar(ActionErrors errores, PersonaBasica paciente, int codigoInstitucion) throws IPSException
	{
		logger.info("\n\n\n************************************ ENTRAMOS A VALIDAR EL GUARDAR DEL CONTRATAR*******************************************");
		
		//String mensajesPaquetes=this.getMensajePaquetes();
		
		/*
		 * VALIDACIO PAQUETES 
		 * xplanner 163987
		 * 1. No se permite contratar paquetes si esta vencidos
		 *  
		
		if(!UtilidadTexto.isEmpty(mensajesPaquetes))
		{
			errores.add("",	new ActionMessage("errors.notEspecific", mensajesPaquetes));
		}
		
		*/
		
	   //validar si se seleccion el programa en algun convenio para marcarlo como contratado
	   for(DtoPresuOdoProgServ dtoProSer :this.getListaProgramasServicios())
	   {
			dtoProSer.setContratado(ConstantesBD.acronimoNo);
			for(DtoPresupuestoOdoConvenio dtoConvenio : dtoProSer.getListPresupuestoOdoConvenio())
			{
				if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
				{
					dtoProSer.setContratado(ConstantesBD.acronimoSi);
				}
			}
		}
		
		logger.info("verificamos que haya seleccionado algo para contratar.......");
		if (!existeContratado())
		{
			logger.info("no contrató nada, error");
			errores.add("", new ActionMessage("error.odontologia.presupuesto.seleccionar", "Al menos un valor a contratar"));
		}
		
		/////si existe cambio en el total presupuesto para descuento entonces debemos evaluar si va ha generar o no una nueva solicitud de dcto
		if(this.getDtoPresupuestoContratado().getExisteCambioTotalPresupuestoParaDcto())
		{
			if(UtilidadTexto.isEmpty(this.getDtoPresupuestoContratado().getGenerarNuevaSolicitudDescuento()))
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Debe seleccionar si desea o no generar una nueva solicitud de dcto."));
			}
			if(this.getDtoPresupuestoContratado().getEstadoDescuentoNuevo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado)
				&& this.getCodigoMotivoAnulacionDctoSel()<=0)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Es requerido el ingreso del motivo de anulación"));
			}
		}
		else
		{	
			/////validamos si es requerido el motivo dea anulacion del dcto odontologico
			if(this.getDtoPresupuestoContratado().getEstadoDescuentoNuevo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado)
				&& this.getCodigoMotivoAnulacionDctoSel()<=0)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Si desea contratar y anular la solicitud de autorización de dcto odontológico entonces es requerido el ingreso del motivo de anulación"));
			}
		}	
		
		if(errores.isEmpty())
		{	
			/*VALIDACION 1. validar que los programas o servicios del plan de tratamiento esten en estado pendiente o cancelado*/
			logger.info("---------VALIDACION 1. validar que los programas o servicios del plan de tratamiento esten en estado pendiente o cancelado");
			for(DtoPresuOdoProgServ dtoProSer :this.getListaProgramasServicios())
			{
				for(DtoPresupuestoOdoConvenio dtoConvenio : dtoProSer.getListPresupuestoOdoConvenio())
				{
				    if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
				    {	
				    	for(DtoPresupuestoPiezas dtoPiezas: dtoProSer.getListPresupuestoPiezas())
				    	{
				    		if(ValidacionesPresupuesto.existeProgramaServicioPresupuestoEnPlanTratamiento(dtoPiezas.getCodigoPk(), ConstantesIntegridadDominio.acronimoEstadoPendiente, this.getUtilizaPrograma())
				    				.doubleValue()<=0)
				    		{
				    			if(ValidacionesPresupuesto.esProgramaServicioAdicionPlanTtoPresupuesto(dtoPiezas.getPieza(), dtoPiezas.getSuperficie(), dtoPiezas.getHallazgo(), dtoPiezas.getSeccion(), this.getDtoPresupuesto().getCodigoPK(), this.getUtilizaPrograma(), new BigDecimal(dtoProSer.getProgramaOServicio(this.getUtilizaPrograma())))
				    					.doubleValue()<=0)
				    			{
					    			if(this.getUtilizaPrograma())
					    			{	
					    				errores.add("", new ActionMessage("errors.notEspecific", "El programa "+dtoProSer.getPrograma().getNombre()+" NO esta en estado pendiente en el plan de tratamiento"));
					    			}
					    			else
					    			{
					    				errores.add("", new ActionMessage("errors.notEspecific", "El servicio "+dtoProSer.getServicio().getNombre()+" NO esta en estado pendiente en el plan de tratamiento"));
					    			}
				    			}	
				    		}
				    	}	
				    }
				}
			}	
			
			if(errores.isEmpty())
			{	
				/*VALIDACION 2. Se debe verificar en los convenios con los cuales se va ha contratar el campo 'tipo atencion convenio'
				 * Si es atencion General continua, 
				 * Si es atencion Odontologica Especial valida*/
				
				logger.info("------------VALIDACION 2. Se debe verificar en los convenios con los cuales se va ha contratar el campo 'tipo atencion convenio'");
				logger.info("Si es atencion General continua,");
				logger.info("Si es atencion Odontologica Especial valida");
				
				this.setListaAnticiposPresupuesto(new ArrayList<DtoValorAnticipoPresupuesto>());
				
				IControlAnticiposContratoMundo controlAnticiposContratoMundo = FacturacionFabricaMundo.crearControlAnticiposContratoMundo();

				
				for(DtoPresupuestoTotalConvenio dtototal : this.getListaSumatoriaConvenios())
				{
					if(dtototal.getTotalContratado().doubleValue()>0)
					{	
						logger.info("**************** CONVENIO EVALUADO "+dtototal.getConvenio().getCodigo()+" "+dtototal.getConvenio().getNombre());
						if(!ValidacionesPresupuesto.puedoContratarXTipoAtencionConvenio(dtototal.getConvenio().getCodigo()))
						{
							// es atencion odontologica especial
							logger.info("es atencion odontologica especial, entonces debemos hacer varias validaciones");
							boolean pacientePagaAtencion= Contrato.pacientePagaAtencion(dtototal.getContrato());
							boolean validarAbonoAtencionOdo= Contrato.pacienteValidaBonoAtenOdo(dtototal.getContrato());
							
							logger.info("pacientePagaAtencion-->"+pacientePagaAtencion);
							logger.info("validarAbonoAtencionOdo-->"+validarAbonoAtencionOdo);
							
							/*3.a Paciente paga la atencion= 'S' y Validar Abono Para atencion odontologica='N' se permite contratar*/
							if(pacientePagaAtencion && !validarAbonoAtencionOdo)
							{
								logger.info("3.a Paciente paga la atencion= 'S' y Validar Abono Para atencion odontologica='N' se permite contratar");
								logger.info("SE PUEDE CONTRATAR !!!!!!");
							}
							/*3.b Paciente paga la atencion= 'S' y Validar Abono Para atencion odontologica='S' */
							else if(pacientePagaAtencion && validarAbonoAtencionOdo)
							{
								logger.info("3.b Paciente paga la atencion= 'S' y Validar Abono Para atencion odontologica='S'");
								///se verifica que el saldo de abono del paciente es igual a cero, se permite contratar
								if(ValidacionesPresupuesto.poseeAbonosDisponibles(paciente.getCodigoPersona(), paciente.getCodigoIngreso(), codigoInstitucion))
								{
									logger.info("puedo contratar tiene abono disponible");
								}
								///si el saldo es menor igual a cero no se permite contratar
								else
								{
									logger.info("NO puede contratar NO tiene abono disponible");
									
									
									// XPLANNER   Integración_Abr2810_"doc"_Quitar validación cuando el paciente no tenga abonos [id=157103]
									//errores.add("", new ActionMessage("error.odontologia.noTieneSaldoDisponible", dtototal.getConvenio().getNombre()));
								}
							}
							/*3.c Paciente paga la atencion= 'N'*/
							else if(!pacientePagaAtencion)
							{
								logger.info("3.c Paciente paga la atencion= 'N'");
								//y el parametro requiere anticipo para contratar Presupuesto/atender paciente= 'N' permito contratar
								boolean controlaAnticipoContratar= Contrato.controlaAnticipos(dtototal.getContrato());
								logger.info("controlaAnticipoContratar-->"+controlaAnticipoContratar);
								
								if(!controlaAnticipoContratar)
								{
									logger.info("puedo contratar porque no requiere anticipo");
								}
								else
								{
									ArrayList<ControlAnticiposContrato> listaControlAnticiposContrato = new ArrayList<ControlAnticiposContrato>();
									boolean requiereAnticipoContratar = false;
									
									UtilidadTransaccion.getTransaccion().begin();
									listaControlAnticiposContrato = controlAnticiposContratoMundo.determinarContratoRequiereAnticipo(dtototal.getContrato());
									UtilidadTransaccion.getTransaccion().commit();
									
									if(!Utilidades.isEmpty(listaControlAnticiposContrato))
									{
										requiereAnticipoContratar = true;
									}
									
									logger.info("requiereAnticipoContratar-->"+requiereAnticipoContratar);
									
									if(requiereAnticipoContratar)
									{
										/*3.d para este caso se debe validar:*/
										logger.info("requiere anticipo, 3.d para este caso se debe validar:");
										
										//Valor anticipo disponible
										//Si el valor total del convenio X del presupuesto es <= 'valor anticipo disponible' del convenio entonces se debe contratar
										if(ValidacionesPresupuesto.esValorPresupuestoMayorAnticipo(dtototal.getTotalContratado(), dtototal.getContrato()))
										{
											logger.info("el valor del presupuesto es mayor al anticipo, entonces NO puedo contratar");
											errores.add("", new ActionMessage("error.odontologia.presupuestoMayorQueAnticipo",dtototal.getConvenio().getNombre()));
										}
										else
										{
											logger.info("puedo contratar el valor del anticipo es mayor al valor total del convenio");
										}
										
										///ahora validamos el valor máximo por paciente
										logger.info("Validamos el valor total convenio es mayor al valor max paciente");
										if (ValidacionesPresupuesto.esValorTotalConvenioMayorValorMaximoXPaciente(dtototal.getContrato(), dtototal.getTotalContratado()))
										{
											logger.info("el valor total del convenio es mayor al maxio x paciente, NO PUEDO CONTRATAR");
											 errores.add("", new ActionMessage("error.odontologia.presupuestoMayorMaximoPorPaciente", dtototal.getConvenio().getNombre()));
										}
										else
										{
											logger.info("puedo contratar el valor del maximo por paciente es menor igual al valor total convenio");
										}
										
										logger.info(" ************ TOTAL CONTRATADO ="+dtototal.getTotalContratado()+" CONTRATO: "+dtototal.getContrato());
										/////si todo salió bien entonces debemos setear los valores de los anticipos que debemos actualizar
										if(errores.isEmpty())
										{
											logger.info(" ************ TOTAL CONTRATADO ="+dtototal.getTotalContratado()+" CONTRATO: "+dtototal.getContrato());
											this.getListaAnticiposPresupuesto().add(new DtoValorAnticipoPresupuesto(dtototal.getTotalContratado(), dtototal.getContrato()));
										}
										
									}
									
								}
							}
						}
						else
						{
							logger.info("El tipo de atencion es general.... entonces lo puedo contratar de una!!!!!");
						}
					}
					else
					{
						logger.info("convenio sin nada para contratar valor=0");
					}
				}
			}
		}	
	}

	/**6+
	 * @param errores
	 * @param paciente
	 */
	private void validarEntrarContratar(ActionErrors errores,
			PersonaBasica paciente , UsuarioBasico usuario)
	{
		this.mensajesPresupuesto = new MensajesPresupuesto(); //Limpiar mensajes Presupuesto
		
		
		logger.info("**************************************************************** AL ENTRAR A CONTRATAR ********************************************************");   	
		logger.info("EL CODIGO DEL PLAN ES  ********************************************************************************************************************************************************************"+ this.getDtoPlanTratamiento().getCodigoPk());
		
		/*for(DtoPresupuestoTotalConvenio dtototal: this.getDtoPresupuesto().getListaTarifas())
		{
			logger.info("valor subtotal sin contratar-->"+dtototal.getValorSubTotalSinContratar());
			if(dtototal.getValorSubTotalSinContratar().doubleValue()>0)
			{	
				logger.info("************************************* CONVENIO EVALUADO"+dtototal.getConvenio().getCodigo());
				if(!dtototal.getExisteConvenioEnIngreso())
				{
					logger.info("SI VALIDA ERROR ValidacionesPresupuesto.existeConvenioEnIngreso ***************************");
					//errores.add("", new ActionMessage("error.odontologia.convenioNoContrata", dtototal.getConvenio().getNombre()));
				}
			}	
		}*/

		/*
		 * Estados a buscar
		 * Contratado contratado.
		 * Contratado Suspendido
		 */
		ArrayList<String> arrayEstados = new ArrayList<String>();
		arrayEstados.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
		arrayEstados.add(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente);

		/*
		 * No debe existir Realizar la actividad de Contratar dado estos estados:  Contratado contratado, Contratado Suspendido
		 */
		if(ValidacionesPresupuesto.existenPresupuestosDadosEstados(paciente.getCodigoIngreso() , arrayEstados))
		{
			
			this.mensajesPresupuesto.setPresentaMensajeInformativoContratarPresupusto(Boolean.TRUE);
			/*
			 *Segun los estandares este mensaje no es un error es informativo 
			 */
			//errores.add("", new ActionMessage("error.odontologia.estadoNoContrata"));
		}
		
		if(ValidacionesPresupuesto.esPlanTratamientoPorConfirmar(this.getDtoPlanTratamiento().getCodigoPk()))
		{
			logger.info("SI VALIDA ERROR esPlanTratamientoPorConfirmar ***************************");
			errores.add("", new ActionMessage("error.odontologia.esPlanPorConfirmar"));
		}
		
		arrayEstados.clear();
		arrayEstados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		arrayEstados.add(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
		if(!ValidacionesPresupuesto.tienePlanTratamientoEstados(this.getDtoPlanTratamiento().getCodigoPk() , arrayEstados))
		{
			logger.info("SI VALIDA ERROR tienePlanTratamientoEstados ***************************");
			errores.add("", new ActionMessage("error.odontologia.noTieneEstadosPlanTratamiento"));
		}
		
		
		  //forma.setDtoPresupuesto((DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		 DtoPresupuestoOdontologico tmpPresupuesto= (DtoPresupuestoOdontologico)this.listaPresupuestos.get(this.getPosArray()).clone();
		  
	
		 if(usuario.getCodigoCentroAtencion()!= tmpPresupuesto.getCentroAtencion().getCodigo())
		 {
			errores.add("",	new ActionMessage("errors.notEspecific"," El Presupuesto fue generado en otro centro de atención, No se permite Contratar")); 
		 }
		
	}

	/**
	 * Valida la sección de los convenios
	 * @param errores Objeto para almacenar los posibles errores presentados
	 */
	private void validarSeleccionConvenio(ActionErrors errores) 
	{
		boolean seleccionoConvenio=false;
		for(InfoConvenioContratoPresupuesto infoConvenio:  this.getListaConvenioPresupuesto())
		{
			 if(infoConvenio.getActivo())
			 {	 
				 seleccionoConvenio=true;
			 }
		}
		if(!seleccionoConvenio)
		{
			errores.add("", new ActionMessage("error.odontologia.presupuesto.seleccionar","un Convenio/Contrato"));
		}
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarSeleccionProgramas(ActionErrors errores) {
		int bandera=0;
		for(InfoDetallePlanTramiento objPlan : this.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
		 {
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 if(objServProg.getActivo())
					 {
						 bandera++;
					 }
				 }
			 }
		 }
		for(InfoDetallePlanTramiento objPlan : this.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
		 {
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 if(objServProg.getActivo())
					 {
						 bandera++;
					 }
				 }
			 }
		 }
		for(InfoHallazgoSuperficie objHallazgo : this.getDtoPlanTratamiento().getSeccionHallazgosBoca())
		 {
			 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
			 {
				 if(objServProg.getActivo())
				 {
					 bandera++;
				 }
			 }
		 }
		if(bandera<=0)
		{
			errores.add("", new ActionMessage("error.odontologia.presupuesto.seleccionar","un "+this.tipoRelacion+" del Plan de Tratamiento"));
		}
		
	}
	
		/**
		 * 
		 * @param errores
		 */
		private void validarNumeroSuperficiesAgrupamiento(ActionErrors errores) 
		{
			ArrayList<String> arrayKeyErrores= new ArrayList<String>();
			for(InfoDetallePlanTramiento objPlan : this.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
			{
				for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
				{
					generarErroresNumeroSuperficies(errores, arrayKeyErrores,objHallazgo, objPlan.getPieza());
				}
			}
			
			for(InfoDetallePlanTramiento objPlan : this.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
			{
				for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
				{
					generarErroresNumeroSuperficies(errores, arrayKeyErrores,objHallazgo, objPlan.getPieza());
				}
			}
			
			/*for(InfoHallazgoSuperficie objHallazgo : this.getDtoPlanTratamiento().getSeccionHallazgosBoca())
			{
				generarErroresNumeroSuperficies(errores, arrayKeyErrores,objHallazgo);
			}*/
		}
	
		/**
		 * @param errores
		 * @param arrayKeyErrores
		 * @param objHallazgo
		 */
		private void generarErroresNumeroSuperficies(ActionErrors errores,
				ArrayList<String> arrayKeyErrores,
				InfoHallazgoSuperficie objHallazgo, InfoDatosInt pieza) 
		{
			for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
			{
				if(objServProg.getActivo() && !objServProg.isAsignadoAlPresupuesto())
				{
					if(objServProg.getNumeroSuperficies()>0 && !arrayKeyErrores.contains(objServProg.getCodigoPkProgramaServicio()+"_"+objHallazgo.getHallazgoREQUERIDO().getCodigo()+"_"+pieza.getCodigo()))
					{	
						int numeroProgramasServAgrupado= obtenerAgrupamientoHallazgoProgramaServ(objServProg.getCodigoPkProgramaServicio(), objHallazgo.getHallazgoREQUERIDO().getCodigo(), arrayKeyErrores, pieza);
						
						//existen menos agrupaciones
						if(numeroProgramasServAgrupado<objServProg.getNumeroSuperficies())
						{
							arrayKeyErrores.add(objServProg.getCodigoPkProgramaServicio()+"_"+objHallazgo.getHallazgoREQUERIDO().getCodigo()+"_"+pieza.getCodigo());
							errores.add("", new ActionMessage("errors.notEspecific","El numero de superficies para el "+this.getTipoRelacion()+"/Hallazgo:  -" +objServProg.getNombreProgramaServicio()+" / "+objHallazgo.getHallazgoREQUERIDO().getNombre()+"- de la pieza "+pieza.getNombre()+" es "+objServProg.getNumeroSuperficies()+", solamente ha seleccionado "+numeroProgramasServAgrupado));
						}
						////existen mas agrupaciones incompletas
						else if((numeroProgramasServAgrupado%objServProg.getNumeroSuperficies())!=0)
						{
							arrayKeyErrores.add(objServProg.getCodigoPkProgramaServicio()+"_"+objHallazgo.getHallazgoREQUERIDO().getCodigo()+"_"+pieza.getCodigo());
							errores.add("", new ActionMessage("errors.notEspecific","El numero de superficies para el "+this.getTipoRelacion()+"/Hallazgo: -" +objServProg.getNombreProgramaServicio()+" / "+objHallazgo.getHallazgoREQUERIDO().getNombre()+"- de la pieza "+pieza.getNombre()+" debe ser multiplo de "+objServProg.getNumeroSuperficies()+", ha seleccionado "+numeroProgramasServAgrupado));
						}	
					}	
				}
			}
		}
	
		/**
		 * 
		 * @param codigoPkProgramaServicio
		 * @param codigo
		 * @param arrayKeyErrores
		 * @return
		 */
		private int obtenerAgrupamientoHallazgoProgramaServ(
				BigDecimal codigoPkProgramaServicio, 
				int codigoHallazgo, ArrayList<String> arrayKeyErrores,
				InfoDatosInt pieza) 
		{
			int contador=0;
			
			logger.info("********************CONTADOR *****************========================================================");
			
			for(InfoDetallePlanTramiento objPlan : this.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
			{
				if(pieza.getCodigo()== objPlan.getPieza().getCodigo())
				{	
					for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
					{
						contador = agruparProgServicioHallazgo(
								codigoPkProgramaServicio, codigoHallazgo, contador,
								objHallazgo);	
						
						logger.info("TERMINA 1");
						
					}
				}	
			}
			
			for(InfoDetallePlanTramiento objPlan : this.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
			{
				if(pieza.getCodigo()== objPlan.getPieza().getCodigo())
				{
					for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
					{
						contador = agruparProgServicioHallazgo(
								codigoPkProgramaServicio, codigoHallazgo, contador,
								objHallazgo);
						
						logger.info("TERMINA 2");
						
					}
				}	
			}
			
			/*for(InfoHallazgoSuperficie objHallazgo : this.getDtoPlanTratamiento().getSeccionHallazgosBoca())
			{
				contador = agruparProgServicioHallazgo(codigoPkProgramaServicio,
						codigoHallazgo, contador, objHallazgo);
				logger.info("TERMINA 3");
			}*/
			
			logger.info("********************FIN CONTADOR *****************========================================================");
			
			return contador;
		}
	
		/**
		 * @param codigoPkProgramaServicio
		 * @param codigoHallazgo
		 * @param contador
		 * @param objHallazgo
		 * @return
		 */
		private int agruparProgServicioHallazgo(
				BigDecimal codigoPkProgramaServicio, int codigoHallazgo,
				int contador, InfoHallazgoSuperficie objHallazgo) 
		{
			logger.info("objHallazgo.getHallazgoREQUERIDO().getCodigo()--> "+objHallazgo.getHallazgoREQUERIDO().getCodigo()+" codigoHallazgo-->"+codigoHallazgo);
			if(objHallazgo.getHallazgoREQUERIDO().getCodigo()== codigoHallazgo)
			{	
				logger.info("entra!!!!!!");
				for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				{
					logger.info("objServProg.getActivo()->"+objServProg.getActivo()+" objServProg.isAsignadoAlPresupuesto()->"+objServProg.isAsignadoAlPresupuesto()+" codigoPkProgramaServicio->"+codigoPkProgramaServicio+" objServProg.getCodigoPkProgramaServicio()-->"+objServProg.getCodigoPkProgramaServicio());
					if(objServProg.getActivo() 
							&& !objServProg.isAsignadoAlPresupuesto() 
							&& codigoPkProgramaServicio.doubleValue()==objServProg.getCodigoPkProgramaServicio().doubleValue())
					{
						logger.info("incrementa contador");
						contador++;
					}
				}
			}
			return contador;
		}
	
	/**
	 * @return the listaProgramasServicios
	 */
	public ArrayList<DtoPresuOdoProgServ> getListaProgramasServicios() {
		return listaProgramasServicios;
	}
	
	/**
	 * @param listaProgramasServicios the listaProgramasServicios to set
	 */
	public void setListaProgramasServicios(
			ArrayList<DtoPresuOdoProgServ> listaProgramasServicios) {
		this.listaProgramasServicios = listaProgramasServicios;
	}
	
	
	/**
	 * @return the tipoRelacion
	 */
	public String getTipoRelacion() {
		return tipoRelacion;
	}
	
	/**
	 * @param tipoRelacion the tipoRelacion to set
	 */
	public void setTipoRelacion(String tipoRelacion) {
		this.tipoRelacion = tipoRelacion;
	}
	
	/**
	 * @return the listaMotivos
	 */
	public ArrayList<DtoMotivosAtencion> getListaMotivos() {
		return listaMotivos;
	}
	
	/**
	 * @param listaMotivos the listaMotivos to set
	 */
	public void setListaMotivos(ArrayList<DtoMotivosAtencion> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}
	
	/**
	 * @return the listaPresupuestosTmp
	 */
	public ArrayList<DtoLogPresupuestoOdontologico> getListaPresupuestosTmp() {
		return listaPresupuestosTmp;
	}
	
	/**
	 * @param listaPresupuestosTmp the listaPresupuestosTmp to set
	 */
	public void setListaPresupuestosTmp(
			ArrayList<DtoLogPresupuestoOdontologico> listaPresupuestosTmp) {
		this.listaPresupuestosTmp = listaPresupuestosTmp;
	}
	
	/**
	 * @return the dtoPlanTratamiento
	 */
	public InfoPlanTratamiento getDtoPlanTratamiento() {
		return dtoPlanTratamiento;
	}
	
	/**
	 * @param dtoPlanTratamiento the dtoPlanTratamiento to set
	 */
	public void setDtoPlanTratamiento(InfoPlanTratamiento dtoPlanTratamiento) {
		this.dtoPlanTratamiento = dtoPlanTratamiento;
	}
	
	/**
	 * @return the 
	 * 
	 */
	public boolean isPuedoEliminar() {
		return puedoEliminar;
	}
	
	/**
	 * @param esCopia the  to set
	 * 
	 * 
	 */
	public void setPuedoEliminar(boolean puedoEliminar) {
		this.puedoEliminar = puedoEliminar;
	}
	
	
	
	public ArrayList<InfoProgramaServicioPlan> getListaDetalleServicioProgramaPlanTratamiento() {
		return listaDetalleServicioProgramaPlanTratamiento;
	}
	
	
	
	public int getPosArrayDetalle() {
		return posArrayDetalle;
	}
	
	
	
	public void setTipoSeccion(String tipoSeccion) {
		this.tipoSeccion = tipoSeccion;
	}
	
	public String getTipoSeccion() {
		return tipoSeccion;
	}
	
	
	public void setListaDetalleServicioProgramaPlanTratamiento(
			ArrayList<InfoProgramaServicioPlan> listaDetalleServicioPrograma) {
		this.listaDetalleServicioProgramaPlanTratamiento = listaDetalleServicioPrograma;
	}
	
	/**
	 * @param posArrayDetalle the posArrayDetalle to set
	 */
	public void setPosArrayDetalle(int posArrayDetalle) {
		this.posArrayDetalle = posArrayDetalle;
	}
	
	
	/**
	 * @return the programasServiciosAInsertar
	 */
	public ArrayList<DtoPresuOdoProgServ> getProgramasServiciosAInsertar() {
		return programasServiciosAInsertar;
	}
	
	/**
	 * @param programasServiciosAInsertar the programasServiciosAInsertar to set
	 */
	public void setProgramasServiciosAInsertar(
			ArrayList<DtoPresuOdoProgServ> programasServiciosAInsertar) {
		this.programasServiciosAInsertar = programasServiciosAInsertar;
	}
	
	/**
	 * @return the listaSumatoriaConveniosContratar
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> getListaSumatoriaConveniosContratar() {
		return listaSumatoriaConveniosContratar;
	}
	
	/**
	 * @param listaSumatoriaConveniosContratar the listaSumatoriaConveniosContratar to set
	 */
	public void setListaSumatoriaConveniosContratar(
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConveniosContratar) {
		this.listaSumatoriaConveniosContratar = listaSumatoriaConveniosContratar;
	}
	
	
		
	
	
	public void setListaConvenioPresupuesto(ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto) {
		this.listaConvenioPresupuesto = listaConvenioPresupuesto;
	}
	
	public ArrayList<InfoConvenioContratoPresupuesto> getListaConvenioPresupuesto() {
		return listaConvenioPresupuesto;
	}
	
	public void setDtoInfoConvenioPresupuesto(InfoConvenioContratoPresupuesto dtoInfoConvenioPresupuesto) {
		this.dtoInfoConvenioPresupuesto = dtoInfoConvenioPresupuesto;
	}
	
	public InfoConvenioContratoPresupuesto getDtoInfoConvenioPresupuesto() {
		return dtoInfoConvenioPresupuesto;
	}
	
	/**
	 * @return the posArrayProgramaServicio
	 */
	public int getPosArrayProgramaServicio()
	{
		return posArrayProgramaServicio;
	}
	
	/**
	 * @param posArrayProgramaServicio the posArrayProgramaServicio to set
	 */
	public void setPosArrayProgramaServicio(int posArrayProgramaServicio)
	{
		this.posArrayProgramaServicio = posArrayProgramaServicio;
	}
	
	/**
	 * @return the modificando
	 */
	public boolean getModificando()
	{
		return modificando;
	}
	
	/**
	 * @return the modificando
	 */
	public boolean isModificando()
	{
		return modificando;
	}
	
	
	/**
	 * @param modificando the modificando to set
	 */
	public void setModificando(boolean modificando)
	{
		this.modificando = modificando;
	}
	
	/**
	 * @return the postArrayProgServicioUno
	 */
	public int getPostArrayProgServicioUno() {
		return postArrayProgServicioUno;
	}
	
	/**
	 * @param postArrayProgServicioUno the postArrayProgServicioUno to set
	 */
	public void setPostArrayProgServicioUno(int postArrayProgServicioUno) {
		this.postArrayProgServicioUno = postArrayProgServicioUno;
	}
	
	/**
	 * @return the postArrayProgServicioDos
	 */
	public int getPostArrayProgServicioDos() {
		return postArrayProgServicioDos;
	}
	
	/**
	 * @param postArrayProgServicioDos the postArrayProgServicioDos to set
	 */
	public void setPostArrayProgServicioDos(int postArrayProgServicioDos) {
		this.postArrayProgServicioDos = postArrayProgServicioDos;
	}
	
	public boolean getUtilizaPrograma()
	{
		if(this.getTipoRelacion().equalsIgnoreCase("Programa") || this.getTipoRelacion().equalsIgnoreCase("Programas"))
			return true;
		return false;
	}
	
	/**
	 * @return the esNuevo
	 */
	public boolean isEsNuevo()
	{
		return esNuevo;
	}
	
	/**
	 * @param esNuevo the esNuevo to set
	 */
	public void setEsNuevo(boolean esNuevo)
	{
		this.esNuevo = esNuevo;
	}
	
	/**
	 * @return the esNuevo
	 */
	public boolean getEsNuevo()
	{
		return esNuevo;
	}
	
	/**
	 * @return the siguientePagina
	 */
	public String getSiguientePagina()
	{
		return siguientePagina;
	}
	
	/**
	 * @param siguientePagina the siguientePagina to set
	 */
	public void setSiguientePagina(String siguientePagina)
	{
		this.siguientePagina = siguientePagina;
	}
	
	/**
	 * @return the permisos
	 */
	public InfoPermisosPresupuesto getPermisos()
	{
		return permisos;
	}
	
	/**
	 * @param permisos the permisos to set
	 */
	public void setPermisos(InfoPermisosPresupuesto permisos)
	{
		this.permisos = permisos;
	}
	
	/**
	 * @return the calcularDescuentoOdontologico
	 */
	public boolean isCalcularDescuentoOdontologico()
	{
		return calcularDescuentoOdontologico;
	}
	
	/**
	 * @param calcularDescuentoOdontologico the calcularDescuentoOdontologico to set
	 */
	public void setCalcularDescuentoOdontologico(
			boolean calcularDescuentoOdontologico)
	{
		this.calcularDescuentoOdontologico = calcularDescuentoOdontologico;
	}
	
	/**
	 * @return the calcularDescuentoOdontologico
	 */
	public boolean getCalcularDescuentoOdontologico()
	{
		return calcularDescuentoOdontologico;
	}
	
	/**
	 * @return the dtoPresupuestoContratado
	 */
	public DtoPresupuestoTotalesContratadoPrecontratado getDtoPresupuestoContratado() {
		return dtoPresupuestoContratado;
	}
	
	/**
	 * @param dtoPresupuestoContratado the dtoPresupuestoContratado to set
	 */
	public void setDtoPresupuestoContratado(
			DtoPresupuestoTotalesContratadoPrecontratado dtoPresupuestoContratado) {
		this.dtoPresupuestoContratado = dtoPresupuestoContratado;
	}
	
	public ArrayList<DtoCheckBox> getListaOpcionesImprimirMostrar() {
		return listaOpcionesImprimirMostrar;
	}
	
	public void setListaOpcionesImprimirMostrar(
			ArrayList<DtoCheckBox> listaOpcionesImprimirMostrar) {
		this.listaOpcionesImprimirMostrar = listaOpcionesImprimirMostrar;
	}
	
	public BigDecimal totalPorPresupuesto()
	{
		
		 double totalFinal = 0;
		for(DtoPresupuestoTotalConvenio total : this.listaSumatoriaConvenios)
		{
		   logger.info("TOTAL -------------__>"+total.getTotalContratado());
		   
		   totalFinal=  totalFinal + total.getTotalContratado().doubleValue();
		   	logger.info(totalFinal);
		}
		
		logger.info("TOTAL FINAL --------_>"+totalFinal);
		return new BigDecimal(totalFinal);
	}
	
	public BigDecimal totalPorDescuentoPresupuesto()
	{
		double totalFinal =0;
	
		for(DtoPresuOdoProgServ dtop : this.getListaProgramasServicios())
		{
			for(DtoPresupuestoOdoConvenio detalle: dtop.getListPresupuestoOdoConvenio())
			{
				logger.info((detalle.getValorDescuentoBono().doubleValue()<=0) +"---"+ (detalle.getValorDescuentoPromocion().doubleValue()<=0));
				if(detalle.getValorDescuentoBono().doubleValue()<= 0 && detalle.getValorDescuentoPromocion().doubleValue()<=0 && detalle.getContratado().equals(ConstantesBD.acronimoSi)
					&& detalle.getDtoPresupuestoPaquete().getCodigoPk().doubleValue()<=0)
				{
					totalFinal=	totalFinal + ( detalle.getValorUnitarioMenosDctoComercial().doubleValue() * dtop.getCantidad());
					logger.info(totalFinal);
				}
			}
		}
		
		logger.info("TOTAL FINAL --------_>"+totalFinal);
		return new BigDecimal(totalFinal);
		
		
	}
	
	/**
	 * @return the deseoPrecontratar
	 */
	public String getDeseoPrecontratar()
	{
		return deseoPrecontratar;
	}
	
	/**
	 * @param deseoPrecontratar the deseoPrecontratar to set
	 */
	public void setDeseoPrecontratar(String deseoPrecontratar)
	{
		this.deseoPrecontratar = deseoPrecontratar;
	}
	
	/**
	 * @return the existeDctoOdontologico
	 */
	public boolean isExisteDctoOdontologico()
	{
		return existeDctoOdontologico;
	}
	
	/**
	 * @return the existeDctoOdontologico
	 */
	public boolean getExisteDctoOdontologico()
	{
		return existeDctoOdontologico;
	}
	
	
	/**
	 * @param existeDctoOdontologico the existeDctoOdontologico to set
	 */
	public void setExisteDctoOdontologico(boolean existeDctoOdontologico)
	{
		this.existeDctoOdontologico = existeDctoOdontologico;
	}
	
	/**
	 * @return the existeIncumplimientoParaAplicarDcto
	 */
	public boolean isExisteIncumplimientoParaAplicarDcto()
	{
		return existeIncumplimientoParaAplicarDcto;
	}
	
	/**
	 * @param existeIncumplimientoParaAplicarDcto the existeIncumplimientoParaAplicarDcto to set
	 */
	public void setExisteIncumplimientoParaAplicarDcto(
			boolean existeIncumplimientoParaAplicarDcto)
	{
		this.existeIncumplimientoParaAplicarDcto = existeIncumplimientoParaAplicarDcto;
	}
	
	/**
	 * @return the existeIncumplimientoParaAplicarDcto
	 */
	public boolean getExisteIncumplimientoParaAplicarDcto()
	{
		return existeIncumplimientoParaAplicarDcto;
	}
	
	/**
	 * @return the warningsContratosVencidos
	 */
	public ArrayList<String> getWarningsContratosVencidos()
	{
		return warningsContratosVencidos;
	}
	
	/**
	 * @param warningsContratosVencidos the warningsContratosVencidos to set
	 */
	public void setWarningsContratosVencidos(
			ArrayList<String> warningsContratosVencidos)
	{
		this.warningsContratosVencidos = warningsContratosVencidos;
	}
	
	/**
	 * @return the listaMotivosAnulacionDcto
	 */
	public ArrayList<DtoMotivoDescuento> getListaMotivosAnulacionDcto()
	{
		return listaMotivosAnulacionDcto;
	}
	
	/**
	 * @param listaMotivosAnulacionDcto the listaMotivosAnulacionDcto to set
	 */
	public void setListaMotivosAnulacionDcto(
			ArrayList<DtoMotivoDescuento> listaMotivosAnulacionDcto)
	{
		this.listaMotivosAnulacionDcto = listaMotivosAnulacionDcto;
	}
	
	/**
	 * @return the codigoMotivoAnulacionDctoSel
	 */
	public double getCodigoMotivoAnulacionDctoSel()
	{
		return codigoMotivoAnulacionDctoSel;
	}
	
	/**
	 * @param codigoMotivoAnulacionDctoSel the codigoMotivoAnulacionDctoSel to set
	 */
	public void setCodigoMotivoAnulacionDctoSel(double codigoMotivoAnulacionDctoSel)
	{
		this.codigoMotivoAnulacionDctoSel = codigoMotivoAnulacionDctoSel;
	}
	
	/**
	 * @return the listaPresupuestosPrecontratados
	 */
	public ArrayList<InfoPresupuestoPrecontratado> getListaPresupuestosPrecontratados() {
		return listaPresupuestosPrecontratados;
	}
	
	/**
	 * @param listaPresupuestosPrecontratados the listaPresupuestosPrecontratados to set
	 */
	public void setListaPresupuestosPrecontratados(
			ArrayList<InfoPresupuestoPrecontratado> listaPresupuestosPrecontratados) {
		this.listaPresupuestosPrecontratados = listaPresupuestosPrecontratados;
	}
	
	/**
	 * @return the listaAnticiposPresupuesto
	 */
	public ArrayList<DtoValorAnticipoPresupuesto> getListaAnticiposPresupuesto() {
		return listaAnticiposPresupuesto;
	}
	
	/**
	 * @param listaAnticiposPresupuesto the listaAnticiposPresupuesto to set
	 */
	public void setListaAnticiposPresupuesto(
			ArrayList<DtoValorAnticipoPresupuesto> listaAnticiposPresupuesto) {
		this.listaAnticiposPresupuesto = listaAnticiposPresupuesto;
	}
	
	/**
	 * 
	 * @param contrato
	 * @return
	 */
	public boolean getUtilizaReservaAnticipo(int contrato) 
	{
		boolean retorna= false;
		for(DtoValorAnticipoPresupuesto dto:this.listaAnticiposPresupuesto)
		{
			if(dto.getContrato()== contrato && dto.getValorAnticipo().doubleValue()>0)
			{
				retorna=true;
				break;
			}
		}
		return retorna;
	}
	
	/**
	 * @return the encabezadoHistorico
	 */
	public InfoDatosDouble getEncabezadoHistorico() {
		return encabezadoHistorico;
	}
	
	/**
	 * @param encabezadoHistorico the encabezadoHistorico to set
	 */
	public void setEncabezadoHistorico(InfoDatosDouble encabezadoHistorico) {
		this.encabezadoHistorico = encabezadoHistorico;
	}
	
	/**
	 * @return the listaCitasPresupuesto
	 */
	public ArrayList<DtoCitasPresupuestoOdo> getListaCitasPresupuesto() {
		return listaCitasPresupuesto;
	}
	
	/**
	 * @return the listaCitasPresupuesto
	 */
	public int getListaCitasPresupuestoSize() {
		return listaCitasPresupuesto.size();
	}
	
	
	/**
	 * @param listaCitasPresupuesto the listaCitasPresupuesto to set
	 */
	public void setListaCitasPresupuesto(
			ArrayList<DtoCitasPresupuestoOdo> listaCitasPresupuesto) {
		this.listaCitasPresupuesto = listaCitasPresupuesto;
	}
	
	/**
	 * @return the listaMotivosCancelacionCita
	 */
	public ArrayList<HashMap> getListaMotivosCancelacionCita() {
		return listaMotivosCancelacionCita;
	}
	
	/**
	 * @param listaMotivosCancelacionCita the listaMotivosCancelacionCita to set
	 */
	public void setListaMotivosCancelacionCita(
			ArrayList<HashMap> listaMotivosCancelacionCita) {
		this.listaMotivosCancelacionCita = listaMotivosCancelacionCita;
	}
	
	/**
	 * @return the motivoCancelacionCita
	 */
	public int getMotivoCancelacionCita() {
		return motivoCancelacionCita;
	}
	
	/**
	 * @param motivoCancelacionCita the motivoCancelacionCita to set
	 */
	public void setMotivoCancelacionCita(int motivoCancelacionCita) {
		this.motivoCancelacionCita = motivoCancelacionCita;
	}
	
	/**
	 * @return the listaPaquetes
	 */
	public ArrayList<InfoPaquetesPresupuesto> getListaPaquetes() {
		return listaPaquetes;
	}
	
	/**
	 * @param listaPaquetes the listaPaquetes to set
	 */
	public void setListaPaquetes(ArrayList<InfoPaquetesPresupuesto> listaPaquetes) {
		this.listaPaquetes = listaPaquetes;
	}
	
	/**
	 * @return the detallePaquete
	 */
	public ArrayList<InfoDetallePaquetePresupuesto> getDetallePaquete() {
		return detallePaquete;
	}
	
	/**
	 * @param detallePaquete the detallePaquete to set
	 */
	public void setDetallePaquete(
			ArrayList<InfoDetallePaquetePresupuesto> detallePaquete) {
		this.detallePaquete = detallePaquete;
	}
	
	/**
	 * @return the detallePaqueteOdonCONVENIOSeleccionado
	 */
	public int getDetallePaqueteOdonCONVENIOSeleccionado() {
		return detallePaqueteOdonCONVENIOSeleccionado;
	}
	
	/**
	 * @param detallePaqueteOdonCONVENIOSeleccionado the detallePaqueteOdonCONVENIOSeleccionado to set
	 */
	public void setDetallePaqueteOdonCONVENIOSeleccionado(
			int detallePaqueteOdonCONVENIOSeleccionado) {
		this.detallePaqueteOdonCONVENIOSeleccionado = detallePaqueteOdonCONVENIOSeleccionado;
	}
	
	public String getMensajePaquetes()
	{
		ArrayList<String> paquetes= new ArrayList<String>();
		for(DtoPresuOdoProgServ dtoProg: this.getListaProgramasServicios())
		{
			for(DtoPresupuestoOdoConvenio dtoConv: dtoProg.getListPresupuestoOdoConvenio())
			{
				if(dtoConv.getDtoPresupuestoPaquete().getDetallePaqueteOdontologicoConvenio()>0
						&& !dtoConv.getDtoPresupuestoPaquete().isVigente())
				{
					if(!paquetes.contains(dtoConv.getDtoPresupuestoPaquete().getCodigoPaqueteMostrar()))
					{	
						paquetes.add(dtoConv.getDtoPresupuestoPaquete().getCodigoPaqueteMostrar());
					}	
				}
			}
		}
		if(paquetes.size()>0)
		{
			return "Los Paquetes "+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(paquetes)+" en el presupuesto NO SE ENCUENTRAN VIGENTES o NO APLICAN.";
		}
		return "";
	}
	
	
	
	public int getIndexOpcionImprimir() {
		return indexOpcionImprimir;
	}
	
	public void setIndexOpcionImprimir(int indexOpcionImprimir) {
		this.indexOpcionImprimir = indexOpcionImprimir;
	}
	
	public DtoFormatoImpresionContratoOdontologico getObjImpresionContratoPresupuesto() {
		return objImpresionContratoPresupuesto;
	}
	
	public void setObjImpresionContratoPresupuesto(
			DtoFormatoImpresionContratoOdontologico objImpresionContratoPresupuesto) {
		this.objImpresionContratoPresupuesto = objImpresionContratoPresupuesto;
	}
	
	public boolean isMostrarImpresionContratoOdonto() {
		return mostrarImpresionContratoOdonto;
	}
	
	public void setMostrarImpresionContratoOdonto(
			boolean mostrarImpresionContratoOdonto) {
		this.mostrarImpresionContratoOdonto = mostrarImpresionContratoOdonto;
	}
	
	public void setEstadoPresupuestoTmp(String estadoPresupuestoTmp) {
		this.estadoPresupuestoTmp = estadoPresupuestoTmp;
	}
	
	public String getEstadoPresupuestoTmp() {
		return estadoPresupuestoTmp;
	}
	
	public void setValidaPresupuestoOdoContratado(
			String validaPresupuestoOdoContratado) {
		this.validaPresupuestoOdoContratado = validaPresupuestoOdoContratado;
	}
	
	public String getValidaPresupuestoOdoContratado() {
		return validaPresupuestoOdoContratado;
	}
	
	
	
	public void setEsContratar(String esContratar) {
		this.esContratar = esContratar;
	}
	
	public String getEsContratar() {
		return esContratar;
	}
	
	/**
	 * @return the mensajesPresupuesto
	 */
	public MensajesPresupuesto getMensajesPresupuesto() {
		return mensajesPresupuesto;
	}
	
	/**
	 * @param mensajesPresupuesto the mensajesPresupuesto to set
	 */
	public void setMensajesPresupuesto(MensajesPresupuesto mensajesPresupuesto) {
		this.mensajesPresupuesto = mensajesPresupuesto;
	}
	
	public void setListaHistoricoPresuIngreso(List<DtoHistoricoIngresoPresupuesto> listaHistoricoPresuIngreso) {
		this.listaHistoricoPresuIngreso = listaHistoricoPresuIngreso;
	}
	
	public List<DtoHistoricoIngresoPresupuesto> getListaHistoricoPresuIngreso() {
		return listaHistoricoPresuIngreso;
	}
	
	public void setPostArrayIngresoHistoricos(int postArrayIngresoHistoricos) {
		this.postArrayIngresoHistoricos = postArrayIngresoHistoricos;
	}
	
	public int getPostArrayIngresoHistoricos() {
		return postArrayIngresoHistoricos;
	}
	
	public void setDtoIngresoHistoricoPresu(DtoHistoricoIngresoPresupuesto dtoIngresoHistoricoPresu) {
		this.dtoIngresoHistoricoPresu = dtoIngresoHistoricoPresu;
	}
	
	/**
	 * @return the mostrarImpresionRecomendacionesOdonto
	 */
	public boolean isMostrarImpresionRecomendacionesOdonto() {
		return mostrarImpresionRecomendacionesOdonto;
	}
	
	/**
	 * @param mostrarImpresionRecomendacionesOdonto the mostrarImpresionRecomendacionesOdonto to set
	 */
	public void setMostrarImpresionRecomendacionesOdonto(
			boolean mostrarImpresionRecomendacionesOdonto) {
		this.mostrarImpresionRecomendacionesOdonto = mostrarImpresionRecomendacionesOdonto;
	}
	
	public DtoHistoricoIngresoPresupuesto getDtoIngresoHistoricoPresu() {
		return dtoIngresoHistoricoPresu;
	}
	
	/**
	 * @return the objImpresionRecomendacionesPresupuesto
	 */
	public DtoFormatoImpresionContratoOdontologico getObjImpresionRecomendacionesPresupuesto() {
		return objImpresionRecomendacionesPresupuesto;
	}
	
	/**
	 * @param objImpresionRecomendacionesPresupuesto the objImpresionRecomendacionesPresupuesto to set
	 */
	public void setObjImpresionRecomendacionesPresupuesto(
			DtoFormatoImpresionContratoOdontologico objImpresionRecomendacionesPresupuesto) {
		this.objImpresionRecomendacionesPresupuesto = objImpresionRecomendacionesPresupuesto;
	}
	
	/**
	 * Obtiene el valor del atributopermitirContratar
	 * @return permitirContratar
	 */
	public boolean isPermitirContratar() {
		return permitirContratar;
	}
	
	/**
	 * Asigna el atributo permitirContratar
	 * @param permitirContratar
	 */
	public void setPermitirContratar(boolean permitirContratar) {
		this.permitirContratar = permitirContratar;
	}

	public String getMensajeInformativoConveniosNoRelacionados() {
		return mensajeInformativoConveniosNoRelacionados;
	}

	public void setMensajeInformativoConveniosNoRelacionados(
			String mensajeInformativoConveniosNoRelacionados) {
		this.mensajeInformativoConveniosNoRelacionados = mensajeInformativoConveniosNoRelacionados;
	}

	public boolean isTieneIngresoAbierto() {
		return tieneIngresoAbierto;
	}

	public void setTieneIngresoAbierto(boolean tieneIngresoAbierto) {
		this.tieneIngresoAbierto = tieneIngresoAbierto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo presupuestoID
	
	 * @return retorna la variable presupuestoID 
	 * @author Angela Maria Aguirre 
	 */
	public long getPresupuestoID() {
		return presupuestoID;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo presupuestoID
	
	 * @param valor para el atributo presupuestoID 
	 * @author Angela Maria Aguirre 
	 */
	public void setPresupuestoID(long presupuestoID) {
		this.presupuestoID = presupuestoID;
	}

	public boolean isDetallePresupuestoOtraFuncionalidad() {
		return detallePresupuestoOtraFuncionalidad;
	}

	public void setDetallePresupuestoOtraFuncionalidad(
			boolean detallePresupuestoOtraFuncionalidad) {
		this.detallePresupuestoOtraFuncionalidad = detallePresupuestoOtraFuncionalidad;
	}
	
	/**
	 * @param precontratar the precontratar to set
	 */
	public void setPrecontratar(boolean precontratar) {
		this.precontratar = precontratar;
	}

	/**
	 * @return the precontratar
	 */
	public boolean isPrecontratar() {
		return precontratar;
	}

	/**
	 * @param codigoProximaCitaRegistrada the codigoProximaCitaRegistrada to set
	 */
	public void setCodigoProximaCitaRegistrada(int codigoProximaCitaRegistrada) {
		this.codigoProximaCitaRegistrada = codigoProximaCitaRegistrada;
	}

	/**
	 * @return the codigoProximaCitaRegistrada
	 */
	public int getCodigoProximaCitaRegistrada() {
		return codigoProximaCitaRegistrada;
	}


	/**
	 * @param estadoTemporal the estadoTemporal to set
	 */
	public void setEstadoTemporal(String estadoTemporal) {
		this.estadoTemporal = estadoTemporal;
	}

	/**
	 * @return the estadoTemporal
	 */
	public String getEstadoTemporal() {
		return estadoTemporal;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estadoOtraFuncionalidad
	
	 * @return retorna la variable estadoOtraFuncionalidad 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstadoOtraFuncionalidad() {
		return estadoOtraFuncionalidad;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estadoOtraFuncionalidad
	
	 * @param valor para el atributo estadoOtraFuncionalidad 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstadoOtraFuncionalidad(String estadoOtraFuncionalidad) {
		this.estadoOtraFuncionalidad = estadoOtraFuncionalidad;
	}

	/**
	 * @param abrePopUpProximaCita the abrePopUpProximaCita to set
	 */
	public void setAbrePopUpProximaCita(boolean abrePopUpProximaCita) {
		this.abrePopUpProximaCita = abrePopUpProximaCita;
	}

	/**
	 * @return the abrePopUpProximaCita
	 */
	public boolean isAbrePopUpProximaCita() {
		return abrePopUpProximaCita;
	}

	public ArrayList<DtoOtroSi> getListaOpcionesImprimirOtrosSi() {
		return listaOpcionesImprimirOtrosSi;
	}

	public void setListaOpcionesImprimirOtrosSi(ArrayList<DtoOtroSi> listaOpcionesImprimirOtrosSi) {
		this.listaOpcionesImprimirOtrosSi = listaOpcionesImprimirOtrosSi;
	}

}