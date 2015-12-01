package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoOdontograma;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.mundo.atencion.Diagnostico;

/**
 * @author axioma
 * Agosto 21 de 2009
 *
 */
public class DtoValoracionesOdonto implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(DtoValoracionesOdonto.class);
	
	private double codigoPk;
	
	private double cita;
	
	private String fechaConsulta;
	
	private String horaConsulta;
	
	private String edadPaciente;
	
	private String motivoConsulta;
	
	private String enfermedadActual;
	
	private int tipoDiagnostico;
	
	private String nombreTipoDiagnostico;
	
	private int causaExterna;
	
	private String nombreCausaExterna;
	
	private String finalidadConsulta;
	
	private String nombreFinalidadConsulta;
	
	private String observaciones;
	
	private String fechaModifica;
	
	private String horaModifica;
	
	private String usuarioModifica;
	
	private double hisAntecedenteOdo;
	
	private ArrayList<DtoValDiagnosticosOdo> diagnosticos = new ArrayList<DtoValDiagnosticosOdo>();
	
	//***************ATRIBUTOS DE LOS DISTINTOS COMPONENTES*****************************************+
	private InfoAntecedenteOdonto infoAntecedentesOdo ;
	private DtoComponenteIndicePlaca dtoIndicePlaca;
	private InfoOdontograma infoOdontograma;
	//**********************************************************************************************
	
	public void clean()
	{	
		this.codigoPk=ConstantesBD.codigoNuncaValidoDouble;
		this.cita=ConstantesBD.codigoNuncaValidoDouble;;
		this.fechaConsulta="";
		this.horaConsulta="";
		this.edadPaciente="";
		this.motivoConsulta="";
		this.enfermedadActual="";
		this.tipoDiagnostico=ConstantesBD.codigoNuncaValido;
		this.nombreTipoDiagnostico = "";
		this.causaExterna=ConstantesBD.codigoNuncaValido;
		this.nombreCausaExterna = "";
		this.finalidadConsulta="";
		this.nombreFinalidadConsulta = "";
		this.observaciones="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.hisAntecedenteOdo=ConstantesBD.codigoNuncaValidoDouble;
		
		
		this.diagnosticos = new ArrayList<DtoValDiagnosticosOdo>();
		
		//Atributos de los distintos antecedentes
		this.infoAntecedentesOdo = new InfoAntecedenteOdonto();
		this.dtoIndicePlaca = new DtoComponenteIndicePlaca();
		this.infoOdontograma = new InfoOdontograma();
	}
	
	public DtoValoracionesOdonto(){
		this.clean();
	}

	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public double getCita() {
		return cita;
	}

	public void setCita(double cita) {
		this.cita = cita;
	}

	public String getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(String fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	public String getEdadPaciente() {
		return edadPaciente;
	}

	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}

	public String getMotivoConsulta() {
		return motivoConsulta;
	}

	public void setMotivoConsulta(String motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
	}

	public String getEnfermedadActual() {
		return enfermedadActual;
	}

	public void setEnfermedadActual(String enfermedadActual) {
		this.enfermedadActual = enfermedadActual;
	}

	public int getTipoDiagnostico() {
		return tipoDiagnostico;
	}

	public void setTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}

	public int getCausaExterna() {
		return causaExterna;
	}

	public void setCausaExterna(int causaExterna) {
		this.causaExterna = causaExterna;
	}

	public String getFinalidadConsulta() {
		return finalidadConsulta;
	}

	public void setFinalidadConsulta(String finalidadConsulta) {
		this.finalidadConsulta = finalidadConsulta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public double getHisAntecedenteOdo() {
		return hisAntecedenteOdo;
	}

	public void setHisAntecedenteOdo(double hisAntecedenteOdo) {
		this.hisAntecedenteOdo = hisAntecedenteOdo;
	}

	public String getHoraConsulta() {
		return horaConsulta;
	}

	public void setHoraConsulta(String horaConsulta) {
		this.horaConsulta = horaConsulta;
	}
	
	/**
	 * Método para validar campos reuqeridos en la valoracion
	 * @param diagnosticos
	 * @param errores
	 * @return
	 */
	public ActionErrors validate(ArrayList<Diagnostico> diagnosticos,int codigoTipoDiagnostico, ActionErrors errores,String nombrePlantilla, DtoPlantilla plantilla)
	{
		/**
		 * Se validan todos los campos que componen el formulario
		 */
		
		//Realizo la validacion de cada seccion si esta es fija y se puede hacer visible/invisible. Solo cuando este visible se deben validar los campos respectivos
		for(DtoSeccionFija seccionFija:plantilla.getSeccionesFijas())
		{
			if(seccionFija.isVisible()&&seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaCausaExterna)
			{
				if (this.causaExterna==ConstantesBD.codigoNuncaValido)
				{
					errores.add("",	new ActionMessage("errors.required","La Causa Externa "+(nombrePlantilla.equals("")?"":"(plantilla "+nombrePlantilla+")")+" "));
				}
			}
			if(seccionFija.isVisible()&&seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaFinalidadConsulta)
			{
				if (this.getFinalidadConsulta().equals(""))
				{
					errores.add("",	new ActionMessage("errors.required","La finalidad de la consulta "+(nombrePlantilla.equals("")?"":"(plantilla "+nombrePlantilla+")")));
				}
			}
		}
		
		if (this.getFechaConsulta().equals(""))
			errores.add("",	new ActionMessage("errors.required","La fecha de Valoración "+(nombrePlantilla.equals("")?"":"(plantilla "+nombrePlantilla+")")+" "));

		if (this.getHoraConsulta().equals(""))
			errores.add("",	new ActionMessage("errors.required","La hora de Valoración "+(nombrePlantilla.equals("")?"":"(plantilla "+nombrePlantilla+")")));		
	
		if (codigoTipoDiagnostico<=0)
			errores.add("",	new ActionMessage("errors.required","El Tipo de diagnóstico principal "+(nombrePlantilla.equals("")?"":"(plantilla "+nombrePlantilla+")")));
		
		if (diagnosticos.size()==0||diagnosticos.get(0).getValor().equals(""))
			errores.add("",	new ActionMessage("errors.required","El diagnóstico principal "+(nombrePlantilla.equals("")?"":"(plantilla "+nombrePlantilla+")")));
		
		return errores;
	}

	/**
	 * @return the nombreCausaExterna
	 */
	public String getNombreCausaExterna() {
		return nombreCausaExterna;
	}

	/**
	 * @param nombreCausaExterna the nombreCausaExterna to set
	 */
	public void setNombreCausaExterna(String nombreCausaExterna) {
		this.nombreCausaExterna = nombreCausaExterna;
	}

	/**
	 * @return the nombreFinalidadConsulta
	 */
	public String getNombreFinalidadConsulta() {
		return nombreFinalidadConsulta;
	}

	/**
	 * @param nombreFinalidadConsulta the nombreFinalidadConsulta to set
	 */
	public void setNombreFinalidadConsulta(String nombreFinalidadConsulta) {
		this.nombreFinalidadConsulta = nombreFinalidadConsulta;
	}

	/**
	 * @return the nombreTipoDiagnostico
	 */
	public String getNombreTipoDiagnostico() {
		return nombreTipoDiagnostico;
	}

	/**
	 * @param nombreTipoDiagnostico the nombreTipoDiagnostico to set
	 */
	public void setNombreTipoDiagnostico(String nombreTipoDiagnostico) {
		this.nombreTipoDiagnostico = nombreTipoDiagnostico;
	}

	/**
	 * @return the diagnosticos
	 */
	public ArrayList<DtoValDiagnosticosOdo> getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(ArrayList<DtoValDiagnosticosOdo> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	/**
	 * @return the infoAntecedentesOdo
	 */
	public InfoAntecedenteOdonto getInfoAntecedentesOdo() {
		return infoAntecedentesOdo;
	}

	/**
	 * @param infoAntecedentesOdo the infoAntecedentesOdo to set
	 */
	public void setInfoAntecedentesOdo(InfoAntecedenteOdonto infoAntecedentesOdo) {
		this.infoAntecedentesOdo = infoAntecedentesOdo;
	}

	/**
	 * @return the dtoIndicePlaca
	 */
	public DtoComponenteIndicePlaca getDtoIndicePlaca() {
		return dtoIndicePlaca;
	}

	/**
	 * @param dtoIndicePlaca the dtoIndicePlaca to set
	 */
	public void setDtoIndicePlaca(DtoComponenteIndicePlaca dtoIndicePlaca) {
		this.dtoIndicePlaca = dtoIndicePlaca;
	}

	/**
	 * @return the infoOdontograma
	 */
	public InfoOdontograma getInfoOdontograma() {
		return infoOdontograma;
	}

	/**
	 * @param infoOdontograma the infoOdontograma to set
	 */
	public void setInfoOdontograma(InfoOdontograma infoOdontograma) {
		this.infoOdontograma = infoOdontograma;
	}
}
