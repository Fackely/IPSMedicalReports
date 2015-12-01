package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.InfoDatosString;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.General.InfoDatosBD;

/**
 * DTO PARA CARGAR LOS PROGRAMAS SERVICIOS DEL PLAN DE TRATAMIENTO
 * @author axioma
 *
 */
	
public class InfoProgramaServicioPlan implements Serializable, Cloneable
{
	/**
	 * 
	 * Versi&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigopk;
	private BigDecimal codigoPkProgramaServicio;
	private String nombreProgramaServicio;
	private String codigoAmostrar;
	private boolean activo;
	private boolean asignadoAlPresupuesto;
	private boolean esSegundoOMas;
	private String codigoEquivalente;
	private String estadoAutorizacion;
	private InfoDatosStr motivoCancelacion;
	private BigDecimal codigoPKProgramasServiciosPlanTratamiento;
	private int numeroSuperficies;
	
	private String fechaUltimaModificacion;
	private String horaUltimaModificacion;
	
	// atributos para el componente antecedentes odontologicos
	private int codigoEspecialidad;
	private String especialidad;
	private String fechaInicio;
	private String fechaFinal;
	private String pathConvencionPrograma;
	private String archivoConvencionPrograma;
	
	private String contratoMigrado;
	private String estadoContratoMigrado;
	
	
	/**
	 * NOMBRE AYUDANTE PARA  COLOCAR LA EXCLUSION , INCLUSION  Y GARANTIA 
	 */
	private String nombreAyuExIncGa;
	
	
	private boolean tieneArticulos;
	
	private String ayudanteEstadoAutorizacion;
	
	
	
	/**
	 * Ayudante para mostrar en presentacion el estado del programa
	 */
	private String ayudanteNombreEstadoPrograma;
	
	
	/**
	 * value = existe en base de datos (S/N)
	 * activo = fue eliminado por el usuario y debe ser eliminado de la BD (si esta en false)
	 * */
	private InfoDatosBD existeBD;
	
	/**
	 * -------------------------------------------
	 * ATRIBUTOS PARA EL PLAN TRATAMIENTO 
	 */
	
	private String estadoPrograma;
	private String newEstadoProg;
	private String estadoServicio;
	private String inclusion;
	private String exclusion;
	private String garantia;
	private String porConfirmar;
	private String inactivarProg;
	
	//****************************************************************************************
	// Atributos de posicionamiento del servicio para la seccion de programas/servicio cita
	// de la evolución.
	private int posDet;
	private int posSupf;
	private int posProg;
	private String seccionAplica;
	private String programasParCita;
	//****************************************************************************************
	
	/**
	 * ------------------------------------------
	 * LISTA DE SERVICIOS PARA EL PROGRAMA 
	 */
	private ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>();
	// array de posible estados del programa a los que puede evolucionar el progarama
	private ArrayList<InfoDatosString> arrayPosEstProg;
	
	/**
	 * Indica si el programa está parametrizado por defecto
	 */
	private boolean porDefecto;
	
	/**
	 * 
	 */
	private String hallazgoAplicaA;
	
	/**
	 * 
	 */
	private ArrayList<InfoNumSuperficiesPresupuesto> superficiesAplicaPresupuesto;
	
	/**
	 * Programa al cual está relacionada la superficie
	 */
	private DtoProgHallazgoPieza progHallazgoPieza=new DtoProgHallazgoPieza();
	
	/**
	 * Color de la letra al seleccionar el programa
	 */
	private String colorLetra;
	
	/**
	 * Atributo de control para verificar si ya fue evaluado al momento de relacionar en BD
	 * las superficies para las que aplica
	 */
	private boolean evaluado;
	
	/**
	 * Hallazgo al cual se encuentra asociado el programa
	 */
	private InfoHallazgoSuperficie hallazgoAsociado;
	
	/**
	 * 
	 */
	private boolean existenServicios;
	
