package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.General.InfoDatosBD;

import com.princetonsa.mundo.odontologia.PlanTratamiento;
/**
 * CLASE PARA GUARDAR EL SERVICIO Y EL ESTADO DEL SERVICIO PARA UN PROGRAMA 
 * DEL PLAN DE TRATAMIENTO EN LA TABLA PROGRAMA SERVICIO PLAN
 * 
 * @author axioma
 *
 */
public class InfoServicios implements Serializable {
	
	/**
	 * Versión serial
	 */
	
	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Código pk del registro del servicio
	 * en la tabla servicios_cita_odontologica
	 */
	private long codigoPk;
	
	private BigDecimal codigoPkProgServ;
	private InfoDatosInt servicio;
	private String estadoServicio;
	private String newEstado;
	private String codigoMostrar;
	private int orderServicio;
	private String porConfirmar;
	private int numeroSuperficies;
	private int duracionCita;
	private boolean modificaDuracionCita;
	
	
	
	/**
	 * listaServArt= Carga los articulos que tiene cada servicio 
	 */
	private boolean tieneArticulos;
	/**
	 * value = existe en base de datos (S/N)
	 * activo = fue eliminado por el usuario y debe ser eliminado de la BD (si esta en false)
	 * */
	private InfoDatosBD existeBD; 
	
	// array de posible estados de servicios a los que puede evolucionar el servicio
	private ArrayList<InfoDatosString> arrayPosEstServ;
	
	//*********ATRIBUTOS QUE TIENEN QEU VER CON EL PLAN DE TRATAMIENTO***********************
	private BigDecimal codigoValoracion;
	private BigDecimal codigoEvolucion;
	private BigDecimal codigoCita;
	private String inclusion;
	private String exclusion;
	private String garantia;
	private InfoDatosStr motivoCancelacion;
	private boolean esSegundoOmas; 
	//****************************************************************************************
	
	//****************************************************************************************
	// Atributos de posicionamiento del servicio para la seccion de programas/servicio cita
	// de la evolución.
	private int posDet;
	private int posSupf;
	private int posProg;
	private int posServ;
	private String seccionAplica;
	private String servicioParCita;
	//****************************************************************************************
	
	// atributo que verifica si el servicio hara parte de una cita programada o no 
	private String incluirCita;
	private String habilitarServ;
	private String marcarServ;
	private String inactivarServ;
	private InfoDatosString msjErrorServicioCita;
	private String pathConvencionPrograma;
	private String archivoConvencionPrograma;
	private String eliminarSeleccionProxCita;
	private int contrato;
	
	
	
	
	//ATRIBUTO PARA MOSTRAR EL COLOR DEL SERVCIO DE LA CITA EVOLUCIONADA
	private String colorServicioCita;
	
	
	/**
	 * Programa al que se encuentra asociado el servicio
	 */
	private InfoProgramaServicioPlan programaAsociado;
	
	/**
	 * Utilizado para almacenar el usuario que realiz&oacute; la confirmaci&oacute;n del servicio
	 */
	private String usuarioConfirmacion;
	
	/**
	 * Ayudante para mostrar en presentacion la inclusion, exclusion y garantia
	 */
	private String nombreAyunInExGar;
	/**
	 * 
	 */
	private String nombreAyudanteEstadoServicio;
	
	/**
	 * Estos parámetros son utilizados para conocer si el servicio esta asociado
	 * a alguna otra cita en estado Asignado, Reservado o Programado.
	 */
	private boolean asociadoAOtraCita;
	private String fechaCitaAsociada;
	
	
	public InfoServicios() {
		super();
		this.reset();
	}
	

