package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class InfoPresupuestoXConvenioProgramaServicio implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4819477664934721658L;

	/**
	 * 
	 */
	private double programaExcluyente;
	
	/**
	 * 
	 */
	private int servicioExcluyente;
	
	/**
	 * 
	 */
	private int convenio;
	
	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * 	en este array voy a tener el calculo de la tarifa
	 * 1. (UTILIZA PROG) para todos los servicios del programa
	 * 2. (NO UTILIZA PROG) la posicion cero con el servicio
	 */
	private ArrayList<InfoTarifaServicioPresupuesto> detalleTarifasServicio;

	/**
	 * 
	 */
	private InfoPromocionPresupuestoServPrograma detallePromocionDescuento;
	
	/**
	 * 
	 */
	private InfoBonoDcto detalleBonoDescuento;
	
	/**
	 * 
	 */
	private boolean seleccionadoBonoInclusion;
	
	/**
	 * 
	 */
	public InfoPresupuestoXConvenioProgramaServicio()
	{
		super();
		this.programaExcluyente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.servicioExcluyente = ConstantesBD.codigoNuncaValido;
		this.convenio = ConstantesBD.codigoNuncaValido;
		this.cantidad = ConstantesBD.codigoNuncaValido;
		this.detalleTarifasServicio = new ArrayList<InfoTarifaServicioPresupuesto>();
		this.detallePromocionDescuento= new InfoPromocionPresupuestoServPrograma();
		this.detalleBonoDescuento= new InfoBonoDcto();
		this.seleccionadoBonoInclusion= false;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * @return the detalleTarifasServicio
	 */
	public ArrayList<InfoTarifaServicioPresupuesto> getDetalleTarifasServicio()
	{
		return detalleTarifasServicio;
	}

	/**
	 * @param detalleTarifasServicio the detalleTarifasServicio to set
	 */
	public void setDetalleTarifasServicio(
			ArrayList<InfoTarifaServicioPresupuesto> detalleTarifasServicio)
	{
		this.detalleTarifasServicio = detalleTarifasServicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getValorUnitarioProgramaServicioConvenio()
	{
		if(this.getErroresTotales().size()>0)
			return new BigDecimal(0);
		BigDecimal valorUnitario= new BigDecimal(0);
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			if(info.getValorTarifaUnitaria().doubleValue()>0)
			{	
				valorUnitario= valorUnitario.add(info.getValorTarifaUnitaria());
			}	
		}
		return valorUnitario;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getValorUnitarioProgramaServicioConvenioConDctoComercial()
	{
		if(this.getErroresTotales().size()>0)
			return new BigDecimal(0);
		BigDecimal valorUnitario= new BigDecimal(0);
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			if(info.getValorTarifaUnitaria().doubleValue()>0)
			{	
				valorUnitario= valorUnitario.add(info.getValorTarifaUnitaria().subtract(info.getValorDescuentoComercial()));
			}	
		}
		return valorUnitario;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getValorUnitarioConDctoBono()
	{
		if(this.getErroresTotales().size()>0)
		{	
			return new BigDecimal(0);
		}
		BigDecimal valorUnitario= new BigDecimal(0);
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			if(info.getValorTarifaUnitaria().doubleValue()>0)
			{	
				valorUnitario= valorUnitario.add(info.getValorTarifaUnitariaConDctoBono());
			}	
		}
		return valorUnitario;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValorUnitarioConDctoBonoFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorUnitarioConDctoBono().doubleValue());
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getValorUnitarioConDctoPromocion()
	{
		if(this.getErroresTotales().size()>0)
		{	
			return new BigDecimal(0);
		}
		BigDecimal valorUnitario= new BigDecimal(0);
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			if(info.getValorTarifaUnitaria().doubleValue()>0)
			{	
				valorUnitario= valorUnitario.add(info.getValorTarifaUnitariaConDctoPromocion());
			}	
		}
		return valorUnitario;
	}
		
	/**
	 * 
	 * @return
	 */
	public String getValorUnitarioConDctoPromocionFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorUnitarioConDctoPromocion().doubleValue());
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getValorUnitarioDctoComercial()
	{
		if(this.getErroresTotales().size()>0)
			return new BigDecimal(0);
		
		BigDecimal valorUnitario= new BigDecimal(0);
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			if(info.getValorDescuentoComercial().doubleValue()>0)
			{	
				valorUnitario= valorUnitario.add(info.getValorDescuentoComercial());
			}	
		}
		return valorUnitario;
	}

	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getValorTotalProgramaServicioConvenio()
	{
		if(this.getErroresTotales().size()>0)
		{	
			return BigDecimal.ZERO;
		}
		BigDecimal valorTotalConvenio= this.getValorUnitarioProgramaServicioConvenioConDctoTotales();
		valorTotalConvenio= valorTotalConvenio.multiply(new BigDecimal(this.cantidad));
		return valorTotalConvenio;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getValorUnitarioProgramaServicioConvenioConDctoTotales()
	{
		if(this.getErroresTotales().size()>0)
		{	
			return new BigDecimal(0);
		}	
		BigDecimal valorUnitario= BigDecimal.ZERO;
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			if(info.getValorTarifaTotalConDctos().doubleValue()>0)
			{	
				valorUnitario= valorUnitario.add(info.getValorTarifaTotalConDctos());
			}	
		}
		return valorUnitario;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getErroresTotales()
	{
		ArrayList<String> errores= new ArrayList<String>();
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			if(!UtilidadTexto.isEmpty(info.getError()))
				errores.add(info.getError());
		}
		return errores;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getErroresTotalesStr(String saltoLinea)
	{
		String errores="";
		
		
		boolean yaTienePrograma=Boolean.FALSE;
		for (InfoTarifaServicioPresupuesto info : this.getDetalleTarifasServicio())
		{
			Log4JManager.info(">>>>>>>>>>>>>>>>>"+info.getError());
			
			
			
			if(!UtilidadTexto.isEmpty(info.getError())){
				
				if(!yaTienePrograma){
					
					if( info.getError().trim().equals("Programa no tiene Tarifa") )
					{
						errores+=info.getError();
						yaTienePrograma=Boolean.TRUE;
					}
				}
				
			}
			
			if(!UtilidadTexto.isEmpty(info.getError())){
				
				if(!info.getError().trim().equals("Programa no tiene Tarifa")  )
				{
					if(!errores.isEmpty()){
						
						errores+= saltoLinea;
					}

					errores+= info.getError();
				}
			}
		}
		
		return errores;
	}
	
	/**
	 * @return the cantidad
	 */
	public int getCantidad()
	{
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad)
	{
		this.cantidad = cantidad;
	}

	/**
	 * @return the valorDescuentosPromociones
	 */
	public BigDecimal getValorDescuentosPromocionTotal()
	{
		if(this.getErroresTotales().size()>0)
			return new BigDecimal(0);
		
		return detallePromocionDescuento.getValorPromocion().multiply(new BigDecimal(this.cantidad));
	}

	/**
	 * @return the servicioExcluyente
	 */
	public int getServicioExcluyente()
	{
		return servicioExcluyente;
	}

	/**
	 * @param servicioExcluyente the servicioExcluyente to set
	 */
	public void setServicioExcluyente(int servicioExcluyente)
	{
		this.servicioExcluyente = servicioExcluyente;
	}

	/**
	 * @param programaExcluyente the programaExcluyente to set
	 */
	public void setProgramaExcluyente(double programaExcluyente)
	{
		this.programaExcluyente = programaExcluyente;
	}

	/**
	 * @return the programaExcluyente
	 */
	public double getProgramaExcluyente()
	{
		return programaExcluyente;
	}

	/**
	 * @return the detallePromocionDescuento
	 */
	public InfoPromocionPresupuestoServPrograma getDetallePromocionDescuento()
	{
		if(this.getErroresTotales().size()>0)
			return new InfoPromocionPresupuestoServPrograma();
		
		return detallePromocionDescuento;
	}

	/**
	 * @param detallePromocionDescuento the detallePromocionDescuento to set
	 */
	public void setDetallePromocionDescuento(
			InfoPromocionPresupuestoServPrograma detallePromocionDescuento)
	{
		this.detallePromocionDescuento = detallePromocionDescuento;
	}

	/**
	 * @return the valorDescuentosPromociones
	 */
	public BigDecimal getValorUnitarioPagoInicialPromocion()
	{
		if(this.getErroresTotales().size()>0)
			return new BigDecimal(0);
		
		
		if(this.getDetallePromocionDescuento().getPorcentajeHonorario()>0)
		{	
			return this.getValorUnitarioProgramaServicioConvenioConDctoComercial().multiply( new BigDecimal(this.getDetallePromocionDescuento().getPorcentajeHonorario()/100));
		}
		return new BigDecimal(0);
	}

	/**
	 * @return the detalleBonoDescuento
	 */
	public InfoBonoDcto getDetalleBonoDescuento()
	{
		return detalleBonoDescuento;
	}

	/**
	 * @param detalleBonoDescuento the detalleBonoDescuento to set
	 */
	public void setDetalleBonoDescuento(InfoBonoDcto detalleBonoDescuento)
	{
		this.detalleBonoDescuento = detalleBonoDescuento;
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

	
	
}
