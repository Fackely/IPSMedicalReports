/**
 * 
 */
package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.InfoDatosAcronimo;
import util.InfoDatosInt;
import util.UtilidadTexto;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 10:43:11 AM
 */
public class InfoPaquetesPresupuesto implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6503425441308401290L;

	/**
	 * 
	 */
	private boolean seleccionado;
	
	/**
	 * 
	 */
	private InfoDatosInt convenio;
	
	/**
	 * 
	 */
	private int contrato;
	
	/**
	 * 
	 */
	private int codigoPkPaqueteOdontologico;
	
	/**
	 * 
	 */
	private InfoDatosAcronimo codigoNombrePaquete;
	
	/**
	 * 
	 */
	private int detallePkPaqueteOdonCONVENIO;
	
	/**
	 * 
	 */
	private ArrayList<InfoDetallePaquetePresupuesto> listaProgramas;
	
	/**
	 * 
	 */
	private int esquemaTarifario;
	
	/**
	 * atributo con los codigos de los paquetes donde se cruzan los programas
	 */
	private ArrayList<Integer> listaDetallesPkPaqueteOdonCONVENIOCruceProgramas;
	
	/**
	 * Constructor de la clase
	 */
	public InfoPaquetesPresupuesto() 
	{
		this.seleccionado = false;
		this.convenio = new InfoDatosInt();
		this.contrato= 0;
		this.codigoPkPaqueteOdontologico = 0;
		this.detallePkPaqueteOdonCONVENIO= 0;
		this.codigoNombrePaquete = new InfoDatosAcronimo("", "");
		this.listaProgramas= new ArrayList<InfoDetallePaquetePresupuesto>();
		this.esquemaTarifario=0;
		this.listaDetallesPkPaqueteOdonCONVENIOCruceProgramas= new ArrayList<Integer>();
	}

	/**
	 * @return the seleccionado
	 */
	public boolean isSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado the seleccionado to set
	 */
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
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
	 * @return the codigoPkPaqueteOdontologico
	 */
	public int getCodigoPkPaqueteOdontologico() {
		return codigoPkPaqueteOdontologico;
	}

	/**
	 * @param codigoPkPaqueteOdontologico the codigoPkPaqueteOdontologico to set
	 */
	public void setCodigoPkPaqueteOdontologico(int codigoPkPaqueteOdontologico) {
		this.codigoPkPaqueteOdontologico = codigoPkPaqueteOdontologico;
	}

	/**
	 * @return the codigoNombrePaquete
	 */
	public InfoDatosAcronimo getCodigoNombrePaquete() {
		return codigoNombrePaquete;
	}

	/**
	 * @param codigoNombrePaquete the codigoNombrePaquete to set
	 */
	public void setCodigoNombrePaquete(InfoDatosAcronimo codigoNombrePaquete) {
		this.codigoNombrePaquete = codigoNombrePaquete;
	}

	/**
	 * @return the valorNeto
	 */
	public BigDecimal getValorNeto() 
	{
		BigDecimal valorNeto= BigDecimal.ZERO;
		for(InfoDetallePaquetePresupuesto programas: this.getListaProgramas())
		{
			valorNeto= valorNeto.add(programas.getValorTotalNetoPrograma());
		}
		return valorNeto;
	}

	
	/**
	 * @return the valorNeto
	 */
	public String getValorNetoFormateado() 
	{
		return UtilidadTexto.formatearValores(this.getValorNeto().doubleValue());
	}	

	/**
	 * @return the listaProgramas
	 */
	public ArrayList<InfoDetallePaquetePresupuesto> getListaProgramas() {
		return listaProgramas;
	}

	/**
	 * @param listaProgramas the listaProgramas to set
	 */
	public void setListaProgramas(
			ArrayList<InfoDetallePaquetePresupuesto> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public int getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(int esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * 
	 * Metodo para obtener los errores de tarifa
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public String getErroresTarifa()
	{
		for(InfoDetallePaquetePresupuesto programa: this.getListaProgramas())
		{
			for(InfoServiciosProgramaPaquetePresupuesto servicio: programa.getListaServicios())
			{	
				if(!servicio.isExisteTarifa())
				{	
					return "NO EXISTE TARIFA";
				}	
			}
		}
		return "";
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
	 * @return the detallePkPaqueteOdonCONVENIO
	 */
	public int getDetallePkPaqueteOdonCONVENIO() {
		return detallePkPaqueteOdonCONVENIO;
	}

	/**
	 * @param detallePkPaqueteOdonCONVENIO the detallePkPaqueteOdonCONVENIO to set
	 */
	public void setDetallePkPaqueteOdonCONVENIO(int detallePkPaqueteOdonCONVENIO) {
		this.detallePkPaqueteOdonCONVENIO = detallePkPaqueteOdonCONVENIO;
	}

	/**
	 * @return the listaDetallesPkPaqueteOdonCONVENIOCruceProgramas
	 */
	public ArrayList<Integer> getListaDetallesPkPaqueteOdonCONVENIOCruceProgramas() {
		return listaDetallesPkPaqueteOdonCONVENIOCruceProgramas;
	}

	/**
	 * @param listaDetallesPkPaqueteOdonCONVENIOCruceProgramas the listaDetallesPkPaqueteOdonCONVENIOCruceProgramas to set
	 */
	public void setListaDetallesPkPaqueteOdonCONVENIOCruceProgramas(
			ArrayList<Integer> listaDetallesPkPaqueteOdonCONVENIOCruceProgramas) {
		this.listaDetallesPkPaqueteOdonCONVENIOCruceProgramas = listaDetallesPkPaqueteOdonCONVENIOCruceProgramas;
	}
}