package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import com.servinte.axioma.orm.ProgramasHallazgoPieza;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class InfoInclusionExclusionBoca implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2730429824021872434L;

	/**
	 * 
	 */
	private BigDecimal codigoPkDetPlanTratamiento;
	
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
	private BigDecimal hallazgo;
	
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
	 * 
	 */
	private boolean seleccionadoBonoInclusion;
	
	
	/**
	 * Código del {@link ProgramasHallazgoPieza}
	 */
	private BigDecimal codigoPkProgramaHallazgoPieza;
	

	/**
	 * Indica si se esa inclusión se encuentra precontratada 
	 */
	private String precontratada;
	
	/**
	 * 
	 * @param programaOservicio
	 */
	public InfoInclusionExclusionBoca()
	{
		super();
		this.codigoPkDetPlanTratamiento= new BigDecimal(0);
		this.programaOservicio = new InfoDatosDouble();
		this.activo=false;
		this.indiceConvenioContratoSeleccionado=ConstantesBD.codigoNuncaValido;
		this.erroresCalculoTarifa="";
		this.hallazgo= new BigDecimal(0);
		this.codigoPkPresupuestoProgServ= new BigDecimal(0);
		this.valorTarifa= BigDecimal.ZERO;
		this.infoTarifa= new InfoPresupuestoXConvenioProgramaServicio();
		this.seleccionadoBonoInclusion= false;
		
		this.codigoPkProgramaHallazgoPieza = new BigDecimal(ConstantesBD.codigoNuncaValido);
		
		this.setPrecontratada(ConstantesBD.acronimoNo);
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
	 * @return the codigoPkDetPlanTratamiento
	 */
	public BigDecimal getCodigoPkDetPlanTratamiento()
	{
		return codigoPkDetPlanTratamiento;
	}

	/**
	 * @param codigoPkDetPlanTratamiento the codigoPkDetPlanTratamiento to set
	 */
	public void setCodigoPkDetPlanTratamiento(BigDecimal codigoPkDetPlanTratamiento)
	{
		this.codigoPkDetPlanTratamiento = codigoPkDetPlanTratamiento;
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
	 * @return the seleccionadoBonoInclusion
	 */
	public boolean isSeleccionadoBonoInclusion() {
		return seleccionadoBonoInclusion;
	}

	/**
	 * @param seleccionadoBonoInclusion the seleccionadoBonoInclusion to set
	 */
	public void setSeleccionadoBonoInclusion(boolean seleccionadoBonoInclusion) {
		this.seleccionadoBonoInclusion = seleccionadoBonoInclusion;
	}

	/**
	 * @param codigoPkProgramaHallazgoPieza the codigoPkProgramaHallazgoPieza to set
	 */
	public void setCodigoPkProgramaHallazgoPieza(
			BigDecimal codigoPkProgramaHallazgoPieza) {
		this.codigoPkProgramaHallazgoPieza = codigoPkProgramaHallazgoPieza;
	}

	/**
	 * @return the codigoPkProgramaHallazgoPieza
	 */
	public BigDecimal getCodigoPkProgramaHallazgoPieza() {
		return codigoPkProgramaHallazgoPieza;
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