	void reset(){
		
		this.codigoPkProgServ = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.servicio = new InfoDatosInt();
		this.estadoServicio = "";
		this.newEstado = "";
		this.codigoMostrar="";
		this.existeBD = new InfoDatosBD();
		this.orderServicio = ConstantesBD.codigoNuncaValido;
		this.porConfirmar = "";
		this.numeroSuperficies = ConstantesBD.codigoNuncaValido;
		this.arrayPosEstServ = new ArrayList<InfoDatosString>();
		this.incluirCita = ConstantesBD.acronimoNo;
		this.habilitarServ = ConstantesBD.acronimoNo;
		this.marcarServ = ConstantesBD.acronimoNo;
		this.inactivarServ = ConstantesBD.acronimoNo;
		this.msjErrorServicioCita = new InfoDatosString();
		this.codigoValoracion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.codigoEvolucion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.codigoCita = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.eliminarSeleccionProxCita= ConstantesBD.acronimoNo;
	
		this.inclusion = "";
		this.exclusion = "";
		this.garantia = "";
		this.motivoCancelacion = new InfoDatosStr();
		
		this.posDet = ConstantesBD.codigoNuncaValido;
		this.posSupf = ConstantesBD.codigoNuncaValido;
		this.posProg = ConstantesBD.codigoNuncaValido;
		this.posServ = ConstantesBD.codigoNuncaValido;
		this.seccionAplica = "";
		this.servicioParCita = ""; 
		
		this.pathConvencionPrograma = "";
		this.archivoConvencionPrograma = "";
	
		this.tieneArticulos=false;
		this.contrato= 0;
		this.esSegundoOmas=false;
		
		this.usuarioConfirmacion="";
		this.nombreAyunInExGar="";
		
		this.setColorServicioCita("#FFFFFF"); // SIEMPRE BLANCO POR DEFECTO 
		this.nombreAyudanteEstadoServicio="";
		this.duracionCita=ConstantesBD.codigoNuncaValido;
		this.modificaDuracionCita=Boolean.TRUE;
		
		
		this.codigoPk = ConstantesBD.codigoNuncaValidoLong;
		
		this.asociadoAOtraCita = false;
		
		this.setFechaCitaAsociada("");
	}

	/**
	 * 
	 * Metodo para obtener el codigo de la cita 
	 * @return
	 * @version  1.0.0 
	 * @see
	 */
	public BigDecimal getCodigoCitaFechaActual(ArrayList<String> estado)
	{
		BigDecimal retorna= BigDecimal.ZERO;
		if(this.getCodigoPkProgServ().doubleValue()>0)
		{
			retorna= PlanTratamiento.obtenerCodigoPkUltimaCitaProgServPlanT(this.getCodigoPkProgServ(), UtilidadFecha.getFechaActual(),estado);
		}
		return retorna;
	}

