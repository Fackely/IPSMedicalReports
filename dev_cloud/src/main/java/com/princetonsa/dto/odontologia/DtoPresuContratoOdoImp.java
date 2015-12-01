package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;


/**
 * Edgar Carvajal 
 * @author axioma
 *
 */
public class DtoPresuContratoOdoImp  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private long codigoPk;
	private  long codigoPresuContratoOdo ;
	private String 	clausulas;
	private String piePagina;
	private String usuarioModifica;
	private String fechaModifica ; 
	private String horaModifica;
	
	private ArrayList<DtoFirmasContratoOtrosiInst> listaFirmasContrato;
	
	
	
	
	
	/**
	 *	 
	 */
	public  DtoPresuContratoOdoImp()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.codigoPresuContratoOdo= ConstantesBD.codigoNuncaValidoLong;
		this.clausulas="";
		this.piePagina="";
		this.usuarioModifica="";
		this.fechaModifica="";
		this.horaModifica="";
		this.listaFirmasContrato= new ArrayList<DtoFirmasContratoOtrosiInst>();
	}
	
	
	
	
	/**
	 * @return the codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}
	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * @return the codigoPresuContratoOdo
	 */
	public long getCodigoPresuContratoOdo() {
		return codigoPresuContratoOdo;
	}
	/**
	 * @param codigoPresuContratoOdo the codigoPresuContratoOdo to set
	 */
	public void setCodigoPresuContratoOdo(long codigoPresuContratoOdo) {
		this.codigoPresuContratoOdo = codigoPresuContratoOdo;
	}
	/**
	 * @return the clausulas
	 */
	public String getClausulas() {
		return clausulas;
	}
	/**
	 * @param clausulas the clausulas to set
	 */
	public void setClausulas(String clausulas) {
		this.clausulas = clausulas;
	}
	/**
	 * @return the piePagina
	 */
	public String getPiePagina() {
		return piePagina;
	}
	/**
	 * @param piePagina the piePagina to set
	 */
	public void setPiePagina(String piePagina) {
		this.piePagina = piePagina;
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
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}
	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}




	public void setListaFirmasContrato(ArrayList<DtoFirmasContratoOtrosiInst> listaFirmasContrato) {
		this.listaFirmasContrato = listaFirmasContrato;
	}




	public ArrayList<DtoFirmasContratoOtrosiInst> getListaFirmasContrato() {
		return listaFirmasContrato;
	} 
	
	
	

}