	/**
	 * Indica si el hallazgo se permite o no tratar varias veces
	 */
	private boolean permiteTratarVariasVeces;
	
	
	private boolean yaSeleccionado;
	
	
	/**
	 * 
	 */
	public InfoProgramaServicioPlan() {
		super();
		this.codigoPkProgramaServicio = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.nombreProgramaServicio = "";
		this.codigopk= new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.activo=false;
		this.codigoAmostrar="";
		this.asignadoAlPresupuesto=false;
		this.codigoEquivalente="";
		this.estadoServicio="";
		// atributos para el componente antecedentes odontologicos
		this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		this.especialidad = "";
		this.fechaInicio = "";
		this.fechaFinal = "";
		this.porConfirmar="";
		this.inactivarProg = ConstantesBD.acronimoNo;
		this.listaServicios = new ArrayList<InfoServicios>();
		this.existeBD = new InfoDatosBD();
		this.setEstadoAutorizacion("");
		this.estadoPrograma="";
		this.newEstadoProg = "";
		this.inclusion="";
		this.exclusion="";
		this.garantia="";
		this.motivoCancelacion = new InfoDatosStr();
		this.codigoPKProgramasServiciosPlanTratamiento= new  BigDecimal(ConstantesBD.codigoNuncaValido);
		this.numeroSuperficies = ConstantesBD.codigoNuncaValido;
		this.arrayPosEstProg = new ArrayList<InfoDatosString>();
		this.pathConvencionPrograma = "";
		this.archivoConvencionPrograma = "";
		this.fechaUltimaModificacion = "";
		this.horaUltimaModificacion = "";
		this.tieneArticulos= false;
		this.esSegundoOMas=false;
		
		this.posDet = ConstantesBD.codigoNuncaValido;
		this.posSupf = ConstantesBD.codigoNuncaValido;
		this.posProg = ConstantesBD.codigoNuncaValido;
		this.seccionAplica = "";
		this.programasParCita = "";
		this.hallazgoAplicaA="";
		this.superficiesAplicaPresupuesto= new ArrayList<InfoNumSuperficiesPresupuesto>();
		this.progHallazgoPieza=new DtoProgHallazgoPieza();
		this.colorLetra="";
		this.evaluado=false;
		this.ayudanteEstadoAutorizacion="";
		this.nombreAyuExIncGa="";
		this.ayudanteNombreEstadoPrograma="";
		this.existenServicios=Boolean.FALSE;
		
		yaSeleccionado = false;
	}
/**
 * 
 * @return
 */

	public BigDecimal getCodigoPkProgramaServicio() {
		return codigoPkProgramaServicio;
	}

	/**
	 * 
	 * @param codigoPkProgramaServicio
	 */
	public void setCodigoPkProgramaServicio(BigDecimal codigoPkProgramaServicio) {
		this.codigoPkProgramaServicio = codigoPkProgramaServicio;
	}

	/**
	 * 
	 * @return
	 */
	public String getNombreProgramaServicio() {
		return nombreProgramaServicio;
	}

	/**
	 * 
	 * @param nombreProgramaServicio
	 */
	public void setNombreProgramaServicio(String nombreProgramaServicio) {
		this.nombreProgramaServicio = nombreProgramaServicio;
	}


