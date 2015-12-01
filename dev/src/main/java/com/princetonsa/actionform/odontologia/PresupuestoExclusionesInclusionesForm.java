package com.princetonsa.actionform.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.odontologia.InfoHistoricoInclusionesExclusionesPresupuesto;
import util.odontologia.InfoInclusionExclusionBoca;
import util.odontologia.InfoInclusionExclusionPiezaSuperficie;
import util.odontologia.InfoPresupuestoExclusionesInclusiones;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.odontologia.DtoContratarInclusion;
import com.princetonsa.dto.odontologia.DtoEncabezadoInclusion;
import com.princetonsa.dto.odontologia.DtoExclusionPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoRegistroContratarInclusion;
import com.princetonsa.dto.odontologia.DtoTotalesContratarInclusion;
import com.servinte.axioma.orm.ExcluPresuEncabezado;
import com.servinte.axioma.orm.ExclusionesPresupuesto;
import com.servinte.axioma.orm.Personas;

/**
 * 
 * @author axioma
 *
 */
@SuppressWarnings("serial")
public class PresupuestoExclusionesInclusionesForm extends ValidatorForm  
{

	/** * */
	private InfoPresupuestoExclusionesInclusiones infoPresupuestoExclusionesInclusiones;
	
	/** * */
	private String estado;
	
	/** * */
	private boolean utilizaProgramas;

	/** * */
	private ArrayList<DtoExclusionPresupuesto> listaDtoExclusiones;
	
	/** * */
	private InfoHistoricoInclusionesExclusionesPresupuesto infoHistorico;
	
	/** * */
	private String siguientePagina;
	
	/** * */
	private String codigoPkPresupuestoXREQUEST;
	
	/** * */
	private ArrayList<String> mensajesNoExistenciaExcInc;
	
	/** * */
	private String infoTarifaAjax;
	
	/** * indica si debo o no mostrar la selecion del bono o prom */
	private boolean seleccionarBonoYPromocionAjax;
	
	/** * */
	private String propiedadFormaAjax;
	
	/** * */
	private String colorAjax;
	
	/**
	 * Lista de códigos de Inclusiones/Exclusiones del presupuesto del paciente
	 */
	private ArrayList<DtoCheckBox> listaInclusionesExclusiones;
	
	/**
	 * Indica si se debe mostrar abierta la sección desplegable
	 * Inclusiones del paciente
	 */
	private boolean mostrarInclusionesPaciente;
	
	/**
	 * Indica si se debe mostrar abierta la sección desplegable
	 * Nuevas Inclusiones
	 */
	private boolean mostrarNuevasInclusiones;
	

	/**
	 * Indica si se debe mostrar abierta la sección desplegable
	 * Exclusiones del paciente
	 */
	private boolean mostrarExclusionesPaciente;
	
	
	/**
	 * Indica si se debe mostrar abierta la sección desplegable
	 * Nuevas Exclusiones
	 */
	private boolean mostrarNuevasExclusiones;
	
	
	/**
	 * Mensaje informativo para las Inclusiones
	 * del paciente
	 */
	private String mensajeInclusiones;
	
	/**
	 * Mensaje informativo para las Exclusiones
	 * del paciente
	 */
	private String mensajeExclusiones;
	
	/**
	 * Indica la sección que se va a actualizar
	 * Sección Inclusiones
	 * Sección Exclusiones
	 */
	private String seccionActualizar;
	
	/**
	 * Indica la sub-sección que se va a actualizar
	 * 
	 * sub-sección Piezas Dentales
	 * sub-sección Boca
	 */
	private String subSeccionActualizar;
	
	/**
	 * Indica la posición del registo que se va a actualizar
	 */
	private int posicionRegistroActualizar;
	
//	/**
//	 * Presupuesto Generado con las Inclusiones
//	 */
//	private DtoPresupuestoOdontologico dtoPresupuesto;
	
	/**
	 * Listado con las Sumatorias del Presupuesto
	 * En este atributo se almacena el total de los convenios después de seleccionar los programas
	 */
	private ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios;
	
	/**
	 * Listado con los programas servicios que se van a incluir en el presupuesto
	 */
	private ArrayList<DtoPresuOdoProgServ> listaProgramasServicios;
	
	/**
	 * Colspan necesario para la interfaz
	 * gráfica en la contratación de las inclusiones
	 */
	private int colspanContratarInclusiones;
	
	/**
	 * Listado con los registros disponibles involucrados en la contratación de las inclusiones.
	 */
	private ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion;
	
	// Atributos para la selección de programas (Parte visual)
	/**
	 * índice del registro de inclusión (Programa) 
	 */
	private int indexRegInclusion;

	/**
	 * índice del convenio 
	 */
	private int indexConvenio;

	/**
	 * Indica si el usuario seleccionó o deseleccionó el programa
	 * true: Seleccionó
	 * false: Deseleccionó 
	 */
	private boolean checkPropiedadPrograma;

