package util.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;

/**
 * OBJETO PARA GUARDAR EL CONTRATO PRESUPUESTO CONVENIO
 * @author axioma
 *
 */
public class InfoConvenioContratoPresupuesto implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3593942453275522409L;

	/**
	 * 
	 */
	private InfoDatosInt convenio;
	
	/**
	 * 
	 */
	private InfoDatosInt contrato;
	
	/**
	 * con este atributo determinamos si lo mostramos a nivel de vista
	 * pues pertenece a los convenios que se cargan x defecto 
	 */
	private boolean esParametroGeneral;
	
	/**
	 * 
	 */
	private boolean activo;

	/**
	 * 
	 */
	private int indice;
	

	/**
	 * Indica cual es el ajuste que se debe realizar
	 * al valor según lo definido en la parametrización de
	 * convenios en Método de Ajuste para Cálculo de Tarifa de Servicios.
	 */
	private String ajusteServicio;
	
	
	/**
	 * 
	 * @param convenio
	 * @param contrato
	 * @param esParametroGeneral
	 * @param activo
	 */
	public InfoConvenioContratoPresupuesto() {
		super();
		reset();
	}

	private void reset() {
		this.convenio =  new InfoDatosInt();
		this.contrato =  new InfoDatosInt();
		this.esParametroGeneral = false;
		this.activo = false;
		this.indice=ConstantesBD.codigoNuncaValido;
		this.ajusteServicio =  ConstantesIntegridadDominio.acronimoSinAjuste;
	}

	/**
	 * @return the convenio
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the contrato
	 */
	public InfoDatosInt getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(InfoDatosInt contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the esParametroGeneral
	 */
	public boolean isEsParametroGeneral() {
		return esParametroGeneral;
	}

	/**
	 * @param esParametroGeneral the esParametroGeneral to set
	 */
	public void setEsParametroGeneral(boolean esParametroGeneral) {
		this.esParametroGeneral = esParametroGeneral;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @return the activo
	 */
	public boolean getActivo() {
		return activo;
	}
	
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the indice
	 */
	public int getIndice()
	{
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice)
	{
		this.indice = indice;
	}
	
	/**
	 * @return the ajusteServicio
	 */
	public String getAjusteServicio() {
		return ajusteServicio;
	}

	/**
	 * @param ajusteServicio the ajusteServicio to set
	 */
	public void setAjusteServicio(String ajusteServicio) {
		this.ajusteServicio = ajusteServicio;
	}
}