	public BigDecimal getCodigopk() {
		return codigopk;
	}
	public void setCodigopk(BigDecimal codigopk) {
		this.codigopk = codigopk;
	}
	
	
	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	/**
	 * @return the activo
	 */
	public boolean getActivo() {
		return this.activo;
	}
	/**
	 * @return the codigoAmostrar
	 */
	public String getCodigoAmostrar() {
		return codigoAmostrar;
	}
	/**
	 * @param codigoAmostrar the codigoAmostrar to set
	 */
	public void setCodigoAmostrar(String codigoAmostrar) {
		this.codigoAmostrar = codigoAmostrar;
	}
	/**
	 * @return the asignadoAlPresupuesto
	 */
	public boolean isAsignadoAlPresupuesto()
	{
		return asignadoAlPresupuesto;
	}
	/**
	 * @param asignadoAlPresupuesto the asignadoAlPresupuesto to set
	 */
	public void setAsignadoAlPresupuesto(boolean asignadoAlPresupuesto)
	{
		this.asignadoAlPresupuesto = asignadoAlPresupuesto;
	}
	public void setCodigoEquivalente(String codigoEquivalente) {
		this.codigoEquivalente = codigoEquivalente;
	}
	public String getCodigoEquivalente() {
		return codigoEquivalente;
	}
	/**
	 * @return the especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}
	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
	/**
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public void setEstadoPrograma(String estadoPrograma) {
		this.estadoPrograma = estadoPrograma;
	}
	public String getEstadoPrograma() {
		return estadoPrograma;
	}
	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}
	public String getInclusion() {
		return inclusion;
	}
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	public String getExclusion() {
		return exclusion;
	}
	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}
	public String getGarantia() {
		return garantia;
	}
	public void setListaServicios(ArrayList<InfoServicios> listaServicios) {
		this.listaServicios = listaServicios;
	}
	public ArrayList<InfoServicios> getListaServicios() {
		return listaServicios;
	}
	public InfoDatosBD getExisteBD() {
		return existeBD;
	}
	public void setExisteBD(InfoDatosBD existeBD) {
		this.existeBD = existeBD;
	}
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}
	public String getPorConfirmar() {
		return porConfirmar;
	}
	public void setEstadoServicio(String estadoServicio) {
		this.estadoServicio = estadoServicio;
	}
	public String getEstadoServicio() {
		return estadoServicio;
	}
	/**
	 * @return the codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}
	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}
	public void setMotivoCancelacion(InfoDatosStr motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}
	public InfoDatosStr getMotivoCancelacion() {
		return motivoCancelacion;
	}
	public void setCodigoPKProgramasServiciosPlanTratamiento(
			BigDecimal codigoPKProgramasServiciosPlanTratamiento) {
		this.codigoPKProgramasServiciosPlanTratamiento = codigoPKProgramasServiciosPlanTratamiento;
	}
	public BigDecimal getCodigoPKProgramasServiciosPlanTratamiento() {
		return codigoPKProgramasServiciosPlanTratamiento;
	}
	public int getNumeroSuperficies() {
		return numeroSuperficies;
	}
	public void setNumeroSuperficies(int numeroSuperficies) {
		this.numeroSuperficies = numeroSuperficies;
	}
	
	/**
	 * Método que verifica si el programa contiene una misma evolucion
	 * @param codigoEvolucion
	 * @return
	 */
	public boolean tieneMismaEvolucionPrograma(BigDecimal codigoEvolucion)
	{
		boolean mismaEvolucion = false;
		
		for(InfoServicios servicio:this.listaServicios)
		{
			if(servicio.getCodigoEvolucion().intValue()==codigoEvolucion.intValue())
			{
				mismaEvolucion = true;
			}
		}
		
		return mismaEvolucion;
	}
	/**
	 * @return the arrayPosEstProg
	 */
	public ArrayList<InfoDatosString> getArrayPosEstProg() {
		return arrayPosEstProg;
	}
	/**
	 * @param arrayPosEstProg the arrayPosEstProg to set
	 */
	public void setArrayPosEstProg(ArrayList<InfoDatosString> arrayPosEstProg) {
		this.arrayPosEstProg = arrayPosEstProg;
	}
	/**
	 * @return the newEstadoProg
	 */
	public String getNewEstadoProg() {
		return newEstadoProg;
	}
	/**
	 * @param newEstadoProg the newEstadoProg to set
	 */
	public void setNewEstadoProg(String newEstadoProg) {
		this.newEstadoProg = newEstadoProg;
	}
	
	/**
	 * @return the detalleSuperficie
	 */
	public int getNumeroServiciosActivosParaCita(boolean validarExcluInclu)
	{
		int cont = 0;
		
		for(int i = 0; i < this.listaServicios.size(); i++)
		{
			if(this.listaServicios.get(i).getExisteBD().isActivo() /*&& 
					((!validarExcluInclu && !this.listaServicios.get(i).getInclusion().equals(ConstantesBD.acronimoSi)) 
							|| (validarExcluInclu && this.listaServicios.get(i).getInclusion().equals(ConstantesBD.acronimoSi)))*/
				&& !this.listaServicios.get(i).getServicioParCita().equals(ConstantesBD.acronimoNo) )
				cont++;
		}
		
		return cont;
	}
	
