package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.facturacion.InfoResponsableCobertura;
import util.odontologia.InfoTarifaServicioPresupuesto;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class DtoServicioCitaOdontologica implements Serializable,Cloneable
{
	/**
	 * VersionSerial
	 */
	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private int citaOdontologica;
	private String tipoCita;
	private int servicio;
	private int duracion;
	private int numeroSolicitud;
	private InfoDatosInt estadoHistoriaClinicaSolicitud;
	private String activo;
	private String usuarioModifica;
	private BigDecimal valorTarifa;
	private String estadoCita;
	private int codigoAgenda;
	private String fechaCita;
	private String horaInicio;
	private String horaFinal;
	private int unidadAgenda;
	private String aplicaAbono;
	private String aplicaAnticipo;
	private int codigoPresuOdoProgSer;
	private String garantiaServicio;
	private String facturado;
	
	private String nombreServicio;
	private String codigoPropietarioServicio;
	private String codigoTipoServicio;
	private String nombreTipoServicio;
	
	private int codigoPrograma;
	private String estadoPrograma;
	private String estadoServicioPlanTratamiento;
	private String codigoCups;
	
	private int piezaDental;
	private String seccionHallazgo;
	
	private int codigoProgramaHallazgoPieza;
	private ArrayList<DtoSuperficiesPorPrograma> superficies=new ArrayList<DtoSuperficiesPorPrograma>() ;
	
	private DtoProgHallazgoPieza programaHallazgoPieza;
	
	
	private int planTratamiento;
	
	private String descripcionPropietario;
	
	private Long programaHallazgoPiezaLong;
	
	public String getDescripcionPropietario() {
		return descripcionPropietario;
	}

	public void setDescripcionPropietario(String descripcionPropietario) {
		this.descripcionPropietario = descripcionPropietario;
	}

	//**************ATRIBUTOS USADOS PARA EL CONTROL DE SALDOS********************************
	/**
	 * Atributo que tiene la informacion de la tarifa del servicio odontologico
	 */
	private InfoTarifaServicioPresupuesto infoTarifa;
	/**
	 * Atributo que tiene la informacion de la cobertura del servicio odontologico
	 */
	private InfoResponsableCobertura infoResponsableCobertura;
	
	private double valorMaximoXPaciente; //tomado de control de anticipos
	//******************************************************************
	
	// atributos usados cambio servicios
	private String nuevoServicio;
	private String modificadoSer;
	private String eliminarSer;
	
	/**
	 * Marca usada para saber si un servicio de la cita fue evolucionado o no
	 */
	private boolean evolucionado;
	
	/**
	 * Objeto que almacena os servicios y articulos incluidos
	 */
	private ArrayList<DtoServArtIncCitaOdo> serviciosArticulosIncluidos;
	
	/**
	 * Arreglo donde se almacenan las condiciones del servicio
	 */
	private ArrayList<InfoDatosInt> condicionesServicio;
	
	/**
	 * Indica si el servicio está asignado a una cita diferente a la cargada
	 */
	private Boolean asignadoAOtraCita;
	
	private int ordenServicio;
	
	/**
	 * Indica si el servicio está asignado a la cita cargada
	 */
	private Boolean asignadoACita;
	
	/**
	 * Atributo con el codigo del servicio que se muestra al usuario
	 */
	private String indicativo ;
	
	/**
	 * Almacena el codigo del estado de la solicitud
	 * de la historia clinica.
	 */
	private int estadoSolHistoClinica;
	
	/**
	 * Almacena el codigo del estado de la solicitud
	 * de la factura.
	 */
	private int estadoSolFactura;
	
	public DtoServicioCitaOdontologica()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.citaOdontologica = ConstantesBD.codigoNuncaValido;
		this.tipoCita = "";
		this.servicio = ConstantesBD.codigoNuncaValido;
		this.duracion = ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.estadoHistoriaClinicaSolicitud = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.activo = "";
		this.usuarioModifica = "";
		this.valorTarifa = new BigDecimal("0.0");
		this.estadoCita = "";
		this.codigoAgenda = ConstantesBD.codigoNuncaValido;
		this.fechaCita = "";
		this.horaInicio = "";
		this.horaFinal = "";
		this.unidadAgenda = ConstantesBD.codigoNuncaValido;
		this.aplicaAbono = "";
		this.aplicaAnticipo = "";
		this.codigoPresuOdoProgSer = ConstantesBD.codigoNuncaValido;
		this.garantiaServicio = ConstantesBD.acronimoNo;  // Organizar otras consultas en donde se tomara por defecto este valor en S
		this.facturado = ConstantesBD.acronimoNo;
		
		this.nombreServicio = "";
		this.codigoPropietarioServicio = "";
		this.codigoTipoServicio = "";
		this.nombreTipoServicio = "";
		
		// atributos usados cambio servicios
		this.nuevoServicio = ConstantesBD.acronimoNo;
		this.modificadoSer = ConstantesBD.acronimoNo;
		this.eliminarSer = ConstantesBD.acronimoNo;
		
		this.infoTarifa = new InfoTarifaServicioPresupuesto();
		this.infoResponsableCobertura = new InfoResponsableCobertura();
		this.valorMaximoXPaciente = 0;
		this.codigoCups = "";
		this.evolucionado = false;
		
		this.serviciosArticulosIncluidos = new ArrayList<DtoServArtIncCitaOdo>();
		this.condicionesServicio = new ArrayList<InfoDatosInt>();
		
		this.codigoPrograma=0;
		this.superficies=new ArrayList<DtoSuperficiesPorPrograma>() ;
		this.codigoProgramaHallazgoPieza=ConstantesBD.codigoNuncaValido;
		this.programaHallazgoPieza=new DtoProgHallazgoPieza();
		this.ordenServicio=ConstantesBD.codigoNuncaValido;
	
		this.setIndicativo("");
		this.programaHallazgoPiezaLong = ConstantesBD.codigoNuncaValidoLong;
		this.estadoSolHistoClinica = ConstantesBD.codigoNuncaValido;
		this.estadoSolFactura = ConstantesBD.codigoNuncaValido;
		
		this.estadoServicioPlanTratamiento = "";
	}


	public int getOrdenServicio() {
		return ordenServicio;
	}

	public void setOrdenServicio(int ordenServicio) {
		this.ordenServicio = ordenServicio;
	}

	public DtoProgHallazgoPieza getProgramaHallazgoPieza() {
		return programaHallazgoPieza;
	}

	public void setProgramaHallazgoPieza(DtoProgHallazgoPieza programaHallazgoPieza) {
		this.programaHallazgoPieza = programaHallazgoPieza;
	}

	public int getCodigoProgramaHallazgoPieza() {
		return codigoProgramaHallazgoPieza;
	}

	public void setCodigoProgramaHallazgoPieza(int codigoProgramaHallazgoPieza) {
		this.codigoProgramaHallazgoPieza = codigoProgramaHallazgoPieza;
	}

	public int getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(int codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	public String getTipoCita() {
		return tipoCita;
	}

	public void setTipoCita(String tipoCita) {
		this.tipoCita = tipoCita;
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the citaOdontologica
	 */
	public int getCitaOdontologica() {
		return citaOdontologica;
	}

	/**
	 * @param citaOdontologica the citaOdontologica to set
	 */
	public void setCitaOdontologica(int citaOdontologica) {
		this.citaOdontologica = citaOdontologica;
	}

	/**
	 * @return the servicio
	 */
	public int getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the duracion
	 */
	public int getDuracion() {
		return duracion;
	}

	/**
	 * @param duracion the duracion to set
	 */
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the valorTarifa
	 */
	public BigDecimal getValorTarifa() {
		return valorTarifa;
	}

	/**
	 * @param valorTarifa the valorTarifa to set
	 */
	public void setValorTarifa(BigDecimal valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	/**
	 * @return the estadoCita
	 */
	public String getEstadoCita() {
		return estadoCita;
	}

	/**
	 * @param estadoCita the estadoCita to set
	 */
	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	/**
	 * @return the codigoAgenda
	 */
	public int getCodigoAgenda() {
		return codigoAgenda;
	}

	/**
	 * @param codigoAgenda the codigoAgenda to set
	 */
	public void setCodigoAgenda(int codigoAgenda) {
		this.codigoAgenda = codigoAgenda;
	}

	/**
	 * @return the fechaCita
	 */
	public String getFechaCita() {
		return fechaCita;
	}

	/**
	 * @param fechaCita the fechaCita to set
	 */
	public void setFechaCita(String fechaCita) {
		this.fechaCita = fechaCita;
	}

	/**
	 * @return the horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio the horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return the horaFinal
	 */
	public String getHoraFinal() {
		return horaFinal;
	}

	/**
	 * @param horaFinal the horaFinal to set
	 */
	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	/**
	 * @return the unidadAgenda
	 */
	public int getUnidadAgenda() {
		return unidadAgenda;
	}

	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(int unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the aplicaAbono
	 */
	public String getAplicaAbono() {
		return aplicaAbono;
	}

	/**
	 * @param aplicaAbono the aplicaAbono to set
	 */
	public void setAplicaAbono(String aplicaAbono) {
		this.aplicaAbono = aplicaAbono;
	}

	/**
	 * @return the aplicaAnticipo
	 */
	public String getAplicaAnticipo() {
		return aplicaAnticipo;
	}

	/**
	 * @param aplicaAnticipo the aplicaAnticipo to set
	 */
	public void setAplicaAnticipo(String aplicaAnticipo) {
		this.aplicaAnticipo = aplicaAnticipo;
	}

	/**
	 * @return the nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * @param nombreServicio the nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * @return the codigoPropietarioServicio
	 */
	public String getCodigoPropietarioServicio() {
		return codigoPropietarioServicio;
	}

	/**
	 * @param codigoPropietarioServicio the codigoPropietarioServicio to set
	 */
	public void setCodigoPropietarioServicio(String codigoPropietarioServicio) {
		this.codigoPropietarioServicio = codigoPropietarioServicio;
	}

	/**
	 * @return the codigoTipoServicio
	 */
	public String getCodigoTipoServicio() {
		return codigoTipoServicio;
	}

	/**
	 * @param codigoTipoServicio the codigoTipoServicio to set
	 */
	public void setCodigoTipoServicio(String codigoTipoServicio) {
		this.codigoTipoServicio = codigoTipoServicio;
	}

	/**
	 * @return the codigoPresuOdoProgSer
	 */
	public int getCodigoPresuOdoProgSer() {
		return codigoPresuOdoProgSer;
	}

	/**
	 * @param codigoPresuOdoProgSer the codigoPresuOdoProgSer to set
	 */
	public void setCodigoPresuOdoProgSer(int codigoPresuOdoProgSer) {
		this.codigoPresuOdoProgSer = codigoPresuOdoProgSer;
	}

	/**
	 * @return the garantiaServicio
	 */
	public String getGarantiaServicio() {
		return garantiaServicio;
	}

	/**
	 * @param garantiaServicio the garantiaServicio to set
	 */
	public void setGarantiaServicio(String garantiaServicio) {
		this.garantiaServicio = garantiaServicio;
	}

	/**
	 * @return the infoTarifa
	 */
	public InfoTarifaServicioPresupuesto getInfoTarifa() {
		return infoTarifa;
	}

	/**
	 * @param infoTarifa the infoTarifa to set
	 */
	public void setInfoTarifa(InfoTarifaServicioPresupuesto infoTarifa) {
		this.infoTarifa = infoTarifa;
	}

	/**
	 * @return the infoResponsableCobertura
	 */
	public InfoResponsableCobertura getInfoResponsableCobertura() {
		return infoResponsableCobertura;
	}

	/**
	 * @param infoResponsableCobertura the infoResponsableCobertura to set
	 */
	public void setInfoResponsableCobertura(
			InfoResponsableCobertura infoResponsableCobertura) {
		this.infoResponsableCobertura = infoResponsableCobertura;
	}

	/**
	 * @return the valorMaximoXPaciente
	 */
	public double getValorMaximoXPaciente() {
		return valorMaximoXPaciente;
	}

	/**
	 * @param valorMaximoXPaciente the valorMaximoXPaciente to set
	 */
	public void setValorMaximoXPaciente(double valorMaximoXPaciente) {
		this.valorMaximoXPaciente = valorMaximoXPaciente;
	}

	/**
	 * @return the facturado
	 */
	public String getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}

	/**
	 * @return the nuevoServicio
	 */
	public String getNuevoServicio() {
		return nuevoServicio;
	}

	/**
	 * @param nuevoServicio the nuevoServicio to set
	 */
	public void setNuevoServicio(String nuevoServicio) {
		this.nuevoServicio = nuevoServicio;
	}

	/**
	 * @return the modificadoSer
	 */
	public String getModificadoSer() {
		return modificadoSer;
	}

	/**
	 * @param modificadoSer the modificadoSer to set
	 */
	public void setModificadoSer(String modificadoSer) {
		this.modificadoSer = modificadoSer;
	}

	/**
	 * @return the eliminarSer
	 */
	public String getEliminarSer() {
		return eliminarSer;
	}

	/**
	 * @param eliminarSer the eliminarSer to set
	 */
	public void setEliminarSer(String eliminarSer) {
		this.eliminarSer = eliminarSer;
	}

	/**
	 * @return the estadoHistoriaClinicaSolicitud
	 */
	public InfoDatosInt getEstadoHistoriaClinicaSolicitud() {
		return estadoHistoriaClinicaSolicitud;
	}

	/**
	 * @param estadoHistoriaClinicaSolicitud the estadoHistoriaClinicaSolicitud to set
	 */
	public void setEstadoHistoriaClinicaSolicitud(
			InfoDatosInt estadoHistoriaClinicaSolicitud) {
		this.estadoHistoriaClinicaSolicitud = estadoHistoriaClinicaSolicitud;
	}

	/**
	 * @return the nombreTipoServicio
	 */
	public String getNombreTipoServicio() {
		return nombreTipoServicio;
	}

	/**
	 * @param nombreTipoServicio the nombreTipoServicio to set
	 */
	public void setNombreTipoServicio(String nombreTipoServicio) {
		this.nombreTipoServicio = nombreTipoServicio;
	}

	/**
	 * @return the codigoCups
	 */
	public String getCodigoCups() {
		return codigoCups;
	}

	/**
	 * @param codigoCups the codigoCups to set
	 */
	public void setCodigoCups(String codigoCups) {
		this.codigoCups = codigoCups;
	}
	
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	           
	        }
	        return obj;
	    }

	/**
	 * @return the evolucionado
	 */
	public boolean isEvolucionado() {
		return evolucionado;
	}

	/**
	 * @param evolucionado the evolucionado to set
	 */
	public void setEvolucionado(boolean evolucionado) {
		this.evolucionado = evolucionado;
	}

	/**
	 * @return the serviciosArticulosIncluidos
	 */
	public ArrayList<DtoServArtIncCitaOdo> getServiciosArticulosIncluidos() {
		return serviciosArticulosIncluidos;
	}

	/**
	 * @param serviciosArticulosIncluidos the serviciosArticulosIncluidos to set
	 */
	public void setServiciosArticulosIncluidos(
			ArrayList<DtoServArtIncCitaOdo> serviciosArticulosIncluidos) {
		this.serviciosArticulosIncluidos = serviciosArticulosIncluidos;
	}
	
	/**
	 * Método para obtneer el numero de servicios/articulos incluidos
	 * @return
	 */
	public int getNumServiciosArticulosIncluidos()
	{
		return this.serviciosArticulosIncluidos.size();
	}

	/**
	 * @return the condicionesServicio
	 */
	public ArrayList<InfoDatosInt> getCondicionesServicio() {
		return condicionesServicio;
	}

	/**
	 * @param condicionesServicio the condicionesServicio to set
	 */
	public void setCondicionesServicio(ArrayList<InfoDatosInt> condicionesServicio) {
		this.condicionesServicio = condicionesServicio;
	}

	/**
	 * @return the asignadoAOtraCita
	 */
	public Boolean getAsignadoAOtraCita()
	{
		return asignadoAOtraCita;
	}

	/**
	 * @param asignadoAOtraCita the asignadoAOtraCita to set
	 */
	public void setAsignadoAOtraCita(Boolean asignadoAOtraCita)
	{
		this.asignadoAOtraCita = asignadoAOtraCita;
	}

	/**
	 * @return the asignadoACita
	 */
	public Boolean getAsignadoACita()
	{
		return asignadoACita;
	}

	/**
	 * @param asignadoACita the asignadoACita to set
	 */
	public void setAsignadoACita(Boolean asignadoACita)
	{
		this.asignadoACita = asignadoACita;
	}

	/**
	 * @return the piezaDental
	 */
	public int getPiezaDental()
	{
		return piezaDental;
	}

	/**
	 * @param piezaDental the piezaDental to set
	 */
	public void setPiezaDental(int piezaDental)
	{
		this.piezaDental = piezaDental;
	}

	/**
	 * @return the seccionHallazgo
	 */
	public String getSeccionHallazgo()
	{
		return seccionHallazgo;
	}

	/**
	 * @param seccionHallazgo the seccionHallazgo to set
	 */
	public void setSeccionHallazgo(String seccionHallazgo)
	{
		this.seccionHallazgo = seccionHallazgo;
	}
	
	public ArrayList<DtoSuperficiesPorPrograma> getSuperficies() {
		return superficies;
	}

	public void setSuperficies(ArrayList<DtoSuperficiesPorPrograma> superficies) {
		this.superficies = superficies;
	}

	public int getPlanTratamiento() {
		return planTratamiento;
	}

	public void setPlanTratamiento(int planTratamiento) {
		this.planTratamiento = planTratamiento;
	}

	/**
	 * @param indicativo the indicativo to set
	 */
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	/**
	 * @return the indicativo
	 */
	public String getIndicativo() {
		return indicativo;
	}

	public Long getProgramaHallazgoPiezaLong() {
		return programaHallazgoPiezaLong;
	}

	public void setProgramaHallazgoPiezaLong(Long programaHallazgoPiezaLong) {
		
		this.programaHallazgoPiezaLong = programaHallazgoPiezaLong;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estadoSolHistoClinica
	
	 * @return retorna la variable estadoSolHistoClinica 
	 * @author Yennifer Guerrero 
	 */
	public int getEstadoSolHistoClinica() {
		return estadoSolHistoClinica;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estadoSolHistoClinica
	
	 * @param valor para el atributo estadoSolHistoClinica 
	 * @author Yennifer Guerrero
	 */
	public void setEstadoSolHistoClinica(int estadoSolHistoClinica) {
		this.estadoSolHistoClinica = estadoSolHistoClinica;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estadoSolFactura
	
	 * @return retorna la variable estadoSolFactura 
	 * @author Yennifer Guerrero 
	 */
	public int getEstadoSolFactura() {
		return estadoSolFactura;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estadoSolFactura
	
	 * @param valor para el atributo estadoSolFactura 
	 * @author Yennifer Guerrero
	 */
	public void setEstadoSolFactura(int estadoSolFactura) {
		this.estadoSolFactura = estadoSolFactura;
	}

	/**
	 * Obtiene el valor del atributo estadoPrograma
	 *
	 * @return Retorna atributo estadoPrograma
	 */
	public String getEstadoPrograma()
	{
		return estadoPrograma;
	}

	/**
	 * Establece el valor del atributo estadoPrograma
	 *
	 * @param valor para el atributo estadoPrograma
	 */
	public void setEstadoPrograma(String estadoPrograma)
	{
		this.estadoPrograma = estadoPrograma;
	}

	/**
	 * Obtiene el atributo estadoServicioPlanTratamiento
	 * @return estadoServicioPlanTratamiento
	 */
	public String getEstadoServicioPlanTratamiento() {
		return estadoServicioPlanTratamiento;
	}

	/**
	 * Asigna el atributo estadoServicioPlanTratamiento
	 * @param estadoServicioPlanTratamiento Atributo estadoServicioPlanTratamiento
	 */
	public void setEstadoServicioPlanTratamiento(
			String estadoServicioPlanTratamiento) {
		this.estadoServicioPlanTratamiento = estadoServicioPlanTratamiento;
	}

	
}
