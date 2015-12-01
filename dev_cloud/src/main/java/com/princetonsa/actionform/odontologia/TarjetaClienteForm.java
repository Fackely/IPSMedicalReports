package com.princetonsa.actionform.odontologia;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadTexto;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.mundo.odontologia.EmisionBonosDesc;
import com.princetonsa.mundo.odontologia.TarjetaCliente;

/**
 * 
 * @author Edgar Carvajal
 *
 */
public class TarjetaClienteForm  extends ValidatorForm 
{
	
	/**
	 * Atribuo para cargar cual es el codigo tarifario de la institucion
	 * Se Utiliza en presentacion grafica
	 */
	private String codigoTarifario;
	
	/**
	 * Ayundante para cargar la el nombre de codigo Tarifario.
	 * Solamente Es utilizado en interfaz Grafica
	 */
	private String nombreTarifa;
	
	
 	/**
	 * Atributo para cargar la lista de tarjetas
	 */
	private ArrayList<DtoTarjetaCliente> listTarjetasCliente = new ArrayList<DtoTarjetaCliente>();
	
	
	/**
	 * Atributo para verificar si es modificabel un consecutivo serial
	 */
	private boolean esModificableConsecutivoSerial;
	
	
	/**
	 *Resultado de Busqueda
	 *ArrayList para mostrar el resultado de la busqueda 
	 */
	private ArrayList<DtoTarjetaCliente> resultadoBusqueda = new ArrayList<DtoTarjetaCliente>();
	
	/**
	 * Dto Tarjeta
	 * Objeto para almacenar temporalmente el objeto que se quiere guardar
	 */
	private  DtoTarjetaCliente tarjetaCliente;	
    
	/**
     * Atributo para manejar el estado de la pagina
     */
	private String estado;
	
	/**
	 * Atributo para manejar la posicion de la lista de tarjeta Cliente
	 */
	private int posArray;
	
	/**
	 * Atributo para cargar el criterio de busqueda 
	 */
	private String criterioBusqueda;
	
	/**
	 *Atributo para almacenar temporalmente el patron de busqueda 
	 */
	private String patronOrdenar;
  
	/**
	 * Arreglo para almacenar los Convenios
	 */
	private ArrayList<HashMap<String, Object>> listConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Reset
	 * Metodo que limpia los objetos 
	 */
	public void reset()
	{
    	this.tarjetaCliente= new DtoTarjetaCliente();
		this.listTarjetasCliente = new ArrayList<DtoTarjetaCliente>();
		this.resultadoBusqueda= new ArrayList<DtoTarjetaCliente>();
		this.posArray = ConstantesBD.codigoNuncaValido;
		this.criterioBusqueda="";
		this.patronOrdenar="";
		this.esModificableConsecutivoSerial=Boolean.FALSE; 
		this.codigoTarifario="";
		this.nombreTarifa="";
		if(this.estado.equals("empezar"))
			this.listConvenios= new ArrayList<HashMap<String,Object>>();
	}

	/**
	 * Metodo de validacion de la funcionalidad venta tarjeta Cliente
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		
		if(this.getEstado().equals("guardar") || this.getEstado().equals("guardarModificar"))
	 	{
			validarCodigo(errores);
			validarNombre(errores);
			validarConvenio(errores);
			validarNumeroBeneficiarios(errores);
			validarAliado(errores);
			validarConvenioExisten(errores);
		
			if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardar"))
					this.estado="mostrarErrores";
				if(this.getEstado().equals("guardarModificar"))
					this.estado="mostrarErroresModificar";
			}
	 	}
		
		
		return errores;
	}
	
	/**
	 * 
	 * @param errores
	 * @return
	 */

	
	
	/**
	 * Metodo para validar aliado odontologico
	 * @param errores
	 */
	private void validarAliado(ActionErrors errores) 
	{
		//hablar con wilson sobre este metodo
		if(this.getTarjetaCliente().getAliado().equals(ConstantesBD.acronimoSi))
		{
			this.getTarjetaCliente().getServicioEmpresarial().setCodigo(0);
			this.getTarjetaCliente().getServicioFamiliar().setCodigo(0);
			this.getTarjetaCliente().getServicioPersonal().setCodigo(0);
			this.getTarjetaCliente().setNumBeneficiariosFam(0);
			
		}
	}

	/**
	 * Validar Codigo
	 * @param errores
	 */
	private void validarCodigo(ActionErrors errores) 
	{
		if(UtilidadTexto.isEmpty(this.getTarjetaCliente().getCodigoTipoTarj().trim()))
		{	
			errores.add("", new ActionMessage("errors.required", "El código"));
		}
		else
		{
			if(this.getEstado().equals("guardar"))
			{	
				validarExistenciaCodigo(errores);
			}
			else if(this.getEstado().equals("guardarModificar"))
			{
			
				if(this.getListTarjetasCliente().get(this.getPosArray()).getCodigoTipoTarj()!=this.getTarjetaCliente().getCodigoTipoTarj())
				{
				///ojo no entiendo el error
					validarExistenciaCodigo(errores);
				}
			}
		}	
	}
	
	
	/**
	 * Valida convenio Existe
	 * @param errores
	 */
	private void validarConvenioExisten (ActionErrors errores) 
	{
		//logger.info("");
		DtoTarjetaCliente dto = new DtoTarjetaCliente();
		dto.getConvenio().setCodigo(this.getTarjetaCliente().getConvenio().getCodigo());
		if(this.getEstado().equals("guardarModificar"))
		{
			dto.setCodigoPk(this.getTarjetaCliente().getCodigoPk());
			
			if(TarjetaCliente.cargarConvenio(dto, true).size()>0)
			{
				errores.add("", new ActionMessage("errors.yaExisteAmplio", "El convenio solo puede ser asociado por un tipo de tarjeta,", ". Por favor verifique"));
			}
		}
		else if(this.getEstado().equals("guardar"))
		{
			if(TarjetaCliente.cargarConvenio(dto, false).size()>0)
			{
				errores.add("", new ActionMessage("errors.yaExisteAmplio", "El convenio solo puede ser asociado por un tipo de tarjeta,", ". Por favor verifique"));
			}
		} 
			
	}

