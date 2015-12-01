package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorActionForm;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoCuotasOdontologicasEspecialidad;
import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.princetonsa.enums.odontologia.ETiposCuota;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.Especialidades;

/**
 * 
 * @author Edgar Carvajal
 *
 */
public class CuotaOdonEspecialidadForm  extends ValidatorActionForm

{

	/**
	 * ATRIBUTO PARA MANAJERA EL ESTADO DEL LA PAGINA
	 */
	private String estado;
	
	
	
	
	
	/**
	 * DTO COUTAS ODONTOLOGICAS
	 */
	private CuotasOdontEspecialidad dtoCuota;
	
	
	/**
	 * LISTA DE ESPECIALIDADES 
	 */
	private List<Especialidades> listaEspecialidades;
	
	/**
	 * ENTIDAD ESPECIALIDAD 
	 */
	private Especialidades dtoEspecialidad;
	
	
	/**
	 * AYUNDATE DE PRESENTACION PARA SETTEAR EL TIPO DE CUOTA
	 */
	private String tipoCouta;

	/**
	 * AYUDANTE PARA CARGAR EL NOMBRE TIPO CUOTA
	 */
	private String nombreTipoCuota;
	
	/**
	 * AYUDANTE PARA MOSTRAR EN PRESENTACION EL NOMBRE DE LA ESPECIALIDAD
	 */
	private String nombreEspecialidad;
	
	
	/**
	 * DETALLE TIPO CUOTA 
	 */
	private String detTipoCouta;
	
	
	/**
	 *POST ARRAY DE TIPOS DE ESPECIALIDAD 
	 */
	private int postArrayLisEspecialidad;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * ATRIBUTO PARA LA VALIDACION GRAFICA DE LA VISTA
	 * SIRVE PARA IDENTIFICA SI EXISTE INFORMACION PREVIA DE LA ESPECIALIDAD 
	 */
	private Boolean existeEspecialidad;
	
	/**
	 * ATRIBUTO PARA VALIDAR SI EL DETALLE ES BORRADO
	 */
	private String borraDetalle;
	
	
	/**
	 * LISTA PARA DETALLE ESPECIALIDAD 
	 * UTILIZADO PARA LA PRESENTACION GRAFICA DEL SISTEMA 
	 */
	private ArrayList<DtoDetalleCuotasOdontoEsp> listaDetallesEspecialidad  = new ArrayList<DtoDetalleCuotasOdontoEsp>();

	
	
	
	
	
	/**
	 * DTO PARA ARMAR EL LOG
	 */
	private DtoCuotasOdontologicasEspecialidad dtoLogCuotas;
	
	/**
	 * DTO  2 PARA ARMAR LOG
	 */
	private DtoCuotasOdontologicasEspecialidad dtoLogCuotas2;
	
	
	

	/**
	 * 	TAMANIO LISTA 
	 */
	private int tamaniolista;
	
	
	
	
	/**
	 * CONTADOR DE LISTA
	 * ATRIBUTO PARA VALIDAR SI LA LISTA ES MAYOR 24 REGISTRO
	 */
	private boolean tamanoListaMayo24;
	
	/**
	 * RESET
	 * @author Edgar Carvajal Ruiz
	 */
	public void reset()
	{
		this.estado="";
		this.dtoCuota=new CuotasOdontEspecialidad();
		this.setListaEspecialidades(new ArrayList<Especialidades>());
		
		this.postArrayLisEspecialidad=ConstantesBD.codigoNuncaValido;
		this.setListaDetallesEspecialidad(new ArrayList<DtoDetalleCuotasOdontoEsp>());
		this.dtoEspecialidad = new Especialidades();
		this.tipoCouta="";
		this.detTipoCouta="";
		
		this.nombreTipoCuota="";
		this.nombreEspecialidad="";
		this.existeEspecialidad=Boolean.FALSE;
		this.borraDetalle=ConstantesBD.acronimoNo;
		this.setTamanoListaMayo24(Boolean.FALSE);
		
		this.dtoLogCuotas= new DtoCuotasOdontologicasEspecialidad();
		this.setDtoLogCuotas2(new DtoCuotasOdontologicasEspecialidad());
		this.setTamaniolista(ConstantesBD.codigoNuncaValido);
	}





	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		
		