	/**
	 * 
	 * Metodo para obtener el codigo de la cita 
	 * @return
	 * @version  1.0.0 
	 * @see
	 */
	public BigDecimal getCodigoCitaNoImportaFecha(ArrayList<String> estado)
	{
		BigDecimal retorna= BigDecimal.ZERO;
		if(this.getCodigoPkProgServ().doubleValue()>0)
		{
			retorna= PlanTratamiento.obtenerCodigoPkUltimaCitaProgServPlanT(this.getCodigoPkProgServ(), "",estado);
		}
		return retorna;
	}
	
	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}


	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}


	/**
	 * @return the estadoServicio
	 */
	public String getEstadoServicio() {
		return estadoServicio;
	}


	/**
	 * @param estadoServicio the estadoServicio to set
	 */
	public void setEstadoServicio(String estadoServicio) {
		this.estadoServicio = estadoServicio;
	}


	public void setCodigoMostrar(String codigoMostrar) {
		this.codigoMostrar = codigoMostrar;
	}


	public String getCodigoMostrar() {
		return codigoMostrar;
	}

	public int getOrderServicio() {
		return orderServicio;
	}


	public void setOrderServicio(int orderServicio) {
		this.orderServicio = orderServicio;
	}


	public BigDecimal getCodigoPkProgServ() {
		return codigoPkProgServ;
	}


	public void setCodigoPkProgServ(BigDecimal codigoPkProgServ) {
		this.codigoPkProgServ = codigoPkProgServ;
	}


	public void setExisteBD(InfoDatosBD existeBD) {
		this.existeBD = existeBD;
	}


	public InfoDatosBD getExisteBD() {
		return existeBD;
	}


	public String getPorConfirmar() {
		return porConfirmar;
	}


	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}


	public int getNumeroSuperficies() {
		return numeroSuperficies;
	}


	public void setNumeroSuperficies(int numeroSuperficies) {
		this.numeroSuperficies = numeroSuperficies;
	}

	/**
	 * @return the arrayPosEstServ
	 */
	public ArrayList<InfoDatosString> getArrayPosEstServ() {
		return arrayPosEstServ;
	}

	/**
	 * @param arrayPosEstServ the arrayPosEstServ to set
	 */
	public void setArrayPosEstServ(ArrayList<InfoDatosString> arrayPosEstServ) {
		this.arrayPosEstServ = arrayPosEstServ;
	}

	/**
	 * @return the incluirCita
	 */
	public String getIncluirCita() {
		return incluirCita;
	}
	
	/**
	 * @return the msjErrorServicioCita
	 */
	public InfoDatosString getMsjErrorServicioCita() {
		return msjErrorServicioCita;
	}

	/**
	 * @param msjErrorServicioCita the msjErrorServicioCita to set
	 */
	public void setMsjErrorServicioCita(InfoDatosString msjErrorServicioCita) {
		this.msjErrorServicioCita = msjErrorServicioCita;
	}

	/**
	 * @param incluirCita the incluirCita to set
	 */
	public void setIncluirCita(String incluirCita) {
		this.incluirCita = incluirCita;
	}

	
	/**
	 * @return the codigoValoracion
	 */
	public BigDecimal getCodigoValoracion() {
		return codigoValoracion;
	}


	/**
	 * @param codigoValoracion the codigoValoracion to set
	 */
	public void setCodigoValoracion(BigDecimal codigoValoracion) {
		this.codigoValoracion = codigoValoracion;
	}


	/**
	 * @return the codigoEvolucion
	 */
	public BigDecimal getCodigoEvolucion() {
		return codigoEvolucion;
	}


	/**
	 * @param codigoEvolucion the codigoEvolucion to set
	 */
	public void setCodigoEvolucion(BigDecimal codigoEvolucion) {
		this.codigoEvolucion = codigoEvolucion;
	}


	/**
	 * @return the codigoCita
	 */
	public BigDecimal getCodigoCita() {
		return codigoCita;
	}


	/**
	 * @param codigoCita the codigoCita to set
	 */
	public void setCodigoCita(BigDecimal codigoCita) {
		this.codigoCita = codigoCita;
	}

	/**
	 * @return the inclusion
	 */
	public String getInclusion() {
		return inclusion;
	}


	/**
	 * @param inclusion the inclusion to set
	 */
	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}


	/**
	 * @return the exclusion
	 */
	public String getExclusion() {
		return exclusion;
	}


	/**
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}


	/**
	 * @return the garantia
	 */
	public String getGarantia() {
		return garantia;
	}


	/**
	 * @param garantia the garantia to set
	 */
	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}

	/**
	 * @return the newEstado
	 */
	public String getNewEstado() {
		return newEstado;
	}

	/**
	 * @param newEstado the newEstado to set
	 */
	public void setNewEstado(String newEstado) {
		this.newEstado = newEstado;
	}

	/**
	 * @return the habilitarServ
	 */
	public String getHabilitarServ() {
		return habilitarServ;
	}

	/**
	 * @param habilitarServ the habilitarServ to set
	 */
	public void setHabilitarServ(String habilitarServ) {
		this.habilitarServ = habilitarServ;
	}

	/**
	 * @return the inactivarServ
	 */
	public String getInactivarServ() {
		return inactivarServ;
	}

	/**
	 * @param inactivarServ the inactivarServ to set
	 */
	public void setInactivarServ(String inactivarServ) {
		this.inactivarServ = inactivarServ;
	}

	/**
	 * @return the marcarServ
	 */
	public String getMarcarServ() {
		return marcarServ;
	}

	/**
	 * @param marcarServ the marcarServ to set
	 */
	public void setMarcarServ(String marcarServ) {
		this.marcarServ = marcarServ;
	}


	public String getArchivoConvencionPrograma() {
		return archivoConvencionPrograma;
	}


	public void setArchivoConvencionPrograma(String archivoConvencionPrograma) {
		this.archivoConvencionPrograma = archivoConvencionPrograma;
	}


	public String getPathConvencionPrograma() {
		return pathConvencionPrograma;
	}


	public void setPathConvencionPrograma(String pathConvencionPrograma) {
		this.pathConvencionPrograma = pathConvencionPrograma;
	}

	/**
	 * @return the eliminarSeleccionProxCita
	 */
	public String getEliminarSeleccionProxCita() {
		return eliminarSeleccionProxCita;
	}


	/**
	 * @param eliminarSeleccionProxCita the eliminarSeleccionProxCita to set
	 */
	public void setEliminarSeleccionProxCita(String eliminarSeleccionProxCita) {
		this.eliminarSeleccionProxCita = eliminarSeleccionProxCita;
	}

	/**
	 * @return the motivoCancelacion
	 */
	public InfoDatosStr getMotivoCancelacion() {
		return motivoCancelacion;
	}

	/**
	 * @param motivoCancelacion the motivoCancelacion to set
	 */
	public void setMotivoCancelacion(InfoDatosStr motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}



	public void setTieneArticulos(boolean tieneArticulos) {
		this.tieneArticulos = tieneArticulos;
	}


	public boolean isTieneArticulos() {
		return tieneArticulos;
	}

	public boolean getTieneArticulos() {
		return tieneArticulos;
	}

	/**
	 * @return the posDet
	 */
	public int getPosDet() {
		return posDet;
	}

	/**
	 * @param posDet the posDet to set
	 */
	public void setPosDet(int posDet) {
		this.posDet = posDet;
	}

	/**
	 * @return the posSupf
	 */
	public int getPosSupf() {
		return posSupf;
	}

	/**
	 * @param posSupf the posSupf to set
	 */
	public void setPosSupf(int posSupf) {
		this.posSupf = posSupf;
	}

	/**
	 * @return the posProg
	 */
	public int getPosProg() {
		return posProg;
	}

	/**
	 * @param posProg the posProg to set
	 */
	public void setPosProg(int posProg) {
		this.posProg = posProg;
	}

	/**
	 * @return the posServ
	 */
	public int getPosServ() {
		return posServ;
	}

	/**
	 * @param posServ the posServ to set
	 */
	public void setPosServ(int posServ) {
		this.posServ = posServ;
	}

	/**
	 * @return the seccionAplica
	 */
	public String getSeccionAplica() {
		return seccionAplica;
	}

	/**
	 * @param seccionAplica the seccionAplica to set
	 */
	public void setSeccionAplica(String seccionAplica) {
		this.seccionAplica = seccionAplica;
	}


	/**
	 * @return the servicioParCita
	 */
	public String getServicioParCita() {
		return servicioParCita;
	}


	/**
	 * @param servicioParCita the servicioParCita to set
	 */
	public void setServicioParCita(String servicioParCita) {
		this.servicioParCita = servicioParCita;
	}


	/**
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}


	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}


	/**
	 * @return the esSegundoOMas
	 */
	public boolean isEsSegundoOmas() {
		return esSegundoOmas;
	}


	/**
	 * @param esSegundoOMas the esSegundoOMas to set
	 */
	public void setEsSegundoOmas(boolean esSegundoOMas) {
		this.esSegundoOmas = esSegundoOMas;
	}

	/**
	 * Retorna usuarioConfirmacion
	 * @return String Login del usuario que realiz&oacute; la confirmaci&oacute;n 
	 */
	public String getUsuarioConfirmacion()
	{
		return usuarioConfirmacion;
	}

	/**
	 * Asigna usuarioConfirmacion
	 * @param usuarioConfirmacion Login del usuario que realiz&oacute; la confirmaci&oacute;n
	 */
	public void setUsuarioConfirmacion(String usuarioConfirmacion)
	{
		this.usuarioConfirmacion = usuarioConfirmacion;
	}

	/**
	 * @return Retorna el atributo programaAsociado
	 */
	public InfoProgramaServicioPlan getProgramaAsociado()
	{
		return programaAsociado;
	}


	/**
	 * @param programaAsociado Asigna el atributo programaAsociado
	 */
	public void setProgramaAsociado(InfoProgramaServicioPlan programaAsociado)
	{
		this.programaAsociado = programaAsociado;
	}

	/**
	 * Ayudante para ordenar por el c&oacute;digo del programa hallazgo pieza
	 * @return c&oacute;digo del programa.
	 */
	public int getAyudanteCodigoProgramaHallazgoPieza()
	{
		return getProgramaAsociado().getProgHallazgoPieza().getCodigoPk();
	}

	public void setNombreAyunInExGar(String nombreAyunInExGar) {
		this.nombreAyunInExGar = nombreAyunInExGar;
	}


	/**
	 * AYUNDATE PARA CARGAR LA INCLUSION, EXCLUSION Y GARANTIA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombreAyunInExGar() 
	{
		if(this.inclusion.equals(ConstantesBD.acronimoSi) )
		{
			nombreAyunInExGar=" I ";
		}
		if(this.exclusion.equals(ConstantesBD.acronimoSi))
		{
			nombreAyunInExGar+=" E ";
		}
		if(this.garantia.equals(ConstantesBD.acronimoSi))
		{
			nombreAyunInExGar+=" G ";
		}
		
		return nombreAyunInExGar;
	}


	public void setColorServicioCita(String colorServicioCita) {
		this.colorServicioCita = colorServicioCita;
	}


	public String getColorServicioCita() {
		return colorServicioCita;
	}

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param nombreAyudanteEstadoPrograma
	 */
	public void setNombreAyudanteEstadoServicio(
			String nombreAyudanteEstadoServicio) {
		this.nombreAyudanteEstadoServicio = nombreAyudanteEstadoServicio;
	}


	/**
	 * Atributo ayudante para Mostrar en presentacion grafica el nombre del estadoServicio
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombreAyudanteEstadoServicio() 
	{
		
		try 
		{
			if(!UtilidadTexto.isEmpty(this.estadoServicio))
			{
				this.nombreAyudanteEstadoServicio=ValoresPorDefecto.getIntegridadDominio(this.estadoServicio).toString();
			}
			
		} 
		catch (Exception e) 
		{
			Log4JManager.error(e);
		}
		
		return nombreAyudanteEstadoServicio;
	}


	public void setDuracionCita(int duracionCita) {
		this.duracionCita = duracionCita;
	}


	public int getDuracionCita() {
		return duracionCita;
	}


	public void setModificaDuracionCita(boolean modificaDuracionCita) {
		this.modificaDuracionCita = modificaDuracionCita;
	}


	public boolean isModificaDuracionCita() {
		if(this.duracionCita>0)
		{
			modificaDuracionCita=Boolean.FALSE;
		}
		
		return modificaDuracionCita;
	}


	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}


	/**
	 * @return the codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}


	/**
	 * @param asociadoAOtraCita the asociadoAOtraCita to set
	 */
	public void setAsociadoAOtraCita(boolean asociadoAOtraCita) {
		this.asociadoAOtraCita = asociadoAOtraCita;
	}


	/**
	 * @return the asociadoAOtraCita
	 */
	public boolean isAsociadoAOtraCita() {
		return asociadoAOtraCita;
	}


	/**
	 * @param fechaCitaAsociada the fechaCitaAsociada to set
	 */
	public void setFechaCitaAsociada(String fechaCitaAsociada) {
		this.fechaCitaAsociada = fechaCitaAsociada;
	}


	/**
	 * @return the fechaCitaAsociada
	 */
	public String getFechaCitaAsociada() {
		return fechaCitaAsociada;
	}


}
