package util.odontologia;

/**
 * @author Víctor Hugo Gómez L.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoTratamientoExterno;

public class InfoAntecedenteOdonto implements Serializable
{
	private String utilizaProgOdonto;
	private int codigoPaciente;
	private int codigoInstitucion;
	private String mostrarPor;
	private String porConfirmar;
	private int codigoAnteOdon;
	private ArrayList<InfoPlanTratamiento> infoPlaTratamiento;
	private String forward;
	

	//Atributos interfaz Tratamiento Externo
	private ArrayList<InfoDatosInt> piezasDentales;
	private ArrayList<HashMap<String, Object>> especialidadesOdonto;
	
	// lista tratamiento externo
	private String procesoExitoTE;
	private String accionTratExt;
	private DtoTratamientoExterno trataExterno;
	private int posTratamientoExterno;
	private DtoAntecendenteOdontologico antecedenteOdon;
	
	public InfoAntecedenteOdonto()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.utilizaProgOdonto = "";
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.mostrarPor = "";
		this.porConfirmar = ConstantesBD.acronimoNo;
		this.codigoAnteOdon = ConstantesBD.codigoNuncaValido;
		this.infoPlaTratamiento = null;
		this.forward = "";
		
		//Atributos interfaz Tratamiento Externo
		this.piezasDentales = null;
		this.especialidadesOdonto = null;
		
		// lista tratamiento externo
		this.procesoExitoTE = ConstantesBD.acronimoNo;
		this.accionTratExt = "";
		this.trataExterno = new DtoTratamientoExterno();
		this.posTratamientoExterno = ConstantesBD.codigoNuncaValido;
		this.antecedenteOdon = new DtoAntecendenteOdontologico();
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the infoPlaTratamiento
	 */
	public ArrayList<InfoPlanTratamiento> getInfoPlaTratamiento() {
		if(infoPlaTratamiento == null)
		{
			infoPlaTratamiento = new ArrayList<InfoPlanTratamiento>();
		}
		return infoPlaTratamiento;
	}

	/**
	 * @param infoPlaTratamiento the infoPlaTratamiento to set
	 */
	public void setInfoPlaTratamiento(
			ArrayList<InfoPlanTratamiento> infoPlaTratamiento) {
		this.infoPlaTratamiento = infoPlaTratamiento;
	}

	/**
	 * @return the mostrarPor
	 */
	public String getMostrarPor() {
		return mostrarPor;
	}

	/**
	 * @param mostrarPor the mostrarPor to set
	 */
	public void setMostrarPor(String mostrarPor) {
		this.mostrarPor = mostrarPor;
	}

	/**
	 * @return the utilizaProgOdonto
	 */
	public String getUtilizaProgOdonto() {
		return utilizaProgOdonto;
	}

	/**
	 * @param utilizaProgOdonto the utilizaProgOdonto to set
	 */
	public void setUtilizaProgOdonto(String utilizaProgOdonto) {
		this.utilizaProgOdonto = utilizaProgOdonto;
	}

	/**
	 * @return the piezasDentales
	 */
	public ArrayList<InfoDatosInt> getPiezasDentales() {
		if(piezasDentales == null)
		{
			piezasDentales = new ArrayList<InfoDatosInt>();
		}
		return piezasDentales;
	}

	/**
	 * @param piezasDentales the piezasDentales to set
	 */
	public void setPiezasDentales(ArrayList<InfoDatosInt> piezasDentales) {
		this.piezasDentales = piezasDentales;
	}

	/**
	 * @return the especialidadesOdonto
	 */
	public ArrayList<HashMap<String, Object>> getEspecialidadesOdonto() {
		if(especialidadesOdonto == null)
		{
			especialidadesOdonto = new ArrayList<HashMap<String, Object>>();
		}
		return especialidadesOdonto;
	}

	/**
	 * @param especialidadesOdonto the especialidadesOdonto to set
	 */
	public void setEspecialidadesOdonto(
			ArrayList<HashMap<String, Object>> especialidadesOdonto) {
		this.especialidadesOdonto = especialidadesOdonto;
	}

	/**
	 * @return the posTratamientoExterno
	 */
	public int getPosTratamientoExterno() {
		return posTratamientoExterno;
	}

	/**
	 * @param posTratamientoExterno the posTratamientoExterno to set
	 */
	public void setPosTratamientoExterno(int posTratamientoExterno) {
		this.posTratamientoExterno = posTratamientoExterno;
	}


	/**
	 * @return the trataExteno
	 */
	public DtoTratamientoExterno getTrataExterno() {
		return trataExterno;
	}

	/**
	 * @param trataExteno the trataExteno to set
	 */
	public void setTrataExterno(DtoTratamientoExterno trataExteno) {
		this.trataExterno = trataExteno;
	}

	/**
	 * @return the forward
	 */
	public String getForward() {
		return forward;
	}

	/**
	 * @param forward the forward to set
	 */
	public void setForward(String forward) {
		this.forward = forward;
	}

	/**
	 * @return the accionTratExt
	 */
	public String getAccionTratExt() {
		return accionTratExt;
	}

	/**
	 * @param accionTratExt the accionTratExt to set
	 */
	public void setAccionTratExt(String accionTratExt) {
		this.accionTratExt = accionTratExt;
	}

	/**
	 * @return the procesoExitoTE
	 */
	public String getProcesoExitoTE() {
		return procesoExitoTE;
	}

	/**
	 * @param procesoExitoTE the procesoExitoTE to set
	 */
	public void setProcesoExitoTE(String procesoExitoTE) {
		this.procesoExitoTE = procesoExitoTE;
	}

	/**
	 * @return the antecedenteOdon
	 */
	public DtoAntecendenteOdontologico getAntecedenteOdon() {
		return antecedenteOdon;
	}

	/**
	 * @param antecedenteOdon the antecedenteOdon to set
	 */
	public void setAntecedenteOdon(DtoAntecendenteOdontologico antecedenteOdon) {
		this.antecedenteOdon = antecedenteOdon;
	}

	/**
	 * @return the porConfirmar
	 */
	public String getPorConfirmar() {
		return porConfirmar;
	}

	/**
	 * @param porConfirmar the porConfirmar to set
	 */
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	/**
	 * @return the codigoAnteOdon
	 */
	public int getCodigoAnteOdon() {
		return codigoAnteOdon;
	}

	/**
	 * @param codigoAnteOdon the codigoAnteOdon to set
	 */
	public void setCodigoAnteOdon(int codigoAnteOdon) {
		this.codigoAnteOdon = codigoAnteOdon;
	}

}