	/**
	 * Contiene toda la información con los totales calculados para
	 * la contratación de las inclusiones
	 */
	private DtoTotalesContratarInclusion totalesContratarInclusion;
	
	/**
	 * Consecutivo del presupuesto
	 */
	private BigDecimal consecutivoPresupuesto;
	
	
	/**
	 * Atributo con la posici&oacute;n del registro a eliminar
	 */
	private int posicionRegistroAEliminar;

	/**
	 * Clave para conocer que mensaje se debe mostrar cuando se ha terminado
	 * algún proceso de la funcionalidad
	 */
	private String clave;
	
	/**
	 * Indice del registro de Exclusion que se va a mostrar
	 */
	private int indiceRegistroExclusion;
	
	/**
	 * Listado con los registros de las exclusiones realizadas al presupuesto del paciente
	 */
	private ArrayList<ExcluPresuEncabezado> registrosExclusion;
	
	/**
	 * Registro de Exclusión cargado para el detalle
	 */
	private ExcluPresuEncabezado registroExclusionDetalle;
	
	
	/**
	 * Indice del registro de Inclusion que se va a mostrar en la sección de detalle
	 */
	private int indiceInclusionMostrar;
	
	/**
	 * Listado con los registros de las Inclusiones realizadas al presupuesto del paciente
	 */
	private ArrayList<DtoEncabezadoInclusion> registrosInclusion;
	
	
	/**
	 * Indica si el motivo de anulación para las solicitudes de 
	 * descuento odontológico está definido
	 */
	private boolean noDefinidoMotivoAnulacion;
	
	
//	/**
//	 * Registro de Exclusión cargado para el detalle
//	 */
//	private IncluPresuEncabezado registroInclusionDetalle;
	
	
//	/**
//	 * Listado con los registros de las inclusiones a contratar que se visualizan desde el detalle
//	 */
//	private ArrayList<DtoRegistroContratarInclusion> registrosInclusionDetalle;
	

	/**
	 * Atributo que indica si se debe o no realizar una pregunta al usuario
	 * sobre el proceso de contratación de las inclusiones.
	 * 
	 * Junto con la variable preguntaContratarPrecontrado
	 * Posibles preguntas:
	 * 
	 * Generar una solicitud de descuento
	 * Contratar o no sin bonos vigentes
	 * Contratar o no sin promociones vigentes
	 * Contratar o no con un descuento autorizado vencido
	 * Contratar o no con un descuento pendiente por definir o autorizar
	 * Contratar o no con un descuento no autorizado o anulado
	 * Contratar o no sin aplicar los Nuevos Bonos y Nuevos Promociones
	 * 
	 */
	private boolean tomarDecisionProcesoContratacion;

	/**
	 * Decision que toma el usuario frente a la pregunta realizada
	 */
	private String decisionPreguntaContratarInclusion;


	/**
	 * Indica si se permite o no generar una solicitud de descuento
	 */
	private boolean permiteGenerarSolicitudDescuento;

	
	/**
	 * Indica el 'key' correspondiente al archivo InclusionesExclusionesForm.properties
	 * con la pregunta que se va a realizar generada como validación del proceso de contratación
	 * de inclusiones precontratadas.
	 */
	private String preguntaContratarPrecontrado;
	
	
	//private ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConveniosDetalleInclusion;
	
	/**
	 * Colspan necesario para la interfaz
	 * gráfica en el detalle de registro de Inclusiones
	 */
	private int colspanDetalleInclusiones;
	
	/**
	 * Indica si existen Inclusiones en estado Precontratada
	 */
	private boolean existenInclusionesPrecontratadas;
	
	/**
	 * Información asociada a la contratación de una Inclusión
	 * en estado precontratado.
	 */
	private DtoContratarInclusion dtoContratarInclusion;
	
