/*
 * @(#)ActivacionMedicoBean.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util;

import java.sql.Connection;
import java.sql.SQLException;

import util.consultaExterna.UtilidadesConsultaExterna;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.sies.mundo.Categoria;

/**
 * Esta clase es un bean que se utiliza para activar y desactivar Médicos
 *
 * @version 1.0, Jan 15, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

public class ActivacionMedicoBean {

	/**
	 * Arreglo de Strings donde se reciben las parejas CodigoTipoId-NumeroId
	 * de todos los médicos que se desean activar/desactivar.
	 */
	private String identificacion[]=null;

	/**
	 * Arreglo de Strings donde después de separar el arreglo identificacion,
	 * quedan almacenados los numeros de identificacion de todos los médicos que
	 * se desean activar/desactivar.
	 */
	private String numeroIdentificacion[];

	/**
	 * Arreglo de Strings donde después de separar el arreglo identificacion,
	 * quedan almacenados los codigos del tipo de identificacion de todos los
	 * médicos que se desean activar/desactivar.
	 */
	private String codigoTipoIdentificacion[];
	
	private String login;

	/**
	 * Inicializa todas las variables (El bean recibe un arreglo de Strings que
	 * contienen la identificación, compuesta por tipo y numero de identificacion),
	 */
	public void inicializarVariables ()
	{
		if (identificacion==null||identificacion.length<1)
			return;
		
		if (this.login==null)
			this.login = "";
		/*codigoTipoIdentificacion=new String[identificacion.length];
		numeroIdentificacion=new String[identificacion.length];
		
		for (i=0;i<identificacion.length;i++)
		{
			
			respuesta=UtilidadTexto.separarNombresDeCodigos(identificacion[i], 1);
			codigoTipoIdentificacion[i]=respuesta[0];
			numeroIdentificacion[i]=respuesta[1];
		}*/
	}

	/**
	 * Método que activa todos los médicos cuya identificación se encuentra
	 * almacenada en este bean.
	 *
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD nombre del tipo de Base de Datos utilizada
	 * @param codigoInstitucion El código de la institución donde permanece el
	 * médico inactivo
	 * @return String[] con información util a mostrar al usuario
	 */
	public String[] activarMedicos (Connection con, String tipoBD, String codigoInstitucion,String usuarioActual) throws SQLException
	{
		String respuesta[];

		if (codigoInstitucion==null||identificacion==null||identificacion.length<1||codigoInstitucion.equals(""))
		{
			respuesta=new String [1];
			respuesta[0]="Por Favor seleccione el Profesional de la Salud";
			return respuesta;
		}

		respuesta=new String[identificacion.length];
		Medico med=new Medico();
		med.init(tipoBD);

		String login=null;
		boolean salioBienUsuario=false;

		RespuestaValidacion respuestaVal;
		int i;
		for (i=0;i<identificacion.length;i++)
		{
			med.clean();
			med.cargarMedico(con,Integer.parseInt(identificacion[i]));
			
			respuestaVal=UtilidadValidacion.validacionActivarMedico (con, tipoBD, med.getCodigoTipoIdentificacion(),med.getNumeroIdentificacion(), codigoInstitucion); 
			if (med.activarMedico(con,codigoInstitucion) < 1)
			{
				respuesta[i]="El Profesional de la Salud con identificaci&oacute;n " + med.getNumeroIdentificacion() + " no pudo ser activado";
			} 
			else
			{
				//Como el médico puede ser también usuario, debe activarse el usuario
		
				Usuario us=new Usuario();
				us.init(tipoBD);
				if (!respuestaVal.textoRespuesta.equals("3-No necesita Activar Usuario"))
				{
					
					//se verifica si se debe inactivar un usuario específico
					if(this.login.equals(""))
						login=us.buscarLogin(con,med.getCodigoTipoIdentificacion(),med.getNumeroIdentificacion(),"false");
					else
						login=this.login;
					
					if (login!=null&&!login.equals(""))
					{
						if (us.activarUsuario(con,login,codigoInstitucion,usuarioActual)>0)
						{
							salioBienUsuario=true;
						}
						else
						{
							salioBienUsuario=false;
						}
					}
					
				}
				else
				{
					//Si la persona no tenía login es porque no existia como usuario, luego no hay
					//que activarlo
					salioBienUsuario=true;
				}
				
				if (salioBienUsuario)
				{
					respuesta[i]="El Profesional de la Salud con identificaci&oacute;n " + med.getNumeroIdentificacion() + " fue activado correctamente";
				}
				else
				{
					respuesta[i]="El Profesional de la Salud con identificaci&oacute;n " + med.getNumeroIdentificacion()+ " no pudo ser activado";
				}
				//Como pueden haber varias desactivaciones, hay que volver a dejar salioBienUsuario en false
				salioBienUsuario=false;

			}
		}
		return respuesta;
	}

	/**
	 * Método que activa todos los médicos cuya identificación se encuentra
	 * almacenada en este bean.
	 *
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD nombre del tipo de Base de Datos utilizada
	 * @param codigoInstitucion El código de la institución donde permanece el
	 * médico inactivo
	 * @return String[] con información util a mostrar al usuario
	 */
	public String[] desactivarMedicos (Connection con, String tipoBD, String codigoInstitucion) throws SQLException
	{
		String respuesta[];
		boolean tieneCategoria = false, tieneAgendaGenerada = false, tieneAgendaGeneradaOdonto = false;
		if (codigoInstitucion==null||identificacion==null||identificacion.length<1||codigoInstitucion.equals(""))
		{
			respuesta=new String [1];
			respuesta[0]="Por Favor seleccione el Profesional de la Salud";
			return respuesta;
		}

		respuesta=new String[identificacion.length];
		Medico med=new Medico();
		med.init(tipoBD);
		int i;
		
		//Nota* El médico se desactiva independiente de su usuario oid[3612] Oct1205
		
		for (i=0;i<identificacion.length;i++)
		{
			med.clean();
			med.cargarMedico(con,Integer.parseInt(identificacion[i]));
			
			UsuarioBasico enfermera=new UsuarioBasico();
			enfermera.cargarUsuarioBasico(con,med.getCodigoPersona());
			
			//se verifica que si es enfermera no puede inactivarse si tiene categoría del cuadro de turnos
			if(UtilidadValidacion.esEnfermera(enfermera).equals(""))
			{
				Categoria categoria = new Categoria();
				ResultadoBoolean resultado=categoria.estaEnfermeraEnCategoria(con,med.getCodigoPersona());
				tieneCategoria = resultado.isTrue();
				respuesta[i] = "ENFER"+ConstantesBD.separadorSplit+"La enfermera con identificaci&oacute;n " + med.getNumeroIdentificacion() + " no pudo ser desactivada porque " +
						"est&aacute; asociada a la categor&iacute;a "+ resultado.getDescripcion();
			}
			
			//Se verifica si el profesional tiene agenda generada
			tieneAgendaGenerada = UtilidadesConsultaExterna.tieneProfesionalAgendaGenerada(con, med.getCodigoPersona());
			tieneAgendaGeneradaOdonto = UtilidadOdontologia.consultarProfesionalTieneAgendaGenerada(con, med.getCodigoPersona());
			if(tieneAgendaGenerada && tieneAgendaGeneradaOdonto)
				respuesta[i] = "AMBAS"+ConstantesBD.separadorSplit+"El profesional con identificaci&oacute;n " + med.getNumeroIdentificacion() + " tiene agenda general y odontológica generada , debe cancelarse la agenda y por consiguiente reprogramar las citas de esa agenda que estan asignadas, reprogramadas o reservadas";
			else if(tieneAgendaGenerada)
				respuesta[i] = "AGGEN"+ConstantesBD.separadorSplit+"El profesional con identificaci&oacute;n " + med.getNumeroIdentificacion() + " tiene agenda generada , debe cancelarse la agenda y por consiguiente reprogramar las citas de esa agenda que estan asignadas, reprogramadas o reservadas";
			else if(tieneAgendaGeneradaOdonto)
				respuesta[i] = "AGODO"+ConstantesBD.separadorSplit+"El profesional con identificaci&oacute;n " + med.getNumeroIdentificacion() + " tiene agenda odontológica generada , debe cancelarse la agenda y por consiguiente reprogramar las citas de esa agenda que estan asignadas o reservadas";
			
			//se verifica si no tiene categoria y no tiene agenda generada
			if(!tieneCategoria && !tieneAgendaGenerada && !tieneAgendaGeneradaOdonto)
			{
				int resp=med.desactivarMedico(con,codigoInstitucion);
				if (resp==0)
					respuesta[i]="OK"+ConstantesBD.separadorSplit+"El Profesional de la Salud con identificaci&oacute;n " + numeroIdentificacion[i] + " no pudo ser desactivado";
				else if (resp==-1)
					respuesta[i]="OK"+ConstantesBD.separadorSplit+"El Profesional de la Salud ya fué inactivado";
				else
					respuesta[i]="OK"+ConstantesBD.separadorSplit+"El Profesional de la Salud con identificaci&oacute;n " + med.getNumeroIdentificacion()+ " fue desactivado correctamente";
				/*if (med.desactivarMedico(con,codigoInstitucion)<1)
					respuesta[i]="El Profesional de la Salud con identificaci&oacute;n " + numeroIdentificacion[i] + " no pudo ser desactivado";
				else
					respuesta[i]="El Profesional de la Salud con identificaci&oacute;n " + med.getNumeroIdentificacion()+ " fue desactivado correctamente";*/
			}
		}

		return respuesta;
	}

	
	/**
	 * Método que limpia los atributos de este objeto
	 */
	public void clean ()
	{
		identificacion=null;
		login=null;
	}

	/**
	 * Establece el arreglo de  identificaciones.
	 * @param identificacion El arreglo de  identificaciones a establecer
	 */
	public void setIdentificacion(String[] identificacion) 
	{
		this.identificacion = identificacion;
	}
	
	/**
	 * Establece el arreglo de  logins.
	 * @param login El arreglo de  logins a establecer
	 */
	public void setLogin(String login) 
	{
		this.login = login;
	}
	
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * Retorna el arreglo  con los codigos de los tipos de identificacion.
	 * @return String[] con los codigos de los tipos de identificacion.
	 */
	public String[] getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}

	/**
	 * Retorna el arreglo  con los números de identificacion..
	 * @return String[] con los números de identificacion..
	 */
	public String[] getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

}