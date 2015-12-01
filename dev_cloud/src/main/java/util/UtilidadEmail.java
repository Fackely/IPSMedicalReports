/*
 * @(#)UtilidadEmail.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Esta clase permite manejar el envio de correos que en ocasiones hay que
 * realizar desde otras clases
 *
 * @version 1.0, Nov 20, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class UtilidadEmail {

    /**
     * El siguiente m�todo nos permite manejar el envio de correo desde 
     * cualquier parte de la aplicaci�n, a trav�s de un servidor SMTP
     * (El cual tambi�n se recibe, lo cual permitira, una vez se haya definido
     * el sitio y almacenamiento de los valores por defecto, usar estos
     * valores, sin cablear servidores por defecto). 
     * @param maquinaSmtp El nombre de la maquina a trav�s de la cual se hace el envio de correo (Puede probar con smtp.007mundo.com)
     * @param correoOrigen La direccion de la persona que envia el mensaje
     * @param correoDestino La direccion a la cual se va a enviar el email
     * @param mensaje El texto / contenido del mensaje que se va a enviar
     * @param asunto Una peque�a linea que describe la raz�n del mensaje
     */           
	public static void enviarCorreo(String correoOrigen, String correoDestino, String mensaje, String asunto)
	{
		try
		{
			String host = "mail.servinte.com.co";
			
			 
			// Get system properties
			Properties props = System.getProperties();
			 
			// Setup mail server
			props.put("mail.smtp.host", host);
			// enable authentification
			props.put("mail.smtp.auth", "true");
			 
			// Get session
			PwdAuthentificator authenticator = new PwdAuthentificator("adminscso", "vaca1234");
			
			
			//Con la informaci�n almacenada en P, creamos una
			//sesi�n de correo, sobre la cual manejaremos 
			//los mensajes y las formas de transmisi�n. 
			Session session=Session.getDefaultInstance(props, authenticator);
			session.setDebug(true);
			
			//A continuaci�n creamos un mensaje de correo,
			//mime, porque aunque actualmente no necesitamos
			//que maneje archivos, tal vez en el futuro sea
			//necesario.
			MimeMessage mensajeMime=new MimeMessage(session);

			//Definimos la direcci�n origen, la de destino
			//el asunto y el mensaje como tal (Y su tipo
			//que aca definimos como html. Usted puede enviar un
			//mensaje de cualquier tipo, si tiene alguna duda
			//revise la configuraci�n de su navegador)

			mensajeMime.setFrom(new InternetAddress(correoOrigen));
			mensajeMime.addRecipient(Message.RecipientType.TO,new InternetAddress(correoDestino));
			mensajeMime.setSubject(asunto);    
			mensajeMime.setContent(mensaje,"text/html");
			//A continuaci�n lo enviamos
			Transport trans = session.getTransport("smtp");
			trans.send(mensajeMime);
			//Transport.send(mensajeMime); 
		}
		//Si el mensaje no se pudo enviar, por errores en
		//el servidor, en la informaci�n de contacto,
		//aqui capturamos las excepciones y las guardamos
		//en el Catalina.out. Si quiere generar error
		//ponga de servidor smtp.uniandes.edu.co :)
		catch(SendFailedException e1)
		{
			System.err.println(e1.getMessage());
			e1.printStackTrace();
		}
		catch(MessagingException e2)
		{
			e2.printStackTrace();
		}
	}

    /**
     * El siguiente m�todo nos permite manejar el envio de varios correos
     * desde cualquier parte de la aplicaci�n, a trav�s de un servidor SMTP
     * (El cual tambi�n se recibe, lo cual permitira, una vez se haya definido
     * el sitio y almacenamiento de los valores por defecto, usar estos
     * valores, sin cablear servidores por defecto). 
     * @param maquinaSmtp El nombre de la maquina a trav�s de la cual se hace el envio de correo (Puede probar con smtp.007mundo.com)
     * @param correoOrigen La direccion de la persona que envia el mensaje
     * @param correosDestino El conjunto de direcciones a las cuales se va a enviar el email
     * @param mensaje El texto / contenido del mensaje que se va a enviar
     * @param asunto Una peque�a linea que describe la raz�n del mensaje
     */           
	public static void enviarSpam (String maquinaSmtp, String correoOrigen, String correosDestino[], String mensaje, String asunto)
	{
		try
		{
			int i=0;
			Properties p=System.getProperties();
			p.put("mail.smtp.maquinaSmtp",maquinaSmtp);
			
			Session session=Session.getDefaultInstance(p,null);
			
			MimeMessage mensajeMime=new MimeMessage(session);
			mensajeMime.setFrom(new InternetAddress(correoOrigen));
			

			for (i=0;i<correosDestino.length;i++)
				mensajeMime.addRecipient(Message.RecipientType.TO,new InternetAddress(correosDestino[i]));mensajeMime.addRecipient(Message.RecipientType.TO,new InternetAddress(correosDestino[i]));

			mensajeMime.setSubject(asunto);    
			mensajeMime.setContent(mensaje,"text/html");
			Transport.send(mensajeMime);
		}
		catch(SendFailedException e1)
		{
			System.err.println(e1.getMessage());
			e1.printStackTrace();
		}
		catch(MessagingException e2)
		{
			e2.printStackTrace();
		}
	}

}