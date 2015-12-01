package com.princetonsa.actionform.administracion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;

public class EnvioEmailAutomaticoForm extends ActionForm{
	
	boolean exito;
	private String estado;
	private String nombre;
	private String email;
	private ArrayList<DtoEnvioEmailAutomatico> listaDtoEnvioMail = new ArrayList<DtoEnvioEmailAutomatico>();
	private ArrayList<Usuario> listaDtoUsuarios = new ArrayList<Usuario>();
	private int indicador1;
	
	//Atributos para la ordenación
	private String patronOrdenar;
	private String esDescendente;
	
	Logger logger =Logger.getLogger( EnvioEmailAutomaticoForm.class);
	
		
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores=new ActionErrors();
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
	
		// VALIDACIONES CAMPOS REQUERIDOS
		if(estado.equals("guardar"))
		{ 
		
			
			int contador = 1;
			for(DtoEnvioEmailAutomatico envio:this.listaDtoEnvioMail)
			{
				// verifica borrado en la funcionalidad
				if(!envio.isEliminado())
				{
					envio.getFuncionalidad();
					// verifica quee el campo funcionalidad no este vacio
					if (envio.getFuncionalidad().equals(""))
					{
						errores.add("funcionalidad", new ActionMessage("errors.required","El Campo funcionalidad  en la fila "+contador));

					}
					
					if (envio.getUsuario().equals(""))
					{
						errores.add("usuario", new ActionMessage("errors.required","El Campo usuario  "+contador));

					}
					contador ++;
				}
			}
			
			
		// VALIDACION INGRESAR MISMA FUNCIONALIDAD MISMO USUARIO	
			for(int i=0;i<this.listaDtoEnvioMail.size();i++)
			{
				for(int j=(this.listaDtoEnvioMail.size()-1);j>i;j--)
				{
					if(
							!this.listaDtoEnvioMail.get(i).getFuncionalidad().equals("")&&
							!this.listaDtoEnvioMail.get(i).getUsuario().equals("")&&
							this.listaDtoEnvioMail.get(i).getFuncionalidad().equals(this.listaDtoEnvioMail.get(j).getFuncionalidad())&&
							this.listaDtoEnvioMail.get(i).getUsuario().equals(this.listaDtoEnvioMail.get(j).getUsuario()))
					{
						if(!listaDtoEnvioMail.get(i).isEliminado())
						{
						
						//accionNombres(this.listaDtoEnvioMail.get(i).getUsuario());
						errores.add("funcionalidad", new ActionMessage("errors.yaExisten","El Campo Funcionalidad  "+ValoresPorDefecto.getIntegridadDominio(this.listaDtoEnvioMail.get(i).getFuncionalidad()) +" y el Usuario "+this.listaDtoEnvioMail.get(i).getNombreCompleto()));
					
						}
						 
						
					}
				}
			}
			
			
			
			
		// VALIDACION USUARIO NO TIENE EMAIL 	

			
			for(int i=0;i<this.listaDtoEnvioMail.size();i++)
			{
			
					
					if(
							!this.listaDtoEnvioMail.get(i).getFuncionalidad().equals("")&&
							!this.listaDtoEnvioMail.get(i).getUsuario().equals("")&& !this.listaDtoEnvioMail.get(i).isEliminado())
					{
					
						    this.accionEmail(this.listaDtoEnvioMail.get(i).getUsuario());
						  	if (this.email.equals("")){
						   		//accionNombres(this.listaDtoEnvioMail.get(i).getUsuario());
						   		errores.add("funcionalidad", new ActionMessage("errors.noExiste2","Email Registrado para el Usuario   "+this.listaDtoEnvioMail.get(i).getNombreCompleto()+" . Por favor verifique la Funcionalidad Usuarios "+this.email));
						 
					
						  	}		
					}
				 
			}
	}
			
			
		
		
		return errores;
}
	
// Metodo que trae el email enviandole el login	
	
	
private void accionEmail(String usuario) {
		
		
	
	for(int i=0;i<this.listaDtoUsuarios.size();i++)
	{
		
		if (this.listaDtoUsuarios.get(i).getLoginUsuario().equals(usuario))
		{
			this.email=this.listaDtoUsuarios.get(i).getEmail();
		}
	}
	
	
	
	}
// METODO QUE TRAE EL NOMBRE DEL USUARIO ENVIANDOLE EL LOGIN AL CONSULTAR
	private void accionNombres(String login) 
	{
		
	
		
		for(int i=0;i<this.listaDtoUsuarios.size();i++)
		{
			
			if (this.listaDtoUsuarios.get(i).getLoginUsuario().equals(login))
			{
				this.nombre=this.listaDtoUsuarios.get(i).getApellidos(false) + " " + this.listaDtoUsuarios.get(i).getNombres(false);
			}
		}
		
		
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
	
	
	public void reset()
	{
		this.estado = "";
		this.nombre="";
		this.email="";
		this.listaDtoEnvioMail = new ArrayList<DtoEnvioEmailAutomatico>();
		this.listaDtoUsuarios = new ArrayList<Usuario>();
		this.exito=false;
		this.indicador1 = ConstantesBD.codigoNuncaValido;
		
		
		this.patronOrdenar = "";
		this.esDescendente = "";
	}

	/**
	 * @return the listaDtoEnvioMail
	 */
	public ArrayList<DtoEnvioEmailAutomatico> getListaDtoEnvioMail() {
		return listaDtoEnvioMail;
	}

	/**
	 * @param listaDtoEnvioMail the listaDtoEnvioMail to set
	 */
	public void setListaDtoEnvioMail(
			ArrayList<DtoEnvioEmailAutomatico> listaDtoEnvioMail) {
		this.listaDtoEnvioMail = listaDtoEnvioMail;
	}

	/**
	 * @return the listaDtoUsuarios
	 */
	public ArrayList<Usuario> getListaDtoUsuarios() {
		return listaDtoUsuarios;
	}

	/**
	 * @param listaDtoUsuarios the listaDtoUsuarios to set
	 */
	public void setListaDtoUsuarios(
			ArrayList<Usuario> listaDtoUsuarios) {
		this.listaDtoUsuarios = listaDtoUsuarios;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the exito
	 */
	public boolean isExito() {
		return exito;
	}

	/**
	 * @param exito the exito to set
	 */
	public void setExito(boolean exito) {
		this.exito = exito;
	}
	
	/**
	 * Método que me retorna el numero de registros existentes del listado de envio email
	 * @return
	 */
	public int getNumListadoEnvio()
	{
		int contador = 0;
		for(DtoEnvioEmailAutomatico envio:this.listaDtoEnvioMail)
		{
			if(!envio.isEliminado())
			{
				contador++;
			}
		}
		return contador;
	}
	/**
	 * @return the indicador1
	 */
	public int getIndicador1() {
		return indicador1;
	}
	/**
	 * @param indicador1 the indicador1 to set
	 */
	public void setIndicador1(int indicador1) {
		this.indicador1 = indicador1;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	
}