	/**
	 * Validar Existencia de codigo ya persistido en el sistema
	 * @param errores
	 */
	private void validarExistenciaCodigo(ActionErrors errores) 
	{
		DtoTarjetaCliente dto= new DtoTarjetaCliente();
		dto.setCodigoTipoTarj(this.getTarjetaCliente().getCodigoTipoTarj());
		dto.setInstitucion(this.getTarjetaCliente().getInstitucion());
		
		if(TarjetaCliente.cargar(dto).size()>0 )
		{	
			errores.add("", new ActionMessage("errors.yaExisteAmplio", "El código "+dto.getCodigoTipoTarj(), ". Por favor verifique"));
		}
	}
	
	/**
	 * Validar nombre de la tarjeta 
	 * @param errores
	 */
	private void validarNombre(ActionErrors errores) 
	{
		if(UtilidadTexto.isEmpty(this.getTarjetaCliente().getNombre().trim()))
		{	
			errores.add("", new ActionMessage("errors.required", "El nombre"));
		}
	}
	
	/**
	 * Validar Convenio
	 * El convenio de la tarjeta es requerido
	 * @param errores
	 */
	private void validarConvenio(ActionErrors errores) 
	{
		if(this.getTarjetaCliente().getConvenio().getCodigo()<1)
		{
			errores.add("", new ActionMessage("errors.required", "El convenio"));
		}
	}
	
	/**
	 * Validar numero de Beneficiarios
	 * @param errores
	 */
	private void validarNumeroBeneficiarios(ActionErrors errores) 
	{
		if (getTarjetaCliente().getServicioFamiliar().getCodigo()>0)
		{
			if(this.getTarjetaCliente().getNumBeneficiariosFam()<2)
			{
			    errores.add("", new ActionMessage("errors.integerMayorQue", "Número de Beneficiarios", "1"));
			}
			if(this.getTarjetaCliente().getNumBeneficiariosFam()>20)
			{
				 errores.add("", new ActionMessage("errors.integerMenorIgualQue", "Número de Beneficiarios", "20"));
			}
		}
	}

	/**
	 * @return the listConvenios
	 */
	public ArrayList<HashMap<String, Object>> getListConvenios() {
		return listConvenios;
	}

	/**
	 * @param listConvenios the listConvenios to set
	 */
	public void setListConvenios(ArrayList<HashMap<String, Object>> listConvenios) {
		this.listConvenios = listConvenios;
	}

	/**
	 * @return the listTarjetasCliente
	 */
	public ArrayList<DtoTarjetaCliente> getListTarjetasCliente() {
		return listTarjetasCliente;
	}

	/**
	 * @param listTarjetasCliente the listTarjetasCliente to set
	 */
	public void setListTarjetasCliente(
			ArrayList<DtoTarjetaCliente> listTarjetasCliente) {
		this.listTarjetasCliente = listTarjetasCliente;
	}

	/**
	 * @return the resultadoBusqueda
	 */
	public ArrayList<DtoTarjetaCliente> getResultadoBusqueda() {
		return resultadoBusqueda;
	}

	/**
	 * @param resultadoBusqueda the resultadoBusqueda to set
	 */
	public void setResultadoBusqueda(ArrayList<DtoTarjetaCliente> resultadoBusqueda) {
		this.resultadoBusqueda = resultadoBusqueda;
	}

	/**
	 * @return the tarjetaCliente
	 */
	public DtoTarjetaCliente getTarjetaCliente() {
		return tarjetaCliente;
	}

	/**
	 * @param tarjetaCliente the tarjetaCliente to set
	 */
	public void setTarjetaCliente(DtoTarjetaCliente tarjetaCliente) {
		this.tarjetaCliente = tarjetaCliente;
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
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public void setEsModificableConsecutivoSerial(
			boolean esModificableConsecutivoSerial) {
		this.esModificableConsecutivoSerial = esModificableConsecutivoSerial;
	}

	public boolean isEsModificableConsecutivoSerial() {
		return esModificableConsecutivoSerial;
	}

	public void setCodigoTarifario(String codigoTarifario) {
		this.codigoTarifario = codigoTarifario;
	}

	public String getCodigoTarifario() {
		return codigoTarifario;
	}

	public void setNombreTarifa(String nombreTarifa) {
		this.nombreTarifa = nombreTarifa;
	}

	public String getNombreTarifa() {
		return nombreTarifa;
	}
	
}