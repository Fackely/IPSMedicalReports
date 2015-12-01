package com.princetonsa.actionform.capitacion;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.capitacion.ConstantesCapitacion;

import com.princetonsa.action.capitacion.ParametrizarPresupuestoCapitacionAction;
import com.servinte.axioma.dto.capitacion.DtoLogParamPresupCap;
import com.servinte.axioma.dto.capitacion.DtoLogParametrizacionPresupuesto;
import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.dto.capitacion.DtoNivelesAtencionPresupuestoParametrizacionGeneral;
import com.servinte.axioma.dto.capitacion.DtoParamPresupCap;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.DetalleValorizacionArt;
import com.servinte.axioma.orm.DetalleValorizacionServ;
import com.servinte.axioma.orm.LogDetalleParamPresup;
import com.servinte.axioma.orm.LogParamPresupuestoCap;
import com.servinte.axioma.orm.MotivosModifiPresupuesto;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.orm.ValorizacionPresCapGen;

public class ParametrizarPresupuestoCapitacionForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** * Log */
	Logger logger = Logger.getLogger(ParametrizarPresupuestoCapitacionAction.class);
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ParametrizarPresupuestoCapitacionForm");
	
	/**
	 * 
	 */
	private String estado;
	
	private Convenios convenio;
	
	private Contratos contrato;
	
	private String vigencia;
	
	private int numContratos;
	
	private double porcentajeGastoMensual;
	
	private double valorGastoMensual;
	
	private boolean existeParametrizacion;
	
	private boolean existeParametrizacionDetalleGrupoServicio;
	
	private boolean existeParametrizacionDetalleClaseInventario;
	
	private String mensajeConfirmacionGuardar;
	
	private String mensajeMotivoModificacion;
	
	private String mensajePorcentajesCien;
	
	private boolean guardoDatos;
	
	private boolean guardoDatosDetalleGrupoServicio;
	
	private boolean guardoDatosDetalleClaseInventario;
	
	private ParamPresupuestosCap parametrizacionPresupuesto;
	
	private Long nivelActual;
	
	private NivelAtencion nivelAtencion;
	
	private String tipoOperacion;
	
	private boolean soloLectura; 
	
	private boolean seleccionListaxYear;
	
	private boolean esModificacionDetalle;
	
	private boolean permiteGuardar;
	
	private int codigoConvenioTemp;
	
	private int codigoContratoTemp;
	
	private String fechaVigenciaTemp;
	
	private MotivosModifiPresupuesto motivoModificacion;
	
	private String observacionesModificacionPresupuesto;
	
	private String fechaInicialLog;
	
	private String fechaFinalLog;
	
	private boolean seleccionLogxFecha;
	
	private boolean esModificacionValorContrato;
	
	private LogParamPresupuestoCap logParametrizacionPresupuesto;
	
	/**
	 * Atributo que almacena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;	
	
	/**
	 * Listado convenios 'Tipo Contrato = Capitado'  'Maneja Presupuesto Capitación = S'.
	 */
	private ArrayList<Convenios> listaConvenios;

	/**
	 * Listado contratos por convenio.
	 */
	private ArrayList<Contratos> listaContratos;
	
	/**
	 *  Listado fechas de vigencias contrato
	 */
	private ArrayList<String> fechasVigencia;
	
	/**
	 *  Listado niveles de atención por contrato
	 */
	private ArrayList<NivelAtencion> nivelesAtencion;

	/**
	 *  Listado niveles de atención asociados a la parametrizacion
	 */
	private ArrayList<NivelAtencion> nivelesAtencionParametrizacion;
	
	/**
	 *  Listado fechas de vigencias contrato
	 */
	private ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> dtoNivelesAtencion;
	
	/**
	 *  Listado fechas de vigencias contrato
	 */
	private DtoLogParametrizacionPresupuesto dtoLogParametrizacionPresupuesto;
	
	/**
	 *  Listado meses vigentes para el contrato seleccionado
	 */
	private ArrayList<String[]> mesesMatriz;
	
	/**
	 *  Listado con la valorización general del presupuesto
	 */
	private ArrayList<ValorizacionPresCapGen> valorizacionGeneral;
	
	/**
	 *  Listado con la valorización general del presupuesto
	 */
	private ArrayList<ValorizacionPresCapGen> valorizacionGeneralTemporal;
	
	/**
	 *  Listado con los valores para servcios o articulos con una nueva
	 *  valorización general del presupuesto
	 */
	private ArrayList<ValorizacionPresCapGen> nuevasValorizacionesGeneral;
	
	/**
	 *  Listado con la valorización para el grupo de servicio del presupuesto
	 */
	private ArrayList<DetalleValorizacionServ> valorizacionDetalleGrupoServicio;
	
	/**
	 *  Listado con la valorización para la clase de inventario del presupuesto
	 */
	private ArrayList<DetalleValorizacionArt> valorizacionDetalleClaseInventario;
	
	/**
	 *  Listado totales parametrización general
	 */
	private ArrayList<Double> totalesParametrizacionGeneral;
	
	/**
	 * Listado totales grupo de servicio de la parametrización general
	 */
	private ArrayList<Double> totalesDetalleGrupoServicio;
	
	/**
	 * Listado totales clase de inventario de la parametrización general
	 */
	private ArrayList<Double> totalesDetalleClaseInventario;
	
	/**
	 * Listado de las parametrizaciones obtenidas en la busqueda para la 
	 * modificación
	 */
	private ArrayList<DtoParamPresupCap> listadoParametrizaciones;
	
	/**
	 * Lista de motivos de modificación de la parametrización
	 */
	private ArrayList<DtoMotivosModifiPresupuesto> listaMotivosModificacion;
	
	/**
	 * lista de logs obtenidos de la busqueda de logs de la parametrización
	 */
	private ArrayList<DtoLogParamPresupCap> listaDtoLogsParametrizacion;
	
	/**
	 * lista de detalles obtenidos para un log de la parametrizción seleccionada
	 */
	private ArrayList<LogDetalleParamPresup> listaDetallesLogParametrizacion;
	

	public ParametrizarPresupuestoCapitacionForm() {
		this.existeParametrizacion = false;
		this.porcentajeGastoMensual = 0D;
		this.mensajeConfirmacionGuardar = fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.guardarCambios");
		this.mensajeMotivoModificacion = fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.motivoModificacionPresupuestoRequerido");
		this.mensajePorcentajesCien = fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.parametrizacionCien");
		this.guardoDatos = false;
		this.soloLectura = false;
		this.esModificacionDetalle = false;
		this.permiteGuardar = false;
		this.seleccionLogxFecha = false;
		this.esModificacionValorContrato = false;
	}
	
	public void reset() {
		this.mesesMatriz = new ArrayList<String[]>();
		this.listaConvenios = new ArrayList<Convenios>();
		this.listaContratos = new ArrayList<Contratos>();
		this.fechasVigencia = new ArrayList<String>();
		this.valorizacionGeneral = new ArrayList<ValorizacionPresCapGen>();
		this.valorizacionDetalleGrupoServicio = new ArrayList<DetalleValorizacionServ>();
		this.valorizacionDetalleClaseInventario = new ArrayList<DetalleValorizacionArt>();
		this.nuevasValorizacionesGeneral = new ArrayList<ValorizacionPresCapGen>();
		this.valorGastoMensual = 0D;
		this.existeParametrizacion = false;
		this.porcentajeGastoMensual = 0D;
		this.convenio = null;
		this.contrato = null;
		this.guardoDatos = false;
		this.parametrizacionPresupuesto = null;
		this.nivelAtencion = null;
		this.soloLectura = false;
		this.permiteGuardar = false;
		this.totalesParametrizacionGeneral = new ArrayList<Double>();
		this.totalesDetalleGrupoServicio = new ArrayList<Double>();
		this.totalesDetalleClaseInventario = new ArrayList<Double>();
		this.dtoLogParametrizacionPresupuesto = new DtoLogParametrizacionPresupuesto();
		this.parametrizacionPresupuesto = new ParamPresupuestosCap();
		this.fechaInicialLog = "";
		this.fechaFinalLog = "";
		this.seleccionLogxFecha = false;
		this.listaMotivosModificacion = new ArrayList<DtoMotivosModifiPresupuesto>();
		this.observacionesModificacionPresupuesto = "";
		this.tipoOperacion = "";
		this.esDescendente = "";
		this.patronOrdenar = "";
	}
	
	public void resetConvenios() {
		this.setConvenioHelper(ConstantesBD.codigoNuncaValido);
		resetContratos();
	}
	
	public void resetContratos() {
		this.porcentajeGastoMensual = 0D;
		this.valorGastoMensual = 0D;
		this.codigoConvenioTemp = ConstantesBD.codigoNuncaValido;
		this.codigoContratoTemp = ConstantesBD.codigoNuncaValido;
		this.fechasVigencia = new ArrayList<String>();
		this.mesesMatriz = new ArrayList<String[]>();
		this.listaContratos = new ArrayList<Contratos>();
		this.parametrizacionPresupuesto = new ParamPresupuestosCap();
		this.valorizacionGeneral = new ArrayList<ValorizacionPresCapGen>();
		this.valorizacionDetalleGrupoServicio = new ArrayList<DetalleValorizacionServ>();
		this.valorizacionDetalleClaseInventario = new ArrayList<DetalleValorizacionArt>();
		this.contrato = null;
		this.guardoDatos = false;
		this.soloLectura = false;
		this.esModificacionValorContrato = false;
		this.parametrizacionPresupuesto = null;
		this.totalesParametrizacionGeneral = new ArrayList<Double>();
		this.totalesDetalleGrupoServicio = new ArrayList<Double>();
		this.totalesDetalleClaseInventario = new ArrayList<Double>();
	}
	
	public void resetFechaVigencia() {
		this.mesesMatriz = new ArrayList<String[]>();
		this.fechasVigencia = new ArrayList<String>();
		this.codigoConvenioTemp = ConstantesBD.codigoNuncaValido;
		this.codigoContratoTemp = ConstantesBD.codigoNuncaValido;
		this.valorizacionGeneral = new ArrayList<ValorizacionPresCapGen>();
		this.valorizacionDetalleGrupoServicio = new ArrayList<DetalleValorizacionServ>();
		this.valorizacionDetalleClaseInventario = new ArrayList<DetalleValorizacionArt>();
		this.vigencia = String.valueOf(ConstantesBD.codigoNuncaValido);
		this.guardoDatos = false;
		this.soloLectura = false;
		this.esModificacionValorContrato = false;
		this.parametrizacionPresupuesto = null;
		this.totalesParametrizacionGeneral = new ArrayList<Double>();
		this.totalesDetalleGrupoServicio = new ArrayList<Double>();
		this.totalesDetalleClaseInventario = new ArrayList<Double>();
	}
	
	public void resetValorizacionGeneral() {
		this.mesesMatriz = new ArrayList<String[]>();
		this.codigoConvenioTemp = ConstantesBD.codigoNuncaValido;
		this.codigoContratoTemp = ConstantesBD.codigoNuncaValido;
		this.valorizacionGeneral = new ArrayList<ValorizacionPresCapGen>();
		this.valorizacionDetalleGrupoServicio = new ArrayList<DetalleValorizacionServ>();
		this.valorizacionDetalleClaseInventario = new ArrayList<DetalleValorizacionArt>();
		this.guardoDatos = false;
		this.seleccionListaxYear = false;
		this.esModificacionDetalle = false;
		this.permiteGuardar = false;
		this.esModificacionValorContrato = false;
		this.parametrizacionPresupuesto = null;
		this.totalesParametrizacionGeneral = new ArrayList<Double>();
		this.totalesDetalleGrupoServicio = new ArrayList<Double>();
		this.totalesDetalleClaseInventario = new ArrayList<Double>();
	}
	
	public void resetDetalles() {
		this.nivelAtencion = null;
		this.codigoConvenioTemp = ConstantesBD.codigoNuncaValido;
		this.codigoContratoTemp = ConstantesBD.codigoNuncaValido;
		this.guardoDatosDetalleClaseInventario = false;
		this.guardoDatosDetalleGrupoServicio = false;
		this.valorizacionDetalleGrupoServicio = new ArrayList<DetalleValorizacionServ>();
		this.valorizacionDetalleClaseInventario = new ArrayList<DetalleValorizacionArt>();
		this.totalesDetalleGrupoServicio = new ArrayList<Double>();
		this.totalesDetalleClaseInventario = new ArrayList<Double>();
	}
	
	public void resetLog() {
		this.convenio = null;
		this.contrato = null;
		this.vigencia = "";
		this.fechasVigencia = new ArrayList<String>();
		this.fechaInicialLog = "";
		this.fechaFinalLog = "";
		this.seleccionLogxFecha = false;
		this.tipoOperacion = "";
		this.listaDtoLogsParametrizacion = new ArrayList<DtoLogParamPresupCap>();
		this.listaDetallesLogParametrizacion = new ArrayList<LogDetalleParamPresup>();
	}
	
	/**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		if (UtilidadTexto.isEmpty(estado)) {
			errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
		} 
		if ("guardarParametrizacionGeneral".equals(this.getEstado())) {
			if (this.getPorcentajeGastoMensual() <= 0 || this.getPorcentajeGastoMensual() > 100 ) {
				errores.add("Porcentaje Gasto Mensual", new ActionMessage("errors.required", 
						fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.general.porcentajeGastoMensual")));
			}
			if (!validarTotales(request, errores , "total_mes_")) {
				errores.add("totales", new ActionMessage("errors.required", 
						fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.totalInvalido")));
			} 
			if(this.getConvenio() != null && this.getContrato() != null) {
				this.setConvenioHelper(codigoConvenioTemp);
				this.setContratoHelper(codigoContratoTemp);
				this.setVigenciaHelper(fechaVigenciaTemp);
				double valorGasto = (this.getContrato().getValor() * this.getPorcentajeGastoMensual()) / 100D;
				BigDecimal valorGastoMensual = new BigDecimal(valorGasto);
				this.setValorGastoMensual(valorGastoMensual.doubleValue());
				for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : this.getDtoNivelesAtencion()) {
					NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
					int i = 0;
					for (String[] mesMatriz : this.getMesesMatriz()) {
						if ("Activo".equals(mesMatriz[2])) {
							if (dtoNivelesAtencion.isExistenServicios()) {
								String porcentajeServicioTemp = request.getParameter(mesMatriz[0] + "_servicio_"+nivel.getConsecutivo()+"_");
								BigDecimal porcentajeServicio = 
									(porcentajeServicioTemp != null && !"".equals(porcentajeServicioTemp)) ? 
											new BigDecimal(porcentajeServicioTemp) : new BigDecimal(0);
											ValorizacionPresCapGen valor = new ValorizacionPresCapGen();
											valor.setMes(i);
											valor.setNivelAtencion(nivel);
											valor.setSubSeccion(ConstantesCapitacion.subSeccionServicio);
											valor.setPorcentajeGastoSubSeccion(porcentajeServicio);
											this.valorizacionGeneral.add(valor);
							}
							if (dtoNivelesAtencion.isExistenArticulos()) {
								String porcentajeArticuloTemp = request.getParameter(mesMatriz[0] + "_med_"+nivel.getConsecutivo()+"_");
								BigDecimal porcentajeArticulo = 
									(porcentajeArticuloTemp != null && !"".equals(porcentajeArticuloTemp)) ? 
											new BigDecimal(porcentajeArticuloTemp) : new BigDecimal(0);		
											ValorizacionPresCapGen valor = new ValorizacionPresCapGen();
											valor.setMes(i);
											valor.setNivelAtencion(nivel);
											valor.setSubSeccion(ConstantesCapitacion.subSeccionArticulo);
											valor.setPorcentajeGastoSubSeccion(porcentajeArticulo);
											this.valorizacionGeneral.add(valor);
							}
						}
						i++;
					}
				}
			} else {
				this.setConvenioHelper(codigoConvenioTemp);
				this.setContratoHelper(codigoContratoTemp);
				this.setVigenciaHelper(fechaVigenciaTemp);
			}
		}
		return errores;
	}	
	
	/**
	 * Valida los totales ingresados en la forma 
	 *  
	 * @param request
	 * @param errores
	 * @param nombreTotal
	 * @return
	 */
	public boolean validarTotales(HttpServletRequest request, ActionErrors errores, String nombreTotal) {
		int i = 0;
		double total = 0;
		boolean resultado = true;
		this.totalesParametrizacionGeneral = new ArrayList<Double>();
		for (String[] meses : this.getMesesMatriz()) {
			if ("Activo".equals(meses[2])) {
				String totalMes = request.getParameter(nombreTotal+i);
				if (totalMes != null && !"".equals(totalMes)) {
					total = Double.parseDouble(totalMes);
					if (total != 100D) {
						resultado = false;
					}
				} else {
					resultado = false;
				}
			}
			this.totalesParametrizacionGeneral.add(total);
			total = 0;
			i++;
		}
		return resultado;
	}
	
	public ArrayList<Contratos> getListaContratos() {
		if (listaContratos != null) {
			this.setNumContratos(listaContratos.size());
		}
		return listaContratos;
	}

	public void setListaContratos(ArrayList<Contratos> listaContratos) {
		this.listaContratos = listaContratos;
	}
	
	public ArrayList<Convenios> getListaConvenios() {
		return listaConvenios;
	}

	public void setListaConvenios(ArrayList<Convenios> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the convenio
	 */
	public Convenios getConvenio() {
		return convenio;
	}	
	
	/**
	 * @param set c&oacute;digo convenio
	 */
	public void setConvenioHelper(int idCodigo) {
		boolean asigno = false;

		for (Convenios convenio : getListaConvenios()) {
			if (convenio.getCodigo() == idCodigo) {
				asigno = true;
				this.setConvenio(convenio);
			}
		}
		if (!asigno) {
			this.setConvenio(null);
		}
	}

	/**
	 * @return  codigo del contrato
	 */
	public int getConvenioHelper() {
		if (convenio == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return convenio.getCodigo();
		
	}	
	
	/**
	 * @param set c&oacute;digo contrato
	 */
	public void setContratoHelper(int idCodigo) {
		boolean asigno = false;
		if (this.getListaContratos() != null) {
			for (Contratos contrato : getListaContratos()) {
				if (contrato.getCodigo() == idCodigo) {
					asigno = true;
					this.setContrato(contrato);
				}
			}
			if (!asigno) {
				this.setContrato(null);
			}
		}
	}

	/**
	 * @return  codigo del convenio
	 */
	public int getContratoHelper() {
		if (contrato == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return contrato.getCodigo();
		
	}	

	/**
	 * @param set NivelAtencion actual
	 */
	public void setNivelAtencionHelper(long idConsecutivo) {
		boolean asigno = false;
		if (this.getNivelesAtencion() != null) {
			for (NivelAtencion nivelAtencion : this.getNivelesAtencion()) {
				if (nivelAtencion.getConsecutivo() == idConsecutivo) {
					asigno = true;
					this.setNivelAtencion(nivelAtencion);
				}
			}
			if (!asigno) {
				this.setNivelAtencion(null);
			}
		}
	}

	/**
	 * @return long consecutivo NivelAtencion actual
	 */
	public long getNivelAtencionHelper() {
		if (this.nivelAtencion == null) {
			return ConstantesBD.codigoNuncaValidoLong;
		}
		return nivelAtencion.getConsecutivo();
		
	}	
	
	public NivelAtencion getNivelAtencion() {
		return nivelAtencion;
	}

	public void setNivelAtencion(NivelAtencion nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(Convenios convenio) {
		this.convenio = convenio;
	}

	public Contratos getContrato() {
		return contrato;
	}

	public void setContrato(Contratos contrato) {
		this.contrato = contrato;
	}
	
	public ArrayList<String> getFechasVigencia() {
		return fechasVigencia;
	}

	public void setFechasVigencia(ArrayList<String> fechasVigencia) {
		this.fechasVigencia = fechasVigencia;
	}

	/**
	 * @param set Año vigencia contrato
	 */
	public void setVigenciaHelper(String idVigencia) {
		boolean asigno = false;
		if (this.getFechasVigencia() != null) {
			for (String vigencia : getFechasVigencia()) {
				if (vigencia.equals(idVigencia)) {
					asigno = true;
					this.setVigencia(vigencia);
				}
			}
			if (!asigno) {
				this.setVigencia(""+ConstantesBD.codigoNuncaValido);
			}
		}
	}
	
	/**
	 * @return Año vigencia contrato
	 */
	public String getVigenciaHelper() {
		if (vigencia == null) {
			return ""+ConstantesBD.codigoNuncaValido;
		}
		return vigencia;
		
	}	
	
	public String getVigencia() {
		return vigencia;
	}

	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	public int getNumContratos() {
		return numContratos;
	}

	public void setNumContratos(int numContratos) {
		this.numContratos = numContratos;
	}

	public double getValorGastoMensual() {
		return valorGastoMensual;
	}

	public void setValorGastoMensual(double valorGastoMensual) {
		this.valorGastoMensual = valorGastoMensual;
	}

	public double getPorcentajeGastoMensual() {
		return porcentajeGastoMensual;
	}

	public void setPorcentajeGastoMensual(double porcentajeGastoMensual) {
		this.porcentajeGastoMensual = porcentajeGastoMensual;
	}

	public ArrayList<String[]> getMesesMatriz() {
		return mesesMatriz;
	}

	public void setMesesMatriz(ArrayList<String[]> mesesMatriz) {
		this.mesesMatriz = mesesMatriz;
	}

	public ArrayList<NivelAtencion> getNivelesAtencion() {
		return nivelesAtencion;
	}

	public void setNivelesAtencion(ArrayList<NivelAtencion> nivelesAtencion) {
		this.nivelesAtencion = nivelesAtencion;
	}

	public ArrayList<NivelAtencion> getNivelesAtencionParametrizacion() {
		return nivelesAtencionParametrizacion;
	}

	public void setNivelesAtencionParametrizacion(
			ArrayList<NivelAtencion> nivelesAtencionParametrizacion) {
		this.nivelesAtencionParametrizacion = nivelesAtencionParametrizacion;
	}

	public boolean isExisteParametrizacion() {
		return existeParametrizacion;
	}

	public void setExisteParametrizacion(boolean existeParametrizacion) {
		this.existeParametrizacion = existeParametrizacion;
	}

	public String getMensajeConfirmacionGuardar() {
		return mensajeConfirmacionGuardar;
	}

	public void setMensajeConfirmacionGuardar(String mensajeConfirmacionGuardar) {
		this.mensajeConfirmacionGuardar = mensajeConfirmacionGuardar;
	}

	public String getMensajeMotivoModificacion() {
		return mensajeMotivoModificacion;
	}

	public void setMensajeMotivoModificacion(String mensajeMotivoModificacion) {
		this.mensajeMotivoModificacion = mensajeMotivoModificacion;
	}

	public ArrayList<ValorizacionPresCapGen> getValorizacionGeneral() {
		return valorizacionGeneral;
	}

	public void setValorizacionGeneral(
			ArrayList<ValorizacionPresCapGen> valorizacionGeneral) {
		this.valorizacionGeneral = valorizacionGeneral;
	}

	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> getDtoNivelesAtencion() {
		return dtoNivelesAtencion;
	}

	public void setDtoNivelesAtencion(
			ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> dtoNivelesAtencion) {
		this.dtoNivelesAtencion = dtoNivelesAtencion;
	}

	public boolean isGuardoDatos() {
		return guardoDatos;
	}

	public void setGuardoDatos(boolean guardoDatos) {
		this.guardoDatos = guardoDatos;
	}

	public ParamPresupuestosCap getParametrizacionPresupuesto() {
		return parametrizacionPresupuesto;
	}

	public void setParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		this.parametrizacionPresupuesto = parametrizacionPresupuesto;
	}

	public ArrayList<DetalleValorizacionServ> getValorizacionDetalleGrupoServicio() {
		return valorizacionDetalleGrupoServicio;
	}

	public void setValorizacionDetalleGrupoServicio(
			ArrayList<DetalleValorizacionServ> valorizacionDetalleGrupoServicio) {
		this.valorizacionDetalleGrupoServicio = valorizacionDetalleGrupoServicio;
	}

	public ArrayList<DetalleValorizacionArt> getValorizacionDetalleClaseInventario() {
		return valorizacionDetalleClaseInventario;
	}

	public void setValorizacionDetalleClaseInventario(
			ArrayList<DetalleValorizacionArt> valorizacionDetalleClaseInventario) {
		this.valorizacionDetalleClaseInventario = valorizacionDetalleClaseInventario;
	}

	public void setNivelActual(Long nivelActual) {
		this.nivelActual = nivelActual;
	}

	public Long getNivelActual() {
		return nivelActual;
	}

	public boolean isGuardoDatosDetalleGrupoServicio() {
		return guardoDatosDetalleGrupoServicio;
	}

	public void setGuardoDatosDetalleGrupoServicio(
			boolean guardoDatosDetalleGrupoServicio) {
		this.guardoDatosDetalleGrupoServicio = guardoDatosDetalleGrupoServicio;
	}

	public boolean isGuardoDatosDetalleClaseInventario() {
		return guardoDatosDetalleClaseInventario;
	}

	public void setGuardoDatosDetalleClaseInventario(
			boolean guardoDatosDetalleClaseInventario) {
		this.guardoDatosDetalleClaseInventario = guardoDatosDetalleClaseInventario;
	}

	public ArrayList<Double> getTotalesParametrizacionGeneral() {
		return totalesParametrizacionGeneral;
	}

	public void setTotalesParametrizacionGeneral(
			ArrayList<Double> totalesParametrizacionGeneral) {
		this.totalesParametrizacionGeneral = totalesParametrizacionGeneral;
	}

	public ArrayList<Double> getTotalesDetalleGrupoServicio() {
		return totalesDetalleGrupoServicio;
	}

	public void setTotalesDetalleGrupoServicio(
			ArrayList<Double> totalesDetalleGrupoServicio) {
		this.totalesDetalleGrupoServicio = totalesDetalleGrupoServicio;
	}

	public ArrayList<Double> getTotalesDetalleClaseInventario() {
		return totalesDetalleClaseInventario;
	}

	public void setTotalesDetalleClaseInventario(
			ArrayList<Double> totalesDetalleClaseInventario) {
		this.totalesDetalleClaseInventario = totalesDetalleClaseInventario;
	}

	public boolean isExisteParametrizacionDetalleGrupoServicio() {
		return existeParametrizacionDetalleGrupoServicio;
	}

	public void setExisteParametrizacionDetalleGrupoServicio(
			boolean existeParametrizacionDetalleGrupoServicio) {
		this.existeParametrizacionDetalleGrupoServicio = existeParametrizacionDetalleGrupoServicio;
	}

	public boolean isExisteParametrizacionDetalleClaseInventario() {
		return existeParametrizacionDetalleClaseInventario;
	}

	public void setExisteParametrizacionDetalleClaseInventario(
			boolean existeParametrizacionDetalleClaseInventario) {
		this.existeParametrizacionDetalleClaseInventario = existeParametrizacionDetalleClaseInventario;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public boolean isSoloLectura() {
		return soloLectura;
	}

	public void setSoloLectura(boolean soloLectura) {
		this.soloLectura = soloLectura;
	}

	public ArrayList<DtoParamPresupCap> getListadoParametrizaciones() {
		return listadoParametrizaciones;
	}

	public void setListadoParametrizaciones(
			ArrayList<DtoParamPresupCap> listadoParametrizaciones) {
		this.listadoParametrizaciones = listadoParametrizaciones;
	}

	public boolean isSeleccionListaxYear() {
		return seleccionListaxYear;
	}

	public void setSeleccionListaxYear(boolean seleccionListaxYear) {
		this.seleccionListaxYear = seleccionListaxYear;
	}

	public boolean isEsModificacionDetalle() {
		return esModificacionDetalle;
	}

	public void setEsModificacionDetalle(boolean esModificacionDetalle) {
		this.esModificacionDetalle = esModificacionDetalle;
	}

	public MotivosModifiPresupuesto getMotivoModificacion() {
		return motivoModificacion;
	}

	public void setMotivoModificacion(MotivosModifiPresupuesto motivoModificacion) {
		this.motivoModificacion = motivoModificacion;
	}

	/**
	 * @param set c&oacute;digo Motivo Modificación 
	 */
	public void setMotivoModificacionHelper(long idCodigo) {
		boolean asigno = false;

		for (MotivosModifiPresupuesto motivo : getListaMotivosModificacion()) {
			if (motivo.getCodigoPk() == idCodigo) {
				asigno = true;
				this.setMotivoModificacion(motivo);
			}
		}
		if (!asigno) {
			this.setMotivoModificacion(null);
		}
	}

	/**
	 * @return  codigo del motivo de modificación
	 */
	public long getMotivoModificacionHelper() {
		if (this.motivoModificacion == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return motivoModificacion.getCodigoPk();
		
	}

	public ArrayList<DtoMotivosModifiPresupuesto> getListaMotivosModificacion() {
		return listaMotivosModificacion;
	}

	public void setListaMotivosModificacion(
			ArrayList<DtoMotivosModifiPresupuesto> listaMotivosModificacion) {
		this.listaMotivosModificacion = listaMotivosModificacion;
	}

	public String getObservacionesModificacionPresupuesto() {
		return observacionesModificacionPresupuesto;
	}

	public void setObservacionesModificacionPresupuesto(
			String observacionesModificacionPresupuesto) {
		this.observacionesModificacionPresupuesto = observacionesModificacionPresupuesto;
	}

	public boolean isPermiteGuardar() {
		return permiteGuardar;
	}

	public void setPermiteGuardar(boolean permiteGuardar) {
		this.permiteGuardar = permiteGuardar;
	}

	public ArrayList<ValorizacionPresCapGen> getValorizacionGeneralTemporal() {
		return valorizacionGeneralTemporal;
	}

	public void setValorizacionGeneralTemporal(
			ArrayList<ValorizacionPresCapGen> valorizacionGeneralTemporal) {
		this.valorizacionGeneralTemporal = valorizacionGeneralTemporal;
	}

	public DtoLogParametrizacionPresupuesto getDtoLogParametrizacionPresupuesto() {
		return dtoLogParametrizacionPresupuesto;
	}

	public void setDtoLogParametrizacionPresupuesto(
			DtoLogParametrizacionPresupuesto dtoLogParametrizacionPresupuesto) {
		this.dtoLogParametrizacionPresupuesto = dtoLogParametrizacionPresupuesto;
	}

	public String getMensajePorcentajesCien() {
		return mensajePorcentajesCien;
	}

	public void setMensajePorcentajesCien(String mensajePorcentajesCien) {
		this.mensajePorcentajesCien = mensajePorcentajesCien;
	}

	public String getFechaInicialLog() {
		return fechaInicialLog;
	}

	public void setFechaInicialLog(String fechaInicialLog) {
		this.fechaInicialLog = fechaInicialLog;
	}

	public String getFechaFinalLog() {
		return fechaFinalLog;
	}

	public void setFechaFinalLog(String fechaFinalLog) {
		this.fechaFinalLog = fechaFinalLog;
	}

	public ArrayList<DtoLogParamPresupCap> getListaDtoLogsParametrizacion() {
		return listaDtoLogsParametrizacion;
	}

	public void setListaDtoLogsParametrizacion(
			ArrayList<DtoLogParamPresupCap> listaDtoLogsParametrizacion) {
		this.listaDtoLogsParametrizacion = listaDtoLogsParametrizacion;
	}

	public ArrayList<LogDetalleParamPresup> getListaDetallesLogParametrizacion() {
		return listaDetallesLogParametrizacion;
	}

	public void setListaDetallesLogParametrizacion(
			ArrayList<LogDetalleParamPresup> listaDetallesLogParametrizacion) {
		this.listaDetallesLogParametrizacion = listaDetallesLogParametrizacion;
	}

	public boolean isSeleccionLogxFecha() {
		return seleccionLogxFecha;
	}

	public void setSeleccionLogxFecha(boolean seleccionLogxFecha) {
		this.seleccionLogxFecha = seleccionLogxFecha;
	}

	public LogParamPresupuestoCap getLogParametrizacionPresupuesto() {
		return logParametrizacionPresupuesto;
	}

	public void setLogParametrizacionPresupuesto(
			LogParamPresupuestoCap logParametrizacionPresupuesto) {
		this.logParametrizacionPresupuesto = logParametrizacionPresupuesto;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public int getCodigoConvenioTemp() {
		return codigoConvenioTemp;
	}

	public void setCodigoConvenioTemp(int codigoConvenioTemp) {
		this.codigoConvenioTemp = codigoConvenioTemp;
	}

	public int getCodigoContratoTemp() {
		return codigoContratoTemp;
	}

	public void setCodigoContratoTemp(int codigoContratoTemp) {
		this.codigoContratoTemp = codigoContratoTemp;
	}

	public String getFechaVigenciaTemp() {
		return fechaVigenciaTemp;
	}

	public void setFechaVigenciaTemp(String fechaVigenciaTemp) {
		this.fechaVigenciaTemp = fechaVigenciaTemp;
	}

	public boolean isEsModificacionValorContrato() {
		return esModificacionValorContrato;
	}

	public void setEsModificacionValorContrato(boolean esModificacionValorContrato) {
		this.esModificacionValorContrato = esModificacionValorContrato;
	}

	public ArrayList<ValorizacionPresCapGen> getNuevasValorizacionesGeneral() {
		return nuevasValorizacionesGeneral;
	}

	public void setNuevasValorizacionesGeneral(
			ArrayList<ValorizacionPresCapGen> nuevasValorizacionesGeneral) {
		this.nuevasValorizacionesGeneral = nuevasValorizacionesGeneral;
	}	

}
