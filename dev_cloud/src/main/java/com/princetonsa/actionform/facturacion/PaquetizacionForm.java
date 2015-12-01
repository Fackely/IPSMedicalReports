/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.actionform.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.InfoCobertura;

import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class PaquetizacionForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * responsables del ingreso.
	 */
	private ArrayList<DtoSubCuentas> responsables;
	
	/**
	 * Responsable seleccionado para paquetizar.
	 */
	private DtoSubCuentas responsableCuenta;
	
	/**
	 * 
	 */
	private int indexResponSeleccionado;
	
	/**
	 * Paquetes validos para mostrar en el select, (paquetes validos para el convenio seleccionado.)
	 */
	private HashMap<String, Object> paquetesValidosConvenio;
	
	/**
	 * Paquetes asociados al responsable.
	 */
	private HashMap<String, Object> paquetesResponsables;
	
	/**
	 * 
	 */
	private HashMap<String, Object> paquetesResponsablesEliminados;
	
	/**
	 * servicios asociados a los paquetes indice codigoservicio_codigopaquetizacion.
	 */
	private HashMap<String, Object> serviciosPaquetes;
	
	/**
	 * 
	 */
	private int numPaquetesMemoria;
	
	
	/**
	 * Varible que maneja el indice del paquete se selecciono desde el pop-up
	 */
	private int indicePaqueteSeleccionado;
	
	/**
	 * Vairable que contine el indice del paquete que se liquidara automaticamente.
	 */
	private int indicePaqueteLiquidacionAuto;
	
	/**
	 * Mapa para manejar los ingresos que tiene un paciente.
	 */
	private HashMap<String, Object> ingresos;
	
	/**
	 * varibales temporales para capturarlas del pop-up de paquetes, y luego pasarla al mapa de paquetes.
	 */
	private String centroCosto;
	private String centroCostoSolicita;
	private String centroCostoEjecuta;
	private String fechaInicial;
	private String fechaFinal;
	
	
	private ResultadoBoolean mostrarMensaje;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensajeProceso;
	
	
	/**
	 * 
	 */
	private HashMap liquidacionPaquetes;
	
	
	private InfoCobertura infoCobertura;
	
	/**
	 * Indice seleccionado del ingreso.
	 */
	private int indiceIngresoSeleccionado;
	
	/**
	 * 
	 */
	private boolean cuentaProcesoDistribucion;

	
	private ArrayList<String> mensajes = new ArrayList<String>();
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.responsables=new ArrayList<DtoSubCuentas>();
		this.responsableCuenta=new DtoSubCuentas();
		this.indexResponSeleccionado=ConstantesBD.codigoNuncaValido;
		this.paquetesResponsables=new HashMap<String,Object>();
		this.paquetesValidosConvenio=new HashMap<String,Object>();
		this.paquetesResponsables.put("numRegistros", "0");
		this.paquetesResponsablesEliminados=new HashMap<String,Object>();
		this.paquetesResponsablesEliminados.put("numRegistros", "0");
		this.paquetesValidosConvenio.put("numRegistros", "0");
		this.serviciosPaquetes=new HashMap<String,Object>();
		this.serviciosPaquetes.put("numRegistros", "0");
		this.numPaquetesMemoria=0;
		this.indicePaqueteSeleccionado=ConstantesBD.codigoNuncaValido;
		this.centroCosto="";
		this.centroCostoSolicita="";
		this.centroCostoEjecuta="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.indicePaqueteLiquidacionAuto=ConstantesBD.codigoNuncaValido;
		this.mostrarMensaje=new ResultadoBoolean(false,"");
		this.mensajeProceso= new ResultadoBoolean(false,"");
		this.liquidacionPaquetes=new HashMap();
		this.liquidacionPaquetes.put("numRegistros", "0");
		this.infoCobertura=new InfoCobertura();
		
		this.ingresos=new HashMap<String, Object>();
		this.ingresos.put("numRegistros", "0");
		this.indiceIngresoSeleccionado=ConstantesBD.codigoNuncaValido;

		this.mensajes = new ArrayList<String>();
	}
	
	
	
	/**
	 * Método para validar la inserción de datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("guardar"))
		{
			for(int i=0;i<Utilidades.convertirAEntero(this.getPaquetesResponsables("numRegistros")+"");i++)
			{
				if(!UtilidadTexto.getBoolean(paquetesResponsables.get("facturado_"+i)+""))
				{	
					if((this.serviciosPaquetes.containsKey("principal_"+this.getPaquetesResponsables("codpaquetizacion_"+i)+"")&&!UtilidadTexto.isEmpty(this.serviciosPaquetes.containsKey("principal_"+this.getPaquetesResponsables("codpaquetizacion_"+i)+"")+"")))
					{
						String servPrincipal[]=(this.getServiciosPaquetes("principal_"+this.getPaquetesResponsables("codpaquetizacion_"+i)+"")+"").split("_");
						
						for(int j=0;j<this.getResponsableCuenta().getSolicitudesSubcuenta().size();j++)
						{
							DtoSolicitudesSubCuenta solSubcuentas=this.getResponsableCuenta().getSolicitudesSubcuenta().get(j);
							
							if(solSubcuentas.getServicio().getCodigo().equals(servPrincipal[1])&&solSubcuentas.getNumeroSolicitud().equals(servPrincipal[0])&&solSubcuentas.getDetcxhonorarios()==Utilidades.convertirAEntero(servPrincipal[4])&&solSubcuentas.getDetasicxsalasmat()==Utilidades.convertirAEntero(servPrincipal[5]))
							{
								String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
								String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
								String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
								servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
								int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
								int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();
								if(this.getServiciosPaquetes().containsKey("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+this.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat))
								{
									if(Utilidades.convertirAEntero(this.getServiciosPaquetes("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+this.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"")<=0)
									{
										errores.add("error.errorEnBlanco", new ActionMessage("error.errorEnBlanco","El servicio principal debe tener asignado cantidad mayor a 0."));
									}
								}
								else
								{
									errores.add("error.errorEnBlanco", new ActionMessage("error.errorEnBlanco","El servicio 55555555 principal debe tener asignado cantidad mayor a 0."));
								}
							}
						}
					}
					else
					{
						errores.add("error.errorEnBlanco", new ActionMessage("error.errorEnBlanco","Se debe marcar un servicio principal por paquete."));
					}
				}	
			}
		}
		return errores;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the responsables
	 */
	public ArrayList<DtoSubCuentas> getResponsables() {
		return responsables;
	}


	/**
	 * @param responsables the responsables to set
	 */
	public void setResponsables(ArrayList<DtoSubCuentas> responsables) {
		this.responsables = responsables;
	}


	/**
	 * @return the responsableCuenta
	 */
	public DtoSubCuentas getResponsableCuenta() {
		return responsableCuenta;
	}


	/**
	 * @param responsableCuenta the responsableCuenta to set
	 */
	public void setResponsableCuenta(DtoSubCuentas responsableCuenta) {
		this.responsableCuenta = responsableCuenta;
	}


	/**
	 * @return the indexResponSeleccionado
	 */
	public int getIndexResponSeleccionado() {
		return indexResponSeleccionado;
	}


	/**
	 * @param indexResponSeleccionado the indexResponSeleccionado to set
	 */
	public void setIndexResponSeleccionado(int indexResponSeleccionado) {
		this.indexResponSeleccionado = indexResponSeleccionado;
	}


	/**
	 * @return the paquetesResponsables
	 */
	public HashMap<String, Object> getPaquetesResponsables() {
		return paquetesResponsables;
	}


	/**
	 * @param paquetesResponsables the paquetesResponsables to set
	 */
	public void setPaquetesResponsables(HashMap<String, Object> paquetesResponsables) {
		this.paquetesResponsables = paquetesResponsables;
	}


	

	/**
	 * @return the paquetesValidosConvenio
	 */
	public HashMap<String, Object>  getPaquetesValidosConvenio(){
		return paquetesValidosConvenio;
	}


	/**
	 * @param paquetesValidosConvenio the paquetesValidosConvenio to set
	 */
	public void setPaquetesValidosConvenio(HashMap<String, Object> paquetesValidosConvenio) 
	{
		this.paquetesValidosConvenio = paquetesValidosConvenio;
	}

	/**
	 * @return the paquetesValidosConvenio
	 */
	public Object getPaquetesValidosConvenio(String key) 
	{
		return paquetesValidosConvenio.get(key);
	}

	
	/**
	 * @param paquetesValidosConvenio the paquetesValidosConvenio to set
	 */
	public void setPaquetesValidosConvenio(String key,Object value) 
	{
		this.paquetesValidosConvenio.put(key, value);
	}
	

	/**
	 * @return the paquetesResponsables
	 */
	public Object getPaquetesResponsables(String key) 
	{
		return paquetesResponsables.get(key);
	}


	/**
	 * @param paquetesResponsables the paquetesResponsables to set
	 */
	public void setPaquetesResponsables(String key,Object value) 
	{
		this.paquetesResponsables.put(key, value);
	}


	/**
	 * @return the serviciosPaquetes
	 */
	public HashMap<String, Object> getServiciosPaquetes() {
		return serviciosPaquetes;
	}


	/**
	 * @param serviciosPaquetes the serviciosPaquetes to set
	 */
	public void setServiciosPaquetes(HashMap<String, Object> serviciosPaquetes) {
		this.serviciosPaquetes = serviciosPaquetes;
	}


	/**
	 * @return the serviciosPaquetes
	 */
	public Object getServiciosPaquetes(String key) 
	{
		return serviciosPaquetes.get(key);
	}


	/**
	 * @param serviciosPaquetes the serviciosPaquetes to set
	 */
	public void setServiciosPaquetes(String key,Object value) 
	{
		this.serviciosPaquetes.put(key, value);
	}


	/**
	 * @return the numPaquetesMemoria
	 */
	public int getNumPaquetesMemoria() {
		return numPaquetesMemoria;
	}


	/**
	 * @param numPaquetesMemoria the numPaquetesMemoria to set
	 */
	public void setNumPaquetesMemoria(int numPaquetesMemoria) {
		this.numPaquetesMemoria = numPaquetesMemoria;
	}


	/**
	 * @return the indicePaqueteSeleccionado
	 */
	public int getIndicePaqueteSeleccionado() {
		return indicePaqueteSeleccionado;
	}


	/**
	 * @param indicePaqueteSeleccionado the indicePaqueteSeleccionado to set
	 */
	public void setIndicePaqueteSeleccionado(int indicePaqueteSeleccionado) {
		this.indicePaqueteSeleccionado = indicePaqueteSeleccionado;
	}


	/**
	 * @return the centroCosto
	 */
	public String getCentroCosto() {
		return centroCosto;
	}


	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}


	/**
	 * @return the centroCostoEjecuta
	 */
	public String getCentroCostoEjecuta() {
		return centroCostoEjecuta;
	}


	/**
	 * @param centroCostoEjecuta the centroCostoEjecuta to set
	 */
	public void setCentroCostoEjecuta(String centroCostoEjecuta) {
		this.centroCostoEjecuta = centroCostoEjecuta;
	}


	/**
	 * @return the centroCostoSolicita
	 */
	public String getCentroCostoSolicita() {
		return centroCostoSolicita;
	}


	/**
	 * @param centroCostoSolicita the centroCostoSolicita to set
	 */
	public void setCentroCostoSolicita(String centroCostoSolicita) {
		this.centroCostoSolicita = centroCostoSolicita;
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


	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * @return the indicePaqueteLiquidacionAuto
	 */
	public int getIndicePaqueteLiquidacionAuto() {
		return indicePaqueteLiquidacionAuto;
	}


	/**
	 * @param indicePaqueteLiquidacionAuto the indicePaqueteLiquidacionAuto to set
	 */
	public void setIndicePaqueteLiquidacionAuto(int indicePaqueteLiquidacionAuto) {
		this.indicePaqueteLiquidacionAuto = indicePaqueteLiquidacionAuto;
	}


	/**
	 * @return the paquetesResponsablesEliminados
	 */
	public HashMap<String, Object> getPaquetesResponsablesEliminados() {
		return paquetesResponsablesEliminados;
	}


	/**
	 * @param paquetesResponsablesEliminados the paquetesResponsablesEliminados to set
	 */
	public void setPaquetesResponsablesEliminados(
			HashMap<String, Object> paquetesResponsablesEliminados) {
		this.paquetesResponsablesEliminados = paquetesResponsablesEliminados;
	}


	/**
	 * @return the mostrarMensaje
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}


	/**
	 * @param mostrarMensaje the mostrarMensaje to set
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}


	/**
	 * @return the liquidacionPaquetes
	 */
	public HashMap getLiquidacionPaquetes() {
		return liquidacionPaquetes;
	}


	/**
	 * @param liquidacionPaquetes the liquidacionPaquetes to set
	 */
	public void setLiquidacionPaquetes(HashMap liquidacionPaquetes) {
		this.liquidacionPaquetes = liquidacionPaquetes;
	}


	/**
	 * @return the inforCobertura
	 */
	public InfoCobertura getInfoCobertura() {
		return infoCobertura;
	}


	/**
	 * @param inforCobertura the inforCobertura to set
	 */
	public void setInfoCobertura(InfoCobertura inforCobertura) {
		this.infoCobertura = inforCobertura;
	}
	
	/**
	 * @return the ingresos
	 */
	public HashMap<String, Object> getIngresos() {
		return ingresos;
	}


	/**
	 * @param ingresos the ingresos to set
	 */
	public void setIngresos(HashMap<String, Object> ingresos) {
		this.ingresos = ingresos;
	}



	public int getIndiceIngresoSeleccionado() {
		return indiceIngresoSeleccionado;
	}



	public void setIndiceIngresoSeleccionado(int indiceIngresoSeleccionado) {
		this.indiceIngresoSeleccionado = indiceIngresoSeleccionado;
	}



	/**
	 * @return the cuentaProcesoDistribucion
	 */
	public boolean isCuentaProcesoDistribucion() {
		return cuentaProcesoDistribucion;
	}



	/**
	 * @param cuentaProcesoDistribucion the cuentaProcesoDistribucion to set
	 */
	public void setCuentaProcesoDistribucion(boolean cuentaProcesoDistribucion) {
		this.cuentaProcesoDistribucion = cuentaProcesoDistribucion;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getMensajes() {
		return mensajes;
	}

	/**
	 * 
	 * @return
	 */
	public int getSizeMensajes() {
		return mensajes.size();
	}

	/**
	 * 
	 * @param mensajes
	 */
	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensajeProceso() {
		return mensajeProceso;
	}

	/**
	 * 
	 * @param mensajeProceso
	 */
	public void setMensajeProceso(ResultadoBoolean mensajeProceso) {
		this.mensajeProceso = mensajeProceso;
	}



}