	/**
	 * Reset
	 * @param institucion
	 */
	public void reset(int institucion) 
	{
		this.codigoPkPresupuestoXREQUEST= "";
		this.utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion));
		this.infoPresupuestoExclusionesInclusiones= new InfoPresupuestoExclusionesInclusiones();
		this.listaDtoExclusiones= new ArrayList<DtoExclusionPresupuesto>();
		this.mensajesNoExistenciaExcInc= new ArrayList<String>();
		this.resetHistorico(institucion);
		this.infoTarifaAjax="";
		this.seleccionarBonoYPromocionAjax= false;
		this.propiedadFormaAjax="";
		this.colorAjax="";
		this.listaInclusionesExclusiones = new ArrayList<DtoCheckBox>();
		
		this.mostrarInclusionesPaciente = false;
		this.mostrarNuevasInclusiones = false;
		
		this.mostrarExclusionesPaciente = false;
		this.mostrarNuevasExclusiones = false;
		
		this.mensajeInclusiones = "";
		this.mensajeExclusiones = "";
		
		this.seccionActualizar = "";
		
		this.subSeccionActualizar = "";
		
		this.posicionRegistroActualizar = ConstantesBD.codigoNuncaValido;
		
		//this.dtoPresupuesto = new DtoPresupuestoOdontologico();
		
		this.listaSumatoriaConvenios = new ArrayList<DtoPresupuestoTotalConvenio>();
		
		this.setListaProgramasServicios(new ArrayList<DtoPresuOdoProgServ>());
		
		this.colspanContratarInclusiones = 5;
		
		this.setRegistrosContratarInclusion(new ArrayList<DtoRegistroContratarInclusion>());
		
		this.totalesContratarInclusion = new DtoTotalesContratarInclusion();
		
		this.consecutivoPresupuesto =  new BigDecimal(ConstantesBD.codigoNuncaValido);
		
		this.posicionRegistroAEliminar = ConstantesBD.codigoNuncaValido;
		
		this.indiceRegistroExclusion = ConstantesBD.codigoNuncaValido;
		
		this.registrosExclusion = new ArrayList<ExcluPresuEncabezado>();
		
		this.registroExclusionDetalle = new ExcluPresuEncabezado();
		//this.clave = "";
		
		this.indiceInclusionMostrar = ConstantesBD.codigoNuncaValido;
		
		this.setRegistrosInclusion(new ArrayList<DtoEncabezadoInclusion>());
		
		//this.registrosInclusionDetalle  = new ArrayList<DtoRegistroContratarInclusion>();
		
		this.permiteGenerarSolicitudDescuento=false;
		
		//this.registroInclusionDetalle = new IncluPresuEncabezado();
		
		//this.listaSumatoriaConveniosDetalleInclusion = new ArrayList<DtoPresupuestoTotalConvenio>();
		
		this.colspanDetalleInclusiones = 4;
		
		this.existenInclusionesPrecontratadas = false;
		
		this.noDefinidoMotivoAnulacion = false;
		
		resetAtributosProcesoContratacion();
	}
	
	/**
	 * Método que reinicializa todos los atributos
	 * relacionados con el proceso de contratación y precontratación de
	 * las inclusiones
	 */
	public void resetAtributosProcesoContratacion (){
		
		this.totalesContratarInclusion = new DtoTotalesContratarInclusion();
		this.colspanContratarInclusiones = 5;
		this.registrosContratarInclusion = new ArrayList<DtoRegistroContratarInclusion>();
		this.existenInclusionesPrecontratadas = false;
		this.permiteGenerarSolicitudDescuento=false;
		
		resetValidacionesContratarInclusiones();
		resetTotales();
	}
	
	/**
	 * Método que se encarga de reiniciar los valores 
	 * para las validaciones realizadas al contratar una inclusión
	 * en estado precontratada
	 */
	public void resetValidacionesContratarInclusiones (){
		
		this.tomarDecisionProcesoContratacion = false;
		this.preguntaContratarPrecontrado = "";
		this.decisionPreguntaContratarInclusion = "";
		this.noDefinidoMotivoAnulacion = false;
	}
	
	/**
	 * Método que reinicializa los totales
	 */
	public void resetTotales (){
		
		this.totalesContratarInclusion = new DtoTotalesContratarInclusion();
	}
	
	/**
	 * Método para reiniciar los valores
	 * para la actualización de programas
	 */
	public void resetActualizarPrograma() 
	{
		this.seccionActualizar = "";
		
		this.subSeccionActualizar = "";
		
		this.posicionRegistroActualizar = ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Reset de historicos
	 * @param institucion
	 */
	public void resetHistorico(int institucion)
	{
		this.siguientePagina="";
		this.utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion));
		this.infoHistorico= new InfoHistoricoInclusionesExclusionesPresupuesto();
	}
	
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.getEstado().equals("guardar"))
		{
			boolean existeSeleccionado=false;
			//recorremos las inclusiones-exclusiones para verificar que haya seleccionado algo
			for(InfoInclusionExclusionPiezaSuperficie info: this.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaPiezasDentalesSuperficies())
			{
				if(info.getIndiceConvenioContratoSeleccionado()>ConstantesBD.codigoNuncaValido && info.getErroresCalculoTarifa().isEmpty() && info.getInfoTarifa()!=null)
				{	
					existeSeleccionado=true;
					break;
				}	
			}
			
			for(InfoInclusionExclusionBoca info: this.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaBoca())
			{
				if(info.getIndiceConvenioContratoSeleccionado()>ConstantesBD.codigoNuncaValido && info.getErroresCalculoTarifa().isEmpty() && info.getInfoTarifa()!=null)
				{	
					existeSeleccionado=true;
					break;
				}	
			}
			
			for(InfoInclusionExclusionPiezaSuperficie info: this.getInfoPresupuestoExclusionesInclusiones().getSeccionExclusiones().getListaPiezasDentalesSuperficies())
			{
				if(info.getActivo())
				{	
					existeSeleccionado=true;
					break;
				}	
			}
			
			for(InfoInclusionExclusionBoca info: this.getInfoPresupuestoExclusionesInclusiones().getSeccionExclusiones().getListaBoca())
			{
				if(info.getActivo())
				{	
					existeSeleccionado=true;
					break;
				}	
			}
			
			if(!existeSeleccionado)
			{
				errores.add("", new ActionMessage("errors.required","La selección de una exclusión o inclusión"));
			}
			
			if(!errores.isEmpty())
			{
				this.setEstado("mostrarErrores");
			}
			
		}else if(estado.equals("contratarInclusiones")){
			
			for(DtoRegistroContratarInclusion registroContratarInclusion : this.registrosContratarInclusion)
			{
				if(!registroContratarInclusion.isInclusionParaContratar()){
					
					MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
					errores.add("", new ActionMessage("errors.notEspecific", mensages.getMessage("InclusionesExclusionesForm.seleccionarTodasInclusiones")));
					
					break;
				}
			}
		}
		
		//Validate para el insertar
		return errores;
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
	 * @return the infoPresupuestoExclusionesInclusiones
	 */
	public InfoPresupuestoExclusionesInclusiones getInfoPresupuestoExclusionesInclusiones()
	{
		return infoPresupuestoExclusionesInclusiones;
	}

	/**
	 * @param infoPresupuestoExclusionesInclusiones the infoPresupuestoExclusionesInclusiones to set
	 */
	public void setInfoPresupuestoExclusionesInclusiones(
			InfoPresupuestoExclusionesInclusiones infoPresupuestoExclusionesInclusiones)
	{
		this.infoPresupuestoExclusionesInclusiones = infoPresupuestoExclusionesInclusiones;
	}

	/**
	 * @return the utilizaProgramas
	 */
	public boolean isUtilizaProgramas()
	{
		return utilizaProgramas;
	}

	/**
	 * @param utilizaProgramas the utilizaProgramas to set
	 */
	public void setUtilizaProgramas(boolean utilizaProgramas)
	{
		this.utilizaProgramas = utilizaProgramas;
	}
	
	/**
	 * @return the utilizaProgramas
	 */
	public boolean getUtilizaProgramas()
	{
		return utilizaProgramas;
	}

	/**
	 * @return the listaDtoExclusiones
	 */
	public ArrayList<DtoExclusionPresupuesto> getListaDtoExclusiones()
	{
		return listaDtoExclusiones;
	}

	/**
	 * @param listaDtoExclusiones the listaDtoExclusiones to set
	 */
	public void setListaDtoExclusiones(
			ArrayList<DtoExclusionPresupuesto> listaDtoExclusiones)
	{
		this.listaDtoExclusiones = listaDtoExclusiones;
	}

	/**
	 * @return the infoHistorico
	 */
	public InfoHistoricoInclusionesExclusionesPresupuesto getInfoHistorico()
	{
		return infoHistorico;
	}

	/**
	 * @param infoHistorico the infoHistorico to set
	 */
	public void setInfoHistorico(
			InfoHistoricoInclusionesExclusionesPresupuesto infoHistorico)
	{
		this.infoHistorico = infoHistorico;
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
	 * @return the codigoPkPresupuestoXREQUEST
	 */
	public String getCodigoPkPresupuestoXREQUEST()
	{
		return codigoPkPresupuestoXREQUEST;
	}

	/**
	 * @param codigoPkPresupuestoXREQUEST the codigoPkPresupuestoXREQUEST to set
	 */
	public void setCodigoPkPresupuestoXREQUEST(String codigoPkPresupuestoXREQUEST)
	{
		this.codigoPkPresupuestoXREQUEST = codigoPkPresupuestoXREQUEST;
	}

	/**
	 * @return the mensajesNoExistenciaExcInc
	 */
	public ArrayList<String> getMensajesNoExistenciaExcInc() {
		return mensajesNoExistenciaExcInc;
	}

	/**
	 * @param mensajesNoExistenciaExcInc the mensajesNoExistenciaExcInc to set
	 */
	public void setMensajesNoExistenciaExcInc(
			ArrayList<String> mensajesNoExistenciaExcInc) {
		this.mensajesNoExistenciaExcInc = mensajesNoExistenciaExcInc;
	}

	/**
	 * @return the infoTarifaAjax
	 */
	public String getInfoTarifaAjax() {
		return infoTarifaAjax;
	}

	/**
	 * @param infoTarifaAjax the infoTarifaAjax to set
	 */
	public void setInfoTarifaAjax(String infoTarifaAjax) {
		this.infoTarifaAjax = infoTarifaAjax;
	}

	/**
	 * @return the seleccionarBonoOPromocionAjax
	 */
	public boolean isSeleccionarBonoYPromocionAjax() {
		return seleccionarBonoYPromocionAjax;
	}

	/**
	 * @param seleccionarBonoOPromocionAjax the seleccionarBonoOPromocionAjax to set
	 */
	public void setSeleccionarBonoYPromocionAjax(
			boolean seleccionarBonoYPromocionAjax) {
		this.seleccionarBonoYPromocionAjax = seleccionarBonoYPromocionAjax;
	}

	/**
	 * @return the propiedadFormaAjax
	 */
	public String getPropiedadFormaAjax() {
		return propiedadFormaAjax;
	}

	/**
	 * @param propiedadFormaAjax the propiedadFormaAjax to set
	 */
	public void setPropiedadFormaAjax(String propiedadFormaAjax) {
		this.propiedadFormaAjax = propiedadFormaAjax;
	}

	/**
	 * @return the colorAjax
	 */
	public String getColorAjax() {
		return colorAjax;
	}

	/**
	 * @param colorAjax the colorAjax to set
	 */
	public void setColorAjax(String colorAjax) {
		this.colorAjax = colorAjax;
	}

	public ArrayList<DtoCheckBox> getListaInclusionesExclusiones() {
		return listaInclusionesExclusiones;
	}

	public void setListaInclusionesExclusiones(
			ArrayList<DtoCheckBox> listaInclusionesExclusiones) {
		this.listaInclusionesExclusiones = listaInclusionesExclusiones;
	}


	/**
	 * @param mostrarInclusionesPaciente the mostrarInclusionesPaciente to set
	 */
	public void setMostrarInclusionesPaciente(boolean mostrarInclusionesPaciente) {
		this.mostrarInclusionesPaciente = mostrarInclusionesPaciente;
	}


	/**
	 * @return the mostrarInclusionesPaciente
	 */
	public boolean isMostrarInclusionesPaciente() {
		return mostrarInclusionesPaciente;
	}


	/**
	 * @param mostrarNuevasInclusiones the mostrarNuevasInclusiones to set
	 */
	public void setMostrarNuevasInclusiones(boolean mostrarNuevasInclusiones) {
		this.mostrarNuevasInclusiones = mostrarNuevasInclusiones;
	}


	/**
	 * @return the mostrarNuevasInclusiones
	 */
	public boolean isMostrarNuevasInclusiones() {
		return mostrarNuevasInclusiones;
	}


	/**
	 * @param mostrarExclusionesPaciente the mostrarExclusionesPaciente to set
	 */
	public void setMostrarExclusionesPaciente(boolean mostrarExclusionesPaciente) {
		this.mostrarExclusionesPaciente = mostrarExclusionesPaciente;
	}


	/**
	 * @return the mostrarExclusionesPaciente
	 */
	public boolean isMostrarExclusionesPaciente() {
		return mostrarExclusionesPaciente;
	}


	/**
	 * @param mostrarNuevasExclusiones the mostrarNuevasExclusiones to set
	 */
	public void setMostrarNuevasExclusiones(boolean mostrarNuevasExclusiones) {
		this.mostrarNuevasExclusiones = mostrarNuevasExclusiones;
	}


	/**
	 * @return the mostrarNuevasExclusiones
	 */
	public boolean isMostrarNuevasExclusiones() {
		return mostrarNuevasExclusiones;
	}


	/**
	 * @param mensajeInclusiones the mensajeInclusiones to set
	 */
	public void setMensajeInclusiones(String mensajeInclusiones) {
		this.mensajeInclusiones = mensajeInclusiones;
	}


	/**
	 * @return the mensajeInclusiones
	 */
	public String getMensajeInclusiones() {
		return mensajeInclusiones;
	}


	/**
	 * @param mensajeExclusiones the mensajeExclusiones to set
	 */
	public void setMensajeExclusiones(String mensajeExclusiones) {
		this.mensajeExclusiones = mensajeExclusiones;
	}


	/**
	 * @return the mensajeExclusiones
	 */
	public String getMensajeExclusiones() {
		return mensajeExclusiones;
	}


	/**
	 * @param seccionActualizar the seccionActualizar to set
	 */
	public void setSeccionActualizar(String seccionActualizar) {
		this.seccionActualizar = seccionActualizar;
	}


	/**
	 * @return the seccionActualizar
	 */
	public String getSeccionActualizar() {
		return seccionActualizar;
	}


	/**
	 * @param subSeccionActualizar the subSeccionActualizar to set
	 */
	public void setSubSeccionActualizar(String subSeccionActualizar) {
		this.subSeccionActualizar = subSeccionActualizar;
	}


	/**
	 * @return the subSeccionActualizar
	 */
	public String getSubSeccionActualizar() {
		return subSeccionActualizar;
	}


	/**
	 * @param posicionRegistroActualizar the posicionRegistroActualizar to set
	 */
	public void setPosicionRegistroActualizar(int posicionRegistroActualizar) {
		this.posicionRegistroActualizar = posicionRegistroActualizar;
	}


	/**
	 * @return the posicionRegistroActualizar
	 */
	public int getPosicionRegistroActualizar() {
		return posicionRegistroActualizar;
	}

//
//	/**
//	 * @param dtoPresupuesto the dtoPresupuesto to set
//	 */
//	public void setDtoPresupuesto(DtoPresupuestoOdontologico dtoPresupuesto) {
//		this.dtoPresupuesto = dtoPresupuesto;
//	}
//
//
//	/**
//	 * @return the dtoPresupuesto
//	 */
//	public DtoPresupuestoOdontologico getDtoPresupuesto() {
//		return dtoPresupuesto;
//	}


	/**
	 * @param listaSumatoriaConvenios the listaSumatoriaConvenios to set
	 */
	public void setListaSumatoriaConvenios(ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios) {
		this.listaSumatoriaConvenios = listaSumatoriaConvenios;
	}


	/**
	 * @return the listaSumatoriaConvenios
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> getListaSumatoriaConvenios() {
		return listaSumatoriaConvenios;
	}

	/**
	 * Obtiene la cantidad de convenios relacionados al paciente
	 * @return de convenios
	 */
	public int getCantidadConvenios()
	{
		return listaSumatoriaConvenios.size();
	}

	/**
	 * @param listaProgramasServicios the listaProgramasServicios to set
	 */
	public void setListaProgramasServicios(ArrayList<DtoPresuOdoProgServ> listaProgramasServicios) {
		this.listaProgramasServicios = listaProgramasServicios;
	}


	/**
	 * @return the listaProgramasServicios
	 */
	public ArrayList<DtoPresuOdoProgServ> getListaProgramasServicios() {
		return listaProgramasServicios;
	}


	/**
	 * @param colspanContratarInclusiones the colspanContratarInclusiones to set
	 */
	public void setColspanContratarInclusiones(int colspanContratarInclusiones) {
		this.colspanContratarInclusiones = colspanContratarInclusiones;
	}


	/**
	 * @return the colspanContratarInclusiones
	 */
	public int getColspanContratarInclusiones() {
		
		colspanContratarInclusiones = colspanContratarInclusiones + (getListaSumatoriaConvenios().size() * 2);
	
		return colspanContratarInclusiones;
	}
	
	
	/**
	 * @param registrosContratarInclusion the registrosContratarInclusion to set
	 */
	public void setRegistrosContratarInclusion(
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion) {
		this.registrosContratarInclusion = registrosContratarInclusion;
	}

	/**
	 * Obtiene la cantidad de programas a contratar
	 * return cantidad de programas
	 */
	public int getCantidadRegistrosContratarInclusion()
	{
		return registrosContratarInclusion.size();
	}

	/**
	 * @return the registrosContratarInclusion
	 */
	public ArrayList<DtoRegistroContratarInclusion> getRegistrosContratarInclusion() {
		return registrosContratarInclusion;
	}


	/**
	 * Obtiene el valor del atributo indexRegInclusion
	 *
	 * @return Retorna atributo indexRegInclusion
	 */
	public int getIndexRegInclusion()
	{
		return indexRegInclusion;
	}


	/**
	 * Establece el valor del atributo indexRegInclusion
	 *
	 * @param valor para el atributo indexRegInclusion
	 */
	public void setIndexRegInclusion(int indexRegInclusion)
	{
		this.indexRegInclusion = indexRegInclusion;
	}


	/**
	 * Obtiene el valor del atributo indexConvenio
	 *
	 * @return Retorna atributo indexConvenio
	 */
	public int getIndexConvenio()
	{
		return indexConvenio;
	}


	/**
	 * Establece el valor del atributo indexConvenio
	 *
	 * @param valor para el atributo indexConvenio
	 */
	public void setIndexConvenio(int indexConvenio)
	{
		this.indexConvenio = indexConvenio;
	}


	/**
	 * Obtiene el valor del atributo checkContratado
	 *
	 * @return Retorna atributo checkContratado
	 */
	public boolean isCheckPropiedadPrograma()
	{
		return checkPropiedadPrograma;
	}


	/**
	 * Establece el valor del atributo checkContratado
	 *
	 * @param valor para el atributo checkContratado
	 */
	public void setCheckPropiedadPrograma(boolean checkPropiedadPrograma)
	{
		this.checkPropiedadPrograma = checkPropiedadPrograma;
	}


	/**
	 * @param totalesContratarInclusion the totalesContratarInclusion to set
	 */
	public void setTotalesContratarInclusion(DtoTotalesContratarInclusion totalesContratarInclusion) {
		this.totalesContratarInclusion = totalesContratarInclusion;
	}


	/**
	 * @return the totalesContratarInclusion
	 */
	public DtoTotalesContratarInclusion getTotalesContratarInclusion() {
		return totalesContratarInclusion;
	}
	
	/**
	 * @return the consecutivoPresupuesto
	 */
	public BigDecimal getConsecutivoPresupuesto() {
		return consecutivoPresupuesto;
	}

	/**
	 * @param consecutivoPresupuesto the consecutivoPresupuesto to set
	 */
	public void setConsecutivoPresupuesto(BigDecimal consecutivoPresupuesto) {
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}

	/**
	 * @param posicionRegistroAEliminar the posicionRegistroAEliminar to set
	 */
	public void setPosicionRegistroAEliminar(int posicionRegistroAEliminar) {
		this.posicionRegistroAEliminar = posicionRegistroAEliminar;
	}

	/**
	 * @return the posicionRegistroAEliminar
	 */
	public int getPosicionRegistroAEliminar() {
		return posicionRegistroAEliminar;
	}

	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * @param indiceRegistroExclusion the indiceRegistroExclusion to set
	 */
	public void setIndiceRegistroExclusion(int indiceRegistroExclusion) {
		this.indiceRegistroExclusion = indiceRegistroExclusion;
	}

	/**
	 * @return the indiceRegistroExclusion
	 */
	public int getIndiceRegistroExclusion() {
		return indiceRegistroExclusion;
	}

	/**
	 * @param registrosExclusion the registrosExclusion to set
	 */
	public void setRegistrosExclusion(ArrayList<ExcluPresuEncabezado> registrosExclusion) {
		this.registrosExclusion = registrosExclusion;
	}

	/**
	 * @return the registrosExclusion
	 */
	public ArrayList<ExcluPresuEncabezado> getRegistrosExclusion() {
		return registrosExclusion;
	}

	/**
	 * @param registroExclusionDetalle the registroExclusionDetalle to set
	 */
	public void setRegistroExclusionDetalle(ExcluPresuEncabezado registroExclusionDetalle) {
		this.registroExclusionDetalle = registroExclusionDetalle;
	}

	/**
	 * @return the registroExclusionDetalle
	 */
	public ExcluPresuEncabezado getRegistroExclusionDetalle() {
		return registroExclusionDetalle;
	}
	
	/**
	 * Método que retorna el valor total de las exclusiones por registro.
	 * 
	 * @param indiceRegistroExclusion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	
	public String getValorTotalExclusionPorRegistro (String indiceRegistroExclusion){
		
		double valorTotalExclusiones = ConstantesBD.codigoNuncaValidoDouble;
		
		if(UtilidadTexto.isNumber(indiceRegistroExclusion)){
			
			int indice = Integer.parseInt(indiceRegistroExclusion);
			
			ExcluPresuEncabezado registroExclusion = getRegistrosExclusion().get(indice);
			
			if(registroExclusion!=null){
				
				for (Iterator<ExclusionesPresupuesto> exclusionesPresupuesto = registroExclusion.getExclusionesPresupuestos().iterator(); exclusionesPresupuesto.hasNext();) {
					
					ExclusionesPresupuesto exclusionPresupuesto = (ExclusionesPresupuesto) exclusionesPresupuesto.next();
					
					if(exclusionPresupuesto!=null && exclusionPresupuesto.getValor()!=null){
						
						valorTotalExclusiones += exclusionPresupuesto.getValor().doubleValue();
					}
				}
			}
		}
		
		return UtilidadTexto.formatearValores(valorTotalExclusiones);
	}
	
	
	/**
	 * Método que retorna el nombre completo del responsable que realizó la exclusión
	 * 
	 * @return
	 */
	public String getNombreCompletoResponsableExclusion(){
		
		String nombreResponsable = "";
		
		ExcluPresuEncabezado exclusion = getRegistroExclusionDetalle();
		
		if(exclusion!=null){
			
			Personas persona = exclusion.getUsuarios().getPersonas();
			
			nombreResponsable = persona.getPrimerNombre() +" "+ persona.getSegundoNombre() +" "+ 
								persona.getPrimerApellido() +" "+ persona.getSegundoApellido();
		}
		
		return nombreResponsable;
	}

	/**
	 * @param indiceInclusionMostrar the indiceInclusionMostrar to set
	 */
	public void setIndiceInclusionMostrar(int indiceInclusionMostrar) {
		this.indiceInclusionMostrar = indiceInclusionMostrar;
	}

	/**
	 * @return the indiceInclusionMostrar
	 */
	public int getIndiceInclusionMostrar() {
		return indiceInclusionMostrar;
	}


//	/**
//	 * @param registrosInclusionDetalle the registrosInclusionDetalle to set
//	 */
//	public void setRegistrosInclusionDetalle(
//			ArrayList<DtoRegistroContratarInclusion> registrosInclusionDetalle) {
//		this.registrosInclusionDetalle = registrosInclusionDetalle;
//	}
//
//	/**
//	 * @return the registrosInclusionDetalle
//	 */
//	public ArrayList<DtoRegistroContratarInclusion> getRegistrosInclusionDetalle() {
//		return registrosInclusionDetalle;
//	}
	

	/**
	 * Obtiene el valor del atributo permiteGenerarSolicitudDescuento
	 *
	 * @return Retorna atributo permiteGenerarSolicitudDescuento
	 */
	public boolean isPermiteGenerarSolicitudDescuento()
	{
		return permiteGenerarSolicitudDescuento;
	}

	/**
	 * Establece el valor del atributo permiteGenerarSolicitudDescuento
	 *
	 * @param valor para el atributo permiteGenerarSolicitudDescuento
	 */
	public void setPermiteGenerarSolicitudDescuento(
			boolean permiteGenerarSolicitudDescuento)
	{
		this.permiteGenerarSolicitudDescuento = permiteGenerarSolicitudDescuento;
	}
	
//	/**
//	 * @param registroInclusionDetalle the registroInclusionDetalle to set
//	 */
//	public void setRegistroInclusionDetalle(IncluPresuEncabezado registroInclusionDetalle) {
//		this.registroInclusionDetalle = registroInclusionDetalle;
//	}
//
//	/**
//	 * @return the registroInclusionDetalle
//	 */
//	public IncluPresuEncabezado getRegistroInclusionDetalle() {
//		return registroInclusionDetalle;
//	}
//
//	/**
//	 * @param listaSumatoriaConveniosDetalleInclusion the listaSumatoriaConveniosDetalleInclusion to set
//	 */
//	public void setListaSumatoriaConveniosDetalleInclusion(
//			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConveniosDetalleInclusion) {
//		this.listaSumatoriaConveniosDetalleInclusion = listaSumatoriaConveniosDetalleInclusion;
//	}
//
//	/**
//	 * @return the listaSumatoriaConveniosDetalleInclusion
//	 */
//	public ArrayList<DtoPresupuestoTotalConvenio> getListaSumatoriaConveniosDetalleInclusion() {
//		return listaSumatoriaConveniosDetalleInclusion;
//	}

	/**
	 * @param colspanDetalleInclusiones the colspanDetalleInclusiones to set
	 */
	public void setColspanDetalleInclusiones(int colspanDetalleInclusiones) {
		this.colspanDetalleInclusiones = colspanDetalleInclusiones;
	}

	/**
	 * @return the colspanDetalleInclusiones
	 */
	public int getColspanDetalleInclusiones() {
		
		colspanDetalleInclusiones = colspanDetalleInclusiones + (this.getDtoContratarInclusion().getListaSumatoriaConvenios().size() * 2);
		
		return colspanDetalleInclusiones;
	}

	/**
	 * @param registrosInclusion the registrosInclusion to set
	 */
	public void setRegistrosInclusion(ArrayList<DtoEncabezadoInclusion> registrosInclusion) {
		this.registrosInclusion = registrosInclusion;
	}

	/**
	 * @return the registrosInclusion
	 */
	public ArrayList<DtoEncabezadoInclusion> getRegistrosInclusion() {
		return registrosInclusion;
	}

	/**
	 * @param existenInclusionesPrecontratadas the existenInclusionesPrecontratadas to set
	 */
	public void setExistenInclusionesPrecontratadas(
			boolean existenInclusionesPrecontratadas) {
		this.existenInclusionesPrecontratadas = existenInclusionesPrecontratadas;
	}

	/**
	 * @return the existenInclusionesPrecontratadas
	 */
	public boolean isExistenInclusionesPrecontratadas() {
		return existenInclusionesPrecontratadas;
	}
	
	/**
	 * @return the tomarDecisionProcesoContratacion
	 */
	public boolean isTomarDecisionProcesoContratacion() {
		return tomarDecisionProcesoContratacion;
	}


	/**
	 * @param tomarDecisionProcesoContratacion the tomarDecisionProcesoContratacion to set
	 */
	public void setTomarDecisionProcesoContratacion(
			boolean tomarDecisionProcesoContratacion) {
		this.tomarDecisionProcesoContratacion = tomarDecisionProcesoContratacion;
	}


	/**
	 * @return the decisionPreguntaContratarInclusion
	 */
	public String getDecisionPreguntaContratarInclusion() {
		return decisionPreguntaContratarInclusion;
	}


	/**
	 * @param decisionPreguntaContratarInclusion the decisionPreguntaContratarInclusion to set
	 */
	public void setDecisionPreguntaContratarInclusion(
			String decisionPreguntaContratarInclusion) {
		this.decisionPreguntaContratarInclusion = decisionPreguntaContratarInclusion;
	}


	/**
	 * @param dtoContratarInclusion the dtoContratarInclusion to set
	 */
	public void setDtoContratarInclusion(DtoContratarInclusion dtoContratarInclusion) {
		this.dtoContratarInclusion = dtoContratarInclusion;
	}


	/**
	 * @return the dtoContratarInclusion
	 */
	public DtoContratarInclusion getDtoContratarInclusion() {
		return dtoContratarInclusion;
	}


	/**
	 * @param preguntaContratarPrecontrado the preguntaContratarPrecontrado to set
	 */
	public void setPreguntaContratarPrecontrado(
			String preguntaContratarPrecontrado) {
		this.preguntaContratarPrecontrado = preguntaContratarPrecontrado;
	}


	/**
	 * @return the preguntaContratarPrecontrado
	 */
	public String getPreguntaContratarPrecontrado() {
		return preguntaContratarPrecontrado;
	}


	/**
	 * @param noDefinidoMotivoAnulacion the noDefinidoMotivoAnulacion to set
	 */
	public void setNoDefinidoMotivoAnulacion(boolean noDefinidoMotivoAnulacion) {
		this.noDefinidoMotivoAnulacion = noDefinidoMotivoAnulacion;
	}


	/**
	 * @return the noDefinidoMotivoAnulacion
	 */
	public boolean isNoDefinidoMotivoAnulacion() {
		return noDefinidoMotivoAnulacion;
	}

}