		if(this.estado.equals("guardarEncabezado"))
		{
			if( this.dtoEspecialidad.getCodigo()<=0 )
			{
				errores.add("",	new ActionMessage("errors.notEspecific","La Especialidad es Requerida"));
			}
			
			if(UtilidadTexto.isEmpty(this.tipoCouta))
			{
				errores.add("",	new ActionMessage("errors.notEspecific","Tipo de Valor es Requerido"));
			}
			
			if(!errores.isEmpty())
			{
				this.setEstado("empezar");
			}
		}
		
		
		
		if(this.estado.equals("guardarDetalle"))
		{
		
			float total=0f;
			
			if( this.dtoCuota.getTipoValor().equals(ETiposCuota.PORC+"") )
			{
				for(DtoDetalleCuotasOdontoEsp dto: this.listaDetallesEspecialidad )
				{
					
					if( dto.getActivo() )
					{
						
						
						
						if(dto.getPorcentaje().intValue()==0)
						{
							errores.add("",	new ActionMessage("errors.notEspecific","El  porcentaje deber ser mayor que cero "));
							this.setEstado("empezarDetalle");
							return errores;
						}
						
						
						
						
						total+=dto.getPorcentaje().floatValue();
						
						
					
					
					
						if( UtilidadTexto.isEmpty(dto.getAyudantePorcentaje()) )
						{
							errores.add("",	new ActionMessage("errors.notEspecific","El  Porcentaje es Requerido "));
							this.setEstado("empezarDetalle");
							return errores;
						}
					}
					
					
					
				}
				
				
				if(total!=100)
				{
					errores.add("",	new ActionMessage("errors.notEspecific","La Sumatoria de Porcentajes no suma el 100%.Por Favor Verfique "));
					this.setEstado("empezarDetalle");
				}
				
			}
			else if(this.dtoCuota.getTipoValor().equals(ETiposCuota.VALOR+""))
			{
				
				for(DtoDetalleCuotasOdontoEsp dto: this.listaDetallesEspecialidad )
				{
					if( dto.getActivo() )
					{
						
						if(dto.getValor().intValue()==0)
						{

							errores.add("",	new ActionMessage("errors.notEspecific","El Valor deber ser mayor que cero "));
							this.setEstado("empezarDetalle");
							return errores;
						}
						
						
						if(UtilidadTexto.isEmpty(dto.getAyudanteValor()))
						{
							errores.add("",	new ActionMessage("errors.notEspecific"," El Valor es Requerido"));
							this.setEstado("empezarDetalle");
							return errores;
							
						}
					}
				
					
				}
			}
			
			
		
			
			/*
			 *VALIDACION DE NRO CUOTAS Y TIPOS DE CUOTAS 
			 */
			
			for(DtoDetalleCuotasOdontoEsp dto: this.listaDetallesEspecialidad )
			{
			
				if( UtilidadTexto.isEmpty(dto.getTipoCuota()) )
				{
					errores.add("",	new ActionMessage("errors.notEspecific"," El tipo de Cuota es Requerido"));
					this.setEstado("empezarDetalle");
					return errores;
				}
			
				if(dto.getNroCuotas()==0)
				{
					errores.add("",	new ActionMessage("errors.notEspecific"," El número de cuotas debe ser mayor que cero"));
					this.setEstado("empezarDetalle");
					
				}
				
				
			}
			
			
		}//FIN DE GUARDAR DETALLE
		
		
		if(this.estado.equals("guardarEncabezado"))
		{
			if( this.dtoEspecialidad.getCodigo()<0)
			{
				errores.add("",	new ActionMessage("errors.notEspecific","La Especialidad es Requeridad "));
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
	 * @return the dtoCuota
	 */
	public CuotasOdontEspecialidad getDtoCuota() {
		return dtoCuota;
	}






	/**
	 * @param dtoCuota the dtoCuota to set
	 */
	public void setDtoCuota(CuotasOdontEspecialidad dtoCuota) {
		this.dtoCuota = dtoCuota;
	}











	public void setPostArrayLisEspecialidad(int postArrayLisEspecialidad) {
		this.postArrayLisEspecialidad = postArrayLisEspecialidad;
	}






	public int getPostArrayLisEspecialidad() {
		return postArrayLisEspecialidad;
	}






	public void setListaDetallesEspecialidad(
			ArrayList<DtoDetalleCuotasOdontoEsp> listaDetallesEspecialidad) {
		this.listaDetallesEspecialidad = listaDetallesEspecialidad;
	}






	public ArrayList<DtoDetalleCuotasOdontoEsp> getListaDetallesEspecialidad() {
		return listaDetallesEspecialidad;
	}






	public void setDtoEspecialida(Especialidades dtoEspecialidad) {
		this.dtoEspecialidad = dtoEspecialidad;
	}






	public Especialidades getDtoEspecialidad() {
		return dtoEspecialidad;
	}






	public void setListaEspecialidades(List<Especialidades> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
	}






	public List<Especialidades> getListaEspecialidades() {
		return listaEspecialidades;
	}






	public void setTipoCouta(String tipoCouta) {
		this.tipoCouta = tipoCouta;
	}






	public String getTipoCouta() {
		return tipoCouta;
	}






	public void setDetTipoCouta(String detTipoCouta) {
		this.detTipoCouta = detTipoCouta;
	}






	public String getDetTipoCouta() {
		return detTipoCouta;
	}






	/**
	 * @return the nombreTipoCuota
	 */
	public String getNombreTipoCuota() {
		return nombreTipoCuota;
	}






	/**
	 * @param nombreTipoCuota the nombreTipoCuota to set
	 */
	public void setNombreTipoCuota(String nombreTipoCuota) {
		this.nombreTipoCuota = nombreTipoCuota;
	}






	/**
	 * @return the nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}






	/**
	 * @param nombreEspecialidad the nombreEspecialidad to set
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}






	public void setExisteEspecialidad(Boolean existeEspecialidad) {
		this.existeEspecialidad = existeEspecialidad;
	}






	public Boolean getExisteEspecialidad() {
		return existeEspecialidad;
	}






	public void setBorraDetalle(String borraDetalle) {
		this.borraDetalle = borraDetalle;
	}






	public String getBorraDetalle() {
		return borraDetalle;
	}






	public void setTamanoListaMayo24(boolean tamanoListaMayo24) {
		this.tamanoListaMayo24 = tamanoListaMayo24;
	}






	public boolean isTamanoListaMayo24() {
		return tamanoListaMayo24;
	}







	public void setDtoLogCuotas(DtoCuotasOdontologicasEspecialidad dtoLogCuotas) {
		this.dtoLogCuotas = dtoLogCuotas;
	}







	public DtoCuotasOdontologicasEspecialidad getDtoLogCuotas() {
		return dtoLogCuotas;
	}







	public void setDtoLogCuotas2(DtoCuotasOdontologicasEspecialidad dtoLogCuotas2) {
		this.dtoLogCuotas2 = dtoLogCuotas2;
	}







	public DtoCuotasOdontologicasEspecialidad getDtoLogCuotas2() {
		return dtoLogCuotas2;
	}







	public void setTamaniolista(int tamaniolista) {
		this.tamaniolista = tamaniolista;
	}






	/**
	 * IMPRIME EL TAMANIO DE LA LISTA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public int getTamaniolista() 
	{
		this.tamaniolista=this.listaDetallesEspecialidad.size();
		return tamaniolista;
	}
	
	
	
	
	
}
