package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;




import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class InfoPlanTratamiento implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	ATRIBUTOS DEL PLAN DE TRATAMIENTO 
	 */
	
	 private String estadoPlan;
	 
	 private String acronimoEstadoPlan;

	 private String consecutivoPlan;
	 
	 private String fechaContrato;
	  
	 private String estadoPresupuesto;
	 
	 private BigDecimal numeroPresupuesto;
	 
	 private String fechaGrabacion;
	 
	 private String horaGrabacion;
	 
	 private String fechaEvolucion;
	 
	 private int ingreso;
	 	
	 /**
	 * PARAMETRO GENERAL PARA PRESENTAR LOS DATOS DEL PRESUPUESTO 
	 */
	 private String validaPresupuestoOdontologico;
	
	/**
	 * 
	 */
	private BigDecimal codigoPk;
	
	/**
	 * 
	 */
	private ArrayList<InfoDetallePlanTramiento> seccionHallazgosDetalle;
	
	/**
	 * 
	 */
	private ArrayList<InfoDetallePlanTramiento> seccionOtrosHallazgos;
	
	/**
	 * 
	 */
	private ArrayList<InfoHallazgoSuperficie> seccionHallazgosBoca;
	
	
	
	/**
	 * METODOS AYUDANTE PARA VALIDAR LA PRESENTACION 
	 */
	private boolean existeSeccionBoca;
	
	private boolean existeSeccioDetalle;
	
	private boolean existeSeccionOtros;
	
	
	
	


	/**
	 * arreglo que contiene el listado de los hallazgos d detarro y otros
	 * para la seccion de programas/servicios de cita asignada
	 */
	private ArrayList<InfoDetallePlanTramiento> seccionProgServCita ;
	private ArrayList<InfoDetallePlanTramiento> seccionProgServCitaOtro ;
	
	/**
	 * LISTA PARA CARGA LAS INCLUSINES
	 */
	
	private ArrayList<InfoDetallePlanTramiento> seccionInclusiones ;
	
	/**
	 * 
	 */
	private ArrayList<InfoDetallePlanTramiento> seccionGarantias;
	
	/**
	 * 
	 */
	private String migrado;
	
	/**
	 * 
	 */
	private String estado;

	/**
	 * 
	 * */
	private String porConfirmar;
	
	/**
	 * 
	 */
	private String esConsulta;
	/**
	 * 
	 */
	private String imagen;
	
	

	
	
	
	
	//**************ATRIBUTOS ADICIONALES PARA VALIDACIONES****************************
	private BigDecimal codigoPresupuesto;
	//************************************************************************************
	//**************ATRIBUTOS ADICIONALES PARA HISTORICO*************************************
	private BigDecimal valoracion;
	private BigDecimal evolucion;
	private String consecutivoPresupuesto;
	private BigDecimal numeroConsecutivoPresupuesto;
	//*****************************************************************************************
	
	
	public InfoPlanTratamiento() 
	{
		super();
		this.codigoPk = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.migrado="";
		this.estado="";
		this.seccionHallazgosDetalle = new ArrayList<InfoDetallePlanTramiento>();
		this.seccionOtrosHallazgos = new ArrayList<InfoDetallePlanTramiento>();
		this.seccionHallazgosBoca = new ArrayList<InfoHallazgoSuperficie>();
		this.seccionProgServCita = new ArrayList<InfoDetallePlanTramiento>();
		this.seccionProgServCitaOtro = new ArrayList<InfoDetallePlanTramiento>();
		this.porConfirmar = "";
		this.seccionInclusiones = new ArrayList<InfoDetallePlanTramiento>();
		this.seccionGarantias = new ArrayList<InfoDetallePlanTramiento>();
		this.fechaEvolucion = "";
		this.esConsulta="";
		
		this.codigoPresupuesto = new BigDecimal(ConstantesBD.codigoNuncaValido);
		
		this.valoracion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.evolucion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		
		this.imagen = "";
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.consecutivoPresupuesto="";
		this.numeroConsecutivoPresupuesto=BigDecimal.ZERO;
		
		
		this.existeSeccioDetalle=Boolean.FALSE;
		this.existeSeccionBoca=Boolean.FALSE;
		this.existeSeccionOtros=Boolean.FALSE;
		this.horaGrabacion="";
		this.acronimoEstadoPlan="";
	}

	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the seccionHallazgosDetalle
	 */
	public ArrayList<InfoDetallePlanTramiento> getSeccionHallazgosDetalle() {
		return seccionHallazgosDetalle;
	}

	/**
	 * @param seccionHallazgosDetalle the seccionHallazgosDetalle to set
	 */
	public void setSeccionHallazgosDetalle(
			ArrayList<InfoDetallePlanTramiento> seccionHallazgosDetalle) {
		this.seccionHallazgosDetalle = seccionHallazgosDetalle;
	}

	/**
	 * @return the seccionOtrosHallazgos
	 */
	public ArrayList<InfoDetallePlanTramiento> getSeccionOtrosHallazgos() {
		return seccionOtrosHallazgos;
	}

	/**
	 * @param seccionOtrosHallazgos the seccionOtrosHallazgos to set
	 */
	public void setSeccionOtrosHallazgos(
			ArrayList<InfoDetallePlanTramiento> seccionOtrosHallazgos) {
		this.seccionOtrosHallazgos = seccionOtrosHallazgos;
	}

	/**
	 * @return the seccionHallazgosBoca
	 */
	public ArrayList<InfoHallazgoSuperficie> getSeccionHallazgosBoca() {
		return seccionHallazgosBoca;
	}

	/**
	 * @param seccionHallazgosBoca the seccionHallazgosBoca to set
	 */
	public void setSeccionHallazgosBoca(
			ArrayList<InfoHallazgoSuperficie> seccionHallazgosBoca) {
		this.seccionHallazgosBoca = seccionHallazgosBoca;
	}

	/**
	 * @return the migrado
	 */
	public String getMigrado() {
		return migrado;
	}

	/**
	 * @param estado the migrado to set
	 */
	public void setMigrado(String migrado) {
		this.migrado = migrado;
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
	  * EL METODO GET DEL ESTADO FORMATEA DEL ESTADO DEL PLAN
	 * @return the estadoPlan
	 */
	public String getEstadoPlan() {
		
		
		if(!UtilidadTexto.isEmpty(this.estadoPlan))
		{
			this.estadoPlan=ValoresPorDefecto.getIntegridadDominio(this.estadoPlan).toString();
		}
		
		return estadoPlan;
	}
	
	
	
	

	/**
	 * @param estadoPlan the estadoPlan to set
	 */
	public void setEstadoPlan(String estadoPlan) {
		this.estadoPlan = estadoPlan;
	}

	/**
	 * @return the consecutivoPlan
	 */
	public String getConsecutivoPlan() {
		return consecutivoPlan;
	}

	/**
	 * @param consecutivoPlan the consecutivoPlan to set
	 */
	public void setConsecutivoPlan(String consecutivoPlan) {
		this.consecutivoPlan = consecutivoPlan;
	}

	/**
	 * @return the fechaContrato
	 */
	public String getFechaContrato() {
		
		if(!UtilidadTexto.isEmpty(this.fechaContrato))
		{
			this.fechaContrato=UtilidadFecha.conversionFormatoFechaAAp(this.fechaContrato);
		}
		return fechaContrato;
	}

	/**
	 * @param fechaContrato the fechaContrato to set
	 */
	public void setFechaContrato(String fechaContrato) {
		this.fechaContrato = fechaContrato;
	}

	/**
	 * ESTE METODO GET DEL ESTADO PRESUPUESTO FORMATEA CON LA CLASE VALORES POR DEFECTO
	 * @return the estadoPresupuesto
	 */
	public String getEstadoPresupuesto() 
	{
		if(!UtilidadTexto.isEmpty(this.estadoPresupuesto))
		{
			this.estadoPresupuesto=ValoresPorDefecto.getIntegridadDominio(this.estadoPresupuesto).toString();
		}
		return estadoPresupuesto;
	}

	/**
	 * @param estadoPresupuesto the estadoPresupuesto to set
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
	}

	/**
	 * @return the numeroPresupuesto
	 */
	public BigDecimal getNumeroPresupuesto() {
		return numeroPresupuesto;
	}

	/**
	 * @param numeroPresupuesto the numeroPresupuesto to set
	 */
	public void setNumeroPresupuesto(BigDecimal numeroPresupuesto) {
		this.numeroPresupuesto = numeroPresupuesto;
	}



	/**
	 * @return the fechaEvolucion
	 */
	public String getFechaEvolucion() {
		return fechaEvolucion;
	}

	/**
	 * @param fechaEvolucion the fechaEvolucion to set
	 */
	public void setFechaEvolucion(String fechaEvolucion) {
		this.fechaEvolucion = fechaEvolucion;
	}

	public void setValidaPresupuestoOdontologico(
			String validaPresupuestoOdontologico) {
		this.validaPresupuestoOdontologico = validaPresupuestoOdontologico;
	}

	public String getValidaPresupuestoOdontologico() {
		return validaPresupuestoOdontologico;
	}

	public String getPorConfirmar() {
		return porConfirmar;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public void setSeccionInclusiones(ArrayList<InfoDetallePlanTramiento> seccionInclusiones) {
		this.seccionInclusiones = seccionInclusiones;
	}

	public ArrayList<InfoDetallePlanTramiento> getSeccionInclusiones() {
		return seccionInclusiones;
	}

	public void setSeccionGarantias(ArrayList<InfoDetallePlanTramiento> seccionGarantias) {
		this.seccionGarantias = seccionGarantias;
	}

	public ArrayList<InfoDetallePlanTramiento> getSeccionGarantias() {
		return seccionGarantias;
	}

	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getFechaGrabacion() {
		return fechaGrabacion;
	}

	public void setEsConsulta(String esConsulta) {
		this.esConsulta = esConsulta;
	}

	public String getEsConsulta() {
		return esConsulta;
	}
	
	
	/**
	 * Devuelve el numero de superficies que posee el mismo hallazgo
	 * @param int codigoPieza
	 * @param int codigoHallazgo
	 * */
	public int getNumeroSuperParaHallazgo(int codigoPieza,int codigoHallazgo)
	{
		int cont = 0;
		for(InfoDetallePlanTramiento pieza:seccionOtrosHallazgos)
		{
			if(pieza.getExisteBD().isActivo() 
				&& pieza.getPieza().getCodigo() == codigoPieza)
			{
				for(InfoHallazgoSuperficie hallazgo:pieza.getDetalleSuperficie())
				{
					if(hallazgo.getHallazgoREQUERIDO().getCodigo() == codigoHallazgo 
							&& hallazgo.getExisteBD().isActivo() 
								&& hallazgo.getSuperficieOPCIONAL().getCodigo() > 0)
						cont++;
				}
			}
		}

		return cont;
	}

	/**
	 * @return the codigoPresupuesto
	 */
	public BigDecimal getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	/**
	 * @param codigoPresupuesto the codigoPresupuesto to set
	 */
	public void setCodigoPresupuesto(BigDecimal codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}

	/**
	 * @return the valoracion
	 */
	public BigDecimal getValoracion() {
		return valoracion;
	}

	/**
	 * @param valoracion the valoracion to set
	 */
	public void setValoracion(BigDecimal valoracion) {
		this.valoracion = valoracion;
	}

	/**
	 * @return the evolucion
	 */
	public BigDecimal getEvolucion() {
		return evolucion;
	}

	/**
	 * @param evolucion the evolucion to set
	 */
	public void setEvolucion(BigDecimal evolucion) {
		this.evolucion = evolucion;
	}

	/**
	 * @return the imagen
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * @return the seccionProgServCita
	 */
	public ArrayList<InfoDetallePlanTramiento> getSeccionProgServCita() {
		return seccionProgServCita;
	}

	/**
	 * @param seccionProgServCita the seccionProgServCita to set
	 */
	public void setSeccionProgServCita(
			ArrayList<InfoDetallePlanTramiento> seccionProgServCita) {
		this.seccionProgServCita = seccionProgServCita;
	}

	/**
	 * @return the seccionProgServCitaOtro
	 */
	public ArrayList<InfoDetallePlanTramiento> getSeccionProgServCitaOtro() {
		return seccionProgServCitaOtro;
	}

	/**
	 * @param seccionProgServCitaOtro the seccionProgServCitaOtro to set
	 */
	public void setSeccionProgServCitaOtro(
			ArrayList<InfoDetallePlanTramiento> seccionProgServCitaOtro) {
		this.seccionProgServCitaOtro = seccionProgServCitaOtro;
	}
	
	
	//************************METODOS PARA LA VALIDACIÓN DE CAMPOS DE CAPTURA DEL ODONTOGRAMA DE EVOLUCION**********************
	/**
	 * Validación de requeridos en el odontograma Evolucion
	 */
	public void validateOdontogramaEvolucion(ActionErrors errores,boolean utilizaProgramas)
	{
		
		
		
		//1) Se verifica que si se marcó como exclusion , garantia o cancelación pero no se ingresó un motivo
		//---------SECCION DETALLE PLAN DE TRATAMIENTO ----------------------------------
		for(InfoDetallePlanTramiento detalle:this.seccionHallazgosDetalle)
		{
			recorridoSeccionesValidacion(detalle.getDetalleSuperficie(), utilizaProgramas, "detalle plan tratamiento", errores);
			
		}
		//--------SECCION OTROS HALLAZGOS--------------------------------------------------------------
		for(InfoDetallePlanTramiento detalle:this.seccionOtrosHallazgos)
		{
			recorridoSeccionesValidacion(detalle.getDetalleSuperficie(), utilizaProgramas, "evolución en piezas dentales", errores);
			
		}
		//-----------SECCION HALLAZGOS BOCA-----------------------------------------------------------
		recorridoSeccionesValidacion(this.seccionHallazgosBoca, utilizaProgramas, "evolución en boca", errores);

	}
	
	
	/**
	 * Recorrido de las secciones validación
	 * @param arregloHallazgos
	 * @param utilizaProgramas
	 * @param nombreSeccion
	 * @param errores
	 */
	private void recorridoSeccionesValidacion(ArrayList<InfoHallazgoSuperficie> arregloHallazgos,boolean utilizaProgramas,String nombreSeccion,ActionErrors errores)
	{
		String seccion = "";
		for(InfoHallazgoSuperficie hallazgo:arregloHallazgos)
		{
			for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
			{
				//Si la institucion utiliza programa y está marcado como exclusion o marcado como garantía o cancelado y no tiene mtovio hay error
				if(utilizaProgramas)
				{
					seccion = nombreSeccion;
					if(UtilidadTexto.getBoolean(programa.getInclusion()))
					{
						seccion = "inclusiones";
					}
					else if(UtilidadTexto.isEmpty(programa.getProgramasParCita()))
					{
						seccion = "cita asignada";
					}
					
					validarMotivoProgServ(seccion,"programa "+programa.getNombreProgramaServicio(), "", "", errores, programa.getNewEstadoProg(), programa.getMotivoCancelacion().getCodigo());
				}
				
				for(InfoServicios servicio:programa.getListaServicios())
				{
					seccion = nombreSeccion;
					if(UtilidadTexto.getBoolean(servicio.getInclusion()))
					{
						seccion = "inclusiones";
					}
					else if(UtilidadTexto.isEmpty(servicio.getServicioParCita()))
					{
						seccion = "cita asignada";
					}
					validarMotivoProgServ(seccion,"el servicio "+servicio.getServicio().getNombre()+" del programa "+programa.getNombreProgramaServicio(), "", "", errores, servicio.getNewEstado(), servicio.getMotivoCancelacion().getCodigo());
				}					
			}
		}
	}
	
	/**
	 * Método para hacer la validación del motivo según el estado del programa o servicio
	 * @param nombreProgServicio
	 * @param nombrePieza
	 * @param nombreSuperficie
	 * @param errores
	 * @param estado
	 * @param motivo
	 */
	private void validarMotivoProgServ(String seccion,String nombreProgServicio,String nombrePieza,String nombreSuperficie,ActionErrors errores,String estado,String motivo)
	{
		if(
				(
				//Se verifica si el programa ha sido excluido 
				estado.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)
				||
				estado.equals(ConstantesIntegridadDominio.acronimoExcluido)
				)
				// Y no tenía motivo de cancelación
				&&
				Utilidades.convertirAEntero(motivo)<=0
			)
			{
				errores.add("", new ActionMessage("errors.required","Sección "+seccion+": El motivo de exclusión para el "+nombreProgServicio+ " ("+(nombrePieza.equals("")?"Boca":"Pieza: "+nombrePieza)+(nombreSuperficie.equals("")?"":", Superficie: "+nombreSuperficie)+ ") "));
			}
			else if(
					(
					//Se verifica si el programa ha sido marcado como garantía
					estado.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia)
					||
					estado.equals(ConstantesIntegridadDominio.acronimoGarantia)
					)
					// Y no tenía motivo de cancelación
					&&
					Utilidades.convertirAEntero(motivo)<=0
			)
			{
				errores.add("", new ActionMessage("errors.required","Sección "+seccion+": El motivo de garantía para el "+nombreProgServicio+ " ("+(nombrePieza.equals("")?"Boca":"Pieza: "+nombrePieza)+(nombreSuperficie.equals("")?"":", Superficie: "+nombreSuperficie)+ ") "));
				
			}
			else if(
				
						///Se verifica si el programa ha sido cancelado
						estado.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
						
						// Y no tenía motivo de cancelación
						&&
						Utilidades.convertirAEntero(motivo)<=0
				)
			{
				errores.add("", new ActionMessage("errors.required","Sección "+seccion+": El motivo de cancelación para el "+nombreProgServicio+ " ("+(nombrePieza.equals("")?"Boca":"Pieza: "+nombrePieza)+(nombreSuperficie.equals("")?"":", Superficie: "+nombreSuperficie)+ ") "));
			}
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	public int getIngreso() {
		return ingreso;
	}

	public void setConsecutivoPresupuesto(String consecutivoPresupuesto) {
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}

	/**
	 * AYUDANTE PARA CARGAR LA INFORAMCION RESPECTIVA DEL CONSECUTIVO HISTORICO DEL PRESUPUESTO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getConsecutivoPresupuesto() {
		
		if(this.numeroConsecutivoPresupuesto!=null && this.numeroConsecutivoPresupuesto.doubleValue()>0)
		{
			if( this.getNumeroConsecutivoPresupuesto().doubleValue()>0)
			{
				this.consecutivoPresupuesto=numeroConsecutivoPresupuesto.toString();
			}
		}
		return consecutivoPresupuesto;
	}
	
	
	
	

	public void setNumeroConsecutivoPresupuesto(
			BigDecimal numeroConsecutivoPresupuesto) 
	{
		this.numeroConsecutivoPresupuesto = numeroConsecutivoPresupuesto;
	}

	public BigDecimal getNumeroConsecutivoPresupuesto() 
	{
		return numeroConsecutivoPresupuesto;
	}
	
	
	
	
	/**
	 * Existe seccion Detalle plan 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	private  boolean existenSeccionDetalle(){
		
		boolean retorno=Boolean.TRUE;
		
		
		if( this.seccionHallazgosDetalle.size()>0)
		{
			retorno=Boolean.TRUE;
		}
		
		
		return retorno;
	}
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	private boolean existeSeccionBoca(){
		
		boolean retorno=Boolean.FALSE;
		
		
		if(this.seccionHallazgosBoca.size()>0)
		{
			retorno=Boolean.TRUE;
		}
		
		return retorno;
	}
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	private boolean existeSeccionOtros()
	{
		
		boolean retorno=Boolean.FALSE;
		
		
		if(this.seccionOtrosHallazgos.size()>0)
		{
			retorno=Boolean.TRUE;
		}
		
		return retorno;
		
	}
	
	
	
	
	
	
	/**
	 * @return the existeSeccionBoca
	 */
	public boolean getExisteSeccionBoca() 
	{
		this.existeSeccionBoca=existeSeccionBoca(); 
		return existeSeccionBoca;
	}

	/**
	 * @param existeSeccionBoca the existeSeccionBoca to set
	 */
	public void setExisteSeccionBoca(boolean existeSeccionBoca) 
	{
		this.existeSeccionBoca = existeSeccionBoca;
	}

	
	
	
	
	
	/**
	 * @return the existeSeccioDetalle
	 */
	public boolean getExisteSeccioDetalle() 
	{
		this.existeSeccioDetalle=existenSeccionDetalle();
	
		return existeSeccioDetalle;
	}
	
	/**
	 * @param existeSeccioDetalle the existeSeccioDetalle to set
	 */
	public void setExisteSeccioDetalle(boolean existeSeccioDetalle) 
	{
		this.existeSeccioDetalle = existeSeccioDetalle;
	}

	
	
	
	
	/**
	 * @return the existeSeccionOtros
	 */
	public boolean getExisteSeccionOtros() 
	{
		this.existeSeccionOtros=existeSeccionOtros();
	
		return this.existeSeccionOtros;
	}

	/**
	 * @param existeSeccionOtros the existeSeccionOtros to set
	 */
	public void setExisteSeccionOtros(boolean existeSeccionOtros) 
	{
		this.existeSeccionOtros = existeSeccionOtros;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	public void setAcronimoEstadoPlan(String acronimoEstadoPlan) {
		this.acronimoEstadoPlan = acronimoEstadoPlan;
	}

	public String getAcronimoEstadoPlan() {
		return acronimoEstadoPlan;
	}
	
	
	
	
	
	
	
}