	/**
	 * @return the detalleSuperficie
	 */
	public int getNumeroServiciosActivos(boolean validarExcluInclu) 
	{
		int cont = 0;
		
		for(int i = 0; i < this.listaServicios.size(); i++)
		{
			if(this.listaServicios.get(i).getExisteBD().isActivo() /*&& 
					((!validarExcluInclu && !this.listaServicios.get(i).getInclusion().equals(ConstantesBD.acronimoSi)) 
							|| (validarExcluInclu && this.listaServicios.get(i).getInclusion().equals(ConstantesBD.acronimoSi)))*/)
				cont++;
		}
		
		return cont;
	}
	
	/**
	 * 
	 * */
	public boolean misServiciosSonTodosExtInt()
	{
		for(int i=0; i<this.listaServicios.size(); i++)
		{
			if(this.listaServicios.get(i).getExisteBD().isActivo())
			{
				if(!(this.listaServicios.get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoExterno) || 
						this.listaServicios.get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)))
					return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * */
	public boolean misServiciosSonTodosConfirmados(boolean caso)
	{
		for(int i=0; i<this.listaServicios.size(); i++)
		{
			if(this.listaServicios.get(i).getExisteBD().isActivo())
			{
				if(caso){
					if(!this.listaServicios.get(i).getPorConfirmar().equals(ConstantesBD.acronimoNo))
						return false;
				}else{
					if(!this.listaServicios.get(i).getGarantia().equals(ConstantesBD.acronimoNo))
						return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @return the inactivarProg
	 */
	public String getInactivarProg() {
		return inactivarProg;
	}
	/**
	 * @param inactivarProg the inactivarProg to set
	 */
	public void setInactivarProg(String inactivarProg) {
		this.inactivarProg = inactivarProg;
	}
	public String getPathConvencionPrograma() {
		return pathConvencionPrograma;
	}
	public void setPathConvencionPrograma(String pathConvencionPrograma) {
		this.pathConvencionPrograma = pathConvencionPrograma;
	}
	public String getArchivoConvencionPrograma() {
		return archivoConvencionPrograma;
	}
	public void setArchivoConvencionPrograma(String archivoConvencionPrograma) {
		this.archivoConvencionPrograma = archivoConvencionPrograma;
	}
	public String getFechaUltimaModificacion() {
		return fechaUltimaModificacion;
	}
	public void setFechaUltimaModificacion(String fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}
	public String getHoraUltimaModificacion() {
		return horaUltimaModificacion;
	}
	public void setHoraUltimaModificacion(String horaUltimaModificacion) {
		this.horaUltimaModificacion = horaUltimaModificacion;
	}

	public String getNombreEstadoPrograma()
	{
		return ValoresPorDefecto.getIntegridadDominio(this.getEstadoPrograma())+"";
	}
	public String getNombreEstadoServicio()
	{
		return ValoresPorDefecto.getIntegridadDominio(this.getEstadoServicio())+"";
	}
	
	public String obtenerErrorAdicion(boolean utilizaProgramas)
	{
		String error="";
		
		if(utilizaProgramas)
		{
			if(this.isAsignadoAlPresupuesto())
			{
				error="PROGRAMA PRESUPUESTADO";
			}
			else
			{	
				if(this.getEstadoPrograma().equals("") || this.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
				{	
					error="";
				}
				else
				{
					error="PROGRAMA EN ESTADO "+this.getNombreEstadoPrograma();
				}
			}
			
			
			if(this.hallazgoAplicaA.equals(ConstantesIntegridadDominio.acronimoAplicaADiente) && this.getNumeroSuperficies()>0)
			{
				error+= error.isEmpty()?"":"<br>";
				error+="EL HALLAZGO APLICA A DIENTE, PERO EL PROGRAMA APLICA A SUPERFICIE, NO SELECCIONABLE";
			}
			else if(this.hallazgoAplicaA.equals(ConstantesIntegridadDominio.acronimoAplicaASuperficie) && this.getNumeroSuperficies()<=0)
			{
				error+= error.isEmpty()?"":"<br>";
				error+="EL HALLAZGO APLICA A SUPERFICIE, PERO EL PROGRAMA APLICA A DIENTE, NO SELECCIONABLE";
			}
			
			if(this.hallazgoAplicaA.equals(ConstantesIntegridadDominio.acronimoAplicaASuperficie) && this.getSuperficiesAplicaPresupuesto().size()<this.getNumeroSuperficies())
			{
				error+= error.isEmpty()?"":"<br>";
				error+="ESTE PROGRAMA APLICA A "+this.getNumeroSuperficies()+" SUPERFICIES, NO SELECCIONABLE";
			}
		}
		else
		{
			if(this.isAsignadoAlPresupuesto())
			{
				error="SERVICIO PRESUPUESTADO";
			}
			else
			{	
				if(this.getEstadoServicio().equals("") || this.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
				{	
					error="";
				}
				else
				{
					error="SERVICIO EN ESTADO "+this.getNombreEstadoPrograma();
				}
			}	
		}
		return error;
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
	 * @return the programasParCita
	 */
	public String getProgramasParCita() {
		return programasParCita;
	}
	/**
	 * @param programasParCita the programasParCita to set
	 */
	public void setProgramasParCita(String programasParCita) {
		this.programasParCita = programasParCita;
	}
	/**
	 * @return the esSegundoOMas
	 */
	public boolean isEsSegundoOMas() {
		return esSegundoOMas;
	}
	/**
	 * @param esSegundoOMas the esSegundoOMas to set
	 */
	public void setEsSegundoOMas(boolean esSegundoOMas) {
		this.esSegundoOMas = esSegundoOMas;
	}
	public boolean getPorDefecto()
	{
		return porDefecto;
	}
	public void setPorDefecto(boolean porDefecto)
	{
		this.porDefecto = porDefecto;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	/**
	 * 
	 * Metodo para determinar si puedo terminar un programa
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean getPuedoTerminarPrograma()
	{
		if(this.listaServicios!=null && this.getListaServicios().size()>0)
		{
			if(this.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoContratado) 
					|| this.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso)
					|| this.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoTerminado))
			{	
				for(InfoServicios info: this.getListaServicios())
				{
					if(!info.getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno) 
							&& !info.getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoExterno))
					{
						return false;
					}
				}
				return true;
			}	
		}
		return false;
	}
	/**
	 * @return the hallazgoAplicaA
	 */
	public String getHallazgoAplicaA() {
		return hallazgoAplicaA;
	}
	/**
	 * @param hallazgoAplicaA the hallazgoAplicaA to set
	 */
	public void setHallazgoAplicaA(String hallazgoAplicaA) {
		this.hallazgoAplicaA = hallazgoAplicaA;
	}
	/**
	 * @return the superficiesAplica
	 */
	public ArrayList<InfoNumSuperficiesPresupuesto> getSuperficiesAplicaPresupuesto() {
		return superficiesAplicaPresupuesto;
	}
	/**
	 * @param superficiesAplica the superficiesAplica to set
	 */
	public void setSuperficiesAplicaPresupuesto(ArrayList<InfoNumSuperficiesPresupuesto> superficiesAplica) {
		this.superficiesAplicaPresupuesto = superficiesAplica;
	}
	/**
	 * @return Retorna el atributo colorLetra
	 */
	public String getColorLetra()
	{
		return colorLetra;
	}
	/**
	 * @param colorLetra Asigna el atributo colorLetra
	 */
	public void setColorLetra(String colorLetra)
	{
		this.colorLetra = colorLetra;
	}
	
	/**
	 * @return Retorna el atributo evaluado
	 */
	public boolean getEvaluado()
	{
		return evaluado;
	}
	/**
	 * @param evaluado Asigna el atributo evaluado
	 */
	public void setEvaluado(boolean evaluado)
	{
		this.evaluado = evaluado;
	}
	
	
	public InfoProgramaServicioPlan copiar() 
	{
		InfoProgramaServicioPlan info= new InfoProgramaServicioPlan();
		info.setActivo(this.getActivo());
		info.setArchivoConvencionPrograma(this.getArchivoConvencionPrograma());
		info.setArrayPosEstProg(this.getArrayPosEstProg());
		info.setAsignadoAlPresupuesto(this.isAsignadoAlPresupuesto());
		info.setCodigoAmostrar(this.getCodigoAmostrar());
		info.setCodigoEquivalente(this.getCodigoEquivalente());
		info.setCodigoEspecialidad(this.getCodigoEspecialidad());
		info.setCodigopk(this.codigopk);
		info.setCodigoPkProgramaServicio(this.getCodigoPkProgramaServicio());
		info.setCodigoPKProgramasServiciosPlanTratamiento(this.getCodigoPKProgramasServiciosPlanTratamiento());
		info.setProgHallazgoPieza(this.getProgHallazgoPieza().clone());
		info.setColorLetra(this.getColorLetra());
		info.setEspecialidad(this.getEspecialidad());
		info.setEsSegundoOMas(this.isEsSegundoOMas());
		info.setEstadoAutorizacion(this.getEstadoAutorizacion());
		info.setEstadoPrograma(this.getEstadoPrograma());
		info.setEstadoServicio(this.getEstadoServicio());
		info.setExclusion(this.getExclusion());
		info.setExisteBD(this.getExisteBD());
		info.setFechaFinal(this.getFechaFinal());
		info.setFechaInicio(this.getFechaInicio());
		info.setFechaUltimaModificacion(this.getFechaUltimaModificacion());
		info.setGarantia(this.getGarantia());
		info.setHallazgoAplicaA(this.getHallazgoAplicaA());
		info.setHoraUltimaModificacion(this.getHoraUltimaModificacion());
		info.setInactivarProg(this.getInactivarProg());
		info.setInclusion(this.getInclusion());
		info.setListaServicios(this.getListaServicios());
		info.setMotivoCancelacion(this.getMotivoCancelacion());
		info.setNewEstadoProg(this.getNewEstadoProg());
		info.setNombreProgramaServicio(this.getNombreProgramaServicio());
		info.setNumeroSuperficies(this.getNumeroSuperficies());
		info.setPathConvencionPrograma(this.getPathConvencionPrograma());
		info.setPorConfirmar(this.getPorConfirmar());
		info.setPorDefecto(this.getPorDefecto());
		info.setPosDet(this.getPosDet());
		info.setPosProg(this.getPosProg());
		info.setPosSupf(this.getPosSupf());
		info.setProgramasParCita(this.getProgramasParCita());
		info.setSeccionAplica(this.getSeccionAplica());
		info.setSuperficiesAplicaPresupuesto(this.getSuperficiesAplicaPresupuesto());
		info.setTieneArticulos(this.getTieneArticulos());
		return info;
	}
	/**
	 * @return Retorna el atributo progHallazgoPieza
	 */
	public DtoProgHallazgoPieza getProgHallazgoPieza()
	{
		return progHallazgoPieza;
	}
	/**
	 * @param progHallazgoPieza Asigna el atributo progHallazgoPieza
	 */
	public void setProgHallazgoPieza(DtoProgHallazgoPieza progHallazgoPieza)
	{
		this.progHallazgoPieza = progHallazgoPieza;
	}
	/**
	 * @return Retorna el atributo hallazgoAsociado
	 */
	public InfoHallazgoSuperficie getHallazgoAsociado()
	{
		return hallazgoAsociado;
	}
	/**
	 * @param hallazgoAsociado Asigna el atributo hallazgoAsociado
	 */
	public void setHallazgoAsociado(InfoHallazgoSuperficie hallazgoAsociado)
	{
		this.hallazgoAsociado = hallazgoAsociado;
	}
	public void setAyudanteEstadoAutorizacion(String ayudanteEstadoAutorizacion) {
		this.ayudanteEstadoAutorizacion = ayudanteEstadoAutorizacion;
	}
	
	
	/**
	 * Ayudante para Mostra el Estado del programa al usuario
	 * 1. Si el estado es pendiente y el estadoPorAutorizar es autorizado
	 * se coloca el estado autorizado
	 * 2. En otro caso carga es estado del programa 
	 * @return
	 */
	public String getAyudanteEstadoAutorizacion() 
	{
		String estadoRetorno;
			
		if( this.estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) )
		{
			if(this.estadoAutorizacion.equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				estadoRetorno=this.estadoAutorizacion;
			}
			else
			{
				estadoRetorno= this.estadoPrograma;
			}
		}
		else
		{
			estadoRetorno=this.estadoPrograma;
		}
		
		
		return estadoRetorno;
	}
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param nombreAyuExIncGa
	 */
	public void setNombreAyuExIncGa(String nombreAyuExIncGa) 
	{
		this.nombreAyuExIncGa = nombreAyuExIncGa;
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombreAyuExIncGa() {
		
		nombreAyuExIncGa="";
		
		if(this.inclusion.equals(ConstantesBD.acronimoSi) )
		{
			nombreAyuExIncGa=" I ";
		}
		if(this.exclusion.equals(ConstantesBD.acronimoSi))
		{
			nombreAyuExIncGa+=" E ";
		}
		if(this.garantia.equals(ConstantesBD.acronimoSi))
		{
			nombreAyuExIncGa+=" G ";
		}
		
		return nombreAyuExIncGa;
	}
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param ayudanteNombreEstadoPrograma
	 */
	public void setAyudanteNombreEstadoPrograma(
			String ayudanteNombreEstadoPrograma) 
	{
		this.ayudanteNombreEstadoPrograma = ayudanteNombreEstadoPrograma;
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getAyudanteNombreEstadoPrograma() {
		try{
			if(!UtilidadTexto.isEmpty(this.estadoPrograma))
			{
				this.ayudanteNombreEstadoPrograma = ValoresPorDefecto.getIntegridadDominio(this.getEstadoPrograma()).toString();
			}
		}
		catch(Exception e)
		{
			
		}
		
		return ayudanteNombreEstadoPrograma;
	}
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	
	private boolean presentaServicios()
	{
		boolean retorno=Boolean.FALSE;
		
		if(this.listaServicios.size()>0)
		{
			retorno= Boolean.TRUE;
		}
		
		return retorno; 
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param existenServicios
	 */
	public void setExistenServicios(boolean existenServicios) {
		this.existenServicios = existenServicios;
	}
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public boolean getExistenServicios() 
	{
		this.existenServicios=presentaServicios();
		return existenServicios;
	}
	/**
	 * Obtiene el valor del atributo permiteTratarVariasVeces
	 *
	 * @return Retorna atributo permiteTratarVariasVeces
	 */
	public boolean isPermiteTratarVariasVeces()
	{
		return permiteTratarVariasVeces;
	}
	/**
	 * Establece el valor del atributo permiteTratarVariasVeces
	 *
	 * @param valor para el atributo permiteTratarVariasVeces
	 */
	public void setPermiteTratarVariasVeces(boolean permiteTratarVariasVeces)
	{
		this.permiteTratarVariasVeces = permiteTratarVariasVeces;
	}
	/**
	 * @param yaSeleccionado the yaSeleccionado to set
	 */
	public void setYaSeleccionado(boolean yaSeleccionado) {
		this.yaSeleccionado = yaSeleccionado;
	}
	/**
	 * @return the yaSeleccionado
	 */
	public boolean isYaSeleccionado() {
		return yaSeleccionado;
	}
	/**
	 * Obtiene el valor del atributo contratoMigrado
	 *
	 * @return Retorna atributo contratoMigrado
	 */
	public String getContratoMigrado()
	{
		return contratoMigrado;
	}
	/**
	 * Establece el valor del atributo contratoMigrado
	 *
	 * @param valor para el atributo contratoMigrado
	 */
	public void setContratoMigrado(String contratoMigrado)
	{
		this.contratoMigrado = contratoMigrado;
	}
	/**
	 * Obtiene el valor del atributo estadoContratoMigrado
	 *
	 * @return Retorna atributo estadoContratoMigrado
	 */
	public String getEstadoContratoMigrado()
	{
		return estadoContratoMigrado;
	}
	/**
	 * Establece el valor del atributo estadoContratoMigrado
	 *
	 * @param valor para el atributo estadoContratoMigrado
	 */
	public void setEstadoContratoMigrado(String estadoContratoMigrado)
	{
		this.estadoContratoMigrado = estadoContratoMigrado;
	}
	
	
}
