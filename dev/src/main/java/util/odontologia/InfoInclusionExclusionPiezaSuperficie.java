package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoPiezaDental;

/**
 * 
 * @author axioma
 *
 */
public class InfoInclusionExclusionPiezaSuperficie implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1118389922021675192L;

	/**
	 * 
	 */
	private BigDecimal codigoPkProgramaHallazgoPieza;
	
	/**
	 * 
	 */
	private DtoPiezaDental piezaDental;
	
	/**
	 * 
	 */
	private ArrayList<InfoSuperficiePkDetPlan> superficies;
	
	/**
	 * 
	 */
	private BigDecimal hallazgo;
	
	/**
	 * 
	 */
	private InfoDatosDouble programaOservicio;

	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 */
	private int indiceConvenioContratoSeleccionado;
	
	/**
	 * 
	 */
	private String erroresCalculoTarifa;
	
	/**
	 * 
	 */
	private String seccion;
	
	/**
	 * 
	 */
	private BigDecimal codigoPkPresupuestoProgServ;
	
	/**
	 * 
	 */
	private BigDecimal valorTarifa;
	
	
	/**
	 * 
	 */
	private InfoPresupuestoXConvenioProgramaServicio infoTarifa;
	
	/**
	 * Indica si se esa inclusión se encuentra precontratada 
	 */
	private String precontratada;
	
	/**
	 * 
	 * @param piezaDental
	 * @param superficie
	 * @param programaOservicio
	 */
	public InfoInclusionExclusionPiezaSuperficie()
	{
		super();
		this.codigoPkProgramaHallazgoPieza= BigDecimal.ZERO;
		this.piezaDental = new DtoPiezaDental();
		this.superficies = new ArrayList<InfoSuperficiePkDetPlan>();
		this.programaOservicio = new InfoDatosDouble();
		this.activo= false;
		this.indiceConvenioContratoSeleccionado= ConstantesBD.codigoNuncaValido;
		this.erroresCalculoTarifa="";
		this.hallazgo= new BigDecimal(0);
		this.seccion="";
		this.codigoPkPresupuestoProgServ= new BigDecimal(0);
		this.valorTarifa= BigDecimal.ZERO;
		this.infoTarifa= new InfoPresupuestoXConvenioProgramaServicio();
		this.setPrecontratada(ConstantesBD.acronimoNo);
	}

	/**
	 * @return the piezaDental
	 */
	public DtoPiezaDental getPiezaDental()
	{
		return piezaDental;
	}

	/**
	 * @param piezaDental the piezaDental to set
	 */
	public void setPiezaDental(DtoPiezaDental piezaDental)
	{
		this.piezaDental = piezaDental;
	}

	/**
	 * @return the programaOservicio
	 */
	public InfoDatosDouble getProgramaOservicio()
	{
		return programaOservicio;
	}

	/**
	 * @param programaOservicio the programaOservicio to set
	 */
	public void setProgramaOservicio(InfoDatosDouble programaOservicio)
	{
		this.programaOservicio = programaOservicio;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo()
	{
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo)
	{
		this.activo = activo;
	}
	
	/**
	 * @return the activo
	 */
	public boolean getActivo()
	{
		return activo;
	}

	/**
	 * @return the indiceConvenioContratoSeleccionado
	 */
	public int getIndiceConvenioContratoSeleccionado()
	{
		return indiceConvenioContratoSeleccionado;
	}

	/**
	 * @param indiceConvenioContratoSeleccionado the indiceConvenioContratoSeleccionado to set
	 */
	public void setIndiceConvenioContratoSeleccionado(
			int indiceConvenioContratoSeleccionado)
	{
		this.indiceConvenioContratoSeleccionado = indiceConvenioContratoSeleccionado;
	}

	/**
	 * @return the erroresCalculoTarifa
	 */
	public String getErroresCalculoTarifa()
	{
		return erroresCalculoTarifa;
	}

	/**
	 * @param erroresCalculoTarifa the erroresCalculoTarifa to set
	 */
	public void setErroresCalculoTarifa(String erroresCalculoTarifa)
	{
		this.erroresCalculoTarifa = erroresCalculoTarifa;
	}

	/**
	 * @return the hallazgo
	 */
	public BigDecimal getHallazgo()
	{
		return hallazgo;
	}

	/**
	 * @param hallazgo the hallazgo to set
	 */
	public void setHallazgo(BigDecimal hallazgo)
	{
		this.hallazgo = hallazgo;
	}

	/**
	 * @return the seccion
	 */
	public String getSeccion()
	{
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion)
	{
		this.seccion = seccion;
	}

	/**
	 * @return the codigoPkPresupuestoProgServ
	 */
	public BigDecimal getCodigoPkPresupuestoProgServ()
	{
		return codigoPkPresupuestoProgServ;
	}

	/**
	 * @param codigoPkPresupuestoProgServ the codigoPkPresupuestoProgServ to set
	 */
	public void setCodigoPkPresupuestoProgServ(
			BigDecimal codigoPkPresupuestoProgServ)
	{
		this.codigoPkPresupuestoProgServ = codigoPkPresupuestoProgServ;
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
	 * @return the valorTarifa
	 */
	public String getValorTarifaFormateado() {
		return UtilidadTexto.formatearValores(valorTarifa.doubleValue());
	}

	/**
	 * @return the infoTarifa
	 */
	public InfoPresupuestoXConvenioProgramaServicio getInfoTarifa() {
		return infoTarifa;
	}

	/**
	 * @param infoTarifa the infoTarifa to set
	 */
	public void setInfoTarifa(InfoPresupuestoXConvenioProgramaServicio infoTarifa) {
		this.infoTarifa = infoTarifa;
	}

	/**
	 * @return the codigoPkProgramaHallazgoPieza
	 */
	public BigDecimal getCodigoPkProgramaHallazgoPieza() {
		return codigoPkProgramaHallazgoPieza;
	}

	/**
	 * @param codigoPkProgramaHallazgoPieza the codigoPkProgramaHallazgoPieza to set
	 */
	public void setCodigoPkProgramaHallazgoPieza(
			BigDecimal codigoPkProgramaHallazgoPieza) {
		this.codigoPkProgramaHallazgoPieza = codigoPkProgramaHallazgoPieza;
	}

	/**
	 * @return the superficies
	 */
	public ArrayList<InfoSuperficiePkDetPlan> getSuperficies() {
		return superficies;
	}

	/**
	 * @param superficies the superficies to set
	 */
	public void setSuperficies(ArrayList<InfoSuperficiePkDetPlan> superficies) {
		this.superficies = superficies;
	}	
	
	/**
	 * 
	 * Metodo para .......
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public String getNombresSuperficiesAgrupada()
	{
		String resultado="";
		for(InfoSuperficiePkDetPlan dto: this.getSuperficies())
		{
			resultado+=(resultado.isEmpty())?"":", ";
			resultado+= dto.getSuperficie().getNombre();
		}
		return resultado;
	}

	/**
	 * @param precontratada the precontratada to set
	 */
	public void setPrecontratada(String precontratada) {
		this.precontratada = precontratada;
	}

	/**
	 * @return the precontratada
	 */
	public String getPrecontratada() {
		return precontratada;
	}
	
}
